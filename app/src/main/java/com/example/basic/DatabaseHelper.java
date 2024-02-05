package com.example.basic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Drivers.db";
    private static final int DATABASE_VERSION = 3;

    static final String TABLE_NAME = "DriversList";
    static final String COLUMN_ID = "id";
    static final String COLUMN_FIRST_NAME = "firstname";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_DOB = "dob";
    static final String COLUMN_CONTACT = "contact";
    static final String COLUMN_ADDRESS = "address";
    static final String COLUMN_LICENSE_NUMBER = "licensenumber";
    static final String COLUMN_VEHICLE_TYPE = "vehicletype";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_DOB + " TEXT, " +
                COLUMN_CONTACT + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_LICENSE_NUMBER + " TEXT, " +
                COLUMN_VEHICLE_TYPE + " TEXT" +
                ")";
        try {
            db.execSQL(createTable);
            Log.d("DatabaseHelper", "Table created successfully");
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String fullName, String email, String password, String dob, String contact, String address, String licenseNo, String vehicleType) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_FIRST_NAME, fullName);
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_DOB, dob);
            contentValues.put(COLUMN_CONTACT, contact);
            contentValues.put(COLUMN_ADDRESS, address);
            contentValues.put(COLUMN_LICENSE_NUMBER, licenseNo);
            contentValues.put(COLUMN_VEHICLE_TYPE, vehicleType);

            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error inserting user: " + e.getMessage());
            return false;
        }
    }

    public boolean doesUserExist(String ocrFirstName, String ocrLastName, String ocrEmail, String ocrPassword) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_FIRST_NAME + " = ? AND " +
                    COLUMN_EMAIL + " = ? AND " +
                    COLUMN_PASSWORD + " = ?";
            String[] selectionArgs = {ocrFirstName + " " + ocrLastName, ocrEmail, ocrPassword};
            try (Cursor cursor = db.rawQuery(query, selectionArgs)) {
                return cursor.getCount() > 0;
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    public boolean isDriverAlreadyRegistered(String licenseNo) {
        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_LICENSE_NUMBER},
                COLUMN_LICENSE_NUMBER + " = ?",
                new String[]{licenseNo},
                null,
                null,
                null)) {
            return cursor.moveToFirst();
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error checking driver registration: " + e.getMessage());
            return false;
        }
    }
}
