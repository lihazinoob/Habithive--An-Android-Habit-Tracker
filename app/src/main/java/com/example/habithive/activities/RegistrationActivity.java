package com.example.habithive.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habithive.R;
import com.example.habithive.activities.database.AppDatabase;
import com.example.habithive.activities.model.User;
import com.example.habithive.activities.model.UserManagerSingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private AppDatabase appDatabase;
    private Uri selectedImageUri;

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
    private ProgressBar registrationLoader;
    private TextView loginbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        Initialize Firebase Auth,db and storage
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        appDatabase = AppDatabase.getInstance(this);




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
          registrationLoader = findViewById(R.id.progressBar);
          loginbutton = findViewById(R.id.LoginButton);

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
        loginbutton.setOnClickListener(view->{
            startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
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
            selectedImageUri = data.getData();
            imagePlaceHolder.setImageURI(selectedImageUri);
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

        registrationLoader.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

//        Authenticate with Firebase
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,task -> {
           if(task.isSuccessful())
           {
               FirebaseUser user = auth.getCurrentUser();
               String userId = user.getUid(); // generating unique user id for storage purpose
//               Saving Image to Storage
                if(selectedImageUri != null )
                {
                    //               upload image to the firebase storage
                    StorageReference imageRef = storageRef.child("profile_images/" + userId + ".jpg");

                    imageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri->
                        {
                            String imageUrl = uri.toString();
                            saveUserData(userId,userName,email,imageUrl);
                        }) ;
                    }).addOnFailureListener(event -> {
                        registrationLoader.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        Toast.makeText(this,"Image Upload Failed: "+event.getMessage(),Toast.LENGTH_SHORT).show();
                    });
                }
                else
                {

                    saveUserData(userId,userName,email,null);
                }


           }
           else
           {
               registrationLoader.setVisibility(View.GONE);
               registerButton.setEnabled(true);
               Toast.makeText(this,"Registration Failed: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
           }
        });
    }

    private void saveUserData(String userId,String userName,String email,String imageUrl)
    {
//        Create a user data map in the backend
        Map<String,Object> userData = new HashMap<>();
        userData.put("username",userName);
        userData.put("email",email);
        if(imageUrl != null)
        {
            userData.put("imageUrl",imageUrl);
        }


//        Save to Firesotre under 'users' collection
        db.collection("users").document(userId).set(userData).addOnSuccessListener(s->
        {
//            Create a User Object
            User user = new User(userId,userName,email,imageUrl);
//            Save to Room and Update Singleton
            new Thread(()->
            {
                try {
                    appDatabase.userDao().insert(user);
                    UserManagerSingleton.getInstance().setCurrentUser(user);
                    runOnUiThread(()->
                    {
                        registrationLoader.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        Toast.makeText(this,"Saved data to firestore",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this,DashboardActivity.class));
                        finish();
                    });
                }
                catch (Exception e)
                {
                    runOnUiThread(()->
                    {
                        registrationLoader.setVisibility(View.GONE);
                        registerButton.setEnabled(true);
                        Toast.makeText(this, "Failed to insert data into Room DB: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

            }).start();


        }).addOnFailureListener(f->
        {
            registrationLoader.setVisibility(View.GONE);
            registerButton.setEnabled(true);
           Toast.makeText(this,"User data is not saved to firestore",Toast.LENGTH_SHORT).show();
        });
    }

}
