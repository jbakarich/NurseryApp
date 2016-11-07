package com.MispronouncedDevelopment.Homeroom;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.R.attr.button;

/**
 * Created by jacob on 9/15/2016.
 */
public class Parent_HomeFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.parent_home, container, false);
        return myView;

    }

    public void Parent_HomeFragment(){

        Button attendaceHistory = (Button) myView.findViewById(R.id.attendaceHistoryCardButton);

        Button callButton = (Button)myView.findViewById(R.id.callButton);

        Button paymentHistroy = (Button)myView.findViewById(R.id.paymentHistoryCardButton);

        Button makePaymentButton = (Button)myView.findViewById(R.id.makePaymentButton);

        //attendaceHistory.setOnClickListener();





    }
}
