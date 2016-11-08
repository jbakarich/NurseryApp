package com.MispronouncedDevelopment.Homeroom;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
 * Created by Cyrille on 11/7/16.
 */

public class Admin_CreateUserFragment extends Fragment {
    View myView;
    String TAG = "CreateUserFragment", url = "http://172.24.95.132/";
    Button submitBtn;
    EditText firstname, lastname, username, childname, address, phone, email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.admin_createuser, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        url = prefs.getString("url", "http://192.168.0.1/");


        submitBtn = (Button) myView.findViewById(R.id.submitnewuser);

        firstname = (EditText) myView.findViewById(R.id.newUserFirstName);
        lastname = (EditText) myView.findViewById(R.id.newUserLastName);
        childname = (EditText) myView.findViewById(R.id.newUserUserName);
        username = (EditText) myView.findViewById(R.id.newUserUserName);
        address = (EditText) myView.findViewById(R.id.newUserAddress);
        phone = (EditText) myView.findViewById(R.id.newUserPhone);
        email = (EditText) myView.findViewById(R.id.newUserEmail);
        Log.d(TAG, "Creating view");

        submitnewuser();
        return myView;
    }

    public void submitnewuser() {
        submitBtn.setOnClickListener(
                new View.OnClickListener() {


                    public void onClick(View v) {
                        Log.d(TAG, "Creating params");
                        Map<String, String> params = new HashMap<>();

                        params.put("firstname", firstname.getText().toString());
                        params.put("lastname", lastname.getText().toString());
                        params.put("username", username.getText().toString());
                        params.put("childname", childname.getText().toString());
                        params.put("address", address.getText().toString());
                        params.put("phone", phone.getText().toString());
                        params.put("email", email.getText().toString());

                        MakeRequest(url+"AddUser", params);

                    }

                }
        );
    }

    void MakeRequest(String url, Map<String, String> data){
        Log.d(TAG, "Making request to " + url);
        final Context context = getActivity();
        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast = Toast.makeText(context, "User submitted successfully", Toast.LENGTH_SHORT);
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, "An error has occured", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
