package com.example.sihfrontend.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sihfrontend.R;

public class AdminBankDetails extends AppCompatActivity {

    Button next_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_bank_details);

        next_btn=findViewById(R.id.btn_next);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminBankDetails.this,AdminMonumentDetails.class));
            }
        });

    }
}