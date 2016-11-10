package com.MispronouncedDevelopment.Homeroom;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.Button;
=======
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
>>>>>>> 3c9ce968182bd3ac985a982a6700df7f4e322b13

/**
 * Created by jacob on 9/15/2016.
 */

public class Admin_HomeFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.admin_home, container, false);


        Admin_HomeFragment();

        return myView;
    }

    public void Admin_HomeFragment()
    {


        Button profile3 = (Button) myView.findViewById(R.id.ProfileButton3);

        profile3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    android.app.FragmentManager fragmentManager = getFragmentManager();


                                                    fragmentManager.beginTransaction().replace(R.id.admin_content_frame, new Parent_ProfileFragment()).commit();
                                                }
                                            }


        );


    }

}

