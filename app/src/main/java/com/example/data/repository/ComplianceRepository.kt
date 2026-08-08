package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ComplianceActionEntity
import com.example.data.model.ComplianceDocumentEntity
import com.example.data.model.DocumentStatus
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ComplianceRepository(private val db: AppDatabase) {

    val userProfile: Flow<UserProfileEntity?> = db.userDao().getUserProfile()
    val allDocuments: Flow<List<ComplianceDocumentEntity>> = db.documentDao().getAllDocuments()
    val activeActions: Flow<List<ComplianceActionEntity>> = db.actionDao().getActiveActions()
    val chatHistory: Flow<List<ChatMessageEntity>> = db.chatDao().getChatHistory()

    suspend fun initializeDefaultDataIfEmpty() {
        val existingProfile = db.userDao().getUserProfile().firstOrNull()
        if (existingProfile == null) {
            // Seed user profile
            db.userDao().insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    fullName = "Ahmed Raza",
                    fullNameUrdu = "احمد رضا",
                    customerId = "994-201-445",
                    dob = "14-Aug-1985",
                    dobUrdu = "14-اگست-1985",
                    registeredAddress = "Apt 4B, Shahrah-e-Faisal, Block 6 PECHS, Karachi, 75400, Pakistan",
                    registeredAddressUrdu = "فلیٹ 4 بی، شاہراہ فیصل، بلاک 6 پی ای سی ایچ ایس، کراچی، 75400، پاکستان",
                    profileStatus = "ACTION_REQUIRED",
                    complianceRiskScore = 78,
                    officerName = "Ali Khan",
                    officerNameUrdu = "علی خان",
                    officerRole = "Senior Compliance Officer",
                    officerRoleUrdu = "سینئر کمپلائنس آفیسر",
                    applicantId = "APP-98234",
                    isTwoFactorActive = true,
                    languageCode = "EN"
                )
            )

            // Seed initial documents matching exact screenshots
            db.documentDao().insertAll(
                listOf(
                    ComplianceDocumentEntity(
                        id = 1,
                        title = "National ID (CNIC)",
                        titleUrdu = "قومی شناختی کارڈ",
                        status = DocumentStatus.VERIFIED,
                        statusLabel = "Verified",
                        statusLabelUrdu = "تصدیق شدہ",
                        iconType = "id_card",
                        uploadDate = "Jan 2023",
                        isActionRequired = false,
                        verificationNotes = "NADRA verified with 99.8% match rate."
                    ),
                    ComplianceDocumentEntity(
                        id = 2,
                        title = "Salary Slip (Sep 2023)",
                        titleUrdu = "تنخواہ کی پرچی (ستمبر 2023)",
                        status = DocumentStatus.OUTDATED,
                        statusLabel = "Outdated",
                        statusLabelUrdu = "پرانا",
                        iconType = "receipt_long",
                        uploadDate = "Sep 2023",
                        isActionRequired = true,
                        verificationNotes = "Requires salary certificate within the last 90 days."
                    ),
                    ComplianceDocumentEntity(
                        id = 3,
                        title = "Proof of Address",
                        titleUrdu = "پتہ کا ثبوت",
                        status = DocumentStatus.REVIEWING,
                        statusLabel = "Reviewing",
                        statusLabelUrdu = "زیر جائزہ",
                        iconType = "home_pin",
                        uploadDate = "Aug 2024",
                        isActionRequired = false,
                        verificationNotes = "Utility bill under review by Karachi regional branch."
                    )
                )
            )

            // Seed initial AI recommendations
            db.actionDao().insertAll(
                listOf(
                    ComplianceActionEntity(
                        id = 1,
                        title = "Submit missing tax docs",
                        titleUrdu = "ٹیکس دستاویزات جمع کروائیں",
                        description = "FBR tax return for previous fiscal year is missing in compliance vault.",
                        iconType = "upload_file",
                        isAiGenerated = true,
                        primaryButtonText = "Request Document",
                        primaryButtonTextUrdu = "دستاویز طلب کریں",
                        secondaryButtonText = "Dismiss",
                        secondaryButtonTextUrdu = "مسترد کریں",
                        isPatternInsight = false
                    ),
                    ComplianceActionEntity(
                        id = 2,
                        title = "Verify secondary income",
                        titleUrdu = "ثانوی آمدنی کی تصدیق کریں",
                        description = "Freelance and consulting inward remittances detected on account.",
                        iconType = "fact_check",
                        isAiGenerated = true,
                        primaryButtonText = "Initiate Verification",
                        primaryButtonTextUrdu = "تصدیق شروع کریں",
                        secondaryButtonText = null,
                        secondaryButtonTextUrdu = null,
                        isPatternInsight = false
                    ),
                    ComplianceActionEntity(
                        id = 3,
                        title = "Pattern Detected",
                        titleUrdu = "پیٹرن کا پتہ چلا",
                        description = "Similar risk profiles often resolve compliance issues by updating standard KYC forms. Consider reviewing basic client info.",
                        iconType = "lightbulb",
                        isAiGenerated = true,
                        primaryButtonText = null,
                        secondaryButtonText = null,
                        isPatternInsight = true
                    )
                )
            )

            // Seed initial chat dialogue matching screenshot
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    id = 1,
                    isUser = true,
                    text = "How do I submit my compliance documents?",
                    textUrdu = "میں اپنے تعمیل کے دستاویزات کیسے جمع کرواؤں؟",
                    timestamp = System.currentTimeMillis() - 60000
                )
            )
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    id = 2,
                    isUser = false,
                    text = "I can help you upload your CNIC. Please tap the camera icon.",
                    textUrdu = "میں آپ کا شناختی کارڈ اپ لوڈ کرنے میں مدد کر سکتا ہوں۔ براہ کرم کیمرہ آئیکن کو دبائیں۔",
                    timestamp = System.currentTimeMillis() - 30000,
                    actionButtonLabel = "Scan Document",
                    actionButtonLabelUrdu = "دستاویز اسکین کریں",
                    actionType = "SCAN_DOCUMENT"
                )
            )
        }
    }

    suspend fun uploadOrReplaceDocument(
        title: String,
        titleUrdu: String,
        iconType: String,
        status: DocumentStatus = DocumentStatus.VERIFIED
    ) {
        val newDoc = ComplianceDocumentEntity(
            title = title,
            titleUrdu = titleUrdu,
            status = status,
            statusLabel = if (status == DocumentStatus.VERIFIED) "Verified" else "Reviewing",
            statusLabelUrdu = if (status == DocumentStatus.VERIFIED) "تصدیق شدہ" else "زیر جائزہ",
            iconType = iconType,
            uploadDate = "Today",
            isActionRequired = false,
            verificationNotes = "OCR verified and cryptographic hash stored."
        )
        db.documentDao().insertDocument(newDoc)
        recalculateRiskScore()
    }

    suspend fun updateDocumentStatus(docId: Int, newStatus: DocumentStatus) {
        val existing = db.documentDao().getDocumentById(docId) ?: return
        val updated = existing.copy(
            status = newStatus,
            statusLabel = when (newStatus) {
                DocumentStatus.VERIFIED -> "Verified"
                DocumentStatus.OUTDATED -> "Outdated"
                DocumentStatus.REVIEWING -> "Reviewing"
                DocumentStatus.PENDING -> "Pending"
                DocumentStatus.REJECTED -> "Rejected"
            },
            statusLabelUrdu = when (newStatus) {
                DocumentStatus.VERIFIED -> "تصدیق شدہ"
                DocumentStatus.OUTDATED -> "پرانا"
                DocumentStatus.REVIEWING -> "زیر جائزہ"
                DocumentStatus.PENDING -> "زیر التواء"
                DocumentStatus.REJECTED -> "مسترد"
            },
            isActionRequired = newStatus == DocumentStatus.OUTDATED || newStatus == DocumentStatus.REJECTED
        )
        db.documentDao().updateDocument(updated)
        recalculateRiskScore()
    }

    suspend fun dismissAction(actionId: Int) {
        db.actionDao().dismissAction(actionId)
        recalculateRiskScore()
    }

    suspend fun sendChatMessage(userQuery: String, aiResponseText: String, aiResponseUrdu: String?, actionButton: String? = null, actionType: String? = null) {
        val userMsg = ChatMessageEntity(
            isUser = true,
            text = userQuery,
            textUrdu = null,
            timestamp = System.currentTimeMillis()
        )
        db.chatDao().insertMessage(userMsg)

        val aiMsg = ChatMessageEntity(
            isUser = false,
            text = aiResponseText,
            textUrdu = aiResponseUrdu,
            timestamp = System.currentTimeMillis() + 500,
            actionButtonLabel = actionButton,
            actionButtonLabelUrdu = if (actionButton != null) "عمل کریں" else null,
            actionType = actionType
        )
        db.chatDao().insertMessage(aiMsg)
    }

    suspend fun updateTwoFactor(active: Boolean) {
        db.userDao().updateTwoFactor(active)
    }

    suspend fun updateLanguage(langCode: String) {
        db.userDao().updateLanguage(langCode)
    }

    suspend fun updateRiskScore(score: Int) {
        db.userDao().updateRiskScore(score)
    }

    private suspend fun recalculateRiskScore() {
        val docs = db.documentDao().getAllDocuments().firstOrNull() ?: emptyList()
        val actions = db.actionDao().getActiveActions().firstOrNull() ?: emptyList()

        var calculatedRisk = 12 // Base baseline
        docs.forEach { doc ->
            when (doc.status) {
                DocumentStatus.OUTDATED -> calculatedRisk += 25
                DocumentStatus.REJECTED -> calculatedRisk += 30
                DocumentStatus.REVIEWING -> calculatedRisk += 10
                DocumentStatus.PENDING -> calculatedRisk += 15
                DocumentStatus.VERIFIED -> calculatedRisk -= 5
            }
        }
        calculatedRisk += actions.size * 8
        val finalScore = calculatedRisk.coerceIn(8, 98)
        db.userDao().updateRiskScore(finalScore)
    }
}
