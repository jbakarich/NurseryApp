package com.MispronouncedDevelopment.Homeroom;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cyrille on 11/8/16.
 */

public class Admin_ProfileFragment extends Fragment {
    View myView;
    private String TAG = "Admin profile fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.admin_profile, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String childname = prefs.getString("childname", "Josh");

        GetCards(childname);

        return myView;
    }

    void GetCards(String childname) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = prefs.getString("url", "Wrong!") + "RequestProfile";

        Map<String, String> params = new HashMap<>();
        params.put("name", childname);

        RequestProfile(url, params);
    }

    void RequestProfile(String url, Map<String, String> data) {

        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UpdateProfile(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    void UpdateProfile(JSONObject response) {
        Log.d(TAG, "got a response: " + response.toString());

        TextView firstName = (TextView) getView().findViewById(R.id.admin_profileName);
//        TextView userName = (TextView) getView().findViewById(R.id.admin_);
//        TextView lastName = (TextView) getView().findViewById(R.id.admin_l);
//        TextView childName = (TextView) getView().findViewById(R.id.admin_);
        TextView phone = (TextView) getView().findViewById(R.id.admin_profilePhone);
        TextView address1 = (TextView) getView().findViewById(R.id.admin_profileAddress1);
        TextView address2 = (TextView) getView().findViewById(R.id.admin_profileAddress2);
        TextView email = (TextView) getView().findViewById(R.id.admin_profileEmail);


        try {
            firstName.setText(response.getString("username"));
            phone.setText(response.getString("phone"));
            address1.setText(response.getString("address1"));
            address2.setText(response.getString("address2"));
            email.setText(response.getString("email"));

        } catch (JSONException e) {
            Log.d(TAG, "err in response:" + e.toString());
        }
    }
}
