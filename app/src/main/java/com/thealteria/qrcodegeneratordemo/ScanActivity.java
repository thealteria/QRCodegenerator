package com.thealteria.qrcodegeneratordemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "ScanActivity";
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        setScannerView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Log.d(TAG, "onGranted: " + "Permission granted");
            } else {
                requestPermissions();
                Log.d(TAG, "onGranted: " + "Requesting permission");
            }
        } else {
            Log.d(TAG, "Permission is granted");
        }

    }

    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();

        Intent intent = new Intent(getApplicationContext(), ScannedDataActivity.class);
        intent.putExtra("scannedData", scanResult);
        startActivity(intent);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ScanActivity.this, CAMERA)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraPermission) {
                        cameraEnabled();
                        Log.e("value", "Permission Granted");
                    }
                } else {
                    Log.e("value", "Permission Denied");
                }
                break;
        }
    }

    private void cameraEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //scannerView.resumeCameraPreview(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    setScannerView();
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermissions();
            }
        } else {
            Log.d(TAG, "Permission is granted");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    private void setScannerView() {
        scannerView = new ZXingScannerView(this);
        scannerView.setAutoFocus(true);
        setContentView(scannerView);
    }
}


