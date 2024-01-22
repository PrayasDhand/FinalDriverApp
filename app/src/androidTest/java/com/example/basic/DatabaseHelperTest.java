package com.example.basic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Before
    public void setUp() throws Exception {
        Context context;
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    @Test
    public void onCreate() {
        String tableName = "DriversList";
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        assertTrue("Error: Table not created", cursor.moveToFirst());
        cursor.close();
    }

    @Test
    public void onUpgrade() {
    }

    @Test
    public void insertUser() {
        assertTrue(dbHelper.insertUser("John Doe", "john@example.com", "password", "01-01-1990", "1234567890", "123 Main St", "ABC123", "Car"));
    }

    @Test
    public void doesUserExist() {
        dbHelper.insertUser("Jane Doe", "jane@example.com", "password", "01-01-1995", "9876543210", "456 Second St", "XYZ456", "Bike");

        assertTrue(dbHelper.doesUserExist("Jane", "Doe", "jane@example.com", "password"));
        assertFalse(dbHelper.doesUserExist("John", "Doe", "john@example.com", "password"));
    }

    @Test
    public void isDriverAlreadyRegistered() {
        dbHelper.insertUser("Alice", "alice@example.com", "password", "01-01-1985", "9876543210", "789 Third St", "DEF789", "Truck");

        assertTrue(dbHelper.isDriverAlreadyRegistered("DEF789"));
        assertFalse(dbHelper.isDriverAlreadyRegistered("NONEXISTENT"));
    }
}