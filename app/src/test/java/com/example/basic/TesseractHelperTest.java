package com.example.basic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.basic.TesseractHelper;
import com.googlecode.tesseract.android.TessBaseAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TesseractHelperTest {

    @Mock
    private Context context;

    @Mock
    private TessBaseAPI tessBaseAPI;

    private TesseractHelper tesseractHelper;

    @Before
    public void setUp() {
        tesseractHelper = new TesseractHelper(context, "eng");
        tesseractHelper.tessBaseAPI = tessBaseAPI;
    }

    @Test
    public void testPerformOCR() {
        Bitmap bitmap = mock(Bitmap.class);
        when(tessBaseAPI.getUTF8Text()).thenReturn("Hello World");
        String result = tesseractHelper.performOCR(bitmap);
        verify(tessBaseAPI).setImage(bitmap);
        verify(tessBaseAPI).getUTF8Text();
        assertEquals("Hello World", result);
    }
}