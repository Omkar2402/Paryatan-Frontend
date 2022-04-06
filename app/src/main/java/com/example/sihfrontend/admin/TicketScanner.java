package com.example.sihfrontend.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sihfrontend.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class TicketScanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ticket_scanner);


        IntentIntegrator intentIntegrator = new IntentIntegrator(TicketScanner.this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(CaptureActivity.class);
        intentIntegrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (intentResult.getContents() != null) {
            Toast.makeText(this, intentResult.getContents(), Toast.LENGTH_SHORT).show();
            String msg = decryption(intentResult.getContents());
            Log.d("Msg: ",msg);
        }else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();

        }
    }
    public static String decryption(String s){
        int l = s.length();
        int b = (int) Math.ceil(Math.sqrt(l));
        int a = (int) Math.floor(Math.sqrt(l));
        String decrypted="";
        // Matrix to generate the
        // Encrypted String
        char [][]arr = new char[a][b];
        int k = 0;
        // Fill the matrix column-wise
        for (int j = 0; j < b; j++) {
            for (int i = 0; i < a; i++) {
                if (k < l){
                    arr[j][i] = s.charAt(k);
                }
                k++;
            }
        }
        // Loop to generate
        // decrypted String
        for (int j = 0; j < a; j++) {
            for (int i = 0; i < b; i++) {
                decrypted = decrypted + arr[i][j];
            }
        }
        return decrypted;
    }
}