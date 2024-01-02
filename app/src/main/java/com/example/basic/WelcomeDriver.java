package com.example.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        logout_btn.setOnClickListener(v -> {
            logout();
            Intent intent = new Intent(WelcomeDriver.this,MainActivity.class);
            startActivity(intent);
            finish();
        });
        contactEditText = findViewById(R.id.contactText_profile);

        String userEmail = getIntent().getStringExtra("email");
        String userPassword = getIntent().getStringExtra("password");

        DatabaseHelper1 dbHelper = new DatabaseHelper1(this);
        Driver user = dbHelper.getDriver(userEmail, userPassword);

        if (user != null) {
            welcomeUser.setText(user.getName());
            fullNameEditText.setText(user.getName());
            emailEditText.setText(user.getEmail());
            passwordEditText.setText(user.getPassword());
            contactEditText.setText(user.getContact());
        }
        if (user != null) {
            welcomeUser.setText(user.getName());
            fullNameEditText.setText(user.getName());
            emailEditText.setText(user.getEmail());
            passwordEditText.setText(user.getPassword());
            contactEditText.setText(user.getContact());
        } else {
            // Log or display a message to indicate that the user data is null
            Log.e("WelcomeDriver", "User data is null");
        }

    }

    private void logout() {

        SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
