package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.app.brandmania.Adapter.FrameInterFace;
import com.app.brandmania.Adapter.ItemeInterFace;
import com.app.brandmania.Adapter.MultiListItem;

import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Interface.IUnderLineTextEvent;
import com.app.brandmania.R;
import com.app.brandmania.Utils.IFontChangeEvent;
import com.app.brandmania.databinding.ActivityOnlyTextEditorBinding;


import static com.app.brandmania.Utils.Utility.Log;

public class OnlyTextEditorActivity extends AppCompatActivity implements ItemeInterFace, FrameInterFace, ITextColorChangeEvent, IFontChangeEvent, ITextBoldEvent, IItaliTextEvent, IUnderLineTextEvent {
    Activity act;
    private ActivityOnlyTextEditorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_only_text_editor);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Background")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Frame"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Texture")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Text")));
        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final EditTabAdapter adapter = new EditTabAdapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.clorframeLayout.setVisibility(View.VISIBLE);
                binding.viewPager.setCurrentItem(3);
            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.clorframeLayout.getVisibility()==View.GONE)
                {
                    binding.clorframeLayout.setVisibility(View.VISIBLE);
                    binding.textClick.setVisibility(View.GONE);
                }

            }
        });

    }
    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public void onItemSelection(int position, MultiListItem listModel) {
        binding.recoImageee.setImageResource(listModel.getImage());
    }

    @Override
    public void onFrameItemSelection(int position, MultiListItem listModel) {
        binding.recoframe.setImageResource(listModel.getImage());
    }

    @Override
    public void onColorItemChange(int colorcode) {
        binding.textEdit.setTextColor(colorcode);
    }

    @Override
    public void onFontChangeListenert(String Font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
        binding.textEdit.setTypeface(custom_font);
    }

    @Override
    public void onBoldTextChange(boolean Bold)
    {
        if (Bold) {
            Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            binding.textEdit.setTypeface(  binding.textEdit.getTypeface(), Typeface.BOLD);

        }else {
            Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
            binding.textEdit.setTypeface(null, Typeface.NORMAL);

        }

    }


    @Override
    public void onItalicTextChange(boolean Italic) {
        if (Italic) {
            Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            binding.textEdit.setTypeface(  binding.textEdit.getTypeface(), Typeface.ITALIC);

        }else {
            Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
            binding.textEdit.setTypeface(null, Typeface.NORMAL);

        }
    }

    @Override
    public void onUnderLineItalic(boolean Left) {
        if (Left) {
            Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            binding.textEdit.setPaintFlags( binding.textEdit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else
        {
            Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
          //  .setTypeface(null, Typeface.NORMAL);
            binding.textEdit.setPaintFlags(0);
        }
    }
}