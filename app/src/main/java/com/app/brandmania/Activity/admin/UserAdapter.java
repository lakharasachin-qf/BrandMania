package com.app.brandmania.Activity.admin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.app.brandmania.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<UserListModel> implements Filterable {
     final Activity activity;
    //activity context
    final Context context;
    //the layout resource file for the list items
    final int resource;
    //the list values in the List of type hero
    ArrayList<UserListModel> heroList;
    ArrayList<UserListModel> tmpArray;
    CustomeFilter cust;
    private int mSelectedPosition = -1;
    handleSelectionEvent selectionEvent;
    public interface handleSelectionEvent{
        void selectionEvent(UserListModel itemmodel, int positino,String flag);
    }
    public void setInteface(handleSelectionEvent inteface){
        selectionEvent=inteface;
    }

    public UserAdapter(Activity activity, Context context, int resource, ArrayList<UserListModel> heroList) {
        super(context, resource, heroList);
        this.activity = activity;
        this.context = context;
        this.resource = resource;
        this.heroList = heroList;
         tmpArray = heroList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, null, false);

        final TextView name = convertView.findViewById(R.id.name);
        final TextView mobileNo = convertView.findViewById(R.id.mobileNo);
        final TextView emailId = convertView.findViewById(R.id.emailId);

        final UserListModel hero = heroList.get(position);
        name.setText(hero.getFirstName()+" "+hero.getLastName());
        mobileNo.setText(hero.getPhoneNo());
        emailId.setText(hero.getEmailId());
        CardView cardview=convertView.findViewById(R.id.cardview);
        CheckBox checkBox =convertView.findViewById(R.id.checked1);

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hero.isSelected()) {
                    hero.setSelected(false);
                }else {
                    hero.setSelected(true);
                    checkBox.setChecked(true);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectionEvent.selectionEvent(hero,position,"Add");
                }
            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        return heroList.size();
    }

    @Nullable
    @Override
    public UserListModel getItem(int position) {
        return heroList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (cust == null) {
            cust = new CustomeFilter();
        }
        return cust;
    }

    public void setSelectedPosition(int mSelectedPosition) {
        this.mSelectedPosition = mSelectedPosition;
    }



    class CustomeFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraints) {
            FilterResults filterResults = new FilterResults();
            if (constraints != null && constraints.length() > 1) {
                constraints = constraints.toString().toLowerCase();

                ArrayList<UserListModel> tmpry = new ArrayList<>();
                for (int i = 0; i < tmpArray.size(); i++) {
//                    if (tmpArray.get(i).getProductName().toLowerCase().startsWith(constraints.toString())) {
//                        Gson gson=new Gson();
//                        String tmpStr=gson.toJson(tmpArray.get(i));
//                        UserListModel model = gson.fromJson(tmpStr,UserListModel.class);
//                        tmpry.add(model);
//                    }
                }
                filterResults.count = tmpry.size();
                filterResults.values = tmpry;
            } else {
                filterResults.count = tmpArray.size();
                filterResults.values = tmpArray;
            }


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
           // heroList = (ArrayList<MultiModel>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}

