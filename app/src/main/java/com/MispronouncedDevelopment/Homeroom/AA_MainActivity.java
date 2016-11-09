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
    int myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        myType = extras.getString("type");
        Toolbar toolbar;
        DrawerLayout drawer;
        NavigationView navigationView;
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
        //UpdateDatabase();
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
        } else if(id == R.id.Admin_CreateUser){
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_CreateUserFragment()).commit();
        } else if(id == R.id.Parent_Home){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
        } else if(id == R.id.Parent_Attendence){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_AttendenceFragment()).commit();
        } else if(id == R.id.Parent_Payment){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_PaymentFragment()).commit();
        } else if(id == R.id.Parent_Settings){
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_SettingsFragment()).commit();
        } else if(id == R.id.Parent_Logout || id == R.id.Admin_Logout){
            Log.d(TAG, "Logging out");
            SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = mySPrefs.edit();
            editor.remove("login");
            editor.remove("type");
            editor.commit();

//            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new AA_LoginFragment()).commit();
//            Context context = this;
            Intent myIntent = new Intent(this, AA_LoginActivity.class);
            myIntent.putExtra("logout", true);
            startActivity(myIntent);
            this.startActivity(myIntent);
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

    void UpdateDatabase(){
        Map<String, String> params = new HashMap<>();
        params.put("name", myName);
        params.put("type", myType);
        params.put("id", myId+"");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url = prefs.getString("url", "http://192.168.0.5/") + "DatabaseUpdate";
        Log.d(TAG, "url = " + url);
        MakeRequest(url, params);
    }

    void MakeRequest(String url, Map<String, String> data){

        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Update(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowError(error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    void Update(JSONObject response){
        Log.d(TAG, "got a response: " + response.toString());
    }

    void ShowError(String error){
        Log.d(TAG, "MakeRequest function: There was an error: " + error);
        //this probably occurs if there was a problem with the connection,
        //therefore we should probably try again in like 1 minute.
    }
}


