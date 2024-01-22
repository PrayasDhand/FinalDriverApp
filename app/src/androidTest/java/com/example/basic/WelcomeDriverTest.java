package com.example.basic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class WelcomeDriverTest {

    @Rule
    public ActivityScenarioRule<WelcomeDriver> activityRule = new ActivityScenarioRule<>(WelcomeDriver.class);

    private ActivityScenario<WelcomeDriver> scenario;

    @Before
    public void setUp() throws Exception {

        scenario = activityRule.getScenario();
    }

    @After
    public void tearDown() throws Exception {

        scenario.close();
    }

//    @Test
//    public void testLogout() {
//        // Ensure that SharedPreferences is cleared after calling logout method
//        SharedPreferences preferences = scenario.getScenario().getResult().getSharedPreferences("loginPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        // Set some values to SharedPreferences
//        editor.putString("key", "value");
//        editor.apply();
//
//        // Call the logout method
//        scenario.onActivity(activity -> activity.logout());
//
//        // Check if SharedPreferences is cleared
//        assertNull(preferences.getString("key", null));
//    }

    @Test
    public void onCreate() {
        // Prepare data to be passed to the activity
        Intent intent = new Intent();
        intent.putExtra("email", "test@example.com");
        intent.putExtra("password", "testPassword");

        // Launch the activity with the intent
        try (ActivityScenario<WelcomeDriver> scenario = ActivityScenario.launch(intent)) {
            // Access the activity to test its behavior
            scenario.onActivity(activity -> {
                // Access UI elements
                TextView welcomeUser = activity.findViewById(R.id.welcomeUser);
                TextView fullNameEditText = activity.findViewById(R.id.fullnametext_profile);
                TextView emailEditText = activity.findViewById(R.id.emailText_profile);
                TextView passwordEditText = activity.findViewById(R.id.passwordText_profile);
                TextView contactEditText = activity.findViewById(R.id.contactText_profile);

                // Check if UI elements are not null
                assertNotNull(welcomeUser);
                assertNotNull(fullNameEditText);
                assertNotNull(emailEditText);
                assertNotNull(passwordEditText);
                assertNotNull(contactEditText);

                // Check if user data is displayed correctly
                assertEquals("Test User", welcomeUser.getText().toString());
                assertEquals("Test User", fullNameEditText.getText().toString());
                assertEquals("test@example.com", emailEditText.getText().toString());
                assertEquals("testPassword", passwordEditText.getText().toString());
                assertEquals("1234567890", contactEditText.getText().toString());

                // Call the logout method
                activity.logout();

                // Check if SharedPreferences is cleared
                SharedPreferences preferences = activity.getSharedPreferences("loginPrefs", MODE_PRIVATE);
                assertNull(preferences.getString("key", null));
            });
        }
    }

}