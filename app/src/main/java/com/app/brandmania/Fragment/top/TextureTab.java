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
import com.app.brandmania.databinding.TextuberTabBinding;

import java.util.ArrayList;

public class TextureTab extends Fragment {

    Activity act;
    private TextuberTabBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.textuber_tab,container,false);
        TextuberList();
        return binding.getRoot();
    }
    public void TextuberList() {

        ArrayList<MultiListItem> menuModels = new ArrayList<>();
        MultiListItem model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.textureblack);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
       // model.setImage(R.drawable.textureblackblue);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.texturechocolate);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
      //  model.setImage(R.drawable.texturegray);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.texturered);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.texturesky);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.texturewhite);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.texturewood);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.texturewoodd);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        //model.setImage(R.drawable.textureyello);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.bluevignet);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
       // model.setImage(R.drawable.textureyello);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
       // model.setImage(R.drawable.texturewoodd);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
       // model.setImage(R.drawable.texturesky);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
       // model.setImage(R.drawable.texturegray);
        menuModels.add(model);
        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
       // model.setImage(R.drawable.texturered);
        menuModels.add(model);
        MenuAddaptor menuAddaptor = new MenuAddaptor(menuModels, act);
        binding.colorRecycler.setLayoutManager(new GridLayoutManager(getActivity(),5));
        binding.colorRecycler.setHasFixedSize(true);
        binding.colorRecycler.setAdapter(menuAddaptor);
    }
}
