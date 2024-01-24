//package com.example.basic;
//
//import androidx.test.espresso.intent.Intents;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.rule.ActivityTestRule;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//@RunWith(AndroidJUnit4.class)
//public class MainActivityTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
//
//    private MainActivity mActivity;
//
//    @Before
//    public void setUp() {
//        mActivity = activityRule.getActivity();
//        Intents.init();
//    }
//
//    @Test
//    public void testButtonLaunchesLoginActivity() {
//        // Find the button by its ID
//        onView(withId(R.id.button2)).perform(click());
//
//        // Check if the LoginActivity is launched
//        intended(hasComponent(LoginActivity.class.getName()));
//    }
//
//    @After
//    public void tearDown() {
//        mActivity = null;
//        Intents.release();
//    }
//}
