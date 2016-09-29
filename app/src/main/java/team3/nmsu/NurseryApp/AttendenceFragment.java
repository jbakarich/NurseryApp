package team3.nmsu.NurseryApp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team3.nmsu.NurseryApp.test.R;

/**
 * Created by cyrille on 9/29/2016.
 */
public class AttendenceFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.attendence_layout, container, false);
        return myView;
    }
}
