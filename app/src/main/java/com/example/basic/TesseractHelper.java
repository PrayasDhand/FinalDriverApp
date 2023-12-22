package com.example.basic;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TesseractHelper {
    private TessBaseAPI tessBaseAPI;
    private String ocrResult;
    private Context context;
    private String language;

    public TesseractHelper(Context context, String language) {
        this.context = context;
        this.language = language;
        initializeTesseract();
    }

    private void initializeTesseract() {
        tessBaseAPI = new TessBaseAPI();
        try {
            // Destination folder for tessdata in the app's internal storage
            String datapath = context.getFilesDir().getPath() + "/";

            // Check if the folder exists, create it if not
            File tessdataFolder = new File(datapath + "tessdata/");
            if (!tessdataFolder.exists()) {
                tessdataFolder.mkdirs();
            }

            // Copy the eng.traineddata file from assets to the tessdata folder
            String trainedDataPath = tessdataFolder.getPath() + "/eng.traineddata";
            if (!new File(trainedDataPath).exists()) {
                InputStream in = context.getAssets().open("tessdata/eng.traineddata");
                OutputStream out = new FileOutputStream(trainedDataPath);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
                in.close();
            }

            // Initialize Tesseract with the correct datapath and language
            tessBaseAPI.init(datapath, language);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String performOCR(Bitmap bitmap) {
        if (tessBaseAPI == null) {
            // Reinitialize if necessary
            initializeTesseract();
        }

        tessBaseAPI.setImage(bitmap);
        ocrResult = tessBaseAPI.getUTF8Text();
        return ocrResult;
    }

    public String getOCRResult() {
        return ocrResult;
    }

    public void onDestroy() {
        if (tessBaseAPI != null) {
            tessBaseAPI.end();
        }
    }
}
