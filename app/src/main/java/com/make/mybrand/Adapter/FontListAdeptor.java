package com.make.mybrand.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Model.FontModel;
import com.make.mybrand.R;
import com.make.mybrand.utils.IFontChangeEvent;

import java.util.ArrayList;

import static com.make.mybrand.utils.Utility.Log;

public class FontListAdeptor extends RecyclerView.Adapter<FontListAdeptor.FontListViewHolder> {
    private ArrayList<FontModel> fontModels;
    private Activity activity;

    public FontListAdeptor(ArrayList<FontModel> fontModels, Activity activity) {
        this.fontModels = fontModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FontListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.font_item_show, parent, false); //inflate layout in view
        return new FontListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), fontModels.get(position).getFontFaimly());  //get font style from list and store into typeface
        holder.fontList.setTypeface(typeface);  //set typeface to textView
        holder.fontList.setText(convertFirstUpper(fontModels.get(position).getFontId()));//set text
        holder.fontList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IFontChangeEvent)activity).onFontChangeListenert(fontModels.get(position).getFontFaimly());
            }
        });
    }

    @Override
    public int getItemCount() {
        return fontModels.size();
    }

    public class FontListViewHolder extends RecyclerView.ViewHolder{
        TextView fontList;

        public FontListViewHolder(@NonNull View itemView) {
            super(itemView);
            fontList = (TextView) itemView.findViewById(R.id.fonList);
        }
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
