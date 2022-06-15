package com.app.brandmania.Fragment;

import android.app.Activity;
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
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DialogUserNewRegistrationBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserNewRegistrationFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private DialogUserNewRegistrationBinding binding;
    private Activity act;
    private PreafManager pref;
    private UserNewRegistrationFragment context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
        setCancelable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_user_new_registration, container, false);
        act = getActivity();
        context = this;
        assert act != null;
        pref = new PreafManager(act);
        Log.e("number", pref.getMobileNumber());
        if (pref.getUserMobileNumber() != null && !pref.getUserMobileNumber().isEmpty()) {
            binding.phoneEdt.setText(pref.getUserMobileNumber());
        }
        binding.cancelBtn.setOnClickListener(v -> dismiss());
        setInitView();
        return binding.getRoot();
    }

    public void setInitView() {
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.emailEdt, binding.emailLayout);
        CodeReUse.RemoveError(binding.lastNameTxt, binding.lastNameTxtLayout);
        CodeReUse.RemoveError(binding.phoneEdt, binding.phoneLayout);
        binding.continueBtn.setOnClickListener(view -> UserNewRegistrationFragment.this.Validation());
    }

    private void Validation() {
        boolean isError = false;
        if (binding.nameTxt.getText().toString().trim().isEmpty()) {
            isError = true;
            binding.nameTxtLayout.setError(getString(R.string.empty_name));
            binding.nameTxt.requestFocus();
        }
        if (binding.lastNameTxt.getText().toString().trim().isEmpty()) {
            isError = true;
            binding.lastNameTxtLayout.setError(getString(R.string.enter_last_name));
            binding.lastNameTxt.requestFocus();
        }
        if (binding.phoneEdt.getText().toString().isEmpty()) {
            isError = true;
            binding.phoneLayout.setError("Enter Number");
            binding.phoneEdt.requestFocus();
        } else if (binding.phoneEdt.getText().toString().length() < 10) {
            isError = true;
            binding.phoneLayout.setError(getString(R.string.enter_valid_number));
            binding.phoneEdt.requestFocus();
        }
        if (!binding.emailEdt.getText().toString().equals("")) {
            if (!CodeReUse.isEmailValid(binding.emailEdt.getText().toString().trim())) {
                isError = true;
                binding.emailLayout.setError(getString(R.string.enter_valid_email_address));
                binding.emailEdt.requestFocus();
            }
        } else {
            if (binding.emailEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.emailLayout.setError(getString(R.string.enter_email_id));
                binding.emailEdt.requestFocus();
            }
        }
        if (!isError) {
            //app Calls
            updateProfile();
        }
    }
    private void updateProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.EDIT_BRAND_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Edit-RESPONSE", response);
                binding.progressCircular.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonArray1 = jsonObject.getJSONObject("data");
                    context.dismiss();
                    pref.setUserName(Objects.requireNonNull(binding.nameTxt.getText()).toString()+" "+binding.lastNameTxt.getText().toString());
                    pref.setUserEmail_Id(Objects.requireNonNull(binding.emailEdt.getText()).toString());
                    pref.setUserMobileNumber(Objects.requireNonNull(binding.phoneEdt.getText()).toString());
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
                //String[] array = binding.nameTxt.getText().toString().split(" ");
                params.put("first_name", Objects.requireNonNull(binding.nameTxt.getText()).toString());
                params.put("last_name", Objects.requireNonNull(binding.lastNameTxt.getText()).toString());
                params.put("phone", Objects.requireNonNull(binding.phoneEdt.getText()).toString());
                params.put("email", Objects.requireNonNull(binding.emailEdt.getText()).toString());
                Log.e("Edit-PARAM", params.toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }
}