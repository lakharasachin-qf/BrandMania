package com.app.brandmania.Common;

import android.content.Context;
import android.util.Log;


import com.app.brandmania.Model.Links;
import com.app.brandmania.Model.SlideSubItem;
import com.app.brandmania.Model.SliderItem;
import com.google.gson.Gson;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.DownloadFavoriteItemList;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.ViewPagerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResponseHandler {
    Context context;


    public ResponseHandler(Context context) {
        this.context = context;
    }

    public static ArrayList<String> getListFromJSon(JSONArray food_types) {
        ArrayList<String> strings = new ArrayList<>();

        if (food_types != null) {
            for (int i = 0; i < food_types.length(); i++) {
                try {
                    strings.add(food_types.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return strings;
        } else {

            return null;
        }

    }

    public static boolean isSuccess(String strResponse, JSONObject jsonResponse) {
        if (strResponse != null) {
            JSONObject jsonObject = createJsonObject(strResponse);
            if (jsonObject != null) {
                return getBool(jsonObject, "status");
            }
        } else if (jsonResponse != null) {
            return getBool(jsonResponse, "status");
        }
        return false;
    }
    public static JSONObject createJsonObject(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getString(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getString(strKey) : "";
        } catch (JSONException e) {
            return "";
        }
    }
    public static int getInt(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getInt(strKey) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }
    public static float getFloat(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? (float) jObj.getDouble(strKey) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }
    public static boolean getBool(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) && jObj.getBoolean(strKey);
        } catch (JSONException e) {
            return false;
        }
    }
    public static JSONObject getJSONObject(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getJSONObject(strKey) : new JSONObject();
        } catch (JSONException e) {
            return new JSONObject();
        }
    }
    public static JSONArray getJSONArray(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getJSONArray(strKey) : new JSONArray();
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
    public static ArrayList<BrandListItem> HandleGetBrandList(JSONObject jsonObject) {
        ArrayList<BrandListItem> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");

            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        BrandListItem examModel = new BrandListItem();
                        examModel.setLayoutType(BrandListItem.LAYOUT_BRANDLIST);
                        examModel.setId(getString(dataJsonObject,"id"));
                        examModel.setCategoryId(getString(dataJsonObject, "br_category_id"));
                        examModel.setCategoryName(getString(dataJsonObject, "br_category_name"));
                        examModel.setName(getString(dataJsonObject, "br_name"));
                        examModel.setPhonenumber(getString(dataJsonObject, "br_phone"));
                        examModel.setWebsite(getString(dataJsonObject, "br_website"));
                        examModel.setEmail(getString(dataJsonObject, "br_email"));
                        examModel.setAddress(getString(dataJsonObject, "br_address"));
                        examModel.setLogo(getString(dataJsonObject,"br_logo"));
                        examModel.setIs_frame(getString(dataJsonObject, "is_frame"));
                        examModel.setFrame_message(getString(dataJsonObject, "frame_message"));
                        examModel.setFrambaseyrl(getString(dataJsonObject, "fream_base_url"));
                        examModel.setIs_payment_pending(getString(dataJsonObject, "is_payment_pending"));
                        examModel.setPayment_message(getString(dataJsonObject, "payment_message"));
                        examModel.setPackagename(getString(dataJsonObject, "package"));
                        examModel.setPackage_id(getString(dataJsonObject, "package_id"));
                        examModel.setPackagemessage(getString(dataJsonObject, "package_message"));
                        examModel.setNo_of_total_image(getString(dataJsonObject, "no_of_img"));
                        examModel.setNo_of_used_image(getString(dataJsonObject, "no_of_used_img"));
                        examModel.setNo_of_frame(getString(dataJsonObject, "no_of_frame"));
                        examModel.setNo_of_remaining(getString(dataJsonObject, "remaining_img"));
                        examModel.setRate(getString(dataJsonObject, "rate"));

                        examModel.setExpiery_date(getString(dataJsonObject, "expire_date"));
                        JSONArray jsonArray = dataJsonObject.getJSONArray("br_frame");

                        ArrayList<FrameItem>frameItems=null;
                        frameItems = new ArrayList<>();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            FrameItem frameItem = new FrameItem();
                            frameItem.setFrame1(ResponseHandler.getString(jsonObject1, "frame_path"));
                            frameItem.setFrameId(ResponseHandler.getString(jsonObject1, "id"));


                            frameItems.add(frameItem);
                        }
                        Gson gson=new Gson();
                        Log.e("Viewww",gson.toJson(frameItems));
                        examModel.setFrame(frameItems);
                        strings.add(examModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }
    public static ArrayList<ViewPagerItem> HandleGetBanneList(JSONObject jsonObject) {
        ArrayList<ViewPagerItem> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");

            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        ViewPagerItem examModel = new ViewPagerItem();

                        examModel.setId(getString(dataJsonObject, "id"));
                        examModel.setSliderImageUrl(getString(dataJsonObject, "banner_photo"));
                        examModel.setRedirection(getString(dataJsonObject, "redirection"));
                        examModel.setBannerstatus(getString(dataJsonObject, "banner_status"));
                        examModel.setPosition(getString(dataJsonObject, "position"));
                        strings.add(examModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }
    public static DashBoardItem HandleGetImageCategory(JSONObject jsonObject) throws JSONException {
        DashBoardItem dashBoardItem=new DashBoardItem();
        ArrayList<DashBoardItem> string = null;
        if (isSuccess(null, jsonObject)) {
            JSONArray datajsonArray = getJSONArray(jsonObject, "data");
            if (!datajsonArray.isNull(0) && datajsonArray.length() != 0) {
                string = new ArrayList<>();
                for (int i = 0; i < datajsonArray.length(); i++) {
                    try {
                        JSONObject datajsonObject = datajsonArray.getJSONObject(i);
                        DashBoardItem model = new DashBoardItem();

                        model.setId(getString(datajsonObject, "id"));
                        model.setName(getString(datajsonObject, "img_cat_name"));
                        model.setDescription(getString(datajsonObject, "img_cat_desc"));
                        model.setTag(getString(datajsonObject, "img_cat_tagd"));
                        JSONArray detailjsonArray = getJSONArray(datajsonObject, "images");
                        ArrayList<ImageList> stringg = null;
                        if (!detailjsonArray.isNull(0) && detailjsonArray.length() != 0) {
                            stringg = new ArrayList<>();
                            for (int j = 0; j < detailjsonArray.length(); j++) {
                                try {
                                    JSONObject detailjsonobject = detailjsonArray.getJSONObject(j);
                                    ImageList data = new ImageList();
                                    data.setLayoutType(ImageList.LAYOUT_IMAGE_CATEGORY);
                                    data.setId(getString(detailjsonobject, "id"));
                                    data.setImagecatid(getString(detailjsonobject, "img_cat_id"));
                                    data.setImageid(getString(detailjsonobject, "img_id"));
                                    data.setLogo(getString(detailjsonobject, "img_thumb_path"));
                                    data.setFrame(getString(detailjsonobject, "img_path"));

                                    stringg.add(data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        model.setImageLists(stringg);

                        if (stringg!=null &&  stringg.size()!=0)
                        string.add(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            dashBoardItem.setDashBoardItems(string);

            JSONObject linkObj=getJSONObject(jsonObject,"link");
            Links links=new Links();
            links.setFirstPage(getString(linkObj,"first_page_url"));
            links.setLastPageUrl(getString(linkObj,"last_page_url"));
            links.setNextPageUrl(getString(linkObj,"next_page_url"));
            links.setPrevPageUrl(getString(linkObj,"prev_page_url"));
            links.setTotalStr(getString(linkObj,"total"));
            dashBoardItem.setLinks(links);



        }

        return dashBoardItem;
    }
    public static ArrayList<ImageList> HandleGetImageByIdCategory(JSONObject jsonObject) {
        ArrayList<ImageList> string = null;
        if (isSuccess(null, jsonObject)) {
            JSONArray datajsonArray = getJSONArray(jsonObject, "data");
            if (!datajsonArray.isNull(0) && datajsonArray.length() != 0) {
                string = new ArrayList<>();
                for (int i = 0; i < datajsonArray.length(); i++) {
                    try {
                        JSONObject datajsonObject = datajsonArray.getJSONObject(i);
                        ImageList model = new ImageList();
                        model.setLayoutType(ImageList.LAYOUT_IMAGE_CATEGORY_BY_ID);
                        model.setName(getString(datajsonObject, "title"));
                        model.setId(getString(datajsonObject,"img_cat_map_id"));
                        model.setImagecatid(getString(datajsonObject, "img_cat_id"));
                        model.setImageid(getString(datajsonObject, "image_id"));
                        model.setLogo(getString(datajsonObject, "img_thumb_path"));
                        model.setFrame(getString(datajsonObject, "img_path"));
                        string.add(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return string;
    }
    public static ArrayList<MultiListItem> HandleFaqResponse(JSONObject jsonObject) {
        ArrayList<MultiListItem> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");

            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        MultiListItem faqModel = new MultiListItem();
                        faqModel.setLayoutType(MultiListItem.LAYOUT_FAQ);
                        faqModel.setQuestion(getString(dataJsonObject, "question"));
                        faqModel.setAnswer(getString(dataJsonObject, "answer"));
                        strings.add(faqModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }
    public static ArrayList<DownloadFavoriteItemList> HandleGetIDownloadFavoritList(JSONObject jsonObject) {
        ArrayList<DownloadFavoriteItemList> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");
            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        DownloadFavoriteItemList downloadFavoriteItemList = new DownloadFavoriteItemList();
                        downloadFavoriteItemList.setLayoutType(DownloadFavoriteItemList.LAYOUT_DOWNLOAD);
                        downloadFavoriteItemList.setName(getString(dataJsonObject, "image_title"));
                        downloadFavoriteItemList.setImage(getString(dataJsonObject, "img_path"));

                        downloadFavoriteItemList.setCustom(getString(dataJsonObject, "is_custom").equalsIgnoreCase("1"));

                        downloadFavoriteItemList.setFrame(getString(dataJsonObject, "frame_path"));
                        strings.add(downloadFavoriteItemList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }

//    public static ArrayList<FrameItem> HandleGetFrame(JSONObject jsonObject) {
//        ArrayList<FrameItem> strings = null;
//        if (isSuccess(null, jsonObject)) {
//            //list fetch
//            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");
//            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
//                strings = new ArrayList<>();
//                for (int i = 0; i < dataJsonArray.length(); i++) {
//                    try {
//                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
//                        FrameItem frameItem = new FrameItem();
//                        frameItem.setFrame1(jsonObject.getString("message")+"/"+ResponseHandler.getString(dataJsonObject, "frame_path"));
//                        frameItem.setFrameId(ResponseHandler.getString(dataJsonObject, "id"));
//
//                        strings.add(frameItem);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//
//        return strings;
//    }
public static ArrayList<FrameItem> HandleGetFrame(JSONObject jsonObject) {
    ArrayList<FrameItem> strings = null;
    if (isSuccess(null, jsonObject)) {
        //list fetch
        JSONObject datajsonobject = getJSONObject(jsonObject, "data");
        JSONArray dataJsonArray = getJSONArray(datajsonobject, "frames");
        if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
            strings = new ArrayList<>();
            for (int i = 0; i < dataJsonArray.length(); i++) {
                try {
                    JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                    FrameItem frameItem = new FrameItem();
                    frameItem.setFrame1(ResponseHandler.getString(dataJsonObject, "frame_path"));
                    frameItem.setFrameId(ResponseHandler.getString(dataJsonObject, "id"));

                    strings.add(frameItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    return strings;
}

    public static ArrayList<DownloadFavoriteItemList> HandleGetIDownloadFavoritGrid(JSONObject jsonObject) {
        ArrayList<DownloadFavoriteItemList> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");
            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        DownloadFavoriteItemList downloadFavoriteItemList = new DownloadFavoriteItemList();
                        downloadFavoriteItemList.setLayoutType(downloadFavoriteItemList.LAYOUT_DOWNLOADGRID);
                        downloadFavoriteItemList.setName(getString(dataJsonObject, "image_title"));
                        downloadFavoriteItemList.setImage(getString(dataJsonObject, "img_path"));
                        downloadFavoriteItemList.setFrame(getString(dataJsonObject, "frame_path"));
                        strings.add(downloadFavoriteItemList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }

    public static ArrayList<DownloadFavoriteItemList> HandleGetIFavoritList(JSONObject jsonObject) {
        ArrayList<DownloadFavoriteItemList> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");
            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        DownloadFavoriteItemList downloadFavoriteItemList = new DownloadFavoriteItemList();
                        downloadFavoriteItemList.setLayoutType(downloadFavoriteItemList.LAYOUT_FAVOURIT);
                        downloadFavoriteItemList.setName(getString(dataJsonObject, "image_title"));
                        downloadFavoriteItemList.setImage(getString(dataJsonObject, "img_path"));
                        downloadFavoriteItemList.setFrame(getString(dataJsonObject, "frame_path"));
                        strings.add(downloadFavoriteItemList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }

    public static ArrayList<DownloadFavoriteItemList> HandleGetIFavoritListGrid(JSONObject jsonObject) {
        ArrayList<DownloadFavoriteItemList> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");
            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        DownloadFavoriteItemList downloadFavoriteItemList = new DownloadFavoriteItemList();
                        downloadFavoriteItemList.setLayoutType(downloadFavoriteItemList.LAYOUT_FAVOURITGRID);
                        downloadFavoriteItemList.setName(getString(dataJsonObject, "image_title"));
                        downloadFavoriteItemList.setImage(getString(dataJsonObject, "img_path"));
                        downloadFavoriteItemList.setFrame(getString(dataJsonObject, "frame_path"));
                        strings.add(downloadFavoriteItemList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }

    public static ArrayList<BrandListItem> HandleGetNotificationList(JSONObject jsonObject) {
        ArrayList<BrandListItem> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");
            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        BrandListItem brandListItem = new BrandListItem();
                        brandListItem.setLayoutType(brandListItem.LAYOUT_NOTIFICATIONlIST);
                        brandListItem.setMessage(getString(dataJsonObject, "message"));
                        brandListItem.setDate(getString(dataJsonObject, "date"));
                        brandListItem.setTime(getString(dataJsonObject, "time"));
                        strings.add(brandListItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }

    public static ArrayList<SliderItem> HandleGetPackageList(JSONObject jsonObject) {
        ArrayList<SliderItem> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");
            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        SliderItem sliderItem = new SliderItem();
                        sliderItem.setPackageid(getString(dataJsonObject,"id"));
                        sliderItem.setPriceForPay(getString(dataJsonObject, "rate"));
                        sliderItem.setPackageTitle(getString(dataJsonObject, "name"));
                        sliderItem.setTemplateTitle(getString(dataJsonObject, "frame_counter"));
                        sliderItem.setImageTitle(getString(dataJsonObject, "img_counter"));
                        sliderItem.setPayTitle(getString(dataJsonObject, "rate"));
                        JSONArray subPackagejsonArray = getJSONArray(dataJsonObject, "service");
                        ArrayList<SlideSubItem> stringg = null;
                        if (!subPackagejsonArray.isNull(0) && subPackagejsonArray.length() != 0) {
                            stringg = new ArrayList<>();
                            for (int j = 0; j < subPackagejsonArray.length(); j++) {
                                try {
                                    JSONObject detailjsonobject = subPackagejsonArray.getJSONObject(j);
                                    SlideSubItem slideSubItem = new SlideSubItem();
                                    slideSubItem.setId(getString(detailjsonobject, "id"));
                                    slideSubItem.setName(getString(detailjsonobject, "name"));
                                    slideSubItem.setDescription(getString(detailjsonobject, "description"));

                                    stringg.add(slideSubItem);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        sliderItem.setSlideSubItems(stringg);
                        if (stringg!=null &&  stringg.size()!=0)
                        strings.add(sliderItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }

    public static ArrayList<BrandListItem> HandleGetBrandById(JSONObject jsonObject) {
        ArrayList<BrandListItem> strings = null;
        if (isSuccess(null, jsonObject)) {
            //list fetch
            JSONArray dataJsonArray = getJSONArray(jsonObject, "data");

            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        BrandListItem examModel = new BrandListItem();
                        examModel.setLayoutType(BrandListItem.LAYOUT_BRANDLIST);
                        examModel.setId(getString(dataJsonObject,"id"));
                        examModel.setCategoryId(getString(dataJsonObject, "br_category_id"));
                        examModel.setCategoryName(getString(dataJsonObject, "br_category_name"));
                        examModel.setName(getString(dataJsonObject, "br_name"));
                        examModel.setPhonenumber(getString(dataJsonObject, "br_phone"));
                        examModel.setWebsite(getString(dataJsonObject, "br_website"));
                        examModel.setEmail(getString(dataJsonObject, "br_email"));
                        examModel.setAddress(getString(dataJsonObject, "br_address"));
                        examModel.setLogo(getString(dataJsonObject,"br_logo"));
                        examModel.setIs_frame(getString(dataJsonObject, "is_frame"));
                        examModel.setFrame_message(getString(dataJsonObject, "frame_message"));
                        examModel.setFrambaseyrl(getString(dataJsonObject, "fream_base_url"));
                        examModel.setIs_payment_pending(getString(dataJsonObject, "is_payment_pending"));
                        examModel.setPayment_message(getString(dataJsonObject, "payment_message"));
                        examModel.setPackagename(getString(dataJsonObject, "package"));
                        examModel.setPackagemessage(getString(dataJsonObject, "package_message"));
                        examModel.setNo_of_total_image(getString(dataJsonObject, "img_counter"));
                        examModel.setNo_of_used_image(getString(dataJsonObject, "no_of_used_img"));
                        examModel.setNo_of_frame(getString(dataJsonObject, "frame_counter"));
                        examModel.setNo_of_remaining(getString(dataJsonObject, "remaining_img"));
                        examModel.setExpiery_date(getString(dataJsonObject, "expire_date"));
                        examModel.setPackage_id(getString(dataJsonObject, "package_id"));
                        examModel.setRate(getString(dataJsonObject, "rate"));
                        examModel.setDuration(getString(dataJsonObject, "duration"));
                        JSONArray jsonArray = dataJsonObject.getJSONArray("br_frame");
                        ArrayList<FrameItem>frameItems=null;
                        frameItems = new ArrayList<>();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            FrameItem frameItem = new FrameItem();
                            frameItem.setFrame1(ResponseHandler.getString(jsonObject1, "frame_path"));
                            frameItem.setFrameId(ResponseHandler.getString(jsonObject1, "id"));


                            frameItems.add(frameItem);
                        }
                        Gson gson=new Gson();
                        Log.e("Viewww",gson.toJson(frameItems));
                        examModel.setFrame(frameItems);
                        strings.add(examModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }

    public static ArrayList<ImageList> HandleGetFrameList(JSONObject jsonObject) {
        ArrayList<ImageList> strings = null;
        if (isSuccess(null, jsonObject)) {
            JSONObject datajsonobject = getJSONObject(jsonObject, "data");
            JSONArray dataJsonArray = getJSONArray(datajsonobject, "frames");
            if (!dataJsonArray.isNull(0) && dataJsonArray.length() != 0) {
                strings = new ArrayList<>();
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    try {
                        JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                        ImageList imageItemItem = new ImageList();
                        imageItemItem.setLayoutType(ImageList.LAYOUT_FRAME);
                        imageItemItem.setFrame1(ResponseHandler.getString(dataJsonObject, "frame_path"));
                        imageItemItem.setFrame1Id(ResponseHandler.getString(dataJsonObject, "id"));

                        strings.add(imageItemItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return strings;
    }
}

