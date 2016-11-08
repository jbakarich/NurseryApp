package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import java.io.IOException;


public class AA_MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";//Use this for logging. ex: Log.d(TAG, "my message");
    AA_DatabaseImport myDB;
    String myType;
    String myName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        myType = extras.getString("type");
        Toolbar toolbar;
        DrawerLayout drawer;
        NavigationView navigationView;
        Log.d(TAG, "there:   " + myType);
        if(myType.equals("admin")){
            setContentView(R.layout.admin_main);
             toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
             drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
             navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        } else {
            setContentView(R.layout.parent_main);
             toolbar = (Toolbar) findViewById(R.id.parent_toolbar);
             drawer = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
             navigationView = (NavigationView) findViewById(R.id.parent_nav_view);
        }

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        myDB = new AA_DatabaseImport(this, "app_data.db");

        try {
            myDB.createDataBase();
        } catch (IOException ioe){
            throw new Error("UNABLE TO CREATE DATABASE");
        }

        try{
            myDB.openDataBase();

        } catch(SQLiteException sqle){
            throw sqle;
        }

        android.app.FragmentManager fragmentManager = getFragmentManager();

       if(myType.equals("admin")){
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
        } else {
           fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
       }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer;
        if(myType.equals("admin")) {
             drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        } else {
             drawer = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        android.app.FragmentManager fragmentManager = getFragmentManager();

        switch (item.getItemId()){

//          Admin menus
            case R.id.Admin_Home:
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
                break;
            case R.id.Admin_Attendence:
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_AttendenceFragment()).commit();
                break;
            case R.id.Admin_Payment:
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_PaymentFragment()).commit();
                break;
            case R.id.Admin_Settings:
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_SettingsFragment()).commit();
                break;
            case R.id.Admin_CreateUser:
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_CreateUserFragment()).commit();
                break;


//            Parent menus
            case R.id.Parent_Home:
                fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
                break;
            case R.id.Parent_Attendence:
                fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_AttendenceFragment()).commit();
                break;
            case R.id.Parent_Payment:
                fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_PaymentFragment()).commit();
                break;
            case R.id.Parent_Settings:
                fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_SettingsFragment()).commit();
                break;
            case R.id.Parent_Profile:
                fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_ProfileFragment()).commit();
                break;


//          Both menus
            case R.id.Parent_Logout:
            case R.id.Admin_Logout:
                SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("login");
                editor.apply();

                Context context = this;
                Intent myIntent = new Intent(context, AA_LoginActivity.class);
                startActivity(myIntent);
                context.startActivity(myIntent);
                break;
            default:
                Log.d(TAG, "Error in the menu switch");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(myType.equals("admin") ? R.id.admin_drawer_layout : R.id.parent_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


