package com.app.brandmania.Activity.details;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
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
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.about_us.AppIntroActivity;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Adapter.ViewAllTopTabAdapter;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.FooterHelper;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.DataBase.DBManager;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Interface.IBackendFrameSelect;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.ImageCateItemeInterFace;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Model.LayoutModelClass;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DialogUpgradeLayoutPackegeExpiredBindingImpl;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.IFontChangeEvent;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityViewAllImageBinding;
import com.app.brandmania.databinding.DialogDiscardImageBinding;
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
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class ImageCategoryDetailActivity extends BaseActivity implements ImageCateItemeInterFace, alertListenerCallback, ITextColorChangeEvent, IFontChangeEvent, ITextBoldEvent, IItaliTextEvent, ColorPickerDialogListener, IColorChange, ColorPickerView.OnColorChangedListener, ITextSizeEvent, onFooterSelectListener, IBackendFrameSelect {
    Activity act;
    ViewPager viewPager;
    private boolean isLoading = false;
    ArrayList<ImageList> AddFavorite = new ArrayList<>();
    private ActivityViewAllImageBinding binding;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    ArrayList<ImageList> menuModels = new ArrayList<>();
    private ViewGroup mainLayout1;
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    public static final int DOWLOAD = 1;
    public static final int ADDFAV = 3;
    private static final int REQUEST_CALL = 1;
    public static final int REMOVEFAV = 3;
    private String is_frame = "";
    public DBManager dbManager;
    private String is_payment_pending = "";
    private String packagee = "";
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
    private ImageList selectedBackendFrame = null;
    private FooterModel selectedFooterModel;
    private boolean updateLogo = false;
    private Bitmap selectedLogo;
    private int colorCodeForBackground;
    private int colorCodeForTextColor = 0;
    private boolean isLoadBold = false;
    private boolean isLoadItalic = false;
    private boolean isLoadUnderLine = false;
    private String loadDefaultFont = "";
    private int previousFontSize = -1;
    int isDownloadOrSharingOrFavPending = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        //triggerUpgradePackage();
        //valve
        act.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_all_image);
        preafManager = new PreafManager(this);
        binding.titleName.setSelected(true);
        gson = new Gson();
        selectedObject = gson.fromJson(getIntent().getStringExtra("selectedimage"), ImageList.class);
        getFrame();
        getBrandList();

        Website = preafManager.getActiveBrand().getWebsite();

        if (getIntent().hasExtra("notification")) {
            binding.titleName.setText(getIntent().getStringExtra("catName"));
        } else {
            imageList = gson.fromJson(getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
            binding.titleName.setText(selectedObject.getName());
        }

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        mainLayout = (RelativeLayout) findViewById(R.id.elementCustomFrame);
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(0), colorCodeForBackground);

        updateLogo = preafManager.getActiveBrand().getLogo().isEmpty();

        colorCodeForBackground = ContextCompat.getColor(act, R.color.colorPrimary);
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
        binding.videoTutorial.setVisibility(View.VISIBLE);
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
                        if (!Utility.isPackageExpired(act)) {
                            getImageDownloadRights("Share");
                        } else {
                            askForUpgradeToEnterpisePackaged();
                        }
                      //  getImageDownloadRights("Share");
                    }
                }
            }
        });
        if (preafManager.getActiveBrand().getLogo() != null && !preafManager.getActiveBrand().getLogo().isEmpty()) {
            binding.logoEmptyState.setVisibility(View.GONE);
            binding.logoCustom.setVisibility(View.VISIBLE);
            binding.logoCustom.setVisibility(View.VISIBLE);
            Glide.with(act)
                    .load(preafManager.getActiveBrand().getLogo())
                    .override(1600, 1600)

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

            binding.logoEmptyState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectImageClick(view);
                }
            });


        }
//        if (!getIntent().hasExtra("viewAll"))
//            LoadDataToUI();

        binding.logoCustom.setTag("0");
    }

    DialogUpgradeLayoutPackegeExpiredBindingImpl expriredBinding;

    public void askForUpgradeToEnterpisePackaged() {
        expriredBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout_packege_expired, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(expriredBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(expriredBinding.getRoot());

        expriredBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile", "1");

                act.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        expriredBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        expriredBinding.element3.setText("Please Upgrade your account for download more images");
        //alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    public void askForDownloadImage() {
        alertDialogBuilder = new AlertDialog.Builder(act);
        alertDialogBuilder.setTitle("Save image");
        alertDialogBuilder.setMessage("You sure to save your image?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        requestAgain();
                        saveImageToGallery(false, false);
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


                    }
                })
                .build()
                .show();
    }


    int tabIndex = 1;
    boolean needToIntro = false;

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

    public void CreateTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Category")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Footer")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Frames")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Background")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Text")));
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
                editorFragment = tab.getPosition();

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
                        needToIntro = false;
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
            needToIntro = true;

            if (binding.logoEmptyState.getVisibility() == View.VISIBLE)
                startIntro(binding.logoEmptyState, "Brand Logo", "Click on icon for choose your logo\n you can resize and move logo around anywhere in the image");
            else
                startIntro(binding.logoCustom, "Brand Logo", "Click your logo to move around anywhere in the image");

            preafManager.setViewAllActivityIntro(false);

        }


    }

    //load firstImage
    public void loadFirstImage() {

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
        if (binding.logoCustom.getTag().toString().equalsIgnoreCase("1"))
            pickerView(true, selectedLogo);
        else
            pickerView(false, null);

        //   CropImage.startPickImageActivity(this);
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

    public void LoadDataToUI() {
        preafManager = new PreafManager(act);
        if (selectedObject != null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);
        } else {
            // binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }

        if (selectedFooterModel == null)
            loadFirstImage();
    }

    //For
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
    }


    //For Image Select Interface
    @Override
    public void ImageCateonItemSelection(int position, ImageList listModel) {
        selectedObject = listModel;
        LoadDataToUI();
        binding.simpleProgressBar.setVisibility(View.GONE);
        if (selectedFooterModel == null)
            loadFirstImage();

        forCheckFavorite();
    }


    //For GetFrame
    @Override
    public void alertListenerClick() {
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
        screenExistDialog();
    }

    DialogDiscardImageBinding discardImageBinding;

    public void screenExistDialog() {
        discardImageBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_discard_image, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(discardImageBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
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
                if (!getIntent().hasExtra("notification")) {
                    CodeReUse.activityBackPress(act);
                } else {
                    Intent i = new Intent(act, HomeActivity.class);
                    startActivity(i);
                    act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    finish();
                }
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
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
                if (selectedObject != null) {
                    params.put("brand_id", preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());
                } else {
                    params.put("brand_id", preafManager.getActiveBrand().getId());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            //   Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
        if (requestCode == CodeReUse.ASK_PERMISSSION) {
            if (isDownloadOrSharingOrFavPending != -1) {
                //for favourit
                if (isDownloadOrSharingOrFavPending == 0) {
                    isDownloadOrSharingOrFavPending = -1;
                    if (binding.fabroutIcon.getVisibility() == View.VISIBLE) {
                        binding.fabroutIcon.setVisibility(View.GONE);
                        binding.addfabroutIcon.setVisibility(View.VISIBLE);
                    }

                    saveImageToGallery(false, true);
                }
                //for download
                if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (isDownloadOrSharingOrFavPending == 1) {
                        //   Toast.makeText(act, "fdggdgd", Toast.LENGTH_SHORT).show();
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

    //fire intent for share
    public void triggerShareIntent(File new_file, Bitmap merged) {
        //  Uri uri = Uri.parse();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Utility.getImageUri(act, merged));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

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
                intent.putExtra("Profile", "1");

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
                intent.putExtra("Profile", "1");

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
                intent.putExtra("Profile", "1");

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
                    Log.e("getActiveBrand", "d" + preafManager.getActiveBrand().getLogo());
                    if ((preafManager.getActiveBrand().getLogo().isEmpty() && selectedLogo == null) || preafManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
                        onSelectImageClick(view);
                    } else {
                        // Toast.makeText(act, "once you download or share image. You can't change your logo", Toast.LENGTH_SHORT).show();
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
                } else {
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
        isDownloadOrSharingOrFavPending = pendingActivity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                } else {
                    return true;
                }

            } else {
                if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                } else {

                    return true;
                }


            }
        } else {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
            } else {

                return true;
            }
        }

    }

    //save image with frame either custom or from backend
    public void saveImageToGallery(boolean wantToShare, boolean isFavourite) {

        Drawable bitmapFrame;
        if (isUsingCustomFrame) {
            bitmapFrame = new BitmapDrawable(getResources(), getCustomFrameInBitmap(isFavourite));
        } else {
            bitmapFrame = (BitmapDrawable) binding.backendFrame.getDrawable();
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
            String name = "image" + System.currentTimeMillis() + ".jpg";
            String file_name = file.getAbsolutePath() + "/" + name;
            new_file = new File(file_name);
            Log.e("new_file", new_file.getAbsolutePath() + "\n" + new_file.getPath());

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
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);
                    binding.FrameImageDuplicate.setImageBitmap(null);
                } else {
                    Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
                }
                Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);
                triggerShareIntent(new_file, merged);
            } else {
                Toast.makeText(act, "Your image is downloaded", Toast.LENGTH_SHORT).show();
                if (isUsingCustomFrame) {
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);

                    // addDynamicFooter(selectedFooterModel.getLayoutType(), true);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);
                    binding.FrameImageDuplicate.setImageBitmap(null);
                } else {
                    Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
                }
            }

            downloadAndShareApi(DOWLOAD, merged);
        } else {
            if (isUsingCustomFrame) {

                ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);
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
        Bitmap returnedBitmap = Bitmap.createBitmap(binding.elementCustomFrame.getWidth(), binding.elementCustomFrame.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = binding.elementCustomFrame.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        }
        binding.elementCustomFrame.draw(canvas);
        binding.FrameImageDuplicate.setVisibility(View.VISIBLE);
        binding.FrameImageDuplicate.setImageBitmap(returnedBitmap);
        BitmapDrawable drawable = (BitmapDrawable) binding.FrameImageDuplicate.getDrawable();
        newFinal = drawable.getBitmap();
        binding.FrameImageDuplicate.setVisibility(View.VISIBLE);
        return newFinal;
    }

    //backend frame load
    @Override
    public void onBackendFrameChoose(ImageList imageList, int position) {
        binding.backendFrame.setVisibility(View.VISIBLE);
        binding.elementCustomFrame.setVisibility(View.GONE);
        selectedBackendFrame = imageList;
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
        addDynamicFooter(footerLayout, false);
        forCheckFavorite();

        changeBorderColorAsFrame();
        loadSameColorToBackgroundAndTextAgain();
        ((ITextSizeEvent) act).onfontSize(previousFontSize);
    }


    //for adding footer dynamically
    int footerLayout = 1;
    LayoutModelClass layoutModelClass;

    private void addDynamicFooter(int layoutType, boolean isReload) {
        layoutModelClass = new LayoutModelClass();
        binding.elementFooter.removeAllViews();
        footerLayout = layoutType;
        if (layoutType == FooterModel.LAYOUT_FRAME_ONE) {
            LayoutForLoadOneBinding oneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_one, null, false);
            binding.elementFooter.addView(oneBinding.getRoot());
            FooterHelper.loadFrameFirstData(act, oneBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.main);
            mainLayout1 = (RelativeLayout) findViewById(R.id.addressLayoutElement2);
            layoutModelClass.setOneBinding(oneBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TWO) {
            LayoutForLoadTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            binding.elementFooter.addView(twoBinding.getRoot());
            layoutModelClass.setTwoBinding(twoBinding);
            FooterHelper.loadFrameTwoData(act, twoBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.firstView);
            mainLayout1 = (RelativeLayout) findViewById(R.id.secondView);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_THREE) {
            LayoutForLoadThreeBinding threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());
            layoutModelClass.setThreeBinding(threeBinding);
            FooterHelper.loadFrameThreeData(act, threeBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.section1);
            mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FOUR) {
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
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SEVEN) {
            LayoutForLoadSevenBinding sevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_seven, null, false);
            binding.elementFooter.addView(sevenBinding.getRoot());
            layoutModelClass.setSevenBinding(sevenBinding);
            FooterHelper.loadFrameSevenData(act, sevenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element0);
            mainLayout1 = (RelativeLayout) findViewById(R.id.socialFollow);

        } else if (layoutType == FooterModel.LAYOUT_FRAME_EIGHT) {
            LayoutForLoadEightBinding eightBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_eight, null, false);
            binding.elementFooter.addView(eightBinding.getRoot());
            layoutModelClass.setEightBinding(eightBinding);
            FooterHelper.loadFrameEightData(act, eightBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element1);
            mainLayout1 = (RelativeLayout) findViewById(R.id.element2);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_NINE) {
            LayoutForLoadNineBinding nineBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_nine, null, false);
            binding.elementFooter.addView(nineBinding.getRoot());
            layoutModelClass.setNineBinding(nineBinding);
            FooterHelper.loadFrameNineData(act, nineBinding);
            //mainLayout = (RelativeLayout) findViewById(R.id.firstLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TEN) {
            LayoutForLoadTenBinding tenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_ten, null, false);
            binding.elementFooter.addView(tenBinding.getRoot());
            layoutModelClass.setTenBinding(tenBinding);
            FooterHelper.loadFrameTenData(act, tenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.addressLayout);
            mainLayout1 = (RelativeLayout) findViewById(R.id.layout);
        }
    }

    @Override
    public void onColorSelected(int dialogId, int colorCode) {

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
    @Override
    public void onColorChanged(int colorCode) {
        colorCodeForTextColor = colorCode;
        if (editorFragment == 4) {
            FooterHelper.baseForTextColor(act, footerLayout, layoutModelClass, colorCode);
        }
    }

    //on border size change
    int borderSize;

    @Override
    public void onBorderSizeChange(int size) {
        borderSize = size;
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(size), colorCodeForBackground);
    }

    //for background color change
    @Override
    public void onChooseColor(int colorCode) {
        colorCodeForBackground = colorCode;
        if (editorFragment == 3) {
            FooterHelper.baseForBackground(act, footerLayout, layoutModelClass, colorCode);
            GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
            drawable.setStroke((int) convertDpToPx(borderSize), colorCodeForBackground);
        }
    }

    private int convertDpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void changeBorderColorAsFrame() {
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
        FooterHelper.baseForFontChange(act, footerLayout, Font, layoutModelClass);
    }

    // for font size
    @Override
    public void onfontSize(int textsize) {
        previousFontSize = textsize;
        if (previousFontSize != -1) {
            FooterHelper.baseForTextSize(textsize, footerLayout, layoutModelClass);
        }
    }

    //for bold text
    @Override
    public void onBoldTextChange(boolean Bold) {
        isLoadBold = Bold;
        FooterHelper.baseForBold(Bold, footerLayout, layoutModelClass);
    }

    //for italic
    @Override
    public void onItalicTextChange(boolean Italic) {
        isLoadItalic = Italic;
        FooterHelper.baseForItalic(Italic, footerLayout, layoutModelClass);
    }


    //to handle click and drag listener
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    //zoom Logo
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
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

    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_BRAND : ", response);
                ArrayList<BrandListItem> brandListItems = new ArrayList<>();
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
                params.put("Authorization", "Bearer " + preafManager.getUserToken());
                Log.e("Token", params.toString());
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

    //check for added to fav or not
    public void forCheckFavorite() {
        preafManager = new PreafManager(act);
        AddFavorite = preafManager.getSavedFavorites();
        Log.e("ALLPREF", gson.toJson(AddFavorite));
        if (AddFavorite != null) {
            boolean isImageFound = false;
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
    private void downloadAndShareApi(final int download, Bitmap customImage) {

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


        if (isUsingCustomFrame) {
            request.addMultipartParameter("brand_id", preafManager.getActiveBrand().getId());
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
        Log.e("Request", gson.toJson(request));
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
                        int imageCounter = Integer.parseInt(ResponseHandler.getString(dataJson.getJSONObject(0), "total_img_counter").equalsIgnoreCase("Unlimited") ? "-1" : ResponseHandler.getString(dataJson.getJSONObject(0), "total_img_counter"));

                        int used_img_counter = ResponseHandler.getString(dataJson.getJSONObject(0), "frame_counter").equals("") ? 0 : Integer.parseInt(ResponseHandler.getString(dataJson.getJSONObject(0), "used_img_counter"));


                        if (ResponseHandler.getBool(dataJson.getJSONObject(0), "status")) {
                            canDownload = true;
                            if (Utility.isUserPaid(preafManager.getActiveBrand())) {

                                if (imageCounter == -1 || used_img_counter <= imageCounter) {
                                    if (flag.equalsIgnoreCase("Download"))
                                        askForDownloadImage();
                                    else {
                                        requestAgain();
                                        saveImageToGallery(true, false);
                                    }
                                } else {
                                    downloadLimitExpireDialog("Your download limit is expired for your current package. To get more images please upgrade your package");
                                }

                            } else {
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
