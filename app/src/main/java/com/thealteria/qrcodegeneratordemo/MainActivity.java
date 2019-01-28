package com.thealteria.qrcodegeneratordemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onScan(View view) {
        intent(ScanActivity.class);
    }

    public void onGen(View view) {
        intent(GenerateActivity.class);
    }

    private void intent(Class activity) {
        try {
            Intent intent = new Intent(getApplicationContext(), activity);
            startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG, "onIntent: " + e.getMessage());
        }
    }
}