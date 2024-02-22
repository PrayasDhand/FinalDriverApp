package com.example.basic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public SharedPreferences sharedPreferences;
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

        FirebaseApp.initializeApp(this);

        try (DatabaseHelper1 databaseHelper = new DatabaseHelper1(this)) {

            if (isLoggedIn()) {
                redirectToRegisterDriver();
            }

            Button loginBtn = findViewById(R.id.loginBtn);
            TextView registerTextView = findViewById(R.id.textView6);
            EditText emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
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

            loginBtn.setOnClickListener(v -> {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                authenticateFirebase(email, password);
            });
            forgotPasswordTextView.setOnClickListener(v -> showForgotPasswordDialog());

            emailEditText.addTextChangedListener(new SimpleTextWatcher(emailEditText, this::validateEmail));
            passwordEditText.addTextChangedListener(new SimpleTextWatcher(passwordEditText, this::validatePassword));
        }
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.alertTitleTextView);
        EditText emailEditText = dialogView.findViewById(R.id.alertEmailEditText);
        AppCompatButton resetPasswordButton = dialogView.findViewById(R.id.resetPasswordButton);
        AppCompatButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        titleTextView.setTypeface(null, Typeface.BOLD);

        AlertDialog alertDialog = builder.create();

        // Set OnClickListener for the "Cancel" button to dismiss the dialog
        cancelButton.setOnClickListener(view -> alertDialog.dismiss());

        resetPasswordButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            if (!email.isEmpty() && isValidEmail(email)) {
                resetPassword(email);
                alertDialog.dismiss(); // Dismiss the dialog after initiating the password reset
            } else {
                Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }

    private void resetPassword(String email) {
        // Use Firebase password reset mechanism
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Password reset email sent. Please check your inbox.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to send password reset email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
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
        if (email.isEmpty() || password.isEmpty()) {
            // Show toast for empty email or password
            Toast.makeText(LoginActivity.this, "Please enter details", Toast.LENGTH_SHORT).show();
            return; // Return from the method to prevent further execution
        }

        // Authenticate with Firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Authentication successful
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String fullName = "";  // Retrieve user details from Firebase if needed
                            String contact = "";

                            saveUserDataToSharedPreferences(fullName, email, password, contact);

                            saveLoginState();
                            redirectToRegisterDriver();
                        }
                    } else {
                        // If authentication fails, display a message to the user.
                        Log.e(TAG, "Authentication failed: " + task.getException());
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void saveUserDataToSharedPreferences(String fullName, String email, String password, String contact) {
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
