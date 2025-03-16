package com.example.habithive.activities.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.habithive.activities.model.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
        // Replace if userID exists
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE userId = :userID LIMIT 1")
    User getUserById(String userID);

}
