# AndKeys

Android Persian/English keyboard project for GitHub + MGit, based on the supplied keyboard layout.

## Send
The `Send` key operates through Android's focused editor connection (`InputConnection`), not through a separate device or network service. It asks the focused editor to perform its declared editor action; when no editor action is declared, it sends an Enter key event so websites whose message editors handle Enter can process it. A keyboard cannot universally click a website's separate blue Send button unless that site exposes an editor action or handles Enter.

## Build with GitHub Actions
1. Extract this ZIP.
2. Put the contents of the `AndKeys` folder at the root of a GitHub repository named `AndKeys`.
3. Commit and push using MGit.
4. In GitHub, open Actions and download `AndKeys-debug-apk` from a successful run.

Workflow uses Gradle 8.9 installed by the action, so no missing `gradlew` wrapper is needed.

## Enable
Install the APK, open AndKeys, tap **Open keyboard settings**, enable AndKeys, then choose it from the Android keyboard picker.
