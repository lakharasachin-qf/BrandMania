package com.app.brandadmin.Connection;

import android.app.Activity;

import com.app.brandadmin.Common.PreafManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class WatermarkPageEvent extends PdfPageEventHelper {
   // Font  = FontFactory.getFont("assets/font/robotobold.ttf", 52,new GrayColor(0.85f)); //10 is the size

   Font FONT = new Font(Font.FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.85f));
    Activity activity;
    public WatermarkPageEvent(Activity act) {
        activity=act;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PreafManager preafManager=new PreafManager(activity);
        ColumnText.showTextAligned(writer.getDirectContentUnder(),
                Element.ALIGN_CENTER, new Phrase(preafManager.getActiveBrand().getName(), FONT),
                297.5f, 421, writer.getPageNumber() % 4 == 1 ? 45 : -45);
    }
}