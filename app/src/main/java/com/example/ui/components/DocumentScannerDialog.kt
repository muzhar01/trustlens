package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DocumentScanResult
import com.example.ui.theme.TrustLensOutlineVariant
import com.example.ui.theme.TrustLensPrimary
import com.example.ui.theme.TrustLensSecondary
import com.example.ui.theme.TrustLensSecondaryContainer

@Composable
fun DocumentScannerDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    selectedDocType: String,
    onSelectDocType: (String) -> Unit,
    isScanning: Boolean,
    scanResult: DocumentScanResult?,
    onStartScan: () -> Unit,
    onCommitScan: () -> Unit,
    currentLanguage: String
) {
    if (!isOpen) return

    val docOptions = listOf(
        "Salary Slip",
        "National ID (CNIC)",
        "Proof of Address",
        "Tax Certificate"
    )

    var flashEnabled by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "laser_scanner")
    val laserYRatio by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000F1D)),
            color = Color(0xFF000F1D)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = TrustLensSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == "UR") "دستاویز اسکینر | OCR Scanner" else "TrustLens Smart OCR Scanner",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("scanner_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Scanner",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Document Types Chip Row
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(docOptions) { docType ->
                        val isSelected = docType == selectedDocType
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectDocType(docType) },
                            label = {
                                Text(
                                    text = docType,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TrustLensSecondary,
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF0F2537),
                                labelColor = Color.White.copy(alpha = 0.8f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) TrustLensSecondary else TrustLensOutlineVariant.copy(alpha = 0.4f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Viewfinder / Live Camera Simulation Frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF071422))
                        .border(1.5.dp, Color(0xFF1E3A52), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Laser Beam Animation Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Corner brackets
                        val bracketLen = 28.dp.toPx()
                        val stroke = 3.dp.toPx()
                        val cornerColor = if (isScanning) Color(0xFF00E676) else Color(0xFF68DBA9)

                        // Top-left
                        drawLine(cornerColor, Offset(16.dp.toPx(), 16.dp.toPx()), Offset(16.dp.toPx() + bracketLen, 16.dp.toPx()), stroke)
                        drawLine(cornerColor, Offset(16.dp.toPx(), 16.dp.toPx()), Offset(16.dp.toPx(), 16.dp.toPx() + bracketLen), stroke)

                        // Top-right
                        drawLine(cornerColor, Offset(size.width - 16.dp.toPx(), 16.dp.toPx()), Offset(size.width - 16.dp.toPx() - bracketLen, 16.dp.toPx()), stroke)
                        drawLine(cornerColor, Offset(size.width - 16.dp.toPx(), 16.dp.toPx()), Offset(size.width - 16.dp.toPx(), 16.dp.toPx() + bracketLen), stroke)

                        // Bottom-left
                        drawLine(cornerColor, Offset(16.dp.toPx(), size.height - 16.dp.toPx()), Offset(16.dp.toPx() + bracketLen, size.height - 16.dp.toPx()), stroke)
                        drawLine(cornerColor, Offset(16.dp.toPx(), size.height - 16.dp.toPx()), Offset(16.dp.toPx(), size.height - 16.dp.toPx() - bracketLen), stroke)

                        // Bottom-right
                        drawLine(cornerColor, Offset(size.width - 16.dp.toPx(), size.height - 16.dp.toPx()), Offset(size.width - 16.dp.toPx() - bracketLen, size.height - 16.dp.toPx()), stroke)
                        drawLine(cornerColor, Offset(size.width - 16.dp.toPx(), size.height - 16.dp.toPx()), Offset(size.width - 16.dp.toPx(), size.height - 16.dp.toPx() - bracketLen), stroke)

                        // Animated scanning laser line
                        if (isScanning || scanResult == null) {
                            val laserY = size.height * laserYRatio
                            drawLine(
                                color = Color(0xFF00E676),
                                start = Offset(24.dp.toPx(), laserY),
                                end = Offset(size.width - 24.dp.toPx(), laserY),
                                strokeWidth = 2.5.dp.toPx()
                            )
                        }
                    }

                    // Inside Viewfinder Overlay Information
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                color = TrustLensSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Analyzing Security Features & OCR...",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "NADRA & SBP cryptographic validation in progress",
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        } else if (scanResult != null) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "OCR Scan Successful (99.4% Match)",
                                color = Color(0xFF00E676),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Ready to update bank compliance profile",
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Align $selectedDocType within frame",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Ensure all 4 corners and seals are visible",
                                color = Color.White.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    // Flash Toggle Button in Top Right
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(CircleShape)
                            .background(if (flashEnabled) TrustLensSecondary.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.4f))
                            .clickable { flashEnabled = !flashEnabled }
                            .padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Flash Toggle",
                            tint = if (flashEnabled) Color(0xFF68DBA9) else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scan Results Panel (if OCR complete)
                if (scanResult != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF006C4A), RoundedCornerShape(12.dp)),
                        color = Color(0xFF082218)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "EXTRACTED METADATA",
                                    color = Color(0xFF82F5C1),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFF82F5C1),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "AI Verified",
                                        color = Color(0xFF82F5C1),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Document: ${scanResult.docType}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Holder / Account: ${scanResult.extractedName}",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Reference / Amount: ${scanResult.extractedCnicOrId}",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Validity: ${scanResult.issueDate}",
                                color = Color(0xFF82F5C1),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Action Buttons
                if (scanResult == null) {
                    Button(
                        onClick = onStartScan,
                        enabled = !isScanning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("start_ocr_scan_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TrustLensSecondary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == "UR") "اسکین اور تجزیہ کریں | Capture & Analyze" else "Capture & OCR Analyze",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onCommitScan,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("commit_scanned_doc_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TrustLensSecondary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == "UR") "تصدیق کریں اور محفوظ کریں | Commit & Verify" else "Commit & Verify Document",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onStartScan,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Retake Photo")
                    }
                }
            }
        }
    }
}
