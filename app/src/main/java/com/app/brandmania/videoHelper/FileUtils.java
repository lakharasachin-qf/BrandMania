package com.app.brandmania.videoHelper;

import android.content.Context;

import java.io.File;

class FileUtils {
    private static final String FFMPEG_FILE_NAME = "ffmpeg";
    private static final String FFPROBE_FILE_NAME = "ffprobe";

    static File getFFmpeg(Context context) {
        File folder = new File(context.getApplicationInfo().nativeLibraryDir);
         System.out.println("Path   " + folder.getPath());
        System.out.println("Name   " + folder.getName());
        System.out.println("Parent   " + folder.getParent());
        return new File(folder, FFMPEG_FILE_NAME);
    }


    static File getFFprobe(Context context) {
        File folder = new File(context.getApplicationInfo().nativeLibraryDir);
        return new File(folder, FFPROBE_FILE_NAME);
    }
}