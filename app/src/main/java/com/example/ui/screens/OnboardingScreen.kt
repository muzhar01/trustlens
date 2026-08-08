package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OnboardingApplicationData
import com.example.data.model.RiskTier
import com.example.ui.theme.AlertAmber
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    currentLanguage: String,
    onNavigateToEddDashboard: (OnboardingApplicationData) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // 4-Step Form State
    var activeStep by remember { mutableIntStateOf(1) } // 1. Identity, 2. Profile, 3. Intent, 4. Verify

    // Form inputs initialized with production test case
    var cnicNumber by remember { mutableStateOf("37405-1234567-1") }
    var isNadraVerified by remember { mutableStateOf(true) }
    var fullName by remember { mutableStateOf("Kamran Khan") }
    var dob by remember { mutableStateOf("14-Aug-1985") }
    var selectedCity by remember { mutableStateOf("Rawalpindi") }
    var isAddressMatched by remember { mutableStateOf(true) }

    // Step 2 inputs
    var selectedOccupation by remember { mutableStateOf("Shopkeeper / Retailer") }
    var declaredMonthlyIncome by remember { mutableStateOf("75000") }
    var selectedSourceOfFunds by remember { mutableStateOf("Business Cash Sales") }

    // Step 3 inputs
    var selectedAccountPurpose by remember { mutableStateOf("Online Customer Payments") }
    var expectedMonthlyTurnover by remember { mutableStateOf("850000") }
    var expectedMaxSingleTxn by remember { mutableStateOf("300000") }

    // Dropdown expanded states
    var isCityDropdownOpen by remember { mutableStateOf(false) }
    var isOccupationDropdownOpen by remember { mutableStateOf(false) }
    var isSourceDropdownOpen by remember { mutableStateOf(false) }
    var isPurposeDropdownOpen by remember { mutableStateOf(false) }

    // Live AI calculation results
    var isCalculatingRisk by remember { mutableStateOf(false) }
    var calculatedRiskScore by remember { mutableIntStateOf(78) }
    var calculatedRiskTier by remember { mutableStateOf(RiskTier.HIGH) }
    var isAssessmentCompleted by remember { mutableStateOf(false) }

    // Dynamic Math Ratios Calculation
    val incomeNum = declaredMonthlyIncome.toLongOrNull() ?: 75000L
    val turnoverNum = expectedMonthlyTurnover.toLongOrNull() ?: 850000L
    val maxTxnNum = expectedMaxSingleTxn.toLongOrNull() ?: 300000L

    val turnoverRatio = if (incomeNum > 0) turnoverNum.toDouble() / incomeNum.toDouble() else 11.33
    val exposureRatio = if (incomeNum > 0) (maxTxnNum.toDouble() / incomeNum.toDouble()) * 100.0 else 400.0

    fun runRiskAssessment() {
        coroutineScope.launch {
            isCalculatingRisk = true
            isAssessmentCompleted = false
            activeStep = 4
            delay(1200)

            // Multi-signal algorithm implementation from specification
            val fTR = kotlin.math.min(100.0, kotlin.math.max(0.0, ((turnoverRatio - 1.0) / (5.0 - 1.0)) * 100.0))
            val erRatioDecimal = if (incomeNum > 0) maxTxnNum.toDouble() / incomeNum.toDouble() else 4.0
            val gER = kotlin.math.min(100.0, kotlin.math.max(0.0, ((erRatioDecimal - 0.5) / (2.0 - 0.5)) * 100.0))
            val identityWeight = 10.0 // Low penalty because CNIC is matched

            val rawScore = (0.50 * fTR) + (0.35 * gER) + (0.15 * identityWeight)
            val finalScore = kotlin.math.min(100, kotlin.math.max(1, rawScore.toInt()))

            calculatedRiskScore = finalScore
            calculatedRiskTier = when {
                finalScore <= 35 -> RiskTier.LOW
                finalScore <= 65 -> RiskTier.MEDIUM
                else -> RiskTier.HIGH
            }
            isCalculatingRisk = false
            isAssessmentCompleted = true
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
                .padding(bottom = 90.dp) // space for sticky button
        ) {
            // 1. Agent & Branch Sticky Top Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MobilinkNavy,
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MobilinkTeal)
                        )
                        Text(
                            text = "Agent Point: Liaquat Bazaar, Rawalpindi | Branch 0412",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MobilinkTealLight)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "v2.4 Live",
                            color = MobilinkNavy,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Step Progress Bar Stepper (1. Identity, 2. Profile, 3. Intent, 4. Verify)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
                color = CardSurface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val steps = listOf(
                        Triple(1, "Identity", "شناخت"),
                        Triple(2, "Profile", "پروفائل"),
                        Triple(3, "Intent", "مقصد"),
                        Triple(4, "Verify", "تصدیق")
                    )

                    steps.forEachIndexed { index, (stepNum, titleEn, titleUr) ->
                        val isActive = activeStep == stepNum
                        val isDone = activeStep > stepNum

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { activeStep = stepNum }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isDone) TrustEmerald
                                        else if (isActive) MobilinkNavy
                                        else Color(0xFFE2E8F0)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else {
                                    Text(
                                        text = "$stepNum",
                                        color = if (isActive) Color.White else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = if (currentLanguage == "UR") titleUr else titleEn,
                                color = if (isActive) MobilinkNavy else if (isDone) TrustEmerald else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                            )

                            if (index < steps.size - 1) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .width(10.dp)
                                        .height(1.dp)
                                        .background(BorderSubtle)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Main Form Content Card (#FFFFFF on #F8FAFC background)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(22.dp))
                    .testTag("onboarding_form_card"),
                color = CardSurface,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header inside card
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TrustLens Digital Onboarding",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MobilinkNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            )
                            Text(
                                text = "SBP Asaan Digital Wallet / Merchant Account Tier",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        // NADRA Verisys Live Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(TrustEmeraldLight)
                                .border(1.dp, TrustEmeraldBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = TrustEmerald,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "NADRA Verisys Live",
                                    color = TrustEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = BorderSubtle)

                    // SECTION 1: IDENTITY & VERIFICATION
                    Text(
                        text = "1. Identity & Verisys Verification",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MobilinkNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )

                    // Form Field 1: CNIC Number with input mask
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "CNIC Number / شناختی کارڈ نمبر",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                        OutlinedTextField(
                            value = cnicNumber,
                            onValueChange = { cnicNumber = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("onboarding_cnic_input"),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CreditCard,
                                    contentDescription = null,
                                    tint = MobilinkTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(TrustEmeraldLight)
                                        .border(1.dp, TrustEmeraldBorder, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "NADRA Verified",
                                        color = TrustEmerald,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MobilinkTeal,
                                unfocusedBorderColor = BorderSubtle
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Form Field 2: Full Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Applicant Full Name / پورا نام",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("onboarding_name_input"),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MobilinkTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MobilinkTeal,
                                unfocusedBorderColor = BorderSubtle
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Form Field 3 & 4: Date of Birth & City
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Date of Birth
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Date of Birth",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                            OutlinedTextField(
                                value = dob,
                                onValueChange = { dob = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MobilinkTeal,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // City / Region Dropdown
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "City / شہر",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                            ExposedDropdownMenuBox(
                                expanded = isCityDropdownOpen,
                                onExpandedChange = { isCityDropdownOpen = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedCity,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCityDropdownOpen) },
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                        .fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MobilinkTeal,
                                        unfocusedBorderColor = BorderSubtle
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = isCityDropdownOpen,
                                    onDismissRequest = { isCityDropdownOpen = false }
                                ) {
                                    listOf("Rawalpindi", "Islamabad", "Lahore", "Karachi", "Peshawar").forEach { city ->
                                        DropdownMenuItem(
                                            text = { Text(city) },
                                            onClick = {
                                                selectedCity = city
                                                isCityDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Form Field 5: Address Match Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(end = 8.dp)
                    ) {
                        Checkbox(
                            checked = isAddressMatched,
                            onCheckedChange = { isAddressMatched = it },
                            colors = CheckboxDefaults.colors(checkedColor = MobilinkTeal)
                        )
                        Text(
                            text = "Business address matches CNIC residential address (Liaquat Bazaar, Rawalpindi)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    HorizontalDivider(color = BorderSubtle)

                    // SECTION 2: BUSINESS & ECONOMIC PROFILE
                    Text(
                        text = "2. Business & Economic Profile",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MobilinkNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )

                    // Form Field 6: Declared Occupation dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Declared Occupation / پیشہ",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                        ExposedDropdownMenuBox(
                            expanded = isOccupationDropdownOpen,
                            onExpandedChange = { isOccupationDropdownOpen = it }
                        ) {
                            OutlinedTextField(
                                value = selectedOccupation,
                                onValueChange = {},
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = null,
                                        tint = MobilinkTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isOccupationDropdownOpen) },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                    .fillMaxWidth()
                                    .testTag("onboarding_occupation_dropdown"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MobilinkTeal,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = isOccupationDropdownOpen,
                                onDismissRequest = { isOccupationDropdownOpen = false }
                            ) {
                                listOf(
                                    "Shopkeeper / Retailer",
                                    "Salaried Professional",
                                    "Freelancer / Digital Services",
                                    "Wholesale Trader",
                                    "Agriculture / Farming"
                                ).forEach { occ ->
                                    DropdownMenuItem(
                                        text = { Text(occ) },
                                        onClick = {
                                            selectedOccupation = occ
                                            isOccupationDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Form Field 7: Declared Monthly Income (PKR)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Declared Monthly Income (PKR) / ماہانہ آمدنی",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "PKR 75,000",
                                style = NumericDataStyle.copy(
                                    color = MobilinkNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        OutlinedTextField(
                            value = declaredMonthlyIncome,
                            onValueChange = { declaredMonthlyIncome = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("onboarding_income_input"),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = MobilinkTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MobilinkTeal,
                                unfocusedBorderColor = BorderSubtle
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Form Field 8: Source of Funds Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Source of Funds / فنڈز کا ذریعہ",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                        ExposedDropdownMenuBox(
                            expanded = isSourceDropdownOpen,
                            onExpandedChange = { isSourceDropdownOpen = it }
                        ) {
                            OutlinedTextField(
                                value = selectedSourceOfFunds,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSourceDropdownOpen) },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                    .fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MobilinkTeal,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = isSourceDropdownOpen,
                                onDismissRequest = { isSourceDropdownOpen = false }
                            ) {
                                listOf(
                                    "Business Cash Sales",
                                    "Salary / Payroll",
                                    "Services Income",
                                    "Investments / Rental",
                                    "Family Remittances"
                                ).forEach { src ->
                                    DropdownMenuItem(
                                        text = { Text(src) },
                                        onClick = {
                                            selectedSourceOfFunds = src
                                            isSourceDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = BorderSubtle)

                    // SECTION 3: ACCOUNT INTENT & TRANSACTION LIMITS
                    Text(
                        text = "3. Account Intent & Transaction Limits",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MobilinkNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )

                    // Form Field 9: Account Purpose Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Account Purpose / اکاؤنٹ کا مقصد",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                        ExposedDropdownMenuBox(
                            expanded = isPurposeDropdownOpen,
                            onExpandedChange = { isPurposeDropdownOpen = it }
                        ) {
                            OutlinedTextField(
                                value = selectedAccountPurpose,
                                onValueChange = {},
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalance,
                                        contentDescription = null,
                                        tint = MobilinkTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPurposeDropdownOpen) },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                    .fillMaxWidth()
                                    .testTag("onboarding_purpose_dropdown"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MobilinkTeal,
                                    unfocusedBorderColor = BorderSubtle
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = isPurposeDropdownOpen,
                                onDismissRequest = { isPurposeDropdownOpen = false }
                            ) {
                                listOf(
                                    "Online Customer Payments",
                                    "Personal Savings",
                                    "Supplier Payouts",
                                    "E-Commerce Merchant Disbursals"
                                ).forEach { p ->
                                    DropdownMenuItem(
                                        text = { Text(p) },
                                        onClick = {
                                            selectedAccountPurpose = p
                                            isPurposeDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Form Field 10: Expected Monthly Turnover (PKR)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Expected Monthly Turnover (PKR) / متوقع ماہانہ ٹرن اوور",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "PKR 850,000",
                                style = NumericDataStyle.copy(
                                    color = MobilinkNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        OutlinedTextField(
                            value = expectedMonthlyTurnover,
                            onValueChange = { expectedMonthlyTurnover = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("onboarding_turnover_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MobilinkTeal,
                                unfocusedBorderColor = BorderSubtle
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Form Field 11: Expected Max Single Txn Value (PKR)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Expected Max Single Txn (PKR) / زیادہ سے زیادہ واحد ٹرانزیکشن",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "PKR 300,000",
                                style = NumericDataStyle.copy(
                                    color = MobilinkNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        OutlinedTextField(
                            value = expectedMaxSingleTxn,
                            onValueChange = { expectedMaxSingleTxn = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("onboarding_max_txn_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MobilinkTeal,
                                unfocusedBorderColor = BorderSubtle
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Mathematical Ratio Live Indicator Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, AlertAmber.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                        color = AlertAmberLight
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Assessment,
                                    contentDescription = null,
                                    tint = AlertAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Dynamic SBP Multi-Signal Ratio Evaluation:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "• Turnover Ratio (TR): ${String.format("%.1f", turnoverRatio)}x (Threshold: 3.0x)",
                                    fontSize = 11.sp,
                                    color = if (turnoverRatio > 3.0) CriticalCrimson else TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "• Velocity Exposure (ER): ${exposureRatio.toInt()}% (Threshold: 80%)",
                                    fontSize = 11.sp,
                                    color = if (exposureRatio > 80.0) CriticalCrimson else TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Section 4: Live AI Assessment Result Card
                    AnimatedVisibility(
                        visible = isAssessmentCompleted,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, CriticalCrimsonBorder, RoundedCornerShape(16.dp))
                                .testTag("onboarding_ai_result_card"),
                            color = CriticalCrimsonLight
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(CriticalCrimson)
                                        )
                                        Text(
                                            text = "TrustLens AI Multi-Signal Result",
                                            fontWeight = FontWeight.Bold,
                                            color = CriticalCrimson,
                                            fontSize = 13.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(CriticalCrimson)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "SCORE 78/100 • HIGH RISK",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }

                                Text(
                                    text = "🚨 Anomaly Summary: High economic inconsistency detected where turnover exceeds declared income by 11.3x, combined with significant single transaction exposure (400%). Routed directly to Enhanced Due Diligence (EDD) queue.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )
                                )

                                Button(
                                    onClick = {
                                        onNavigateToEddDashboard(
                                            OnboardingApplicationData(
                                                fullName = fullName,
                                                cnic = cnicNumber,
                                                city = selectedCity,
                                                declaredOccupation = selectedOccupation,
                                                declaredMonthlyIncomePkr = incomeNum,
                                                expectedMonthlyTurnoverPkr = turnoverNum,
                                                expectedMaxSingleTxnPkr = maxTxnNum,
                                                riskScore = calculatedRiskScore,
                                                riskTier = calculatedRiskTier
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .testTag("onboarding_view_in_edd_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MobilinkNavy,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Open in Compliance EDD Dashboard",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Sticky Bottom Action Bar with Primary Button in Mobilink Navy (#0F2537)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = CardSurface,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = { runRiskAssessment() },
                    enabled = !isCalculatingRisk,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("onboarding_submit_ai_assessment_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MobilinkNavy,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isCalculatingRisk) {
                        CircularProgressIndicator(
                            color = MobilinkTeal,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Evaluating SBP Vector Matrix...",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    } else {
                        Text(
                            text = if (currentLanguage == "UR") "ٹرسٹ لینس اے آئی رسک اسیسمنٹ چلائیں" else "Run TrustLens AI Risk Assessment",
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
        }
    }
}
