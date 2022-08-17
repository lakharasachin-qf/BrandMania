package com.app.brandmania.Common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class HELPER {

    /**
     * INTENT-EVENT
     */
    public static void ROUTE(Activity act, Class routeName) {
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }


    /**
     * HOME FRAGMENT *
     */
    @NonNull
    public static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    public static void generatePDF(Activity act, PreafManager preafManager, Bitmap brandLogo) {
        Document document = new Document(PageSize.A4);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BrandManiaPdf";

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "brandmania.pdf");


        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(act.getResources(), R.drawable.pdfbackk);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image img;

            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(0, 0);
            img.scalePercent(60f, 60f);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(document, fOut);
            writer.setPageEvent(new MyPdfPageEventHelper(act));
            document.open();

            //Drawable d = act.getResources().getDrawable(R.drawable.pdf_banner);
            //BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = brandLogo;// ((BitmapDrawable) binding.pdfLogo.getDrawable()).getBitmap();//bitDw.getBitmap();
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
            BaseColor baseColorFirst = new BaseColor(173, 39, 83);
            canvas.setColorStroke(baseColorFirst);
            canvas.moveTo(30, 500);
            canvas.lineTo(570, 500);
            canvas.setLineWidth(2f);
            canvas.closePathStroke();

            //For Second UnderLine
            PdfContentByte canvasSecond = writer.getDirectContent();
            BaseColor baseColorSecond = new BaseColor(173, 39, 83);
            canvasSecond.setColorStroke(baseColorSecond);
            canvasSecond.moveTo(30, 492);
            canvasSecond.lineTo(570, 492);
            canvasSecond.setLineWidth(5f);
            canvasSecond.closePathStroke();

            //For Third UnderLine
            PdfContentByte canvasThird = writer.getDirectContent();
            BaseColor baseColorThird = new BaseColor(173, 39, 83);
            canvasThird.setColorStroke(baseColorThird);
            canvasThird.moveTo(30, 484);
            canvasThird.lineTo(570, 484);
            canvasThird.setLineWidth(2f);
            canvasThird.closePathStroke();


            //For Contact Number and Contact Logo..........
            if (preafManager.getActiveBrand().getPhonenumber() != null && !preafManager.getActiveBrand().getPhonenumber().isEmpty()) {
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
            if (preafManager.getActiveBrand().getEmail() != null && !preafManager.getActiveBrand().getEmail().isEmpty()) {
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
            if (preafManager.getActiveBrand().getWebsite() != null && !preafManager.getActiveBrand().getWebsite().isEmpty()) {
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

            if (preafManager.getActiveBrand().getBrandService() != null && !preafManager.getActiveBrand().getBrandService().isEmpty()) {
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

    public static String getDateAfterSevenDay() {

//        SimpleDateFormat dateFormat= new SimpleDateFormat("EEEE dd.MM.yyyy");
//        Calendar currentCal = Calendar.getInstance();
//        String currentdate=dateFormat.format(currentCal.getTime());
//        currentCal.add(Calendar.DATE, 7);
//        String toDate=dateFormat.format(currentCal.getTime());

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, +7);
        Date afterDay = cal.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(afterDay);
    }

    public static Date StringToDate(String apiData) {
        String dtStart = apiData;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dtStart);
            System.out.println(date);
            Log.e("DateTimeValue", String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String simpleDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static boolean IsTwoDateComparison(String ApiData, Activity act, String daysCounter) {
        PreafManager pre = new PreafManager(act);
        //startDate
        Calendar c = Calendar.getInstance();
        c.setTime(StringToDate(ApiData));
        Date startDate = c.getTime();
        String SDate = simpleDateFormat(startDate);
        Log.e("Created Date", SDate);
        //TodayDate
        Date TodayDATE = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String TodayDate = simpleDateFormat.format(TodayDATE);
        Log.e("TodayDate", TodayDate);

        //String todayDate = "23-06-2022";

        //endDate
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(c.getTime());
        cal.add(Calendar.DATE, Integer.parseInt(daysCounter));
        //cal.add(Calendar.DATE, 7);
        Date endDate = cal.getTime();
        String eDate = simpleDateFormat(endDate);
        Log.e("EndDate", eDate);

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date convertedEndDate = formatter.parse(eDate);
            Date convertedStartDate = formatter.parse(SDate);
            Date convertTodayDate = formatter.parse(TodayDate);
            assert convertedStartDate != null;
            assert convertedEndDate != null;
            assert convertTodayDate != null;

            if (convertTodayDate.compareTo(convertedStartDate) >= 0 && convertTodayDate.compareTo(convertedEndDate) <= 0) {
                Log.e("dateIsBetween", "true");
                pre = new PreafManager(act);
                pre.setFreeUserDownloadForOneWeak(true);
                return true;
            } else {
                Log.e("dateIsBetween", "false");
                pre.setFreeUserDownloadForOneWeak(false);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Date stringToDaTE(String dates) {

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = format.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean IsDateComparison(String ApiData) {

        Calendar c = Calendar.getInstance();
        c.setTime(StringToDate(ApiData));
        //c.add(Calendar.DATE, 7);
        Log.e("FromDate", String.valueOf(c.getTime()));
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(c.getTime());
        cal.add(Calendar.DATE, 7);
        Date toDate = cal.getTime();
        Log.e("toDate", String.valueOf(toDate));

        if (c.getTime().compareTo(toDate) < 0) {
            Log.e("isDateBetween", "yes");
            return true;
        } else {
            Log.e("isDAteBetween", "no");
        }
        return false;
    }

    public static String DateToStringApi(String apiData) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

        Date d = null;
        try {
            d = input.parse(apiData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d != null) {
            Log.e("convertDateValue", output.format(d));
        }
        return output.format(d);
    }

    public static String DateToStringApis(String apiData) {
        SimpleDateFormat input = new SimpleDateFormat("EEE MM dd kk:mm:ss zzzz yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

        Date d = null;
        try {
            d = input.parse(apiData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d != null) {
            Log.e("convertDateValuesss", output.format(d));
        }
        return output.format(d);
    }

    public static void DateToString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        String dateTime = dateFormat.format(date);
        System.out.println("Current Date Time : " + dateTime);
    }

    public static void date() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(c);

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, +7);
        Date afterDay = cal.getTime();

        String dateStr = "16-06-2022";
        SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.before(dateObj)) {
            Log.e("yesAfter", "yes");
            // In between
        } else {
            Log.e("yesAfter", "no");
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

    public static void WHATSAPP_REDIRECTION(Activity act, String businessName, String mobileNumber) {
        try {
            String number = Constant.ADMIN_CONTACT_NUMBER;
            String BrandContact = "\nRegistered Number: ";
            String text = "Hello *BrandMania* ,  \n" + "This is request to add  *Frame* For BrandName:" + businessName + BrandContact + mobileNumber;
            String toNumber = "91" + number;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            act.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String deviceINFO() {
        return "VERSION.RELEASE : " + Build.VERSION.RELEASE + "- VERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT + " - BRAND : " + Build.BRAND;
    }

    public static void WHATSAPP_REDIRECTION_2(Activity act, String businessName, String mobileNumber) {
        try {
            String number = Constant.ADMIN_CONTACT_NUMBER;
            String BrandContact = "\nRegistered Number Is: ";
            String BusinessName = "\nBrand Name: " + businessName;
            //String text = "Hello *BrandMania* ,  \n" + "This is request From BrandName: " +businessName + BrandContact + mobileNumber+"\n Issue : ";
            String text = "Hello *BrandMania*,\n" + "I wish To Purchase a plan," + BrandContact + mobileNumber + BusinessName;
            String toNumber = "91" + number;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            act.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String realPathForImage(final Context context, final Uri uri) {

        final boolean isKitKat = true;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    //For CreatFileeDisc For Download Image.........................
    public static File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "BrandMania");
    }


    public static void _INIT_FOLDER(String folderName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String destURL = null;
            File baseFolder;
            if (folderName.equalsIgnoreCase(Constant.ROOT)) {
                destURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT;
            }

            if (folderName.equalsIgnoreCase(Constant.DATA)) {
                destURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/" + Constant.DATA;
            }
            if (folderName.equalsIgnoreCase(Constant.VIDEOS)) {
                destURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/" + Constant.VIDEOS;

            }
            if (folderName.equalsIgnoreCase(Constant.GIF)) {
                destURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/" + Constant.GIF;
            }
            if (folderName.equalsIgnoreCase(Constant.IMAGES)) {
                destURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/" + Constant.IMAGES;
            }

            if (folderName.equalsIgnoreCase(Constant.DOCUMENT)) {
                destURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/" + Constant.DOCUMENT;
            }

            if (destURL != null) {
                baseFolder = new File(destURL);
                if (!baseFolder.exists()) {
                    baseFolder.mkdir();
                }
            }
        } else {
            File baseFolder = null;

            if (folderName.equalsIgnoreCase(Constant.ROOT)) {
                baseFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constant.ROOT);
            }
            if (folderName.equalsIgnoreCase(Constant.DATA)) {
                baseFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/", Constant.DATA);
            }
            if (folderName.equalsIgnoreCase(Constant.VIDEOS)) {
                baseFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/", Constant.VIDEOS);
            }
            if (folderName.equalsIgnoreCase(Constant.IMAGES)) {
                baseFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/", Constant.IMAGES);
            }
            if (folderName.equalsIgnoreCase(Constant.DOCUMENT)) {
                baseFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/", Constant.DOCUMENT);
            }
            if (folderName.equalsIgnoreCase(Constant.GIF)) {
                baseFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + Constant.ROOT + "/", Constant.GIF);
            }

            if (!baseFolder.exists()) {
                baseFolder.mkdirs();
            }

        }
    }
}
