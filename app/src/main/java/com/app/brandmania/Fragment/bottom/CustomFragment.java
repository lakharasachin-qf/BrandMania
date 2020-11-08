package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.brandmania.Adapter.MenuAddaptor;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.Activity.ColorAndTextEditActivity;
import com.app.brandmania.Activity.OnlyTextEditorActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentCustomBinding;

import java.util.ArrayList;
import java.util.Timer;

public class CustomFragment extends Fragment {
    Activity act;
    private FragmentCustomBinding binding;
    Timer timer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_custom,container,false);
        binding.textAndImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ColorAndTextEditActivity.class);
                startActivity(intent);

            }
        });

        binding.textLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OnlyTextEditorActivity.class);
                startActivity(intent);

            }
        });        Recommendation();
        return binding.getRoot();

    }

    public void Recommendation() {
        ArrayList<MultiListItem> menuModels = new ArrayList<>();
        MultiListItem model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.firstn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.secondn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.thirdn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.fourn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.five);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.sixn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.firstn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.secondn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.thirdn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.fourn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.five);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.sixn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.firstn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.secondn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.LAYOUT_RECOMMANDATION);
        model.setImage(R.drawable.thirdn);
        menuModels.add(model);
        MenuAddaptor menuAddaptor = new MenuAddaptor(menuModels, act);
        binding.rocommRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));
        binding.rocommRecycler.setHasFixedSize(true);
        binding.rocommRecycler.setAdapter(menuAddaptor);
    }

}
