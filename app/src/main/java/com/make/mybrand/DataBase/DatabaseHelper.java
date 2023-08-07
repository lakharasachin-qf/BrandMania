package com.make.mybrand.DataBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Locale;
   /*
  CREATE TABLE test_master(id integer primary key, title TEXT, startDate text ,startTime TEXT, duration VARCHAR,userId VARCHAR(10), totalQuestions INTEGER, totalRightAnswers INTEGER, totalWrongAnswers INTEGER,negativeMarking FLOAT, passingMark INTEGER);
  */

/*
CREATE TABLE test_Series_details (series_details_id integer primary key AUTOINCREMENT, questionId INTEGER, question text,
 option_1 TEXT, option_2 TEXT, option_3 TEXT, option_4, rightAnswer TEXT, userGivenAnswered TEXT,questionFlag INTEGER,testId INTEGER, FOREIGN KEY(testId) REFERENCES test_master(id));
*/
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "userActivities";

    public static final int FLAG_DOWNLOAD=0;
    public static final int FLAG_FAVORITE=1;


    // Table columns
    public static final String ID = "id";
    public static final String IMAGE_PATH = "imagePath";
    public static final String FLAG = "flag";

    //all index
    public static final int ID_INDEX = 0;
    public static final int IMAGE_PATH_INDEX = 1;
    public static final int FLAG_INDEX = 2;

    // Database Information
    static final String DB_NAME = "brandMania.db";

    // database version
    static final int DB_VERSION = 1;
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    Context mcontext;
    private String DATABASE_PATH;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mcontext = context;
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        createDB();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }

    public void createDB() {
        boolean dbExist = DbExists();
        if (!dbExist) {
            this.getReadableDatabase();
            copyDataBase();
        } else {
        }
    }

    /*private void alterDataBase(){
        SQLiteDatabase db = this.getWritableDatabase();

        String jfu = "ALTER TABLE " + TABLE_NAME + " RENAME COLUMN " + IS_JFU + " INTEGER PRIMARY KEY AUTOINCREMENT, "+DBTable.COL_KNOWLEDGEBOOKS_NAME+" TEXT, "+DBTable.COL_KNOWLEDGEBOOKS_LANUAGEID+" TEXT, "+DBTable.COL_KNOWLEDGEBOOKS_DOWNLOAD+" INTEGER, "+DBTable.COL_KNOWLEDGEBOOKS_DOWNLOADURL+" TEXT, "+DBTable.COL_KNOWLEDGEBOOKS_IMAGE+" TEXT, "+DBTable.COL_KNOWLEDGEBOOKS_MOSTVIEWS+" INTEGER, "+DBTable.COL_KNOWLEDGEBOOKS_PAYPOINTS+" TEXT )";
        db.execSQL(jfu);

        *//*String questionPapers = "CREATE TABLE IF NOT EXISTS "+DBTable.TBL_QUESTION_PAPERS+" ( "+DBTable.COL_QUESTION_PAPERS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+DBTable.COL_QUESTION_PAPERS_NAME+" TEXT, "+DBTable.COL_KNOWLEDGEBOOKS_LANUAGEID+" TEXT, "+DBTable.COL_QUESTION_PAPERS_DOWNLOAD+" INTEGER, "+DBTable.COL_QUESTION_PAPERS_DOWNLOADURL+" TEXT, "+DBTable.COL_QUESTION_PAPERS_IMAGE+" TEXT, "+DBTable.COL_QUESTION_PAPERS_MOSTVIEWS+" INTEGER, "+DBTable.COL_QUESTION_PAPERS_PAYPOINTS+" TEXT )";
        db.execSQL(questionPapers);*//*

        db.execSQL("Vacuum");
    }*/

    @SuppressWarnings("deprecation")
    private boolean DbExists() {
        SQLiteDatabase db = null;
        try {
            String databasePath = "";
            if (Build.VERSION.SDK_INT >= 4.2) {
                databasePath = mcontext.getApplicationInfo().dataDir + "/databases/" + DB_NAME;
            } else {
                databasePath = "/data/data/" + mcontext.getPackageName() + "/databases/" + DB_NAME;
            }

            File file = new File(databasePath);
            if (!file.exists())
                return false;

            //String databasePath = DATABASE_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.setVersion(DB_VERSION);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (db != null) {
            db.close();
        }

        return db != null;
    }

    private void copyDataBase() {
        InputStream iStream = null;
        OutputStream oStream = null;
        String outFilePath = DATABASE_PATH + DB_NAME;
        try {
            iStream = mcontext.getAssets().open(DB_NAME);
            oStream = new FileOutputStream(outFilePath);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = iStream.read(buffer)) > 0) {
                oStream.write(buffer, 0, length);
            }
            oStream.flush();
            oStream.close();
            iStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportDatabase() {
        File dbFile = new File(Environment.getDataDirectory() + "/data/" + mcontext.getPackageName() + "/databases/" + DB_NAME);

        File exportDir = new File(Environment.getExternalStorageDirectory() + "/Ringtones", "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, dbFile.getName());

        try {
            file.createNewFile();
            this.copyFile(dbFile, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }


}