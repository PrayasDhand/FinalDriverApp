package com.example.basic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseHelper1Test {

    @Mock
    private SQLiteDatabase mockDb;

    @Mock
    private Cursor mockCursor;

    private DatabaseHelper1 databaseHelper;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        databaseHelper = new DatabaseHelper1(context);
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        databaseHelper.close();
    }

    @Test
    public void testGetDriverWithValidData() {
        // Given
        String email = "test@example.com";
        String password = "password";
        Driver expectedDriver = new Driver(1, "John Doe", email, password, "123456789");

        when(mockDb.query(anyString(), any(String[].class), anyString(), any(String[].class),
                anyString(), anyString(), anyString(), anyString())).thenReturn(mockCursor);

        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.getInt(anyInt())).thenReturn(expectedDriver.getId());
        when(mockCursor.getString(anyInt())).thenReturn(expectedDriver.getName(),
                expectedDriver.getEmail(), expectedDriver.getPassword(), expectedDriver.getContact());

        when(mockCursor.getColumnIndex(anyString())).thenReturn(1, 2, 3, 4);

        // When
        Driver resultDriver = databaseHelper.getDriver(email, password);

        // Then
        verify(mockDb).query(anyString(), any(String[].class), anyString(), any(String[].class),
                anyString(), anyString(), anyString(), anyString());
        verify(mockCursor).moveToFirst();
        verify(mockCursor, Mockito.times(4)).getInt(anyInt());
        verify(mockCursor, Mockito.times(4)).getString(anyInt());
        verify(mockCursor).close();

        assertEquals(expectedDriver, resultDriver);
    }

    @Test
    public void testGetDriverWithInvalidData() {
        // Given
        String email = "test@example.com";
        String password = "password";

        when(mockDb.query(anyString(), any(String[].class), anyString(), any(String[].class),
                anyString(), anyString(), anyString(), anyString())).thenReturn(mockCursor);

        when(mockCursor.moveToFirst()).thenReturn(false);

        // When
        Driver resultDriver = databaseHelper.getDriver(email, password);

        // Then
        verify(mockDb).query(anyString(), any(String[].class), anyString(), any(String[].class),
                anyString(), anyString(), anyString(), anyString());
        verify(mockCursor).moveToFirst();
        verify(mockCursor, Mockito.never()).getInt(anyInt());
        verify(mockCursor, Mockito.never()).getString(anyInt());
        verify(mockCursor).close();

        assertNull(resultDriver);
    }

    @Test
    public void getDriver() {
    }

    @Test
    public void onCreate() {

        // Given
        SQLiteOpenHelper sqLiteOpenHelper = mock(SQLiteOpenHelper.class);
        SQLiteDatabase sqLiteDatabase = mock(SQLiteDatabase.class);
        when(sqLiteOpenHelper.getWritableDatabase()).thenReturn(sqLiteDatabase);

        // When
        databaseHelper.onCreate(sqLiteDatabase);

        // Then
        verify(sqLiteDatabase).execSQL(anyString());
    }

    @Test
    public void onUpgrade() {

        SQLiteOpenHelper sqLiteOpenHelper = mock(SQLiteOpenHelper.class);
        SQLiteDatabase sqLiteDatabase = mock(SQLiteDatabase.class);
        when(sqLiteOpenHelper.getWritableDatabase()).thenReturn(sqLiteDatabase);

        // When
        databaseHelper.onUpgrade(sqLiteDatabase, 1, 2);

        // Then
        verify(sqLiteDatabase).execSQL(anyString());
    }
}