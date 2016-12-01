package com.MispronouncedDevelopment.Homeroom;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class AA_MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";//Use this for logging. ex: Log.d(TAG, "my message");
    SharedPreferences prefs;
    private String parentName = "";
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

        FragmentManager fragmentManager = getSupportFragmentManager();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String isAdmin = prefs.getString("isAdmin", "Falese");
        Log.d(TAG, isAdmin + " is what we got");
        if (isAdmin.equals("True")) {
            setContentView(R.layout.admin_main);
            toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
            drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
            fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
            GetCards();
        } else {
            setContentView(R.layout.parent_main);
            toolbar = (Toolbar) findViewById(R.id.parent_toolbar);
            drawer = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.parent_nav_view);
            fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_HomeFragment()).commit();
            GetParentHome();
        }

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = prefs.getString("isAdmin", "False") == "True" ? (DrawerLayout) findViewById(R.id.admin_drawer_layout) : (DrawerLayout) findViewById(R.id.parent_drawer_layout);
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

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
//          Admin menus
            case R.id.Admin_Home:
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_HomeFragment()).commit();
                GetCards();
                break;
//            case R.id.Admin_Attendence:
//                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_AttendenceFragment()).commit();
//                break;
//            case R.id.Admin_Payment:
//                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_PaymentFragment()).commit();
//                break;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(prefs.getString("isAdmin", "False").equals("True") ? R.id.admin_drawer_layout : R.id.parent_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void GetParentHome() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url = prefs.getString("url", "Wrong!") + "ParentHome";

        Map<String, String> params = new HashMap<>();
        params.put("name", prefs.getString("username", "josh"));

        JSONObject obj = new JSONObject(params);
        Log.d(TAG, "Trying call to: " + url);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UpdateHome(response);
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

    void UpdateHome(JSONObject response) {
        TextView lastcheckin = (TextView) findViewById(R.id.lastcheckinText);
        TextView activityNameText = (TextView) findViewById(R.id.activitiesTitleText);
        TextView activityTimeText = (TextView) findViewById(R.id.ActivityTimeText);
        int time = 0;
        int activityTime = 0;
        String activityName = "";
        try {
            time = response.getInt("lastcheckin");
            activityTime = response.getInt("time");
            activityName = response.getString("name");
        } catch (JSONException e) {
            Log.d(TAG, "err in response:" + e.toString());
        }

        activityNameText.setText(activityName);
        activityTimeText.setText(formatTime(activityTime));
        lastcheckin.setText(formatTime(time));
    }

    void GetCards() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url = prefs.getString("url", "Wrong!") + "AdminHome";

        Map<String, String> params = new HashMap<>();
        params.put("id", prefs.getInt("id", -1) + "");

        JSONObject obj = new JSONObject(params);
        Log.d(TAG, "Trying call to: " + url);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UpdateCards(response);
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

    void UpdateCards(JSONObject response) {
        List<HomeCard> childCards = new ArrayList<>();
        final ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<>();
        String[] ids;
        try {
            JSONArray parents = response.getJSONArray("children");
            ids = new String[parents.length()];
            for (int i = 0; i < parents.length(); i++) {

                JSONObject cur = parents.getJSONObject(i);

                HomeCard newCard = new HomeCard();
                newCard.setName(cur.getString("username"));
                newCard.setDate(Integer.parseInt(cur.getString("lastcheckin")));

                childCards.add(newCard);
                list.add(newCard.name);
                ids[i] = i + "";
            }

            SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = mySPrefs.edit();

            for (int i = 0; i < ids.length; i++) {
                editor.putString("id" + i + "name", childCards.get(i).name);

                String formattedDate = formatTime(childCards.get(i).date);

                editor.putString("id" + i + "date", formattedDate);

            }
            editor.apply();

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
                            view.setAlpha(1);
                        }
                    });
                }

            });
        } catch (JSONException e) {
            Log.d(TAG, "err in response:" + e.toString());
        }
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


    public static String formatTime(int cur) {
        //taken from http://stackoverflow.com/questions/17432735/convert-unix-time-stamp-to-date-in-java
        long unixSeconds = cur;
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-6")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
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


