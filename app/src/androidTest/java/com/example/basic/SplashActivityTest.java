package com.example.basic;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    private ActivityScenario<SplashActivity> activityScenario;

    @Before
    public void setUp() {
        activityScenario = ActivityScenario.launch(SplashActivity.class);
    }

    @Test
    public void testSplashActivityNotNull() {
        activityScenario.onActivity(activity -> {
            assertNotNull(activity);
        });
    }

    @Test
    public void testAnimationNotNull() {
        activityScenario.onActivity(activity -> {
            Animation topAnim = AnimationUtils.loadAnimation(activity, R.anim.top_animation);
            Animation bottomAnim = AnimationUtils.loadAnimation(activity, R.anim.bottom_animation);
            assertNotNull(topAnim);
            assertNotNull(bottomAnim);
        });
    }

    @Test
    public void testLogoAndSplashImageNotNull() {
        activityScenario.onActivity(activity -> {
            TextView logo = activity.findViewById(R.id.logo);
            ImageView splashImage = activity.findViewById(R.id.splashImage);
            assertNotNull(logo);
            assertNotNull(splashImage);
        });
    }

    @Test
    public void testHandlerDelay() {
        activityScenario.onActivity(activity -> {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                assertTrue(activity.isFinishing());
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }, 3000);
        });
    }

    @Test
    public void testNavigateToMainActivity() {
        activityScenario.onActivity(activity -> {
            onView(isRoot()).perform(waitFor(3000)); // Wait for 3000 milliseconds
            assertTrue(activity.isFinishing());
            activityScenario = null; // Prevents the next test from relaunching the activity
        });
    }

    @After
    public void tearDown() {
        if (activityScenario != null) {
            activityScenario.close();
        }
    }

    // Helper method to wait for a specified duration
    public static ViewAction waitFor(long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}
