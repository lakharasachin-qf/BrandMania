package com.make.mybrand.Adapter;

import static com.make.mybrand.Model.BrandListItem.LAYOUT_BRANDLIST;
import static com.make.mybrand.Model.BrandListItem.LAYOUT_BRANDLISTBYID;
import static com.make.mybrand.Model.BrandListItem.LAYOUT_NOTIFICATIONlIST;
import static com.make.mybrand.Model.ImageList.LAYOUT_LOADING;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Activity.brand.UpdateBandList;
import com.make.mybrand.Activity.packages.PackageActivity;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.Model.FrameItem;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ItemLayoutGetbrandlistBinding;
import com.make.mybrand.databinding.ItemNotificationLayoutBinding;
import com.make.mybrand.databinding.PackageDetailAlertDialogBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.Utility;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public interface BRANDBYIDIF {
        void fireBrandList(int position, BrandListItem model);
    }

    public BrandAdapter(ArrayList<BrandListItem> brandListItems, Activity activity) {
        this.brandListItems = brandListItems;
        this.activity = activity;
        gson = new Gson();
        preafManager = new PreafManager(activity);
        this.isLoadingAdded = isLoadingAdded;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {

            case LAYOUT_BRANDLIST:

            case LAYOUT_BRANDLISTBYID:
                ItemLayoutGetbrandlistBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_getbrandlist, viewGroup, false);
                return new BrandHolder(layoutBinding);
            case LAYOUT_NOTIFICATIONlIST:
                ItemNotificationLayoutBinding notificationLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_notification_layout, viewGroup, false);
                return new NotificationHolder(notificationLayoutBinding);


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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final BrandListItem model = brandListItems.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_BRANDLIST:
                    ((BrandHolder) holder).binding.businessName.setText(model.getName());
                    ((BrandHolder) holder).binding.firsttitle.setText(model.getWebsite());
                    ((BrandHolder) holder).binding.addressText.setText(model.getAddress());
                    ((BrandHolder) holder).binding.brandService.setText(model.getBrandService());
                    // getFrame();
                    Glide.with(activity)
                            .load(model.getLogo())
                            .placeholder(R.drawable.placeholder)
                            .into(((BrandHolder) holder).binding.logo);


                    ((BrandHolder) holder).binding.editImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(activity, UpdateBandList.class);
                            i.putExtra("detailsObj", gson.toJson(model));
                            activity.startActivity(i);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                        }
                    });

                    ((BrandHolder) holder).binding.deletImage.setOnClickListener(new View.OnClickListener() {
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


                    if (brandListItems.get(position).getIs_frame().equalsIgnoreCase("0")) {
                        //payment done
                        //payment done  - isFrame=0, isPayment=0
                        if (brandListItems.get(position).getIs_payment_pending().equalsIgnoreCase("0")) {
                            //((BrandHolder)holder).binding.warning.setText("Please create your frame!!");

                            // brandListItems.get(position).setNo_of_frame("0");

                            if (brandListItems.get(position).getNo_of_frame().equalsIgnoreCase("0")) {
                                Utility.Log("NoOfFrames", "0");
                                ((BrandHolder) holder).binding.warning.setVisibility(View.GONE);
                                ((BrandHolder) holder).binding.contactTxtLayout.setVisibility(View.GONE);
                            } else {
                                ((BrandHolder) holder).binding.warning.setVisibility(View.VISIBLE);
                                ((BrandHolder) holder).binding.warning.setText("Please create your frame!!");
                                ((BrandHolder) holder).binding.warning.setTextColor(Color.RED);
                                ((BrandHolder) holder).binding.contactTxtLayout.setVisibility(View.VISIBLE);
                                ((BrandHolder) holder).binding.whatsappImage.setVisibility(View.VISIBLE);
                            }
                            ((BrandHolder) holder).binding.showImage.setVisibility(View.VISIBLE);
                            ((BrandHolder) holder).binding.selectPlane.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.makePayment.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.showImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showDialog(position);
                                }
                            });
                            ((BrandHolder) holder).binding.contactTxtLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HELPER.WHATSAPP_REDIRECTION(activity, ((BrandHolder) holder).binding.businessName.getText().toString(), preafManager.getMobileNumber());
                                }
                            });
                        }
                        //payment not done
                        //First- isFrame=0,isPending=1
                        else {
                            ((BrandHolder) holder).binding.warning.setVisibility(View.GONE);
                            //((BrandHolder) holder).binding.warning.setText("You haven't selected any package yet!");
                            HELPER.print("FREE_IMAGE-1", preafManager.getAllFreeImage().toString());
                            if (!preafManager.getAllFreeImage()) {
                                ((BrandHolder) holder).binding.selectPlane.setVisibility(View.VISIBLE);
                            } else {
                                ((BrandHolder) holder).binding.selectPlane.setVisibility(View.GONE);
                            }
                            ((BrandHolder) holder).binding.view.setVisibility(View.VISIBLE);
                            ((BrandHolder) holder).binding.showImage.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.makePayment.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.makePaymentView.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.frameitemLayoutRelative.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.contactTxtLayout.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.warning.setTextColor(Color.RED);
                            ((BrandHolder) holder).binding.selectPlane.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(activity, PackageActivity.class);
                                    i.putExtra("fromBrandList", "fromBrandList");
                                    i.putExtra("detailsObj", gson.toJson(brandListItems.get(position)));
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(i);
                                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                                }
                            });
                        }
                    }
                    //frame created
                    else {
                        //payment Done- isFrame=1, isPending=0
                        if (brandListItems.get(position).getIs_payment_pending().equalsIgnoreCase("0")) {
                            ((BrandHolder) holder).binding.showImage.setVisibility(View.VISIBLE);
                            ((BrandHolder) holder).binding.warning.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.contactTxtLayout.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.selectPlane.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.makePayment.setVisibility(View.GONE);

                            ((BrandHolder) holder).binding.showImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showDialog(position);
                                }

                            });

                            if (model.getFrame() != null && model.getFrame().size() != 0) {
                                FrameAddaptor frameAddaptor = new FrameAddaptor(brandListItems.get(position).getFrame(), activity);
                                ((BrandHolder) holder).binding.frameitemLayout.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                                ((BrandHolder) holder).binding.frameitemLayout.setHasFixedSize(true);
                                frameAddaptor.setBrandListItem(brandListItems.get(position));
                                ((BrandHolder) holder).binding.frameitemLayout.setAdapter(frameAddaptor);
                                ((BrandHolder) holder).binding.frameitemLayout.setVisibility(View.VISIBLE);
                                ((BrandHolder) holder).binding.frameitemLayoutRelative.setVisibility(View.VISIBLE);
                            }
                        }
                        //Not payment - isFrame=1, isPendingPayment=1 but create frame

                        else {
                            ((BrandHolder) holder).binding.warning.setVisibility(View.VISIBLE);
                            ((BrandHolder) holder).binding.warning.setText("You haven't selected any package yet!");
                            if (!preafManager.getAllFreeImage()) {
                                ((BrandHolder) holder).binding.selectPlane.setVisibility(View.VISIBLE);
                            } else {
                                ((BrandHolder) holder).binding.selectPlane.setVisibility(View.GONE);
                            }
                            ((BrandHolder) holder).binding.view.setVisibility(View.VISIBLE);
                            ((BrandHolder) holder).binding.showImage.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.makePayment.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.makePaymentView.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.frameitemLayoutRelative.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.contactTxtLayout.setVisibility(View.GONE);
                            ((BrandHolder) holder).binding.warning.setTextColor(Color.RED);
                            ((BrandHolder) holder).binding.selectPlane.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(activity, PackageActivity.class);
                                    i.putExtra("fromBrandList", "1");
                                    i.putExtra("detailsObj", gson.toJson(brandListItems.get(position)));
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(i);
                                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                                }
                            });
                            if (model.getFrame() != null && model.getFrame().size() != 0) {
                                FrameAddaptor frameAddaptor = new FrameAddaptor(brandListItems.get(position).getFrame(), activity);
                                ((BrandHolder) holder).binding.frameitemLayout.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                                ((BrandHolder) holder).binding.frameitemLayout.setHasFixedSize(true);
                                frameAddaptor.setBrandListItem(brandListItems.get(position));
                                ((BrandHolder) holder).binding.frameitemLayout.setAdapter(frameAddaptor);
                                ((BrandHolder) holder).binding.frameitemLayout.setVisibility(View.VISIBLE);
                                ((BrandHolder) holder).binding.frameitemLayoutRelative.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                    if (preafManager.getAllFreeImage()) {
                        ((BrandHolder) holder).binding.msg.setVisibility(View.VISIBLE);
                        ((BrandHolder) holder).binding.msg.setText("As you are free user you can download or share only one image in a day");
                    } else {
                        if (!Utility.isUserPaid(brandListItems.get(position))) {
                            ((BrandHolder) holder).binding.msg.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case LAYOUT_NOTIFICATIONlIST:
                    ((NotificationHolder) holder).binding.messgae.setText(model.getMessage());
                    ((NotificationHolder) holder).binding.date.setText(model.getDate());
                    ((NotificationHolder) holder).binding.time.setText(" " + model.getTime());
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

    public void showDialog(int position) {
        Utility.Log("brandListItems", gson.toJson(brandListItems.get(position)));

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity, R.style.MyAlertDialogStyle_extend2);
        PackageDetailAlertDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.package_detail_alert_dialog, null, false);
        binding.packageName.setText(brandListItems.get(position).getPackagename());
        binding.totalImages.setText("Download Limit: " + brandListItems.get(position).getNo_of_total_image() + " Images");
        binding.usedImages.setText("Used Images: " + brandListItems.get(position).getNo_of_used_image() + " Images");
        binding.expiryDate.setText(brandListItems.get(position).getExpiery_date());
        if (Utility.isPackageExpired(brandListItems.get(position))) {
            binding.alert.setVisibility(View.VISIBLE);
            binding.alert.setText("     Dear user, your package is expired on date " + brandListItems.get(position).getExpiery_date() + ". Please Upgrade your plan.");
            binding.alert.setSelected(true);
        } else {
            binding.alert.setVisibility(View.GONE);
        }
        Utility.Log("Duration", brandListItems.get(position).getDuration());
        binding.price.setText(activity.getString(R.string.Rs) + brandListItems.get(position).getRate());
        binding.duration.setText(brandListItems.get(position).getDuration());
        builder.setView(binding.getRoot());
        androidx.appcompat.app.AlertDialog dialog;
        dialog = builder.create();


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void DeletAssigement(final String BrandId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.DELETE_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("hello", response);
                try {
                    JSONObject object = new JSONObject(response);
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
                params.put("X-Authorization", "Bearer " + preafManager.getUserToken());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id", BrandId);
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
