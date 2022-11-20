package com.example.opticcommunication.transceiver;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TransceiverRC5 extends Thread {

    private final String cameraId;
    private final CameraManager cameraManager;

    public TransceiverRC5(CameraManager cameraManager) throws CameraAccessException {
        this.cameraManager = cameraManager;
        this.cameraId = cameraManager.getCameraIdList()[0];
    }

    private long calculateBitDuration(int fps) {
        return (long) 1_000 * 1_000_000 / fps;
    }

    private int calculateProgressBarStep(int messageLength) {
        return 1000 / messageLength / 11;
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

    public void transmitMessage(String inputMessage, int fps, ProgressBar progressBar)
            throws InterruptedException, CameraAccessException {

        ArrayList<String> convertedMessage = convertStringToBits(inputMessage);

        if (convertedMessage.isEmpty()) {
            return;
        }
        int step = calculateProgressBarStep(convertedMessage.size());
        System.out.println(step);
        long frameDurationInNanos = calculateBitDuration(fps);

        for (String message : convertedMessage) {
            for (int i = 0; i < message.length(); i++) {
                try {
                    cameraManager.setTorchMode(cameraId, message.charAt(i) == '1');
                //System.out.println(message.charAt(i) == '1');
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupted!");
                    System.out.println(false);
                    return;
                }
                progressBar.setProgress(progressBar.getProgress() + step);
                waitInNanos(frameDurationInNanos);
            }
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            waitInNanos(1_000_000_000);
        }

        System.out.println(false);
        try {
            cameraManager.setTorchMode(cameraId, false);
            progressBar.setProgress(1000);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void waitInNanos(long nanos) {
        long elapsed;
        final long startTime = System.nanoTime();
        do {
            elapsed = System.nanoTime() - startTime;
        } while (elapsed < nanos);
    }
}

