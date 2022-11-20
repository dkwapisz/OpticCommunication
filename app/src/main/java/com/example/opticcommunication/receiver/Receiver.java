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
    private List<Integer> frameList = new ArrayList<>();
    private List<Integer> frameList2 = new ArrayList<>();
    private Integer frameRate = 0;

    public Receiver() {
        this.flashlightDetection.setPercent(0.05);
        this.flashlightDetection.setX(210);
        this.flashlightDetection.setY(100);
        this.flashlightDetection.setW(200);
        this.flashlightDetection.setH(200);
    }

    public void addFrame(Mat frame) {
        this.frameList.add(flashlightDetection.detect(frame)?1:0);
    }

    public void decodeMessage() {
        while(this.frameList.size() > 0) {
            this.stripStream();
            if (this.frameList.size() < 11*this.frameRate){
                break;
            }
            List<Integer> currFrame = new ArrayList<>(this.frameList.subList(0, 11*this.frameRate));
            for (int i = 0; i + this.frameRate <= 11*this.frameRate; i += this.frameRate) {
                this.bitBuffer.append(this.getBit(new ArrayList<>(currFrame.subList(i, i + this.frameRate))));
            }
            this.decodedMessage.append(DecoderRC5.decodeBitBuffer(this.bitBuffer.toString()));
            this.bitBuffer = new StringBuilder();
            this.frameList = new ArrayList<>(this.frameList.subList(11*this.frameRate, this.frameList.size()));
        }
        this.decodedMessage.append(DecoderRC5.decodeBitBuffer(this.bitBuffer.toString()));
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

    public void setFrameRate(int time) {
        this.frameRate = Math.round(this.frameList.size()/ (time/1000000000));
    }

    public void resetReceiver() {
        decodedMessage = new StringBuilder();
        bitBuffer = new StringBuilder();
        frameList = new ArrayList<>();
        frameList2 = new ArrayList<>();
        frameRate = 0;
    }
}
