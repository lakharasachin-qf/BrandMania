package com.app.brandmania.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityRazorPayBinding;
import com.app.brandmania.databinding.ItemServiceLayoutBinding;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RazorPayActivity extends BaseActivity implements PaymentResultWithDataListener,alertListenerCallback {
    Activity act;
    Button pay;
    private ActivityRazorPayBinding binding;
    String sliderItem;
    SliderItem sliderItemList;
    BrandListItem brandListItem;
    private String amountToPay;
    Gson gson;
    private boolean isLoading=false;
    private String orderIdStr;
    private String paymentIdStr;
    private String signatureStr;
    private String generatedOrderId;
    private String currency="INR";
    PreafManager preafManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_razor_pay);
        Checkout.preload(getApplicationContext());
        preafManager=new PreafManager(this);
        gson=new Gson();

        sliderItemList=gson.fromJson(getIntent().getStringExtra("detailsObj"), SliderItem.class);
        Gson gson =new Gson();
        Log.e("EEEE",gson.toJson(sliderItemList));


        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sliderItem = sliderItemList.getPriceForPay();

        binding.proceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateOrderID();
            }
        });


        if (sliderItemList != null) {
            binding.actualPriceTxt.setText(act.getString(R.string.Rs) + sliderItemList.getPriceForPay());
            binding.packageNameTxt.setText(sliderItemList.getPackageTitle());
            binding.durationTxt.setText(sliderItemList.getDuration());
            Log.e("Services",new Gson().toJson(sliderItemList.getSlideSubItems()));
            for (int i=0;i<sliderItemList.getSlideSubItems().size();i++){
                addDynamicServices(sliderItemList.getSlideSubItems().get(i).getName());
            }
            addDynamicServices(sliderItemList.getImageTitle()+" Images Download / Year");
            addDynamicServices(act.getString(R.string.Rs)+sliderItemList.getPayTitle()+" / "+sliderItemList.getDuration());

            //show for one month count


            if (Utility.monthsBetweenDates(preafManager.getActiveBrand().getSubscriptionDate())<1){

                int actualPrice=Integer.parseInt(sliderItemList.getPriceForPay());
                int previousPackagePrice=Integer.parseInt(preafManager.getActiveBrand().getRate());
                if (actualPrice>previousPackagePrice) {
                    int countedPrice = actualPrice - previousPackagePrice;
                    sliderItem = String.valueOf(countedPrice);
                    Log.e("Price", preafManager.getActiveBrand().getRate() + " - " + sliderItemList.getPriceForPay());
                    binding.discountedAmountLayout.setVisibility(View.GONE);
                    binding.prevAmount.setText(preafManager.getActiveBrand().getPackagename());
                    binding.prevAmount.setText(act.getString(R.string.Rs) + preafManager.getActiveBrand().getRate());
                    binding.previousLayout.setVisibility(View.VISIBLE);
                    binding.noticeTxt.setVisibility(View.VISIBLE);
                    //- and rs icon with red colpr
                    binding.noticeTxt.setText("Your currently active package is \"" + preafManager.getActiveBrand().getPackagename() + "\". so your previous paid amount is deducted.");
                }
            }
            binding.finalAmountTxt.setText(act.getString(R.string.Rs) +sliderItem);

        }
    }

    private void addDynamicServices(String featuresTxt) {
        ItemServiceLayoutBinding serviceLayoutBinding;
        serviceLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.item_service_layout, null, false);
        serviceLayoutBinding.servicesTxt.setText("- "+featuresTxt);
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
                    Log.e("RoserPay Order Id",generatedOrderId);
                    sliderItem = ResponseHandler.getString(jsonObject, "orderAmount");
                    currency = ResponseHandler.getString(jsonObject, "currency");
                    setUpPaymentMethod();
                } else {
                    Toast.makeText(act,""+ResponseHandler.getString(ResponseHandler.createJsonObject(response), "message"),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                Utility.dismissLoadingTran();

                Utility.showSnackBar(binding.rootBackground, act, "There is something internal problem");

                error.printStackTrace();
         //       String body;
                //get status code here
//                body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                //   Log.e("Error ", body);



            }
        }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() {
                Utility.Log("Header", getHeader(CodeReUse.GET_JSON_HEADER).toString());
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("Authorization","Bearer"+preafManager.getUserToken());

                return hashMap;

            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                //hashMap.put("amount", sliderItemList.getPriceForPay());
                hashMap.put("amount",sliderItem);
                //hashMap.put("amount", "1");
                hashMap.put("currency", "INR");

                Utility.Log("Param", hashMap.toString());
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.getCache().clear();
        queue.add(stringRequest);
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
            options.put("amount", String.valueOf(sliderItem));
            options.put("prefill.email", preafManager.getActiveBrand().getEmail());
            options.put("prefill.contact","Enter Mobile Number");
            Log.e("Param : ", options.toString());
            checkout.open(activity, options);
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            Log.e("Payment Succcessful", gson.toJson(paymentData));
            orderIdStr = paymentData.getOrderId();

            paymentIdStr = paymentData.getPaymentId();

            signatureStr = paymentData.getSignature();

            makeSubscription("0");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.e("Payment Fail",s);
        makeSubscription("1");
    }

    private void paymentSuccessDiaog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.payment_success, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView element3=dialogView.findViewById(R.id.element3);
        TextView closeBtn=dialogView.findViewById(R.id.closeBtn);
        element3.setText("Your current package is "+sliderItemList.getPackageTitle());
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
                JSONObject jsonObject=ResponseHandler.createJsonObject(response);
                Utility.showAlert(act, ResponseHandler.getString(jsonObject, "message"), "Error");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                Utility.dismissLoadingTran();

                Utility.showSnackBar(binding.rootBackground, act, "There is something internal problem");

                error.printStackTrace();
             /*   String body;
                //get status code here
                body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                Log.e("Error ", body);*/


            }
        }) {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() {
                Utility.Log("Header", getHeader(CodeReUse.GET_JSON_HEADER).toString());
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("Authorization","Bearer"+preafManager.getUserToken());

                return hashMap;
            }
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("brand",sliderItemList.getBrandId());
                hashMap.put("package",sliderItemList.getPackageid());
                hashMap.put("amount",sliderItemList.getPriceForPay());
                hashMap.put("total_amount",sliderItemList.getPriceForPay());
                hashMap.put("img_counter",sliderItemList.getImageTitle());
                hashMap.put("frame_counter",sliderItemList.getTemplateTitle());
                hashMap.put("is_pending",subscription);
                if (subscription.equals("0")){
                    hashMap.put("razorpay_payment_id",paymentIdStr);
                    Log.e("razorpay_payment_id",paymentIdStr);


                    if (signatureStr!=null) {
                        hashMap.put("razorpay_signature", signatureStr);
                    }

                }
                hashMap.put("razorpay_order_id",generatedOrderId);
//
              //  razorpay_payment_id, razorpay_order_id, razorpay_signature
                Utility.Log("Param", hashMap.toString());
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.getCache().clear();
        queue.add(stringRequest);
    }
    ArrayList<BrandListItem> multiListItems=new ArrayList<>();
    private void getBrandList() {
        Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            Utility.dismissLoadingTran();
                Utility.Log("GET_BRAND : ", response);
                try {
                    paymentSuccessDiaog();
                    JSONObject jsonObject = new JSONObject(response);

                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                    preafManager.setAddBrandList(multiListItems);
                    for (int i=0;i<multiListItems.size();i++){
                        if (multiListItems.get(i).getId().equalsIgnoreCase(preafManager.getActiveBrand().getId())){
                            preafManager.setActiveBrand(multiListItems.get(i));
                            break;
                        }
                    }
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
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
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
    public void onBackPressed() {CodeReUse.activityBackPress(act);
    }
}