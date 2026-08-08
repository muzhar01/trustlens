# TrustLens — AI-Driven Customer Risk Profiling for Digital Onboarding

**Clear Intelligence, Instant Inclusion.**

> Sponsored by **Mobilink Microfinance Bank Limited (MMBL)** · Built for the State Bank of Pakistan (SBP) **Customers' Digital Onboarding Framework** (Asaan Digital Wallet / Merchant Account Tier).

TrustLens is a native **Android (Jetpack Compose)** application that lets field agents onboard customers and lets compliance officers review, approve, reject, or escalate cases — all driven by a multi-signal AI risk engine that routes applications into **Auto-Approved**, **Standard Review**, or **Enhanced Due Diligence (EDD)** queues in real time.

---

## Table of Contents

1. [Product Overview](#1-product-overview)
2. [Features](#2-features)
3. [Design System & Brand Tokens](#3-design-system--brand-tokens)
4. [App Architecture](#4-app-architecture)
5. [Multi-Signal Risk Engine](#5-multi-signal-risk-engine)
6. [Screens & Flows](#6-screens--flows)
7. [Tech Stack & Dependencies](#7-tech-stack--dependencies)
8. [Project Structure](#8-project-structure)
9. [Getting Started](#9-getting-started)
10. [Environment & Secrets](#10-environment--secrets)
11. [AI Provider Fallback Chain](#11-ai-provider-fallback-chain)
12. [Data Model & Local Storage](#12-data-model--local-storage)
13. [Testing](#13-testing)
14. [Build & Release](#14-build--release)
15. [API Payloads](#15-api-payloads)

---

## 1. Product Overview

TrustLens digitises the SBP onboarding and EDD workflow for MMBL into two tightly-coupled mobile experiences:

- **Field Agent Onboarding Portal** — used on the ground (e.g., **Liaquat Bazaar, Rawalpindi**, branch `0412`) to capture identity, economic profile, and account intent, then run an instant AI risk assessment.
- **Compliance Officer EDD Dashboard** — used at **Islamabad Head Office** to inspect high-risk cases, review the AI multi-signal reasoning trail, and take decisive action (Approve / Reject / Escalate).

The design philosophy is **mobile-native first**: high contrast for outdoor field-agent visibility in local bazaars, combined with dense, scannable data layouts for compliance officers on the move. The base viewport target is **`390 × 844` px**.

---

## 2. Features

### Field Agent / Customer Onboarding

- **Agent & Branch sticky banner** (`Agent Point: Liaquat Bazaar, Rawalpindi | Branch 0412 | v2.4 Live`).
- **4-step horizontal stepper**: `1. Identity → 2. Profile → 3. Intent → 4. Verify` with bilingual (EN/UR) labels.
- **Step 1 — Identity:** CNIC masked input (`37405-XXXXXXX-X`), live NADRA Verisys check simulation with a green `NADRA Verified` pill, name auto-fill, DOB picker, City dropdown, and *"Business address matches CNIC residential address"* checkbox.
- **Step 2 — Profile:** Declared Occupation, Monthly Income (PKR), Source of Funds.
- **Step 3 — Intent:** Account Purpose, Expected Monthly Turnover (PKR), Max Expected Single Txn (PKR).
- **Step 4 — Verify:** *"Run TrustLens AI Risk Assessment"* primary CTA computes the risk score live and routes the case.

### Compliance Officer EDD Dashboard

- **Dark Navy header** with `TrustLens EDD Queue`, notification/search actions, and a red **`87 Action Req.`** badge.
- **Swipeable KPI carousel** — `Total Today 1,248`, `Auto-Approved 1,023 (82%)`, `Standard Queue 138 (11%)`, `EDD Queue 87 (7%)`.
- **Queue tabs** — `EDD Queue (87)` · `Standard (138)` · `Approved (1,023)`.
- **Expandable case accordion** with risk badge (`HIGH RISK — 78/100`).
- **AI Multi-Signal Reason Drawer** (light-red `#FEF2F2` box) listing the reasoning trail.
- **Sticky bottom action dock** — Officer Audit Notes input + `Approve` (green) / `Reject` (red) / `Escalate` (navy).

### Compliance Copilot

- **AI Chat Assistant** with bilingual (English / Urdu) responses and contextual action buttons.
- **AI Insights screen** with an animated risk gauge and required-action cards.
- **Document Scanner** (simulated OCR) for CNIC, salary slips, proof of address, and tax certificates.
- **Bilingual profile** (EN/UR) with 2FA toggle, risk score, documents vault, and change password / help centre.

---

## 3. Design System & Brand Tokens

All tokens live in `app/src/main/java/com/example/ui/theme/Color.kt` and are consumed via `Theme.kt` / `Type.kt`.

| Token | Hex | Role |
| --- | --- | --- |
| **Mobilink Navy** | `#0F2537` | Primary dark — App bar, headers, primary buttons |
| **Mobilink Teal Accent** | `#00A896` | Brand accent — active taps, steppers, radio states |
| **Trust Emerald (Low)** | `#059669` | `LOW` — auto-approved, verified NADRA |
| **Alert Amber (Medium)** | `#D97706` | `MEDIUM` — standard review, discrepancies |
| **Critical Crimson (High)** | `#DC2626` | `HIGH` — EDD routing, anomaly flags |
| **Neutral Surface** | `#F8FAFC` | Screen background |
| **Card Surface** | `#FFFFFF` | Form cards, accordions |

**Typography** (`Type.kt`) uses `FontFamily.SansSerif` (Inter/Roboto-style crisp rendering) across a dense mobile scale — `displaySmall` 24sp down to `labelSmall` 10sp — plus a `NumericDataStyle` for financial figures.

**Theme** (`Theme.kt`) exposes both light and dark `MaterialTheme` color schemes, defaulting to a **light, high-contrast compliance theme** for precision and outdoor readability.

---

## 4. App Architecture

The app follows a clean, layered architecture with a single-activity Compose UI and a `ViewModel` acting as the state coordinator.

```
┌────────────────────────────────────────────────────────────────────┐
│ UI LAYER  (Jetpack Compose, Material 3)                            │
│  Screens: Splash · Login · Onboarding · OfficerEddDashboard        │
│           UserData · AiInsights · Chat · Profile                   │
│  Components: BottomNavBar · TopAppBar · RiskScoreGauge             │
│              DocumentScannerDialog · ProfileDialogs                │
└──────────────────────────────┬─────────────────────────────────────┘
                               │ StateFlow
┌──────────────────────────────▼─────────────────────────────────────┐
│ VIEWMODEL  MainViewModel  (AppTab enum, auth/role/session state,   │
│            onboarding data, scanner & dialog state, chat state)    │
└──────────────────────────────┬─────────────────────────────────────┘
                               │
┌──────────────────────────────▼─────────────────────────────────────┐
│ DOMAIN  ComplianceAiEngine  (multi-provider AI fallback chain)     │
└──────────────────────────────┬─────────────────────────────────────┘
                               │
┌──────────────────────────────▼─────────────────────────────────────┐
│ DATA  ComplianceRepository · AppDatabase (Room) · Retrofit         │
│       Models in data/model · AI DTOs in data/ai/network            │
└────────────────────────────────────────────────────────────────────┘
```

**Data flow (onboarding → decision):**

```
Field Agent captures form  ─▶  TrustLens AI Risk Engine  ─▶  Risk Tier
     (CNIC, income,              (TR + ER + identity)          (LOW / MEDIUM / HIGH)
      turnover, max txn)                                          │
                                                                  ▼
                       Auto-Approved      Standard Queue    Routed to EDD Queue
                       (instant live)     (manual review)   (officer action)
                                                                    │
                                                                    ▼
                                                    Compliance Officer Mobile
                                                    EDD Dashboard (Approve/Reject/Escalate)
```

---

## 5. Multi-Signal Risk Engine

The scoring logic is implemented live in `OnboardingScreen.kt` (`runRiskAssessment()`) and mirrors the specification:

$$\text{Risk Score} = w_1 \cdot f(\text{TR}) + w_2 \cdot g(\text{ER}) + w_3 \cdot I_{\text{Identity}}$$

Where:

- $$\text{TR (Turnover Ratio)} = \frac{\text{Expected Monthly Turnover}}{\text{Declared Monthly Income}}$$
- $$f(\text{TR}) = \min\!\left(100,\ \max\!\left(0,\ \frac{\text{TR} - 1.0}{5.0 - 1.0} \times 100\right)\right)$$
- $$\text{ER (Exposure Ratio)} = \frac{\text{Expected Max Single Txn}}{\text{Declared Monthly Income}}$$
- $$g(\text{ER}) = \min\!\left(100,\ \max\!\left(0,\ \frac{\text{ER} - 0.5}{2.0 - 0.5} \times 100\right)\right)$$
- $$w_1 = 0.50\ \text{(turnover inconsistency)},\quad w_2 = 0.35\ \text{(velocity exposure)},\quad w_3 = 0.15\ \text{(identity)}$$

### Risk Tier Threshold Mapping

| Score | Tier | Routing Action |
| --- | --- | --- |
| **0 – 35** | `LOW` | Auto-Approved — account live immediately |
| **36 – 65** | `MEDIUM` | Standard Review Queue — manual review |
| **66 – 100** | `HIGH` | Instant EDD Routing — account locked pending officer action |

**Reference test case (seeded by default):**

| Metric | Value |
| --- | --- |
| Declared Monthly Income | PKR 75,000 |
| Expected Monthly Turnover | PKR 850,000 (→ 11.33× income) |
| Max Single Txn | PKR 300,000 (→ 400% of income) |
| CNIC | NADRA Verisys matched, SIM confirmed, zero PEP/Watchlist |
| **Result** | **Risk Score 78 — HIGH — ROUTE_TO_EDD_QUEUE** |

---

## 6. Screens & Flows

| Screen | File | Purpose |
| --- | --- | --- |
| Splash | `SplashScreen.kt` | Branded 2.4s splash gate |
| Login | `LoginScreen.kt` | Role selection (`FIELD_AGENT` / `COMPLIANCE_OFFICER`) |
| Onboarding | `OnboardingScreen.kt` | 4-step onboarding form + live risk calc |
| Officer EDD Dashboard | `OfficerEddDashboardScreen.kt` | KPI carousel, queue tabs, case accordion, action dock |
| User Data / Docs | `UserDataScreen.kt` | Document vault & statuses |
| AI Insights | `AiInsightsScreen.kt` | Risk gauge + required actions |
| Chat | `ChatScreen.kt` | Bilingual AI compliance copilot |
| Profile | `ProfileScreen.kt` | EN/UR profile, 2FA, settings, dialogs |

**Navigation & tabs:** Navigation is driven by `MainViewModel`'s `AppTab` enum (`ONBOARDING`, `EDD_DASHBOARD`, `DATA`, `AI`, `CHAT`, `PROFILE`). The bottom nav adapts to the active role: field agents see Onboard / Docs / AI Risk / Chat / Profile; officers land on the EDD dashboard.

---

## 7. Tech Stack & Dependencies

Version catalog: `gradle/libs.versions.toml`.

| Category | Technology |
| --- | --- |
| Language | Kotlin `2.2.10` |
| UI | Jetpack Compose (BOM `2024.09.00`), Material 3, Navigation Compose |
| Architecture | Single-activity + `ViewModel` + StateFlow, Repository pattern |
| Persistence | Room `2.7.0` (KSP) |
| Networking | Retrofit `2.12.0` + Moshi `1.15.2` + OkHttp `4.10.0` |
| AI | Gemini API + OpenCode.ai fallback (via Retrofit) |
| Firebase | BoM `34.15.0` — `firebase-ai`, `firebase-appcheck-recaptcha` |
| Images | Coil Compose `2.7.0` |
| Build | AGP `9.1.1`, KSP `2.3.5`, Secrets plugin, Google Services |
| Testing | JUnit, Robolectric `4.16.1`, Roborazzi `1.59.0`, Compose UI test |

---

## 8. Project Structure

```
trustlens/
├── build.gradle.kts            # Top-level build config
├── settings.gradle.kts         # Root project "TrustLens", :app module
├── gradle.properties           # JVM args, caching, config cache
├── gradle/libs.versions.toml   # Version catalog
├── metadata.json               # Gemini API metadata (server-side Gemini capability)
└── app/
    ├── build.gradle.kts        # Android config, secrets, google-services
    ├── proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/
        │   │   ├── MainActivity.kt
        │   │   ├── data/
        │   │   │   ├── ai/ComplianceAiEngine.kt
        │   │   │   ├── ai/network/AiDataTransferObjects.kt
        │   │   │   ├── ai/network/AiNetworkServices.kt
        │   │   │   ├── local/AppDatabase.kt
        │   │   │   ├── model/ComplianceModels.kt
        │   │   │   └── repository/ComplianceRepository.kt
        │   │   └── ui/
        │   │       ├── components/   (BottomNavBar, TopAppBar, RiskScoreGauge,
        │   │       │                   DocumentScannerDialog, ProfileDialogs)
        │   │       ├── screens/      (Splash, Login, Onboarding, OfficerEddDashboard,
        │   │       │                   UserData, AiInsights, Chat, Profile)
        │   │       ├── theme/        (Color.kt, Theme.kt, Type.kt)
        │   │       └── viewmodel/MainViewModel.kt
        │   └── res/                 (drawable, mipmap, values, xml)
        ├── androidTest/java/...      (ExampleInstrumentedTest)
        └── test/
            ├── java/com/example/     (ExampleUnitTest, ExampleRobolectricTest,
            │                          GreetingScreenshotTest)
            └── screenshots/          (Roborazzi captures)
```

---

## 9. Getting Started

### Prerequisites

- **JDK 17+** and **Android SDK** with API level 36 (`compileSdk 36`, `targetSdk 36`, `minSdk 24`).
- Android Studio (recommended) or a terminal with `JAVA_HOME` / `ANDROID_HOME` configured.
- An Android device/emulator with a `390 × 844`-class portrait viewport for the reference UX.

### Build

```bash
# Linux / macOS / Windows (PowerShell)
./gradlew assembleDebug        # or  gradlew.bat on Windows
```

### Run (device/emulator attached)

```bash
./gradlew installDebug
```

### Run unit tests

```bash
./gradlew testDebugUnitTest
```

> Note: a `debug.keystore` at the project root is required for debug signing (see `app/build.gradle.kts`). Release signing reads `KEYSTORE_PATH` / `STORE_PASSWORD` / `KEY_PASSWORD` from the environment.

---

## 10. Environment & Secrets

Secrets are managed by the **Secrets Gradle Plugin**, which reads `.env` (and falls back to `.env.example`). Create a `.env` at the project root:

```dotenv
# Google Gemini API key (used first by the AI engine)
GEMINI_API_KEY=your_key_here

# Release signing (optional — otherwise KEYSTORE_PATH env fallback applies)
KEYSTORE_PATH=/path/to/my-upload-key.jks
STORE_PASSWORD=your_store_password
KEY_PASSWORD=your_key_password

# Ignored from secret substitution (App Check debug token):
FIREBASE_APPCHECK_DEBUG_TOKEN=
```

The build exposes `GEMINI_API_KEY` via `BuildConfig` (`buildConfig = true`). The `google-services` plugin is configured with `MissingGoogleServicesStrategy.WARN` so builds succeed even without `google-services.json`.

---

## 11. AI Provider Fallback Chain

`ComplianceAiEngine.answerQuery()` resolves every user/chat query through a resilient chain:

1. **Gemini 2.5 Flash** (`generativelanguage.googleapis.com`) — used when a real `GEMINI_API_KEY` is configured.
2. **OpenCode.ai — `deepseek-v4-flash-free`** (`https://opencode.ai/zen/v1/chat/completions`).
3. **OpenCode.ai — `big-pickle`**.
4. **Local high-fidelity domain engine** — a curated, offline Mobilink/SBP compliance knowledge base that always answers (bilingual EN/UR) with contextual action buttons.

Every reply reports its provider (`GEMINI` / `OPENCODE_DEEPSEEK` / `OPENCODE_BIG_PICKLE` / `LOCAL`), and the system prompt anchors the assistant to MMBL's SBP onboarding and EDD rules.

---

## 12. Data Model & Local Storage

**Room database** `trustlens_compliance.db` (`AppDatabase.kt`) with DAOs: `UserDao`, `DocumentDao`, `ActionDao`, `ChatDao`.

| Entity | Table | Notes |
| --- | --- | --- |
| `UserProfileEntity` | `user_profile` | Profile, risk score, officer, 2FA, language |
| `ComplianceDocumentEntity` | `compliance_documents` | Doc vault with status + verification notes |
| `ComplianceActionEntity` | `compliance_actions` | AI-generated required actions (dismissable) |
| `ChatMessageEntity` | `chat_messages` | Bilingual chat history with action buttons |

**Domain objects** in `ComplianceModels.kt` include `OnboardingApplicationData` and `EddCaseEntity` (the reference Kamran Khan case), plus enums `UserRole`, `RiskTier`, `DocumentStatus`.

**Repository** (`ComplianceRepository.kt`) seeds the database with the default case, documents, actions, and chat on first launch, and recalculates the risk score as documents are added/verified.

---

## 13. Testing

| Test | Type | Coverage |
| --- | --- | --- |
| `ExampleUnitTest.kt` | Unit | AI local fallback routing (CNIC, salary, risk, tax, Urdu) + SBP ratio/scoring math |
| `ExampleRobolectricTest.kt` | Robolectric | App name resource resolution (`TrustLens`) |
| `GreetingScreenshotTest.kt` | Roborazzi | Visual regression capture of `LoginScreen` (Pixel 8, `sdk 34`) → `src/test/screenshots/login.png` |
| `ExampleInstrumentedTest.kt` | Instrumented | Espresso device test |

Run:

```bash
./gradlew testDebugUnitTest            # unit + robolectric
./gradlew connectedAndroidTest         # instrumentation (device/emulator)
```

---

## 14. Build & Release

- **Signing:** Release uses a keystore path from `KEYSTORE_PATH` (defaults to `my-upload-key.jks` in the project root) with credentials from env vars. Debug uses `debug.keystore`.
- **Minification:** `isMinifyEnabled = false` in release; ProGuard rules are staged in `app/proguard-rules.pro`.
- **Config:** Compile/target SDK `36`, `minSdk 24`, `applicationId com.aistudio.trustlens.cmplnc`, version `1.0` (`versionCode 1`).

```bash
./gradlew assembleRelease
```

---

## 15. API Payloads

The reference case is mirrored in the code as `OnboardingApplicationData` / `EddCaseEntity` and matches the production schema.

### Input (`POST /api/v1/onboard`)

```json
{
  "applicant_id": "APP-98234",
  "full_name": "Kamran Khan",
  "cnic": "37405-1234567-1",
  "city": "Rawalpindi",
  "declared_occupation": "Shopkeeper (Retail)",
  "declared_monthly_income_pkr": 75000,
  "expected_monthly_turnover_pkr": 850000,
  "expected_max_single_txn_pkr": 300000,
  "purpose_of_account": "Online Customer Payments",
  "agent_location": "Liaquat Bazaar, Rawalpindi"
}
```

### AI Reasoning Output

```json
{
  "applicant_id": "APP-98234",
  "risk_score": 78,
  "risk_tier": "HIGH",
  "edd_required": true,
  "summary_reason": "High economic inconsistency detected where turnover exceeds declared income by 11.3x, combined with significant single transaction exposure.",
  "risk_factors": [
    "Economic Inconsistency: Turnover (PKR 850,000) is 11.3x declared income (PKR 75,000), exceeding normal retail ratio threshold (3.0x).",
    "Velocity Exposure: Max single transaction (PKR 300,000) represents 400% of total monthly income.",
    "Identity & PEP: CNIC verified via NADRA Verisys. Zero PEP/Watchlist hits."
  ],
  "routing_action": "ROUTE_TO_EDD_QUEUE"
}
```

---

## License

TrustLens is a Mobilink Microfinance Bank Limited sponsored internal compliance platform. All data shown is representative/simulated for demonstration and development purposes.
