package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Cyrille on 11/15/16.
 */

public class DB_Manager extends SQLiteOpenHelper{
    private static final String TAG = "DB_Manager";//Use this for logging. ex: Log.d(TAG, "my message");
    private static String DB_PATH = "/data/data/com.MispronouncedDevelopment.Homeroom/databases/";
    private static String DB_NAME = "app_data.db";
    private SQLiteDatabase myDataBase;


    public DB_Manager(Context context){
        super(context, DB_NAME, null, 1);
        LoadDatabase();
    }

    private void LoadDatabase(){
        boolean dbExist = checkDataBase();
        if(!dbExist) {
            try {
                createDataBase();
            } catch (IOException ioe) {
                throw new Error("UNABLE TO CREATE DATABASE");
            }
        }

        try {
            openDataBase();
        } catch (SQLiteException sqle) {
            throw sqle;
        }
    }

    private void createDataBase() throws IOException {
        this.getReadableDatabase();
        DB_Accessor.WriteDB(myDataBase);
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

        return checkDB != null;
    }

    private void openDataBase() throws SQLiteException {
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






















}



