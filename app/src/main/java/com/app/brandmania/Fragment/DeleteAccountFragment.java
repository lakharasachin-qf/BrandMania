package com.app.brandmania.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Adapter.BrandAdapter;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Interface.onDeleteAccountClickListener;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DialogDeleteAccountFragmentLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DeleteAccountFragment extends BottomSheetDialogFragment {

    private DialogDeleteAccountFragmentLayoutBinding binding;

    private Activity act;

    private DeleteAccountInterface deleteAccount;

    public void setClickListener(DeleteAccountInterface deleteAccount) {
        this.deleteAccount = deleteAccount;
    }

    public interface DeleteAccountInterface {
        void onClickListener(int position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    public void setActivity(Activity act) {
        this.act = act;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_delete_account_fragment_layout, container, false);
        binding.cancelBtn.setOnClickListener(v -> dismiss());
        binding.continueBtn.setOnClickListener(v -> {
            deleteAccount.onClickListener(1);
            //MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.DELETE_ACCOUNT_OBSERVER);
            dismiss();
        });
        return binding.getRoot();
    }
}