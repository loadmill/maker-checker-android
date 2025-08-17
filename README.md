# Loadmill Maker–Checker for Android

An Android (Jetpack Compose) client for the Maker–Checker funds-transfer demo. It’s designed for **traffic capture via an HTTP proxy** so you can turn real app flows into **Loadmill** API tests.

The app has two roles:

* **Maker** – creates a transfer (amount + recipient)
* **Checker** – approves or rejects pending transfers

---

## Repo layout (Android)

```
app/
├── src/main/java/com/loadmill/makerchecker/
│   ├── data/
│   │   ├── remote/
│   │   │   ├── ApiClient.kt      # Retrofit + OkHttp (logging enabled)
│   │   │   └── ApiService.kt     # REST interface
│   │   ├── Repository.kt         # Thin wrapper with Result<T>
│   │   └── Models.kt             # LoginResponse, Transfer, etc.
│   ├── ui/
│   │   ├── components/TopBar.kt  # Burger menu + user chip
│   │   ├── login/LoginScreen.kt
│   │   ├── maker/MakerScreen.kt
│   │   └── checker/CheckerScreen.kt
│   └── MainActivity.kt           # App scaffold + simple navigation
├── src/main/res/                 # Icons, strings, themes, etc.
│   └── xml/network_security_config.xml  # trusts system + user CAs
└── build.gradle.kts              # Compose/Retrofit/OkHttp/coroutines
```

The app reads its backend base URL from Gradle at build time and exposes it as `BuildConfig.BASE_URL`.

---

## Tooling & prerequisites

* **Android Studio Koala (or newer)**
* **JDK 17**
* Android SDK **Compile SDK 36**, **Target SDK 35**, **minSdk 24**
* An HTTP proxy (e.g., **Proxyman**, **Charles**, **OWASP ZAP**, **Burp**) if you plan to capture traffic

---

## Backend & Web (related projects)

This Android client talks to the same demo backend & APIs used by the [web app](https://github.com/loadmill/maker-checker-demo):

* **Backend (Express + Loadmill recorder)** – records every request for easy test generation

    * `/api/login`
    * `/api/transfer/initiate`
    * `/api/transfer/my`
    * `/api/transfer/pending`
    * `/api/transfer/approve`
    * `/api/transfer/reject`
    * `/api/transfer/:id`
    * `/api/audit`
* **Web (React)** – maker/checker dashboards for browser demos

> If you don’t run the backend locally, the Android app defaults to a hosted demo URL.

---

## Pointing the app at your backend

You can override the base URL (used by Retrofit) per-developer/build without code changes:

1. Create or edit `<project-root>/local.properties` (the same file that holds `sdk.dir`).
2. Add:

   ```
   makerChecker.baseUrl=https://<your-host-or-proxy>/   # trailing slash optional
   ```
3. Sync Gradle.

If not set, the app falls back to a hosted demo URL. A trailing slash is added automatically.

---

## Build variants & when to use them

Both variants are capture-friendly:

* **Debug** – for day-to-day development

    * `debuggable = true`
    * OkHttp **LoggingInterceptor** enabled
    * Trusts user-installed CAs (see `network_security_config.xml`)
    * Build:

      ```bash
      ./gradlew :app:assembleDebug
      ```
    * Install on a connected device/emulator:

      ```bash
      ./gradlew :app:installDebug
      ```

* **Release (Demo)** – for sharing an APK with SEs/customers

    * **Minify off**, no obfuscation
    * **Signed with the debug keystore on purpose** so it installs easily\*
    * Same network-capture settings as debug
    * Build:

      ```bash
      ./gradlew :app:assembleRelease
      ```
    * Output: `app/build/outputs/apk/release/app-release.apk`
      (Rename if you like, e.g. `loadmill-maker-checker-release.apk`)

\* If you prefer a “real” release signature, create a keystore and switch the `release` `signingConfig` to it; otherwise keep the convenient default for demos.

---

## Developing in Android Studio

1. **Clone** and open the Android project (`File → Open…` → select the project root).
2. Optional: set `makerChecker.baseUrl` in `local.properties`.
3. Select **app** configuration → choose a device/emulator → **Run**.

**Demo logins** (same as the web app):

| Role    | Username | Password     |
| ------- | -------- | ------------ |
| Maker   | maker    | maker1234!   |
| Checker | checker  | checker1234! |

The app respects system dark/light theme (you’ll see different looks across devices).

---

## Capturing HTTPS traffic with a proxy

This app is intentionally proxy-friendly:

* `network_security_config.xml` trusts **system** and **user** CAs.
* No certificate pinning.
* OkHttp logging is enabled in all builds.

Steps:

1. **Install your proxy’s root certificate** on the device
   Settings → Security → Encryption & credentials → **Install a certificate** → **CA certificate** → select your `.cer/.pem`.
2. **Set the device network proxy** to your proxy’s host\:port (Wi-Fi network → proxy → Manual), or use the proxy’s companion VPN app.
3. Launch the app and run a Maker→Checker flow.
   You should see full request/response bodies in the proxy, ready for Loadmill.

> For Android 14+ devices, installing a user CA requires a screen lock; also ensure your proxy is reachable over the device’s network.

---

## Common tasks

* **Run unit tests**

  ```bash
  ./gradlew test
  ```

* **Clean & rebuild**

  ```bash
  ./gradlew clean assembleDebug
  ```

* **Generate a Play-style AAB (optional)**

  ```bash
  ./gradlew :app:bundleRelease
  ```

  (Demo distribution is simpler with the APK.)

---

## Troubleshooting installs

* **“Problem while parsing the package”**
  Usually means the APK wasn’t signed or got corrupted during transfer. Use the `assembleRelease` output directly and avoid re-zipping. This project signs the demo release with the debug key to keep installs simple.

* **Can’t see HTTPS in the proxy**
  Ensure the **proxy CA is installed** as a **CA**, not as a “VPN & app user certificate”; confirm the device is actually using the proxy; verify the backend URL you’re hitting is HTTPS and reachable.

* **401/403 from the API**
  The token is passed in the `token` header. Use the demo credentials above and make sure your Android app points at the same backend your web demo uses.

---

## Notes on security (by design)

* Release build is **not hardened** (no R8 shrinking, user-CA trusted, verbose HTTP logging).
* This is deliberate to make traffic capture & test generation easy for demos.
* Do **not** reuse this configuration for production apps.
