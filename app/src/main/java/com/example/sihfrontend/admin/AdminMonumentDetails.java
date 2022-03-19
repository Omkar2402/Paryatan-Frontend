package com.example.sihfrontend.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sihfrontend.R;
import com.example.sihfrontend.Test_Image;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

public class AdminMonumentDetails extends AppCompatActivity {

    private ImageButton VideoUpload;
    private Button buttonChoose;
    private Button buttonUpload;
    private TextView textView;
    private TextView textViewResponse;
    static final int PICK_IMAGE_REQUEST = 1;
    private EditText indian_adult,indian_child,foreign_adult,foreign_child,monument_description;
    String filePath;
    String monument_name;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Uri uri;

    ProgressBar submitprogressbar;
    Button btn_submit;

    TextView StartingTime, EndingTime, ClosingDays, inputmonumentvid;
    TimePickerDialog PickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    String selectedDays;

    ArrayList<Integer> daysList = new ArrayList<>();
    String[] DaysArray = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_monument_details);
        getSupportActionBar().hide();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();


        VideoUpload = (ImageButton) findViewById(R.id.VideoUpload);

        indian_adult = findViewById(R.id.fareindianadult);
        indian_child = findViewById(R.id.fareindianchild);
        foreign_adult = findViewById(R.id.fareforeignadult);
        foreign_child = findViewById(R.id.fareforeignchild);
        monument_description = findViewById(R.id.MonumentDescription);


        inputmonumentvid = (TextView) findViewById(R.id.inputmonumentimg);

        VideoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imageBrowse();
                videoBrowse();
            }
        });
        StartingTime = findViewById(R.id.StartingTime);
        EndingTime = findViewById(R.id.EndingTime);
        ClosingDays= findViewById(R.id.ClosingDays);
        submitprogressbar = findViewById(R.id.submitprogressbar);
        btn_submit = findViewById(R.id.btn_submit);
        //selectedDays = new boolean[DaysArray.length];
        StartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                PickerDialog = new TimePickerDialog(AdminMonumentDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        StartingTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                PickerDialog.show();
            }
        });
        EndingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                PickerDialog = new TimePickerDialog(AdminMonumentDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        EndingTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                PickerDialog.show();
            }
        });
        ClosingDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMonumentDetails.this);

                // set title
                builder.setTitle("Select Days");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setSingleChoiceItems(DaysArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedDays = DaysArray[i];
                    }
                });
//                builder.setMultiChoiceItems(DaysArray, selectedDays, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//                        // check condition
//                        if (b) {
//                            // when checkbox selected
//                            // Add position  in lang list
//                            daysList.add(i);
//                            // Sort array list
//                            Collections.sort(daysList);
//                        } else {
//                            // when checkbox unselected
//                            // Remove position from langList
//                            daysList.remove(Integer.valueOf(i));
//                        }
//                    }
//                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClosingDays.setText(selectedDays);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        selectedDays=null;
                    }
                });
                // show dialog
                builder.show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(AdminMonumentDetails.this);
                submitprogressbar.setVisibility(View.VISIBLE);
                uploadVideo();

                //addMonumentDetails();
            }
        });
    }

    private void videoBrowse() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select video using...."),20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 20 && resultCode == Activity.RESULT_OK) {
            uri = null;
            if (data != null) {
                uri = data.getData();
                Log.d("Uri: ", "" + uri.toString());
//                imagePreview.setImageURI(uri);
//                upload.setVisibility(View.VISIBLE);
//                Log.d("Before upload",":");
//                uploadImage();
//                Log.d("After upload",":");
            }
        }
    }



//    private void addMonumentDetails(){
//        SharedPreferences sh = AdminMonumentDetails.this.getSharedPreferences("SIH",MODE_PRIVATE);
//        String token = sh.getString("token",null);
//        SharedPreferences sh1 = AdminMonumentDetails.this.getSharedPreferences("Admin_Monument",MODE_PRIVATE);
//        String monument_name = sh1.getString("monument_name",null);
//        OkHttpClient client = new OkHttpClient();
//        RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("monument_name",monument_name)
//                .addFormDataPart("monument_preview", inputmonumentvid.getText().toString())
//                .addFormDataPart("opening_time",bankname.getText().toString())
//                .addFormDataPart("closing_time",accholdername.getText().toString())
//                .addFormDataPart("indian_adult",branchname.getText().toString())
//                .addFormDataPart("indian_child",branchname.getText().toString())
//                .addFormDataPart("foreign_adult",branchname.getText().toString())
//                .addFormDataPart("foreign_child",branchname.getText().toString())
//                .addFormDataPart("closed_day",branchname.getText().toString())
//                .addFormDataPart("description",branchname.getText().toString())
//                .build();
//        Request request = new Request.Builder()
//                .url("http://ec2-18-233-60-31.compute-1.amazonaws.com:8080/admin/add-monument")
//                .addHeader("Authorization","Bearer "+token)
//                .post(formBody)
//                .build();
//    }


    /*
        private void imageUpload(final String imagePath) {

            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, BASE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String message = jObj.getString("message");

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "MSG: "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            smr.addFile("file", imagePath);
            Log.d(TAG, "imageUpload: +"+imagePath);
            MyApplication.getInstance().addToRequestQueue(smr);

        }*/
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
        android.content.CursorLoader cursorLoader = new CursorLoader(
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

    private  void uploadVideo(){

        if(uri == null){
            Log.d("uri","null");
            return;
        }
        try {
            final File videoFile = new File(uriToFilename(uri));
            Uri uris = Uri.fromFile(videoFile);

//            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString());
//            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
//            String videoName = videoFile.getName();
//            Log.d("videoName",videoName);
            //Log.e(TAG, imageFile.getName()+" "+mime+" "+uriToFilename(uri));
            Log.d("Before InputStream","....");
            InputStream stream = getContentResolver().openInputStream(uri);
            Log.d("After InputStream","....");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            data = buffer.toByteArray();
            SharedPreferences sh = AdminMonumentDetails.this.getSharedPreferences("SIH",MODE_PRIVATE);
            String token = sh.getString("token",null);
            SharedPreferences sh1 = AdminMonumentDetails.this.getSharedPreferences("Admin_Monument",MODE_PRIVATE);
            String monument_name = sh1.getString("monument_name",null);

            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1000, TimeUnit.MILLISECONDS).build();
//            Log.d("Monument name","monument_name");
//            Log.d("token",token);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("monument_name","efefe")
                    .addFormDataPart("opening_time",StartingTime.getText().toString())
                    .addFormDataPart("closing_time",EndingTime.getText().toString())
                    .addFormDataPart("indian_adult",indian_adult.getText().toString())
                    .addFormDataPart("indian_child",indian_child.getText().toString())
                    .addFormDataPart("foreign_adult",foreign_adult.getText().toString())
                    .addFormDataPart("foreign_child",foreign_child.getText().toString())
                    .addFormDataPart("closed_day",selectedDays)
                    .addFormDataPart("description",monument_description.getText().toString())
                    .addFormDataPart("monument_preview", "fname",
                            RequestBody.create(MediaType.parse("video/mp4"), data))
                    .build();

            String auth = "Bearer "+"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMmNnZGZAZyIsImV4cCI6MTY0Nzc3MTE3OCwiaWF0IjoxNjQ3Njg0Nzc4fQ.ylqt6y85ukdiW2Ozp81qsGAbCMIH1b1A4Ly3-vnAuSc";
            Request request = new Request.Builder()
                    .url("http://ec2-18-233-60-31.compute-1.amazonaws.com:8080/admin/add-monument")
                    .addHeader("Authorization",auth)
                    .post(requestBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    //Toast.maText(ChatScreen.this, "Error uploading file", Toast.LENGTH_LONG).show();
                    Log.e("failure Response", mMessage);
                    AdminMonumentDetails.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("message", mMessage);
                            submitprogressbar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String mMessage = response.body().string();

                    AdminMonumentDetails.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("message", mMessage);
                            submitprogressbar.setVisibility(View.GONE);
                            startActivity(new Intent(AdminMonumentDetails.this,AdminHomeActivity.class));
                        }
                    });
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}