package com.MispronouncedDevelopment.Homeroom;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AA_MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";//Use this for logging. ex: Log.d(TAG, "my message");
    DB_Manager myDB;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar;
        DrawerLayout drawer;
        NavigationView navigationView;

        myDB = new DB_Manager(this);

        FragmentManager fragmentManager = getFragmentManager();

        if (myDB.getIsAdmin()) {
            setContentView(R.layout.admin_main);
            toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
            drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
        } else {
            setContentView(R.layout.parent_main);
            toolbar = (Toolbar) findViewById(R.id.parent_toolbar);
            drawer = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.parent_nav_view);
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
        }

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        UpdateDatabase();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = myDB.getIsAdmin() ? (DrawerLayout) findViewById(R.id.admin_drawer_layout) : (DrawerLayout) findViewById(R.id.parent_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

        FragmentManager fragmentManager = getFragmentManager();

        switch (item.getItemId()) {

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
                Intent myPaymentIntent = new Intent(this, AndroidPay.class);
                startActivity(myPaymentIntent);
                this.startActivity(myPaymentIntent);
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("USERID");
                editor.putInt("USERID", -1);
                editor.apply();

                Context context = this;
                Intent myLogoutIntent = new Intent(context, AA_LoginActivity.class);
                startActivity(myLogoutIntent);
                context.startActivity(myLogoutIntent);
                break;
            default:
                Log.d(TAG, "Error in the menu switch");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(myDB.getIsAdmin() ? R.id.admin_drawer_layout : R.id.parent_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void UpdateDatabase() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url = prefs.getString("url", "Wrong!") + "DatabaseUpdate";

        Map<String, String> params = new HashMap<>();
        params.put("id", prefs.getInt("id", -1) + "");

        Log.d(TAG, "updating database from url = " + url);
        MakeRequest(url, params);
    }

    void MakeRequest(String url, Map<String, String> data) {

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

    //THIS ENTIRE FUNCTION NEEDS TO BE REWRITTEN TO UPDATE LOCAL DATABASE
    //after the face, a second function to update homevards should be written
    void Update(JSONObject response) {
        String[] ids = new String[0];
        Log.d(TAG, "got a response: " + response.toString());
        try {
            int myId = response.getInt("ID");
            myDB.setID(myId);
            myDB.setIsAdmin(myId, response.getString("IsAdmin"));
            myDB.setPin(myId, response.getInt("Pin"));
            myDB.setFirstName(myId, response.getString("FirstName"));
            myDB.setLastName(myId, response.getString("LastName"));
            myDB.setUserName(myId, response.getString("UserName"));
            myDB.setChildName(myId, response.getString("ChildName"));
            myDB.setPhone(myId, response.getInt("Phone"));
            myDB.setAddress1(myId, response.getString("Address1"));
            myDB.setAddress2(myId, response.getString("Address2"));
            myDB.setEmail(myId, response.getString("Email"));

            if(myDB.getIsAdmin()) {
                //this updates the admins records
                JSONArray parents = response.getJSONArray("parents");
                for (int i = 0; i < parents.length(); i++) {

                    JSONObject parent = parents.getJSONObject(i);
                    int ParentId = parent.getInt("ID");
                    myDB.setID(ParentId);
                    myDB.setIsAdmin(ParentId, parent.getString("IsAdmin"));
                    myDB.setPin(ParentId, parent.getInt("Pin"));
                    myDB.setFirstName(ParentId, parent.getString("FirstName"));
                    myDB.setLastName(ParentId, parent.getString("LastName"));
                    myDB.setUserName(ParentId, parent.getString("UserName"));
                    myDB.setChildName(ParentId, parent.getString("ChildName"));
                    myDB.setPhone(ParentId, parent.getInt("Phone"));
                    myDB.setAddress1(ParentId, parent.getString("Address1"));
                    myDB.setAddress2(ParentId, parent.getString("Address2"));
                    myDB.setEmail(ParentId, parent.getString("Email"));

                    JSONArray attendenceRecords = parent.getJSONArray("AttendanceRecords");

                    for (int j = 0; j < attendenceRecords.length(); j++) {
                        JSONObject attendanceRecord = attendenceRecords.getJSONObject(j);
                        int attendenceID = attendanceRecord.getInt("ID");
                        myDB.setAttendanceID(attendenceID);
                        myDB.setAttendanceParentID(attendenceID, attendanceRecord.getInt("ID"));
                        myDB.setAttendanceAmount(attendenceID, attendanceRecord.getInt("ID"));
                        myDB.setAttendanceDate(attendenceID, attendanceRecord.getInt("ID"));
                        myDB.setAttendanceIsPaid(attendenceID, attendanceRecord.getInt("ID"));
                    }

                    JSONArray paymentRecords = parent.getJSONArray("PaymentRecords");

                    for (int j = 0; j < paymentRecords.length(); j++) {
                        JSONObject paymentRecord = paymentRecords.getJSONObject(j);
                        int paymentID = paymentRecord.getInt("ID");
                        myDB.setAttendanceID(paymentID);
                        myDB.setAttendanceParentID(paymentID, paymentRecord.getInt("ID"));
                        myDB.setAttendanceAmount(paymentID, paymentRecord.getInt("ID"));
                        myDB.setAttendanceDate(paymentID, paymentRecord.getInt("ID"));
                        myDB.setAttendanceIsPaid(paymentID, paymentRecord.getInt("ID"));
                    }
                }
                UpdateCards();
            }
        } catch (JSONException e) {
            Log.d(TAG, "Error in her");
        }
    }

    void UpdateCards(){

        int[] parentIds = myDB.getParentIds();
        String[] ids = new String[parentIds.length];

        List<HomeCard> childCards = new ArrayList<>();
        final ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<>();


        for(int i =0; i < parentIds.length; i++) {
            HomeCard newCard = new HomeCard();
            newCard.setName(myDB.getParentName(parentIds[i]));
            newCard.setDate(myDB.getLastCheckin(parentIds[i]));
            newCard.setId(parentIds[i]);
            childCards.add(newCard);
            list.add(newCard.name);
            ids[i] = parentIds[i]+"";
        }

        myAdapter adapter = new myAdapter(this, ids);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        list.remove(item);
//                                adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }

        });
    }


    void ShowError(String error) {
        Log.d(TAG, "MakeRequest function: There was an error: " + error);
        //this probably occurs if there was a problem with the connection,
        //therefore we should probably try again in like 1 minute.
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AA_Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

class HomeCard{
    String name;
    int date;
    int id;

    public HomeCard(){
        name = "";
        date = 0;
        id = -1;
    }

    public void setName(String newName){
        name = newName;
    }

    public void setDate(int newDate){
        date = newDate;
    }

    public void setId(int newId){
        id = newId;
    }
}


