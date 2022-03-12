package com.app.brandmania.Common;

import com.app.brandmania.Model.LetterHeadModel;
import com.app.brandmania.Model.VisitingCardModel;

import java.util.ArrayList;

public class LetterHeadHelper {

    public static ArrayList<LetterHeadModel> getDigitalCardList() {
        ArrayList<LetterHeadModel> digitalCardList = new ArrayList<>();
        LetterHeadModel visitingCardModel = new LetterHeadModel();
        visitingCardModel.setLayoutType(LetterHeadModel.LAYOUT_ONE);
        visitingCardModel.setFree(true);
        digitalCardList.add(visitingCardModel);

        visitingCardModel = new LetterHeadModel();
        visitingCardModel.setLayoutType(LetterHeadModel.LAYOUT_TWO);
        visitingCardModel.setFree(true);
        digitalCardList.add(visitingCardModel);

        return digitalCardList;
    }

}
