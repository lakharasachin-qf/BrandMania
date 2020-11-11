package com.app.brandmania.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.ImageList;

import java.util.ArrayList;

public class PreafManager

{
    private static final String PREF_NAME = "makemybrand";
    private static final String USER_TOKEN = "user_token";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final  String IS_Brand="is_brand";
    private static final  String IS_Registration="is_registration";
    private static final String ADD_BRAND_LIST="add_brand_list";
    private static final String ADD_TO_FAVOURIT="add_to_favourit";
    private static final String EMAIL_Id="email_id";
    private static final String MOBILE_NUMBER="MOBILE_NUMBER";



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
    public String getEMAIL_Id() {
        return pref.getString("email_id", null);
    }
    public void setEMAIL_Id(String parameters) {
        pref.edit().putString("email_id", parameters).apply();
    }

    public String getMobileNumber() {
        return pref.getString("MOBILE_NUMBER", null);
    }
    public void setMobileNumber(String parameters) {
        pref.edit().putString("MOBILE_NUMBER", parameters).apply();
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
    public void removeFromMyFavorites(ImageList imageList){
        ArrayList<ImageList> FavouritImage;
        FavouritImage=getSavedFavorites();
        for (int i=0;i<FavouritImage.size();i++)
        {
           if (FavouritImage.get(i).getId().equals(imageList.getId()) && FavouritImage.get(i).getFrameId().equalsIgnoreCase(imageList.getFrameId())){
               FavouritImage.remove(i);
           }
        }
        Gson gson=new Gson();
        String jsonshare=gson.toJson(FavouritImage);
        editor.putString("favouritImage",jsonshare);
        editor.apply();
        editor.commit();

    }
    public void AddToMyFavorites(ImageList imageList) {
        ArrayList<ImageList> FavouritImage;
        FavouritImage=getSavedFavorites();
        if (FavouritImage == null) {
            FavouritImage=new ArrayList<>();

        }
        boolean isExits=false;
        int existPos=0;
        for (int i=0;i<FavouritImage.size();i++){
            if (imageList.getId().equals(FavouritImage.get(i).getId()) && imageList.getFrameId().equalsIgnoreCase(FavouritImage.get(i).getFrameId())){
                isExits=true;
                existPos=i;
                break;
            }
        }
        if (!isExits)
            FavouritImage.add(imageList);
      /*  else {
            FavouritImage.set(existPos,imageList);
        }*/

        Gson gson=new Gson();
      String jsonshare=gson.toJson(FavouritImage);
      editor.putString("favouritImage",jsonshare);
      editor.apply();
      editor.commit();
    }
    public void removeAllSavedFav(){
        editor.putString("favouritImage","");
        editor.apply();
        editor.commit();
    }
    public ArrayList<ImageList> getSavedFavorites() {
        ArrayList<ImageList> favouritImageItem=new ArrayList<>();
        String jsonFavorites = pref.getString("favouritImage", null);
        TypeToken<ArrayList<ImageList>> typeToken = new TypeToken<ArrayList<ImageList>>() {};
        Gson gson = new Gson();
        //setUserToken(favorites.getToken());
        favouritImageItem = gson.fromJson(jsonFavorites, typeToken.getType());
        return favouritImageItem;
    }
    public void setAddBrandList(ArrayList<BrandListItem> list){
        Gson gson = new Gson();
        String jsonUserProfile = gson.toJson(list);

        editor.putString("brands", jsonUserProfile);
        editor.apply();
        editor.commit();
    }
    public void  setActiveBrand(BrandListItem activeBrand){
        Gson gson=new Gson();
        editor.putString("activeBrands",gson.toJson(activeBrand));
        editor.apply();
        editor.commit();
    }
    public BrandListItem getActiveBrand(){
        Gson gson= new Gson();
        return  gson.fromJson(pref.getString("activeBrands",null),BrandListItem.class);
    }
    public ArrayList<BrandListItem> getAddBrandList() {
        ArrayList<BrandListItem> brandListItems=new ArrayList<>();
        String jsonFavorites = pref.getString("brands", null);
        TypeToken<ArrayList<BrandListItem>> typeToken = new TypeToken<ArrayList<BrandListItem>>() {};

        Gson gson = new Gson();
        //setUserToken(favorites.getToken());
        brandListItems = gson.fromJson(jsonFavorites, typeToken.getType());
        return brandListItems;
    }
    public String getUserToken() {
        return pref.getString(USER_TOKEN, null);
    }
    public void loginStep(String is_completed){
        editor.putString("is_completed",is_completed);
        editor.commit();
        editor.apply();
    }
    public String getLoginStep(){
        return  pref.getString("is_completed","0");
    }

    public void setUserToken(String token) {
        editor.putString(USER_TOKEN, token);
        editor.commit();
        editor.apply();
    }
}
