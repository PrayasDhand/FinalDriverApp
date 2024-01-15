package com.example.basic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverRegistration extends AppCompatActivity {
    private static final int PICK_DRIVER_IMAGE_REQUEST = 1;
    private static final int PICK_LICENSE_IMAGE_REQUEST = 2;
    private ImageView imageView;
    private CircleImageView circularImageViewDriver;
    private ImageView licenseImageView;
    private TextInputEditText dobEditText;
    private TextInputLayout datePickerLayout;
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private EditText vehicleEditText;
    private DatabaseHelper databaseHelper;
    private TesseractHelper ocrHelper;
    private EditText contactEditText;
    private EditText licenseNoEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set the window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register_driver);

        imageView = findViewById(R.id.imageView);
        licenseImageView = findViewById(R.id.licenseImageView);
        fullNameEditText = findViewById(R.id.fname2);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        dobEditText = findViewById(R.id.dobEditText);
        datePickerLayout = findViewById(R.id.datePickerLayout);
        dobEditText.setOnClickListener(v -> showDatePickerDialog());

        vehicleEditText = findViewById(R.id.vehicleType);
        licenseNoEditText = findViewById(R.id.licenseNo);
        contactEditText = findViewById(R.id.contact);
        circularImageViewDriver = findViewById(R.id.circularImageViewDriver);
        circularImageViewDriver.setOnClickListener(this::onChooseDriverImageClick);


        AppCompatButton chooseLicenseImageBtn = findViewById(R.id.chooseLicenseImageBtn);
        chooseLicenseImageBtn.setOnClickListener(this::onChooseLicenseImageClick);
        databaseHelper = new DatabaseHelper(this);
        ocrHelper = new TesseractHelper(this, "eng");

        fullNameEditText.addTextChangedListener(new SimpleTextWatcher(fullNameEditText, this::validateFullName));
        emailEditText.addTextChangedListener(new SimpleTextWatcher(emailEditText, this::validateEmail));
        passwordEditText.addTextChangedListener(new SimpleTextWatcher(passwordEditText, this::validatePassword));
        vehicleEditText.addTextChangedListener(new SimpleTextWatcher(vehicleEditText, this::validateVehicle));

        contactEditText.addTextChangedListener(new SimpleTextWatcher(contactEditText, this::validateContact));
    }

    private void validateFullName(String fullName) {
        if (fullName.isEmpty()) {
            fullNameEditText.setError("Please enter your full name");
        } else if (fullName.length() > 30) {
            fullNameEditText.setError("Full name should be at most 30 characters");
        } else {
            fullNameEditText.setError(null);
        }
    }

    private void validateEmail(String email) {
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

    private void validatePassword(String password) {
        if (password.isEmpty() || !isStrongPassword(password)) {
            passwordEditText.setError("Password should contain one uppercase, one lowercase, and one special character");
        } else {
            passwordEditText.setError(null);
        }
    }

    boolean isStrongPassword(String password) {
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[!@#$%^&*()-_+=<>?].*");
    }

    private void validateVehicle(String vehicle) {
        if (vehicle.isEmpty()) {
            vehicleEditText.setError("Please enter the vehicle type");
        } else {
            vehicleEditText.setError(null);
        }
    }
    private void validateContact(String contact) {
        if (contact.isEmpty() || contact.length() != 10) {
            contactEditText.setError("Phone number should be of 10 digits");
        } else {
            contactEditText.setError(null);
        }
    }
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Calculate the minimum birthdate for 18 years old
        int minBirthYear = currentYear - 18;
        int minBirthMonth = currentMonth;
        int minBirthDay = currentDay;

        // Create the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            // Check if the selected date is 18 years ago or older
            if (year < minBirthYear || (year == minBirthYear && monthOfYear < minBirthMonth) || (year == minBirthYear && monthOfYear == minBirthMonth && dayOfMonth < minBirthDay)) {
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                dobEditText.setText(selectedDate);
            } else {
                // Show a toast or alert indicating that the selected date is not allowed
                Toast.makeText(this, "You must be 18 years or older to register.", Toast.LENGTH_SHORT).show();
                // Optionally, you can clear the selected date
                dobEditText.setText("");
            }
        }, minBirthYear, minBirthMonth, minBirthDay);

        // Set the maximum date to the current date
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
    private void onChooseDriverImageClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Choose from gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, PICK_DRIVER_IMAGE_REQUEST);
                        break;

                    case 1:
                        // Open the camera
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, PICK_DRIVER_IMAGE_REQUEST);
                        } else {
                            Toast.makeText(DriverRegistration.this, "Camera not available", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    private void onChooseLicenseImageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_LICENSE_IMAGE_REQUEST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ocrHelper.onDestroy();
    }



    public void onRegisterClick(View view) {
        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        String dob = dobEditText.getText().toString();

        String licenseNo = licenseNoEditText.getText().toString();
        String vehicleType = vehicleEditText.getText().toString();
        String contact = contactEditText.getText().toString();


        if (licenseNo.isEmpty()) {
            Toast.makeText(this, "Please enter the license number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isDriverAlreadyRegistered(licenseNo)) {
            // Display a toast indicating that the driver already exists
            Toast.makeText(this, "Driver with this license number already exists", Toast.LENGTH_SHORT).show();
            return; // Stop registration process
        }

        String ocrResultFromImage = ocrHelper.getOCRResult();

        if (ocrResultFromImage != null && !ocrResultFromImage.isEmpty()) {
            try {
                // Log the OCR result before extraction
                Log.d("OCR", "Original OCR Result: " + ocrResultFromImage);

                boolean detailsVerified = verifyDetailsWithOCR(ocrResultFromImage, licenseNo);

                if (detailsVerified) {
                    // Proceed with registration
                    boolean success = databaseHelper.insertUser(fullName, email, password, dob, contact, licenseNo, vehicleType);

                    if (success) {
                        Intent intent = new Intent(DriverRegistration.this,WelcomeDriver.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(this, "Driver registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to register driver", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error: License numbers do not match. Please check and try again", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("OCR", "Exception during registration: " + e.getMessage());
                Toast.makeText(this, "Error: Registration failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Log the reason why OCR result extraction failed
            Log.e("OCR", "Error: Unable to extract OCR result. Result is null or empty.");

            Toast.makeText(this, "Error: Unable to extract OCR result", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDriverAlreadyRegistered(String licenseNo) {
        return databaseHelper.isDriverAlreadyRegistered(licenseNo);
    }

    private boolean verifyDetailsWithOCR(String ocrResult, String licenseNo) {
        // Check if the license number is present in the OCR result
        return ocrResult.toLowerCase().contains(licenseNo.toLowerCase());
    }


//    private String extractLicenseNumberFromOCR(String ocrResult) {
//        Log.d("OCR", "Original OCR Result: " + ocrResult);
//        // Use a regular expression to find the pattern "DL No. 1234567890"
//        // Adjust the regular expression based on variations in your OCR results
//        String licenseNumberPattern = "(?i)\\b(\\w+\\s*\\w+\\s*\\w+)\\b";        Pattern pattern = Pattern.compile(licenseNumberPattern);
//        Matcher matcher = pattern.matcher(ocrResult);
//
//        if (matcher.find()) {
//            // Extract the license number from the matched pattern
//            return matcher.group(1).trim();
//        } else {
//            // Handle the case where the license number is not found
//            return null;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DRIVER_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            handleImageSelection(data.getData());
        } else if (requestCode == PICK_LICENSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            handleLicenseImageSelection(data.getData());
        }
    }
    private void handleImageSelection(Uri imageUri) {
        circularImageViewDriver.setImageURI(imageUri);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            // Perform OCR or any other processing for the driver's image
            // ocrHelper.performOCRForDriverImage(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleLicenseImageSelection(Uri imageUri) {
        licenseImageView.setImageURI(imageUri);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            // Perform OCR or any other processing for the license image
            ocrHelper.performOCR(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("OCR", "FileNotFoundException: " + e.getMessage());
            Toast.makeText(this, "Error: File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OCR", "IOException: " + e.getMessage());
            Toast.makeText(this, "Error: Unable to read image file", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("OCR", "Exception: " + e.getMessage());
            Toast.makeText(this, "Error: OCR processing failed", Toast.LENGTH_SHORT).show();
        }
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


//    private String[] extractUserInfoFromOCR(String ocrResult) {
//        // Split the OCR result into lines
//        String[] lines = ocrResult.split("\n");
//
//        // Check if there are at least 5 lines (adjust as needed based on your OCR result format)
//        if (lines.length >= 5) {
//            String firstName = lines[0].trim();
//            String lastName = lines[1].trim();
//            String ageStr = lines[2].trim(); // Assuming age is on the third line
//            String email = lines[3].trim();
//            String password = lines[4].trim();
//
//            // Validate or process the extracted data as needed
//
//            return new String[]{firstName, lastName, ageStr, email, password};
//        } else {
//            // Handle the case where the OCR result does not have enough lines
//            return null;
//        }
//    }
}