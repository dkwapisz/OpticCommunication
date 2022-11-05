package com.example.opticcommunication.receiver;


import android.util.Log;

import com.example.opticcommunication.decoder.DecoderRC5;
import com.example.opticcommunication.flashligtDetection.FlashlightDetection;

import org.opencv.core.Mat;

import java.util.ArrayList;

public class Receiver {
    private StringBuilder decodedMessage = new StringBuilder();
    private StringBuilder bitBuffer = new StringBuilder();
    private FlashlightDetection flashlightDetection = new FlashlightDetection(234,3,3);
    private int ratio = 0;
    private int frameCounter = 0;
    private boolean isReceiving = true;
    private ArrayList<Integer> frameList = new ArrayList<>();

    public Receiver(int fps, int cameraFramerate) {
        this.flashlightDetection.setPercent(0.05);
        this.ratio = cameraFramerate/fps;
    }

    public void decodeFrame(Mat frame) {
        this.frameCounter++;
        Log.d("FrameCounter", String.valueOf(this.frameCounter));
        if (this.isReceiving) {
            if (this.frameList.size() == 500) {
                System.out.println(this.frameList);
            }
            this.frameList.add(flashlightDetection.detect(frame)?1:0);
            return;
        }
        Log.d("Message", this.decodedMessage.toString());
        Log.d("Buff", this.bitBuffer.toString());
        if (flashlightDetection.detect(frame)) {
            if (!this.isReceiving) {
                this.isReceiving = true;
            }
            this.frameList.add(1);
            this.bitBuffer.append("1");
        } else {
            if (bitBuffer.length() == 0) {
                return;
            }
            this.bitBuffer.append("0");
        }
        if (this.bitBuffer.length() == 12) {
            this.decodedMessage.append(DecoderRC5.decodeBitBuffer(this.bitBuffer.toString()));
            this.bitBuffer.delete(0, 12);
        }
    }

    public String getMessage() {

        return this.decodedMessage.toString();
    }

}
