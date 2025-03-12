package com.example.habithive.activities.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public String userID;
    public String username;
    public String email;
    public String imageURL;

//    Constructor which required for working with room
    public User(String userID,String username,String email,String imageURL)
    {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return imageURL;
    }




}
