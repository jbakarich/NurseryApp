package com.MispronouncedDevelopment.Homeroom;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jacob on 9/15/2016.
 */
public class AA_LoginFragment extends Fragment {
    private static final String TAG = "LoginFrag";//Use this for logging. ex: Log.d(TAG, "my message");
    public static final String PREFS_NAME = "PrefsFile";
    public boolean loginState = false;
    View myView;
    DB_Manager myDB;
    Button loginButton;
    EditText editName, editPin;
    CheckBox myCheckbox;
    String url ="http://172.24.95.132:8080/";//this is the location of wherever the server is running.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("url");
        editor.putString("url", url);
        editor.commit();


        Log.d(TAG, "Creating loginfrag");
        Context context = getActivity();
        myView = inflater.inflate(R.layout.aa_login, container, false);
        myDB = new DB_Manager(context, "DB1.db");
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

        editName = (EditText) myView.findViewById(R.id.editUserNameText);
        editPin = (EditText) myView.findViewById(R.id.EditUserPIN);
        loginButton = (Button) myView.findViewById(R.id.loginButton);
        myCheckbox = (CheckBox) myView.findViewById(R.id.serverCheckBox);
        login();
        return myView;
    }

    public void login() {
        loginButton.setOnClickListener(
                new View.OnClickListener() {


                    public void onClick(View v) {
                        String userName = editName.getText().toString();
                        String PIN = editPin.getText().toString();
                        if(myCheckbox.isChecked()){
                            Map<String, String> params = new HashMap<>();
                            params.put("username", editName.getText().toString());
                            params.put("password", editPin.getText().toString());
                            String newUrl = url+"CheckLogin";
                            Log.d(TAG, "The url being used is: " + newUrl);
                            MakeRequest(newUrl, params);
                        } else {
                            //to be deleted in live state:
                            Context context1 = getActivity();
                            Cursor res = myDB.getAllData();
                            boolean success = false;
                            while (res.moveToNext()) {
                                if (userName.matches(res.getString(1).toLowerCase()) && PIN.matches(res.getString(2).toLowerCase())) {
                                    Toast toast = Toast.makeText(context1, "Logged in as " + userName, Toast.LENGTH_SHORT);
                                    toast.show();
                                    successfulLogin(res.getString(4));
                                    success = true;
                                    break;
                                }
                            }
                            if (!success) {
                                Toast toast = Toast.makeText(context1, "Incorrect Login or PIN", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        }
                    }

                }
        );
    }

    public void successfulLogin(String type) {

        String loginType;
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (type.equals("ADMIN")) {
            Log.d(TAG, "Admin success");
            loginType = "admin";
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new Admin_HomeFragment()).commit();
        } else {
            Log.d(TAG, "parent success");
            loginType ="parent";
            type = "parent";
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new Parent_HomeFragment()).commit();
        }

        Context context = getActivity();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putBoolean("login", true);//for login persistance
        editor.putString("type", loginType);//for login persistance
        editor.putString("url", url);
        editor.commit();

        Intent myIntent = new Intent(context, AA_MainActivity.class);
        myIntent.putExtra("type", type);
        startActivity(myIntent);
        context.startActivity(myIntent);
    }

    void MakeRequest(String url, Map<String, String> data){

        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                doLogin(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowError(error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    void doLogin(JSONObject response){
        Context context = getActivity();
        Toast toast;

        int myId;
        String isAdmin, name;
        try {
            myId = response.getInt("id");
            isAdmin = response.getString("isAdmin");
            name =  response.getString("name");
        }catch(JSONException ex) {
            toast = Toast.makeText(context, "There was an error with the data from the server.", Toast.LENGTH_SHORT);
            Log.d(TAG, "The data that came back to us was: " + response.toString());
            toast.show();
            return;//quit early
        }
        if(name.equals("invalid")){
            toast = Toast.makeText(context, "Incorrect Login or PIN", Toast.LENGTH_SHORT);
            toast.show();
            return;//quit early
        }

        toast = Toast.makeText(context, "Welcome back " + name, Toast.LENGTH_SHORT);
        toast.show();

        String loginType;

        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (isAdmin.equals("True")) {
            Log.d(TAG, "Admin success");
            loginType = "admin";
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new Admin_HomeFragment()).commit();
        } else {
            Log.d(TAG, "parent success");
            loginType ="parent";
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new Parent_HomeFragment()).commit();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("login", true);//for login persistance
        editor.putString("type", loginType);//for login persistance
        editor.putString("name", name);
        editor.putInt("id", myId);//for any queries needed

        editor.commit();

        Intent myIntent = new Intent(context, AA_MainActivity.class);
        myIntent.putExtra("type", loginType);
        startActivity(myIntent);
        context.startActivity(myIntent);
    }

    void ShowError(String error){
        Log.d(TAG, "There was an error: " + error);
    }
}