package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.viewmodel.AppTab

@Composable
fun TrustLensBottomNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    currentRole: UserRole,
    currentLanguage: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = CardSurface,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentRole == UserRole.FIELD_AGENT) {
                    // Tab 1: Onboarding Flow
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "آن بورڈنگ" else "Onboard",
                        activeIcon = Icons.Filled.HowToReg,
                        inactiveIcon = Icons.Outlined.HowToReg,
                        isSelected = selectedTab == AppTab.ONBOARDING,
                        onClick = { onTabSelected(AppTab.ONBOARDING) },
                        testTag = "nav_tab_onboarding"
                    )

                    // Tab 2: Records / Data
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "دستاویزات" else "Docs",
                        activeIcon = Icons.Filled.Storage,
                        inactiveIcon = Icons.Outlined.Storage,
                        isSelected = selectedTab == AppTab.DATA,
                        onClick = { onTabSelected(AppTab.DATA) },
                        testTag = "nav_tab_data"
                    )

                    // Tab 3: AI Insights
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "اے آئی" else "AI Risk",
                        activeIcon = Icons.Filled.Assessment,
                        inactiveIcon = Icons.Outlined.Assessment,
                        isSelected = selectedTab == AppTab.AI,
                        onClick = { onTabSelected(AppTab.AI) },
                        testTag = "nav_tab_ai"
                    )

                    // Tab 4: Chat Assistant
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "چیٹ" else "Chat",
                        activeIcon = Icons.Filled.ChatBubble,
                        inactiveIcon = Icons.Outlined.ChatBubbleOutline,
                        isSelected = selectedTab == AppTab.CHAT,
                        onClick = { onTabSelected(AppTab.CHAT) },
                        testTag = "nav_tab_chat"
                    )

                    // Tab 5: Profile
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "پروفائل" else "Profile",
                        activeIcon = Icons.Filled.AccountCircle,
                        inactiveIcon = Icons.Outlined.AccountCircle,
                        isSelected = selectedTab == AppTab.PROFILE,
                        onClick = { onTabSelected(AppTab.PROFILE) },
                        testTag = "nav_tab_profile"
                    )
                } else {
                    // EDD Officer Role:
                    // Tab 1: EDD Dashboard
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "ای ڈی ڈی" else "EDD Queue",
                        activeIcon = Icons.Filled.Dashboard,
                        inactiveIcon = Icons.Outlined.Dashboard,
                        isSelected = selectedTab == AppTab.EDD_DASHBOARD,
                        onClick = { onTabSelected(AppTab.EDD_DASHBOARD) },
                        testTag = "nav_tab_edd_dashboard"
                    )

                    // Tab 2: Customer Data
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "ریکارڈز" else "Records",
                        activeIcon = Icons.Filled.Storage,
                        inactiveIcon = Icons.Outlined.Storage,
                        isSelected = selectedTab == AppTab.DATA,
                        onClick = { onTabSelected(AppTab.DATA) },
                        testTag = "nav_tab_data"
                    )

                    // Tab 3: AI Engine & Actions
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "اے آئی" else "AI Insights",
                        activeIcon = Icons.Filled.Psychology,
                        inactiveIcon = Icons.Outlined.Psychology,
                        isSelected = selectedTab == AppTab.AI,
                        onClick = { onTabSelected(AppTab.AI) },
                        testTag = "nav_tab_ai"
                    )

                    // Tab 4: Compliance Chat
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "کمپلائنس" else "Assistant",
                        activeIcon = Icons.Filled.ChatBubble,
                        inactiveIcon = Icons.Outlined.ChatBubbleOutline,
                        isSelected = selectedTab == AppTab.CHAT,
                        onClick = { onTabSelected(AppTab.CHAT) },
                        testTag = "nav_tab_chat"
                    )

                    // Tab 5: Profile
                    BottomNavItem(
                        label = if (currentLanguage == "UR") "پروفائل" else "Profile",
                        activeIcon = Icons.Filled.AccountCircle,
                        inactiveIcon = Icons.Outlined.AccountCircle,
                        isSelected = selectedTab == AppTab.PROFILE,
                        onClick = { onTabSelected(AppTab.PROFILE) },
                        testTag = "nav_tab_profile"
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MobilinkTealLight else Color.Transparent)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) activeIcon else inactiveIcon,
                contentDescription = label,
                tint = if (isSelected) MobilinkNavy else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MobilinkNavy else TextSecondary,
                maxLines = 1
            )
        }
    }
}


