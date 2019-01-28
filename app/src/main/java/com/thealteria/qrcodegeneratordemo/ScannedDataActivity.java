package com.thealteria.qrcodegeneratordemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class ScannedDataActivity extends AppCompatActivity {

    private static final String TAG = "ScannedDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_data);

        try {
            String data = getIntent().getStringExtra("scannedData");
            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
            Log.d(TAG, "onData: " + data);
        }
        catch (Exception e) {
            Log.d(TAG, "onCreate: " + e.getMessage());
        }
    }
}
