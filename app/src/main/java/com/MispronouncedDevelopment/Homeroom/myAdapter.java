package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
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
        TextView textView = (TextView) rowView.findViewById(R.id.childname);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
        String s = values[position];


        return rowView;
    }
}