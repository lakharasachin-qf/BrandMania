package com.app.brandmania.Activity.details;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Fragment.AddBrandFragment;
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
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Model.LayoutModelClass;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityViewAllImageBinding;
import com.app.brandmania.databinding.DialogDiscardImageBinding;
import com.app.brandmania.databinding.DialogUpgradeDownloadLimitExpireBinding;
import com.app.brandmania.databinding.DialogUpgradeLayoutEnterpriseBinding;
import com.app.brandmania.databinding.DialogUpgradeLayoutPackegeExpiredBindingImpl;
import com.app.brandmania.databinding.DialogUpgradeLayoutSecondBinding;
import com.app.brandmania.databinding.LayoutFooterEightteenBinding;
import com.app.brandmania.databinding.LayoutFooterElevenBinding;
import com.app.brandmania.databinding.LayoutFooterFifteenBinding;
import com.app.brandmania.databinding.LayoutFooterFourteenBinding;
import com.app.brandmania.databinding.LayoutFooterNineteenBinding;
import com.app.brandmania.databinding.LayoutFooterSeventeenBinding;
import com.app.brandmania.databinding.LayoutFooterSixteenBinding;
import com.app.brandmania.databinding.LayoutFooterThirteenBinding;
import com.app.brandmania.databinding.LayoutFooterTweloneBinding;
import com.app.brandmania.databinding.LayoutFooterTwentyBinding;
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
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.IFontChangeEvent;
import com.app.brandmania.utils.Utility;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Random;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class ImageCategoryDetailActivity extends BaseActivity implements ImageCateItemeInterFace, alertListenerCallback, ITextColorChangeEvent,
        IFontChangeEvent, ITextBoldEvent, IItaliTextEvent, ColorPickerDialogListener, IColorChange, ColorPickerView.OnColorChangedListener,
        ITextSizeEvent, onFooterSelectListener, IBackendFrameSelect, Player.EventListener {
    Activity act;
    private boolean isLoading = false;
    ArrayList<ImageList> AddFavorite = new ArrayList<>();
    private ActivityViewAllImageBinding binding;

    ArrayList<ImageList> menuModels = new ArrayList<>();
    ArrayList<FrameItem> brandListItems = new ArrayList<>();

    public static final int DOWLOAD = 1;
    public static final int ADDFAV = 3;
    public static final int REMOVEFAV = 3;
    private String is_frame = "";


    Gson gson;

    private ImageList selectedObject;
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


    private boolean isUsingCustomFrame = true;
    private ImageList selectedBackendFrame = null;
    private FooterModel selectedFooterModel;
    private boolean updateLogo = false;
    private Bitmap selectedLogo;
    private int colorCodeForBackground;
    private int colorCodeForTextColor = 0;
    private boolean isLoadBold = false;
    private boolean isLoadItalic = false;
    private String loadDefaultFont = "";
    private int previousFontSize = -1;
    int isDownloadOrSharingOrFavPending = -1;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        act.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_all_image);
        setActiveBrand();
        binding.titleName.setSelected(true);
        gson = new Gson();
        selectedObject = gson.fromJson(getIntent().getStringExtra("selectedimage"), ImageList.class);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (getIntent().hasExtra("notification")) {
            binding.titleName.setText(getIntent().getStringExtra("catName"));
        } else {
            binding.titleName.setText(selectedObject.getName());
        }
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        mainLayout = (RelativeLayout) findViewById(R.id.elementCustomFrame);
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(0), colorCodeForBackground);

        if (prefManager.getActiveBrand() != null) {
            //getFrame();
            updateLogo = prefManager.getActiveBrand().getLogo().isEmpty();
        }
        CreateTabs();

        colorCodeForBackground = ContextCompat.getColor(act, R.color.colorPrimary);
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
                if (prefManager.getActiveBrand() != null) {
                    selectedObject.setBrandId(prefManager.getActiveBrand().getId());
                    if (selectedBackendFrame != null) {
                        selectedObject.setFrame1Id(selectedBackendFrame.getFrame1Id());

                    }
                    selectedObject.setCustom(isUsingCustomFrame);

                    prefManager.AddToMyFavorites(selectedObject);

                    if (manuallyEnablePermission(0)) {
                        if (binding.fabroutIcon.getVisibility() == View.VISIBLE) {
                            binding.fabroutIcon.setVisibility(View.GONE);
                            binding.addfabroutIcon.setVisibility(View.VISIBLE);
                        }

                        saveImageToGallery(false, true);
                        Toast.makeText(act, "Added to Favourite", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    addBrandList();
                }
            }
        });

        binding.addfabroutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.getActiveBrand() != null) {
                    if (selectedBackendFrame != null) {
                        selectedObject.setFrame1Id(selectedBackendFrame.getFrame1Id());
                    }
                    selectedObject.setBrandId(prefManager.getActiveBrand().getId());
                    selectedObject.setCustom(isUsingCustomFrame);

                    prefManager.removeFromMyFavorites(selectedObject);
                    if (binding.addfabroutIcon.getVisibility() == View.VISIBLE) {
                        binding.addfabroutIcon.setVisibility(View.GONE);
                        binding.fabroutIcon.setVisibility(View.VISIBLE);
                    }
                    removeFromFavourite(REMOVEFAV);
                    Toast.makeText(act, "Removed From Favourite", Toast.LENGTH_SHORT).show();
                } else {
                    addBrandList();
                }
            }

        });
        binding.downloadIcon.setOnClickListener(v -> {
            if (prefManager.getActiveBrand() != null) {
                if (manuallyEnablePermission(1)) {
                    if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
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
                        getImageDownloadRights("Download");
                    }
                }
            } else {
                addBrandList();
            }
        });

        binding.shareIcon.setOnClickListener(v -> {

            if (prefManager.getActiveBrand() != null) {
                if (manuallyEnablePermission(2)) {

                    if (!Utility.isUserPaid(prefManager.getActiveBrand())) {

                        if (!prefManager.getLoginDate().isEmpty()) {
                            //For Seven Day Image Download for free User in a day
                            Utility.Log("ImageConuter:::", prefManager.getDaysCounter());
                            if (HELPER.IsTwoDateComparison(prefManager.getLoginDate(), act, prefManager.getDaysCounter())) {

                                if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                                    askForUpgradeToEnterpisePackage();
                                    return;
                                }
                                if (selectedObject.getImageType() == ImageList.IMAGE) {
                                    //for Image download
                                    getImageDownloadRights("Share");
                                } else {
                                    String SubscriptionDate = new PreafManager(act).getActiveBrand().getSubscriptionDate();
                                    //Utility.Log("getSubscriptionDate", SubscriptionDate);
                                    if (SubscriptionDate.isEmpty()) {
                                        alertOffer("Kindly upgrade your package to use video and gif feature.");
                                    } else {
                                        //for gif/video download
                                        checkForDownload();
                                    }
                                }

                            } else {

                                if (selectedObject.isImageFree()) {
                                    if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                                        askForUpgradeToEnterpisePackage();
                                        return;
                                    }
                                    if (selectedObject.getImageType() == ImageList.IMAGE) {
                                        getImageDownloadRights("Share");
                                    } else {
                                        checkForDownload();
                                    }
                                } else {
                                    askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
                                }

                            }

                        }

                        //For normal Flow

////                        if (selectedObject.isImageFree()) {
//                        if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
//                            askForUpgradeToEnterpisePackage();
//                            return;
//                        }
//                        if (selectedObject.getImageType() == ImageList.IMAGE) {
//                            getImageDownloadRights("Share");
//                        } else {
//                            checkForDownload();
//                        }
////                        } else {
////                            askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
////                        }
                    } else {

                        if (!Utility.isPackageExpired(act)) {
                            if (selectedObject.getImageType() == ImageList.IMAGE) {
                                getImageDownloadRights("Share");
                            } else {
                                checkForDownload();
                            }
                        } else {
                            askForUpgradeToEnterpisePackaged();
                        }

                    }

                }
            } else {
                addBrandList();
            }
        });

//        binding.shareIcon.setOnClickListener(v -> {
//            if (prefManager.getActiveBrand() != null) {
//                if (manuallyEnablePermission(2)) {
//
//                    if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
//
//                        if (!prefManager.getLoginDate().isEmpty()) {
//                            //For Seven Day Image Download for free User in a day
//                            Log.e("ImageConuter:::", prefManager.getDaysCounter());
//                            if (HELPER.IsTwoDateComparison(prefManager.getLoginDate(), act, prefManager.getDaysCounter())) {
//
//                                if (!prefManager.getActiveBrand().getLogo().isEmpty()) {
//                                    if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
//                                        askForUpgradeToEnterpisePackage();
//                                        return;
//                                    }
//                                    if (selectedObject.getImageType() == ImageList.IMAGE) {
//                                        //for Image download
//                                        getImageDownloadRights("Share");
//                                    } else {
//                                        //for gif/video download
//                                        checkForDownload();
//                                    }
//
//                                } else {
//                                    androidx.appcompat.app.AlertDialog AlertDialogBuilder = new MaterialAlertDialogBuilder(act, R.style.RoundShapeTheme)
//                                            .setTitle("Add Your Logo")
//                                            .setMessage("Your Logo is empty..!")
//                                            .setPositiveButton("OK", (dialogInterface, i) -> HELPER.ROUTE(act, UpdateBandList.class))
//                                            .setNeutralButton("LATER", (dialogInterface, i) -> {
//                                            })
//                                            .show();
//                                    AlertDialogBuilder.setCancelable(false);
//                                }
//
//                            } else {
//
//                                if (!prefManager.getActiveBrand().getLogo().isEmpty()) {
//                                    if (selectedObject.isImageFree()) {
//                                        if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
//                                            askForUpgradeToEnterpisePackage();
//                                            return;
//                                        }
//                                        if (selectedObject.getImageType() == ImageList.IMAGE) {
//                                            getImageDownloadRights("Share");
//                                        } else {
//                                            checkForDownload();
//                                        }
//                                    } else {
//                                        askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
//                                    }
//
//                                } else {
//                                    androidx.appcompat.app.AlertDialog AlertDialogBuilder = new MaterialAlertDialogBuilder(act, R.style.RoundShapeTheme)
//                                            .setTitle("Add Your Logo")
//                                            .setMessage("Your Logo is empty..!")
//                                            .setPositiveButton("OK", (dialogInterface, i) -> HELPER.ROUTE(act, UpdateBandList.class))
//                                            .setNeutralButton("LATER", (dialogInterface, i) -> {
//                                            })
//                                            .show();
//                                    AlertDialogBuilder.setCancelable(false);
//
//                                }
//
//
//                            }
//
//                        }
//
//                        //For normal Flow
//
//////                        if (selectedObject.isImageFree()) {
////                        if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
////                            askForUpgradeToEnterpisePackage();
////                            return;
////                        }
////                        if (selectedObject.getImageType() == ImageList.IMAGE) {
////                            getImageDownloadRights("Share");
////                        } else {
////                            checkForDownload();
////                        }
//////                        } else {
//////                            askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
//////                        }
//                    } else {
//                        if (!prefManager.getActiveBrand().getLogo().isEmpty()) {
//
//                            if (!Utility.isPackageExpired(act)) {
//                                if (selectedObject.getImageType() == ImageList.IMAGE) {
//                                    getImageDownloadRights("Share");
//                                } else {
//                                    checkForDownload();
//                                }
//                            } else {
//                                askForUpgradeToEnterpisePackaged();
//                            }
//
//                        } else {
//                            androidx.appcompat.app.AlertDialog AlertDialogBuilder = new MaterialAlertDialogBuilder(act, R.style.RoundShapeTheme)
//                                    .setTitle("Add Your Logo")
//                                    .setMessage("Your Logo is empty..!")
//                                    .setPositiveButton("OK", (dialogInterface, i) -> HELPER.ROUTE(act, UpdateBandList.class))
//                                    .setNeutralButton("LATER", (dialogInterface, i) -> {
//                                    })
//                                    .show();
//                            AlertDialogBuilder.setCancelable(false);
//
//                        }
//
//                    }
//
//                }
//            } else {
//                addBrandList();
//            }
//        });
        setLogo();
        if (!getIntent().hasExtra("viewAll"))
            LoadDataToUI();
        binding.logoCustom.setTag("0");
    }

    public void setActiveBrand() {

        if (prefManager.getActiveBrand() == null && prefManager.getAddBrandList() != null && prefManager.getAddBrandList().size() != 0)
            prefManager.setActiveBrand(prefManager.getAddBrandList().get(0));

        prefManager = new PreafManager(this);
        if (prefManager.getActiveBrand() != null) {

        }
    }


    public void setLogo() {

        if (prefManager.getActiveBrand() != null && prefManager.getActiveBrand().getLogo() != null && !prefManager.getActiveBrand().getLogo().isEmpty()) {
            binding.logoEmptyState.setVisibility(View.GONE);
            binding.logoCustom.setVisibility(View.VISIBLE);
            binding.logoCustom.setVisibility(View.VISIBLE);
            Glide.with(act)
                    .load(prefManager.getActiveBrand().getLogo())
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

    }

    AddBrandFragment addBrandFragment;

    public void addBrandList() {
        if (addBrandFragment != null) {
            if (addBrandFragment.isVisible()) {
                addBrandFragment.dismiss();
            }
        }
        addBrandFragment = new AddBrandFragment();
        addBrandFragment.setAddBrandFromImageCat(true);
        addBrandFragment.show(getSupportFragmentManager(), "");
    }

    boolean isShareIntentVisisble = false;

    @Override
    public void onResume() {
        if (exoPlayer != null && selectedObject.getImageType() != ImageList.IMAGE) {
            startPlayer();
        }
        if (isShareIntentVisisble) {
            //Log.e("stopLoader", "yes");
            Utility.dismissLoadingTran();
        }

        if (isLogoEmpty) {
            Utility.Log("isLogoClickFromEmptyLAyout", "yesssssss");
            if (prefManager.getActiveBrand().getLogo() != null && prefManager.getActiveBrand().getLogo().isEmpty()) {
                if (isLogoNotEmpty) {
                    binding.logoEmptyState.setVisibility(View.GONE);
                } else {
                    binding.logoEmptyState.setVisibility(View.VISIBLE);
                }
            } else {
                binding.logoEmptyState.setVisibility(View.GONE);
            }
        }
        super.onResume();
    }

    ArrayList<BrandListItem> multiListItems = new ArrayList<>();

    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_BRAND);
        binding.simpleProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_BRAND : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);

                    if (multiListItems != null && multiListItems.size() != 0) {
                        for (int i = 0; i < multiListItems.size(); i++) {
                            if (multiListItems.get(i).getName().equalsIgnoreCase(prefManager.getActiveBrand().getName())) {
                                prefManager.setActiveBrand(multiListItems.get(i));
                                break;
                            }
                        }
                    }
                    prefManager = new PreafManager(act);

                    if (prefManager.getActiveBrand() == null) {
                        if (multiListItems.size() != 0) {
                            prefManager.setActiveBrand(multiListItems.get(0));
                        }
                    }
                    setLogo();
                    setActiveBrand();
                    loadFirstImage();
                    binding.simpleProgressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        binding.simpleProgressBar.setVisibility(View.GONE);

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Log.e("DateNdClass", params.toString());

                //Log.e("DateNdClass", params.toString());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
        super.onDestroy();

    }

    public void checkForDownload() {

        try {

            String subscriptionDate = new PreafManager(act).getActiveBrand().getSubscriptionDate().replace('-', '/');

            Log.e("subscriptionDate::::::", subscriptionDate);
            String currentDateStr = "01/11/2021";  //new prefManager(act).getActiveBrand().getSubscriptionDate().replace('-', '/');
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date convertedExpireDate = formatter.parse(subscriptionDate);
            Date convertedCurrentDate = formatter.parse(currentDateStr);
            //7016920443
            //Log.e("subscriptionDate"+subscriptionDate);

            boolean canDownloads = true;
            if (convertedCurrentDate.compareTo(convertedExpireDate) < 0) {
                //new user
                if (prefManager.getActiveBrand().getPackagename().contains("Enterprise") || prefManager.getActiveBrand().getPackagename().contains("Standard")) {
                    //999, 1999
                    canDownloads = true;
                } else {
                    canDownloads = false;
                    //299,
                    alertOffer("Kindly upgrade your package to use video and gif feature.");
                }
            } else {
                //old user
                if (prefManager.getActiveBrand().getPackagename().contains("Enterprise")) {
                    //means user in 999, 1999
                    canDownloads = true;
                } else {
                    //means user are in 299, 599
                    //also check k download kri ske only till new year sudhi
                    String offerValidDate = "05/11/2021";  //new prefManager(act).getActiveBrand().getSubscriptionDate().replace('-', '/');
                    Date calDate = new Date();
                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String calDateStr = formatter.format(calDate);

                    Date calDateFF = formatter.parse(calDateStr);  //calDateStr
                    Date offerValidDateFF = formatter.parse(offerValidDate);
                    if (calDateFF.compareTo(offerValidDateFF) < 0) {
                        //allow download
                        canDownloads = true;
                    } else {
                        canDownloads = false;
                        alertOffer("Your offer was limited to Diwali only. Kindly upgrade your package to enjoy video and gif feature");
                    }

                }

            }

            if (canDownloads) {
                if (selectedObject.getImageType() == ImageList.GIF) {
                    saveGif();
                } else if (selectedObject.getImageType() == ImageList.VIDEO) {
                    saveVideo();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ExtractorsFactory extractorsFactory;
    DefaultHttpDataSourceFactory dataSourceFactory;
    SimpleExoPlayer exoPlayer;

    @Override
    protected void onPause() {
        if (exoPlayer != null && selectedObject.getImageType() != ImageList.IMAGE) {
            pausePlayer();
        }
        super.onPause();
    }

    private void startPlayer() {
        exoPlayer.setPlayWhenReady(true);

    }

    private void pausePlayer() {
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.seekTo(0);
    }

    public void LoadDataToUI() {
        prefManager = new PreafManager(act);
        if (selectedObject != null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            if (selectedObject.getImageType() == ImageList.IMAGE) {
                if (exoPlayer != null && binding.videoView.getVisibility() == View.VISIBLE) {
                    exoPlayer.stop();
                    exoPlayer.release();
                }
                binding.videoView.setVisibility(View.GONE);
                binding.recoImage.setVisibility(View.VISIBLE);
                binding.imageKoadingView.setVisibility(View.VISIBLE);
                binding.imageKoadingView.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext())
                        .load(selectedObject.getFrame())
                        .override(720, 720)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                binding.imageKoadingView.setVisibility(View.GONE);
                                binding.recoImage.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(binding.recoImage);
                binding.fabroutIcon.setVisibility(View.VISIBLE);
            }

            if (selectedObject.getImageType() == ImageList.VIDEO || selectedObject.getImageType() == ImageList.GIF) {
                binding.recoImage.setVisibility(View.GONE);
                binding.videoView.setVisibility(View.VISIBLE);
                binding.videoView.requestFocus();
                binding.simpleProgressBar.setVisibility(View.VISIBLE);
                try {
                    if (exoPlayer != null) {
                        exoPlayer.stop();
                        exoPlayer.release();
                    }
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
                    dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                    extractorsFactory = new DefaultExtractorsFactory();
                    exoPlayer.addListener(this);
                    MediaSource mediaSource = new ExtractorMediaSource(selectedObject.getVideoSet(), dataSourceFactory, extractorsFactory, null, null);
                    binding.videoView.setPlayer(exoPlayer);
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(true);
                    exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
                    exoPlayer.addListener(this);
                    binding.simpleProgressBar.setVisibility(View.VISIBLE);


                } catch (Exception ignored) {
                }

                binding.fabroutIcon.setVisibility(View.GONE);
            }
        }

        if (selectedFooterModel == null)
            loadFirstImage();
    }


    public void saveVideo() {

        HELPER._INIT_FOLDER(Constant.ROOT);
        HELPER._INIT_FOLDER(Constant.GIF);
        HELPER._INIT_FOLDER(Constant.VIDEOS);
        HELPER._INIT_FOLDER(Constant.DATA);

        saveImageInCache();
    }

    ProgressDialog progressDialog;

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                binding.simpleProgressBar.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                break;
            case Player.STATE_READY:
                binding.simpleProgressBar.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        binding.simpleProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping...");
            saveVideoInCatch();
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {

            return;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(act);
            if (isItGIF)
                progressDialog.setMessage("Downloading the GIF...");
            else
                progressDialog.setMessage("Downloading the video...");

            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    public void shareVideo(File uris) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        File imageFileToShare = new File(String.valueOf(uris));
        Uri uri = Uri.fromFile(imageFileToShare);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setDataAndType(uri, "video/*");
        shareIntent.setType("video/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(Intent.createChooser(shareIntent, "Share Video to.."));
    }

    public void shareGIF(File uris) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        File imageFileToShare = new File(String.valueOf(uris));
        Uri uri = Uri.fromFile(imageFileToShare);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setDataAndType(uri, "image/gif");
        shareIntent.setType("image/gif");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(Intent.createChooser(shareIntent, "Share GIF to.."));
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    private String FinalVideoPath = "";
    boolean isItGIF = false;


    public void saveImageInCache() {
        String overlayImage = "overlay_" + System.currentTimeMillis() + ".png";
        Bitmap frontFrameBitmap;
        if (isUsingCustomFrame) {
            frontFrameBitmap = getResizedBitmap(getCustomFrameInBitmap(true), 1080, 1080);
        } else {
            frontFrameBitmap = getResizedBitmap(((BitmapDrawable) binding.backendFrame.getDrawable()).getBitmap(), 1080, 1080);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, overlayImage);
                String desDirectory = Environment.DIRECTORY_DOWNLOADS + File.separator + Constant.ROOT + "/" + Constant.DATA;
                String outputFile = desDirectory + File.separator + overlayImage;
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, desDirectory);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                Uri uri = act.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream outputStream = act.getContentResolver().openOutputStream(uri);
                    frontFrameBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Objects.requireNonNull(outputStream);
                }
                framePath = outputFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File file;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constant.ROOT + "/" + Constant.DATA + "/" + overlayImage);
            try {
                OutputStream outStream = new FileOutputStream(file);
                frontFrameBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            framePath = file.getPath();

        }

        new AsyncTaskRunner().execute();

    }

    public void coding() {
        String loadedFile = String.valueOf(selectedObject.getVideoSet()).substring(String.valueOf(selectedObject.getVideoSet()).lastIndexOf('/') + 1).split("\\.")[0] + System.currentTimeMillis();
        loadedFile = selectedObject.getName().replaceAll("\\s", "") + new Random().nextInt(1000);
        String outputFilePath;
        String filePrefix = loadedFile;
        String fileExtn = ".mp4";
        String fileType = "video/mp4";
        if (isItGIF) {
            fileExtn = ".gif";
            fileType = "image/gif";
        }

        File outputFile;
        if (isItGIF) {
            outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constant.ROOT + "/" + Constant.GIF + "/", filePrefix + fileExtn);
        } else {
            outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constant.ROOT + "/" + Constant.VIDEOS + "/", filePrefix + fileExtn);
        }

        outputFilePath = outputFile.getAbsolutePath();

        String command = "-i " + finalVideoPath + " -i " + framePath + " -filter_complex " + "[0]scale=1080:-2[bg];[bg][1]overlay=main_w-overlay_w:main_h-overlay_h " + outputFilePath;

        execCommand(command);
        finalOutputFile = new File(outputFilePath);
    }

    String framePath = "";
    String finalVideoPath = "";
    long downloadID;
    String videoUrl;
    DownloadManager manager;
    boolean isVideoIsDownloading = false;

    public void saveVideoInCatch() {
        if (!isVideoIsDownloading) {
            String url = String.valueOf(selectedObject.getVideoSet());
            //Log.e("URL-Download", url);

            //url = url.replace("http", "https");
            DownloadManager.Request request;
            request = new DownloadManager.Request(Uri.parse(url));
            videoUrl = URLUtil.guessFileName(url, null, null).replace("0", System.currentTimeMillis() + "");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setDescription("Downloading Please wait...");
            request.setTitle(videoUrl);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, videoUrl);
            request.allowScanningByMediaScanner();
            manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = manager.enqueue(request);
            registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            isVideoIsDownloading = true;
        }
    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                //Log.e("Download-Process", "Done");
                File outputFrameFile = new File(act.getCacheDir() + File.separator);
                String vdPaths = outputFrameFile.getAbsolutePath();

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + File.separator);
                if (!file.exists()) {
                    file.mkdir();
                }

                String path = file.getAbsolutePath();
                file = new File(path + "/" + videoUrl);
                String vdPath = file.getAbsolutePath();
                file = new File(vdPaths + "/" + videoUrl);
                if (!vdPath.isEmpty()) {
                    finalVideoPath = vdPath;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    framePath = path.replace("Download", "") + framePath;
                }
                isVideoIsDownloading = false;
                coding();
            }
        }
    };
    FFmpeg ffmpeg;
    File finalOutputFile = null;

    public void execCommand(String cmd) {
        progressDialog.setMessage("Processing......");
        //Log.e("Prcess Start", "Yes");
        long executionId = FFmpeg.executeAsync(cmd, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {
                progressDialog.dismiss();

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + videoUrl);
                if (file.exists())
                    file.delete();

                file = new File(framePath);
                if (file.exists())
                    file.delete();

                downloadAndShareApi(DOWLOAD, null);
            }
        });
    }


    public void saveGif() {
        isItGIF = true;
        HELPER._INIT_FOLDER(Constant.ROOT);
        HELPER._INIT_FOLDER(Constant.DATA);
        saveVideo();
    }

    DialogUpgradeLayoutPackegeExpiredBindingImpl expriredBinding;

    public void askForUpgradeToEnterpisePackaged() {
        expriredBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout_packege_expired, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(expriredBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(expriredBinding.getRoot());

        expriredBinding.viewPackage.setOnClickListener((View.OnClickListener) v -> {
            alertDialog.dismiss();

            Intent intent = new Intent(act, PackageActivity.class);
            intent.putExtra("Profile", "1");
            act.startActivity(intent);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });
        expriredBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        expriredBinding.element3.setText("Please Upgrade your account for download more images");
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
                .setDismissType(DismissType.anywhere)
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
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(Utility.convertFirstUpper("Post")));
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
            prefManager.setFrameIntro(false);
        }

        if (prefManager.getViewAllActivityIntro()) {
            needToIntro = true;
            if (binding.logoEmptyState.getVisibility() == View.VISIBLE)
                startIntro(binding.logoEmptyState, "Brand Logo", "Click on icon for choose your logo\n you can resize and move logo around anywhere in the image");
            else
                startIntro(binding.logoCustom, "Brand Logo", "Click your logo to move around anywhere in the image");
            prefManager.setViewAllActivityIntro(false);
        }
    }

    public void loadFirstImage() {

        FooterModel model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_TWELVE);
        model.setFree(true);
        if (prefManager.getActiveBrand() != null) {
            model.setAddress(prefManager.getActiveBrand().getAddress());
            model.setEmailId(prefManager.getActiveBrand().getEmail());
            model.setContactNo(prefManager.getActiveBrand().getPhonenumber());
            model.setWebsite(prefManager.getActiveBrand().getWebsite());
        }
        ((onFooterSelectListener) act).onFooterSelectEvent(FooterModel.LAYOUT_FRAME_TWELVE, model);
    }

    //For CustomFrame
    public void onSelectImageClick(View view) {
        if (binding.logoCustom.getTag().toString().equalsIgnoreCase("1"))
            pickerView(true, selectedLogo);
        else
            pickerView(false, null);
    }

    boolean isLogoNotEmpty = false;

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
                    if (selectedLogo != null) {
                        isLogoNotEmpty = true;
                    }
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


    public void refreshgallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
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
        if (selectedObject.getImageType() == ImageList.IMAGE)
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

                if (exoPlayer != null) {
                    exoPlayer.stop();
                    exoPlayer.release();
                }

                if (!getIntent().hasExtra("notification")) {
                    CodeReUse.activityBackPress(act);
                } else {
                    Intent i = new Intent(act, HomeActivity.class);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    finish();
                }
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (!act.isDestroyed() && !act.isFinishing())
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

                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (selectedObject != null) {
                    params.put("brand_id", prefManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());
                } else {
                    params.put("brand_id", prefManager.getActiveBrand().getId());
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
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

//                    if (selectedObject.getImageType() == ImageList.IMAGE) {
//                        saveImageToGallery(false, true);
//                    } else {
//                        saveGif();
//                    }
                    saveGif();
                    // saveImageToGallery(false, true);
                }
                //for download
                if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (isDownloadOrSharingOrFavPending == 1) {
                        //   Toast.makeText(act, "fdggdgd", Toast.LENGTH_SHORT).show();
                        isDownloadOrSharingOrFavPending = -1;
                        if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
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
                        if (!Utility.isUserPaid(prefManager.getActiveBrand())) {
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
        //Log.e("shareIntent", "yes");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //File imageFileToShare = new File(String.valueOf(uris));
        Uri uri = Uri.fromFile(new_file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setDataAndType(uri, "image/jpeg");
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(Intent.createChooser(shareIntent, "Share Image to.."));

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
    DialogUpgradeLayoutEnterpriseBinding offerBinding;

    public void alertOffer(String detailsMsg) {
        offerBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_upgrade_layout_enterprise, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(offerBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(offerBinding.getRoot());

        offerBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(act, PackageActivity.class);
                intent.putExtra("Profile", "1");

                act.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        offerBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        offerBinding.element2.setText("Upgrade Package");
        offerBinding.element3.setText(detailsMsg);
        //alertDialog.setCancelable(false);
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
                    if (prefManager.getActiveBrand() != null) {
                        if ((prefManager.getActiveBrand().getLogo().isEmpty() && selectedLogo == null) || prefManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
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

    boolean isLogoEmpty = false;

    public void saveImageToGallery(boolean wantToShare, boolean isFavourite) {
        HELPER._INIT_FOLDER(Constant.ROOT);
        HELPER._INIT_FOLDER(Constant.DATA);
        HELPER._INIT_FOLDER(Constant.IMAGES);

        if ((prefManager.getActiveBrand().getLogo() == null && prefManager.getActiveBrand().getLogo().isEmpty())) {
            binding.logoEmptyState.setVisibility(View.GONE);
            isLogoEmpty = true;
        }

        Drawable bitmapFrame;
        if (isUsingCustomFrame) {
            bitmapFrame = new BitmapDrawable(getResources(), getCustomFrameInBitmap(isFavourite));
        } else {
            bitmapFrame = (BitmapDrawable) binding.backendFrame.getDrawable();
        }
        Drawable ImageDrawable = (BitmapDrawable) binding.recoImage.getDrawable();
        Bitmap merged = Bitmap.createBitmap(720, 720, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(merged);
        bitmapFrame.setBounds(0, 0, 720, 720);
        ImageDrawable.setBounds(0, 0, 720, 720);
        ImageDrawable.draw(canvas);
        bitmapFrame.draw(canvas);

        if (!isFavourite) {
            FileOutputStream fileOutputStream;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
            String date = simpleDateFormat.format(new Date());
            String name = "image" + System.currentTimeMillis() + ".jpg";
            String file_name = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constant.ROOT + "/" + Constant.IMAGES + "/" + name;
            new_file = new File(file_name);

            try {
                fileOutputStream = new FileOutputStream(new_file);
                merged.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            refreshgallery(new_file);

            if (wantToShare) {

                //Log.e("wantToShare", "yes");
                if (isUsingCustomFrame) {
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);
                    binding.FrameImageDuplicate.setVisibility(View.GONE);
                    binding.FrameImageDuplicate.setImageBitmap(null);
                } else {
                    Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
                }
                Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);
                Utility.showLoadingTran(act);
                isShareIntentVisisble = true;

                triggerShareIntent(new_file, merged);

            } else {
                Toast.makeText(act, "Your image is downloaded", Toast.LENGTH_SHORT).show();
                if (isUsingCustomFrame) {
                    ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);
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

    @Override
    public void onBackendFrameChoose(ImageList imageList, int position) {
        binding.backendFrame.setVisibility(View.VISIBLE);
        binding.elementCustomFrame.setVisibility(View.GONE);
        selectedBackendFrame = imageList;
        Glide.with(getApplicationContext()).load(imageList.getFrame1()).into(binding.backendFrame);
        isUsingCustomFrame = false;
        if (selectedObject.getImageType() == ImageList.IMAGE)
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
        if (selectedObject != null && selectedObject.getImageType() == ImageList.IMAGE)
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
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFirstData(act, oneBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.main);
            layoutModelClass.setOneBinding(oneBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TWO) {
            LayoutForLoadTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            binding.elementFooter.addView(twoBinding.getRoot());
            layoutModelClass.setTwoBinding(twoBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameTwoData(act, twoBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.firstView);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_THREE) {
            LayoutForLoadThreeBinding threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());
            layoutModelClass.setThreeBinding(threeBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameThreeData(act, threeBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.section1);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FOUR) {
            LayoutForLoadFourBinding fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_four, null, false);
            binding.elementFooter.addView(fourBinding.getRoot());
            layoutModelClass.setFourBinding(fourBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFourData(act, fourBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.section1);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FIVE) {
            LayoutForLoadFiveBinding fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_five, null, false);
            binding.elementFooter.addView(fiveBinding.getRoot());
            layoutModelClass.setFiveBinding(fiveBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFiveData(act, fiveBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.main);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SIX) {
            LayoutForLoadSixBinding sixBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_six, null, false);
            binding.elementFooter.addView(sixBinding.getRoot());
            layoutModelClass.setSixBinding(sixBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameSixData(act, sixBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.containerElement);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SEVEN) {
            LayoutForLoadSevenBinding sevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_seven, null, false);
            binding.elementFooter.addView(sevenBinding.getRoot());
            layoutModelClass.setSevenBinding(sevenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameSevenData(act, sevenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element0);

        } else if (layoutType == FooterModel.LAYOUT_FRAME_EIGHT) {
            LayoutForLoadEightBinding eightBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_eight, null, false);
            binding.elementFooter.addView(eightBinding.getRoot());
            layoutModelClass.setEightBinding(eightBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameEightData(act, eightBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element1);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_NINE) {
            LayoutForLoadNineBinding nineBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_nine, null, false);
            binding.elementFooter.addView(nineBinding.getRoot());
            layoutModelClass.setNineBinding(nineBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameNineData(act, nineBinding);
            //mainLayout = (RelativeLayout) findViewById(R.id.firstLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TEN) {
            LayoutForLoadTenBinding tenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_ten, null, false);
            binding.elementFooter.addView(tenBinding.getRoot());
            layoutModelClass.setTenBinding(tenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameTenData(act, tenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_ELEVEN) {
            LayoutFooterElevenBinding elevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_eleven, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(elevenBinding.getRoot());

            View view = elevenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setElevenBinding(elevenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameElevenData(act, elevenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TWELVE) {
            LayoutFooterTweloneBinding tweloneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_twelone, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(tweloneBinding.getRoot());

            View view = tweloneBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setTwelveBinding(tweloneBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameTweloneData(act, tweloneBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_THIRTEEN) {
            LayoutFooterThirteenBinding thirteenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_thirteen, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(thirteenBinding.getRoot());

            View view = thirteenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setThirteenBinding(thirteenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameThirteenData(act, thirteenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FOURTEEN) {
            LayoutFooterFourteenBinding fourteenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_fourteen, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(fourteenBinding.getRoot());

            View view = fourteenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setFourteenBinding(fourteenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFourteenData(act, fourteenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FIFTEEN) {
            LayoutFooterFifteenBinding fifteenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_fifteen, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(fifteenBinding.getRoot());

            View view = fifteenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setFifteenBinding(fifteenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFifteenData(act, fifteenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SIXTEEN) {
            LayoutFooterSixteenBinding sixteenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_sixteen, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(sixteenBinding.getRoot());

            View view = sixteenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setSixteenBinding(sixteenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrame16Data(act, sixteenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SEVENTEEN) {
            LayoutFooterSeventeenBinding seventeenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_seventeen, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(seventeenBinding.getRoot());

            View view = seventeenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setSeventeenBinding(seventeenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrame17Data(act, seventeenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_EIGHTEEN) {
            LayoutFooterEightteenBinding eighteenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_eightteen, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(eighteenBinding.getRoot());

            View view = eighteenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setEightteenBinding(eighteenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrame18Data(act, eighteenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_NINETEEN) {
            LayoutFooterNineteenBinding nineteenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_nineteen, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(nineteenBinding.getRoot());

            View view = nineteenBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setNineteenBinding(nineteenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrame19Data(act, nineteenBinding);
            mainLayout = findViewById(R.id.addressLayout);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TWENTY) {
            LayoutFooterTwentyBinding twentyBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_footer_twenty, null, false);

            binding.elementFooter.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            binding.elementFooter.requestLayout();
            binding.elementFooter.addView(twentyBinding.getRoot());

            View view = twentyBinding.getRoot();
            view.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            view.requestLayout();
            layoutModelClass.setTwentyBinding(twentyBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrame20Data(act, twentyBinding);
            mainLayout = findViewById(R.id.addressLayout);
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
        //binding.logoCustom.onTouchEvent(motionEvent);
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


    public void forCheckFavorite() {
        prefManager = new PreafManager(act);
        if (prefManager.getActiveBrand() != null) {
            AddFavorite = prefManager.getSavedFavorites();
            if (AddFavorite != null) {
                boolean isImageFound = false;
                for (int i = 0; i < AddFavorite.size(); i++) {
                    if (prefManager.getActiveBrand().getId().equalsIgnoreCase(AddFavorite.get(i).getBrandId())) {
                        if (isUsingCustomFrame) {
                            if (AddFavorite.get(i).isCustom()) {
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
    }

    //getFrames


    //For Download,Share and Fav
    private void downloadAndShareApi(final int download, Bitmap customImage) {

        //    Utility.showLoadingTran(act);
        Utility.Log("API : ", APIs.DOWNLOAD_SHARE);
        File img1File = null;
        if (customImage != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", customImage);
        }
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.DOWNLOAD_SHARE)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-Authorization", "Bearer" + prefManager.getUserToken())
                .setPriority(Priority.HIGH);


        if (isUsingCustomFrame) {
            request.addMultipartParameter("brand_id", prefManager.getActiveBrand().getId());
            request.addMultipartParameter("image_id", selectedObject.getImageid());
            request.addMultipartParameter("is_custom", "1");
            request.addMultipartParameter("footer_id", String.valueOf(selectedFooterModel.getLayoutType()));
            if (img1File != null) {
                request.addMultipartFile("image", img1File);
            }
        } else {
            request.addMultipartParameter("brand_id", prefManager.getActiveBrand().getId());
            request.addMultipartParameter("image_id", selectedObject.getImageid());
            request.addMultipartParameter("frame_id", selectedBackendFrame.getFrame1Id());
            request.addMultipartParameter("is_custom", "0");
        }
        request.addMultipartParameter("type", String.valueOf(download));
        request.build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                // do anything with progress
            }
        })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Utility.dismissLoadingTran();


                        if (updateLogo && selectedLogo != null)
                            uploadLogoForBrand(selectedLogo);

                        if (download == DOWLOAD) {
                            //this is coding for can we change logo or not
                            String usedImageCountStr = prefManager.getActiveBrand().getNo_of_used_image();
                            if (usedImageCountStr.isEmpty())
                                usedImageCountStr = "0";

                            int usedCounter = Integer.parseInt(usedImageCountStr) + 1;
                            BrandListItem brandListItem = prefManager.getActiveBrand();
                            brandListItem.setNo_of_used_image(String.valueOf(usedCounter));
                            prefManager.setActiveBrand(brandListItem);
                            prefManager = new PreafManager(act);

                            if (selectedObject.getImageType() != ImageList.IMAGE) {
                                shareVideoOrGIF();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading = false;


                        if (selectedObject.getImageType() != ImageList.IMAGE) {

                            shareVideoOrGIF();
                        }

                    }
                });
    }


    @Override
    public void update(Observable observable, Object data) {

        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.REFRESH_IMAGE_CATEGORY_DATA) {
            Utility.Log("REFRESH_IMAGE_CATEGORY_DATA", "Yessssssss");
            getBrandList();
        }
        super.update(observable, data);
    }

    public void shareVideoOrGIF() {
        if (isUsingCustomFrame) {
            ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);
            binding.FrameImageDuplicate.setVisibility(View.GONE);
            binding.FrameImageDuplicate.setImageBitmap(null);
        } else {
            Glide.with(getApplicationContext()).load(selectedBackendFrame.getFrame1()).into(binding.backendFrame);
        }
        binding.recoImage.setVisibility(View.GONE);
        binding.videoView.setVisibility(View.VISIBLE);

        binding.videoView.requestFocus();
        binding.simpleProgressBar.setVisibility(View.VISIBLE);
        try {
            if (exoPlayer != null) {
                exoPlayer.stop();
                exoPlayer.release();
            }
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(act, trackSelector);
            dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(selectedObject.getVideoSet(), dataSourceFactory, extractorsFactory, null, null);
            binding.videoView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
            exoPlayer.addListener((Player.EventListener) act);
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
        } catch (Exception ignored) {
        }

        System.out.println("SHARED");
        if (isItGIF)
            shareGIF(finalOutputFile);
        else
            shareVideo(finalOutputFile);

        isItGIF = false;
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

                        int imageCounter = Integer.parseInt(ResponseHandler.getString(dataJson.getJSONObject(0), "total_img_counter").equalsIgnoreCase("Unlimited") ? "-1" : ResponseHandler.getString(dataJson.getJSONObject(0), "total_img_counter"));

                        int used_img_counter = ResponseHandler.getString(dataJson.getJSONObject(0), "frame_counter").equals("") ? 0 : Integer.parseInt(ResponseHandler.getString(dataJson.getJSONObject(0), "used_img_counter"));

                        if (ResponseHandler.getBool(dataJson.getJSONObject(0), "status")) {

                            if (Utility.isUserPaid(prefManager.getActiveBrand())) {

                                if (imageCounter == -1 || used_img_counter <= imageCounter) {
                                    if (flag.equalsIgnoreCase("Download"))
                                        askForDownloadImage();
                                    else {
                                        requestAgain();
                                       /* if (selectedObject.getImageType() == ImageList.IMAGE) {
                                            saveImageToGallery(false, true);
                                        } else {
                                            saveGif();
                                        }*/

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
                                   /* if (selectedObject.getImageType() == ImageList.IMAGE) {
                                        saveImageToGallery(false, true);
                                    } else {
                                        saveGif();
                                    }*/
                                    saveImageToGallery(true, false);
                                }
                            }

                        } else {
                            downloadLimitExpireDialog("You have already used one image for today, As you are free user you can download or share only one image in a day for a week.To get more images please upgrade your package");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id", prefManager.getActiveBrand().getId());
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
                .addHeaders("X-Authorization", "Bearer" + prefManager.getUserToken())
                .addMultipartParameter("brand_id", prefManager.getActiveBrand().getId())
                .setPriority(Priority.HIGH);
        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
        }
        request.build().setUploadProgressListener((bytesUploaded, totalBytes) -> {
            // do anything with progress
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
                    }
                });

    }


}
