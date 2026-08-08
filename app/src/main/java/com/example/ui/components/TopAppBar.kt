package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardSurface
import com.example.ui.theme.MobilinkNavy
import com.example.ui.theme.MobilinkTeal
import com.example.ui.theme.MobilinkTealContainer
import com.example.ui.theme.MobilinkTealLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TrustLensTopAppBar(
    currentLanguage: String,
    currentRole: UserRole,
    onToggleLanguage: () -> Unit,
    onSwitchRole: () -> Unit,
    onSignOutClick: () -> Unit,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .testTag("top_app_bar"),
        color = CardSurface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Mobilink Shield & App Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MobilinkNavy)
                            .clickable(onClick = onAvatarClick)
                            .testTag("avatar_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "TrustLens Shield",
                            tint = MobilinkTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "TrustLens",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MobilinkNavy,
                                    fontSize = 17.sp
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MobilinkTealLight)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "MMBL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MobilinkNavy
                                )
                            }
                        }
                        Text(
                            text = if (currentRole == UserRole.FIELD_AGENT) "Liaquat Bazaar (0412)" else "Islamabad HQ • EDD",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Right Actions: Role Switch Pill + Language Toggle + Logout
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Role Indicator Pill with 1-tap quick switch
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (currentRole == UserRole.FIELD_AGENT) MobilinkTealLight else Color(0xFFF1F5F9))
                            .border(1.dp, if (currentRole == UserRole.FIELD_AGENT) MobilinkTeal.copy(alpha = 0.4f) else BorderSubtle, RoundedCornerShape(12.dp))
                            .clickable(onClick = onSwitchRole)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("top_bar_role_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (currentRole == UserRole.FIELD_AGENT) Icons.Default.Storefront else Icons.Default.Badge,
                                contentDescription = null,
                                tint = if (currentRole == UserRole.FIELD_AGENT) MobilinkTeal else MobilinkNavy,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (currentRole == UserRole.FIELD_AGENT) "Agent" else "Officer",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Switch Role",
                                tint = TextSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Language Toggle
                    IconButton(
                        onClick = onToggleLanguage,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("language_toggle_button")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (currentLanguage == "UR") MobilinkTealContainer else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Switch Language",
                                tint = if (currentLanguage == "UR") MobilinkNavy else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Sign Out Icon Button
                    IconButton(
                        onClick = onSignOutClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_bar_signout_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sign Out",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                color = BorderSubtle,
                thickness = 1.dp
            )
        }
    }
}


