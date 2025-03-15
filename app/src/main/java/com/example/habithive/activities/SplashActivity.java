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
import com.example.habithive.activities.database.AppDatabase;
import com.example.habithive.activities.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private AppDatabase appDatabase;

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

        // Initialize Firebase and Room
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        appDatabase = AppDatabase.getInstance(this);
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthAndNavigate,SPLASH_DELAY);

    }
    private void checkAuthAndNavigate()
    {
        SharedPreferences prefs = getSharedPreferences("PREFS",MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);

        if(isFirstRun)
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
            finish();
        }
        else if(auth.getCurrentUser() != null)
        {
//            User is logged in. So sync room database and go to Dashboard
            String userId = auth.getCurrentUser().getUid();
            new Thread(()->
            {
                User user = appDatabase.userDao().getUserById(userId);
                if(user != null)
                {
                    // User exists in Room, proceed to Dashboard
                    startDashboardActivity(user.getUsername());
                }
                else {
                    firestore.collection("users").document(userId)
                            .get().addOnSuccessListener(documentSnapshot ->
                            {
                                if(documentSnapshot.exists())
                                {
                                    User newUser = new User(userId,documentSnapshot.getString("username"),auth.getCurrentUser().getEmail(),documentSnapshot.getString("imageUrl"));
                                    appDatabase.userDao().insert(newUser);
                                    startDashboardActivity(newUser.getUsername());
                                }
                                else {
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(e->
                            {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            });
                }
            }).start();
        }
        else
        {
            // User is not logged in: Go to Login
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void startDashboardActivity(String username)
    {
        runOnUiThread(()->
        {
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            intent.putExtra("username", username); // Optional: pass username
            startActivity(intent);
            finish();
        });
    }
}
