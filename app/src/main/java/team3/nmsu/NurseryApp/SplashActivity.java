package team3.nmsu.NurseryApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import team3.nmsu.NurseryApp.test.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // Background Tasks

//        for (int i = 0; i < 50000; i++) {
//            Log.i("LOG", "i: " +i);
//        }

        startActivity(new Intent(this, MainActivity.class));
    }
}
