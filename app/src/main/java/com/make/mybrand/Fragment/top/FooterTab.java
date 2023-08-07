package com.make.mybrand.Fragment.top;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Activity.brand.AddBranddActivity;
import com.make.mybrand.Adapter.FooterAdapter;
import com.make.mybrand.Adapter.FooterModel;
import com.make.mybrand.Common.MakeMyBrandApp;
import com.make.mybrand.Common.ObserverActionID;
import com.make.mybrand.Fragment.BaseFragment;
import com.make.mybrand.Interface.onFooterSelectListener;
import com.make.mybrand.R;
import com.make.mybrand.databinding.FooterTabBinding;

import java.util.ArrayList;
import java.util.Observable;

public class FooterTab extends BaseFragment {
    private FooterTabBinding binding;
    ArrayList<FooterModel> footerModels = new ArrayList<>();


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();

        binding = DataBindingUtil.inflate(inflater, R.layout.footer_tab, parent, false);
        binding.addbrandTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(act, AddBranddActivity.class);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        setAdapter();
        return binding.getRoot();
    }

    @Override
    public void update(Observable observable, Object data) {

        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.REFRESH_IMAGE_CATEGORY_DATA) {
            setAdapter();
        }
        super.update(observable, data);
    }

    public void setData(FooterModel model) {
        if (prefManager.getActiveBrand() != null) {
            model.setAddress(prefManager.getActiveBrand().getAddress());
            model.setEmailId(prefManager.getActiveBrand().getEmail());
            model.setContactNo(prefManager.getActiveBrand().getPhonenumber());
            model.setWebsite(prefManager.getActiveBrand().getWebsite());
        } else {
            model.setAddress("");
            model.setEmailId("");
            model.setContactNo("");
            model.setWebsite("");
        }
        footerModels.add(model);
    }


    public void setAdapter() {

        FooterModel model = new FooterModel();

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_ELEVEN);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TWELVE);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_THIRTEEN);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FOURTEEN);
        model.setFree(true);
        setData(model);


        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FIFTEEN);
        model.setFree(true);
        setData(model);


        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SIXTEEN);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SEVENTEEN);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_EIGHTEEN);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_NINETEEN);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TWENTY);
        model.setFree(true);
        setData(model);

        //premium
        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_ONE);
        // model.setEnable(FooterRules._ONE(preafManager.getActiveBrand()));
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TWO);
        //     model.setEnable(FooterRules._ONE(preafManager.getActiveBrand()));
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_THREE);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FOUR);
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_FIVE);
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SIX);
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SEVEN);
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_EIGHT);
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);


        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_NINE);
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);

        model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TEN);
//        if (Utility.isUserPaid(preafManager.getActiveBrand())) {
//            model.setFree(true);
//        }
        model.setFree(true);
        setData(model);

        FooterAdapter footerAdapter = new FooterAdapter(footerModels, act);
        FooterAdapter.onFooterListener onFooterListener = new FooterAdapter.onFooterListener() {
            @Override
            public void onFooterChoose(int footerLayout, FooterModel footerModel) {
                footerAdapter.notifyDataSetChanged();
                ((onFooterSelectListener) act).onFooterSelectEvent(footerLayout, footerModel);

            }
        };
        footerAdapter.setFooterListener(onFooterListener);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 2);

        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act,LinearLayoutManager.VERTICAL,false);
        binding.footerRecycler.setLayoutManager(mLayoutManager);
        binding.footerRecycler.setHasFixedSize(true);
        binding.footerRecycler.setAdapter(footerAdapter);
    }
}
