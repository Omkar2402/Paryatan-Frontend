package com.example.sihfrontend.admin;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihfrontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AdminBankDetails extends AppCompatActivity {
    EditText accno, accholdername, ifsccode, branchname, bankname;
    Button next;
    String monument_name="xyz123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_bank_details);

        accno = findViewById(R.id.accountno);
        accholdername = findViewById(R.id.accholdername);
        ifsccode = findViewById(R.id.ifsccode);
        branchname = findViewById(R.id.bankbranch);
        bankname = findViewById(R.id.bankname);
        next = findViewById(R.id.btn_next);
        SharedPreferences sharedPreferences = AdminBankDetails.this.getSharedPreferences("SIH", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accno.length()==0){
                    Toast.makeText(AdminBankDetails.this, "Enter account number", Toast.LENGTH_SHORT).show();
                }else if(accholdername.getText().toString().isEmpty()){
                    Toast.makeText(AdminBankDetails.this, "Enter the name of the account holder", Toast.LENGTH_SHORT).show();
                }else if(ifsccode.length()!=11){
                    Toast.makeText(AdminBankDetails.this, "Enter correct IFSC code", Toast.LENGTH_SHORT).show();
                }else if(bankname.getText().toString().isEmpty()){
                    Toast.makeText(AdminBankDetails.this, "Enter Bank Name", Toast.LENGTH_SHORT).show();
                }else if(branchname.getText().toString().isEmpty()){
                    Toast.makeText(AdminBankDetails.this, "Enter Branch Name", Toast.LENGTH_SHORT).show();
                }else{

                    storeBankDetails(token);
                }
            }
        });

    }


    private void storeBankDetails(String token) {
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("account_no",accno.getText().toString())
                    .addFormDataPart("ifsc_code", ifsccode.getText().toString())
                    .addFormDataPart("bank_name",bankname.getText().toString())
                    .addFormDataPart("acc_holder_name",accholdername.getText().toString())
                    .addFormDataPart("branch_name",branchname.getText().toString())
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-44-195-177-209.compute-1.amazonaws.com:8080/admin/accountDetails")
                    .addHeader("Authorization","Bearer "+token)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Log.d("sfsd", String.valueOf(response));
                        JSONObject obj = new JSONObject(response.body().string());
                        String msg = obj.getString("message");
                        Log.d("kjsf",msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}