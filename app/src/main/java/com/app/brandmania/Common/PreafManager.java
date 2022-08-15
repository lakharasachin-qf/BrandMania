package com.app.brandmania.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.ImageList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PreafManager {
    private static final String PREF_NAME = "makemybrand";
    private static final String USER_TOKEN = "user_token";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String ViewAllActivityIntro = "viewAllActivityIntro";
    private static final String ViewAllFrameActivityIntro = "viewAllFrameActivityIntro";
    private static final String ViewAllCustomeImageActivityIntro = "viewAllCustomeImageActivityIntro";
    private static final String FORCUSTOMEFRAME = "FORCUSTOMEFRAME";
    private static final String appTutorial = "appTutorial";

    public int getPreviousCode() {
        return pref.getInt("code", 1);
    }

    public void setAppCode(int parameters) {
        pref.edit().putInt("code", parameters).apply();
    }


    public String getAppTutorial() {
        return pref.getString(appTutorial, "");
    }

    public void setAppTutorial(String parameters) {
        pref.edit().putString(appTutorial, parameters).apply();
    }

    public String getWallet() {
        return pref.getString("walletCoin", "");
    }

    public String setWallet(String parameters) {
        pref.edit().putString("walletCoin", parameters).apply();
        return parameters;
    }

    public String getSpleshReferrer() {
        return pref.getString("SplashReferrer", "");
    }

    public String setSpleshReferrer(String parameters) {
        pref.edit().putString("SplashReferrer", parameters).apply();
        return parameters;
    }
    public String getImageCounter() {
        return pref.getString("ImageCounter", "");
    }

    public String setImageCounter(String parameters) {
        pref.edit().putString("ImageCounter", parameters).apply();
        return parameters;
    }
    public String getDaysCounter() {
        return pref.getString("DaysCounter", "");
    }

    public String setDaysCounter(String parameters) {
        pref.edit().putString("DaysCounter", parameters).apply();
        return parameters;
    }

    public String getReferrerCode() {
        return pref.getString("referrerCode", "");
    }

    public String setReferrerCode(String parameters) {
        pref.edit().putString("referrerCode", parameters).apply();
        return parameters;
    }

    public String getReferCode() {
        return pref.getString("referralCode", "");
    }

    public String setReferCode(String parameters) {
        pref.edit().putString("referralCode", parameters).apply();
        return parameters;
    }

    public String isOneTimeLoad(String date) {
        pref.edit().putString("day", date).apply();
        editor.commit();
        return date;
    }

    public String getOneTimeLoad() {
        return pref.getString("day", "");
    }

    public String isLoginDate(String date) {
        pref.edit().putString("afterDate", date).apply();
        editor.commit();
        return date;
    }

    public String getLoginDate() {
        return pref.getString("afterDate", "");
    }

    public String getUserMobileNo() {
        return pref.getString("mobileNo", "");
    }

    public String setUserMobileNo(String parameters) {
        pref.edit().putString("mobileNo", parameters).apply();
        return parameters;
    }

    @SuppressLint("CommitPrefEdits")
    public PreafManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void Logout() {
        editor.clear();
        editor.commit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
        setUserToken("");
        editor.commit();
        editor.apply();
    }

//    public String getUserName() {
//        return pref.getString("Username", "");
//    }
//
//    public void setUserName(String parameters) {
//        pref.edit().putString("Username", parameters).apply();
//    }

    public String getEMAIL_Id() {
        return pref.getString("email_id", null);
    }

    public void setEMAIL_Id(String parameters) {
        pref.edit().putString("email_id", parameters).apply();
    }

    public Boolean getFrameIntro() {
        return pref.getBoolean(FORCUSTOMEFRAME, true);
    }

    public void setFrameIntro(Boolean parameters) {
        pref.edit().putBoolean(FORCUSTOMEFRAME, parameters).apply();
    }

    public Boolean getViewAllActivityIntro() {
        return pref.getBoolean(ViewAllActivityIntro, true);
    }

    public void setViewAllActivityIntro(Boolean parameters) {
        pref.edit().putBoolean(ViewAllActivityIntro, parameters).apply();
    }


    public Boolean getViewAllFrameActivityIntro() {
        return pref.getBoolean(ViewAllFrameActivityIntro, true);
    }

    public void setViewAllFrameActivityIntro(Boolean parameters) {
        pref.edit().putBoolean(ViewAllFrameActivityIntro, parameters).apply();
    }


    public Boolean getViewAllCustomeImageActivityIntro() {
        return pref.getBoolean(ViewAllCustomeImageActivityIntro, true);
    }

    public void setViewAllCustomeImageActivityIntro(Boolean parameters) {
        pref.edit().putBoolean(ViewAllCustomeImageActivityIntro, parameters).apply();
    }

    public void setMobileNumber(String parameters) {
        pref.edit().putString("MOBILE_NUMBER", parameters).apply();
    }

    public String getMobileNumber() {
        return pref.getString("MOBILE_NUMBER", null);
    }

    public String getUserMobileNumber() {
        return pref.getString("User_number", null);
    }

    public void setUserMobileNumber(String parameters) {
        pref.edit().putString("User_number", parameters).apply();
    }

    public String getUserEmail_Id() {
        return pref.getString("User_email", "");
    }

    public void setUserEmail_Id(String parameters) {
        pref.edit().putString("User_email", parameters).apply();
    }

    public String getUserName() {
        return pref.getString("Username", "");
    }

    public void setUserName(String parameters) {
        pref.edit().putString("Username", parameters).apply();
    }

    public Boolean getIS_Brand() {
        return pref.getBoolean("is_brand", false);
    }

    public void setIS_Brand(boolean parameters) {
        pref.edit().putBoolean("is_brand", parameters).apply();
        editor.apply();
        editor.commit();
    }

    public boolean getIs_Registration() {
        return pref.getBoolean("is_registration", false);
    }

    public void setIs_Registration(boolean parameters) {
        editor.putBoolean("is_registration", parameters).apply();
        editor.apply();
        editor.commit();
    }

    public void removeFromMyFavorites(ImageList imageList) {
        ArrayList<ImageList> FavouritImage;
        FavouritImage = getSavedFavorites();
        for (int i = 0; i < FavouritImage.size(); i++) {
            if (imageList.isCustom()) {
                if (FavouritImage.get(i).getId().equals(imageList.getId())) {
                    FavouritImage.remove(i);
                }
            } else {
                if (!FavouritImage.get(i).isCustom()) {
                    if (FavouritImage.get(i).getId().equals(imageList.getId()) && FavouritImage.get(i).getFrame1Id().equalsIgnoreCase(imageList.getFrame1Id())) {
                        FavouritImage.remove(i);
                    }
                }
            }
        }
        Gson gson = new Gson();
        String jsonshare = gson.toJson(FavouritImage);
        editor.putString("favouritImage", jsonshare);
        editor.apply();
        editor.commit();

    }

    public void AddToMyFavorites(ImageList imageList) {
        ArrayList<ImageList> FavouritImage;
        FavouritImage = getSavedFavorites();
        if (FavouritImage == null) {
            FavouritImage = new ArrayList<>();

        }
        boolean isExits = false;
        int existPos = 0;
        for (int i = 0; i < FavouritImage.size(); i++) {
            if (imageList.isCustom()) {
                if (imageList.getId().equals(FavouritImage.get(i).getId())) {
                    isExits = true;
                    existPos = i;
                    break;
                }
            } else {
                if (imageList.getId().equals(FavouritImage.get(i).getId()) && imageList.getFrame1Id().equalsIgnoreCase(FavouritImage.get(i).getFrame1Id())) {
                    isExits = true;
                    existPos = i;
                    break;
                }
            }
        }
        if (!isExits)
            FavouritImage.add(imageList);

        Gson gson = new Gson();
        String jsonshare = gson.toJson(FavouritImage);
        editor.putString("favouritImage", jsonshare);
        editor.apply();
        editor.commit();
    }

    public ArrayList<ImageList> getSavedFavorites() {
        ArrayList<ImageList> favouritImageItem = new ArrayList<>();
        String jsonFavorites = pref.getString("favouritImage", null);
        TypeToken<ArrayList<ImageList>> typeToken = new TypeToken<ArrayList<ImageList>>() {
        };
        Gson gson = new Gson();
        //setUserToken(favorites.getToken());
        favouritImageItem = gson.fromJson(jsonFavorites, typeToken.getType());
        return favouritImageItem;
    }

    public void setAddBrandList(ArrayList<BrandListItem> list) {
        Gson gson = new Gson();
        String jsonUserProfile = gson.toJson(list);
        editor.putString("brands", jsonUserProfile);
        editor.apply();
        editor.commit();
    }

    public void setActiveBrand(BrandListItem activeBrand) {
        Gson gson = new Gson();
        editor.putString("activeBrands", gson.toJson(activeBrand));
        editor.apply();
        editor.commit();
    }

    public BrandListItem getActiveBrand() {
        Gson gson = new Gson();
        return gson.fromJson(pref.getString("activeBrands", ""), BrandListItem.class);
    }

    public ArrayList<BrandListItem> getAddBrandList() {
        ArrayList<BrandListItem> brandListItems = new ArrayList<>();
        String jsonFavorites = pref.getString("brands", null);
        TypeToken<ArrayList<BrandListItem>> typeToken = new TypeToken<ArrayList<BrandListItem>>() {
        };

        Gson gson = new Gson();
        //setUserToken(favorites.getToken());
        brandListItems = gson.fromJson(jsonFavorites, typeToken.getType());
        return brandListItems;
    }

    public String getUserToken() {
        return pref.getString(USER_TOKEN, null);
    }

    public void loginStep(String is_completed) {
        editor.putString("is_completed", is_completed);
        editor.commit();
        editor.apply();
    }

    public void setUserToken(String token) {
        editor.putString(USER_TOKEN, token);
        editor.commit();
        editor.apply();
    }

    //new coding
    public void setLogin(boolean token) {
        editor.putBoolean("isLogin", token);
        editor.commit();
        editor.apply();
    }

    public boolean isLogin() {
        return pref.getBoolean("isLogin", false);
    }

    //for freeUser
    public void setFreeUserDownloadForOneWeak(boolean status) {
        editor.putBoolean("isUserFree", status);
        editor.commit();
        editor.apply();
    }

    public boolean IsFreeUserDownloadForOneWeak() {
        return pref.getBoolean("isUserFree", false);
    }
}
