package team3.nmsu.NurseryApp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import team3.nmsu.NurseryApp.test.R;

/**
 * Created by jacob on 9/15/2016.
 */
public class HomeFragment extends Activity {

    View myView;

        GridView gridview;
        String[] item = new String[]{
                "ONE",
                "TWO",
                "THREE",
                "FOUR",
                "FIVE",
                "SIX"
        };
        Button button;
        List<String> ITEM_LIST;
        ArrayAdapter<String> arrayadapter;
        EditText edittext;
        String GetItem;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.d("hi", "hello");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            gridview = (GridView)findViewById(R.id.gridView1);

            button = (Button)findViewById(R.id.button1);

            edittext = (EditText)findViewById(R.id.editText1);

            ITEM_LIST = new ArrayList<String>(Arrays.asList(item));


            gridview.setAdapter(arrayadapter);

            button.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.d("hi", "hello");
                    GetItem = edittext.getText().toString();

                    ITEM_LIST.add(ITEM_LIST.size(),GetItem);

                    arrayadapter.notifyDataSetChanged();

                }
            });
        }
    }

