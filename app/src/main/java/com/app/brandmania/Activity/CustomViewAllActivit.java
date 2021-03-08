package com.app.brandmania.Activity;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Adapter.EditPicAddapter;
import com.app.brandmania.Adapter.FilterListener;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.FrameInterFace;
import com.app.brandmania.Adapter.IImageFromGalary;
import com.app.brandmania.Adapter.ItemeInterFace;
import com.app.brandmania.Adapter.MultiListItem;

import com.app.brandmania.Common.DialogHelpers;
import com.app.brandmania.Common.FooterHelper;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Connection.ThumbnailCallback;
import com.app.brandmania.DataBase.DBManager;
import com.app.brandmania.DataBase.DatabaseHelper;
import com.app.brandmania.Interface.IBackendFrameSelect;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.IImageBritnessEvent;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.IRemoveFrame;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.IrotateEvent;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.IFontChangeEvent;
import com.app.brandmania.Utils.Utility;

import com.app.brandmania.databinding.ActivityCustomViewAllBinding;
import com.app.brandmania.databinding.DialogUpgradeDownloadLimitExpireBinding;
import com.app.brandmania.databinding.DialogUpgradeLayoutEnterpriseBinding;
import com.app.brandmania.databinding.LayoutForLoadEightBinding;
import com.app.brandmania.databinding.LayoutForLoadFiveBinding;
import com.app.brandmania.databinding.LayoutForLoadFourBinding;
import com.app.brandmania.databinding.LayoutForLoadNineBinding;
import com.app.brandmania.databinding.LayoutForLoadOneBinding;
import com.app.brandmania.databinding.LayoutForLoadSevenBinding;
import com.app.brandmania.databinding.LayoutForLoadSixBinding;
import com.app.brandmania.databinding.LayoutForLoadTenBinding;
import com.app.brandmania.databinding.LayoutForLoadThreeBinding;
import com.app.brandmania.databinding.LayoutForLoadTwoBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zomato.photofilters.imageprocessors.Filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.app.brandmania.Fragment.top.EditTab.setBrightness;

public class CustomViewAllActivit extends BaseActivity implements FrameInterFace, ItemeInterFace,alertListenerCallback,
        IImageFromGalary,ITextColorChangeEvent,IFontChangeEvent,ITextBoldEvent,IItaliTextEvent,ColorPickerDialogListener,IColorChange,
        ColorPickerView.OnColorChangedListener,ITextSizeEvent,onFooterSelectListener, View.OnTouchListener,FilterListener,
        IImageBritnessEvent, IrotateEvent, ThumbnailCallback, IBackendFrameSelect, IRemoveFrame {
    public static final int VIEW_RECOMDATION = 0;
    Activity act;
    File new_file;
    EditText selectedForEdit;
    private int _xDelta;
    private int _yDelta;
    EditText myEditText;
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
    int isDownloadOrSharingOrFavPending=-1;
    // these PointF objects are used to record the point(s) the user is touching
    Matrix savedMatrix = new Matrix();
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    Bitmap selectedImageBitmap=null;

    private PhotoEditor mPhotoEditor;
    public DBManager dbManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_custom_view_all);
        dbManager=new DBManager(act);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        gson = new Gson();
        preafManager=new PreafManager(act);
        binding.backImage.setOnTouchListener((View.OnTouchListener) act);
        colorCodeForBackground= ContextCompat.getColor(act,R.color.colorPrimary);
        binding.logoEmptyState.setOnTouchListener(onTouchListener());
        binding.logoCustom.setOnTouchListener(onTouchListener());
        gestureDetector = new GestureDetector(this, new CustomViewAllActivit.SingleTapConfirm());
//        binding.backImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                myEditText.setCursorVisible(false);
//                myEditText.clearFocus();
//
//                return false;
//            }
//        });
//        binding.elementCustomFrame.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                myEditText.setCursorVisible(false);
//                myEditText.clearFocus();
//
//                return false;
//            }
//        });

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

              //  Toast.makeText(act,"hghjgjh",Toast.LENGTH_LONG).show();
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
        }
        else
        {
            binding.logoEmptyState.setVisibility(View.GONE);
            binding.logoCustom.setVisibility(View.GONE);
        }

       // gestureDetector = new GestureDetector(this, new CustomViewAllActivit().SingleTapConfirm());
        RelativeLayout mRlayout = (RelativeLayout) findViewById(R.id.CustomImageMain);
        RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        myEditText = new EditText(act,null);
        mRparams.leftMargin = 200;
        mRparams.topMargin = 600;
        myEditText.setLayoutParams(mRparams);
        myEditText.setText("Add Text");
        myEditText.setTextColor(Color.parseColor("#0C0C0C"));
        myEditText.setTextSize(13);
        Typeface face = Typeface.createFromAsset(getAssets(), "font/inter_semibold.otf");
        myEditText.setTypeface(face);
        myEditText.setCursorVisible(true);
        myEditText.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        myEditText.setOnTouchListener(onTouchListeneForEditText());
        mRlayout.addView(myEditText);
        myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {

                   selectedForEdit=myEditText;
                    myEditText.setCursorVisible(true);
                    binding.viewPager.setCurrentItem(4);
                    editorFragment=4;
                }
            }
        });





        if (getIntent().hasExtra("flag")) {
            int flag = getIntent().getIntExtra("flag", -1);
            if (flag == VIEW_RECOMDATION) {
                showingView = VIEW_RECOMDATION;
            }
        }
        bottomFramgment();
        binding.backImage.setTag("0");
        loadFirstImage();
    }
    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        //  Utility.Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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

                           /* if (IntroCounter == 1) {
                                startIntro(binding.shareIcon, "Share", "Share Your Image Directly");
                            }
                            else if (IntroCounter == 2) {
                                startIntro(binding.fabroutIcon, "Save", "Save To Your Brand");
                            }
                            else if (IntroCounter == 3) {
                                if (binding.logoEmptyState.getVisibility()==View.VISIBLE)
                                    startIntro(binding.logoEmptyState, "Brand Logo", "Click on icon for choose your logo\n you can also move logo around anywhere in the image");
                                else
                                    startIntro(binding.logoCustom, "Brand Logo", "Click your logo to move around anywhere in the image");
                            }else {
                                showTabIntro(binding.viewPager.getChildAt(0), "Category", "Choose your image as you want");
                            }*/
                    }
                })
                .build()
                .show();
    }
    public void startIntroForFrameOnly(View view, String title, String desc) {

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
/*
                            if (IntroCounter == 1) {
                                startIntroForFrameOnly(binding.customAddressEdit1, "Add Text", "Add text like email-id or address");
                            }
                            if (IntroCounter == 2) {
                                startIntroForFrameOnly(binding.customeContactEdit1, "Add Text", "Add your contact no. here");
                            }
                            if (IntroCounter == 3) {
                                startIntroForFrameOnly(binding.bottomBarView1, "Background", "Change background color as you want");
                            }
                            if (IntroCounter == 4) {
                                startIntroForFrameOnly(binding.bottomBarView2, "Background", "Change background color as you want");
                            }*/

                    }
                })
                .build()
                .show();
    }
    int tabIndex=1;
    boolean needToIntro=false;
    public void showIntroForTabLayout(){


    }
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
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Image")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Footer")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Frame")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Background")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Text")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Edit")));
        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"),Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final EditPicAddapter adapter = new EditPicAddapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(7);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()

        {
            @Override
            public void onTabSelected (TabLayout.Tab tab){
                binding.viewPager.setCurrentItem(tab.getPosition());
                editorFragment=tab.getPosition();
               // handler(editorFragment);

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

            if (binding.logoEmptyState.getVisibility()==View.VISIBLE)
                startIntro(binding.logoEmptyState, "Brand Logo", "Click on icon for choose your logo\n you can resize and move logo around anywhere in the image");
            else
                startIntro(binding.logoCustom, "Brand Logo", "Click your logo to move around anywhere in the image");

            preafManager.setViewAllCustomeImageActivityIntro(false);

        }else {
            //showTabIntro(binding.viewPager.getChildAt(0), "Category", "Choose your image as you want");
        }

    }
    @Override public void onFrameItemSelection(int position, MultiListItem listModel) {
        binding.frameImage.setDrawingCacheEnabled(true);
        binding.frameImage.setImageDrawable(ContextCompat.getDrawable(act,listModel.getImage()));
        //binding.backImage.setOnTouchListener(touchListener);
        //binding.frameImage.setOnTouchListener(null);
        binding.frameImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                if (bmp!=null) {
                    int color = bmp.getPixel((int) event.getX(), (int) event.getY());
                    if (color == Color.TRANSPARENT) {


                        binding.frameImage.setOnTouchListener(null);
                        isFirstTouchOnImage=true;
                        if (binding.backImage.getTag().toString().equals("1"))
                        {
                            // binding.backImage.setOnTouchListener(touchListener);
                        }else{

                        }

                      //  Toast.makeText(act, "Transperent", Toast.LENGTH_SHORT).show();
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
    public void LoadDataToUI(){
        preafManager=new PreafManager(act);
        if (imageFromGalaryModel != null) {
           // binding.simpleProgressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(imageFromGalaryModel.getUri()).into(binding.backImage);
        } else {
            // binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }

        if (selectedFooterModel==null)
            loadFirstImage();
    }

    @Override public void onImageFromGalaryItemSelection(int position, ImageFromGalaryModel listModel) {
        try {
          //  mPhotoEditor.clearAllViews();
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

        }
    }
    @Override public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        myEditText.setCursorVisible(false);
        myEditText.clearFocus();

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
//        binding.elementFooter.setVisibility(View.VISIBLE);
//        binding.elementFooter.setVisibility(View.VISIBLE);

        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(borderSize), colorCodeForBackground);

        selectedFooterModel = footerModel;
        addDynamicFooter(footerLayout,false);
      ///  forCheckFavorite();

        changeBorderColorAsFrame();
        loadSameColorToBackgroundAndTextAgain();
        ((ITextSizeEvent) act).onfontSize(previousFontSize);
    }




    //for adding footer dynamically
    int footerLayout = 1;
    private LayoutForLoadOneBinding oneBinding;
    private LayoutForLoadTwoBinding twoBinding;
    private LayoutForLoadThreeBinding threeBinding;
    private LayoutForLoadFourBinding fourBinding;
    private LayoutForLoadFiveBinding fiveBinding;
    private LayoutForLoadSixBinding sixBinding;
    private LayoutForLoadSevenBinding sevenBinding;
    private LayoutForLoadEightBinding eightBinding;
    private LayoutForLoadNineBinding nineBinding;
    private LayoutForLoadTenBinding tenBinding;
    private void addDynamicFooter(int layoutType,boolean isReload) {
        binding.elementFooter.removeAllViews();
        footerLayout=layoutType;
        if (layoutType== FooterModel.LAYOUT_FRAME_ONE) {
            oneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_one, null, false);
            binding.elementFooter.addView(oneBinding.getRoot());

            FooterHelper.loadFrameFirstData(act,oneBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.main);
            mainLayout1=(RelativeLayout) findViewById(R.id.addressLayoutElement2);
//            oneBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            oneBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            oneBinding.addressLayoutElement.setOnTouchListener(onTouchListenerrr());
            oneBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_TWO) {
            twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            binding.elementFooter.addView(twoBinding.getRoot());

            FooterHelper.loadFrameTwoData(act,twoBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.firstView);
            mainLayout1=(RelativeLayout) findViewById(R.id.secondView);
//            twoBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            twoBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            twoBinding.locationLayout.setOnTouchListener(onTouchListenerrr());
//            twoBinding.websiteLayout.setOnTouchListener(onTouchListenerrr());
            twoBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_THREE) {
            threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());

            FooterHelper.loadFrameThreeData(act,threeBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.section1);
            mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
//            threeBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            threeBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            threeBinding.loactionLayout.setOnTouchListener(onTouchListenerrr());
//            threeBinding.websiteEdtLayout.setOnTouchListener(onTouchListenerrr());
            threeBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_FOUR) {
            fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_four, null, false);
            binding.elementFooter.addView(fourBinding.getRoot());

            FooterHelper.loadFrameFourData(act,fourBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.section1);
            //   mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
//            fourBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            fourBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            fourBinding.locationLayout.setOnTouchListener(onTouchListenerrr());
//            fourBinding.websiteLayout.setOnTouchListener(onTouchListenerrr());
            fourBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FIVE) {
            fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_five, null, false);
            binding.elementFooter.addView(fiveBinding.getRoot());

            FooterHelper.loadFrameFiveData(act,fiveBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.main);
            mainLayout1 = (RelativeLayout) findViewById(R.id.element2);
            //   mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
//            fiveBinding.element0.setOnTouchListener(onTouchListenerrr());
//            fiveBinding.elementMobile.setOnTouchListener(onTouchListenerrr());
//            fiveBinding.elementEmail.setOnTouchListener(onTouchListenerrr());
            fiveBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SIX) {
            sixBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_six, null, false);
            binding.elementFooter.addView(sixBinding.getRoot());
            FooterHelper.loadFrameSixData(act,sixBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.containerElement);
//            sixBinding.socialFollow.setOnTouchListener(onTouchListenerrr());
//            sixBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
            sixBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_SEVEN) {
            sevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_seven, null, false);
            binding.elementFooter.addView(sevenBinding.getRoot());
            FooterHelper.loadFrameSevenData(act,sevenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element0);
           // sevenBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
            mainLayout1 = (RelativeLayout) findViewById(R.id.socialFollow);
            //sevenBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
            //sevenBinding.socialLayout.setOnTouchListener(onTouchListenerrr());
            sevenBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_EIGHT) {
            eightBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_eight, null, false);
            binding.elementFooter.addView(eightBinding.getRoot());
            FooterHelper.loadFrameEightData(act,eightBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element1);
           // eightBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
            //eightBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
            mainLayout1= (RelativeLayout) findViewById(R.id.element2);
            //eightBinding.addressLayoutElement.setOnTouchListener(onTouchListenerrr());
            eightBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });

        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_NINE) {
            nineBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_nine, null, false);
            binding.elementFooter.addView(nineBinding.getRoot());
            FooterHelper.loadFrameNineData(act,nineBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.firstLayout);
//            nineBinding.gmailText.setOnTouchListener(onTouchListenerrr());
//            nineBinding.contactText.setOnTouchListener(onTouchListenerrr());
//            nineBinding.soialLayout.setOnTouchListener(onTouchListenerrr());
            nineBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });

        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_TEN) {
            tenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_ten, null, false);
            binding.elementFooter.addView(tenBinding.getRoot());
            FooterHelper.loadFrameTenData(act,tenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.addressLayout);
            mainLayout1 = (RelativeLayout) findViewById(R.id.layout);
         //                    tenBinding.addressEdtLayout.setOnTouchListener(onTouchListenerrr());
            //tenBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
            //tenBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
            tenBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedForEdit=null;
                    myEditText.clearFocus();
                }
            });
        }
    }
    @Override public void onColorSelected(int dialogId, int colorCode) {
    }
    @Override public void onDialogDismissed(int dialogId) {

    }
    public void loadSameColorToBackgroundAndTextAgain() {

        if (colorCodeForTextColor!=0) {
            if (footerLayout == 1) {
                FooterHelper.ChangeTextColorForFrameOne(act,oneBinding,colorCodeForTextColor);
            } else if (footerLayout == 2) {
                FooterHelper.ChangeTextColorForFrameTwo(act,twoBinding,colorCodeForTextColor);
            } else if (footerLayout == 3) {
                FooterHelper.ChangeTextColorForFrameThree(act,threeBinding,colorCodeForTextColor);
            } else if (footerLayout == 4) {
                FooterHelper.ChangeTextColorForFrameFour(act,fourBinding,colorCodeForTextColor);
            } else if (footerLayout == 5) {
                FooterHelper.ChangeTextColorForFrameFive(act,fiveBinding,colorCodeForTextColor);
            } else if (footerLayout == 6) {
                FooterHelper.ChangeTextColorForFrameSix(act,sixBinding,colorCodeForTextColor);
            }else if (footerLayout == 7) {
                FooterHelper.ChangeTextColorForFrameSeven(act,sevenBinding,colorCodeForTextColor);
            }else if (footerLayout == 8) {
                FooterHelper.ChangeTextColorForFrameEight(act,eightBinding,colorCodeForTextColor);
            }else if (footerLayout == 9) {
                FooterHelper.ChangeTextColorForFrameNine(act,nineBinding,colorCodeForTextColor);
            }else if (footerLayout == 10) {
                FooterHelper.ChangeTextColorForFrameTen(act,tenBinding,colorCodeForTextColor);
            }

        }


        if (footerLayout == 1) {
            FooterHelper. ChangeBackgroundColorForFrameOne(act,oneBinding,colorCodeForBackground);
        } else if (footerLayout == 2) {
            FooterHelper. ChangeBackgroundColorForFrameTwo(act,twoBinding,colorCodeForBackground);
        } else if (footerLayout == 3) {

        } else if (footerLayout == 4) {
            FooterHelper.ChangeBackgroundColorForFrameFour(act,fourBinding,colorCodeForBackground);
        } else if (footerLayout == 5) {
            FooterHelper.ChangeBackgroundColorForFrameFive(act,fiveBinding,colorCodeForBackground);
        } else if (footerLayout == 6) {
            FooterHelper.ChangeBackgroundColorForFrameSix(act,sixBinding,colorCodeForBackground);
        }else if (footerLayout == 7) {
            FooterHelper.ChangeBackgroundColorForFrameSeven(act,sevenBinding,colorCodeForBackground);
        }else if (footerLayout == 8) {
            FooterHelper.ChangeBackgroundColorForFrameEight(act,eightBinding,colorCodeForBackground);
        }else if (footerLayout == 9) {
            FooterHelper.ChangeBackgroundColorForFrameNine(act,nineBinding,colorCodeForBackground);
        }else if (footerLayout == 10) {
            FooterHelper.ChangeBackgroundColorForFrameTen(act,tenBinding,colorCodeForBackground);
        }

        changeBorderColorAsFrame();

        //bold
        if (footerLayout == 1) { FooterHelper.makeBoldForOne(oneBinding,isLoadBold);}
        else if (footerLayout == 2) {  FooterHelper.makeItalicForTwo(twoBinding,isLoadBold);}
        else if (footerLayout == 3) { FooterHelper.makeItalicForThree(threeBinding,isLoadBold);}
        else if (footerLayout == 4) { FooterHelper.makeBoldForFour(fourBinding,isLoadBold);}
        else if (footerLayout == 5) { FooterHelper.makeBoldForFive(fiveBinding,isLoadBold); }
        else if (footerLayout == 6) {  FooterHelper.makeBoldForSix(sixBinding,isLoadBold);}
        else if (footerLayout == 7) {  FooterHelper.makeBoldForSeven(sevenBinding,isLoadBold);}
        else if (footerLayout == 8)   {  FooterHelper.makeBoldForEight(eightBinding,isLoadBold);}
        else if (footerLayout==9) { FooterHelper.makeBoldForNine(nineBinding,isLoadBold); }
        else if (footerLayout==10) { FooterHelper.makeBoldForTen(tenBinding,isLoadBold); }



        //italic
        if (footerLayout == 1) {
            FooterHelper.makeItalicForOne(oneBinding,isLoadItalic);
        } else if (footerLayout == 2) {
            FooterHelper.makeItalicForTwo(twoBinding,isLoadItalic);

        } else if (footerLayout == 3) {

            FooterHelper.makeItalicForThree(threeBinding,isLoadItalic);

        } else if (footerLayout == 4) {
            FooterHelper.makeItalicForFour(fourBinding,isLoadItalic);
        } else if (footerLayout == 5) {
            FooterHelper.makeItalicForFive(fiveBinding,isLoadItalic);
        } else if (footerLayout == 6) {
            FooterHelper.makeItalicForSix(sixBinding,isLoadItalic);
        }
        else if (footerLayout == 7) {

            FooterHelper.makeItalicForSeven(sevenBinding,isLoadItalic);

        }

        else if (footerLayout==8) {
            FooterHelper.makeItalicForEight(eightBinding,isLoadItalic);
        }

        else if (footerLayout==9) {
            FooterHelper.makeItalicForNine(nineBinding,isLoadItalic);
        }
        else if (footerLayout==10) {
            FooterHelper.makeItalicForTen(tenBinding,isLoadItalic);

        }



        if (!loadDefaultFont.isEmpty()) {
            if (footerLayout == 1) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                oneBinding.gmailText.setTypeface(custom_font);
                oneBinding.contactText.setTypeface(custom_font);
                oneBinding.locationText.setTypeface(custom_font);
            } else if (footerLayout == 2) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                twoBinding.gmailText.setTypeface(custom_font);
                twoBinding.contactText.setTypeface(custom_font);
                twoBinding.locationText.setTypeface(custom_font);
                twoBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 3) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                threeBinding.gmailText.setTypeface(custom_font);
                threeBinding.contactText.setTypeface(custom_font);
                threeBinding.locationText.setTypeface(custom_font);
                threeBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 4) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                fourBinding.gmailText.setTypeface(custom_font);
                fourBinding.contactText.setTypeface(custom_font);
                fourBinding.locationText.setTypeface(custom_font);
                fourBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 5) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                fiveBinding.gmailText.setTypeface(custom_font);
                fiveBinding.phoneTxt.setTypeface(custom_font);
                fiveBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 6) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                sixBinding.textElement1.setTypeface(custom_font);
                sixBinding.contactText.setTypeface(custom_font);
            } else if (footerLayout == 7) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                sevenBinding.brandNameText.setTypeface(custom_font);
                sevenBinding.gmailText.setTypeface(custom_font);
                sevenBinding.contactText.setTypeface(custom_font);


            } else if (footerLayout == 8) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                eightBinding.brandNameText.setTypeface(custom_font);
                eightBinding.gmailText.setTypeface(custom_font);
                eightBinding.contactText.setTypeface(custom_font);
                eightBinding.locationText.setTypeface(custom_font);


            } else if (footerLayout == 9) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                nineBinding.brandNameText.setTypeface(custom_font);
                nineBinding.gmailText.setTypeface(custom_font);
                nineBinding.contactText.setTypeface(custom_font);


            } else if (footerLayout == 10) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), loadDefaultFont);
                tenBinding.locationText.setTypeface(custom_font);
                tenBinding.gmailText.setTypeface(custom_font);
                tenBinding.contactText.setTypeface(custom_font);


            }
        }


    }
    //for Text Color change
    @Override public void onColorChanged(int colorCode) {




        if (preafManager.getActiveBrand()!=null) {

            if (editorFragment == 4 && selectedForEdit != null) {
                selectedForEdit.setTextColor(colorCode);

            } else if (editorFragment == 4) {
                colorCodeForTextColor = colorCode;

                if (footerLayout == 1) {
                    FooterHelper.ChangeTextColorForFrameOne(act, oneBinding, colorCode);
                } else if (footerLayout == 2) {
                    FooterHelper.ChangeTextColorForFrameTwo(act, twoBinding, colorCode);
                } else if (footerLayout == 3) {
                    FooterHelper.ChangeTextColorForFrameThree(act, threeBinding, colorCode);
                } else if (footerLayout == 4) {
                    FooterHelper.ChangeTextColorForFrameFour(act, fourBinding, colorCode);
                } else if (footerLayout == 5) {
                    FooterHelper.ChangeTextColorForFrameFive(act, fiveBinding, colorCode);
                } else if (footerLayout == 6) {
                    FooterHelper.ChangeTextColorForFrameSix(act, sixBinding, colorCode);
                } else if (footerLayout == 7) {
                    FooterHelper.ChangeTextColorForFrameSeven(act, sevenBinding, colorCode);
                } else if (footerLayout == 8) {
                    FooterHelper.ChangeTextColorForFrameEight(act, eightBinding, colorCode);
                } else if (footerLayout == 9) {
                    FooterHelper.ChangeTextColorForFrameNine(act, nineBinding, colorCode);
                } else if (footerLayout == 10) {
                    FooterHelper.ChangeTextColorForFrameTen(act, tenBinding, colorCode);
                }
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
      //  Toast.makeText(act, editorFragment+"fdgdf", Toast.LENGTH_SHORT).show();
        if (editorFragment==3){

            if (footerLayout==1){
                FooterHelper.ChangeBackgroundColorForFrameOne(act,oneBinding,colorCode);
            }else if (footerLayout==2){
                FooterHelper. ChangeBackgroundColorForFrameTwo(act,twoBinding,colorCode);
            }else if (footerLayout==3){

            }else if (footerLayout==4){
                FooterHelper.ChangeBackgroundColorForFrameFour(act,fourBinding,colorCode);
            } else if (footerLayout == 5) {
                FooterHelper.ChangeBackgroundColorForFrameFive(act,fiveBinding,colorCode);
            } else if (footerLayout == 6) {
                FooterHelper.ChangeBackgroundColorForFrameSix(act,sixBinding,colorCode);
            }
            else if (footerLayout==7){
                FooterHelper.ChangeBackgroundColorForFrameSeven(act,sevenBinding,colorCode);
            }
            else if (footerLayout==8){
                FooterHelper.ChangeBackgroundColorForFrameEight(act,eightBinding,colorCode);
            }
            else if (footerLayout==9){
                FooterHelper.ChangeBackgroundColorForFrameNine(act,nineBinding,colorCode);
            }
            else if (footerLayout==10){

                FooterHelper.ChangeBackgroundColorForFrameTen(act,tenBinding,colorCode);
            }
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
                // selectedForEdit.setTextColor(colorCode);
            } else if (footerLayout == 1) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                oneBinding.gmailText.setTypeface(custom_font);
                oneBinding.contactText.setTypeface(custom_font);
                oneBinding.locationText.setTypeface(custom_font);
            } else if (footerLayout == 2) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                twoBinding.gmailText.setTypeface(custom_font);
                twoBinding.contactText.setTypeface(custom_font);
                twoBinding.locationText.setTypeface(custom_font);
                twoBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 3) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                threeBinding.gmailText.setTypeface(custom_font);
                threeBinding.contactText.setTypeface(custom_font);
                threeBinding.locationText.setTypeface(custom_font);
                threeBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 4) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                fourBinding.gmailText.setTypeface(custom_font);
                fourBinding.contactText.setTypeface(custom_font);
                fourBinding.locationText.setTypeface(custom_font);
                fourBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 5) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                fiveBinding.gmailText.setTypeface(custom_font);
                fiveBinding.phoneTxt.setTypeface(custom_font);
                fiveBinding.websiteText.setTypeface(custom_font);
            } else if (footerLayout == 6) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                sixBinding.textElement1.setTypeface(custom_font);
                sixBinding.contactText.setTypeface(custom_font);
            } else if (footerLayout == 7) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                sevenBinding.brandNameText.setTypeface(custom_font);
                sevenBinding.gmailText.setTypeface(custom_font);
                sevenBinding.contactText.setTypeface(custom_font);


            } else if (footerLayout == 8) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                eightBinding.brandNameText.setTypeface(custom_font);
                eightBinding.gmailText.setTypeface(custom_font);
                eightBinding.contactText.setTypeface(custom_font);
                eightBinding.locationText.setTypeface(custom_font);


            } else if (footerLayout == 9) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                nineBinding.brandNameText.setTypeface(custom_font);
                nineBinding.gmailText.setTypeface(custom_font);
                nineBinding.contactText.setTypeface(custom_font);


            } else if (footerLayout == 10) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                tenBinding.locationText.setTypeface(custom_font);
                tenBinding.gmailText.setTypeface(custom_font);
                tenBinding.contactText.setTypeface(custom_font);


            }
        }
        else {
            if (editorFragment == 4 && selectedForEdit != null) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                selectedForEdit.setTypeface(custom_font);
                // selectedForEdit.setTextColor(colorCode);
            }
        }

    }
    //for font size
    @Override public void onfontSize(int textsize) {

        if (preafManager.getActiveBrand() != null) {

            if (editorFragment == 4 && selectedForEdit != null) {
                selectedForEdit.setTextSize(textsize);
            } else if (footerLayout == 1) {
                FooterHelper.makeTextSizeForOne(oneBinding, textsize);
            } else if (footerLayout == 2) {
                FooterHelper.makeTextSizeForTwo(twoBinding, textsize);
            } else if (footerLayout == 3) {
                FooterHelper.makeTextSizeForThree(threeBinding, textsize);
            } else if (footerLayout == 4) {
                FooterHelper.makeTextSizeForFour(fourBinding, textsize);
            } else if (footerLayout == 5) {
                FooterHelper.makeTextSizeForFive(fiveBinding, textsize);
            } else if (footerLayout == 6) {
                FooterHelper.makeTextSizeForSix(sixBinding, textsize);
            } else if (footerLayout == 7) {
                FooterHelper.makeTextSizeForSeven(sevenBinding, textsize);
            } else if (footerLayout == 8) {
                FooterHelper.makeTextSizeForEight(eightBinding, textsize);
            } else if (footerLayout == 9) {
                FooterHelper.makeTextSizeForNine(nineBinding, textsize);
            } else if (footerLayout == 10) {
                FooterHelper.makeTextSizeForTen(tenBinding, textsize);
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

                } else if (footerLayout == 1) {
                    FooterHelper.makeBoldForOne(oneBinding, true);
                } else if (footerLayout == 2) {
                    FooterHelper.makeBoldForTwo(twoBinding, true);
                } else if (footerLayout == 3) {
                    FooterHelper.makeBoldForThree(threeBinding, true);
                } else if (footerLayout == 4) {
                    FooterHelper.makeBoldForFour(fourBinding, true);
                } else if (footerLayout == 5) {
                    FooterHelper.makeBoldForFive(fiveBinding, true);
                } else if (footerLayout == 6) {
                    FooterHelper.makeBoldForSix(sixBinding, true);
                } else if (footerLayout == 7) {
                    FooterHelper.makeBoldForSeven(sevenBinding, true);
                } else if (footerLayout == 8) {
                    FooterHelper.makeBoldForEight(eightBinding, true);
                } else if (footerLayout == 9) {
                    FooterHelper.makeBoldForNine(nineBinding, true);
                } else if (footerLayout == 10) {
                    FooterHelper.makeBoldForOne(oneBinding, true);
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

                } else if (footerLayout == 1) {
                    FooterHelper.makeBoldForOne(oneBinding, false);
                } else if (footerLayout == 2) {
                    FooterHelper.makeBoldForTwo(twoBinding, false);
                } else if (footerLayout == 3) {
                    FooterHelper.makeBoldForThree(threeBinding, false);
                } else if (footerLayout == 4) {
                    FooterHelper.makeBoldForFour(fourBinding, false);
                } else if (footerLayout == 5) {
                    FooterHelper.makeBoldForFive(fiveBinding, false);
                } else if (footerLayout == 6) {
                    FooterHelper.makeBoldForSix(sixBinding, false);
                } else if (footerLayout == 7) {
                    FooterHelper.makeBoldForSeven(sevenBinding, false);
                } else if (footerLayout == 8) {
                    FooterHelper.makeBoldForEight(eightBinding, false);
                } else if (footerLayout == 9) {
                    FooterHelper.makeBoldForNine(nineBinding, false);
                } else if (footerLayout == 10) {
                    FooterHelper.makeBoldForOne(oneBinding, false);
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
                } else if (footerLayout == 1) {
                    FooterHelper.makeItalicForOne(oneBinding, true);
                } else if (footerLayout == 2) {
                    FooterHelper.makeItalicForTwo(twoBinding, true);
                } else if (footerLayout == 3) {
                    FooterHelper.makeItalicForThree(threeBinding, true);
                } else if (footerLayout == 4) {
                    FooterHelper.makeItalicForFour(fourBinding, true);
                } else if (footerLayout == 5) {
                    FooterHelper.makeItalicForFive(fiveBinding, true);
                } else if (footerLayout == 6) {
                    FooterHelper.makeItalicForSix(sixBinding, true);
                } else if (footerLayout == 7) {
                    FooterHelper.makeItalicForSeven(sevenBinding, true);
                } else if (footerLayout == 8) {
                    FooterHelper.makeItalicForEight(eightBinding, true);
                } else if (footerLayout == 9) {
                    FooterHelper.makeItalicForNine(nineBinding, true);
                } else if (footerLayout == 10) {
                    FooterHelper.makeItalicForTen(tenBinding, true);
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
                } else if (footerLayout == 1) {
                    FooterHelper.makeItalicForOne(oneBinding, false);
                } else if (footerLayout == 2) {
                    FooterHelper.makeItalicForTwo(twoBinding, false);
                } else if (footerLayout == 3) {
                    FooterHelper.makeItalicForThree(threeBinding, false);
                } else if (footerLayout == 4) {
                    FooterHelper.makeItalicForFour(fourBinding, false);
                } else if (footerLayout == 5) {
                    FooterHelper.makeItalicForFive(fiveBinding, false);
                } else if (footerLayout == 6) {
                    FooterHelper.makeItalicForSix(sixBinding, false);
                } else if (footerLayout == 7) {
                    FooterHelper.makeItalicForSeven(sevenBinding, false);
                } else if (footerLayout == 8) {
                    FooterHelper.makeItalicForEight(eightBinding, false);
                } else if (footerLayout == 9) {
                    FooterHelper.makeItalicForNine(nineBinding, false);
                } else if (footerLayout == 10) {
                    FooterHelper.makeItalicForTen(tenBinding, false);
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




    private View.OnTouchListener onTouchListenerrr() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                      //  Toast.makeText(act, "thanks for new location!", Toast.LENGTH_SHORT).show();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }
//                mainLayout1.invalidate();
                return true;
            }
        };
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




    public void askForDownloadImage(){
        alertDialogBuilder = new AlertDialog.Builder(act);
        alertDialogBuilder.setTitle("Save image");
        alertDialogBuilder.setMessage("You sure to save your image?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        requestAgain();
                        saveImageToGallery(false,false);
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }





    //api for access rights
    private void getImageDownloadRights(String flag) {
        Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.CUSTOM_FRAME_ACCESS);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.CUSTOM_FRAME_ACCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissLoadingTran();
                Utility.Log("Access-Rights-Response:", response);
                JSONObject respJson = ResponseHandler.createJsonObject(response);
                if (ResponseHandler.getBool(respJson, "status")) {
                    JSONArray dataJson = ResponseHandler.getJSONArray(respJson, "data");
                    try {
                        String frameCount = ResponseHandler.getString(dataJson.getJSONObject(0), "frame_counter").equals("") ? "0" : ResponseHandler.getString(dataJson.getJSONObject(0), "frame_counter");
                        FrameCountForDownload = Integer.parseInt(frameCount);
                        int imageCounter=Integer.parseInt( ResponseHandler.getString(dataJson.getJSONObject(0),"total_img_counter").equalsIgnoreCase("Unlimited") ?"-1": ResponseHandler.getString(dataJson.getJSONObject(0),"total_img_counter"));

                        int used_img_counter = ResponseHandler.getString(dataJson.getJSONObject(0), "frame_counter").equals("") ? 0  : Integer.parseInt(ResponseHandler.getString(dataJson.getJSONObject(0), "used_img_counter"));


                        if (ResponseHandler.getBool(dataJson.getJSONObject(0), "status")) {
                            canDownload = true;
                            if (Utility.isUserPaid(preafManager.getActiveBrand())){

                                if (imageCounter==-1 || used_img_counter <= imageCounter) {
                                    if (flag.equalsIgnoreCase("Download"))
                                        askForDownloadImage();
                                    else {
                                        requestAgain();
                                        saveImageToGallery(true, false);
                                    }
                                }else {
                                    DialogHelpers.downloadLimitExpireDialog(act,"Your download limit is expired for your current package. To get more images please upgrade your package");
                                }

                            }else {
                                if (flag.equalsIgnoreCase("Download"))
                                    askForDownloadImage();
                                else {
                                    requestAgain();
                                    saveImageToGallery(true, false);
                                }
                            }

                        } else {
                            canDownload = false;
                            DialogHelpers.downloadLimitExpireDialog(act,"You have already used one image for today, As you are free user you can download or share only one image in a day for 7 days. To get more images please upgrade your package");
                            //Toast.makeText(act, "You can't download image bcoz your limit get expire for one day", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    canDownload = false;
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utility.dismissLoadingTran();
                        error.printStackTrace();
//                        String body;
//                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                        Log.e("Load-Get_Exam ", body);

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id", preafManager.getActiveBrand().getId());
                Utility.Log("Params", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }






    //save image with frame either custome or from backend
    public void saveImageToGallery(boolean wantToShare,boolean isFavourite) {
        Utility.showLoadingTran(act);
        Drawable bitmapFrame;
        if (isUsingCustomFrame){
            bitmapFrame=new BitmapDrawable(getResources(), FooterHelper.getCustomFrameInBitmap(binding.CustomImageMain,binding.backImage));
        }else{
            bitmapFrame=new BitmapDrawable(getResources(), FooterHelper.getCustomFrameInBitmap1(binding.CustomImageMain,binding.backImage,binding.frameImage));
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

        FooterHelper.refreshgallery(act,new_file);

        if (!isFavourite) {
            if (wantToShare) {
                if (isUsingCustomFrame) {
                    if (!isRemoveFrame)
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(),selectedFooterModel);
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





    //load firstImage

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
            width = (int)(width / ratio);
        } else {
            // square
            height = binding.backImage.getHeight();
            width = binding.backImage.getWidth();
        }
       binding.backImage.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(selectedImageBitmap, width, height, false)));
        Log.v("filter", "Width and height are " + width + "--" +height);

    }




    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override public boolean onTouch(View view, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    if ((preafManager.getActiveBrand().getLogo().isEmpty() && selectedLogo != null) || preafManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
                        onSelectImageClick(view);
                    } else {
                        // Toast.makeText(act, "once you download or share image. You can't change your logo", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(act)
                                .setMessage("once you download or share image. You can't change your logo")
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
                }else {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();

                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                    view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;
                            break;

                        case MotionEvent.ACTION_UP:

//                            Toast.makeText(act, "I'm here!", Toast.LENGTH_SHORT).show();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
                            break;
                    }

                    binding.elementCustomFrame.invalidate();
                }
                return true;
            }
        };
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
        CropImage.startPickImageActivity(this);
    }




    @Override public void onRemoveSelectEvent() {
        isUsingCustomFrame=true;
        isRemoveFrame=true;
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





    @Override public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }




    //to handle click and drag listener EditBox
    private View.OnTouchListener onTouchListeneForEditText() {
        return new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    //   binding.viewPager.setCurrentItem(5);
                    //   Toast.makeText(act, "click", Toast.LENGTH_SHORT).show();
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
        };
    }
}




