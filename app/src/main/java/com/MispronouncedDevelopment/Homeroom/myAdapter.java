package com.MispronouncedDevelopment.Homeroom;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Cyrille on 11/10/16.
 */
public class myAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String TAG = "adapter";

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

        View.OnClickListener viewProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("childname");
                editor.putString("childname", childName.getText()+"");
                editor.apply();
                fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Admin_ProfileFragment()).commit();
            }
        };



        profileBtn.setOnClickListener(viewProfile);
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String val1 = mySPrefs.getString("id" + values[position] + "name", "Henrey");
        String val2 = mySPrefs.getString("id" + values[position] + "date", "Nov 1");

        childName.setText(val1);
        childDate.setText(val2);

        return rowView;
    }
}
















