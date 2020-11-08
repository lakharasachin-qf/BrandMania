package com.app.brandmania.Common;

import android.app.Activity;
import android.os.Environment;

import com.app.brandmania.R;

import java.io.File;
import java.util.Locale;

class FilesURis {

    private FilesURis() {
        // no instance
    }

    private static String getRootDirPath(Activity act) {
        return Environment.getExternalStorageDirectory() + "/" + act.getString(R.string.app_name);
    }

    public static boolean checkFileExist(Activity act, String downloadUrl) {
        if (downloadUrl.length() == 0) {
            return false;
        }
        return new File(getRootDirPath(act), downloadUrl).exists();
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

}
