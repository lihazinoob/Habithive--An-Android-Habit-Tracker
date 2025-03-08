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
    private TextInputLayout userNameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private ImageView addImage;
    private ImageView imagePlaceHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance();
////        Binding with component
          addImage = findViewById(R.id.AddImage);
          imagePlaceHolder = findViewById(R.id.ImagePlaceHolder);

//            Handling the click on the "+" sign
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
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

}
