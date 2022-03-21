package com.example.sihfrontend.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.monument.MonumentDescription;
import com.example.sihfrontend.utils.CountingRequestBody;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminFlagReport extends AppCompatActivity {

    ImageButton ProofSelectImage;
    ImageView ProofPreviewImage;
    ImageButton UserPhotoSelectImage;
    ImageView UserPhotoPreviewImage;
    ImageButton IDSelectImage;
    ImageView IDPreviewImage;
    Button btn_report;
    Button selectdateofvisit;
    EditText reportemailid;
    ProgressBar progressBar;

    Uri ProofImageUri,UserPhotoUri,IDUri;

    private static  int READ_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flag_report);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        ProofSelectImage = findViewById(R.id.ProofSelectImage);
        ProofPreviewImage = findViewById(R.id.ProofPreviewImage);
        UserPhotoSelectImage = findViewById(R.id.UserPhotoSelectImage);
        UserPhotoPreviewImage = findViewById(R.id.UserPhotoPreviewImage);
        IDSelectImage = findViewById(R.id.IDSelectImage);
        IDPreviewImage = findViewById(R.id.IDPreviewImage);
        btn_report = findViewById(R.id.btn_report);
        selectdateofvisit = findViewById(R.id.selectdateofvisit);
        reportemailid = findViewById(R.id.reportemailid);
        progressBar = findViewById(R.id.flag_report_progress_bar);

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                uploadImage();
            }
        });


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

        selectdateofvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(AdminFlagReport.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, (month));
                            calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                            String myFormat = "dd/MM/yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                            selectdateofvisit.setText(sdf.format(calendar.getTime()));
                        }
                    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
                    calendar.add(Calendar.YEAR, 0);
                    dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()+(1000*60*60*24*7));// TODO: used to hide future date,month and year
                    dialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if(ProofImageUri ==null || IDUri==null || UserPhotoUri==null){
            try {
                Log.d("ProofImage Uri",""+ProofImageUri.toString());
                Log.d("ID Uri",""+IDUri.toString());
                Log.d("UserPhoto Uri",""+UserPhotoUri.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        final File proofImageFile = new File(uriToFilename(ProofImageUri));
        Uri proofImageUris = Uri.fromFile(proofImageFile);

        final File userPhotoUriFile = new File(uriToFilename(UserPhotoUri));
        Uri userPhotoUris = Uri.fromFile(userPhotoUriFile);

        final File IdUriFile = new File(uriToFilename(IDUri));
        Uri idUris = Uri.fromFile(IdUriFile);

        String proofImageFileExtension = MimeTypeMap.getFileExtensionFromUrl(proofImageUris.toString());
        String proofImageMime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(proofImageFileExtension.toLowerCase());
        String proofImageName = proofImageFile.getName();


        String userPhotoFileExtension = MimeTypeMap.getFileExtensionFromUrl(userPhotoUris.toString());
        String userPhotoMime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(userPhotoFileExtension.toLowerCase());
        String userPhotoName = userPhotoUriFile.getName();

        String idFileExtension = MimeTypeMap.getFileExtensionFromUrl(idUris.toString());
        String idMime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(idFileExtension.toLowerCase());
        String idName = IdUriFile.getName();
        Log.d("ProofImageName",proofImageName);
        Log.d("userPhotoName",userPhotoName);
        Log.d("idFileName",idName);
        //Log.e(TAG, imageFile.getName()+" "+mime+" "+uriToFilename(uri));

        // monument_name website monument_image monument_location admin_phone monument_poa admin_aadhar monument_type
        //
        SharedPreferences sh = AdminFlagReport.this.getSharedPreferences("Admin_Monument",MODE_PRIVATE);
        String monument_name = sh.getString("monument_name",null);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("monument_name",monument_name)
                .addFormDataPart("user_email",reportemailid.getText().toString())
                .addFormDataPart("date_of_visit",selectdateofvisit.getText().toString())
                .addFormDataPart("qr_scan", proofImageName,
                        RequestBody.create(MediaType.parse(proofImageMime), proofImageFile))
                .addFormDataPart("photo", userPhotoName,
                        RequestBody.create(MediaType.parse(userPhotoMime), userPhotoUriFile))
                .addFormDataPart("verification_photo", idName,
                        RequestBody.create(MediaType.parse(idMime), IdUriFile))
                .build();

        final CountingRequestBody.Listener progressListener;
        progressListener = new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesRead, long contentLength) {
                if (bytesRead >= contentLength) {
                    if (progressBar != null)
                        AdminFlagReport.this.runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                } else {
                    if (contentLength > 0) {
                        final int progress = (int) (((double) bytesRead / contentLength) * 100);
                        if (progressBar != null)
                            AdminFlagReport.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setProgress(progress);
                                }
                            });

                        if(progress >= 100){
                            AdminFlagReport.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
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
                .connectTimeout(1000, TimeUnit.MILLISECONDS)
                .build();
        SharedPreferences sharedPreferences = AdminFlagReport.this.getSharedPreferences("SIH",MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        Log.d("token",token);
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhQHMuYyIsImV4cCI6MTY0NzQzODcyOCwiaWF0IjoxNjQ3MzUyMzI4fQ.LSOZkqIrF_hORQH0wlNGk2a2bpWFd5e-w7T-k4qYt7E";
        if(token!=null){
            String auth = "Bearer "+token;
            Request request = new Request.Builder()
                    .url(getString(R.string.api)+"/admin/report-user")
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

                    AdminFlagReport.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("In AdminInputs", mMessage);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"User Reported Successfully",Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(AdminInputs.this,AdminBankDetails.class);
//                            startActivity(intent);
                        }
                    });
                }
            });
        }
    }

}