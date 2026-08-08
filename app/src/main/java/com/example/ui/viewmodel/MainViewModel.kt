package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ComplianceAiEngine
import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ComplianceActionEntity
import com.example.data.model.ComplianceDocumentEntity
import com.example.data.model.DocumentScanResult
import com.example.data.model.DocumentStatus
import com.example.data.model.OnboardingApplicationData
import com.example.data.model.UserProfileEntity
import com.example.data.model.UserRole
import com.example.data.repository.ComplianceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    ONBOARDING,
    EDD_DASHBOARD,
    DATA,
    AI,
    CHAT,
    PROFILE
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ComplianceRepository
    val userProfile: StateFlow<UserProfileEntity?>
    val documents: StateFlow<List<ComplianceDocumentEntity>>
    val actions: StateFlow<List<ComplianceActionEntity>>
    val chatHistory: StateFlow<List<ChatMessageEntity>>

    // Authentication & Role State
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.FIELD_AGENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _loggedInUserLabel = MutableStateFlow("Kamran Khan (Agent 0412)")
    val loggedInUserLabel: StateFlow<String> = _loggedInUserLabel.asStateFlow()

    private val _onboardingData = MutableStateFlow(OnboardingApplicationData())
    val onboardingData: StateFlow<OnboardingApplicationData> = _onboardingData.asStateFlow()

    private val _currentTab = MutableStateFlow(AppTab.ONBOARDING)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _isSplashScreenActive = MutableStateFlow(true)
    val isSplashScreenActive: StateFlow<Boolean> = _isSplashScreenActive.asStateFlow()

    private val _currentLanguage = MutableStateFlow("EN") // "EN" or "UR"
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _isScannerDialogOpen = MutableStateFlow(false)
    val isScannerDialogOpen: StateFlow<Boolean> = _isScannerDialogOpen.asStateFlow()

    private val _isScanningInProgress = MutableStateFlow(false)
    val isScanningInProgress: StateFlow<Boolean> = _isScanningInProgress.asStateFlow()

    private val _scanResult = MutableStateFlow<DocumentScanResult?>(null)
    val scanResult: StateFlow<DocumentScanResult?> = _scanResult.asStateFlow()

    private val _selectedScannerDocType = MutableStateFlow("Salary Slip")
    val selectedScannerDocType: StateFlow<String> = _selectedScannerDocType.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _isVoiceRecording = MutableStateFlow(false)
    val isVoiceRecording: StateFlow<Boolean> = _isVoiceRecording.asStateFlow()

    private val _userChatInput = MutableStateFlow("")
    val userChatInput: StateFlow<String> = _userChatInput.asStateFlow()

    private val _activeSnackbarMessage = MutableStateFlow<String?>(null)
    val activeSnackbarMessage: StateFlow<String?> = _activeSnackbarMessage.asStateFlow()

    // Profile Dialogs
    private val _isChangePasswordDialogOpen = MutableStateFlow(false)
    val isChangePasswordDialogOpen: StateFlow<Boolean> = _isChangePasswordDialogOpen.asStateFlow()

    private val _isHelpCenterDialogOpen = MutableStateFlow(false)
    val isHelpCenterDialogOpen: StateFlow<Boolean> = _isHelpCenterDialogOpen.asStateFlow()

    private val _isGuidelinesDialogOpen = MutableStateFlow(false)
    val isGuidelinesDialogOpen: StateFlow<Boolean> = _isGuidelinesDialogOpen.asStateFlow()

    private val _isSignOutConfirmDialogOpen = MutableStateFlow(false)
    val isSignOutConfirmDialogOpen: StateFlow<Boolean> = _isSignOutConfirmDialogOpen.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ComplianceRepository(db)

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        documents = repository.allDocuments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        actions = repository.activeActions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        chatHistory = repository.chatHistory.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.initializeDefaultDataIfEmpty()
            // Auto splash transition after 2.4 seconds
            delay(2400)
            _isSplashScreenActive.value = false
        }
    }

    fun dismissSplashScreen() {
        _isSplashScreenActive.value = false
    }

    fun showSplashScreen() {
        _isSplashScreenActive.value = true
        viewModelScope.launch {
            delay(2200)
            _isSplashScreenActive.value = false
        }
    }

    fun selectRole(role: UserRole) {
        _currentRole.value = role
        if (role == UserRole.FIELD_AGENT) {
            _currentTab.value = AppTab.ONBOARDING
        } else {
            _currentTab.value = AppTab.EDD_DASHBOARD
        }
    }

    fun loginUser(role: UserRole, userLabel: String) {
        _currentRole.value = role
        _loggedInUserLabel.value = userLabel
        _isUserLoggedIn.value = true
        if (role == UserRole.FIELD_AGENT) {
            _currentTab.value = AppTab.ONBOARDING
            _activeSnackbarMessage.value = "Signed in as Field Agent ($userLabel) at Liaquat Bazaar"
        } else {
            _currentTab.value = AppTab.EDD_DASHBOARD
            _activeSnackbarMessage.value = "Signed in as Compliance Officer ($userLabel) - Islamabad HQ"
        }
    }

    fun logoutUser() {
        _isUserLoggedIn.value = false
        _activeSnackbarMessage.value = "Session signed out securely"
    }

    fun updateOnboardingData(data: OnboardingApplicationData) {
        _onboardingData.value = data
        _currentRole.value = UserRole.COMPLIANCE_OFFICER
        _currentTab.value = AppTab.EDD_DASHBOARD
        _activeSnackbarMessage.value = "Case ${data.applicantId} (${data.fullName}) routed to EDD Queue"
    }

    fun switchTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun toggleLanguage() {
        val newLang = if (_currentLanguage.value == "EN") "UR" else "EN"
        setLanguage(newLang)
    }

    fun setLanguage(lang: String) {
        _currentLanguage.value = lang
        viewModelScope.launch {
            repository.updateLanguage(lang)
            val msg = if (lang == "UR") "زبان اردو پر تبدیل کر دی گئی ہے" else "Language switched to English"
            _activeSnackbarMessage.value = msg
        }
    }

    fun openScanner(docType: String = "Salary Slip") {
        _selectedScannerDocType.value = docType
        _scanResult.value = null
        _isScanningInProgress.value = false
        _isScannerDialogOpen.value = true
    }

    fun closeScanner() {
        _isScannerDialogOpen.value = false
        _isScanningInProgress.value = false
        _scanResult.value = null
    }

    fun setScannerDocType(docType: String) {
        _selectedScannerDocType.value = docType
    }

    fun startOcrScanSimulation() {
        viewModelScope.launch {
            _isScanningInProgress.value = true
            delay(1800)
            val docType = _selectedScannerDocType.value
            val result = when (docType) {
                "National ID (CNIC)" -> DocumentScanResult(
                    docType = "National ID (CNIC)",
                    extractedName = "Ahmed Raza",
                    extractedCnicOrId = "42101-9942014-4",
                    issueDate = "12-Jan-2023",
                    confidenceScore = 0.99f,
                    isMatch = true
                )
                "Salary Slip", "Salary Slip (Sep 2023)" -> DocumentScanResult(
                    docType = "Salary Certificate / Slip",
                    extractedName = "Ahmed Raza (Mobilink/Jazz Telecom)",
                    extractedCnicOrId = "PKR 340,000 / Month",
                    issueDate = "01-Aug-2024 (Valid)",
                    confidenceScore = 0.98f,
                    isMatch = true
                )
                "Proof of Address" -> DocumentScanResult(
                    docType = "K-Electric Utility Bill",
                    extractedName = "Ahmed Raza",
                    extractedCnicOrId = "Shahrah-e-Faisal PECHS Block 6",
                    issueDate = "July 2024",
                    confidenceScore = 0.96f,
                    isMatch = true
                )
                else -> DocumentScanResult(
                    docType = docType,
                    extractedName = "Ahmed Raza",
                    extractedCnicOrId = "TAX-CERT-2024-994",
                    issueDate = "FY 2023-2024",
                    confidenceScore = 0.97f,
                    isMatch = true
                )
            }
            _scanResult.value = result
            _isScanningInProgress.value = false
        }
    }

    fun commitScannedDocument() {
        val result = _scanResult.value ?: return
        viewModelScope.launch {
            when (result.docType) {
                "Salary Certificate / Slip", "Salary Slip", "Salary Slip (Sep 2023)" -> {
                    // Update salary slip document to Verified
                    val docs = documents.value
                    val salaryDoc = docs.find { it.id == 2 }
                    if (salaryDoc != null) {
                        repository.updateDocumentStatus(salaryDoc.id, DocumentStatus.VERIFIED)
                    } else {
                        repository.uploadOrReplaceDocument("Salary Slip (Aug 2024)", "تنخواہ کی پرچی (اگست 2024)", "receipt_long", DocumentStatus.VERIFIED)
                    }
                    _activeSnackbarMessage.value = "Salary Slip updated & verified successfully! Risk score reduced."
                }
                "Proof of Address", "K-Electric Utility Bill" -> {
                    val docs = documents.value
                    val addressDoc = docs.find { it.id == 3 }
                    if (addressDoc != null) {
                        repository.updateDocumentStatus(addressDoc.id, DocumentStatus.VERIFIED)
                    } else {
                        repository.uploadOrReplaceDocument("Proof of Address (Aug 2024)", "پتہ کا ثبوت (اگست 2024)", "home_pin", DocumentStatus.VERIFIED)
                    }
                    _activeSnackbarMessage.value = "Proof of Address verified! Regional compliance approved."
                }
                else -> {
                    repository.uploadOrReplaceDocument(result.docType, "تصدیق شدہ دستاویز", "description", DocumentStatus.VERIFIED)
                    _activeSnackbarMessage.value = "${result.docType} uploaded and verified!"
                }
            }
            closeScanner()
        }
    }

    fun dismissAction(actionId: Int) {
        viewModelScope.launch {
            repository.dismissAction(actionId)
            _activeSnackbarMessage.value = "Action dismissed from active audit queue."
        }
    }

    fun handleActionClick(action: ComplianceActionEntity) {
        when {
            action.title.contains("tax", ignoreCase = true) -> {
                openScanner("FBR Tax Certificate")
            }
            action.title.contains("income", ignoreCase = true) -> {
                openScanner("Secondary Income Affidavit")
            }
            else -> {
                openScanner("Compliance Supporting Doc")
            }
        }
    }

    fun updateChatInput(text: String) {
        _userChatInput.value = text
    }

    fun toggleVoiceRecording() {
        if (_isVoiceRecording.value) {
            _isVoiceRecording.value = false
            sendUserMessage("How do I verify my secondary income stream?")
        } else {
            _isVoiceRecording.value = true
            viewModelScope.launch {
                delay(2200)
                if (_isVoiceRecording.value) {
                    _isVoiceRecording.value = false
                    sendUserMessage("Please explain the verification steps for PEP compliance.")
                }
            }
        }
    }

    fun sendUserMessage(textToSend: String? = null) {
        val query = (textToSend ?: _userChatInput.value).trim()
        if (query.isEmpty()) return

        _userChatInput.value = ""
        viewModelScope.launch {
            _isAiThinking.value = true
            val reply = ComplianceAiEngine.answerQuery(query, _currentLanguage.value)
            repository.sendChatMessage(
                userQuery = query,
                aiResponseText = reply.englishText,
                aiResponseUrdu = reply.urduText,
                actionButton = reply.actionButtonLabel,
                actionType = reply.actionType
            )
            _isAiThinking.value = false
        }
    }

    fun handleChatAction(actionType: String?) {
        when (actionType) {
            "SCAN_DOCUMENT" -> openScanner("National ID (CNIC)")
            "UPLOAD_SALARY" -> openScanner("Salary Slip (Sep 2023)")
            "VIEW_RISK" -> switchTab(AppTab.AI)
            "SWITCH_LANG" -> toggleLanguage()
            else -> openScanner("Compliance Supporting Doc")
        }
    }

    fun toggleTwoFactor(active: Boolean) {
        viewModelScope.launch {
            repository.updateTwoFactor(active)
            _activeSnackbarMessage.value = if (active) "Two-Factor Authentication is now ACTIVE" else "2FA deactivated"
        }
    }

    fun openChangePasswordDialog() {
        _isChangePasswordDialogOpen.value = true
    }

    fun closeChangePasswordDialog() {
        _isChangePasswordDialogOpen.value = false
    }

    fun openHelpCenterDialog() {
        _isHelpCenterDialogOpen.value = true
    }

    fun closeHelpCenterDialog() {
        _isHelpCenterDialogOpen.value = false
    }

    fun openGuidelinesDialog() {
        _isGuidelinesDialogOpen.value = true
    }

    fun closeGuidelinesDialog() {
        _isGuidelinesDialogOpen.value = false
    }

    fun openSignOutConfirmDialog() {
        _isSignOutConfirmDialogOpen.value = true
    }

    fun closeSignOutConfirmDialog() {
        _isSignOutConfirmDialogOpen.value = false
    }

    fun confirmSignOut() {
        _isSignOutConfirmDialogOpen.value = false
        showSplashScreen()
    }

    fun clearSnackbar() {
        _activeSnackbarMessage.value = null
    }
}
