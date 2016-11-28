package com.MispronouncedDevelopment.Homeroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;
import com.roomorama.caldroid.CaldroidFragment;


/**
 * Created by cyrille on 9/29/2016.
 */
public class Parent_AttendenceFragment extends Fragment {

    View myView;
    public Parent_AttendenceFragment(){}

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.parent_attendence, container, false);

        CaldroidFragment mCaldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY );
        mCaldroidFragment.setArguments( args );
        getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.cal_container , mCaldroidFragment ).commit();

        return myView;
    }
}
