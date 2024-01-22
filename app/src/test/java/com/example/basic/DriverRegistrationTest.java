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
        assert (!result);
    }

    @Test
    public void testIsDriverAlreadyRegistered_True() {
        String licenseNo = "ABC123";
        when(mockDatabaseHelper.isDriverAlreadyRegistered(licenseNo)).thenReturn(true);

        boolean result = driverRegistration.isDriverAlreadyRegistered(licenseNo);

        verify(mockDatabaseHelper).isDriverAlreadyRegistered(licenseNo);
        assert (result);
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
        // Call onRegisterClick and assert the expected behavior
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
    public void onRegisterClick() {
    }

    @Test
    public void onActivityResult() {
    }
}