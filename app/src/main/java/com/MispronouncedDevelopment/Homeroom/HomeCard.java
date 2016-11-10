package com.MispronouncedDevelopment.Homeroom;

/**
 * Created by Cyrille on 11/9/16.
 */

public class HomeCard{
    String name;
    int lastCheckin;
    String id;

    public HomeCard(){
        name = "";
        lastCheckin = 0;
        id = "";
    }

    public HomeCard(String newName, int newCheckin, String newId){
        name = newName;
        lastCheckin = newCheckin;
        id = newId;
    }

    public String getName(){
        return name;
    }

    public int getCheckin(){
        return lastCheckin;
    }

    public String getId(){
        return id;
    }
}