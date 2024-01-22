package com.example.basic;

import android.content.Context;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest {

    private DatabaseHelper1 dbHelper;

    @Before
    public void setUp() {
        // Initialize DatabaseHelper1 and any other setup needed
        dbHelper = new DatabaseHelper1(getContext());
    }

    @After
    public void tearDown() {
        // Clean up resources, close the database, etc.
        dbHelper.close();
    }

    @Test
    public void testRegistrationSuccess() {
        // Start the RegistrationActivity
        ActivityScenario.launch(RegistrationActivity.class);

        // Perform actions to fill in valid registration data
        Espresso.onView(ViewMatchers.withId(R.id.edText1)).perform(ViewActions.typeText("John Doe"));
        Espresso.onView(ViewMatchers.withId(R.id.editText2)).perform(ViewActions.typeText("john.doe@example.com"));
        Espresso.onView(ViewMatchers.withId(R.id.editText3)).perform(ViewActions.typeText("StrongPassword123!"));
        Espresso.onView(ViewMatchers.withId(R.id.editText4)).perform(ViewActions.typeText("1234567890"));

        // Click the registration button
        Espresso.onView(ViewMatchers.withId(R.id.regBtn)).perform(ViewActions.click());

        // Wait for the registration process (Handler delay)
        // Note: This assumes a delay of 2000 milliseconds in the actual implementation
        Espresso.onView(ViewMatchers.withId(R.id.regBtn)).check(matches(isDisplayed()));

        // Verify that the LoginActivity is displayed after successful registration
        Espresso.onView(ViewMatchers.withText("Login"))
                .check(matches(withText("Login"))); // Replace "Login" with the actual text of your login activity title
    }

    @Test
    public void testRegistrationValidation() {
        // Start the RegistrationActivity
        ActivityScenario.launch(RegistrationActivity.class);

        // Perform actions to fill in invalid registration data
        Espresso.onView(ViewMatchers.withId(R.id.edText1)).perform(ViewActions.typeText(""));
        Espresso.onView(ViewMatchers.withId(R.id.editText2)).perform(ViewActions.typeText("invalid_email"));
        Espresso.onView(ViewMatchers.withId(R.id.editText3)).perform(ViewActions.typeText("weakpassword"));
        Espresso.onView(ViewMatchers.withId(R.id.editText4)).perform(ViewActions.typeText("123"));

        // Click the registration button
        Espresso.onView(ViewMatchers.withId(R.id.regBtn)).perform(ViewActions.click());

        // Verify that error messages are displayed for invalid fields
        Espresso.onView(withId(R.id.edText1)).check(matches(ViewMatchers.hasErrorText("Please enter your name")));
        Espresso.onView(withId(R.id.editText2)).check(matches(ViewMatchers.hasErrorText("Please enter a valid email address")));
        Espresso.onView(withId(R.id.editText3)).check(matches(ViewMatchers.hasErrorText("Password should contain one uppercase, one lowercase, and one special character")));
        Espresso.onView(withId(R.id.editText4)).check(matches(ViewMatchers.hasErrorText("Phone number should be of 10 digits")));
    }

    private Context getContext() {
        // Return the context of the app under test
        return androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
    }
}

