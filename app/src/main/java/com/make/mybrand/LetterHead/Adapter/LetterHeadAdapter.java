package com.make.mybrand.LetterHead.Adapter;

import static com.make.mybrand.Model.VisitingCardModel.LAYOUT_ONE;
import static com.make.mybrand.Model.VisitingCardModel.LAYOUT_TWO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.Model.LetterHeadModel;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ItemDigitalCardFiveBinding;
import com.make.mybrand.databinding.ItemDigitalCardFourBinding;
import com.make.mybrand.databinding.ItemDigitalCardOneBinding;
import com.make.mybrand.databinding.ItemDigitalCardThreeBinding;
import com.make.mybrand.databinding.ItemDigitalCardTwoBinding;
import com.make.mybrand.databinding.ItemLetterHeadCardOneBinding;
import com.make.mybrand.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LetterHeadAdapter extends RecyclerView.Adapter {
    private ArrayList<LetterHeadModel> footerModels;
    Activity activity;
    private boolean isLoadingAdded = false;
    private int checkedPosition = 0;
    public LetterHeadAdapter.onLetterHeadListener cardListener;
    PreafManager preafManager;


    public LetterHeadAdapter setListener(LetterHeadAdapter.onLetterHeadListener footerListener) {
        this.cardListener = footerListener;
        return this;
    }

    public interface onLetterHeadListener {
        void onCardChoose(int layout, LetterHeadModel letterHeadModel);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        switch (i) {

            case LAYOUT_ONE:
                ItemLetterHeadCardOneBinding oneBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_letter_head_card_one, parent, false);
                return new LetterHeadAdapter.CardHolderOne(oneBinding);
            case LAYOUT_TWO:
                ItemDigitalCardTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_digital_card_two, parent, false);
                return new LetterHeadAdapter.CardHolderTwo(twoBinding);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final LetterHeadModel model = footerModels.get(position);
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (model != null) {
            switch (model.getLayoutType()) {

                case LAYOUT_ONE:
                    ((LetterHeadAdapter.CardHolderOne) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cardListener.onCardChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                            Picasso.get().load(activeBrand.getLogo()).into(((LetterHeadAdapter.CardHolderOne) holder).binding.logo);
                        }
                    });

                    break;
                case LAYOUT_TWO:
                    ((LetterHeadAdapter.CardHolderTwo) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cardListener.onCardChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));

                        }
                    });

                    if (activeBrand.getIs_payment_pending().equalsIgnoreCase("1") || Utility.isPackageExpired(activity)) {
                        ((LetterHeadAdapter.CardHolderTwo) holder).binding.paidUserCard.setVisibility(View.VISIBLE);
                    }
                    Picasso.get().load(activeBrand.getLogo()).into(((LetterHeadAdapter.CardHolderTwo) holder).binding.logo);
                    ((LetterHeadAdapter.CardHolderTwo) holder).binding.webTxt.setText(activeBrand.getWebsite());
                    break;

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (footerModels.get(position).getLayoutType()) {
            case 0:
                return LAYOUT_ONE;
            case 1:
                return LAYOUT_TWO;
            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {

        return footerModels.size();
    }

    static class CardHolderOne extends RecyclerView.ViewHolder {
        ItemLetterHeadCardOneBinding binding;

        CardHolderOne(ItemLetterHeadCardOneBinding itemView) {
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

    public LetterHeadAdapter(ArrayList<LetterHeadModel> footerModels, Activity activity) {
        this.footerModels = footerModels;
        this.activity = activity;
        this.preafManager = new PreafManager(activity);

    }
}
