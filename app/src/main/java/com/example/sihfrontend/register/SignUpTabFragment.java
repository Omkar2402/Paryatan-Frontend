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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sihfrontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private RadioGroup radioGroup;
    private RadioButton user;
    private RadioButton admin;
    private ProgressBar progressBar;

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
        tvupload = root.findViewById(R.id.txtUpload);
        tvrole = root.findViewById(R.id.txtFile);
        imgUploadIcon = root.findViewById(R.id.imgUpload);
        radioGroup = root.findViewById(R.id.radio_group);
        user = root.findViewById(R.id.radio_user);
        admin = root.findViewById(R.id.radio_admin);
        signup = root.findViewById(R.id.btnSignUp);
        progressBar = root.findViewById(R.id.progressbar);


        etname.setTranslationX(800);
        etpassword.setTranslationX(800);
        etconfirmPassword.setTranslationX(800);
        tvupload.setTranslationX(800);
        tvrole.setTranslationX(800);
        imgUploadIcon.setTranslationX(800);
        radioGroup.setTranslationX(800);
        signup.setTranslationX(800);

        etname.setAlpha(v);
        etpassword.setAlpha(v);
        etconfirmPassword.setAlpha(v);
        tvupload.setAlpha(v);
        tvrole.setAlpha(v);
        imgUploadIcon.setAlpha(v);
        radioGroup.setAlpha(v);
        signup.setAlpha(v);

        etname.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        etpassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        etconfirmPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tvupload.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        imgUploadIcon.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
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

        imgUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent photo_picker_intent = new Intent(Intent.ACTION_PICK);
//                photo_picker_intent.setType("image/*");
//                startActivityForResult(photo_picker_intent,1);
                Toast.makeText(getContext(),"Image upload clicked",Toast.LENGTH_SHORT).show();
                Intent photo_picker_intent = new Intent(Intent.ACTION_PICK);
                photo_picker_intent.setType("image/*");
                progressBar.setVisibility(root.getRootView().VISIBLE);
                startActivityForResult(Intent.createChooser(photo_picker_intent,"Complete action using...."),1);
                String imagePath = registerActivity.getImagepath();
                if(imagePath!=null){
                    file = new File(imagePath);
                    Log.d("File Image path",imagePath);
                    progressBar.setVisibility(root.getRootView().GONE);
                }
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
                if(role==null){
                    Toast.makeText(getContext(),"Please specify your role",Toast.LENGTH_SHORT).show();

                }else if(name.isEmpty()){
                    Toast.makeText(getContext(),"Please specify your name",Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty() || confirm_password.isEmpty()){
                    Toast.makeText(getContext(),"Please specify your password",Toast.LENGTH_SHORT).show();
                }else if(!password.equals(confirm_password)){
                    Toast.makeText(getContext(),"Your password and confrm password don't match",Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        OkHttpClient client = new OkHttpClient();
                        RequestBody formBody = new MultipartBody.Builder()
                                .addFormDataPart("name",name)
                                .addFormDataPart("email",email)
                                .addFormDataPart("password",password)
                                .addFormDataPart("role",role)
                                .addFormDataPart("profile-image", String.valueOf(R.drawable.splash))
//                                .addFormDataPart("profile-image",email+".jpg",
//                                        RequestBody.create(MediaType.parse("image/*"), file))
                                .build();
                        Request request = new Request.Builder()
                                .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/register")
                                .post(formBody)
                                .build();

                        Log.d("Before Response",request.toString());

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        String message = jsonObject.getString("message");
                                        String token = jsonObject.getString("token");
                                        Log.d("message:",message);
                                        Log.d("token",token);
                                        Toast.makeText(getActivity().getApplication(), "Token:"+token,Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(),"error1",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getContext(),"error2",Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getContext(),"Registered successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }




}
