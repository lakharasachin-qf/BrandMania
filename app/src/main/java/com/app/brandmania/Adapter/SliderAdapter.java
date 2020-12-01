package com.app.brandmania.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.brandmania.Activity.RazorPayActivity;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
   private List<SliderItem> sliderItems;
   private ViewPager2 viewPager2;
    Activity activity;

    public SliderAdapter(List<SliderItem> sliderItems, Activity activity) {
        this.sliderItems = sliderItems;
        this.activity = activity;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container,parent,false));
    }

    @Override public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
            holder.setLayout(sliderItems.get(position));
            holder.packageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity, RazorPayActivity.class);
                    activity.startActivity(intent);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });
    }

    @Override public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder
    {
        private TextView priceForPay;
        private TextView packageTitle;
        private TextView templateTitle;
        private TextView imageTitle;
        private TextView payTitle;
        private TextView packageBtn;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            priceForPay=itemView.findViewById(R.id.priceForPay);
            packageTitle=itemView.findViewById(R.id.packageTitle);
            templateTitle=itemView.findViewById(R.id.templateTiltle);
            imageTitle=itemView.findViewById(R.id.ImageTiltle);
            payTitle=itemView.findViewById(R.id.payTiltle);
            packageBtn=itemView.findViewById(R.id.packageBtn);
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
