package com.example.opticcommunication;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opticcommunication.flashligtDetection.FlashlightDetection;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {


    Button photo;
    ImageView imageView;
    Bitmap bitmap;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(OpenCVLoader.initDebug()) Log.d("LOADED","success");
        else Log.d("LOADED","err");

        setContentView(R.layout.activity_main);
        photo = findViewById(R.id.photo);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.text);

        photo.setOnClickListener(view -> {
            getPermission();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getCameraPictureActivity.launch(intent);
        });

    }

    ActivityResultLauncher<Intent> getCameraPictureActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            FlashlightDetection flashlightDetection = new FlashlightDetection(225,5,5);
//                            flashlightDetection.setRectangleParameters(0,0,100,100);
                            flashlightDetection.setPercent(0.01);
                            if (flashlightDetection.detect(bitmap)){
                                textView.setText("swiatło");
                            }else {
                                textView.setText("brak");
                            }
                            imageView.setImageBitmap(flashlightDetection.bitMapTest);
                        }
                    }
                }
            });

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