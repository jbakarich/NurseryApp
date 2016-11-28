package com.MispronouncedDevelopment.Homeroom;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.io.IOException;


public class AA_LoginActivity extends AppCompatActivity{
    private static final String TAG = "LoginActivity";//Use this for logging. ex: Log.d(TAG, "my message");
    AA_DatabaseImport myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = new AA_DatabaseImport(this, "app_data.db");

        try {
            myDB.createDataBase();
        } catch (IOException ioe) {
            throw new Error("UNABLE TO CREATE DATABASE");
        }

        try {
            myDB.openDataBase();

        } catch (SQLiteException sqle) {
            throw sqle;
        }

        android.app.FragmentManager fragmentManager = getFragmentManager();

        boolean loginStatus = getDefaultLoginStatus("login", this);
        String loginType =  getDefaultLoginType("type", this);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("logout")) {
            boolean logout = extras.getBoolean("logout", false);
            if (logout) {
                loginStatus = false;
                loginType = "";
            }
        }
        Log.d(TAG, "The defaults are " + loginStatus + " and " + loginType);

        String type;

        if (!loginStatus) {
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new AA_LoginFragment()).commit();
            setContentView(R.layout.default_main);
            return;
        } else if (loginType.matches("admin")) {
            setContentView(R.layout.admin_main);
            type = "admin";
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
        } else {
            setContentView(R.layout.parent_main);
            type = "parent";
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
        }
        Intent myIntent = new Intent(this, AA_MainActivity.class);
        myIntent.putExtra("type", type);
        startActivity(myIntent);
        this.startActivity(myIntent);
    }

    public static boolean getDefaultLoginStatus(String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public static String getDefaultLoginType(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(key, "admin");
    }
}

