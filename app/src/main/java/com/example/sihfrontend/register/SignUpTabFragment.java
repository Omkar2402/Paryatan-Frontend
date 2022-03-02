package com.example.sihfrontend.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sihfrontend.R;
import com.example.sihfrontend.admin.AdminMainActivity;
import com.example.sihfrontend.user.UserMainActivity;
import java.io.File;
import java.util.Map;


// Inflater: convert xml file into a view
//View Group : Adding fragment to activity parent class for all views
public class SignUpTabFragment extends Fragment {

    private EditText etname;
    private EditText etpassword;
    private EditText etconfirmPassword;
    private TextView tvrole;
    private Button signup;
    private RadioGroup radioGroup;
    private RadioButton user;
    private RadioButton admin;
    private ProgressBar progressBar;
    loginhttp loginHttp = new loginhttp();

    private  File file;

    String role = null;
    float v =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        String  email = registerActivity.getEmail();

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);





        etname = root.findViewById(R.id.etName);
        etpassword = root.findViewById(R.id.etPassword);
        etconfirmPassword = root.findViewById(R.id.etConfirmPassword);
        tvrole = root.findViewById(R.id.txtFile);
        radioGroup = root.findViewById(R.id.radio_group);
        user = root.findViewById(R.id.radio_user);
        admin = root.findViewById(R.id.radio_admin);
        signup = root.findViewById(R.id.btnSignUp);
        progressBar = root.findViewById(R.id.progressbar);


        etname.setTranslationX(800);
        etpassword.setTranslationX(800);
        etconfirmPassword.setTranslationX(800);
        tvrole.setTranslationX(800);
        radioGroup.setTranslationX(800);
        signup.setTranslationX(800);

        etname.setAlpha(v);
        etpassword.setAlpha(v);
        etconfirmPassword.setAlpha(v);
        tvrole.setAlpha(v);
        radioGroup.setAlpha(v);
        signup.setAlpha(v);

        etname.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        etpassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        etconfirmPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tvrole.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        radioGroup.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        signup.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1000).start();


        user.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                role = "user";
            }
        });

        admin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                role="admin";
            }
        });




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString();
                String password = etpassword.getText().toString();
                String confirm_password = etconfirmPassword.getText().toString();
                Log.d("name:", name);
                Log.d("password:", password);
                Log.d("confirm_password:", confirm_password);
                Log.d("email:", email);
                if (role == null) {
                    Toast.makeText(getContext(), "Please specify your role", Toast.LENGTH_SHORT).show();

                } else if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Please specify your name", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty() || confirm_password.isEmpty()) {
                    Toast.makeText(getContext(), "Please specify your password", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirm_password)) {
                    Toast.makeText(getContext(), "Your password and confrm password don't match", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        while (loginHttp.wait){
                            loginHttp.register(name, email, password, role, file);
                        }
                        progressBar.setVisibility(View.GONE);
                        Log.d("token",""+loginHttp.getToken());
                         Toast.makeText(getContext(), "User registered successfully!!", Toast.LENGTH_SHORT).show();
                         Log.d("Before Shared Prefrences", "Before");
                            SharedPreferences preferences = getActivity().getSharedPreferences("SIH", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("role", role);
                            editor.putString("password", password);
                            editor.putString("token", loginHttp.getToken());
                            editor.apply();
                            Log.d("After Shared Prefrences", "After");

                            if (role.equals("user")) {
                                Log.d("User Intent:", "Before");
                                Intent intent = new Intent(SignUpTabFragment.this.getActivity(), UserMainActivity.class);
                                intent.putExtra("role", role);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                intent.putExtra("token", loginHttp.getToken());
                                intent.putExtra("name",name);
                                startActivity(intent);
                                Log.d("User Intent:", "Before");
                                SignUpTabFragment.this.getActivity().finish();
                            } else {
                                Log.d("Admin Intent:", "Before");
                                Intent intent = new Intent(SignUpTabFragment.this.getActivity(), AdminMainActivity.class);
                                intent.putExtra("role",role);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                intent.putExtra("token", loginHttp.getToken());
                                intent.putExtra("name", name);
                                startActivity(intent);
                                Log.d("Admin Intent:", "Before");
                                SignUpTabFragment.this.getActivity().finish();
                            }

                        //Toast.makeText(getContext(),"Registered successfully",Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }});

        return root;
    }



}
