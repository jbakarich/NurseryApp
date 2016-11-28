package com.MispronouncedDevelopment.Homeroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


/**
 * Created by cyrille on 9/29/2016.
 */
public class Admin_AttendenceFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.admin_attendence, container, false);



        return myView;
    }

}
