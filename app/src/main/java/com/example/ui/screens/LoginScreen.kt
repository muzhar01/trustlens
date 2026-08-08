package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardSurface
import com.example.ui.theme.MobilinkNavy
import com.example.ui.theme.MobilinkTeal
import com.example.ui.theme.MobilinkTealContainer
import com.example.ui.theme.MobilinkTealLight
import com.example.ui.theme.NeutralSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.TrustEmerald
import com.example.ui.theme.TrustEmeraldLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onLoginSuccess: (UserRole, String) -> Unit,
    currentLanguage: String = "EN",
    modifier: Modifier = Modifier
) {
    var selectedRole by remember(currentRole) { mutableStateOf(currentRole) }
    var identifier by remember(selectedRole) {
        mutableStateOf(
            if (selectedRole == UserRole.FIELD_AGENT) "AGT-RAWAL-0412" else "tariq.mehmood@mobilinkbank.com"
        )
    }
    var password by remember { mutableStateOf("Mobilink@2026") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    // Biometric Scanner State
    var isScanningThumb by remember { mutableStateOf(false) }
    var thumbScanProgressText by remember { mutableStateOf("") }
    var isThumbVerified by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Pulse animation for thumb sensor
    val infiniteTransition = rememberInfiniteTransition(label = "thumb_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    fun executeThumbLogin() {
        if (isScanningThumb) return
        isScanningThumb = true
        isThumbVerified = false
        coroutineScope.launch {
            thumbScanProgressText = if (currentLanguage == "UR") "بائیو میٹرک تصدیق جاری ہے..." else "Initializing Optical Scanner..."
            delay(600)
            thumbScanProgressText = if (currentLanguage == "UR") "نادرا بایو ویریفائی کیا جا رہا ہے..." else "Connecting to NADRA BioVeriSys..."
            delay(800)
            thumbScanProgressText = if (currentLanguage == "UR") "شناخت کی تصدیق کامیاب (100%)" else "Thumbprint Verified (100% Match)"
            isThumbVerified = true
            delay(500)
            isScanningThumb = false
            onLoginSuccess(selectedRole, if (selectedRole == UserRole.FIELD_AGENT) "Kamran Khan (Agent 0412)" else "Tariq Mehmood (EDD Officer)")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeutralSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Brand Logo & MMBL Sponsorship Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 12.dp)
            ) {
                // Bank Crest / Badge
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MobilinkTeal.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    color = MobilinkNavy,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MobilinkTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "MOBILINK MICROFINANCE BANK",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.08.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "TrustLens",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MobilinkNavy,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        letterSpacing = (-0.5).sp
                    )
                )

                Text(
                    text = "Clear Intelligence, Instant Inclusion.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MobilinkTeal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )

                Text(
                    text = "AI-Driven Customer Risk Profiling & Digital Onboarding Platform",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Role Selector Switcher Pill
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    // Role 1: Field Agent
                    val isAgent = selectedRole == UserRole.FIELD_AGENT
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isAgent) MobilinkNavy else Color.Transparent)
                            .clickable {
                                selectedRole = UserRole.FIELD_AGENT
                                onRoleSelected(UserRole.FIELD_AGENT)
                            }
                            .padding(vertical = 10.dp, horizontal = 8.dp)
                            .testTag("login_role_agent_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = if (isAgent) MobilinkTeal else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (currentLanguage == "UR") "فیلڈ ایجنٹ" else "Field Agent",
                                color = if (isAgent) Color.White else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isAgent) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }

                    // Role 2: Compliance Officer
                    val isOfficer = selectedRole == UserRole.COMPLIANCE_OFFICER
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isOfficer) MobilinkNavy else Color.Transparent)
                            .clickable {
                                selectedRole = UserRole.COMPLIANCE_OFFICER
                                onRoleSelected(UserRole.COMPLIANCE_OFFICER)
                            }
                            .padding(vertical = 10.dp, horizontal = 8.dp)
                            .testTag("login_role_officer_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = if (isOfficer) MobilinkTeal else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (currentLanguage == "UR") "کمپلائنس آفیسر" else "EDD Officer",
                                color = if (isOfficer) Color.White else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isOfficer) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Role Context Indicator Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = if (selectedRole == UserRole.FIELD_AGENT) MobilinkTealLight else Color(0xFFF1F5F9)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (selectedRole == UserRole.FIELD_AGENT) MobilinkTeal else MobilinkNavy)
                    )
                    Text(
                        text = if (selectedRole == UserRole.FIELD_AGENT)
                            "Branch: Liaquat Bazaar, Rawalpindi (Code 0412) | SBP Asaan Tier"
                        else
                            "Islamabad Head Office | Enhanced Due Diligence (EDD) Queue Portal",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Authentication Form Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(22.dp)),
                color = CardSurface,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (selectedRole == UserRole.FIELD_AGENT) "Agent Credentials Login" else "Officer Secure Sign-In",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )

                    // Identifier input
                    OutlinedTextField(
                        value = identifier,
                        onValueChange = { identifier = it },
                        label = {
                            Text(
                                text = if (selectedRole == UserRole.FIELD_AGENT) "Agent / Terminal ID" else "Official Bank Email"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MobilinkTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_identifier_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MobilinkTeal,
                            unfocusedBorderColor = BorderSubtle,
                            focusedLabelColor = MobilinkTeal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Password input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password / PIN") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MobilinkTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onLoginSuccess(selectedRole, identifier)
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MobilinkTeal,
                            unfocusedBorderColor = BorderSubtle,
                            focusedLabelColor = MobilinkTeal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Remember Me Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MobilinkTeal,
                                checkmarkColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Remember this device at Rawalpindi branch",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 12.sp)
                        )
                    }

                    // Sign In Button
                    Button(
                        onClick = {
                            onLoginSuccess(selectedRole, identifier)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_submit_credentials_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MobilinkNavy,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (currentLanguage == "UR") "سائن ان کریں" else "Sign In with Credentials",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Biometric Thumb Sensor Login Section
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, if (isScanningThumb) MobilinkTeal else BorderSubtle, RoundedCornerShape(22.dp)),
                color = CardSurface,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = MobilinkTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (currentLanguage == "UR") "بایومیٹرک / انگوٹھا سکینر لاگ ان" else "NADRA BioVeriSys Biometric Login",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                    }

                    Text(
                        text = if (currentLanguage == "UR") "انگوٹھے کا نشان لگانے کے لیے نیچے ٹیپ کریں"
                        else "Touch sensor below to scan thumb for instant biometric authorization",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Animated Thumb Scanner Button
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .scale(if (isScanningThumb) pulseScale else 1f)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = if (isThumbVerified) listOf(TrustEmeraldLight, TrustEmerald)
                                    else if (isScanningThumb) listOf(MobilinkTealLight, MobilinkTeal)
                                    else listOf(Color(0xFFF1F5F9), MobilinkNavy.copy(alpha = 0.08f))
                                )
                            )
                            .border(
                                width = if (isScanningThumb || isThumbVerified) 2.5.dp else 1.5.dp,
                                color = if (isThumbVerified) TrustEmerald else if (isScanningThumb) MobilinkTeal else BorderSubtle,
                                shape = CircleShape
                            )
                            .clickable(enabled = !isScanningThumb) {
                                executeThumbLogin()
                            }
                            .testTag("login_biometric_thumb_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isScanningThumb) {
                            CircularProgressIndicator(
                                color = MobilinkTeal,
                                modifier = Modifier.size(76.dp),
                                strokeWidth = 3.dp
                            )
                        }
                        Icon(
                            imageVector = if (isThumbVerified) Icons.Default.CheckCircle else Icons.Default.Fingerprint,
                            contentDescription = "Scan Thumbprint",
                            tint = if (isThumbVerified) TrustEmerald else if (isScanningThumb) MobilinkTeal else MobilinkNavy,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    if (isScanningThumb || isThumbVerified) {
                        Text(
                            text = thumbScanProgressText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isThumbVerified) TrustEmerald else MobilinkTeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    } else {
                        Text(
                            text = "Tap to Scan Thumb / انگوٹھا سکین کریں",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MobilinkTeal,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // Quick 1-Tap Demo Switcher Shortcuts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onRoleSelected(UserRole.FIELD_AGENT)
                        onLoginSuccess(UserRole.FIELD_AGENT, "Kamran Khan (Field Agent 0412)")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("quick_login_agent_shortcut"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MobilinkTeal.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MobilinkNavy)
                ) {
                    Text(
                        text = "Demo Field Agent",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {
                        onRoleSelected(UserRole.COMPLIANCE_OFFICER)
                        onLoginSuccess(UserRole.COMPLIANCE_OFFICER, "Tariq Mehmood (EDD Officer)")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("quick_login_officer_shortcut"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MobilinkNavy.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MobilinkNavy)
                ) {
                    Text(
                        text = "Demo EDD Officer",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Footer Compliance Disclaimer
            Text(
                text = "State Bank of Pakistan (SBP) Customers' Digital Onboarding Framework • Tier 1 Merchant & Asaan Digital Wallet",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextTertiary,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
