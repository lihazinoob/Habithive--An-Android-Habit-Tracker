package com.example.habithive.activities.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.habithive.activities.model.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace if userID exists
    void insert(User user);

    @Query("SELECT * FROM users WHERE userId = :userID")
    User getUserById(String userID);

}
