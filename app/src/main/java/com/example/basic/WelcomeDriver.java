package com.example.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;

public class WelcomeDriver extends AppCompatActivity {
    TextView welcomeUser;
    TextInputEditText fullNameEditText;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText contactEditText;
    AppCompatButton logout_btn;

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

        logout_btn.setOnClickListener(v -> {
            logout();
            Intent intent = new Intent(WelcomeDriver.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    void logout() {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
