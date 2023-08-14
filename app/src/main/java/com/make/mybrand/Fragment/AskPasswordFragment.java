package com.make.mybrand.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DialogAskPasswordBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AskPasswordFragment extends BottomSheetDialogFragment {

    private DialogAskPasswordBinding binding;

    private Activity act;
    private boolean canCancel=true;

    private AskPasswordInterface askPasswordInterface;

    public void setClickListener(AskPasswordInterface deleteAccount) {
        this.askPasswordInterface = deleteAccount;
    }

    public interface AskPasswordInterface {
        void onClickListener(int position,String password);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    public void setActivity(Activity act) {
        this.act = act;
    }

    public void setCanCancel(boolean act) {
        this.canCancel = act;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_ask_password, container, false);

        binding.cancelBtn.setOnClickListener(v -> dismiss());
        if(!canCancel){
            binding.cancelBtn.setVisibility(View.GONE);
        }

        binding.continueBtn.setOnClickListener(v -> {

            String password = binding.passwordNumber.getText().toString().trim();
            String confirmPassword = binding.confirmpasswordNumber.getText().toString().trim();
            if(password.isEmpty()){
                binding.passwordNumber.setError("Enter Password");
                binding.passwordNumber.requestFocus();
                return;
            }
            if(password.length() < 6){
                binding.passwordNumber.setError("Password should be at least 6 digit long");
                binding.passwordNumber.requestFocus();
                return;
            }

            if(confirmPassword.isEmpty()){
                binding.confirmpasswordNumber.setError("Enter Confirm Password");
                binding.confirmpasswordNumber.requestFocus();
                return;
            }
            if(!password.equals(confirmPassword)){
                binding.confirmpasswordNumber.setError("Password doesn't match");
                binding.confirmpasswordNumber.requestFocus();
                return;
            }
                askPasswordInterface.onClickListener(1,password);
                dismiss();

        });
        return binding.getRoot();
    }
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9a-zA-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


}