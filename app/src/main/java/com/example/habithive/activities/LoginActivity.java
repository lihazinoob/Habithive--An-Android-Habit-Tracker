package com.example.habithive.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habithive.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Binding the UI
        emailLayout = findViewById(R.id.EmailLoginTextField);
        passwordLayout = findViewById(R.id.PasswordLoginTextField);
        emailEditText = findViewById(R.id.EmailLoginEditText);
        passwordEditText = findViewById(R.id.PasswordLoginEditText);
        loginButton = findViewById(R.id.Login);
        registerButton = findViewById(R.id.registerButtonLinkefromLogin);


    }
}
