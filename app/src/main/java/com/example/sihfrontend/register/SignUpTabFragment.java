package com.example.sihfrontend.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sihfrontend.R;

// Inflater: convert xml file into a view
//View Group : Adding fragment to activity parent class for all views
public class SignUpTabFragment extends Fragment {

    private EditText name;
    private EditText password;
    private EditText confirmPassword;
    private TextView upload;
    private TextView role;
    private ImageView imgUploadIcon;
    private Spinner spinner;
    private Button signup;

    float v =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        name = root.findViewById(R.id.etName);
        password = root.findViewById(R.id.etPassword);
        confirmPassword = root.findViewById(R.id.etConfirmPassword);
        upload = root.findViewById(R.id.txtUpload);
        role = root.findViewById(R.id.txtFile);
        imgUploadIcon = root.findViewById(R.id.imgUpload);
        spinner = root.findViewById(R.id.spinner);
        signup = root.findViewById(R.id.btnSignUp);


        name.setTranslationX(800);
        password.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        upload.setTranslationX(800);
        role.setTranslationX(800);
        imgUploadIcon.setTranslationX(800);
        spinner.setTranslationX(800);
        signup.setTranslationX(800);

        name.setAlpha(v);
        password.setAlpha(v);
        confirmPassword.setAlpha(v);
        upload.setAlpha(v);
        role.setAlpha(v);
        imgUploadIcon.setAlpha(v);
        spinner.setAlpha(v);
        signup.setAlpha(v);

        name.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        upload.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        imgUploadIcon.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        role.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        spinner.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        signup.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

        return root;
    }
}
