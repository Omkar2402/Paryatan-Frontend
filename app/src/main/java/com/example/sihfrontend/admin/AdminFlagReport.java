package com.example.sihfrontend.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.sihfrontend.R;

public class AdminFlagReport extends AppCompatActivity {

    ImageButton ProofSelectImage;
    ImageView ProofPreviewImage;
    ImageButton UserPhotoSelectImage;
    ImageView UserPhotoPreviewImage;
    ImageButton IDSelectImage;
    ImageView IDPreviewImage;
    Button btn_report;

    Uri ProofImageUri,UserPhotoUri,IDUri;

    private static  int READ_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flag_report);

        ProofSelectImage = findViewById(R.id.ProofSelectImage);
        ProofPreviewImage = findViewById(R.id.ProofPreviewImage);
        UserPhotoSelectImage = findViewById(R.id.UserPhotoSelectImage);
        UserPhotoPreviewImage = findViewById(R.id.UserPhotoPreviewImage);
        IDSelectImage = findViewById(R.id.IDSelectImage);
        IDPreviewImage = findViewById(R.id.IDPreviewImage);
        btn_report = findViewById(R.id.btn_report);


        ProofSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                READ_REQUEST_CODE=50;
                performFileSearch();
            }
        });

        UserPhotoSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                READ_REQUEST_CODE=100;
                performFileSearch();
            }
        });

        IDSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                READ_REQUEST_CODE=150;
                performFileSearch();
            }
        });



    }

    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 50 && resultCode == Activity.RESULT_OK){
            Log.d("Uploading Proof Select Image","...");
            ProofImageUri = null;
            if (data != null) {

                ProofImageUri = data.getData();
                Log.i("Uri", "Uri: " + ProofImageUri.toString());
                ProofPreviewImage.setImageURI(ProofImageUri);

            }
        }
        else if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Log.d("Uploading User photo Select Image","...");
            UserPhotoUri = null;
            if (data != null) {
                UserPhotoUri = data.getData();
                Log.i("Uri", "Uri: " + UserPhotoUri.toString());
                UserPhotoPreviewImage.setImageURI(UserPhotoUri);

            }
        }
        else if (requestCode == 150 && resultCode == Activity.RESULT_OK) {
            Log.d("Uploading ID Select Image","...");
            IDUri = null;
            if (data != null) {
                IDUri = data.getData();
                Log.i("Uri", "Uri: " + IDUri.toString());
                IDPreviewImage.setImageURI(IDUri);

            }
        }
    }
}