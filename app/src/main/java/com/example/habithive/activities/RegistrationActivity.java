package com.example.habithive.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habithive.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputLayout userNameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance();
////        Binding with component
//        usernameEditText = findViewById(R.id.UserNameEditText);
//        emailEditText = findViewById(R.id.EmailEditText);
//        passwordEditText = findViewById(R.id.PasswordEditText);
//        userNameLayout = findViewById(R.id.UserNameLayout);
//        emailLayout = findViewById(R.id.EmailLayout);
//        passwordLayout = findViewById(R.id.PasswordLayout);
//
//        Button RegisterButton = findViewById(R.id.registerButton);
//
//
////        Handle Sign Up Button Click
//
//        RegisterButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                registerUser();
//            }
//        });
//
//
//    }
//    private void registerUser()
//    {
//        String username = usernameEditText.getText().toString().trim();
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
////        Clear Previous Errors
//        userNameLayout.setError(null);
//        emailLayout.setError(null);
//        passwordLayout.setError(null);
//
//        //        I will validation here later
//        boolean haserrors = false;
//        if(username.isEmpty())
//        {
//            userNameLayout.setError("Required");
//            haserrors = true;
//        }
//        if(email.isEmpty())
//        {
//            emailLayout.setError("Required");
//            haserrors = true;
//        }
//        if(password.isEmpty())
//        {
//            passwordLayout.setError("Required");
//            haserrors = true;
//        }
//
//        if(haserrors)
//        {
//            return;
//        }
//
//        if(password.length() < 6)
//        {
//            passwordLayout.setError("Password must be at least of 6 characters");
//        }
//
//
//        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,task -> {
//            if(task.isSuccessful())
//            {
//                FirebaseUser user = auth.getCurrentUser();
//                Toast.makeText(RegistrationActivity.this,"Registration Successfull",Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
////                Log.d("Error",task.getException().getMessage());
////                Toast.makeText(RegistrationActivity.this,"An Unknown Error.Please Try Again",Toast.LENGTH_SHORT).show();
//            }
//        });
//



    }
}
