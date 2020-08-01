package com.thealteria.qrcodegeneratordemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
