# AndKeys

An Android Persian/English keyboard project designed for GitHub + MGit.

## Send button behavior
`Send` is an in-keyboard editor action. It does **not** connect to, control, or send data to another device. In a website's message editor it asks the focused editor to perform its Send action; if the editor does not expose one, it inserts a newline rather than pretending that a message was sent.

## Build with GitHub Actions
1. Create a GitHub repository named `AndKeys`.
2. Upload/commit the contents of this ZIP to the repository root using MGit.
3. Open **Actions** and run **Android Build** (or push a commit).
4. Download the `AndKeys-debug-apk` artifact from the successful workflow.
5. Install the APK, then enable AndKeys in Android Settings → System/Language & input → On-screen keyboard (wording varies by device).

The workflow intentionally invokes the installed `gradle` command and does not depend on a missing `gradlew` wrapper.

## Local build
Requires JDK 17, Android SDK platform 35 and Gradle 8.9:
`gradle assembleDebug`

## Layout
The keyboard preserves the photographed arrangement as closely as practical. `Send` is the existing top-row button; no extra control is added.
