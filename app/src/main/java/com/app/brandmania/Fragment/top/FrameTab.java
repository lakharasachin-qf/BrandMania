package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Activity.custom.ViewAllFrameImageActivity;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.BrandAdapter;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Interface.IRemoveFrame;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.DialogUpgradeLayoutBinding;
import com.app.brandmania.databinding.FrameTabBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FrameTab extends Fragment {

    Activity act;
    private FrameTabBinding binding;
    private String is_frame="";
    PreafManager preafManager;
    ArrayList<ImageList> menuModels = new ArrayList<>();
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.frame_tab,container,false);
        preafManager=new PreafManager(Objects.requireNonNull(getActivity()));

            getFrame();

        binding.subscribePlaneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("0") &&(preafManager.getActiveBrand().getPackagename().equalsIgnoreCase("Enterprise")) || preafManager.getActiveBrand().getPackagename().equalsIgnoreCase("Standard")){
                    HELPER.WHATSAPP_REDIRECTION(act,preafManager.getActiveBrand().getName(),preafManager.getMobileNumber());
                }else {
                    triggerUpgradePackage();
                }
            }
        });
        binding.addbrandTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(act, AddBranddActivity.class);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        if (preafManager.getActiveBrand()!=null) {
            if (this.getActivity().getClass() == ViewAllFrameImageActivity.class) {
                binding.removeFrameBtn.setVisibility(View.VISIBLE);
                binding.subscribePlaneBtn.setVisibility(View.VISIBLE);
            }
            binding.removeFrameBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((IRemoveFrame) act).onRemoveSelectEvent();
                }
            });
        }
        else {
            binding.removeFrameBtn.setVisibility(View.GONE);
            binding.subscribePlaneBtn.setVisibility(View.GONE);
            binding.addbrandTag.setVisibility(View.VISIBLE);

        }
        return binding.getRoot();
    }

    //show dialog for upgrading package for using all 6 frames
    public DialogUpgradeLayoutBinding upgradeLayoutBinding;
    private void triggerUpgradePackage() {
        upgradeLayoutBinding=DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(upgradeLayoutBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(upgradeLayoutBinding.getRoot());
        if (!preafManager.getActiveBrand().getPackagename().isEmpty())
            upgradeLayoutBinding.element4.setText("Currently you are subscribed with \""+preafManager.getActiveBrand().getPackagename()+"\" package");
        else
            upgradeLayoutBinding.element4.setText("Currently you are subscribed with \"Free\" package");

        upgradeLayoutBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                    Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile","1");

                act.startActivity(intent);
                    act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        upgradeLayoutBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }


    public void setAdapterFrame() {
        ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(menuModels, act);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 4);
        binding.frameRecycler.setLayoutManager(mLayoutManager);
        binding.frameRecycler.setHasFixedSize(true);
        binding.frameRecycler.setAdapter(menuAddaptor);
    }
    private void getFrame() {
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("FrameTab: ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    menuModels = ResponseHandler.HandleGetFrameList(jsonObject);
                    if (preafManager.getActiveBrand()!=null) {
                      //  Toast.makeText(act, "NotNull", Toast.LENGTH_SHORT).show();
                        if (menuModels != null && menuModels.size() != 0 && jsonObject.getJSONObject("data").getString("is_frame").equalsIgnoreCase("1")) {
                            ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(menuModels, act);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 4);
                            binding.frameRecycler.setLayoutManager(mLayoutManager);
                            binding.frameRecycler.setHasFixedSize(true);
                            binding.frameRecycler.setAdapter(menuAddaptor);
                            binding.subscribePlaneBtn.setVisibility(View.VISIBLE);
                        } else {

                            binding.text1.setVisibility(View.VISIBLE);
                            binding.frameRecycler.setVisibility(View.GONE);
                            binding.subscribePlaneBtn.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                      //  Toast.makeText(act, "Null", Toast.LENGTH_SHORT).show();
                        binding.subscribePlaneBtn.setVisibility(View.GONE);
                        binding.removeFrameBtn.setVisibility(View.GONE);
                        binding.frameRecycler.setVisibility(View.GONE);
                        binding.addbrandTag.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
//                        String body;
//                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                        Log.e("Load-Get_Exam ", body);

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
               if (preafManager.getActiveBrand()!=null)
                {
                    params.put("brand_id", preafManager.getActiveBrand().getId());
                }
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        if (preafManager.getActiveBrand()!=null) {
            queue.add(stringRequest);
        }
    }

}
