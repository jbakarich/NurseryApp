package com.MispronouncedDevelopment.Homeroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class AA_SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_splash_screen);


        startActivity(new Intent(this, AA_LoginActivity.class));
    }
}
