package com.app.brandmania.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Interface.IImageFromGalary;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class ImageFromGalaryAddpater extends RecyclerView.Adapter<ImageFromGalaryAddpater.MyViewHolder> {
    Context c;
    ArrayList<String> spacecrafts;
    public interface onImageSelection{
        void onImageSelection();

    }
    public onImageSelection imageSelection;

    public ImageFromGalaryAddpater setImageSelection(onImageSelection imageSelection) {
        this.imageSelection = imageSelection;
        return this;
    }

    public ImageFromGalaryAddpater(Context c, ArrayList<String> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.itemlayout_image_from_galary,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {



        Glide.with(c).load(spacecrafts.get(position)).placeholder(R.drawable.placeholder).into(holder.image);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFromGalaryModel galaryModel =new ImageFromGalaryModel();
                galaryModel.setName(spacecrafts.get(position));
                File file = new File(spacecrafts.get(position));
                galaryModel.setUri(Uri.fromFile(file));
                ((IImageFromGalary) c).onImageFromGalaryItemSelection( position, galaryModel);
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
