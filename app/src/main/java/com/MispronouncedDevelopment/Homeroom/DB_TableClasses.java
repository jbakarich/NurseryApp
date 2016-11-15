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
    public int id = -1;
    public String username = "username";
    public int pin = 1234;
    public boolean isAdmin = false;
    public String firstName = "firstName";
    public String lastName = "lastName";
    public String childName = "childName";
    public int phone = 1234567890;
    public String address1 = "address1";
    public String address2 = "address2";
    public String email = "email";
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