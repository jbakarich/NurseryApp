package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Cyrille on 10/24/16.
 */

public class Parent_PaymentFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.parent_payment_history, container, false);

        final Context context = this.getActivity();


        CaldroidFragment parentPaymentCalFragment = new CaldroidFragment(); //initialize Caldroid Fragment
        Bundle args = new Bundle();
        args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY );//pass default arguments
        parentPaymentCalFragment.setArguments( args );

        DateFormat df = DateFormat.getDateInstance();
        HashMap CheckInDates = new HashMap(); //hashmap for date background mapping
        ColorDrawable background = new ColorDrawable(Color.LTGRAY);

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                String toast = "Payment Of AMOUNT on DATE";
                Toast.makeText(context,  toast, Toast.LENGTH_SHORT).show();
            }

        };

        try {

            CheckInDates.put(df.parse("November 29, 2016"), background);


        }catch(java.text.ParseException e){;}







        parentPaymentCalFragment.setBackgroundDrawableForDates(CheckInDates);
        parentPaymentCalFragment.setCaldroidListener(listener);
        getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.payment_cal_container , parentPaymentCalFragment).commit();





        return myView;
    }
}
