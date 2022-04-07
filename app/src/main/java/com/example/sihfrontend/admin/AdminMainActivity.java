package com.example.sihfrontend.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.sihfrontend.R;
import com.example.sihfrontend.UserProfile;
import com.example.sihfrontend.register.RegisterActivity;
import com.example.sihfrontend.streaming.ApplicationInput;
import com.example.sihfrontend.streaming.LiveStreamMain;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_main);

        SharedPreferences sh = AdminMainActivity.this.getSharedPreferences("Admin_Monument",MODE_PRIVATE);
        String monument_name = sh.getString("monument_name",null);
        Log.d("monument_name",""+monument_name);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id==R.id.reportuser) {
            startActivity(new Intent(AdminMainActivity.this,AdminFlagReport.class));
        }
        else if (item_id==R.id.user_profile) {
            startActivity(new Intent(AdminMainActivity.this, UserProfile.class));
        }
        else if(item_id == R.id.video_livestream){
            try{
                SharedPreferences sharedPreferences = AdminMainActivity.this.getSharedPreferences("LiveStream",MODE_PRIVATE);
                String APPLICATION_ID = sharedPreferences.getString("APPLICATION_ID",null);
                String resource_uri = sharedPreferences.getString("RESOURCE_URI",null);
                if(APPLICATION_ID==null || resource_uri==null){
                    startActivity(new Intent(AdminMainActivity.this, ApplicationInput.class));
                }else{
                    startActivity(new Intent(AdminMainActivity.this, LiveStreamMain.class));
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(item_id == R.id.logout){
            startActivity(new Intent(AdminMainActivity.this, RegisterActivity.class));
        }
        return true;
    }
}
