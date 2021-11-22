package com.app.brandmania.Common;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.ColorsModel;
import com.app.brandmania.Model.VisitingCardModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.LayoutDigitalCardFifthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardFourthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardOneBinding;
import com.app.brandmania.databinding.LayoutDigitalCardThreeBinding;
import com.app.brandmania.databinding.LayoutDigitalCardTwoBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VisitingCardHelper {


    public static ArrayList<VisitingCardModel> getDigitalCardList() {
        ArrayList<VisitingCardModel> digitalCardList = new ArrayList<>();
        VisitingCardModel visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_ONE);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_TWO);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_THREE);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_FOUR);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_FIVE);
        digitalCardList.add(visitingCardModel);

        return digitalCardList;

    }

    public static void loadDataCardOne(Activity act, LayoutDigitalCardOneBinding oneBinding, Palette colors) {
        BrandListItem brand = new PreafManager(act).getActiveBrand();
        Picasso.get().load(brand.getLogo()).into(oneBinding.logo2);
        Picasso.get().load(brand.getLogo()).into(oneBinding.logo);

        if (!brand.getWebsite().isEmpty() && !brand.getWebsite().equalsIgnoreCase("https://")) {
            oneBinding.websiteTxt1.setText(brand.getWebsite());
        }

        if (!brand.getEmail().isEmpty()) {
            oneBinding.phoneTxt.setText(brand.getEmail());
        }

        if (!brand.getPhonenumber().isEmpty()) {
            oneBinding.brandName.setText(brand.getPhonenumber());
        }

        if (!brand.getWebsite().isEmpty() && !brand.getWebsite().equalsIgnoreCase("https://")) {
            oneBinding.websiteTxt.setText(brand.getWebsite());
        }

        if (!brand.getAddress().isEmpty()) {
            oneBinding.addressTxt.setText(brand.getAddress());
        }

        loadDefaultColorCardOne(act, oneBinding, colors);
    }

    public static void loadDataCardTwo(Activity act, LayoutDigitalCardTwoBinding twoBinding, Palette colors) {
        BrandListItem brand = new PreafManager(act).getActiveBrand();
        Picasso.get().load(brand.getLogo()).into(twoBinding.logoThumbnail);
        Picasso.get().load(brand.getLogo()).into(twoBinding.logo);
        if (!brand.getWebsite().isEmpty() && !brand.getWebsite().equalsIgnoreCase("https://")) {
            twoBinding.websiteTxt.setText(brand.getWebsite());
            twoBinding.webTxt.setText(brand.getWebsite());
        }
        twoBinding.brandName.setText(brand.getName());

        if (!brand.getAddress().isEmpty()) {
            twoBinding.address.setText(brand.getAddress());
        }

        if (!brand.getPhonenumber().isEmpty()) {
            twoBinding.phoneTxt.setText(brand.getPhonenumber());
        }

        if (!brand.getEmail().isEmpty()) {
            twoBinding.emailTxt.setText(brand.getEmail());
        }

        loadDefaultColorCardTwo(act, twoBinding, colors);
    }

    public static void loadDataCardThree(Activity act, LayoutDigitalCardThreeBinding threeBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(threeBinding.logo);
        BrandListItem brand = new PreafManager(act).getActiveBrand();
        threeBinding.brandNameText.setText(brand.getName());

        if (!brand.getAddress().isEmpty()) {
            threeBinding.addressTxt.setText(brand.getAddress());
        }

        if (!brand.getPhonenumber().isEmpty()) {
            threeBinding.phoneTxt.setText(brand.getPhonenumber());
        }

        if (!brand.getEmail().isEmpty()) {
            threeBinding.emailTxt.setText(brand.getEmail());
        }

        loadDefaultColorCardThree(act, threeBinding, colors);
    }

    public static void loadDataCardFour(Activity act, LayoutDigitalCardFourthBinding fourBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fourBinding.logo);
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fourBinding.logo2);
        loadDefaultColorCardFour(act, fourBinding, colors);
    }

    public static void loadDataCardFive(Activity act, LayoutDigitalCardFifthBinding fiveBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fiveBinding.logo);
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fiveBinding.logo2);
        loadDefaultColorCardFive(act, fiveBinding, colors);
    }

    public static void loadDefaultColorCardOne(Activity act, LayoutDigitalCardOneBinding binding, Palette colors) {
        binding.frontPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.verticalView.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.vertical2.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));

        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.addressIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.leftView.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.rightView.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
    }

    public static void loadDefaultColorCardTwo(Activity act, LayoutDigitalCardTwoBinding binding, Palette colors) {
        binding.frontPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.topBackground.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.thirdBackground.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));

        binding.secondBackground.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.fourthBackground.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        GradientDrawable drawable = (GradientDrawable) binding.logoThumbnail.getBackground();
        drawable.setStroke(2, colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
    }

    public static void loadDefaultColorCardThree(Activity act, LayoutDigitalCardThreeBinding binding, Palette colors) {
        binding.frontPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.backPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));

        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
    }

    public static void loadDefaultColorCardFour(Activity act, LayoutDigitalCardFourthBinding binding, Palette colors) {
        binding.frontPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.backPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.view.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.verticalView.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));

    }

    public static void loadDefaultColorCardFive(Activity act, LayoutDigitalCardFifthBinding binding, Palette colors) {
        binding.frontPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.backPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));

        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.addressIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.userIcon.setImageTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
    }

    public static ArrayList<ColorsModel> getColorList(VisitingCardModel model, Palette colors, Activity act) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            return getColorsListCardOne(colors, act, model.getOneBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            return getColorsListCardTwo(colors, act, model.getTwoBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            return getColorsListCardThree(colors, act, model.getThreeBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            return getColorsListCardFour(colors, act, model.getFourBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            return getColorsListCardFive(colors, act, model.getFiveBinding());
        }
        return new ArrayList<>();
    }

    public static ArrayList<ColorsModel> getColorsListCardOne(Palette colors, Activity act, LayoutDigitalCardOneBinding binding) {
        ArrayList<ColorsModel> colorsList = new ArrayList<>();
        ColorsModel model = new ColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        viewId.add(binding.vertical2.getId());
        viewId.add(binding.verticalView.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        viewId = new ArrayList<>();
        model.setObjectPosition(1);
        viewId.add(binding.leftView.getId());
        viewId.add(binding.rightView.getId());
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        viewId.add(binding.addressIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.white));
        viewId = new ArrayList<>();
        model.setObjectPosition(2);
        viewId.add(binding.logo2.getId());
        viewId.add(binding.userIcon.getId());

        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.black));
        viewId = new ArrayList<>();
        model.setObjectPosition(3);
        viewId.add(binding.brandName.getId());
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.websiteTxt.getId());
        viewId.add(binding.addressTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<ColorsModel> getColorsListCardTwo(Palette colors, Activity act, LayoutDigitalCardTwoBinding binding) {
        ArrayList<ColorsModel> colorsList = new ArrayList<>();
        ColorsModel model = new ColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        viewId.add(binding.topBackground.getId());
        viewId.add(binding.thirdBackground.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        viewId = new ArrayList<>();
        model.setObjectPosition(1);
        viewId.add(binding.secondBackground.getId());
        viewId.add(binding.fourthBackground.getId());
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        viewId.add(binding.logoThumbnail.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.white));
        viewId = new ArrayList<>();
        model.setObjectPosition(2);
        viewId.add(binding.brandName.getId());
        viewId.add(binding.fb.getId());
        viewId.add(binding.wp.getId());
        viewId.add(binding.insta.getId());
        viewId.add(binding.address.getId());

        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.black));
        viewId = new ArrayList<>();
        model.setObjectPosition(3);
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.websiteTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<ColorsModel> getColorsListCardThree(Palette colors, Activity act, LayoutDigitalCardThreeBinding binding) {
        ArrayList<ColorsModel> colorsList = new ArrayList<>();
        ColorsModel model = new ColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        viewId.add(binding.backPage.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        viewId = new ArrayList<>();
        model.setObjectPosition(1);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.white));
        viewId = new ArrayList<>();
        model.setObjectPosition(2);
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.addressTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.black));
        viewId = new ArrayList<>();
        model.setObjectPosition(3);
        viewId.add(binding.fb.getId());
        viewId.add(binding.insta.getId());
        viewId.add(binding.wp.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<ColorsModel> getColorsListCardFour(Palette colors, Activity act, LayoutDigitalCardFourthBinding binding) {
        ArrayList<ColorsModel> colorsList = new ArrayList<>();
        ColorsModel model = new ColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        viewId.add(binding.backPage.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        viewId = new ArrayList<>();
        model.setObjectPosition(1);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.white));
        viewId = new ArrayList<>();
        model.setObjectPosition(2);
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.addressTxt.getId());
        viewId.add(binding.websiteTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.black));
        viewId = new ArrayList<>();
        model.setObjectPosition(3);
//        viewId.add(binding.fb.getId());
//        viewId.add(binding.insta.getId());
//        viewId.add(binding.wp.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }


    public static ArrayList<ColorsModel> getColorsListCardFive(Palette colors, Activity act, LayoutDigitalCardFifthBinding binding) {
        ArrayList<ColorsModel> colorsList = new ArrayList<>();
        ColorsModel model = new ColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        viewId.add(binding.backPage.getId());
        viewId.add(binding.verticalView.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        viewId = new ArrayList<>();
        model.setObjectPosition(1);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.addressIcon.getId());
        viewId.add(binding.userIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.white));
        viewId = new ArrayList<>();
        model.setObjectPosition(2);
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.addressTxt.getId());
        viewId.add(binding.emailTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.black));
        viewId = new ArrayList<>();
        model.setObjectPosition(3);
//        viewId.add(binding.fb.getId());
//        viewId.add(binding.insta.getId());
//        viewId.add(binding.wp.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }


    public static void applyColor(VisitingCardModel model, int colorCode, ColorsModel colorsModel) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            applyColorCardOne(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            applyColorCardTwo(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            applyColorCardThree(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            applyColorCardFour(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            applyColorCardFive(model, colorCode, colorsModel);
        }
    }

    public static void applyColorCardOne(VisitingCardModel model, int color, ColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getOneBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getOneBinding().vertical2.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getOneBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getOneBinding().leftView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getOneBinding().rightView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getOneBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getOneBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getOneBinding().addressIcon.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getOneBinding().logo2.setImageTintList(ColorStateList.valueOf(color));
            model.getOneBinding().userIcon.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getOneBinding().brandName.setText((color));
            model.getOneBinding().phoneTxt.setText((color));
            model.getOneBinding().websiteTxt.setText((color));
            model.getOneBinding().addressTxt.setText((color));
        }
    }

    public static void applyColorCardTwo(VisitingCardModel model, int color, ColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getTwoBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().topBackground.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().thirdBackground.setBackgroundTintList(ColorStateList.valueOf(color));

        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getTwoBinding().secondBackground.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().fourthBackground.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().logoThumbnail.setBackgroundTintList(ColorStateList.valueOf(color));
            GradientDrawable drawable = (GradientDrawable) model.getTwoBinding().logoThumbnail.getBackground();
            drawable.setStroke(2, color);
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getTwoBinding().brandName.setTextColor((color));
            model.getTwoBinding().fb.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().wp.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().insta.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().address.setTextColor(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getTwoBinding().websiteTxt.setTextColor((color));
            model.getTwoBinding().phoneTxt.setTextColor((color));
            model.getTwoBinding().emailTxt.setTextColor((color));
        }
    }

    public static void applyColorCardThree(VisitingCardModel model, int color, ColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getThreeBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().backPage.setBackgroundTintList(ColorStateList.valueOf(color));
        }

        if (colorsModel.getObjectPosition() == 1) {
            model.getThreeBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getThreeBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getThreeBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
            model.getThreeBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getThreeBinding().fb.setImageTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().wp.setImageTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().insta.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    public static void applyColorCardFour(VisitingCardModel model, int color, ColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFourBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFourBinding().leftView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().rightView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().view.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getFourBinding().logo2.setImageTintList(ColorStateList.valueOf(color));
            model.getFourBinding().logo.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getFourBinding().brandName.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().websiteTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
        }
    }


    public static void applyColorCardFive(VisitingCardModel model, int color, ColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFiveBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFiveBinding().BottomView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().addressIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().userIcon.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getFiveBinding().logo2.setImageTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().logo.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getFiveBinding().brandNames.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
        }
    }


}
