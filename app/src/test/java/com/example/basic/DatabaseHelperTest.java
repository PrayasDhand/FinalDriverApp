package com.example.basic;//package com.example.basic;
//
//import android.content.Context;
//import androidx.test.platform.app.InstrumentationRegistry;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DatabaseHelperTest {
//
//    @Mock
//    private Context mockContext;
//
//    private DatabaseHelper databaseHelper;
//
//    @Before
//    public void setUp() {
//        // Use the mocked context
//        databaseHelper = new DatabaseHelper(mockContext);
//    }
//
//    @Test
//    public void testInsertUser() {
//        // Insert a user with valid data
//        String fullName = "John Doe";
//        String email = "john.doe@example.com";
//        String password = "password123";
//        String dob = "1990-01-01";
//        String contact = "1234567890";
//        String address = "123 Main St";
//        String licenseNo = "ABC123";
//        String vehicleType = "Car";
//
//        assertTrue(databaseHelper.insertUser(fullName, email, password, dob, contact, address, licenseNo, vehicleType));
//
//        // Insert a user with invalid data (null values)
//        String fullNameNull = null;
//        String emailNull = null;
//        String passwordNull = null;
//        String dobNull = null;
//        String contactNull = null;
//        String addressNull = null;
//        String licenseNoNull = null;
//        String vehicleTypeNull = null;
//
//        assertFalse(databaseHelper.insertUser(fullNameNull, emailNull, passwordNull, dobNull, contactNull, addressNull, licenseNoNull, vehicleTypeNull));
//    }
//
//    @Test
//    public void testDoesUserExist() {
//        // Insert a user with valid data
//        String fullName = "John Doe";
//        String email = "john.doe@example.com";
//        String password = "password123";
//        String dob = "1990-01-01";
//        String contact = "1234567890";
//        String address = "123 Main St";
//        String licenseNo = "ABC123";
//        String vehicleType = "Car";
//
//        databaseHelper.insertUser(fullName, email, password, dob, contact, address, licenseNo, vehicleType);
//
//        // Check if the user exists
//        String ocrFirstName = "John";
//        String ocrLastName = "Doe";
//        String ocrEmail = "john.doe@example.com";
//        String ocrPassword = "password123";
//
//        assertTrue(databaseHelper.doesUserExist(ocrFirstName, ocrLastName, ocrEmail, ocrPassword));
//
//        // Check if the user doesn't exist
//        String ocrFirstNameNull = null;
//        String ocrLastNameNull = null;
//        String ocrEmailNull = null;
//        String ocrPasswordNull = null;
//
//        assertFalse(databaseHelper.doesUserExist(ocrFirstNameNull, ocrLastNameNull, ocrEmailNull, ocrPasswordNull));
//    }
//
//    @Test
//    public void testIsDriverAlreadyRegistered() {
//        // Insert a user with valid data
//        String fullName = "John Doe";
//        String email = "john.doe@example.com";
//        String password = "password123";
//        String dob = "1990-01-01";
//        String contact = "1234567890";
//        String address = "123 Main St";
//        String licenseNo = "ABC123";
//        String vehicleType = "Car";
//
//        databaseHelper.insertUser(fullName, email, password, dob, contact, address, licenseNo, vehicleType);
//
//        // Check if the driver is already registered
//        String licenseNoToCheck = "ABC123";
//
//        assertTrue(databaseHelper.isDriverAlreadyRegistered(licenseNoToCheck));
//
//        // Check if the driver is not registered
//        String licenseNoNull = null;
//
//        assertFalse(databaseHelper.isDriverAlreadyRegistered(licenseNoNull));
//    }
//}
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.example.basic.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseHelperTest {

    @Mock
    private SQLiteDatabase mockDatabase;

    @Mock
    private SQLiteOpenHelper mockSQLiteOpenHelper;

    @InjectMocks
    private DatabaseHelper databaseHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onCreate_shouldCreateTable() {
        // Mock successful execution of execSQL
        doNothing().when(mockDatabase).execSQL(anyString());

        // Call the method to be tested
        databaseHelper.onCreate(mockDatabase);

        // Verify that execSQL was called with the correct query
        verify(mockDatabase).execSQL(anyString());
    }

    @Test
    public void onUpgrade_shouldDropTableAndCallOnCreate() {
        // Mock successful execution of execSQL
        doNothing().when(mockDatabase).execSQL(anyString());

        // Call the method to be tested
        databaseHelper.onUpgrade(mockDatabase, 1, 2);

        // Verify that execSQL was called to drop the table
        verify(mockDatabase).execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_NAME);

        // Verify that onCreate was called
        verify(databaseHelper).onCreate(mockDatabase);
    }

    @Test
    public void insertUser_shouldInsertDataIntoDatabase() {
        // Mock successful execution of insert
        when(mockDatabase.insert(anyString(), anyString(), any(ContentValues.class))).thenReturn(1L);

        // Call the method to be tested
        boolean result = databaseHelper.insertUser("John Doe", "john@example.com", "password123",
                "01-01-2000", "1234567890", "123 Main St", "ABC123", "Car");

        // Verify that insert was called with the correct parameters
        verify(mockDatabase).insert(eq(DatabaseHelper.TABLE_NAME), eq(null), any(ContentValues.class));

        // Verify that the result is true (indicating success)
        assertTrue(result);
    }

    @Test
    public void doesUserExist_shouldCheckIfUserExists() {
        // Mock successful execution of rawQuery
        when(mockDatabase.rawQuery(anyString(), any(String[].class))).thenReturn(mock(Cursor.class));

        // Call the method to be tested
        boolean result = databaseHelper.doesUserExist("John", "Doe", "john@example.com", "password123");

        // Verify that rawQuery was called with the correct parameters
        verify(mockDatabase).rawQuery(anyString(), any(String[].class));

        // Verify that the result is true (indicating the user exists)
        assertTrue(result);
    }

    @Test
    public void isDriverAlreadyRegistered_shouldCheckIfDriverExists() {
        // Mock successful execution of query
        when(mockDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class),
                anyString(), anyString(), anyString())).thenReturn(mock(Cursor.class));

        // Call the method to be tested
        boolean result = databaseHelper.isDriverAlreadyRegistered("ABC123");

        // Verify that query was called with the correct parameters
        verify(mockDatabase).query(eq(DatabaseHelper.TABLE_NAME), any(String[].class), eq("licensenumber = ?"),
                any(String[].class), eq(null), eq(null), eq(null));

        // Verify that the result is true (indicating the driver is already registered)
        assertTrue(result);
    }
}
