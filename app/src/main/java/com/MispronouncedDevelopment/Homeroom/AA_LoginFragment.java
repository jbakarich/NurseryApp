package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jacob on 9/15/2016.
 */
public class AA_LoginFragment extends Fragment {
    private static final String TAG = "LoginFrag";//Use this for logging. ex: Log.d(TAG, "my message");
    View myView;

    Button loginButton;
    EditText editName, editPin;
    CheckBox myCheckbox;

    SharedPreferences prefs;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.aa_login, container, false);

        editName = (EditText) myView.findViewById(R.id.editUserNameText);
        editPin = (EditText) myView.findViewById(R.id.EditUserPIN);
        loginButton = (Button) myView.findViewById(R.id.loginButton);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        login();
        return myView;
    }

    public void login() {

        loginButton.setOnClickListener(
                new View.OnClickListener() {


                    public void onClick(View v) {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", editName.getText().toString());
                        params.put("password", editPin.getText().toString());
                        String newUrl = prefs.getString("url", "Wrong!")+"CheckLogin";
                        MakeRequest(newUrl, params);
                    }

                }
        );
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

            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("USERID");
            editor.putInt("USERID", myId);
            editor.remove("username");
            editor.putString("username", name);
            editor.remove("isAdmin");
            editor.putString("isAdmin", isAdmin);
            editor.commit();
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

        FragmentManager fragmentManager = getFragmentManager();

        if (isAdmin.equals("True")) {
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new Admin_HomeFragment()).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.default_content_frame, new Parent_HomeFragment()).commit();
        }

        Intent myIntent = new Intent(context, AA_MainActivity.class);
        startActivity(myIntent);
        context.startActivity(myIntent);
    }

    void ShowError(String error){
        Log.d(TAG, "There was an error: " + error);
    }
}