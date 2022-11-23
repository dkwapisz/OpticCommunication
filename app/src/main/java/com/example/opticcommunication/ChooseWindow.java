package com.example.opticcommunication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opticcommunication.transceiver.TransceiverRC5;
import com.example.opticcommunication.transceiver.TransceiverWindow;

public class ChooseWindow extends AppCompatActivity {

    private Button receiverButton;
    private Button transceiverButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_type);
        receiverButton = findViewById(R.id.receiver);
        transceiverButton = findViewById(R.id.transceiver);

        receiverButton.setOnClickListener(view -> {
            startActivity(new Intent(ChooseWindow.this, MainActivity.class));
        });

        transceiverButton.setOnClickListener(view -> {
            startActivity(new Intent(ChooseWindow.this, TransceiverWindow.class));
        });
    }

}
