package com.example.opticcommunication.receiver;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

public class ReceiverCapture extends JavaCameraView {

    public ReceiverCapture(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPreviewFPS(double min, double max){
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewFpsRange((int)(min*1000), (int)(max*1000));
        mCamera.setParameters(params);
    }
}
