package com.app.brandmania.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.app.brandmania.Model.BrandListItem.LAYOUT_BRANDLIST;
import static com.app.brandmania.Model.ImageList.LAYOUT_LOADING;

public class BrandAdapter extends RecyclerView.Adapter {
    private ArrayList<BrandListItem> brandListItems;
    ArrayList<FrameItem> frameItems = new ArrayList<>();
    Activity activity;
    private Gson gson;
    private boolean isLoadingAdded = false;
    PreafManager preafManager;


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

            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return brandListItems.size();
    }

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
                             ((BrandHolder) holder).binding.frameitemLayout.setLayoutManager(new GridLayoutManager(activity, 4));
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
                                     try {


                                         String number ="8460638464";
                                         Intent callIntent = new Intent(Intent.ACTION_CALL);
                                         callIntent.setData(Uri.parse("tel:" + number));
                                         if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                             // TODO: Consider calling
                                             //    ActivityCompat#requestPermissions
                                             // here to request the missing permissions, and then overriding
                                             //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                             //                                          int[] grantResults)
                                             // to handle the case where the user grants the permission. See the documentation
                                             // for ActivityCompat#requestPermissions for more details.
                                             return;
                                         }
                                         activity.startActivity(callIntent);
                                     } catch (Exception e) {
                                         e.printStackTrace();
                                     }
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
                         break;
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

