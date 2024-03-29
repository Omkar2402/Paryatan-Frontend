package com.example.sihfrontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sihfrontend.register.loginhttp;
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

public class Test_Image extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    Button selectImage, upload;
    private static final int READ_REQUEST_CODE = 42;
    ImageView imagePreview;
    Uri uri;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        selectImage = (Button)findViewById(R.id.selectImage);
        imagePreview = (ImageView)findViewById(R.id.imagePreview);
        upload = (Button)findViewById(R.id.upload);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("Before upload",":");
                uploadImage();
                Log.d("After upload",":");
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

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                imagePreview.setImageURI(uri);
                upload.setVisibility(View.VISIBLE);
                Log.d("Before upload",":");
                uploadImage();
                Log.d("After upload",":");
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

        if(uri == null){
            Log.d("uri","null");
            return;
        }

        final File imageFile = new File(uriToFilename(uri));
        Uri uris = Uri.fromFile(imageFile);

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString());
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        String imageName = imageFile.getName();
        Log.d("ImageName",imageName);
        //Log.e(TAG, imageFile.getName()+" "+mime+" "+uriToFilename(uri));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("profile-image", imageName,
                        RequestBody.create(MediaType.parse(mime), imageFile))
                .build();

        final CountingRequestBody.Listener progressListener;
        progressListener = new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesRead, long contentLength) {
                if (bytesRead >= contentLength) {
                    if (progressBar != null)
                        Test_Image.this.runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                } else {
                    if (contentLength > 0) {
                        final int progress = (int) (((double) bytesRead / contentLength) * 100);
                        if (progressBar != null)
                            Test_Image.this.runOnUiThread(new Runnable() {
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

        String auth = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlQGFxcXEiLCJleHAiOjE2NDYyNDQyMTEsImlhdCI6MTY0NjE1NzgxMX0.TbGtFhhn-ZO1slnOfLkVS4Enypzzk_fkWi8iNW9_Z50";
        Request request = new Request.Builder()
                .url(getString(R.string.api)+"/upload-image")
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

                Test_Image.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, mMessage);
                        progressBar.setVisibility(View.GONE);
                        upload.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}