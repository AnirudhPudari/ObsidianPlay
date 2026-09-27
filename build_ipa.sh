#!/usr/bin/env bash
set -e

echo "=========================================="
echo "🎮 ObsidianPlay iOS .IPA Build Automation"
echo "=========================================="

# 1. Ensure JAVA_HOME is configured
if [ -z "$JAVA_HOME" ]; then
    if [ -d "/Applications/Android Studio.app/Contents/jbr/Contents/Home" ]; then
        export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
    elif [ -x "/usr/libexec/java_home" ]; then
        export JAVA_HOME=$(/usr/libexec/java_home 2>/dev/null || true)
    fi
fi

echo "☕ Using JAVA_HOME: $JAVA_HOME"

# 2. Check for Xcode Developer Directory
if ! xcode-select -p >/dev/null 2>&1; then
    echo "❌ ERROR: Xcode is not installed or active developer directory is not set."
    echo "💡 Please install Xcode from the Mac App Store, then run:"
    echo "   sudo xcode-select -s /Applications/Xcode.app/Contents/Developer"
    exit 1
fi

echo "🔨 Xcode Version: $(xcodebuild -version | tr '\n' ' ')"

# 3. Clean and Build Kotlin iOS Multiplatform Framework
echo "📦 Step 1/3: Compiling Kotlin Multiplatform iOS Release Framework..."
./gradlew :shared:linkReleaseFrameworkIosArm64 --no-daemon

# 4. Archive Xcode Project
BUILD_DIR="$(pwd)/build/ios"
ARCHIVE_PATH="$BUILD_DIR/iosApp.xcarchive"
IPA_OUTPUT_DIR="$(pwd)/build/outputs/ipa"
IPA_PATH="$IPA_OUTPUT_DIR/ObsidianPlay.ipa"

mkdir -p "$BUILD_DIR"
mkdir -p "$IPA_OUTPUT_DIR"

echo "🔨 Step 2/3: Archiving Xcode iOS App..."
xcodebuild -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Release \
  -destination "generic/platform=iOS" \
  -archivePath "$ARCHIVE_PATH" \
  archive \
  CODE_SIGNING_ALLOWED=NO \
  CODE_SIGNING_REQUIRED=NO \
  CODE_SIGN_IDENTITY=""

# 5. Package into .IPA (Payload structure)
echo "📦 Step 3/3: Packaging into ObsidianPlay.ipa..."
rm -rf "$BUILD_DIR/Payload"
mkdir -p "$BUILD_DIR/Payload"
cp -R "$ARCHIVE_PATH/Products/Applications/iosApp.app" "$BUILD_DIR/Payload/"

cd "$BUILD_DIR"
zip -qr "$IPA_PATH" Payload
rm -rf Payload

echo "=========================================="
echo "🎉 SUCCESS! iOS .IPA generated at:"
echo "👉 $IPA_PATH"
echo "=========================================="
