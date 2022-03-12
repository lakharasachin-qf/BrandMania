package com.app.brandmania.LetterHead;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Adapter.FontListAdeptor;
import com.app.brandmania.Adapter.FooterAdapter;
import com.app.brandmania.Adapter.VisitingCardAdapter;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.FooterHelper;
import com.app.brandmania.Common.LetterHeadHelper;
import com.app.brandmania.Common.VisitingCardHelper;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Fragment.bottom.ColorPickerFragment;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Interface.AddTextEvent;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.LetterHead.Adapter.FrameAdaptor;
import com.app.brandmania.LetterHead.Adapter.LetterHeadAdapter;
import com.app.brandmania.Model.FontModel;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.LetterHeadModel;
import com.app.brandmania.Model.VisitingCardModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityLetterHeadBinding;
import com.app.brandmania.databinding.LayoutDigitalCardOneBinding;
import com.app.brandmania.databinding.LetterHeadCardOneBinding;
import com.app.brandmania.databinding.LetterHeadCardTwoBinding;
import com.app.brandmania.utils.IFontChangeEvent;
import com.app.brandmania.utils.Utility;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LetterHeadActivity extends BaseActivity implements AddTextEvent, IItaliTextEvent, ITextBoldEvent, IFontChangeEvent {
    ActivityLetterHeadBinding binding;
    Activity act;
    private TextView selectedTextView;
    GestureDetector gestureDetector;
    private int _xDelta;
    private int _yDelta;
    private ArrayList<LetterHeadModel> LetterHeadList;
    private LetterHeadModel CurrentSelectedCard;
    public boolean BOLD_TEXT = false;
    public boolean ITALIC_TEXT = false;
    public int firstText;

    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_letter_head);
        LetterHeadList = new ArrayList<>();
        LetterHeadList.addAll(LetterHeadHelper.getDigitalCardList());
        binding.backButton.setOnClickListener(view -> {
            Intent intent = new Intent(act, HomeActivity.class);
            startActivity(intent);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });
        setLetterHeadAdapter();
        binding.cancelTextProperties.setOnClickListener(v -> {

            binding.textPropertiesLayout.setVisibility(View.GONE);
            binding.cancelTextProperties.setVisibility(View.GONE);
            binding.fontListLayout.setVisibility(View.GONE);
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
        });
        binding.deleteLayout.setOnClickListener(v -> {
            binding.textPropertiesLayout.setVisibility(View.GONE);
            binding.cancelTextProperties.setVisibility(View.GONE);
            binding.fontListLayout.setVisibility(View.GONE);
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));

        });
        dynamicChangeOnText();

        binding.textPropertiesLayoutShow.setOnClickListener(v -> {
            binding.framesViewLayout.setVisibility(View.GONE);
            binding.deleteLayout.setVisibility(View.VISIBLE);
            binding.textAlignmentLayout.setVisibility(View.GONE);
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.cancelTextProperties.setVisibility(View.VISIBLE);
        });

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        binding.chooseCameraIntent.setOnClickListener(v -> {
            binding.framesViewLayout.setVisibility(View.GONE);
            pickerView(true);
        });

        binding.framesLayout.setOnClickListener(v -> {

            if (binding.framesViewLayout.getVisibility() == View.GONE) {
                binding.framesViewLayout.setVisibility(View.VISIBLE);
            } else {
                binding.framesViewLayout.setVisibility(View.GONE);
            }
        });

        setIconForAlignment();
        setIconForAlignmentLayout();

        binding.done.setOnClickListener(v -> {

            if (isTextEditing) {
                binding.textEditorView.setVisibility(View.GONE);
                binding.MainLatterHeadLayout.setEnabled(true);
                binding.MainLatterHeadLayout.setClickable(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.rootLayout.getWindowToken(), 0);
                if (binding.editingBox.getText().toString().trim().length() != 0) {
                    selectedTextView.setText(binding.editingBox.getText().toString());
                    selectedTextView.setTextAlignment(selectedTextAlignment);
                }
            } else {
                binding.textEditorView.setVisibility(View.GONE);
                binding.MainLatterHeadLayout.setEnabled(true);
                binding.MainLatterHeadLayout.setClickable(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.rootLayout.getWindowToken(), 0);
                addTextViewToLayout(binding.editingBox.getText().toString());
            }

            binding.editingBox.setText("");
            binding.editImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

        });

        binding.textAlignmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIconForAlignmentLayout();
            }
        });

        binding.setBoldLayout.setOnClickListener(v -> {
            if (BOLD_TEXT) {
                binding.boldImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                ((ITextBoldEvent) act).onBoldTextChange(false);
                BOLD_TEXT = false;
            } else {

                BOLD_TEXT = true;
                binding.boldImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#faa81e")));
                ((ITextBoldEvent) act).onBoldTextChange(true);

            }
        });

        binding.setItalicLayout.setOnClickListener(v -> {
            if (ITALIC_TEXT) {
                binding.italicImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                ((IItaliTextEvent) act).onItalicTextChange(false);
                ITALIC_TEXT = false;
            } else {
                ITALIC_TEXT = true;
                binding.italicImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#faa81e")));
                ((IItaliTextEvent) act).onItalicTextChange(true);
            }
        });

        binding.addText.setOnClickListener(v -> {
            binding.editImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#faa81e")));
            ((AddTextEvent) act).onAddTextTrigger();
        });
        binding.setColorOnLatterHeadCard.setOnClickListener(v -> {
            showTextColorList(false);
        });
        binding.fontLayout.setOnClickListener(v -> {

            if (binding.fontListLayout.getVisibility() == View.GONE) {
                binding.fontListLayout.setVisibility(View.VISIBLE);

                binding.fontsImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#faa81e")));
            } else {
                binding.fontListLayout.setVisibility(View.GONE);
                binding.fontsImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }
            FontChange();

        });
        setFramesRecycler();
    }

    private LetterHeadAdapter visitingCardAdapter;

    public void setFramesRecycler() {
        Log.e("Frames List", gson.toJson(prefManager.getActiveBrand().getFrame()));
        FrameAdaptor frameAdaptor = new FrameAdaptor(prefManager.getActiveBrand().getFrame(), act);
        binding.framesRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));
        binding.framesRecycler.setHasFixedSize(true);

        FrameAdaptor.onFooterListener onFooterListener = new FrameAdaptor.onFooterListener() {
            @Override
            public void onFooterChoose(String Frames, Integer position) {
                if (position == 0) {
                    binding.footerOverlay.setVisibility(View.GONE);
                } else {
                    binding.footerOverlay.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(Frames).into(binding.letterItemImg);
                }
            }
        };
        frameAdaptor.setFooterListener(onFooterListener);
        binding.framesRecycler.setAdapter(frameAdaptor);
    }

    public void setLetterHeadAdapter() {
        Utility.dismissProgress();
        visitingCardAdapter = new LetterHeadAdapter(LetterHeadList, act);
        LetterHeadAdapter.onLetterHeadListener onLetterHeadListener = (layout, letterHeadModel) -> {
            CurrentSelectedCard = letterHeadModel;
            addDynamicLayout();
        };
        visitingCardAdapter.setListener(onLetterHeadListener);
        binding.letterHeadList.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));
        binding.letterHeadList.setHasFixedSize(true);
        binding.letterHeadList.setAdapter(visitingCardAdapter);
        CurrentSelectedCard = LetterHeadList.get(0);
        addDynamicLayout();
    }

    public void dynamicChangeOnText() {
        binding.phoneLabel.setOnClickListener(v -> {
            firstText = 0;
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.cancelTextProperties.setVisibility(View.VISIBLE);
            binding.deleteLayout.setVisibility(View.GONE);
            binding.textAlignmentLayout.setVisibility(View.GONE);
            // showTextFragmentList(true);
            binding.setColorOnLatterHeadCard.setOnClickListener(v1 -> showTextColorList(true));

        });
        binding.phoneText.setOnClickListener(v -> {
            firstText = 1;
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.cancelTextProperties.setVisibility(View.VISIBLE);
            binding.textAlignmentLayout.setVisibility(View.GONE);
            binding.deleteLayout.setVisibility(View.GONE);
        });
        binding.webLabel.setOnClickListener(v -> {
            firstText = 2;
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.cancelTextProperties.setVisibility(View.VISIBLE);
            binding.textAlignmentLayout.setVisibility(View.GONE);
            binding.deleteLayout.setVisibility(View.GONE);
        });
        binding.webText.setOnClickListener(v -> {
            firstText = 3;
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.cancelTextProperties.setVisibility(View.VISIBLE);
            binding.textAlignmentLayout.setVisibility(View.GONE);
            binding.deleteLayout.setVisibility(View.GONE);
        });
        binding.addressLabel.setOnClickListener(v -> {
            firstText = 4;
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.cancelTextProperties.setVisibility(View.VISIBLE);
            binding.textAlignmentLayout.setVisibility(View.GONE);
            binding.deleteLayout.setVisibility(View.GONE);
        });
        binding.addressText.setOnClickListener(v -> {
            firstText = 5;
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
            binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            binding.textPropertiesLayout.setVisibility(View.VISIBLE);
            binding.cancelTextProperties.setVisibility(View.VISIBLE);
            binding.textAlignmentLayout.setVisibility(View.GONE);
            binding.deleteLayout.setVisibility(View.GONE);
        });
    }

    ArrayList<FontModel> fontModelList = new ArrayList<>();
    FontModel[] fontModelObject;
    String[] fontStyle;

    public void FontChange() {
        fontModelList = new ArrayList<>();  //Initialized list
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
        binding.fontRecycler.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));   //set layout
        binding.fontRecycler.setAdapter(new FontListAdeptor(fontModelList, act));    //set Adapter
    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    ColorPickerFragment bottomSheetFragment;

    public void showTextColorList(boolean textViewColor) {
        bottomSheetFragment = new ColorPickerFragment();

        ColorPickerFragment.OnColorChoose onColorChoose = color -> {

            if (textViewColor) {
                if (firstText == 0) {
                    binding.phoneLabel.setTextColor(color);
                }
            } else {
                VisitingCardHelper.applyTextColorOnLatterHead(color, binding);
            }

            // selectedTextView.setTextColor(color);

        };
        bottomSheetFragment.setOnColorChoose(onColorChoose);
        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void pickerView(boolean viewMode) {
        PickerFragment pickerFragment = new PickerFragment(act);
        pickerFragment.setEnableViewMode(viewMode);
        pickerFragment.setActionId(Constant.PICKER_FIRST);

        PickerFragment.HandlerImageLoad imageLoad = new PickerFragment.HandlerImageLoad() {
            @Override
            public void onGalleryResult(int flag, Bitmap bitmap) {
                if (flag == Constant.PICKER_FIRST) {
                    binding.mainImage.setVisibility(View.VISIBLE);
                    binding.mainImage.setImageBitmap(bitmap);

                }
            }
        };
        pickerFragment.setImageLoad(imageLoad);
        pickerFragment.show(getSupportFragmentManager(), pickerFragment.getTag());
    }

    int CurrentFlagAlign = 1;
    private int selectedTextAlignment;
    private boolean isTextEditing = false;

    @Override
    public void onAddTextTrigger() {
        CurrentFlagAlign = 1;
        binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
        selectedTextAlignment = View.TEXT_ALIGNMENT_TEXT_START;
        binding.editingBox.setGravity(android.view.Gravity.CENTER);
        isTextEditing = false;
        binding.textEditorView.setVisibility(View.VISIBLE);
        binding.MainLatterHeadLayout.setEnabled(false);
        binding.MainLatterHeadLayout.setClickable(false);
    }

    public void setIconForAlignmentLayout() {

        binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        binding.alignImg.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
        binding.alignImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        binding.editingBox.setGravity(android.view.Gravity.CENTER);
        binding.textAlignmentLayout.setOnClickListener(v -> {

            if (CurrentFlagAlign == 0) {
                binding.alignImg.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
                binding.alignImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ad2753")));
                CurrentFlagAlign = 1;
                binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                selectedTextAlignment = View.TEXT_ALIGNMENT_CENTER;
                binding.editingBox.setGravity(android.view.Gravity.CENTER);
            } else if (CurrentFlagAlign == 1) {
                binding.alignImg.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_right_align));
                binding.alignImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#faa81e")));
                CurrentFlagAlign = 2;
                selectedTextAlignment = View.TEXT_ALIGNMENT_TEXT_END;
                binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                binding.editingBox.setGravity(android.view.Gravity.RIGHT | android.view.Gravity.CENTER);
            } else if (CurrentFlagAlign == 2) {
                binding.alignImg.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_left_align));
                binding.alignImg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ed2c66")));
                CurrentFlagAlign = 0;
                selectedTextAlignment = View.TEXT_ALIGNMENT_TEXT_START;
                binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                binding.editingBox.setGravity(android.view.Gravity.LEFT | android.view.Gravity.CENTER);
            }
        });

    }

    public void setIconForAlignment() {

        binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
        binding.editingBox.setGravity(android.view.Gravity.CENTER);
        binding.textAlignment.setOnClickListener(v -> {

            if (CurrentFlagAlign == 0) {
                binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
                CurrentFlagAlign = 1;
                binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                selectedTextAlignment = View.TEXT_ALIGNMENT_CENTER;
                binding.editingBox.setGravity(android.view.Gravity.CENTER);
            } else if (CurrentFlagAlign == 1) {
                binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_right_align));
                CurrentFlagAlign = 2;
                selectedTextAlignment = View.TEXT_ALIGNMENT_TEXT_END;
                binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                binding.editingBox.setGravity(android.view.Gravity.RIGHT | android.view.Gravity.CENTER);
            } else if (CurrentFlagAlign == 2) {
                binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_left_align));
                CurrentFlagAlign = 0;
                selectedTextAlignment = View.TEXT_ALIGNMENT_TEXT_START;
                binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                binding.editingBox.setGravity(android.view.Gravity.LEFT | android.view.Gravity.CENTER);
            }
        });

    }

    TextView selectedForEdit;
    TextView textView;

    public void addTextViewToLayout(String string) {

        if (selectedForEdit != null)
            selectedForEdit.setBackground(null);

        textView = new TextView(act, null);
        CardView cardLayout = (CardView) findViewById(R.id.MainLatterHeadLayout);
        CardView.LayoutParams mRparams = new CardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRparams.leftMargin = 200;
        mRparams.topMargin = 600;
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(mRparams);
        textView.setText(string);
        textView.setTextColor(Color.parseColor("#0C0C0C"));
        textView.setTextSize(13);
        Typeface face = Typeface.createFromAsset(getAssets(), "font/inter_semibold.otf");
        textView.setTypeface(face);
        textView.setTextAlignment(selectedTextAlignment);
        //textView.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        textView.setOnTouchListener(onTouchListeneForEditText());
        cardLayout.addView(textView);
        textView.setBackground(null);
        textView.setOnClickListener(new DoubleClickListener() {
            @Override
            protected void onDoubleClick(View v) {

                binding.mainEditorLayout.setVisibility(View.VISIBLE);
              /* isTextEditing=true;
                binding.textEditorView.setVisibility(View.VISIBLE);
                binding.contentView.setEnabled(false);
                selectedTextView = textView;
                binding.editingBox.setText(textView.getText().toString());*/
            }

            @Override
            protected void onSingleClick(View v) {
                selectedTextView = textView;
                selectedTextView.setId(0);
                binding.phoneLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
                binding.phoneText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
                binding.webLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
                binding.webText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
                binding.addressLabel.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
                binding.addressText.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border_white));
                textView.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            }

        });

    }

    abstract class DoubleClickListener implements View.OnClickListener {
        long lastClickTime = 0;
        long DOUBLE_CLICK_TIME_DELTA = 500;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();

            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;

        }

        protected abstract void onDoubleClick(View v);

        protected abstract void onSingleClick(View v);
    }

    int editMode;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    float angle = 0;
    float x = 0, y = 0;
    private boolean isLoadItalic = false;
    private boolean isLoadBold = false;
    int footerLayout = 1;

    private View.OnTouchListener onTouchListeneForEditText() {
        return new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            public boolean onTouch(View view, MotionEvent event) {
                if (selectedForEdit != null) {
                    selectedForEdit.setBackground(null);
                    selectedTextView = null;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.rootLayout.getWindowToken(), 0);
                }

                TextView textView = (TextView) view;

                textView.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
                selectedForEdit = textView;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap
                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if (numberOfTaps == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    selectedTextAlignment = textView.getTextAlignment();
                                    if (selectedTextAlignment == View.TEXT_ALIGNMENT_TEXT_START) {
                                        CurrentFlagAlign = 0;
                                        binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_left_align));
                                    }
                                    if (selectedTextAlignment == View.TEXT_ALIGNMENT_CENTER) {
                                        CurrentFlagAlign = 1;
                                        binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
                                    }
                                    if (selectedTextAlignment == View.TEXT_ALIGNMENT_TEXT_END) {
                                        CurrentFlagAlign = 2;
                                        binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_right_align));
                                    }

                                    isTextEditing = true;
                                    binding.textEditorView.setVisibility(View.VISIBLE);
                                    binding.MainLatterHeadLayout.setEnabled(false);
                                    binding.MainLatterHeadLayout.setClickable(false);
                                    selectedTextView = textView;
                                    binding.editingBox.setText(selectedTextView.getText().toString());
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                } else {
                    final int X = (int) event.getRawX();
                    final int Y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            CardView.LayoutParams lParams = (CardView.LayoutParams) view.getLayoutParams();
                            _xDelta = X - lParams.leftMargin;
                            _yDelta = Y - lParams.topMargin;
                            editMode = DRAG;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            oldDist = spacing(event);
                            if (oldDist > 10f) {
                                editMode = ZOOM;
                            }
                            d = rotation(event);
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            editMode = NONE;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            if (editMode == DRAG) {
                                CardView.LayoutParams mRparams = (CardView.LayoutParams) view.getLayoutParams();
                                mRparams.leftMargin = X - _xDelta;
                                mRparams.topMargin = Y - _yDelta;
                                mRparams.rightMargin = -250;
                                mRparams.bottomMargin = -250;
                                view.setLayoutParams(mRparams);
                            } else if (editMode == ZOOM) {
                                CardView.LayoutParams mRparams = (CardView.LayoutParams) view.getLayoutParams();

                                if (event.getPointerCount() == 2) {
                                    newRot = rotation(event);
                                    float r = newRot - d;
                                    angle = r;
                                    x = event.getRawX();
                                    y = event.getRawY();

                                    view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();
                                    x = event.getRawX();
                                    y = event.getRawY();
                                    mRparams.leftMargin = X - _xDelta;
                                    mRparams.topMargin = Y - _yDelta;
                                    mRparams.rightMargin = -250;
                                    mRparams.bottomMargin = -250;
                                    view.setLayoutParams(mRparams);
                                }
                            }
                            break;
                    }
                    // root.invalidate();
                    return false;
                }
            }
        };
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    @Override
    public void onBoldTextChange(boolean Bold) {
        if (Bold) {
            isLoadBold = Bold;

            if (selectedForEdit != null) {

                Utility.setBold(selectedForEdit, true);
            } else {
                FooterHelper.baseForBoldForLatterHead(Bold, footerLayout, binding);
            }
        } else {
            if (selectedForEdit != null) {

                Utility.setBold(selectedForEdit, false);
            } else {
                FooterHelper.baseForItalicInText(Bold, footerLayout, binding);
            }
        }
    }

    @Override
    public void onItalicTextChange(boolean Italic) {

        isLoadItalic = Italic;
        if (Italic) {

            if (selectedForEdit != null) {

                Utility.setItalicText(selectedForEdit, true);
            } else {
                FooterHelper.baseForItalicInText(Italic, footerLayout, binding);
            }

        } else {
            if (selectedForEdit != null) {

                Utility.setItalicText(selectedForEdit, false);
            } else {
                FooterHelper.baseForItalicInText(Italic, footerLayout, binding);
            }
        }

    }

    public void addDynamicLayout() {
        binding.container.removeAllViews();
        if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_ONE) {
            LetterHeadCardOneBinding oneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.letter_head_card_one, null, false);
            CurrentSelectedCard.setOneBinding(oneBinding);
            binding.container.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.container.requestLayout();
            binding.container.addView(oneBinding.getRoot());
            View view = oneBinding.getRoot();
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();


        } else if (CurrentSelectedCard.getLayoutType() == VisitingCardModel.LAYOUT_TWO) {
            LetterHeadCardTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.letter_head_card_two, null, false);
            CurrentSelectedCard.setTwoBinding(twoBinding);
            binding.container.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.container.requestLayout();
            binding.container.addView(twoBinding.getRoot());
            View view = twoBinding.getRoot();
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
        }
    }

    //For font change
    @Override
    public void onFontChangeListenert(String Font) {

        FooterHelper.baseForFontChangeOnLatterHead(act, footerLayout, Font, binding);
    }

}