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
    public String type;
    public String goal;
    public String frequency;
    public int progress;

    public Habit(String userId, String name,String type, String goal, String frequency) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.goal = goal;
        this.frequency = frequency;
        this.progress = 0;
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

    public String getType() {
        return type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
