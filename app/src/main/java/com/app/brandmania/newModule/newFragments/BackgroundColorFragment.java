package com.app.brandmania.newModule.newFragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.brandmania.Adapter.OnlyTextColorPickerAddaptor;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentBackgroundColorBinding;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import java.util.Objects;

public class BackgroundColorFragment extends BaseFragment implements ColorPickerView.OnColorChangedListener{


    Activity act;
    private FragmentBackgroundColorBinding binding;
    private int mColorCode;
    private BackgroundColorFragment context;
    int tabLayou=0;

    public void setTabLayou(int tabLayou) {
        this.tabLayou = tabLayou;
    }

    int textSize = 5;
    int saveProgress;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        context=this;
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_background_color,parent,false);
        if (tabLayou==1){
            binding.checkbox.setVisibility(View.GONE);
            binding.seekBar.setVisibility(View.GONE);
        }
        binding.colorRecycler.setLayoutManager(new GridLayoutManager(getActivity(),6));
        binding.colorRecycler.setHasFixedSize(true);
        OnlyTextColorPickerAddaptor colorPickerAdapter = new OnlyTextColorPickerAddaptor(getActivity());
      //  colorPickerAdapter.setColorTab(context);
        colorPickerAdapter.setOnColorPickerClickListener(colorCode -> mColorCode = colorCode);
        binding.colorRecycler.setAdapter(colorPickerAdapter);

        binding.chooseColorTxt.setOnClickListener(v -> ColorPickerDialog.newBuilder().setColor(ContextCompat.getColor(act, R.color.black)).show(Objects.requireNonNull(getActivity())));

        binding.colorPickerView.setOnColorChangedListener(this);
        binding.colorPickerView.setOnColorChangedListener(this);
        // binding.colorPickerView.setColor(ContextCompat.getColor(act,R.color.black), true);


        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSize = textSize + (progress - saveProgress);
                saveProgress = progress;
                //  binding.seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                ((IColorChange)act).onBorderSizeChange(textSize);


            }
        });
        binding.seekBar.setEnabled(false);

        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.seekBar.setEnabled(true);
                }else {
                    binding.seekBar.setProgress(0);
                    ((IColorChange)act).onBorderSizeChange(0);
                    binding.seekBar.setEnabled(false);
                }
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onColorChanged(int newColor) {

        Log.e("OnColorChoose",String.valueOf(newColor));
        ((IColorChange)act).onChooseColor(newColor);
    }

}