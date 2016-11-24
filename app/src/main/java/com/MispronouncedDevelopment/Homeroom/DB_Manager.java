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
        Log.d(TAG, "DB Constructor");
        LoadDatabase();
        runTestFunctions();
    }

    private void LoadDatabase(){
        Log.d(TAG, "DB LoadDatabase");
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

    private boolean checkDataBase(){
        Log.d(TAG, "DB checkData");
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH+DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch (SQLiteException e){
            Log.d(TAG, "error in checkDataBase: " + e.toString());
        }
        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null;
    }

    private void createDataBase() throws IOException {
        Log.d(TAG, "DB createDatabase");
        this.getWritableDatabase();
        DB_Accessor.WriteDB(myDataBase);
    }

    private void openDataBase() throws SQLiteException {
        Log.d(TAG, "DB OpenData");
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.d(TAG, "This is what we got:");
        Log.d(TAG, myDataBase.toString());
    }

    @Override
    public synchronized void close() {
        Log.d(TAG, "DB close");
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

    public void testData(){
        Log.d(TAG, "Begin test");
        Cursor res = myDataBase.query("user", null, null, null, null, null, null);
        Log.d(TAG, "From the testFunction:");
        Log.d(TAG, res.toString());
    }

    public int[] getParentIds(){
        int[] myInts = new int[5];
        return myInts;
    }

    public Cursor getLoginData() {
        Log.d(TAG, "getting login data");
        String[] columns = new String[2];
        columns[0] = "UserName";
        columns[1] = "Pin";
        Cursor res = myDataBase.query("user", columns, null, null, null, null, null);
        Log.d(TAG, "This is the res we got:");
        Log.d(TAG, res.toString());
        return res;
    }

    public boolean getIsAdmin(int UserId){
        String[] columns = new String[2];
        columns[0] = "UserId";
        columns[1] = "isAdmin";
        Cursor res = myDataBase.query("user", columns, null, null, null, null, null);
        Log.d(TAG, "This is the res we got:");
        Log.d(TAG, res.toString());
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

int testIndex = 0;

    //test functions
    public void runTestFunctions(){
        testIndex++;
        if(testIndex > 10){
            Log.d(TAG, "Quiting tests early");
            return;
        }
        Log.d(TAG, "Running test functions");
        boolean userTable = tableExists(myDataBase, "user", true);
        Log.d(TAG, "User table = " + userTable);
        boolean attendenceRecord = tableExists(myDataBase, "attendenceRecord", true);
        Log.d(TAG, "attendenceRecord table = " + attendenceRecord);
        boolean paymentRecord = tableExists(myDataBase, "paymentRecord", true);
        Log.d(TAG, "paymentRecord table = " + paymentRecord);

        runRepairs(userTable, attendenceRecord, paymentRecord);
    }

    public void runRepairs(boolean user, boolean attendence, boolean payment){
        boolean rerunTest = false;
        if(!user){
            rerunTest = true;
            Log.d(TAG, "Running user repair");
            DB_Accessor.CreateUserTable(myDataBase);
        }
        if(!attendence){
            rerunTest = true;
            Log.d(TAG, "Running attendence repair");
            DB_Accessor.CreateAttendenceTable(myDataBase);
        }
        if(!payment){
            rerunTest = true;
            Log.d(TAG, "Running payment repair");
            DB_Accessor.CreatePaymentTable(myDataBase);
        }

        if(rerunTest){
            runTestFunctions();
        }
    }

    //taken from http://stackoverflow.com/questions/3058909/how-does-one-check-if-a-table-exists-in-an-android-sqlite-database

    public boolean tableExists(SQLiteDatabase DataBase, String tableName, boolean openDb) {
        if(openDb) {
            if(DataBase == null || !DataBase.isOpen()) {
                DataBase = getWritableDatabase();
            }

            if(DataBase.isReadOnly()) {
                DataBase.close();
                DataBase = getWritableDatabase();
            }
        }

        Cursor cursor = DataBase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}




