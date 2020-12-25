package com.app.brandmania.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.ImageCateItemeInterFace;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Adapter.ViewAllTopTabAdapter;
import com.app.brandmania.Adapter.ViewPagerAdapterFrame;
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
import com.app.brandmania.databinding.DialogUpgradeLayoutBinding;
import com.app.brandmania.databinding.LayoutForLoadFiveBinding;
import com.app.brandmania.databinding.LayoutForLoadFourBinding;
import com.app.brandmania.databinding.LayoutForLoadOneBinding;
import com.app.brandmania.databinding.LayoutForLoadSixBinding;
import com.app.brandmania.databinding.LayoutForLoadThreeBinding;
import com.app.brandmania.databinding.LayoutForLoadTwoBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    int editorFragment;
    private int xDelta;
    private int yDelta;
    private ViewGroup mainLayout;
    private boolean isUserFree = true;
    private boolean canDownload = true;
    private int FrameCountForDownload = 2;

    private boolean isUsingCustomFrame = true;

    //Version 3
    private ImageList selectedBackendFrame=null;

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
        Website=preafManager.getActiveBrand().getWebsite();
        imageList = gson.fromJson(getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
        binding.titleName.setText(imageList.getName());
        getImageCtegory();


        mainLayout = (RelativeLayout) findViewById(R.id.elementCustomFrame);


        binding.logoEmptyState.setOnTouchListener(onTouchListener());
        binding.logoCustom.setOnTouchListener(onTouchListener());
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());


        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.fabroutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedObject.setFrameId(selectedModelFromView.getFrameId());
                preafManager.AddToMyFavorites(selectedObject);
                if (binding.fabroutIcon.getVisibility()==View.VISIBLE)
                {
                    binding.fabroutIcon.setVisibility(View.GONE);
                    binding.addfabroutIcon.setVisibility(View.VISIBLE);
                }
                downloadAndShareApi(ADDFAV);
            }
        });
        binding.addfabroutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedObject.setFrameId(selectedModelFromView.getFrameId());
                preafManager.removeFromMyFavorites(selectedObject);
                if (binding.addfabroutIcon.getVisibility()==View.VISIBLE)
                {
                    binding.addfabroutIcon.setVisibility(View.GONE);
                    binding.fabroutIcon.setVisibility(View.VISIBLE);
                }
                removeFromFavourite(REMOVEFAV);
            }
        });

        binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageDownloadRights();
            }
        });
        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestAgain();

                saveImageToGallery(true);

                downloadAndShareApi(DOWLOAD);
            }
        });

       // binding.logoEmptyState.setVisibility(View.VISIBLE);


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

                        saveImageToGallery(false);

                        downloadAndShareApi(DOWLOAD);

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

                            if (IntroCounter == 1) {
                                startIntro(binding.shareIcon, "Share", "Share Your Image Directly");
                            }
                            if (IntroCounter == 2) {
                                startIntro(binding.fabroutIcon, "Save", "Save To Your Brand");
                            }
                            if (IntroCounter == 3) {
                                startIntro(binding.viewPager.getChildAt(0), "Catogery", "List of images");
                            }
/*
                        if (!is_frame.equalsIgnoreCase("1") && preafManager.getFrameIntro()) {
                            preafManager.setFrameIntro(false);
                            if (IntroCounter == 4) {
                                startIntro(binding.logoCard, "Logo", "Change Your Logo");
                            }
                            if (IntroCounter == 5) {
                                startIntro(binding.customAddressEdit1, "AddressText", "Change Your Address Text color");
                            }
                            if (IntroCounter == 6) {
                                startIntro(binding.customeContactEdit1, "ContactText", "Change Your Contact Text color");
                            }
                            if (IntroCounter == 7) {
                                startIntro(binding.bottomBarView1, "Address Background", "Change Your Address Background color");
                            }
                            if (IntroCounter == 8) {
                                startIntro(binding.bottomBarView2, "Contac Background", "Change Your Contact Background color");
                            }
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


    public void CreateTabs(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Category")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Background")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Text")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Footer")));


        if (!is_frame.equalsIgnoreCase("1")) {
                 IntroCounter=0;
                preafManager.setFrameIntro(false);
                startIntroForFrameOnly(binding.logoEmptyState, "Logo", "you can upload logo here");
            addDynamicFooter(1);

        }else{
            if (preafManager.getViewAllActivityIntro()) {
                startIntro(binding.downloadIcon,"Download","Download Image From here");
                preafManager.setViewAllActivityIntro(false);
            }
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Frames")));
        }


        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewAllTopTabAdapter adapter = new ViewAllTopTabAdapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(6);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                editorFragment=tab.getPosition();

              //  handler(editorFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
       /* binding.customAddressEdit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewPager.setCurrentItem(3);
            }
        });
        binding.customeContactEdit1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b==true)
                {
                    selectedForEdit=binding.customeContactEdit1;
                    binding.viewPager.setCurrentItem(2);
                    editorFragment=2;
                }
            }
        });
        binding.customAddressEdit1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b==true)
                {
                    selectedForEdit=binding.customAddressEdit1;
                    binding.viewPager.setCurrentItem(2);
                    editorFragment=2;
                }
            }
        });
        binding.customFrameWebsite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b==true)
                {
                    selectedForEdit=binding.customFrameWebsite;
                    binding.viewPager.setCurrentItem(2);
                    editorFragment=2;
                }
            }
        });
        binding.bottomBarView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FramePrimaryOrSecondary=0;
                selectedForBackgroundChange=binding.bottomBarView1;
                binding.customAddressEdit1.clearFocus();
                binding.customeContactEdit1.clearFocus();
                binding.customFrameWebsite.clearFocus();
                binding.viewPager.setCurrentItem(1);
            }
        });
        binding.bottomBarView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FramePrimaryOrSecondary=1;
                selectedForBackgroundChange=binding.bottomBarView2;
                binding.customAddressEdit1.clearFocus();
                binding.customeContactEdit1.clearFocus();
                binding.customFrameWebsite.clearFocus();
                binding.viewPager.setCurrentItem(1);
            }
        });
        binding.bottomBarView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FramePrimaryOrSecondary=0;
                selectedForBackgroundChange=binding.bottomBarView1;
                binding.customAddressEdit1.clearFocus();
                binding.customeContactEdit1.clearFocus();
                binding.customFrameWebsite.clearFocus();
                binding.viewPager.setCurrentItem(1);
            }
        });
*/

    }
    //For CustomFrame
    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }
    public void LoadDataToUI(){
        preafManager=new PreafManager(act);
        if (selectedObject != null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
         //   Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.backgrounImageDuplicate);
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);

            AddFavorite= preafManager.getSavedFavorites();

            if (AddFavorite!=null) {
                for (int i = 0; i < AddFavorite.size(); i++) {
                    if (AddFavorite.get(i).getId().equals(selectedObject.getId()) && AddFavorite.get(i).getFrameId().equalsIgnoreCase(selectedModelFromView.getFrameId())) {
                        binding.addfabroutIcon.setVisibility(View.VISIBLE);
                        binding.fabroutIcon.setVisibility(View.GONE);
                        break;
                    } else {
                        binding.addfabroutIcon.setVisibility(View.GONE);
                        binding.fabroutIcon.setVisibility(View.VISIBLE);

                    }
                }
            }

        }
        else
        {
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }
    }
    public void  reloadSaved(){
        AddFavorite= preafManager.getSavedFavorites();

        if (AddFavorite!=null) {
            for (int i = 0; i < AddFavorite.size(); i++) {
                if (!AddFavorite.get(i).getId().equals(selectedObject.getId()) || !AddFavorite.get(i).getFrameId().equalsIgnoreCase(selectedModelFromView.getFrameId())) {
                    binding.addfabroutIcon.setVisibility(View.GONE);
                    binding.fabroutIcon.setVisibility(View.VISIBLE);
                } else {
                    binding.addfabroutIcon.setVisibility(View.VISIBLE);
                    binding.fabroutIcon.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }

    BitmapDrawable FrameDrawbable;



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

        if (getIntent().hasExtra("viewAll")) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            selectedObject = menuModels.get(0);
            LoadDataToUI();
        } else {
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }
    }


    //For GetImageCategory
    private void getImageCtegory() {
        Utility.Log("API : ", APIs.GET_IMAGEBUID_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGEBUID_CATEGORY + "/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("GET_IMAGE_CATEGORY : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    menuModels = ResponseHandler.HandleGetImageByIdCategory(jsonObject);

                    if (menuModels != null && menuModels.size() != 0) {
                        setAdapter();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.viewRecoRecycler.setVisibility(View.VISIBLE);
                        binding.emptyStateLayout.setVisibility(View.GONE);
                    }
                    if (menuModels == null || menuModels.size() == 0) {
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.viewRecoRecycler.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                if (imageList != null)
                    params.put("image_category_id", imageList.getId());
                else
                    params.put("image_category_id", selectedObject.getId());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }



    //For Image Select Interface
    @Override
    public void ImageCateonItemSelection(int position, ImageList listModel) {
        if (selectedObject != null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            selectedObject = listModel;
            LoadDataToUI();
        } else {
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }
    }

    // For Frame Load View Pager
    public void frameViewPager() {
        /*viewPager = (ViewPager) findViewById(R.id.recoframe);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        viewPagerItems = brandListItems;
        if (viewPagerItems != null && viewPagerItems.size() != 0) {
            Gson gson = new Gson();
            ViewPagerAdapterFrame viewPagerAdapter = new ViewPagerAdapterFrame(viewPagerItems, this);
            viewPager.setAdapter(viewPagerAdapter);
            dotscount = viewPagerAdapter.getCount();
            int h = viewPager.getCurrentItem();
            if (dotscount > 0) {
                dots = new ImageView[dotscount];
                for (int i = 0; i < dotscount; i++) {
                    dots[i] = new ImageView(this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                    }
                    try {
                        selectedModelFromView = (FrameItem) viewPagerItems.get(position).clone();

                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    reloadSaved();
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    reloadSaved();
                }
            });
            try {
                selectedModelFromView = (FrameItem) viewPagerItems.get(viewPager.getCurrentItem()).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
*/
        LoadDataToUI();

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

    private void getFrame() {
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_FRAME : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    brandListItems = ResponseHandler.HandleGetFrame(jsonObject);
                    JSONObject datajsonobjecttt =ResponseHandler.getJSONObject(jsonObject, "data");
                    is_frame= datajsonobjecttt.getString("is_frame");
                    if (is_frame.equals("1")) {
                        //binding.customFrameRelative.setVisibility(View.GONE);
                        // Toast.makeText(act,brandListItems.size()+"",Toast.LENGTH_LONG).show();
                        frameViewPager();
                        is_payment_pending= datajsonobjecttt.getString("is_payment_pending");
                        packagee=datajsonobjecttt.getString("package");
                        if (packagee.equals("")) {
                         /*   binding.shareIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPackage(act,ResponseHandler.getString(datajsonobjecttt,"package_message"));
                                }
                            });
                            binding.fabroutIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPackage(act,ResponseHandler.getString(datajsonobjecttt,"package_message"));
                                }
                            });*/
                     /*       binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPackage(act,ResponseHandler.getString(datajsonobjecttt,"package_message"));

                                }
                            });*/


                        }
                        else if (is_payment_pending.equals("1"))
                        {

                          /*  binding.shareIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPayment(act,ResponseHandler.getString(datajsonobjecttt,"payment_message"));
                                }
                            });
                            binding.fabroutIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPayment(act,ResponseHandler.getString(datajsonobjecttt,"payment_message"));
                                }
                            });*/
                         /*   binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPayment(act,ResponseHandler.getString(datajsonobjecttt,"payment_message"));
                                }
                            });*/
                        }
                    }
                    else
                    {
                        LoadDataToUI();
                        //fetchAutomaticCustomeFrame();
                   //     binding.customFrameRelative.setVisibility(View.VISIBLE);
                     //   binding.recoframe.setVisibility(View.GONE);
                        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertBoxForSaveFrame();                            }
                        });
                        binding.fabroutIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertBoxForSaveFrame();                            }
                        });
                       /* binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertBoxForSaveFrame();
                            }
                        });*/
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

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

    @Override
    public void onDialogDismissed(int dialogId) {
    }




    /*private class DownloadImageTask extends AsyncTask<String, Void, BitmapDrawable> {
        String url;

        public DownloadImageTask(String url) {
            this.url = url;
        }
        protected BitmapDrawable doInBackground(String... urls) {

            Bitmap mIcon11 = null;
            try {
                Log.e("ErrorImage", url);
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("ErrorImage", e.getMessage());
                e.printStackTrace();
            }
            return new BitmapDrawable(getResources(), mIcon11);
        }
        protected void onPostExecute(BitmapDrawable result) {
            FrameDrawbable=result;

            Utility.dismissLoadingTran();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showLoadingTran(act);
        }
    }
    class ShareImageTask extends AsyncTask<String, Void, BitmapDrawable> {
        String url;
        public ShareImageTask(String url) {
            this.url = url;
        }
        protected BitmapDrawable doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                Log.e("ErrorImage", url);
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("ErrorImage", e.getMessage());
                e.printStackTrace();
            }
            return new BitmapDrawable(getResources(), mIcon11);
        }
        protected void onPostExecute(BitmapDrawable result) {
            //bmImage.setImageBitmap(result);
            FrameDrawbable=result;
            startsShare();
            isLoading = false;
            Utility.dismissProgress();

        }

        @Override
        protected void onPreExecute() {
            if (isLoading)
                return;
            isLoading = true;
            Utility.showProgress(act);
            super.onPreExecute();
        }
    }*/

    private void removeFromFavourite(final int removeFav) {

        Utility.Log("API : ", APIs.REMOVE_FAVOURIT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.REMOVE_FAVOURIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
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
                //  Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //  Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
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
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
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


    //fire intent for share
    public void triggerShareIntent(File new_file) {
        Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(new_file.getPath()),null,null));
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }


    //show dialog for upgrading package for using all 6 frames
    public DialogUpgradeLayoutBinding upgradeLayoutBinding;
    private void triggerUpgradePackage() {
        upgradeLayoutBinding=DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(upgradeLayoutBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(upgradeLayoutBinding.getRoot());

        upgradeLayoutBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.setCancelable(false);
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
                    onSelectImageClick(view);
                    return true;
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

                            Toast.makeText(act,
                                    "I'm here!", Toast.LENGTH_SHORT)
                                    .show();
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

                    mainLayout.invalidate();
                    return true;
                }
            }
        };
    }


    //save image with frame either custome or from backend
    public void saveImageToGallery(boolean wantToShare) {

        Drawable bitmapFrame;
        if (isUsingCustomFrame){
            bitmapFrame=new BitmapDrawable(getResources(), getCustomFrameInBitmap());
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

        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "Img" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = merged;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshgallery(new_file);

        if (wantToShare){
            triggerShareIntent(new_file);
        }else {
            Toast.makeText(act, "Your image is downloaded", Toast.LENGTH_SHORT).show();
            if (isUsingCustomFrame){
                addDynamicFooter(0);
            }else {
                Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
            }
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);

        }


    }

    //generate custom frame from relative layout
    private Bitmap getCustomFrameInBitmap() {

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

        FileOutputStream fileOutputStream = null;
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
        refreshgallery(new_file);
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
        isUsingCustomFrame=false;
    }

    //fire on footer select listener
    @Override public void onFooterSelectEvent(int layoutType) {
        isUsingCustomFrame=true;
        binding.backendFrame.setVisibility(View.GONE);
        binding.elementCustomFrame.setVisibility(View.VISIBLE);
        addDynamicFooter(layoutType);
    }

    //for adding footer dynamically
    int footerLayout=1;
    private LayoutForLoadOneBinding oneBinding;
    private LayoutForLoadTwoBinding twoBinding;
    private LayoutForLoadThreeBinding threeBinding;
    private LayoutForLoadFourBinding fourBinding;
    private LayoutForLoadFiveBinding fiveBinding;
    private LayoutForLoadSixBinding sixBinding;
    private void addDynamicFooter(int layoutType) {
        binding.elementFooter.removeAllViews();
        footerLayout=layoutType;
        if (layoutType== FooterModel.LAYOUT_FRAME_ONE) {
            oneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_one, null, false);
            binding.elementFooter.addView(oneBinding.getRoot());
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_TWO) {
            twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            binding.elementFooter.addView(twoBinding.getRoot());
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_THREE) {
            threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_FOUR) {
            fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_four, null, false);
            binding.elementFooter.addView(fourBinding.getRoot());
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_FIVE) {
            fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_five, null, false);
            binding.elementFooter.addView(fiveBinding.getRoot());
        }
        else if (layoutType== FooterModel.LAYOUT_FRAME_SIX) {
            sixBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_six, null, false);
            binding.elementFooter.addView(sixBinding.getRoot());
        }
    }

    @Override public void onColorItemChange(int colorCode) {
    }
    @Override public void onColorSelected(int dialogId, int colorCode) {

    }

    //for Text Color change
    @Override public void onColorChanged(int colorCode) {
        if (editorFragment==2) {
            if (footerLayout==1){
                ChangeTextColorForFrameOne(colorCode);
            }else if (footerLayout==2){
                ChangeTextColorForFrameTwo(colorCode);
            }else if (footerLayout==3){
                ChangeTextColorForFrameThree(colorCode);
            }else if (footerLayout==4){
                ChangeTextColorForFrameFour(colorCode);
            }else if (footerLayout==5){
                ChangeTextColorForFrameFive(colorCode);
            }else if (footerLayout==6){
                ChangeTextColorForFrameSix(colorCode);
            }
        }

    }

    //for background color change
    @Override public void onChooseColor(int colorCode) {
        if (editorFragment==1){
            if (footerLayout==1){
                ChangeBackgroundColorForFrameOne(colorCode);
            }else if (footerLayout==2){
                ChangeBackgroundColorForFrameTwo(colorCode);
            }else if (footerLayout==3){

            }else if (footerLayout==4){
                ChangeBackgroundColorForFrameFour(colorCode);
            }else if (footerLayout==5){
                ChangeBackgroundColorForFrameFive(colorCode);
            }else if (footerLayout==6){
                ChangeBackgroundColorForFrameSix(colorCode);
            }
        }

    }

    //for font change
    @Override public void onFontChangeListenert(String Font) {
            if (footerLayout==1){
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                oneBinding.gmailText.setTypeface(custom_font);
                oneBinding.contactText.setTypeface(custom_font);
                 oneBinding.locationText.setTypeface(custom_font);
            }else if (footerLayout==2){
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
                fiveBinding.contactText.setTypeface(custom_font);
                fiveBinding.websiteText.setTypeface(custom_font);
            }else if (footerLayout==6) {
                Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
                sixBinding.textElement1.setTypeface(custom_font);

            }
    }

    //for underline
    @Override public void onUnderLineItalic(boolean Left) {
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
                Utility.setUnderlineText(fiveBinding.contactText, true);
                Utility.setUnderlineText(fiveBinding.websiteText, true);
            }else if (footerLayout==6) {
                Utility.setUnderlineText(sixBinding.textElement1, true);

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
                Utility.setUnderlineText(fiveBinding.contactText, false);
                Utility.setUnderlineText(fiveBinding.websiteText, false);
            } else if (footerLayout == 6) {

                Utility.setUnderlineText(sixBinding.textElement1, false);

            }

        }
    }

    //for font size
    @Override public void onfontSize(int textsize) {
        if (footerLayout==1){
            oneBinding.gmailText.setTextSize(textsize);
            oneBinding.contactText.setTextSize(textsize);
            oneBinding.locationText.setTextSize(textsize);
        }else if (footerLayout==2){

            twoBinding.gmailText.setTextSize(textsize);
            twoBinding.contactText.setTextSize(textsize);
            twoBinding.locationText.setTextSize(textsize);
            twoBinding.websiteText.setTextSize(textsize);
        }else if (footerLayout==3){

            threeBinding.gmailText.setTextSize(textsize);
            threeBinding.contactText.setTextSize(textsize);
            threeBinding.locationText.setTextSize(textsize);
            threeBinding.websiteText.setTextSize(textsize);
        }else if (footerLayout==4){

            fourBinding.gmailText.setTextSize(textsize);
            fourBinding.contactText.setTextSize(textsize);
            fourBinding.locationText.setTextSize(textsize);
            fourBinding.websiteText.setTextSize(textsize);
        }else if (footerLayout==5){

            fiveBinding.gmailText.setTextSize(textsize);
            fiveBinding.contactText.setTextSize(textsize);
            fiveBinding.websiteText.setTextSize(textsize);
        }else if (footerLayout==6) {

            sixBinding.textElement1.setTextSize(textsize);

        }

    }

    //for bold text
    @Override public void onBoldTextChange(boolean Bold) {
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
                Utility.setBold(fiveBinding.contactText, true);
                Utility.setBold(fiveBinding.websiteText, true);
            } else if (footerLayout == 6) {
                Utility.setBold(sixBinding.textElement1, true);

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
                Utility.setBold(fiveBinding.contactText, false);
                Utility.setBold(fiveBinding.websiteText, false);
            } else if (footerLayout == 6) {

                Utility.setBold(sixBinding.textElement1, false);

            }

        }

    }

    //for italic
    @Override public void onItalicTextChange(boolean Italic) {
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
                Utility.setItalicText(fiveBinding.contactText, true);
                Utility.setItalicText(fiveBinding.websiteText, true);
            } else if (footerLayout == 6) {
                Utility.setItalicText(sixBinding.textElement1, true);
            }

        } else {
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
                Utility.setItalicText(fiveBinding.contactText, false);
                Utility.setItalicText(fiveBinding.websiteText, false);
            } else if (footerLayout == 6) {

                Utility.setItalicText(sixBinding.textElement1, false);

            }


        }
    }




    public void ChangeBackgroundColorForFrameOne(int colorCode){
         oneBinding.topView.setBackgroundColor(colorCode);
         oneBinding.topView2.setBackgroundColor(colorCode);
         oneBinding.locationBackgroundLayout.setBackgroundColor(colorCode);
    }
    public void ChangeTextColorForFrameOne(int colodCode){
        oneBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        oneBinding.gmailText.setTextColor(colodCode);
        oneBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        oneBinding.contactText.setTextColor(colodCode);

    }
    public void ChangeBackgroundColorForFrameTwo(int colorCode){
        twoBinding.firstView.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        twoBinding.secondView.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }
    public void ChangeTextColorForFrameTwo(int colodCode){
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
        threeBinding.locationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        threeBinding.locationText.setTextColor(colodCode);

    }
    public void ChangeBackgroundColorForFrameFour(int colorCode){
        fourBinding.topView2.setBackgroundColor(colorCode); }
    public void ChangeTextColorForFrameFour(int colodCode){
        fourBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.gmailText.setTextColor(colodCode);
        fourBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.contactText.setTextColor(colodCode);
        fourBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.websiteText.setTextColor(colodCode);
        fourBinding.locationImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fourBinding.locationText.setTextColor(colodCode);

    }
    public void ChangeBackgroundColorForFrameFive(int colorCode){
        fiveBinding.element1.setImageTintList(ColorStateList.valueOf(colorCode));
        fiveBinding.element3.setImageTintList(ColorStateList.valueOf(colorCode));

    }
    public void ChangeTextColorForFrameFive(int colodCode){
        fiveBinding.gmailImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.gmailText.setTextColor(colodCode);
        fiveBinding.contactImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.contactText.setTextColor(colodCode);
        fiveBinding.websiteImage.setImageTintList(ColorStateList.valueOf(colodCode));
        fiveBinding.websiteText.setTextColor(colodCode);
    }
    public void ChangeBackgroundColorForFrameSix(int colorCode){
        sixBinding.containerElement.setBackgroundTintList(ColorStateList.valueOf(colorCode));
        sixBinding.viewElement2.setBackgroundTintList(ColorStateList.valueOf(colorCode));

    }
    public void ChangeTextColorForFrameSix(int colodCode){
        sixBinding.imgElement1.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.imgElement2.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.imgElement3.setImageTintList(ColorStateList.valueOf(colodCode));
        sixBinding.textElement1.setTextColor(colodCode);


    }



    //to handle click and drag listener
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }





    //API CALLS

    //For Download,Share and Fav
    private void downloadAndShareApi(final int download) {
        Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.DOWNLOAD_SHARE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.DOWNLOAD_SHARE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissLoadingTran();
                Utility.Log("DOWNLOAD_SHARE : ", response);
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
                if (isUsingCustomFrame){
                    params.put("brand_id",  preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());
                } else {
                    params.put("brand_id", preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());
                    params.put("frame_id", selectedBackendFrame.getFrame1Id());
                }

                /*if (!preafManager.getActiveBrand().getIs_frame().equals("0")) {
                    params.put("brand_id", preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());
                    params.put("frame_id", selectedModelFromView.getFrameId());

                } else {
                    params.put("brand_id",  preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());
                }*/

                params.put("type", String.valueOf(download));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    private void getImageDownloadRights() {
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
                        if (ResponseHandler.getBool(dataJson.getJSONObject(0), "status")){
                            canDownload=true;
                            askForDownloadImage();
                        }else{
                            canDownload=false;
                            Toast.makeText(act, "You can't download image bcoz your limit get expire for one day", Toast.LENGTH_SHORT).show();
                            //pop up show for limit ended
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
}
