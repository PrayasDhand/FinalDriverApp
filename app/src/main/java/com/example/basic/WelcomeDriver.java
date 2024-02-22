package com.example.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeDriver extends AppCompatActivity {
    private static final String TAG = "WelcomeDriverActivity";
    TextView welcomeUser;
    TextInputEditText fullNameEditText;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText contactEditText;
    AppCompatButton logout_btn;
    AppCompatButton updateDetails_btn; // Added button for updating details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_driver);

        // Initialize TextView and TextInputEditText fields
        welcomeUser = findViewById(R.id.welcomeUser);
        fullNameEditText = findViewById(R.id.fullnametext_profile);
        emailEditText = findViewById(R.id.emailText_profile);
        passwordEditText = findViewById(R.id.passwordText_profile);
        logout_btn = findViewById(R.id.logout_btn);
        contactEditText = findViewById(R.id.contactText_profile);
        updateDetails_btn = findViewById(R.id.updateDetails); // Initialize the button

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        String contact = sharedPreferences.getString("contact", "");

        // Set user data to the respective fields
        welcomeUser.setText(fullName);
        fullNameEditText.setText(fullName);
        emailEditText.setText(email);
        passwordEditText.setText(password);
        contactEditText.setText(contact);

        // Set click listener for the Logout button
        logout_btn.setOnClickListener(v -> {
            logout();
            Intent intent = new Intent(WelcomeDriver.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set click listener for the Update Details button
        updateDetails_btn.setOnClickListener(v -> updateDetailsInFirebase());
    }

    // Method to logout
    void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    // Method to update details in Firebase and SharedPreferences
    void updateDetailsInFirebase() {
        // Retrieve updated details from TextInputEditText
        String updatedFullName = fullNameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedPassword = passwordEditText.getText().toString();
        String updatedContact = contactEditText.getText().toString();

        // Retrieve the current user's email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // Update details in Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("drivers");

        databaseReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            // Update the values in Firebase
                            snapshot.getRef().child("name").setValue(updatedFullName);
                            snapshot.getRef().child("email").setValue(updatedEmail);
                            snapshot.getRef().child("password").setValue(updatedPassword);
                            snapshot.getRef().child("contact").setValue(updatedContact);

                            // Update values in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("fullName", updatedFullName);
                            editor.putString("email", updatedEmail);
                            editor.putString("password", updatedPassword);
                            editor.putString("contact", updatedContact);
                            editor.apply();


                            welcomeUser.setText(updatedFullName);
                            fullNameEditText.setText(updatedFullName);
                            emailEditText.setText(updatedEmail);
                            passwordEditText.setText(updatedPassword);
                            contactEditText.setText(updatedContact);

                            // Display a message or perform any other action to indicate the successful update
                            Toast.makeText(WelcomeDriver.this, "Details updated successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, "Error updating details in Firebase: " + e.getMessage());
                            Toast.makeText(WelcomeDriver.this, "Failed to update details. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
                Log.e(TAG, "Database Error: " + databaseError.getMessage());
                Toast.makeText(WelcomeDriver.this, "Failed to update details. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
