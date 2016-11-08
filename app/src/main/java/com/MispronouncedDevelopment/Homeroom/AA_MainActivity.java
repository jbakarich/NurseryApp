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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


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
        Log.d(TAG, myType);
        Toolbar toolbar;
        DrawerLayout drawer;
        NavigationView navigationView;
        if(myType.equals("admin")){
            Log.d(TAG, "making admin");
            setContentView(R.layout.admin_main);
             toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
             drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
             navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        } else {
            Log.d(TAG, "making parent");
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
        UpdateData();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.Admin_Home) {
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
        } else if(id == R.id.Admin_Attendence){
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_AttendenceFragment()).commit();
        } else if(id == R.id.Admin_Payment){
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_PaymentFragment()).commit();
        } else if(id == R.id.Admin_Settings){
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_SettingsFragment()).commit();
        } else if(id == R.id.Parent_Home){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
        } else if(id == R.id.Parent_Attendence){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_AttendenceFragment()).commit();
        } else if(id == R.id.Parent_Payment){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_PaymentFragment()).commit();
        } else if(id == R.id.Parent_Settings){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_SettingsFragment()).commit();
        } else if(id == R.id.Parent_Logout || id == R.id.Admin_Logout){


            SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = mySPrefs.edit();
            editor.remove("login");
            editor.apply();

            Log.d(TAG, "logging out");
            Context context = this;
            Intent myIntent = new Intent(context, AA_LoginActivity.class);
            startActivity(myIntent);
            context.startActivity(myIntent);
        }

        DrawerLayout drawer;
        if(myType.equals("admin")){
             drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        } else {
             drawer = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void UpdateData(){

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("type", myType);
        postParam.put("name", myName);

        JSONObject obj = new JSONObject(postParam);

        // Instantiate the RequestQueue.

        String url ="http://192.168.0.5:8080/testfunction";//this is the location of wherever the server is running.

    Log.d(TAG, "Trying here");
        Log.d(TAG, obj.toString());
        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Trying response");
                        Log.d(TAG, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Trying error");
                        Log.d(TAG, error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("user", "something");
                params.put("pass", "else");


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}


