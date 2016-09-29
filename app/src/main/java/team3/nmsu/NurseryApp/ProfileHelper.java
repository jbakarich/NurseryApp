package team3.nmsu.NurseryApp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jacob on 9/17/2016.
 */
public class ProfileHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Profiles.db";
    public static final String TABLE_NAME = "profile_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USER_NAME";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "CHILD_NAME";
    public static final String COL_5 = "TYPE";


    public ProfileHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE "+ "profiles" + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_NAME TEXT, PASSWORD TEXT, CHILD_NAME TEXT, TYPE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);

    }
}
