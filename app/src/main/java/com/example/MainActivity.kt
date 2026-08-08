package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.model.UserRole
import com.example.ui.components.ChangePasswordDialog
import com.example.ui.components.ComplianceGuidelinesDialog
import com.example.ui.components.DocumentScannerDialog
import com.example.ui.components.HelpCenterDialog
import com.example.ui.components.SignOutConfirmDialog
import com.example.ui.components.TrustLensBottomNavBar
import com.example.ui.components.TrustLensTopAppBar
import com.example.ui.screens.AiInsightsScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OfficerEddDashboardScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.UserDataScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                TrustLensApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TrustLensApp(viewModel: MainViewModel) {
    val isSplashScreenActive by viewModel.isSplashScreenActive.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val actions by viewModel.actions.collectAsState()
    val chatMessages by viewModel.chatHistory.collectAsState()
    val userChatInput by viewModel.userChatInput.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val isVoiceRecording by viewModel.isVoiceRecording.collectAsState()
    val onboardingData by viewModel.onboardingData.collectAsState()
    val snackbarMessage by viewModel.activeSnackbarMessage.collectAsState()

    // Dialog States
    val isScannerOpen by viewModel.isScannerDialogOpen.collectAsState()
    val isScanningInProgress by viewModel.isScanningInProgress.collectAsState()
    val scanResult by viewModel.scanResult.collectAsState()
    val selectedScannerDocType by viewModel.selectedScannerDocType.collectAsState()
    val isChangePasswordOpen by viewModel.isChangePasswordDialogOpen.collectAsState()
    val isHelpCenterOpen by viewModel.isHelpCenterDialogOpen.collectAsState()
    val isGuidelinesOpen by viewModel.isGuidelinesDialogOpen.collectAsState()
    val isSignOutConfirmOpen by viewModel.isSignOutConfirmDialogOpen.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    if (isSplashScreenActive) {
        SplashScreen(onDismissSplash = { viewModel.dismissSplashScreen() })
    } else if (!isUserLoggedIn) {
        LoginScreen(
            currentRole = currentRole,
            onRoleSelected = { viewModel.selectRole(it) },
            onLoginSuccess = { role, identifier ->
                viewModel.loginUser(role, identifier)
            },
            currentLanguage = currentLanguage
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TrustLensTopAppBar(
                    currentLanguage = currentLanguage,
                    currentRole = currentRole,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onSwitchRole = {
                        val nextRole = if (currentRole == UserRole.FIELD_AGENT) UserRole.COMPLIANCE_OFFICER else UserRole.FIELD_AGENT
                        viewModel.selectRole(nextRole)
                    },
                    onSignOutClick = { viewModel.logoutUser() },
                    onAvatarClick = { viewModel.switchTab(AppTab.PROFILE) }
                )
            },
            bottomBar = {
                TrustLensBottomNavBar(
                    selectedTab = currentTab,
                    onTabSelected = { viewModel.switchTab(it) },
                    currentRole = currentRole,
                    currentLanguage = currentLanguage
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        AppTab.ONBOARDING -> OnboardingScreen(
                            currentLanguage = currentLanguage,
                            onNavigateToEddDashboard = { data ->
                                viewModel.updateOnboardingData(data)
                            }
                        )

                        AppTab.EDD_DASHBOARD -> OfficerEddDashboardScreen(
                            currentLanguage = currentLanguage,
                            onboardingData = onboardingData,
                            onActionCompleted = { msg ->
                                // Officer completed action on case
                            }
                        )

                        AppTab.DATA -> UserDataScreen(
                            userProfile = userProfile,
                            documents = documents,
                            currentLanguage = currentLanguage,
                            onUploadNewClick = { viewModel.openScanner("Salary Slip") },
                            onDocumentClick = { doc ->
                                viewModel.openScanner(doc.title)
                            }
                        )

                        AppTab.AI -> AiInsightsScreen(
                            riskScore = userProfile?.complianceRiskScore ?: 78,
                            actions = actions,
                            currentLanguage = currentLanguage,
                            onActionClick = { action -> viewModel.handleActionClick(action) },
                            onDismissAction = { actionId -> viewModel.dismissAction(actionId) }
                        )

                        AppTab.CHAT -> ChatScreen(
                            chatMessages = chatMessages,
                            inputText = userChatInput,
                            onInputChanged = { viewModel.updateChatInput(it) },
                            onSendMessage = { query -> viewModel.sendUserMessage(query) },
                            onChatActionClick = { actionType -> viewModel.handleChatAction(actionType) },
                            isThinking = isAiThinking,
                            isRecording = isVoiceRecording,
                            onToggleRecording = { viewModel.toggleVoiceRecording() },
                            currentLanguage = currentLanguage
                        )

                        AppTab.PROFILE -> ProfileScreen(
                            userProfile = userProfile,
                            currentLanguage = currentLanguage,
                            onLanguageSelected = { viewModel.setLanguage(it) },
                            onTwoFactorToggle = { viewModel.toggleTwoFactor(it) },
                            onChangePasswordClick = { viewModel.openChangePasswordDialog() },
                            onHelpCenterClick = { viewModel.openHelpCenterDialog() },
                            onGuidelinesClick = { viewModel.openGuidelinesDialog() },
                            onSignOutClick = { viewModel.openSignOutConfirmDialog() }
                        )
                    }
                }
            }
        }

        // Modals & Dialogs
        DocumentScannerDialog(
            isOpen = isScannerOpen,
            onDismiss = { viewModel.closeScanner() },
            selectedDocType = selectedScannerDocType,
            onSelectDocType = { viewModel.setScannerDocType(it) },
            isScanning = isScanningInProgress,
            scanResult = scanResult,
            onStartScan = { viewModel.startOcrScanSimulation() },
            onCommitScan = { viewModel.commitScannedDocument() },
            currentLanguage = currentLanguage
        )

        ChangePasswordDialog(
            isOpen = isChangePasswordOpen,
            onDismiss = { viewModel.closeChangePasswordDialog() },
            currentLanguage = currentLanguage,
            onPasswordChanged = { viewModel.closeChangePasswordDialog() }
        )

        HelpCenterDialog(
            isOpen = isHelpCenterOpen,
            onDismiss = { viewModel.closeHelpCenterDialog() },
            currentLanguage = currentLanguage
        )

        ComplianceGuidelinesDialog(
            isOpen = isGuidelinesOpen,
            onDismiss = { viewModel.closeGuidelinesDialog() },
            currentLanguage = currentLanguage
        )

        SignOutConfirmDialog(
            isOpen = isSignOutConfirmOpen,
            onDismiss = { viewModel.closeSignOutConfirmDialog() },
            onConfirm = {
                viewModel.closeSignOutConfirmDialog()
                viewModel.logoutUser()
            },
            currentLanguage = currentLanguage
        )
    }
}

