package com.example.habithive.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.habithive.R;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Handler handler;
    private Runnable autoAdvanceRunnable;
    private static final int PAGE_DELAY = 3000; // 3 seconds
    private boolean isAutoAdvancing = true;
    private boolean isFirstPageSelection = true; // New flag
    private static final String TAG = "OnBoardingDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_boarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate called");

        viewPager = findViewById(R.id.viewpager);
        Log.d(TAG, "ViewPager initialized: " + (viewPager != null));

        // Adapter design pattern is implemented here
        OnBoardingAdapter adapter = new OnBoardingAdapter(this);
        viewPager.setAdapter(adapter);
        Log.d(TAG, "Adapter set, item count: " + adapter.getItemCount());

        handler = new Handler(Looper.getMainLooper());
        autoAdvanceRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Inside the run function");
                int currentItem = viewPager.getCurrentItem();
                int maxItem = adapter.getItemCount();
                Log.d(TAG, "Runnable running - Current: " + currentItem + ", Max: " + maxItem + ", AutoAdvancing: " + isAutoAdvancing);

                if (currentItem < (maxItem - 1) && isAutoAdvancing) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                    Log.d(TAG, "Advancing to: " + (currentItem + 1));
                    handler.postDelayed(this, PAGE_DELAY);
                } else {
                    isAutoAdvancing = false;
                    Log.d(TAG, "Stopped auto-advance");
                }
            }
        };

        Log.d(TAG, "Starting auto-advance");
        handler.postDelayed(autoAdvanceRunnable, PAGE_DELAY);
        Log.d(TAG, "Closing auto-advance");

        // Handling user interaction
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (isFirstPageSelection) {
                    isFirstPageSelection = false; // Ignore initial selection
                    Log.d(TAG, "Initial page selection: " + position + ", ignoring");
                } else {
                    isAutoAdvancing = false;
                    handler.removeCallbacks(autoAdvanceRunnable);
                    Log.d(TAG, "User swiped to position: " + position + ", auto-advance stopped");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoAdvanceRunnable);
        Log.d(TAG, "onDestroy called");
    }
}