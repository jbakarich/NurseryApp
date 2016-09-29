package team3.nmsu.NurseryApp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team3.nmsu.NurseryApp.test.R;

/**
 * Created by jacob on 9/15/2016.
 * Edited by Cyrille on 9/19/2016
 */
public class FirstFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        myView = inflater.inflate(R.layout.mainpage, container, false);




        return myView;
    }
}
