package com.example.basic;

import android.content.Intent;
import androidx.appcompat.widget.AppCompatButton;

import com.example.basic.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class MainActivityTest {

    @Test
    public void testMainActivityNotNull() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        assertNotNull(mainActivity);
    }

    @Test
    public void testButtonClickShouldStartLoginActivity() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        AppCompatButton button = mainActivity.findViewById(R.id.button2);

        assertNotNull(button);

        // Perform click
        button.performClick();

        // Check if LoginActivity is started
        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
        assertTrue(ShadowApplication.getInstance().getNextStartedActivity().filterEquals(expectedIntent));
    }


}
