package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.ComplianceActionEntity
import com.example.ui.components.RiskScoreCard
import com.example.ui.theme.NumericDataStyle
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
fun AiInsightsScreen(
    riskScore: Int,
    actions: List<ComplianceActionEntity>,
    currentLanguage: String,
    onActionClick: (ComplianceActionEntity) -> Unit,
    onDismissAction: (Int) -> Unit,
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
        // Screen Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "AI Recommendations & Insights",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnBackground,
                    fontSize = 22.sp
                )
            )
            Text(
                text = if (currentLanguage == "UR") "سفارشات اور بصیرت | AI Recommendations"
                else "Automated compliance analysis based on submitted documents | اے آئی کی سفارشات اور بصیرت",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TrustLensOnSurfaceVariant,
                    lineHeight = 18.sp
                )
            )
        }

        // Bento Risk Score Gauge Card
        RiskScoreCard(
            riskScore = riskScore,
            currentLanguage = currentLanguage
        )

        // Required Actions Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = TrustLensPrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = if (currentLanguage == "UR") "لازمی اقدامات | REQUIRED ACTIONS" else "REQUIRED ACTIONS | لازمی اقدامات",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnSurfaceVariant,
                    letterSpacing = 0.06.sp,
                    fontSize = 11.sp
                )
            )
        }

        // Action Items
        actions.forEach { action ->
            if (action.isPatternInsight) {
                // Insight Pattern Card
                InsightCard(action = action, currentLanguage = currentLanguage)
            } else {
                // AI Action Reason Box
                AiActionCard(
                    action = action,
                    currentLanguage = currentLanguage,
                    onPrimaryClick = { onActionClick(action) },
                    onDismissClick = { onDismissAction(action.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AiActionCard(
    action: ComplianceActionEntity,
    currentLanguage: String,
    onPrimaryClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val isTaxDoc = action.title.contains("tax", ignoreCase = true)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(22.dp))
            .testTag("ai_action_card_${action.id}"),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Top Right AI Pill
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isTaxDoc) TrustLensTertiaryContainer else TrustLensSecondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = if (isTaxDoc) TrustLensOnTertiaryContainer else TrustLensOnPrimaryContainer,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "AI",
                        color = if (isTaxDoc) TrustLensOnTertiaryContainer else TrustLensOnPrimaryContainer,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            // Card Body with Icon & Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Leading Icon Box
                val icon: ImageVector = when (action.iconType) {
                    "fact_check" -> Icons.AutoMirrored.Filled.FactCheck
                    else -> Icons.Default.UploadFile
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isTaxDoc) TrustLensTertiaryContainer else TrustLensPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isTaxDoc) TrustLensOnTertiaryContainer else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Texts and Action Buttons
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = action.title,
                        style = NumericDataStyle.copy(
                            color = TrustLensOnBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = action.titleUrdu,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TrustLensOnSurfaceVariant,
                            fontSize = 12.sp
                        ),
                        textAlign = if (currentLanguage == "UR") TextAlign.Right else TextAlign.Left,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Buttons (Lilac container pill & outline)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onPrimaryClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TrustLensPrimaryContainer,
                                contentColor = TrustLensOnPrimaryContainer
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("action_primary_button_${action.id}")
                        ) {
                            Text(
                                text = action.primaryButtonText ?: "Execute",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TrustLensOnPrimaryContainer,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        if (action.secondaryButtonText != null) {
                            OutlinedButton(
                                onClick = onDismissClick,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.testTag("action_dismiss_button_${action.id}")
                            ) {
                                Text(
                                    text = action.secondaryButtonText,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TrustLensOnBackground,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightCard(
    action: ComplianceActionEntity,
    currentLanguage: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(22.dp)),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TrustLensSecondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = TrustLensOnPrimaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = action.title,
                    style = NumericDataStyle.copy(
                        color = TrustLensOnBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = action.description ?: "Similar risk profiles often resolve compliance issues by updating standard KYC forms. Consider reviewing basic client info.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TrustLensOnSurfaceVariant,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}

