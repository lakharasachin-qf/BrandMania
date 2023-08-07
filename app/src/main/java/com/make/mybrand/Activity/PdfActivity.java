package com.make.mybrand.Activity;

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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Activity.brand.AddBrandMultipleActivity;
import com.make.mybrand.Activity.brand.UpdateBandList;
import com.make.mybrand.Activity.packages.PackageActivity;
import com.make.mybrand.Adapter.BackgroundColorsAdapter;
import com.make.mybrand.Adapter.ColorsAdapterPDF;
import com.make.mybrand.Adapter.IconsColorsAdapter;
import com.make.mybrand.Adapter.TextColorsAdapter;
import com.make.mybrand.Adapter.VisitingCardAdapter;
import com.make.mybrand.BuildConfig;
import com.make.mybrand.Common.Constant;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Common.VisitingCardHelper;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.Fragment.bottom.ColorPickerFragment;
import com.make.mybrand.Model.BackgroundColorsModel;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.Model.ColorsModel;
import com.make.mybrand.Model.IconsColorsModel;
import com.make.mybrand.Model.TextColorsModel;
import com.make.mybrand.Model.VisitingCardModel;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ActivityPdfBinding;
import com.make.mybrand.databinding.DialogUpgradeLayoutSecondBinding;
import com.make.mybrand.databinding.LayoutDigitalCardFifthBinding;
import com.make.mybrand.databinding.LayoutDigitalCardFourthBinding;
import com.make.mybrand.databinding.LayoutDigitalCardOneBinding;
import com.make.mybrand.databinding.LayoutDigitalCardThreeBinding;
import com.make.mybrand.databinding.LayoutDigitalCardTwoBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;
import com.make.mybrand.views.MyBounceInterpolator;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PdfActivity extends BaseActivity {
    private ActivityPdfBinding binding;
    private Activity act;
    private Palette colors;
    private ArrayList<VisitingCardModel> digitalCardList;
    private VisitingCardModel CurrentSelectedCard;
    private VisitingCardAdapter visitingCardAdapter;
    public boolean isUserPaid = true;
    private boolean isLoading = false;
    int objectSelectedPosition = 0;

    ArrayList<BrandListItem> multiListItems = new ArrayList<>();

    ArrayList<ColorsModel> colorsList = new ArrayList<>();
    ColorsModel selectedModel;
    ColorsAdapterPDF colorsAdapterPDF;

    ArrayList<BackgroundColorsModel> backgroundColorsList = new ArrayList<>();
    BackgroundColorsModel backgroundSelectModel;
    BackgroundColorsAdapter backgroundColorsAdapter;

    ArrayList<TextColorsModel> textColorsList = new ArrayList<>();
    TextColorsModel textSelectModel;
    TextColorsAdapter textColorsAdapter;

    ArrayList<IconsColorsModel> iconsColorsList = new ArrayList<>();
    IconsColorsModel iconsSelectModel;
    IconsColorsAdapter iconsColorsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_pdf);

        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (prefManager.getActiveBrand() != null) {

            digitalCardList = new ArrayList<>();
            digitalCardList.addAll(VisitingCardHelper.getDigitalCardList());
            binding.saveIcon.setOnClickListener(v -> {
                //New Update[07/07/2023]
                if (prefManager.getAllFreeImage()) {
                    frontPageLayoutImage(false);
                } else {
                    if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
                        askForUpgradeToEnterprisePackage();
                        // if (CurrentSelectedCard.isFree()) {
                        // isUserPaid = true;
                        // frontPageLayoutImage(false);
                        // } else {
                        // isUserPaid = false;
                        // }

                    } else {
                        if (Utility.isPackageExpired(act)) {
                            // if (CurrentSelectedCard.isFree()) {
                            // isUserPaid = true;
                            // frontPageLayoutImage(false);
                            // } else {
                            // isUserPaid = false;
                            // }
                            askForUpgradeToEnterprisePackage();
                        } else {
                            frontPageLayoutImage(false);
                        }
                    }
                }

                //Old Update
//                    if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
//
//                        askForUpgradeToEnterprisePackage();
//                        // if (CurrentSelectedCard.isFree()) {
//                        // isUserPaid = true;
//                        // frontPageLayoutImage(false);
//                        // } else {
//                        // isUserPaid = false;
//                        // }
//
//                    } else {
//                        if (Utility.isPackageExpired(act)) {
//                            // if (CurrentSelectedCard.isFree()) {
//                            // isUserPaid = true;
//                            // frontPageLayoutImage(false);
//                            // } else {
//                            // isUserPaid = false;
//                            // }
//                            askForUpgradeToEnterprisePackage();
//                        } else {
//                            frontPageLayoutImage(false);
//                        }
//                    }
            });
            binding.exportIcon.setOnClickListener(v -> {
                //New Update[07/07/2023]
                // frontPageLayoutImage(true);

                if (prefManager.getAllFreeImage()) {
                    frontPageLayoutImage(true);
                } else {
                    if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
                        // if (CurrentSelectedCard.isFree()) {
                        // isUserPaid = true;
                        // frontPageLayoutImage(true);
                        // } else {
                        // isUserPaid = false;
                        // }
                        askForUpgradeToEnterprisePackage();

                    } else {
                        if (Utility.isPackageExpired(act)) {
                            // if (CurrentSelectedCard.isFree()) {
                            // isUserPaid = true;
                            // frontPageLayoutImage(true);
                            // } else {
                            // isUserPaid = false;
                            // askForUpgradeToEnterpisePackage();
                            // }
                            askForUpgradeToEnterprisePackage();
                        } else {
                            frontPageLayoutImage(true);
                        }
                    }
                }

                //Old Update
//                if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
//
//                    // if (CurrentSelectedCard.isFree()) {
//                    // isUserPaid = true;
//                    // frontPageLayoutImage(true);
//                    // } else {
//                    // isUserPaid = false;
//                    // }
//                    askForUpgradeToEnterpisePackage();
//
//                } else {
//                    if (Utility.isPackageExpired(act)) {
//                        // if (CurrentSelectedCard.isFree()) {
//                        // isUserPaid = true;
//                        // frontPageLayoutImage(true);
//                        // } else {
//                        // isUserPaid = false;
//                        // askForUpgradeToEnterpisePackage();
//                        // }
//                        askForUpgradeToEnterpisePackage();
//                    } else {
//                        frontPageLayoutImage(true);
//                    }
//                }
            });

            activity(0);

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

            binding.alertText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.ROUTE(act, UpdateBandList.class);
                }
            });

            if (prefManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("0")) {
                binding.waterMark.setVisibility(View.GONE);
            }

            if (prefManager.getActiveBrand().getBrandService() == null) {
                binding.services.setVisibility(View.INVISIBLE);
            } else {
                String[] list = prefManager.getActiveBrand().getBrandService().split("[,\n]");
                String sericesStr = "";
                int i = 0;
                for (int j = 0; j < list.length; j++) {
                    String s = list[j];
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
        } else {
            binding.scrollView.setVisibility(View.GONE);
            binding.loader.setVisibility(View.GONE);
            binding.saveIcon.setVisibility(View.GONE);
            binding.exportIcon.setVisibility(View.GONE);
            binding.content.setVisibility(View.VISIBLE);
            animateButton();
            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.ROUTE(act, AddBrandMultipleActivity.class);
                }
            });

        }
    }


    void animateButton() {
        final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce);
        double animationDuration = 4 * 1000;
        //myAnim.setDuration((long)animationDuration);
        myAnim.setRepeatCount(Animation.INFINITE);

        // Use custom animation interpolator to achieve the bounce effect
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);

        myAnim.setInterpolator(interpolator);

        // Animate the button
        binding.continueBtn.startAnimation(myAnim);


        // Run button animation again after it finished
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animateButton();
            }
        });
    }

    public void setBackgroundAdapter() {

        if (backgroundColorsList.size() != 0) {
            backgroundColorsList.clear();
            if (backgroundColorsAdapter != null) {
                backgroundColorsAdapter.notifyDataSetChanged();
            }
        }
        backgroundColorsList.addAll(VisitingCardHelper.getBackgroundColorList(CurrentSelectedCard, colors, act));

        backgroundColorsAdapter = new BackgroundColorsAdapter(backgroundColorsList, act);
        BackgroundColorsAdapter.onItemSelectListener onItemSelectListener = new BackgroundColorsAdapter.onItemSelectListener() {
            @Override
            public void onItemSelect(BackgroundColorsModel model, int position) {
                if (backgroundSelectModel != null) {
                    backgroundSelectModel.setSelected(false);
                }
                model.setSelected(true);
                backgroundSelectModel = model;
                if (backgroundColorsAdapter != null) {
                    backgroundColorsAdapter.notifyDataSetChanged();
                }
                objectSelectedPosition = position;
                showBackgroundFragmentList();
            }
        };
        backgroundColorsAdapter.setOnItemSelectListener(onItemSelectListener);


        binding.backgroundcolorList.setLayoutManager(new GridLayoutManager(act, 3));
        binding.backgroundcolorList.setHasFixedSize(true);
        binding.backgroundcolorList.setAdapter(backgroundColorsAdapter);
        backgroundSelectModel = backgroundColorsList.get(0);
    }

    public void setTextAdapter() {

        if (textColorsList.size() != 0) {
            textColorsList.clear();
            if (textColorsAdapter != null) {
                textColorsAdapter.notifyDataSetChanged();
            }
        }

        textColorsList.addAll(VisitingCardHelper.getTextColorList(CurrentSelectedCard, colors, act));

        textColorsAdapter = new TextColorsAdapter(textColorsList, act);
        TextColorsAdapter.onItemSelectListener onItemSelectListener = new TextColorsAdapter.onItemSelectListener() {
            @Override
            public void onItemSelect(TextColorsModel model, int position) {
                if (textSelectModel != null) {
                    textSelectModel.setSelected(false);
                }
                model.setSelected(true);
                textSelectModel = model;
                if (textColorsAdapter != null) {
                    textColorsAdapter.notifyDataSetChanged();
                }
                objectSelectedPosition = position;
                showTextFragmentList();
            }

        };
        textColorsAdapter.setOnItemSelectListener(onItemSelectListener);
        binding.TextcolorList.setLayoutManager(new GridLayoutManager(act, 3));
        binding.TextcolorList.setHasFixedSize(true);
        binding.TextcolorList.setAdapter(textColorsAdapter);
        textSelectModel = textColorsList.get(0);
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


    public void setDigitalCardAdapter() {
        isLoading = false;
        Utility.dismissProgress();
        binding.alertText.setText(Html.fromHtml("Please fill all the details for better perfect design." + "<font color=\"red\">" + "<b>" + "Click here to fill details." + "</b>" + "</font>"));
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

    public void showBackgroundFragmentList() {
        bottomSheetFragment = new ColorPickerFragment();
        ColorPickerFragment.OnColorChoose onColorChoose;
        onColorChoose = color -> {
            VisitingCardHelper.applyBackgroundColor(CurrentSelectedCard, color, backgroundSelectModel);
            backgroundSelectModel.setColor(color);
            backgroundColorsList.set(objectSelectedPosition, backgroundSelectModel);
            backgroundColorsAdapter.notifyItemChanged(objectSelectedPosition);
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

    public void showTextFragmentList() {
        bottomSheetFragment = new ColorPickerFragment();
        ColorPickerFragment.OnColorChoose onColorChoose = color -> {
            VisitingCardHelper.applyTextColor(CurrentSelectedCard, color, textSelectModel);
            textSelectModel.setColor(color);
            textColorsList.set(objectSelectedPosition, textSelectModel);
            textColorsAdapter.notifyItemChanged(objectSelectedPosition);
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

    public void showIconsFragmentList() {
        bottomSheetFragment = new ColorPickerFragment();
        ColorPickerFragment.OnColorChoose onColorChoose = color -> {
            VisitingCardHelper.applyIconsColor(CurrentSelectedCard, color, iconsSelectModel);
            iconsSelectModel.setColor(color);
            iconsColorsList.set(objectSelectedPosition, iconsSelectModel);
            iconsColorsAdapter.notifyItemChanged(objectSelectedPosition);
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

    public void setIconsdAdapter() {

        if (iconsColorsList.size() != 0) {
            iconsColorsList.clear();
            if (iconsColorsAdapter != null) {
                iconsColorsAdapter.notifyDataSetChanged();
            }
        }

        iconsColorsList.addAll(VisitingCardHelper.getIconsColorList(CurrentSelectedCard, colors, act));

        iconsColorsAdapter = new IconsColorsAdapter(iconsColorsList, act);
        IconsColorsAdapter.onItemSelectListener onItemSelectListener = (model, position) -> {
            if (iconsSelectModel != null) {
                iconsSelectModel.setSelected(false);
            }
            model.setSelected(true);
            iconsSelectModel = model;
            if (iconsColorsAdapter != null) {
                iconsColorsAdapter.notifyDataSetChanged();
            }
            objectSelectedPosition = position;
            showIconsFragmentList();
        };

        iconsColorsAdapter.setOnItemSelectListener(onItemSelectListener);
        binding.iconscolorList.setLayoutManager(new GridLayoutManager(act, 3));
        binding.iconscolorList.setHasFixedSize(true);
        binding.iconscolorList.setAdapter(iconsColorsAdapter);
        iconsSelectModel = iconsColorsList.get(0);
    }

    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    String dirpath;
    File frontPage;
    File backPage;


    @Override
    public void onResume() {
        super.onResume();

        if (CurrentSelectedCard != null) {
            getBrandList();
        }
    }

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
            setBackgroundAdapter();
            setTextAdapter();
            setIconsdAdapter();
            //setAdapter();
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
            setBackgroundAdapter();
            setTextAdapter();
            setIconsdAdapter();
            //setAdapter();
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
            setBackgroundAdapter();
            setTextAdapter();
            setIconsdAdapter();
            //setAdapter();
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
            setBackgroundAdapter();
            setTextAdapter();
            setIconsdAdapter();
            //setAdapter();

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
            setBackgroundAdapter();
            setTextAdapter();
            setIconsdAdapter();
            //setAdapter();
        }
    }

//    public void layoutToImage() {
//        binding.pdfLayout.setDrawingCacheEnabled(true);
//        binding.pdfLayout.buildDrawingCache();
//        FileOutputStream fileOutputStream = null;
//        String name = "image" + System.currentTimeMillis() + ".jpg";
//        frontPage = new File(act.getCacheDir(), name);
//
//        try {
//            fileOutputStream = new FileOutputStream(frontPage);
//            Bitmap bitmap = binding.pdfLayout.getDrawingCache();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            imageToPDF(forShareUser);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void frontPageLayoutImage(boolean forShareUser) {
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
                backPageLayoutImage(forShareUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//            textColorsAdapter.setOnItemSelectListener(onItemSelectListener);
//            binding.TextcolorList.setLayoutManager(new GridLayoutManager(act, 2));
//            binding.TextcolorList.setHasFixedSize(true);
//            binding.TextcolorList.setAdapter(textColorsAdapter);
//            textSelectModel = textColorsList.get(0);
    }

    public void backPageLayoutImage(boolean forShareUser) {
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
                imageToPDF(forShareUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String outputFile = "";

    public void imageToPDF(boolean forShareUser) throws FileNotFoundException {
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

//                String FilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constant.ROOT + "/" + Constant.DOCUMENT + "/" + prefManager.getActiveBrand().getName() + ".pdf";
//                Intent intent = new Intent(Intent.CATEGORY_OPENABLE);
//                File file = new File(FilePath);
//                Uri apkURI = FileProvider.getUriForFile(act, BuildConfig.APPLICATION_ID + ".provider", file);
//                intent.setDataAndType(apkURI, "application/pdf");
//                startActivity(intent);
            }
            document.close();
            Toast.makeText(act, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();

            if (forShareUser) {
                //2 for share
                activity(2);// 2 for share
                actionFlagForDownloadOrShare = 2;
            } else {
                //1 for download only
                activity(1); // 1 for download
                actionFlagForDownloadOrShare = 1;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewPdf(String name, Activity act) {

        String FilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constant.ROOT + "/" + Constant.DOCUMENT + "/" + name + ".pdf";
        //Log.e("FilePath", "New Path: " + FilePath);
        File file;
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            file = new File(FilePath);
            Uri apkURI = FileProvider.getUriForFile(act, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, apkURI);
        } else {
            file = new File(Environment.getExternalStorageDirectory() + "/" + outputFile);
            Uri apkURI = FileProvider.getUriForFile(act, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, apkURI);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(Intent.createChooser(intent, "Share Pdf to..."));
    }

    // ask to upgrade package to 999 for use all frames
    DialogUpgradeLayoutSecondBinding layoutSecondBinding;

    public void askForUpgradeToEnterprisePackage() {
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
        if (Utility.isPackageExpired(act)) {
            layoutSecondBinding.element2.setText("Expired");
        } else {
            layoutSecondBinding.element2.setText("Free");
        }
        layoutSecondBinding.element3.setText("To download digital visiting card, please upgrade your package.");
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    String activityId = "";
    int actionFlagForDownloadOrShare = -1;

    private void activity(int flag) {
        if (isLoading)
            return;
        isLoading = true;

        if (flag == 0) {
            binding.loader.setVisibility(View.VISIBLE);
        } else {
            Utility.showLoadingTran(act);
        }
        StringRequest request = new StringRequest(Request.Method.POST, APIs.ADD_BUSS_ACTIVITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("response : ", response);
                isLoading = false;
                if (flag == 0) {
                    binding.loader.setVisibility(View.GONE);
                    binding.scrollView.setVisibility(View.VISIBLE);
                } else {
                    Utility.dismissLoadingTran();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (ResponseHandler.isSuccess(null, jsonObject)) {
                        activityId = ResponseHandler.getString(ResponseHandler.getJSONObject(jsonObject, "data"), "id");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (actionFlagForDownloadOrShare == 2) {
                    actionFlagForDownloadOrShare = -1;
                    viewPdf(prefManager.getActiveBrand().getName(), act);
                }
            }
        }, error -> {
            error.printStackTrace();
            isLoading = false;
            Utility.dismissLoadingTran();
            binding.loader.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.VISIBLE);
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Authorization", "Bearer" + prefManager.getUserToken());
                //Log.e("Token", params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("is_download", String.valueOf(flag));

                if (!activityId.isEmpty())
                    map.put("id ", activityId);

                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    public void loadDataRefreshing() {
        prefManager = new PreafManager(act);
        if (CurrentSelectedCard != null && CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            VisitingCardHelper.loadDataCardOne(act, CurrentSelectedCard.getOneBinding(), colors);
        } else if (CurrentSelectedCard != null && CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            VisitingCardHelper.loadDataCardTwo(act, CurrentSelectedCard.getTwoBinding(), colors);
        } else if (CurrentSelectedCard != null && CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            VisitingCardHelper.loadDataCardThree(act, CurrentSelectedCard.getThreeBinding(), colors);
        } else if (CurrentSelectedCard != null && CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            //Log.e("data", "4");
            VisitingCardHelper.loadDataCardFour(act, CurrentSelectedCard.getFourBinding(), colors);
        } else if (CurrentSelectedCard != null && CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            VisitingCardHelper.loadDataCardFive(act, CurrentSelectedCard.getFiveBinding(), colors);
        }
    }

    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_BRAND);

        binding.loader.setVisibility(View.VISIBLE);
        binding.scrollView.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_BRAND : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);

                    if (multiListItems != null && multiListItems.size() != 0) {
                        for (int i = 0; i < multiListItems.size(); i++) {
                            if (multiListItems.get(i).getName().equalsIgnoreCase(prefManager.getActiveBrand().getName())) {
                                prefManager.setActiveBrand(multiListItems.get(i));
                                break;
                            }
                        }
                    }
                    loadDataRefreshing();
                    binding.loader.setVisibility(View.GONE);
                    binding.scrollView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        binding.loader.setVisibility(View.GONE);
                        binding.scrollView.setVisibility(View.VISIBLE);
                        loadDataRefreshing();

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Log.e("DateNdClass", params.toString());

                //Log.e("DateNdClass", params.toString());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

}