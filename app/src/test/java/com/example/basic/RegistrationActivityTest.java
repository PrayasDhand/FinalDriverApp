package com.example.basic;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistrationActivityTest {

    @Mock
    private DatabaseHelper1 mockDatabaseHelper;

    @Mock
    private DatabaseReference mockDatabaseReference;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SQLiteDatabase mockSQLiteDatabase;

    @InjectMocks
    private RegistrationActivity registrationActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        registrationActivity = spy(new RegistrationActivity());
        when(mockDatabaseHelper.getWritableDatabase()).thenReturn(mockSQLiteDatabase);
        registrationActivity.dbHelper = mockDatabaseHelper;
    }



    @Test
    public void testIsValidName_ValidName() {
        assertTrue(registrationActivity.isValidName("John"));
    }

    @Test
    public void testIsValidName_InvalidName() {
        assertFalse(registrationActivity.isValidName("John123"));
    }

    @Test
    public void testIsStrongPassword_ValidPassword() {
        assertTrue(registrationActivity.isStrongPassword("P@ssw0rd"));
    }

    @Test
    public void testIsStrongPassword_InvalidPassword() {
        assertFalse(registrationActivity.isStrongPassword("password"));
    }

//    @Test
//    public void testIsUserAlreadyRegistered_UserExists() {
//        when(dbHelper.getReadableDatabase()).thenReturn(sqLiteDatabase);
//
//        Cursor cursor = mock(Cursor.class);
//        when(sqLiteDatabase.query(anyString(), any(), anyString(), any(), any(), any(), any())).thenReturn(cursor);
//        when(cursor.moveToFirst()).thenReturn(true);
//
//        assertTrue(registrationActivity.isUserAlreadyRegistered("test@example.com"));
//
//        verify(cursor).close();
//        verify(sqLiteDatabase).close();
//    }
//
//    @Test
//    public void testIsUserAlreadyRegistered_UserDoesNotExist() {
//        when(dbHelper.getReadableDatabase()).thenReturn(sqLiteDatabase);
//
//        Cursor cursor = mock(Cursor.class);
//        when(sqLiteDatabase.query(anyString(), any(), anyString(), any(), any(), any(), any())).thenReturn(cursor);
//        when(cursor.moveToFirst()).thenReturn(false);
//
//        assertFalse(registrationActivity.isUserAlreadyRegistered("test@example.com"));
//
//        verify(cursor).close();
//        verify(sqLiteDatabase).close();
//    }

    @Test
    public void testIsUserAlreadyRegistered_WhenUserExists_ReturnsTrue() {
        // Arrange
        String existingEmail = "existing@example.com";

        // Mock the cursor with a result
        when(mockSQLiteDatabase.query(
                any(String.class),
                any(String[].class),
                any(String.class),
                any(String[].class),
                any(String.class),
                any(String.class),
                any(String.class)
        )).thenReturn(mock(Cursor.class));

        // Act
        boolean result = registrationActivity.isUserAlreadyRegistered(existingEmail);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsUserAlreadyRegistered_WhenUserDoesNotExist_ReturnsFalse() {
        // Arrange
        String nonExistingEmail = "nonexisting@example.com";

        // Mock the cursor with no result
        when(mockSQLiteDatabase.query(
                any(String.class),
                any(String[].class),
                any(String.class),
                any(String[].class),
                any(String.class),
                any(String.class),
                any(String.class)
        )).thenReturn(null);

        // Act
        boolean result = registrationActivity.isUserAlreadyRegistered(nonExistingEmail);

        // Assert
        assertFalse(result);
    }

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
        EditText nameEditText = mock(EditText.class);

        // Test with an empty name
        when(nameEditText.getText().toString()).thenReturn("");
        registrationActivity.validateName(nameEditText.getText().toString());
        assertFalse(nameEditText.getError() != null);

        // Test with a non-empty name
        when(nameEditText.getText().toString()).thenReturn("John Doe");
        registrationActivity.validateName(nameEditText.getText().toString());
        assertTrue(nameEditText.getError() == null);
    }

    @Test
    public void testValidateEmail() {
        RegistrationActivity registrationActivity = new RegistrationActivity();
        EditText emailEditText = mock(EditText.class);

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

    @Test
    public void testValidateContactNumber() {
        RegistrationActivity registrationActivity = new RegistrationActivity();
        EditText contactEditText = mock(EditText.class);

        // Test with an empty contact number
        when(contactEditText.getText().toString()).thenReturn("");
        registrationActivity.validateContact(contactEditText.getText().toString());
        assertTrue(contactEditText.getError() != null);

        // Test with a valid contact number
        when(contactEditText.getText().toString()).thenReturn("1234567890");
        registrationActivity.validateContact(contactEditText.getText().toString());
        assertTrue(contactEditText.getError() == null);

        // Test with an invalid contact number
        when(contactEditText.getText().toString()).thenReturn("invalid-contact");
        registrationActivity.validateContact(contactEditText.getText().toString());
        assertTrue(contactEditText.getError() != null);
    }


}
