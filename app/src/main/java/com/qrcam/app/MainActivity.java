package com.qrcam.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Primary target: new Google ML Kit QR scanner UI
    private static final String GMS_PACKAGE = "com.google.android.gms";
    private static final String GMS_SCANNER  =
        "com.google.android.gms.mlkit.barcode.v2.ScannerActivity";

    // Fallback: older Google QR scanner UI
    private static final String GMS_SCANNER_FALLBACK =
        "com.google.android.gms.mlkit.barcode.ui.PlatformBarcodeScanningActivityProxy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchQrScanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Finish immediately so it doesn't linger in recents
        finish();
    }

    private void launchQrScanner() {
        if (tryLaunch(GMS_PACKAGE, GMS_SCANNER)) return;
        if (tryLaunch(GMS_PACKAGE, GMS_SCANNER_FALLBACK)) return;

        // Last resort: generic ZXing-compatible ACTION_SCAN
        try {
            Intent fallback = new Intent("com.google.zxing.client.android.SCAN");
            fallback.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivity(fallback);
        } catch (Exception e) {
            Toast.makeText(this,
                "No QR scanner found. Please install Google Lens or a QR app.",
                Toast.LENGTH_LONG).show();
        }
    }

    private boolean tryLaunch(String pkg, String cls) {
        try {
            Intent i = new Intent();
            i.setComponent(new ComponentName(pkg, cls));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
