package com.example.otpverify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnteremailActivity extends AppCompatActivity {

//    EditText enteremail;
//    EditText verifybtn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        final EditText input_email = findViewById(R.id.input_email);
        Button verifybtn = findViewById(R.id.verify_email);

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input_email.getText().toString().trim().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(),verify.class);
                    intent.putExtra("email",input_email.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(EnteremailActivity.this, "Enter email id", Toast.LENGTH_SHORT).show();
                }

            }
        });





    }
}



//    final EditText input_email = findViewById(R.id.input_email);
//        Button verifybtn = findViewById(R.id.verify_email);
//
//        verifybtn.setOnClickListener(view -> {
//
//            if (!enteremail.getText().toString().trim().isEmpty()) {
//                Intent intent = new Intent(getApplicationContext(),verify.class);
//                intent.putExtra("email",enteremail.getText().toString());
//                startActivity(intent);
//            }
//            else {
//                Toast.makeText(main.this, "Enter email id", Toast.LENGTH_SHORT).show();
//            }
//
//        });