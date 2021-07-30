package com.app.brandadmin.Activity.custom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.brandadmin.Activity.packages.PackageActivity;
import com.app.brandadmin.Adapter.EditPicAddapter;
import com.app.brandadmin.Adapter.FooterModel;
import com.app.brandadmin.Adapter.MultiListItem;
import com.app.brandadmin.Common.Constant;
import com.app.brandadmin.Common.FooterHelper;
import com.app.brandadmin.Common.MakeMyBrandApp;
import com.app.brandadmin.Common.ObserverActionID;
import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Connection.BaseActivity;
import com.app.brandadmin.DataBase.DBManager;
import com.app.brandadmin.DataBase.DatabaseHelper;
import com.app.brandadmin.Fragment.bottom.PickerFragment;
import com.app.brandadmin.Interface.AddTextEvent;
import com.app.brandadmin.Interface.FilterListener;
import com.app.brandadmin.Interface.FrameInterFace;
import com.app.brandadmin.Interface.IBackendFrameSelect;
import com.app.brandadmin.Interface.IColorChange;
import com.app.brandadmin.Interface.IImageBritnessEvent;
import com.app.brandadmin.Interface.IImageFromGalary;
import com.app.brandadmin.Interface.IItaliTextEvent;
import com.app.brandadmin.Interface.IRemoveFrame;
import com.app.brandadmin.Interface.ITextBoldEvent;
import com.app.brandadmin.Interface.ITextColorChangeEvent;
import com.app.brandadmin.Interface.ITextSizeEvent;
import com.app.brandadmin.Interface.IrotateEvent;
import com.app.brandadmin.Interface.ItemeInterFace;
import com.app.brandadmin.Interface.ThumbnailCallback;
import com.app.brandadmin.Interface.alertListenerCallback;
import com.app.brandadmin.Interface.onFooterSelectListener;
import com.app.brandadmin.Model.ImageFromGalaryModel;
import com.app.brandadmin.Model.ImageList;
import com.app.brandadmin.Model.LayoutModelClass;
import com.app.brandadmin.R;
import com.app.brandadmin.Utils.CodeReUse;
import com.app.brandadmin.Utils.IFontChangeEvent;
import com.app.brandadmin.Utils.Utility;
import com.app.brandadmin.databinding.ActivityCustomViewAllBinding;
import com.app.brandadmin.databinding.DialogDiscardImageBinding;
import com.app.brandadmin.databinding.DialogUpgradeLayoutEnterpriseBinding;
import com.app.brandadmin.databinding.LayoutForLoadEightBinding;
import com.app.brandadmin.databinding.LayoutForLoadFiveBinding;
import com.app.brandadmin.databinding.LayoutForLoadFourBinding;
import com.app.brandadmin.databinding.LayoutForLoadNineBinding;
import com.app.brandadmin.databinding.LayoutForLoadOneBinding;
import com.app.brandadmin.databinding.LayoutForLoadSevenBinding;
import com.app.brandadmin.databinding.LayoutForLoadSixBinding;
import com.app.brandadmin.databinding.LayoutForLoadTenBinding;
import com.app.brandadmin.databinding.LayoutForLoadThreeBinding;
import com.app.brandadmin.databinding.LayoutForLoadTwoBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.app.brandadmin.Fragment.top.EditTab.setBrightness;

public class CustomViewAllActivit extends BaseActivity implements FrameInterFace, ItemeInterFace, alertListenerCallback,
        IImageFromGalary, ITextColorChangeEvent, IFontChangeEvent, ITextBoldEvent, IItaliTextEvent, ColorPickerDialogListener, IColorChange,
        ColorPickerView.OnColorChangedListener, ITextSizeEvent, onFooterSelectListener, View.OnTouchListener, FilterListener,
        IImageBritnessEvent, IrotateEvent, ThumbnailCallback, IBackendFrameSelect, IRemoveFrame, AddTextEvent {
    public static final int VIEW_RECOMDATION = 0;

    File new_file;
    TextView selectedForEdit;
    private int _xDelta;
    private int _yDelta;
    //EditText myEditText;
    AlertDialog.Builder alertDialogBuilder;
    private boolean isUserFree = true;
    private boolean canDownload = true;
    private int FrameCountForDownload = 2;
    private boolean isLoadBold=false;
    private boolean isLoadItalic=false;
    private boolean isLoadUnderLine = false;
    private String loadDefaultFont="";
    int editorFragment;
    private Bitmap selectedLogo;
    private ImageList selectedBackendFrame = null;
    private boolean mIsFilterVisible;
    ImageFromGalaryModel imageFromGalaryModel;
    private Uri mCropImageUri;
    GestureDetector gestureDetector;
    private FooterModel selectedFooterModel;
    private ActivityCustomViewAllBinding binding;
    Gson gson;
    PreafManager preafManager;
    private int colorCodeForBackground=0;
    int windowwidth;
    int windowheight;
    private int previousFontSize=-1;
    private ViewGroup mainLayout;
    ArrayList<MultiListItem> menuModels = new ArrayList<>();
    private MultiListItem listModel;
    Drawable yourDrawable;
    private int colorCodeForTextColor=0;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    MotionEvent onClickTimeHelper;
    boolean isFirstTouchOnImage=false;
    private int showingView = -1;
    private int xDelta, yDelta;
    private ViewGroup mainLayout1;
    private boolean isUsingCustomFrame = true;
    private boolean isRemoveFrame=false;
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
    Matrix matrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    int isDownloadOrSharingOrFavPending = -1;
    Matrix savedMatrix = new Matrix();
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    Bitmap selectedImageBitmap = null;

    private PhotoEditor mPhotoEditor;
    public DBManager dbManager;


    int CurrentFlagAlign = 1;
    private int selectedTextAlignment;

    public void setIconForAlignment() {

        binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
        binding.editingBox.setGravity(android.view.Gravity.CENTER);
        binding.textAlignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(act, R.layout.activity_custom_view_all);
        dbManager = new DBManager(act);
        gson = new Gson();
        preafManager = new PreafManager(act);
        binding.backImage.setOnTouchListener((View.OnTouchListener) act);
        colorCodeForBackground= ContextCompat.getColor(act,R.color.colorPrimary);
        binding.logoEmptyState.setOnTouchListener(onTouchListener());
        setIconForAlignment();
        binding.logoCustom.setOnTouchListener(onTouchListener());
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manuallyEnablePermission(1)) {
                    if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                        askForUpgradeToEnterpisePackage();
                        return;
                    }
                    requestAgain();
                    saveImageToGallery(false, false);
                }
            }
        });
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.fabroutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.backImage.setRotation(binding.backImage.getRotation() + 90);
            }
        });
        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manuallyEnablePermission(2)) {
                    if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                        askForUpgradeToEnterpisePackage();
                        return;
                    }
                    requestAgain();
                    saveImageToGallery(true, false);
                }

            }
        });

        if (preafManager.getActiveBrand()!=null) {
            if (preafManager.getActiveBrand().getLogo() != null && !preafManager.getActiveBrand().getLogo().isEmpty()) {
                binding.logoEmptyState.setVisibility(View.GONE);
                binding.logoCustom.setVisibility(View.VISIBLE);
                binding.logoCustom.setVisibility(View.VISIBLE);
                Glide.with(act)
                        .load(preafManager.getActiveBrand().getLogo())
                        .into(binding.logoCustom);
                binding.logoCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSelectImageClick(view);
                    }
                });
            } else {
                binding.logoEmptyState.setVisibility(View.VISIBLE);
                binding.logoCustom.setVisibility(View.GONE);

                binding.logoCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSelectImageClick(view);
                    }
                });


            }
        } else {
            binding.logoEmptyState.setVisibility(View.GONE);
            binding.logoCustom.setVisibility(View.GONE);
        }


        if (getIntent().hasExtra("flag")) {
            int flag = getIntent().getIntExtra("flag", -1);
            if (flag == VIEW_RECOMDATION) {
                showingView = VIEW_RECOMDATION;
            }
        }
        bottomFramgment();
        binding.backImage.setTag("0");
        loadFirstImage();


        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextEditing) {
                    binding.textEditorView.setVisibility(View.GONE);
                    binding.contentView.setEnabled(true);
                    binding.contentView.setClickable(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 0);
                    if (binding.editingBox.getText().toString().trim().length() != 0) {
                        selectedTextView.setText(binding.editingBox.getText().toString());
                        selectedTextView.setTextAlignment(selectedTextAlignment);
                    } else {
                        binding.CustomImageMain.removeView(selectedTextView);
                        selectedTextView = null;
                    }
                } else {
                    binding.textEditorView.setVisibility(View.GONE);
                    binding.contentView.setEnabled(true);
                    binding.contentView.setClickable(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 0);
                    addTextViewToLayout(binding.editingBox.getText().toString());
                }
                binding.editingBox.setText("");
            }
        });

        binding.CustomImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTextView = null;
                selectedForEdit = null;
                for (int i = 0; i < binding.CustomImageMain.getChildCount(); i++) {
                    if (binding.CustomImageMain.getChildAt(i) instanceof TextView) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 0);
                        TextView textView = (TextView) binding.CustomImageMain.getChildAt(i);
                        textView.setBackground(null);
                    }
                }

            }
        });

        binding.logoCustom.setTag("0");


    }

    abstract static class DoubleClickListener implements View.OnClickListener {
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

    private boolean isTextEditing = false;
    private TextView selectedTextView;

    public void addTextViewToLayout(String string) {

        if (selectedForEdit != null)
            selectedForEdit.setBackground(null);

        TextView textView = new TextView(act, null);
        RelativeLayout mRlayout = (RelativeLayout) findViewById(R.id.CustomImageMain);
        RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        mRlayout.addView(textView);
        textView.setOnClickListener(new DoubleClickListener() {
            @Override
            protected void onDoubleClick(View v) {
              /*  isTextEditing=true;
                binding.textEditorView.setVisibility(View.VISIBLE);
                binding.contentView.setEnabled(false);
                selectedTextView = textView;
                binding.editingBox.setText(textView.getText().toString());*/
            }

            @Override
            protected void onSingleClick(View v) {
                selectedTextView = textView;
                textView.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
            }
        });


    }


    private int IntroCounter = 0;

    public void startIntro(View view, String title, String desc) {

        new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(desc)
                .setGravity(Gravity.center)
                .setDismissType(DismissType.anywhere)
                .setTargetView(view)
                .setContentTextSize(12)
                .setTitleTextSize(14)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        IntroCounter++;
                        showTabIntro(binding.viewPager.getChildAt(0), "Image", "Choose your image as you want");
                    }
                })
                .build()
                .show();
    }
    int tabIndex=1;
    boolean needToIntro=false;
    public void showTabIntro(View view, String title, String desc) {

        new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(desc)
                .setGravity(Gravity.center)
                .setDismissType(DismissType.targetView)
                .setTargetView(view)
                .setContentTextSize(12)
                .setTitleTextSize(14)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        binding.viewPager.setCurrentItem(tabIndex);
                        tabIndex++;
                    }
                })
                .build()
                .show();
    }
    public void bottomFramgment() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Image")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Footer")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Frame")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Background")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Text")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Edit")));
        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final EditPicAddapter adapter = new EditPicAddapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(7);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                editorFragment = tab.getPosition();
                if (needToIntro) {
                    if (tabIndex == 1) {
                        showTabIntro(binding.viewPager, "Footer", "if you want to custom frame then choose your own footer");
                    }
                    if (tabIndex == 2) {
                        showTabIntro(binding.viewPager, "Frames", "Apply custom frame");
                    }

                    if (tabIndex == 3) {
                        showTabIntro(binding.viewPager, "Background", "Choose your background color as you want");
                    }
                    if (tabIndex == 4) {
                        showTabIntro(binding.viewPager, "Text", "Change your text and icon color as u want");

                    }
                    if (tabIndex == 5) {
                        showTabIntro(binding.viewPager, "Edit", "Change your image filter as u want");
                        needToIntro=false;
                    }

                }

            }

            @Override
            public void onTabUnselected (TabLayout.Tab tab){
            }

            @Override
            public void onTabReselected (TabLayout.Tab tab){
            }
        });

        if (preafManager.getViewAllCustomeImageActivityIntro()) {
            needToIntro=true;

            if (binding.logoEmptyState.getVisibility() == View.VISIBLE)
                startIntro(binding.logoEmptyState, "Brand Logo", "Click on icon for choose your logo\n you can resize and move logo around anywhere in the image");
            else
                startIntro(binding.logoCustom, "Brand Logo", "Click your logo to move around anywhere in the image");

            preafManager.setViewAllCustomeImageActivityIntro(false);

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onFrameItemSelection(int position, MultiListItem listModel) {
        binding.frameImage.setDrawingCacheEnabled(true);
        binding.frameImage.setImageDrawable(ContextCompat.getDrawable(act, listModel.getImage()));
        binding.frameImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                if (bmp != null) {
                    @SuppressLint("ClickableViewAccessibility") int color = bmp.getPixel((int) event.getX(), (int) event.getY());
                    if (color == Color.TRANSPARENT) {
                        binding.frameImage.setOnTouchListener(null);
                        isFirstTouchOnImage=true;
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
        binding.customeViewRelative.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = binding.customeViewRelative.getWidth();
                windowheight = binding.customeViewRelative.getHeight();
            }
        });

    }


    @Override public void onImageFromGalaryItemSelection(int position, ImageFromGalaryModel listModel) {
        try {
            imageFromGalaryModel=listModel;
            InputStream inputStream = getContentResolver().openInputStream(listModel.getUri());
            yourDrawable = Drawable.createFromStream(inputStream, listModel.getUri().toString() );
            binding.backImage.setImageDrawable(yourDrawable);
           // LoadDataToUI();
            BitmapDrawable drawable = (BitmapDrawable) binding.backImage.getDrawable();
             selectedImageBitmap= drawable.getBitmap();
             selectedImageBitmap=selectedImageBitmap.copy(Bitmap.Config.ARGB_8888 , true);
            if (selectedFooterModel==null)
                loadFirstImage();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override public void onItemSelection(int position, MultiListItem listModel) {
        binding.frameImage.setImageResource(listModel.getImage());
    }




    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Event", sb.toString());
    }

    //fire on footer select listener
    @Override
    public void onFooterSelectEvent(int footerLayout, FooterModel footerModel) {
        isUsingCustomFrame = true;
        binding.frameImage.setVisibility(View.GONE);
        binding.elementCustomFrame.setVisibility(View.VISIBLE);
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(borderSize), colorCodeForBackground);
        selectedFooterModel = footerModel;
        addDynamicFooter(footerLayout,false);
        changeBorderColorAsFrame();
        loadSameColorToBackgroundAndTextAgain();
        ((ITextSizeEvent) act).onfontSize(previousFontSize);
    }

    //for adding footer dynamically
    int footerLayout = 1;
    LayoutModelClass layoutModelClass;
    private void addDynamicFooter(int layoutType,boolean isReload) {
        layoutModelClass = new LayoutModelClass();
        binding.elementFooter.removeAllViews();
        footerLayout=layoutType;
        if (layoutType== FooterModel.LAYOUT_FRAME_ONE) {
            LayoutForLoadOneBinding oneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_one, null, false);
            binding.elementFooter.addView(oneBinding.getRoot());
            FooterHelper.loadFrameFirstData(act, oneBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.main);
            mainLayout1 = (RelativeLayout) findViewById(R.id.addressLayoutElement2);
            layoutModelClass.setOneBinding(oneBinding);
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_TWO) {
            LayoutForLoadTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            binding.elementFooter.addView(twoBinding.getRoot());
            layoutModelClass.setTwoBinding(twoBinding);
            FooterHelper.loadFrameTwoData(act, twoBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.firstView);
            mainLayout1 = (RelativeLayout) findViewById(R.id.secondView);
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_THREE) {
            LayoutForLoadThreeBinding threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());
            layoutModelClass.setThreeBinding(threeBinding);
            FooterHelper.loadFrameThreeData(act, threeBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.section1);
            mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_FOUR) {
            LayoutForLoadFourBinding fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_four, null, false);
            binding.elementFooter.addView(fourBinding.getRoot());
            layoutModelClass.setFourBinding(fourBinding);
            FooterHelper.loadFrameFourData(act, fourBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.section1);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FIVE) {
            LayoutForLoadFiveBinding fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_five, null, false);
            binding.elementFooter.addView(fiveBinding.getRoot());
            layoutModelClass.setFiveBinding(fiveBinding);
            FooterHelper.loadFrameFiveData(act, fiveBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.main);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SIX) {
            LayoutForLoadSixBinding sixBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_six, null, false);
            binding.elementFooter.addView(sixBinding.getRoot());
            layoutModelClass.setSixBinding(sixBinding);
            FooterHelper.loadFrameSixData(act, sixBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.containerElement);
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_SEVEN) {
            LayoutForLoadSevenBinding sevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_seven, null, false);
            binding.elementFooter.addView(sevenBinding.getRoot());
            layoutModelClass.setSevenBinding(sevenBinding);
            FooterHelper.loadFrameSevenData(act, sevenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element0);
            mainLayout1 = (RelativeLayout) findViewById(R.id.socialFollow);

        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_EIGHT) {
           LayoutForLoadEightBinding eightBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_eight, null, false);
            binding.elementFooter.addView(eightBinding.getRoot());
            layoutModelClass.setEightBinding(eightBinding);
            FooterHelper.loadFrameEightData(act, eightBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element1);
            mainLayout1 = (RelativeLayout) findViewById(R.id.element2);
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_NINE) {
            LayoutForLoadNineBinding nineBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_nine, null, false);
            binding.elementFooter.addView(nineBinding.getRoot());
            layoutModelClass.setNineBinding(nineBinding);
            FooterHelper.loadFrameNineData(act, nineBinding);
           // mainLayout = (RelativeLayout) findViewById(R.id.firstLayout);
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_TEN) {
            LayoutForLoadTenBinding tenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_ten, null, false);
            binding.elementFooter.addView(tenBinding.getRoot());
            layoutModelClass.setTenBinding(tenBinding);
            FooterHelper.loadFrameTenData(act, tenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.addressLayout);
            mainLayout1 = (RelativeLayout) findViewById(R.id.layout);
        }
    }
    @Override public void onColorSelected(int dialogId, int colorCode) {
    }
    @Override public void onDialogDismissed(int dialogId) {

    }
    public void loadSameColorToBackgroundAndTextAgain() {

        if (colorCodeForTextColor != 0) {
            FooterHelper.baseForTextColor(act, footerLayout, layoutModelClass, colorCodeForTextColor);
        }
        FooterHelper.baseForBackground(act, footerLayout, layoutModelClass, colorCodeForBackground);
        changeBorderColorAsFrame();

        FooterHelper.baseForBold(isLoadBold, footerLayout, layoutModelClass);
        FooterHelper.baseForItalic(isLoadItalic, footerLayout, layoutModelClass);

        if (!loadDefaultFont.isEmpty()) {
            FooterHelper.baseForFontChange(act, footerLayout, loadDefaultFont, layoutModelClass);
        }


    }

    //for Text Color change
    @Override public void onColorChanged(int colorCode) {
        if (preafManager.getActiveBrand()!=null) {
            if (editorFragment == 4 && selectedForEdit != null) {
                selectedForEdit.setTextColor(colorCode);
            } else if (editorFragment == 4) {
                colorCodeForTextColor = colorCode;
                FooterHelper.baseForTextColor(act, footerLayout, layoutModelClass, colorCode);
            }
        }
        else
        {
            if (editorFragment == 4 && selectedForEdit != null) {
                selectedForEdit.setTextColor(colorCode);

            }
        }

    }
    //on border size change
    int borderSize;
    @Override public void onBorderSizeChange(int size) {
        borderSize=size;
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(size), colorCodeForBackground);
    }
    //for background color change
    @Override public void onChooseColor(int colorCode) {
        colorCodeForBackground = colorCode;

        if (editorFragment==3){
            FooterHelper.baseForBackground(act, footerLayout, layoutModelClass, colorCode);
            GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
            drawable.setStroke((int) convertDpToPx(borderSize), colorCodeForBackground);
        }
    }
    private int convertDpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public void changeBorderColorAsFrame(){
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(borderSize), colorCodeForBackground);
    }
    @Override public void onColorItemChange(int colorCode) {
    }
    //for font change
    public void onFontChangeListenert(String Font) {
        loadDefaultFont = Font;
        if (preafManager.getActiveBrand() != null) {
            if (editorFragment == 4 && selectedForEdit != null) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                selectedForEdit.setTypeface(custom_font);
            } else {
                FooterHelper.baseForFontChange(act, footerLayout, Font, layoutModelClass);
            }
        }
        else {
            if (editorFragment == 4 && selectedForEdit != null) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                selectedForEdit.setTypeface(custom_font);
            }
        }

    }
    //for font size
    @Override public void onfontSize(int textsize) {

        if (preafManager.getActiveBrand() != null) {

            if (editorFragment == 4 && selectedForEdit != null) {
                selectedForEdit.setTextSize(textsize);
            } else {
                FooterHelper.baseForTextSize(textsize, footerLayout, layoutModelClass);
            }
        }
        else
        {
            if (editorFragment == 4 && selectedForEdit != null) {
                selectedForEdit.setTextSize(textsize);
            }
        }


    }
    //for bold text
    @Override public void onBoldTextChange(boolean Bold) {
        if (Bold) {


            if (preafManager.getActiveBrand()!=null) {
                isLoadBold = Bold;
                if (editorFragment == 4 && selectedForEdit != null) {
                    Utility.setBold(selectedForEdit, true);
                } else {
                    FooterHelper.baseForBold(Bold, footerLayout, layoutModelClass);
                }
            }
            else
            {
                if (editorFragment == 4 && selectedForEdit != null) {
                    Utility.setBold(selectedForEdit, true);

                }
            }
        }else {


            if (preafManager.getActiveBrand()!=null) {
                if (editorFragment == 4 && selectedForEdit != null) {
                    Utility.setBold(selectedForEdit, false);

                } else {
                    FooterHelper.baseForBold(Bold, footerLayout, layoutModelClass);
                }
            }
            else
            {
                if (editorFragment == 4 && selectedForEdit != null) {
                    Utility.setBold(selectedForEdit, false);
                }
            }
        }

    }
    //for italic
    @Override public void onItalicTextChange(boolean Italic) {
        isLoadItalic=Italic;
        if (Italic) {
            if (preafManager.getActiveBrand() != null) {
                if (editorFragment == 4 && selectedForEdit != null) {

                    Utility.setItalicText(selectedForEdit, true);
                } else   {
                    FooterHelper.baseForItalic(Italic,footerLayout,layoutModelClass);
                }
            }
            else
            {
                if (editorFragment == 4 && selectedForEdit != null) {

                    Utility.setItalicText(selectedForEdit, true);
                }
            }
        }
        else
        {
            if (preafManager.getActiveBrand()!=null)
            {
                if (editorFragment == 4 && selectedForEdit != null) {

                    Utility.setItalicText(selectedForEdit, false);
                } else   {
                    FooterHelper.baseForItalic(Italic,footerLayout,layoutModelClass);
                }
            }
            else
            {
                if (editorFragment==4 && selectedForEdit!=null) {

                    Utility.setItalicText(selectedForEdit, false);
                }
            }
        }
    }
     public boolean manuallyEnablePermission(int pendingActivity) {
        isDownloadOrSharingOrFavPending=pendingActivity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (ContextCompat.checkSelfPermission(act,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    new AlertDialog.Builder(act)
                            .setMessage("Allow BrandMania to access photos, files to download and share images ")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", act.getPackageName(), null)));
                                }
                            })
                            .show();
                    return false;
                }else {
                    return true;
                }

            }else {
                if (ContextCompat.checkSelfPermission(act,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    new AlertDialog.Builder(act)
                            .setMessage("Allow BrandMania to access photos, files to download and share images ")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    ActivityCompat.requestPermissions(act,
                                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                                            CodeReUse.ASK_PERMISSSION);
                                }
                            })
                            .show();
                    return false;
                }else {

                    return true;
                }


            }
        }else {
            if (ContextCompat.checkSelfPermission(act,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                new AlertDialog.Builder(act)
                        .setMessage("Allow BrandMania to access photos, files to download and share images ")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                ActivityCompat.requestPermissions(act,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE},
                                        CodeReUse.ASK_PERMISSSION);
                            }
                        })
                        .show();
                return false;
            }else {

                return true;
            }
        }

    }
    // ask to upgrade package to 999 for use all frames
    DialogUpgradeLayoutEnterpriseBinding enterpriseBinding;
    public void askForUpgradeToEnterpisePackage() {
        enterpriseBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout_enterprise, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(enterpriseBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(enterpriseBinding.getRoot());

        enterpriseBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile","1");

                act.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        enterpriseBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        enterpriseBinding.element3.setText("You have selected premium footer design. To use this design please upgrade your package");
        //alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    @Override public void alertListenerClick() {
        requestAgain();
    }
    //save image with frame either custome or from backend
    public void saveImageToGallery(boolean wantToShare,boolean isFavourite) {
        Log.e("TTTT", "sSSS");
        Utility.showLoadingTran(act);
        Drawable bitmapFrame;
        for (int i = 0; i < binding.CustomImageMain.getChildCount(); i++) {
            if (binding.CustomImageMain.getChildAt(i) instanceof TextView) {
                TextView textView = (TextView) binding.CustomImageMain.getChildAt(i);
                textView.setBackground(null);
            }
        }
        if (isUsingCustomFrame) {
            bitmapFrame = new BitmapDrawable(getResources(), FooterHelper.getCustomFrameInBitmap(binding.CustomImageMain, binding.backImage));
        } else {
            bitmapFrame = new BitmapDrawable(getResources(), FooterHelper.getCustomFrameInBitmap1(binding.CustomImageMain, binding.backImage, binding.frameImage));
        }

        Drawable ImageDrawable = (BitmapDrawable) binding.backImage.getDrawable();
        Bitmap merged = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(merged);
        bitmapFrame.setBounds(0, 0, 1000, 1000);
        ImageDrawable.setBounds(0, 0, 1000, 1000);
        ImageDrawable.draw(canvas);
        bitmapFrame.draw(canvas);

        FileOutputStream fileOutputStream = null;
        File file = FooterHelper.createNewFolderForImages();
        if (!file.exists() && !file.mkdirs()) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "image" +System.currentTimeMillis()+ ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        new_file = new File(file_name);
        Log.e("new_file",new_file.getAbsolutePath()+"\n"+new_file.getPath());

        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = merged;
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FooterHelper.refreshgallery(act, new_file);

        if (!isFavourite) {
            if (wantToShare) {
                if (isUsingCustomFrame) {
                    if (!isRemoveFrame)
                        ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);
                    binding.FrameImageDuplicate.setImageBitmap(null);
                } else {
                }
                FooterHelper.triggerShareIntent(act,new_file,merged);
                dbManager.insertStaticContent(new_file.toString(), DatabaseHelper.FLAG_DOWNLOAD);
            } else {
                Toast.makeText(act, "Your image is downloaded", Toast.LENGTH_SHORT).show();
                if (isUsingCustomFrame)
                {
                    if (!isRemoveFrame)
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(),selectedFooterModel);
                    // addDynamicFooter(selectedFooterModel.getLayoutType(), true);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);
                    binding.FrameImageDuplicate.setImageBitmap(null);
                } else {
                    binding.FrameImageDuplicate.setImageBitmap(null);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);

                   Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.frameImage);
                }

                dbManager.insertStaticContent(new_file.toString(), DatabaseHelper.FLAG_DOWNLOAD);
            }

            InputStream inputStream = null;
            try {

                inputStream = getContentResolver().openInputStream(imageFromGalaryModel.getUri());
                yourDrawable = Drawable.createFromStream(inputStream, imageFromGalaryModel.getUri().toString() );
                binding.backImage.setImageDrawable(yourDrawable);
                BitmapDrawable drawable = (BitmapDrawable) binding.backImage.getDrawable();
                selectedImageBitmap=drawable.getBitmap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }else {
            if (isUsingCustomFrame) {
                if (!isRemoveFrame)
                ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(),selectedFooterModel);
                binding.FrameImageDuplicate.setVisibility(View.GONE);
                binding.FrameImageDuplicate.setImageBitmap(null);
            } else {
                Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.frameImage);
            }
            dbManager.insertStaticContent(new_file.toString(), DatabaseHelper.FLAG_FAVORITE);
        }
        Utility.dismissLoadingTran();
    }
    private void requestAgain() {
        ActivityCompat.requestPermissions(act,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                CodeReUse.ASK_PERMISSSION);
    }
    public void loadFirstImage(){
        if (preafManager.getActiveBrand()!=null) {
            FooterModel model = new FooterModel();
            model.setLayoutType(FooterModel.LAYOUT_FRAME_SEVEN);
            model.setFree(true);
            model.setAddress(preafManager.getActiveBrand().getAddress());
            model.setEmailId(preafManager.getActiveBrand().getEmail());
            model.setContactNo(preafManager.getActiveBrand().getPhonenumber());
            model.setWebsite(preafManager.getActiveBrand().getWebsite());

            ((onFooterSelectListener) act).onFooterSelectEvent(FooterModel.LAYOUT_FRAME_SEVEN, model);
        }
    }



    @Override public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);

    }




    @Override public void onimageBritness(int britness) {

        binding.backImage.setColorFilter(setBrightness(britness));

    }





    @Override public void onRotateImage(int rotate) {
        binding.backImage.setRotation(binding.backImage.getRotation() + 90);
    }





    @Override public void onThumbnailClick(Filter filter) {
        int width = selectedImageBitmap.getWidth();
        int height = selectedImageBitmap.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / binding.backImage.getWidth();
            width = binding.backImage.getWidth();
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / binding.backImage.getHeight();
            height = binding.backImage.getHeight();
            width = (int) (width / ratio);
        } else {
            // square
            height = binding.backImage.getHeight();
            width = binding.backImage.getWidth();
        }
        binding.backImage.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(selectedImageBitmap, width, height, false)));
        Log.v("filter", "Width and height are " + width + "--" + height);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (selectedTextView != null) {

            selectedTextView.setBackground(null);
            selectedTextView = null;
            selectedForEdit = null;
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 0);
        }

        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y); /*
                     * create the transformation in the matrix
                     * of points
                     */
                } else if (mode == ZOOM) {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;
                        /*
                         * setting the scaling of the matrix...if scale > 1 means
                         * zoom in...if scale < 1 means zoom out
                         */
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true;
    }

    float scalediff;
    float angle = 0;
    private float newRot = 0f;
    float dx = 0, dy = 0, x = 0, y = 0;
    private float d = 0f;

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
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

                        if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if (numberOfTaps == 1) {

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (preafManager.getActiveBrand().getLogo().isEmpty()) {
                                        onSelectImageClick(view);
                                    } else {
                                        if (!preafManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
                                            new AlertDialog.Builder(act)
                                                    .setMessage("once you download or share image. You can't change your logo.\nIf you want to change logo please contact to admin.")
                                                    .setCancelable(true)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                            ((alertListenerCallback) act).alertListenerClick();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    }
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }
                if (gestureDetector.onTouchEvent(event)) {

                } else {
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();

                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                    view.getLayoutParams();
                            mode = DRAG;
                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:

                            mode = NONE;
                            Log.d(TAG, "mode=NONE");
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            oldDist = spacing(event);
                            Log.d(TAG, "oldDist=" + oldDist);
                            if (oldDist > 5f) {
                                midPoint(mid, event);
                                mode = ZOOM;
                                Log.d(TAG, "mode=ZOOM");
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();


                            if (mode == DRAG) {
                                layoutParams.leftMargin = x - xDelta;
                                layoutParams.topMargin = y - yDelta;
                                layoutParams.rightMargin = 0;
                                layoutParams.bottomMargin = 0;
                                view.setLayoutParams(layoutParams);
                            } else if (mode == ZOOM) {

                                if (event.getPointerCount() == 2) {
                                    newRot = rotation(event);
                                    float r = newRot - d;
                                    angle = r;
                                    x = (int) event.getRawX();
                                    y = (int) event.getRawY();
                                    float newDist = spacing(event);
                                    if (newDist > 10f) {
                                        float scale = newDist / oldDist * view.getScaleX();
                                        if (scale > 0.6) {
                                            scalediff = scale;
                                            view.setScaleX(scale);
                                            view.setScaleY(scale);
                                        }
                                    }

                                    view.setLayoutParams(layoutParams);
                                }
                            }
                            break;
                    }

                    binding.elementCustomFrame.invalidate();
                }
                return true;
            }
        };
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                binding.logoCustom.setVisibility(View.VISIBLE);
                binding.logoEmptyState.setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.logoCustom)).setImageURI(result.getUri());
                ImageView imageView = ((ImageView) findViewById(R.id.logoCustom));
                selectedLogo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .start(this);

    }


    //For CustomFrame
    public void onSelectImageClick(View view) {
        if (binding.logoCustom.getTag().toString().equalsIgnoreCase("1"))
            pickerView(true, selectedLogo);
        else
            pickerView(false, null);
    }

    private void pickerView(boolean viewMode, Bitmap selectedBitmap) {
        PickerFragment pickerFragment = new PickerFragment(act);
        pickerFragment.setEnableViewMode(viewMode);
        pickerFragment.setActionId(Constant.PICKER_FIRST);

        if (viewMode) {
            pickerFragment.setSelectedBitmapForFullView(selectedBitmap);
        }
        PickerFragment.HandlerImageLoad imageLoad = new PickerFragment.HandlerImageLoad() {
            @Override
            public void onGalleryResult(int flag, Bitmap bitmap) {
                if (flag == Constant.PICKER_FIRST) {
                    selectedLogo = bitmap;
                    binding.logoCustom.setTag("1");
                    binding.logoCustom.setImageBitmap(bitmap);
                    binding.logoCustom.setVisibility(View.VISIBLE);
                    binding.logoEmptyState.setVisibility(View.GONE);
                }

            }
        };
        pickerFragment.setImageLoad(imageLoad);
        pickerFragment.show(getSupportFragmentManager(), pickerFragment.getTag());
    }


    @Override
    public void onRemoveSelectEvent() {
        isUsingCustomFrame = true;
        isRemoveFrame = true;
        // Toast.makeText(act, "dsgfgds", Toast.LENGTH_SHORT).show();
        binding.elementCustomFrame.setVisibility(View.GONE);
        binding.frameImage.setImageBitmap(null);
        binding.FrameImageDuplicate.setImageBitmap(null);

        //    binding.frameImage.setVisibility(View.GONE);
    }




    //to handle click and drag listener
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }



    @Override public void onBackendFrameChoose(ImageList imageList, int position) {
        binding.frameImage.setVisibility(View.VISIBLE);
        binding.elementCustomFrame.setVisibility(View.GONE);
        selectedBackendFrame=imageList;
        Glide.with(getApplicationContext()).load(imageList.getFrame1()).into(binding.frameImage);
        isUsingCustomFrame = false;
      //  forCheckFavorite();
    }



    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.GALLERY_CALLBACK) {
            screenExistDialog();
        }
    }

    @Override public void onBackPressed() {
        if (binding.textEditorView.getVisibility() == View.VISIBLE) {
            binding.textEditorView.setVisibility(View.GONE);
            binding.contentView.setEnabled(true);
            binding.contentView.setClickable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 1);
            return;
        }
        if (editorFragment == 0){
            MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.GALLERY_ACTION);
        }else {
            screenExistDialog();
        }
    }
    androidx.appcompat.app.AlertDialog alertDialog;
    DialogDiscardImageBinding discardImageBinding;
    public void screenExistDialog() {
        if (alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();

        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
          alertDialog = builder.create();
        alertDialog.setContentView(discardImageBinding.getRoot());

        discardImageBinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });
        discardImageBinding.yesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                CodeReUse.activityBackPress(act);
            }
        });
        if (!isFinishing() || !isDestroyed()) {
            alertDialog.setCancelable(true);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }
    }


    int editMode;
    //to handle click and drag listener EditBox
    private View.OnTouchListener onTouchListeneForEditText() {
        return new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            public boolean onTouch(View view, MotionEvent event) {
                if (selectedForEdit != null)
                    selectedForEdit.setBackground(null);
                selectedForEdit = null;
                if (selectedTextView != null)
                    selectedTextView.setBackground(null);
                selectedTextView = null;

                TextView textView = (TextView) view;

                textView.setBackground(ContextCompat.getDrawable(act, R.drawable.editing_text_border));
                selectedForEdit = textView;
                selectedTextView = textView;

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
                                    binding.contentView.setEnabled(false);
                                    binding.contentView.setClickable(false);
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
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
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
                                RelativeLayout.LayoutParams mRparams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                mRparams.leftMargin = X - _xDelta;
                                mRparams.topMargin = Y - _yDelta;
                                mRparams.rightMargin = -250;
                                mRparams.bottomMargin = -250;
                                view.setLayoutParams(mRparams);
                            } else if (editMode == ZOOM) {
                                RelativeLayout.LayoutParams mRparams = (RelativeLayout.LayoutParams) view.getLayoutParams();

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


    @Override
    public void onAddTextTrigger() {
        CurrentFlagAlign = 1;
        binding.editingBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        binding.textAlignment.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_centered_align));
        selectedTextAlignment = View.TEXT_ALIGNMENT_TEXT_START;
        binding.editingBox.setGravity(android.view.Gravity.CENTER);

        isTextEditing = false;
        binding.textEditorView.setVisibility(View.VISIBLE);
        binding.contentView.setEnabled(false);
        binding.contentView.setClickable(false);
    }
}




