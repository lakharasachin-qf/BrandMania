package com.app.brandmania.Common;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.app.brandmania.Model.BackgroundColorsModel;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.ColorsModel;
import com.app.brandmania.Model.IconsColorsModel;
import com.app.brandmania.Model.TextColorsModel;
import com.app.brandmania.Model.VisitingCardModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.LayoutDigitalCardFifthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardFourthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardOneBinding;
import com.app.brandmania.databinding.LayoutDigitalCardThreeBinding;
import com.app.brandmania.databinding.LayoutDigitalCardTwoBinding;
import com.app.brandmania.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VisitingCardHelper {

    public static ArrayList<VisitingCardModel> getDigitalCardList() {
        ArrayList<VisitingCardModel> digitalCardList = new ArrayList<>();
        VisitingCardModel visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_FOUR);
        visitingCardModel.setFree(true);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_FIVE);
        visitingCardModel.setFree(true);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_ONE);
        visitingCardModel.setFree(false);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_TWO);
        visitingCardModel.setFree(false);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new VisitingCardModel();
        visitingCardModel.setFree(false);
        visitingCardModel.setLayoutType(VisitingCardModel.LAYOUT_THREE);
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
//        else {
//            if (!brand.getWebsite().isEmpty()) {
//                oneBinding.websiteTxt1.setText("www." + brand.getWebsite().replace("https://", "").replace("https://", "").replace("www.", ""));
//            } else {
//                oneBinding.websiteTxt1.setVisibility(View.VISIBLE);
//            }
//        }

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
        if (brand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(act)) {
            oneBinding.paidUserCard.setVisibility(View.VISIBLE);
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
        if (brand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(act)) {
            twoBinding.paidUserCard.setVisibility(View.VISIBLE);
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
        if (brand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(act)) {
            threeBinding.paidUserCard.setVisibility(View.VISIBLE);
        }
        loadDefaultColorCardThree(act, threeBinding, colors);
    }

    public static void loadDataCardFour(Activity act, LayoutDigitalCardFourthBinding fourBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fourBinding.logo);
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fourBinding.logo2);

        BrandListItem brand = new PreafManager(act).getActiveBrand();
        fourBinding.brandName.setText(brand.getName());
        fourBinding.frontBrandName.setText(brand.getName());

        if (!brand.getAddress().isEmpty()) {
            fourBinding.addressTxt.setText(brand.getAddress());
        }

        if (!brand.getPhonenumber().isEmpty()) {
            fourBinding.phoneTxt.setText(brand.getPhonenumber());
        }

        if (!brand.getEmail().isEmpty()) {
            fourBinding.emailTxt.setText(brand.getEmail());
        }
        if (!brand.getWebsite().isEmpty()) {
            fourBinding.websiteTxt.setText(brand.getWebsite());
        }
        if (brand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(act)) {
            fourBinding.freeCard.setVisibility(View.GONE);
            fourBinding.bmLogo.setVisibility(View.VISIBLE);
        }
        loadDefaultColorCardFour(act, fourBinding, colors);
    }

    public static void loadDataCardFive(Activity act, LayoutDigitalCardFifthBinding fiveBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fiveBinding.logo);
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(fiveBinding.logo2);
        BrandListItem brand = new PreafManager(act).getActiveBrand();
        fiveBinding.brandName.setText(brand.getName());
        fiveBinding.frontBrandName.setText(brand.getName());

        if (!brand.getAddress().isEmpty()) {
            fiveBinding.addressTxt.setText(brand.getAddress());
        }

        if (!brand.getPhonenumber().isEmpty()) {
            fiveBinding.phoneTxt.setText(brand.getPhonenumber());
        }

        if (!brand.getEmail().isEmpty()) {
            fiveBinding.emailTxt.setText(brand.getEmail());
        }

        if (brand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(act)) {
            fiveBinding.freeCard.setVisibility(View.GONE);
            fiveBinding.bmLogo.setVisibility(View.VISIBLE);
        }
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
        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.address.setTextColor(ContextCompat.getColor(act, R.color.black));
        binding.phoneTxt.setTextColor(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailTxt.setTextColor(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteTxt.setTextColor(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        GradientDrawable drawable = (GradientDrawable) binding.logoThumbnail.getBackground();
        drawable.setStroke(2, colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
    }

    public static void loadDefaultColorCardThree(Activity act, LayoutDigitalCardThreeBinding binding, Palette colors) {
        binding.backPage.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
    }

    public static void loadDefaultColorCardFour(Activity act, LayoutDigitalCardFourthBinding binding, Palette colors) {
        binding.view.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.verticalView.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.leftView.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.rightView.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        //binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        //binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        //binding.websiteIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.frontBrandName.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.black)));
        binding.brandName.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.black)));
        binding.phoneTxt.setTextColor(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailTxt.setTextColor(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.addressTxt.setTextColor(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.frontBottomView.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.websiteTxt.setTextColor(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
    }

    public static void loadDefaultColorCardFive(Activity act, LayoutDigitalCardFifthBinding binding, Palette colors) {

        binding.bottomView.setBackgroundTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.brandName.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.black)));
        binding.addressTxt.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.black)));
        binding.emailTxt.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.black)));
        binding.phoneTxt.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.black)));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.userIcon.setImageTintList(ColorStateList.valueOf(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.addressIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.userIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.white)));
        binding.callIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
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

    public static ArrayList<BackgroundColorsModel> getBackgroundColorList(VisitingCardModel model, Palette colors, Activity act) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            return getBackgroundColorsListCardOne(colors, act, model.getOneBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            return getBackgroundColorsListCardTwo(colors, act, model.getTwoBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            return getBackgroundColorsListCardThree(colors, act, model.getThreeBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            return getBackgroundColorsListCardFour(colors, act, model.getFourBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            return getBackgroundColorsListCardFive(colors, act, model.getFiveBinding());
        }
        return new ArrayList<>();
    }

    public static ArrayList<TextColorsModel> getTextColorList(VisitingCardModel model, Palette colors, Activity act) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            return getTextColorsListCardOne(colors, act, model.getOneBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            return getTextColorsListCardTwo(colors, act, model.getTwoBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            return getTextColorsListCardThree(colors, act, model.getThreeBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            return getTextColorsListCardFour(colors, act, model.getFourBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            return getTextColorsListCardFive(colors, act, model.getFiveBinding());
        }
        return new ArrayList<>();
    }

    public static ArrayList<IconsColorsModel> getIconsColorList(VisitingCardModel model, Palette colors, Activity act) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            return getIconsColorsListCardOne(colors, act, model.getOneBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            return getIconsColorsListCardTwo(colors, act, model.getTwoBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            return getIconsColorsListCardThree(colors, act, model.getThreeBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            return getIconsColorsListCardFour(colors, act, model.getFourBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            return getIconsColorsListCardFive(colors, act, model.getFiveBinding());
        }
        return new ArrayList<>();
    }


    public static ArrayList<BackgroundColorsModel> getBackgroundColorsListCardOne(Palette colors, Activity act, LayoutDigitalCardOneBinding binding) {
        ArrayList<BackgroundColorsModel> colorsList = new ArrayList<>();
        BackgroundColorsModel model = new BackgroundColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        viewId.add(binding.vertical2.getId());
        viewId.add(binding.verticalView.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new BackgroundColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        viewId = new ArrayList<>();
        model.setObjectPosition(1);
        viewId.add(binding.leftView.getId());
        viewId.add(binding.rightView.getId());
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        viewId.add(binding.addressIcon.getId());

        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<TextColorsModel> getTextColorsListCardOne(Palette colors, Activity act, LayoutDigitalCardOneBinding binding) {
        ArrayList<TextColorsModel> colorsList = new ArrayList<>();
        TextColorsModel model = new TextColorsModel(ContextCompat.getColor(act, R.color.white));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.websiteTxt1.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new TextColorsModel(ContextCompat.getColor(act, R.color.black));
        model.setObjectPosition(1);
        viewId.add(binding.brandName.getId());
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.websiteTxt.getId());
        viewId.add(binding.addressTxt.getId());

        model.setViewId(viewId);
        colorsList.add(model);
        return colorsList;
    }

    public static ArrayList<IconsColorsModel> getIconsColorsListCardOne(Palette colors, Activity act, LayoutDigitalCardOneBinding binding) {
        ArrayList<IconsColorsModel> colorsList = new ArrayList<>();
        IconsColorsModel model = new IconsColorsModel(ContextCompat.getColor(act, R.color.white));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.userIcon.getId());
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        viewId.add(binding.addressIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);


        return colorsList;
    }


    public static ArrayList<BackgroundColorsModel> getBackgroundColorsListCardTwo(Palette colors, Activity act, LayoutDigitalCardTwoBinding binding) {
        ArrayList<BackgroundColorsModel> colorsList = new ArrayList<>();
        BackgroundColorsModel model = new BackgroundColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        viewId.add(binding.backPage.getId());
        viewId.add(binding.topBackground.getId());
        viewId.add(binding.thirdBackground.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new BackgroundColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        model.setObjectPosition(1);
        viewId.add(binding.secondBackground.getId());
        viewId.add(binding.fourthBackground.getId());
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

//        model = new BackgroundColorsModel(ContextCompat.getColor(act, R.color.black));
//        model.setObjectPosition(2);
//        viewId.add(binding.fb.getId());
//        viewId.add(binding.wp.getId());
//        viewId.add(binding.insta.getId());
//        model.setViewId(viewId);
//        colorsList.add(model);
        return colorsList;
    }

    public static ArrayList<TextColorsModel> getTextColorsListCardTwo(Palette colors, Activity act, LayoutDigitalCardTwoBinding binding) {
        ArrayList<TextColorsModel> colorsList = new ArrayList<>();
        TextColorsModel model = new TextColorsModel(ContextCompat.getColor(act, R.color.white));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.webTxt.getId());
        viewId.add(binding.brandName.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new TextColorsModel(ContextCompat.getColor(act, R.color.black));
        model.setObjectPosition(1);
        viewId.add(binding.address.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new TextColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        model.setObjectPosition(2);
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.websiteTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);
        return colorsList;
    }

    public static ArrayList<IconsColorsModel> getIconsColorsListCardTwo(Palette colors, Activity act, LayoutDigitalCardTwoBinding binding) {
        ArrayList<IconsColorsModel> colorsList = new ArrayList<>();
        IconsColorsModel model = new IconsColorsModel(ContextCompat.getColor(act, R.color.white));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.webIcon.getId());
        viewId.add(binding.locationIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new IconsColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        model.setObjectPosition(1);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        viewId.add(binding.emailIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new IconsColorsModel(ContextCompat.getColor(act, R.color.white));
        model.setObjectPosition(2);
        viewId.add(binding.fb.getId());
        viewId.add(binding.insta.getId());
        viewId.add(binding.wp.getId());
        model.setViewId(viewId);
        colorsList.add(model);
        return colorsList;
    }


    public static ArrayList<BackgroundColorsModel> getBackgroundColorsListCardThree(Palette colors, Activity act, LayoutDigitalCardThreeBinding binding) {
        ArrayList<BackgroundColorsModel> colorsList = new ArrayList<>();
        BackgroundColorsModel model = new BackgroundColorsModel(ContextCompat.getColor(act, R.color.colorChocolaty));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontPage.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new BackgroundColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        model.setObjectPosition(1);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        viewId.add(binding.emailIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

//        model = new BackgroundColorsModel(ContextCompat.getColor(act, R.color.black));
//        model.setObjectPosition(2);
//        viewId.add(binding.fb.getId());
//        viewId.add(binding.wp.getId());
//        viewId.add(binding.insta.getId());
//        model.setViewId(viewId);
//        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<TextColorsModel> getTextColorsListCardThree(Palette colors, Activity act, LayoutDigitalCardThreeBinding binding) {
        ArrayList<TextColorsModel> colorsList = new ArrayList<>();
        TextColorsModel model = new TextColorsModel(ContextCompat.getColor(act, R.color.black));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.brandNameText.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new TextColorsModel(ContextCompat.getColor(act, R.color.white));
        model.setObjectPosition(1);
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.addressTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<IconsColorsModel> getIconsColorsListCardThree(Palette colors, Activity act, LayoutDigitalCardThreeBinding binding) {
        ArrayList<IconsColorsModel> colorsList = new ArrayList<>();
        IconsColorsModel model = new IconsColorsModel(ContextCompat.getColor(act, R.color.white));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new IconsColorsModel(ContextCompat.getColor(act, R.color.black));
        model.setObjectPosition(1);
        viewId.add(binding.fb.getId());
        viewId.add(binding.wp.getId());
        viewId.add(binding.insta.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }


    public static ArrayList<BackgroundColorsModel> getBackgroundColorsListCardFour(Palette colors, Activity act, LayoutDigitalCardFourthBinding binding) {
        ArrayList<BackgroundColorsModel> colorsList = new ArrayList<>();
        BackgroundColorsModel model = new BackgroundColorsModel(ContextCompat.getColor(act, R.color.deepColour));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontBottomView.getId());
        viewId.add(binding.view.getId());
        viewId.add(binding.verticalView.getId());
        viewId.add(binding.leftView.getId());
        viewId.add(binding.rightView.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new BackgroundColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        model.setObjectPosition(1);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);
        return colorsList;
    }

    public static ArrayList<TextColorsModel> getTextColorsListCardFour(Palette colors, Activity act, LayoutDigitalCardFourthBinding binding) {
        ArrayList<TextColorsModel> colorsList = new ArrayList<>();
        TextColorsModel model = new TextColorsModel(ContextCompat.getColor(act, R.color.black));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontBrandName.getId());
        viewId.add(binding.brandName.getId());
        model.setViewId(viewId);
        colorsList.add(model);


        model = new TextColorsModel(ContextCompat.getColor(act, R.color.colorPrimary));
        model.setObjectPosition(1);

        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.addressTxt.getId());
        viewId.add(binding.websiteTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<IconsColorsModel> getIconsColorsListCardFour(Palette colors, Activity act, LayoutDigitalCardFourthBinding binding) {
        ArrayList<IconsColorsModel> colorsList = new ArrayList<>();
        IconsColorsModel model = new IconsColorsModel(ContextCompat.getColor(act, R.color.white));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }


    public static ArrayList<BackgroundColorsModel> getBackgroundColorsListCardFive(Palette colors, Activity act, LayoutDigitalCardFifthBinding binding) {
        ArrayList<BackgroundColorsModel> colorsList = new ArrayList<>();
        BackgroundColorsModel model = new BackgroundColorsModel(ContextCompat.getColor(act, R.color.colorPrimary));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.topRightView.getId());
        viewId.add(binding.bottomLeftView.getId());
        viewId.add(binding.bottomView.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new BackgroundColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        model.setObjectPosition(1);
        viewId.add(binding.verticalView.getId());
        viewId.add(binding.call.getId());
        viewId.add(binding.email.getId());
        viewId.add(binding.address.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new BackgroundColorsModel(ContextCompat.getColor(act, R.color.white));
        model.setObjectPosition(2);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.addressIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<TextColorsModel> getTextColorsListCardFive(Palette colors, Activity act, LayoutDigitalCardFifthBinding binding) {
        ArrayList<TextColorsModel> colorsList = new ArrayList<>();
        TextColorsModel model = new TextColorsModel(ContextCompat.getColor(act, R.color.white));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.frontBrandName.getId());
        model.setViewId(viewId);
        colorsList.add(model);


        model = new TextColorsModel(ContextCompat.getColor(act, R.color.black));
        model.setObjectPosition(1);
        viewId.add(binding.brandName.getId());
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.addressTxt.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
    }

    public static ArrayList<IconsColorsModel> getIconsColorsListCardFive(Palette colors, Activity act, LayoutDigitalCardFifthBinding binding) {
        ArrayList<IconsColorsModel> colorsList = new ArrayList<>();
        IconsColorsModel model = new IconsColorsModel(colors.getDarkVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        ArrayList<Integer> viewId = new ArrayList<>();
        model.setObjectPosition(0);
        viewId.add(binding.userIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new IconsColorsModel(ContextCompat.getColor(act, R.color.white));
        model.setObjectPosition(1);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.addressIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        return colorsList;
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
        viewId.add(binding.leftView.getId());
        viewId.add(binding.rightView.getId());
        viewId.add(binding.verticalView.getId());
        viewId.add(binding.view.getId());


        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.white));
        viewId = new ArrayList<>();
        model.setObjectPosition(2);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.websiteIcon.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.black));
        viewId = new ArrayList<>();
        model.setObjectPosition(3);
        viewId.add(binding.brandName.getId());
        viewId.add(binding.frontBrandName.getId());
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.addressTxt.getId());
        viewId.add(binding.websiteTxt.getId());

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
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary)));
        viewId = new ArrayList<>();
        model.setObjectPosition(1);
        viewId.add(binding.bottomView.getId());
        viewId.add(binding.verticalView.getId());
        viewId.add(binding.call.getId());
        viewId.add(binding.email.getId());
        viewId.add(binding.address.getId());
        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.white));
        viewId = new ArrayList<>();
        model.setObjectPosition(2);
        viewId.add(binding.callIcon.getId());
        viewId.add(binding.emailIcon.getId());
        viewId.add(binding.addressIcon.getId());
        viewId.add(binding.userIcon.getId());

        model.setViewId(viewId);
        colorsList.add(model);

        model = new ColorsModel(ContextCompat.getColor(act, R.color.black));
        viewId = new ArrayList<>();
        model.setObjectPosition(3);
        viewId.add(binding.phoneTxt.getId());
        viewId.add(binding.brandName.getId());
        viewId.add(binding.frontBrandName.getId());
        viewId.add(binding.emailTxt.getId());
        viewId.add(binding.addressTxt.getId());
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

    public static void applyBackgroundColor(VisitingCardModel model, int colorCode, BackgroundColorsModel colorsModel) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            applyBackgroundColorCardOne(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            applyBackgroundColorCardTwo(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            applyBackgroundColorCardThree(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            applyBackgroundColorCardFour(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            applyBackgroundColorCardFive(model, colorCode, colorsModel);
        }
    }

    public static void applyTextColor(VisitingCardModel model, int colorCode, TextColorsModel colorsModel) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            applyTextColorCardOne(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            applyTextColorCardTwo(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            applyTextColorCardThree(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            applyTextColorCardFour(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            applyTextColorCardFive(model, colorCode, colorsModel);
        }
    }

    public static void applyIconsColor(VisitingCardModel model, int colorCode, IconsColorsModel colorsModel) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            applyIconsColorCardOne(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            applyIconsColorCardTwo(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            applyIconsColorCardThree(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FOUR) {
            applyIconsColorCardFour(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_FIVE) {
            applyIconsColorCardFive(model, colorCode, colorsModel);
        }
    }


    public static void applyBackgroundColorCardOne(VisitingCardModel model, int color, BackgroundColorsModel colorsModel) {
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

    }

    public static void applyTextColorCardOne(VisitingCardModel model, int color, TextColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getOneBinding().websiteTxt1.setTextColor(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getOneBinding().brandName.setTextColor(ColorStateList.valueOf(color));
            model.getOneBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getOneBinding().websiteTxt.setTextColor(ColorStateList.valueOf(color));
            model.getOneBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
        }
    }

    public static void applyIconsColorCardOne(VisitingCardModel model, int color, IconsColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getOneBinding().userIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getOneBinding().callIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getOneBinding().websiteIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getOneBinding().addressIcon.setImageTintList(ColorStateList.valueOf(color));
        }
    }


    public static void applyBackgroundColorCardTwo(VisitingCardModel model, int color, BackgroundColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getTwoBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().backPage.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().topBackground.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().thirdBackground.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getTwoBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().secondBackground.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().fourthBackground.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getTwoBinding().fb.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().wp.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().insta.setBackgroundTintList(ColorStateList.valueOf(color));
        }

    }

    public static void applyTextColorCardTwo(VisitingCardModel model, int color, TextColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getTwoBinding().webTxt.setTextColor(ColorStateList.valueOf(color));
            model.getTwoBinding().brandName.setTextColor(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getTwoBinding().address.setTextColor(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getTwoBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getTwoBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
            model.getTwoBinding().websiteTxt.setTextColor(ColorStateList.valueOf(color));
        }
    }

    public static void applyIconsColorCardTwo(VisitingCardModel model, int color, IconsColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getTwoBinding().webIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().locationIcon.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getTwoBinding().callIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().emailIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().websiteIcon.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getTwoBinding().fb.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().insta.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().wp.setImageTintList(ColorStateList.valueOf(color));
        }

    }


    public static void applyBackgroundColorCardThree(VisitingCardModel model, int color, BackgroundColorsModel colorsModel) {
        Activity act = null;
        if (colorsModel.getObjectPosition() == 0) {
            model.getThreeBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getThreeBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getThreeBinding().fb.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().insta.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().wp.setBackgroundTintList(ColorStateList.valueOf(color));
        }

    }

    public static void applyTextColorCardThree(VisitingCardModel model, int color, TextColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getThreeBinding().brandNameText.setTextColor(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getThreeBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
            model.getThreeBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getThreeBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
        }

    }

    public static void applyIconsColorCardThree(VisitingCardModel model, int color, IconsColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getThreeBinding().callIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().emailIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().websiteIcon.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getThreeBinding().fb.setImageTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().wp.setImageTintList(ColorStateList.valueOf(color));
            model.getThreeBinding().insta.setImageTintList(ColorStateList.valueOf(color));
        }

    }


    public static void applyBackgroundColorCardFour(VisitingCardModel model, int color, BackgroundColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {

            model.getFourBinding().frontBottomView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().view.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().leftView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().rightView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFourBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));

        }
    }

    public static void applyTextColorCardFour(VisitingCardModel model, int color, TextColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFourBinding().frontBrandName.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().brandName.setTextColor(ColorStateList.valueOf(color));

        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFourBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().websiteTxt.setTextColor(ColorStateList.valueOf(color));

        }

    }

    public static void applyIconsColorCardFour(VisitingCardModel model, int color, IconsColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFourBinding().callIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getFourBinding().emailIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getFourBinding().websiteIcon.setImageTintList(ColorStateList.valueOf(color));
        }
    }


    public static void applyBackgroundColorCardFive(VisitingCardModel model, int color, BackgroundColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFiveBinding().bottomView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().topRightView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().bottomLeftView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFiveBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().call.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().email.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().address.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getFiveBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().addressIcon.setBackgroundTintList(ColorStateList.valueOf(color));
        }

    }

    public static void applyTextColorCardFive(VisitingCardModel model, int color, TextColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFiveBinding().frontBrandName.setTextColor(ColorStateList.valueOf(color));

        }
        if (colorsModel.getObjectPosition() == 1) {

            model.getFiveBinding().brandName.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));

        }

    }

    public static void applyIconsColorCardFive(VisitingCardModel model, int color, IconsColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFiveBinding().userIcon.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFiveBinding().addressIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().callIcon.setImageTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().emailIcon.setImageTintList(ColorStateList.valueOf(color));
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
            model.getOneBinding().brandName.setTextColor(ColorStateList.valueOf(color));
            model.getOneBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getOneBinding().websiteTxt.setTextColor(ColorStateList.valueOf(color));
            model.getOneBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
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
            model.getTwoBinding().brandName.setTextColor(ColorStateList.valueOf(color));
            model.getTwoBinding().fb.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().wp.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().insta.setImageTintList(ColorStateList.valueOf(color));
            model.getTwoBinding().address.setTextColor(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getTwoBinding().websiteTxt.setTextColor(ColorStateList.valueOf(color));
            model.getTwoBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getTwoBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
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
            model.getFourBinding().backPage.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFourBinding().leftView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().rightView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().view.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getFourBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFourBinding().websiteIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            //model.getFourBinding().logo2.setImageTintList(ColorStateList.valueOf(color));
            //model.getFourBinding().logo.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getFourBinding().brandName.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().frontBrandName.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().websiteTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFourBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
        }
    }

    public static void applyColorCardFive(VisitingCardModel model, int color, ColorsModel colorsModel) {
        if (colorsModel.getObjectPosition() == 0) {
            model.getFiveBinding().frontPage.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().backPage.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 1) {
            model.getFiveBinding().bottomView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().verticalView.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().call.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().email.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().address.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 2) {
            model.getFiveBinding().callIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().addressIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().emailIcon.setBackgroundTintList(ColorStateList.valueOf(color));
            model.getFiveBinding().userIcon.setImageTintList(ColorStateList.valueOf(color));
            //model.getFiveBinding().logo2.setImageTintList(ColorStateList.valueOf(color));
            //model.getFiveBinding().logo.setImageTintList(ColorStateList.valueOf(color));
        }
        if (colorsModel.getObjectPosition() == 3) {
            model.getFiveBinding().brandName.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().frontBrandName.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().phoneTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().emailTxt.setTextColor(ColorStateList.valueOf(color));
            model.getFiveBinding().addressTxt.setTextColor(ColorStateList.valueOf(color));
        }
    }

}