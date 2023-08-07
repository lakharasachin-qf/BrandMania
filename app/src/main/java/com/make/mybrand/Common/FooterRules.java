package com.make.mybrand.Common;

import com.make.mybrand.Model.BrandListItem;

public class FooterRules {
    public static boolean _ONE(BrandListItem brand){
        int count=0;
        if (!brand.getPhonenumber().isEmpty() || !brand.getEmail().isEmpty()){
            count++;
        }
        if (!brand.getAddress().isEmpty()){
            count++;
        }
        return count == 2;
    }
}
