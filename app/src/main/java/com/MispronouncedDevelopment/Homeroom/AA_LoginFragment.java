package com.MispronouncedDevelopment.Homeroom;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.MispronouncedDevelopment.Homeroom.AA_MainActivity;


import java.io.IOException;



/**
 * Created by jacob on 9/15/2016.
 */
public class AA_LoginFragment extends Fragment {

    View myView;

    AA_DatabaseImport myDB;

    Button loginButton;

    EditText editName, editPin;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Context context = getActivity();

        myView = inflater.inflate(R.layout.aa_login, container, false);

        myDB = new AA_DatabaseImport(context, "DB1.db");

        try {

            myDB.createDataBase();

        }catch (IOException ioe){

            throw new Error("UNABLE TO CREATE DATABASE");
        }

        try{

            myDB.openDataBase();

        }catch(SQLiteException sqle){

            throw sqle;
        }

        editName = (EditText)myView.findViewById(R.id.editUserNameText);

        editPin = (EditText)myView.findViewById(R.id.EditUserPIN);

        loginButton = (Button)myView.findViewById(R.id.loginButton);

        login();

        return myView;
    }


   public void login(){



        loginButton.setOnClickListener(

                new View.OnClickListener(){


                    public void onClick(View v)
                    {

                        String userName = editName.getText().toString();

                        String PIN = editPin.getText().toString();

                        boolean login = false;

                        Context context1 = getActivity();

                        Cursor res = myDB.getAllData();

                        //StringBuffer buffer = new StringBuffer();

                        while(res.moveToNext()){

                            //buffer.append("" + res.getString(1));

                           if(userName.matches(res.getString(1).toLowerCase()) && PIN.matches(res.getString(2).toLowerCase()))
                           {

                               Toast toast = Toast.makeText(context1, "Logged in as " + userName, Toast.LENGTH_SHORT);
                               toast.show();
                               successfulLogin();
                               break;
                           }else{

                               Toast toast = Toast.makeText(context1, "Incorrect Login or PIN", Toast.LENGTH_SHORT);
                               toast.show();

                           }


                        }

                       //showMessage("Data", buffer.toString());

                    }

                }

        );

    }

    public void showMessage(String title, String Message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setCancelable(true);

        builder.setTitle(title);

        builder.setMessage(Message);

        builder.show();


    }

    public void successfulLogin(){

        AA_DatabaseImport apd = new AA_DatabaseImport(this.getActivity(), "app_data");


        ContentValues cv = new ContentValues();

        cv.put("STATUS", "'1'");


        android.app.FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, new Admin_HomeFragment()).commit();

    }

}