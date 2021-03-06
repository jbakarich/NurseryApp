package com.MispronouncedDevelopment.Homeroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * AA_LoginActivity
 * Handles automatic log in and is the default activity when the app launches.
 * NOTE: the url that is hard-coded here needs to be changed to the ip of wherever the server is being run from.
 */
public class AA_LoginActivity extends AppCompatActivity{
    String url ="http://172.24.95.132:8080/";//this is the location of wherever the server is running.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAdmin = prefs.getString("isAdmin", "False") == "True";
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("url");
        editor.putString("url", url);
        editor.apply();
        int userID = prefs.getInt("USERID", -1);
        if(userID != -1){
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

