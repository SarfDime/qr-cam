# QR Camera – Power Button Shortcut → QR Scanner

A minimal Android app (~15KB) that masquerades as a camera so the
**double-press power button** shortcut launches the Google ML Kit QR scanner
instead of the stock camera.

---

## How it works

1. The app registers `android.media.action.STILL_IMAGE_CAMERA` intent filters,
   making Android see it as a camera app.
2. When launched, it immediately fires an explicit intent to:
   `com.google.android.gms/com.google.android.gms.mlkit.barcode.v2.ScannerActivity`
3. It then calls `finish()` so it never appears in your recents.

---

## Build (free via GitHub Actions)

1. **Fork or push this repo to GitHub.**
2. Go to **Actions** tab → select **Build QR Camera APK** → **Run workflow**.
3. After ~2 minutes, download `QRCamera-release.apk` from the Artifacts section.

No Android Studio, no local SDK, no cost.

---

## Install

```bash
# Via ADB (easiest with root)
adb install -r app-release.apk

# Or: copy APK to phone, enable "Install unknown apps" for your file manager, tap it
```

---

## Setup: Set as default camera

After installing, you need to set this app as the **default camera**:

**Method A – Default apps settings:**
Settings → Apps → Default Apps → Camera app → select "QR Camera"

**Method B – Via ADB / root terminal:**
```bash
# Set as default camera via package manager (run on device with root)
pm set-default-activity-to-prefer \
  com.qrcam.app \
  com.qrcam.app.MainActivity \
  android.media.action.STILL_IMAGE_CAMERA
```

**Method C – Just launch it once:**
The next time you double-press power, Android will ask you to pick a camera
app. Select "QR Camera" and choose "Always".

---

## Verify the QR scanner intent manually (root/ADB)

Before installing, you can test the GMS scanner intent directly:

```bash
adb shell am start -n \
  com.google.android.gms/com.google.android.gms.mlkit.barcode.v2.ScannerActivity
```

If that opens the QR scanner, the app will work. If not, try the fallback:
```bash
adb shell am start -n \
  "com.google.android.gms/com.google.android.gms.mlkit.barcode.ui.PlatformBarcodeScanningActivityProxy"
```

---

## Alternative: Root-only (no APK needed)

If you'd rather not install an app at all, use **Tasker** (or a root terminal
with a persistent daemon) to intercept the double-press and run:

```bash
am start -n com.google.android.gms/com.google.android.gms.mlkit.barcode.v2.ScannerActivity
```

See the "Root Alternative" section below for details.

---

## Root Alternative: Tasker / Shell Daemon

Since you have root + Android 16, you can skip the APK entirely:

### Option A – Tasker (recommended, ~$4 one-time)
1. Profile → Event → Hardware → Button → Double Press Power
2. Task → App → Launch App (or Run Shell):
   `am start -n com.google.android.gms/com.google.android.gms.mlkit.barcode.v2.ScannerActivity`

### Option B – Button Mapper (free tier available)
- Remap "Double press power" to a custom app/activity
- Supports launching specific activities with package+class

### Option C – Keylayout override (advanced, root)
Android 16's power-button double-press is handled by `PhoneWindowManager`.
You'd need a Magisk module or `framework.jar` patch to redirect it at the
system level — not recommended unless you're comfortable with framework modding.
