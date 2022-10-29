package com.example.opticcommunication.transceiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opticcommunication.R;

public class TransceiverWindow extends AppCompatActivity {

    private TransceiverRC5 transceiverRC5;
    private TextView messageTextView;
    private TextView fpsTextView;
    private Button sendButton;
    private Button clearButton;
    private ProgressBar messageProgressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_trainsceiver_view);

        initializeAttributes();
    }

    private void initializeAttributes() {
        try {
            this.transceiverRC5 = new TransceiverRC5((CameraManager) getSystemService(Context.CAMERA_SERVICE));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        messageTextView = findViewById(R.id.messageTextView);
        fpsTextView = findViewById(R.id.fpsTextView);
        sendButton = findViewById(R.id.sendButton);
        clearButton = findViewById(R.id.clearButton);
        messageProgressBar = findViewById(R.id.messageProgressBar);
        messageProgressBar.setMax(1000);

        messageTextView.setCursorVisible(true);
        messageTextView.setFocusableInTouchMode(true);
        messageTextView.requestFocus();
        messageTextView.setEnabled(true);
        fpsTextView.setCursorVisible(true);
        fpsTextView.setFocusableInTouchMode(true);
        fpsTextView.setEnabled(true);

        sendButton.setOnClickListener(view -> {
            if (isNotNullValues()) {
                String message = String.valueOf(messageTextView.getText());
                int fps = Integer.parseInt(String.valueOf(fpsTextView.getText()));
                try {
                    transceiverRC5.transmitMessage(message, fps, messageProgressBar);
                } catch (InterruptedException | CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        clearButton.setOnClickListener(view -> clearValues());
    }

    private boolean isNotNullValues() {
        return messageTextView.getText().length() > 0 &&
                Integer.parseInt(String.valueOf(fpsTextView.getText())) > 0;
    }

    private void clearValues() {
        messageTextView.setText("");
        messageProgressBar.setProgress(0);
    }
}
