package com.example.basic;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelper1Test {

//    @Rule
//    public ActivityTestRule<RegistrationActivity> activityRule = new ActivityTestRule<>(YourActivity.class);

    private DatabaseHelper1 databaseHelper;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        databaseHelper = new DatabaseHelper1(context);
    }

    @After
    public void tearDown() throws Exception {
        databaseHelper.close();
    }

    @Test
    public void testGetDriverValidCredentials() {
        // Insert test data into the database
        // You might want to use a testing database for this purpose
        // and clean up the data in the tearDown method

        String testEmail = "test@example.com";
        String testPassword = "testPassword";

        // Call the method you want to test
        Driver result = databaseHelper.getDriver(testEmail, testPassword);

        // Assert that the result is not null
        assertNotNull(result);

        // Add more assertions based on your expected results
        assertEquals("test@example.com", result.getEmail());
        assertEquals("testPassword", result.getPassword());
    }

    @Test
    public void testGetDriverNullCredentials() {
        // Call the method with null credentials
        Driver result = databaseHelper.getDriver(null, null);

        // Assert that the result is null because credentials are null
        assertNull(result);
    }

    @Test
    public void testOnCreate() {
        // You may need to run this in a test database environment
        // or use mocking to simulate database behavior
        databaseHelper.onCreate(databaseHelper.getWritableDatabase());

    }

    @Test
    public void testOnUpgrade() {
        // You may need to run this in a test database environment
        // or use mocking to simulate database behavior
        databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 1, 2);
    }
}
