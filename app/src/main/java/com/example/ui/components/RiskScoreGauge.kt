package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TrustLensOnPrimaryContainer
import com.example.ui.theme.TrustLensOnSurfaceVariant
import com.example.ui.theme.TrustLensPrimary
import com.example.ui.theme.TrustLensPrimaryContainer
import com.example.ui.theme.TrustLensSecondaryContainer

@Composable
fun RiskScoreCard(
    riskScore: Int,
    currentLanguage: String,
    modifier: Modifier = Modifier
) {
    val targetProgress = (riskScore / 100f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "risk_score_progress"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp)),
        color = TrustLensPrimaryContainer,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Row: Category label & Big Metric & Analytics Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = if (currentLanguage == "UR") "کمپلائنس رسک سکور | RISK INDEX"
                        else "COMPLIANCE RISK INDEX | رسک سکور",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.08.sp,
                            color = TrustLensOnPrimaryContainer
                        )
                    )
                    Text(
                        text = "$riskScore%",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrustLensOnPrimaryContainer,
                            lineHeight = 38.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TrustLensSecondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analytics",
                        tint = TrustLensOnPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Linear Progress Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFFEF7FF).copy(alpha = 0.6f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(TrustLensPrimary)
                )
            }

            // Summary description
            Text(
                text = if (riskScore >= 60) {
                    if (currentLanguage == "UR") "آپ کے پاس 3 فوری تصدیقی دستاویزات باقی ہیں۔"
                    else "You have 3 priority compliance filings requiring immediate review."
                } else {
                    if (currentLanguage == "UR") "آپ کا تعمیل پروفائل مکمل اور فعال ہے۔"
                    else "All core compliance filings verified. Safe standing maintained."
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TrustLensOnSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            // Source indicator badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(TrustLensSecondaryContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Source Document",
                    tint = TrustLensOnPrimaryContainer,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Source: {{DATA:DOCUMENT:DOCUMENT_3}}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TrustLensOnPrimaryContainer,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

