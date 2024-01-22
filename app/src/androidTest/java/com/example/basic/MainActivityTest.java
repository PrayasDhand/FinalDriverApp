//package com.example.basic;
//
//import android.content.Intent;
//
//
//import androidx.test.core.shadow.Shadow;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//@RunWith(AndroidJUnit4.class)
//public class MainActivityTest {
//
//    @Test
//    public void testButtonClick() {
//        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
//            // Access the activity to test its behavior
//            scenario.onActivity(activity -> {
//                // Access the button
//                AppCompatButton button2 = activity.findViewById(R.id.button2);
//
//                // Check if the button is not null
//                assertNotNull(button2);
//
//                // Perform click on the button
//                button2.performClick();
//
//                // Get the launched intent
//                Intent launchedIntent = Shadow.getNextStartedActivity(ApplicationProvider.getApplicationContext());
//
//                // Check if the correct activity is launched
//                assertEquals(LoginActivity.class.getName(), launchedIntent.getComponent().getClassName());
//            });
//        }
//    }
//}