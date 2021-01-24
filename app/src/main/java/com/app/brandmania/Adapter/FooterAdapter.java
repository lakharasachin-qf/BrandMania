package com.app.brandmania.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Activity.PackageActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ItemFooterEightBinding;
import com.app.brandmania.databinding.ItemFooterFiveBinding;
import com.app.brandmania.databinding.ItemFooterFourBinding;
import com.app.brandmania.databinding.ItemFooterLayoutSixBinding;
import com.app.brandmania.databinding.ItemFooterNineBinding;
import com.app.brandmania.databinding.ItemFooterOneBinding;
import com.app.brandmania.databinding.ItemFooterSevenBinding;
import com.app.brandmania.databinding.ItemFooterTenBinding;
import com.app.brandmania.databinding.ItemFooterThreeBinding;
import com.app.brandmania.databinding.ItemFooterTwoBinding;

import java.util.ArrayList;

import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_EIGHT;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FIVE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FOUR;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_NINE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_ONE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_SEVEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_SIX;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_TEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_THREE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_TWO;
import static com.app.brandmania.Model.ImageList.LAYOUT_LOADING;


public class FooterAdapter extends RecyclerView.Adapter{
    private ArrayList<FooterModel> footerModels;
    Activity activity;
    private boolean isLoadingAdded = false;
    private int checkedPosition = 0;
    public onFooterListener footerListener;
    PreafManager preafManager;

    public FooterAdapter setFooterListener(onFooterListener footerListener) {
        this.footerListener = footerListener;
        return this;
    }

    public interface onFooterListener{
        void onFooterChoose(int footerLayout, FooterModel footerModel);
    }

    public FooterAdapter(ArrayList<FooterModel> footerModels, Activity activity) {
        this.footerModels = footerModels;
        this.activity = activity;
        this.preafManager=new PreafManager(activity);

    }
    @NonNull
    @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {

            case LAYOUT_FRAME_ONE   :
                ItemFooterOneBinding itemFooterOneBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_one, viewGroup, false);
                return new FooterHolderOne(itemFooterOneBinding);
            case LAYOUT_FRAME_TWO   :
                ItemFooterTwoBinding itemFooterTwoBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_two, viewGroup, false);
                return new FooterHolderTwo(itemFooterTwoBinding);
            case LAYOUT_FRAME_THREE   :
                ItemFooterThreeBinding itemFooterThreeBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_three, viewGroup, false);
                return new FooterHolderThree(itemFooterThreeBinding);
            case LAYOUT_FRAME_FOUR   :
                ItemFooterFourBinding itemFooterFourBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_four, viewGroup, false);
                return new FooterHolderFour(itemFooterFourBinding);
            case LAYOUT_FRAME_FIVE   :
                ItemFooterFiveBinding itemFooterFiveBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_five, viewGroup, false);
                return new FooterHolderFive(itemFooterFiveBinding);
            case LAYOUT_FRAME_SIX   :
                ItemFooterLayoutSixBinding itemFooterLayoutSixBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_layout_six, viewGroup, false);
                return new FooterHolderSix(itemFooterLayoutSixBinding);
            case LAYOUT_FRAME_SEVEN   :
                ItemFooterSevenBinding itemFooterSevenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_seven, viewGroup, false);
                return new FooterHolderSeven(itemFooterSevenBinding);
            case LAYOUT_FRAME_EIGHT   :
                ItemFooterEightBinding itemFooterEightBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_eight, viewGroup, false);
                return new FooterHolderEight(itemFooterEightBinding);
            case LAYOUT_FRAME_NINE   :
                ItemFooterNineBinding itemFooterNineBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_nine, viewGroup, false);
                return new FooterHolderNine(itemFooterNineBinding);

            case LAYOUT_FRAME_TEN   :
                ItemFooterTenBinding itemFooterTenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_ten, viewGroup, false);

                return new FooterHolderTen(itemFooterTenBinding);



        }
        return null;

    }
    @Override public int getItemViewType(int position) {
        switch (footerModels.get(position).getLayoutType()) {
            case 1:
                return LAYOUT_FRAME_ONE;
            case 2:
                return LAYOUT_FRAME_TWO;
            case 3:
                return LAYOUT_FRAME_THREE;
            case 4:
                return LAYOUT_FRAME_FOUR;
            case 5:
                return LAYOUT_FRAME_FIVE;
            case 6:
                return LAYOUT_FRAME_SIX;
            case 7:
                return LAYOUT_FRAME_SEVEN;
            case 8:
                return LAYOUT_FRAME_EIGHT;
            case 9:
                return LAYOUT_FRAME_NINE;

            case 10:
                return LAYOUT_FRAME_TEN;
                default:
                return -1;
        }

    }
    @Override public int getItemCount() {
        return footerModels.size();
    }
    @SuppressLint("ResourceAsColor")
    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FooterModel model = footerModels.get(position);
        BrandListItem activeBrand=preafManager.getActiveBrand();
        Log.e("NOTIFY", String.valueOf(checkedPosition));
        if (model != null) {
            switch (model.getLayoutType()) {

                case LAYOUT_FRAME_ONE:
                    ((FooterHolderOne) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                Toast.makeText(activity,"khushboogggg",Toast.LENGTH_LONG).show();
                                ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);

                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);

                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.GONE);

                    }
                    if (!model.isFree()) {
                        ((FooterHolderOne) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                       // ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.GONE);
                       // ((FooterHolderOne) holder).binding.freePremium.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((FooterHolderOne) holder).binding.freePremium.setVisibility(View.VISIBLE);
                       // ((FooterHolderOne) holder).binding.elementPremium.setVisibility(View.GONE);
                        //((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderOne) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderOne) holder).binding.gmailLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderOne) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderOne) holder).binding.contactLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getAddress().isEmpty()){
                        ((FooterHolderOne) holder).binding.locationText.setText(activeBrand.getAddress());
                    }else {
                        ((FooterHolderOne) holder).binding.addressLayoutElement.setVisibility(View.GONE);
                    }


                    break;
                case LAYOUT_FRAME_TWO:
                    ((FooterHolderTwo) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    if (!model.isFree()) {
                        ((FooterHolderTwo) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderTwo) holder).binding.freePremium.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((FooterHolderTwo) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderTwo) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderTwo) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderTwo) holder).binding.gmailLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderTwo) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderTwo) holder).binding.contactLayout.setVisibility(View.GONE);
                    }
                    if (activeBrand.getPhonenumber().isEmpty() && activeBrand.getEmail().isEmpty()){
                        ((FooterHolderTwo) holder).binding.firstView.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getAddress().isEmpty()){
                        ((FooterHolderTwo) holder).binding.locationText.setText(activeBrand.getAddress());
                    }else {
                        ((FooterHolderTwo) holder).binding.locationLayout.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getWebsite().isEmpty()){
                        ((FooterHolderTwo) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    }else {
                        ((FooterHolderTwo) holder).binding.websiteLayout.setVisibility(View.GONE);
                    }

                    if (activeBrand.getAddress().isEmpty() && activeBrand.getWebsite().isEmpty()){
                        ((FooterHolderTwo) holder).binding.secondView.setVisibility(View.GONE);
                    }

                    break;
                case LAYOUT_FRAME_THREE:
                    ((FooterHolderThree) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    if (!model.isFree()) {
                        ((FooterHolderThree) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderThree) holder).binding.freePremium.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((FooterHolderThree) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderThree) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }


                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderThree) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderThree) holder).binding.gmailLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderThree) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderThree) holder).binding.contactLayout.setVisibility(View.GONE);
                    }



                    if (!activeBrand.getAddress().isEmpty()){
                        ((FooterHolderThree) holder).binding.locationText.setText(activeBrand.getAddress());
                    }else {
                        ((FooterHolderThree) holder).binding.loactionLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getWebsite().isEmpty()){
                        ((FooterHolderThree) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    }else {
                        ((FooterHolderThree) holder).binding.websiteText.setVisibility(View.GONE);
                    }


                    break;
                case LAYOUT_FRAME_FOUR:
                    ((FooterHolderFour) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    if (!model.isFree()) {
                        ((FooterHolderFour) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.freePremium.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((FooterHolderFour) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderFour) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderFour) holder).binding.gmailLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderFour) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderFour) holder).binding.contactLayout.setVisibility(View.GONE);
                    }



                    if (!activeBrand.getAddress().isEmpty()){
                        ((FooterHolderFour) holder).binding.locationText.setText(activeBrand.getAddress());
                    }else {
                        ((FooterHolderFour) holder).binding.locationLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getWebsite().isEmpty()){
                        ((FooterHolderFour) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    }else {
                        ((FooterHolderFour) holder).binding.websiteText.setVisibility(View.GONE);
                    }


                    break;
                case LAYOUT_FRAME_FIVE:
                    ((FooterHolderFive) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);

                            }

                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {

                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!model.isFree()) {
                        ((FooterHolderFive) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderFive) holder).binding.freePremium.setVisibility(View.GONE);
                    }

                    else {
                        ((FooterHolderFive) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderFive) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                        if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderFive) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderFive) holder).binding.elementEmail.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderFive) holder).binding.phoneTxt.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderFive) holder).binding.elementMobile.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getWebsite().isEmpty()){
                        ((FooterHolderFive) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    }else {
                        ((FooterHolderFive) holder).binding.element0.setVisibility(View.GONE);
                    }

                    break;
                case LAYOUT_FRAME_SIX:
                    ((FooterHolderSix) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!model.isFree()) {
                        ((FooterHolderSix) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderSix) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderSix) holder).binding.contactLayout.setVisibility(View.GONE);
                    }


                    break;

                    case LAYOUT_FRAME_SEVEN:
                    ((FooterHolderSeven) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!model.isFree()) {
                        ((FooterHolderSeven) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderSeven) holder).binding.freePremium.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((FooterHolderSeven) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderSeven) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderSeven) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderSeven) holder).binding.contactLayout.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderSeven) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderSeven) holder).binding.gmailLayout.setVisibility(View.GONE);
                    }
                        if (!activeBrand.getName().isEmpty()){
                            ((FooterHolderSeven) holder).binding.brandNameText.setText(activeBrand.getName());
                        }else {
                            ((FooterHolderSeven) holder).binding.brandNameText.setVisibility(View.GONE);
                        }

                    break;

                case LAYOUT_FRAME_EIGHT:
                    ((FooterHolderEight) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!model.isFree()) {
                        ((FooterHolderEight) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderEight) holder).binding.freePremium.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((FooterHolderEight) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderEight) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderEight) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderEight) holder).binding.contactLayout.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderEight) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderEight) holder).binding.gmailLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getAddress().isEmpty()){
                        ((FooterHolderEight) holder).binding.locationText.setText(activeBrand.getAddress());
                    }else {
                        ((FooterHolderEight) holder).binding.addressLayoutElement.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getName().isEmpty()){
                        ((FooterHolderEight) holder).binding.brandNameText.setText(activeBrand.getName());
                    }else {
                        ((FooterHolderEight) holder).binding.addressLayoutElement.setVisibility(View.GONE);
                    }


                    break;


                case LAYOUT_FRAME_NINE:
                    ((FooterHolderNine) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                                           } else {
                        ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!model.isFree()) {
                        ((FooterHolderNine) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderNine) holder).binding.freePremium.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((FooterHolderNine) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.GONE);
                        ((FooterHolderNine) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderNine) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderNine) holder).binding.contactText.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderNine) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderNine) holder).binding.gmailText.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getName().isEmpty()){
                        ((FooterHolderNine) holder).binding.brandNameText.setText(activeBrand.getName());
                    }else {
                        ((FooterHolderNine) holder).binding.brandNameText.setVisibility(View.GONE);
                    }


                    break;


                case LAYOUT_FRAME_TEN:
                    ((FooterHolderTen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(),footerModels.get(position));
                                ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    if (checkedPosition == position) {
                        ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!model.isFree()) {
                        ((FooterHolderTen) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()){
                        ((FooterHolderTen) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }else {
                        ((FooterHolderTen) holder).binding.contactText.setVisibility(View.GONE);
                        ((FooterHolderTen) holder).binding.callImage.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()){
                        ((FooterHolderTen) holder).binding.gmailText.setText(activeBrand.getEmail());
                    }else {
                        ((FooterHolderTen) holder).binding.gmailText.setVisibility(View.GONE);
                        ((FooterHolderTen) holder).binding.gmailImage.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getAddress().isEmpty()){
                        ((FooterHolderTen) holder).binding.locationText.setText(activeBrand.getAddress());
                    }else {
                        ((FooterHolderTen) holder).binding.locationText.setVisibility(View.GONE);
                        ((FooterHolderTen) holder).binding.locationImage.setVisibility(View.GONE);
                    }


                    break;

            }
        }
    }

    public void targetNextActivity() {
        Intent intent = new Intent(activity, PackageActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    static class FooterHolderOne extends RecyclerView.ViewHolder {
        ItemFooterOneBinding binding;

        FooterHolderOne(ItemFooterOneBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class FooterHolderTwo extends RecyclerView.ViewHolder {
        ItemFooterTwoBinding binding;

        FooterHolderTwo(ItemFooterTwoBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class FooterHolderThree extends RecyclerView.ViewHolder {
        ItemFooterThreeBinding binding;

        FooterHolderThree(ItemFooterThreeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class FooterHolderFour extends RecyclerView.ViewHolder {
        ItemFooterFourBinding binding;

        FooterHolderFour(ItemFooterFourBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class FooterHolderFive extends RecyclerView.ViewHolder {
        ItemFooterFiveBinding binding;

        FooterHolderFive(ItemFooterFiveBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class FooterHolderSix extends RecyclerView.ViewHolder {
        ItemFooterLayoutSixBinding binding;

        FooterHolderSix(ItemFooterLayoutSixBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class FooterHolderSeven extends RecyclerView.ViewHolder {
        ItemFooterSevenBinding binding;

        FooterHolderSeven(ItemFooterSevenBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class FooterHolderEight extends RecyclerView.ViewHolder {
        ItemFooterEightBinding binding;

        FooterHolderEight(ItemFooterEightBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class FooterHolderNine extends RecyclerView.ViewHolder {
        ItemFooterNineBinding binding;

        FooterHolderNine(ItemFooterNineBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class FooterHolderTen extends RecyclerView.ViewHolder {
        ItemFooterTenBinding binding;

        FooterHolderTen(ItemFooterTenBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }



}
