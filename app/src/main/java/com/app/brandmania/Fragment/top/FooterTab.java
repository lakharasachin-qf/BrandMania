package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Adapter.FooterAdapter;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.CategoryTabBinding;
import com.app.brandmania.databinding.FooterTabBinding;

import java.util.ArrayList;

import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;

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
        setAdapter();
        return binding.getRoot();
    }


    public void setData(FooterModel model){
        model.setAddress(preafManager.getActiveBrand().getAddress());
        model.setEmailId(preafManager.getActiveBrand().getEmail());
        model.setContactNo(preafManager.getActiveBrand().getPhonenumber());
        model.setWebsite(preafManager.getActiveBrand().getWebsite());
        footerModels.add(model);
    }
    public void setAdapter() {
        FooterModel model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_ONE);
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TWO);
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_THREE);
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FOUR);
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FIVE);
        setData(model);

        model=new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SIX);
        setData(model);



        FooterAdapter footerAdapter = new FooterAdapter(footerModels, act);
        FooterAdapter.onFooterListener onFooterListener=new FooterAdapter.onFooterListener() {
            @Override
            public void onFooterChoose(int footerLayout) {
                footerAdapter.notifyDataSetChanged();
                ((onFooterSelectListener)act).onFooterSelectEvent(footerLayout);
            }
        };
        footerAdapter.setFooterListener(onFooterListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act,LinearLayoutManager.VERTICAL,false);
        binding.footerRecycler.setLayoutManager(mLayoutManager);
        binding.footerRecycler.setHasFixedSize(true);
        binding.footerRecycler.setAdapter(footerAdapter);
    }
}
