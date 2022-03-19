package com.example.sihfrontend.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.sihfrontend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminHomeActivity extends AppCompatActivity {

    TextView ticketInfo;
    TextView predictedNumber;
    Button openScanner,confirm;
    int rain = 0;

    HashSet<String> holiday_dates = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        ticketInfo = findViewById(R.id.tvAHScannedTicketInfo);
        ticketInfo.setMovementMethod(new ScrollingMovementMethod());

        predictedNumber = findViewById(R.id.tvAHpredictedNumber);
        openScanner = findViewById(R.id.btnAHopenScanner);
        confirm = findViewById(R.id.btnAHconfirm);
        //rain = isRain();
        Log.d("Rain", String.valueOf(rain));

        fetchfestivals();
    }

//    public int isHoliday(){
//
//    }
//
    public void fetchfestivals() {
        try {
            OkHttpClient client = new OkHttpClient();

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
        if(city != null){
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://community-open-weather-map.p.rapidapi.com/find?q=washington%20dc&cnt=1&mode=null&lon=0&type=link%2C%20accurate&lat=0&units=imperial%2C%20metric")
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
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        JSONObject rainObj = jsonArray.getJSONObject(0).getJSONObject("rain");
                        if(rainObj == null){
                            rain = 0;
                        }
                        else{
                            rain = 1;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        return rain;
    }
}