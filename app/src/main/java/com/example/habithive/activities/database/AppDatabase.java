package com.example.habithive.activities.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.habithive.activities.dao.HabitDao;
import com.example.habithive.activities.dao.UserDao;
import com.example.habithive.activities.model.Habit;
import com.example.habithive.activities.model.User;

@Database(entities = {User.class, Habit.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract HabitDao habitDao();
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "habithive_database").addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add the new columns 'type' and 'progress' to the habits table
            database.execSQL("ALTER TABLE habits ADD COLUMN type TEXT NOT NULL DEFAULT 'Time'");
            database.execSQL("ALTER TABLE habits ADD COLUMN progress INTEGER NOT NULL DEFAULT 0");
        }
    };


}
