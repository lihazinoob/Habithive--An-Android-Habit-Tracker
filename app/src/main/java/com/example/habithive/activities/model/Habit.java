package com.example.habithive.activities.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE))
public class Habit {
    @PrimaryKey(autoGenerate = true)
    public int habitId;
    public String userId;
    public String name;
    public String goal;
    public String frequency;

    public Habit(String userId, String name, String goal, String frequency) {
        this.userId = userId;
        this.name = name;
        this.goal = goal;
        this.frequency = frequency;
    }

    public int getHabitId() {
        return habitId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getGoal() {
        return goal;
    }

    public String getFrequency() {
        return frequency;
    }
}
