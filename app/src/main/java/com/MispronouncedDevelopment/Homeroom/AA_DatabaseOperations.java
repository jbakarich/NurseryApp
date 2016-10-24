package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jacob on 10/13/2016.
 */

public class AA_DatabaseOperations extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Profiles.db";
    public static final String TABLE_NAME = "profile_table";
    public static final String COL_2 = "USER_NAME";
    public static final String COL_3 = "PIN";
    public static final String COL_4 = "NAME";
    public static final String COL_5 = "TYPE";

    public AA_DatabaseOperations(Context context){

        super(context, DATABASE_NAME, null, 1);


    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table" + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_NAME TEXT, PIN TEXT, NAME TEXT, TYPE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }
}
