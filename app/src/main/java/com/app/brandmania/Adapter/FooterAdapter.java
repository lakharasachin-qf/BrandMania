package com.app.brandmania.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Activity.PackageActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ItemFooterFiveBinding;
import com.app.brandmania.databinding.ItemFooterFourBinding;
import com.app.brandmania.databinding.ItemFooterLayoutSixBinding;
import com.app.brandmania.databinding.ItemFooterOneBinding;
import com.app.brandmania.databinding.ItemFooterThreeBinding;
import com.app.brandmania.databinding.ItemFooterTwoBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FIVE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FOUR;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_ONE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_SIX;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_THREE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_TWO;
import static com.app.brandmania.Model.ImageList.LAYOUT_LOADING;


public class FooterAdapter extends RecyclerView.Adapter{
    private ArrayList<FooterModel> footerModels;
    Activity activity;
    private boolean isLoadingAdded = false;
    private int checkedPosition = -1;
    public onFooterListener footerListener;
    PreafManager preafManager;

    public FooterAdapter setFooterListener(onFooterListener footerListener) {
        this.footerListener = footerListener;
        return this;
    }

    public interface onFooterListener{
        void onFooterChoose(int footerLayout);
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

        }
        return null;

    }
    @Override public int getItemViewType(int position) {
        if (position == footerModels.size() - 1 && isLoadingAdded)
            return LAYOUT_LOADING;
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
        Log.e("NOTIFY", String.valueOf(checkedPosition));
        if (model != null) {
            switch (model.getLayoutType()) {

                case LAYOUT_FRAME_ONE:
                    ((FooterHolderOne) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkedPosition=position;
                            footerListener.onFooterChoose(footerModels.get(position).getLayoutType());
                            ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();

                        }
                    });
                    if (checkedPosition==position){
                        ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    }else {
                        ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;
                case LAYOUT_FRAME_TWO:
                    ((FooterHolderTwo) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkedPosition=position;
                            footerListener.onFooterChoose(footerModels.get(position).getLayoutType());
                            ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        }
                    });
                    if (checkedPosition==position){
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    }else {
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_THREE:
                    ((FooterHolderThree) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("1")) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType());
                                ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }else{
                                Intent intent=new Intent(activity, PackageActivity.class);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                        }
                    });
                    if (checkedPosition==position){
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    }else {
                        if (preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("1")){
                            ((FooterHolderThree) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                            ((FooterHolderThree) holder).binding.elementTxt.setText("Premium");
                        }
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;
                case LAYOUT_FRAME_FOUR:
                    ((FooterHolderFour) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkedPosition=position;
                            footerListener.onFooterChoose(footerModels.get(position).getLayoutType());
                            ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        }
                    });
                    if (checkedPosition==position){
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    }else {
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_FIVE:
                    ((FooterHolderFive) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("1")) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType());
                                ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }else{
                                Intent intent=new Intent(activity, PackageActivity.class);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }

                        }
                    });
                    if (checkedPosition==position){
                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    }else {
                        if (preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("1")){
                            ((FooterHolderFive) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                        }
                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_SIX:
                    ((FooterHolderSix) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("1")) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType());
                                ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }else{
                                Intent intent=new Intent(activity, PackageActivity.class);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                        }
                    });
                    if (checkedPosition==position){
                        ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                    }else {
                        if (preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("1")){
                            ((FooterHolderSix) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                        }
                        ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
            }
        }
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
}
