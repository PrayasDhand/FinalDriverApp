package com.example.basic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper1 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DriversDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper1(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Driver getDriver(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id", "name", "email", "password", "contact"};
        String selection = "email=? AND password=?";
        String[] selectionArgs = {email, password};

        if (email == null || password == null) {
            return null;
        }

        Cursor cursor = db.query("Drivers", columns, selection, selectionArgs, null, null, null, null);
        Driver driver = null;

        if (cursor != null && cursor.moveToFirst()) {
            // Pass the values from the cursor to the Driver constructor
            driver = new Driver(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("contact"))
            );
            cursor.close();
        }
        return driver;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE Drivers (id INTEGER PRIMARY KEY, name TEXT, email TEXT, password TEXT, contact TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS Drivers";
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
}
