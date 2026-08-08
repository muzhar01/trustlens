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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComplianceDocumentEntity
import com.example.data.model.DocumentStatus
import com.example.data.model.UserProfileEntity
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
fun UserDataScreen(
    userProfile: UserProfileEntity?,
    documents: List<ComplianceDocumentEntity>,
    currentLanguage: String,
    onUploadNewClick: () -> Unit,
    onDocumentClick: (ComplianceDocumentEntity) -> Unit,
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
        // Top Header Section
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Compliance Records",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnBackground,
                    fontSize = 22.sp
                )
            )
            Text(
                text = if (currentLanguage == "UR") "اپنے تعمیل پروفائل اور دستاویزات کا نظم کریں | Manage compliance profile"
                else "Manage your compliance profile and documents | اپنے تعمیل پروفائل اور دستاویزات کا نظم کریں",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TrustLensOnSurfaceVariant,
                    lineHeight = 18.sp
                )
            )
        }

        // Profile Status Overview Card (Professional Polish Style)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(24.dp))
                .testTag("profile_status_card"),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "PROFILE STATUS | پروفائل کی حیثیت",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TrustLensOnSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.06.sp
                        )
                    )
                    Text(
                        text = userProfile?.fullName ?: "Ahmed Raza",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TrustLensOnBackground,
                            fontSize = 19.sp
                        )
                    )
                    Text(
                        text = "CID: ${userProfile?.customerId ?: "994-201-445"}",
                        style = NumericDataStyle.copy(
                            color = TrustLensOnSurfaceVariant,
                            fontSize = 13.sp
                        )
                    )
                }

                // Action Required Rose Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(TrustLensTertiaryContainer)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Action Required",
                            tint = TrustLensOnTertiaryContainer,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Action Required",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TrustLensOnTertiaryContainer,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Personal Information Bento Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "PERSONAL INFO | ذاتی معلومات",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnSurfaceVariant,
                    letterSpacing = 0.06.sp,
                    fontSize = 11.sp
                )
            )

            // 2-Column Bento Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Full Name Tile
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(18.dp)),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Full Name | نام",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TrustLensOnSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userProfile?.fullName ?: "Ahmed Raza",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TrustLensOnBackground
                            )
                        )
                    }
                }

                // DOB Tile
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(18.dp)),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "DOB | پیدائش",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TrustLensOnSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = userProfile?.dob ?: "14-Aug-1985",
                            style = NumericDataStyle.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TrustLensOnBackground,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            // Registered Address Tile
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(18.dp)),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Registered Address | رجسٹرڈ پتہ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TrustLensOnSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = userProfile?.registeredAddress ?: "Apt 4B, Shahrah-e-Faisal, Block 6 PECHS, Karachi, 75400, Pakistan",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TrustLensOnBackground,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Documents List Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "UPCOMING & REQUIRED DOCUMENTS | دستاویزات",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnSurfaceVariant,
                    letterSpacing = 0.06.sp,
                    fontSize = 11.sp
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                documents.forEach { doc ->
                    DocumentRowItem(
                        document = doc,
                        onClick = { onDocumentClick(doc) }
                    )
                }
            }
        }

        // Primary Upload New Action Button (Styled as Lilac Container Action)
        Button(
            onClick = onUploadNewClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("upload_new_document_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = TrustLensPrimaryContainer,
                contentColor = TrustLensOnPrimaryContainer
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TrustLensOnPrimaryContainer,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (currentLanguage == "UR") "نیا دستاویز اپ لوڈ کریں | Upload New Document" else "Upload New Document | نیا دستاویز اپ لوڈ کریں",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TrustLensOnPrimaryContainer,
                    fontSize = 14.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DocumentRowItem(
    document: ComplianceDocumentEntity,
    onClick: () -> Unit
) {
    val isOutdated = document.status == DocumentStatus.OUTDATED
    val isVerified = document.status == DocumentStatus.VERIFIED

    val icon: ImageVector = when (document.iconType) {
        "id_card" -> Icons.Default.Badge
        "receipt_long" -> Icons.AutoMirrored.Filled.ReceiptLong
        "home_pin" -> Icons.Default.LocationOn
        else -> Icons.Default.Description
    }

    // Assign distinctive palette containers as seen in the design HTML
    val iconContainerColor = when (document.iconType) {
        "id_card" -> TrustLensPrimary
        "receipt_long" -> TrustLensSecondaryContainer
        "home_pin" -> TrustLensTertiaryContainer
        else -> TrustLensPrimaryContainer
    }

    val iconTint = when (document.iconType) {
        "id_card" -> Color.White
        "receipt_long" -> TrustLensOnBackground
        "home_pin" -> TrustLensOnTertiaryContainer
        else -> TrustLensOnPrimaryContainer
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, TrustLensOutlineVariant, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Leading Icon Badge Box
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconContainerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = document.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TrustLensOnBackground,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = if (isOutdated) "Urgent Review • ${document.titleUrdu}" else document.titleUrdu,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isOutdated) TrustLensOnTertiaryContainer else TrustLensOnSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = if (isOutdated) FontWeight.Medium else FontWeight.Normal
                        )
                    )
                }
            }

            // Status Badge Pill
            when (document.status) {
                DocumentStatus.VERIFIED -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(TrustLensSecondaryContainer)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = TrustLensOnPrimaryContainer,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Verified",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TrustLensOnPrimaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
                DocumentStatus.OUTDATED -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(TrustLensTertiaryContainer)
                            .clickable(onClick = onClick)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Upload",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TrustLensOnTertiaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = "Upload New",
                                tint = TrustLensOnTertiaryContainer,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
                DocumentStatus.REVIEWING -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(TrustLensPrimaryContainer)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = "Reviewing",
                                tint = TrustLensOnPrimaryContainer,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reviewing",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TrustLensOnPrimaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(TrustLensSecondaryContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = document.statusLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TrustLensOnPrimaryContainer,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}

