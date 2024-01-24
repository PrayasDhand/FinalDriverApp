package com.example.basic;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private LoginActivity loginActivity;

    @Before
    public void setUp() throws Exception {
        loginActivity = new LoginActivity();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onCreate() {
        assertNotNull(loginActivity);
    }

    @Test
    public void validateEmail() {
        assertTrue(loginActivity.isValidEmail("test@example.com"));
assertFalse(loginActivity.isValidEmail("invalid.email"));
    }

    @Test
    public void isValidEmail() {
    }

    @Test
    public void saveLoginState() {
    }

    @Test
    public void isLoggedIn() {
    }

    @Test
    public void onActivityResult() {
    }
}

