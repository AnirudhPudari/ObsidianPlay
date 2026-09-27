# 🎮 ObsidianPlay

<p align="center">
  <strong>The Ultimate Video Game Backlog, Library Manager & Social Discovery Companion</strong><br>
  <em>Built with Kotlin Multiplatform (KMP) & Compose Multiplatform targeting Android, iOS, and Web.</em>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin Multiplatform" />
  <img src="https://img.shields.io/badge/Compose-Multiplatform-4285F4?logo=jetpackcompose&logoColor=white" alt="Compose Multiplatform" />
  <img src="https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20Web-3DDC84" alt="Platforms" />
  <img src="https://img.shields.io/badge/Database-SQLDelight%20(SQLite)-003B57?logo=sqlite&logoColor=white" alt="SQLDelight" />
  <img src="https://img.shields.io/badge/API-IGDB%20(Twitch)-9146FF?logo=twitch&logoColor=white" alt="IGDB API" />
  <img src="https://img.shields.io/badge/Billing-RevenueCat-E5534B" alt="RevenueCat" />
</p>

---

## ✨ Features

- ⚡ **Social Video Game Discovery**: Share gameplay clips or links directly from **TikTok, Instagram Reels, YouTube Shorts, Twitch, Reddit, X (Twitter), and Steam**. ObsidianPlay automatically cleans and extracts the game title, instantly querying the IGDB database.
- 📚 **Complete Backlog & Library Management**: Seamlessly categorize games across **Playing**, **Backlog**, **Completed**, and **Dropped** with personal ratings and completion timestamps.
- 🎨 **Modern Gaming UI**: Dark-themed obsidian design with vibrant cyan/violet gradients, smooth Compose animations, and platform tags (PC, PlayStation, Xbox, Switch, Retro).
- 🎲 **Backlog Roulette**: Can't decide what to play next? Spin the built-in backlog roulette to randomly pick a title from your queue.
- 📊 **Gamer Stats & Wrapped**: Visual completion statistics, playtime summaries, and shareable gamer cards.
- 🔒 **Offline-First & Lightning Fast**: Fast local SQLite persistence using SQLDelight. All your library data is saved locally on your device with instant load times.
- 👑 **Obsidian PRO**: In-app purchases powered by RevenueCat, custom promo code engine, and demo trial modes.

---

## 🏗️ Architecture & Tech Stack

- **UI Layer**: Compose Multiplatform (Material 3 + Custom Obsidian Gaming Theme)
- **Architecture**: Clean Architecture (MVI / MVVM pattern with Kotlin Coroutines & `StateFlow`)
- **Networking**: Ktor HTTP Client + Kotlinx Serialization
- **Database**: SQLDelight (Multiplatform SQLite for Android, iOS, and Web)
- **Game Metadata**: IGDB API (Internet Game Database via Twitch Developer)
- **Monetization**: RevenueCat SDK (Android Google Play Billing & iOS StoreKit)

---

## 🚀 Getting Started & Setup Guide

### 1. Prerequisites
- **JDK 17 or 21** installed (e.g. JetBrains Runtime or OpenJDK)
- **Android Studio** (Ladybug / Koala or newer) with Android SDK 34+
- **Xcode** 15+ (required only if building for iOS)

---

### 2. Configure IGDB API Credentials (Required)
ObsidianPlay uses the free **IGDB (Internet Game Database)** API to fetch game artwork, ratings, screenshots, and metadata.

1. Go to the [Twitch Developer Portal](https://dev.twitch.tv/console/apps) and log in with your Twitch account.
2. Click **Register Your Application**:
   - **Name**: `ObsidianPlay (or your custom name)`
   - **OAuth Redirect URLs**: `http://localhost`
   - **Category**: `Application Integration`
3. Click **Create** and copy your **Client ID**.
4. Click **New Secret** and copy your **Client Secret**.

---

### 3. Setup `local.properties`
Copy the template file to create your private `local.properties` (this file is gitignored and will never be committed):

```bash
cp local.properties.example local.properties
```

Open `local.properties` and paste your IGDB keys:

```properties
sdk.dir=/Users/YOUR_USERNAME/Library/Android/sdk

# Your Twitch / IGDB API Credentials
IGDB_CLIENT_ID=your_actual_twitch_client_id_here
IGDB_CLIENT_SECRET=your_actual_twitch_client_secret_here
```

*(During compilation, Gradle automatically generates the type-safe `IgdbSecrets.kt` object from this file).*

---

## 📱 Building & Running the Apps

### Android
Open the project in Android Studio and run `androidApp`, or build via terminal:
```bash
# Debug APK
./gradlew :androidApp:assembleDebug

# Release App Bundle (AAB for Google Play)
./gradlew :androidApp:bundleRelease
```

### iOS
1. Open the Xcode project:
   ```bash
   open iosApp/iosApp.xcodeproj
   ```
2. In Xcode $\rightarrow$ select your development team under **Signing & Capabilities**.
3. Choose a simulator or connected iPhone and click **Run** (or `Product` $\rightarrow$ `Archive` for App Store distribution).

Alternatively, compile the shared Kotlin iOS framework from terminal:
```bash
./gradlew :shared:linkReleaseFrameworkIosArm64
```

### Web (Wasm / JS)
Run the fast WebAssembly (Wasm) browser target:
```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

---

## 📂 Project Structure

```text
ObsidianPlay/
├── androidApp/          # Android application module (MainActivity, Manifest, Billing)
├── iosApp/              # iOS Xcode project & SwiftUI entry point
├── webApp/              # Web application module (Kotlin/Wasm & JS targets)
├── shared/              # Shared Compose Multiplatform codebase
│   ├── commonMain/      # Shared UI, Domain models, SQLDelight DB, IGDB API client
│   ├── androidMain/     # Android platform implementations (BackHandler, Context)
│   ├── iosMain/         # iOS platform implementations (MainViewController, DB Factory)
│   └── wasmJsMain/      # Web platform implementations
└── docs/                # Public GitHub Pages (Privacy Policy, Support & Landing page)
```

---

## 🔒 Privacy & Legal

- **Privacy Policy**: [https://anirudhpudari.github.io/ObsidianPlay/privacy.html](https://anirudhpudari.github.io/ObsidianPlay/privacy.html)
- **Support & FAQ**: [https://anirudhpudari.github.io/ObsidianPlay/support.html](https://anirudhpudari.github.io/ObsidianPlay/support.html)

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).