package com.app.brandmania.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
   private List<SliderItem> sliderItems;
   private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
            holder.setLayout(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder
    {
        private TextView priceForPay;
        private TextView packageTitle;
        private TextView templateTitle;
        private TextView imageTitle;
        private TextView payTitle;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            priceForPay=itemView.findViewById(R.id.priceForPay);
            packageTitle=itemView.findViewById(R.id.packageTitle);
            templateTitle=itemView.findViewById(R.id.templateTiltle);
            imageTitle=itemView.findViewById(R.id.ImageTiltle);
            payTitle=itemView.findViewById(R.id.payTiltle);
        }
        void setLayout(SliderItem sliderItem)
        {

            priceForPay.setText(sliderItem.getPriceForPay());
            packageTitle.setText(sliderItem.getPackageTitle());
            templateTitle.setText(sliderItem.getTemplateTitle());
            imageTitle.setText(sliderItem.getImageTitle());
            payTitle.setText(sliderItem.getPayTitle());
        }
    }
}
