package com.example.habithive.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.habithive.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity  {
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        // Initialize views
        drawerLayout = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);
        // Handle Bottom Navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Handle Home click
                Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_logout) {
                // Handle Logout click
                Toast.makeText(this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Handle FAB click
        fab.setOnClickListener(view -> {
            // Handle FAB click (e.g., open a habit creation dialog)
            Toast.makeText(this, "Create Habit", Toast.LENGTH_SHORT).show();
        });


    }







}