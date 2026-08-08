package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    FIELD_AGENT,
    COMPLIANCE_OFFICER
}

enum class RiskTier {
    LOW,
    MEDIUM,
    HIGH
}

data class OnboardingApplicationData(
    val applicantId: String = "APP-98234",
    val fullName: String = "Kamran Khan",
    val fullNameUrdu: String = "کامران خان",
    val cnic: String = "37405-1234567-1",
    val dob: String = "14-Aug-1985",
    val city: String = "Rawalpindi",
    val isAddressMatched: Boolean = true,
    val declaredOccupation: String = "Shopkeeper / Retailer",
    val declaredMonthlyIncomePkr: Long = 75000L,
    val sourceOfFunds: String = "Business Cash Sales",
    val accountPurpose: String = "Online Customer Payments",
    val expectedMonthlyTurnoverPkr: Long = 850000L,
    val expectedMaxSingleTxnPkr: Long = 300000L,
    val agentLocation: String = "Liaquat Bazaar, Rawalpindi",
    val branchCode: String = "0412",
    val isNadraVerified: Boolean = true,
    val riskScore: Int = 78,
    val riskTier: RiskTier = RiskTier.HIGH,
    val isEddRequired: Boolean = true
)

data class EddCaseEntity(
    val id: String = "APP-98234",
    val fullName: String = "Kamran Khan",
    val occupation: String = "Shopkeeper / Retailer",
    val city: String = "Rawalpindi",
    val locationDetail: String = "Liaquat Bazaar, Rawalpindi",
    val cnic: String = "37405-1234567-1",
    val declaredIncomePkr: Long = 75000L,
    val expectedTurnoverPkr: Long = 850000L,
    val maxSingleTxnPkr: Long = 300000L,
    val turnoverMultiplier: Double = 11.33,
    val exposurePercentage: Int = 400,
    val riskScore: Int = 78,
    val riskTier: RiskTier = RiskTier.HIGH,
    val status: String = "EDD_PENDING", // "EDD_PENDING", "STANDARD_REVIEW", "APPROVED", "REJECTED", "ESCALATED"
    val auditNotes: String = "",
    val reasonFactors: List<String> = listOf(
        "Economic Inconsistency: Turnover (PKR 850,000) is 11.3x declared income (PKR 75,000). Threshold is 3.0x.",
        "Velocity Exposure: Max single transaction (PKR 300,000) is 400% of monthly income.",
        "Verification Status: CNIC active via NADRA Verisys, SIM ownership confirmed, zero PEP/Watchlist matches."
    )
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val fullName: String = "Kamran Khan",
    val fullNameUrdu: String = "کامران خان",
    val customerId: String = "APP-98234",
    val dob: String = "14-Aug-1985",
    val dobUrdu: String = "14-اگست-1985",
    val registeredAddress: String = "Shop 14, Liaquat Bazaar, Rawalpindi, Punjab, Pakistan",
    val registeredAddressUrdu: String = "دکان 14، لیاقت بازار، راولپنڈی، پنجاب، پاکستان",
    val profileStatus: String = "EDD_REQUIRED",
    val complianceRiskScore: Int = 78,
    val officerName: String = "Tariq Mehmood",
    val officerNameUrdu: String = "طارق محمود",
    val officerRole: String = "Senior EDD Compliance Officer",
    val officerRoleUrdu: String = "سینئر ای ڈی ڈی کمپلائنس آفیسر",
    val applicantId: String = "APP-98234",
    val isTwoFactorActive: Boolean = true,
    val languageCode: String = "EN" // "EN" or "UR"
)

enum class DocumentStatus {
    VERIFIED,
    OUTDATED,
    REVIEWING,
    PENDING,
    REJECTED
}

@Entity(tableName = "compliance_documents")
data class ComplianceDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val titleUrdu: String,
    val status: DocumentStatus,
    val statusLabel: String,
    val statusLabelUrdu: String,
    val iconType: String, // "id_card", "receipt_long", "home_pin", "description", "account_balance"
    val uploadDate: String = "Sep 2023",
    val fileUri: String? = null,
    val isActionRequired: Boolean = false,
    val verificationNotes: String? = null
)

@Entity(tableName = "compliance_actions")
data class ComplianceActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val titleUrdu: String,
    val description: String? = null,
    val iconType: String, // "upload_file", "fact_check", "lightbulb"
    val isAiGenerated: Boolean = true,
    val primaryButtonText: String? = null,
    val primaryButtonTextUrdu: String? = null,
    val secondaryButtonText: String? = null,
    val secondaryButtonTextUrdu: String? = null,
    val isPatternInsight: Boolean = false,
    val isDismissed: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val isUser: Boolean,
    val text: String,
    val textUrdu: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val actionButtonLabel: String? = null,
    val actionButtonLabelUrdu: String? = null,
    val actionType: String? = null // "SCAN_DOCUMENT", "UPLOAD_SALARY", "VIEW_RISK"
)

data class DocumentScanResult(
    val docType: String,
    val extractedName: String,
    val extractedCnicOrId: String,
    val issueDate: String,
    val confidenceScore: Float,
    val isMatch: Boolean
)
