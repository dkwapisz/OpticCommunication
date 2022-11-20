package com.example.opticcommunication.flashligtDetection;

import android.graphics.Bitmap;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class FlashlightDetection {

    private int threshold;

    private int kernelWidth;

    private int kernelHeight;

    private int x;

    private int y;

    private int w;

    private int h;

    private double percent;

    private Size blurSize;

    private Size erodeSize;

    public Bitmap bitMapTest;

    public Mat mat;



    public FlashlightDetection(int threshold,int kernelWidth, int kernelHeight){
        bitMapTest = null;
        setThreshold(threshold);
        setKernelWidth(kernelWidth);
        setKernelHeight(kernelHeight);
        setX(0);
        setY(0);
        setW(0);
        setX(0);
        blurSize = new Size(3, 3);
        erodeSize = new Size(getKernelWidth(), getKernelHeight());
    }

    public boolean detect(Mat matOriginal){

        this.mat = matOriginal.clone();
        Imgproc.cvtColor(this.mat, this.mat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(this.mat, this.mat, blurSize, 0);
        Imgproc.threshold(this.mat, this.mat, getThreshold(),255 ,Imgproc.THRESH_BINARY);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, erodeSize);
        Imgproc.erode(this.mat, this.mat, kernel);
        Mat roi;
        if (this.x == 0 & this.y == 0 & this.w == 0 & this.h == 0){
            roi = this.mat;
        }else {
            Rect rectCrop = new Rect(this.x, this.y, this.w, this.h);
            roi = this.mat.submat(rectCrop);
        }
        Mat m = new Mat();
        bitMapTest = Bitmap.createBitmap(roi.cols(), roi.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(roi,bitMapTest);
        Core.extractChannel(roi,m,0);
        double totalNumberOfPixels = (double) roi.total();
        double numberOfWhitePixels = (double) Core.countNonZero(m);
        Point point1 = new Point(x, y);
        Point point2 = new Point(x+w, y+h);
        Scalar color = new Scalar(255, 0, 0);
        int thickness = 5;
        Imgproc.rectangle (matOriginal, point1, point2, color, thickness);
        if (numberOfWhitePixels/totalNumberOfPixels >= percent){
            return true;
        }else {
            return false;
        }
    }

    public void setRectangleParameters(int x, int y, int w, int h){
        setX(x);
        setY(y);
        setW(w);
        setH(h);
    }


    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getKernelWidth() {
        return kernelWidth;
    }

    public void setKernelWidth(int kernelWidth) {
        this.kernelWidth = kernelWidth;
    }

    public int getKernelHeight() {
        return kernelHeight;
    }

    public void setKernelHeight(int kernelHeight) {
        this.kernelHeight = kernelHeight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
