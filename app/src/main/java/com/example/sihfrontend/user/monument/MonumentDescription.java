package com.example.sihfrontend.user.monument;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sihfrontend.R;
import com.example.sihfrontend.helper.VideoHelper;
import com.example.sihfrontend.user.ticket.MonumentBookTickets;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MonumentDescription extends AppCompatActivity {

//    private VideoView videoView;
    private TextView name;
    private TextView description;
    private TextView start_time;
    private TextView close_time;
    private TextView closedOn;
    private TextView websiteLink;
    private Button bookTicket;
    private Button monLocation;
//    private ProgressBar progressBar;

    private Button selectDate;

    private double indian_adult ;
    private double indian_child;
    private double foreign_adult;
    private double foreign_child;

    private byte[] videoBytes;
    private  boolean wait = true;

    private MediaController ctlr;


    private ProgressBar progressBar;
    VideoView videoView = null;

    Context context = null;
    long totalRead = 0;
    int bytesToRead = 50 * 1024;
    private int mPlayerPosition;
    private File mBufferFile;

    private OutputStream outputStream;



    private String monument_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_description);
        videoView = findViewById(R.id.videomonumentVideo);
        name = findViewById(R.id.tvMonumentName);
        description = findViewById(R.id.tvDescription);
        start_time = findViewById(R.id.tvStartTime);
        close_time = findViewById(R.id.tvCloseTime);
        closedOn = findViewById(R.id.tvClosedOn);
        websiteLink = findViewById(R.id.tvWebsiteLink);
        bookTicket = findViewById(R.id.btnBookTicket);
        monLocation = findViewById(R.id.btnMonumentLocation);
        progressBar = findViewById(R.id.progressBarVideo);
        selectDate = findViewById(R.id.btnTicketDate);

//        try{
//            Log.d("Fare", String.valueOf(fare));
//            Toast.makeText(getApplicationContext(), (int) fare,Toast.LENGTH_LONG).show();
//
//        }catch (Exception e){
//            Log.d("Fare","....");
//            e.printStackTrace();
//
//        }

//        try{
//            String path = "android.resource://" + getPackageName() + "/" + R.raw.sample_video2;
//            videoView.setVideoURI(Uri.parse(path));
//
//            videoView.start();
//            //progressBar.setVisibility(View.VISIBLE);
//            //fetchVideo();
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        try {

            Intent intent = getIntent();

            indian_adult = intent.getDoubleExtra("indian_adult",0.0);
            Log.d("Indian_adult_MonumentDesc",""+indian_adult);
            indian_child = intent.getDoubleExtra("indian_child",0.0);
            foreign_adult = intent.getDoubleExtra("foreign_adult",0.0);
            foreign_child = intent.getDoubleExtra("foreign_child",0.0);
            monument_Name = intent.getStringExtra("monumentName");
            name.setText(intent.getStringExtra("monumentName"));
            description.setText(intent.getStringExtra("desc"));
            websiteLink.setText(intent.getStringExtra("link"));
            start_time.setText(intent.getStringExtra("start_time"));
            close_time.setText(intent.getStringExtra("close_time"));
            closedOn.setText("Closed on:"+intent.getStringExtra("closed_day"));
        }catch (Exception e){
            e.printStackTrace();
        }

        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String url = "http://"+websiteLink.getText().toString();
                    //String url = "http://www.google.com";
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(MonumentDescription.this, Uri.parse(url));


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(MonumentDescription.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, (month));
                            calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                            String myFormat = "dd/MM/yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                            selectDate.setText(sdf.format(calendar.getTime()));
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

        bookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(selectDate.getText().toString().equalsIgnoreCase("Select Date")){
                   Toast.makeText(getApplicationContext(),"Please Select date",Toast.LENGTH_LONG).show();
               }else {
                   try {
                       Log.d("Book tickets clicked","null");
                       Intent in = new Intent(MonumentDescription.this, MonumentBookTickets.class);
                       in.putExtra("monumentName",name.getText().toString());
                       in.putExtra("indian_adult",indian_adult);
                       in.putExtra("indian_child",indian_child);
                       in.putExtra("foreign_adult",foreign_adult);
                       in.putExtra("foreign_child",foreign_child);
                       in.putExtra("date_of_visit",selectDate.getText().toString());
                       startActivity(in);
                   }
                   catch (Exception e){
                       e.printStackTrace();
                   }
               }



            }
        });

        Intent intent = getIntent();
        monument_Name = intent.getStringExtra("name");
        name.setText(monument_Name);
        description.setText(intent.getStringExtra("desc"));
        websiteLink.setText(intent.getStringExtra("link"));
        start_time.setText(intent.getStringExtra("start_time"));
        close_time.setText(intent.getStringExtra("close_time"));
        closedOn.setText("Closed on:"+intent.getStringExtra("closed_day"));

        try{
            progressBar.setVisibility(View.VISIBLE);
            fetchVideo();
            while (wait){
                Log.d("In wait",".....");
            }
            try {
                if(videoBytes==null){
                    Toast.makeText(getApplicationContext(),"Video not fetched",Toast.LENGTH_LONG).show();
                }else{
                    ctlr = new MediaController(MonumentDescription.this);
                    ctlr.setMediaPlayer(videoView);
                    videoView.setMediaController(ctlr);
                    videoView.requestFocus();
                    new GetYoutubeFile().start();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fetchVideo() {

        Log.d("In fetch","fetch");
        SharedPreferences sharedPreferences = MonumentDescription.this.getSharedPreferences("SIH", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token",null);

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("http://ec2-44-202-82-75.compute-1.amazonaws.com:8080/monument/"+monument_Name)
                .addHeader("Authorization","Bearer "+token)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Log.d("In Response","response");
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String monumentVideo = jsonObject.getString("monumentVideo");
                    videoBytes = Base64.decode(monumentVideo,Base64.DEFAULT);

                    Log.d("Before Video Bytes","");
                    progressBar.setVisibility(View.GONE);
                    wait = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    wait = false;
                    Log.d("In JSON Exception",""+e.getMessage());
                }catch (Exception e){
                    wait = false;
                    e.printStackTrace();
                    Log.d("In Exception",""+e.getMessage());
                }
            }

        });

    }

    private class GetYoutubeFile extends Thread {
        private String mUrl;
        private String mFile;

        public GetYoutubeFile() {

        }

        @Override
        public void run() {
            super.run();
            try {
                File bufferFile = null;
                try {
                    bufferFile = File.createTempFile("test1", "mp4");
                    Log.d("Temp file","Written successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedOutputStream bufferOS = new BufferedOutputStream(
                        new FileOutputStream(bufferFile));


                //InputStream is = getAssets().open("famous.3gp");
                //BufferedInputStream bis = new BufferedInputStream(is, 2048);

                int numRead;
                boolean started = false;
                bufferOS.write(videoBytes);
                Log.d("buffer os","Written successfully");
                Log.e("Player", "BufferHIT:StartPlay");
                setSourceAndStartPlay(bufferFile);
                started = true;

                mBufferFile = bufferFile;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setSourceAndStartPlay(File bufferFile) {
        try {

            mPlayerPosition = videoView.getCurrentPosition();
            videoView.setVideoPath(bufferFile.getAbsolutePath());

            videoView.start();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCompletion(MediaPlayer mp) {

    }
}