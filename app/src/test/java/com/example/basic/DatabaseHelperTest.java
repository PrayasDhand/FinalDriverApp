//package com.example.basic;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//
//
//import com.example.basic.DatabaseHelper;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public class DatabaseHelperTest {
//
//    private DatabaseHelper dbHelper;
//    private SQLiteDatabase database;
//
//    @Before
//    public void setUp() {
////        Context context = ApplicationProvider.getApplicationContext();
//        dbHelper = new DatabaseHelper(context);
//        database = dbHelper.getWritableDatabase();
//    }
//
//    @After
//    public void tearDown() {
//        dbHelper.close();
//    }
//
//    @Test
//    public void testDatabaseCreation() {
//        assertTrue(database.isOpen());
//    }
//
//    @Test
//    public void testInsertUser() {
//        String fullName = "John Doe";
//        String email = "john@example.com";
//        String password = "password123";
//        String dob = "1990-01-01";
//        String contact = "1234567890";
//        String licenseNo = "ABC123";
//        String vehicleType = "Car";
//
//        boolean result = dbHelper.insertUser(fullName, email, password, dob, contact, licenseNo, vehicleType);
//        assertTrue(result);
//
//        // Check if the user exists in the database
//        String[] columns = {DatabaseHelper.COLUMN_FIRST_NAME};
//        String selection = DatabaseHelper.COLUMN_EMAIL + " = ?";
//        String[] selectionArgs = {email};
//        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
//
//        try {
//            assertTrue(cursor.moveToFirst());
//            String storedFullName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
//            assertEquals(fullName, storedFullName);
//        } finally {
//            cursor.close();
//        }
//    }
//
//    @Test
//    public void testIsDriverAlreadyRegistered() {
//        // Add a sample driver to the database
//        String licenseNo = "XYZ789";
//        dbHelper.insertUser("Jane Doe", "jane@example.com", "password456", "1995-05-05", "9876543210", licenseNo, "Motorcycle");
//
//        // Check if the driver is already registered
//        assertTrue(dbHelper.isDriverAlreadyRegistered(licenseNo));
//
//        // Check with a non-existing license number
//        assertFalse(dbHelper.isDriverAlreadyRegistered("NonExistingLicense"));
//    }
//
//    // Add more test cases as needed
//}
//
