package com.app.brandmania.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

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

        binding.amount.setText("INR " +sliderItem+".00");

        pay=(Button)findViewById(R.id.btn_pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateOrderID();
            }
        });
    }

    public void generateOrderID() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.Log("APi", APIs.GENERATE_ORDER_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GENERATE_ORDER_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();

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
                Utility.dismissProgress();

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
                hashMap.put("amount", sliderItemList.getPriceForPay());
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
        Utility.showProgress(act);
        Utility.Log("APi", APIs.MAKE_PAYMENT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.MAKE_PAYMENT, response -> {
            isLoading = false;
            Utility.dismissProgress();
            Utility.Log("Make-subscription", response);

            //{"status":true,"data":"","message":"Subscription Added Successfully."}
            if (ResponseHandler.isSuccess(response, null)) {

                paymentSuccessDiaog();
            } else {
                JSONObject jsonObject=ResponseHandler.createJsonObject(response);
                Utility.showAlert(act, ResponseHandler.getString(jsonObject, "message"), "Error");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                Utility.dismissProgress();

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