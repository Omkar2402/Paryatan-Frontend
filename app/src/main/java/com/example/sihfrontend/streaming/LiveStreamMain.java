package com.example.sihfrontend.streaming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.example.sihfrontend.R;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class LiveStreamMain extends AppCompatActivity {
    private static final String APPLICATION_ID = "17LguohzblGi0utJGc2qtw";
    private static final String APPLICATION_ID1 = "nD1vom8zCP5Mdy63g2TRaQ";
    SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;
    Button mBroadcastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream_main);

        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID1, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());

        mBroadcastButton = findViewById(R.id.BroadcastButton);
        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBroadcaster.canStartBroadcasting())
                    mBroadcaster.startBroadcast();
                else
                    mBroadcaster.stopBroadcast();
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }


    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mBroadcastButton.setText(broadcastStatus == BroadcastStatus.IDLE ? "Broadcast" : "Disconnect");
            Log.i("Mybroadcastingapp", "Received status change: " + broadcastStatus);
        }
        @Override
        public void onStreamHealthUpdate(int i) {
        }
        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w("Mybroadcastingapp", "Received connection error: " + connectionError + ", " + s);
        }
        @Override
        public void onCameraError(CameraError cameraError) {
            Toast.makeText(getApplicationContext(),""+cameraError.toString(),Toast.LENGTH_LONG).show();
        }
        @Override
        public void onChatMessage(String s) {
        }
        @Override
        public void onResolutionsScanned() {
        }
        @Override
        public void onCameraPreviewStateChanged() {
        }
        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {
        }
        @Override
        public void onBroadcastIdAvailable(String s) {
        }

    };



    @Override
    public void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(LiveStreamMain.this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(LiveStreamMain.this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(LiveStreamMain.this, new String[] {Manifest.permission.CAMERA}, 1);
        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(LiveStreamMain.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

}