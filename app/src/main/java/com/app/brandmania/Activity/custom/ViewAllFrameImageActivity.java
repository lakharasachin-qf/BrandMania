package com.app.brandmania.Activity.custom;

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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import com.app.brandmania.Activity.about_us.AppIntroActivity;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.Adapter.ViewAllTopCustomeFrameTabAdapter;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.FooterHelper;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.DataBase.DBManager;
import com.app.brandmania.DataBase.DatabaseHelper;
import com.app.brandmania.Fragment.AddBrandFragment;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Interface.AddTextEvent;
import com.app.brandmania.Interface.FilterListener;
import com.app.brandmania.Interface.FrameInterFace;
import com.app.brandmania.Interface.IBackendFrameSelect;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.IImageBritnessEvent;
import com.app.brandmania.Interface.IImageFromGalary;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.IRemoveFrame;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.ImageCateItemeInterFace;
import com.app.brandmania.Interface.IrotateEvent;
import com.app.brandmania.Interface.ItemeInterFace;
import com.app.brandmania.Interface.ThumbnailCallback;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Model.LayoutModelClass;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DialogUpgradeLayoutPackegeExpiredBindingImpl;
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
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.IFontChangeEvent;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityViewAllFrameImageBinding;
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
import java.util.Observable;

import ja.burhanrashid52.photoeditor.PhotoFilter;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static com.app.brandmania.Activity.details.ImageCategoryDetailActivity.DOWLOAD;
import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;
import static com.app.brandmania.Fragment.top.EditTab.setBrightness;


public class ViewAllFrameImageActivity extends BaseActivity implements FrameInterFace, alertListenerCallback, ItemeInterFace,
        ImageCateItemeInterFace, ITextColorChangeEvent, IFontChangeEvent, ITextBoldEvent, IItaliTextEvent, ColorPickerDialogListener,
        IColorChange, ColorPickerView.OnColorChangedListener, ITextSizeEvent, onFooterSelectListener, View.OnTouchListener,
        FilterListener, IImageBritnessEvent, IImageFromGalary, IRemoveFrame, IrotateEvent, ThumbnailCallback, IBackendFrameSelect, AddTextEvent {

    private ActivityViewAllFrameImageBinding binding;
    File new_file;
    GestureDetector gestureDetector;
    float angle = 0;
    private int _xDelta;
    private int _yDelta;
    ImageFromGalaryModel imageFromGalaryModel;
    Drawable yourDrawable;
    public int startwidth;
    private boolean isRemoveFrame = false;
    TextView selectedForEdit;
    Bitmap selectedImageBitmap = null;
    int startheight;
    private float d = 0f;
    float dx = 0, dy = 0, x = 0, y = 0;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;


    float oldDist = 1f;
    private float newRot = 0f;
    RelativeLayout.LayoutParams parms;
    private float mScaleFactor = 1.0f;
    ArrayList<ImageList> AddFavorite = new ArrayList<>();
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    private Bitmap selectedLogo;
    private boolean updateLogo = false;
    private Uri mCropImageUri;
    AlertDialog.Builder alertDialogBuilder;
    private String loadDefaultFont = "";
    private FooterModel selectedFooterModel;
    float scalediff;
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    int isDownloadOrSharingOrFavPending = -1;

    private ScaleGestureDetector scaleGestureDetector;

    private boolean isUsingCustomFrame = true;
    private ImageList selectedBackendFrame = null;
    private int previousFontSize = -1;
    int editorFragment;
    private boolean isLoadItalic = false;
    private boolean isLoadBold = false;
    ArrayList<ImageList> menuModels = new ArrayList<>();
    private int colorCodeForTextColor = 0;
    private ImageList selectedObject;

    int mode = NONE;
    private int colorCodeForBackground = 0;
    Gson gson;
    public DBManager dbManager;
    boolean canLoadImage = false;
    boolean fromViewAll = false;
    private float DefaultScaleX;
    private float DefaultScaleY;


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
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_all_frame_image);
        gson = new Gson();
        dbManager = new DBManager(act);
        selectedObject = gson.fromJson(getIntent().getStringExtra("selectedimage"), ImageList.class);

        setIconForAlignment();


        CreateTabs();

        DefaultScaleY = binding.editableImageview.getScaleY();
        DefaultScaleX = binding.editableImageview.getScaleX();

        binding.editableImageview.getLayoutParams().height = 150;
        binding.editableImageview.getLayoutParams().width = 150;
        binding.editableImageview.requestLayout();

        DashBoardItem imageList = gson.fromJson(getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
        binding.titleName.setText(imageList.getName());

        binding.backImage.setDrawingCacheEnabled(true);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());


        TouchImageMotion();
        GradientDrawable drawable = (GradientDrawable) binding.elementCustomFrame.getBackground();
        drawable.setStroke((int) convertDpToPx(0), colorCodeForBackground);


        if (prefManager.getActiveBrand() != null) {
            updateLogo = prefManager.getActiveBrand().getLogo().isEmpty();
        }


        colorCodeForBackground = ContextCompat.getColor(act, R.color.colorPrimary);
        // colorCodeForTextColor= ContextCompat.getColor(act,R.color.colorPrimary);
        binding.logoEmptyState.setOnTouchListener(onTouchListener());
        binding.logoCustom.setOnTouchListener(onTouchListener());
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (!prefManager.getAppTutorial().isEmpty()) {
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


        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (prefManager.getActiveBrand() != null) {
                    if (manuallyEnablePermission(1)) {

                        if (!Utility.isUserPaid(prefManager.getActiveBrand())) {

                            if (selectedObject.isImageFree()) {
                                if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
                                    askForUpgradeToEnterpisePackage();
                                    return;
                                }
                                requestAgain();
                                saveImageToGallery(true, false);
                                if (prefManager.getActiveBrand().getLogo().isEmpty() && selectedLogo != null) {
                                    uploadLogoForBrand(selectedLogo);
                                }
                            } else {
                                askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
                            }
                        } else {
                            if (!Utility.isPackageExpired(act)) {
                                requestAgain();
                                saveImageToGallery(true, false);
                                if (prefManager.getActiveBrand().getLogo().isEmpty() && selectedLogo != null) {
                                    uploadLogoForBrand(selectedLogo);
                                }
                            } else {
                                askForUpgradeToEnterpisePackaged();
                            }
                        }
                    }

                } else {
                    addBrandList();
                }
            }
        });
        LoadDataToUI();

        if (prefManager.getActiveBrand() != null) {
            if (prefManager.getActiveBrand().getLogo() != null && !prefManager.getActiveBrand().getLogo().isEmpty()) {
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
                binding.logoCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSelectImageClick(view);
                    }
                });
            }
        } else {
            binding.logoCustom.setVisibility(View.GONE);
            binding.logoEmptyState.setVisibility(View.GONE);
        }


        if (!getIntent().hasExtra("viewAll"))
            LoadDataToUI();

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
        binding.recommendation.setOnClickListener(new View.OnClickListener() {
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

    AddBrandFragment addBrandFragment;

    public void addBrandList() {
        if (addBrandFragment != null) {
            if (addBrandFragment.isVisible()) {
                addBrandFragment.dismiss();
            }
        }

        addBrandFragment = new AddBrandFragment();
        addBrandFragment.show(getSupportFragmentManager(), "");
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

    public void askForDownloadImage() {
        alertDialogBuilder = new AlertDialog.Builder(act);
        alertDialogBuilder.setTitle("Save image");
        alertDialogBuilder.setMessage("You sure to save your image?");
        alertDialogBuilder.setPositiveButton("yes",
                (arg0, arg1) -> {
                    requestAgain();
                    saveImageToGallery(false, false);
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

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.GALLERY_CALLBACK) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    screenExistDialog();

                }
            });
        }
    }

    public void CreateTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Post")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Footer")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Gallery")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Frames")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Background")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Text")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Edit")));
        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewAllTopCustomeFrameTabAdapter adapter = new ViewAllTopCustomeFrameTabAdapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());

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
                if (editorFragment == 2) {
                    canLoadImage = true;
                }
                if (needToIntro) {
                    if (tabIndex == 1) {
                        showTabIntro(binding.viewPager, "Footer", "if you want to custom frame then choose your own footer");
                    }

                    if (tabIndex == 2) {
                        showTabIntro(binding.viewPager, "Image", "Choose your image as you want");
                    }
                    if (tabIndex == 3) {
                        showTabIntro(binding.viewPager, "Frames", "Apply custom frame");
                    }

                    if (tabIndex == 4) {
                        showTabIntro(binding.viewPager, "Background", "Choose your background color as you want");
                    }
                    if (tabIndex == 5) {
                        showTabIntro(binding.viewPager, "Text", "Change your text and icon color as u want");

                    }
                    if (tabIndex == 6) {
                        showTabIntro(binding.viewPager, "Edit", "Change your image filter as u want");
                        needToIntro = false;
                    }


                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        if (prefManager.getViewAllFrameActivityIntro()) {
            needToIntro = true;

            if (binding.logoEmptyState.getVisibility() == View.VISIBLE)
                startIntro(binding.logoEmptyState, "Brand Logo", "Click on icon for choose your logo\n you can resize and move logo around anywhere in the image");
            else
                startIntro(binding.logoCustom, "Brand Logo", "Click your logo to move around anywhere in the image");

            prefManager.setViewAllFrameActivityIntro(false);

        } else {
            //showTabIntro(binding.viewPager.getChildAt(0), "Category", "Choose your image as you want");
        }


    }

    //load firstImage
    public void loadFirstImage() {

        FooterModel model = new FooterModel();
        model.setLayoutType(FooterModel.LAYOUT_FRAME_SEVEN);
        model.setFree(true);
        if (prefManager.getActiveBrand() != null) {
            model.setAddress(prefManager.getActiveBrand().getAddress());
            model.setEmailId(prefManager.getActiveBrand().getEmail());
            model.setContactNo(prefManager.getActiveBrand().getPhonenumber());
            model.setWebsite(prefManager.getActiveBrand().getWebsite());
        }
        ((onFooterSelectListener) act).onFooterSelectEvent(FooterModel.LAYOUT_FRAME_SEVEN, model);
    }

    //For CustomFrame
    public void onSelectImageClick(View view) {
        if (binding.logoCustom.getTag().toString().equalsIgnoreCase("1"))
            pickerView(true, selectedLogo);
        else
            pickerView(false, null);

        //CropImage.startPickImageActivity(this);
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

    @SuppressLint("ClickableViewAccessibility")
    public void LoadDataToUI() {

        prefManager = new PreafManager(act);
        if (selectedObject != null) {
            selectedObject.setTextX_Cordinate("100");
            selectedObject.setTextY_Cordinate("100");
            if (selectedObject.getX_conrdinate() == null || selectedObject.getX_conrdinate().equalsIgnoreCase("null") || selectedObject.getX_conrdinate().isEmpty()) {
                selectedObject.setX_conrdinate(String.valueOf("330"));
                selectedObject.setY_cordinate(String.valueOf("380"));
//                selectedObject.setTextX_Cordinate("100");
//                selectedObject.setTextY_Cordinate("100");
            }
            if (selectedObject.getIndex() == 15) {
                selectedObject.setX_conrdinate(String.valueOf("520"));
                selectedObject.setY_cordinate(String.valueOf("280"));
//                selectedObject.setTextX_Cordinate("100");
//                selectedObject.setTextY_Cordinate("260");
            }
            if (selectedObject.getIndex() == 14) {
                selectedObject.setX_conrdinate(String.valueOf("400"));
                selectedObject.setY_cordinate(String.valueOf("330"));
//                selectedObject.setTextX_Cordinate("220");
//                selectedObject.setTextY_Cordinate("280");
            }
            if (selectedObject.getIndex() == 13) {
                selectedObject.setX_conrdinate(String.valueOf("350"));
                selectedObject.setY_cordinate(String.valueOf("290"));
//                selectedObject.setTextX_Cordinate("230");
//                selectedObject.setTextY_Cordinate("280");
            }
            if (selectedObject.getIndex() == 12) {
                selectedObject.setX_conrdinate(String.valueOf("250"));
                selectedObject.setY_cordinate(String.valueOf("270"));
//                selectedObject.setTextX_Cordinate("50");
//                selectedObject.setTextY_Cordinate("250");
            }

            if (selectedObject.getIndex() == 11) {
                selectedObject.setX_conrdinate(String.valueOf("250"));
                selectedObject.setY_cordinate(String.valueOf("380"));
//                selectedObject.setTextX_Cordinate("250");
//                selectedObject.setTextY_Cordinate("250");

            }
            if (selectedObject.getIndex() == 10) {
                selectedObject.setX_conrdinate(String.valueOf("450"));
                selectedObject.setY_cordinate(String.valueOf("200"));
//                selectedObject.setTextX_Cordinate("50");
//                selectedObject.setTextY_Cordinate("250");
            }

            if (selectedObject.getIndex() == 9) {
//                selectedObject.setTextX_Cordinate("50");
//                selectedObject.setTextY_Cordinate("280");
            }

//            if (selectedObject.getIndex() == 7){
//                selectedObject.setTextX_Cordinate("50");
//                selectedObject.setTextY_Cordinate("250");
//            }


            binding.simpleProgressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.backImage);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, 250);
//            int WeidthRefDevice=1080;
//            int HeightRefDevice=2028;
            int dpValuex = Integer.parseInt(selectedObject.getX_conrdinate());
            int dpValuey = Integer.parseInt(selectedObject.getY_cordinate()); //margin in dips
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            binding.editableImageview.setOnTouchListener(null);
            Resources r = getResources();
            int marginx = Math.round(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dpValuex, r.getDisplayMetrics()));
            ;//(int)((dpValuex * width)/WeidthRefDevice);  // margin in pixels
            int marginy = Math.round(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dpValuey, r.getDisplayMetrics()));
            ;//(int)((dpValuey * height)/HeightRefDevice);  // margin in pixels

            layoutParams.leftMargin = marginx;
            layoutParams.topMargin = marginy;
            layoutParams.bottomMargin = -250;
            layoutParams.rightMargin = -250;


            layoutParams = new RelativeLayout.LayoutParams(200, 200);
            int WeidthRefDevice = 1080;
            int HeightRefDevice = 2028;
            dpValuex = Integer.parseInt(selectedObject.getX_conrdinate()); // margin in dips
            dpValuey = Integer.parseInt(selectedObject.getY_cordinate()); // margin in dips
            displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            marginx = (int) ((dpValuex * width) / WeidthRefDevice); // margin in pixels
            marginy = (int) ((dpValuey * height) / HeightRefDevice); // margin in pixels
            layoutParams.leftMargin = marginx;
            layoutParams.topMargin = marginy;
            layoutParams.bottomMargin = -250;
            layoutParams.rightMargin = -250;
            binding.editableImageview.setOnTouchListener(null);
            layoutParams.height = 200;
            layoutParams.width = 200;
            binding.editableImageview.setScaleX(DefaultScaleX);
            binding.editableImageview.setScaleY(DefaultScaleY);
            binding.editableImageview.setRotation(0);
            binding.editableImageview.setLayoutParams(layoutParams);
            binding.editableImageview.setVisibility(View.VISIBLE);
            binding.backImage.setVisibility(View.VISIBLE);
            selectedImageBitmap = drawableToBitmap(ContextCompat.getDrawable(act, R.drawable.ic_gallry));
            binding.editableImageview.setImageBitmap(selectedImageBitmap);


            binding.editableImageview.requestLayout();

            binding.editableImageview.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    binding.viewPager.setCurrentItem(2);
                    TouchImageMotion();
                    return false;
                }
            });


        } else {
            // binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }
        if (prefManager.getActiveBrand() != null) {
            if (selectedFooterModel == null)
                loadFirstImage();
        }
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
        int WeidthRefDevice = 1080;
        int HeightRefDevice = 2028;
        int dpValuex = Integer.parseInt(selectedObject.getX_conrdinate()); // margin in dips
        int dpValuey = Integer.parseInt(selectedObject.getY_cordinate()); // margin in dips
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int marginx = (int) ((dpValuex * width) / WeidthRefDevice); // margin in pixels
        int marginy = (int) ((dpValuey * height) / HeightRefDevice); // margin in pixels
        layoutParams.leftMargin = marginx;
        layoutParams.topMargin = marginy;
        layoutParams.bottomMargin = -250;
        layoutParams.rightMargin = -250;
        binding.editableImageview.setOnTouchListener(null);
        layoutParams.height = 200;
        layoutParams.width = 200;
        binding.editableImageview.setScaleX(DefaultScaleX);
        binding.editableImageview.setScaleY(DefaultScaleY);
        binding.editableImageview.setRotation(0);
        binding.editableImageview.setLayoutParams(layoutParams);
        binding.editableImageview.setVisibility(View.VISIBLE);
        binding.backImage.setVisibility(View.VISIBLE);
        selectedImageBitmap = drawableToBitmap(ContextCompat.getDrawable(act, R.drawable.ic_gallry));
        binding.editableImageview.setImageBitmap(selectedImageBitmap);
        binding.editableImageview.requestLayout();
        binding.editableImageview.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.viewPager.setCurrentItem(2);
                TouchImageMotion();
                return false;
            }
        });


        binding.simpleProgressBar.setVisibility(View.GONE);
        if (prefManager.getActiveBrand() != null) {
            if (selectedFooterModel == null)
                loadFirstImage();

            forCheckFavorite();
        }

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
        if (binding.textEditorView.getVisibility() == View.VISIBLE) {
            binding.textEditorView.setVisibility(View.GONE);
            binding.contentView.setEnabled(true);
            binding.contentView.setClickable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 1);
            return;
        }
        if (editorFragment == 2) {
            MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.GALLERY_ACTION);
        } else {
            screenExistDialog();
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {
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
//                    if (binding.fabroutIcon.getVisibility() == View.VISIBLE) {
//                        binding.fabroutIcon.setVisibility(View.GONE);
//                        binding.addfabroutIcon.setVisibility(View.VISIBLE);
//                    }

                    saveImageToGallery(false, true);
                }
                //for download
                if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (isDownloadOrSharingOrFavPending == 1) {
                        //  Toast.makeText(act, "fdggdgd", Toast.LENGTH_SHORT).show();
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

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Utility.Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


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

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    @Override
    public void onImageFromGalaryItemSelection(int position, ImageFromGalaryModel listModel) {
        if (canLoadImage) {
            try {
                imageFromGalaryModel = listModel;

                binding.editableImageview.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
                if (fromViewAll) {
                    int WeigthRefDevice = 1080;
                    int HeightRefDevice = 2028;
                    int dpValuex = Integer.parseInt(selectedObject.getX_conrdinate()); // margin in dips
                    int dpValuey = Integer.parseInt(selectedObject.getY_cordinate()); // margin in dips
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    int width = displayMetrics.widthPixels;
                    int marginx = (int) ((dpValuex * width) / WeigthRefDevice); // margin in pixels
                    int marginy = (int) ((dpValuey * height) / HeightRefDevice); // margin in pixel
                    layoutParams.leftMargin = marginx;
                    layoutParams.topMargin = marginy;

                } else {
                    fromViewAll = true;
                }
                layoutParams.bottomMargin = -250;
                layoutParams.rightMargin = -250;
                binding.editableImageview.setLayoutParams(layoutParams);
                TouchImageMotion();
                InputStream inputStream = getContentResolver().openInputStream(listModel.getUri());
                yourDrawable = Drawable.createFromStream(inputStream, listModel.getUri().toString());
                binding.editableImageview.setImageDrawable(yourDrawable);
                BitmapDrawable drawable = (BitmapDrawable) binding.editableImageview.getDrawable();
                selectedImageBitmap = drawable.getBitmap();
                binding.editableImageview.requestFocus();
            } catch (FileNotFoundException e) {

            }
        } else {
            binding.editableImageview.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
            if (fromViewAll) {
                int WeigthRefDevice = 1080;
                int HeightRefDevice = 2028;
                int dpValuex = Integer.parseInt(selectedObject.getX_conrdinate()); // margin in dips
                int dpValuey = Integer.parseInt(selectedObject.getY_cordinate()); // margin in dips
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                int marginx = (int) ((dpValuex * width) / WeigthRefDevice); // margin in pixels
                int marginy = (int) ((dpValuey * height) / HeightRefDevice); // margin in pixels
                layoutParams.leftMargin = marginx;
                layoutParams.topMargin = marginy;
                binding.editableImageview.setLayoutParams(layoutParams);
                binding.editableImageview.setVisibility(View.VISIBLE);
                binding.backImage.setVisibility(View.VISIBLE);
                //selectedImageBitmap=drawableToBitmap(ContextCompat.getDrawable(act,R.drawable.ic_gallry));
                binding.editableImageview.setImageBitmap(selectedImageBitmap);
                binding.editableImageview.requestFocus();

            } else {
                fromViewAll = true;
            }


            binding.editableImageview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    binding.viewPager.setCurrentItem(2);
                    TouchImageMotion();
                    return false;
                }
            });

        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
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

    androidx.appcompat.app.AlertDialog alertDialog;
    DialogDiscardImageBinding discardImageBinding;

    public void screenExistDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        if (act != null) {
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

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        view.setBackground(null);

        //return the bitmap
        return returnedBitmap;
    }

    public void saveImageToGallery(boolean wantToShare, boolean isFavourite) {
        for (int i = 0; i < binding.CustomImageMain.getChildCount(); i++) {
            if (binding.CustomImageMain.getChildAt(i) instanceof TextView) {
                TextView textView = (TextView) binding.CustomImageMain.getChildAt(i);
                textView.setBackground(null);
            }
        }

        Utility.showLoadingTran(act);
        Drawable bitmapFrame;
        if (isUsingCustomFrame) {
            bitmapFrame = new BitmapDrawable(getResources(), getBitmapFromView(binding.CustomImageMain));
        } else {
            bitmapFrame = new BitmapDrawable(getResources(), FooterHelper.getCustomFrameInBitmap1(binding.CustomImageMain, binding.editableImageview, binding.backendFrame));
        }
        Drawable ImageDrawable = (BitmapDrawable) binding.editableImageview.getDrawable();
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
        String name = "image" + System.currentTimeMillis() + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        new_file = new File(file_name);

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
        Utility.dismissLoadingTran();
        FooterHelper.refreshgallery(act, new_file);


        if (!isFavourite) {
            if (wantToShare) {
                if (prefManager.getActiveBrand() != null) {
                    if (isUsingCustomFrame) {
                        if (!isRemoveFrame) {
                            ((onFooterSelectListener) act).onFooterSelectEvent(selectedFooterModel.getLayoutType(), selectedFooterModel);
                            binding.FrameImageDuplicate.setVisibility(View.GONE);
                            binding.FrameImageDuplicate.setImageBitmap(null);
                        }


                    }
                }
         //       FooterHelper.triggerShareIntent(act, new_file, merged);
            }
            dbManager.insertStaticContent(new_file.toString(), DatabaseHelper.FLAG_DOWNLOAD);
            InputStream inputStream = null;
            try {

                if (imageFromGalaryModel != null) {
                    inputStream = getContentResolver().openInputStream(imageFromGalaryModel.getUri());
                    yourDrawable = Drawable.createFromStream(inputStream, imageFromGalaryModel.getUri().toString());
                } else {
                    yourDrawable = ContextCompat.getDrawable(act, R.drawable.photo);
                }

                binding.editableImageview.setImageDrawable(yourDrawable);
                BitmapDrawable drawable = (BitmapDrawable) binding.editableImageview.getDrawable();
                selectedImageBitmap = drawable.getBitmap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            downloadAndShareApi(DOWLOAD, merged);
        }

    }

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
                .addHeaders("Authorization", "Bearer " + prefManager.getUserToken())
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
                        Utility.dismissLoadingTran();
                        Utility.Log("DOWNLOAD_SHARE : ", response);
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
                            FooterHelper.triggerShareIntent(act, new_file, customImage);
                            CodeReUse.activityBackPress(act);

                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        Utility.dismissLoadingTran();

                    }
                });
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
        this.footerLayout = footerLayout;
        selectedFooterModel = footerModel;
        addDynamicFooter(footerLayout, false);
        forCheckFavorite();

        changeBorderColorAsFrame();
        loadSameColorToBackgroundAndTextAgain();
        ((ITextSizeEvent) act).onfontSize(previousFontSize);
    }

    //check for added to fav or not
    public void forCheckFavorite() {
        prefManager = new PreafManager(act);
        AddFavorite = prefManager.getSavedFavorites();
        if (AddFavorite != null) {
            boolean isImageFound = false;
            for (int i = 0; i < AddFavorite.size(); i++) {
                if (prefManager.getActiveBrand().getId().equalsIgnoreCase(AddFavorite.get(i).getBrandId())) {
                    if (isUsingCustomFrame) {
                        if (AddFavorite.get(i).isCustom()) {
                            if (AddFavorite.get(i).getId().equals(selectedObject.getId())) {
//                                binding.addfabroutIcon.setVisibility(View.VISIBLE);
//                                binding.fabroutIcon.setVisibility(View.GONE);
                                isImageFound = true;
                                break;
                            } else {
//                                binding.addfabroutIcon.setVisibility(View.GONE);
//                                binding.fabroutIcon.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {
                        if (!AddFavorite.get(i).isCustom()) {
                            if (AddFavorite.get(i).getId().equals(selectedObject.getId()) && AddFavorite.get(i).getFrame1Id().equalsIgnoreCase(selectedBackendFrame.getFrame1Id())) {
//                                binding.addfabroutIcon.setVisibility(View.VISIBLE);
//                                binding.fabroutIcon.setVisibility(View.GONE);
                                isImageFound = true;
                                break;
                            } else {
//                                binding.addfabroutIcon.setVisibility(View.GONE);
//                                binding.fabroutIcon.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }
            if (!isImageFound) {
//                binding.addfabroutIcon.setVisibility(View.GONE);
//                binding.fabroutIcon.setVisibility(View.VISIBLE);
            }
        }
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
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFirstData(act, oneBinding);
            layoutModelClass.setOneBinding(oneBinding);
            binding.elementFooter.addView(oneBinding.getRoot());

        } else if (layoutType == FooterModel.LAYOUT_FRAME_TWO) {
            LayoutForLoadTwoBinding twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            layoutModelClass.setTwoBinding(twoBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameTwoData(act, twoBinding);
            binding.elementFooter.addView(twoBinding.getRoot());

        } else if (layoutType == FooterModel.LAYOUT_FRAME_THREE) {
            LayoutForLoadThreeBinding threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());
            layoutModelClass.setThreeBinding(threeBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameThreeData(act, threeBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FOUR) {
            LayoutForLoadFourBinding fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_four, null, false);
            binding.elementFooter.addView(fourBinding.getRoot());
            layoutModelClass.setFourBinding(fourBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFourData(act, fourBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FIVE) {
            LayoutForLoadFiveBinding fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_five, null, false);
            binding.elementFooter.addView(fiveBinding.getRoot());
            layoutModelClass.setFiveBinding(fiveBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameFiveData(act, fiveBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SIX) {
            LayoutForLoadSixBinding sixBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_six, null, false);
            binding.elementFooter.addView(sixBinding.getRoot());
            layoutModelClass.setSixBinding(sixBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameSixData(act, sixBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SEVEN) {
            LayoutForLoadSevenBinding sevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_seven, null, false);
            binding.elementFooter.addView(sevenBinding.getRoot());
            layoutModelClass.setSevenBinding(sevenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameSevenData(act, sevenBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_EIGHT) {
            LayoutForLoadEightBinding eightBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_eight, null, false);
            binding.elementFooter.addView(eightBinding.getRoot());
            layoutModelClass.setEightBinding(eightBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameEightData(act, eightBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_NINE) {
            LayoutForLoadNineBinding nineBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_nine, null, false);
            binding.elementFooter.addView(nineBinding.getRoot());
            layoutModelClass.setNineBinding(nineBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameNineData(act, nineBinding);
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TEN) {
            LayoutForLoadTenBinding tenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_ten, null, false);
            binding.elementFooter.addView(tenBinding.getRoot());
            layoutModelClass.setTenBinding(tenBinding);
            if (prefManager.getActiveBrand() != null)
                FooterHelper.loadFrameTenData(act, tenBinding);
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

        if (editorFragment == 5 && selectedForEdit != null) {
            selectedForEdit.setTextColor(colorCode);

        } else if (editorFragment == 5) {
            colorCodeForTextColor = colorCode;
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
        // Toast.makeText(act, editorFragment+"dfgdfgf", Toast.LENGTH_SHORT).show();
        if (editorFragment == 4) {
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

        if (editorFragment == 5 && selectedForEdit != null) {
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            selectedForEdit.setTypeface(custom_font);
            // selectedForEdit.setTextColor(colorCode);
        } else {
            FooterHelper.baseForFontChange(act, footerLayout, Font, layoutModelClass);
        }
    }

    //for underline
    //for font size
    @Override
    public void onfontSize(int textsize) {

        if (editorFragment == 5 && selectedForEdit != null) {
            selectedForEdit.setTextSize(textsize);
        } else {
            FooterHelper.baseForTextSize(textsize, footerLayout, layoutModelClass);
        }

    }

    //for bold text
    @Override
    public void onBoldTextChange(boolean Bold) {
        if (Bold) {

            isLoadBold = Bold;
            if (editorFragment == 5 && selectedForEdit != null) {
                Utility.setBold(selectedForEdit, true);

            } else {
                FooterHelper.baseForBold(Bold, footerLayout, layoutModelClass);
            }

        } else {

            if (editorFragment == 5 && selectedForEdit != null) {
                Utility.setBold(selectedForEdit, false);

            } else {
                FooterHelper.baseForBold(Bold, footerLayout, layoutModelClass);
            }

        }

    }

    //for italic
    @Override
    public void onItalicTextChange(boolean Italic) {
        isLoadItalic = Italic;
        if (Italic) {
            if (editorFragment == 5 && selectedForEdit != null) {

                Utility.setItalicText(selectedForEdit, true);
            } else {
                FooterHelper.baseForItalic(Italic, footerLayout, layoutModelClass);
            }

        } else {
            if (editorFragment == 5 && selectedForEdit != null) {

                Utility.setItalicText(selectedForEdit, false);
            } else {
                FooterHelper.baseForItalic(Italic, footerLayout, layoutModelClass);
            }
        }
    }

    @Override
    public void onRemoveSelectEvent() {
        isUsingCustomFrame = true;
        isRemoveFrame = true;
        //Toast.makeText(act, "dsgfgds", Toast.LENGTH_SHORT).show();
        binding.elementCustomFrame.setVisibility(View.GONE);
        binding.backendFrame.setImageBitmap(null);
        binding.FrameImageDuplicate.setImageBitmap(null);

        //    binding.frameImage.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        //mPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onFrameItemSelection(int position, MultiListItem listModel) {

    }

    @Override
    public void onItemSelection(int position, MultiListItem listModel) {
        binding.backendFrame.setImageResource(listModel.getImage());
    }

    @Override
    public void onThumbnailClick(Filter filter) {
        int width = selectedImageBitmap.getWidth();
        int height = selectedImageBitmap.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / binding.editableImageview.getWidth();
            width = binding.editableImageview.getWidth();
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / binding.editableImageview.getHeight();
            height = binding.editableImageview.getHeight();
            width = (int) (width / ratio);
        } else {
            // square
            height = binding.editableImageview.getHeight();
            width = binding.editableImageview.getWidth();
        }


        binding.editableImageview.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(selectedImageBitmap, width, height, false)));
    }

    @Override
    public void onimageBritness(int britness) {
        binding.editableImageview.setColorFilter(setBrightness(britness));
    }

    @Override
    public void onRotateImage(int rotate) {
        binding.editableImageview.setRotation(binding.editableImageview.getRotation() + 90);
    }

    @Override
    public void onCropImage() {
    }


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
                        int imageCounter = Integer.parseInt(ResponseHandler.getString(dataJson.getJSONObject(0), "total_img_counter").equalsIgnoreCase("Unlimited") ? "-1" : ResponseHandler.getString(dataJson.getJSONObject(0), "total_img_counter"));

                        int used_img_counter = ResponseHandler.getString(dataJson.getJSONObject(0), "frame_counter").equals("") ? 0 : Integer.parseInt(ResponseHandler.getString(dataJson.getJSONObject(0), "used_img_counter"));


                        if (ResponseHandler.getBool(dataJson.getJSONObject(0), "status")) {

                            if (Utility.isUserPaid(prefManager.getActiveBrand())) {

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

                            downloadLimitExpireDialog("You have already used one image for today, As you are free user you can download or share only one image in a day for 7 days. To get more images please upgrade your package");
                            //Toast.makeText(act, "You can't download image bcoz your limit get expire for one day", Toast.LENGTH_SHORT).show();
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
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Authorization", "Bearer" + prefManager.getUserToken());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (prefManager.getActiveBrand() != null) {
                    params.put("brand_id", prefManager.getActiveBrand().getId());
                }
                Utility.Log("Params", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        if (prefManager.getActiveBrand() != null) {
            queue.add(stringRequest);
        }
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

                        Utility.Log("Logo Uploaded", response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.dismissLoadingTran();

                    }
                });

    }

    private RelativeLayout.LayoutParams imgParams;
    private float imgDX, imgDY;
    private float imgX, imgY;
    private int imgMode;
    private float imgNewRot = 0f;

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
                                    if (prefManager.getActiveBrand() != null) {
                                        if (prefManager.getActiveBrand().getLogo().isEmpty()) {
                                            onSelectImageClick(view);
                                        } else {
                                            if (!prefManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
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
                                        addBrandList();
                                    }
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }
                if (gestureDetector.onTouchEvent(event)) {
                } else {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            imgParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                            imgDX = event.getRawX() - imgParams.leftMargin;
                            imgDY = event.getRawY() - imgParams.topMargin;
                            imgMode = DRAG;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            oldDist = spacing(event);
                            if (oldDist > 10f) {
                                imgMode = ZOOM;
                            }
                            d = rotation(event);
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            imgMode = NONE;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (imgMode == DRAG) {
                                imgX = event.getRawX();
                                imgY = event.getRawY();
                                imgParams.leftMargin = (int) (imgX - imgDX);
                                imgParams.topMargin = (int) (imgY - imgDY);
                                imgParams.rightMargin = 0;
                                imgParams.bottomMargin = 0;
                                imgParams.rightMargin = imgParams.leftMargin + (5 * imgParams.width);
                                imgParams.bottomMargin = imgParams.topMargin + (10 * imgParams.height);
                                view.setLayoutParams(imgParams);
                            } else if (imgMode == ZOOM) {
                                if (event.getPointerCount() == 2) {
                                    imgNewRot = rotation(event);
                                    float r = imgNewRot - d;
                                    angle = r;
                                    imgX = event.getRawX();
                                    imgY = event.getRawY();
                                    float newDist = spacing(event);
                                    if (newDist > 10f) {
                                        float scale = newDist / oldDist * view.getScaleX();
                                        if (scale > 0.6) {
                                            scalediff = scale;
                                            view.setScaleX(scale);
                                            view.setScaleY(scale);
                                        }
                                    }
                                    view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();
                                    imgX = event.getRawX();
                                    imgY = event.getRawY();
                                    imgParams.leftMargin = (int) ((imgX - imgDX) + scalediff);
                                    imgParams.topMargin = (int) ((imgY - imgDY) + scalediff);
                                    imgParams.rightMargin = 0;
                                    imgParams.bottomMargin = 0;
                                    imgParams.rightMargin = imgParams.leftMargin + (5 * imgParams.width);
                                    imgParams.bottomMargin = imgParams.topMargin + (10 * imgParams.height);
                                    view.setLayoutParams(imgParams);
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

    int editMode;

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
                    imm.hideSoftInputFromWindow(binding.rootBackground.getWindowToken(), 0);
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

    public void TouchImageMotion() {
        binding.editableImageview.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
    }


}
