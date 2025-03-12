package com.example.habithive.activities.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.habithive.activities.model.User;

@Dao
public class UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE userId = :userID")
    User getUserById(String userId);

}
