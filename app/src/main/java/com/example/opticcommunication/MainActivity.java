package com.example.opticcommunication;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opticcommunication.flashligtDetection.FlashlightDetection;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

<<<<<<< Updated upstream
import com.example.opticcommunication.transceiver.TransceiverRC5;

public class MainActivity extends AppCompatActivity {
=======
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
>>>>>>> Stashed changes


public class MainActivity extends CameraActivity {

    ArrayList<Boolean> bites = new ArrayList<Boolean>();
    JavaCameraView javaCameraView;

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(javaCameraView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(OpenCVLoader.initDebug()) Log.d("LOADED","success");
        else Log.d("LOADED","err");
        FlashlightDetection flashlightDetection = new FlashlightDetection(234,3,3);
        flashlightDetection.setPercent(0.1);

        setContentView(R.layout.activity_main);
        javaCameraView = findViewById(R.id.JavaCameraView);

        javaCameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                Mat mat = inputFrame.rgba();
                if (!mat.empty()){
                    bites.add(flashlightDetection.detect(inputFrame.rgba()));
                }
//                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
                return mat;
            }
        });
//        photo.setOnClickListener(view -> {
//            getPermission();
//            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//            getCameraPictureActivity.launch(intent);
//        });
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

    void getPermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==102 && grantResults.length > 0){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                getPermission();
            }

        }
    }
}