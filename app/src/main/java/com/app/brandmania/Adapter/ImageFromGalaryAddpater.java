package com.app.brandmania.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageFromGalaryAddpater extends RecyclerView.Adapter<ImageFromGalaryAddpater.MyViewHolder> {
    Context c;
    ArrayList<ImageFromGalaryModel> spacecrafts;

    public ImageFromGalaryAddpater(Context c, ArrayList<ImageFromGalaryModel> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.itemlayout_image_from_galary,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ImageFromGalaryModel s=spacecrafts.get(position);

        Glide.with(c).load(s.getUri()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IImageFromGalary) c).onImageFromGalaryItemSelection( position, spacecrafts.get(position));
            }
        });



    }

    @Override
    public int getItemCount() {
        return spacecrafts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        CardView itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);


            image= (ImageView) itemView.findViewById(R.id.image);
            itemLayout=itemView.findViewById(R.id.itemLayout);

        }
    }

}
