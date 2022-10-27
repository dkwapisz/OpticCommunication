package com.example.opticcommunication.decoder;

import java.nio.charset.StandardCharsets;

public class DecoderRC5 {

    private static final int frameLength = 12;
    private DecoderRC5() {}

    public static String decodeBitBuffer(String bitBuffer) {
        String[] rc5Frames = bitBuffer.split("(?<=\\G.{" + (DecoderRC5.frameLength - 1) + "})");
        StringBuilder message = new StringBuilder();
        for (String frame : rc5Frames) {
            message.append(decodeFrame(frame));
        }
        return message.toString();
    }

    private static String decodeFrame(String frame) {
        byte characterToDecode = Byte.parseByte(frame.substring(3), 2);
        return new String(new byte[]{characterToDecode}, StandardCharsets.UTF_8);
    }
}