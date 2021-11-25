package com.app.brandmania.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.LayoutModelClass;
import com.app.brandmania.R;
import com.app.brandmania.databinding.LayoutFooterElevenBinding;
import com.app.brandmania.databinding.LayoutFooterFifteenBinding;
import com.app.brandmania.databinding.LayoutFooterFourteenBinding;
import com.app.brandmania.databinding.LayoutFooterSixteenBinding;
import com.app.brandmania.databinding.LayoutFooterThirteenBinding;
import com.app.brandmania.databinding.LayoutFooterTweloneBinding;
import com.app.brandmania.databinding.LayoutForLoadEightBinding;
import com.app.brandmania.databinding.LayoutForLoadFiveBinding;
import com.app.brandmania.databinding.LayoutForLoadFourBinding;
import com.app.brandmania.databinding.LayoutForLoadNineBinding;
import com.app.brandmania.databinding.LayoutForLoadOneBinding;
import com.app.brandmania.databinding.LayoutForLoadSevenBinding;
import com.app.brandmania.databinding.LayoutForLoadSixBinding;
import com.app.brandmania.databinding.LayoutForLoadTenBinding;
import com.app.brandmania.databinding.LayoutForLoadThreeBinding;
import com.app.brandmania.databinding.LayoutForLoadTwoBinding;
import com.app.brandmania.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

public class FooterHelper {

    public static void baseForItalic(boolean italic, int footerLayout, LayoutModelClass layoutModelClass) {
        if (italic) {
            if (footerLayout == 1) {
                FooterHelper.makeItalicForOne(layoutModelClass.getOneBinding(), true);
            } else if (footerLayout == 2) {
                FooterHelper.makeItalicForTwo(layoutModelClass.getTwoBinding(), true);
            } else if (footerLayout == 3) {
                FooterHelper.makeItalicForThree(layoutModelClass.getThreeBinding(), true);
            } else if (footerLayout == 4) {
                FooterHelper.makeItalicForFour(layoutModelClass.getFourBinding(), true);
            } else if (footerLayout == 5) {
                FooterHelper.makeItalicForFive(layoutModelClass.getFiveBinding(), true);
            } else if (footerLayout == 6) {
                FooterHelper.makeItalicForSix(layoutModelClass.getSixBinding(), true);
            } else if (footerLayout == 7) {
                FooterHelper.makeItalicForSeven(layoutModelClass.getSevenBinding(), true);
            } else if (footerLayout == 8) {
                FooterHelper.makeItalicForEight(layoutModelClass.getEightBinding(), true);
            } else if (footerLayout == 9) {
                FooterHelper.makeItalicForNine(layoutModelClass.getNineBinding(), true);
            } else if (footerLayout == 10) {
                FooterHelper.makeItalicForTen(layoutModelClass.getTenBinding(), true);
            } else if (footerLayout == 11) {
                FooterHelper.makeItalicForEleven(layoutModelClass.getElevenBinding(), true);
            } else if (footerLayout == 12) {
                FooterHelper.makeItalicForTwelve(layoutModelClass.getTwelveBinding(), true);
            } else if (footerLayout == 13) {
                FooterHelper.makeItalicForThirteen(layoutModelClass.getThirteenBinding(), true);
            } else if (footerLayout == 14) {
                FooterHelper.makeItalicForFourteen(layoutModelClass.getFourteenBinding(), true);
            } else if (footerLayout == 15) {
                FooterHelper.makeItalicForFifteen(layoutModelClass.getFifteenBinding(), true);
            } else if (footerLayout == 16) {
                FooterHelper.makeItalicFor16(layoutModelClass.getSixteenBinding(), true);
            }
        } else {
            if (footerLayout == 1) {
                FooterHelper.makeItalicForOne(layoutModelClass.getOneBinding(), false);
            } else if (footerLayout == 2) {
                FooterHelper.makeItalicForTwo(layoutModelClass.getTwoBinding(), false);
            } else if (footerLayout == 3) {
                FooterHelper.makeItalicForThree(layoutModelClass.getThreeBinding(), false);
            } else if (footerLayout == 4) {
                FooterHelper.makeItalicForFour(layoutModelClass.getFourBinding(), false);
            } else if (footerLayout == 5) {
                FooterHelper.makeItalicForFive(layoutModelClass.getFiveBinding(), false);
            } else if (footerLayout == 6) {
                FooterHelper.makeItalicForSix(layoutModelClass.getSixBinding(), false);
            } else if (footerLayout == 7) {
                FooterHelper.makeItalicForSeven(layoutModelClass.getSevenBinding(), false);
            } else if (footerLayout == 8) {
                FooterHelper.makeItalicForEight(layoutModelClass.getEightBinding(), false);
            } else if (footerLayout == 9) {
                FooterHelper.makeItalicForNine(layoutModelClass.getNineBinding(), false);
            } else if (footerLayout == 10) {
                FooterHelper.makeItalicForTen(layoutModelClass.getTenBinding(), false);
            } else if (footerLayout == 11) {
                FooterHelper.makeItalicForEleven(layoutModelClass.getElevenBinding(), false);
            } else if (footerLayout == 12) {
                FooterHelper.makeItalicForTwelve(layoutModelClass.getTwelveBinding(), false);
            } else if (footerLayout == 13) {
                FooterHelper.makeItalicForThirteen(layoutModelClass.getThirteenBinding(), false);
            } else if (footerLayout == 14) {
                FooterHelper.makeItalicForFourteen(layoutModelClass.getFourteenBinding(), false);
            } else if (footerLayout == 15) {
                FooterHelper.makeItalicForFifteen(layoutModelClass.getFifteenBinding(), false);
            } else if (footerLayout == 16) {
                FooterHelper.makeItalicFor16(layoutModelClass.getSixteenBinding(), false);
            }

        }
    }

    //For Italic
    public static void makeItalicForOne(LayoutForLoadOneBinding oneBinding, boolean italic) {
        Utility.setItalicText(oneBinding.gmailText, italic);
        Utility.setItalicText(oneBinding.contactText, italic);
        Utility.setItalicText(oneBinding.locationText, italic);
    }

    public static void makeItalicForTwo(LayoutForLoadTwoBinding twoBinding, boolean italic) {
        Utility.setItalicText(twoBinding.gmailText, italic);
        Utility.setItalicText(twoBinding.contactText, italic);
        Utility.setItalicText(twoBinding.locationText, italic);
        Utility.setItalicText(twoBinding.websiteText, italic);

    }

    public static void makeItalicForThree(LayoutForLoadThreeBinding threeBinding, boolean italic) {
        Utility.setItalicText(threeBinding.gmailText, italic);
        Utility.setItalicText(threeBinding.contactText, italic);
        Utility.setItalicText(threeBinding.locationText, italic);
        Utility.setItalicText(threeBinding.websiteText, italic);
    }

    public static void makeItalicForFour(LayoutForLoadFourBinding fourBinding, boolean italic) {
        Utility.setItalicText(fourBinding.gmailText, italic);
        Utility.setItalicText(fourBinding.contactText, italic);
        Utility.setItalicText(fourBinding.locationText, italic);
        Utility.setItalicText(fourBinding.websiteText, italic);
    }

    public static void makeItalicForFive(LayoutForLoadFiveBinding fiveBinding, boolean italic) {
        Utility.setItalicText(fiveBinding.gmailText, italic);
        Utility.setItalicText(fiveBinding.phoneTxt, italic);
        Utility.setItalicText(fiveBinding.websiteText, italic);
    }

    public static void makeItalicForSix(LayoutForLoadSixBinding sixBinding, boolean italic) {
        Utility.setItalicText(sixBinding.textElement1, italic);
        Utility.setItalicText(sixBinding.contactText, italic);
    }

    public static void makeItalicForSeven(LayoutForLoadSevenBinding sevenBinding, boolean italic) {
        Utility.setItalicText(sevenBinding.brandNameText, italic);
        Utility.setItalicText(sevenBinding.gmailText, italic);
        Utility.setItalicText(sevenBinding.contactText, italic);
    }

    public static void makeItalicForEight(LayoutForLoadEightBinding eightBinding, boolean italic) {
        Utility.setItalicText(eightBinding.brandNameText, italic);
        Utility.setItalicText(eightBinding.gmailText, italic);
        Utility.setItalicText(eightBinding.contactText, italic);
        Utility.setItalicText(eightBinding.locationText, italic);
    }

    public static void makeItalicForNine(LayoutForLoadNineBinding nineBinding, boolean italic) {
        Utility.setItalicText(nineBinding.brandNameText, italic);
        //Utility.setItalicText(nineBinding.gmailText, italic);
        Utility.setItalicText(nineBinding.contactText, italic);

    }

    public static void makeItalicForTen(LayoutForLoadTenBinding tenBinding, boolean italic) {
        Utility.setItalicText(tenBinding.gmailText, italic);
        Utility.setItalicText(tenBinding.contactText, italic);
        Utility.setItalicText(tenBinding.locationText, italic);
    }

    public static void makeItalicForEleven(LayoutFooterElevenBinding tenBinding, boolean italic) {
        Utility.setItalicText(tenBinding.emailEdt, italic);
        Utility.setItalicText(tenBinding.phoneTxt, italic);
    }

    public static void makeItalicForTwelve(LayoutFooterTweloneBinding tweloneBinding, boolean italic) {
        Utility.setItalicText(tweloneBinding.emailTxt, italic);
        Utility.setItalicText(tweloneBinding.mobileNo, italic);
    }

    public static void makeItalicForThirteen(LayoutFooterThirteenBinding thirteenBinding, boolean italic) {
        Utility.setItalicText(thirteenBinding.emailTxt, italic);
        Utility.setItalicText(thirteenBinding.mobileNo, italic);
    }

    public static void makeItalicForFourteen(LayoutFooterFourteenBinding fourteenBinding, boolean italic) {
        Utility.setItalicText(fourteenBinding.mobileNo, italic);

    }

    public static void makeItalicForFifteen(LayoutFooterFifteenBinding fifteenBinding, boolean italic) {
        Utility.setItalicText(fifteenBinding.address, italic);
    }

    public static void makeItalicFor16(LayoutFooterSixteenBinding fifteenBinding, boolean italic) {
        Utility.setItalicText(fifteenBinding.address, italic);
        Utility.setItalicText(fifteenBinding.mobileNo, italic);
        Utility.setItalicText(fifteenBinding.email, italic);
    }


    //ForBold
    public static void baseForBold(boolean bold, int footerLayout, LayoutModelClass layoutModelClass) {
        if (bold) {
            if (footerLayout == 1) {
                FooterHelper.makeBoldForOne(layoutModelClass.getOneBinding(), true);
            } else if (footerLayout == 2) {
                FooterHelper.makeBoldForTwo(layoutModelClass.getTwoBinding(), true);
            } else if (footerLayout == 3) {
                FooterHelper.makeBoldForThree(layoutModelClass.getThreeBinding(), true);
            } else if (footerLayout == 4) {
                FooterHelper.makeBoldForFour(layoutModelClass.getFourBinding(), true);
            } else if (footerLayout == 5) {
                FooterHelper.makeBoldForFive(layoutModelClass.getFiveBinding(), true);
            } else if (footerLayout == 6) {
                FooterHelper.makeBoldForSix(layoutModelClass.getSixBinding(), true);
            } else if (footerLayout == 7) {
                FooterHelper.makeBoldForSeven(layoutModelClass.getSevenBinding(), true);
            } else if (footerLayout == 8) {
                FooterHelper.makeBoldForEight(layoutModelClass.getEightBinding(), true);
            } else if (footerLayout == 9) {
                FooterHelper.makeBoldForNine(layoutModelClass.getNineBinding(), true);
            } else if (footerLayout == 10) {
                FooterHelper.makeBoldForTen(layoutModelClass.getTenBinding(), true);
            } else if (footerLayout == 11) {
                FooterHelper.makeBoldForEleven(layoutModelClass.getElevenBinding(), true);
            } else if (footerLayout == 12) {
                FooterHelper.makeBoldForTwelve(layoutModelClass.getTwelveBinding(), true);
            } else if (footerLayout == 13) {
                FooterHelper.makeBoldForThirteen(layoutModelClass.getThirteenBinding(), true);
            } else if (footerLayout == 14) {
                FooterHelper.makeBoldForFourteen(layoutModelClass.getFourteenBinding(), true);
            } else if (footerLayout == 15) {
                FooterHelper.makeBoldForFifteen(layoutModelClass.getFifteenBinding(), true);
            } else if (footerLayout == 16) {
                FooterHelper.makeBoldFor16(layoutModelClass.getSixteenBinding(), true);
            }

        } else {
            if (footerLayout == 1) {
                FooterHelper.makeBoldForOne(layoutModelClass.getOneBinding(), false);
            } else if (footerLayout == 2) {
                FooterHelper.makeBoldForTwo(layoutModelClass.getTwoBinding(), false);
            } else if (footerLayout == 3) {
                FooterHelper.makeBoldForThree(layoutModelClass.getThreeBinding(), false);
            } else if (footerLayout == 4) {
                FooterHelper.makeBoldForFour(layoutModelClass.getFourBinding(), false);
            } else if (footerLayout == 5) {
                FooterHelper.makeBoldForFive(layoutModelClass.getFiveBinding(), false);
            } else if (footerLayout == 6) {
                FooterHelper.makeBoldForSix(layoutModelClass.getSixBinding(), false);
            } else if (footerLayout == 7) {
                FooterHelper.makeBoldForSeven(layoutModelClass.getSevenBinding(), false);
            } else if (footerLayout == 8) {
                FooterHelper.makeBoldForEight(layoutModelClass.getEightBinding(), false);
            } else if (footerLayout == 9) {
                FooterHelper.makeBoldForNine(layoutModelClass.getNineBinding(), false);
            } else if (footerLayout == 10) {
                FooterHelper.makeBoldForTen(layoutModelClass.getTenBinding(), false);
            } else if (footerLayout == 11) {
                FooterHelper.makeBoldForEleven(layoutModelClass.getElevenBinding(), false);
            } else if (footerLayout == 12) {
                FooterHelper.makeBoldForTwelve(layoutModelClass.getTwelveBinding(), false);
            } else if (footerLayout == 13) {
                FooterHelper.makeBoldForThirteen(layoutModelClass.getThirteenBinding(), false);
            } else if (footerLayout == 14) {
                FooterHelper.makeBoldForFourteen(layoutModelClass.getFourteenBinding(), false);
            } else if (footerLayout == 15) {
                FooterHelper.makeBoldForFifteen(layoutModelClass.getFifteenBinding(), false);
            } else if (footerLayout == 16) {
                FooterHelper.makeBoldFor16(layoutModelClass.getSixteenBinding(), false);
            }
        }
    }

    public static void makeBoldForOne(LayoutForLoadOneBinding oneBinding, boolean bold) {
        Utility.setBold(oneBinding.gmailText, bold);
        Utility.setBold(oneBinding.contactText, bold);
        Utility.setBold(oneBinding.locationText, bold);
    }

    public static void makeBoldForTwo(LayoutForLoadTwoBinding twoBinding, boolean bold) {
        Utility.setBold(twoBinding.gmailText, bold);
        Utility.setBold(twoBinding.contactText, bold);
        Utility.setBold(twoBinding.locationText, bold);
        Utility.setBold(twoBinding.websiteText, bold);

    }

    public static void makeBoldForThree(LayoutForLoadThreeBinding threeBinding, boolean bold) {
        Utility.setBold(threeBinding.gmailText, bold);
        Utility.setBold(threeBinding.contactText, bold);
        Utility.setBold(threeBinding.locationText, bold);
        Utility.setBold(threeBinding.websiteText, bold);
    }

    public static void makeBoldForFour(LayoutForLoadFourBinding fourBinding, boolean bold) {
        Utility.setBold(fourBinding.gmailText, bold);
        Utility.setBold(fourBinding.contactText, bold);
        Utility.setBold(fourBinding.locationText, bold);
        Utility.setBold(fourBinding.websiteText, bold);
    }

    public static void makeBoldForFive(LayoutForLoadFiveBinding fiveBinding, boolean bold) {
        Utility.setBold(fiveBinding.gmailText, bold);
        Utility.setBold(fiveBinding.phoneTxt, bold);
        Utility.setBold(fiveBinding.websiteText, bold);
    }

    public static void makeBoldForSix(LayoutForLoadSixBinding sixBinding, boolean bold) {
        Utility.setBold(sixBinding.contactText, bold);
        Utility.setBold(sixBinding.textElement1, bold);
    }

    public static void makeBoldForSeven(LayoutForLoadSevenBinding sevenBinding, boolean bold) {
        Utility.setBold(sevenBinding.brandNameText, bold);
        Utility.setBold(sevenBinding.gmailText, bold);
        Utility.setBold(sevenBinding.contactText, bold);
    }

    public static void makeBoldForEight(LayoutForLoadEightBinding eightBinding, boolean bold) {
        Utility.setBold(eightBinding.brandNameText, bold);
        Utility.setBold(eightBinding.gmailText, bold);
        Utility.setBold(eightBinding.contactText, bold);
        Utility.setBold(eightBinding.locationText, bold);
    }

    public static void makeBoldForNine(LayoutForLoadNineBinding nineBinding, boolean bold) {
        Utility.setBold(nineBinding.brandNameText, bold);
        //  Utility.setBold(nineBinding.gmailText, bold);
        Utility.setBold(nineBinding.contactText, bold);


    }

    public static void makeBoldForTen(LayoutForLoadTenBinding tenBinding, boolean bold) {
        Utility.setBold(tenBinding.gmailText, bold);
        Utility.setBold(tenBinding.contactText, bold);
        Utility.setBold(tenBinding.locationText, bold);
    }

    public static void makeBoldForEleven(LayoutFooterElevenBinding tenBinding, boolean bold) {
        Utility.setBold(tenBinding.emailEdt, bold);
        Utility.setBold(tenBinding.phoneTxt, bold);
    }

    public static void makeBoldForTwelve(LayoutFooterTweloneBinding tweloneBinding, boolean bold) {
        Utility.setBold(tweloneBinding.emailTxt, bold);
        Utility.setBold(tweloneBinding.mobileNo, bold);
    }

    public static void makeBoldForThirteen(LayoutFooterThirteenBinding thirteenBinding, boolean bold) {
        Utility.setBold(thirteenBinding.emailTxt, bold);
        Utility.setBold(thirteenBinding.mobileNo, bold);
    }

    public static void makeBoldForFourteen(LayoutFooterFourteenBinding fourteenBinding, boolean bold) {
        Utility.setBold(fourteenBinding.mobileNo, bold);
    }

    public static void makeBoldForFifteen(LayoutFooterFifteenBinding fifteenBinding, boolean bold) {
        Utility.setBold(fifteenBinding.address, bold);
    }

    public static void makeBoldFor16(LayoutFooterSixteenBinding sixteenBinding, boolean bold) {
        Utility.setBold(sixteenBinding.address, bold);
        Utility.setBold(sixteenBinding.mobileNo, bold);
        Utility.setBold(sixteenBinding.email, bold);
    }


    //For Text Size
    public static void baseForTextSize(int textsize, int footerLayout, LayoutModelClass layoutModelClass) {
        if (footerLayout == 1) {
            FooterHelper.makeTextSizeForOne(layoutModelClass.getOneBinding(), textsize);
        } else if (footerLayout == 2) {
            FooterHelper.makeTextSizeForTwo(layoutModelClass.getTwoBinding(), textsize);
        } else if (footerLayout == 3) {
            FooterHelper.makeTextSizeForThree(layoutModelClass.getThreeBinding(), textsize);
        } else if (footerLayout == 4) {
            FooterHelper.makeTextSizeForFour(layoutModelClass.getFourBinding(), textsize);
        } else if (footerLayout == 5) {
            FooterHelper.makeTextSizeForFive(layoutModelClass.getFiveBinding(), textsize);
        } else if (footerLayout == 6) {
            FooterHelper.makeTextSizeForSix(layoutModelClass.getSixBinding(), textsize);
        } else if (footerLayout == 7) {
            FooterHelper.makeTextSizeForSeven(layoutModelClass.getSevenBinding(), textsize);
        } else if (footerLayout == 8) {
            FooterHelper.makeTextSizeForEight(layoutModelClass.getEightBinding(), textsize);
        } else if (footerLayout == 9) {
            FooterHelper.makeTextSizeForNine(layoutModelClass.getNineBinding(), textsize);
        } else if (footerLayout == 10) {
            FooterHelper.makeTextSizeForTen(layoutModelClass.getTenBinding(), textsize);
        } else if (footerLayout == 11) {
            FooterHelper.makeTextSizeForEleven(layoutModelClass.getElevenBinding(), textsize);
        } else if (footerLayout == 12) {
            FooterHelper.makeTextSizeForTwelve(layoutModelClass.getTwelveBinding(), textsize);
        } else if (footerLayout == 13) {
            FooterHelper.makeTextSizeForThirteen(layoutModelClass.getThirteenBinding(), textsize);
        } else if (footerLayout == 14) {
            FooterHelper.makeTextSizeForFourteen(layoutModelClass.getFourteenBinding(), textsize);
        } else if (footerLayout == 15) {
            FooterHelper.makeTextSizeForFifteen(layoutModelClass.getFifteenBinding(), textsize);
        } else if (footerLayout == 16) {
            FooterHelper.makeTextSizeFor16(layoutModelClass.getSixteenBinding(), textsize);
        }
    }

    public static void makeTextSizeForOne(LayoutForLoadOneBinding oneBinding, int textsize) {
        oneBinding.gmailText.setTextSize(textsize);
        oneBinding.contactText.setTextSize(textsize);
        oneBinding.locationText.setTextSize(textsize);
    }

    public static void makeTextSizeForTwo(LayoutForLoadTwoBinding twoBinding, int textsize) {
        twoBinding.gmailText.setTextSize(textsize);
        twoBinding.contactText.setTextSize(textsize);
        twoBinding.locationText.setTextSize(textsize);
        twoBinding.websiteText.setTextSize(textsize);
    }

    public static void makeTextSizeForThree(LayoutForLoadThreeBinding threeBinding, int textsize) {
        threeBinding.gmailText.setTextSize(textsize);
        threeBinding.contactText.setTextSize(textsize);
        threeBinding.locationText.setTextSize(textsize);
        threeBinding.websiteText.setTextSize(textsize);
    }

    public static void makeTextSizeForFour(LayoutForLoadFourBinding fourBinding, int textsize) {
        fourBinding.gmailText.setTextSize(textsize);
        fourBinding.contactText.setTextSize(textsize);
        fourBinding.locationText.setTextSize(textsize);
        fourBinding.websiteText.setTextSize(textsize);
    }

    public static void makeTextSizeForFive(LayoutForLoadFiveBinding fiveBinding, int textsize) {
        fiveBinding.gmailText.setTextSize(textsize);
        fiveBinding.phoneTxt.setTextSize(textsize);
        fiveBinding.websiteText.setTextSize(textsize);
    }

    public static void makeTextSizeForSix(LayoutForLoadSixBinding sixBinding, int textsize) {
        sixBinding.textElement1.setTextSize(textsize);
        sixBinding.contactText.setTextSize(textsize);
    }

    public static void makeTextSizeForSeven(LayoutForLoadSevenBinding sevenBinding, int textsize) {
        sevenBinding.brandNameText.setTextSize(textsize);
        sevenBinding.gmailText.setTextSize(textsize);
        sevenBinding.contactText.setTextSize(textsize);
    }

    public static void makeTextSizeForEight(LayoutForLoadEightBinding eightBinding, int textsize) {
        eightBinding.brandNameText.setTextSize(textsize);
        eightBinding.gmailText.setTextSize(textsize);
        eightBinding.contactText.setTextSize(textsize);
        eightBinding.locationText.setTextSize(textsize);
    }

    public static void makeTextSizeForNine(LayoutForLoadNineBinding nineBinding, int textsize) {
        nineBinding.brandNameText.setTextSize(textsize);
        //  nineBinding.gmailText.setTextSize(textsize);
        nineBinding.contactText.setTextSize(textsize);


    }

    public static void makeTextSizeForTen(LayoutForLoadTenBinding tenBinding, int textsize) {
        tenBinding.gmailText.setTextSize(textsize);
        tenBinding.contactText.setTextSize(textsize);
        tenBinding.locationText.setTextSize(textsize);
    }

    public static void makeTextSizeForEleven(LayoutFooterElevenBinding tenBinding, int textsize) {
        tenBinding.emailEdt.setTextSize(textsize);
        tenBinding.phoneTxt.setTextSize(textsize);
    }

    public static void makeTextSizeForTwelve(LayoutFooterTweloneBinding tweloneBinding, int textsize) {
        tweloneBinding.emailTxt.setTextSize(textsize);
        tweloneBinding.mobileNo.setTextSize(textsize);
    }

    public static void makeTextSizeForThirteen(LayoutFooterThirteenBinding thirteenBinding, int textsize) {
        thirteenBinding.emailTxt.setTextSize(textsize);
        thirteenBinding.mobileNo.setTextSize(textsize);
    }

    public static void makeTextSizeForFourteen(LayoutFooterFourteenBinding fourteenBinding, int textsize) {
        fourteenBinding.mobileNo.setTextSize(textsize);
    }

    public static void makeTextSizeForFifteen(LayoutFooterFifteenBinding fifteenBinding, int textsize) {
        fifteenBinding.address.setTextSize(textsize);
    }

    public static void makeTextSizeFor16(LayoutFooterSixteenBinding fifteenBinding, int textsize) {
        fifteenBinding.address.setTextSize(textsize);
        fifteenBinding.email.setTextSize(textsize);
        fifteenBinding.mobileNo.setTextSize(textsize);
    }


    //change color for background and text of footer
    public static void baseForBackground(Activity act, int footerLayout, LayoutModelClass layoutModelClass, int colorCode) {
        if (footerLayout == 1) {
            FooterHelper.ChangeBackgroundColorForFrameOne(act, layoutModelClass.getOneBinding(), colorCode);
        } else if (footerLayout == 2) {
            FooterHelper.ChangeBackgroundColorForFrameTwo(act, layoutModelClass.getTwoBinding(), colorCode);
        }
        //else if (footerLayout == 3) {  FooterHelper.ChangeBackgroundColorForFrameFour(act,layoutModelClass.getThreeBinding(),colorCode);}
        else if (footerLayout == 4) {
            FooterHelper.ChangeBackgroundColorForFrameFour(act, layoutModelClass.getFourBinding(), colorCode);
        } else if (footerLayout == 5) {
            FooterHelper.ChangeBackgroundColorForFrameFive(act, layoutModelClass.getFiveBinding(), colorCode);
        } else if (footerLayout == 6) {
            FooterHelper.ChangeBackgroundColorForFrameSix(act, layoutModelClass.getSixBinding(), colorCode);
        } else if (footerLayout == 7) {
            FooterHelper.ChangeBackgroundColorForFrameSeven(act, layoutModelClass.getSevenBinding(), colorCode);
        } else if (footerLayout == 8) {
            FooterHelper.ChangeBackgroundColorForFrameEight(act, layoutModelClass.getEightBinding(), colorCode);
        } else if (footerLayout == 9) {
            FooterHelper.ChangeBackgroundColorForFrameNine(act, layoutModelClass.getNineBinding(), colorCode);
        } else if (footerLayout == 10) {
            FooterHelper.ChangeBackgroundColorForFrameTen(act, layoutModelClass.getTenBinding(), colorCode);
        } else if (footerLayout == 11) {
            FooterHelper.ChangeBackgroundColorForFrameEleven(act, layoutModelClass.getElevenBinding(), colorCode);
        } else if (footerLayout == 12) {
            FooterHelper.ChangeBackgroundColorForFrameTwelve(act, layoutModelClass.getTwelveBinding(), colorCode);
        } else if (footerLayout == 13) {
            FooterHelper.ChangeBackgroundColorForFrameThirteen(act, layoutModelClass.getThirteenBinding(), colorCode);
        } else if (footerLayout == 14) {
            FooterHelper.ChangeBackgroundColorForFrameFourteen(act, layoutModelClass.getFourteenBinding(), colorCode);
        } else if (footerLayout == 15) {
            FooterHelper.ChangeBackgroundColorForFrameFifteen(act, layoutModelClass.getFifteenBinding(), colorCode);
        } else if (footerLayout == 16) {
            FooterHelper.ChangeBackgroundColorForFrame16(act, layoutModelClass.getSixteenBinding(), colorCode);
        }
    }

    public static void baseForTextColor(Activity act, int footerLayout, LayoutModelClass layoutModelClass, int colorCode) {
        if (footerLayout == 1) {
            FooterHelper.ChangeTextColorForFrameOne(act, layoutModelClass.getOneBinding(), colorCode);
        } else if (footerLayout == 2) {
            FooterHelper.ChangeTextColorForFrameTwo(act, layoutModelClass.getTwoBinding(), colorCode);
        } else if (footerLayout == 3) {
            FooterHelper.ChangeTextColorForFrameThree(act, layoutModelClass.getThreeBinding(), colorCode);
        } else if (footerLayout == 4) {
            FooterHelper.ChangeTextColorForFrameFour(act, layoutModelClass.getFourBinding(), colorCode);
        } else if (footerLayout == 5) {
            FooterHelper.ChangeTextColorForFrameFive(act, layoutModelClass.getFiveBinding(), colorCode);
        } else if (footerLayout == 6) {
            FooterHelper.ChangeTextColorForFrameSix(act, layoutModelClass.getSixBinding(), colorCode);
        } else if (footerLayout == 7) {
            FooterHelper.ChangeTextColorForFrameSeven(act, layoutModelClass.getSevenBinding(), colorCode);
        } else if (footerLayout == 8) {
            FooterHelper.ChangeTextColorForFrameEight(act, layoutModelClass.getEightBinding(), colorCode);
        } else if (footerLayout == 9) {
            FooterHelper.ChangeTextColorForFrameNine(act, layoutModelClass.getNineBinding(), colorCode);
        } else if (footerLayout == 10) {
            FooterHelper.ChangeTextColorForFrameTen(act, layoutModelClass.getTenBinding(), colorCode);
        } else if (footerLayout == 11) {
            FooterHelper.ChangeTextColorForFrameEleven(act, layoutModelClass.getElevenBinding(), colorCode);
        } else if (footerLayout == 12) {
            FooterHelper.ChangeTextColorForFrameTwelve(act, layoutModelClass.getTwelveBinding(), colorCode);
        } else if (footerLayout == 13) {
            FooterHelper.ChangeTextColorForFrameThirteen(act, layoutModelClass.getThirteenBinding(), colorCode);
        } else if (footerLayout == 14) {
            FooterHelper.ChangeTextColorForFrameFourteen(act, layoutModelClass.getFourteenBinding(), colorCode);
        } else if (footerLayout == 15) {
            FooterHelper.ChangeTextColorForFrameFifteen(act, layoutModelClass.getFifteenBinding(), colorCode);
        } else if (footerLayout == 16) {
            FooterHelper.ChangeTextColorForFrame16(act, layoutModelClass.getSixteenBinding(), colorCode);
        }
    }


    public static void ChangeBackgroundColorForFrameOne(Activity act, LayoutForLoadOneBinding oneBinding, int colorCode) {
        oneBinding.topView.setBackgroundColor(colorCode);
        oneBinding.topView2.setBackgroundColor(colorCode);
        oneBinding.addressLayoutElement2.setBackgroundColor(colorCode);
    }

    public static void ChangeTextColorForFrameOne(Activity act, LayoutForLoadOneBinding oneBinding, int colodCode) {
        oneBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        oneBinding.gmailText.setTextColor(colodCode);
        oneBinding.contactImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        oneBinding.contactText.setTextColor(colodCode);
    }

    public static void ChangeBackgroundColorForFrameTwo(Activity act, LayoutForLoadTwoBinding twoBinding, int colorCode) {
        twoBinding.firstView.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        twoBinding.secondView.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public static void ChangeTextColorForFrameTwo(Activity act, LayoutForLoadTwoBinding twoBinding, int colodCode) {
        twoBinding.gmailImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.gmailText.setTextColor(colodCode);
        twoBinding.contactImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.contactText.setTextColor(colodCode);
        twoBinding.websiteImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.websiteText.setTextColor(colodCode);
        twoBinding.locationImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.locationText.setTextColor(colodCode);

    }

    public static void ChangeTextColorForFrameThree(Activity act, LayoutForLoadThreeBinding threeBinding, int colodCode) {
        threeBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.gmailText.setTextColor(colodCode);
        threeBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.contactText.setTextColor(colodCode);
        threeBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.websiteText.setTextColor(colodCode);
        threeBinding.loacationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.locationText.setTextColor(colodCode);

    }

    public static void ChangeBackgroundColorForFrameFour(Activity act, LayoutForLoadFourBinding fourBinding, int colorCode) {
        fourBinding.topView2.setBackgroundColor(colorCode);
    }

    public static void ChangeTextColorForFrameFour(Activity act, LayoutForLoadFourBinding fourBinding, int colodCode) {
        fourBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.gmailText.setTextColor(colodCode);
        fourBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.contactText.setTextColor(colodCode);
        fourBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.websiteText.setTextColor(colodCode);
        fourBinding.locationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.locationText.setTextColor(colodCode);

    }

    public static void ChangeBackgroundColorForFrameFive(Activity act, LayoutForLoadFiveBinding fiveBinding, int colorCode) {
        fiveBinding.element1.setImageTintList(ColorStateList.valueOf(colorCode));
        fiveBinding.element3.setImageTintList(ColorStateList.valueOf(colorCode));
        fiveBinding.viewElement2.setBackgroundColor(colorCode);

    }

    public static void ChangeTextColorForFrameFive(Activity act, LayoutForLoadFiveBinding fiveBinding, int colodCode) {
        fiveBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.gmailText.setTextColor(colodCode);
        fiveBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.phoneTxt.setTextColor(colodCode);
        fiveBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.websiteText.setTextColor(colodCode);
    }

    public static void ChangeBackgroundColorForFrameSix(Activity act, LayoutForLoadSixBinding sixBinding, int colorCode) {
        sixBinding.containerElement.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        sixBinding.viewElement2.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public static void ChangeTextColorForFrameSix(Activity act, LayoutForLoadSixBinding sixBinding, int colodCode) {
        sixBinding.imgElement1.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.imgElement2.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.imgElement3.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.textElement1.setTextColor(colodCode);
        sixBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.contactText.setTextColor(colodCode);
    }

    public static void ChangeBackgroundColorForFrameSeven(Activity act, LayoutForLoadSevenBinding sevenBinding, int colorCode) {

        sevenBinding.element.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public static void ChangeTextColorForFrameSeven(Activity act, LayoutForLoadSevenBinding sevenBinding, int colodCode) {
        sevenBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.imgElement1.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.imgElement2.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.imgElement3.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.gmailText.setTextColor(colodCode);
        sevenBinding.contactText.setTextColor(colodCode);
        sevenBinding.brandNameText.setTextColor(colodCode);

    }

    public static void ChangeBackgroundColorForFrameEight(Activity act, LayoutForLoadEightBinding eightBinding, int colorCode) {

        eightBinding.topView2.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        eightBinding.viewone.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        eightBinding.topView2.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public static void ChangeTextColorForFrameEight(Activity act, LayoutForLoadEightBinding eightBinding, int colodCode) {

        eightBinding.locationText.setTextColor(colodCode);
        eightBinding.gmailText.setTextColor(colodCode);
        eightBinding.contactText.setTextColor(colodCode);
        eightBinding.brandNameText.setTextColor(colodCode);
        eightBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        eightBinding.loacationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        eightBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
    }

    public static void ChangeBackgroundColorForFrameNine(Activity act, LayoutForLoadNineBinding nineBinding, int colorCode) {

        //  nineBinding.element.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        //  nineBinding.element0.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public static void ChangeTextColorForFrameNine(Activity act, LayoutForLoadNineBinding nineBinding, int colodCode) {
        nineBinding.imgElement1.setImageTintList(ColorStateList.valueOf(colodCode));
        nineBinding.imgElement2.setImageTintList(ColorStateList.valueOf(colodCode));
        nineBinding.imgElement3.setImageTintList(ColorStateList.valueOf(colodCode));
        //  nineBinding.gmailText.setTextColor(colodCode);
        nineBinding.contactText.setTextColor(colodCode);
        nineBinding.brandNameText.setTextColor(colodCode);

    }


    public static void ChangeBackgroundColorForFrameTen(Activity act, LayoutForLoadTenBinding tenBinding, int colorCode) {

        tenBinding.gmailImage.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        tenBinding.callImage.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        tenBinding.locationImage.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public static void ChangeBackgroundColorForFrameEleven(Activity act, LayoutFooterElevenBinding tenBinding, int colorCode) {
        tenBinding.socialBackground.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        tenBinding.view1.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        tenBinding.view2.setBackgroundTintList(ColorStateList.valueOf(colorCode));
    }

    public static void ChangeBackgroundColorForFrameTwelve(Activity act, LayoutFooterTweloneBinding tweloneBinding, int colorCode) {
        tweloneBinding.firstLayer.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        //tweloneBinding.view2.setBackgroundTintList(ColorStateList.valueOf(colorCode));
    }

    public static void ChangeBackgroundColorForFrameThirteen(Activity act, LayoutFooterThirteenBinding thirteenBinding, int colorCode) {
        thirteenBinding.secondLayer.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        thirteenBinding.thirdLayer.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        //tweloneBinding.view2.setBackgroundTintList(ColorStateList.valueOf(colorCode));
    }

    public static void ChangeBackgroundColorForFrameFourteen(Activity act, LayoutFooterFourteenBinding fourteenBinding, int colorCode) {
        fourteenBinding.contentLayout.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        fourteenBinding.view.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        //tweloneBinding.view2.setBackgroundTintList(ColorStateList.valueOf(colorCode));
    }

    public static void ChangeBackgroundColorForFrameFifteen(Activity act, LayoutFooterFifteenBinding fifteenBinding, int colorCode) {
        fifteenBinding.layer.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        fifteenBinding.secondLayer.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        fifteenBinding.fb.setImageTintList(ColorStateList.valueOf(colorCode));
        fifteenBinding.insta.setImageTintList(ColorStateList.valueOf(colorCode));
        fifteenBinding.wp.setImageTintList(ColorStateList.valueOf(colorCode));
        //tweloneBinding.view2.setBackgroundTintList(ColorStateList.valueOf(colorCode));
    }

    public static void ChangeBackgroundColorForFrame16(Activity act, LayoutFooterSixteenBinding fifteenBinding, int colorCode) {
        fifteenBinding.layoutOne.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        fifteenBinding.fb.setImageTintList(ColorStateList.valueOf(colorCode));
        fifteenBinding.insta.setImageTintList(ColorStateList.valueOf(colorCode));
        fifteenBinding.wp.setImageTintList(ColorStateList.valueOf(colorCode));
    }

    public static void ChangeTextColorForFrameTen(Activity act, LayoutForLoadTenBinding tenBinding, int colodCode) {
        tenBinding.gmailText.setTextColor(colodCode);
        tenBinding.contactText.setTextColor(colodCode);
        tenBinding.locationText.setTextColor(colodCode);
    }

    public static void ChangeTextColorForFrameEleven(Activity act, LayoutFooterElevenBinding tenBinding, int colodCode) {
        tenBinding.emailEdt.setTextColor(colodCode);
        tenBinding.phoneTxt.setTextColor(colodCode);
    }

    public static void ChangeTextColorForFrameTwelve(Activity act, LayoutFooterTweloneBinding tweloneBinding, int colodCode) {
        tweloneBinding.emailTxt.setTextColor(colodCode);
        tweloneBinding.secondLayer.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        //tweloneBinding.mobileNo.setTextColor(colodCode);
    }

    public static void ChangeTextColorForFrameThirteen(Activity act, LayoutFooterThirteenBinding thirteenBinding, int colodCode) {
        thirteenBinding.emailTxt.setTextColor(colodCode);
        thirteenBinding.layer.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        //tweloneBinding.mobileNo.setTextColor(colodCode);
    }

    public static void ChangeTextColorForFrameFourteen(Activity act, LayoutFooterFourteenBinding fourteenBinding, int colodCode) {

        fourteenBinding.socialBackground.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        //fourteenBinding.mobileNo.setTextColor(colodCode);
        //fourteenBinding.mobileNo.setTextColor(colodCode);
    }

    public static void ChangeTextColorForFrameFifteen(Activity act, LayoutFooterFifteenBinding fifteenBinding, int colodCode) {

        //   fifteenBinding.socialBackground.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        //fourteenBinding.mobileNo.setTextColor(colodCode);
        //fourteenBinding.mobileNo.setTextColor(colodCode);
    }

    public static void ChangeTextColorForFrame16(Activity act, LayoutFooterSixteenBinding fifteenBinding, int colodCode) {
        fifteenBinding.mobileNo.setTextColor((colodCode));
        fifteenBinding.email.setTextColor((colodCode));
        //fifteenBinding.layoutTwo.setBackgroundTintList(ColorStateList.valueOf((colodCode)));
    }

    //Footer Data Load

    public static void loadFrameFirstData(Activity act, LayoutForLoadOneBinding oneBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            oneBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            oneBinding.gmailLayout.setVisibility(View.GONE);
            oneBinding.contactLayout.setGravity(Gravity.CENTER);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            oneBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            oneBinding.contactLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getAddress().isEmpty()) {
            oneBinding.locationText.setText(activeBrand.getAddress());
        } else {
            oneBinding.addressLayoutElement.setVisibility(View.GONE);
        }
    }

    public static void loadFrameTwoData(Activity act, LayoutForLoadTwoBinding twoBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            twoBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            twoBinding.gmailLayout.setVisibility(View.GONE);
            twoBinding.contactLayout.setGravity(Gravity.CENTER);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            twoBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            twoBinding.contactLayout.setVisibility(View.GONE);
            twoBinding.gmailLayout.setGravity(Gravity.CENTER);
        }

        if (activeBrand.getPhonenumber().isEmpty() && activeBrand.getEmail().isEmpty()) {
            twoBinding.firstView.setVisibility(View.GONE);
        }


        if (!activeBrand.getAddress().isEmpty()) {
            twoBinding.locationText.setText(activeBrand.getAddress());
        } else {
            twoBinding.locationLayout.setVisibility(View.GONE);
            twoBinding.websiteLayout.setGravity(Gravity.CENTER);
        }


        if (!activeBrand.getWebsite().isEmpty()) {
            twoBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            twoBinding.websiteLayout.setVisibility(View.GONE);
            twoBinding.locationLayout.setGravity(Gravity.CENTER);
        }
        if (activeBrand.getWebsite().equals("https://")) {
            twoBinding.websiteLayout.setVisibility(View.GONE);
            twoBinding.locationLayout.setGravity(Gravity.CENTER);
        } else {
            twoBinding.websiteLayout.setVisibility(View.VISIBLE);
        }


        if (activeBrand.getAddress().isEmpty() && activeBrand.getWebsite().equals("https://")) {
            twoBinding.secondView.setVisibility(View.GONE);
        }
    }

    public static void loadFrameThreeData(Activity act, LayoutForLoadThreeBinding threeBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            threeBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            threeBinding.gmailLayout.setVisibility(View.GONE);
            threeBinding.contactLayout.setGravity(Gravity.CENTER);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            threeBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            threeBinding.contactLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getAddress().isEmpty()) {
            threeBinding.locationText.setText(activeBrand.getAddress());
        } else {
            threeBinding.loactionLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getWebsite().isEmpty()) {
            threeBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            threeBinding.websiteEdtLayout.setVisibility(View.GONE);
            threeBinding.loactionLayout.setGravity(Gravity.CENTER);
        }
        if (activeBrand.getWebsite().equals("https://")) {
            threeBinding.websiteEdtLayout.setVisibility(View.GONE);
            threeBinding.loactionLayout.setGravity(Gravity.CENTER);
        } else {
            threeBinding.websiteEdtLayout.setVisibility(View.VISIBLE);
        }


    }

    public static void loadFrameFourData(Activity act, LayoutForLoadFourBinding fourBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            fourBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            fourBinding.gmailLayout.setVisibility(View.GONE);
            fourBinding.topView2.setVisibility(View.GONE);
            fourBinding.topView22.setVisibility(View.VISIBLE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            fourBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            fourBinding.contactLayout.setVisibility(View.GONE);
        }


        if (!activeBrand.getAddress().isEmpty()) {
            fourBinding.locationText.setText(activeBrand.getAddress());
        } else {
            fourBinding.locationLayout.setVisibility(View.GONE);
        }
        if (!activeBrand.getWebsite().isEmpty()) {
            fourBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            fourBinding.websiteLayout.setVisibility(View.GONE);
            fourBinding.topView2.setVisibility(View.GONE);
            fourBinding.topView22.setVisibility(View.VISIBLE);
        }
        if (activeBrand.getWebsite().equals("https://")) {
            fourBinding.websiteLayout.setVisibility(View.GONE);
        } else {
            fourBinding.websiteLayout.setVisibility(View.VISIBLE);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fourBinding.topView2.getLayoutParams();
        final int[] layoutHeight = {params.height};

        fourBinding.main.post(new Runnable() {
            public void run() {
                layoutHeight[0] = fourBinding.main.getHeight();
                params.height = layoutHeight[0];
                fourBinding.topView2.setLayoutParams(params);

            }
        });


    }

    public static void loadFrameFiveData(Activity act, LayoutForLoadFiveBinding fiveBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            fiveBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            fiveBinding.elementEmail.setVisibility(View.GONE);
            fiveBinding.elementMobile.setGravity(Gravity.CENTER);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            fiveBinding.phoneTxt.setText(activeBrand.getPhonenumber());
        } else {
            fiveBinding.elementMobile.setVisibility(View.GONE);
        }

        if (!activeBrand.getWebsite().isEmpty()) {
            fiveBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            fiveBinding.element0.setVisibility(View.GONE);
        }
        if (activeBrand.getWebsite().equals("https://")) {
            fiveBinding.element0.setVisibility(View.GONE);
        } else {
            fiveBinding.element0.setVisibility(View.VISIBLE);
        }
    }

    public static void loadFrameSixData(Activity act, LayoutForLoadSixBinding sixBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();

        if (!activeBrand.getPhonenumber().isEmpty()) {
            sixBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            sixBinding.contactLayout.setVisibility(View.GONE);
        }

    }

    public static void loadFrameSevenData(Activity act, LayoutForLoadSevenBinding sevenBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            sevenBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            sevenBinding.gmailLayout.setVisibility(View.GONE);
            sevenBinding.element.setVisibility(View.GONE);
            sevenBinding.contactLayout.setGravity(Gravity.CENTER);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            sevenBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            sevenBinding.contactLayout.setVisibility(View.GONE);

        }

        if (!activeBrand.getName().isEmpty()) {
            sevenBinding.brandNameText.setText(activeBrand.getName());
        } else {
            sevenBinding.brandNameText.setVisibility(View.GONE);
        }
    }

    public static void loadFrameEightData(Activity act, LayoutForLoadEightBinding eightBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            eightBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            eightBinding.gmailLayout.setVisibility(View.GONE);
            eightBinding.element.setVisibility(View.GONE);
            eightBinding.contactLayout.setGravity(Gravity.CENTER);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            eightBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            eightBinding.contactLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getName().isEmpty()) {
            eightBinding.brandNameText.setText(activeBrand.getName());
        } else {
            eightBinding.brandNameText.setVisibility(View.GONE);
        }

        if (!activeBrand.getAddress().isEmpty()) {
            eightBinding.locationText.setText(activeBrand.getAddress());
        } else {
            eightBinding.addressLayoutElement.setVisibility(View.GONE);
        }
    }

    public static void loadFrameTenData(Activity act, LayoutForLoadTenBinding tenBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            tenBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            tenBinding.gmailText.setVisibility(View.GONE);
            tenBinding.gmailImage.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            tenBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            tenBinding.contactText.setVisibility(View.GONE);
            tenBinding.callImage.setVisibility(View.GONE);
        }
        if (!activeBrand.getAddress().isEmpty()) {
            tenBinding.locationText.setText(activeBrand.getAddress());
        } else {
            tenBinding.locationText.setVisibility(View.GONE);
            tenBinding.locationImage.setVisibility(View.GONE);
        }
    }

    public static void loadFrameElevenData(Activity act, LayoutFooterElevenBinding elevenBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            elevenBinding.emailEdt.setText(activeBrand.getEmail());
        } else {
            elevenBinding.emailEdt.setVisibility(View.GONE);
            elevenBinding.emailIcon.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            elevenBinding.phoneTxt.setText(activeBrand.getPhonenumber());
        } else {
            elevenBinding.phoneTxt.setVisibility(View.GONE);
            elevenBinding.phoneIcon.setVisibility(View.GONE);
        }

    }

    public static void loadFrameTweloneData(Activity act, LayoutFooterTweloneBinding tweloneBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            tweloneBinding.emailTxt.setText(activeBrand.getEmail());
        } else {
            tweloneBinding.emailTxt.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            tweloneBinding.mobileNo.setText(activeBrand.getPhonenumber());
        } else {
            tweloneBinding.mobileNo.setVisibility(View.GONE);
        }

    }

    public static void loadFrameThirteenData(Activity act, LayoutFooterThirteenBinding thirteenBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            thirteenBinding.emailTxt.setText(activeBrand.getEmail());
        } else {
            thirteenBinding.emailTxt.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            thirteenBinding.mobileNo.setText(activeBrand.getPhonenumber());
        } else {
            thirteenBinding.mobileNo.setVisibility(View.GONE);
        }

    }

    public static void loadFrameFourteenData(Activity act, LayoutFooterFourteenBinding fourteenBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getPhonenumber().isEmpty()) {
            fourteenBinding.mobileNo.setText(activeBrand.getPhonenumber());
        } else {
            fourteenBinding.mobileNo.setVisibility(View.GONE);
        }
    }

    public static void loadFrameFifteenData(Activity act, LayoutFooterFifteenBinding fifteenBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getAddress().isEmpty()) {
            fifteenBinding.address.setText(activeBrand.getAddress());
        } else {
            if (!activeBrand.getEmail().isEmpty()) {
                fifteenBinding.locationPin.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_outline_mail_24));
                fifteenBinding.address.setText(activeBrand.getEmail());
            }
        }
    }

    public static void loadFrameNineData(Activity act, LayoutForLoadNineBinding nineBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getPhonenumber().isEmpty()) {
            nineBinding.contactText.setText(activeBrand.getPhonenumber());
        }

        if (!activeBrand.getEmail().isEmpty()) {
            nineBinding.contactText.append("\n");
            nineBinding.contactText.append(activeBrand.getEmail());
        }

        if (!activeBrand.getName().isEmpty()) {
            nineBinding.brandNameText.setText(activeBrand.getName());
        } else {
            nineBinding.brandNameText.setVisibility(View.GONE);
        }


    }

    public static void loadFrame16Data(Activity act, LayoutFooterSixteenBinding tweloneBinding) {
        BrandListItem activeBrand = new PreafManager(act).getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            tweloneBinding.email.setText(activeBrand.getEmail());
        } else {
            tweloneBinding.email.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            tweloneBinding.mobileNo.setText(activeBrand.getPhonenumber());
        } else {
            tweloneBinding.mobileNo.setVisibility(View.GONE);
        }
        if (!activeBrand.getAddress().isEmpty()) {
            tweloneBinding.address.setText(activeBrand.getAddress());
        } else {
            if (!activeBrand.getWebsite().isEmpty()) {
                tweloneBinding.address.setText("www."+activeBrand.getWebsite().replace("http://","").replace("https://","").replace("www.",""));
            }else{
                tweloneBinding.address.setVisibility(View.GONE);
            }
        }
    }
    //change font style----------------------------------------------------------------------------
    public static void baseForFontChange(Activity act, int footerLayout, String font, LayoutModelClass layoutModelClass) {
        if (footerLayout == 1) {
            FooterHelper.setTypographyForOneLayout(layoutModelClass.getOneBinding(), act, font);
        } else if (footerLayout == 2) {
            FooterHelper.setTypographyForTwoLayout(layoutModelClass.getTwoBinding(), act, font);
        } else if (footerLayout == 3) {
            FooterHelper.setTypographyForThreeLayout(layoutModelClass.getThreeBinding(), act, font);
        } else if (footerLayout == 4) {
            FooterHelper.setTypographyForFourLayout(layoutModelClass.getFourBinding(), act, font);
        } else if (footerLayout == 5) {
            FooterHelper.setTypographyForFiveLayout(layoutModelClass.getFiveBinding(), act, font);
        } else if (footerLayout == 6) {
            FooterHelper.setTypographyForSixLayout(layoutModelClass.getSixBinding(), act, font);
        } else if (footerLayout == 7) {
            FooterHelper.setTypographyForSevenLayout(layoutModelClass.getSevenBinding(), act, font);
        } else if (footerLayout == 8) {
            FooterHelper.setTypographyForEightLayout(layoutModelClass.getEightBinding(), act, font);
        } else if (footerLayout == 9) {
            FooterHelper.setTypographyForNineLayout(layoutModelClass.getNineBinding(), act, font);
        } else if (footerLayout == 10) {
            FooterHelper.setTypographyForTenLayout(layoutModelClass.getTenBinding(), act, font);
        } else if (footerLayout == 11) {
            FooterHelper.setTypographyForElevenLayout(layoutModelClass.getElevenBinding(), act, font);
        } else if (footerLayout == 12) {
            FooterHelper.setTypographyForTwelveLayout(layoutModelClass.getTwelveBinding(), act, font);
        } else if (footerLayout == 13) {
            FooterHelper.setTypographyForThirteenLayout(layoutModelClass.getThirteenBinding(), act, font);
        } else if (footerLayout == 14) {
            FooterHelper.setTypographyForFourteenLayout(layoutModelClass.getFourteenBinding(), act, font);
        } else if (footerLayout == 15) {
            //FooterHelper.setTypographyForElevenLayout(layoutModelClass.getElevenBinding(), act, font);
        } else if (footerLayout == 16) {
            FooterHelper.setTypographyFor16(layoutModelClass.getSixteenBinding(), act, font);
        }
    }

    public static void setTypographyForOneLayout(LayoutForLoadOneBinding oneBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        oneBinding.gmailText.setTypeface(custom_font);
        oneBinding.contactText.setTypeface(custom_font);
        oneBinding.locationText.setTypeface(custom_font);
    }

    public static void setTypographyForTwoLayout(LayoutForLoadTwoBinding twoBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        twoBinding.gmailText.setTypeface(custom_font);
        twoBinding.contactText.setTypeface(custom_font);
        twoBinding.locationText.setTypeface(custom_font);
        twoBinding.websiteText.setTypeface(custom_font);
    }

    public static void setTypographyForThreeLayout(LayoutForLoadThreeBinding threeBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        threeBinding.gmailText.setTypeface(custom_font);
        threeBinding.contactText.setTypeface(custom_font);
        threeBinding.locationText.setTypeface(custom_font);
        threeBinding.websiteText.setTypeface(custom_font);
    }

    public static void setTypographyForFourLayout(LayoutForLoadFourBinding fourBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        fourBinding.gmailText.setTypeface(custom_font);
        fourBinding.contactText.setTypeface(custom_font);
        fourBinding.locationText.setTypeface(custom_font);
        fourBinding.websiteText.setTypeface(custom_font);
    }

    public static void setTypographyForFiveLayout(LayoutForLoadFiveBinding fiveBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        fiveBinding.gmailText.setTypeface(custom_font);
        fiveBinding.phoneTxt.setTypeface(custom_font);
        fiveBinding.websiteText.setTypeface(custom_font);
    }

    public static void setTypographyForSixLayout(LayoutForLoadSixBinding sixBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        sixBinding.textElement1.setTypeface(custom_font);
        sixBinding.contactText.setTypeface(custom_font);
    }

    public static void setTypographyForSevenLayout(LayoutForLoadSevenBinding sevenBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        sevenBinding.brandNameText.setTypeface(custom_font);
        sevenBinding.gmailText.setTypeface(custom_font);
        sevenBinding.contactText.setTypeface(custom_font);
    }

    public static void setTypographyForEightLayout(LayoutForLoadEightBinding eightBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        eightBinding.brandNameText.setTypeface(custom_font);
        eightBinding.gmailText.setTypeface(custom_font);
        eightBinding.contactText.setTypeface(custom_font);
        eightBinding.locationText.setTypeface(custom_font);
    }

    public static void setTypographyForNineLayout(LayoutForLoadNineBinding nineBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        nineBinding.brandNameText.setTypeface(custom_font);
        //  nineBinding.gmailText.setTypeface(custom_font);
        nineBinding.contactText.setTypeface(custom_font);
    }

    public static void setTypographyForTenLayout(LayoutForLoadTenBinding tenBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        tenBinding.locationText.setTypeface(custom_font);
        tenBinding.gmailText.setTypeface(custom_font);
        tenBinding.contactText.setTypeface(custom_font);
    }

    public static void setTypographyForElevenLayout(LayoutFooterElevenBinding tenBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        tenBinding.emailEdt.setTypeface(custom_font);
        tenBinding.phoneTxt.setTypeface(custom_font);
    }

    public static void setTypographyForTwelveLayout(LayoutFooterTweloneBinding tweloneBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        tweloneBinding.emailTxt.setTypeface(custom_font);
        tweloneBinding.mobileNo.setTypeface(custom_font);
    }

    public static void setTypographyForThirteenLayout(LayoutFooterThirteenBinding thirteenBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        thirteenBinding.emailTxt.setTypeface(custom_font);
        thirteenBinding.mobileNo.setTypeface(custom_font);
    }

    public static void setTypographyForFourteenLayout(LayoutFooterFourteenBinding fourteenBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        fourteenBinding.mobileNo.setTypeface(custom_font);
    }
    public static void setTypographyFor16(LayoutFooterSixteenBinding fourteenBinding, Activity act, String font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), font);
        fourteenBinding.mobileNo.setTypeface(custom_font);
        fourteenBinding.email.setTypeface(custom_font);
        fourteenBinding.address.setTypeface(custom_font);
    }

    //create custom image from relative layout and view
    public static Bitmap getCustomFrameInBitmap(RelativeLayout customeFrame, ImageView actualImage) {
        customeFrame.setDrawingCacheEnabled(true);
        Bitmap newFinal;
        Bitmap returnedBitmap = Bitmap.createBitmap(customeFrame.getWidth(), customeFrame.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);

        Drawable bgDrawable = customeFrame.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        }
        customeFrame.draw(canvas);

        actualImage.setVisibility(View.VISIBLE);

        actualImage.setImageBitmap(returnedBitmap);

        BitmapDrawable drawable = (BitmapDrawable) actualImage.getDrawable();

        newFinal = drawable.getBitmap();


        actualImage.setVisibility(View.VISIBLE);
        //newFinal bitmap
        customeFrame.setDrawingCacheEnabled(false);
        return newFinal;

    }

    //create custom image from relative layout and view
    public static Bitmap getCustomFrameInBitmap(Activity act, RelativeLayout customeFrame, ImageView actualImage) {

        Bitmap newFinal;
        Bitmap returnedBitmap = Bitmap.createBitmap(customeFrame.getWidth(), customeFrame.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        ImageView imageView = act.findViewById(R.id.FrameImageDuplicate);

        Drawable bgDrawable = customeFrame.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        }
        customeFrame.draw(canvas);

        imageView.setVisibility(View.VISIBLE);

        imageView.setImageBitmap(returnedBitmap);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        newFinal = drawable.getBitmap();


        imageView.setVisibility(View.GONE);

        //newFinal bitmap
        return newFinal;

    }

    //create custom image from relative layout and view
    public static Bitmap getCustomFrameInBitmap1(RelativeLayout customeFrame, ImageView actualImage, ImageView frameImage) {

        Bitmap newFinal;
        Bitmap returnedBitmap = Bitmap.createBitmap(customeFrame.getWidth(), customeFrame.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);

        Drawable bgDrawable = customeFrame.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        }
        customeFrame.draw(canvas);

        actualImage.setVisibility(View.VISIBLE);
        frameImage.setVisibility(View.VISIBLE);
        actualImage.setImageBitmap(returnedBitmap);
        frameImage.setImageBitmap(returnedBitmap);
        BitmapDrawable drawable = (BitmapDrawable) actualImage.getDrawable();

        newFinal = drawable.getBitmap();


        actualImage.setVisibility(View.VISIBLE);
        frameImage.setVisibility(View.VISIBLE);
        //newFinal bitmap
        return newFinal;

    }


    //For CreatFileeDisc For Download Image.........................
    public static File createNewFolderForImages() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "BrandMania");
    }


    //fire intent for share
    public static void triggerShareIntent(Activity act, File new_file, Bitmap merged) {
        //  Uri uri = Uri.parse();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(act, merged));
        act.startActivity(Intent.createChooser(share, "Share Image"));
    }

    // generate image from uri
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    //For RefresGalary
    public static void refreshgallery(Activity act, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        act.sendBroadcast(intent);
    }


}
