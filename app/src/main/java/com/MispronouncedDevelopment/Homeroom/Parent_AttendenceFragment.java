package com.MispronouncedDevelopment.Homeroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.R.color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.button;


/**
 * Created by cyrille on 9/29/2016.
 */
public class Parent_AttendenceFragment extends Fragment {
    private String TAG = "parentAttendence";
    View myView;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.parent_attendence, container, false);
        final Context context = this.getActivity();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = prefs.getString("url", "Wrong!")+"GetAttendence";
        String name = prefs.getString("username", "Josh");
        Map<String, String> params = new HashMap<>();
        params.put("username", name);

        JSONObject obj = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CaldroidFragment parentAttendenceCalFragment = new CaldroidFragment(); //initialize Caldroid Fragment
                Bundle args = new Bundle();
                args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY );//pass default arguments
                parentAttendenceCalFragment.setArguments( args );

                Log.d(TAG, response.toString());

                HashMap CheckInDates = new HashMap(); //hashmap for date background mapping
                DateFormat df = DateFormat.getDateInstance();
                ColorDrawable background1 = new ColorDrawable(Color.GREEN);
                ColorDrawable background2 = new ColorDrawable(Color.RED);



                int lastcheckinDate = 0;
                try {
                    JSONArray myArr = response.getJSONArray("data");

                    for (int i = 0; i < myArr.length(); i++) {
                        JSONObject newDate = myArr.getJSONObject(i);
                        try {
                            if (newDate.has("checkoutTime")) {
                                if(newDate.getInt("checkoutTime") > lastcheckinDate){
                                    lastcheckinDate = newDate.getInt("checkoutTime");
                                }
                                CheckInDates.put(df.parse(formatDate(newDate.getInt("checkoutTime"))), background1);
                            } else if (newDate.has("checkinTime")) {
                                //use different color
                                CheckInDates.put(df.parse(newDate.getInt("checkoutTime")+""), background2);
                            }
                        } catch (ParseException o){
                            Log.d(TAG, "from parsing: " + o.toString());
                        }
                    }
                } catch(JSONException e){
                    Log.d(TAG, "error in response: " + e.toString());
                }

//                final CaldroidListener listener = new CaldroidListener() {
//
//                    @Override
//                    public void onSelectDate(Date date, View view) {
//
//                        String toast = "NAME was checked in at TIME on DATE";
//                        Toast.makeText(context,  toast, Toast.LENGTH_SHORT).show();
//                    }
//
//                };

                Button callButton = (Button)myView.findViewById(R.id.ParentAttendenceCallButton); //onClick Listener for Call Button
                callButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent intent = new Intent(Intent.ACTION_DIAL);
                                                      intent.setData(Uri.parse("tel:8675309"));
                                                      startActivity(intent);
                                                  }
                                              }
                );

//                TextView lastcheckin = (TextView) myView.findViewById(R.id.LastCheckedIn);
//                lastcheckin.setText(AA_MainActivity.formatTime(lastcheckinDate));

                parentAttendenceCalFragment.setBackgroundDrawableForDates(CheckInDates);
//                parentAttendenceCalFragment.setCaldroidListener(listener);
                getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.cal_container , parentAttendenceCalFragment ).commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

        return myView;
    }

    String formatDate(int unixTime){

        long unixSeconds = unixTime;
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-6")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        int month = Integer.parseInt(formattedDate.substring(0, 2));
        int day = Integer.parseInt(formattedDate.substring(3, 5));
        int year = Integer.parseInt(formattedDate.substring(6, 10));
        Log.d(TAG, "day: " + day);
        Log.d(TAG, "month: " + month);
        Log.d(TAG, "year: " + year);
        String[] months = {"January", "Febuary", "March", "April", "May", "June", "July", "Augest", "September", "October", "November", "December"};
        String finalDate = months[month-1] + " " + day + ", " + year;
        Log.d(TAG, "final date = " + finalDate);

//        "November 12, 2016"
        return finalDate;
    }
}
