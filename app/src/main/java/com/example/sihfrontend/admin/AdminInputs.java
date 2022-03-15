package com.example.sihfrontend.admin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sihfrontend.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class AdminInputs extends AppCompatActivity {

    ImageButton MSelectImage;

    // One Preview Image
    ImageView MPreviewImage;
    EditText inputmonumentname;
    //TextView inputmonumentimg;
    EditText inputmonumentcity;
    EditText inputaadharnumber;
    EditText inputweblink;
    ImageButton POASelectImage;
    ImageView POAPreviewImage;
    Button btn_next;

    Uri MImageUri,MPOAUri;


    private static  int READ_REQUEST_CODE;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_inputs);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();


        MSelectImage = findViewById(R.id.MSelectImage);
        MPreviewImage = findViewById(R.id.MPreviewImage);
        inputmonumentname = findViewById(R.id.inputmonumentname);
        inputmonumentcity = findViewById(R.id.inputmonumentcity);
        inputaadharnumber = findViewById(R.id.inputaadharnumber);
        inputweblink = findViewById(R.id.inputweblink);

        POAPreviewImage = findViewById(R.id.POAPreviewImage);
        POASelectImage = findViewById(R.id.POASelectImage);
        btn_next = findViewById(R.id.btn_next);

        // handle the Choose Image button to trigger
        // the image chooser function
        MSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageChooser();
                READ_REQUEST_CODE = 50;
                performFileSearch();
            }
        });

        MPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                READ_REQUEST_CODE = 100;
                performFileSearch();
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(AdminInputs.this,AdminBankDetails.class));
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
        if(requestCode == 100 && resultCode ==Activity.RESULT_OK){
            MPOAUri = null;
            if (data != null) {
                MPOAUri = data.getData();
                Log.i("Uri", "Uri: " + MPOAUri.toString());
                POAPreviewImage.setImageURI(MPOAUri);
//                upload.setVisibility(View.VISIBLE);
//                Log.d("Before upload",":");
//                uploadImage();
//                Log.d("After upload",":");
            }
        }
        else if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            MImageUri = null;
            if (data != null) {
                MImageUri = data.getData();
                Log.i("Uri", "Uri: " + MImageUri.toString());
                MPreviewImage.setImageURI(MImageUri);
//                upload.setVisibility(View.VISIBLE);
//                Log.d("Before upload",":");
//                uploadImage();
//                Log.d("After upload",":");
            }
        }
    }

    private String uriToFilename(Uri uri) {
        String path = null;

        if ((Build.VERSION.SDK_INT < 19) && (Build.VERSION.SDK_INT > 11)) {
            path = getRealPathFromURI_API11to18(this, uri);
        } else {
            path = getFilePath(this, uri);
        }

        return path;
    }

    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public String getFilePath(Context context, Uri uri) {
        //Log.e("uri", uri.getPath());
        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            //Log.e("wholeID", wholeID);
            // Split at colon, use second item in the array
            String[] splits = wholeID.split(":");
            if (splits.length == 2) {
                String id = splits[1];

                String[] column = {MediaStore.Images.Media.DATA};
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }


}
