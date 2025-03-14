package com.example.habithive.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.habithive.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //    Linking the Activity to an design file
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splashMainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        new Handler(Looper.getMainLooper()).postDelayed(()->
        {
            SharedPreferences prefs = getSharedPreferences("PREFS",MODE_PRIVATE);
            boolean isFirstRun = prefs.getBoolean("isFirstRun",true);

            Intent intent = isFirstRun ? new Intent(SplashActivity.this, OnBoardingActivity.class):new Intent(SplashActivity.this,RegistrationActivity.class);
            startActivity(intent);
            finish();

        },SPLASH_DELAY);








    }
}
