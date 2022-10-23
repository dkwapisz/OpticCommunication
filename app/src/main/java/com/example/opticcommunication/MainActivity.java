package com.example.opticcommunication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;

import com.example.opticcommunication.transceiver.TransceiverRC5;

public class MainActivity extends AppCompatActivity {

    private TransceiverRC5 transceiverRC5;
    private CameraManager cameraManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            transceiverRC5 = new TransceiverRC5(cameraManager);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        try {
            transceiverRC5.transmitMessage("testMessage123");
        } catch (InterruptedException | CameraAccessException e) {
            e.printStackTrace();
        }
    }
}