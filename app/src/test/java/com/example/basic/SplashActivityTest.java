package com.example.basic;

import android.content.Intent;
import android.os.Handler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class SplashActivityTest {

    @Mock
    Handler mockHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandlerDelay() {
        SplashActivity splashActivity = Mockito.spy(new SplashActivity());
        splashActivity.handler = mockHandler;

        Runnable mockRunnable = Mockito.mock(Runnable.class);
        Intent mockIntent = Mockito.mock(Intent.class);

        splashActivity.handler.postDelayed(() -> {
            splashActivity.startActivity(mockIntent);
            splashActivity.finish();
        }, 3000L);

        Mockito.verify(mockHandler).postDelayed(any(), eq(3000L));
        Mockito.verify(splashActivity).startActivity(mockIntent);
        Mockito.verify(splashActivity).finish();
    }

}
