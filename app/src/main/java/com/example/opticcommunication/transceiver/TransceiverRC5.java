package com.example.opticcommunication.transceiver;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TransceiverRC5 {

    private final long frameDurationMilis = 48;
    private final int frameDurationNanos = 666667;
    private final int framesPerSecond = 60;
    private final String cameraId;
    private final CameraManager cameraManager;

    public TransceiverRC5(CameraManager cameraManager) throws CameraAccessException {
        this.cameraManager = cameraManager;
        this.cameraId = cameraManager.getCameraIdList()[0];
    }

    private ArrayList<String> convertStringToBits(String message) {
        byte[] buf = message.getBytes(StandardCharsets.UTF_8);
        ArrayList<String> result = new ArrayList<>();
        int counter = 0;
        for (byte b : buf) {
            String binary = Integer.toBinaryString((int) b);
            String binaryConverted = ("00000000" + binary).substring(binary.length());
            String resultMessage = appendEntryBits(binaryConverted, counter);
            result.add(resultMessage);
            counter++;
        }
        return result;
    }

    private String appendEntryBits(String message, int counter) {
        if (counter % 2 == 0) {
            message = "110" + message;
        } else {
            message = "111" + message;
        }

        return message;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public synchronized void transmitMessage(String inputMessage) throws InterruptedException, CameraAccessException {
        ArrayList<String> convertedMessage = convertStringToBits(inputMessage);

        for (String message : convertedMessage) {
            for (int i = 0; i < message.length(); i++) {
                cameraManager.setTorchMode(cameraId, message.charAt(i) == '1');

                wait(frameDurationMilis, frameDurationNanos);
            }
        }

        cameraManager.setTorchMode(cameraId, false);
    }

}

