package com.example.habithive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habithive.R;
import com.example.habithive.activities.database.AppDatabase;
import com.example.habithive.activities.model.User;
import com.example.habithive.activities.model.UserManagerSingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private AppDatabase appDatabase;
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Initialize Firbase Auth and room database
        appDatabase = AppDatabase.getInstance(this);
        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();


//        Binding the UI
        emailLayout = findViewById(R.id.EmailLoginTextField);
        passwordLayout = findViewById(R.id.PasswordLoginTextField);
        emailEditText = findViewById(R.id.EmailLoginEditText);
        passwordEditText = findViewById(R.id.PasswordLoginEditText);
        loginButton = findViewById(R.id.Login);
        registerButton = findViewById(R.id.registerButtonLinkefromLogin);
        progressBar = findViewById(R.id.LoginProgressBar);

        loginButton.setOnClickListener(view -> {
            loginUser();
        });
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);

        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

//        Clear Previous Errors
        emailLayout.setError(null);
        passwordLayout.setError(null);

        boolean hasErrors = false;
        if (email.isEmpty()) {
            emailLayout.setError("Email is Required");
            hasErrors = true;
        }
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            hasErrors = true;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            hasErrors = true;
        }
        if (hasErrors) return;

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {


                    if (task.isSuccessful()) {
                        //After Successful Login fetch the user data from FireStore
                        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        fireStore.collection("users").document(userId).get().
                                addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
//                                    Corresponding username and ImageUrl is fetched from here
                                        String userName = documentSnapshot.getString("username");
                                        String imageUrl = documentSnapshot.getString("imageUrl");

//                                    Now create a User Object that will be mappped into the User table in the room Database
                                        User user = new User(userId, userName, email, imageUrl);
                                        new Thread(() ->
                                        {
                                            appDatabase.userDao().insert(user);
                                            UserManagerSingleton.getInstance().setCurrentUser(user);
                                            runOnUiThread(() -> {
                                                Toast.makeText(this, "Logged in as " + userName, Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                loginButton.setEnabled(true);
                                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                                finish();
                                            });
                                        }).start();
                                    } else {
                                        Toast.makeText(this, "User data not found in Firestore", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Handle specific Firebase errors
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            emailLayout.setError("No account found with this email");

                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            // Check if the error is specifically due to the password
                            if (e.getMessage().contains("password is invalid")) {
                                passwordLayout.setError("Incorrect password");
                            } else {
                                emailLayout.setError("Invalid email format");
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
