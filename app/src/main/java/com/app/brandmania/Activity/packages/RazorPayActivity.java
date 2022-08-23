package com.app.brandmania.Activity.packages;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.BuildConfig;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MySingleton;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityRazorPayBinding;
import com.app.brandmania.databinding.ItemServiceLayoutBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class RazorPayActivity extends BaseActivity implements PaymentResultWithDataListener, alertListenerCallback {
    Activity act;
    Button pay;
    public static int BUSINESS_TYPE = 1;
    private String BusinessTitle;
    private ActivityRazorPayBinding binding;
    String calculateAmount;
    SliderItem sliderItemList;
    BrandListItem brandListItem;
    private String amountToPay;
    Gson gson;
    public String discount;
    public String type;
    public String code;
    public String code_type;
    public String discounted_amount;
    public String total_amount;
    public String brand_id;
    public String package_id;
    public String promocode;
    public static String CouponCode = "#brandmania25";
    public static String codetype = "per";
    private boolean isLoading = false;
    private String orderIdStr;
    private String paymentIdStr;
    private String signatureStr;
    private String payment_id;
    private String generatedOrderId;
    private String currency = "INR";
    PreafManager preafManager;
    private RazorPayActivity razorPayActivity;
    private BottomSheetDialog bottomSheetDialog;
    private BrandListItem selectedBrand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_razor_pay);
        Checkout.preload(getApplicationContext());
        razorPayActivity = this;
        preafManager = new PreafManager(this);
        gson = new Gson();
        sliderItemList = gson.fromJson(getIntent().getStringExtra("detailsObj"), SliderItem.class);
        selectedBrand = gson.fromJson(getIntent().getStringExtra("BrandListItem"), BrandListItem.class);
        Gson gson = new Gson();

        binding.BackButtonMember.setOnClickListener(v -> onBackPressed());
        calculateAmount = sliderItemList.getPriceForPay();
        //createPayment();
        binding.proceedToPayment.setOnClickListener(v -> generateOrderID());

        showReferrer();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (sliderItemList != null) {

            binding.packageNameTxt.setText(sliderItemList.getPackageTitle());
            binding.durationTxt.setText(sliderItemList.getDuration());
            if (sliderItemList.getSlideSubItems().size() != 0) {
                String description = sliderItemList.getSlideSubItems().get(0).getDescription();
                String[] serviveArray = description.split(",");
                for (String s : serviveArray) {
                    addDynamicServices(s);
                }
            }

            binding.actualPriceTxt.setText(act.getString(R.string.Rs) + sliderItemList.getPriceForPay());
            calculateAmount = sliderItemList.getPriceForPay();

            if (selectedBrand != null && !selectedBrand.getIs_payment_pending().isEmpty()
                    && selectedBrand.getIs_payment_pending().equalsIgnoreCase("0")
                    && Utility.monthsBetweenDates(selectedBrand.getSubscriptionDate()) < 1) {

                int actualPrice = Integer.parseInt(sliderItemList.getPriceForPay());
                int previousPackagePrice = Integer.parseInt(preafManager.getActiveBrand().getRate());
                if (actualPrice > previousPackagePrice) {
                    int countedPrice = actualPrice - previousPackagePrice;
                    calculateAmount = String.valueOf(countedPrice);
                    binding.discountedAmountLayout.setVisibility(View.GONE);
                    binding.prevAmount.setText(preafManager.getActiveBrand().getPackagename());
                    binding.prevAmount.setText(act.getString(R.string.Rs) + preafManager.getActiveBrand().getRate());
                    binding.previousLayout.setVisibility(View.VISIBLE);
                    binding.noticeTxt.setVisibility(View.VISIBLE);
                    //- and rs icon with red colour
                    binding.noticeTxt.setText("Your currently active package is \"" + preafManager.getActiveBrand().getPackagename() + "\". so your previous paid amount will be deducted. As It was purchased within one month");
                }
            }
            binding.finalAmountTxt.setText(act.getString(R.string.Rs) + calculateAmount);
        }

        binding.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(binding.promoCodeTxt.getText().toString());
            }
        });
        binding.cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAmount = sliderItemList.getPriceForPay();
                code = null;
                if (selectedBrand.getSubscriptionDate() != null
                        && !selectedBrand.getSubscriptionDate().isEmpty() &&
                        Utility.monthsBetweenDates(selectedBrand.getSubscriptionDate()) < 1) {

                    int actualPrice = Integer.parseInt(sliderItemList.getPriceForPay());
                    int previousPackagePrice = Integer.parseInt(preafManager.getActiveBrand().getRate());
                    if (actualPrice > previousPackagePrice) {
                        int countedPrice = actualPrice - previousPackagePrice;
                        calculateAmount = String.valueOf(countedPrice);
                        binding.discountedAmountLayout.setVisibility(View.GONE);
                        binding.prevAmount.setText(preafManager.getActiveBrand().getPackagename());
                        binding.prevAmount.setText(act.getString(R.string.Rs) + preafManager.getActiveBrand().getRate());
                        binding.previousLayout.setVisibility(View.VISIBLE);
                        binding.noticeTxt.setVisibility(View.VISIBLE);
                        //- and rs icon with red colpr
                        binding.noticeTxt.setText("Your currently active package is \"" + preafManager.getActiveBrand().getPackagename() + "\". so your previous paid amount will be deducted. As It was purchased within one month");
                    }
                }

                binding.finalAmountTxt.setText(act.getString(R.string.Rs) + calculateAmount);
                binding.applypromoEditTxt.setVisibility(View.GONE);
                binding.discountLayout.setVisibility(View.GONE);
                binding.promoEditTxt.setVisibility(View.VISIBLE);
            }
        });

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadQrCode();
            }
        });
    }

    /*   @Override
       public void update(Observable observable, Object data) {

           if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.REFRESH_BRAND_NAME) {
               getBrandList();
           }

       }*/
    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);

        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myBrandApp.getObserver().getValue() == Constant.SELECTEDREFFERCODE) {
                    binding.promoCodeTxt.setText(preafManager.getReferrerCode());
                }
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    File new_file;
    File imagePath;

    public void downloadQrCode() {
        Utility.showLoadingTran(act);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (manuallyEnablePermission()) {
                HELPER._INIT_FOLDER(Constant.ROOT);
                HELPER._INIT_FOLDER(Constant.DATA);
                HELPER._INIT_FOLDER(Constant.IMAGES);
                Bitmap ImageDrawable;
                ImageDrawable = getResizedBitmap(((BitmapDrawable) (BitmapDrawable) binding.qrCodeImg.getDrawable()).getBitmap(), 1280, 908);
                FileOutputStream fileOutputStream;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
                String date = simpleDateFormat.format(new Date());
                String name = "BM-QRCode" + System.currentTimeMillis() + ".jpg";
                String file_name = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constant.ROOT + "/" + Constant.IMAGES + "/" + name;
                new_file = new File(file_name);
                imagePath = new File(act.getCacheDir(), file_name);
                Utility.Log("ImagePAth:::", new_file);
                Utility.Log("ImagePAth:::", imagePath);

                try {
                    fileOutputStream = new FileOutputStream(new_file);
                    ImageDrawable.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                triggerShareIntent(new_file);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CodeReUse.ASK_PERMISSSION: {

                if (grantResults.length > 0) {
                    boolean cameraGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageGrant = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readStorageGrant = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (cameraGrant && writeStorageGrant && readStorageGrant) {
                        //downloadQrCode();
                        Utility.Log("Permisstion Granted", "Yesssss");
                    }
                } else {
                    Toast.makeText(act, "You need to allow permission for better performance", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean manuallyEnablePermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                new android.app.AlertDialog.Builder(act)
                        .setMessage("Allow BrandMania to access photos, files to download and share images ")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", act.getPackageName(), null)));
                            }
                        })
                        .show();
                return false;
            } else {
                return true;
            }

        } else {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                new android.app.AlertDialog.Builder(act)
                        .setMessage("Allow BrandMania to access photos, files to download and share images ")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                ActivityCompat.requestPermissions(act,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE},
                                        CodeReUse.ASK_PERMISSSION);
                            }
                        })
                        .show();
                return false;
            } else {
                return true;
            }
        }
    }


    private void requestAgain() {
        ActivityCompat.requestPermissions(act,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                CodeReUse.ASK_PERMISSSION);
    }

    //fire intent for share
    public void triggerShareIntent(File new_file) {

        Utility.dismissLoadingTran();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        List<ResolveInfo> resInfoList = act.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            act.grantUriPermission(packageName, Uri.fromFile(new_file), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri = Uri.fromFile(new_file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new_file));
        shareIntent.setDataAndType(uri, "image/*");
        shareIntent.setClipData(ClipData.newRawUri("", uri));
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(Intent.createChooser(shareIntent, "Share Image to.."));
        Toast.makeText(act, "QR code saved to your gallery", Toast.LENGTH_SHORT).show();

    }

    public void showReferrer() {

        if (preafManager.getSpleshReferrer() != null && !preafManager.getSpleshReferrer().isEmpty()) {
            verifyCode(preafManager.getSpleshReferrer());
        }
    }

    private void createPayment() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showLoadingTran(act);
        Utility.Log("APi", APIs.CREATE_PAYMENT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.CREATE_PAYMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissLoadingTran();
                Utility.Log("Payment_ID ; ", response);
                if (ResponseHandler.isSuccess(response, null)) {
                    JSONObject jsonObject = ResponseHandler.getJSONObject(ResponseHandler.createJsonObject(response), "data");
                    payment_id = (ResponseHandler.getString(jsonObject, "id"));

                } else {
                    Utility.showSnackBar(binding.rootBackground, act, ResponseHandler.getString(ResponseHandler.createJsonObject(response), "message"));
                }
            }
        }, error -> {
            isLoading = false;
            Utility.dismissLoadingTran();

            Utility.showSnackBar(binding.rootBackground, act, "There is something internal problem");

            error.printStackTrace();
        }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() {
                Utility.Log("Header", getHeader(CodeReUse.GET_JSON_HEADER).toString());
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("X-Authorization", "Bearer " + preafManager.getUserToken());

                return hashMap;

            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("brand", sliderItemList.getBrandId());
                hashMap.put("package", sliderItemList.getPackageid());
                Utility.Log("Param", hashMap.toString());
                return hashMap;
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    boolean isCodeApply = false;

    private void verifyCode(String codedd) {
        if (isCodeApply)
            return;
        isCodeApply = true;
        Utility.showLoadingTran(act);
        Utility.Log("APi", APIs.GET_PROMOCODE_DESCOUNT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_PROMOCODE_DESCOUNT, response -> {
            isCodeApply = false;
            Utility.dismissLoadingTran();

            Utility.Log("PromocodeID ; ", response);

            if (ResponseHandler.isSuccess(response, null)) {
                JSONObject jsonObject = ResponseHandler.getJSONObject(ResponseHandler.createJsonObject(response), "data");
                discounted_amount = ResponseHandler.getString(jsonObject, "discounted_amount");
                discount = ResponseHandler.getString(jsonObject, "discount");
                type = ResponseHandler.getString(jsonObject, "type");
                code = ResponseHandler.getString(jsonObject, "code");
                code_type = ResponseHandler.getString(jsonObject, "code_type");
                total_amount = ResponseHandler.getString(jsonObject, "total_amount");
                brand_id = ResponseHandler.getString(jsonObject, "brand_id");
                package_id = ResponseHandler.getString(jsonObject, "package_id");

                applyCodeCalculation();

            } else {
                Utility.showSnackBar(binding.rootBackground, act, ResponseHandler.getString(ResponseHandler.createJsonObject(response), "message"));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isCodeApply = false;
                Utility.dismissLoadingTran();
                Utility.showSnackBar(binding.rootBackground, act, "There is something internal problem");
                error.printStackTrace();

            }
        }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() {
                Utility.Log("Header", getHeader(CodeReUse.GET_JSON_HEADER).toString());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("X-Authorization", "Bearer " + preafManager.getUserToken());
                return hashMap;

            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("code", codedd);
                hashMap.put("amount", calculateAmount);
                hashMap.put("brand", sliderItemList.getBrandId());
                hashMap.put("package", sliderItemList.getPackageid());
                Utility.Log("Param", hashMap.toString());
                return hashMap;
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    public void applyCodeCalculation() {
        binding.promoEditTxt.setVisibility(View.GONE);
        binding.codeTxt.setText(act.getString(R.string.Rs) + discounted_amount);
        binding.couponCodeTxt.setText("Discount(" + discount + "%" + ")");
        binding.discountLayout.setVisibility(View.VISIBLE);
        binding.applyPromoCodeTxt.setText("Congratesss! You saved(" + act.getString(R.string.Rs) + discounted_amount + ")");
        binding.applysuccesfullyTxt.setText(Html.fromHtml("<font color=\"red\">" + "<b>" + code + "</b>" + "</font>" + "<font color=\"#FFFFFF\"><b> Applied Successfully</b></font>"));
        binding.applypromoEditTxt.setVisibility(View.VISIBLE);
        binding.finalAmountTxt.setText(act.getString(R.string.Rs) + total_amount);
        calculateAmount = total_amount;
        Utility.Log("calculateAmount", calculateAmount);
    }

    private void addDynamicServices(String featuresTxt) {
        ItemServiceLayoutBinding serviceLayoutBinding;
        serviceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.item_service_layout, null, false);
        serviceLayoutBinding.servicesTxt.setText("- " + featuresTxt);
        binding.servicesContainer.addView(serviceLayoutBinding.getRoot());

    }

    public void generateOrderID() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showLoadingTran(act);
        Utility.Log("APi", APIs.GENERATE_ORDER_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GENERATE_ORDER_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissLoadingTran();

                Utility.Log("OrderID ; ", response);

                if (ResponseHandler.isSuccess(response, null)) {
                    JSONObject jsonObject = ResponseHandler.getJSONObject(ResponseHandler.createJsonObject(response), "data");
                    generatedOrderId = ResponseHandler.getString(jsonObject, "orderId");
                    calculateAmount = ResponseHandler.getString(jsonObject, "orderAmount");
                    currency = ResponseHandler.getString(jsonObject, "currency");

                    setUpPaymentMethod();
                } else {
                    Toast.makeText(act, "" + ResponseHandler.getString(ResponseHandler.createJsonObject(response), "message"), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                Utility.dismissLoadingTran();
                Utility.showSnackBar(binding.rootBackground, act, "There is something internal problem");
                error.printStackTrace();
            }
        }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() {
                Utility.Log("Header", getHeader(CodeReUse.GET_JSON_HEADER).toString());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("X-Authorization", "Bearer" + preafManager.getUserToken());
                return hashMap;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                //hashMap.put("amount", sliderItemList.getPriceForPay());
                hashMap.put("amount", calculateAmount);
                //hashMap.put("amount", "1");
                hashMap.put("currency", "INR");
                Utility.Log("Param", hashMap.toString());
                return hashMap;
            }
        };
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    private void setUpPaymentMethod() {

        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.ic_launcher_icon);
        final Activity activity = this;


        try {
            JSONObject options = new JSONObject();
            options.put("name", "Brand Mania");
            //options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("orderId", generatedOrderId);
            options.put("theme.color", "#ad2753");
            options.put("currency", "INR");
            //int amountInPaisa = Integer.parseInt(sliderItem) * 100;
            options.put("amount", String.valueOf(calculateAmount));
            options.put("prefill.email", preafManager.getActiveBrand().getEmail());
            options.put("prefill.contact", "Enter Mobile Number");
            checkout.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            orderIdStr = paymentData.getOrderId();

            paymentIdStr = paymentData.getPaymentId();

            signatureStr = paymentData.getSignature();

            makeSubscription("0");
            preafManager.setReferrerCode(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        makeSubscription("1");
    }

    private void paymentSuccessDiaog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.payment_success, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView element3 = dialogView.findViewById(R.id.element3);
        TextView closeBtn = dialogView.findViewById(R.id.closeBtn);
        element3.setText("Your current package is " + sliderItemList.getPackageTitle());
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                finish();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.show();

    }

    public void makeSubscription(String subscription) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showLoadingTran(act);
        Utility.Log("APi", APIs.MAKE_PAYMENT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.MAKE_PAYMENT, response -> {
            isLoading = false;
            Utility.dismissLoadingTran();
            Utility.Log("Make-subscription", response);

            //{"status":true,"data":"","message":"Subscription Added Successfully."}
            if (ResponseHandler.isSuccess(response, null)) {
                getBrandList();
            } else {
                JSONObject jsonObject = ResponseHandler.createJsonObject(response);
                showAlert(act, ResponseHandler.getString(jsonObject, "message"), "Error");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                Utility.dismissLoadingTran();

                Utility.showSnackBar(binding.rootBackground, act, "There is something internal problem");

                error.printStackTrace();


            }
        }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() {
                Utility.Log("Header", getHeader(CodeReUse.GET_JSON_HEADER).toString());
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("X-Authorization", "Bearer" + preafManager.getUserToken());

                return hashMap;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("is_wallet", "0");
                hashMap.put("brand", sliderItemList.getBrandId());
                hashMap.put("package", sliderItemList.getPackageid());
                hashMap.put("amount", sliderItemList.getPriceForPay());
                //hashMap.put("total_amount",sliderItemList.getPriceForPay());
                hashMap.put("total_amount", calculateAmount);
                hashMap.put("img_counter", sliderItemList.getImageTitle());
                hashMap.put("frame_counter", sliderItemList.getTemplateTitle());
                hashMap.put("is_pending", subscription);
                if (payment_id != null)
                    hashMap.put("create_payment_id", payment_id);

                if (subscription.equals("0")) {
                    hashMap.put("razorpay_payment_id", paymentIdStr);

                    if (signatureStr != null) {
                        hashMap.put("razorpay_signature", signatureStr);
                    }

                }
                hashMap.put("razorpay_order_id", generatedOrderId);
                if (code != null && !code.isEmpty()) {
                    hashMap.put("code", code);
                    hashMap.put("code_type", code_type);
                }
                Utility.Log("Param", hashMap.toString());
                return hashMap;
            }
        };

        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    public void showAlert(Activity act, String msg, String flag) {
        new android.app.AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (code != null) {
                            //means promocode is used

                            applyCodeCalculation();
                        } else {
                            //not used
                            //show for one month count
                            binding.actualPriceTxt.setText(act.getString(R.string.Rs) + sliderItemList.getPriceForPay());
                            calculateAmount = sliderItemList.getPriceForPay();
                            if (selectedBrand.getSubscriptionDate() != null
                                    && !selectedBrand.getSubscriptionDate().isEmpty() &&
                                    Utility.monthsBetweenDates(selectedBrand.getSubscriptionDate()) < 1) {

                                int actualPrice = Integer.parseInt(sliderItemList.getPriceForPay());
                                int previousPackagePrice = Integer.parseInt(preafManager.getActiveBrand().getRate());
                                if (actualPrice > previousPackagePrice) {
                                    int countedPrice = actualPrice - previousPackagePrice;
                                    calculateAmount = String.valueOf(countedPrice);
                                    binding.discountedAmountLayout.setVisibility(View.GONE);
                                    binding.prevAmount.setText(preafManager.getActiveBrand().getPackagename());
                                    binding.prevAmount.setText(act.getString(R.string.Rs) + preafManager.getActiveBrand().getRate());
                                    binding.previousLayout.setVisibility(View.VISIBLE);
                                    binding.noticeTxt.setVisibility(View.VISIBLE);
                                    //- and rs icon with red colpr
                                    binding.noticeTxt.setText("Your currently active package is \"" + preafManager.getActiveBrand().getPackagename() + "\". so your previous paid amount will be deducted. As It was purchased within one month");
                                }
                            }

                            binding.finalAmountTxt.setText(act.getString(R.string.Rs) + calculateAmount);
                        }
                        // ((alertListenerCallback) act).alertListenerClick();
                    }
                })
                .show();
    }

    ArrayList<BrandListItem> multiListItems = new ArrayList<>();

    private void getBrandList() {
        Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissLoadingTran();
                Utility.Log("GET_BRAND : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                    preafManager.setAddBrandList(multiListItems);
                    prefManager.setActiveBrand(multiListItems.get(0));
                    paymentSuccessDiaog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utility.dismissLoadingTran();
                        error.printStackTrace();


                    }
                }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("X-Authorization", "Bearer " + preafManager.getUserToken());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    @Override
    public void alertListenerClick() {
        Intent i = new Intent(act, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        finish();

    }

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
}