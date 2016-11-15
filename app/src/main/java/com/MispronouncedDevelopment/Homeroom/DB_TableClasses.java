package com.MispronouncedDevelopment.Homeroom;

/**
 * Created by Cyrille on 11/15/16.
 */

public class DB_TableClasses {

    public void CreateNewUser(int id, String FirstName, String LastName, String UserName, String ChildName ){
        User newUser = new User();
//        setFirstName(newUser, FirstName);
//        setLastName(newUser, LastName)
    }
}

class User {
    private  int id = -1;
    private String username = "username";
    private int pin = 1234;
    private boolean isAdmin = false;
    private String firstName = "firstName";
    private String lastName = "lastName";
    private String childName = "childName";
    private int phone = 1234567890;
    private String address1 = "address1";
    private String address2 = "address2";
    private String email = "email";

    public SetUser(int UserId){

    }

    public SetUserName(int UserId, String newName){
        username = newName;
    }
}

/* Inner class that defines the table contents */
 class AttendanceRecord {
    public int id = -1;
    public int userId = -1;
    public String date = "date";
    public boolean attended = false;
}

/* Inner class that defines the table contents */
class PaymentRecord {
    public int id = -1;
    public int userId = -1;
    public String date = "date";
    public double amount = 0.0;
    public boolean isPaid = false;
}