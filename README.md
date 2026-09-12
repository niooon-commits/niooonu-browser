# niooonu browser

Modern Native Android Browser built with **Kotlin** and **Jetpack Compose (Material 3)**, featuring an aesthetic 3D **Liquid Glassmorphism** interface, official Google Material Symbols, shortcut tiles, discover feed, and integrated in-app web browsing.

## Features
- **Liquid Glassmorphism Canvas**: Procedural 3D fluid wave gradients, specular lighting reflections, and floating glass water droplets.
- **Translucent Smart Omnibox**: Frosted glass capsule search bar with search query execution, colorful Google voice search, and Google Lens shortcuts.
- **4x2 Glass Squircle Shortcuts**: YouTube, Instagram, Facebook, WhatsApp, Google, X (Twitter), Pinterest, and custom Add shortcut button.
- **Discover News Feed**: Glass container with high-contrast typography, thumbnails, metadata, and "See more" navigation.
- **Floating Frosted Glass Dock**: Back, forward, quick-search capsule, tabs counter, and browser overflow menu.
- **In-App WebView Engine**: Smooth web rendering, progress indicator, and hardware back-press navigation.
- **Deterministic Keystore Signing**: Permanent release keystore (`android/my-upload-key.jks`) ensuring persistent application signature across every continuous build.

## Continuous Integration & Release
- Automated GitHub Actions workflow (`.github/workflows/build-android.yml`) builds the release APK on every push.
- Automatically maintains and updates a single continuous GitHub Release (`v1.0.0`), replacing the `.apk` asset in place without creating duplicate releases.

## Local Build
```bash
cd android
./gradlew assembleRelease
```
The signed output APK will be located at:
`android/app/build/outputs/apk/release/app-release.apk`
