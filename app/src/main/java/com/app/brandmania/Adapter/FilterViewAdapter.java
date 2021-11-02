package com.app.brandmania.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Interface.ThumbnailCallback;
import com.app.brandmania.Model.ThumbnailItem;
import com.app.brandmania.R;

import java.util.List;


public class FilterViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private static final String TAG = "THUMBNAILS_ADAPTER";
private static int lastPosition = -1;
private ThumbnailCallback thumbnailCallback;
private List<ThumbnailItem> dataSet;

public FilterViewAdapter(List<ThumbnailItem> dataSet, ThumbnailCallback thumbnailCallback) {
        Log.v(TAG, "Thumbnails Adapter has " + dataSet.size() + " items");
        this.dataSet = dataSet;
        this.thumbnailCallback = thumbnailCallback;
        }


@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.v(TAG, "On Create View Holder Called");
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.row_filter_view, viewGroup, false);
        return new ThumbnailsViewHolder(itemView);
        }

@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int i) {
final ThumbnailItem thumbnailItem = dataSet.get(i);
        Log.v(TAG, "On Bind View Called");
        ThumbnailsViewHolder thumbnailsViewHolder = (ThumbnailsViewHolder) holder;
        thumbnailsViewHolder.thumbnail.setImageBitmap(thumbnailItem.image);
        thumbnailsViewHolder.thumbnail.setScaleType(ImageView.ScaleType.FIT_START);
        //setAnimation(thumbnailsViewHolder.thumbnail, i);
        thumbnailsViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        if (lastPosition != i) {
        thumbnailCallback.onThumbnailClick(thumbnailItem.filter);
        lastPosition = i;
        }
        }

        });
        }

        @Override
        public int getItemCount() {
        return dataSet.size();
        }
        public static class ThumbnailsViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnail;
        public RelativeLayout itemLayout;
    public ThumbnailsViewHolder(View v) {
        super(v);
        this.itemLayout=v.findViewById(R.id.itemLayout);
        this.thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
    }
}
}