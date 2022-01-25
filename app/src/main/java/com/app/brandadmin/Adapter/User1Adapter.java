package com.app.brandadmin.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandadmin.Activity.admin.CategoryAdapter;
import com.app.brandadmin.Activity.admin.UserListModel;
import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.R;
import com.app.brandadmin.databinding.ItemUserListBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

public class User1Adapter extends RecyclerView.Adapter implements Filterable {

    ArrayList<UserListModel> userList = new ArrayList<>();
    boolean isclick = true;
    Activity activity;
    private Gson gson;
    private boolean isLoadingAdded = false;
    PreafManager preafManager;
    private static final int REQUEST_CALL = 1;
    private CategoryAdapter.BRANDBYIDIF brandbyidif;
    ArrayList<UserListModel> tmpArray;
    CategoryAdapter.handleSelectionEvent selectionEvent;
    OnCheckedItem onCheckedItem;
    CustomeFilter cust;


    public interface OnCheckedItem {
        void onCheck(UserListModel itemmodel, int position, String flag);
    }

    public void setInteface(OnCheckedItem inteface) {
        onCheckedItem = inteface;
    }


    public User1Adapter(ArrayList<UserListModel> userList, Activity activity) {
        this.activity = activity;
        gson = new Gson();
        preafManager = new PreafManager(activity);
        this.isLoadingAdded = isLoadingAdded;
        tmpArray = userList;
        this.userList = tmpArray;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemUserListBinding itemUserListBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_user_list, parent, false);
        return new UserlistHolder(itemUserListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final UserListModel model = userList.get(position);


        ((UserlistHolder) holder).binding.name.setText(model.getFirstName());
        ((UserlistHolder) holder).binding.mobileNo.setText(model.getPhoneNo());
        ((UserlistHolder) holder).binding.emailId.setText(model.getEmailId());


//        ((UserlistHolder) holder).binding.checked1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((UserlistHolder) holder).binding.checked1.setChecked(true);
//                onCheckedItem.onCheck(model, position, "add");
//            }
//        });
        ((UserlistHolder) holder).binding.checked1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    Log.e("userList", model.getFirstName());
                    ((UserlistHolder) holder).binding.checked1.setChecked(true);
                    onCheckedItem.onCheck(model, position, "add");
                } else {
                    ((UserlistHolder) holder).binding.checked1.setChecked(false);
                    onCheckedItem.onCheck(model, position, "");
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        if (cust == null) {
            cust = new CustomeFilter();
        }
        return cust;
    }

    class CustomeFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            ArrayList<UserListModel> searchArray = new ArrayList<>();

            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                for (int i = 0; i < tmpArray.size(); i++) {
                    if (tmpArray.get(i).getFirstName().toUpperCase().contains(constraint)) {
                        searchArray.add(tmpArray.get(i));
                        //       Log.e("CategoryList", new Gson().toJson(searchArray.size()));
                    }
                }
                filterResults.values = searchArray;
                filterResults.count = searchArray.size();

            } else {
                filterResults.count = tmpArray.size();
                filterResults.values = tmpArray;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList = (ArrayList<UserListModel>) results.values;
            notifyDataSetChanged();
        }
    }

    public class UserlistHolder extends RecyclerView.ViewHolder {
        ItemUserListBinding binding;

        public UserlistHolder(ItemUserListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
