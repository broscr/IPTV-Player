package com.broscr.iptvplayer.utils;

import android.content.Context;
import android.widget.Toast;

public class Helper {

    public static final String FILE_MIME_TYPE = "audio/x-mpegurl";
    private static final String MIME_TYPE = ".m3u";

    public static boolean checkFileType(String fileName) {
        return fileName.endsWith(MIME_TYPE);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
