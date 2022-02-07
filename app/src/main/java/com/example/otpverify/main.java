package com.example.otpverify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class main extends AppCompatActivity {

    EditText enteremail;
    EditText verifybtn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enteremail = findViewById(R.id.input_email);
        verifybtn = findViewById(R.id.verify_email);

        verifybtn.setOnClickListener(view -> {

            if (!enteremail.getText().toString().trim().isEmpty()) {
                Intent intent = new Intent(getApplicationContext(),verify.class);
                intent.putExtra("email",enteremail.getText().toString());
                startActivity(intent);
            }
            else {
                Toast.makeText(main.this, "Enter email id", Toast.LENGTH_SHORT).show();
            }

        });

    }
}