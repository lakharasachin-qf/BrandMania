package com.app.brandmania.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.BuildConfig;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityPdfBinding;
import com.app.brandmania.databinding.DialogUpgradeLayoutSecondBinding;
import com.app.brandmania.utils.Utility;
import com.bumptech.glide.Glide;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfActivity extends BaseActivity {
    private ActivityPdfBinding binding;
    private Activity act;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_pdf);

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
                    //freee user
                    askForUpgradeToEnterpisePackage();
                } else {
                    layoutToImage();
                }

            }
        });

        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(act).load(prefManager.getActiveBrand().getLogo()).into(binding.pdfLogo);

        binding.brandName.setText(prefManager.getActiveBrand().getName());

        if (prefManager.getActiveBrand().getEmail().isEmpty()) {
            binding.emailTxtLayout.setVisibility(View.GONE);
        } else {
            binding.emailId.setText(prefManager.getActiveBrand().getEmail());
        }


        if (prefManager.getActiveBrand().getPhonenumber().isEmpty()) {
            binding.contactTxtLayout.setVisibility(View.GONE);
        } else {
            binding.contactText.setText(prefManager.getActiveBrand().getPhonenumber());
        }

        if (prefManager.getActiveBrand().getAddress().isEmpty()) {
            binding.addressEdtLayout.setVisibility(View.GONE);
        } else {
            binding.address.setText(prefManager.getActiveBrand().getAddress());
        }

        if (prefManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("0")) {
            binding.waterMark.setVisibility(View.GONE);
        }

        if (prefManager.getActiveBrand().getBrandService().isEmpty()) {
            binding.services.setVisibility(View.INVISIBLE);
        } else {
            String[] list = prefManager.getActiveBrand().getBrandService().split("[,\n]");
            String sericesStr = "";
            int i = 0;
            for (String s : list) {
                sericesStr = sericesStr + "\n- " + s;
                i++;
                if (i == 5) {
                    break;
                }
            }
            binding.servicesTxt.setText(sericesStr);
        }


    }

    String dirpath;
    File layoutImageFilePng;

    public void layoutToImage() {

        binding.pdfLayout.setDrawingCacheEnabled(true);
        binding.pdfLayout.buildDrawingCache();


        FileOutputStream fileOutputStream = null;
        String name = "image" + System.currentTimeMillis() + ".jpg";
        layoutImageFilePng = new File(act.getCacheDir(), name);

        try {
            fileOutputStream = new FileOutputStream(layoutImageFilePng);
            Bitmap bitmap = binding.pdfLayout.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            imageToPDF();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    String outputFile = "";

    public void imageToPDF() throws FileNotFoundException {
        try {

            HELPER._INIT_FOLDER(Constant.DOCUMENT);
            Document document = new Document(new Rectangle(595, 850), 0, 0, 0, 0);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + Constant.ROOT + "/" + Constant.DOCUMENT;
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();


            dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                PdfWriter.getInstance(document, new FileOutputStream(path + "/" + prefManager.getActiveBrand().getName() + ".pdf")); //  Change pdf's name.
                document.open();
                Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.png");
                float scaler = ((document.getPageSize().getWidth() - 0) / img.getWidth()) * 100;
                img.scalePercent(scaler);
                img.setPaddingTop(0f);
                img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

                document.add(img);
            } else {

                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, prefManager.getActiveBrand().getName() + ".pdf");
                String desDirectory = Environment.DIRECTORY_DOWNLOADS + "/" + Constant.ROOT + "/" + Constant.DOCUMENT;

                outputFile = desDirectory + File.separator + prefManager.getActiveBrand().getName() + ".pdf";
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, desDirectory);

                Uri uri = act.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                PdfWriter.getInstance(document, act.getContentResolver().openOutputStream(uri));  //new FileOutputStream(path + "/" + prefManager.getActiveBrand().getName() + ".pdf")); //  Change pdf's name.
                document.open();
                Image img = Image.getInstance(layoutImageFilePng.getAbsolutePath());
                float scaler = ((document.getPageSize().getWidth() - 0) / img.getWidth()) * 100;
                img.scalePercent(scaler);
                img.setPaddingTop(0f);
                img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(img);
            }
            document.close();
            Toast.makeText(act, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
            viewPdf(prefManager.getActiveBrand().getName(), act);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for opening a pdf file
    private void viewPdf(String name, Activity act) {


        String FilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constant.ROOT + "/" + Constant.DOCUMENT + "/" + name + ".pdf";
        File file;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

            file = new File(FilePath);
            Uri apkURI = FileProvider.getUriForFile(act, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
        } else {

            file = new File(Environment.getExternalStorageDirectory() + "/" + outputFile);
            Uri apkURI = FileProvider.getUriForFile(act, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(intent);

    }


    // ask to upgrade package to 999 for use all frames
    DialogUpgradeLayoutSecondBinding layoutSecondBinding;

    public void askForUpgradeToEnterpisePackage() {
        layoutSecondBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout_second, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(layoutSecondBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(layoutSecondBinding.getRoot());

        layoutSecondBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile", "1");
                act.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        layoutSecondBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        layoutSecondBinding.element3.setText("To download business card, please upgrade your package");
        //alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}