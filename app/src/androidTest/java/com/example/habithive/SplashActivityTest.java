package com.example.habithive;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withAlpha;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.annotation.ContentView;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
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
            Thread.sleep(4500); //Freezes the test thread

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        verify the Intent was sent to LoginActivity
        intended(hasComponent(LoginActivity.class.getName()));
    }

}
