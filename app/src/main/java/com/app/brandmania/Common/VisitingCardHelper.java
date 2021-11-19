package com.app.brandmania.Common;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.app.brandmania.Activity.ColorsModel;
import com.app.brandmania.Model.VisitingCardModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.LayoutDigitalCardOneBinding;
import com.app.brandmania.databinding.LayoutDigitalCardThreeBinding;
import com.app.brandmania.databinding.LayoutDigitalCardTwoBinding;
import com.squareup.picasso.Callback;
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
        return digitalCardList;

    }

    public static void loadDataCardOne(Activity act, LayoutDigitalCardOneBinding oneBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(oneBinding.logo2);
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(oneBinding.logo);


        loadDefaultColorCardOne(act, oneBinding, colors);
    }

    public static void loadDataCardTwo(Activity act, LayoutDigitalCardTwoBinding twoBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(twoBinding.logoThumbnail);
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(twoBinding.logo);
        loadDefaultColorCardTwo(act, twoBinding, colors);
    }

    public static void loadDataCardThree(Activity act, LayoutDigitalCardThreeBinding threeBinding, Palette colors) {
        Picasso.get().load(new PreafManager(act).getActiveBrand().getLogo()).into(threeBinding.logo);
        loadDefaultColorCardThree(act, threeBinding, colors);
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
        binding.addressIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
        binding.emailIcon.setBackgroundTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
//        binding.fb.setImageTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
//        binding.wp.setImageTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
//        binding.insta.setImageTintList(ColorStateList.valueOf(colors.getVibrantColor(ContextCompat.getColor(act, R.color.colorPrimary))));
    }

    public static ArrayList<ColorsModel> getColorList(VisitingCardModel model, Palette colors, Activity act) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            return getColorsListCardOne(colors, act, model.getOneBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            return getColorsListCardTwo(colors, act, model.getTwoBinding());
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            return getColorsListCardThree(colors, act, model.getThreeBinding());
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
        viewId.add(binding.addressIcon.getId());
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


    public static void applyColor(VisitingCardModel model, int colorCode, ColorsModel colorsModel) {
        if (model.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            applyColorCardOne(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            applyColorCardTwo(model, colorCode, colorsModel);
        } else if (model.getLayoutType() == VisitingCardModel.LAYOUT_THREE) {
            applyColorCardThree(model, colorCode, colorsModel);
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
            model.getThreeBinding().addressIcon.setBackgroundTintList(ColorStateList.valueOf(color));
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

}
