package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.database.Cursor;
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

    //GETTERS

    public int[] getParentIds(){
        int[] myInts = new int[5];
        return myInts;
    }

    public Cursor getAllData() {
        String[] columns = new String[2];
        columns[0] = "UserName";
        columns[1] = "Pin";
        Cursor res = myDataBase.query("user", columns, null, null, null, null, null);
        Log.d(TAG, "This is the res we got:");
        Log.d(TAG, res.toString());
        return res;
    }

    public boolean getIsAdmin(int UserId){
        return true;
    }

    public int getLastCheckin(int UserId){
        return 1;
    }

    public String getParentName(int UserId){
        return "larry";
    }

    public int[] getParentIds(int UserId){
        int[] ints = new int[3];
        ints[0] = 1;
        ints[1] =2;
        ints[2] = 3;
        return ints;
    }


//    SETTERS

    public void setID(int newId){

    }
    public void setIsAdmin(int UserID, String isAdmin){

    }
    public void setPin(int UserID, int newPin){

    }
    public void setFirstName(int UserID, String newFirstName){

    }
    public void setLastName(int UserID, String newLastName){

    }
    public void setUserName(int UserID, String newUserName){

    }
    public void setChildName(int UserID, String newChildName){

    }
    public void setPhone(int UserID, int newPhone){

    }
    public void setAddress1(int UserID, String newAddress1){

    }
    public void setAddress2(int UserID, String newAddress2){

    }
    public void setEmail(int UserID, String newEmail){

    }

//    Attendance Setters
    public void setAttendanceID(int AttendenceID){

    }
    public void setAttendanceParentID(int AttendenceID, int newParentId){

    }
    public void setAttendanceAmount(int AttendenceID, int newAttendenceAmount){

    }
    public void setAttendanceDate(int AttendenceID, int newAttendenceDate){

    }
    public void setAttendanceIsPaid(int AttendenceID, int newAttendenceIsPaid){

    }

    //Payment Setters
    public void setPaymentID(int AttendenceID){

    }
    public void setPaymentParentID(int AttendenceID, int newPaymentId){

    }
    public void setPaymentAmount(int AttendenceID, int newPaymentAmount){

    }
    public void setPaymentDate(int AttendenceID, int newPaymentDate){

    }
    public void setPaymentIsPaid(int AttendenceID, int newPaymentisPaid){

    }





}




