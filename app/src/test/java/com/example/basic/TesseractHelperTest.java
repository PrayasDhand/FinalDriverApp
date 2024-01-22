package com.example.basic;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import com.googlecode.tesseract.android.TessBaseAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.File;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TesseractHelperTest {

    @Mock
    private Context mockContext;

    @Mock
    private TessBaseAPI mockTessBaseAPI;

    @Mock
    private AssetManager mockAssetManager;

    private TesseractHelper tesseractHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockContext.getFilesDir()).thenReturn(new File("/fake/filesDir"));
        when(mockContext.getAssets()).thenReturn(mockAssetManager);
        tesseractHelper = spy(new TesseractHelper(mockContext, "eng"));
//        doReturn(mockTessBaseAPI).when(tesseractHelper).createTessBaseAPI();
   }

    @Test
    public void testPerformOCR() {
        Bitmap mockBitmap = mock(Bitmap.class);
        when(mockTessBaseAPI.getUTF8Text()).thenReturn("Mock OCR Result");

        String result = tesseractHelper.performOCR(mockBitmap);

        verify(mockTessBaseAPI).setImage(mockBitmap);
        assertEquals("Mock OCR Result", result);
    }

    @Test
    public void testGetOCRResult() {
        when(mockTessBaseAPI.getUTF8Text()).thenReturn("Mock OCR Result");
        assertEquals("Mock OCR Result", tesseractHelper.getOCRResult());
    }

    @Test
    public void testOnDestroy() {
        tesseractHelper.onDestroy();
        verify(mockTessBaseAPI).end();
    }

    @Test
    public void testInitializeTesseract() throws Exception {
        // Mock assets
        InputStream mockInputStream = mock(InputStream.class);
        when(mockAssetManager.open("tessdata/eng.traineddata")).thenReturn(mockInputStream);

        // Initialize Tesseract
        tesseractHelper.initializeTesseract();

        // Verify file operations
        verify(mockContext).getFilesDir();
        verify(mockAssetManager).open("tessdata/eng.traineddata");
        verify(mockInputStream).close();
        verify(mockTessBaseAPI).init(anyString(), eq("eng"));
    }

    @After
    public void tearDown() {
        tesseractHelper.onDestroy(); // Ensure that onDestroy is called to end the TessBaseAPI
    }
}
