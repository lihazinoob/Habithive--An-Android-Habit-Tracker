package com.example.habithive;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.habithive.activities.LoginActivity;
import com.example.habithive.activities.SplashActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)  //Uses Android's test runner for UI test
public class SplashActivityTest {
    @Rule
//    Defining which activity will be launched before each test
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Before
    public void setUp()
    {
        Intents.init();
    }

    @After
    public void tearDown()
    {
        Intents.release();
    }
    @Test
    public void testSplashScreenTime()
    {
//        Mock the Intent to LoginActivity
        intending(hasComponent(LoginActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK,null));
        try {
            Thread.sleep(4500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFadeInAnimation()
    {

    }
    @Test
    public void testColorAnimation()
    {

    }
}
