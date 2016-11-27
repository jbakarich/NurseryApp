package com.MispronouncedDevelopment.Homeroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AA_LoginActivity extends AppCompatActivity{
    private static final String TAG = "LoginActivity";//Use this for logging. ex: Log.d(TAG, "my message");
    String url ="http://192.168.0.5:8080/";//this is the location of wherever the server is running.
   public DB_Manager myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("url");
        editor.putString("url", url);
        editor.apply();
        myDB = new DB_Manager(this);
        myDB.testData();
        Log.d(TAG, "Made it here");
        int userID = prefs.getInt("USERID", -1);
        Log.d(TAG, "USERID = " + userID);
        if(userID != -1){
            boolean isAdmin = myDB.getIsAdmin(userID);
            Log.d(TAG, "isAdmin = " + isAdmin);

            if (isAdmin) {
                setContentView(R.layout.admin_main);
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
            } else {
                setContentView(R.layout.parent_main);
                fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
            }
            Intent myIntent = new Intent(this, AA_MainActivity.class);
            startActivity(myIntent);
            this.startActivity(myIntent);
        }  else {
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new AA_LoginFragment()).commit();
            setContentView(R.layout.default_main);
        }
    }
}

