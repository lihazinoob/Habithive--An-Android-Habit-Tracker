package com.example.habithive.activities.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.habithive.activities.model.Habit;

import java.util.List;
@Dao
public interface HabitDao {
    @Insert
    void insert(Habit habit);
    @Query("SELECT * FROM habits WHERE userId = :userID")
    List<Habit> getHabitsByUserId(String userID);
}
