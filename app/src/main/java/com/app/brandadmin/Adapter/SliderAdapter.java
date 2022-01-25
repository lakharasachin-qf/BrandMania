package com.app.brandadmin.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.brandadmin.Activity.packages.RazorPayActivity;
import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Model.SliderItem;
import com.app.brandadmin.R;
import com.google.gson.Gson;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
   private List<SliderItem> sliderItems;
   private ViewPager2 viewPager2;
    Activity activity;
    private String brandId;
    PreafManager preafManager;
    Gson gson;
    public SliderAdapter(List<SliderItem> sliderItems, Activity activity, String selectedBrand) {
        this.sliderItems = sliderItems;
        this.activity = activity;
        brandId=selectedBrand;
        gson=new Gson();
        preafManager=new PreafManager(activity);
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
                    intent.putExtra("AmountText", holder.priceForPay.getText().toString());
                    sliderItems.get(position).setBrandId(brandId);
                    intent.putExtra("detailsObj",gson.toJson(sliderItems.get(position)));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });
            holder.payTitle.setText(activity.getString(R.string.Rs)+sliderItems.get(position).getPriceForPay()+" / "+sliderItems.get(position).getDuration());
            if (activity.getIntent().hasExtra("Profile") && preafManager.getActiveBrand().getPackage_id().equals(sliderItems.get(position).getPackageid()) && preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("0")){
                holder.packageBtn.setVisibility(View.GONE);
                holder.subcribedBtn.setVisibility(View.VISIBLE);
            }
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
        private TextView subcribedBtn;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            priceForPay=itemView.findViewById(R.id.priceForPay);
            packageTitle=itemView.findViewById(R.id.packageTitle);
            templateTitle=itemView.findViewById(R.id.templateTiltle);
            imageTitle=itemView.findViewById(R.id.ImageTiltle);
            payTitle=itemView.findViewById(R.id.payTiltle);
            packageBtn=itemView.findViewById(R.id.selectPlaneBtn);
            subcribedBtn=itemView.findViewById(R.id.subscribePlaneBtn);
        }
        void setLayout(SliderItem sliderItem)
        {

            priceForPay.setText(sliderItem.getPriceForPay());
            packageTitle.setText(sliderItem.getPackageTitle());
            templateTitle.setText(sliderItem.getTemplateTitle()+" - Template / Brand");
            imageTitle.setText(sliderItem.getImageTitle() + " Image Download / Year");
            payTitle.setText(sliderItem.getPayTitle() +" / Year");

        }
    }
}