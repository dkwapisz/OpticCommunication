package com.example.opticcommunication;


import androidx.annotation.NonNull;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import com.example.opticcommunication.flashligtDetection.FlashlightDetection;
import com.example.opticcommunication.receiver.Receiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends CameraActivity {

    JavaCameraView javaCameraView;
    boolean ifStopped;
    Button stopButton;
    Button resetButton;
    Long captureTime = null;
    TextView message;
    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(javaCameraView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Receiver receiver = new Receiver();
        OpenCVLoader.initDebug();
        setContentView(R.layout.activity_main);
        javaCameraView = findViewById(R.id.JavaCameraView);
        message = findViewById(R.id.message);
        stopButton = findViewById(R.id.stopRecButton);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(view -> {
            ifStopped = false;
            receiver.resetReceiver();
        });
        stopButton.setOnClickListener(view -> {
            ifStopped = true;
            receiver.setFrameRate(Math.round(System.nanoTime() - captureTime));
            receiver.decodeMessage();
            message.setText(receiver.getMessage());
        });
        javaCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {}

            @Override
            public void onCameraViewStopped() {}

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                if (captureTime == null) {
                    captureTime = System.nanoTime();
                }
                Mat mat = inputFrame.rgba();
                if (!ifStopped) {
                    receiver.addFrame(mat);
                }
                return mat;
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        javaCameraView.enableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        javaCameraView.disableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        javaCameraView.disableView();
    }

    void getPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102 && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                getPermission();
            }

        }
    }
}