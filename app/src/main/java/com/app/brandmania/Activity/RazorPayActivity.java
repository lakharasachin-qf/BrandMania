package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class RazorPayActivity extends BaseActivity implements PaymentResultListener {
    Button pay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_pay);
        Checkout.preload(getApplicationContext());
        pay=(Button)findViewById(R.id.btn_pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpPaymentMethod();
            }
        });
    }

    private void setUpPaymentMethod() {
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_launcher_icon);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Merchant Name");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#ad2753");
            options.put("currency", "INR");
            options.put("amount", "100");//pass amount in currency subunits
            options.put("prefill.email", "khushboo.qf@gmail.com");
            options.put("prefill.contact","8849896537");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),"Payment Succesfull"+ s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Payment Failed"+ s, Toast.LENGTH_LONG).show();
    }
}