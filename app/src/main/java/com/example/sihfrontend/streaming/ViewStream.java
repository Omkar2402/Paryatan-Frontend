package com.example.sihfrontend.streaming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.example.sihfrontend.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.graphics.Point;
import android.view.Display;
import android.widget.MediaController;
import android.view.MotionEvent;

public class ViewStream extends AppCompatActivity {

    SurfaceViewWithAutoAR mVideoSurface;
    TextView mPlayerStatusTextView;
    BroadcastPlayer mBroadcastPlayer;
    Display mDefaultDisplay;
    BroadcastPlayer mBroadcastPlayerObserver;
    MediaController mMediaController = null;


    final OkHttpClient mOkHttpClient = new OkHttpClient();
    View mPlayerContentView;
    private static final String APPLICATION_ID = "17LguohzblGi0utJGc2qtw";
    private  static  final String API_KEY = "npqKVJ8hgjuX3fiShUvsG";

    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        mVideoSurface = null;
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (ev.getActionMasked() == MotionEvent.ACTION_UP && mBroadcastPlayer != null && mMediaController != null) {
            PlayerState state = mBroadcastPlayer.getState();
            if (state == PlayerState.PLAYING ||
                    state == PlayerState.BUFFERING ||
                    state == PlayerState.PAUSED ||
                    state == PlayerState.COMPLETED) {
                if (mMediaController.isShowing())
                    mMediaController.hide();
                else
                    mMediaController.show();
            } else {
                mMediaController.hide();
            }
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurface = findViewById(R.id.videoSurfaceView);
        mPlayerStatusTextView.setText("Loading latest broadcast");
        getLatestResourceUri();
    }

    private void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","Bearer "+API_KEY)
                .get()
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ViewStream.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mPlayerStatusTextView!=null){
                            mPlayerStatusTextView.setText("Http Exception:"+e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {

                }catch (Exception e){
                    e.printStackTrace();
                }
                final  String uri = resourceUri;
                ViewStream.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPlayer(uri);
                    }
                });
            }
        });
    }

    private void initPlayer(String resourceUri) {
        if(resourceUri==null){
            if(mPlayerStatusTextView!=null)
                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if(mVideoSurface==null){
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        //mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();
    }

    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is officially supported since SDK 17 and said to work down to SDK 14 through reflection,
            // so it might be everything we need.
            mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(mDefaultDisplay, size);
        } catch (Exception e) {
            // fallback to approximate size.
            mDefaultDisplay.getSize(size);
        }
        return size;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
        mVideoSurface = findViewById(R.id.videoSurfaceView);
        mPlayerStatusTextView = findViewById(R.id.PlayerStatusTextView);

        mDefaultDisplay = getWindowManager().getDefaultDisplay();

//        mPlayerContentView = findViewById(R.id.PlayerContentView);
        BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
            @Override
            public void onStateChange(PlayerState playerState) {
                if(mPlayerStatusTextView!=null){
                    mPlayerStatusTextView.setText("Status:"+playerState);
                }
                if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                    if (mMediaController == null && mBroadcastPlayer != null && !mBroadcastPlayer.isTypeLive()) {
                        mMediaController = new MediaController(ViewStream.this);
                        mMediaController.setAnchorView(mPlayerContentView);
                        mMediaController.setMediaPlayer(mBroadcastPlayer);
                    }
                    if (mMediaController != null) {
                        mMediaController.setEnabled(true);
                        mMediaController.show();
                    }
                } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                    if (mMediaController != null) {
                        mMediaController.setEnabled(false);
                        mMediaController.hide();
                    }
                    mMediaController = null;
                }
            }

            @Override
            public void onBroadcastLoaded(boolean live, int width, int height) {
                Point size = getScreenSize();
                float screenAR = size.x / (float) size.y;
                float videoAR = width / (float) height;
                float arDiff = screenAR - videoAR;
                mVideoSurface.setCropToParent(Math.abs(arDiff) < 0.2);
            }
        };
    }


}