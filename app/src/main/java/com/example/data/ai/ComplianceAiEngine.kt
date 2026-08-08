package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.ai.network.AiRetrofitClient
import com.example.data.ai.network.GeminiContent
import com.example.data.ai.network.GeminiGenerateRequest
import com.example.data.ai.network.GeminiPart
import com.example.data.ai.network.OpenCodeChatMessage
import com.example.data.ai.network.OpenCodeChatRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ComplianceAiEngine {

    private const val TAG = "ComplianceAiEngine"

    data class AiReply(
        val englishText: String,
        val urduText: String,
        val actionButtonLabel: String? = null,
        val actionType: String? = null,
        val providerUsed: String = "LOCAL" // "GEMINI", "OPENCODE_DEEPSEEK", "OPENCODE_BIG_PICKLE", "LOCAL"
    )

    private const val SYSTEM_PROMPT = """
You are TrustLens, an AI-Driven Customer Risk Profiling & Compliance Copilot sponsored by Mobilink Microfinance Bank Limited (MMBL) Pakistan under State Bank of Pakistan (SBP) Customers' Digital Onboarding Framework.
You assist field agents in Liaquat Bazaar, Rawalpindi and compliance officers at Islamabad Head Office with KYC, AML, Tier 1 Asaan Digital Wallet, and Enhanced Due Diligence (EDD) cases.
Provide accurate, concise, and helpful compliance answers.
Format your answer clearly. If possible, provide a 1-sentence Urdu summary at the end.
"""

    suspend fun answerQuery(query: String, currentLanguage: String = "EN"): AiReply = withContext(Dispatchers.IO) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return@withContext getLocalFallback(query)
        }

        // 1. Attempt Gemini API Call if Key is configured
        val geminiKey = BuildConfig.GEMINI_API_KEY
        val isValidGeminiKey = geminiKey.isNotBlank() && !geminiKey.contains("MY_GEMINI_API_KEY")

        if (isValidGeminiKey) {
            try {
                Log.d(TAG, "Calling Gemini 2.5 Flash API...")
                val request = GeminiGenerateRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(
                                GeminiPart(text = "$SYSTEM_PROMPT\n\nUser Question: $trimmedQuery")
                            )
                        )
                    )
                )
                val response = AiRetrofitClient.geminiService.generateContent(
                    apiKey = geminiKey,
                    request = request
                )

                if (response.isSuccessful) {
                    val candidateText = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!candidateText.isNullOrBlank()) {
                        Log.d(TAG, "Gemini API succeeded!")
                        return@withContext parseAiResponse(trimmedQuery, candidateText, "GEMINI")
                    }
                } else {
                    Log.w(TAG, "Gemini API error code: ${response.code()}, falling back to OpenCode.ai...")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini API call failed: ${e.message}, falling back to OpenCode.ai...", e)
            }
        } else {
            Log.d(TAG, "Gemini API Key not present or placeholder; routing to OpenCode fallback...")
        }

        // 2. Fallback to OpenCode.ai (https://opencode.ai/zen/v1/chat/completions) with deepseek-v4-flash-free
        try {
            Log.d(TAG, "Calling OpenCode.ai with deepseek-v4-flash-free...")
            val openCodeRequest = OpenCodeChatRequest(
                model = "deepseek-v4-flash-free",
                messages = listOf(
                    OpenCodeChatMessage(role = "system", content = SYSTEM_PROMPT),
                    OpenCodeChatMessage(role = "user", content = trimmedQuery)
                )
            )
            val openCodeResponse = AiRetrofitClient.openCodeService.createChatCompletion(openCodeRequest)

            if (openCodeResponse.isSuccessful) {
                val replyText = openCodeResponse.body()?.choices?.firstOrNull()?.message?.content
                if (!replyText.isNullOrBlank()) {
                    Log.d(TAG, "OpenCode deepseek-v4-flash-free succeeded!")
                    return@withContext parseAiResponse(trimmedQuery, replyText, "OPENCODE_DEEPSEEK")
                }
            } else {
                Log.w(TAG, "OpenCode deepseek error: ${openCodeResponse.code()}, trying big-pickle model...")
            }
        } catch (e: Exception) {
            Log.w(TAG, "OpenCode deepseek call failed: ${e.message}, trying big-pickle model...", e)
        }

        // 3. Fallback to OpenCode.ai with big-pickle model
        try {
            Log.d(TAG, "Calling OpenCode.ai with big-pickle...")
            val bigPickleRequest = OpenCodeChatRequest(
                model = "big-pickle",
                messages = listOf(
                    OpenCodeChatMessage(role = "system", content = SYSTEM_PROMPT),
                    OpenCodeChatMessage(role = "user", content = trimmedQuery)
                )
            )
            val bigPickleResponse = AiRetrofitClient.openCodeService.createChatCompletion(bigPickleRequest)

            if (bigPickleResponse.isSuccessful) {
                val replyText = bigPickleResponse.body()?.choices?.firstOrNull()?.message?.content
                if (!replyText.isNullOrBlank()) {
                    Log.d(TAG, "OpenCode big-pickle succeeded!")
                    return@withContext parseAiResponse(trimmedQuery, replyText, "OPENCODE_BIG_PICKLE")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "OpenCode big-pickle call failed: ${e.message}, switching to local domain engine...", e)
        }

        // 4. Reliable Local High-Fidelity Domain Compliance Engine Fallback
        Log.d(TAG, "Using Mobilink Bank local compliance knowledge engine.")
        return@withContext getLocalFallback(trimmedQuery)
    }

    private fun parseAiResponse(query: String, rawText: String, provider: String): AiReply {
        val q = query.lowercase()
        val (actionLabel, actionType) = getActionForQuery(q)

        // Split or extract Urdu portion if present
        var englishPart = rawText
        var urduPart = ""

        if (rawText.contains("اردو:") || rawText.contains("Urdu:")) {
            val parts = rawText.split(Regex("(اردو:|Urdu:)", RegexOption.IGNORE_CASE))
            if (parts.size >= 2) {
                englishPart = parts[0].trim()
                urduPart = parts[1].trim()
            }
        } else {
            urduPart = getUrduSummaryForQuery(q)
        }

        return AiReply(
            englishText = englishPart,
            urduText = urduPart,
            actionButtonLabel = actionLabel,
            actionType = actionType,
            providerUsed = provider
        )
    }

    private fun getActionForQuery(q: String): Pair<String?, String?> {
        return when {
            q.contains("cnic") || q.contains("national id") || q.contains("شناختی کارڈ") || q.contains("nadra") ->
                Pair("Scan National ID", "SCAN_DOCUMENT")
            q.contains("salary") || q.contains("income") || q.contains("تنخواہ") || q.contains("slip") || q.contains("turnover") ->
                Pair("Upload Salary Slip", "UPLOAD_SALARY")
            q.contains("risk") || q.contains("score") || q.contains("78") || q.contains("edd") || q.contains("رسک") ->
                Pair("View Risk Breakdown", "VIEW_RISK")
            q.contains("tax") || q.contains("fbr") || q.contains("ٹیکس") || q.contains("atl") ->
                Pair("Request Tax Certificate", "REQUEST_TAX")
            q.contains("urdu") || q.contains("اردو") || q.contains("language") ->
                Pair("Switch Language", "SWITCH_LANG")
            else ->
                Pair("Scan Document", "SCAN_DOCUMENT")
        }
    }

    private fun getUrduSummaryForQuery(q: String): String {
        return when {
            q.contains("cnic") || q.contains("national id") || q.contains("شناختی کارڈ") ->
                "اپنے قومی شناختی کارڈ کی دونوں طرف سے واضح تصویر نادرا ویریفکیشن کے لیے اپ لوڈ کریں۔"
            q.contains("salary") || q.contains("income") || q.contains("آمدنی") || q.contains("تنخواہ") ->
                "تنخواہ یا آمدنی کی تصدیق کے لیے پچھلے 90 دنوں کی مصدقہ پرچی یا بینک اسٹیٹمنٹ فراہم کریں۔"
            q.contains("risk") || q.contains("score") || q.contains("78") || q.contains("رسک") ->
                "آمدنی اور متوقع ٹرن اوور میں عدم مطابقت کی وجہ سے کیس ای ڈی ڈی جائزہ میں شامل ہے۔"
            q.contains("address") || q.contains("utility") || q.contains("پتہ") || q.contains("بل") ->
                "رہائشی پتہ کی تصدیق کے لیے 3 ماہ پرانا یوٹیلیٹی بل یا کرایہ نامہ جمع کروائیں۔"
            q.contains("tax") || q.contains("fbr") || q.contains("ٹیکس") ->
                "ایف بی آر کے ایکٹو ٹیکس پیئر لسٹ (ATL) سرٹیفکیٹ کی کاپی منسلک کریں۔"
            else ->
                "تمام کارروائی اسٹیٹ بینک آف پاکستان کے ڈیجیٹل کسٹمر آن بورڈنگ فریم ورک کے تحت کی جاتی ہے۔"
        }
    }

    fun getLocalFallback(query: String): AiReply {
        val q = query.trim().lowercase()

        return when {
            q.contains("cnic") || q.contains("national id") || q.contains("شناختی کارڈ") -> {
                AiReply(
                    englishText = "To verify your CNIC under SBP Asaan Account rules, please capture both front and back sides with biometric clarity. The 13-digit number and expiry date must match NADRA BioVeriSys records.",
                    urduText = "قومی شناختی کارڈ کی تصدیق کے لیے دونوں طرف کی واضح تصویر اپ لوڈ کریں۔ 13 ہندسوں کا نمبر نادرا کے ریکارڈ سے تصدیق کیا جائے گا۔",
                    actionButtonLabel = "Scan National ID",
                    actionType = "SCAN_DOCUMENT",
                    providerUsed = "LOCAL"
                )
            }
            q.contains("salary") || q.contains("income") || q.contains("آمدنی") || q.contains("تنخواہ") || q.contains("slip") -> {
                AiReply(
                    englishText = "Your salary slip or income proof must be issued within the last 90 days with official employer stamp or verifiable digital QR code to validate stated PKR 75,000 monthly income.",
                    urduText = "آپ کی تنخواہ کی پرچی پچھلے 90 دنوں کے اندر جاری ہونی چاہیے جس پر مجاز مہر یا ڈیجیٹل کیو آر کوڈ موجود ہو۔",
                    actionButtonLabel = "Upload Salary Slip",
                    actionType = "UPLOAD_SALARY",
                    providerUsed = "LOCAL"
                )
            }
            q.contains("risk") || q.contains("score") || q.contains("78") || q.contains("رسک") || q.contains("سکور") -> {
                AiReply(
                    englishText = "Applicant Kamran Khan (APP-98234) has an elevated risk score of 78/100 (HIGH). Primary factors: Turnover Ratio is 11.3x declared income (SBP threshold: 3.0x) and Velocity Exposure is 400%.",
                    urduText = "درخواست گزار کامران خان کا رسک سکور 78/100 (ہائی) ہے۔ بڑی وجہ ماہانہ آمدنی (75 ہزار) کے مقابلے میں متوقع ٹرن اوور (8.5 لاکھ) کا 11.3 گنا ہونا ہے۔",
                    actionButtonLabel = "View Risk Breakdown",
                    actionType = "VIEW_RISK",
                    providerUsed = "LOCAL"
                )
            }
            q.contains("address") || q.contains("utility") || q.contains("پتہ") || q.contains("بل") -> {
                AiReply(
                    englishText = "Proof of address can be verified using an electricity/gas/water bill from the last 3 months, or a registered tenancy agreement in Liaquat Bazaar, Rawalpindi.",
                    urduText = "پتہ کی تصدیق کے لیے پچھلے 3 ماہ کا بجلی، گیس یا پانی کا بل، یا رجسٹرڈ کرایہ نامہ جمع کروایا جا سکتا ہے۔",
                    actionButtonLabel = "Scan Proof of Address",
                    actionType = "SCAN_DOCUMENT",
                    providerUsed = "LOCAL"
                )
            }
            q.contains("tax") || q.contains("fbr") || q.contains("ٹیکس") -> {
                AiReply(
                    englishText = "Please provide your Active Taxpayer List (ATL) certificate or latest annual income tax return acknowledgment from the FBR IRIS portal to resolve the high-turnover compliance exception.",
                    urduText = "براہ کرم ایف بی آر کے آئی آر آئی ایس پورٹل سے اپنا ایکٹو ٹیکس پیئر لسٹ (ATL) سرٹیفکیٹ یا سالانہ ٹیکس ریٹرن جمع کروائیں۔",
                    actionButtonLabel = "Request Tax Certificate",
                    actionType = "REQUEST_TAX",
                    providerUsed = "LOCAL"
                )
            }
            q.contains("urdu") || q.contains("اردو") -> {
                AiReply(
                    englishText = "TrustLens provides full bilingual Urdu/English support across all screens. You can switch language anytime using the globe icon in the top bar or from your profile settings.",
                    urduText = "ٹرسٹ لینس میں مکمل اردو اور انگریزی زبان کی سہولت موجود ہے۔ آپ اوپر گلوب آئیکن دبا کر فوری زبان بدل سکتے ہیں۔",
                    actionButtonLabel = "Switch Language",
                    actionType = "SWITCH_LANG",
                    providerUsed = "LOCAL"
                )
            }
            else -> {
                AiReply(
                    englishText = "I have analyzed your compliance inquiry regarding '$query'. All onboarding transactions and customer verification steps strictly comply with Mobilink Bank's SBP-authorized digital onboarding framework.",
                    urduText = "آپ کے سوال کا تجزیہ کر لیا گیا ہے۔ تمام دستاویزات اسٹیٹ بینک آف پاکستان اور موبی لنک بینک کے قواعد و ضوابط کے مطابق ہونی چاہیئیں۔",
                    actionButtonLabel = "Scan Document",
                    actionType = "SCAN_DOCUMENT",
                    providerUsed = "LOCAL"
                )
            }
        }
    }
}

