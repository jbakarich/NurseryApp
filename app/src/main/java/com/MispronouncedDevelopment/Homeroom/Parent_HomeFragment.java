package com.MispronouncedDevelopment.Homeroom;

import android.content.Context;
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
 * Parent_HomeFragment
 * Handles some of the buttons on the parent home screen.
 * NOTE: I'm unsure why, but android studio really wants that indentation so we left it..
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
        final Context context = this.getContext();

        Button attendaceHistory = (Button) myView.findViewById(R.id.attendaceHistoryCardButton);
        Button callButton = (Button)myView.findViewById(R.id.attendenceCallButton);
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

        makePaymentButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent myPaymentIntent = new Intent(context, AndroidPay.class);
                                              startActivity(myPaymentIntent);
                                          }
                                      }
        );
    }
}
