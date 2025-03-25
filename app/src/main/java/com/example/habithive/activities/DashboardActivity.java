package com.example.habithive.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.habithive.R;
import com.example.habithive.activities.model.UserManagerSingleton;
import com.example.habithive.activities.services.StepTrackingService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity  {
    private static final int REQUEST_CODE_ACTIVITY_RECOGNITION = 100;
    private static final int REQUEST_CODE_IGNORE_BATTERY_OPTIMIZATION = 101;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabCreate;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        // Initialize views
        auth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabCreate = findViewById(R.id.fab_create);

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        // Bottom Navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            }
            else if (id == R.id.nav_progress) {
                loadFragment(new ProgressFrament());
                return true;
            } else if (id == R.id.nav_logout) {
                logoutUser();
                return true;
            }
            return false;
        });
        fabCreate.setOnClickListener(v -> {
            HabitCreationBottomSheet bottomSheet = new HabitCreationBottomSheet();
            bottomSheet.show(getSupportFragmentManager(),"habitcreating");
        });
        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Request battery optimization exemption
        requestIgnoreBatteryOptimizations();

        // Request permission and start step tracking service
        requestActivityRecognitionPermission();
    }

    private void requestIgnoreBatteryOptimizations()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                batteryOptimizationLauncher.launch(intent);
            } else {
                // Already exempted, proceed with permission check
                requestActivityRecognitionPermission();
            }
        }
    }

    private final ActivityResultLauncher<Intent> batteryOptimizationLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                    if (pm.isIgnoringBatteryOptimizations(getPackageName())) {
                        Toast.makeText(this, "Battery optimization disabled", Toast.LENGTH_SHORT).show();
                        requestActivityRecognitionPermission();
                    } else {
                        Toast.makeText(this, "Please disable battery optimization for step tracking", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void requestActivityRecognitionPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
            } else {
                startStepTrackingService();
            }
        } else {
            startStepTrackingService(); // No permission needed below Android 10
        }
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startStepTrackingService();
                } else {
                    Toast.makeText(this, "Step tracking permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private void startStepTrackingService() {
        Intent serviceIntent = new Intent(this, StepTrackingService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void logoutUser()
    {
        auth.signOut();
//        Clear UserManagerSingleton
        UserManagerSingleton.getInstance().clearCurrentUser();
        // Show logout confirmation
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to LoginActivity
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


}