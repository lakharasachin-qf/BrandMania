package com.app.brandmania.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.brandmania.Activity.ViewAllImage;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.R;

import java.util.ArrayList;
import java.util.List;

import static com.app.brandmania.Utils.Utility.Log;


public class DasboardAddaptor extends RecyclerView.Adapter<DasboardAddaptor.DasboardViewHolder> {

    private ArrayList<DashBoardItem> dashBoardItemList;

    private final Gson gson;
    Activity activity;

    public DasboardAddaptor(ArrayList<DashBoardItem> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        gson=new Gson();
    }

    @NonNull
    @Override
    public DasboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_item_layout, viewGroup, false);

        return new DasboardViewHolder(layout);
    }
    @Override public void onBindViewHolder(@NonNull DasboardViewHolder dasboardViewHolder, int position) {
        dasboardViewHolder.title.setText(convertFirstUpper(dashBoardItemList.get(position).getName()));
        Log.e("LLLLLL", String.valueOf(dashBoardItemList.get(position).getImageLists().size()));
        ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(dashBoardItemList.get(position).getImageLists(), activity);
        menuAddaptor.setLayoutType(ImageCategoryAddaptor.FROM_HOMEFRAGEMENT);
        dasboardViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        dasboardViewHolder.recyclerView.setHasFixedSize(true);
        menuAddaptor.setDashBoardItem(dashBoardItemList.get(position));
        dasboardViewHolder.recyclerView.setAdapter(menuAddaptor);
        dasboardViewHolder.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i =new Intent(activity, ViewAllImage.class);
                i.putExtra("viewAll","12");
                i.putExtra("detailsObj", gson.toJson(dashBoardItemList.get(position)));
                activity.startActivity(i);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });

    }
    @Override public int getItemCount() {
        return dashBoardItemList.size();
    }
    public void setfilter(List<DashBoardItem> listitem) {
        dashBoardItemList = new ArrayList<>();
        dashBoardItemList.addAll(listitem);
        notifyDataSetChanged();
    }
    public class DasboardViewHolder extends RecyclerView.ViewHolder {
        TextView title,viewAll;
        RecyclerView recyclerView;

        public DasboardViewHolder(@NonNull View itemView) {
            super(itemView);
          title=itemView.findViewById(R.id.title);
          viewAll=itemView.findViewById(R.id.viewAll);
          recyclerView=itemView.findViewById(R.id.imageCategoryRecycler);

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

