package com.MispronouncedDevelopment.Homeroom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Cyrille on 11/9/16.
 */

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<HomeCard> cardItems;

    public CustomListAdapter(Activity activity, List<HomeCard> cardItems) {
        this.activity = activity;
        this.cardItems = cardItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.admin_homecard, null);


        TextView childname = (TextView) convertView.findViewById(R.id.childname);
        TextView lastcheckin = (TextView) convertView.findViewById(R.id.lastCheckInDateText);

        HomeCard m = cardItems.get(position);

        // title
        childname.setText(m.getName());

        lastcheckin.setText(m.getCheckin());

        return convertView;
    }

}
