package com.example.sihfrontend.user.ticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

    ConstraintLayout constraintLayout;



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
                createPdfFromView(content,date_of_visit,constraintLayout.getWidth(),constraintLayout.getHeight(),1);

            }

        });



        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Share clicked",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createPdfFromView(View view, String fileName, int pageWidth, int pageHeight, int pageNumber) {

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName.concat(".pdf"));

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()) {
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            view.draw(page.getCanvas());

            document.finishPage(page);

            try {
                Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
                document.writeTo(fOut);
            } catch (IOException e) {
                Toast.makeText(this, "Failed...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            document.close();

            /*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);*/

        } else {
            //..
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
                    .url("http://ec2-44-202-82-75.compute-1.amazonaws.com:8080/addQRticket")
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
                        Bitmap bmp = BitmapFactory.decodeByteArray(qrbytearray, 0, qrbytearray.length);
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