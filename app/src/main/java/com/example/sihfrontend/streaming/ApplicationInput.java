package com.example.sihfrontend.streaming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.monument.MonumentDescription;

import org.w3c.dom.Text;

public class ApplicationInput extends AppCompatActivity {

    TextView link;
    EditText applicationId,playerLink;
    Button saveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_input);

        applicationId = findViewById(R.id.tvApplicationId);
        playerLink = findViewById(R.id.tvPlayerLink);
        saveInfo = findViewById(R.id.btnSave);
        link = findViewById(R.id.link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = link.getText().toString();
                    //String url = "http://www.google.com";
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(ApplicationInput.this, Uri.parse(url));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(applicationId.getText().toString().isEmpty() || playerLink.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_LONG).show();
                else{
                    try {
                        SharedPreferences sharedPreferences = getSharedPreferences("LiveStream",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("APPLICATION_ID",applicationId.getText().toString());
                        editor.putString("RESOURCE_URI",playerLink.getText().toString());
                        editor.apply();
                        startActivity(new Intent(ApplicationInput.this,LiveStreamMain.class));
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}