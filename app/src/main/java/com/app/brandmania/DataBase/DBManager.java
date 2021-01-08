package com.app.brandmania.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.brandmania.Model.DownloadFavoriteItemList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DBManager {
    public static final int EXAM_TYPE = 0;
    public static final int SUBJECT_TYPE = 1;
    public static final int CITY_TYPE = 2;
    private static final String TAG = DBManager.class.getSimpleName();
    private static String DATABASE_NAME = "brandMania.db";
    public SQLiteDatabase database;
    public DatabaseHelper dbHelper;
    private String DATABASE_PATH;

    public DBManager(Context ctx) {

        DATABASE_PATH = "/data/data/" + ctx.getPackageName() + "/databases/";
        try {
            dbHelper = new DatabaseHelper(ctx);

            database = dbHelper.getWritableDatabase();
            if (database == null) {
                String path = DATABASE_PATH + DATABASE_NAME;
                database = SQLiteDatabase.openOrCreateDatabase(path, null);
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR ===> " + e.toString());
        }

    }



    public void insertStaticContent(String JsonData,int flag) {
        Gson gson = new Gson();
        ContentValues contentValue = new ContentValues();
        //contentValue.put(DatabaseHelper.ADM_ID, columnIndex);
        contentValue.put(DatabaseHelper.IMAGE_PATH, JsonData);
        contentValue.put(DatabaseHelper.FLAG, flag);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public void deleteFromCart(String _id) {
        Log.e("ItemDetelte", "YEs----------------------------------------" + _id);

        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + _id, null);
    }


    public ArrayList<DownloadFavoriteItemList> getAllPracticeQuestion() {

        ArrayList<DownloadFavoriteItemList> downloadFavoriteItemListsModels = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME;

        Cursor cursor = database.rawQuery(selectQuery, null);
        String[] data = null;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.e("All Practice Data", cursor.toString());
                DownloadFavoriteItemList downloadFavoriteItemList = new DownloadFavoriteItemList();
                downloadFavoriteItemList.setId(cursor.getInt(DatabaseHelper.ID_INDEX));
                downloadFavoriteItemList.setImage(cursor.getString(DatabaseHelper.IMAGE_PATH_INDEX));
                downloadFavoriteItemList.setFlag(cursor.getInt(DatabaseHelper.FLAG_INDEX));
                downloadFavoriteItemListsModels.add(downloadFavoriteItemList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return downloadFavoriteItemListsModels;
    }








  /*  public String getRestraruntName() {

        String RestraruntName = "";

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RestraruntName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.RESTAURANT_NAME));
            } while (cursor.moveToNext());
        }

        return RestraruntName;
    }

    public boolean checkRestrarunt(String id, boolean jfu) {
        String jfuId = "0";
        if (jfu) {
            jfuId = "1";
        }
        String allCountQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursorAll = database.rawQuery(allCountQuery, null);

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.RESTAURANT_ID + " = " + id + " AND " + DatabaseHelper.IS_JFU + "=" + jfuId;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursorAll.getCount() != 0 && cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }
    public boolean checkRestrarunt(String id, String jfu) {
        if (jfu.isEmpty()){
            jfu="0";
        }
        String allCountQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursorAll = database.rawQuery(allCountQuery, null);

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.RESTAURANT_ID + " = " + id + " AND " + DatabaseHelper.IS_JFU + "=" + jfu;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursorAll.getCount() != 0 && cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void insert(Products products) {

        Products dbproduct = getSingleUserInfo(products.getId());
        if (dbproduct != null && dbproduct.getId().equals(products.getId())) {
            updateDataCustomize(products);
            return;
        }
        Gson gson = new Gson();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.PRODUCT_ID, products.getId());
        contentValue.put(DatabaseHelper.ITEM_NAME, products.getName());
        contentValue.put(DatabaseHelper.ITEM_PRICE, products.getPrice());
        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_VALUE, products.getDiscount_value());
        contentValue.put(DatabaseHelper.ITEM_CUSTOMIZATION, gson.toJson(products.getCustomizations()));
        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_UNIT, products.getDiscount_unit());
        contentValue.put(DatabaseHelper.ITEM_PACKAGINCAHARGES, products.getPackaging_charges());
        contentValue.put(DatabaseHelper.ITEM_PICTURE, products.getPicture());

        if (products.getTex_rate() != null && !products.getTex_rate().equals("") && !products.getTex_rate().equals("null") && !products.getTex_rate().equals("0")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getTex_rate());

        } else if (products.getData().getTaxRate() != null && !products.getData().getTaxRate().equals("") && !products.getData().getTaxRate().equals("null") && !products.getData().getTaxRate().equals("0")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getData().getTaxRate());

        } else {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, "00.00");

        }

        contentValue.put(DatabaseHelper.ITEM_FOOD_TYPE, gson.toJson(products.getFood_type()));
        contentValue.put(DatabaseHelper.ITEM_DESC, products.getDescription());
        contentValue.put(DatabaseHelper.ITEM_QUANTITY, products.getItem_count());

        contentValue.put(DatabaseHelper.SELECTION_CUSTOMIZATION, products.getSelectedOptions());
        contentValue.put(DatabaseHelper.RESTURANT_PACKAGING_CHARGES, products.getData().getPackagingCharges());

        contentValue.put(DatabaseHelper.RESTAURANT_ID, products.getData().getId());
        contentValue.put(DatabaseHelper.RESTAURANT_NAME, products.getData().getName());
        if (products.getData().getArea() != null && products.getData().getArea().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getData().getArea());
        } else if (products.getData().getAddress() != null && products.getData().getAddress().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getData().getAddress());
        } else {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, "");
        }

        contentValue.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValue.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());

        contentValue.put(DatabaseHelper.TOTAL_SERVICE_CHARGE, products.getData().getServiceCharges());
        contentValue.put(DatabaseHelper.TOTAL_TEX, products.getProduct_Totaltxt());
        Log.e("totaltxt DB--", products.getProduct_Totaltxt());
        contentValue.put(DatabaseHelper.DELIVARYFEE, products.getData().getDeliveryCharge());

        contentValue.put(DatabaseHelper.MAXQUANTITY, products.getMax_quantity());
        contentValue.put(DatabaseHelper.IS_JFU, products.isJFu());

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }

    public int updateWithSameCustomization(Products products) {

        Products products1 = getSingleUserInfo(products.getId());
        ContentValues contentValues = new ContentValues();

        if (products1 != null) {

            double subTotal = 00.00;
            double showed_price = 00.00;
            double quntity = 00.00;
            ArrayList<SubOptions> optionsArrayList = new ArrayList<>();
            double selectedProductValue = 00.00;
            Gson gson = new Gson();
            Type userListType = new TypeToken<ArrayList<SubOptions>>() {
            }.getType();

            optionsArrayList = gson.fromJson(products.getSelectedOptions(), userListType);
            if (optionsArrayList != null) {
                for (SubOptions subOptions : optionsArrayList) {
                    selectedProductValue = selectedProductValue + Double.parseDouble(subOptions.getPrice());
                }
            }

            if (products.getPrice_showed() != null && !products.getPrice_showed().equals("null")  && !products.getPrice_showed().equals("")) {
                showed_price = Double.parseDouble(products.getPrice());
                quntity = Double.parseDouble(String.valueOf(products.getItem_count()));
                subTotal = (showed_price + selectedProductValue) * quntity;
                products.setSubTotal(String.valueOf(subTotal));
            }
            //total tex
            double totaltxt = 00.00;
            double serviceChange = 00.00;
            double packChange = 00.00;

            if (products.getSubTotal() != null && !products.getSubTotal().equals("null") && !products.getSubTotal().equals("")) {
                subTotal = Double.parseDouble(products.getSubTotal());
            }
            if (products.getPackaging_charges() != null && !products.getPackaging_charges().equals("null") && !products.getPackaging_charges().equals("")) {
                packChange = Double.parseDouble(products.getPackaging_charges());
            }
            if (products.getData()!=null && products.getData().getServiceCharges() != null && !products.getData().getServiceCharges().equals("null") && !products.getData().getServiceCharges().equals("")) {
                serviceChange = Double.parseDouble(products.getData().getServiceCharges());
            }else if( products.getServiceCharges()!=null && products.getServiceCharges() != null && !products.getServiceCharges().equals("null") && !products.getServiceCharges().equals("")) {
                serviceChange = Double.parseDouble(products.getServiceCharges());
            }


            totaltxt = (serviceChange + packChange + subTotal);
            if (products.getTex_rate() != null && !products.getTex_rate().equals("") && !products.getTex_rate().equals("null") && Double.compare(Double.parseDouble(products.getTex_rate()), 0.00) != 0) {
                totaltxt = (totaltxt * Double.parseDouble(products.getTex_rate())) / 100;
            } else if (products.getTaxRate() != null && !products.getTaxRate().equals("") && !products.getTaxRate().equals("null") && Double.compare(Double.parseDouble(products.getTaxRate()), 0.00) != 0) {
                totaltxt = (totaltxt * Double.parseDouble(products1.getTaxRate())) / 100;
            } else {
                totaltxt = 00.00;
            }

            products.setProduct_Totaltxt(String.valueOf(totaltxt));

            //grand total
            double grandTotal = 00.00;
            grandTotal = serviceChange + totaltxt + packChange + subTotal;
            products.setGrandTotal(String.valueOf(grandTotal));

            try {
                Log.e("getItem_count", products.getItem_count());
                Log.e("getSubTotal", products.getSubTotal());
                Log.e("GrandTotal", products.getPackaging_charges());
                Log.e("getTotalServiceCharge", products.getData().getServiceCharges());
                Log.e("getProduct_Totaltxt", products.getProduct_Totaltxt());
                Log.e("GrandTotal", products.getGrandTotal());
            } catch (Exception e) {

            }
        } else {
            return -1;
        }
        contentValues.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());
        contentValues.put(DatabaseHelper.ITEM_QUANTITY, products.getItem_count());
        contentValues.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValues.put(DatabaseHelper.TOTAL_TEX, products.getProduct_Totaltxt());

        String fetchId = getLastSpecificField(DatabaseHelper._ID, products.getId());
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + products.getTable_index(), null);
        return i;
    }
    public boolean checkForSameCustomization(String dbObject,String productsObj){
        boolean isSame=false;
        int matchCount=0;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SubOptions>>() {
        }.getType();
      ArrayList<SubOptions> dbSub=gson.fromJson(dbObject,type);
        ArrayList<SubOptions> currSub=gson.fromJson(productsObj,type);
        if (dbSub!=null && currSub != null && dbSub.size()!=0 && currSub.size()!=0) {
            if (currSub.size()==dbSub.size()) {
                for (SubOptions subOptions : currSub) {
                    for (SubOptions subOptions1 : dbSub) {
                        if (subOptions.getId().equals(subOptions1.getId())) {

                            matchCount++;
                        }
                    }
                }
            }
            *//*if (matchCount==currSub.size()){
                isSame=true;
            }*//*
            if (matchCount==dbSub.size()){
                isSame=true;
            }

        }

        return isSame;
    }

    public int insertWithCustomizationSelection(Products products) {
        Gson gson = new Gson();
        ArrayList<Products> dbproduct = getAllSameProduct(products.getId());
        if (dbproduct != null && dbproduct.size()!=0) {
            if (dbproduct.size() == 1 && checkForSameCustomization(dbproduct.get(0).getSelectedOptions(),products.getSelectedOptions())){
                int count= Integer.parseInt(dbproduct.get(0).getItem_count())+1;
                dbproduct.get(0).setItem_count(String.valueOf(count));
                dbproduct.get(0).setData(products.getData());
                dbproduct.get(0).setPrice_showed(products.getPrice_showed());
                updateWithSameCustomization(dbproduct.get(0));
                return 0;
            }
            for (Products products1:dbproduct){
                if (products1.getSelectedOptions()!=null && checkForSameCustomization(products1.getSelectedOptions(),products.getSelectedOptions())){
                    int count= Integer.parseInt(products1.getItem_count())+1;
                    products1.setItem_count(String.valueOf(count));
                    products1.setData(products.getData());
                    products1.setPrice_showed(products.getPrice_showed());
                    updateWithSameCustomization(products1);
                    return 0;
                }
            }
        }




        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.PRODUCT_ID, products.getId());
        contentValue.put(DatabaseHelper.ITEM_NAME, products.getName());
        contentValue.put(DatabaseHelper.ITEM_PRICE, products.getPrice());
        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_VALUE, products.getDiscount_value());
        contentValue.put(DatabaseHelper.ITEM_CUSTOMIZATION, gson.toJson(products.getCustomizations()));
        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_UNIT, products.getDiscount_unit());
        contentValue.put(DatabaseHelper.ITEM_PACKAGINCAHARGES, products.getPackaging_charges());
        contentValue.put(DatabaseHelper.ITEM_PICTURE, products.getPicture());
        if (products.getTex_rate() != null && !products.getTex_rate().equals("") && !products.getTex_rate().equals("null") && !products.getTex_rate().equals("0") &&   !products.getTex_rate().equals("00.00")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getTex_rate());
        } else if (products.getData().getTaxRate() != null && !products.getData().getTaxRate().equals("") && !products.getData().getTaxRate().equals("null") && !products.getData().getTaxRate().equals("0") &&  !products.getData().getTaxRate().equals("00.00")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getData().getTaxRate());
        } else {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, "00.00");
        }

        contentValue.put(DatabaseHelper.ITEM_FOOD_TYPE, gson.toJson(products.getFood_type()));
        contentValue.put(DatabaseHelper.ITEM_DESC, products.getDescription());
        contentValue.put(DatabaseHelper.ITEM_QUANTITY, products.getItem_count());

        contentValue.put(DatabaseHelper.SELECTION_CUSTOMIZATION, products.getSelectedOptions());

        contentValue.put(DatabaseHelper.RESTAURANT_ID, products.getData().getId());
        contentValue.put(DatabaseHelper.RESTAURANT_NAME, products.getData().getName());
        if (products.getData().getArea() != null && products.getData().getArea().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getData().getArea());
        } else if (products.getData().getAddress() != null && products.getData().getAddress().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getData().getAddress());
        } else {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, "");
        }

        contentValue.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValue.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());

        contentValue.put(DatabaseHelper.TOTAL_SERVICE_CHARGE, products.getData().getServiceCharges());
        contentValue.put(DatabaseHelper.TOTAL_TEX, products.getProduct_Totaltxt());

        contentValue.put(DatabaseHelper.DELIVARYFEE, products.getData().getDeliveryCharge());

        contentValue.put(DatabaseHelper.MAXQUANTITY, products.getMax_quantity());
        contentValue.put(DatabaseHelper.IS_JFU, products.isJFu());

        contentValue.put(DatabaseHelper.RESTURANT_PACKAGING_CHARGES, products.getData().getPackagingCharges());

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        return 1;
    }
    public ArrayList<Products> getAllSameProduct(String productId){
        ArrayList<Products> productsArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.PRODUCT_ID + "=" +productId;

        //String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        String[] data = null;
        Log.e("All Table Data", cursor.toString());
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Products products = new Products();

                products.setResturant_area(cursor.getString(DatabaseHelper.RESTAURANT_AREA_INDEX));
                products.setResturant_name(cursor.getString(DatabaseHelper.RESTAURANT_NAME_INDEX));
                products.setResturant_id(cursor.getString(DatabaseHelper.RESTAURANT_ID_INDEX));
                products.setTable_index(cursor.getString(DatabaseHelper._ID_INDEX));
                products.setId(cursor.getString(DatabaseHelper.PRODUCT_ID_INDEX));
                products.setName(cursor.getString(DatabaseHelper.ITEM_NAME_INDEX));
                products.setPicture(cursor.getString(DatabaseHelper.ITEM_PICTURE_INDEX));
                products.setDescription(cursor.getString(DatabaseHelper.ITEM_DESC_INDEX));
                products.setPrice(cursor.getString(DatabaseHelper.ITEM_PRICE_INDEX));
                products.setRestra_packageingCharge(cursor.getString(DatabaseHelper.RESTAURANT_PACKAGING_CHARGE_INDEX));
                if (cursor.getString(DatabaseHelper.IS_JFU_INDEX).equals("1")) {
                    products.setJFu(true);
                } else {
                    products.setJFu(false);
                }
                if (cursor.getString(DatabaseHelper.ITEM_DISCOUNT_VALUE_INDEX).length() == 0) {
                    products.setDiscount_value("0.0");
                } else {
                    products.setDiscount_value(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_VALUE_INDEX));
                }
                products.setDiscount_unit(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_UNIT_INDEX));

                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>() {
                }.getType();
                products.setFood_type(gson.fromJson(cursor.getString(DatabaseHelper.ITEM_FOOD_TYPE_INDEX), type));
                products.setTex_rate(cursor.getString(DatabaseHelper.ITEM_TEX_RATE_INDEX));
                products.setPackaging_charges(cursor.getString(DatabaseHelper.ITEM_PACKAGINCAHARGES_INDEX));
                Customizations customizations = gson.fromJson(cursor.getString(DatabaseHelper.ITEM_CUSTOMIZATION_INDEX), Customizations.class);
                products.setCustomizations(customizations);
                try {
                    Log.e("ConfimationActivity", cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
                    products.setSelectedOptions(cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
                } catch (NullPointerException e) {
                    products.setSelectedOptions("[]");
                }

                //products.setSelectedOptions(cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
                //15 for quintity
                products.setItem_count(cursor.getString(DatabaseHelper.ITEM_QUANTITY_INDEX));
                products.setSubTotal(cursor.getString(DatabaseHelper.SUBTOTAL_INDEX));
                //17 for totolDisCount
                products.setProduct_Totaltxt(cursor.getString(DatabaseHelper.TOTAL_TEX_INDEX));

                products.setTotalServiceCharge(cursor.getString(DatabaseHelper.TOTAL_SERVICE_CHARGE_INDEX));
                products.setGrandTotal(cursor.getString(DatabaseHelper.GRAND_TOTAL_INDEX));
                products.setDevlivaryCharges(cursor.getString(DatabaseHelper.DELIVARYFEE_INDEX));
                products.setDeliveryCharge(cursor.getString(DatabaseHelper.DELIVARYFEE_INDEX));
                products.setMax_quantity(cursor.getString(DatabaseHelper.MAX_QUANTITY));
                if (cursor.getString(DatabaseHelper.IS_JFU_INDEX).equals("1"))
                    products.setJFu(true);
                else
                    products.setJFu(false);
                productsArrayList.add(products);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return  productsArrayList;
    }
    public void deleteAllProduct() {
        database.execSQL("delete from " + DatabaseHelper.TABLE_NAME);
    }

    public void delete(String _id) {
        Log.e("ItemDetelte", "YEs----------------------------------------");
        String fetchId = getLastSpecificField(DatabaseHelper._ID, _id);

        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + fetchId, null);
    }

    public void makeCartEmpty() {
        database.execSQL("delete from " + DatabaseHelper.TABLE_NAME);
    }

    public Products getSingleUserInfo(String product_id) {

        String fetchId = getLastSpecificField(DatabaseHelper._ID, product_id);

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper._ID + "=" + fetchId;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Products products = new Products();
            products.setResturant_id(cursor.getString(DatabaseHelper.RESTAURANT_ID_INDEX));
            products.setId(cursor.getString(DatabaseHelper.PRODUCT_ID_INDEX));
            products.setName(cursor.getString(DatabaseHelper.ITEM_NAME_INDEX));
            products.setPicture(cursor.getString(DatabaseHelper.ITEM_PICTURE_INDEX));
            products.setDescription(cursor.getString(DatabaseHelper.ITEM_DESC_INDEX));
            products.setPrice(cursor.getString(DatabaseHelper.ITEM_PRICE_INDEX));
            products.setDiscount_value(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_VALUE_INDEX));
            products.setDiscount_unit(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_UNIT_INDEX));
            products.setMax_quantity(cursor.getString(DatabaseHelper.MAX_QUANTITY));
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            products.setFood_type(gson.fromJson(cursor.getString(DatabaseHelper.ITEM_FOOD_TYPE_INDEX), type));

            products.setTex_rate(cursor.getString(DatabaseHelper.ITEM_TEX_RATE_INDEX));
            products.setPackaging_charges(cursor.getString(DatabaseHelper.ITEM_PACKAGINCAHARGES_INDEX));
            Customizations customizations = gson.fromJson(cursor.getString(DatabaseHelper.ITEM_CUSTOMIZATION_INDEX), Customizations.class);
            products.setCustomizations(customizations);
            products.setSelectedOptions(cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
            products.setDeliveryCharge(cursor.getString(DatabaseHelper.DELIVARYFEE_INDEX));
            products.setItem_count(cursor.getString(DatabaseHelper.ITEM_QUANTITY_INDEX));
            products.setSubTotal(cursor.getString(DatabaseHelper.SUBTOTAL_INDEX));
            products.setRestra_packageingCharge(cursor.getString(DatabaseHelper.RESTAURANT_PACKAGING_CHARGE_INDEX));
            if (cursor.getString(DatabaseHelper.IS_JFU_INDEX).equals("1"))
                products.setJFu(true);
            else
                products.setJFu(false);
            products.setGrandTotal(cursor.getString(DatabaseHelper.GRAND_TOTAL_INDEX));


            cursor.close();
            return products;
        }
        return null;
    }

    public Products getSingleUserInfoNew(String product_id,String product_option) {

        String fetchId = getLastSpecificField(DatabaseHelper._ID, product_id, product_option);

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper._ID + "=" + fetchId;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Products products = new Products();
            products.setResturant_id(cursor.getString(DatabaseHelper.RESTAURANT_ID_INDEX));
            products.setId(cursor.getString(DatabaseHelper.PRODUCT_ID_INDEX));
            products.setName(cursor.getString(DatabaseHelper.ITEM_NAME_INDEX));
            products.setPicture(cursor.getString(DatabaseHelper.ITEM_PICTURE_INDEX));
            products.setDescription(cursor.getString(DatabaseHelper.ITEM_DESC_INDEX));
            products.setPrice(cursor.getString(DatabaseHelper.ITEM_PRICE_INDEX));
            products.setDiscount_value(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_VALUE_INDEX));
            products.setDiscount_unit(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_UNIT_INDEX));
            products.setMax_quantity(cursor.getString(DatabaseHelper.MAX_QUANTITY));
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            products.setFood_type(gson.fromJson(cursor.getString(DatabaseHelper.ITEM_FOOD_TYPE_INDEX), type));

            products.setTex_rate(cursor.getString(DatabaseHelper.ITEM_TEX_RATE_INDEX));
            products.setPackaging_charges(cursor.getString(DatabaseHelper.ITEM_PACKAGINCAHARGES_INDEX));
            Customizations customizations = gson.fromJson(cursor.getString(DatabaseHelper.ITEM_CUSTOMIZATION_INDEX), Customizations.class);
            products.setCustomizations(customizations);
            products.setSelectedOptions(cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
            products.setDeliveryCharge(cursor.getString(DatabaseHelper.DELIVARYFEE_INDEX));
            products.setItem_count(cursor.getString(DatabaseHelper.ITEM_QUANTITY_INDEX));
            products.setSubTotal(cursor.getString(DatabaseHelper.SUBTOTAL_INDEX));
            products.setRestra_packageingCharge(cursor.getString(DatabaseHelper.RESTAURANT_PACKAGING_CHARGE_INDEX));
            if (cursor.getString(DatabaseHelper.IS_JFU_INDEX).equals("1"))
                products.setJFu(true);
            else
                products.setJFu(false);
            products.setGrandTotal(cursor.getString(DatabaseHelper.GRAND_TOTAL_INDEX));


            cursor.close();
            return products;
        }
        return null;
    }

    public void deleteFromCart(String _id) {
        Log.e("ItemDetelte", "YEs----------------------------------------" + _id);

        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public String getItemCountOfProduct(String productId) {
        String selectQuery = "SELECT  SUM("+ DatabaseHelper.ITEM_QUANTITY+") FROM " + DatabaseHelper.TABLE_NAME+" WHERE "+DatabaseHelper.PRODUCT_ID+" = "+productId;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "";
    }
    public String getAllPackageCharge() {
        String pakagingCharge = DatabaseHelper.ITEM_PACKAGINCAHARGES;

        String selectQuery = "SELECT  SUM(packagingCharges) FROM " + DatabaseHelper.TABLE_NAME;

        Cursor cursor = database.rawQuery(selectQuery, null);

        return String.valueOf(cursor.getInt(0));
     }

    public ArrayList<Products> getAllContacts() {

        vendorDetailModel model = null;

        // Select All Query
        ArrayList<Products> productsArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        String[] data = null;

        if (cursor != null && cursor.moveToFirst()) {
            do {
               Log.e("All Table Data", cursor.toString());
                Products products = new Products();

                products.setResturant_area(cursor.getString(DatabaseHelper.RESTAURANT_AREA_INDEX));
                products.setResturant_name(cursor.getString(DatabaseHelper.RESTAURANT_NAME_INDEX));
                products.setResturant_id(cursor.getString(DatabaseHelper.RESTAURANT_ID_INDEX));
                products.setTable_index(cursor.getString(DatabaseHelper._ID_INDEX));
                products.setId(cursor.getString(DatabaseHelper.PRODUCT_ID_INDEX));
                products.setName(cursor.getString(DatabaseHelper.ITEM_NAME_INDEX));
                products.setPicture(cursor.getString(DatabaseHelper.ITEM_PICTURE_INDEX));
                products.setDescription(cursor.getString(DatabaseHelper.ITEM_DESC_INDEX));
                products.setPrice(cursor.getString(DatabaseHelper.ITEM_PRICE_INDEX));
                products.setRestra_packageingCharge(cursor.getString(DatabaseHelper.RESTAURANT_PACKAGING_CHARGE_INDEX));
                if (cursor.getString(DatabaseHelper.IS_JFU_INDEX).equals("1")) {
                    products.setJFu(true);
                } else {
                    products.setJFu(false);
                }
                if (cursor.getString(DatabaseHelper.ITEM_DISCOUNT_VALUE_INDEX).length() == 0) {
                    products.setDiscount_value("0.0");
                } else {
                    products.setDiscount_value(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_VALUE_INDEX));
                }
                products.setDiscount_unit(cursor.getString(DatabaseHelper.ITEM_DISCOUNT_UNIT_INDEX));

                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>() {
                }.getType();
                products.setFood_type(gson.fromJson(cursor.getString(DatabaseHelper.ITEM_FOOD_TYPE_INDEX), type));
                products.setTex_rate(cursor.getString(DatabaseHelper.ITEM_TEX_RATE_INDEX));
                products.setPackaging_charges(cursor.getString(DatabaseHelper.ITEM_PACKAGINCAHARGES_INDEX));
                Customizations customizations = gson.fromJson(cursor.getString(DatabaseHelper.ITEM_CUSTOMIZATION_INDEX), Customizations.class);
                products.setCustomizations(customizations);
                try {
                    //Log.e("ConfimationActivity", cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
                    products.setSelectedOptions(cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
                } catch (NullPointerException e) {
                    products.setSelectedOptions("[]");
                }

                //products.setSelectedOptions(cursor.getString(DatabaseHelper.SELECTION_CUSTOMIZATION_INDEX));
                //15 for quintity
                products.setItem_count(cursor.getString(DatabaseHelper.ITEM_QUANTITY_INDEX));
                products.setSubTotal(cursor.getString(DatabaseHelper.SUBTOTAL_INDEX));
                //17 for totolDisCount
                products.setProduct_Totaltxt(cursor.getString(DatabaseHelper.TOTAL_TEX_INDEX));

                products.setTotalServiceCharge(cursor.getString(DatabaseHelper.TOTAL_SERVICE_CHARGE_INDEX));
                products.setGrandTotal(cursor.getString(DatabaseHelper.GRAND_TOTAL_INDEX));
                products.setDevlivaryCharges(cursor.getString(DatabaseHelper.DELIVARYFEE_INDEX));
                products.setMax_quantity(cursor.getString(DatabaseHelper.MAX_QUANTITY));
                if (cursor.getString(DatabaseHelper.IS_JFU_INDEX).equals("1"))
                    products.setJFu(true);
                else
                    products.setJFu(false);
                productsArrayList.add(products);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productsArrayList;
    }


    public String getItemSelectionOption(String productId){
        String fetchId = getLastSpecificField(DatabaseHelper._ID, productId);
        String selectQuery = "SELECT "+DatabaseHelper.SELECTION_CUSTOMIZATION+" FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper._ID + "=" + fetchId ;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "";
    }
    public String getOptionBasedOnTable_Index(String productId){

        String selectQuery = "SELECT "+DatabaseHelper.SELECTION_CUSTOMIZATION+" FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper._ID + "=" + productId ;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "";
    }
    public String getItemSelectedOptions(String productId){

        String selectQuery = "SELECT "+DatabaseHelper.SELECTION_CUSTOMIZATION+" FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.PRODUCT_ID + "=" + productId + " ORDER BY "+ DatabaseHelper.PRODUCT_ID +" DESC LIMIT 1";
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "";
    }

    public String getCustomizationQuntity(String productId){
        String selectQuery = "SELECT SUM(" + DatabaseHelper.ITEM_QUANTITY + ") FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.PRODUCT_ID + "=" + productId;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "";
    }
    public void insertFromCart(Products products) {


        Gson gson = new Gson();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.PRODUCT_ID, products.getId());
        contentValue.put(DatabaseHelper.ITEM_NAME, products.getName());
        contentValue.put(DatabaseHelper.ITEM_PRICE, products.getPrice());
        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_VALUE, products.getDiscount_value());
        contentValue.put(DatabaseHelper.ITEM_CUSTOMIZATION, gson.toJson(products.getCustomizations()));
        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_UNIT, products.getDiscount_unit());
        contentValue.put(DatabaseHelper.ITEM_PACKAGINCAHARGES, products.getPackaging_charges());
        contentValue.put(DatabaseHelper.ITEM_PICTURE, products.getPicture());

        if (products.getTex_rate() != null && !products.getTex_rate().equals("") && !products.getTex_rate().equals("null") && !products.getTex_rate().equals("0")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getTex_rate());

        }  else {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, "00.00");

        }

        contentValue.put(DatabaseHelper.ITEM_FOOD_TYPE, gson.toJson(products.getFood_type()));
        contentValue.put(DatabaseHelper.ITEM_DESC, products.getDescription());
        contentValue.put(DatabaseHelper.ITEM_QUANTITY, products.getItem_count());

        contentValue.put(DatabaseHelper.SELECTION_CUSTOMIZATION, products.getSelectedOptions());
        contentValue.put(DatabaseHelper.RESTURANT_PACKAGING_CHARGES, products.getPackaging_charges());

        contentValue.put(DatabaseHelper.RESTAURANT_ID, products.getResturant_id());
        contentValue.put(DatabaseHelper.RESTAURANT_NAME, products.getResturant_name());
        if (products.getResturant_area() != null && products.getResturant_area().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getResturant_area());
        }   else {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, "");
        }

        contentValue.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValue.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());

        contentValue.put(DatabaseHelper.TOTAL_SERVICE_CHARGE, products.getServiceCharges());
        contentValue.put(DatabaseHelper.TOTAL_TEX, products.getProduct_Totaltxt());
        Log.e("totaltxt DB--", products.getProduct_Totaltxt());
        contentValue.put(DatabaseHelper.DELIVARYFEE, products.getDevlivaryCharges());

        contentValue.put(DatabaseHelper.MAXQUANTITY, products.getMax_quantity());
        contentValue.put(DatabaseHelper.IS_JFU, products.isJFu());

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }

    public int updateDataFromCartCustomization(Products products) {
        ArrayList<Products> dbproduct = getAllSameProduct(products.getId());
        if (dbproduct != null && dbproduct.size()!=0) {
            if (dbproduct.size() == 1  ){
                if ( checkForSameCustomization(dbproduct.get(0).getSelectedOptions(),products.getSelectedOptions())) {
                    int count = Integer.parseInt(dbproduct.get(0).getItem_count())+1;
                    dbproduct.get(0).setItem_count(String.valueOf(count));
                    dbproduct.get(0).setSelectedOptions(products.getSelectedOptions());
                    dbproduct.get(0).setPrice_showed(products.getPrice_showed());
                    updateWithSameCustomization(dbproduct.get(0));
                    return 0;
                }else {
                    products.setItem_count("1");
                    insertFromCart(products);
                    return 0;
                }
            }

            int size=dbproduct.size();
            boolean isEntered=false;
            for (Products products1:dbproduct){
                    if (products1.getSelectedOptions() != null && checkForSameCustomization(products1.getSelectedOptions(), products.getSelectedOptions())) {
                        int count = Integer.parseInt(products1.getItem_count()) + 1;
                        products1.setItem_count(String.valueOf(count));
                        products1.setSelectedOptions(products.getSelectedOptions());
                        products1.setPrice_showed(products.getPrice_showed());
                        updateWithSameCustomization(products1);
                        isEntered=false;
                        return 0;
                    }else {
                        isEntered=true;
                    }
                    size--;

            }
            if (isEntered){
                   products.setItem_count("1");
                   insertFromCart(products);
                   return 0;
            }
        }

        Products products1 = getSingleUserInfo(products.getId());
        ContentValues contentValues = new ContentValues();

        if (products1 != null) {

            double subTotal = 00.00;
            //sub total
            double showed_price = 00.00;
            double quntity = 00.00;
            ArrayList<SubOptions> optionsArrayList = new ArrayList<>();
            double selectedProductValue = 00.00;


            //total tex
            double totaltxt = 00.00;
            double serviceChange = 00.00;
            double packChange = 00.00;

            if (products.getSubTotal() != null && !products.getSubTotal().equals("null") && !products.getSubTotal().equals("")) {
                subTotal = Double.parseDouble(products.getSubTotal());
            }
            if (products.getPackaging_charges() != null && !products.getPackaging_charges().equals("null") && !products.getPackaging_charges().equals("")) {
                packChange = Double.parseDouble(products.getPackaging_charges());
            }
            if (products.getTotalServiceCharge() != null && !products.getTotalServiceCharge().equals("null") && !products.getTotalServiceCharge().equals("")) {
                serviceChange = Double.parseDouble(products.getTotalServiceCharge());
            }
            totaltxt = (serviceChange + packChange + subTotal);
            if (products.getTex_rate() != null && !products.getTex_rate().equals("") && !products.getTex_rate().equals("null")) {
                totaltxt = (totaltxt * Double.parseDouble(products.getTex_rate())) / 100;
            } else {
                totaltxt = (totaltxt * Double.parseDouble(products.getData().getTaxRate())) / 100;
            }
            products.setProduct_Totaltxt(String.valueOf(totaltxt));


            //grand total
            double grandTotal = 00.00;
            grandTotal = serviceChange + totaltxt + packChange + subTotal;
            products.setGrandTotal(String.valueOf(grandTotal));


        }
        Log.e("SELECTION_CUSTOMIZATION", products.getSelectedOptions());
        contentValues.put(DatabaseHelper.SELECTION_CUSTOMIZATION, products.getSelectedOptions());

        contentValues.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());
        contentValues.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());
        contentValues.put(DatabaseHelper.ITEM_QUANTITY, products.getItem_count());
        contentValues.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValues.put(DatabaseHelper.TOTAL_TEX, products.getProduct_Totaltxt());

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.PRODUCT_ID + " = " + products.getId(), null);
        return 1;
    }

    public Cursor getCount() {
        String selectQuery = "SELECT (" + DatabaseHelper.DELIVARYFEE + ") as DELIVARYFEE, SUM(" + DatabaseHelper.SUBTOTAL + ") as ItemTotal,SUM(" + DatabaseHelper.GRAND_TOTAL + ") as GrandTotal,SUM(" + DatabaseHelper.SUBTOTAL + ")  as  SubTotal, SUM(" + DatabaseHelper.TOTAL_TEX + ") as total_tex, SUM(" + DatabaseHelper.TOTAL_SERVICE_CHARGE + ")  as total_service_charge ,SUM(" + DatabaseHelper.ITEM_PRICE + ") as Item_total  FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor;
    }

    public String getSingleDelivaryFee() {
        String selectQuery = "SELECT " + DatabaseHelper.DELIVARYFEE + " FROM (" + DatabaseHelper.TABLE_NAME + ") GROUP BY " + DatabaseHelper.DELIVARYFEE;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "00.00";
    }

    public String CheckCustomizationData(String id, String selectedOptions, boolean jfu) {

        String data = "0";

        String jfuId = "0";
        if (jfu) {
            jfuId = "1";
        }
        String selectQuery = "SELECT SUM(quantity) as quantity FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.PRODUCT_ID + " = " + id
                + " AND " + DatabaseHelper.SELECTION_CUSTOMIZATION + "='" + selectedOptions
                + "' AND " + DatabaseHelper.IS_JFU + "=" + jfuId;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            data = cursor.getString(0);
            cursor.close();
        }

        if(data == null){
            return "0";
        } else {
            return data;
        }
    }

    public String getAllDataAndGenerateJSON() throws JSONException, FileNotFoundException {
        String query = "select " + DatabaseHelper._ID + "," +
                DatabaseHelper.PRODUCT_ID + "," +
                DatabaseHelper.ITEM_NAME + "," +
                DatabaseHelper.ITEM_PICTURE + "," +
                DatabaseHelper.ITEM_DESC + "," +
                DatabaseHelper.ITEM_PRICE + "," +
                DatabaseHelper.ITEM_DISCOUNT_VALUE + "," +
                DatabaseHelper.ITEM_DISCOUNT_UNIT + "," +
                DatabaseHelper.ITEM_FOOD_TYPE + "," +
                DatabaseHelper.ITEM_TEX_RATE + "," +
                DatabaseHelper.ITEM_PACKAGINCAHARGES + "," +
                DatabaseHelper.ITEM_CUSTOMIZATION + "," +
                DatabaseHelper.SELECTION_CUSTOMIZATION + "," +
                DatabaseHelper.ITEM_QUANTITY + "," +
                DatabaseHelper.SUBTOTAL + "," +
                DatabaseHelper.TOTAL_DISCOUNT + "," +
                DatabaseHelper.TOTAL_TEX + "," +
                DatabaseHelper.TOTAL_SERVICE_CHARGE + "," +
                DatabaseHelper.GRAND_TOTAL + "," +
                DatabaseHelper.RESTAURANT_ID + "," +
                DatabaseHelper.RESTAURANT_NAME + " from " +
                DatabaseHelper.TABLE_NAME;

        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray ContactArray = new JSONArray();

        *//*File f = new File(Environment.getExternalStorageDirectory() + "/ContactDetail.txt");
        FileOutputStream fos = new FileOutputStream(f, true);
        PrintStream ps = new PrintStream(fos);*//*

        int i = 0;
        while (!c.isAfterLast()) {

            JSONObject contact = new JSONObject();
            try {
                contact.put(DatabaseHelper._ID, c.getString(c.getColumnIndex(DatabaseHelper._ID)));
                contact.put(DatabaseHelper.RESTAURANT_ID, c.getString(c.getColumnIndex(DatabaseHelper.RESTAURANT_ID)));
                contact.put(DatabaseHelper.RESTAURANT_NAME, c.getString(c.getColumnIndex(DatabaseHelper.RESTAURANT_NAME)));
                contact.put(DatabaseHelper.PRODUCT_ID, c.getString(c.getColumnIndex(DatabaseHelper.PRODUCT_ID)));
                contact.put(DatabaseHelper.ITEM_NAME, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_NAME)));
                contact.put(DatabaseHelper.ITEM_PICTURE, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_PICTURE)));
                contact.put(DatabaseHelper.ITEM_DESC, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_DESC)));
                contact.put(DatabaseHelper.ITEM_PRICE, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_PRICE)));
                contact.put(DatabaseHelper.RESTURANT_PACKAGING_CHARGES, c.getColumnIndex(DatabaseHelper.RESTURANT_PACKAGING_CHARGES));
                contact.put(DatabaseHelper.ITEM_DISCOUNT_VALUE, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_DISCOUNT_VALUE)));
                contact.put(DatabaseHelper.ITEM_DISCOUNT_UNIT, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_DISCOUNT_UNIT)));
                contact.put(DatabaseHelper.ITEM_FOOD_TYPE, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_FOOD_TYPE)));
                contact.put(DatabaseHelper.ITEM_TEX_RATE, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_TEX_RATE)));
                contact.put(DatabaseHelper.ITEM_PACKAGINCAHARGES, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_PACKAGINCAHARGES)));
                contact.put(DatabaseHelper.ITEM_CUSTOMIZATION, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_CUSTOMIZATION)));
                contact.put(DatabaseHelper.SELECTION_CUSTOMIZATION, c.getString(c.getColumnIndex(DatabaseHelper.SELECTION_CUSTOMIZATION)));
                contact.put(DatabaseHelper.ITEM_QUANTITY, c.getString(c.getColumnIndex(DatabaseHelper.ITEM_QUANTITY)));
                contact.put(DatabaseHelper.SUBTOTAL, c.getString(c.getColumnIndex(DatabaseHelper.SUBTOTAL)));
                contact.put(DatabaseHelper.TOTAL_DISCOUNT, c.getString(c.getColumnIndex(DatabaseHelper.TOTAL_DISCOUNT)));
                contact.put(DatabaseHelper.TOTAL_TEX, c.getString(c.getColumnIndex(DatabaseHelper.TOTAL_TEX)));
                contact.put(DatabaseHelper.TOTAL_SERVICE_CHARGE, c.getString(c.getColumnIndex(DatabaseHelper.TOTAL_SERVICE_CHARGE)));
                contact.put(DatabaseHelper.GRAND_TOTAL, c.getString(c.getColumnIndex(DatabaseHelper.GRAND_TOTAL)));

                c.moveToNext();

                ContactArray.put(i, contact);

                i++;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Root.put("CONTACTDETAILS", ContactArray);
        //ps.append(Root.toString());
        return Root.toString();
    }

    public String getSpecificField(String columnName) {
        String selectQuery = "SELECT " + columnName + " FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            String returnStr=cursor.getString(0);
            cursor.close();
            return returnStr;
        }
        return "";
    }

    public String getLastSpecificField(String columnName, String productId) {
        String selectQuery = "SELECT  MAX(" + columnName + " ) FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.PRODUCT_ID + "=" + productId;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            String returnStr=cursor.getString(0);
            cursor.close();
            return returnStr;
        }
        return "";
    }

    public String getLastSpecificField(String columnName, String productId, String productOption) {
        String selectQuery = "SELECT  " + columnName + " FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.PRODUCT_ID + "=" + productId
                +" AND "+ DatabaseHelper.SELECTION_CUSTOMIZATION + "='" + productOption + "'";
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToNext()) {
            String returnStr=cursor.getString(0);
            cursor.close();
            return returnStr;
        }
        return "";
    }

    public Cursor isRowExitst(String productId) {
        String selectQuery = "SELECT   " + DatabaseHelper._ID + "  FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.PRODUCT_ID + "=" + productId;
        return database.rawQuery(selectQuery, null);
    }

    public boolean updateDeliveryCharge(String deliveryCharge, String restaurantId) {
        database.execSQL("UPDATE "+DatabaseHelper.TABLE_NAME+" SET " + DatabaseHelper.DELIVARYFEE + " = "+"'"+deliveryCharge+"' WHERE "+ DatabaseHelper.RESTAURANT_ID + " = '"+restaurantId+"'");
        return true;
    }

    public int incrementDescrimentFromCart(Products products) {

        Products products1 = getSingleUserInfo(products.getId());
        ContentValues contentValues = new ContentValues();

        if (products1 != null) {


            double subTotal = 00.00;
            //sub total
            double showed_price = 00.00;
            double quntity = 00.00;
            ArrayList<SubOptions> optionsArrayList = new ArrayList<>();
            double selectedProductValue = 00.00;
            Gson gson = new Gson();
            Type userListType = new TypeToken<ArrayList<SubOptions>>() {
            }.getType();

            //this code add selected customization price to showed price
            optionsArrayList = gson.fromJson(products.getSelectedOptions(), userListType);
            if (optionsArrayList != null) {
                for (SubOptions subOptions : optionsArrayList) {
                    selectedProductValue = selectedProductValue + Double.parseDouble(subOptions.getPrice());
                }
            }

            if (products.getPrice_showed() != null && !products.getPrice_showed().equals("null") && !products.getPrice_showed().equals("")) {
                //showed_price = Double.parseDouble(products.getPrice_showed());
                showed_price = Double.parseDouble(products.getPrice());
                quntity = Double.parseDouble(products.getItem_count());
                subTotal = (showed_price + selectedProductValue)*quntity;
                products.setSubTotal(String.valueOf(subTotal));
            }


            //total tex
            double totaltxt = 00.00;
            double serviceChange = 00.00;
            double packChange = 00.00;

            if (products.getSubTotal() != null && !products.getSubTotal().equals("null") && !products.getSubTotal().equals("")) {
                subTotal = Double.parseDouble(products.getSubTotal());
            }
            if (products.getPackaging_charges() != null && !products.getPackaging_charges().equals("null") && !products.getPackaging_charges().equals("")) {
                packChange = Double.parseDouble(products.getPackaging_charges());
            }
            if (products.getTotalServiceCharge() != null && !products.getTotalServiceCharge().equals("null") && !products.getTotalServiceCharge().equals("")) {
                serviceChange = Double.parseDouble(products.getTotalServiceCharge());
            }

            totaltxt = (serviceChange + packChange + subTotal);
            if (products.getTotalServiceCharge() != null && !products.getTotalServiceCharge().equals("") && !products.getTotalServiceCharge().equals("null") && Double.compare(Double.parseDouble(products.getTotalServiceCharge()), 0.00) != 0) {
                totaltxt = (totaltxt * Double.parseDouble(products.getTex_rate())) / 100;
            } else {
                totaltxt = (totaltxt * Double.parseDouble(products1.getTex_rate())) / 100;

            }
            products.setProduct_Totaltxt(String.valueOf(totaltxt));


            //grand total
            double grandTotal = 00.00;
            grandTotal = serviceChange + totaltxt + packChange + subTotal;
            products.setGrandTotal(String.valueOf(grandTotal));

           *//* Log.e("getSubTotal", products.getSubTotal());
            Log.e("getItem_count", products.getItem_count());
            Log.e("getPackaging_charges", products.getPackaging_charges());
            Log.e("getTotalServiceCharge", products.getTotalServiceCharge());
            Log.e("getProduct_Totaltxt", products.getProduct_Totaltxt());
            Log.e("GrandTotal", products.getGrandTotal());*//*
        }
        contentValues.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());
        contentValues.put(DatabaseHelper.ITEM_QUANTITY, products.getItem_count());
        contentValues.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValues.put(DatabaseHelper.TOTAL_TEX, products.getProduct_Totaltxt());

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + products.getTable_index(), null);
        return i;
    }

    public long repeatPreviousOrder(MyOrderProduct products) {

        Gson gson = new Gson();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.PRODUCT_ID, products.getProductId());
        contentValue.put(DatabaseHelper.ITEM_NAME, products.getName());
        contentValue.put(DatabaseHelper.ITEM_PRICE, products.getPrice());
        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_VALUE, products.getDiscountValue());
        if (products.getCustomization()!=null)
         contentValue.put(DatabaseHelper.ITEM_CUSTOMIZATION, gson.toJson(products.getCustomization()));
        else
        contentValue.put(DatabaseHelper.ITEM_CUSTOMIZATION, "");

        contentValue.put(DatabaseHelper.ITEM_DISCOUNT_UNIT, products.getDiscountUnit());
        contentValue.put(DatabaseHelper.ITEM_PACKAGINCAHARGES, products.getPackagingCharges());
        contentValue.put(DatabaseHelper.ITEM_PICTURE, products.getPicture());

        if (products.getTaxRate() != null && !products.getTaxRate().equals("") && !products.getTaxRate().equals("null") && !products.getTaxRate().equals("0")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getTaxRate());
        } else if (products.getBusiness().gettax_rate() != null && !products.getBusiness().gettax_rate().equals("") && !products.getBusiness().gettax_rate().equals("null") && !products.getBusiness().gettax_rate().equals("0")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getBusiness().gettax_rate());
        } else {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, "00.00");
        }

        contentValue.put(DatabaseHelper.ITEM_FOOD_TYPE, gson.toJson(products.getFoodType()));
        contentValue.put(DatabaseHelper.ITEM_DESC, products.getDescription());
        contentValue.put(DatabaseHelper.ITEM_QUANTITY, products.getQuantity());
        contentValue.put(DatabaseHelper.SELECTION_CUSTOMIZATION, gson.toJson(products.getMyOrderSubOptions()));
        contentValue.put(DatabaseHelper.RESTURANT_PACKAGING_CHARGES, products.getBusiness().getPackagingCharges());
        contentValue.put(DatabaseHelper.RESTAURANT_ID, products.getBusiness().getId());
        contentValue.put(DatabaseHelper.RESTAURANT_NAME, products.getBusiness().getName());

        if (products.getBusiness().getarea() != null && products.getBusiness().getarea().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getBusiness().getarea());
        } else if (products.getBusiness().getAddress() != null && products.getBusiness().getAddress().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getBusiness().getAddress());
        } else {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, "");
        }
        contentValue.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValue.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());
        contentValue.put(DatabaseHelper.TOTAL_SERVICE_CHARGE, products.getBusiness().getServiceCharges());
        contentValue.put(DatabaseHelper.TOTAL_TEX, products.getTax());
        contentValue.put(DatabaseHelper.DELIVARYFEE, products.getDeliveryCharges());

        contentValue.put(DatabaseHelper.MAXQUANTITY, products.getMaxQuantity());

        contentValue.put(DatabaseHelper.IS_JFU, products.getBusiness().isJfu());



        return database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }*/

    /*
    public void insert(Products products) {

        Gson gson = new Gson();
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.ITEM_NAME, products.getName());
        contentValue.put(DatabaseHelper.ITEM_PRICE, products.getPrice());

         contentValue.put(DatabaseHelper.ITEM_CUSTOMIZATION, gson.toJson(products.getCustomizations()));
         contentValue.put(DatabaseHelper.ITEM_PACKAGINCAHARGES, products.getPackaging_charges());

        contentValue.put(DatabaseHelper.ITEM_PICTURE, products.getPicture());

        if (products.getTex_rate() != null && !products.getTex_rate().equals("") && !products.getTex_rate().equals("null") && !products.getTex_rate().equals("0")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getTex_rate());

        }
        else if (products.getData().getTaxRate() != null && !products.getData().getTaxRate().equals("") && !products.getData().getTaxRate().equals("null") && !products.getData().getTaxRate().equals("0")) {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, products.getData().getTaxRate());

        }
        else {
            contentValue.put(DatabaseHelper.ITEM_TEX_RATE, "00.00");
        }

        contentValue.put(DatabaseHelper.ITEM_FOOD_TYPE, gson.toJson(products.getFood_type()));
        contentValue.put(DatabaseHelper.ITEM_DESC, products.getDescription());
        contentValue.put(DatabaseHelper.ITEM_QUANTITY, products.getItem_count());
        contentValue.put(DatabaseHelper.SELECTION_CUSTOMIZATION, products.getSelectedOptions());
        contentValue.put(DatabaseHelper.RESTURANT_PACKAGING_CHARGES, products.getData().getPackagingCharges());
        contentValue.put(DatabaseHelper.RESTAURANT_ID, products.getData().getId());
        contentValue.put(DatabaseHelper.RESTAURANT_NAME, products.getData().getName());
        if (products.getData().getArea() != null && products.getData().getArea().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getData().getArea());
        } else if (products.getData().getAddress() != null && products.getData().getAddress().length() != 0) {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, products.getData().getAddress());
        } else {
            contentValue.put(DatabaseHelper.RESTUARANT_AREA, "");
        }
        contentValue.put(DatabaseHelper.SUBTOTAL, products.getSubTotal());
        contentValue.put(DatabaseHelper.GRAND_TOTAL, products.getGrandTotal());
        contentValue.put(DatabaseHelper.TOTAL_SERVICE_CHARGE, products.getData().getServiceCharges());
        contentValue.put(DatabaseHelper.TOTAL_TEX, products.getProduct_Totaltxt());
        Log.e("totaltxt DB--", products.getProduct_Totaltxt());
        contentValue.put(DatabaseHelper.DELIVARYFEE, products.getData().getDeliveryCharge());
        contentValue.put(DatabaseHelper.MAXQUANTITY, products.getMax_quantity());
        contentValue.put(DatabaseHelper.IS_JFU, products.isJFu());
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }*/


}