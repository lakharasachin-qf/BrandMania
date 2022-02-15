package com.app.brandmania.LetterHead.Adapter;

import static com.app.brandmania.Model.VisitingCardModel.LAYOUT_FIVE;
import static com.app.brandmania.Model.VisitingCardModel.LAYOUT_FOUR;
import static com.app.brandmania.Model.VisitingCardModel.LAYOUT_ONE;
import static com.app.brandmania.Model.VisitingCardModel.LAYOUT_THREE;
import static com.app.brandmania.Model.VisitingCardModel.LAYOUT_TWO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Adapter.VisitingCardAdapter;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.LetterHeadModel;
import com.app.brandmania.Model.VisitingCardModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ItemDigitalCardFiveBinding;
import com.app.brandmania.databinding.ItemDigitalCardFourBinding;
import com.app.brandmania.databinding.ItemDigitalCardOneBinding;
import com.app.brandmania.databinding.ItemDigitalCardThreeBinding;
import com.app.brandmania.databinding.ItemDigitalCardTwoBinding;
import com.app.brandmania.databinding.ItemLetterHeadCardOneBinding;
import com.app.brandmania.utils.Utility;
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
