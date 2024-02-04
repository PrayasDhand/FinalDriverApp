package com.example.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText passwordEditText;
    private CheckBox showPasswordCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_page);

        try (DatabaseHelper1 databaseHelper = new DatabaseHelper1(this)) {

            if (isLoggedIn()) {
                redirectToRegisterDriver();
            }

            Button loginBtn = findViewById(R.id.loginBtn);
            TextView registerTextView = findViewById(R.id.textView6);
            EditText emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
            showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Toggle password visibility based on CheckBox state
                togglePasswordVisibility(isChecked);
            });

            registerTextView.setOnClickListener(v -> {
                // Redirect to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            });

            loginBtn.setOnClickListener(v -> authenticateFirebase(emailEditText.getText().toString().trim(),
                    passwordEditText.getText().toString().trim()));

            emailEditText.addTextChangedListener(new SimpleTextWatcher(emailEditText, this::validateEmail));
            passwordEditText.addTextChangedListener(new SimpleTextWatcher(passwordEditText, this::validatePassword));
        }
    }

    private void validatePassword(String password) {
        // Use the correct ID for the password EditText
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        int minLength = 5;
        int maxLength = 16;

        if (password.isEmpty() || !isStrongPassword(password) || password.length() < minLength || password.length() > maxLength) {
            passwordEditText.setError("Password should be between 5 and 16 characters and contain one uppercase, one lowercase, and one special character");
        } else {
            passwordEditText.setError(null);
        }
    }

    boolean isStrongPassword(String password) {
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[!@#$%^&*()-_+=<>?].*");
    }

    private void togglePasswordVisibility(boolean showPassword) {
        if (showPassword) {
            // Show Password
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // Hide Password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.length());
    }

    private void authenticateFirebase(String email, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("drivers");

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean authenticationSuccessful = false;

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String storedPassword = snapshot.child("password").getValue(String.class);

                        if (storedPassword != null && storedPassword.trim().equals(password)) {
                            String fullName = snapshot.child("name").getValue(String.class);
                            String contact = snapshot.child("contact").getValue(String.class);

                            saveUserDataToSharedPreferences(fullName, email, password, contact);

                            saveLoginState();
                            redirectToRegisterDriver();
                            authenticationSuccessful = true;
                            break; // Exit the loop if authentication is successful
                        }
                    }
                }

                if (!authenticationSuccessful) {
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "Invalid Password");
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "User not found for email: " + email);
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database Error: " + databaseError.getMessage());
                Toast.makeText(LoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveUserDataToSharedPreferences(String fullName, String email, String password, String contact) {
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", fullName);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("contact", contact);
        editor.apply();
    }

    void validateEmail(String email) {
        EditText emailEditText = findViewById(R.id.emailEditText);
        if (email.isEmpty() || !isValidEmail(email)) {
            emailEditText.setError("Please enter a valid email address");
        } else {
            emailEditText.setError(null);
        }
    }

    boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    void saveLoginState() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void redirectToRegisterDriver() {
        Intent intent = new Intent(LoginActivity.this, DriverRegistration.class);
        startActivity(intent);
        finish();
    }

    private class SimpleTextWatcher implements TextWatcher {
        private final EditText editText;
        private final Consumer<String> validationCallback;

        SimpleTextWatcher(EditText editText, Consumer<String> validationCallback) {
            this.editText = editText;
            this.validationCallback = validationCallback;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // TODO document why this method is empty
        }

        @Override
        public void afterTextChanged(Editable editable) {
            validationCallback.accept(editable.toString());
        }
    }

    interface Consumer<T> {
        void accept(T t);
    }
}
