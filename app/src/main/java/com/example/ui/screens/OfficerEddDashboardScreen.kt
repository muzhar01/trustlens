package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EddCaseEntity
import com.example.data.model.RiskTier
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertAmberBorder
import com.example.ui.theme.AlertAmberLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CriticalCrimson
import com.example.ui.theme.CriticalCrimsonBorder
import com.example.ui.theme.CriticalCrimsonLight
import com.example.ui.theme.MobilinkNavy
import com.example.ui.theme.MobilinkNavyDark
import com.example.ui.theme.MobilinkTeal
import com.example.ui.theme.MobilinkTealLight
import com.example.ui.theme.NeutralSurface
import com.example.ui.theme.NumericDataStyle
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.theme.TrustEmerald
import com.example.ui.theme.TrustEmeraldBorder
import com.example.ui.theme.TrustEmeraldLight

import com.example.data.model.OnboardingApplicationData

@Composable
fun OfficerEddDashboardScreen(
    currentLanguage: String,
    onboardingData: OnboardingApplicationData = OnboardingApplicationData(),
    onActionCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val carouselScrollState = rememberScrollState()

    // Metrics counters state
    var totalTodayCount by remember { mutableIntStateOf(1248) }
    var autoApprovedCount by remember { mutableIntStateOf(1023) }
    var standardQueueCount by remember { mutableIntStateOf(138) }
    var eddPendingCount by remember { mutableIntStateOf(87) }

    // Active Tab in Queue
    var selectedQueueTab by remember { mutableStateOf("EDD_QUEUE") } // "EDD_QUEUE", "STANDARD_QUEUE", "APPROVED_QUEUE"

    // Focused Case Accordion Expansion State
    var isKamranExpanded by remember { mutableStateOf(true) }
    var isSecondaryCaseExpanded by remember { mutableStateOf(false) }

    // Officer Audit Notes text input
    var auditNotesInput by remember { mutableStateOf("") }
    var isAuditNoteExpanded by remember { mutableStateOf(false) }

    // Action execution state
    var lastActionFeedback by remember { mutableStateOf<String?>(null) }
    var kamranStatus by remember { mutableStateOf("EDD_PENDING") } // "EDD_PENDING", "APPROVED", "REJECTED", "ESCALATED"

    // Search query
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    fun executeOfficerDecision(decision: String) {
        kamranStatus = decision
        val message = when (decision) {
            "APPROVED" -> {
                autoApprovedCount += 1
                eddPendingCount = maxOf(0, eddPendingCount - 1)
                "Applicant Kamran Khan (APP-98234) APPROVED with audit note logged."
            }
            "REJECTED" -> {
                eddPendingCount = maxOf(0, eddPendingCount - 1)
                "Applicant Kamran Khan (APP-98234) REJECTED on velocity exposure violation."
            }
            else -> {
                eddPendingCount = maxOf(0, eddPendingCount - 1)
                "Case APP-98234 ESCALATED to Head of Compliance, Islamabad."
            }
        }
        lastActionFeedback = message
        onActionCompleted(message)
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
                .padding(bottom = 140.dp) // space for bottom sticky action dock
        ) {
            // 1. Header App Bar Section (Mobilink Dark Navy #0F2537)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MobilinkNavy,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(CriticalCrimson)
                                )
                                Text(
                                    text = "TrustLens EDD Queue",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                            Text(
                                text = "Islamabad Head Office • Tariq Mehmood (Senior EDD Officer)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            )
                        }

                        // Red Action Req Badge Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CriticalCrimson)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "$eddPendingCount Action Req.",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = { isSearchActive = !isSearchActive },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Filter by Applicant Name, CNIC, or ID...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = MobilinkNavyDark,
                                unfocusedContainerColor = MobilinkNavyDark,
                                focusedBorderColor = MobilinkTeal,
                                unfocusedBorderColor = BorderSubtle.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // 2. Top Metrics Carousel (Horizontal Scrolling Row of 4 KPI cards)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "TODAY'S ONBOARDING FLOW METRICS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.08.sp,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(carouselScrollState)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: Total Today 1,248
                    MetricCarouselCard(
                        label = "Total Today",
                        value = "$totalTodayCount",
                        subtext = "SBP digital submissions",
                        badgeColor = Color(0xFF64748B),
                        backgroundColor = CardSurface,
                        textColor = TextPrimary
                    )

                    // Card 2: Auto-Approved 1,023 (82%)
                    MetricCarouselCard(
                        label = "Auto-Approved",
                        value = "$autoApprovedCount (82%)",
                        subtext = "Instant low-risk live",
                        badgeColor = TrustEmerald,
                        backgroundColor = TrustEmeraldLight,
                        textColor = TrustEmerald
                    )

                    // Card 3: Standard Queue 138 (11%)
                    MetricCarouselCard(
                        label = "Standard Queue",
                        value = "$standardQueueCount (11%)",
                        subtext = "Manual documentation",
                        badgeColor = AlertAmber,
                        backgroundColor = AlertAmberLight,
                        textColor = AlertAmber
                    )

                    // Card 4: EDD Queue 87 (7%) - Action Required Crimson Pulse
                    MetricCarouselCard(
                        label = "EDD Queue",
                        value = "$eddPendingCount (7%)",
                        subtext = "High risk anomaly alert",
                        badgeColor = CriticalCrimson,
                        backgroundColor = CriticalCrimsonLight,
                        textColor = CriticalCrimson,
                        isWarning = true
                    )
                }
            }

            // 3. Active Cases Tab Bar: [ EDD Queue (87) ] | [ Standard (138) ] | [ Approved (1,023) ]
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
                color = CardSurface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    val tabs = listOf(
                        Triple("EDD_QUEUE", "EDD Queue ($eddPendingCount)", CriticalCrimson),
                        Triple("STANDARD_QUEUE", "Standard ($standardQueueCount)", AlertAmber),
                        Triple("APPROVED_QUEUE", "Approved ($autoApprovedCount)", TrustEmerald)
                    )

                    tabs.forEach { (tabKey, label, color) ->
                        val isSelected = selectedQueueTab == tabKey
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) MobilinkNavy else Color.Transparent)
                                .clickable { selectedQueueTab = tabKey }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action feedback banner if executed
            AnimatedVisibility(
                visible = lastActionFeedback != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                lastActionFeedback?.let { feedback ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        color = if (kamranStatus == "APPROVED") TrustEmeraldLight else if (kamranStatus == "REJECTED") CriticalCrimsonLight else MobilinkTealLight
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (kamranStatus == "APPROVED") Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (kamranStatus == "APPROVED") TrustEmerald else if (kamranStatus == "REJECTED") CriticalCrimson else MobilinkNavy,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = feedback,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }

            // 4. Main Content: Case Detail Accordion Mobile View
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary High Risk Case: Kamran Khan (APP-98234)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .border(
                            width = if (kamranStatus == "APPROVED") 1.5.dp else 1.5.dp,
                            color = if (kamranStatus == "APPROVED") TrustEmeraldBorder else CriticalCrimsonBorder,
                            shape = RoundedCornerShape(22.dp)
                        )
                        .testTag("officer_case_card_kamran"),
                    color = CardSurface,
                    shadowElevation = 2.dp
                ) {
                    // Dynamic applicant values from onboarding form data or fallback
                    val applicantName = if (onboardingData.fullName.isNotBlank()) onboardingData.fullName else "Kamran Khan"
                    val applicantId = if (onboardingData.applicantId.isNotBlank()) onboardingData.applicantId else "APP-98234"
                    val applicantCnic = if (onboardingData.cnic.isNotBlank()) onboardingData.cnic else "37405-1234567-1"
                    val applicantOccupation = if (onboardingData.declaredOccupation.isNotBlank()) onboardingData.declaredOccupation else "Shopkeeper"
                    val applicantLocation = if (onboardingData.agentLocation.isNotBlank()) onboardingData.agentLocation else "Liaquat Bazaar, Rawalpindi"
                    val incomeVal = if (onboardingData.declaredMonthlyIncomePkr > 0L) onboardingData.declaredMonthlyIncomePkr.toDouble() else 75000.0
                    val turnoverVal = if (onboardingData.expectedMonthlyTurnoverPkr > 0L) onboardingData.expectedMonthlyTurnoverPkr.toDouble() else 850000.0
                    val maxTxnVal = if (onboardingData.expectedMaxSingleTxnPkr > 0L) onboardingData.expectedMaxSingleTxnPkr.toDouble() else 300000.0
                    val turnoverRatio = String.format("%.1f", turnoverVal / incomeVal)
                    val velocityRatio = String.format("%.0f", (maxTxnVal / incomeVal) * 100.0)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Top Row: Applicant | Occupation, Location | Red Risk Pill
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = applicantName,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = "($applicantId)",
                                        style = NumericDataStyle.copy(
                                            color = TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                                Text(
                                    text = "$applicantOccupation • $applicantLocation | CNIC: $applicantCnic",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            // Red Risk Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (kamranStatus == "APPROVED") TrustEmerald else CriticalCrimson)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (kamranStatus == "APPROVED") "APPROVED" else "SCORE 78/100 - HIGH",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // Financial Metric Snapshot Grid
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeutralSurface)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Declared Income", fontSize = 10.sp, color = TextSecondary)
                                Text("PKR ${String.format("%,.0f", incomeVal)}/mo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Column {
                                Text("Exp. Turnover", fontSize = 10.sp, color = TextSecondary)
                                Text("PKR ${String.format("%,.0f", turnoverVal)}/mo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CriticalCrimson)
                            }
                            Column {
                                Text("Max Single Txn", fontSize = 10.sp, color = TextSecondary)
                                Text("PKR ${String.format("%,.0f", maxTxnVal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CriticalCrimson)
                            }
                        }

                        // Accordion Expand/Collapse Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isKamranExpanded = !isKamranExpanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isKamranExpanded) "Hide Multi-Signal Details" else "View AI Multi-Signal Reason Trail",
                                color = MobilinkTeal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = if (isKamranExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = MobilinkTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // 5. Mobile Multi-Signal Intelligence Drawer (Light red tint #FEF2F2 with dark red border)
                        AnimatedVisibility(
                            visible = isKamranExpanded,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, CriticalCrimsonBorder, RoundedCornerShape(16.dp))
                                    .testTag("officer_ai_reason_box"),
                                color = CriticalCrimsonLight
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PriorityHigh,
                                            contentDescription = null,
                                            tint = CriticalCrimson,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "AI Multi-Signal Reason Trail",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = CriticalCrimson,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }

                                    // Bullet 1: Economic Inconsistency
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text("•", color = CriticalCrimson, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                        Text(
                                            text = "Economic Inconsistency: Turnover (PKR ${String.format("%,.0f", turnoverVal)}) is ${turnoverRatio}x declared income (PKR ${String.format("%,.0f", incomeVal)}). SBP normal retail threshold is 3.0x.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextPrimary,
                                                fontSize = 11.sp,
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }

                                    // Bullet 2: Velocity Exposure
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text("•", color = CriticalCrimson, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                        Text(
                                            text = "Velocity Exposure: Single transaction intent (PKR ${String.format("%,.0f", maxTxnVal)}) represents ${velocityRatio}% of declared monthly income (Threshold: 80%).",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextPrimary,
                                                fontSize = 11.sp,
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }

                                    // Bullet 3: Verification Status
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text("•", color = TrustEmerald, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                        Text(
                                            text = "Verification Status: CNIC active via NADRA Verisys, SIM ownership confirmed, zero PEP/Watchlist matches.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextPrimary,
                                                fontSize = 11.sp,
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Secondary Case in Queue: Asif Mehmood (APP-98235) - Standard Review
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, AlertAmberBorder, RoundedCornerShape(20.dp)),
                    color = CardSurface,
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "Asif Mehmood (APP-98235)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Freelancer • Islamabad Sector F-10 | PKR 140,000/mo",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AlertAmberLight)
                                    .border(1.dp, AlertAmberBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SCORE 48 - MED",
                                    color = AlertAmber,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. Mobile Officer Action Bar (Sticky at Viewport Bottom)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = CardSurface,
            shadowElevation = 12.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Small text input field with placeholder
                OutlinedTextField(
                    value = auditNotesInput,
                    onValueChange = { auditNotesInput = it },
                    placeholder = {
                        Text(
                            text = "Add mandatory compliance audit notes...",
                            fontSize = 12.sp,
                            color = TextTertiary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("officer_audit_notes_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MobilinkTeal,
                        unfocusedBorderColor = BorderSubtle
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                // 3 Equal-Width Action Buttons Side-by-Side: [ Approve ] | [ Reject ] | [ Escalate ]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Button 1: Approve (Green #059669)
                    Button(
                        onClick = { executeOfficerDecision("APPROVED") },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("officer_action_approve_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TrustEmerald,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Approve",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Button 2: Reject (Red #DC2626)
                    Button(
                        onClick = { executeOfficerDecision("REJECTED") },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("officer_action_reject_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CriticalCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Reject",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Button 3: Escalate (Navy #0F2537)
                    Button(
                        onClick = { executeOfficerDecision("ESCALATED") },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("officer_action_escalate_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MobilinkNavy,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Escalate",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCarouselCard(
    label: String,
    value: String,
    subtext: String,
    badgeColor: Color,
    backgroundColor: Color,
    textColor: Color,
    isWarning: Boolean = false
) {
    Surface(
        modifier = Modifier
            .width(132.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        color = backgroundColor,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1
                )
            }

            Text(
                text = value,
                style = NumericDataStyle.copy(
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            )

            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 9.sp
                ),
                maxLines = 1
            )
        }
    }
}
