package com.example.basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class TesseractHelperTest {

    private TesseractHelper tesseractHelper;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        tesseractHelper = new TesseractHelper(context, "eng");
    }

    @After
    public void tearDown() throws Exception {
        tesseractHelper.onDestroy();
    }

    @Test
    public void performOCR() {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.driver);

        // Perform OCR on the test image
        String ocrResult = tesseractHelper.performOCR(bitmap);

        // Check if OCR result is not null
        assertNotNull(ocrResult);

        // Add assertions based on the expected OCR result from the test image
        // For example, if the expected result is "Hello World", you can use:
        assertEquals("1234567890", ocrResult.trim());
    }

    @Test
    public void getOCRResult() {
        // Check if initial OCR result is null
        assertNull(tesseractHelper.getOCRResult());

        // Perform OCR on a test image
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.driver);
        tesseractHelper.performOCR(bitmap);

        // Check if getOCRResult returns the correct result
        assertEquals("Hello World", tesseractHelper.getOCRResult().trim());
    }

    @Test
    public void onDestroy() {
        tesseractHelper.onDestroy();

        // Check if tessBaseAPI is ended (set to null in onDestroy)
        assertNull(tesseractHelper.tessBaseAPI);
    }
}