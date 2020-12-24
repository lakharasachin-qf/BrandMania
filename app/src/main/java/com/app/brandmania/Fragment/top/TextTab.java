package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Adapter.ColorPickerAdapter;
import com.app.brandmania.Adapter.FontListAdeptor;
import com.app.brandmania.Adapter.IImageFromGalary;
import com.app.brandmania.Adapter.OnlyTextColorPickerAddaptor;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.IUnderLineTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Model.FontModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.TextTabBinding;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import java.util.ArrayList;
import java.util.Objects;

public class TextTab extends Fragment implements ITextColorChangeEvent , ColorPickerView.OnColorChangedListener {
    public  boolean BOLD_TEXT =false;
    public  boolean ITALIC_TEXT =false;
    public  boolean UNDERLINE_TEXT =false;
    public static final boolean NO_BOLD_TEXT =false;
    Activity act;
    int textSize = 5;
    int saveProgress;
    private TextTabBinding binding;
    ArrayList<FontModel> fontModelList=new ArrayList<>();
    FontModel[] fontModelObject;
    String[] fontStyle;
    private int mColorCode;
    private TextTab context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        context=this;
        binding = DataBindingUtil.inflate(inflater, R.layout.text_tab, container, false);
      //  binding.seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

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
        binding.categoryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(act,"Toast",Toast.LENGTH_LONG).show();
                binding.fontRecycler.setVisibility(View.VISIBLE);
                binding.fontStyleList.setVisibility(View.GONE);
                binding.colorChose.setVisibility(View.GONE);
                binding.cancleClick.setVisibility(View.VISIBLE);
                binding.boldRelative.setVisibility(View.GONE);
            }
        });
        binding.boldText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ITALIC_TEXT) {
                    binding.boldText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C0C0C")));
                    ((ITextBoldEvent) act).onBoldTextChange(false);

                    ITALIC_TEXT=false;


                }else {
                    ITALIC_TEXT=true;
                    binding.boldText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD2753")));
                    ((ITextBoldEvent) act).onBoldTextChange(true);

                }
            }
        });
        binding.underlineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UNDERLINE_TEXT) {
                    binding.underlineText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C0C0C")));
                    ((IUnderLineTextEvent) act).onUnderLineItalic(false);
                    UNDERLINE_TEXT=false;
                }
                else {
                    UNDERLINE_TEXT=true;
                    binding.underlineText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD2753")));
                    ((IUnderLineTextEvent) act).onUnderLineItalic(true);


                }

            }
        });


        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                textSize = textSize + (progress-saveProgress);
                saveProgress = progress;
              //  binding.seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                ((ITextSizeEvent) act).onfontSize(textSize);



            }
        });


        binding.italicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BOLD_TEXT) {
                    binding.italicText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C0C0C")));
                    ((IItaliTextEvent) act).onItalicTextChange(false);

                    BOLD_TEXT=false;


                }else {
                    BOLD_TEXT=true;
                    binding.italicText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AD2753")));
                    ((IItaliTextEvent) act).onItalicTextChange(true);

                }
            }
        });
        binding.colorPickerView.setOnColorChangedListener((ColorPickerView.OnColorChangedListener) act);
      //  binding.colorPickerView.setColor(ContextCompat.getColor(act,R.color.black), true);
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
    @Override public void onColorItemChange(int colorcode) {

        binding.colorChose.setBackgroundColor(colorcode);
    }
    public void FontChanege() {
        fontModelList = new ArrayList<>();  //initialized list
        fontModelObject=new FontModel[15];   //Array of model class
        fontStyle = new String[15];  //Array of String
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

        for (int i = 0; i < 15; i++) {
            fontModelObject[i] = new FontModel();
            fontModelObject[i].setFontFaimly(fontStyle[i]);
            fontModelObject[i].setFontId(fontStyle[i]
                    .replace("font/","")
                    .replace(".ttf","")
                    .replace(".otf","")
                    .replace("_bold","")
                    .replace("_light","")
                    .replace("_medium","")
                    .replace("_regular","")
                    .replace("_extrabold","")
                    .replace("_semibold","")
                    .replace("_extralight","")
                    .replace("_black","")
                    .replace("worksans","BrandMania")
                    .replace("inter","BrandMania")
                    .replace("poppins","BrandMania")
                    .replace("montserrat","BrandMania")
            );
            fontModelList.add(fontModelObject[i]);
        }
        binding.fontRecycler.setLayoutManager(new GridLayoutManager(act,3));   //set layout
        binding.fontRecycler.setAdapter(new FontListAdeptor(fontModelList,act));    //set Adapter
    }
    @Override public void onColorChanged(int newColor) {
        Log.e("OnColorChoose",String.valueOf(newColor));
        ((IColorChange)act).onChooseColor(newColor);
    }
}
