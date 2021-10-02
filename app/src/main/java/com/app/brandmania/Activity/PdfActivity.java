package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityPdfBinding;
import com.bumptech.glide.Glide;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
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
                layoutToImage();
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
                if (i == 3) {
                    break;
                }
            }
            binding.servicesTxt.setText(sericesStr);
        }


    }

    String dirpath;

    public void layoutToImage() {

        binding.pdfLayout.setDrawingCacheEnabled(true);
        binding.pdfLayout.buildDrawingCache();
        Bitmap bm = binding.pdfLayout.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/png");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "image.png");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            imageToPDF();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void imageToPDF() throws FileNotFoundException {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BrandManiaPdf";

            Document document = new Document(new Rectangle(595, 850), 0, 0, 0, 0);
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();


            dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(path + "/" + prefManager.getActiveBrand().getName() + ".pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.png");
            float scaler = ((document.getPageSize().getWidth() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            img.setPaddingTop(0f);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

            document.add(img);
            document.close();
            Toast.makeText(act, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
            viewPdf(prefManager.getActiveBrand().getName(), act);
        } catch (Exception e) {

        }
    }

    // Method for opening a pdf file
    private static void viewPdf(String name, Activity act) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + "BrandManiaPdf" + "/" + name + ".pdf");
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
}