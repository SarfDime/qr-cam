package com.qrcam.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String GMS_PACKAGE = "com.google.android.gms";
    private static final String PRIMARY_SCANNER = "com.google.android.gms.mlkit.barcode.v2.ScannerActivity";
    private static final String FALLBACK_SCANNER = "com.google.android.gms.mlkit.barcode.ui.PlatformBarcodeScanningActivityProxy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startScanner();
        finish();
    }

    private void startScanner() {
        if (launch(createScannerIntent(PRIMARY_SCANNER))) return;
        if (launch(createScannerIntent(FALLBACK_SCANNER))) return;

        Intent fallback = new Intent("com.google.zxing.client.android.SCAN");
        fallback.putExtra("SCAN_MODE", "QR_CODE_MODE");
        if (!launch(fallback)) {
            Toast.makeText(this, "No QR scanner installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private Intent createScannerIntent(String className) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(GMS_PACKAGE, className));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private boolean launch(Intent intent) {
        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }
}
