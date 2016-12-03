package com.MispronouncedDevelopment.Homeroom;

import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Admin_ProfileFragment
 * Allows admin to view details about any of the parents.
 */

public class Admin_ProfileFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.admin_profile, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String childname = prefs.getString("childname", "Josh");

        GetProfile(childname);

        return myView;
    }

    void GetProfile(String childname) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = prefs.getString("url", "Wrong!") + "RequestProfile";

        Map<String, String> params = new HashMap<>();
        params.put("name", childname);

        RequestProfile(url, params);
    }

    /*Makes a request to the server for user data*/
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
                Log.d("Admin Profile", error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    /*On response from server, populates the profile page*/
    void UpdateProfile(JSONObject response) {

        TextView firstName = (TextView) getView().findViewById(R.id.admin_profileName);
        TextView phone = (TextView) getView().findViewById(R.id.admin_profilePhone);
        TextView address1 = (TextView) getView().findViewById(R.id.admin_profileAddress1);
        TextView address2 = (TextView) getView().findViewById(R.id.admin_profileAddress2);
        TextView email = (TextView) getView().findViewById(R.id.admin_profileEmail);

        TextView lastcheckin = (TextView) getView().findViewById(R.id.admin_profile_lastCheckin);
        TextView membersince = (TextView) getView().findViewById(R.id.admin_profile_memberSince);

        try {
            JSONObject parentInfo = response.getJSONObject("user");
            firstName.setText(parentInfo.getString("username"));
            phone.setText(parentInfo.getString("phone"));
            address1.setText(parentInfo.getString("address1"));
            address2.setText(parentInfo.getString("address2"));
            email.setText(parentInfo.getString("email"));
            lastcheckin.setText("Last check-in: " + AA_MainActivity.formatTime(response.getInt("lastcheckin")));
            membersince.setText("Member since: " + AA_MainActivity.formatTime(parentInfo.getInt("creationdate")));

        } catch (JSONException e) {
            Log.d("Admin Profile", "err in response:" + e.toString());
        }
    }
}
