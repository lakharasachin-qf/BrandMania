package com.app.brandmania.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.LoginActivity;
import com.app.brandmania.Activity.PackageActivity;
import com.app.brandmania.Activity.RazorPayActivity;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.databinding.ItemNotificationLayoutBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.app.brandmania.Activity.UpdateBandList;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ItemLayoutGetbrandlistBinding;
import com.app.brandmania.databinding.ItemLayoutHomeBinding;
import com.app.brandmania.databinding.ItemLayoutViewallimageBinding;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.app.brandmania.Model.BrandListItem.LAYOUT_BRANDLIST;
import static com.app.brandmania.Model.BrandListItem.LAYOUT_BRANDLISTBYID;
import static com.app.brandmania.Model.BrandListItem.LAYOUT_NOTIFICATIONlIST;
import static com.app.brandmania.Model.ImageList.LAYOUT_LOADING;

public class BrandAdapter extends RecyclerView.Adapter {
    private ArrayList<BrandListItem> brandListItems;
    ArrayList<FrameItem> frameItems = new ArrayList<>();
    Activity activity;
    private Gson gson;
    private boolean isLoadingAdded = false;
    PreafManager preafManager;
    private static final int REQUEST_CALL = 1;
    private BRANDBYIDIF brandbyidif;

    public void setBrandbyidif(BRANDBYIDIF brandbyidif) {
        this.brandbyidif = brandbyidif;
    }

    public interface BRANDBYIDIF{
        void fireBrandList(int position,BrandListItem model);
    }

    public BrandAdapter(ArrayList<BrandListItem> brandListItems, Activity activity) {
        this.brandListItems = brandListItems;
        this.activity = activity;
        gson=new Gson();
        preafManager=new PreafManager(activity);
        this.isLoadingAdded = isLoadingAdded;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {

            case LAYOUT_BRANDLIST :
                ItemLayoutGetbrandlistBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_getbrandlist, viewGroup, false);
                return new BrandHolder(layoutBinding);
            case LAYOUT_NOTIFICATIONlIST :
                ItemNotificationLayoutBinding notificationLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_notification_layout, viewGroup, false);
                return new NotificationHolder(notificationLayoutBinding);

            case LAYOUT_BRANDLISTBYID :
                ItemLayoutGetbrandlistBinding layoutBinding1 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_getbrandlist, viewGroup, false);
                return new BrandHolder(layoutBinding1);


        }
        return null;

    }



    @Override
    public int getItemViewType(int position) {
        if (position == brandListItems.size() - 1 && isLoadingAdded)
            return LAYOUT_LOADING;
        switch (brandListItems.get(position).getLayoutType()) {
            case 1:
                return LAYOUT_BRANDLIST;
            case 2:
                return LAYOUT_NOTIFICATIONlIST;
            case 3:
                return LAYOUT_BRANDLISTBYID;
            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return brandListItems.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BrandListItem model = brandListItems.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_BRANDLIST:
                    ((BrandHolder) holder).binding.businessName.setText(model.getName());
                    Log.e("CurrentBrand",model.getId());
                    ((BrandHolder) holder).binding.firsttitle.setText(model.getWebsite());
                    ((BrandHolder) holder).binding.addressText.setText(model.getAddress());
                   // getFrame();
                    Glide.with(activity)
                            .load(model.getLogo())
                            .placeholder(R.drawable.placeholder)
                            .into(((BrandHolder)holder).binding.logo);

                    Log.e("BrandBrandBrandId",preafManager.getActiveBrand().getId());
                    ((BrandHolder)holder).binding.editImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(activity, UpdateBandList.class);
                            i.putExtra("detailsObj", gson.toJson(model));
                            activity.startActivity(i);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                        }
                    });
                    ((BrandHolder)holder).binding.deletImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeletAssigement(model.getId());
                            brandListItems.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, brandListItems.size());
                            Snackbar snackbar1 = Snackbar.make(v, "Brand Deleted!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });

                         if (model.getFrame() != null && model.getFrame().size() != 0) {
                             FrameAddaptor frameAddaptor = new FrameAddaptor(brandListItems.get(position).getFrame(), activity);
                             ((BrandHolder) holder).binding.frameitemLayout.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false));
                             ((BrandHolder) holder).binding.frameitemLayout.setHasFixedSize(true);
                             frameAddaptor.setBrandListItem(brandListItems.get(position));
                             ((BrandHolder) holder).binding.frameitemLayout.setAdapter(frameAddaptor);
                             ((BrandHolder) holder).binding.frameitemLayout.setVisibility(View.VISIBLE);
                             ((BrandHolder) holder).binding.messgaeShow.setVisibility(View.GONE);
                             ((BrandHolder) holder).binding.whatsappImage.setVisibility(View.GONE);
                             ((BrandHolder) holder).binding.contactSupport.setVisibility(View.GONE);
                             ((BrandHolder) holder).binding.callImageImage.setVisibility(View.GONE);
                         }
                         else
                         {
                             ((BrandHolder) holder).binding.frameitemLayout.setVisibility(View.GONE);
                             ((BrandHolder) holder).binding.messgaeShow.setVisibility(View.VISIBLE);
                             ((BrandHolder) holder).binding.whatsappImage.setVisibility(View.VISIBLE);
                             ((BrandHolder) holder).binding.contactSupport.setVisibility(View.VISIBLE);
                             ((BrandHolder) holder).binding.callImageImage.setVisibility(View.VISIBLE);
                             ((BrandHolder) holder).binding.messgaeShow.setText("Your Business is in Verification mode , We will update your frame soon");

                             String Text = "Contact Support:";
                             String ColorNumber = "<font color='#FF0000'>8460638464</font>";
                             String ColorCodeNumber = "<font color='#FF0000'>+91</font>";
                             ((BrandHolder) holder).binding.contactSupport.setText(Text);

                             ((BrandHolder)holder).binding.callImageImage.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     makePhoneCall();
                                    // try {


//                                         String number ="8460638464";
//                                         Intent callIntent = new Intent(Intent.ACTION_CALL);
//                                         callIntent.setData(Uri.parse("tel:" + number));
//                                         if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                             // TODO: Consider calling
//                                             //    ActivityCompat#requestPermissions
//                                             // here to request the missing permissions, and then overriding
//                                             //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                             //                                          int[] grantResults)
//                                             // to handle the case where the user grants the permission. See the documentation
//                                             // for ActivityCompat#requestPermissions for more details.
//                                             return;
//                                         }
//                                         activity.startActivity(callIntent);
//                                     } catch (Exception e) {
//                                         e.printStackTrace();
//                                     }
                                 }
                             });
                             ((BrandHolder)holder).binding.whatsappImage.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     try {
                                         String number ="8460638464";
                                         String BrandContact="\nRegistered Number: ";
                                         String text = "Hello *BrandMania* ,  \n" + "this is request to add  *Frame* For BrandName:"+ ((BrandHolder)holder).binding.businessName.getText().toString() +BrandContact+preafManager.getMobileNumber();
                                         String toNumber ="91"+number;
                                         Intent intent = new Intent(Intent.ACTION_VIEW);
                                         intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
                                         activity.startActivity(intent);
                                     } catch (Exception e) {
                                         e.printStackTrace();
                                     }
                                 }
                             });






















                         }

                    if (brandListItems.get(position).getIs_frame().equals("1")){
                        ((BrandHolder)holder).binding.warning.setText("View your Current Package");
                        ((BrandHolder)holder).binding.warning.setTextColor(Color.parseColor("#4BB543"));
                        ((BrandHolder)holder).binding.selectPlane.setVisibility(View.GONE);
                        ((BrandHolder)holder).binding.makePayment.setVisibility(View.GONE);
                        ((BrandHolder)holder).binding.warning.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Create an alert builder
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                // set the custom layout
                                final View customLayout = activity.getLayoutInflater().inflate(R.layout.package_detail_alert_dialog, null);
                                TextView packageName=customLayout.findViewById(R.id.packageName);
                                TextView totalImage=customLayout.findViewById(R.id.totalImage);
                                TextView usedImage=customLayout.findViewById(R.id.usedImage);
                                TextView remainingImage=customLayout.findViewById(R.id.remainingImage);
                                TextView expirydate=customLayout.findViewById(R.id.expieryDateName);
                                ImageView closed=customLayout.findViewById(R.id.CloseImg);
                                packageName.setText(brandListItems.get(position).getPackagename());
                                totalImage.setText(brandListItems.get(position).getNo_of_total_image());
                                usedImage.setText(brandListItems.get(position).getNo_of_used_image());
                                remainingImage.setText(brandListItems.get(position).getNo_of_remaining());
                                expirydate.setText(brandListItems.get(position).getExpiery_date());
                                builder.setView(customLayout);


                                AlertDialog dialog
                                        = builder.create();
                                dialog.getWindow().setBackgroundDrawableResource(R.color.colorNavText);
                                dialog.setCancelable(false);
                                dialog.show();
                                closed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                pbutton.setBackgroundColor(Color.WHITE);
                            }
                        });

                         if (brandListItems.get(position).getPackagename().equals(""))
                        {
                            ((BrandHolder)holder).binding.makePayment.setVisibility(View.GONE);
                            ((BrandHolder)holder).binding.warning.setText(" You have't selected any plan yet!");
                            ((BrandHolder)holder).binding.warning.setTextColor(Color.RED);
                            ((BrandHolder)holder).binding.selectPlane.setVisibility(View.VISIBLE);
                            ((BrandHolder)holder).binding.warning.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            ((BrandHolder)holder).binding.selectPlane.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e("BrandIDIDIDIDIDI",preafManager.getActiveBrand().getId());
                                    Intent i = new Intent(activity, PackageActivity.class);
                                    i.putExtra("fromBrandList","v");
                                    i.putExtra("detailsObj",gson.toJson(brandListItems.get(position)));
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(i);
                                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                                }
                            });
                        }
                        else if (brandListItems.get(position).getIs_payment_pending().equals("1"))
                        {
                            ((BrandHolder)holder).binding.makePaymentView.setVisibility(View.VISIBLE);
                            ((BrandHolder)holder).binding.view.setVisibility(View.GONE);
                            ((BrandHolder)holder).binding.selectPlane.setVisibility(View.GONE);
                            ((BrandHolder)holder).binding.makePayment.setVisibility(View.VISIBLE);
                            ((BrandHolder)holder).binding.warning.setText(brandListItems.get(position).getPayment_message());
                            ((BrandHolder)holder).binding.warning.setTextColor(Color.RED);
                            ((BrandHolder)holder).binding.makePayment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    brandbyidif.fireBrandList(position,brandListItems.get(position));
                                }
                            });
                            ((BrandHolder)holder).binding.warning.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });

                        }

                    }
                    else{
                        ((BrandHolder)holder).binding.makePayment.setVisibility(View.GONE);
//                        ((BrandHolder)holder).binding.warning.setText(brandListItems.get(position).getFrame_message());
                        ((BrandHolder)holder).binding.warning.setText(" You have't selected any plan yet!");
                        ((BrandHolder)holder).binding.warning.setTextColor(Color.RED);
                        ((BrandHolder)holder).binding.selectPlane.setVisibility(View.VISIBLE);
                        ((BrandHolder)holder).binding.selectPlane.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                 Intent i = new Intent(activity, PackageActivity.class);
                                 i.putExtra("detailsObj",gson.toJson(brandListItems.get(position)));
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(i);
                                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                        });
                        ((BrandHolder)holder).binding.warning.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }

                         break;
                case LAYOUT_NOTIFICATIONlIST:
                    ((NotificationHolder) holder).binding.messgae.setText(model.getMessage());
                    Log.e("CurrentBrand",model.getId());
                    ((NotificationHolder) holder).binding.date.setText(model.getDate());
                    ((NotificationHolder) holder).binding.time.setText(model.getTime());
            }

        }


    }

    static class BrandHolder extends RecyclerView.ViewHolder {
        ItemLayoutGetbrandlistBinding binding;

        BrandHolder(ItemLayoutGetbrandlistBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class NotificationHolder extends RecyclerView.ViewHolder {
        ItemNotificationLayoutBinding binding;

        NotificationHolder(ItemNotificationLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    private void makePhoneCall() {
        String number ="8460638464";
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }



    private void DeletAssigement(final String BrandId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.DELETE_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("hello", response);
                Log.e("BrandBrandBrandId",preafManager.getActiveBrand().getId());
                try {
                    JSONObject object = new JSONObject(response);
                    Log.e("BrandBrandBrandId",preafManager.getActiveBrand().getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
               // params.put("Content-Type", "application/json");
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id",BrandId);
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }
        };

        //Adding request to request queue
        //AppController.getInstance().addToRequestQueue(stringRequest, tag String req);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(stringRequest);

    }

}

