package com.example.sihfrontend.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sihfrontend.R;
import com.example.sihfrontend.UserProfile;
import com.example.sihfrontend.streaming.ApplicationInput;
import com.example.sihfrontend.streaming.LiveStreamMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Timeout;

public class AdminHomeActivity extends AppCompatActivity {

    TextView ticketInfo;
    TextView predictedNumber;
    Button openScanner,confirm;
    ProgressBar progressBar;
    int rain = 0;

    HashSet<String> holiday_dates = new HashSet<>();
    Calendar today = Calendar.getInstance();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id==R.id.reportuser) {
            startActivity(new Intent(AdminHomeActivity.this,AdminFlagReport.class));
        }
        else if (item_id==R.id.user_profile) {
            startActivity(new Intent(AdminHomeActivity.this, UserProfile.class));
        }else if(item_id == R.id.video_livestream){
            SharedPreferences sharedPreferences = AdminHomeActivity.this.getSharedPreferences("LiveStream",MODE_PRIVATE);
            String APPLICATION_ID = sharedPreferences.getString("APPLICATION_ID",null);
            String resource_uri = sharedPreferences.getString("RESOURCE_URI",null);
            if(APPLICATION_ID==null || resource_uri==null){
                startActivity(new Intent(AdminHomeActivity.this, ApplicationInput.class));
            }else{
                startActivity(new Intent(AdminHomeActivity.this, LiveStreamMain.class));
            }

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        ticketInfo = findViewById(R.id.tvAHScannedTicketInfo);
        progressBar = findViewById(R.id.progressbarvisitor);
        progressBar.setVisibility(View.VISIBLE);
        ticketInfo.setMovementMethod(new ScrollingMovementMethod());

        predictedNumber = findViewById(R.id.tvAHpredictedNumber);
        openScanner = findViewById(R.id.btnAHopenScanner);
        confirm = findViewById(R.id.btnAHconfirm);
        today.set(Calendar.HOUR_OF_DAY, 0);

        Log.d("Date", String.valueOf(today.getTime()));
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(today.getTime());
        Log.e("Date",formattedDate);
        rain = isRain();


    }

    private void predictVisitors() {
        try{
            int festival;
            if(holiday_dates.contains(today.getTime()))
                festival=1;
            else
                festival=0;
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).build();
            Log.d("Festival", String.valueOf(festival));
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("date", String.valueOf(today.getTime()))
                    .addFormDataPart("festival", String.valueOf(festival))
                    .addFormDataPart("rain", String.valueOf(rain))
                    .build();

            Request request = new Request.Builder()
                    .url("https://app-predictvisitors.herokuapp.com/predict")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Log.d("Response", String.valueOf(response));
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String predict_visitor = jsonObject.getString("no_of_visitors");
                        AdminHomeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                predictedNumber.setText(predict_visitor);
                                Log.d("predict",predict_visitor);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void fetchfestivals() {
        try {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).build();

            Request request = new Request.Builder()
                    .url("https://holidays-by-api-ninjas.p.rapidapi.com/v1/holidays?country=in&year=2022")
                    .get()
                    .addHeader("x-rapidapi-host", "holidays-by-api-ninjas.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "30588d3752mshf0c872044c237c8p11fc5cjsn681d618b2020")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String date = jsonObject.getString("date");
                            holiday_dates.add(date);
                        }
                        Log.d("holiday_dates_size",""+holiday_dates.size());
                        AdminHomeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                predictVisitors();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int isRain(){

        SharedPreferences sh = AdminHomeActivity.this.getSharedPreferences("Admin_Monument", MODE_PRIVATE);
        String city = sh.getString("monument_location",null);
        Log.d("city",""+city);
        city = "agra";
        if(city != null){
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).build();

            Request request = new Request.Builder()
                    .url("https://community-open-weather-map.p.rapidapi.com/find?q="+city+"&cnt=1&mode=null&lon=0&type=link%2C%20accurate&lat=0&units=imperial%2C%20metric")
                    .get()
                    .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "83d7734bd0msheded835799722b7p18c40bjsn8fd5a7c72da2")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.d("JSONObject",""+jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        Log.d("JsonArray",""+jsonArray);
                        String rainObj = jsonArray.getJSONObject(0).getString("rain");
                        Log.d("rainObj",""+rainObj.toString());
                        if(rainObj == null){
                            rain = 0;
                        }
                        else{
                            rain = 1;
                        }
                        AdminHomeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Rain", String.valueOf(rain));
                                fetchfestivals();
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        return rain;
    }
}