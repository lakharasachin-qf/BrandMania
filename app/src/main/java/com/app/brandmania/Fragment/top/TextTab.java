package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Adapter.ColorPickerAdapter;
import com.app.brandmania.Adapter.FontListAdeptor;
import com.app.brandmania.Adapter.OnlyTextColorPickerAddaptor;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.IUnderLineTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Model.FontModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.TextTabBinding;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;

import java.util.ArrayList;
import java.util.Objects;

public class TextTab extends Fragment implements ITextColorChangeEvent {
    public  boolean BOLD_TEXT =false;
    public  boolean ITALIC_TEXT =false;
    public  boolean UNDERLINE_TEXT =false;
    public static final boolean NO_BOLD_TEXT =false;
    Activity act;
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
        binding.textRecycler.setLayoutManager(new GridLayoutManager(getActivity(),6));
        binding.textRecycler.setHasFixedSize(true);
        OnlyTextColorPickerAddaptor colorPickerAdapter = new OnlyTextColorPickerAddaptor(getActivity());
        colorPickerAdapter.setTextTab(context);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                mColorCode = colorCode;
            }
        });
        binding.textRecycler.setAdapter(colorPickerAdapter);

        binding.cancleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    binding.fontRecycler.setVisibility(View.GONE);
                    binding.textRecycler.setVisibility(View.GONE);
                    binding.cancleClick.setVisibility(View.GONE);
                    binding.imageChoose.setVisibility(View.VISIBLE);
                binding.boldRelative.setVisibility(View.VISIBLE);
                binding.underLineRelativ.setVisibility(View.VISIBLE);
                binding.ItalicClick.setVisibility(View.VISIBLE);



            }
        });
        binding.boldRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ITALIC_TEXT) {
                    ((ITextBoldEvent) act).onBoldTextChange(false);
                    binding.NormaTextChoose.setVisibility(View.VISIBLE);
                    binding.BOldTextChoose.setVisibility(View.GONE);
                    ITALIC_TEXT=false;


                }else {
                    ITALIC_TEXT=true;
                    ((ITextBoldEvent) act).onBoldTextChange(true);
                    binding.NormaTextChoose.setVisibility(View.GONE);
                    binding.BOldTextChoose.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.underLineRelativ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UNDERLINE_TEXT) {
                    ((IUnderLineTextEvent) act).onUnderLineItalic(false);
                    binding.NormalUnderTextChoose.setVisibility(View.VISIBLE);
                    binding.UndelLIneChoose.setVisibility(View.GONE);
                    UNDERLINE_TEXT=false;
                }
                else {
                    UNDERLINE_TEXT=true;
                    ((IUnderLineTextEvent) act).onUnderLineItalic(true);
                    binding.NormalUnderTextChoose.setVisibility(View.GONE);
                    binding.UndelLIneChoose.setVisibility(View.VISIBLE);

                }

            }
        });
        binding.ItalicClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BOLD_TEXT) {
                    ((IItaliTextEvent) act).onItalicTextChange(false);
                    binding.NormalUnderTextChoose.setVisibility(View.VISIBLE);
                    binding.UndelLIneChoose.setVisibility(View.GONE);
                    BOLD_TEXT=false;


                }else {
                    BOLD_TEXT=true;
                    ((IItaliTextEvent) act).onItalicTextChange(true);
                    binding.NormalItalicTextChoose.setVisibility(View.GONE);
                    binding.ItalicTextChoose.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.imageChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*   binding.fontRecycler.setVisibility(View.GONE);
                   binding.textRecycler.setVisibility(View.VISIBLE);
                   binding.cancleClick.setVisibility(View.VISIBLE);
                   binding.imageChoose.setVisibility(View.GONE);
                   binding.boldRelative.setVisibility(View.GONE);
                   binding.underLineRelativ.setVisibility(View.GONE);
                   binding.ItalicClick.setVisibility(View.GONE);
*/
                ColorPickerDialog.newBuilder().setColor(ContextCompat.getColor(act,R.color.black)).show(Objects.requireNonNull(getActivity()));

            }
        });
        FontChanege();
              return binding.getRoot();
    }
    @Override public void onColorItemChange(int colorcode) {

        binding.imageChoose.setBackgroundColor(colorcode);
    }
    public void FontChanege() {
        fontModelList = new ArrayList<>();  //initialized list
        fontModelObject=new FontModel[16];   //Array of model class
        fontStyle = new String[16];  //Array of String
        fontStyle[0] = "font/inter_bold.otf";
        fontStyle[1] = "font/poppins_bold.ttf";
        fontStyle[2] = "font/poppins_light.ttf";
        fontStyle[3] = "font/poppins_medium.ttf";
        fontStyle[4] = "font/poppins_regular.ttf";
        fontStyle[5] = "font/inter_extrabold.otf";
        fontStyle[6] = "font/inter_medium.otf";
        fontStyle[7] = "font/inter_regular.otf";
        fontStyle[8] = "font/inter_semibold.otf";
        fontStyle[9] = "font/worksans_bold.ttf";
        fontStyle[10] = "font/worksans_extrabold.ttf";
        fontStyle[11] = "font/worksans_light.ttf";
        fontStyle[12] = "font/worksans_medium.ttf";
        fontStyle[13] = "font/worksans_regular.ttf";
        fontStyle[14] = "font/worksans_semibold.ttf";
        fontStyle[15] = "font/inter_medium.otf";
        for (int i = 0; i < 16; i++) {
            fontModelObject[i] = new FontModel();
            fontModelObject[i].setFontFaimly(fontStyle[i]);
            fontModelObject[i].setFontId(fontStyle[i].replace("font/","")
                    .replace(".ttf","").replace(".otf","").substring(0,2));
            fontModelList.add(fontModelObject[i]);
        }
        binding.fontRecycler.setLayoutManager(new LinearLayoutManager(act,RecyclerView.HORIZONTAL,false));   //set layout
        binding.fontRecycler.setAdapter(new FontListAdeptor(fontModelList,act));    //set Adapter
    }

}
