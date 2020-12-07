package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.DownloadFavoriteItemList;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityRazorPayBinding;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RazorPayActivity extends BaseActivity implements PaymentResultWithDataListener,alertListenerCallback {
    Activity act;
    Button pay;
    private ActivityRazorPayBinding binding;
    String sliderItem;
    SliderItem sliderItemList;
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

        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sliderItem = getIntent().getStringExtra("AmountText");
        binding.amount.setText("INR " +sliderItem+".00");

        pay=(Button)findViewById(R.id.btn_pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateOrderID();
            }
        });
    }
//generat orderid

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
                    Toast.makeText(act, "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(act,""+ResponseHandler.getString(ResponseHandler.createJsonObject(response), "message"),Toast.LENGTH_LONG).show();
                  //  Utility.showAlert(act, ResponseHandler.getString(ResponseHandler.createJsonObject(response), "message"), "Error");
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
                hashMap.put("currency", "INR");

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
            options.put("prefill.contact",preafManager.getActiveBrand().getPhonenumber());
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
                //paymentSuccess();
                JSONObject jsonObject=ResponseHandler.createJsonObject(response);
                Utility.showAlert(act, ResponseHandler.getString(jsonObject, "message"), "Success");
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
                hashMap.put("brand",preafManager.getActiveBrand().getId());
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
        onBackPressed();
    }

//    private void paymentSuccess() {
//        DialogPaymentSubscriptionSuccessBinding helpDialog = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_payment_subscription_success, null, false);
//        AlertDialog.Builder builder = new AlertDialog.Builder(act);
//        builder.setView(helpDialog.getRoot());
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.setContentView(helpDialog.getRoot());
//
//        // helpDialog.text1.setText("Are you sure to\nleave exam");
//
//        if (examTypeModel == null) {
//            if (subjectModel != null) {
//
//                //helpDialog.text1.setText("Are you sure to\nleave exam");
//            } else if (testSeries != null) {
//                // helpDialog.text1.setText("Are you sure to\nleave exam");
//            } else {
//                // helpDialog.text1.setText("Are you sure to\nleave exam");
//            }
//        }  // helpDialog.text1.setText("Are you sure to\nleave exam");
//
//        if (examTypeModel != null) {
//            //Utility.printHtmlText(examTypeModel.getName(), helpDialog.title);
//            helpDialog.title.setText(Utility.convertFirstUpperWord(examTypeModel.getName()));
//
//        } else if (subjectModel != null) {
//            //Utility.printHtmlText(subjectModel.getName(), helpDialog.title);
//            helpDialog.title.setText(Utility.convertFirstUpperWord(subjectModel.getName()));
//
//        } else if (testSeries != null) {
//            Utility.printHtmlText(testSeries.getName(), helpDialog.title);
//            binding.titleTxt.setText(Utility.convertFirstUpperWord(testSeries.getName()));
//        } else if (bookModel != null) {
//            Utility.printHtmlText(bookModel.getTitle(), helpDialog.title);
//            helpDialog.title.setText(Utility.convertFirstUpperWord(bookModel.getTitle()));
//        }
//
//        helpDialog.saveNNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                onBackPressed();
//            }
//        });
//
//        alertDialog.show();
//    }
//
//    private void paymentFails(String msg) {
//        DialogPaymentFailsBinding helpDialog = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_payment_fails, null, false);
//        AlertDialog.Builder builder = new AlertDialog.Builder(act);
//        builder.setView(helpDialog.getRoot());
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.setContentView(helpDialog.getRoot());
//
//        // helpDialog.text1.setText("Are you sure to\nleave exam");
//
//        if (examTypeModel == null) {
//            if (subjectModel != null) {
//
//                //helpDialog.text1.setText("Are you sure to\nleave exam");
//            } else if (testSeries != null) {
//                // helpDialog.text1.setText("Are you sure to\nleave exam");
//            } else {
//                // helpDialog.text1.setText("Are you sure to\nleave exam");
//            }
//        }  // helpDialog.text1.setText("Are you sure to\nleave exam");
//
//        // helpDialog.text1.setText(msg);
//        if (examTypeModel != null) {
//
//            Utility.printHtmlText(examTypeModel.getName(), helpDialog.title);
//            helpDialog.title.setText(Utility.convertFirstUpperWord(examTypeModel.getName()));
//        } else if (subjectModel != null) {
//            Utility.printHtmlText(subjectModel.getName(), helpDialog.title);
//            helpDialog.title.setText(Utility.convertFirstUpperWord(subjectModel.getName()));
//        } else if (testSeries != null) {
//            Utility.printHtmlText(testSeries.getName(), helpDialog.title);
//            helpDialog.title.setText(Utility.convertFirstUpperWord(testSeries.getName()));
//
//        } else if (bookModel != null) {
//            Utility.printHtmlText(bookModel.getTitle(), helpDialog.title);
//            helpDialog.title.setText(Utility.convertFirstUpperWord(bookModel.getTitle()));
//
//        }
//
//        helpDialog.saveNNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//
//            }
//        });
//
//        alertDialog.show();
//    }


    @Override
    public void onBackPressed() {CodeReUse.activityBackPress(act);
    }
}