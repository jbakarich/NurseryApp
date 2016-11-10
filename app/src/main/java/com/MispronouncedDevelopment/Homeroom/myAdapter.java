package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Cyrille on 11/10/16.
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.admin_homecard, parent, false);
        TextView childName = (TextView) rowView.findViewById(R.id.childName);
        TextView childDate = (TextView) rowView.findViewById(R.id.childDate);


        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String val1 = mySPrefs.getString("id" + values[position] + "name", "Henrey");
        String val2 = mySPrefs.getString("id" + values[position] + "date", "Nov 1");

        childName.setText(val1);
        childDate.setText(val2);
        String s = values[position];

        return rowView;
    }
}