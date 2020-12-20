package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.brandmania.Adapter.ColorPickerAdapter;
import com.app.brandmania.Adapter.OnlyTextColorPickerAddaptor;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ColorTabBinding;
import com.app.brandmania.databinding.FragmentCustomBinding;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import java.util.Objects;

public class ColorTab extends Fragment implements ColorPickerView.OnColorChangedListener {
    Activity act;
    private ColorTabBinding binding;
    private int mColorCode;
    private ColorTab context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        context=this;
        binding= DataBindingUtil.inflate(inflater,R.layout.color_tab,container,false);
        binding.colorRecycler.setLayoutManager(new GridLayoutManager(getActivity(),6));
        binding.colorRecycler.setHasFixedSize(true);
        OnlyTextColorPickerAddaptor colorPickerAdapter = new OnlyTextColorPickerAddaptor(getActivity());
        colorPickerAdapter.setColorTab(context);
        colorPickerAdapter.setOnColorPickerClickListener(colorCode -> mColorCode = colorCode);
        binding.colorRecycler.setAdapter(colorPickerAdapter);

        binding.chooseColorTxt.setOnClickListener(v -> ColorPickerDialog.newBuilder().setColor(ContextCompat.getColor(act,R.color.black)).show(Objects.requireNonNull(getActivity())));

        binding.colorPickerView.setOnColorChangedListener(this);
        binding.colorPickerView.setOnColorChangedListener(this);
        binding.colorPickerView.setColor(ContextCompat.getColor(act,R.color.black), true);

        return binding.getRoot();
    }


    @Override
    public void onColorChanged(int newColor) {
        Log.e("OnColorChoose",String.valueOf(newColor));
        ((IColorChange)act).onChooseColor(newColor);
    }
}
