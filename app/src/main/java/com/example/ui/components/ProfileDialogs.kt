package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TrustLensError
import com.example.ui.theme.TrustLensPrimary
import com.example.ui.theme.TrustLensSecondary

@Composable
fun ChangePasswordDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentLanguage: String,
    onPasswordChanged: () -> Unit
) {
    if (!isOpen) return

    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TrustLensPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (currentLanguage == "UR") "پاس ورڈ تبدیل کریں | Change Password" else "Change Password | پاس ورڈ تبدیل کریں",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (success) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Password updated securely! SBP encryption applied.",
                            color = Color(0xFF2E7D32),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = currentPass,
                        onValueChange = { currentPass = it },
                        label = { Text("Current Password") },
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth().testTag("input_current_password"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("New Password (Min 8 chars)") },
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { showPass = !showPass }) {
                                Icon(
                                    imageVector = if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("input_new_password"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPass,
                        onValueChange = { confirmPass = it },
                        label = { Text("Confirm New Password") },
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth().testTag("input_confirm_password"),
                        singleLine = true
                    )

                    if (errorText != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = errorText ?: "",
                            color = TrustLensError,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (!success) {
                Button(
                    onClick = {
                        if (currentPass.isEmpty()) {
                            errorText = "Please enter current password."
                        } else if (newPass.length < 6) {
                            errorText = "New password must be at least 6 characters."
                        } else if (newPass != confirmPass) {
                            errorText = "New passwords do not match."
                        } else {
                            errorText = null
                            success = true
                            onPasswordChanged()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TrustLensPrimary),
                    modifier = Modifier.testTag("submit_change_password_button")
                ) {
                    Text("Update Password")
                }
            } else {
                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        },
        dismissButton = {
            if (!success) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Composable
fun HelpCenterDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentLanguage: String
) {
    if (!isOpen) return

    val faqs = listOf(
        Pair("How to resolve 'Action Required' status?", "Upload all outdated or missing documents (e.g. Salary Slip, Tax return) using the OCR scanner."),
        Pair("Who is my assigned Compliance Officer?", "Ali Khan (ID: APP-98234) at Mobilink Microfinance Bank Head Office, Islamabad."),
        Pair("What is the maximum file size for uploads?", "TrustLens accepts JPG, PNG, and PDF documents up to 25MB with 256-bit AES encryption."),
        Pair("How does TrustLens calculate Risk Score?", "Risk score aggregates document recency, PEP indicators, income source cross-checks, and NADRA verification.")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Filled.HelpCenter, contentDescription = null, tint = TrustLensSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (currentLanguage == "UR") "ہیلپ سینٹر | Help Center" else "Help Center | ہیلپ سینٹر",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                faqs.forEach { (q, a) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = q, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TrustLensPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = a, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TrustLensPrimary)
            ) {
                Text("Got It")
            }
        }
    )
}

@Composable
fun ComplianceGuidelinesDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentLanguage: String
) {
    if (!isOpen) return

    val guidelines = listOf(
        "1. CNIC / National ID must be valid and verifiable via NADRA Verisys.",
        "2. Salary Slip or Proof of Income must be dated within the last 90 calendar days.",
        "3. Residential Address proof (Utility Bill, Tenancy Contract) must match applicant's registered name.",
        "4. FATCA / CRS declarations must be submitted for foreign currency transactions.",
        "5. Politically Exposed Person (PEP) screening is executed automatically upon submission."
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = TrustLensPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (currentLanguage == "UR") "تعمیل کے رہنما خطوط | Compliance Guidelines" else "Compliance Guidelines | تعمیل کے رہنما خطوط",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Mobilink Microfinance Bank & SBP Compliance Framework (2024-2025)",
                    fontSize = 12.sp,
                    color = TrustLensSecondary,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                guidelines.forEach { rule ->
                    Text(
                        text = rule,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TrustLensPrimary)
            ) {
                Text("Understood")
            }
        }
    )
}

@Composable
fun SignOutConfirmDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    currentLanguage: String
) {
    if (!isOpen) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = TrustLensError)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (currentLanguage == "UR") "لاگ آؤٹ کی تصدیق کریں" else "Sign Out Confirmation",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Text(
                text = if (currentLanguage == "UR") "کیا آپ واقعی ٹرسٹ لینس سیشن سے لاگ آؤٹ کرنا چاہتے ہیں؟"
                else "Are you sure you want to end your secure TrustLens compliance session?",
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = TrustLensError),
                modifier = Modifier.testTag("confirm_sign_out_button")
            ) {
                Text("Sign Out | لاگ آؤٹ")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
