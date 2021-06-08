package com.app.brandmania.newModule.newFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Adapter.FooterAdapter;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.R;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FragmentCustomFooterBinding;

import java.util.ArrayList;

public class CustomFooterFragment extends BaseFragment {

    private FragmentCustomFooterBinding binding;
    Activity act;

    ArrayList<FooterModel> footerModels = new ArrayList<>();
    private PreafManager preafManager;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_footer, parent, false);

        preafManager=new PreafManager(act);

        binding.addbrandTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(act, AddBranddActivity.class);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (preafManager.getActiveBrand()!=null) {
                    setAdapter();
                }
                else {
                    binding.footerRecycler.setVisibility(View.GONE);
                    binding.addbrandTag.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }
    public void setData(FooterModel model){
        if (preafManager.getActiveBrand()!=null) {
            model.setAddress(preafManager.getActiveBrand().getAddress());
            model.setEmailId(preafManager.getActiveBrand().getEmail());
            model.setContactNo(preafManager.getActiveBrand().getPhonenumber());
            model.setWebsite(preafManager.getActiveBrand().getWebsite());
            footerModels.add(model);
        }
        else
        {
            model.setAddress("");
            model.setEmailId("");
            model.setContactNo("");
            model.setWebsite("");
            footerModels.add(model);
        }
    }
    public void setAdapter() {


        FooterModel model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SEVEN);
        model.setFree(true);
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_THREE);
        model.setFree(true);
        setData(model);



        //premium

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_ONE);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TWO);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);



        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FOUR);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FIVE);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SIX);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);




        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_EIGHT);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);


        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_NINE);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TEN);
        if (Utility.isUserPaid(preafManager.getActiveBrand())){
            model.setFree(true);
        }
        setData(model);


        FooterAdapter footerAdapter = new FooterAdapter(footerModels, act);
        FooterAdapter.onFooterListener onFooterListener=new FooterAdapter.onFooterListener() {
            @Override
            public void onFooterChoose(int footerLayout, FooterModel footerModel) {
                footerAdapter.notifyDataSetChanged();
                ((onFooterSelectListener)act).onFooterSelectEvent(footerLayout,footerModel);

            }
        };
        footerAdapter.setFooterListener(onFooterListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act,LinearLayoutManager.VERTICAL,false);
        binding.footerRecycler.setLayoutManager(mLayoutManager);
        binding.footerRecycler.setHasFixedSize(true);
        binding.footerRecycler.setAdapter(footerAdapter);
    }
}