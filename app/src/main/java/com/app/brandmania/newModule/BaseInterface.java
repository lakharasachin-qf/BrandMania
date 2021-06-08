package com.app.brandmania.newModule;

import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Model.ImageList;

public interface BaseInterface {
    void loadFirstImageEvent(int position, ImageList listModel);
    void loadImageOnCategorySelection(int position, ImageList listModel);
    void loadReadyFrameOnSelection(ImageList imageList,int position);
    void loadFooterOnSelection(int footerLayout, FooterModel footerModel);
    void loadFontSizeOnChange(int fontSize);
    void onTextColorChange(int fontSize);
    void onBackgroundColorChange(int fontSize);
}
