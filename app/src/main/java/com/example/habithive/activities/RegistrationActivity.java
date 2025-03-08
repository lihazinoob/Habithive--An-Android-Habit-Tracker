package com.example.habithive.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private TextInputEditText confirmPasswordEditText;
    private TextInputLayout userNameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmpasswordLayout;
    private ImageView addImage;
    private ImageView imagePlaceHolder;
    private ImageView registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();


//       Binding with UI component
          usernameEditText = findViewById(R.id.UserNameEditText);
          emailEditText = findViewById(R.id.EmailEditText);
          passwordEditText = findViewById(R.id.PasswordEditText);
          confirmPasswordEditText = findViewById(R.id.ConfirmEditText);
          userNameLayout = findViewById(R.id.UserNameTextField);
          emailLayout = findViewById(R.id.EmailTextField);
          passwordLayout = findViewById(R.id.PasswordTextField);
          confirmpasswordLayout = findViewById(R.id.ConfirmTextField);
          addImage = findViewById(R.id.AddImage);
          imagePlaceHolder = findViewById(R.id.ImagePlaceHolder);
          registerButton = findViewById(R.id.RegisterButton);

//            Handling the click on the "+" sign
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

//        Handling the sign-up button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }



//    Method to open the image picker
    private void openImagePicker()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
//    Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null)
        {
            Uri imageUri = data.getData();
            imagePlaceHolder.setImageURI(imageUri);
        }

    }

//   Function for handling user registration
    public void registerUser()
    {
        String userName = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

//        Clear Previous Errors
        userNameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmpasswordLayout.setError(null);
        boolean hasErrors = false;
        if (userName.isEmpty()) {
            userNameLayout.setError("Required");
            hasErrors = true;
        }
        if (email.isEmpty()) {
            emailLayout.setError("Required");
            hasErrors = true;
        }
        if (password.isEmpty()) {
            passwordLayout.setError("Required");
            hasErrors = true;
        }
        if (confirmPassword.isEmpty()) {
            confirmpasswordLayout.setError("Required");
            hasErrors = true;
        }
        if (hasErrors) {
            return;
        }

        if (password.length() < 6) {
            passwordLayout.setError("The password should be at least 6 characters long");
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmpasswordLayout.setError("Passwords do not match");
            return;
        }


    }

}
