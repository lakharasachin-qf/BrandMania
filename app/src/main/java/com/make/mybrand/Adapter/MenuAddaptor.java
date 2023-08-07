package com.make.mybrand.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Activity.custom.CustomViewAllActivit;
import com.make.mybrand.Interface.FrameInterFace;
import com.make.mybrand.Interface.ItemeInterFace;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ItemFaqLayoutBinding;
import com.make.mybrand.databinding.ItemLayoutHomeBinding;
import com.make.mybrand.databinding.ItemLayoutViewallimageBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.make.mybrand.Adapter.MultiListItem.ACTIVITY_VIEWALLFRAME;
import static com.make.mybrand.Adapter.MultiListItem.ACTIVITY_VIEWALLIMAGE;
import static com.make.mybrand.Adapter.MultiListItem.LAYOUT_COLOR;
import static com.make.mybrand.Adapter.MultiListItem.LAYOUT_FAQ;
import static com.make.mybrand.Adapter.MultiListItem.LAYOUT_RECOMMANDATION;

public class MenuAddaptor extends RecyclerView.Adapter {
    private final ArrayList<MultiListItem> menuModels;
    private boolean isLoadingAdded = false;
    Activity activity;
    private final Gson gson;

    public MenuAddaptor(ArrayList<MultiListItem> menuModels, Activity activity) {
        this.menuModels = menuModels;
        gson = new Gson();
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {

            case LAYOUT_RECOMMANDATION:
                ItemLayoutHomeBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_home, parent, false);
                return new HomeHolder(layoutBinding);
            case LAYOUT_COLOR:
                ItemLayoutHomeBinding layoutBinding1 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_home, parent, false);
                return new HomeHolder(layoutBinding1);
            case MultiListItem.LAYOUT_FAQ:
                ItemFaqLayoutBinding faqLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_faq_layout, parent, false);
                return new FaqHolder(faqLayoutBinding);

            case ACTIVITY_VIEWALLIMAGE:
                ItemLayoutViewallimageBinding meetingsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_viewallimage, parent, false);
                return new ViewAllHolder(meetingsLayoutBinding);
            case ACTIVITY_VIEWALLFRAME:
                ItemLayoutViewallimageBinding meetingsLayoutBinding1 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_viewallimage, parent, false);
                return new ViewAllHolder(meetingsLayoutBinding1);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (menuModels.get(position).getLayoutType()) {
            case 1:
                return LAYOUT_RECOMMANDATION;
            case 2:
                return LAYOUT_COLOR;
            case 3:
                return LAYOUT_FAQ;
            case 22:
                return ACTIVITY_VIEWALLIMAGE;
            case 23:
                return ACTIVITY_VIEWALLFRAME;
            default:
                return -1;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MultiListItem model = menuModels.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_RECOMMANDATION:
                    ((HomeHolder) holder).binding.image.setImageResource(model.getImage());
                    ((HomeHolder) holder).binding.itemLayout.setOnClickListener(v -> ((ItemeInterFace) activity).onItemSelection(position, model));

                    if (CustomViewAllActivit.VIEW_RECOMDATION == 0) {
                        ((HomeHolder) holder).binding.image.setOnClickListener(v -> {
                            Intent i = new Intent(activity, CustomViewAllActivit.class);
                            i.putExtra("flag", CustomViewAllActivit.VIEW_RECOMDATION);
                            i.putExtra("detailsObj", gson.toJson(model));
                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        });
                    }
                    break;


                case LAYOUT_COLOR:
                    ((HomeHolder) holder).binding.image.setImageResource(model.getImage());
                    break;

                case LAYOUT_FAQ:
                    ((FaqHolder) holder).binding.answerTxt.setText(model.getAnswer());
                    ((FaqHolder) holder).binding.questionTxt.setText(model.getQuestion() + " ?");
                    break;

                case ACTIVITY_VIEWALLIMAGE:
                    ((ViewAllHolder) holder).binding.image.setImageResource(model.getImage());
                    ;

                    ((ViewAllHolder) holder).binding.image.setImageResource(model.getImage());
                    ;

                    ((ViewAllHolder) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ItemeInterFace) activity).onItemSelection(position, model);
                        }
                    });
                    break;

                case ACTIVITY_VIEWALLFRAME:
                    ((ViewAllHolder) holder).binding.image.setImageResource(model.getImage());

                    ((ViewAllHolder) holder).binding.image.setImageResource(model.getImage());

                    ((ViewAllHolder) holder).binding.itemLayout.setOnClickListener(v -> ((FrameInterFace) activity).onFrameItemSelection(position, model));
                    break;
            }

        }


    }


    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    static class HomeHolder extends RecyclerView.ViewHolder {
        ItemLayoutHomeBinding binding;

        HomeHolder(ItemLayoutHomeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    static class FaqHolder extends RecyclerView.ViewHolder {
        ItemFaqLayoutBinding binding;

        FaqHolder(ItemFaqLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class ViewAllHolder extends RecyclerView.ViewHolder {
        ItemLayoutViewallimageBinding binding;

        ViewAllHolder(ItemLayoutViewallimageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    public interface HandleEventInterface {
        void onEventFired(int position, MultiListItem model, String flag);
    }

}
