package com.example.sihfrontend.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sihfrontend.R;

// Inflater: convert xml file into a view
//View Group : Adding fragment to activity parent class for all views
public class LoginTabFragment extends Fragment {

    private EditText email;
    private EditText password;
    private TextView forgotPassword;
    private Button login;

    float v =0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        email = root.findViewById(R.id.etEmail);
        password = root.findViewById(R.id.etPassword);
        forgotPassword = root.findViewById(R.id.tvForgotPassword);
        login = root.findViewById(R.id.btnLogin);

        email.setTranslationX(800);
        password.setTranslationX(800);
        forgotPassword.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        forgotPassword.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        forgotPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return  root;


    }
}
