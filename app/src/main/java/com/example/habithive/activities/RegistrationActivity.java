package com.example.habithive.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habithive.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.UserNameEditText);
        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);

        Button RegisterButton = findViewById(R.id.registerButton);


//        Handle Sign Up Button Click

        RegisterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerUser();
            }
        });


    }
    private void registerUser()
    {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        //        I will validation here later

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,task -> {
            if(task.isSuccessful())
            {
                FirebaseUser user = auth.getCurrentUser();
                Toast.makeText(RegistrationActivity.this,"Registration Successfull",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(RegistrationActivity.this,"Error: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




    }
}
