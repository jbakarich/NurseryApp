package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cyrille on 11/15/16.
 */

public class DB_Accessor extends SQLiteOpenHelper {
    //Database Generation
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String CreateUserTable =
            "CREATE TABLE " + DB_Models.User.tableName + " (" +
                    DB_Models.User.id + " INTEGER PRIMARY KEY," +
                    DB_Models.User.username + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.pin + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.isAdmin + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.firstName + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.lastName + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.childName + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.phone + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.address1 + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.address2 + TEXT_TYPE + COMMA_SEP +
                    DB_Models.User.email + TEXT_TYPE + " )";

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
    private static final String DATABASE_NAME = "Homeroom.db";

    public DB_Accessor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        WriteDB(db);
    }

    static void WriteDB(SQLiteDatabase db) {
        db.execSQL(CreateUserTable);
        db.execSQL(CreatePaymentTable);
        db.execSQL(CreateAttendanceTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DeleteUserEntries);
        db.execSQL(DeletePaymentEntries);
        db.execSQL(DeleteAttendanceEntries);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
