package com.example.sihfrontend.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.sihfrontend.R;
import com.google.android.material.tabs.TabLayout;

public class RegisterActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    String email="a@b";
    float v =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        //Intent intent = getIntent();
        //email = intent.getStringExtra("email");


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        //add tabs to tab layout
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign up"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); //spread above tabs so to acquire entire space

        final RegisterAdapter registerAdapter = new RegisterAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),this);

        //Set register adapter to view pager

        viewPager.setAdapter(registerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(300);

        tabLayout.setAlpha(v);

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();




    }


    public String getEmail(){
        return email;
    }
}