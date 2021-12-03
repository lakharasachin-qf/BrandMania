package com.app.brandmania.Fragment.top;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
import com.app.brandmania.Activity.brand.AddBrandMultipleActivity;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Activity.custom.ViewAllFrameImageActivity;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Interface.IRemoveFrame;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DialogUpgradeLayoutBinding;
import com.app.brandmania.databinding.FrameTabBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.views.MyBounceInterpolator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FrameTab extends BaseFragment {

    private FrameTabBinding binding;

    ArrayList<ImageList> menuModels = new ArrayList<>();

    void animateButton() {
        final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce_two);
        double animationDuration = 4 * 1000;
        myAnim.setRepeatCount(Animation.INFINITE);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);

        myAnim.setInterpolator(interpolator);

        // Animate the button
        binding.continueBtn.startAnimation(myAnim);


        // Run button animation again after it finished
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animateButton();
            }
        });
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.frame_tab, parent, false);

        if (prefManager.getActiveBrand() != null) {
            getFrame();
        }

        binding.subscribePlaneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.getActiveBrand() != null) {
                    if (prefManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("0") && (prefManager.getActiveBrand().getPackagename().equalsIgnoreCase("Enterprise")) || prefManager.getActiveBrand().getPackagename().equalsIgnoreCase("Standard")) {
                        HELPER.WHATSAPP_REDIRECTION(act, prefManager.getActiveBrand().getName(), prefManager.getMobileNumber());
                    } else {
                        triggerUpgradePackage();
                    }
                } else {

                }
            }
        });
        binding.addbrandTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(act, AddBranddActivity.class);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        if (prefManager.getActiveBrand() != null) {
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
            if (prefManager.getActiveBrand().getFrame() != null && prefManager.getActiveBrand().getFrame().size() != 0) {
                binding.subscribePlaneBtn.setVisibility(View.GONE);
            }
        } else {
            binding.removeFrameBtn.setVisibility(View.GONE);
            binding.subscribePlaneBtn.setVisibility(View.GONE);
            binding.addbrandTag.setVisibility(View.VISIBLE);
        }


        if (prefManager.getActiveBrand() == null) {
            binding.removeFrameBtn.setVisibility(View.GONE);
            binding.subscribePlaneBtn.setVisibility(View.GONE);
            binding.addbrandTag.setVisibility(View.GONE);
            binding.content.setVisibility(View.VISIBLE);

            animateButton();

            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.ROUTE(act, AddBrandMultipleActivity.class);
                }
            });
        }
        return binding.getRoot();
    }

    public DialogUpgradeLayoutBinding upgradeLayoutBinding;
    private void triggerUpgradePackage() {
        upgradeLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(upgradeLayoutBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(upgradeLayoutBinding.getRoot());
        if (!prefManager.getActiveBrand().getPackagename().isEmpty())
            upgradeLayoutBinding.element4.setText("Currently you are subscribed with \"" + prefManager.getActiveBrand().getPackagename() + "\" package");
        else
            upgradeLayoutBinding.element4.setText("Currently you are subscribed with \"Free\" package");

        upgradeLayoutBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile", "1");

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

    private void getFrame() {
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("FrameTab: ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    menuModels = ResponseHandler.HandleGetFrameList(jsonObject);
                    if (prefManager.getActiveBrand() != null) {
                        //  Toast.makeText(act, "NotNull", Toast.LENGTH_SHORT).show();
                        if (menuModels != null && menuModels.size() != 0 && jsonObject.getJSONObject("data").getString("is_frame").equalsIgnoreCase("1")) {
                            ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(menuModels, act);
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 3);
                            binding.frameRecycler.setLayoutManager(mLayoutManager);
                            binding.frameRecycler.setHasFixedSize(true);
                            binding.frameRecycler.setAdapter(menuAddaptor);
                            binding.subscribePlaneBtn.setVisibility(View.VISIBLE);

                            if (prefManager.getActiveBrand().getIs_frame().equalsIgnoreCase("1")) {
                                binding.subscribePlaneBtn.setVisibility(View.GONE);
                            }

                        } else {
                            binding.text1.setVisibility(View.VISIBLE);
                            binding.frameRecycler.setVisibility(View.GONE);
                            binding.subscribePlaneBtn.setText("Request For Frame");
                            binding.subscribePlaneBtn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // Toast.makeText(act, "Null", Toast.LENGTH_SHORT).show();
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

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (prefManager.getActiveBrand() != null) {
                    params.put("brand_id", prefManager.getActiveBrand().getId());
                }

                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);

    }

}
