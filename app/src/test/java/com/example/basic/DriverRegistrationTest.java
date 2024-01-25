package com.example.basic;

import android.content.Context;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DriverRegistrationTest {
    @Mock
    private DatabaseHelper mockDatabaseHelper;

    @Mock
    private TesseractHelper mockOcrHelper;

    @Mock
    private EditText mockFullNameEditText;

    @Mock
    private EditText mockEmailEditText;

    @Mock
    private EditText mockPasswordEditText;

    @Mock
    private EditText mockVehicleEditText;

    @Mock
    private EditText mockContactEditText;

    @Mock
    private EditText mockLicenseNoEditText;

    @Mock
    private Context mockContext;
    private DriverRegistration driverRegistration;

    @Before
    public void setUp() throws Exception {
        driverRegistration = new DriverRegistration();
        driverRegistration.databaseHelper = mockDatabaseHelper;
        driverRegistration.ocrHelper = mockOcrHelper;

        mockFullNameEditText = mock(EditText.class);
        mockEmailEditText = mock(EditText.class);
        mockPasswordEditText = mock(EditText.class);
        mockVehicleEditText = mock(EditText.class);
        mockContactEditText = mock(EditText.class);
        mockLicenseNoEditText = mock(EditText.class);

        driverRegistration.fullNameEditText = mockFullNameEditText;
        driverRegistration.emailEditText = mockEmailEditText;
        driverRegistration.passwordEditText = mockPasswordEditText;
        driverRegistration.vehicleEditText = mockVehicleEditText;
        driverRegistration.contactEditText = mockContactEditText;
    }

    @Test
    public void testValidateFullName_Empty() {
        when(mockFullNameEditText.getText().toString()).thenReturn("");
        driverRegistration.validateFullName(mockFullNameEditText.getText().toString());
        verify(mockFullNameEditText).setError("Please enter your name");
    }

    @Test
    public void testValidateFullName_InvalidName() {
        when(mockFullNameEditText.getText().toString()).thenReturn("123");
        driverRegistration.validateFullName(mockFullNameEditText.getText().toString());
        verify(mockFullNameEditText).setError("You can't enter numbers or special characters (except ') in your name");
    }

    @Test
    public void testIsDriverAlreadyRegistered_False() {
        String licenseNo = "ABC123";
        when(mockDatabaseHelper.isDriverAlreadyRegistered(licenseNo)).thenReturn(false);

        boolean result = driverRegistration.isDriverAlreadyRegistered(licenseNo);

        verify(mockDatabaseHelper).isDriverAlreadyRegistered(licenseNo);
        assertFalse(result);
    }

    @Test
    public void testIsDriverAlreadyRegistered_True() {
        String licenseNo = "ABC123";
        when(mockDatabaseHelper.isDriverAlreadyRegistered(licenseNo)).thenReturn(true);

        boolean result = driverRegistration.isDriverAlreadyRegistered(licenseNo);

        verify(mockDatabaseHelper).isDriverAlreadyRegistered(licenseNo);
        assertTrue(result);
    }


    @Test
    public void isValidEmail_invalidEmail() {
        assertFalse(driverRegistration.isValidEmail("invalidemail"));
    }

    @Test
    public void onCreate() {
    }

    @Test
    public void isValidEmail() {
        assertTrue(driverRegistration.isValidEmail("valid@email.com"));
    }

    @Test
    public void isStrongPassword() {
        assertTrue(driverRegistration.isStrongPassword("StrongPassword!1"));
    }

    @Test
    public void onRegisterClick_emptyLicenseNo() {
        driverRegistration.licenseNoEditText.setText("");
        driverRegistration.onRegisterClick(null);
        // Assert that an appropriate error message is displayed or handle the behavior as needed
    }

    @Test
    public void onRegisterClick_existingDriver() {
        // Mock a scenario where the driver with the license number already exists
        driverRegistration.licenseNoEditText.setText("existingLicense");
        when(mockDatabaseHelper.isDriverAlreadyRegistered("existingLicense")).thenReturn(true);

        // Call onRegisterClick and assert the expected behavior
        driverRegistration.onRegisterClick(null);
        // Assert that an appropriate error message is displayed or handle the behavior as needed
    }


    @Test
    public void onDestroy() {
        driverRegistration.onDestroy();
    }

    @Test
    public void validateFullName_emptyFullName() {
        driverRegistration.fullNameEditText.setText("");
        driverRegistration.validateFullName(driverRegistration.fullNameEditText.getText().toString());
        assertTrue(driverRegistration.fullNameEditText.getError() != null); // Assert that an error message is displayed for empty full name
    }

    @Test
    public void validateFullName_validFullName() {
        driverRegistration.fullNameEditText.setText("John Doe");
        driverRegistration.validateFullName(driverRegistration.fullNameEditText.getText().toString());
        assertNull(driverRegistration.fullNameEditText.getError()); // Assert that no error message is displayed for a valid full name
    }

    @Test
    public void validateEmail_emptyEmail() {
        driverRegistration.emailEditText.setText("");
        driverRegistration.validateEmail(driverRegistration.emailEditText.getText().toString());
        assertTrue(driverRegistration.emailEditText.getError() != null); // Assert that an error message is displayed for empty email
    }

    @Test
    public void validateEmail_invalidEmail() {
        driverRegistration.emailEditText.setText("invalidemail");
        driverRegistration.validateEmail(driverRegistration.emailEditText.getText().toString());
        assertTrue(driverRegistration.emailEditText.getError() != null); // Assert that an error message is displayed for an invalid email
    }

    @Test
    public void validateEmail_validEmail() {
        driverRegistration.emailEditText.setText("valid@email.com");
        driverRegistration.validateEmail(driverRegistration.emailEditText.getText().toString());
        assertNull(driverRegistration.emailEditText.getError()); // Assert that no error message is displayed for a valid email
    }

    @Test
    public void validatePassword_emptyPassword() {
        driverRegistration.passwordEditText.setText("");
        driverRegistration.validatePassword(driverRegistration.passwordEditText.getText().toString());
        assertTrue(driverRegistration.passwordEditText.getError() != null); // Assert that an error message is displayed for empty password
    }

    @Test
    public void validatePassword_weakPassword() {
        driverRegistration.passwordEditText.setText("weakpassword");
        driverRegistration.validatePassword(driverRegistration.passwordEditText.getText().toString());
        assertTrue(driverRegistration.passwordEditText.getError() != null); // Assert that an error message is displayed for a weak password
    }

    @Test
    public void validatePassword_strongPassword() {
        driverRegistration.passwordEditText.setText("StrongPassword!1");
        driverRegistration.validatePassword(driverRegistration.passwordEditText.getText().toString());
        assertNull(driverRegistration.passwordEditText.getError()); // Assert that no error message is displayed for a strong password
    }

    @Test
    public void validateVehicle_emptyVehicle() {
        driverRegistration.vehicleEditText.setText("");
        driverRegistration.validateVehicle(driverRegistration.vehicleEditText.getText().toString());
        assertTrue(driverRegistration.vehicleEditText.getError() != null); // Assert that an error message is displayed for empty vehicle type
    }

    @Test
    public void validateVehicle_validVehicle() {
        driverRegistration.vehicleEditText.setText("Car");
        driverRegistration.validateVehicle(driverRegistration.vehicleEditText.getText().toString());
        assertNull(driverRegistration.vehicleEditText.getError()); // Assert that no error message is displayed for a valid vehicle type
    }

    @Test
    public void validateContact_emptyContact() {
        driverRegistration.contactEditText.setText("");
        driverRegistration.validateContact(driverRegistration.contactEditText.getText().toString());
        assertTrue(driverRegistration.contactEditText.getError() != null); // Assert that an error message is displayed for empty contact
    }

    @Test
    public void validateContact_invalidContact() {
        driverRegistration.contactEditText.setText("123");
        driverRegistration.validateContact(driverRegistration.contactEditText.getText().toString());
        assertTrue(driverRegistration.contactEditText.getError() != null); // Assert that an error message is displayed for an invalid contact
    }

    @Test
    public void validateContact_validContact() {
        driverRegistration.contactEditText.setText("1234567890");
        driverRegistration.validateContact(driverRegistration.contactEditText.getText().toString());
        assertNull(driverRegistration.contactEditText.getError()); // Assert that no error message is displayed for a valid contact
    }



    @Test
    public void testValidateFullName_TooLongName() {
        when(mockFullNameEditText.getText().toString()).thenReturn("John Doe John Doe John Doe John Doe John Doe");
        driverRegistration.validateFullName(mockFullNameEditText.getText().toString());
        verify(mockFullNameEditText).setError("Name should be at most 30 characters");
    }

    @Test
    public void testValidateEmail_InvalidEmail() {
        when(mockEmailEditText.getText().toString()).thenReturn("invalidemail");
        driverRegistration.validateEmail(mockEmailEditText.getText().toString());
        verify(mockEmailEditText).setError("Please enter a valid email address");
    }

    @Test
    public void testValidatePassword_WeakPassword() {
        when(mockPasswordEditText.getText().toString()).thenReturn("weakpassword");
        driverRegistration.validatePassword(mockPasswordEditText.getText().toString());
        verify(mockPasswordEditText).setError("Password should be between 5 and 16 characters and contain one uppercase, one lowercase, and one special character");
    }

    @Test
    public void testValidateVehicle_InvalidVehicle() {
        when(mockVehicleEditText.getText().toString()).thenReturn("Car123");
        driverRegistration.validateVehicle(mockVehicleEditText.getText().toString());
        verify(mockVehicleEditText).setError("Please enter a valid vehicle type with only alphabets");
    }

    @Test
    public void testValidateContact_InvalidContact() {
        when(mockContactEditText.getText().toString()).thenReturn("123");
        driverRegistration.validateContact(mockContactEditText.getText().toString());
        verify(mockContactEditText).setError("Please enter a valid contact number with country code (e.g., +911234567890)");
    }

    @Test
    public void testValidateLicenseNumber_InvalidLicenseNumber() {
        when(mockLicenseNoEditText.getText().toString()).thenReturn("License@123");
        driverRegistration.validateLicenseNumber(mockLicenseNoEditText.getText().toString());
        verify(mockLicenseNoEditText).setError("Please enter a valid license number without special characters");
    }

    @Test
    public void testValidateLicenseNumber_ValidLicenseNumber() {
        when(mockLicenseNoEditText.getText().toString()).thenReturn("ABCD1234");
        driverRegistration.validateLicenseNumber(mockLicenseNoEditText.getText().toString());
        assertNull(mockLicenseNoEditText.getError());
    }

    @Test
    public void onRegisterClick() {
    }

    @Test
    public void onActivityResult() {
    }
}