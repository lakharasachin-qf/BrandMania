package com.make.mybrand.Adapter;


import static com.make.mybrand.Model.VisitingCardModel.LAYOUT_FIVE;
import static com.make.mybrand.Model.VisitingCardModel.LAYOUT_FOUR;
import static com.make.mybrand.Model.VisitingCardModel.LAYOUT_ONE;
import static com.make.mybrand.Model.VisitingCardModel.LAYOUT_THREE;
import static com.make.mybrand.Model.VisitingCardModel.LAYOUT_TWO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Activity.packages.PackageActivity;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.Model.VisitingCardModel;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ItemDigitalCardFiveBinding;
import com.make.mybrand.databinding.ItemDigitalCardFourBinding;
import com.make.mybrand.databinding.ItemDigitalCardOneBinding;
import com.make.mybrand.databinding.ItemDigitalCardThreeBinding;
import com.make.mybrand.databinding.ItemDigitalCardTwoBinding;
import com.make.mybrand.databinding.LayoutFooterElevenBinding;
import com.make.mybrand.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class VisitingCardAdapter extends RecyclerView.Adapter {
    private ArrayList<VisitingCardModel> footerModels;
    Activity activity;
    private boolean isLoadingAdded = false;
    private int checkedPosition = 0;
    public onVisitingCardListener cardListener;
    PreafManager preafManager;

    public VisitingCardAdapter setListener(onVisitingCardListener footerListener) {
        this.cardListener = footerListener;
        return this;
    }

    public interface onVisitingCardListener {
        void onCardChoose(int layout, VisitingCardModel visitingCardModel);
    }

    public VisitingCardAdapter(ArrayList<VisitingCardModel> footerModels, Activity activity) {
        this.footerModels = footerModels;
        this.activity = activity;
        this.preafManager = new PreafManager(activity);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {

            case LAYOUT_ONE:
                ItemDigitalCardOneBinding oneBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_digital_card_one, viewGroup, false);
                return new CardHolderOne(oneBinding);
            case LAYOUT_TWO:
                ItemDigitalCardTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_digital_card_two, viewGroup, false);
                return new CardHolderTwo(twoBinding);
            case LAYOUT_THREE:
                ItemDigitalCardThreeBinding threeBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_digital_card_three, viewGroup, false);
                return new CardHolderThree(threeBinding);
            case LAYOUT_FOUR:
                ItemDigitalCardFourBinding fourBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_digital_card_four, viewGroup, false);
                return new CardHolderFour(fourBinding);
            case LAYOUT_FIVE:
                ItemDigitalCardFiveBinding fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_digital_card_five, viewGroup, false);
                return new CardHolderFive(fiveBinding);

        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        switch (footerModels.get(position).getLayoutType()) {
            case 0:
                return LAYOUT_ONE;
            case 1:
                return LAYOUT_TWO;
            case 2:
                return LAYOUT_THREE;
            case 3:
                return LAYOUT_FOUR;
            case 4:
                return LAYOUT_FIVE;
            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return footerModels.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final VisitingCardModel model = footerModels.get(position);
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (model != null) {
            switch (model.getLayoutType()) {

                case LAYOUT_ONE:
                    ((CardHolderOne) holder).binding.itemLayout.setOnClickListener(v -> {
                        cardListener.onCardChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                        Picasso.get().load(activeBrand.getLogo()).into(((CardHolderOne) holder).binding.logo);
                        ((CardHolderOne) holder).binding.brandName.setText(activeBrand.getName());
                    });
                    if (!preafManager.getAllFreeImage()) {
                        if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
                            ((CardHolderOne) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
                        }
                    }
//                    if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
//                        ((CardHolderOne) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
//                    }

                    Picasso.get().load(activeBrand.getLogo()).into(((CardHolderOne) holder).binding.logo);
                    ((CardHolderOne) holder).binding.brandName.setText(activeBrand.getName());
                    break;
                case LAYOUT_TWO:
                    ((CardHolderTwo) holder).binding.itemLayout.setOnClickListener(v -> cardListener.onCardChoose(footerModels.get(position).getLayoutType(), footerModels.get(position)));

//                    if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
//                        ((CardHolderTwo) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
//                    }

                    if (!preafManager.getAllFreeImage()) {
                        if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
                            ((CardHolderTwo) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
                        }
                    }

                    Picasso.get().load(activeBrand.getLogo()).into(((CardHolderTwo) holder).binding.logo);
                    ((CardHolderTwo) holder).binding.webTxt.setText(activeBrand.getWebsite());
                    break;
                case LAYOUT_THREE:
                    ((CardHolderThree) holder).binding.itemLayout.setOnClickListener(v -> cardListener.onCardChoose(footerModels.get(position).getLayoutType(), footerModels.get(position)));
                    if (!preafManager.getAllFreeImage()) {
                        if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
                            ((CardHolderThree) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
                        }
                    }
                    Picasso.get().load(activeBrand.getLogo()).into(((CardHolderThree) holder).binding.logo);
                    break;
                case LAYOUT_FOUR:
                    ((CardHolderFour) holder).binding.itemLayout.setOnClickListener(v -> cardListener.onCardChoose(footerModels.get(position).getLayoutType(), footerModels.get(position)));
                    if (!preafManager.getAllFreeImage()) {
                        if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
                            ((CardHolderFour) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
                        }
                    }
//                    if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
//                        ((CardHolderFour) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
//                    }
                    Picasso.get().load(activeBrand.getLogo()).into(((CardHolderFour) holder).binding.logo);
                    ((CardHolderFour) holder).binding.brandName.setText(activeBrand.getName());
                    break;
                case LAYOUT_FIVE:
                    ((CardHolderFive) holder).binding.itemLayout.setOnClickListener(v -> cardListener.onCardChoose(footerModels.get(position).getLayoutType(), footerModels.get(position)));
                    if (!preafManager.getAllFreeImage()) {
                        if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
                            ((CardHolderFive) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
                        }
                    }
//                    if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
//                        ((CardHolderFive) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
//                    }
                    Picasso.get().load(activeBrand.getLogo()).into(((CardHolderFive) holder).binding.logo);
                    ((CardHolderFive) holder).binding.brandName.setText(activeBrand.getName());
                    break;
            }
        }
    }

    public void targetNextActivity() {
        Intent intent = new Intent(activity, PackageActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    static class CardHolderOne extends RecyclerView.ViewHolder {
        ItemDigitalCardOneBinding binding;

        CardHolderOne(ItemDigitalCardOneBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class CardHolderTwo extends RecyclerView.ViewHolder {
        ItemDigitalCardTwoBinding binding;

        CardHolderTwo(ItemDigitalCardTwoBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class CardHolderThree extends RecyclerView.ViewHolder {
        ItemDigitalCardThreeBinding binding;

        CardHolderThree(ItemDigitalCardThreeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class CardHolderFour extends RecyclerView.ViewHolder {
        ItemDigitalCardFourBinding binding;

        CardHolderFour(ItemDigitalCardFourBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class CardHolderFive extends RecyclerView.ViewHolder {
        ItemDigitalCardFiveBinding binding;

        CardHolderFive(ItemDigitalCardFiveBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

}