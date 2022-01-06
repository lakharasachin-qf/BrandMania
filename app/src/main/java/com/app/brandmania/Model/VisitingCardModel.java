package com.app.brandmania.Model;

import com.app.brandmania.databinding.DesignLetterHeadLayoutOneBinding;
import com.app.brandmania.databinding.LayoutDigitalCardFifthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardFourthBinding;
import com.app.brandmania.databinding.LayoutDigitalCardOneBinding;
import com.app.brandmania.databinding.LayoutDigitalCardThreeBinding;
import com.app.brandmania.databinding.LayoutDigitalCardTwoBinding;

public class VisitingCardModel {


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

    public VisitingCardModel setFree(boolean free) {
        isFree = free;
        return this;
    }

    private boolean isFree = true;

    private LayoutDigitalCardOneBinding oneBinding;
    private LayoutDigitalCardTwoBinding twoBinding;
    private LayoutDigitalCardThreeBinding threeBinding;
    private LayoutDigitalCardFourthBinding fourBinding;
    private LayoutDigitalCardFifthBinding fiveBinding;

    public DesignLetterHeadLayoutOneBinding getLatterHeadBinding() {
        return latterHeadBinding;
    }

    public void setLatterHeadBinding(DesignLetterHeadLayoutOneBinding latterHeadBinding) {
        this.latterHeadBinding = latterHeadBinding;
    }

    private DesignLetterHeadLayoutOneBinding latterHeadBinding;


    public LayoutDigitalCardFourthBinding getFourBinding() {
        return fourBinding;
    }

    public void setFourBinding(LayoutDigitalCardFourthBinding fourBinding) {
        this.fourBinding = fourBinding;
    }

    public LayoutDigitalCardFifthBinding getFiveBinding() {
        return fiveBinding;
    }

    public void setFiveBinding(LayoutDigitalCardFifthBinding fiveBinding) {
        this.fiveBinding = fiveBinding;
    }

    public LayoutDigitalCardOneBinding getOneBinding() {
        return oneBinding;
    }

    public void setOneBinding(LayoutDigitalCardOneBinding oneBinding) {
        this.oneBinding = oneBinding;
    }

    public LayoutDigitalCardTwoBinding getTwoBinding() {
        return twoBinding;
    }

    public void setTwoBinding(LayoutDigitalCardTwoBinding twoBinding) {
        this.twoBinding = twoBinding;
    }

    public LayoutDigitalCardThreeBinding getThreeBinding() {
        return threeBinding;
    }


    public void setThreeBinding(LayoutDigitalCardThreeBinding threeBinding) {
        this.threeBinding = threeBinding;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
}
