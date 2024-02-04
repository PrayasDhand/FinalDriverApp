package com.example.basic;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import com.example.basic.DatabaseHelper1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    @Mock
    DatabaseHelper1 databaseHelper;

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private SharedPreferences.Editor editor;






    @InjectMocks
    LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Context context = Mockito.mock(Context.class);
        loginActivity = new LoginActivity();
        loginActivity.sharedPreferences = sharedPreferences;

        // Mock the behavior of SharedPreferences and Editor
        when(sharedPreferences.edit()).thenReturn(editor);
    }

    @After
    public void tearDown() {
        // Clear any shared preferences changes
        sharedPreferences.edit().clear().commit();
    }

    @Test
    public void testValidateEmail() {
        EditText emailEditText = mock(EditText.class);
        when(emailEditText.getText().toString()).thenReturn("test@example.com");

        loginActivity.validateEmail(emailEditText.getText().toString());

        verify(emailEditText, never()).setError(any());
    }

//    @Test
//    public void testAuthenticateUser_Success() {
//        SQLiteDatabase db = mock(SQLiteDatabase.class);
//        when(databaseHelper.getReadableDatabase()).thenReturn(db);
//
//        Cursor cursor = mock(Cursor.class);
//        when(db.query(anyString(), any(), anyString(), any(), any(), any(), any())).thenReturn(cursor);
//        when(cursor.moveToFirst()).thenReturn(true);
//
//        assertTrue(loginActivity.authenticateUser("test@example.com", "password"));
//
//        verify(cursor).close();
//        verify(db).close();
//    }
//
//    @Test
//    public void testAuthenticateUser_Failure() {
//        SQLiteDatabase db = mock(SQLiteDatabase.class);
//        when(databaseHelper.getReadableDatabase()).thenReturn(db);
//
//        Cursor cursor = mock(Cursor.class);
//        when(db.query(anyString(), any(), anyString(), any(), any(), any(), any())).thenReturn(cursor);
//        when(cursor.moveToFirst()).thenReturn(false);
//
//        assertFalse(loginActivity.authenticateUser("test@example.com", "password"));
//
//        verify(cursor).close();
//        verify(db).close();
//    }

    @Test
    public void testSaveLoginState() {
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        loginActivity.saveLoginState();

        verify(editor).putBoolean(anyString(), anyBoolean());
        verify(editor).apply();
    }

    @Test
    public void testIsLoggedIn_True() {
        when(sharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(true);

        assertTrue(loginActivity.isLoggedIn());
    }

    @Test
    public void testIsLoggedIn_False() {
        when(sharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false);

        assertFalse(loginActivity.isLoggedIn());
    }

    @Test
    public void testSaveUserDataToSharedPreferences() {
        // Given
        String fullName = "John Doe";
        String email = "john.doe@example.com";
        String password = "password123";
        String contact = "1234567890";

        // When
        loginActivity.saveUserDataToSharedPreferences(fullName, email, password, contact);

        // Then
        verify(editor).putString("fullName", fullName);
        verify(editor).putString("email", email);
        verify(editor).putString("password", password);
        verify(editor).putString("contact", contact);
        verify(editor).apply();

        // Verify that the values were stored correctly
        assertEquals(fullName, sharedPreferences.getString("fullName", null));
        assertEquals(email, sharedPreferences.getString("email", null));
        assertEquals(password, sharedPreferences.getString("password", null));
        assertEquals(contact, sharedPreferences.getString("contact", null));
    }
}
