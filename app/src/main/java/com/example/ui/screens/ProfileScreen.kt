package com.example.ui.screens

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.ui.theme.NumericDataStyle
import com.example.ui.theme.TrustLensError
import com.example.ui.theme.TrustLensOnBackground
import com.example.ui.theme.TrustLensOnPrimaryContainer
import com.example.ui.theme.TrustLensOnSurfaceVariant
import com.example.ui.theme.TrustLensOnTertiaryContainer
import com.example.ui.theme.TrustLensOutlineVariant
import com.example.ui.theme.TrustLensPrimary
import com.example.ui.theme.TrustLensPrimaryContainer
import com.example.ui.theme.TrustLensSecondaryContainer
import com.example.ui.theme.TrustLensTertiaryContainer

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onTwoFactorToggle: (Boolean) -> Unit,
    onChangePasswordClick: () -> Unit,
    onHelpCenterClick: () -> Unit,
    onGuidelinesClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Profile & Settings",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnBackground,
                    fontSize = 22.sp
                )
            )
            Text(
                text = if (currentLanguage == "UR") "پروفائل اور ترتیبات | Officer Configuration"
                else "Officer account configuration and preferences | پروفائل اور ترتیبات",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TrustLensOnSurfaceVariant
                )
            )
        }

        // Profile Overview Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(22.dp))
                .testTag("officer_profile_card"),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Officer Avatar
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(TrustLensPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = userProfile?.officerName ?: "Ali Khan",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TrustLensOnBackground,
                                fontSize = 17.sp
                            )
                        )

                        Text(
                            text = if (currentLanguage == "UR") "سینئر کمپلائنس آفیسر"
                            else "Senior Compliance Officer | سینئر کمپلائنس آفیسر",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TrustLensOnSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )

                        // Applicant ID Badge
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(TrustLensSecondaryContainer)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Applicant ID: ${userProfile?.applicantId ?: "APP-98234"}",
                                style = NumericDataStyle.copy(
                                    color = TrustLensOnPrimaryContainer,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onChangePasswordClick,
                    modifier = Modifier.testTag("edit_profile_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = TrustLensPrimary
                    )
                }
            }
        }

        // Security Section
        ProfileSectionCard(title = "SECURITY | سیکیورٹی", icon = Icons.Default.Security) {
            // Row 1: Change Password
            ProfileNavigationRow(
                title = "Change Password | پاس ورڈ تبدیل کریں",
                subtitle = "Update account authentication credentials",
                leadingIcon = Icons.Default.LockReset,
                onClick = onChangePasswordClick,
                testTag = "change_password_row"
            )

            HorizontalDivider(color = TrustLensOutlineVariant.copy(alpha = 0.5f))

            // Row 2: Two-Factor Auth
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = TrustLensPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Two-Factor Auth | دو عنصری تصدیق",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TrustLensOnBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = if (userProfile?.isTwoFactorActive == true) "Active (SMS & Authenticator)" else "Disabled",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (userProfile?.isTwoFactorActive == true) TrustLensPrimary else TrustLensError,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                Switch(
                    checked = userProfile?.isTwoFactorActive ?: true,
                    onCheckedChange = onTwoFactorToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = TrustLensPrimary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = TrustLensOutlineVariant
                    ),
                    modifier = Modifier.testTag("two_factor_switch")
                )
            }
        }

        // Language Section
        ProfileSectionCard(title = "LANGUAGE | زبان", icon = Icons.Default.Badge) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                // Radio Option 1: English
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = currentLanguage == "EN",
                            onClick = { onLanguageSelected("EN") }
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentLanguage == "EN",
                        onClick = { onLanguageSelected("EN") },
                        colors = RadioButtonDefaults.colors(selectedColor = TrustLensPrimary)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "English (US) - Default Interface",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TrustLensOnBackground,
                            fontWeight = if (currentLanguage == "EN") FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }

                HorizontalDivider(color = TrustLensOutlineVariant.copy(alpha = 0.5f))

                // Radio Option 2: Urdu
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = currentLanguage == "UR",
                            onClick = { onLanguageSelected("UR") }
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentLanguage == "UR",
                        onClick = { onLanguageSelected("UR") },
                        colors = RadioButtonDefaults.colors(selectedColor = TrustLensPrimary)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "اردو (Urdu) - علاقائی انٹرفیس",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TrustLensOnBackground,
                            fontWeight = if (currentLanguage == "UR") FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        // Support & Legal Section
        ProfileSectionCard(title = "SUPPORT & LEGAL | سپورٹ اور قانونی", icon = Icons.AutoMirrored.Filled.HelpOutline) {
            ProfileNavigationRow(
                title = "Help Center | ہیلپ سینٹر",
                subtitle = "State Bank KYC & SBP regulatory FAQs",
                leadingIcon = Icons.AutoMirrored.Filled.HelpOutline,
                onClick = onHelpCenterClick,
                testTag = "help_center_row"
            )

            HorizontalDivider(color = TrustLensOutlineVariant.copy(alpha = 0.5f))

            ProfileNavigationRow(
                title = "Compliance Guidelines | تعمیل کے رہنما خطوط",
                subtitle = "AML/CFT verification regulations & standards",
                leadingIcon = Icons.AutoMirrored.Filled.MenuBook,
                onClick = onGuidelinesClick,
                testTag = "compliance_guidelines_row"
            )
        }

        // Sign Out Button (Rose Container pill)
        Button(
            onClick = onSignOutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("profile_sign_out_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = TrustLensTertiaryContainer,
                contentColor = TrustLensOnTertiaryContainer
            ),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TrustLensOutlineVariant)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = TrustLensOnTertiaryContainer,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SIGN OUT | لاگ آؤٹ",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnTertiaryContainer,
                    fontSize = 13.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .background(TrustLensPrimary, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnSurfaceVariant,
                    letterSpacing = 0.06.sp,
                    fontSize = 11.sp
                )
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(22.dp)),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun ProfileNavigationRow(
    title: String,
    subtitle: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = TrustLensPrimary,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TrustLensOnBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TrustLensOnSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TrustLensOutlineVariant,
            modifier = Modifier.size(22.dp)
        )
    }
}

