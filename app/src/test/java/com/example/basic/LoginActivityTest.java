package com.example.basic;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import com.example.basic.DatabaseHelper1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    @Mock
    DatabaseHelper1 databaseHelper;

    @Mock
    SharedPreferences sharedPreferences;

    @InjectMocks
    LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
}
