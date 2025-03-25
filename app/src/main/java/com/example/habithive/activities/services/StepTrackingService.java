package com.example.habithive.activities.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.habithive.R;
import com.example.habithive.activities.database.AppDatabase;
import com.example.habithive.activities.model.Habit;
import com.example.habithive.activities.model.UserManagerSingleton;

import java.util.List;

public class StepTrackingService extends Service implements SensorEventListener {
    private static final String CHANNEL_ID = "StepTrackingChannel";
    private static final int NOTIFICATION = 1;
    private static final String PREFS_NAME = "StepTrackingPrefs";
    private static final String KEY_INITIAL_STEP_COUNT = "initialStepCount";
    private static final String KEY_CURRENT_STEPS = "currentSteps";
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private AppDatabase appDatabase;
    private int initialStepCount = -1;
    private int currentSteps = 0;
    private SharedPreferences prefs;

    @Override
    public void onCreate()
    {
        super.onCreate();
        appDatabase = AppDatabase.getInstance(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCounterSensor != null)
        {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("StepTrackingService", "Step counter sensor registered");
        }
        else
        {
            Log.e("StepTrackingService", "Step counter sensor not available");
            stopSelf();
        }

//        Create a Notification Channel
        createNotificationChannel();
        startForeground(NOTIFICATION,buildNotification());
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
//        Service will be restarted if killed
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
        {
            int totalSteps =(int) event.values[0];
            Log.d("StepTrackingService", "Total steps from sensor: " + totalSteps);
            if(initialStepCount == -1)
            {
                initialStepCount = totalSteps;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(KEY_INITIAL_STEP_COUNT,initialStepCount);
                editor.apply();
                Log.d("StepTrackingService", "Initial step count set: " + initialStepCount);
            }
            currentSteps = totalSteps - initialStepCount;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_CURRENT_STEPS, currentSteps);
            editor.apply();
            Log.d("StepTrackingService", "Current steps: " + currentSteps);
            updateStepHabits(currentSteps);

        }
    }

    private void updateStepHabits(int steps)
    {
        String userId = UserManagerSingleton.getInstance().getCurrentUser().getUserID();
        List<Habit> habits = appDatabase.habitDao().getHabitsByUserIdSync(userId);

        for(Habit habit:habits)
        {
            if(habit.getType().equals("Steps"))
            {
                int goal = Integer.parseInt(habit.getGoal());
                int newProgress = Math.min(steps,goal);
                if(newProgress != habit.getProgress())
                {
                    habit.setProgress(newProgress);
                    appDatabase.habitDao().updateProgress(habit.getHabitId(),newProgress);
                    Log.d("StepTrackingService", "Updated habit: " + habit.getName() + ", Progress: " + newProgress + "/" + goal);
                }
            }
        }
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Step Tracking Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    private Notification buildNotification()
    {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("HabitHive Step Tracking")
                .setContentText("Tracking your steps in the background...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        Log.d("StepTrackingService", "Service destroyed");
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
