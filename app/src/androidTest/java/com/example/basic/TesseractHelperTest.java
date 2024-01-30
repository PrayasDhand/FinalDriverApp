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
        assertEquals("l N E VADA '~ DRIVtR LlLtht q\n" +
                "\n" +
                "' VT\" ‘~ ' 35 4am. \"01534567890\n" +
                "\n" +
                "3 ff ‘ 3 nos 10l27l19§9\n" +
                "\n" +
                "g >i f‘ 1 SAMPLE\n" +
                "\n" +
                "3 . g} 9; g 2 JOHN LEWIS\n" +
                "\n" +
                "E \" w» ' uzsmmsmea\n" +
                "\n" +
                "! P ’ ' , ‘! CARSON CITY. NV “000-1234\n" +
                "\n" +
                "s F??? f ﬂ; ocuss c 9. mono“:\n" +
                "\n" +
                "5 “13, “2\" 2:5 12 REST nous '\n" +
                "\n" +
                "'. “Show $635,120” f \\\n" +
                "\n" +
                "i I, Jag)” is sex u .° °.\n" +
                "15 HGT saw , ' '\n" +
                "\n" +
                "; mm «s.» 10/27/69 V\n" +
                "\n" +
                ": SQMW 18 ms no ;\n" +
                "\n" +
                "3 mm m\n" +
                "\n" +
                "i 4 nn nunnmnnurnm '", ocrResult.trim());
    }

    @Test
    public void getOCRResult() {
        // Check if initial OCR result is null
        assertNull(tesseractHelper.getOCRResult());

        // Perform OCR on a test image
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.driver);
        tesseractHelper.performOCR(bitmap);

        // Check if getOCRResult returns the correct result
        assertEquals("l N E VADA '~ DRIVtR LlLtht q\n" +
                "\n" +
                "' VT\" ‘~ ' 35 4am. \"01534567890\n" +
                "\n" +
                "3 ff ‘ 3 nos 10l27l19§9\n" +
                "\n" +
                "g >i f‘ 1 SAMPLE\n" +
                "\n" +
                "3 . g} 9; g 2 JOHN LEWIS\n" +
                "\n" +
                "E \" w» ' uzsmmsmea\n" +
                "\n" +
                "! P ’ ' , ‘! CARSON CITY. NV “000-1234\n" +
                "\n" +
                "s F??? f ﬂ; ocuss c 9. mono“:\n" +
                "\n" +
                "5 “13, “2\" 2:5 12 REST nous '\n" +
                "\n" +
                "'. “Show $635,120” f \\\n" +
                "\n" +
                "i I, Jag)” is sex u .° °.\n" +
                "15 HGT saw , ' '\n" +
                "\n" +
                "; mm «s.» 10/27/69 V\n" +
                "\n" +
                ": SQMW 18 ms no ;\n" +
                "\n" +
                "3 mm m\n" +
                "\n" +
                "i 4 nn nunnmnnurnm '", tesseractHelper.getOCRResult().trim());
    }

//    @Test
//    public void onDestroy() {
//        tesseractHelper.onDestroy();
//
//        // Check if tessBaseAPI is ended (set to null in onDestroy)
//        assertNull(tesseractHelper.tessBaseAPI);
//    }
}