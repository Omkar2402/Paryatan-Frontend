package com.example.sihfrontend.user.ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihfrontend.BuildConfig;
import com.example.sihfrontend.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TicketQR extends AppCompatActivity {

    private ImageView imgQR;
    private TextView tvMonumentName;
    private TextView tvDateOfVisit;
    private RecyclerView recyclerViewQR;
    private TicketQRAdapter ticketQRAdapter;
    private String monumentName;
    private  String date_of_visit;
    private ProgressBar progressBar;
    ArrayList<ticketInfo>  ticketInfoArrayList;
    private  double fare;

    private  ImageView downloadImg;
    private  ImageView shareImg;

    private Bitmap bmp,scaledBitmap;

    ConstraintLayout constraintLayout;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;


    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_qr);
        imgQR = findViewById(R.id.imgQRCode);
        tvMonumentName = findViewById(R.id.tvMonumentNameQR);
        tvDateOfVisit = findViewById(R.id.tvDateofVisit);
        recyclerViewQR = findViewById(R.id.ticketQR_recycler_view);
        progressBar = findViewById(R.id.ProgressBarQR);
        downloadImg = findViewById(R.id.imgDownloadBtn);
        shareImg = findViewById(R.id.imgShareBtn);

//
//        progressBar.setVisibility(View.VISIBLE);

        constraintLayout = findViewById(R.id.constraint_layout);

        Intent intent = getIntent();
//        Bundle args = intent.getBundleExtra("bundle");
        ticketInfoArrayList = (ArrayList<ticketInfo>) intent.getSerializableExtra("arrayList");
        monumentName = intent.getStringExtra("monumentName");
        fare = intent.getDoubleExtra("fare",0.0);
        date_of_visit = intent.getStringExtra("date_of_visit");

        Toast.makeText(getApplicationContext(),"ArrayList len:"+ticketInfoArrayList.size(),Toast.LENGTH_LONG);

        Log.d("Ticket Info arrayList",""+ticketInfoArrayList.toString());
        ticketQRAdapter = new TicketQRAdapter(TicketQR.this,ticketInfoArrayList){

        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TicketQR.this,LinearLayoutManager.VERTICAL,false);
        recyclerViewQR.setLayoutManager(linearLayoutManager);
        recyclerViewQR.setAdapter(ticketQRAdapter);

        tvMonumentName.setText(monumentName);
        tvDateOfVisit.setText(date_of_visit);

        ticketQRAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);
        fetchQRImage();


        downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Download clicked",Toast.LENGTH_LONG).show();
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = layoutInflater.inflate(R.layout.activity_ticket_qr,null);
                //createPdfFromView(content,date_of_visit,constraintLayout.getWidth(),constraintLayout.getHeight(),1);
                scaledBitmap = Bitmap.createScaledBitmap(bmp,400,400,false);
                if (checkPermission()) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermission();
                }
                generatePDF();
                openPDF();
            }

        });




        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Share clicked",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void openPDF() {

//        try {
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile()+"/"+date_of_visit+".pdf");
//            if (file.exists())
//            {
//                Intent intent=new Intent(Intent.ACTION_SENDTO);
//                Log.d("date_of_visit",date_of_visit);
//                Uri uri = Uri.fromFile(file);
//                intent.setDataAndType(uri, "application/pdf");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                try
//                {
//                    startActivity(intent);
//                }
//                catch(ActivityNotFoundException e)
//                {
//                    Toast.makeText(TicketQR.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
//                }
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),date_of_visit+".pdf");
//            Uri path = Uri.fromFile(file);
//            Log.d("path",String.valueOf(path));
//            Intent i=new Intent(Intent.ACTION_VIEW);
//            i.setDataAndType(path,"*/*");
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            TicketQR.this.startActivity(i);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledBitmap, 200, 130, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(30);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(TicketQR.this, R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("E-Ticket", 300, 100, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(TicketQR.this, R.color.black));
        title.setTextSize(30);
        canvas.drawText("Thank you for booking with us!!", 100, 540, title);
        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(TicketQR.this, R.color.purple_200));
        title.setTextSize(27);
        canvas.drawText("Wish you a very happy,smooth and hassle-free journey!!",100,580,title);
        // below line is used for setting
        // our text to center of PDF.
        //title.setTextAlign(Paint.Align.CENTER);
        /*
        int x = 10;
        int y = 550;
        for (int i = 0; i<ticketInfoArrayList.size();i++) {
            canvas.drawText(ticketInfoArrayList.get(i).getVisitorName()+"\t\t"+ticketInfoArrayList.get(i).getVerificationId()+"\t\t"+ticketInfoArrayList.get(i).getGender()+"\t\t"+ticketInfoArrayList.get(i).getAge()+"\t\t"+ticketInfoArrayList.get(i).getNationality(), x, y+30*(i+1), title);
        }*/


        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), date_of_visit+".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(TicketQR.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(TicketQR.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(TicketQR.this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TicketQR.this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private void fetchQRImage() {
        try {

            Log.d("arraylist size",""+ticketInfoArrayList.size());
            int males = 0, females = 0, indian_adult = 0, indian_child = 0, foreign_adult = 0, foreign_child = 0;

            for (int i = 0; i < ticketInfoArrayList.size(); i++) {
                if (ticketInfoArrayList.get(i).getGender().equalsIgnoreCase("male")) males++;
                else females++;
                if (ticketInfoArrayList.get(i).getAge().equalsIgnoreCase("adult") && ticketInfoArrayList.get(i).getNationality().equalsIgnoreCase("indian"))
                    indian_adult++;
                else if (ticketInfoArrayList.get(i).getAge().equalsIgnoreCase("child") && ticketInfoArrayList.get(i).getNationality().equalsIgnoreCase("indian"))
                    indian_child++;
                else if (ticketInfoArrayList.get(i).getAge().equalsIgnoreCase("adult") && ticketInfoArrayList.get(i).getNationality().equalsIgnoreCase("foreign"))
                    foreign_adult++;
                else foreign_child++;
            }


            SharedPreferences sh = TicketQR.this.getSharedPreferences("SIH", MODE_PRIVATE);
            String token = sh.getString("token", null);

            OkHttpClient client = new OkHttpClient();
            Log.d("arraylist size",""+ticketInfoArrayList.size());
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("monument_name", monumentName)
                    .addFormDataPart("date_of_visit", date_of_visit)
                    .addFormDataPart("no_of_tickets", ""+ticketInfoArrayList.size())
                    .addFormDataPart("fare", ""+fare)
                    .addFormDataPart("indian_child", ""+indian_child)
                    .addFormDataPart("foreign_child", ""+foreign_child)
                    .addFormDataPart("foreign_adult", ""+foreign_adult)
                    .addFormDataPart("males", ""+males)
                    .addFormDataPart("females", ""+females)
                    .addFormDataPart("indian_adult",""+indian_adult)
                    .build();

            Request requestBody = new Request.Builder()
                    .url("http://ec2-18-233-60-31.compute-1.amazonaws.com:8080/addQRticket")
                    .addHeader("Authorization", "Bearer " + token)
                    .post(formBody)
                    .build();

            client.newCall(requestBody).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String message = jsonObject.getString("message");
                        String qrbyte = jsonObject.getString("profile_image");
                        byte[] qrbytearray = Base64.decode(qrbyte, Base64.DEFAULT);

                        Log.d("Success:", "bytes.toString()");

                        Log.d("Length", "" + qrbytearray.length);
                        Log.d("In try", "..");
                        bmp = BitmapFactory.decodeByteArray(qrbytearray, 0, qrbytearray.length);
                        imgQR.setImageBitmap(bmp);
                        Log.d("String added successfully", message);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        //Not calculated bill yet
        //monument_name,fare,no_of_tickets,indian_adult,foreign_adult,indian_child,foreign_child,males,females,date_of_visit

    }
}