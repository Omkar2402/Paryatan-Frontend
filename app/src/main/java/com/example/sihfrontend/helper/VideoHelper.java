package com.example.sihfrontend.helper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sihfrontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class VideoHelper extends AppCompatActivity {

    private MediaController ctlr;


    private ProgressBar progressBar;
    VideoView videoView = null;

    Context context = null;
    long totalRead = 0;
    int bytesToRead = 50 * 1024;

    private int mPlayerPosition;
    private File mBufferFile;
    private byte[] videoBytes;

    private  boolean wait = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_video_helper);

        videoView = (VideoView) findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        fetchVideo();
        while (wait){
            Log.d("In wait",".....");
        }
        try {
            if(videoBytes==null){
                Toast.makeText(getApplicationContext(),"Video not fetched",Toast.LENGTH_LONG).show();
            }else{
                ctlr = new MediaController(VideoHelper.this);
                ctlr.setMediaPlayer(videoView);
                videoView.setMediaController(ctlr);
                videoView.requestFocus();
                new GetYoutubeFile().start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void fetchVideo() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(getString(R.string.api)+"/monument/Abc6")
                .addHeader("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBnbWFpbC5jb20iLCJleHAiOjE2NDcxNzY2MjMsImlhdCI6MTY0NzA5MDIyM30.HbeVHHrnj2zUrOU2Pwd3i8VBrJTtXKIJGLPnvpMeVlg")
                .get()
                .build();

        Log.d("Before Response",request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                wait = false;
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String video = jsonObject.getString("monumentVideo");
                    Log.d("Before Video Bytes","");
                    videoBytes = Base64.decode(video,Base64.DEFAULT);
                    Log.d("Video Bytes",""+videoBytes.length);
                    progressBar.setVisibility(View.GONE);
                    wait = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    wait = false;
                    Log.d("In JSON Exception",""+e.getMessage());
                }catch (Exception e){
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

//                File bufferingDir = new File(
//                        Environment.getExternalStorageDirectory()
//                                + "/YoutubeBuff");
//                InputStream stream = getAssets().open("famous.3gp");
//                if (stream == null)
//                    throw new RuntimeException("stream is null");
//                File temp = File.createTempFile("test", "mp4");
//                System.out.println("hi");
//                temp.deleteOnExit();
//                String tempPath = temp.getAbsolutePath();

                File bufferFile = null;
                try {
                    bufferFile = File.createTempFile("test", "mp4");
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

