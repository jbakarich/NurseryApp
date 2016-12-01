package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.R.color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import static android.R.attr.button;


/**
 * Created by cyrille on 9/29/2016.
 */
public class Parent_AttendenceFragment extends Fragment {

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


        CaldroidFragment parentAttendenceCalFragment = new CaldroidFragment(); //initialize Caldroid Fragment
        Bundle args = new Bundle();
        args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY );//pass default arguments
        parentAttendenceCalFragment.setArguments( args );

        DateFormat df = DateFormat.getDateInstance();
        HashMap CheckInDates = new HashMap(); //hashmap for date background mapping
        ColorDrawable background = new ColorDrawable(Color.LTGRAY);

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                String toast = "NAME was checked in at TIME on DATE";
                Toast.makeText(context,  toast, Toast.LENGTH_SHORT).show();
            }

            };

        try {

            CheckInDates.put(df.parse("November 29, 2016"), background);
            CheckInDates.put(df.parse("November 30, 2016"), background);
            CheckInDates.put(df.parse("December 1, 2016"), background);
            CheckInDates.put(df.parse("December 2, 2016"), background);

        }catch(java.text.ParseException e){;}


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





        parentAttendenceCalFragment.setBackgroundDrawableForDates(CheckInDates);
        parentAttendenceCalFragment.setCaldroidListener(listener);
        getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.cal_container , parentAttendenceCalFragment ).commit();





        return myView;
    }


}
