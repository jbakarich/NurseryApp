package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
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

public class Parent_SettingsFragment extends Fragment {
    View myView;
    Button submitBtn;
    EditText passwordA, passwordB;
    private String TAG = "parent settings";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.parent_settings, container, false);


        submitBtn = (Button) myView.findViewById(R.id.submitenewpassword);
        passwordA = (EditText) myView.findViewById(R.id.passwordA);
        passwordB = (EditText) myView.findViewById(R.id.passwordB);

        submitBtn.setOnClickListener(
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
                        Log.d(TAG, name);
                        params.put("name", name);
                        params.put("password", passwordA.getText().toString());

                        String url = prefs.getString("url", "Wrong again!")+"PasswordChange";
                        Log.d(TAG, "Making request to " + url);
                        MakeRequest(url, params);
                    }

                }
        );

        return myView;
    }

    void MakeRequest(String url, Map<String, String> data) {
        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast;
                Log.d(TAG, response.toString());
                if(response.has("success")) {
                    toast = Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getActivity(), "An error has occurred with something", Toast.LENGTH_SHORT);
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
