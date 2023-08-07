package com.make.mybrand.Model;

import com.make.mybrand.databinding.DesignLetterHeadLayoutOneBinding;
import com.make.mybrand.databinding.LayoutDigitalCardFifthBinding;
import com.make.mybrand.databinding.LayoutDigitalCardFourthBinding;
import com.make.mybrand.databinding.LayoutDigitalCardOneBinding;
import com.make.mybrand.databinding.LayoutDigitalCardThreeBinding;
import com.make.mybrand.databinding.LayoutDigitalCardTwoBinding;
import com.make.mybrand.databinding.LetterHeadCardOneBinding;
import com.make.mybrand.databinding.LetterHeadCardTwoBinding;

public class LetterHeadModel {


    public static final int LAYOUT_ONE = 0;
    public static final int LAYOUT_TWO = 1;
    public static final int LAYOUT_THREE = 2;
    public static final int LAYOUT_FOUR = 3;
    public static final int LAYOUT_FIVE = 4;
    public static final int LAYOUT_SIX = 5;
    int layoutType;

    public boolean isFree() {
        return isFree;
    }

    public LetterHeadModel setFree(boolean free) {
        isFree = free;
        return this;
    }

    private boolean isFree = true;

    private LetterHeadCardOneBinding oneBinding;
    private LetterHeadCardTwoBinding twoBinding;

    public LetterHeadCardTwoBinding getTwoBinding() {
        return twoBinding;
    }

    public void setTwoBinding(LetterHeadCardTwoBinding twoBinding) {
        this.twoBinding = twoBinding;
    }


    public LetterHeadCardOneBinding getOneBinding() {
        return oneBinding;
    }

    public void setOneBinding(LetterHeadCardOneBinding oneBinding) {
        this.oneBinding = oneBinding;
    }


    public DesignLetterHeadLayoutOneBinding getLatterHeadBinding() {
        return latterHeadBinding;
    }

    public void setLatterHeadBinding(DesignLetterHeadLayoutOneBinding latterHeadBinding) {
        this.latterHeadBinding = latterHeadBinding;
    }

    private DesignLetterHeadLayoutOneBinding latterHeadBinding;

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
}
