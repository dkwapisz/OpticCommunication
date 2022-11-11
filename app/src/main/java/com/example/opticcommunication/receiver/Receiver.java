package com.example.opticcommunication.receiver;


import android.util.Log;

import com.example.opticcommunication.decoder.DecoderRC5;
import com.example.opticcommunication.flashligtDetection.FlashlightDetection;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class Receiver {
    private StringBuilder decodedMessage = new StringBuilder();
    private StringBuilder bitBuffer = new StringBuilder();
    private FlashlightDetection flashlightDetection = new FlashlightDetection(234,3,3);
    private int ratio = 0;
    private List<Integer> frameList = new ArrayList<>();
    private List<Integer> frameList2 = new ArrayList<>();

    public Receiver(int fps, int cameraFramerate) {
        this.flashlightDetection.setPercent(0.05);
        this.ratio = cameraFramerate/fps;
    }

    public void decodeFrame(Mat frame) {
//        this.frameCounter++;
//        Log.d("FrameCounter", String.valueOf(this.frameCounter));
//        if (this.isReceiving) {
//            if (this.frameList.size() == 500) {
//                System.out.println(this.frameList);
//            }
//            this.frameList.add(flashlightDetection.detect(frame)?1:0);
//            return;
//        }
//        Log.d("Message", this.decodedMessage.toString());
//        Log.d("Buff", this.bitBuffer.toString());
//        if (flashlightDetection.detect(frame)) {
//            this.frameList.add(1);
//            this.bitBuffer.append("1");
//        } else {
//            if (bitBuffer.length() == 0) {
//                return;
//            }
//            this.bitBuffer.append("0");
//        }
//        if (this.bitBuffer.length() == 12) {
//            this.decodedMessage.append(DecoderRC5.decodeBitBuffer(this.bitBuffer.toString()));
//            this.bitBuffer.delete(0, 12);
//        }
    }

    public void addFrame(Mat frame) {
        this.frameList.add(flashlightDetection.detect(frame)?1:0);
    }

    public void decodeMessage() {
        while(this.frameList.size() > 0) {
            this.stripStream();
            if (this.frameList.size() < 330){
                break;
            }
            List<Integer> currFrame = new ArrayList<>(this.frameList.subList(0, 330));
//            this.frameList = new ArrayList<>(this.frameList.subList(330, this.frameList.size() - 1));
            for (int i = 0; i + 30 <= 330; i += 30) {
                this.bitBuffer.append(this.getBit(new ArrayList<>(currFrame.subList(i, i + 30))));
            }
            this.decodedMessage.append(DecoderRC5.decodeBitBuffer(this.bitBuffer.toString()));
            this.bitBuffer = new StringBuilder();
            this.frameList = new ArrayList<>(this.frameList.subList(330, this.frameList.size()));
        }
        this.decodedMessage.append(DecoderRC5.decodeBitBuffer(this.bitBuffer.toString()));
//        for(int i = 0; i + 30 < this.frameList.size(); i += 30){
//            this.bitBuffer.append(this.getBit(this.frameList.subList(i,i+30)));
//        }
//        this.decodedMessage.append(DecoderRC5.decodeBitBuffer(this.bitBuffer.toString()));
    }

    private void stripStream() {
        if (this.frameList.size() == 0){
            return;
        }
        this.frameList = new ArrayList<>(this.frameList.subList(this.frameList.indexOf(0), this.frameList.size()));
        if (this.frameList.indexOf(1) == -1){
            return;
        }
        this.frameList = new ArrayList<>(this.frameList.subList(this.frameList.indexOf(1), this.frameList.size()));
    }

    private String getBit(List<Integer> frames) {
        long numberOfZeros = frames.stream().filter(frame -> frame == 0).count();
        if (numberOfZeros > 15) {
            frameList2.add(0);
            return "0";
        }
        frameList2.add(1);
        return "1";
    }

    public String getMessage() {
        return this.decodedMessage.toString();
    }

}
