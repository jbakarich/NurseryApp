package com.MispronouncedDevelopment.Homeroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
 * Created by Cyrille on 11/8/16.
 */

public class Parent_ProfileFragment extends Fragment {
    View myView;
    private String TAG = "Parent Profile";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.parent_profile, container, false);

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

        TextView firstName = (TextView) getView().findViewById(R.id.parent_profileName);
        TextView phone = (TextView) getView().findViewById(R.id.parent_profilePhone);
        TextView address1 = (TextView) getView().findViewById(R.id.parent_profileAddress1);
        TextView address2 = (TextView) getView().findViewById(R.id.parent_profileAddress2);
        TextView email = (TextView) getView().findViewById(R.id.parent_profileEmail);

        TextView lastcheckin = (TextView) getView().findViewById(R.id.parent_profile_lastCheckin);
        TextView membersince = (TextView) getView().findViewById(R.id.parent_profile_memberSince);

        try {
            JSONObject parentInfo = response.getJSONObject("user");
            firstName.setText(parentInfo.getString("username"));
            phone.setText(parentInfo.getString("phone"));
            address1.setText(parentInfo.getString("address1"));
            address2.setText(parentInfo.getString("address2"));
            email.setText(parentInfo.getString("email"));
            lastcheckin.setText(response.getString("lastcheckin"));
            membersince.setText(parentInfo.getString("creationdate"));

        } catch (JSONException e) {
            Log.d(TAG, "err in response:" + e.toString());
        }
    }
}
