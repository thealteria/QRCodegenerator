package com.thealteria.qrcodegeneratordemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class GenerateActivity extends AppCompatActivity {

    private static final String TAG = "GenerateActivity";
    private EditText editText;
    private ImageView qrCodeImg;
    private Spinner vehiclesScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        editText = findViewById(R.id.editText);
        qrCodeImg = findViewById(R.id.qrCodeImg);
        vehiclesScroll = findViewById(R.id.vehiclesScroll);

        vehiclesSpinner();
    }

    public void onBtnClick(View view) {
        String nameText = "Name: " + editText.getText().toString();
        String sampleVeh = String.valueOf(vehiclesScroll.getSelectedItem());
        String vehiclesText = "Vehicle: " + sampleVeh;
        String text = nameText + "\n" + vehiclesText;


        if (editText.length() == 0 || nameText.isEmpty()) {
            toast("Please enter your name!", Toast.LENGTH_SHORT);
        }
        if (sampleVeh.equals("Choose your vehicle..")) {
            toast("Please choose a vehicle!", Toast.LENGTH_SHORT);
        } else {
            hideKeyboard(editText);
            generateQRCode(text);
        }
    }

    private void generateQRCode(String text) {
        try {
            int qrWidth = 300;
            int qrHeight = 300;
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,
                    qrWidth, qrHeight, hintMap);
            //BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            //Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            int[] pixels = new int[width * height];

            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //setting color from graphics
                    pixels[offset + x] = bitMatrix.get(x, y) ? BLACK   : WHITE;

                    //setting color from color resource
//                    pixels[offset + x] = bitMatrix.get(x, y) ? ResourcesCompat.getColor(getResources(),
//                            R.color.orange,null)   : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            Bitmap dtcLogo = BitmapFactory.decodeResource(getResources(), R.drawable.dtc_logo);
            Bitmap merge = mergeBitmaps(dtcLogo, bitmap);

            qrCodeImg.setImageBitmap(merge);


        } catch (Exception e) {
            Log.d(TAG, "onClick: " + e.toString());
        }
    }

    private void vehiclesSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vehicles_arrays,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehiclesScroll.setAdapter(adapter);
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private void toast(String message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    public Bitmap mergeBitmaps(Bitmap logo, Bitmap qrcode) {
        Bitmap combined = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 5, canvasHeight / 5, true);
        int centreX = (canvasWidth - resizeLogo.getWidth()) / 2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        return combined;
    }
}