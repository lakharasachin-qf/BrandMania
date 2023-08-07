package com.make.mybrand.Fragment.top;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.make.mybrand.Adapter.FontListAdeptor;
import com.make.mybrand.Fragment.BaseFragment;
import com.make.mybrand.Interface.AddTextEvent;
import com.make.mybrand.Interface.IColorChange;
import com.make.mybrand.Interface.IItaliTextEvent;
import com.make.mybrand.Interface.ITextBoldEvent;
import com.make.mybrand.Interface.ITextColorChangeEvent;
import com.make.mybrand.Interface.IUnderLineTextEvent;
import com.make.mybrand.Model.FontModel;
import com.make.mybrand.R;
import com.make.mybrand.databinding.TextTabBinding;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import java.util.ArrayList;

public class TextTab extends BaseFragment implements ITextColorChangeEvent, ColorPickerView.OnColorChangedListener {
    public boolean BOLD_TEXT = false;
    public boolean ITALIC_TEXT = false;
    public boolean UNDERLINE_TEXT = false;
    int textSize = 5;
    int saveProgress;
    ArrayList<FontModel> fontModelList = new ArrayList<>();
    FontModel[] fontModelObject;
    String[] fontStyle;
    private TextTabBinding binding;
    private int activityType = 0;

    public void setActivityType(int activityType) {
        this.activityType = activityType;

    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();

        binding = DataBindingUtil.inflate(inflater, R.layout.text_tab, parent, false);
        if (activityType == 1) {
            binding.addTextLayout.setVisibility(View.GONE);
        }
        binding.cancleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.colorPickerView.setVisibility(View.GONE);
                binding.cancleClick.setVisibility(View.GONE);
                binding.colorChose.setVisibility(View.VISIBLE);
                binding.fontStyleList.setVisibility(View.VISIBLE);
                binding.boldRelative.setVisibility(View.VISIBLE);
                binding.fontRecycler.setVisibility(View.GONE);


            }
        });
        binding.categoryEdt.setOnClickListener(view -> {
            // Toast.makeText(act,"Toast",Toast.LENGTH_LONG).show();
            binding.fontRecycler.setVisibility(View.VISIBLE);
            binding.fontStyleList.setVisibility(View.GONE);
            binding.colorChose.setVisibility(View.GONE);
            binding.cancleClick.setVisibility(View.VISIBLE);
            binding.boldRelative.setVisibility(View.GONE);
        });
        binding.addText.setOnClickListener(v -> ((AddTextEvent) act).onAddTextTrigger());
        binding.boldText.setOnClickListener(v -> {
            if (ITALIC_TEXT) {
                binding.boldText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C0C0C")));
                ((ITextBoldEvent) act).onBoldTextChange(false);

                ITALIC_TEXT = false;


            } else {
                ITALIC_TEXT = true;
                binding.boldText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD2753")));
                ((ITextBoldEvent) act).onBoldTextChange(true);

            }
        });
        binding.underlineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UNDERLINE_TEXT) {
                    binding.underlineText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C0C0C")));
                    ((IUnderLineTextEvent) act).onUnderLineItalic(false);
                    UNDERLINE_TEXT = false;
                } else {
                    UNDERLINE_TEXT = true;
                    binding.underlineText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD2753")));
                    ((IUnderLineTextEvent) act).onUnderLineItalic(true);


                }

            }
        });

        binding.seekBar.setVisibility(View.GONE);
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

            }
        });


        binding.italicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BOLD_TEXT) {
                    binding.italicText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C0C0C")));
                    ((IItaliTextEvent) act).onItalicTextChange(false);

                    BOLD_TEXT = false;


                } else {
                    BOLD_TEXT = true;
                    binding.italicText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD2753")));
                    ((IItaliTextEvent) act).onItalicTextChange(true);

                }
            }
        });
        binding.colorPickerView.setOnColorChangedListener((ColorPickerView.OnColorChangedListener) act);
        binding.colorChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.colorPickerView.setVisibility(View.VISIBLE);
                binding.fontStyleList.setVisibility(View.GONE);
                binding.colorChose.setVisibility(View.GONE);
                binding.cancleClick.setVisibility(View.VISIBLE);
                binding.boldRelative.setVisibility(View.GONE);


            }
        });
        FontChanege();
        return binding.getRoot();
    }

    @Override
    public void onColorItemChange(int colorcode) {

        binding.colorChose.setBackgroundColor(colorcode);
    }

    public void FontChanege() {
        fontModelList = new ArrayList<>();  //initialized list
        fontModelObject = new FontModel[24];   //Array of model class
        fontStyle = new String[24];  //Array of String
        fontStyle[0] = "font/inter_bold.otf";
        fontStyle[1] = "font/inter_extrabold.otf";
        fontStyle[2] = "font/inter_medium.otf";
        fontStyle[3] = "font/inter_regular.otf";
        fontStyle[4] = "font/inter_semibold.otf";
        fontStyle[5] = "font/poppins_bold.ttf";
        fontStyle[6] = "font/poppins_light.ttf";
        fontStyle[7] = "font/poppins_medium.ttf";
        fontStyle[8] = "font/poppins_regular.ttf";
        fontStyle[9] = "font/worksans_bold.ttf";
        fontStyle[10] = "font/worksans_extrabold.ttf";
        fontStyle[11] = "font/worksans_light.ttf";
        fontStyle[12] = "font/worksans_medium.ttf";
        fontStyle[13] = "font/worksans_regular.ttf";
        fontStyle[14] = "font/worksans_semibold.ttf";
        fontStyle[15] = "font/source_serifpro_regular.ttf";
        fontStyle[16] = "font/robotomono_regular.ttf";
        fontStyle[17] = "font/ptsansnarrow_regular.ttf";
        fontStyle[18] = "font/nunito_regular.ttf";
        fontStyle[19] = "font/josefinsans_medium.ttf";
        fontStyle[20] = "font/cabin_variable.ttf";
        fontStyle[21] = "font/balsamiqsans_regular.ttf";
        fontStyle[22] = "font/asap_regular.ttf";
        fontStyle[23] = "font/andika_basic.ttf";


        for (int i = 0; i < 24; i++) {
            fontModelObject[i] = new FontModel();
            fontModelObject[i].setFontFaimly(fontStyle[i]);
            fontModelObject[i].setFontId(fontStyle[i]
                    .replace("font/", "")
                    .replace(".ttf", "")
                    .replace(".otf", "")
                    .replace("_bold", "")
                    .replace("_light", "")
                    .replace("_medium", "")
                    .replace("_regular", "")
                    .replace("_extrabold", "")
                    .replace("_semibold", "")
                    .replace("_extralight", "")
                    .replace("_black", "")
                    .replace("worksans", "BrandMania")
                    .replace("inter", "BrandMania")
                    .replace("poppins", "BrandMania")
                    .replace("montserrat", "BrandMania")
                    .replace("sourceserifpro", "BrandMania")
                    .replace("robotomono", "BrandMania")
                    .replace("ptsansnarrow", "BrandMania")
                    .replace("nunito", "BrandMania")
                    .replace("josefinsans", "BrandMania")
                    .replace("cabin_variable", "BrandMania")
                    .replace("balsamiqsans", "BrandMania")
                    .replace("asap", "BrandMania")
                    .replace("andika", "BrandMania")
                    .replace("source_serifpro", "BrandMania")
            );
            fontModelList.add(fontModelObject[i]);
        }
        binding.fontRecycler.setLayoutManager(new GridLayoutManager(act, 3));   //set layout
        binding.fontRecycler.setAdapter(new FontListAdeptor(fontModelList, act));    //set Adapter
    }

    @Override
    public void onColorChanged(int newColor) {
        binding.colorChose.setBackgroundColor(newColor);
        ((IColorChange) act).onChooseColor(newColor);
    }
}