package com.make.mybrand.Connection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.make.mybrand.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

public class MyPdfPageEventHelper extends PdfPageEventHelper {
    Activity activity;

    public MyPdfPageEventHelper(Activity act) {
        activity=act;
    }

    @Override
    public void onEndPage(PdfWriter pdfWriter, Document document) {

        System.out.println("Creating Waterwark Image in PDF");

        try {

            //Use this method if you want to get image from your Local system
            //Image waterMarkImage = Image.getInstance("E:/tiger.jpg");


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.pdfbackk);
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100, stream);
            Image img;

            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(0, 0);
            img.scalePercent(60f,60f);


            //Get width and height of whole page
            float pdfPageWidth = document.getPageSize().getWidth();
            float pdfPageHeight = document.getPageSize().getHeight();

            //Set waterMarkImage on whole page
            pdfWriter.getDirectContentUnder().addImage(img,
                    pdfPageWidth, 0, 0, pdfPageHeight, 0, 0);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}