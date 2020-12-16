package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.brandmania.Adapter.MenuAddaptor;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FrameTabBinding;

import java.util.ArrayList;

public class FrameTab extends Fragment {

    Activity act;
   private FrameTabBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.frame_tab,container,false);
       // FrameList();
        return binding.getRoot();
    }
//    public void FrameList() {
//
//        ArrayList<MultiListItem> menuModels = new ArrayList<>();
//        MultiListItem model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLFRAME);
//        model.setImage(R.drawable.framee);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLFRAME);
//        model.setImage(R.drawable.framee);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLFRAME);
//        model.setImage(R.drawable.framee);
//        menuModels.add(model);
//
//        MenuAddaptor menuAddaptor = new MenuAddaptor(menuModels, act);
//        binding.frameRecycler.setLayoutManager(new GridLayoutManager(getActivity(),4));
//        binding.frameRecycler.setHasFixedSize(true);
//        binding.frameRecycler.setAdapter(menuAddaptor);
//    }
}
