package com.MispronouncedDevelopment.Homeroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by jacob on 9/15/2016.
 */
public class Parent_HomeFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.parent_home, container, false);
        
        Parent_HomeFragment();

        return myView;

    }

    public void Parent_HomeFragment(){


        Button attendaceHistory = (Button) myView.findViewById(R.id.attendaceHistoryCardButton);

        Button callButton = (Button)myView.findViewById(R.id.attendenceCallButton);

        Button paymentHistroy = (Button)myView.findViewById(R.id.paymentHistoryCardButton);

        Button makePaymentButton = (Button)myView.findViewById(R.id.makePaymentButton);

        attendaceHistory.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    FragmentManager fragmentManager = getFragmentManager();


                                                    fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_AttendenceFragment()).commit();
                                                }
                                            }


        );

        callButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                                    intent.setData(Uri.parse("tel:8675309"));
                                                    startActivity(intent);
                                                }
                                            }


        );

        paymentHistroy.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              FragmentManager fragmentManager = getFragmentManager();
                                              fragmentManager.beginTransaction().replace(R.id.parent_content_frame, new Parent_PaymentFragment()).commit();

                                          }
                                      }


        );





    }
}
