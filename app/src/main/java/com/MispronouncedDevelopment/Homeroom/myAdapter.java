package com.MispronouncedDevelopment.Homeroom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
 * myAdapter
 * Handles the dynamic population of homecards on the admin home screen.
 */
public class myAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public myAdapter(Context context, String[] values) {
        super(context, R.layout.admin_homecard, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.admin_homecard, parent, false);
        final TextView childName = (TextView) rowView.findViewById(R.id.childName);
        TextView childDate = (TextView) rowView.findViewById(R.id.childDate);
        Button profileBtn = (Button) rowView.findViewById(R.id.viewProfileBtn);
        Button callBtn = (Button) rowView.findViewById(R.id.callBtn);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);

        View.OnClickListener viewProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("childname");
                editor.putString("childname", childName.getText()+"");
                editor.apply();
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_ProfileFragment()).commit();
            }
        };

        View.OnClickListener checkin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String url;
                if(checkBox.isChecked()){
                    url = prefs.getString("url", "Wrong!") + "CheckIn";
                } else {
                    url = prefs.getString("url", "Wrong!") + "CheckOut";
                }
                
                Map<String, String> params = new HashMap<>();
                params.put("name", childName.getText()+"");

                JSONObject obj = new JSONObject(params);
                JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Adapter", "User has checked in");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Adapter", error.toString());
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(request);
            }
        };

        View.OnClickListener callParent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String url = prefs.getString("url", "Wrong!")+"getPhone";
                Map<String, String> params = new HashMap<>();
                params.put("name", childName.getText()+"");

                JSONObject obj = new JSONObject(params);
                JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        try {
                            callIntent.setData(Uri.parse("tel:" + response.getString("phonenumber").toString()));
                        } catch (JSONException e){
                            Log.d("Adapter", e.toString());
                        }
                        Activity myAct = (AA_MainActivity) getContext();
                        try{
                            myAct.startActivity(callIntent);
                        } catch(SecurityException e){
                            Log.d("Adapter", "User didn't give permission to use phone!");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Adapter", error.toString());
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(request);
            }
        };

        callBtn.setOnClickListener(callParent);
        profileBtn.setOnClickListener(viewProfile);
        checkBox.setOnClickListener(checkin);
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String val1 = mySPrefs.getString("id" + values[position] + "name", "Henrey");
        String val2 = mySPrefs.getString("id" + values[position] + "date", "Nov 1");

        childName.setText(val1);
        childDate.setText(val2);

        return rowView;
    }
}
















