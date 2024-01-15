package com.example.basic;//package com.example.basic;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public class RegistrationActivityTest {
//
//    @Test
//    public void testHandleRegistrationWithEmptyFields() {
//        RegistrationActivity activity = new RegistrationActivity();
//
//        // Call handleRegistration with empty fields
//        activity.handleRegistration();
//
//        // Check if the activity displays a toast about filling in all fields
//        // Note: This assumes that the toast message is set in your handleRegistration method
//        assertEquals(false, !activity.showToastMessage.contains("Please fill in all fields"));
//    }
//
//    @Test
//    public void testHandleRegistrationWithValidData() {
//        RegistrationActivity activity = new RegistrationActivity();
//
//        // Set up valid data
//        activity.nameEditText.setText("John Doe");
//        activity.emailEditText.setText("john.doe@example.com");
//        activity.passwordEditText.setText("password123");
//        activity.contactEditText.setText("1234567890");
//
//        // Call handleRegistration with valid data
//        activity.handleRegistration();
//
//        // Check if the activity displays a toast about successful data save
//        // Note: This assumes that the toast message is set in your handleRegistration method
//        assertTrue(activity.showToastMessage.contains("Data saved successfully"));
//    }
//
//    @Test
//    public void testHandleRegistrationWithEmptyName() {
//        RegistrationActivity activity = new RegistrationActivity();
//
//        // Set up data with an empty name
//        activity.emailEditText.setText("john.doe@example.com");
//        activity.passwordEditText.setText("password123");
//        activity.contactEditText.setText("1234567890");
//
//        // Call handleRegistration with empty name
//        activity.handleRegistration();
//
//        // Check if the activity displays an error for the name field
//        // Note: This assumes that the error message is set in your handleRegistration method
//        assertTrue(activity.nameEditText.getError().toString().contains("Please enter your name"));
//
//        // Check if the activity does not display a toast about successful data save
//        assertFalse(activity.showToastMessage.contains("Data saved successfully"));
//    }
//
//    // Similar test methods can be written for other validation scenarios
//
//    // Additional Notes:
//    // - These test cases assume that your activity has EditText fields for name, email, password, and contact.
//    // - Make sure to handle UI updates and asynchronous tasks properly in your activity for accurate testing.
//    // - These tests are simplified and may need modification based on the actual implementation details.
//}
import android.widget.EditText;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RegistrationActivityTest {

    @Test
    public void testIsStrongPassword() {
        RegistrationActivity registrationActivity = new RegistrationActivity();

        // Test with a weak password
        assertFalse(registrationActivity.isStrongPassword("password"));

        // Test with a strong password
        assertTrue(registrationActivity.isStrongPassword("Password123!"));
    }

    @Test
    public void testIsValidEmail() {
        RegistrationActivity registrationActivity = new RegistrationActivity();

        // Test with a valid email
        assertTrue(registrationActivity.isValidEmail("test@example.com"));

        // Test with an invalid email
        assertFalse(registrationActivity.isValidEmail("invalid-email"));
    }

    @Test
    public void testValidateName() {
        RegistrationActivity registrationActivity = new RegistrationActivity();
        EditText nameEditText = Mockito.mock(EditText.class);

        // Test with an empty name
        when(nameEditText.getText().toString()).thenReturn("");
        registrationActivity.validateName(nameEditText.getText().toString());
        assertTrue(nameEditText.getError() != null);

        // Test with a non-empty name
        when(nameEditText.getText().toString()).thenReturn("John Doe");
        registrationActivity.validateName(nameEditText.getText().toString());
        assertTrue(nameEditText.getError() == null);
    }

    @Test
    public void testValidateEmail() {
        RegistrationActivity registrationActivity = new RegistrationActivity();
        EditText emailEditText = Mockito.mock(EditText.class);

        // Test with an empty email
        when(emailEditText.getText().toString()).thenReturn("");
        registrationActivity.validateEmail(emailEditText.getText().toString());
        assertTrue(emailEditText.getError() != null);

        // Test with an invalid email
        when(emailEditText.getText().toString()).thenReturn("invalid-email");
        registrationActivity.validateEmail(emailEditText.getText().toString());
        assertTrue(emailEditText.getError() != null);

        // Test with a valid email
        when(emailEditText.getText().toString()).thenReturn("test@example.com");
        registrationActivity.validateEmail(emailEditText.getText().toString());
        assertTrue(emailEditText.getError() == null);
    }


}
