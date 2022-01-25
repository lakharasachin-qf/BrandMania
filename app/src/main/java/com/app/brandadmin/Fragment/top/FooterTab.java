package com.app.brandadmin.Fragment.top;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandadmin.Activity.brand.AddBranddActivity;
import com.app.brandadmin.Adapter.FooterAdapter;
import com.app.brandadmin.Adapter.FooterModel;
import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Interface.onFooterSelectListener;
import com.app.brandadmin.R;
import com.app.brandadmin.Utils.Utility;
import com.app.brandadmin.databinding.FooterTabBinding;

import java.util.ArrayList;

public class FooterTab extends Fragment {
    Activity act;
    private FooterTabBinding binding;
    ArrayList<FooterModel> footerModels = new ArrayList<>();
    private PreafManager preafManager;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        preafManager=new PreafManager(act);
        binding = DataBindingUtil.inflate(inflater, R.layout.footer_tab, container, false);
        binding.addbrandTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(act, AddBranddActivity.class);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        if (preafManager.getActiveBrand()!=null) {
            setAdapter();
        }
        else {
            binding.footerRecycler.setVisibility(View.GONE);
            binding.addbrandTag.setVisibility(View.VISIBLE);
        }
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
            public void onFooterChoose(int footerLayout,FooterModel footerModel) {
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