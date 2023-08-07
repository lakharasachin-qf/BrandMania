package com.make.mybrand.Fragment;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Common.MakeMyBrandApp;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DialogUserRegistrationLayoutBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationFragment extends BottomSheetDialogFragment {

    private DialogUserRegistrationLayoutBinding binding;
    private Activity act;
    private PreafManager  pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
        setCancelable(false);
    }

    private UserRegistrationFragment context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_user_registration_layout, container, false);
        act = getActivity();
        binding.cancelBtn.setOnClickListener(v -> dismiss());
        binding.cancelBtn.setVisibility(View.GONE);
        pref = new PreafManager(act);
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.referlEdt, binding.referlLayout);


        if (pref.getSpleshReferrer() != null && !pref.getSpleshReferrer().isEmpty()) {
            binding.referlEdt.setText(pref.getSpleshReferrer());
        }

        context = this;
        binding.continueBtn.setOnClickListener(v -> {
            boolean isError = false;
            boolean isFocus = false;

            if (binding.nameTxt.getText().toString().trim().length() == 0) {
                isError = true;
                isFocus = true;
                binding.nameTxtLayout.setError(getString(R.string.enter_first_name));
                binding.nameTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNavText)));
                binding.nameTxt.requestFocus();

            }
            if (!isError) {
                updatePreLogin();
            }
        });
        return binding.getRoot();
    }

    private void updatePreLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.PRE_LOGIN_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response-REP", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonArray1 = jsonObject.getJSONObject("data");
                    context.dismiss();
                    pref.setUserName(binding.nameTxt.getText().toString());

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
            public Map<String, String> getHeaders() {
                return MakeMyBrandApp.getInstance().getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String[] array = binding.nameTxt.getText().toString().split(" ");
                params.put("first_name", array[0]);
                params.put("last_name", array[1]);
                params.put("referral_code", binding.referlEdt.getText().toString());
                Log.e("PARAM",params.toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
}