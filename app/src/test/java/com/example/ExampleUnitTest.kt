package com.example

import com.example.data.ai.ComplianceAiEngine
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testComplianceAiLocalFallback_cnicQuery() {
        val reply = ComplianceAiEngine.getLocalFallback("How do I verify my CNIC?")
        assertEquals("SCAN_DOCUMENT", reply.actionType)
        assertEquals("Scan National ID", reply.actionButtonLabel)
        assertTrue(reply.englishText.contains("CNIC") || reply.englishText.contains("NADRA"))
        assertTrue(reply.urduText.contains("شناختی کارڈ"))
    }

    @Test
    fun testComplianceAiLocalFallback_salaryQuery() {
        val reply = ComplianceAiEngine.getLocalFallback("Upload salary slip for income verification")
        assertEquals("UPLOAD_SALARY", reply.actionType)
        assertEquals("Upload Salary Slip", reply.actionButtonLabel)
        assertTrue(reply.englishText.contains("salary slip") || reply.englishText.contains("income"))
    }

    @Test
    fun testComplianceAiLocalFallback_riskScoreQuery() {
        val reply = ComplianceAiEngine.getLocalFallback("Why is my risk score 78?")
        assertEquals("VIEW_RISK", reply.actionType)
        assertEquals("View Risk Breakdown", reply.actionButtonLabel)
        assertTrue(reply.englishText.contains("78/100") || reply.englishText.contains("risk"))
    }

    @Test
    fun testComplianceAiLocalFallback_taxQuery() {
        val reply = ComplianceAiEngine.getLocalFallback("FBR tax certificate requirements")
        assertEquals("REQUEST_TAX", reply.actionType)
        assertEquals("Request Tax Certificate", reply.actionButtonLabel)
    }

    @Test
    fun testComplianceAiLocalFallback_urduLanguageQuery() {
        val reply = ComplianceAiEngine.getLocalFallback("اردو میں تبدیل کریں")
        assertEquals("SWITCH_LANG", reply.actionType)
        assertEquals("Switch Language", reply.actionButtonLabel)
    }

    @Test
    fun testSbpTurnoverRatioCalculation() {
        val declaredIncome = 75000.0
        val expectedTurnover = 850000.0
        val maxTxn = 300000.0

        val turnoverRatio = expectedTurnover / declaredIncome
        val exposureRatio = (maxTxn / declaredIncome) * 100.0

        assertEquals(11.33, turnoverRatio, 0.01)
        assertEquals(400.0, exposureRatio, 0.01)
        assertTrue("Turnover ratio should exceed SBP threshold of 3.0x", turnoverRatio > 3.0)
    }
}

