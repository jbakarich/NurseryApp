package com.MispronouncedDevelopment.Homeroom;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;

public class AA_LoginActivity extends AppCompatActivity{
    private static final String TAG = "LoginActivity";//Use this for logging. ex: Log.d(TAG, "my message");
    AA_DatabaseImport myDB;
    boolean isAdmin = false;

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
        Cursor res = myDB.getLoginStatus();
        res.moveToNext();
        String loginStatus = "0";

        if (loginStatus.matches(res.getString(1).toLowerCase())) {
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new AA_LoginFragment()).commit();//change this to a default view
            setContentView(R.layout.default_main);
        } else if (isAdmin) {
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
            setContentView(R.layout.admin_main);
        } else {
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
            setContentView(R.layout.parent_main);
        }
    }

}

