package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Cyrille on 11/15/16.
 */

public final class DB_Models {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DB_Models() {}

    /* Inner class that defines the table contents */
    public static class User implements BaseColumns {
        public static final String tableName = "user";
        public static final String id = "_id";
        public static final String username = "username";
        public static final String pin = "_pin";
        public static final String isAdmin = "false";
        public static final String firstName = "firstName";
        public static final String lastName = "lastName";
        public static final String childName = "childName";
        public static final String phone = "_phone";
        public static final String address1 = "address1";
        public static final String address2 = "address2";
        public static final String email = "email";
    }

    /* Inner class that defines the table contents */
    public static class AttendanceRecord implements BaseColumns {
        public static final String tableName = "attendenceRecords";
        public static final int id = -1;
        public static final int userId = -1;
        public static final String date = "date";
        public static final boolean attended = false;
    }

    /* Inner class that defines the table contents */
    public static class PaymentRecord implements BaseColumns {
        public static final String tableName = "tableRecords";
        public static final int id = -1;
        public static final int userId = -1;
        public static final String date = "date";
        public static final double amount = 0.0;
        public static final boolean isPaid = false;
    }
}



