package com.make.mybrand.Adapter;

import static com.make.mybrand.utils.Utility.Log;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Interface.ItemSelectionInterface;
import com.make.mybrand.Model.CommonListModel;
import com.make.mybrand.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DropDownAdpt extends RecyclerView.Adapter<DropDownAdpt.TenamentHolder> {

    private ArrayList<CommonListModel> arrayList;
    private Activity act;
    private int checkedPosition = -1;
    private int calledFlag;
    PreafManager preafManager;

    private HandlerFragmentSelection fragmentSelection = null;

    public DropDownAdpt(Activity act, ArrayList<CommonListModel> arrayList, int calledFlag) {
        this.arrayList = arrayList;
        this.act = act;
        this.calledFlag = calledFlag;
        preafManager = new PreafManager(act);
    }

    public void setFragmentSelection(HandlerFragmentSelection fragmentSelection) {
        this.fragmentSelection = fragmentSelection;
    }

    @Override
    public TenamentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout_new, parent, false);
        return new TenamentHolder(view);
    }


    @Override
    public void onBindViewHolder(final TenamentHolder holder, @SuppressLint("RecyclerView") int position) {
        CommonListModel listModel = arrayList.get(position);

        Glide.with(act).load(arrayList.get(position).getThumbnail()).placeholder(R.drawable.placeholder).into((holder.imageView));
        holder.title.setText(convertFirstUpper(listModel.getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedPosition = position;
                notifyDataSetChanged();
                if (fragmentSelection != null) {
                    fragmentSelection.handlerFragmentSelection(calledFlag, position, listModel);
                } else {
                    ((ItemSelectionInterface) act).onItemSelection(calledFlag, position, listModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface HandlerFragmentSelection {
        void handlerFragmentSelection(int calledFlag, int position, CommonListModel listModel);
    }

    public class TenamentHolder extends RecyclerView.ViewHolder {
        //RadioButton radioButton;
        TextView title;
        CircleImageView imageView;

        public TenamentHolder(@NonNull View itemView) {
            super(itemView);
            //radioButton = itemView.findViewById(R.id.radioButton);
            imageView = itemView.findViewById(R.id.brandCategoryImage);
            title = itemView.findViewById(R.id.text);
        }
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<CommonListModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }
}