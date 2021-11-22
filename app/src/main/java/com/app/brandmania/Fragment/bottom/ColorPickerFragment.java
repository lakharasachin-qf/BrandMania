package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentColorsPickerBinding;
import com.app.brandmania.utils.CodeReUse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorPickerFragment extends BottomSheetDialogFragment implements ColorPickerView.OnColorChangedListener {
    private Activity act;
    private View view;
    private FragmentColorsPickerBinding binding;
    private ColorPickerFragment fragment;
    private OnColorChoose onColorChoose;

    public OnColorChoose getOnColorChoose() {
        return onColorChoose;
    }

    public void setOnColorChoose(OnColorChoose onColorChoose) {
        this.onColorChoose = onColorChoose;
    }

    public interface OnColorChoose {
        void onColorSelected(int color);
    }

    public ColorPickerFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//            CodeReUse.setWhiteNavigationBar(dialog, getActivity());
//        }
//        return dialog;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            CodeReUse.setWhiteNavigationBar(dialog, getActivity());
        }
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheet != null;
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight() / 3;
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_colors_picker, container, false);
        view = binding.getRoot();
        act = getActivity();
        fragment = this;
        binding.titleText.setText("Pick Your Color");
        binding.cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!act.isDestroyed() && !act.isFinishing())
                    fragment.dismiss();
            }
        });
        binding.colorPickerView.setOnColorChangedListener(this);
        binding.codeEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (isValidHexaCode(binding.codeEdt.getText().toString())) {
                        try {
                            int color = Color.parseColor(binding.codeEdt.getText().toString());
                            binding.colorPickerView.setColor(color);
                            binding.codeEdt.setText(String.valueOf(color));
                        } catch (IllegalArgumentException iae) {
                            Toast.makeText(act, "Enter valid color code", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                return false;
            }
        });
        binding.codeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    // Function to validate hexadecimal color code .
    public static boolean isValidHexaCode(String str) {
        String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern p = Pattern.compile(regex);

        if (str == null) {
            return false;
        }

        Matcher m = p.matcher(str);
        return m.matches();
    }


    @Override
    public void onColorChanged(int newColor) {
        (onColorChoose).onColorSelected(newColor);
        binding.codeEdt.setText(String.valueOf(newColor));
    }
}
