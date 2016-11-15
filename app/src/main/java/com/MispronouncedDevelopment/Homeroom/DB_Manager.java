package com.MispronouncedDevelopment.Homeroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Cyrille on 11/15/16.
 */

public class DB_Manager extends SQLiteOpenHelper{
    private static final String TAG = "DB_Manager";//Use this for logging. ex: Log.d(TAG, "my message");

    private static String DB_PATH = "/data/data/com.MispronouncedDevelopment.Homeroom/databases/";
    private static String DB_NAME = "null";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DB_Manager(Context context, String database){
        super(context, database, null, 1);
        DB_NAME = database;
        this.myContext = context;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(!dbExist){
            this.getReadableDatabase();
            try{
                copyDataBase();
            }catch (IOException e){
                throw new Error("Error Copying Database");
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH+DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            Log.d(TAG, "error in checkDataBase: " + e.toString());
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB!= null ? true : false;
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer)) > 0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLiteException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM profiles", null);
        return res;
    }

    public Cursor getLoginStatus(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users", null);
        return res;
    }

    public void updateTable(String table, String column, ContentValues values) {
        this.myDataBase.execSQL("UPDATE " + table + " SET "+ column + "=" + values + "WHERE id=0" );
    }
}




