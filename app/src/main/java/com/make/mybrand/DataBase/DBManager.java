package com.make.mybrand.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.make.mybrand.Model.DownloadFavoriteItemList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DBManager {
    public static final int EXAM_TYPE = 0;
    public static final int SUBJECT_TYPE = 1;
    public static final int CITY_TYPE = 2;
    private static final String TAG = DBManager.class.getSimpleName();
    private static String DATABASE_NAME = "brandMania.db";
    public SQLiteDatabase database;
    public DatabaseHelper dbHelper;
    private String DATABASE_PATH;

    public DBManager(Context ctx) {

        DATABASE_PATH = "/data/data/" + ctx.getPackageName() + "/databases/";
        try {
            dbHelper = new DatabaseHelper(ctx);

            database = dbHelper.getWritableDatabase();
            if (database == null) {
                String path = DATABASE_PATH + DATABASE_NAME;
                database = SQLiteDatabase.openOrCreateDatabase(path, null);
            }
        } catch (Exception e) {
        }

    }


    public void insertStaticContent(String JsonData, int flag) {
        Gson gson = new Gson();
        ContentValues contentValue = new ContentValues();
        //contentValue.put(DatabaseHelper.ADM_ID, columnIndex);
        contentValue.put(DatabaseHelper.IMAGE_PATH, JsonData);
        contentValue.put(DatabaseHelper.FLAG, flag);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public void deleteFromCart(String _id) {

        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + _id, null);
    }


    public ArrayList<DownloadFavoriteItemList> getAllPracticeQuestion() {

        ArrayList<DownloadFavoriteItemList> downloadFavoriteItemListsModels = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME;

        Cursor cursor = database.rawQuery(selectQuery, null);
        String[] data = null;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DownloadFavoriteItemList downloadFavoriteItemList = new DownloadFavoriteItemList();
                downloadFavoriteItemList.setLayoutType(DownloadFavoriteItemList.LAYOUT_FAVOURIT);
                downloadFavoriteItemList.setId(cursor.getInt(DatabaseHelper.ID_INDEX));
                downloadFavoriteItemList.setImage(cursor.getString(DatabaseHelper.IMAGE_PATH_INDEX));
                downloadFavoriteItemList.setFlag(cursor.getInt(DatabaseHelper.FLAG_INDEX));
                downloadFavoriteItemListsModels.add(downloadFavoriteItemList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return downloadFavoriteItemListsModels;
    }

}