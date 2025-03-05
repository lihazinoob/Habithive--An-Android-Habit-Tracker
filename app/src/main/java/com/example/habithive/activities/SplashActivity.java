package com.example.habithive.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.habithive.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//    Linking the Activity to an design file
        setContentView(R.layout.activity_splash);
        TextView animatedText1 = findViewById(R.id.textView);
        TextView animatedText2 = findViewById(R.id.textView2);
        TextView ColorAnimatedText = findViewById(R.id.textView3);
        /*Fade In automation*/
        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
//        animationIdling.increment();
        animatedText1.startAnimation(fadeIn);
        animatedText2.startAnimation(fadeIn);

//        Animating color of the logo
        int startColor = ContextCompat.getColor(this,R.color.logoColor);
        int endColor = ContextCompat.getColor(this,R.color.black);

        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),startColor,endColor);
        colorAnimator.setDuration(2500);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animator)
            {
                ColorAnimatedText.setTextColor((int) animator.getAnimatedValue());
            }

        });

        colorAnimator.start();








//        Set the delay for 3 seconds, then redirecting
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.d("SplashActivity", "Redirecting after 4000ms");
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
//                Close SplashActivity so it doesnt stay in the back stack
                finish();
            }
        },4000);

    }
}
