package com.app.brandmania.Common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.app.brandmania.Activity.custom.CustomViewAllActivit;
import com.app.brandmania.Connection.MyPdfPageEventHelper;
import com.app.brandmania.R;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HELPER {

    /** INTENT-EVENT*/
    public static void ROUTE(Activity act, Class routeName){
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }


    /** HOME FRAGMENT * */
    @NonNull
    public static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    public static void generatePDF(Activity act,PreafManager preafManager,Bitmap brandLogo){
        Document document = new Document(PageSize.A4);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BrandManiaPdf";

        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "brandmania.pdf");


        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(act.getResources(), R.drawable.pdfbackk);
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100, stream);
            Image img;

            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(0, 0);
            img.scalePercent(60f,60f);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(document, fOut);
            writer.setPageEvent(new MyPdfPageEventHelper(act));
            document.open();

            //Drawable d = act.getResources().getDrawable(R.drawable.pdf_banner);
            //BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp =brandLogo;// ((BitmapDrawable) binding.pdfLogo.getDrawable()).getBitmap();//bitDw.getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            Image image = Image.getInstance(stream1.toByteArray());
            image.scalePercent(25);

            image.setAlignment(Element.ALIGN_CENTER);
            image.setAbsolutePosition(240, 670);

            document.add(image);




            Paragraph preface = new Paragraph();

            //For Brand Name..............
            Font brandName = FontFactory.getFont("assets/font/montserrat_bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 40); //10 is the size
            brandName.setColor(WebColors.getRGBColor("#faa81e"));
            addEmptyLine(preface, 11);
            preface.add(new Paragraph(preafManager.getActiveBrand().getName(), brandName));



            //For Address
            Font address = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 23); //10 is the size
            preface.add(new Paragraph(preafManager.getActiveBrand().getAddress(), address));
            addEmptyLine(preface, 0);
            preface.setIndentationLeft(0);
            document.add(preface);

            //For First UnderLine
            PdfContentByte canvas = writer.getDirectContent();
            BaseColor baseColorFirst=new BaseColor(173,39,83);
            canvas.setColorStroke(baseColorFirst);
            canvas.moveTo(30, 500);
            canvas.lineTo(570, 500);
            canvas.setLineWidth(2f);
            canvas.closePathStroke();

            //For Second UnderLine
            PdfContentByte canvasSecond = writer.getDirectContent();
            BaseColor baseColorSecond=new BaseColor(173,39,83);
            canvasSecond.setColorStroke(baseColorSecond);
            canvasSecond.moveTo(30, 492);
            canvasSecond.lineTo(570, 492);
            canvasSecond.setLineWidth(5f);
            canvasSecond.closePathStroke();

            //For Third UnderLine
            PdfContentByte canvasThird = writer.getDirectContent();
            BaseColor baseColorThird=new BaseColor(173,39,83);
            canvasThird.setColorStroke(baseColorThird);
            canvasThird.moveTo(30, 484);
            canvasThird.lineTo(570, 484);
            canvasThird.setLineWidth(2f);
            canvasThird.closePathStroke();





            //For Contact Number and Contact Logo..........
            if (preafManager.getActiveBrand().getPhonenumber()!=null && !preafManager.getActiveBrand().getPhonenumber().isEmpty()) {
                //  Drawable contact = act.getResources().getDrawable(R.drawable.ic_call_for_pdf);
                Paragraph prefaceClicableContact = new Paragraph();
                //BitmapDrawable bitContact = ((BitmapDrawable) contact);
                @SuppressLint("UseCompatLoadingForDrawables") Bitmap bmpContact = HELPER.getBitmapFromDrawable(act.getResources().getDrawable(R.drawable.ic_call_for_pdf));//bitContact.getBitmap();
                ByteArrayOutputStream streamContact = new ByteArrayOutputStream();
                bmpContact.compress(Bitmap.CompressFormat.PNG, 100, streamContact);
                Image imageContact = Image.getInstance(streamContact.toByteArray());
                imageContact.scalePercent(50);
                imageContact.setAbsolutePosition(30f, 415f);
                imageContact.setAlignment(Element.ALIGN_LEFT);
                document.add(imageContact);
                addEmptyLine(prefaceClicableContact, 3);
                prefaceClicableContact.add(new Phrase(""));
                prefaceClicableContact.setIndentationLeft(50);
                Font contactFont = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
                contactFont.setColor(WebColors.getRGBColor("#ad2753"));
                Anchor anchor = new Anchor(preafManager.getActiveBrand().getPhonenumber(), contactFont);
                anchor.setReference(String.valueOf(Uri.parse("tel:" + 91 + preafManager.getActiveBrand().getPhonenumber())));
                prefaceClicableContact.add(anchor);
                document.add(prefaceClicableContact);
            }
            //For Gmail Id and Gmail logo................
            if (preafManager.getActiveBrand().getEmail()!=null &&  !preafManager.getActiveBrand().getEmail().isEmpty()) {
                Paragraph prefaceClicableEmail = new Paragraph();
                //   Drawable email = act.getResources().getDrawable(R.drawable.ic_call_for_pdf);
                //   BitmapDrawable bitEmail = ((BitmapDrawable) email);
                @SuppressLint("UseCompatLoadingForDrawables") Bitmap bmpEmail = HELPER.getBitmapFromDrawable(act.getResources().getDrawable(R.drawable.ic_gmail_for_pdf)); //bitEmail.getBitmap();
                ByteArrayOutputStream streamEmail = new ByteArrayOutputStream();
                bmpEmail.compress(Bitmap.CompressFormat.PNG, 100, streamEmail);
                Image imageEmail = Image.getInstance(streamEmail.toByteArray());
                imageEmail.scalePercent(50);
                imageEmail.setAbsolutePosition(30f, 358f);
                imageEmail.setAlignment(Element.ALIGN_LEFT);
                document.add(imageEmail);
                addEmptyLine(prefaceClicableEmail, 1);
                prefaceClicableEmail.add(new Phrase(""));
                prefaceClicableEmail.setIndentationLeft(50);
                Font emailFont = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
                emailFont.setColor(WebColors.getRGBColor("#ad2753"));
                Anchor anchorEmail = new Anchor(preafManager.getActiveBrand().getEmail(), emailFont);
                anchorEmail.setReference(String.valueOf(Uri.parse("mailto:" + preafManager.getActiveBrand().getEmail())));
                prefaceClicableEmail.add(anchorEmail);
                document.add(prefaceClicableEmail);
            }
            //For Website and websiteLogo..................
            if (preafManager.getActiveBrand().getWebsite()!=null && !preafManager.getActiveBrand().getWebsite().isEmpty()) {
                Paragraph prefaceClicableWebsite = new Paragraph();
                //  Drawable website = act.getResources().getDrawable(R.drawable.ic_call_for_pdf);
                //   BitmapDrawable bitWebsite = ((BitmapDrawable) website);
                @SuppressLint("UseCompatLoadingForDrawables") Bitmap bmpWebsite = HELPER.getBitmapFromDrawable(act.getResources().getDrawable(R.drawable.ic_website_for_pdf));//bitWebsite.getBitmap();
                ByteArrayOutputStream streamWebsite = new ByteArrayOutputStream();
                bmpWebsite.compress(Bitmap.CompressFormat.PNG, 100, streamWebsite);
                Image imageWebsite = Image.getInstance(streamWebsite.toByteArray());
                imageWebsite.scalePercent(50);
                imageWebsite.setAbsolutePosition(30f, 300f);
                imageWebsite.setAlignment(Element.ALIGN_LEFT);
                addEmptyLine(prefaceClicableWebsite, 1);
                prefaceClicableWebsite.setIndentationLeft(50);
                Font websiteFont = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
                websiteFont.setColor(WebColors.getRGBColor("#ad2753"));
                Anchor anchorWebsite = new Anchor(preafManager.getActiveBrand().getWebsite(), websiteFont);
                anchorWebsite.setReference(preafManager.getActiveBrand().getWebsite());
                prefaceClicableWebsite.add(anchorWebsite);
                document.add(prefaceClicableWebsite);
                document.add(imageWebsite);
            }

            if (preafManager.getActiveBrand().getBrandService()!=null && !preafManager.getActiveBrand().getBrandService().isEmpty()) {
                Paragraph prefaceClicableServicesTag = new Paragraph();
                addEmptyLine(prefaceClicableServicesTag, 1);
                Font brandServicetag = FontFactory.getFont("assets/font/robotobold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 30); //10 is the size
                brandServicetag.setColor(WebColors.getRGBColor("#faa81e"));
                prefaceClicableServicesTag.add(new Paragraph("Service:", brandServicetag));
                prefaceClicableServicesTag.setIndentationLeft(0);
                document.add(prefaceClicableServicesTag);


                Paragraph paragraphClicableService = new Paragraph();
                addEmptyLine(paragraphClicableService, 0);
                paragraphClicableService.setIndentationLeft(0);
                Font bsuinessService = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
                bsuinessService.setColor(WebColors.getRGBColor("#000"));
                String[] list = preafManager.getActiveBrand().getBrandService().split("[,\n]");
                for (String s : list) {
                    paragraphClicableService.add(new Paragraph("\u2022\u00a0" + s, bsuinessService));
                }
                paragraphClicableService.setIndentationLeft(0);
                document.add(paragraphClicableService);
            }
            document.close();
            viewPdf(act);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    // Method for opening a pdf file
    private static void viewPdf(Activity act) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + "BrandManiaPdf" + "/" + "brandmania.pdf");
        Uri path = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            act.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(act, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    public static void WHATSAPP_REDIRECTION(Activity act, String businessName,String mobileNumber){
        try {
            String number = Constant.ADMIN_CONTACT_NUMBER;
            String BrandContact = "\nRegistered Number: ";
            String text = "Hello *BrandMania* ,  \n" + "This is request to add  *Frame* For BrandName:" +businessName + BrandContact + mobileNumber;
            String toNumber = "91" + number;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            act.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void WHATSAPP_REDIRECTION_2(Activity act, String businessName,String mobileNumber){
        try {
            String number = Constant.ADMIN_CONTACT_NUMBER;
            String BrandContact = "\nRegistered Number: ";
            String text = "Hello *BrandMania* ,  \n" + "This is request From BrandName: " +businessName + BrandContact + mobileNumber+"\n Issue : ";
            String toNumber = "91" + number;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            act.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
