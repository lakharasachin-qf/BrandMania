package com.app.brandmania.Adapter;

import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_EIGHT;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_EIGHTEEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_ELEVEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FIFTEEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FIVE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FOUR;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_FOURTEEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_NINE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_NINETEEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_ONE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_SEVEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_SEVENTEEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_SIX;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_SIXTEEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_TEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_THIRTEEN;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_THREE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_TWELVE;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_TWENTY;
import static com.app.brandmania.Adapter.FooterModel.LAYOUT_FRAME_TWO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Common.PreafManager;
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
import com.app.brandmania.databinding.ItemPreviewEighteenBinding;
import com.app.brandmania.databinding.ItemPreviewElevenBinding;
import com.app.brandmania.databinding.ItemPreviewFifteenBinding;
import com.app.brandmania.databinding.ItemPreviewFourteenBinding;
import com.app.brandmania.databinding.ItemPreviewNineteenBinding;
import com.app.brandmania.databinding.ItemPreviewSeventeenBinding;
import com.app.brandmania.databinding.ItemPreviewSixteenBinding;
import com.app.brandmania.databinding.ItemPreviewThirteenBinding;
import com.app.brandmania.databinding.ItemPreviewTweloneBinding;
import com.app.brandmania.databinding.ItemPreviewTwentyBinding;

import java.util.ArrayList;


public class FooterAdapter extends RecyclerView.Adapter {
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

    public interface onFooterListener {
        void onFooterChoose(int footerLayout, FooterModel footerModel);
    }

    public FooterAdapter(ArrayList<FooterModel> footerModels, Activity activity) {
        this.footerModels = footerModels;
        this.activity = activity;
        this.preafManager = new PreafManager(activity);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {

            case LAYOUT_FRAME_ONE:
                ItemFooterOneBinding itemFooterOneBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_one, viewGroup, false);
                return new FooterHolderOne(itemFooterOneBinding);
            case LAYOUT_FRAME_TWO:
                ItemFooterTwoBinding itemFooterTwoBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_two, viewGroup, false);
                return new FooterHolderTwo(itemFooterTwoBinding);
            case LAYOUT_FRAME_THREE:
                ItemFooterThreeBinding itemFooterThreeBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_three, viewGroup, false);
                return new FooterHolderThree(itemFooterThreeBinding);
            case LAYOUT_FRAME_FOUR:
                ItemFooterFourBinding itemFooterFourBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_four, viewGroup, false);
                return new FooterHolderFour(itemFooterFourBinding);
            case LAYOUT_FRAME_FIVE:
                ItemFooterFiveBinding itemFooterFiveBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_five, viewGroup, false);
                return new FooterHolderFive(itemFooterFiveBinding);
            case LAYOUT_FRAME_SIX:
                ItemFooterLayoutSixBinding itemFooterLayoutSixBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_layout_six, viewGroup, false);
                return new FooterHolderSix(itemFooterLayoutSixBinding);
            case LAYOUT_FRAME_SEVEN:
                ItemFooterSevenBinding itemFooterSevenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_seven, viewGroup, false);
                return new FooterHolderSeven(itemFooterSevenBinding);
            case LAYOUT_FRAME_EIGHT:
                ItemFooterEightBinding itemFooterEightBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_eight, viewGroup, false);
                return new FooterHolderEight(itemFooterEightBinding);
            case LAYOUT_FRAME_NINE:
                ItemFooterNineBinding itemFooterNineBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_nine, viewGroup, false);
                return new FooterHolderNine(itemFooterNineBinding);
            case LAYOUT_FRAME_TEN:
                ItemFooterTenBinding itemFooterTenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_footer_ten, viewGroup, false);
                return new FooterHolderTen(itemFooterTenBinding);
            case LAYOUT_FRAME_ELEVEN:
                ItemPreviewElevenBinding footerLayoutElevenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_eleven, viewGroup, false);
                return new FooterHolderEleven(footerLayoutElevenBinding);
            case LAYOUT_FRAME_TWELVE:
                ItemPreviewTweloneBinding tweloneBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_twelone, viewGroup, false);
                return new FooterHolderTwelve(tweloneBinding);
            case LAYOUT_FRAME_THIRTEEN:
                ItemPreviewThirteenBinding previewThirteenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_thirteen, viewGroup, false);
                return new FooterHolderThirteen(previewThirteenBinding);
            case LAYOUT_FRAME_FOURTEEN:
                ItemPreviewFourteenBinding fourteenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_fourteen, viewGroup, false);
                return new FooterHolderFourteen(fourteenBinding);
            case LAYOUT_FRAME_FIFTEEN:
                ItemPreviewFifteenBinding itemPreviewFifteenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_fifteen, viewGroup, false);
                return new FooterHolderFifteen(itemPreviewFifteenBinding);
            case LAYOUT_FRAME_SIXTEEN:
                ItemPreviewSixteenBinding itemPreviewSixteenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_sixteen, viewGroup, false);
                return new FooterHolderSixteen(itemPreviewSixteenBinding);
            case LAYOUT_FRAME_SEVENTEEN:
                ItemPreviewSeventeenBinding previewSeventeenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_seventeen, viewGroup, false);
                return new FooterHolderSevenTeen(previewSeventeenBinding);
            case LAYOUT_FRAME_EIGHTEEN:
                ItemPreviewEighteenBinding previewEighteenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_eighteen, viewGroup, false);
                return new FooterHolderFEighteen(previewEighteenBinding);
            case LAYOUT_FRAME_NINETEEN:
                ItemPreviewNineteenBinding previewNineteenBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_nineteen, viewGroup, false);
                return new FooterHolderNineTeen(previewNineteenBinding);
            case LAYOUT_FRAME_TWENTY:
                ItemPreviewTwentyBinding twentyBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_preview_twenty, viewGroup, false);
                return new FooterHolderTwenty(twentyBinding);
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
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
            case 11:
                return LAYOUT_FRAME_ELEVEN;
            case 12:
                return LAYOUT_FRAME_TWELVE;
            case 13:
                return LAYOUT_FRAME_THIRTEEN;
            case 14:
                return LAYOUT_FRAME_FOURTEEN;
            case 15:
                return LAYOUT_FRAME_FIFTEEN;
            case 16:
                return LAYOUT_FRAME_SIXTEEN;
            case 17:
                return LAYOUT_FRAME_SEVENTEEN;
            case 18:
                return LAYOUT_FRAME_EIGHTEEN;
            case 19:
                return LAYOUT_FRAME_NINETEEN;
            case 20:
                return LAYOUT_FRAME_TWENTY;
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
        final FooterModel model = footerModels.get(position);
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (model != null) {
            switch (model.getLayoutType()) {

                case LAYOUT_FRAME_ONE:
                    ((FooterHolderOne) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));

                                ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);

                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);

                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderOne) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderOne) holder).binding.freePremium.setVisibility(View.VISIBLE);

                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderOne) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderOne) holder).binding.gmailLayout.setVisibility(View.GONE);
                        ((FooterHolderOne) holder).binding.contactLayout.setGravity(Gravity.CENTER);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderOne) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderOne) holder).binding.contactLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderOne) holder).binding.locationText.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderOne) holder).binding.addressLayoutElement.setVisibility(View.GONE);
                    }
                    if (checkedPosition == position) {
                        ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderOne) holder).binding.freePremium.setVisibility(View.GONE);
                        ((FooterHolderOne) holder).binding.elementPremium.setVisibility(View.GONE);

                    } else {
                        ((FooterHolderOne) holder).binding.elementSelected.setVisibility(View.GONE);

                    }

                    break;
                case LAYOUT_FRAME_TWO:
                    ((FooterHolderTwo) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {

                        ((FooterHolderTwo) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                    } else {

                        ((FooterHolderTwo) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderTwo) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderTwo) holder).binding.gmailLayout.setVisibility(View.GONE);
                        ((FooterHolderTwo) holder).binding.contactLayout.setGravity(Gravity.CENTER);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderTwo) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderTwo) holder).binding.contactLayout.setVisibility(View.GONE);
                    }
                    if (activeBrand.getPhonenumber().isEmpty() && activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderTwo) holder).binding.firstView.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderTwo) holder).binding.locationText.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderTwo) holder).binding.locationLayout.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getWebsite().isEmpty()) {
                        ((FooterHolderTwo) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    } else {
                        ((FooterHolderTwo) holder).binding.websiteLayout.setVisibility(View.GONE);
                        ((FooterHolderTwo) holder).binding.locationLayout.setGravity(Gravity.CENTER);
                    }
                    if (activeBrand.getWebsite().equals("https://")) {
                        ((FooterHolderTwo) holder).binding.websiteLayout.setVisibility(View.GONE);
                        ((FooterHolderTwo) holder).binding.locationLayout.setGravity(Gravity.CENTER);
                    } else {
                        ((FooterHolderTwo) holder).binding.websiteLayout.setVisibility(View.VISIBLE);
                    }

                    if (activeBrand.getAddress().isEmpty() && activeBrand.getWebsite().isEmpty()) {
                        ((FooterHolderTwo) holder).binding.secondView.setVisibility(View.GONE);
                    }


                    if (checkedPosition == position) {
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderTwo) holder).binding.freePremium.setVisibility(View.GONE);
                        ((FooterHolderTwo) holder).binding.elementPremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderTwo) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_THREE:
                    ((FooterHolderThree) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderThree) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderThree) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderThree) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderThree) holder).binding.gmailLayout.setVisibility(View.GONE);
                        ((FooterHolderThree) holder).binding.contactLayout.setGravity(Gravity.CENTER);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderThree) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderThree) holder).binding.contactLayout.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderThree) holder).binding.locationText.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderThree) holder).binding.loactionLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getWebsite().isEmpty()) {
                        ((FooterHolderThree) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    } else {
                        ((FooterHolderThree) holder).binding.websiteEdtLayout.setVisibility(View.GONE);
                        ((FooterHolderThree) holder).binding.loactionLayout.setGravity(Gravity.CENTER);
                    }
                    if (activeBrand.getWebsite().equals("https://")) {
                        ((FooterHolderThree) holder).binding.websiteEdtLayout.setVisibility(View.GONE);
                        ((FooterHolderThree) holder).binding.loactionLayout.setGravity(Gravity.CENTER);
                    } else {
                        ((FooterHolderThree) holder).binding.websiteEdtLayout.setVisibility(View.VISIBLE);
                    }
                    if (checkedPosition == position) {
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderThree) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderThree) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderThree) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;
                case LAYOUT_FRAME_FOUR:
                    ((FooterHolderFour) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderFour) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {

                        ((FooterHolderFour) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderFour) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderFour) holder).binding.gmailLayout.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.topView2.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.topView22.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderFour) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderFour) holder).binding.contactLayout.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderFour) holder).binding.locationText.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderFour) holder).binding.locationLayout.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getWebsite().isEmpty()) {
                        ((FooterHolderFour) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    } else {
                        ((FooterHolderFour) holder).binding.websiteLayout.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.topView2.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.topView22.setVisibility(View.VISIBLE);
                    }
                    if (activeBrand.getWebsite().equals("https://")) {
                        ((FooterHolderFour) holder).binding.websiteLayout.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.topView2.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.topView22.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderFour) holder).binding.websiteLayout.setVisibility(View.VISIBLE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderFour) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderFour) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderFour) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_FIVE:
                    ((FooterHolderFive) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {

                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);

                            }

                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderFive) holder).binding.elementPremium.setVisibility(View.VISIBLE);


                    } else {
                        ((FooterHolderFive) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderFive) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderFive) holder).binding.elementEmail.setVisibility(View.GONE);
                        ((FooterHolderFive) holder).binding.elementMobile.setGravity(Gravity.CENTER);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderFive) holder).binding.phoneTxt.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderFive) holder).binding.elementMobile.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getWebsite().isEmpty()) {
                        ((FooterHolderFive) holder).binding.websiteText.setText(activeBrand.getWebsite());
                    } else {
                        ((FooterHolderFive) holder).binding.element0.setVisibility(View.GONE);
                    }
                    if (activeBrand.getWebsite().equals("https://")) {
                        ((FooterHolderFive) holder).binding.element0.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderFive) holder).binding.element0.setVisibility(View.VISIBLE);
                    }
                    if (checkedPosition == position) {
                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderFive) holder).binding.freePremium.setVisibility(View.GONE);
                        ((FooterHolderFive) holder).binding.elementPremium.setVisibility(View.GONE);
                    } else {

                        ((FooterHolderFive) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;
                case LAYOUT_FRAME_SIX:
                    ((FooterHolderSix) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderSix) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderSix) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderSix) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderSix) holder).binding.contactLayout.setVisibility(View.GONE);
                    }
                    if (checkedPosition == position) {
                        ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderSix) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderSix) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderSix) holder).binding.elementSelected.setVisibility(View.GONE);
                    }


                    break;

                case LAYOUT_FRAME_SEVEN:
                    ((FooterHolderSeven) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderSeven) holder).binding.elementPremium.setVisibility(View.VISIBLE);


                    } else {


                        ((FooterHolderSeven) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderSeven) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderSeven) holder).binding.contactLayout.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderSeven) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderSeven) holder).binding.gmailLayout.setVisibility(View.GONE);
                        ((FooterHolderSeven) holder).binding.element.setVisibility(View.GONE);
                        ((FooterHolderSeven) holder).binding.contactLayout.setGravity(Gravity.CENTER);
                    }
                    if (!activeBrand.getName().isEmpty()) {
                        ((FooterHolderSeven) holder).binding.brandNameText.setText(activeBrand.getName());
                    } else {
                        ((FooterHolderSeven) holder).binding.brandNameText.setVisibility(View.GONE);
                    }


                    if (checkedPosition == position) {
                        ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderSeven) holder).binding.freePremium.setVisibility(View.GONE);
                        ((FooterHolderSeven) holder).binding.elementPremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderSeven) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;

                case LAYOUT_FRAME_EIGHT:
                    ((FooterHolderEight) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderEight) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderEight) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderEight) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderEight) holder).binding.contactLayout.setVisibility(View.GONE);

                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderEight) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderEight) holder).binding.gmailLayout.setVisibility(View.GONE);
                        ((FooterHolderEight) holder).binding.element.setVisibility(View.GONE);
                        ((FooterHolderEight) holder).binding.contactLayout.setGravity(Gravity.CENTER);
                    }

                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderEight) holder).binding.locationText.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderEight) holder).binding.addressLayoutElement.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getName().isEmpty()) {
                        ((FooterHolderEight) holder).binding.brandNameText.setText(activeBrand.getName());
                    } else {
                        ((FooterHolderEight) holder).binding.addressLayoutElement.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderEight) holder).binding.freePremium.setVisibility(View.GONE);
                        ((FooterHolderEight) holder).binding.elementPremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderEight) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;


                case LAYOUT_FRAME_NINE:
                    ((FooterHolderNine) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderNine) holder).binding.elementPremium.setVisibility(View.VISIBLE);
                    } else {
                        ((FooterHolderNine) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }


                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderNine) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderNine) holder).binding.contactText.append("\n");
                        ((FooterHolderNine) holder).binding.contactText.append(activeBrand.getEmail());
                    }

                    if (!activeBrand.getName().isEmpty()) {
                        ((FooterHolderNine) holder).binding.brandNameText.setText(activeBrand.getName());
                    } else {
                        ((FooterHolderNine) holder).binding.brandNameText.setVisibility(View.GONE);
                    }
                    if (checkedPosition == position) {
                        ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderNine) holder).binding.freePremium.setVisibility(View.GONE);
                        ((FooterHolderNine) holder).binding.elementPremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderNine) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;


                case LAYOUT_FRAME_TEN:
                    ((FooterHolderTen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderTen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderTen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderTen) holder).binding.contactText.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderTen) holder).binding.contactText.setVisibility(View.GONE);
                        ((FooterHolderTen) holder).binding.callImage.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderTen) holder).binding.gmailText.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderTen) holder).binding.gmailText.setVisibility(View.GONE);
                        ((FooterHolderTen) holder).binding.gmailImage.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderTen) holder).binding.locationText.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderTen) holder).binding.locationText.setVisibility(View.GONE);
                        ((FooterHolderTen) holder).binding.locationImage.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderTen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderTen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderTen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }

                    break;
                case LAYOUT_FRAME_ELEVEN:
                    ((FooterHolderEleven) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderEleven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderEleven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderEleven) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderEleven) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderEleven) holder).binding.phoneTxt.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderEleven) holder).binding.phoneTxt.setVisibility(View.GONE);
                        ((FooterHolderEleven) holder).binding.call.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderEleven) holder).binding.emailEdt.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderEleven) holder).binding.emailEdt.setVisibility(View.GONE);
                        ((FooterHolderEleven) holder).binding.email.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderEleven) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderEleven) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderEleven) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderEleven) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_TWELVE:
                    ((FooterHolderTwelve) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTwelve) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTwelve) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderTwelve) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderTwelve) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderTwelve) holder).binding.mobileNo.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderTwelve) holder).binding.mobileNo.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderTwelve) holder).binding.emailTxt.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderTwelve) holder).binding.emailTxt.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderTwelve) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderTwelve) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderTwelve) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderTwelve) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_THIRTEEN:
                    ((FooterHolderThirteen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderThirteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderThirteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderThirteen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderThirteen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderThirteen) holder).binding.mobileNo.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderThirteen) holder).binding.mobileNo.setVisibility(View.GONE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderThirteen) holder).binding.emailTxt.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderThirteen) holder).binding.emailTxt.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderThirteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderThirteen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderThirteen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderThirteen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_FOURTEEN:
                    ((FooterHolderFourteen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFourteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFourteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderFourteen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderFourteen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderFourteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderFourteen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderFourteen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderFourteen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_FIFTEEN:
                    ((FooterHolderFifteen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFifteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFifteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderFifteen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderFifteen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }


                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderFifteen) holder).binding.address.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderFifteen) holder).binding.address.setVisibility(View.GONE);
                        ((FooterHolderFifteen) holder).binding.locationPin.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderFifteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderFifteen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderFifteen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderFifteen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_SIXTEEN:
                    ((FooterHolderSixteen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSixteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSixteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderSixteen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderSixteen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }


                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderSixteen) holder).binding.address.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderSixteen) holder).binding.address.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderSixteen) holder).binding.email.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderSixteen) holder).binding.email.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderSixteen) holder).binding.mobileNo.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderSixteen) holder).binding.mobileNo.setVisibility(View.GONE);
                    }


                    if (checkedPosition == position) {
                        ((FooterHolderSixteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderSixteen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderSixteen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderSixteen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;

                case LAYOUT_FRAME_SEVENTEEN:
                    ((FooterHolderSevenTeen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSevenTeen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderSevenTeen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderSevenTeen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderSevenTeen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }


                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderSevenTeen) holder).binding.address.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderSevenTeen) holder).binding.address.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderSevenTeen) holder).binding.emailTxt.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderSevenTeen) holder).binding.emailTxt.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderSevenTeen) holder).binding.mobileNo.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderSevenTeen) holder).binding.mobileNo.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderSevenTeen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderSevenTeen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderSevenTeen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderSevenTeen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;

                case LAYOUT_FRAME_EIGHTEEN:
                    ((FooterHolderFEighteen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFEighteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderFEighteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderFEighteen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderFEighteen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderFEighteen) holder).binding.address.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderFEighteen) holder).binding.address.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderFEighteen) holder).binding.mobileNo.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderFEighteen) holder).binding.mobileNo.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderFEighteen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderFEighteen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderFEighteen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderFEighteen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;

                case LAYOUT_FRAME_NINETEEN:
                    ((FooterHolderNineTeen) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderNineTeen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderNineTeen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });


                    if (!model.isFree()) {
                        ((FooterHolderNineTeen) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderNineTeen) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderNineTeen) holder).binding.address.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderNineTeen) holder).binding.address.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderNineTeen) holder).binding.emailTxt.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderNineTeen) holder).binding.emailTxt.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderNineTeen) holder).binding.mobileNo.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderNineTeen) holder).binding.mobileNo.setVisibility(View.GONE);
                    }

                    if (checkedPosition == position) {
                        ((FooterHolderNineTeen) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderNineTeen) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderNineTeen) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderNineTeen) holder).binding.elementSelected.setVisibility(View.GONE);
                    }
                    break;
                case LAYOUT_FRAME_TWENTY:
                    ((FooterHolderTwenty) holder).binding.footerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isFree()) {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTwenty) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            } else {
                                checkedPosition = position;
                                footerListener.onFooterChoose(footerModels.get(position).getLayoutType(), footerModels.get(position));
                                ((FooterHolderTwenty) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if (!model.isFree()) {
                        ((FooterHolderTwenty) holder).binding.elementPremium.setVisibility(View.VISIBLE);

                    } else {
                        ((FooterHolderTwenty) holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                    if (!activeBrand.getAddress().isEmpty()) {
                        ((FooterHolderTwenty) holder).binding.address.setText(activeBrand.getAddress());
                    } else {
                        ((FooterHolderTwenty) holder).binding.address.setVisibility(View.GONE);
                    }

                    if (!activeBrand.getEmail().isEmpty()) {
                        ((FooterHolderTwenty) holder).binding.emailTxt.setText(activeBrand.getEmail());
                    } else {
                        ((FooterHolderTwenty) holder).binding.emailTxt.setVisibility(View.GONE);
                    }
                    if (!activeBrand.getPhonenumber().isEmpty()) {
                        ((FooterHolderTwenty) holder).binding.mobileNo.setText(activeBrand.getPhonenumber());
                    } else {
                        ((FooterHolderTwenty) holder).binding.mobileNo.setVisibility(View.GONE);
                    }
                    if (checkedPosition == position) {
                        ((FooterHolderTwenty) holder).binding.elementSelected.setVisibility(View.VISIBLE);
                        ((FooterHolderTwenty) holder).binding.elementPremium.setVisibility(View.GONE);
                        ((FooterHolderTwenty) holder).binding.freePremium.setVisibility(View.GONE);
                    } else {
                        ((FooterHolderTwenty) holder).binding.elementSelected.setVisibility(View.GONE);
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

    static class FooterHolderEleven extends RecyclerView.ViewHolder {
        ItemPreviewElevenBinding binding;

        FooterHolderEleven(ItemPreviewElevenBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    private class FooterHolderTwelve extends RecyclerView.ViewHolder {
        ItemPreviewTweloneBinding binding;

        public FooterHolderTwelve(ItemPreviewTweloneBinding tweloneBinding) {
            super(tweloneBinding.getRoot());
            binding = tweloneBinding;
        }
    }

    private class FooterHolderThirteen extends RecyclerView.ViewHolder {
        ItemPreviewThirteenBinding binding;

        public FooterHolderThirteen(ItemPreviewThirteenBinding previewThirteenBinding) {
            super(previewThirteenBinding.getRoot());
            binding = previewThirteenBinding;
        }
    }

    private class FooterHolderFourteen extends RecyclerView.ViewHolder {
        ItemPreviewFourteenBinding binding;

        public FooterHolderFourteen(ItemPreviewFourteenBinding previewFourteenBinding) {
            super(previewFourteenBinding.getRoot());
            binding = previewFourteenBinding;
        }
    }

    private class FooterHolderFifteen extends RecyclerView.ViewHolder {
        ItemPreviewFifteenBinding binding;

        public FooterHolderFifteen(ItemPreviewFifteenBinding previewFourteenBinding) {
            super(previewFourteenBinding.getRoot());
            binding = previewFourteenBinding;
        }
    }

    private class FooterHolderSixteen extends RecyclerView.ViewHolder {
        ItemPreviewSixteenBinding binding;

        public FooterHolderSixteen(ItemPreviewSixteenBinding previewFourteenBinding) {
            super(previewFourteenBinding.getRoot());
            binding = previewFourteenBinding;
        }
    }

    private class FooterHolderSevenTeen extends RecyclerView.ViewHolder {
        ItemPreviewSeventeenBinding binding;

        public FooterHolderSevenTeen(ItemPreviewSeventeenBinding previewFourteenBinding) {
            super(previewFourteenBinding.getRoot());
            binding = previewFourteenBinding;
        }
    }

    private class FooterHolderFEighteen extends RecyclerView.ViewHolder {
        ItemPreviewEighteenBinding binding;

        public FooterHolderFEighteen(ItemPreviewEighteenBinding previewFourteenBinding) {
            super(previewFourteenBinding.getRoot());
            binding = previewFourteenBinding;
        }
    }

    private class FooterHolderNineTeen extends RecyclerView.ViewHolder {
        ItemPreviewNineteenBinding binding;

        public FooterHolderNineTeen(ItemPreviewNineteenBinding previewFourteenBinding) {
            super(previewFourteenBinding.getRoot());
            binding = previewFourteenBinding;
        }
    }

    private class FooterHolderTwenty extends RecyclerView.ViewHolder {
        ItemPreviewTwentyBinding binding;

        public FooterHolderTwenty(ItemPreviewTwentyBinding previewFourteenBinding) {
            super(previewFourteenBinding.getRoot());
            binding = previewFourteenBinding;
        }
    }
}
