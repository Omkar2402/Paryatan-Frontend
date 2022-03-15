package com.example.sihfrontend.admin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sihfrontend.R;
import com.example.sihfrontend.Test_Image;
import com.example.sihfrontend.utils.CountingRequestBody;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminInputs extends AppCompatActivity {

    ImageButton MSelectImage;

    // One Preview Image
    ImageView MPreviewImage;
    EditText inputmonumentname;
    //TextView inputmonumentimg;
    EditText inputmonumentcity;
    EditText inputadminnumber;
    EditText inputaadharnumber;
    EditText inputweblink;
    ImageButton POASelectImage;
    ImageView POAPreviewImage;
    Button btn_next;
    RadioGroup radiogroup;
    RadioButton inputheritage;
    RadioButton inputmuseum;


    ProgressBar progressBar;
    Uri MImageUri,MPOAUri;

    private  String monument_type=null;
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
        inputadminnumber = findViewById(R.id.inputadminnumber);
        inputaadharnumber = findViewById(R.id.inputaadharnumber);
        inputweblink = findViewById(R.id.inputweblink);
        radiogroup = findViewById(R.id.radiogroup);
        inputheritage = findViewById(R.id.inputheritage);
        inputmuseum = findViewById(R.id.inputmuseum);
        POAPreviewImage = findViewById(R.id.POAPreviewImage);
        POASelectImage = findViewById(R.id.POASelectImage);
        btn_next = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.admin_progress_bar);

        inputheritage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monument_type = "heritage";
                inputmuseum.setChecked(false);
            }
        });

        inputmuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monument_type = "museum";
                inputheritage.setChecked(false);
            }
        });
        //Log.e(TAG, imageFile.getName()+" "+mime+" "+uriToFilename(uri));


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

        POASelectImage.setOnClickListener(new View.OnClickListener() {
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
                if(inputmonumentname.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter monument name",Toast.LENGTH_LONG).show();
                }else if(inputmonumentcity.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter monument city",Toast.LENGTH_LONG).show();
                }else if(inputweblink.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter monument website link",Toast.LENGTH_LONG).show();
                }else if(inputaadharnumber.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter correct 12 digit Aadhar number",Toast.LENGTH_LONG).show();
                }else if(POAPreviewImage.getDrawable()==null){
                    Toast.makeText(getApplicationContext(),"Please upload your Power of Attorney for verification",Toast.LENGTH_LONG).show();
                }
                else if(MPreviewImage.getDrawable()==null){
                    Toast.makeText(getApplicationContext(),"Please  upload your Monument Image for verification",Toast.LENGTH_LONG).show();
                }else if(monument_type==null){
                    Toast.makeText(getApplicationContext(),"Please select your monument type",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Correct Information",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImage();

                }
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
            Log.d("Uploading POA","...");
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
            Log.d("Uploading Monument image","...");
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

    public void uploadImage(){

        if(MImageUri == null || MPOAUri==null){
            try {
                Log.d("MImage Uri",""+MImageUri.toString());
                Log.d("MPOA Uri",""+MPOAUri.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }

        final File monumentImageFile = new File(uriToFilename(MImageUri));
        Uri monumentImageUris = Uri.fromFile(monumentImageFile);

        final File monumentPOAFile = new File(uriToFilename(MPOAUri));
        Uri monumentPOAUris = Uri.fromFile(monumentPOAFile);

        String monumentImageFileExtension = MimeTypeMap.getFileExtensionFromUrl(monumentImageUris.toString());
        String monumentImageMime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(monumentImageFileExtension.toLowerCase());
        String monumentImageName = monumentImageFile.getName();


        String monumentPOAFileExtension = MimeTypeMap.getFileExtensionFromUrl(monumentPOAUris.toString());
        String monumentPOAMime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(monumentPOAFileExtension.toLowerCase());
        String monumentPOAName = monumentPOAFile.getName();
        Log.d("MonumentImageName",monumentImageName);
        Log.d("MonumentPOAName",monumentPOAName);
        //Log.e(TAG, imageFile.getName()+" "+mime+" "+uriToFilename(uri));

        // monument_name website monument_image monument_location admin_phone monument_poa admin_aadhar monument_type
        //
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("monument_name",inputmonumentname.getText().toString())
                .addFormDataPart("website",inputweblink.getText().toString())
                .addFormDataPart("monument_location",inputmonumentcity.getText().toString())
                .addFormDataPart("admin_phone",inputadminnumber.getText().toString())
                .addFormDataPart("admin_aadhar",inputaadharnumber.getText().toString())
                .addFormDataPart("monument_type",monument_type)
                .addFormDataPart("monument_image", monumentImageName,
                        RequestBody.create(MediaType.parse(monumentImageMime), monumentImageFile))
                .addFormDataPart("monument_poa", monumentPOAName,
                        RequestBody.create(MediaType.parse(monumentPOAMime), monumentPOAFile))
                .build();

        final CountingRequestBody.Listener progressListener;
        progressListener = new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesRead, long contentLength) {
                if (bytesRead >= contentLength) {
                    if (progressBar != null)
                        AdminInputs.this.runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                } else {
                    if (contentLength > 0) {
                        final int progress = (int) (((double) bytesRead / contentLength) * 100);
                        if (progressBar != null)
                            AdminInputs.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setProgress(progress);
                                }
                            });

                        if(progress >= 100){
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.e("uploadProgress called", progress+" ");
                    }
                }
            }
        };

        OkHttpClient imageUploadClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        if (originalRequest.body() == null) {
                            return chain.proceed(originalRequest);
                        }
                        Request progressRequest = originalRequest.newBuilder()
                                .method(originalRequest.method(),
                                        new CountingRequestBody(originalRequest.body(), progressListener))
                                .build();

                        return chain.proceed(progressRequest);

                    }
                })
                .build();
        SharedPreferences sh = AdminInputs.this.getSharedPreferences("SIH",MODE_PRIVATE);
        String token = sh.getString("token",null);
        Log.d("token",token);
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhQHMuYyIsImV4cCI6MTY0NzQzODcyOCwiaWF0IjoxNjQ3MzUyMzI4fQ.LSOZkqIrF_hORQH0wlNGk2a2bpWFd5e-w7T-k4qYt7E";
        if(token!=null){
            String auth = "Bearer "+token;
            Request request = new Request.Builder()
                    .url("http://ec2-44-195-177-209.compute-1.amazonaws.com:8080/admin/verify-monument")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .addHeader("Authorization",auth)
                    .post(requestBody)
                    .build();


            imageUploadClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    //Toast.maText(ChatScreen.this, "Error uploading file", Toast.LENGTH_LONG).show();
                    Log.e("failure Response", mMessage);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String mMessage = response.body().string();

                    AdminInputs.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("In AdminInputs", mMessage);
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(AdminInputs.this,AdminBankDetails.class);
                            startActivity(intent);
                        }
                    });
                }
            });
        }

    }


}
