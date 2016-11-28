package com.MispronouncedDevelopment.Homeroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by jacob on 9/15/2016.
 */

public class Admin_HomeFragment extends Fragment {

    View myView;
    private String TAG = "Admin Home";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.admin_home, container, false);
        Log.d(TAG, "Home frag for admin");

        Admin_HomeFragment();

        return myView;
    }

    public void Admin_HomeFragment()
    {
//        Button profile3 = (Button) myView.findViewById(R.id.ProfileButton3);
//        profile3.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    android.app.FragmentManager fragmentManager = getFragmentManager();
//                                                    fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Parent_ProfileFragment()).commit();
//                                                }
//                                            }
//        );
    }

}

