package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Cyrille on 11/15/16.
 */

public class DB_Accessor extends SQLiteOpenHelper {
    //Database Generation
    public static final String TAG = "DB_Accessor";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String CreateUserTable =
            "CREATE TABLE " + DB_Models.User.tableName + " ( " +
                    DB_Models.User.id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DB_Models.User.username + " TEXT, " +
                    DB_Models.User.pin + " INTEGER, " +
                    DB_Models.User.isAdmin + " TEXT, " +
                    DB_Models.User.firstName + " TEXT, " +
                    DB_Models.User.lastName + " TEXT, " +
                    DB_Models.User.childName + " TEXT, " +
                    DB_Models.User.phone + " INTEGER, " +
                    DB_Models.User.address1 + " TEXT, " +
                    DB_Models.User.address2 + " TEXT, " +
                    DB_Models.User.email + " TEXT );";

    private static final String CreateAttendanceTable =
            "CREATE TABLE " + DB_Models.AttendanceRecord.tableName + " (" +
                    DB_Models.AttendanceRecord.id + " INTEGER PRIMARY KEY," +
                    DB_Models.AttendanceRecord.userId + TEXT_TYPE + COMMA_SEP +
                    DB_Models.AttendanceRecord.date + TEXT_TYPE + COMMA_SEP +
                    DB_Models.AttendanceRecord.attended + TEXT_TYPE  + " )";

    private static final String CreatePaymentTable =
            "CREATE TABLE " + DB_Models.PaymentRecord.tableName + " (" +
                    DB_Models.PaymentRecord.id + " INTEGER PRIMARY KEY," +
                    DB_Models.PaymentRecord.userId + TEXT_TYPE + COMMA_SEP +
                    DB_Models.PaymentRecord.date + TEXT_TYPE + COMMA_SEP +
                    DB_Models.PaymentRecord.amount + TEXT_TYPE + COMMA_SEP +
                    DB_Models.PaymentRecord.isPaid + TEXT_TYPE  + " )";


    private static final String DeleteUserEntries =
            "DROP TABLE IF EXISTS " + DB_Models.User.tableName;
    private static final String DeleteAttendanceEntries =
            "DROP TABLE IF EXISTS " + DB_Models.AttendanceRecord.tableName;
    private static final String DeletePaymentEntries =
            "DROP TABLE IF EXISTS " + DB_Models.PaymentRecord.tableName;



    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "app_data.db";

    public DB_Accessor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DB Constructor");
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "DB OnCreate");
        WriteDB(db);
    }

    static void WriteDB(SQLiteDatabase db) {
        Log.d(TAG, "DB write DB");
        db.execSQL(CreateUserTable);
        db.execSQL(CreatePaymentTable);
        db.execSQL(CreateAttendanceTable);
    }

    static void CreateUserTable(SQLiteDatabase db){
        Log.d(TAG, "About to run the following sql statement:");
        Log.d(TAG, CreateUserTable);
        db.execSQL(CreateUserTable);
    }

    static void CreateAttendenceTable(SQLiteDatabase db){
        db.execSQL(CreateAttendanceTable);
    }

    static void CreatePaymentTable(SQLiteDatabase db){
        db.execSQL(CreatePaymentTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DB Upgrade");
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DeleteUserEntries);
        db.execSQL(DeletePaymentEntries);
        db.execSQL(DeleteAttendanceEntries);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DB Downgrade");
        onUpgrade(db, oldVersion, newVersion);
    }
}
