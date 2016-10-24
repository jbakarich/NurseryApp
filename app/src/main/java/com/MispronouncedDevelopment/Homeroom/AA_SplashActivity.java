package com.MispronouncedDevelopment.Homeroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class AA_SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_splash_screen);


        // Background Tasks

//        for (int i = 0; i < 50000; i++) {
//            Log.i("LOG", "i: " +i);
//        }

        startActivity(new Intent(this, AA_MainActivity.class));
    }
}
