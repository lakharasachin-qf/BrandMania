package com.app.brandmania.Connection;

import android.app.Activity;
import android.os.StrictMode;

import com.app.brandmania.Common.PreafManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MyPdfPageEventHelper extends PdfPageEventHelper {
    Activity activity;

    public MyPdfPageEventHelper(Activity act) {
        activity=act;
    }

    @Override
    public void onEndPage(PdfWriter pdfWriter, Document document) {

        System.out.println("Creating Waterwark Image in PDF");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            //Use this method if you want to get image from your Local system
            //Image waterMarkImage = Image.getInstance("E:/tiger.jpg");
            PreafManager preafManager=new PreafManager(activity);
            String urlOfWaterMarKImage =preafManager.getActiveBrand().getLogo();

            //Get waterMarkImage from some URL
            Image waterMarkImage = Image.getInstance(getImage(new java.net.URL(urlOfWaterMarKImage)));
            waterMarkImage.setScaleToFitHeight(false);
            waterMarkImage.setTransparency(new int[] { 0x10, 0x10 });
            waterMarkImage.setAbsolutePosition(50, 250);
             //Get width and height of whole page
            float pdfPageWidth = document.getPageSize().getWidth();
            float pdfPageHeight = document.getPageSize().getHeight();

            //Set waterMarkImage on whole page
            pdfWriter.getDirectContentUnder().addImage(waterMarkImage,
                    pdfPageWidth, 0, 0, pdfPageHeight, 0, 0);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public byte[] getImage(URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        byte[] b = new byte[4096];
        int n;
        while ( (n = is.read(b)) > -1 ) {
            baos.write(b, 0, n);
        }
        return baos.toByteArray();
    }
}