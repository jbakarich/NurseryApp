package com.MispronouncedDevelopment.Homeroom;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;


public class AA_MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener{
        private static final String TAG = "MyActivity";
        AA_DatabaseImport myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDB = new AA_DatabaseImport(this, "app_data.db");

        try {

            myDB.createDataBase();

        }catch (IOException ioe){

            throw new Error("UNABLE TO CREATE DATABASE");
        }

        try{

            myDB.openDataBase();

        }catch(SQLiteException sqle){

            throw sqle;
        }


        android.app.FragmentManager fragmentManager = getFragmentManager();

        Cursor res = myDB.getLoginStatus();

        res.moveToNext();

        String loginStatus = "0";

        if(loginStatus.matches(res.getString(1).toLowerCase()))
        {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AA_LoginFragment()).commit();
        } else{
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Admin_HomeFragment()).commit();
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.Admin_Home) {
            Log.d(TAG, "home");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Admin_HomeFragment()).commit();
        } else if(id == R.id.Admin_Attendence){
            Log.d(TAG, "attendence");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Admin_AttendenceFragment()).commit();
        } else if(id == R.id.Admin_Payment){
            Log.d(TAG, "payment");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Admin_PaymentFragment()).commit();
        } else if(id == R.id.Admin_Settings){
            Log.d(TAG, "settings");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Admin_SettingsFragment()).commit();
        }








        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


