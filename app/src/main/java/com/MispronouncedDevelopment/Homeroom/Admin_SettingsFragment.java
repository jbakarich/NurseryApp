package com.MispronouncedDevelopment.Homeroom;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cyrille on 10/24/16.
 */

/**
 * Admin_SettingsFragment
 * Handles all settings and activity creation for admins
 */
public class Admin_SettingsFragment extends Fragment {

    View myView;
    Button passwordSubmit, activitySubmit;
    EditText passwordA, passwordB, oldPassword, activityDescription;
    TimePicker activityClock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.admin_settings, container, false);

        passwordSubmit = (Button) myView.findViewById(R.id.submitenewpassword);
        passwordA = (EditText) myView.findViewById(R.id.passwordA);
        passwordB = (EditText) myView.findViewById(R.id.passwordB);
        oldPassword = (EditText) myView.findViewById(R.id.oldPassword);

        passwordSubmit.setOnClickListener(
                new View.OnClickListener() {


                    public void onClick(View v) {
                        if(!passwordA.getText().toString().equals(passwordB.getText().toString())){
                            Toast toast = Toast.makeText(getActivity(), "Passwords don't match!", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Map<String, String> params = new HashMap<>();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String name = prefs.getString("username", "Josh");
                        params.put("name", name);
                        params.put("password", passwordA.getText().toString());
                        params.put("oldpassword", oldPassword.getText().toString());

                        String url = prefs.getString("url", "Wrong again!")+"PasswordChange";
                        MakeRequest(url, params);
                    }

                }
        );

        activitySubmit = (Button) myView.findViewById(R.id.submitnewactivity);
        activityDescription = (EditText) myView.findViewById(R.id.activity_description);
        activityClock = (TimePicker) myView.findViewById(R.id.activityTime);

        activitySubmit.setOnClickListener(
                new View.OnClickListener() {


                    public void onClick(View v) {

                        Map<String, String> params = new HashMap<>();

                        if(activityDescription.getText().toString().equals("")){
                            Toast toast = Toast.makeText(getContext(), "All fields must be entered.", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        params.put("name", activityDescription.getText().toString());
                        String time = 1480550400 + (activityClock.getHour()*3600) + (activityClock.getMinute()*60) + "";
                        params.put("time", time);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String url = prefs.getString("url", "Wrong again!")+"AddActivity";
                        SubmitActivity(url, params);
                    }

                }
        );
        return myView;
    }

    /*Sends data to server about a new activity logged*/
    void SubmitActivity(String url, Map<String, String> data){
        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast;
                if(response.has("success")) {
                    toast = Toast.makeText(getActivity(), "Activity submitted successfully", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getActivity(), "This message should never show", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getActivity(), "Error with the server", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    /*Sends new/old passwords to server to update them.*/
    void MakeRequest(String url, Map<String, String> data) {
        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast;
                if(response.has("success")) {
                    toast = Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getActivity(), "Your old password wasn't correct", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getActivity(), "An error has occurred", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}