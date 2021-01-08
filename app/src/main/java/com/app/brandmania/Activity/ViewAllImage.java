package com.app.brandmania.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.ImageCateItemeInterFace;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Adapter.ViewAllTopTabAdapter;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.IBackendFrameSelect;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.IUnderLineTextEvent;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.IFontChangeEvent;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityViewAllImageBinding;
import com.app.brandmania.databinding.DialogUpgradeDownloadLimitExpireBinding;
import com.app.brandmania.databinding.DialogUpgradeLayoutEnterpriseBinding;
import com.app.brandmania.databinding.DialogUpgradeLayoutSecondBinding;
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
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;

public class ViewAllImage extends BaseActivity implements ImageCateItemeInterFace,alertListenerCallback, ITextColorChangeEvent, IFontChangeEvent,ITextBoldEvent,
        IItaliTextEvent, ColorPickerDialogListener, IUnderLineTextEvent, IColorChange, ColorPickerView.OnColorChangedListener,
        ITextSizeEvent, onFooterSelectListener, IBackendFrameSelect {

    Activity act;
    ViewPager viewPager;
    private boolean isLoading = false;
    ArrayList<ImageList> AddFavorite=new ArrayList<>();
    private ActivityViewAllImageBinding binding;
    ArrayList<BrandListItem> multiListItems=new ArrayList<>();
    ArrayList<ImageList> menuModels = new ArrayList<>();

    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    public static final int DOWLOAD = 1;
    public static final int ADDFAV = 3;
    private static final int REQUEST_CALL = 1;
    public static final int REMOVEFAV = 3;
    private String is_frame="";
    private String is_payment_pending="";
    private String packagee="";
    ArrayList<FrameItem> viewPagerItems = new ArrayList<>();
    PreafManager preafManager;
    Gson gson;
    String Website;
    private DashBoardItem imageList;
    private ImageList selectedObject;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    FrameItem selectedModelFromView;
    AlertDialog.Builder alertDialogBuilder;
    File new_file;
    private Uri mCropImageUri;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    int editorFragment;
    private int xDelta;
    private int yDelta;
    private ViewGroup mainLayout;
    private boolean isUserFree = true;
    private boolean canDownload = true;
    private int FrameCountForDownload = 2;

    private boolean isUsingCustomFrame = true;


    //Version 3
    private ImageList selectedBackendFrame = null;
    private FooterModel selectedFooterModel;
    private boolean updateLogo = false;
    private Bitmap selectedLogo;

    private int colorCodeForBackground;
    private int colorCodeForTextColor=0;
    private boolean isLoadBold=false;
    private boolean isLoadItalic=false;
    private boolean isLoadUnderLine = false;
    private String loadDefaultFont="";
    private int previousFontSize=-1;
    int isDownloadOrSharingOrFavPending=-1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        //triggerUpgradePackage();
        act.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_all_image);
        preafManager = new PreafManager(this);
        binding.titleName.setSelected(true);
        gson = new Gson();
        selectedObject = gson.fromJson(getIntent().getStringExtra("selectedimage"), ImageList.class);
        getFrame();
        getBrandList();
        Website = preafManager.getActiveBrand().getWebsite();
        imageList = gson.fromJson(getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
        binding.titleName.setText(imageList.getName());

       // getAllImages();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        mainLayout = (RelativeLayout) findViewById(R.id.elementCustomFrame);

        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(0), colorCodeForBackground);

        updateLogo = preafManager.getActiveBrand().getLogo().isEmpty();

        colorCodeForBackground= ContextCompat.getColor(act,R.color.colorPrimary);
       // colorCodeForTextColor= ContextCompat.getColor(act,R.color.colorPrimary);

        binding.logoEmptyState.setOnTouchListener(onTouchListener());
        binding.logoCustom.setOnTouchListener(onTouchListener());

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (!preafManager.getAppTutorial().isEmpty()){
            binding.videoTutorial.setVisibility(View.VISIBLE);
        }

        binding.videoTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, AppIntroActivity.class);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        binding.fabroutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    selectedObject.setBrandId(preafManager.getActiveBrand().getId());
                    if (selectedBackendFrame != null) {
                        selectedObject.setFrame1Id(selectedBackendFrame.getFrame1Id());

                    }
                    selectedObject.setCustom(isUsingCustomFrame);


                    preafManager.AddToMyFavorites(selectedObject);

                if (manuallyEnablePermission(0)) {
                    if (binding.fabroutIcon.getVisibility() == View.VISIBLE) {
                        binding.fabroutIcon.setVisibility(View.GONE);
                        binding.addfabroutIcon.setVisibility(View.VISIBLE);
                    }

                    saveImageToGallery(false, true);
                }

                //downloadAndShareApi(ADDFAV,null);
            }
        });

        binding.addfabroutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (selectedBackendFrame != null) {
                        selectedObject.setFrame1Id(selectedBackendFrame.getFrame1Id());
                    }
                    selectedObject.setBrandId(preafManager.getActiveBrand().getId());
                    selectedObject.setCustom(isUsingCustomFrame);

                    preafManager.removeFromMyFavorites(selectedObject);
               // if (manuallyEnablePermission()) {
                    if (binding.addfabroutIcon.getVisibility() == View.VISIBLE) {
                        binding.addfabroutIcon.setVisibility(View.GONE);
                        binding.fabroutIcon.setVisibility(View.VISIBLE);
                    }

                    removeFromFavourite(REMOVEFAV);
               // }
            }
        });

        binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (manuallyEnablePermission(1)) {

                    if (!Utility.isUserPaid(preafManager.getActiveBrand())) {
                        //freee ------
                        if (selectedObject.isImageFree()) {
                            if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                                askForUpgradeToEnterpisePackage();
                                return;
                            }
                            getImageDownloadRights("Download");
                        } else {
                            askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
                        }
                    } else {
                        //paid
                     /*if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                            askForUpgradeToEnterpisePackage();
                            return;
                        }*/
                        getImageDownloadRights("Download");
                    }
                }
            }
        });

        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manuallyEnablePermission(2)) {
                    if (!Utility.isUserPaid(preafManager.getActiveBrand())) {
                        if (selectedObject.isImageFree()) {
                            if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                                askForUpgradeToEnterpisePackage();
                                return;
                            }
                            getImageDownloadRights("Share");
                        } else {
                            askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
                        }
                    } else {
                        getImageDownloadRights("Share");
                    }
                }
            }
        });

        if (preafManager.getActiveBrand().getLogo() != null && !preafManager.getActiveBrand().getLogo().isEmpty() ) {
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
        }
        else
        {
            binding.logoEmptyState.setVisibility(View.VISIBLE);
            binding.logoCustom.setVisibility(View.GONE);

            binding.logoCustom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectImageClick(view);
                }
            });


        }

        if (!getIntent().hasExtra("viewAll"))
            LoadDataToUI();
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

    public void isPermissionGranted(boolean permission) {
        if (!permission) {
            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", act.getPackageName(), null)));
        }
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

                        showTabIntro(binding.viewPager.getChildAt(0), "Category", "Choose your image as you want");

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

    public void CreateTabs(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Category")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Footer")));



        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Frames")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Background")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Text")));



        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewAllTopTabAdapter adapter = new ViewAllTopTabAdapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        if (getIntent().hasExtra("viewAll"))
            adapter.setViewAll(true);

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(6);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                editorFragment=tab.getPosition();

                if (needToIntro) {
                    if (tabIndex == 1) {
                        showTabIntro(binding.viewPager, "Footer", "if you want to custom frame then choose your own footer");
                    }
                    if (tabIndex == 2) {
                        showTabIntro(binding.viewPager, "Frames", "Apply custom frame");
                    }
                    if (tabIndex == 3) {
                        showTabIntro(binding.viewPager, "Background", "Choose your image as you want");
                    }
                    if (tabIndex == 4) {
                        showTabIntro(binding.viewPager, "Text", "Change your text and icon color as u want");
                        needToIntro=false;
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        if (!is_frame.equalsIgnoreCase("1")) {
            IntroCounter = 0;
            preafManager.setFrameIntro(false);
            //   startIntroForFrameOnly(binding.logoEmptyState, "Logo", "you can upload logo here");

        } else {

        }


        if (preafManager.getViewAllActivityIntro()) {
            needToIntro=true;

            if (binding.logoEmptyState.getVisibility()==View.VISIBLE)
                startIntro(binding.logoEmptyState, "Brand Logo", "Click on icon for choose your logo\n you can resize and move logo around anywhere in the image");
            else
                startIntro(binding.logoCustom, "Brand Logo", "Click your logo to move around anywhere in the image");

            preafManager.setViewAllActivityIntro(false);

        }else {
            //showTabIntro(binding.viewPager.getChildAt(0), "Category", "Choose your image as you want");
        }



    }

    //load firstImage
    public void loadFirstImage(){

        FooterModel model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SEVEN);
        model.setFree(true);
        model.setAddress(preafManager.getActiveBrand().getAddress());
        model.setEmailId(preafManager.getActiveBrand().getEmail());
        model.setContactNo(preafManager.getActiveBrand().getPhonenumber());
        model.setWebsite(preafManager.getActiveBrand().getWebsite());
        ((onFooterSelectListener) act).onFooterSelectEvent(FooterModel.LAYOUT_FRAME_SEVEN, model);
    }

    //For CustomFrame
    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    public void LoadDataToUI(){
        preafManager=new PreafManager(act);
        if (selectedObject != null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);
        } else {
           // binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }

        if (selectedFooterModel==null)
            loadFirstImage();
    }

    //For RefresGalary
    public void refreshgallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    //For CreatFileeDisc For Download Image.........................
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "BrandMania");
    }


    //For adepter
    public void setAdapter() {
        ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(menuModels, act);
        menuAddaptor.setLayoutType(FROM_VIEWALL);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        binding.viewRecoRecycler.setLayoutManager(mLayoutManager);
        binding.viewRecoRecycler.setHasFixedSize(true);
        binding.viewRecoRecycler.setAdapter(menuAddaptor);

     /*   if (getIntent().hasExtra("viewAll")) {
            binding.simpleProgressBar.setVisibility(View.VISIBLE);

            selectedObject = menuModels.get(0);
            LoadDataToUI();
        } else {
            Toast.makeText(act, "dfgdfgdfgd", Toast.LENGTH_SHORT).show();
            binding.simpleProgressBar.setVisibility(View.GONE);
        }*/

    }



    //For Image Select Interface
    @Override
    public void ImageCateonItemSelection(int position, ImageList listModel) {


         //   binding.simpleProgressBar.setVisibility(View.GONE);
            selectedObject = listModel;
            LoadDataToUI();
       // else {
           // binding.simpleProgressBar.setVisibility(View.VISIBLE);
        binding.simpleProgressBar.setVisibility(View.GONE);
        if (selectedFooterModel==null)
            loadFirstImage();

        forCheckFavorite();

    }

    // For Frame Load View Pager
    public void frameViewPager() {

    }

    public void AlertBoxForSaveFrame() {
        alertDialogBuilder = new AlertDialog.Builder(act);
        alertDialogBuilder.setTitle("Save Frame");
        alertDialogBuilder.setMessage("Do you want to save your template, you will not able to change once save.");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        /*binding.customFrameRelative.setVisibility(View.GONE);
                        binding.FrameImageDuplicate.setVisibility(View.GONE);
                        if (binding.customeContactEdit1.getText().toString().length()==0)
                        {
                            binding.customeContactEdit1.setVisibility(View.GONE);
                        }
                        if (binding.customFrameWebsite.getText().toString().length()==0)
                        {
                            binding.customFrameWebsite.setVisibility(View.GONE);
                        }
                        if (binding.customAddressEdit1.getText().toString().length()==0)
                        {
                            binding.customAddressEdit1.setVisibility(View.GONE);
                        }*/
                        //getCustomFrameInBitmap();

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

    //For GetFrame
    @Override public void alertListenerClick() {
        requestAgain();
    }

    private void requestAgain() {
        ActivityCompat.requestPermissions(act,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                CodeReUse.ASK_PERMISSSION);
    }



    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

    @Override
    public void onDialogDismissed(int dialogId) {
    }


    private void removeFromFavourite(final int removeFav) {
        Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.REMOVE_FAVOURIT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.REMOVE_FAVOURIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissLoadingTran();
                Utility.Log("REMOVE_FAVOURIT : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utility.dismissLoadingTran();
                        error.printStackTrace();

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
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (imageList != null) {
                    params.put("brand_id", preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());
                } else {
                    params.put("brand_id",  preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());

                }
                params.put("type", String.valueOf(removeFav));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
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

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            //   Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
        if (requestCode==CodeReUse.ASK_PERMISSSION) {
            if (isDownloadOrSharingOrFavPending != -1) {
                //for favourit
                if (isDownloadOrSharingOrFavPending==0){
                    isDownloadOrSharingOrFavPending=-1;
                    if (binding.fabroutIcon.getVisibility() == View.VISIBLE) {
                        binding.fabroutIcon.setVisibility(View.GONE);
                        binding.addfabroutIcon.setVisibility(View.VISIBLE);
                    }

                    saveImageToGallery(false, true);
                }
                //for download
                if (ContextCompat.checkSelfPermission(act,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (isDownloadOrSharingOrFavPending == 1) {
                        Toast.makeText(act, "fdggdgd", Toast.LENGTH_SHORT).show();
                        isDownloadOrSharingOrFavPending = -1;
                        if (!Utility.isUserPaid(preafManager.getActiveBrand())) {
                            //freee ------
                            if (selectedObject.isImageFree()) {
                                if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                                    askForUpgradeToEnterpisePackage();
                                    return;
                                }
                                getImageDownloadRights("Download");
                            } else {
                                askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
                            }
                        } else {
                            //paid
                     /*if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                            askForUpgradeToEnterpisePackage();
                            return;
                        }*/
                            getImageDownloadRights("Download");
                        }
                    }
                    //for share
                    if (isDownloadOrSharingOrFavPending == 2) {
                        isDownloadOrSharingOrFavPending = -1;
                        if (!Utility.isUserPaid(preafManager.getActiveBrand())) {
                            if (selectedObject.isImageFree()) {
                                if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                                    askForUpgradeToEnterpisePackage();
                                    return;
                                }
                                getImageDownloadRights("Share");
                            } else {
                                askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
                            }
                        } else {
                            getImageDownloadRights("Share");
                        }
                    }
                }
            }
        }
    }
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    private void startCropImageActivity(Uri imageUri) {
            CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .start(this);

    }
    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_BRAND : ", response);
                ArrayList<BrandListItem> brandListItems=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                    JSONArray dataJsonArray = ResponseHandler.getJSONArray(jsonObject, "data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // binding.swipeContainer.setRefreshing(false);
                        error.printStackTrace();



                    }
                }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }




    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Utility.Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    //version 3 ======================================

    // ask for payment
    public DialogUpgradeLayoutSecondBinding secondBinding;

    public void askForPayTheirPayment(String msg) {
        secondBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout_second, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(secondBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(secondBinding.getRoot());
        secondBinding.element3.setText(msg);
        secondBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile","1");

                act.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        secondBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

     //   secondBinding.element3.setText("You haven't selected any package yet. Please choose any package for download more images");
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    //fire intent for share
    public void triggerShareIntent(File new_file,Bitmap merged) {
      //  Uri uri = Uri.parse();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(act,merged));
        startActivity(Intent.createChooser(share, "Share Image"));
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,"IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }



    //show dialog for upgrading package for using all 6 frames
    public DialogUpgradeDownloadLimitExpireBinding expireBinding;
    private void downloadLimitExpireDialog(String msg) {
        expireBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_download_limit_expire, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(expireBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(expireBinding.getRoot());
        expireBinding.element3.setText(msg);
        expireBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile","1");

                act.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        expireBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

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


    //for logo drag and click event handle
    GestureDetector gestureDetector;

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
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

    //save image with frame either custome or from backend
    public void saveImageToGallery(boolean wantToShare,boolean isFavourite) {

        Drawable bitmapFrame;
        if (isUsingCustomFrame){
            bitmapFrame=new BitmapDrawable(getResources(), getCustomFrameInBitmap(isFavourite));
        }else{
            bitmapFrame=(BitmapDrawable) binding.backendFrame.getDrawable();
        }
        Drawable ImageDrawable = (BitmapDrawable) binding.recoImage.getDrawable();
        Bitmap merged = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(merged);
        bitmapFrame.setBounds(0, 0, 1000, 1000);
        ImageDrawable.setBounds(0, 0, 1000, 1000);
        ImageDrawable.draw(canvas);
        bitmapFrame.draw(canvas);

        if (!isFavourite) {
            FileOutputStream fileOutputStream = null;
            File file = getDisc();
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            refreshgallery(new_file);

            if (wantToShare) {
                if (isUsingCustomFrame) {
                   // addDynamicFooter(selectedFooterModel.getLayoutType(), true);
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(),selectedFooterModel);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);
                    binding.FrameImageDuplicate.setImageBitmap(null);
                } else {
                    Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
                }
                Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);
                triggerShareIntent(new_file,merged);
            } else {
                Toast.makeText(act, "Your image is downloaded", Toast.LENGTH_SHORT).show();
                if (isUsingCustomFrame) {
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(),selectedFooterModel);

                   // addDynamicFooter(selectedFooterModel.getLayoutType(), true);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);
                    binding.FrameImageDuplicate.setImageBitmap(null);
                } else {
                    Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
                }
            }

            downloadAndShareApi(DOWLOAD, merged);
        }else {
            if (isUsingCustomFrame) {

                ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(),selectedFooterModel);
                binding.FrameImageDuplicate.setVisibility(View.GONE);
                binding.FrameImageDuplicate.setImageBitmap(null);
            } else {
                Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
            }
            downloadAndShareApi(ADDFAV, merged);
        }

    }


    //generate custom frame from relative layout
    private Bitmap getCustomFrameInBitmap(boolean isFavourite) {

        Bitmap newFinal;
        Bitmap returnedBitmap = Bitmap.createBitmap(binding.elementCustomFrame.getWidth(), binding.elementCustomFrame.getHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);

        Drawable bgDrawable =binding.elementCustomFrame.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        }
        binding.elementCustomFrame.draw(canvas);

        binding.FrameImageDuplicate.setVisibility(View.VISIBLE);

        binding.FrameImageDuplicate.setImageBitmap(returnedBitmap);

        BitmapDrawable drawable = (BitmapDrawable) binding.FrameImageDuplicate.getDrawable();

        newFinal = drawable.getBitmap();

      /*  FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "Img" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = newFinal;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshgallery(new_file);*/

        binding.FrameImageDuplicate.setVisibility(View.VISIBLE);
        //newFinal bitmap
        return newFinal;

    }


    //backend frame load
    @Override
    public void onBackendFrameChoose(ImageList imageList, int position) {
        binding.backendFrame.setVisibility(View.VISIBLE);
        binding.elementCustomFrame.setVisibility(View.GONE);
        selectedBackendFrame=imageList;
        Glide.with(getApplicationContext()).load(imageList.getFrame1()).into(binding.backendFrame);
        isUsingCustomFrame = false;
        forCheckFavorite();
    }

    //fire on footer select listener
    @Override
    public void onFooterSelectEvent(int footerLayout, FooterModel footerModel) {
        isUsingCustomFrame = true;
        binding.backendFrame.setVisibility(View.GONE);
        binding.elementCustomFrame.setVisibility(View.VISIBLE);

        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(borderSize), colorCodeForBackground);

        selectedFooterModel = footerModel;
        addDynamicFooter(footerLayout,false);
        forCheckFavorite();

        changeBorderColorAsFrame();
        loadSameColorToBackgroundAndTextAgain();
        ((ITextSizeEvent) act).onfontSize(previousFontSize);
    }

    //check for added to fav or not
    public void forCheckFavorite(){
        preafManager=new PreafManager(act);
        AddFavorite= preafManager.getSavedFavorites();
        Log.e("ALLPREF",gson.toJson(AddFavorite));
        if (AddFavorite!=null) {
            boolean isImageFound=false;
            for (int i = 0; i < AddFavorite.size(); i++) {
                if (preafManager.getActiveBrand().getId().equalsIgnoreCase(AddFavorite.get(i).getBrandId())) {
                    if (isUsingCustomFrame) {
                        if (AddFavorite.get(i).isCustom()) {
                            Log.e("FFF", AddFavorite.get(i).getId() + " " + selectedObject.getId());
                            if (AddFavorite.get(i).getId().equals(selectedObject.getId())) {
                                binding.addfabroutIcon.setVisibility(View.VISIBLE);
                                binding.fabroutIcon.setVisibility(View.GONE);
                                isImageFound = true;
                                break;
                            } else {
                                binding.addfabroutIcon.setVisibility(View.GONE);
                                binding.fabroutIcon.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {
                        if (!AddFavorite.get(i).isCustom()) {
                            if (AddFavorite.get(i).getId().equals(selectedObject.getId()) && AddFavorite.get(i).getFrame1Id().equalsIgnoreCase(selectedBackendFrame.getFrame1Id())) {
                                binding.addfabroutIcon.setVisibility(View.VISIBLE);
                                binding.fabroutIcon.setVisibility(View.GONE);
                                isImageFound = true;
                                break;
                            } else {
                                binding.addfabroutIcon.setVisibility(View.GONE);
                                binding.fabroutIcon.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }
            if (!isImageFound) {
                binding.addfabroutIcon.setVisibility(View.GONE);
                binding.fabroutIcon.setVisibility(View.VISIBLE);
            }
        }
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
            loadFrameFirstData();
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_TWO) {
            twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            binding.elementFooter.addView(twoBinding.getRoot());
            loadFrameTwoData();
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_THREE) {
            threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());
            loadFrameThreeData();
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_FOUR) {
            fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_four, null, false);
            binding.elementFooter.addView(fourBinding.getRoot());
            loadFrameFourData();
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FIVE) {
            fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_five, null, false);
            binding.elementFooter.addView(fiveBinding.getRoot());
            loadFrameFiveData();
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SIX) {
            sixBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_six, null, false);
            binding.elementFooter.addView(sixBinding.getRoot());
            loadFrameSixData();
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_SEVEN) {
            sevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_seven, null, false);
            binding.elementFooter.addView(sevenBinding.getRoot());
            loadFrameSevenData();

        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_EIGHT) {
            eightBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_eight, null, false);
            binding.elementFooter.addView(eightBinding.getRoot());
            loadFrameEightData();

        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_NINE) {
            nineBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_nine, null, false);
            binding.elementFooter.addView(nineBinding.getRoot());
            loadFrameNineData();

        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_TEN) {
            tenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_ten, null, false);
            binding.elementFooter.addView(tenBinding.getRoot());
            loadFrameTenData();
        }
    }

    @Override
    public void onColorSelected(int dialogId, int colorCode) {

    }

    public void loadSameColorToBackgroundAndTextAgain() {

        if (colorCodeForTextColor!=0) {
            if (footerLayout == 1) {
                ChangeTextColorForFrameOne(colorCodeForTextColor);
            } else if (footerLayout == 2) {
                ChangeTextColorForFrameTwo(colorCodeForTextColor);
            } else if (footerLayout == 3) {
                ChangeTextColorForFrameThree(colorCodeForTextColor);
            } else if (footerLayout == 4) {
                ChangeTextColorForFrameFour(colorCodeForTextColor);
            } else if (footerLayout == 5) {
                ChangeTextColorForFrameFive(colorCodeForTextColor);
            } else if (footerLayout == 6) {
                ChangeTextColorForFrameSix(colorCodeForTextColor);
            }else if (footerLayout == 7) {
                ChangeTextColorForFrameSeven(colorCodeForTextColor);
            }else if (footerLayout == 8) {
                ChangeTextColorForFrameEight(colorCodeForTextColor);
            }else if (footerLayout == 9) {
                ChangeTextColorForFrameNine(colorCodeForTextColor);
            }else if (footerLayout == 10) {
                ChangeTextColorForFrameTen(colorCodeForTextColor);
            }

        }


        if (footerLayout == 1) {
            ChangeBackgroundColorForFrameOne(colorCodeForBackground);
        } else if (footerLayout == 2) {
            ChangeBackgroundColorForFrameTwo(colorCodeForBackground);
        } else if (footerLayout == 3) {

        } else if (footerLayout == 4) {
            ChangeBackgroundColorForFrameFour(colorCodeForBackground);
        } else if (footerLayout == 5) {
            ChangeBackgroundColorForFrameFive(colorCodeForBackground);
        } else if (footerLayout == 6) {
            ChangeBackgroundColorForFrameSix(colorCodeForBackground);
        }else if (footerLayout == 7) {
            ChangeBackgroundColorForFrameSeven(colorCodeForBackground);
        }else if (footerLayout == 8) {
            ChangeBackgroundColorForFrameEight(colorCodeForBackground);
        }else if (footerLayout == 9) {
            ChangeBackgroundColorForFrameNine(colorCodeForBackground);
        }else if (footerLayout == 10) {
            ChangeBackgroundColorForFrameTen(colorCodeForBackground);
        }

        changeBorderColorAsFrame();

        //bold
        if (footerLayout == 1) {
            Utility.setBold(oneBinding.gmailText, isLoadBold);
            Utility.setBold(oneBinding.contactText, isLoadBold);
            Utility.setBold(oneBinding.locationText, isLoadBold);

        } else if (footerLayout == 2) {
            Utility.setBold(twoBinding.gmailText, isLoadBold);
            Utility.setBold(twoBinding.contactText, isLoadBold);
            Utility.setBold(twoBinding.locationText, isLoadBold);
            Utility.setBold(twoBinding.websiteText, isLoadBold);

        } else if (footerLayout == 3) {
            Utility.setBold(threeBinding.gmailText, isLoadBold);
            Utility.setBold(threeBinding.contactText, isLoadBold);
            Utility.setBold(threeBinding.locationText, isLoadBold);
            Utility.setBold(threeBinding.websiteText, isLoadBold);


        } else if (footerLayout == 4) {

            Utility.setBold(fourBinding.gmailText, isLoadBold);
            Utility.setBold(fourBinding.contactText, isLoadBold);
            Utility.setBold(fourBinding.locationText, isLoadBold);
            Utility.setBold(fourBinding.websiteText, isLoadBold);
        } else if (footerLayout == 5) {
            Utility.setBold(fiveBinding.gmailText, isLoadBold);
            Utility.setBold(fiveBinding.phoneTxt, isLoadBold);
            Utility.setBold(fiveBinding.websiteText, isLoadBold);
        } else if (footerLayout == 6) {
            Utility.setBold(sixBinding.textElement1, isLoadBold);
            Utility.setBold(sixBinding.contactText, isLoadBold);
        }
        else if (footerLayout == 7) {


            Utility.setBold(sevenBinding.brandNameText, isLoadBold);
            Utility.setBold(sevenBinding.gmailText, isLoadBold);
            Utility.setBold(sevenBinding.contactText, isLoadBold);
        }

        else if (footerLayout==8) {
            Utility.setBold(eightBinding.brandNameText, isLoadBold);
            Utility.setBold(eightBinding.gmailText, isLoadBold);
            Utility.setBold(eightBinding.contactText, isLoadBold);
            Utility.setBold(eightBinding.locationText, isLoadBold);
        }

        else if (footerLayout==9) {
            Utility.setBold(nineBinding.brandNameText, isLoadBold);
            Utility.setBold(nineBinding.gmailText, isLoadBold);
            Utility.setBold(nineBinding.contactText, isLoadBold);
        }
        else if (footerLayout==10) {
            Utility.setBold(tenBinding.gmailText, isLoadBold);
            Utility.setBold(tenBinding.contactText, isLoadBold);
            Utility.setBold(tenBinding.locationText, isLoadBold);
        }



        //italic
        if (footerLayout == 1) {
            Utility.setItalicText(oneBinding.gmailText, isLoadItalic);
            Utility.setItalicText(oneBinding.contactText, isLoadItalic);
            Utility.setItalicText(oneBinding.locationText, isLoadItalic);

        } else if (footerLayout == 2) {

            Utility.setItalicText(twoBinding.gmailText, isLoadItalic);
            Utility.setItalicText(twoBinding.contactText, isLoadItalic);
            Utility.setItalicText(twoBinding.locationText, isLoadItalic);
            Utility.setItalicText(twoBinding.websiteText, isLoadItalic);

        } else if (footerLayout == 3) {

            Utility.setItalicText(threeBinding.gmailText, isLoadItalic);
            Utility.setItalicText(threeBinding.contactText, isLoadItalic);
            Utility.setItalicText(threeBinding.locationText, isLoadItalic);
            Utility.setItalicText(threeBinding.websiteText, isLoadItalic);

        } else if (footerLayout == 4) {
            Utility.setItalicText(fourBinding.gmailText, isLoadItalic);
            Utility.setItalicText(fourBinding.contactText, isLoadItalic);
            Utility.setItalicText(fourBinding.locationText, isLoadItalic);
            Utility.setItalicText(fourBinding.websiteText, isLoadItalic);
        } else if (footerLayout == 5) {
            Utility.setItalicText(fiveBinding.gmailText, isLoadItalic);
            Utility.setItalicText(fiveBinding.phoneTxt, isLoadItalic);
            Utility.setItalicText(fiveBinding.websiteText, isLoadItalic);
        } else if (footerLayout == 6) {
            Utility.setItalicText(sixBinding.textElement1, isLoadItalic);
            Utility.setItalicText(sixBinding.contactText, isLoadItalic);
        }
        else if (footerLayout == 7) {

            Utility.setItalicText(sevenBinding.brandNameText, isLoadItalic);
            Utility.setItalicText(sevenBinding.gmailText, isLoadItalic);
            Utility.setItalicText(sevenBinding.contactText, isLoadItalic);

        }

        else if (footerLayout==8) {
            Utility.setItalicText(eightBinding.brandNameText, isLoadItalic);
            Utility.setItalicText(eightBinding.gmailText, isLoadItalic);
            Utility.setItalicText(eightBinding.contactText, isLoadItalic);
            Utility.setItalicText(eightBinding.locationText, isLoadItalic);
        }

        else if (footerLayout==9) {
            Utility.setItalicText(nineBinding.brandNameText, isLoadItalic);
            Utility.setItalicText(nineBinding.gmailText, isLoadItalic);
            Utility.setItalicText(nineBinding.contactText, isLoadItalic);

        }
        else if (footerLayout==10) {

            Utility.setItalicText(tenBinding.gmailText, isLoadItalic);
            Utility.setItalicText(tenBinding.contactText, isLoadItalic);
            Utility.setItalicText(tenBinding.locationText, isLoadItalic);
        }


        // underline
        if (footerLayout==1) {
            Utility.setUnderlineText(oneBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(oneBinding.contactText, isLoadUnderLine);
            Utility.setUnderlineText(oneBinding.locationText, isLoadUnderLine);


        }else if (footerLayout==2) {
            Utility.setUnderlineText(twoBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(twoBinding.contactText, isLoadUnderLine);
            Utility.setUnderlineText(twoBinding.locationText, isLoadUnderLine);
            Utility.setUnderlineText(twoBinding.websiteText, isLoadUnderLine);

        }else if (footerLayout==3) {
            Utility.setUnderlineText(threeBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(threeBinding.contactText, isLoadUnderLine);
            Utility.setUnderlineText(threeBinding.locationText, isLoadUnderLine);
            Utility.setUnderlineText(threeBinding.websiteText, isLoadUnderLine);


        }else if (footerLayout==4) {

            Utility.setUnderlineText(fourBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(fourBinding.contactText, isLoadUnderLine);
            Utility.setUnderlineText(fourBinding.locationText, isLoadUnderLine);
            Utility.setUnderlineText(fourBinding.websiteText, isLoadUnderLine);
        }else if (footerLayout==5) {
            Utility.setUnderlineText(fiveBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(fiveBinding.phoneTxt, isLoadUnderLine);
            Utility.setUnderlineText(fiveBinding.websiteText, isLoadUnderLine);
        }else if (footerLayout==6) {
            Utility.setUnderlineText(sixBinding.textElement1, isLoadUnderLine);
            Utility.setUnderlineText(sixBinding.contactText, isLoadUnderLine);
        }

        else if (footerLayout==7) {
            Utility.setUnderlineText(sevenBinding.brandNameText, isLoadUnderLine);
            Utility.setUnderlineText(sevenBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(sevenBinding.contactText, isLoadUnderLine);
        }

        else if (footerLayout==8) {
            Utility.setUnderlineText(eightBinding.brandNameText, isLoadUnderLine);
            Utility.setUnderlineText(eightBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(eightBinding.contactText, isLoadUnderLine);
            Utility.setUnderlineText(eightBinding.locationText, isLoadUnderLine);
        }

        else if (footerLayout==9) {
            Utility.setUnderlineText(nineBinding.brandNameText, isLoadUnderLine);
            Utility.setUnderlineText(nineBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(nineBinding.contactText, isLoadUnderLine);

        } else if (footerLayout == 10) {

            Utility.setUnderlineText(tenBinding.gmailText, isLoadUnderLine);
            Utility.setUnderlineText(tenBinding.contactText, isLoadUnderLine);
            Utility.setUnderlineText(tenBinding.locationText, isLoadUnderLine);
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
    @Override
    public void onColorChanged(int colorCode) {
        colorCodeForTextColor = colorCode;
        if (editorFragment == 4) {

            if (footerLayout == 1) {
                ChangeTextColorForFrameOne(colorCode);
            } else if (footerLayout == 2) {
                ChangeTextColorForFrameTwo(colorCode);
            } else if (footerLayout == 3) {
                ChangeTextColorForFrameThree(colorCode);
            }else if (footerLayout==4){
                ChangeTextColorForFrameFour(colorCode);
            }else if (footerLayout==5){
                ChangeTextColorForFrameFive(colorCode);
            }else if (footerLayout==6){
                ChangeTextColorForFrameSix(colorCode);
            }
            else if (footerLayout==7){
                ChangeTextColorForFrameSeven(colorCode);
            }
            else if (footerLayout==8){
                ChangeTextColorForFrameEight(colorCode);
            }
            else if (footerLayout==9){
                ChangeTextColorForFrameNine(colorCode);
            }
            else if (footerLayout==10){
                ChangeTextColorForFrameTen(colorCode);
            }
        }

    }

    //on border size change
    int borderSize;
    @Override
    public void onBorderSizeChange(int size) {
        borderSize=size;
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(size), colorCodeForBackground);
    }

    //for background color change
    @Override public void onChooseColor(int colorCode) {
        colorCodeForBackground = colorCode;
        if (editorFragment==3){
            if (footerLayout==1){
                ChangeBackgroundColorForFrameOne(colorCode);
            }else if (footerLayout==2){
                ChangeBackgroundColorForFrameTwo(colorCode);
            }else if (footerLayout==3){

            }else if (footerLayout==4){
                ChangeBackgroundColorForFrameFour(colorCode);
            } else if (footerLayout == 5) {
                ChangeBackgroundColorForFrameFive(colorCode);
            } else if (footerLayout == 6) {
                ChangeBackgroundColorForFrameSix(colorCode);
            }
            else if (footerLayout==7){
                ChangeBackgroundColorForFrameSeven(colorCode);
            }
            else if (footerLayout==8){
                ChangeBackgroundColorForFrameEight(colorCode);
            }
            else if (footerLayout==9){
                ChangeBackgroundColorForFrameNine(colorCode);
            }
            else if (footerLayout==10){
                ChangeBackgroundColorForFrameTen(colorCode);
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

    @Override
    public void onColorItemChange(int colorCode) {
    }



    //for font change
    @Override
    public void onFontChangeListenert(String Font) {
        loadDefaultFont = Font;
        if (footerLayout == 1) {
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
        }else if (footerLayout==3){
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            threeBinding.gmailText.setTypeface(custom_font);
            threeBinding.contactText.setTypeface(custom_font);
            threeBinding.locationText.setTypeface(custom_font);
            threeBinding.websiteText.setTypeface(custom_font);
        }else if (footerLayout==4){
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            fourBinding.gmailText.setTypeface(custom_font);
            fourBinding.contactText.setTypeface(custom_font);
            fourBinding.locationText.setTypeface(custom_font);
            fourBinding.websiteText.setTypeface(custom_font);
        }else if (footerLayout==5){
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            fiveBinding.gmailText.setTypeface(custom_font);
            fiveBinding.phoneTxt.setTypeface(custom_font);
            fiveBinding.websiteText.setTypeface(custom_font);
        }else if (footerLayout==6) {
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            sixBinding.textElement1.setTypeface(custom_font);
            sixBinding.contactText.setTypeface(custom_font);
        }
        else if (footerLayout==7) {
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            sevenBinding.brandNameText.setTypeface(custom_font);
            sevenBinding.gmailText.setTypeface(custom_font);
            sevenBinding.contactText.setTypeface(custom_font);


        }
        else if (footerLayout==8) {
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            eightBinding.brandNameText.setTypeface(custom_font);
            eightBinding.gmailText.setTypeface(custom_font);
            eightBinding.contactText.setTypeface(custom_font);
            eightBinding.locationText.setTypeface(custom_font);


        }
        else if (footerLayout==9) {
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            nineBinding.brandNameText.setTypeface(custom_font);
            nineBinding.gmailText.setTypeface(custom_font);
            nineBinding.contactText.setTypeface(custom_font);


        }
        else if (footerLayout==10) {
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            tenBinding.locationText.setTypeface(custom_font);
            tenBinding.gmailText.setTypeface(custom_font);
            tenBinding.contactText.setTypeface(custom_font);


        }
    }

    //for underline
    @Override public void onUnderLineItalic(boolean Left) {
        isLoadUnderLine=Left;
        if (Left) {
            if (footerLayout==1) {
                Utility.setUnderlineText(oneBinding.gmailText, true);
                Utility.setUnderlineText(oneBinding.contactText, true);
                Utility.setUnderlineText(oneBinding.locationText, true);


            }else if (footerLayout==2) {
                Utility.setUnderlineText(twoBinding.gmailText, true);
                Utility.setUnderlineText(twoBinding.contactText, true);
                Utility.setUnderlineText(twoBinding.locationText, true);
                Utility.setUnderlineText(twoBinding.websiteText, true);

            }else if (footerLayout==3) {
                Utility.setUnderlineText(threeBinding.gmailText, true);
                Utility.setUnderlineText(threeBinding.contactText, true);
                Utility.setUnderlineText(threeBinding.locationText, true);
                Utility.setUnderlineText(threeBinding.websiteText, true);


            }else if (footerLayout==4) {

                Utility.setUnderlineText(fourBinding.gmailText, true);
                Utility.setUnderlineText(fourBinding.contactText, true);
                Utility.setUnderlineText(fourBinding.locationText, true);
                Utility.setUnderlineText(fourBinding.websiteText, true);
            }else if (footerLayout==5) {
                Utility.setUnderlineText(fiveBinding.gmailText, true);
                Utility.setUnderlineText(fiveBinding.phoneTxt, true);
                Utility.setUnderlineText(fiveBinding.websiteText, true);
            }else if (footerLayout==6) {
                Utility.setUnderlineText(sixBinding.textElement1, true);
                Utility.setUnderlineText(sixBinding.contactText, true);
            }

            else if (footerLayout==7) {
                Utility.setUnderlineText(sevenBinding.brandNameText, true);
                Utility.setUnderlineText(sevenBinding.gmailText, true);
                Utility.setUnderlineText(sevenBinding.contactText, true);
            }

            else if (footerLayout==8) {
                Utility.setUnderlineText(eightBinding.brandNameText, true);
                Utility.setUnderlineText(eightBinding.gmailText, true);
                Utility.setUnderlineText(eightBinding.contactText, true);
                Utility.setUnderlineText(eightBinding.locationText, true);
            }

            else if (footerLayout==9) {
                Utility.setUnderlineText(nineBinding.brandNameText, true);
                Utility.setUnderlineText(nineBinding.gmailText, true);
                Utility.setUnderlineText(nineBinding.contactText, true);

            }
            else if (footerLayout==10) {

                Utility.setUnderlineText(tenBinding.gmailText, true);
                Utility.setUnderlineText(tenBinding.contactText, true);
                Utility.setUnderlineText(tenBinding.locationText, true);
            }

        }
        else {
            if (footerLayout == 1) {
                Utility.setUnderlineText(oneBinding.gmailText, false);
                Utility.setUnderlineText(oneBinding.contactText, false);
                Utility.setUnderlineText(oneBinding.locationText, false);
            } else if (footerLayout == 2) {
                Utility.setUnderlineText(twoBinding.gmailText, false);
                Utility.setUnderlineText(twoBinding.contactText, false);
                Utility.setUnderlineText(twoBinding.locationText, false);
                Utility.setUnderlineText(twoBinding.websiteText, false);
            } else if (footerLayout == 3) {
                Utility.setUnderlineText(threeBinding.gmailText, false);
                Utility.setUnderlineText(threeBinding.contactText, false);
                Utility.setUnderlineText(threeBinding.locationText, false);
                Utility.setUnderlineText(threeBinding.websiteText, false);
            } else if (footerLayout == 4) {

                Utility.setUnderlineText(fourBinding.gmailText, false);
                Utility.setUnderlineText(fourBinding.contactText, false);
                Utility.setUnderlineText(fourBinding.locationText, false);
                Utility.setUnderlineText(fourBinding.websiteText, false);
            } else if (footerLayout == 5) {
                Utility.setUnderlineText(fiveBinding.gmailText, false);
                Utility.setUnderlineText(fiveBinding.phoneTxt, false);
                Utility.setUnderlineText(fiveBinding.websiteText, false);
            } else if (footerLayout == 6) {
                Utility.setUnderlineText(sixBinding.contactText, false);
                Utility.setUnderlineText(sixBinding.textElement1, false);

            }
            else if (footerLayout == 7) {

                Utility.setUnderlineText(sevenBinding.brandNameText, false);
                Utility.setUnderlineText(sevenBinding.gmailText, false);
                Utility.setUnderlineText(sevenBinding.contactText, false);


            }

            else if (footerLayout==8) {
                Utility.setUnderlineText(eightBinding.brandNameText, false);
                Utility.setUnderlineText(eightBinding.gmailText, false);
                Utility.setUnderlineText(eightBinding.contactText, false);
                Utility.setUnderlineText(eightBinding.locationText, false);
            }

            else if (footerLayout==9) {
                Utility.setUnderlineText(nineBinding.brandNameText, false);
                Utility.setUnderlineText(nineBinding.gmailText, false);
                Utility.setUnderlineText(nineBinding.contactText, false);

            }
            else if (footerLayout==10) {

                Utility.setUnderlineText(tenBinding.gmailText, false);
                Utility.setUnderlineText(tenBinding.contactText, false);
                Utility.setUnderlineText(tenBinding.locationText, false);
            }

        }
        loadSameColorToBackgroundAndTextAgain();
        changeBorderColorAsFrame();


        Log.e("NewColor", colorCodeForBackground + " " + colorCodeForTextColor);
    }

    //for font size
    @Override public void onfontSize(int textsize) {

        previousFontSize=textsize;
        if (previousFontSize!=-1) {
            if (footerLayout == 1) {
                oneBinding.gmailText.setTextSize(textsize);
                oneBinding.contactText.setTextSize(textsize);
                oneBinding.locationText.setTextSize(textsize);
            } else if (footerLayout == 2) {

                twoBinding.gmailText.setTextSize(textsize);
                twoBinding.contactText.setTextSize(textsize);
                twoBinding.locationText.setTextSize(textsize);
                twoBinding.websiteText.setTextSize(textsize);
            } else if (footerLayout == 3) {

                threeBinding.gmailText.setTextSize(textsize);
                threeBinding.contactText.setTextSize(textsize);
                threeBinding.locationText.setTextSize(textsize);
                threeBinding.websiteText.setTextSize(textsize);
            } else if (footerLayout == 4) {

                fourBinding.gmailText.setTextSize(textsize);
                fourBinding.contactText.setTextSize(textsize);
                fourBinding.locationText.setTextSize(textsize);
                fourBinding.websiteText.setTextSize(textsize);
            } else if (footerLayout == 5) {

                fiveBinding.gmailText.setTextSize(textsize);
                fiveBinding.phoneTxt.setTextSize(textsize);
                fiveBinding.websiteText.setTextSize(textsize);
            } else if (footerLayout == 6) {

                sixBinding.textElement1.setTextSize(textsize);
                sixBinding.contactText.setTextSize(textsize);

            } else if (footerLayout == 7) {
                sevenBinding.brandNameText.setTextSize(textsize);
                sevenBinding.gmailText.setTextSize(textsize);
                sevenBinding.contactText.setTextSize(textsize);
            } else if (footerLayout == 8) {
                eightBinding.brandNameText.setTextSize(textsize);
                eightBinding.gmailText.setTextSize(textsize);
                eightBinding.contactText.setTextSize(textsize);
                eightBinding.locationText.setTextSize(textsize);

            } else if (footerLayout == 9) {
                nineBinding.brandNameText.setTextSize(textsize);
                nineBinding.gmailText.setTextSize(textsize);
                nineBinding.contactText.setTextSize(textsize);


            } else if (footerLayout == 10) {

                tenBinding.gmailText.setTextSize(textsize);
                tenBinding.contactText.setTextSize(textsize);
                tenBinding.locationText.setTextSize(textsize);
            }
        }
    }

    //for bold text
    @Override public void onBoldTextChange(boolean Bold) {
        isLoadBold=Bold;
        if (Bold) {
            if (footerLayout == 1) {
                Utility.setBold(oneBinding.gmailText, true);
                Utility.setBold(oneBinding.contactText, true);
                Utility.setBold(oneBinding.locationText, true);


            } else if (footerLayout == 2) {
                Utility.setBold(twoBinding.gmailText, true);
                Utility.setBold(twoBinding.contactText, true);
                Utility.setBold(twoBinding.locationText, true);
                Utility.setBold(twoBinding.websiteText, true);

            } else if (footerLayout == 3) {
                Utility.setBold(threeBinding.gmailText, true);
                Utility.setBold(threeBinding.contactText, true);
                Utility.setBold(threeBinding.locationText, true);
                Utility.setBold(threeBinding.websiteText, true);


            } else if (footerLayout == 4) {

                Utility.setBold(fourBinding.gmailText, true);
                Utility.setBold(fourBinding.contactText, true);
                Utility.setBold(fourBinding.locationText, true);
                Utility.setBold(fourBinding.websiteText, true);
            } else if (footerLayout == 5) {
                Utility.setBold(fiveBinding.gmailText, true);
                Utility.setBold(fiveBinding.phoneTxt, true);
                Utility.setBold(fiveBinding.websiteText, true);
            } else if (footerLayout == 6) {
                Utility.setBold(sixBinding.textElement1, true);
                Utility.setBold(sixBinding.contactText, true);
            }
            else if (footerLayout == 7) {


                Utility.setBold(sevenBinding.brandNameText, true);
                Utility.setBold(sevenBinding.gmailText, true);
                Utility.setBold(sevenBinding.contactText, true);
            }


            else if (footerLayout==8) {
                Utility.setBold(eightBinding.brandNameText, true);
                Utility.setBold(eightBinding.gmailText, true);
                Utility.setBold(eightBinding.contactText, true);
                Utility.setBold(eightBinding.locationText, true);
            }

            else if (footerLayout==9) {
                Utility.setBold(nineBinding.brandNameText, true);
                Utility.setBold(nineBinding.gmailText, true);
                Utility.setBold(nineBinding.contactText, true);

            }
            else if (footerLayout==10) {

                Utility.setBold(tenBinding.gmailText, true);
                Utility.setBold(tenBinding.contactText, true);
                Utility.setBold(tenBinding.locationText, true);
            }



        }else {
            if (footerLayout == 1) {
                Utility.setBold(oneBinding.gmailText, false);
                Utility.setBold(oneBinding.contactText, false);
                Utility.setBold(oneBinding.locationText, false);
            } else if (footerLayout == 2) {
                Utility.setBold(twoBinding.gmailText, false);
                Utility.setBold(twoBinding.contactText, false);
                Utility.setBold(twoBinding.locationText, false);
                Utility.setBold(twoBinding.websiteText, false);
            } else if (footerLayout == 3) {
                Utility.setBold(threeBinding.gmailText, false);
                Utility.setBold(threeBinding.contactText, false);
                Utility.setBold(threeBinding.locationText, false);
                Utility.setBold(threeBinding.websiteText, false);
            } else if (footerLayout == 4) {

                Utility.setBold(fourBinding.gmailText, false);
                Utility.setBold(fourBinding.contactText, false);
                Utility.setBold(fourBinding.locationText, false);
                Utility.setBold(fourBinding.websiteText, false);
            } else if (footerLayout == 5) {
                Utility.setBold(fiveBinding.gmailText, false);
                Utility.setBold(fiveBinding.phoneTxt, false);
                Utility.setBold(fiveBinding.websiteText, false);
            } else if (footerLayout == 6) {
                Utility.setBold(sixBinding.contactText, false);
                Utility.setBold(sixBinding.textElement1, false);

            }
            else if (footerLayout == 7) {


                Utility.setBold(sevenBinding.brandNameText, false);
                Utility.setBold(sevenBinding.gmailText, false);
                Utility.setBold(sevenBinding.contactText, false);
            }

            else if (footerLayout==8) {
                Utility.setBold(eightBinding.brandNameText, false);
                Utility.setBold(eightBinding.gmailText, false);
                Utility.setBold(eightBinding.contactText, false);
                Utility.setBold(eightBinding.locationText, false);
            }

            else if (footerLayout==9) {
                Utility.setBold(nineBinding.brandNameText, false);
                Utility.setBold(nineBinding.gmailText, false);
                Utility.setBold(nineBinding.contactText, false);

            }
            else if (footerLayout==10) {

                Utility.setBold(tenBinding.gmailText, false);
                Utility.setBold(tenBinding.contactText, false);
                Utility.setBold(tenBinding.locationText, false);
            }


        }

    }

    //for italic
    @Override public void onItalicTextChange(boolean Italic) {
        isLoadItalic=Italic;
        if (Italic) {
            if (footerLayout == 1) {
                Utility.setItalicText(oneBinding.gmailText, true);
                Utility.setItalicText(oneBinding.contactText, true);
                Utility.setItalicText(oneBinding.locationText, true);

            } else if (footerLayout == 2) {

                Utility.setItalicText(twoBinding.gmailText, true);
                Utility.setItalicText(twoBinding.contactText, true);
                Utility.setItalicText(twoBinding.locationText, true);
                Utility.setItalicText(twoBinding.websiteText, true);

            } else if (footerLayout == 3) {

                Utility.setItalicText(threeBinding.gmailText, true);
                Utility.setItalicText(threeBinding.contactText, true);
                Utility.setItalicText(threeBinding.locationText, true);
                Utility.setItalicText(threeBinding.websiteText, true);

            } else if (footerLayout == 4) {
                Utility.setItalicText(fourBinding.gmailText, true);
                Utility.setItalicText(fourBinding.contactText, true);
                Utility.setItalicText(fourBinding.locationText, true);
                Utility.setItalicText(fourBinding.websiteText, true);
            } else if (footerLayout == 5) {
                Utility.setItalicText(fiveBinding.gmailText, true);
                Utility.setItalicText(fiveBinding.phoneTxt, true);
                Utility.setItalicText(fiveBinding.websiteText, true);
            } else if (footerLayout == 6) {
                Utility.setItalicText(sixBinding.textElement1, true);
                Utility.setItalicText(sixBinding.contactText, true);
            }
            else if (footerLayout == 7) {

                Utility.setItalicText(sevenBinding.brandNameText, true);
                Utility.setItalicText(sevenBinding.gmailText, true);
                Utility.setItalicText(sevenBinding.contactText, true);

            }

            else if (footerLayout==8) {
                Utility.setItalicText(eightBinding.brandNameText, true);
                Utility.setItalicText(eightBinding.gmailText, true);
                Utility.setItalicText(eightBinding.contactText, true);
                Utility.setItalicText(eightBinding.locationText, true);
            }

            else if (footerLayout==9) {
                Utility.setItalicText(nineBinding.brandNameText, true);
                Utility.setItalicText(nineBinding.gmailText, true);
                Utility.setItalicText(nineBinding.contactText, true);

            }
            else if (footerLayout==10) {

                Utility.setItalicText(tenBinding.gmailText, true);
                Utility.setItalicText(tenBinding.contactText, true);
                Utility.setItalicText(tenBinding.locationText, true);
            }



        }
        else {
            if (footerLayout == 1) {
                Utility.setItalicText(oneBinding.gmailText, false);
                Utility.setItalicText(oneBinding.contactText, false);
                Utility.setItalicText(oneBinding.locationText, false);
            } else if (footerLayout == 2) {
                Utility.setItalicText(twoBinding.gmailText, false);
                Utility.setItalicText(twoBinding.contactText, false);
                Utility.setItalicText(twoBinding.locationText, false);
                Utility.setItalicText(twoBinding.websiteText, false);
            } else if (footerLayout == 3) {
                Utility.setItalicText(threeBinding.gmailText, false);
                Utility.setItalicText(threeBinding.contactText, false);
                Utility.setItalicText(threeBinding.locationText, false);
                Utility.setItalicText(threeBinding.websiteText, false);
            } else if (footerLayout == 4) {

                Utility.setItalicText(fourBinding.gmailText, false);
                Utility.setItalicText(fourBinding.contactText, false);
                Utility.setItalicText(fourBinding.locationText, false);
                Utility.setItalicText(fourBinding.websiteText, false);
            } else if (footerLayout == 5) {
                Utility.setItalicText(fiveBinding.gmailText, false);
                Utility.setItalicText(fiveBinding.phoneTxt, false);
                Utility.setItalicText(fiveBinding.websiteText, false);
            } else if (footerLayout == 6) {
                Utility.setItalicText(sixBinding.contactText, false);
                Utility.setItalicText(sixBinding.textElement1, false);

            }
            else if (footerLayout == 7) {

                Utility.setItalicText(sevenBinding.brandNameText, false);
                Utility.setItalicText(sevenBinding.gmailText, false);
                Utility.setItalicText(sevenBinding.contactText, false);

            }
            else if (footerLayout==8) {
                Utility.setItalicText(eightBinding.brandNameText, false);
                Utility.setItalicText(eightBinding.gmailText, false);
                Utility.setItalicText(eightBinding.contactText, false);
                Utility.setItalicText(eightBinding.locationText, false);
            }

            else if (footerLayout==9) {
                Utility.setItalicText(nineBinding.brandNameText, false);
                Utility.setItalicText(nineBinding.gmailText, false);
                Utility.setItalicText(nineBinding.contactText, false);

            }
            else if (footerLayout==10) {

                Utility.setItalicText(tenBinding.gmailText, false);
                Utility.setItalicText(tenBinding.contactText, false);
                Utility.setItalicText(tenBinding.locationText, false);
            }


        }
    }


    //change color for background and text of footer

    public void ChangeBackgroundColorForFrameOne(int colorCode) {
        oneBinding.topView.setBackgroundColor(colorCode);
        oneBinding.topView2.setBackgroundColor(colorCode);
        oneBinding.addressLayoutElement2.setBackgroundColor(colorCode);
    }

    public void ChangeTextColorForFrameOne(int colodCode) {
        oneBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        oneBinding.gmailText.setTextColor(colodCode);
        oneBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        oneBinding.contactText.setTextColor(colodCode);
    }

    public void ChangeBackgroundColorForFrameTwo(int colorCode) {
        twoBinding.firstView.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        twoBinding.secondView.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public void ChangeTextColorForFrameTwo(int colodCode) {
        twoBinding.gmailImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.gmailText.setTextColor(colodCode);
        twoBinding.contactImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.contactText.setTextColor(colodCode);
        twoBinding.websiteImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.websiteText.setTextColor(colodCode);
        twoBinding.locationImage.setBackgroundTintList(ColorStateList.valueOf(colodCode));
        twoBinding.locationText.setTextColor(colodCode);

    }

    public void ChangeTextColorForFrameThree(int colodCode){
        threeBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.gmailText.setTextColor(colodCode);
        threeBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.contactText.setTextColor(colodCode);
        threeBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.websiteText.setTextColor(colodCode);
        threeBinding.loacationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.locationText.setTextColor(colodCode);

    }

    public void ChangeBackgroundColorForFrameFour(int colorCode) {
        fourBinding.topView2.setBackgroundColor(colorCode);
    }

    public void ChangeTextColorForFrameFour(int colodCode) {
        fourBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.gmailText.setTextColor(colodCode);
        fourBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.contactText.setTextColor(colodCode);
        fourBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.websiteText.setTextColor(colodCode);
        fourBinding.locationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.locationText.setTextColor(colodCode);

    }

    public void ChangeBackgroundColorForFrameFive(int colorCode) {
        fiveBinding.element1.setImageTintList(ColorStateList.valueOf(colorCode));
        fiveBinding.element3.setImageTintList(ColorStateList.valueOf(colorCode));
        fiveBinding.viewElement2.setBackgroundColor(colorCode);

    }

    public void ChangeTextColorForFrameFive(int colodCode) {
        fiveBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.gmailText.setTextColor(colodCode);
        fiveBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.phoneTxt.setTextColor(colodCode);
        fiveBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.websiteText.setTextColor(colodCode);
    }

    public void ChangeBackgroundColorForFrameSix(int colorCode) {
        sixBinding.containerElement.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        sixBinding.viewElement2.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public void ChangeTextColorForFrameSix(int colodCode) {
        sixBinding.imgElement1.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.imgElement2.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.imgElement3.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.textElement1.setTextColor(colodCode);
        sixBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.contactText.setTextColor(colodCode);
    }

    public void ChangeBackgroundColorForFrameSeven(int colorCode) {

        sevenBinding.element.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public void ChangeTextColorForFrameSeven(int colodCode) {
        sevenBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.imgElement1.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.imgElement2.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.imgElement3.setImageTintList(ColorStateList.valueOf(colodCode));
        sevenBinding.gmailText.setTextColor(colodCode);
        sevenBinding.contactText.setTextColor(colodCode);
        sevenBinding.brandNameText.setTextColor(colodCode);

    }

    public void ChangeBackgroundColorForFrameEight(int colorCode) {

        eightBinding.topView2.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        eightBinding.viewone.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        eightBinding.topView2.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public void ChangeTextColorForFrameEight(int colodCode) {

        eightBinding.locationText.setTextColor(colodCode);
        eightBinding.gmailText.setTextColor(colodCode);
        eightBinding.contactText.setTextColor(colodCode);
        eightBinding.brandNameText.setTextColor(colodCode);
        eightBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        eightBinding.loacationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        eightBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
    }

    public void ChangeBackgroundColorForFrameNine(int colorCode) {

        nineBinding.element.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        nineBinding.element0.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public void ChangeTextColorForFrameNine(int colodCode) {
        nineBinding.imgElement1.setImageTintList(ColorStateList.valueOf(colodCode));
        nineBinding.imgElement2.setImageTintList(ColorStateList.valueOf(colodCode));
        nineBinding.imgElement3.setImageTintList(ColorStateList.valueOf(colodCode));
        nineBinding.gmailText.setTextColor(colodCode);
        nineBinding.contactText.setTextColor(colodCode);
        nineBinding.brandNameText.setTextColor(colodCode);

    }

    public void ChangeBackgroundColorForFrameTen(int colorCode) {

            tenBinding.gmailImage.setBackgroundTintList(ColorStateList.valueOf(colorCode));
            tenBinding.callImage.setBackgroundTintList(ColorStateList.valueOf(colorCode));
            tenBinding.locationImage.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }

    public void ChangeTextColorForFrameTen(int colodCode) {

        tenBinding.gmailText.setTextColor(colodCode);
        tenBinding.contactText.setTextColor(colodCode);
        tenBinding.locationText.setTextColor(colodCode);

    }



    //Footer Data Load

    public void loadFrameFirstData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            oneBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            oneBinding.gmailLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            oneBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            oneBinding.contactLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getAddress().isEmpty()) {
            oneBinding.locationText.setText(activeBrand.getAddress());
        } else {
            oneBinding.addressLayoutElement.setVisibility(View.GONE);
        }
    }

    public void loadFrameTwoData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            twoBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            twoBinding.gmailLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            twoBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            twoBinding.contactLayout.setVisibility(View.GONE);
        }
        if (activeBrand.getPhonenumber().isEmpty() && activeBrand.getEmail().isEmpty()) {
            twoBinding.firstView.setVisibility(View.GONE);
        }


        if (!activeBrand.getAddress().isEmpty()) {
            twoBinding.locationText.setText(activeBrand.getAddress());
        } else {
            twoBinding.locationLayout.setVisibility(View.GONE);
        }
        if (!activeBrand.getWebsite().isEmpty()) {
            twoBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            twoBinding.websiteLayout.setVisibility(View.GONE);
        }

        if (activeBrand.getAddress().isEmpty() && activeBrand.getWebsite().isEmpty()) {
            twoBinding.secondView.setVisibility(View.GONE);
        }
    }

    public void loadFrameThreeData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            threeBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            threeBinding.gmailLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            threeBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            threeBinding.contactLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getAddress().isEmpty()) {
            threeBinding.locationText.setText(activeBrand.getAddress());
        } else {
            threeBinding.loactionLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getWebsite().isEmpty()) {
            threeBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            threeBinding.websiteEdtLayout.setVisibility(View.GONE);
        }

    }

    public void loadFrameFourData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            fourBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            fourBinding.gmailLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            fourBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            fourBinding.contactLayout.setVisibility(View.GONE);
        }


        if (!activeBrand.getAddress().isEmpty()) {
            fourBinding.locationText.setText(activeBrand.getAddress());
        } else {
            fourBinding.locationLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getWebsite().isEmpty()) {
            fourBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            fourBinding.websiteLayout.setVisibility(View.GONE);
        }

    }

    public void loadFrameFiveData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            fiveBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            fiveBinding.elementEmail.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            fiveBinding.phoneTxt.setText(activeBrand.getPhonenumber());
        } else {
            fiveBinding.elementMobile.setVisibility(View.GONE);
        }

        if (!activeBrand.getWebsite().isEmpty()) {
            fiveBinding.websiteText.setText(activeBrand.getWebsite());
        } else {
            fiveBinding.element0.setVisibility(View.GONE);
        }
    }

    public void loadFrameSixData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();

        if (!activeBrand.getPhonenumber().isEmpty()) {
            sixBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            sixBinding.contactLayout.setVisibility(View.GONE);
        }

    }

    public void loadFrameSevenData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            sevenBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            sevenBinding.gmailLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            sevenBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            sevenBinding.contactLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getName().isEmpty()) {
            sevenBinding.brandNameText.setText(activeBrand.getName());
        } else {
            sevenBinding.brandNameText.setVisibility(View.GONE);
        }
    }

    public void loadFrameEightData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            eightBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            eightBinding.gmailLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            eightBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            eightBinding.contactLayout.setVisibility(View.GONE);
        }

        if (!activeBrand.getName().isEmpty()) {
            eightBinding.brandNameText.setText(activeBrand.getName());
        } else {
            eightBinding.brandNameText.setVisibility(View.GONE);
        }

        if (!activeBrand.getAddress().isEmpty()) {
            eightBinding.locationText.setText(activeBrand.getAddress());
        } else {
            eightBinding.addressLayoutElement.setVisibility(View.GONE);
        }
    }

    public void loadFrameNineData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            nineBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            nineBinding.gmailText.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            nineBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            nineBinding.contactText.setVisibility(View.GONE);
        }

        if (!activeBrand.getName().isEmpty()) {
            nineBinding.brandNameText.setText(activeBrand.getName());
        } else {
            nineBinding.brandNameText.setVisibility(View.GONE);
        }


    }

    public void loadFrameTenData() {
        BrandListItem activeBrand = preafManager.getActiveBrand();
        if (!activeBrand.getEmail().isEmpty()) {
            tenBinding.gmailText.setText(activeBrand.getEmail());
        } else {
            tenBinding.gmailText.setVisibility(View.GONE);
            tenBinding.gmailImage.setVisibility(View.GONE);
        }

        if (!activeBrand.getPhonenumber().isEmpty()) {
            tenBinding.contactText.setText(activeBrand.getPhonenumber());
        } else {
            tenBinding.contactText.setVisibility(View.GONE);
            tenBinding.callImage.setVisibility(View.GONE);
        }
        if (!activeBrand.getAddress().isEmpty()) {
            tenBinding.locationText.setText(activeBrand.getAddress());
        } else {
            tenBinding.locationText.setVisibility(View.GONE);
            tenBinding.locationImage.setVisibility(View.GONE);
        }



    }






    //to handle click and drag listener
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    //zoom Logo
    @Override public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        binding.logoCustom.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            binding.logoCustom.setScaleX(mScaleFactor);
            binding.logoCustom.setScaleY(mScaleFactor);
            return true;
        }
    }














    //API CALLS---------------------


    //getFrames
    private void getFrame() {
        Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissLoadingTran();
                Utility.Log("GET_FRAME : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    brandListItems = ResponseHandler.HandleGetFrame(jsonObject);
                    JSONObject datajsonobjecttt = ResponseHandler.getJSONObject(jsonObject, "data");
                    is_frame = datajsonobjecttt.getString("is_frame");
                    if (is_frame.equals("1")) {
                        is_payment_pending = datajsonobjecttt.getString("is_payment_pending");
                        packagee = datajsonobjecttt.getString("package");
                    }


                    CreateTabs();

                } catch (JSONException e) {
                    e.printStackTrace();
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
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id", preafManager.getActiveBrand().getId());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    //For Download,Share and Fav
    private void downloadAndShareApi(final int download,Bitmap customImage) {

        Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.DOWNLOAD_SHARE);
        File img1File = null;
        if (customImage != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", customImage);
        }
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.DOWNLOAD_SHARE)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer" + preafManager.getUserToken())
                .setPriority(Priority.HIGH);



        if (isUsingCustomFrame){
            request.addMultipartParameter("brand_id",  preafManager.getActiveBrand().getId());
            request.addMultipartParameter("image_id", selectedObject.getImageid());
            request.addMultipartParameter("is_custom", "1");
            request.addMultipartParameter("footer_id", String.valueOf(selectedFooterModel.getLayoutType()));
            if (img1File != null) {
                request.addMultipartFile("image", img1File);
                Log.e("br_logo", String.valueOf(img1File));
            }
        } else {
            request.addMultipartParameter("brand_id", preafManager.getActiveBrand().getId());
            request.addMultipartParameter("image_id", selectedObject.getImageid());
            request.addMultipartParameter("frame_id", selectedBackendFrame.getFrame1Id());
            request.addMultipartParameter("is_custom", "0");
        }
        request.addMultipartParameter("type", String.valueOf(download));
        Log.e("Request",gson.toJson(request));
        request.build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                // do anything with progress
            }
        })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.dismissLoadingTran();
                        Utility.Log("DOWNLOAD_SHARE : ", response);
                        if (updateLogo && selectedLogo != null)
                            uploadLogoForBrand(selectedLogo);

                        if (download == DOWLOAD) {
                            //this is coding for can we change logo or not
                            String usedImageCountStr = preafManager.getActiveBrand().getNo_of_used_image();
                            if (usedImageCountStr.isEmpty())
                                usedImageCountStr = "0";

                            int usedCounter = Integer.parseInt(usedImageCountStr) + 1;
                            BrandListItem brandListItem = preafManager.getActiveBrand();
                            brandListItem.setNo_of_used_image(String.valueOf(usedCounter));
                            preafManager.setActiveBrand(brandListItem);
                            preafManager = new PreafManager(act);
                            Log.e("UUUU", preafManager.getActiveBrand().getNo_of_used_image() + "s");
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading = false;
                        Utility.dismissLoadingTran();
                        if (error.getErrorCode() != 0) {
                            Log.e("onError errorCode : ", String.valueOf(error.getErrorCode()));
                            Log.e("onError errorBody : ", error.getErrorBody());
                            Log.e("onError errorDetail : ", error.getErrorDetail());
                        } else {
                            Log.e("onError errorDetail : ", error.getErrorDetail());
                        }
                    }
                });
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
                                   downloadLimitExpireDialog("Your download limit is expired for your current package. To get more images please upgrade your package");
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
                            downloadLimitExpireDialog("You have already used one image for today, As you are free user you can download or share only one image in a day for 7 days. To get more images please upgrade your package");
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

    //update logo to brand
    private void uploadLogoForBrand(Bitmap img) {
        Utility.showLoadingTran(act);

        File img1File = null;
        if (img != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", img);
        }
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.EDIT_BRAND)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer" + preafManager.getUserToken())
                .addMultipartParameter("brand_id", preafManager.getActiveBrand().getId())
                .setPriority(Priority.HIGH);

        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
            Log.e("br_logo", String.valueOf(img1File));
        }

        request.build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                // do anything with progress
            }
        })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        isLoading = false;
                        Utility.dismissLoadingTran();
                        Utility.Log("Logo Uploaded", response);
                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading = false;
                        Utility.dismissLoadingTran();
                        if (error.getErrorCode() != 0) {
                            Log.e("onError errorCode : ", String.valueOf(error.getErrorCode()));
                            Log.e("onError errorBody : ", error.getErrorBody());
                            Log.e("onError errorDetail : ", error.getErrorDetail());
                        } else {
                            Log.e("onError errorDetail : ", error.getErrorDetail());
                        }
                    }
                });

    }

}
