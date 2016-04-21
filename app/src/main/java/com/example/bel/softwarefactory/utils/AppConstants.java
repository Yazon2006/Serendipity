package com.example.bel.softwarefactory.utils;

import android.media.MediaRecorder;

public class AppConstants {
    public static final String MIME_TYPE = "audio/*";
    public static final String SERVER_ADDRESS = "http://serendipity.netne.net/";
    // PARAMETERS THAT SHOULD BE TAKEN FROM SETTINGS
    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    //OUTPUT FORMAT SHOULD ALSO CHANGE THE FORMAT OF THE FILE
    public static final int OUTPUT_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;
    public static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;
    public static final String AUDIO_EXTENSION = ".3gp";
    //радиус доступности аудиозаписи
    public static final double AVAILABILITY_RADIUS = 100;
}
