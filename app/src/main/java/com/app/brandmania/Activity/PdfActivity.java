package com.app.brandmania.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.ColorsAdapterPDF;
import com.app.brandmania.Adapter.VisitingCardAdapter;
import com.app.brandmania.BuildConfig;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.VisitingCardHelper;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Fragment.bottom.ColorPickerFragment;
import com.app.brandmania.Model.ColorsModel;
import com.app.brandmania.Model.VisitingCardModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityPdfBinding;
import com.app.brandmania.databinding.DialogUpgradeLayoutSecondBinding;
import com.app.brandmania.databinding.LayoutDigitalCardFifthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardFourthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardOneBinding;
import com.app.brandmania.databinding.LayoutDigitalCardThreeBinding;
import com.app.brandmania.databinding.LayoutDigitalCardTwoBinding;
import com.app.brandmania.utils.Utility;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfActivity extends BaseActivity {
    private ActivityPdfBinding binding;
    private Activity act;
    private Palette colors;
    private ArrayList<VisitingCardModel> digitalCardList;
    private VisitingCardModel CurrentSelectedCard;
    private VisitingCardAdapter visitingCardAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_pdf);

        digitalCardList = new ArrayList<>();
        digitalCardList.addAll(VisitingCardHelper.getDigitalCardList());
        binding.exportIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
                    askForUpgradeToEnterpisePackage();
                } else {
                    frontPageLayoutImage();

                }
            }
        });

        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Picasso.get().load(prefManager.getActiveBrand().getLogo())
                .into(binding.pdfLogo, new Callback() {
                    @Override
                    public void onSuccess() {
                        colors = createPaletteSync(((BitmapDrawable) binding.pdfLogo.getDrawable()).getBitmap());
                        setDigitalCardAdapter();

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

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

        digitalCardList = new ArrayList<>();
        digitalCardList.addAll(VisitingCardHelper.getDigitalCardList());


    }


    public void setAdapter() {
        if (colorsList.size() != 0) {
            colorsList.clear();
            if (colorsAdapterPDF != null) {
                colorsAdapterPDF.notifyDataSetChanged();
            }
        }

        colorsList.addAll(VisitingCardHelper.getColorList(CurrentSelectedCard, colors, act));

        colorsAdapterPDF = new ColorsAdapterPDF(colorsList, act);
        ColorsAdapterPDF.onItemSelectListener onItemSelectListener = new ColorsAdapterPDF.onItemSelectListener() {
            @Override
            public void onItemSelect(ColorsModel model, int position) {
                if (selectedModel != null) {
                    selectedModel.setSelected(false);
                }
                model.setSelected(true);
                selectedModel = model;
                if (colorsAdapterPDF != null) {
                    colorsAdapterPDF.notifyDataSetChanged();
                }
                objectSelectedPosition = position;
                showFragmentList();
            }
        };
        colorsAdapterPDF.setOnItemSelectListener(onItemSelectListener);
        binding.colorList.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));
        binding.colorList.setHasFixedSize(true);
        binding.colorList.setAdapter(colorsAdapterPDF);
        selectedModel = colorsList.get(0);
    }

    int LAYOUT_ONE = 0;
    int LAYOUT_TWO = 1;
    int LAYOUT_THREE = 2;

    ArrayList<ColorsModel> colorsList = new ArrayList<>();
    ColorsAdapterPDF colorsAdapterPDF;
    int SELECTED_LAYOUT = LAYOUT_THREE;
    ColorsModel selectedModel;
    int objectSelectedPosition = 0;

    public void setDigitalCardAdapter() {
        visitingCardAdapter = new VisitingCardAdapter(digitalCardList, act);
        VisitingCardAdapter.onVisitingCardListener onItemSelectListener = (layout, visitingCardModel) -> {
            CurrentSelectedCard = visitingCardModel;
            addDynamicLayout();
        };
        visitingCardAdapter.setListener(onItemSelectListener);

        binding.cardList.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));
        binding.cardList.setHasFixedSize(true);
        binding.cardList.setAdapter(visitingCardAdapter);

        CurrentSelectedCard = digitalCardList.get(0);
        addDynamicLayout();
    }


    ColorPickerFragment bottomSheetFragment;

    public void showFragmentList() {
        bottomSheetFragment = new ColorPickerFragment();
        ColorPickerFragment.OnColorChoose onColorChoose = color -> {
            VisitingCardHelper.applyColor(CurrentSelectedCard, color, selectedModel);
            selectedModel.setColor(color);
            colorsList.set(objectSelectedPosition, selectedModel);
            colorsAdapterPDF.notifyItemChanged(objectSelectedPosition);
        };

        bottomSheetFragment.setOnColorChoose(onColorChoose);
        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }

        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }


    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }


    String dirpath;
    File frontPage;
    File backPage;

    public void addDynamicLayout() {
        binding.container.removeAllViews();
        if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            LayoutDigitalCardOneBinding oneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_digital_card_one, null, false);

            CurrentSelectedCard.setOneBinding(oneBinding);
            binding.container.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.container.requestLayout();
            binding.container.addView(oneBinding.getRoot());
            View view = oneBinding.getRoot();
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            VisitingCardHelper.loadDataCardOne(act, oneBinding, colors);
            setAdapter();
        } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            LayoutDigitalCardTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_digital_card_two, null, false);

            CurrentSelectedCard.setTwoBinding(twoBinding);
            binding.container.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.container.requestLayout();
            binding.container.addView(twoBinding.getRoot());
            View view = twoBinding.getRoot();
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            VisitingCardHelper.loadDataCardTwo(act, twoBinding, colors);
            setAdapter();
        } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            LayoutDigitalCardThreeBinding threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_digital_card_three, null, false);

            CurrentSelectedCard.setThreeBinding(threeBinding);
            binding.container.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.container.requestLayout();
            binding.container.addView(threeBinding.getRoot());
            View view = threeBinding.getRoot();
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            VisitingCardHelper.loadDataCardThree(act, threeBinding, colors);
            setAdapter();
        } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            LayoutDigitalCardFourthBinding fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_digital_card_fourth, null, false);

            CurrentSelectedCard.setFourBinding(fourBinding);
            binding.container.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.container.requestLayout();
            binding.container.addView(fourBinding.getRoot());
            View view = fourBinding.getRoot();
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            VisitingCardHelper.loadDataCardFour(act, fourBinding, colors);
            setAdapter();
        } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            LayoutDigitalCardFifthBinding fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_digital_card_fifth, null, false);

            CurrentSelectedCard.setFiveBinding(fiveBinding);
            binding.container.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.container.requestLayout();
            binding.container.addView(fiveBinding.getRoot());
            View view = fiveBinding.getRoot();
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            VisitingCardHelper.loadDataCardFive(act, fiveBinding, colors);
            setAdapter();
        }
    }


    public void layoutToImage() {
        binding.pdfLayout.setDrawingCacheEnabled(true);
        binding.pdfLayout.buildDrawingCache();
        FileOutputStream fileOutputStream = null;
        String name = "image" + System.currentTimeMillis() + ".jpg";
        frontPage = new File(act.getCacheDir(), name);

        try {
            fileOutputStream = new FileOutputStream(frontPage);
            Bitmap bitmap = binding.pdfLayout.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            imageToPDF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void frontPageLayoutImage() {
        if (CurrentSelectedCard != null) {
            if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
                CurrentSelectedCard.getOneBinding().frontPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getOneBinding().frontPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
                CurrentSelectedCard.getTwoBinding().frontPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getTwoBinding().frontPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
                CurrentSelectedCard.getThreeBinding().frontPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getThreeBinding().frontPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
                CurrentSelectedCard.getFourBinding().frontPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getFourBinding().frontPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
                CurrentSelectedCard.getFiveBinding().frontPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getFiveBinding().frontPage.buildDrawingCache();
            }
            FileOutputStream fileOutputStream = null;
            String name = "image" + System.currentTimeMillis() + ".jpg";
            frontPage = new File(act.getCacheDir(), name);

            try {
                fileOutputStream = new FileOutputStream(frontPage);
                Bitmap bitmap = null;

                if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
                    bitmap = CurrentSelectedCard.getOneBinding().frontPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
                    bitmap = CurrentSelectedCard.getTwoBinding().frontPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
                    bitmap = CurrentSelectedCard.getThreeBinding().frontPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
                    bitmap = CurrentSelectedCard.getFourBinding().frontPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
                    bitmap = CurrentSelectedCard.getFiveBinding().frontPage.getDrawingCache();
                }

                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                //imageToPDF();

                backPageLayoutImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void backPageLayoutImage() {
        if (CurrentSelectedCard != null) {
            if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
                CurrentSelectedCard.getOneBinding().backPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getOneBinding().backPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
                CurrentSelectedCard.getTwoBinding().backPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getTwoBinding().backPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
                CurrentSelectedCard.getThreeBinding().backPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getThreeBinding().backPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
                CurrentSelectedCard.getFourBinding().backPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getFourBinding().backPage.buildDrawingCache();
            } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
                CurrentSelectedCard.getFiveBinding().backPage.setDrawingCacheEnabled(true);
                CurrentSelectedCard.getFiveBinding().backPage.buildDrawingCache();
            }

            FileOutputStream fileOutputStream = null;
            String name = "image" + System.currentTimeMillis() + ".jpg";
            backPage = new File(act.getCacheDir(), name);

            try {
                fileOutputStream = new FileOutputStream(backPage);
                Bitmap bitmap = null;

                if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
                    bitmap = CurrentSelectedCard.getOneBinding().backPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
                    bitmap = CurrentSelectedCard.getTwoBinding().backPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
                    bitmap = CurrentSelectedCard.getThreeBinding().backPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
                    bitmap = CurrentSelectedCard.getFourBinding().backPage.getDrawingCache();
                } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
                    bitmap = CurrentSelectedCard.getFiveBinding().backPage.getDrawingCache();
                }
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                imageToPDF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String outputFile = "";

    public void imageToPDF() throws FileNotFoundException {
        try {
            HELPER._INIT_FOLDER(Constant.DOCUMENT);
            Document document = new Document(new Rectangle(1050, 600), 0, 0, 0, 0);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + Constant.ROOT + "/" + Constant.DOCUMENT;
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                PdfWriter.getInstance(document, new FileOutputStream(path + "/" + prefManager.getActiveBrand().getName() + ".pdf")); //  Change pdf's name.
                document.open();
                Image img = Image.getInstance(frontPage.getAbsolutePath());
                float scaler = ((document.getPageSize().getWidth() - 0) / img.getWidth()) * 100;
                img.scalePercent(scaler);
                img.setPaddingTop(0f);
                img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(img);

                img = Image.getInstance(backPage.getAbsolutePath());
                scaler = ((document.getPageSize().getWidth() - 0) / img.getWidth()) * 100;
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

                PdfWriter.getInstance(document, act.getContentResolver().openOutputStream(uri));
                document.open();

                Image img = Image.getInstance(frontPage.getAbsolutePath());
                float scaler = ((document.getPageSize().getWidth() - 0) / img.getWidth()) * 100;
                img.scalePercent(scaler);
                img.setPaddingTop(0f);
                img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(img);

                img = Image.getInstance(backPage.getAbsolutePath());
                scaler = ((document.getPageSize().getWidth() - 0) / img.getWidth()) * 100;
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
        layoutSecondBinding.viewPackage.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(act, PackageActivity.class);
            intent.putExtra("Profile", "1");
            act.startActivity(intent);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });
        layoutSecondBinding.closeBtn.setOnClickListener(v -> alertDialog.dismiss());
        layoutSecondBinding.element3.setText("To download business card, please upgrade your package");
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}