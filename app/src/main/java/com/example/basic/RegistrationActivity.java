package com.example.basic;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    DatabaseHelper1 dbHelper = new DatabaseHelper1(this);
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
        setContentView(R.layout.register_page);

        // Set up the UI components
        EditText nameEditText = findViewById(R.id.edText1);
        EditText emailEditText = findViewById(R.id.editText2);
        passwordEditText = findViewById(R.id.editText3);
        EditText contactEditText = findViewById(R.id.editText4);

        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Toggle password visibility based on CheckBox state
            togglePasswordVisibility(isChecked);
        });



        // Example: Set up a TextWatcher for name validation
        nameEditText.addTextChangedListener(new SimpleTextWatcher(nameEditText, this::validateName));

        // Example: Set up a TextWatcher for email validation
        emailEditText.addTextChangedListener(new SimpleTextWatcher(emailEditText, this::validateEmail));

        // Example: Set up a TextWatcher for password validation
        passwordEditText.addTextChangedListener(new SimpleTextWatcher(passwordEditText, this::validatePassword));

        // Example: Set up a TextWatcher for contact validation
        contactEditText.addTextChangedListener(new SimpleTextWatcher(contactEditText, this::validateContact));

        // Example: Set up a button click listener
        findViewById(R.id.regBtn).setOnClickListener(v -> {
            // Call the method to handle registration when the button is clicked
            handleRegistration();
        });
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


    void validateName(String name) {
        EditText nameEditText = findViewById(R.id.edText1);

        if (name.isEmpty()) {
            nameEditText.setError("Please enter your name");
        } else if (!isValidName(name)) {
            nameEditText.setError("You can't enter numbers or special characters (except ') in your name");
        } else if (name.length() > 30) {
            nameEditText.setError("Name should be at most 30 characters");
        } else {
            nameEditText.setError(null);
        }
    }

    boolean isValidName(String name) {
        return name.matches("^[a-zA-Z' ]+");
    }

    void validateEmail(String email) {
        EditText emailEditText = findViewById(R.id.editText2);
        if (email.isEmpty() || !isValidEmail(email)) {
            emailEditText.setError("Please enter a valid email address");
        } else {
            emailEditText.setError(null);
        }
    }

    private void validatePassword(String password) {
        EditText passwordEditText = findViewById(R.id.editText3);
        int minLength = 5;
        int maxLength = 16;

        if (password.isEmpty() || !isStrongPassword(password) || password.length() < minLength || password.length() > maxLength) {
            passwordEditText.setError("Password should be between 5 and 16 characters and contain one uppercase, one lowercase, and one special character");
        } else {
            passwordEditText.setError(null);
        }
    }


    public void validateContact(String contact) {
        EditText contactEditText = findViewById(R.id.editText4);
        if (contact.isEmpty() || !isValidContact(contact)) {
            contactEditText.setError("Please enter a valid contact number with country code (e.g., +911234567890)");
        } else {
            contactEditText.setError(null);
        }
    }
    boolean isValidContact(String contact) {
        // Allowing optional '+' followed by 1 to 15 digits
        return contact.matches("^\\+?\\d{1,12}$");
    }

    void handleRegistration() {
        // Retrieve values from EditText fields
        EditText nameEditText = findViewById(R.id.edText1);
        EditText emailEditText = findViewById(R.id.editText2);
        EditText passwordEditText = findViewById(R.id.editText3);
        EditText contactEditText = findViewById(R.id.editText4);

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String contact = contactEditText.getText().toString();

        // Check if any field is empty before proceeding with registration
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isUserAlreadyRegistered(email)) {
            Toast.makeText(this, "User with this email is already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Driver object
        Driver driver = new Driver(0, name, email, password, contact);

        // Register user with Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Firebase Authentication successful
                        saveUserDataToSharedPreferences(name, email, password, contact);

                        // Save user data to SQLite database
                        saveUserDataToSQLite(driver);

                        // Save user data to Firebase Realtime Database
                        saveUserDataToFirebase(driver);

                        // Show a toast indicating successful registration
                        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                        // Use a Handler to delay the redirection to LoginActivity
                        new Handler().postDelayed(() -> {
                            // Redirect to LoginActivity
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);

                            // Finish the current activity to remove it from the stack
                            finish();
                        }, 2000); // Delay in milliseconds (e.g., 2000 milliseconds = 2 seconds)
                    } else {
                        // If registration fails, display a message to the user.
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void saveUserDataToFirebase(Driver driver) {
        // Open the Firebase Realtime Database reference
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("drivers");

        // Push the data to the Firebase Realtime Database
        String firebaseKey = firebaseRef.push().getKey();
        DatabaseReference driverRef = firebaseRef.child(firebaseKey);
        driverRef.setValue(driver);
    }

    void saveUserDataToSQLite(Driver driver) {
        // Open the SQLite database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues to insert data into the SQLite database
        ContentValues values = new ContentValues();
        values.put("name", driver.getName());
        values.put("email", driver.getEmail());
        values.put("password", driver.getPassword());
        values.put("contact", driver.getContact());

        // Insert the data into the SQLite database
        db.insert("Drivers", null, values);

        // Close the SQLite database
        db.close();
    }

    void saveUserDataToSharedPreferences(String name, String email, String password, String contact) {
        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("fullName", name);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("contact", contact);

        editor.apply();
    }

    boolean isUserAlreadyRegistered(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"id"};

        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                "Drivers",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isUserRegistered = cursor.moveToFirst();

        // Close the cursor and database
        cursor.close();
        db.close();

        return isUserRegistered;
    }

    boolean isStrongPassword(String password) {
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[!@#$%^&*()-_+=<>?].*");
    }

    boolean isValidEmail(String email) {
        String emailPattern = "^[a-z][a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$";
        return email.matches(emailPattern);
    }

    public void redirectToLoginPage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: finish the current activity to prevent going back to it
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
