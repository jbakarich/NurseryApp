package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
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
        String name = prefs.getString("username", "Josh");

        GetProfile(name);

        Button editProfile = (Button) myView.findViewById(R.id.parent_editProfile);
        Button makePayment = (Button) myView.findViewById(R.id.parent_paymentButton);

        editProfile.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    FragmentManager fragmentManager = getFragmentManager();
                                                    fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_EditProfileFragment()).commit();
                                                }
                               }


        );

        makePayment.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               Context context = getActivity();
                                               Intent myPaymentIntent = new Intent(context, AndroidPay.class);
                                               startActivity(myPaymentIntent);
                                           }
                                       }


        );

        return myView;
    }

    void GetProfile(String name) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = prefs.getString("url", "Wrong!") + "RequestProfile";

        Map<String, String> params = new HashMap<>();
        params.put("name", name);

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
        TextView childName = (TextView) getView().findViewById(R.id.parent_childName);
        TextView phone = (TextView) getView().findViewById(R.id.parent_profilePhone);
        TextView address1 = (TextView) getView().findViewById(R.id.parent_profileAddress1);
        TextView address2 = (TextView) getView().findViewById(R.id.parent_profileAddress2);
        TextView email = (TextView) getView().findViewById(R.id.parent_profileEmail);

        TextView lastcheckin = (TextView) getView().findViewById(R.id.parent_profile_lastCheckin);
        TextView membersince = (TextView) getView().findViewById(R.id.parent_profile_memberSince);

        try {
            JSONObject parentInfo = response.getJSONObject("user");
            String name = parentInfo.getString("firstname") + " " + parentInfo.getString("lastname");
            firstName.setText(name);
            childName.setText(parentInfo.getString("childname"));
            phone.setText(parentInfo.getString("phone"));
            address1.setText(parentInfo.getString("address1"));
            address2.setText(parentInfo.getString("address2"));
            email.setText(parentInfo.getString("email"));
            lastcheckin.setText("Last check-in: " + AA_MainActivity.formatTime(response.getInt("lastcheckin")));
            membersince.setText("Member since: " +AA_MainActivity.formatTime(parentInfo.getInt("creationdate")));

        } catch (JSONException e) {
            Log.d(TAG, "err in response:" + e.toString());
        }
    }
}
