package com.app.brandmania.Activity;

import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.brandmania.Adapter.IImageFromGalary;
import com.app.brandmania.Adapter.ColorAndEditTabAdapter;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.IUnderLineTextEvent;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.Utils.IFontChangeEvent;
import com.app.brandmania.Utils.Utility;
import com.google.android.material.tabs.TabLayout;
import com.app.brandmania.Adapter.ItemeInterFace;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityColorAndTextEditBinding;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ColorAndTextEditActivity extends BaseActivity implements IColorChange,ItemeInterFace, ITextBoldEvent,
        IItaliTextEvent, IUnderLineTextEvent, ITextSizeEvent, IImageFromGalary,ColorPickerDialogListener,
        ColorPickerView.OnColorChangedListener,View.OnTouchListener,ITextColorChangeEvent,
        IFontChangeEvent {

    Activity act;
    private ActivityColorAndTextEditBinding binding;
    TextView selectedForEdit;
    View selectedForBackgroundChange;
    int editorFragment;
    Drawable yourDrawable;
    int FramePrimaryOrSecondary=0;
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    float scalediff;
    private float d = 0f;
    private float newRot = 0f;






    private int _xDelta;
    private int _yDelta;
    EditText myEditText;
    GestureDetector gestureDetector;
    @SuppressLint("ResourceAsColor")
    @Override public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_color_and_text_edit);

        init();


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, 250);
        layoutParams.leftMargin = 50;
        layoutParams.topMargin = 50;
        layoutParams.bottomMargin = -250;
        layoutParams.rightMargin = -250;
        binding.editableImageview.setLayoutParams(layoutParams);
        binding.editableImageview.setOnTouchListener(new View.OnTouchListener() {
            RelativeLayout.LayoutParams parms;
            int startwidth;
            int startheight;
            float dx = 0, dy = 0, x = 0, y = 0;
            float angle = 0;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


            final ImageView view1 = (ImageView) view;

            ((BitmapDrawable) view1.getDrawable()).setAntiAlias(true);
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    startwidth = parms.width;
                    startheight = parms.height;
                    dx = motionEvent.getRawX() - parms.leftMargin;
                    dy = motionEvent.getRawY() - parms.topMargin;
                    mode = DRAG;
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(motionEvent);
                    if (oldDist > 10f) {
                        mode = ZOOM;
                    }

                    d = rotation(motionEvent);

                    break;
                case MotionEvent.ACTION_UP:

                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {

                        x = motionEvent.getRawX();
                        y = motionEvent.getRawY();

                        parms.leftMargin = (int) (x - dx);
                        parms.topMargin = (int) (y - dy);

                        parms.rightMargin = 0;
                        parms.bottomMargin = 0;
                        parms.rightMargin = parms.leftMargin + (5 * parms.width);
                        parms.bottomMargin = parms.topMargin + (10 * parms.height);

                        view.setLayoutParams(parms);

                    } else if (mode == ZOOM) {

                        if (motionEvent.getPointerCount() == 2) {

                            newRot = rotation(motionEvent);
                            float r = newRot - d;
                            angle = r;

                            x = motionEvent.getRawX();
                            y = motionEvent.getRawY();

                            float newDist = spacing(motionEvent);
                            if (newDist > 10f) {
                                float scale = newDist / oldDist * view.getScaleX();
                                if (scale > 0.6) {
                                    scalediff = scale;
                                    view.setScaleX(scale);
                                    view.setScaleY(scale);

                                }
                            }

                            view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();

                            x = motionEvent.getRawX();
                            y = motionEvent.getRawY();

                            parms.leftMargin = (int) ((x - dx) + scalediff);
                            parms.topMargin = (int) ((y - dy) + scalediff);

                            parms.rightMargin = 0;
                            parms.bottomMargin = 0;
                            parms.rightMargin = parms.leftMargin + (5 * parms.width);
                            parms.bottomMargin = parms.topMargin + (10 * parms.height);

                            view.setLayoutParams(parms);


                        }
                    }
                    break;
            }

            return true;

        }

});



        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        RelativeLayout mRlayout = (RelativeLayout) findViewById(R.id.main);
        RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myEditText = new EditText(act,null);
        myEditText.setLayoutParams(mRparams);
        myEditText.setHint(R.string.enter_email_id);
        myEditText.setHintTextColor(Color.parseColor("#CDCDCD"));
//        myEditText.setTextColor(Color.parseColor("#E6494848"));
        myEditText.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        myEditText.setOnTouchListener((View.OnTouchListener) act);
        mRlayout.addView(myEditText);





        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Color")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Image")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Frame")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Texture")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Text")));
        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ColorAndEditTabAdapter adapter = new ColorAndEditTabAdapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));



        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                editorFragment=tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b==true)
                {
                    selectedForEdit=myEditText;
                    binding.viewPager.setCurrentItem(3);
                    editorFragment=3;
                }
            }
        });
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FramePrimaryOrSecondary=0;
                selectedForBackgroundChange=binding.backImage;
                binding.viewPager.setCurrentItem(0);
            }
        });



    }


    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Utility.Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public void onColorItemChange(int colorcode) {
        myEditText.setTextColor(colorcode);
    }

    @Override
    public void onFontChangeListenert(String Font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
        myEditText.setTypeface(custom_font);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
    @Override
    public void onChooseColor(int colorCode) {
        if (editorFragment==3 && selectedForEdit!=null) {
            selectedForEdit.setTextColor(colorCode);
        }
        if (editorFragment==0 && selectedForBackgroundChange!=null){
            if (FramePrimaryOrSecondary==0){
                binding.backgroundClick.setVisibility(View.GONE);
                binding.backImage.setBackgroundColor(colorCode);
            }
            selectedForBackgroundChange.setBackgroundColor(colorCode);
        }
    }

    @Override
    public void onBorderSizeChange(int size) {

    }

    @Override public void onItemSelection(int position, MultiListItem listModel) {
        binding.frameImage.setImageResource(listModel.getImage());
    }

    @Override public void onImageFromGalaryItemSelection(int position, ImageFromGalaryModel listModel) {
        try {
            binding.editableImageview.setVisibility(View.VISIBLE);
            InputStream inputStream = getContentResolver().openInputStream(listModel.getUri());
            yourDrawable = Drawable.createFromStream(inputStream, listModel.getUri().toString() );
            binding.editableImageview.setImageDrawable(yourDrawable);
        } catch (FileNotFoundException e) {

        }
    }
    private void init() {
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
    @Override public void onColorSelected(int dialogId, int color) {
        if (editorFragment==3 && selectedForEdit!=null) {
            selectedForEdit.setTextColor(color);
        }
        if (editorFragment==0 && selectedForBackgroundChange!=null){
            if (FramePrimaryOrSecondary==0){
                binding.backgroundClick.setVisibility(View.GONE);
                binding.backImage.setBackgroundColor(color);
            }
            selectedForBackgroundChange.setBackgroundColor(color);
        }
    }
    @Override public void onDialogDismissed(int dialogId) {

    }

    @Override public void onColorChanged(int newColor) {
        if (editorFragment==3 && selectedForEdit!=null) {
            selectedForEdit.setTextColor(newColor);
        }
        if (editorFragment==0 && selectedForBackgroundChange!=null){
            if (FramePrimaryOrSecondary==0){
                binding.backgroundClick.setVisibility(View.GONE);
                binding.backImage.setBackgroundColor(newColor);
            }
            selectedForBackgroundChange.setBackgroundColor(newColor);
        }
    }
    @Override public void onBoldTextChange(boolean Bold) {
        if (Bold) {
            //  Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            myEditText.setTypeface(  myEditText.getTypeface(), Typeface.BOLD);

        }else {
            //  Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
            myEditText.setTypeface(null, Typeface.NORMAL);

        }

    }
    @Override public void onItalicTextChange(boolean Italic) {
        if (Italic) {
            //Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            myEditText.setTypeface(  myEditText.getTypeface(), Typeface.ITALIC);

        }else {
            // Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
            myEditText.setTypeface(null, Typeface.NORMAL);

        }
    }
    @Override public void onUnderLineItalic(boolean Left) {
        if (Left) {
            //  Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            myEditText.setPaintFlags( myEditText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else
        {
            // Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
            //  .setTypeface(null, Typeface.NORMAL);
            myEditText.setPaintFlags(0);
        }
    }
    @Override public void onfontSize(int textsize) {

        myEditText.setTextSize(textsize);
    }
    public boolean onTouch(View view, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            Toast.makeText(act, "click", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams mRparams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    mRparams.leftMargin = X - _xDelta;
                    mRparams.topMargin = Y - _yDelta;
                    mRparams.rightMargin = -250;
                    mRparams.bottomMargin = -250;
                    view.setLayoutParams(mRparams);
                    break;
            }
            // root.invalidate();
            return false;
        }
    }
}