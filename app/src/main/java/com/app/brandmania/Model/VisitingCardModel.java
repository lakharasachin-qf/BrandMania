package com.app.brandmania.Model;

import com.app.brandmania.databinding.LayoutDigitalCardOneBinding;
import com.app.brandmania.databinding.LayoutDigitalCardThreeBinding;
import com.app.brandmania.databinding.LayoutDigitalCardTwoBinding;

public class VisitingCardModel {


    public static final int LAYOUT_ONE = 0;
    public static final int LAYOUT_TWO = 1;
    public static final int LAYOUT_THREE = 2;
    int layoutType;

    private LayoutDigitalCardOneBinding oneBinding;
    private LayoutDigitalCardTwoBinding twoBinding;
    private LayoutDigitalCardThreeBinding threeBinding;

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
