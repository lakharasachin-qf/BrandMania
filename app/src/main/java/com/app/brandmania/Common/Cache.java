package com.app.brandmania.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cache {

    public static final String TAG = Cache.class.getSimpleName();

    private static final String CHILD_DIR = "images";
    private static final String TEMP_FILE_NAME = "img";
    private static final String FILE_EXTENSION = ".png";

    private static final int COMPRESS_QUALITY = 100;
    Activity act;

    /**
     * Save image to the App cache
     *
     * @param bitmap to save to the cache
     * @param name   file name in the cache.
     *               If name is null file will be named by default {@link #TEMP_FILE_NAME}
     * @return file dir when file was saved
     */
    public File saveImgToCache(Activity act, Bitmap bitmap, @Nullable String name) {
        File cachePath = null;
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        try {
            cachePath = new File(act.getCacheDir(), CHILD_DIR);
            cachePath.mkdirs();

            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fileName + FILE_EXTENSION);
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, stream);
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "saveImgToCache error: " + bitmap, e);
        }
        return cachePath;
    }

    /**
     * Save an image to the App cache dir and return it {@link Uri}
     *
     * @param bitmap to save to the cache
     */
    public Uri saveToCacheAndGetUri(Activity act, Bitmap bitmap) {
        return saveToCacheAndGetUri(act, bitmap, null);
    }

    /**
     * Save an image to the App cache dir and return it {@link Uri}
     *
     * @param bitmap to save to the cache
     * @param name   file name in the cache.
     *               If name is null file will be named by default {@link #TEMP_FILE_NAME}
     */
    public Uri saveToCacheAndGetUri(Activity act, Bitmap bitmap, @Nullable String name) {
        File file = saveImgToCache(act, bitmap, name);
        return getImageUri(act, file, name);
    }


    /**
     * Get a file {@link Uri}
     *
     * @param name of the file
     * @return file Uri in the App cache or null if file wasn't found
     */
    @Nullable
    public Uri getUriByFileName(Activity act, String name) {
        String fileName;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        } else {
            return null;
        }

        File imagePath = new File(act.getCacheDir(), CHILD_DIR);
        File newFile = new File(imagePath, fileName + FILE_EXTENSION);
        return FileProvider.getUriForFile(act, "com.app.brandmania.fileProvider", newFile);
    }

    // Get an image Uri by name without extension from a file dir
    private Uri getImageUri(Activity act, File fileDir, @Nullable String name) {
        String fileName = TEMP_FILE_NAME;
        if (!TextUtils.isEmpty(name)) {
            fileName = name;
        }
        File newFile = new File(fileDir, fileName + FILE_EXTENSION);
        return FileProvider.getUriForFile(act, "com.app.brandmania.fileProvider", newFile);
        //act.getApplicationContext().getPackageName()
    }

    /**
     * Get Uri type by {@link Uri}
     */
    public String getContentType(Uri uri) {
        return act.getContentResolver().getType(uri);
    }
}
