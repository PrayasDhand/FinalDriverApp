package com.example.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WelcomeDriverTest {

    @Mock
    private DatabaseHelper1 mockDbHelper;

    @Mock
    private DatabaseReference mockedDatabaseReference;

    @Mock
    private android.app.Activity activity;

    @InjectMocks
    private WelcomeDriver welcomeDriver;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void onCreate_whenUserNotNull_shouldPopulateFields() {
        // Mock user data
        Driver mockUser = new Driver(1, "John Doe", "john@example.com", "password123", "1234567890");

        // Mock database interaction
        when(mockDbHelper.getDriver("john@example.com", "password123")).thenReturn(mockUser);

        // Mock getIntent
        Intent mockIntent = mock(Intent.class);
        when(mockedDatabaseReference.orderByChild(anyString())).thenReturn(mock(Query.class));
        when(welcomeDriver.getIntent()).thenReturn(mockIntent);
        when(mockIntent.getStringExtra("email")).thenReturn("john@example.com");
        when(mockIntent.getStringExtra("password")).thenReturn("password123");

        // Call the method to be tested
        welcomeDriver.onCreate(null);

        // Verify that the UI fields are populated
        assertEquals("John Doe", welcomeDriver.welcomeUser.getText().toString());
        assertEquals("John Doe", welcomeDriver.fullNameEditText.getText().toString());
        assertEquals("john@example.com", welcomeDriver.emailEditText.getText().toString());
        assertEquals("password123", welcomeDriver.passwordEditText.getText().toString());
        assertEquals("1234567890", welcomeDriver.contactEditText.getText().toString());
    }

    @Test
    public void onCreate_whenUserIsNull_shouldNotPopulateFields() {
        // Mock database interaction returning null user
        when(mockDbHelper.getDriver("john@example.com", "password123")).thenReturn(null);

        // Mock getIntent
        Intent mockIntent = mock(Intent.class);
        when(welcomeDriver.getIntent()).thenReturn(mockIntent);
        when(mockIntent.getStringExtra("email")).thenReturn("john@example.com");
        when(mockIntent.getStringExtra("password")).thenReturn("password123");

        // Call the method to be tested
        welcomeDriver.onCreate(null);

        // Verify that the UI fields are not populated
        assertEquals("", welcomeDriver.welcomeUser.getText().toString());
        assertEquals("", welcomeDriver.fullNameEditText.getText().toString());
        assertEquals("", welcomeDriver.emailEditText.getText().toString());
        assertEquals("", welcomeDriver.passwordEditText.getText().toString());
        assertEquals("", welcomeDriver.contactEditText.getText().toString());
    }

    @Test
    public void logout_shouldClearSharedPreferences() {
        // Mock SharedPreferences
        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        when(welcomeDriver.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);

        // Call the method to be tested
        welcomeDriver.logout();

        // Verify that SharedPreferences editor is cleared
        verify(mockSharedPreferences.edit()).clear();
        verify(mockSharedPreferences.edit()).apply();
    }

    @Test
    public void updateDetailsInFirebase_shouldUpdateDetails() {
        // Mock user input
        when(welcomeDriver.fullNameEditText.getText()).thenReturn(mock(Editable.class));
        when(welcomeDriver.emailEditText.getText()).thenReturn(mock(Editable.class));
        when(welcomeDriver.passwordEditText.getText()).thenReturn(mock(Editable.class));
        when(welcomeDriver.contactEditText.getText()).thenReturn(mock(Editable.class));

        // Mock SharedPreferences behavior
        when(mockDbHelper.getDriver(anyString(), anyString())).thenReturn(new Driver(1, "John Doe", "john@example.com", "password123", "1234567890"));
        when(welcomeDriver.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)).thenReturn(mock(SharedPreferences.class));
        when(welcomeDriver.getSharedPreferences("userPrefs", Context.MODE_PRIVATE).getString(anyString(), anyString())).thenReturn("mockedValue");

        // Mock Firebase behavior
        DatabaseReference mockedDatabaseReference = mock(DatabaseReference.class);

        when(mockedDatabaseReference.orderByChild(anyString())).thenReturn(mock(Query.class));

        // Call the method to be tested
        welcomeDriver.updateDetailsInFirebase();

        // Verify interactions
        verify(mockedDatabaseReference, times(1)).orderByChild(anyString());

        verify(mockedDatabaseReference, times(1)).setValue(anyString());
        verify(welcomeDriver.getSharedPreferences("userPrefs", Context.MODE_PRIVATE).edit(), times(4)).putString(anyString(), anyString());
        verify(welcomeDriver.getSharedPreferences("userPrefs", Context.MODE_PRIVATE).edit(), times(1)).apply();
    }
}
