package com.example.habithive.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.habithive.R;
import com.example.habithive.activities.database.AppDatabase;
import com.example.habithive.activities.model.Habit;
import com.example.habithive.activities.model.UserManagerSingleton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CustomHabitCreationActivity extends AppCompatActivity {

    private TextInputLayout habitNameTextinputLayout;
    private TextInputEditText habitNameTextInputEditText;
    private Spinner habitTypeSpinner;
    private TextInputLayout goalLayout;
    private TextInputEditText goalEditText;
    private Spinner frequencySpinner;
    private Button saveButton;
    private AppDatabase appDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_habit_creation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainCreationLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Initialize room database
        appDatabase = AppDatabase.getInstance(this);

//        Binding the UI element
        habitNameTextinputLayout = findViewById(R.id.habitNameTextinputLayout);
        habitNameTextInputEditText = findViewById(R.id.habitNameTextInputEditText);
        habitTypeSpinner = findViewById(R.id.habitTypeSpinner);
        goalLayout = findViewById(R.id.goalLayout);
        goalEditText = findViewById(R.id.goalEditText);
        frequencySpinner = findViewById(R.id.frequencySpinner);
        saveButton = findViewById(R.id.saveCustomHabitButton);

//        Setup Habit Type Spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.habit_types,android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitTypeSpinner.setAdapter(typeAdapter);

//        Setup frequency spinner
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);

//        Clicking the save button
        saveButton.setOnClickListener(i->{
            saveHabit();
        });
        overridePendingTransition(R.anim.slide_in_up,R.anim.stay);
    }
    private void saveHabit()
    {
        String habitName = habitNameTextInputEditText.getText().toString().trim();
        String goalValue = goalEditText.getText().toString().trim();
        String frequency = frequencySpinner.getSelectedItem().toString();
        String type = habitTypeSpinner.getSelectedItem().toString().split(" ")[0]; // Extract "Time", "Steps", etc.
//        Some form of validation, will be more concise later
        if (habitName.isEmpty() || goalValue.isEmpty()) {
            if (habitName.isEmpty()) habitNameTextinputLayout.setError("Habit name is required");
            if (goalValue.isEmpty()) goalLayout.setError("Goal value is required");
            return;
        }


        // Validate goal value is numeric
        try {
            Integer.parseInt(goalValue);
        } catch (NumberFormatException e) {
            goalLayout.setError("Goal must be a number");
            return;
        }

        String userId = UserManagerSingleton.getInstance().getCurrentUser().getUserID();
        Habit habit = new Habit(userId, habitName, type, goalValue, frequency);

//        Save to Room Database
        new Thread(()->
        {
            appDatabase.habitDao().insert(habit);
            runOnUiThread(()->
            {
                Toast.makeText(this, "Habit saved: " + habitName, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish(); // Close the activity
            });

        }).start();



    }



}