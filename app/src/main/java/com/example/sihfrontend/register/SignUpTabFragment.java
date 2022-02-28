package com.example.sihfrontend.register;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sihfrontend.R;

import java.io.FileNotFoundException;

// Inflater: convert xml file into a view
//View Group : Adding fragment to activity parent class for all views
public class SignUpTabFragment extends Fragment {

    private EditText etname;
    private EditText etpassword;
    private EditText etconfirmPassword;
    private TextView tvupload;
    private TextView tvrole;
    private ImageView imgUploadIcon;
    private Spinner spinner;
    private Button signup;

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
        tvupload = root.findViewById(R.id.txtUpload);
        tvrole = root.findViewById(R.id.txtFile);
        imgUploadIcon = root.findViewById(R.id.imgUpload);
        spinner = root.findViewById(R.id.spinner);
        signup = root.findViewById(R.id.btnSignUp);


        etname.setTranslationX(800);
        etpassword.setTranslationX(800);
        etconfirmPassword.setTranslationX(800);
        tvupload.setTranslationX(800);
        tvrole.setTranslationX(800);
        imgUploadIcon.setTranslationX(800);
        spinner.setTranslationX(800);
        signup.setTranslationX(800);

        etname.setAlpha(v);
        etpassword.setAlpha(v);
        etconfirmPassword.setAlpha(v);
        tvupload.setAlpha(v);
        tvrole.setAlpha(v);
        imgUploadIcon.setAlpha(v);
        spinner.setAlpha(v);
        signup.setAlpha(v);

        etname.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        etpassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        etconfirmPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tvupload.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        imgUploadIcon.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        tvrole.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        spinner.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        signup.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1000).start();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.adminoruser, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this.getActivity());

        imgUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo_picker_intent = new Intent(Intent.ACTION_PICK);
                photo_picker_intent.setType("image/*");
                startActivityForResult(photo_picker_intent,1);


            }

        });





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString();
                String password = etpassword.getText().toString();
                String confirm_password = etconfirmPassword.getText().toString();
                Log.d("name:",name);
                Log.d("password:",password);
                Log.d("confirm_password:",confirm_password);
                Log.d("email:",email);

            }
        });

        return root;
    }




}
