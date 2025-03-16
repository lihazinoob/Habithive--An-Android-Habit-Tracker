package com.example.habithive.activities.dao;

import androidx.lifecycle.LiveData;
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
    LiveData<List<Habit>> getHabitsByUserId(String userID);

    @Query("UPDATE habits SET progress = :progress WHERE habitId = :habitId")
    void updateProgress(int habitId, int progress);
}
