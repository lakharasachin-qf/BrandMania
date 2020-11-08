package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.brandmania.Adapter.ColorPickerAdapter;
import com.app.brandmania.Adapter.OnlyTextColorPickerAddaptor;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ColorTabBinding;
import com.app.brandmania.databinding.FragmentCustomBinding;

public class ColorTab extends Fragment {
    Activity act;
    private ColorTabBinding binding;
    private int mColorCode;
    private ColorTab context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.color_tab,container,false);
        binding.colorRecycler.setLayoutManager(new GridLayoutManager(getActivity(),6));
        binding.colorRecycler.setHasFixedSize(true);
        OnlyTextColorPickerAddaptor colorPickerAdapter = new OnlyTextColorPickerAddaptor(getActivity());
        colorPickerAdapter.setColorTab(context);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                mColorCode = colorCode;
            }
        });
        binding.colorRecycler.setAdapter(colorPickerAdapter);

        return binding.getRoot();
    }
//    public void ColorList() {
//
//        ArrayList<MultiListItem> menuModels = new ArrayList<>();
//        MultiListItem model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.solidcolor);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.solidgreen);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.solidlightsky);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.singlesolid);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.bluesolid);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.greensolid);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.yellowsolid);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.
//                bluesolid);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.marun);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.sky);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.purple);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.graycolor);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.greensolid);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.darkgreen);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.lightpink);
//        menuModels.add(model);
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
//        model.setImage(R.drawable.mediuplightpink);
//        menuModels.add(model);
//        MenuAddaptor menuAddaptor = new MenuAddaptor(menuModels, act);
//        binding.colorRecycler.setLayoutManager(new GridLayoutManager(getActivity(),5));
//        binding.colorRecycler.setHasFixedSize(true);
//        binding.colorRecycler.setAdapter(menuAddaptor);
//    }
   
}
