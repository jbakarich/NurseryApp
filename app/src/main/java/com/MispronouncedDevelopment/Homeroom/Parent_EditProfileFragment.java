package com.MispronouncedDevelopment.Homeroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
 * Parent_EditProfileFragment
 * handles all transactions to server when a user wants to update their profile information.
 */

public class Parent_EditProfileFragment extends Fragment {
    View myView;
    Button submitBtn;
    EditText firstname, lastname, childname, address1, address2, phone, email, password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.parent_edit_profile, container, false);

        firstname = (EditText) myView.findViewById(R.id.editFirstName);
        lastname = (EditText) myView.findViewById(R.id.editLastName);
        childname = (EditText) myView.findViewById(R.id.editChildName);
        address1 = (EditText) myView.findViewById(R.id.editAddress1);
        address2 = (EditText) myView.findViewById(R.id.editAddress2);
        phone = (EditText) myView.findViewById(R.id.editPhone);
        email = (EditText) myView.findViewById(R.id.editEmail);
        password = (EditText) myView.findViewById(R.id.editPassword);

        submitBtn = (Button) myView.findViewById(R.id.submitEdits);
        submitBtn.setOnClickListener(
                new View.OnClickListener() {


                    public void onClick(View v) {
                        Map<String, String> params = new HashMap<>();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        params.put("firstname", firstname.getText().toString());
                        params.put("lastname", lastname.getText().toString());
                        params.put("childname", childname.getText().toString());
                        params.put("address1", address1.getText().toString());
                        params.put("address2", address2.getText().toString());
                        params.put("phone", phone.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("username", prefs.getString("username", "josh"));

                        String url = prefs.getString("url", "Wrong again!");
                        MakeRequest(url+"EditProfile", params);
                    }

                }
        );

        return myView;
    }

    /*Passes request to server*/
    void MakeRequest(String url, Map<String, String> data){
        final Context context = getActivity();
        JSONObject obj = new JSONObject(data);

        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast;
                if (response.has("badpassword")) {
                    toast = Toast.makeText(context, "You've entered an incorrect password", Toast.LENGTH_SHORT);
                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_ProfileFragment()).commit();
                    toast = Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
