package com.app.brandmania.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Model.ImagesFolder;
import com.app.brandmania.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryFolderAdapter extends RecyclerView.Adapter<GalleryFolderAdapter.MyViewHolder> {
    Context c;
    ArrayList<ImagesFolder> spacecrafts;
    private onFolderSelect folderSelect;

    public GalleryFolderAdapter setFolderSelect(onFolderSelect folderSelect) {
        this.folderSelect = folderSelect;
        return this;
    }

    public interface onFolderSelect {
        void onFolderSelect(int position);
    }
    public GalleryFolderAdapter(Context c, ArrayList<ImagesFolder> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_folder_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ImagesFolder s = spacecrafts.get(position);
        holder.folderNameTxt.setText(s.getAllFolderName());
        Glide.with(c).load(s.getAllImagePaths().get(0)).placeholder(R.drawable.placeholder).into(holder.image);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              folderSelect.onFolderSelect(position);
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
        TextView folderNameTxt;

        public MyViewHolder(View itemView) {
            super(itemView);


            folderNameTxt = itemView.findViewById(R.id.folderNameTxt);
            image = (ImageView) itemView.findViewById(R.id.image);
            itemLayout = itemView.findViewById(R.id.itemLayout);

        }
    }

}
