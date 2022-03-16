package com.example.sihfrontend.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sihfrontend.R;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_main);

        SharedPreferences sh = AdminMainActivity.this.getSharedPreferences("Admin_Monument",MODE_PRIVATE);
        String monument_name = sh.getString("monument_name",null);
        if(monument_name!=null){
            startActivity(new Intent(AdminMainActivity.this,InVerificationProcess.class));
        }

        Button btn_proceed =findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this,AdminInputs.class));
            }
        });


    }
}
