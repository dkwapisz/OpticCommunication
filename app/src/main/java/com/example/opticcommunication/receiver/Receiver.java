package com.example.opticcommunication.receiver;


import com.example.opticcommunication.decoder.DecoderRC5;
import com.example.opticcommunication.flashligtDetection.FlashlightDetection;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class Receiver {
    private Object video;

    public Receiver(Object video) {
        this.video = video;
    }

    public String getVideoMessage() {
        VideoCapture cap = new VideoCapture();
        cap.open("testWideo.mp4");
        int video_length = (int) cap.get(Videoio.CAP_PROP_FRAME_COUNT);
        int frames_per_second = (int) cap.get(Videoio.CAP_PROP_FPS);
        int frame_number = (int) cap.get(Videoio.CAP_PROP_POS_FRAMES);

        Mat frame = new Mat();
        StringBuilder bitBuffer = new StringBuilder();
        FlashlightDetection flashlightDetection = new FlashlightDetection(225,5,5);
//                            flashlightDetection.setRectangleParameters(0,0,100,100);
        flashlightDetection.setPercent(0.01);
        if (cap.isOpened())
        {
            System.out.println("Video is opened");
            System.out.println("Number of Frames: " + video_length);
            System.out.println(frames_per_second + " Frames per Second");
            System.out.println("Converting Video...");

            cap.read(frame);

            while(frame_number <= video_length)
            {
                if (flashlightDetection.detect(frame)) {
                    bitBuffer.append("1");
                } else {
                    bitBuffer.append("0");
                }
            }
            cap.release();

            System.out.println(video_length + " Frames extracted");

        }
        return DecoderRC5.decodeBitBuffer(bitBuffer.toString());
    }

}
