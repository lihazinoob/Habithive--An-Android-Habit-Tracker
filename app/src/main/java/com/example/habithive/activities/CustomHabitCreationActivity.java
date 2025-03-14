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

public class CustomHabitCreationActivity extends AppCompatActivity {

    private TextInputEditText habitNameEditText;
    private MaterialAutoCompleteTextView goalAutoCompleteTextView;
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
        habitNameEditText = findViewById(R.id.habitNameTextInputEditText);
        goalAutoCompleteTextView = findViewById(R.id.goalAutoCompleteTextView);
        frequencySpinner = findViewById(R.id.frequencySpinner);
        saveButton = findViewById(R.id.saveCustomHabitButton);


//        Define the time options

        String[] timeOptions = new String[]{"15 min", "30 min", "45 min", "1 hr", "1.5 hr", "2 hr"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,timeOptions);
        goalAutoCompleteTextView.setAdapter(adapter);

//        Define the elements for spinners and setting the font family of the text programmatically
        String[] frequencyOptions = new String[]{"Daily","Weekly","Monthly"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,frequencyOptions);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter1);

//        Clicking the save button
        saveButton.setOnClickListener(i->{
            saveHabit();
        });
        overridePendingTransition(R.anim.slide_in_up,R.anim.stay);
    }
    private void saveHabit()
    {
        String habitName = habitNameEditText.getText().toString().trim();
        String goal = goalAutoCompleteTextView.getText().toString().trim();
        String frequency = frequencySpinner.getSelectedItem().toString();
//        Some form of validation, will be more concise later
        if (habitName.isEmpty() || goal.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = UserManagerSingleton.getInstance().getCurrentUser().getUserID();
        Habit habit = new Habit(userId,habitName,goal,frequency);

//        Save to Room Database
        new Thread(()->
        {
            appDatabase.habitDao().insert(habit);
            runOnUiThread(()->
            {
                Toast.makeText(this, "Habit saved: " + habitName, Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            });

        }).start();



    }



}