package com.app.brandmania.Activity.details;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.app.brandmania.Activity.about_us.AppIntroActivity;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Adapter.ViewAllTopTabAdapter;
import com.app.brandmania.Common.FooterHelper;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.DataBase.DBManager;
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
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
 
import com.app.brandmania.utils.IFontChangeEvent;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityViewGifDetailsBinding;
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
import com.app.brandmania.gifHelper.AnimatedGifEncoder;
import com.app.brandmania.gifHelper.GifDataDownloader;
import com.app.brandmania.gifHelper.GifImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
/*import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;*/
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.madhavanmalolan.ffmpegandroidlibrary.Controller;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
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


public class GifCategoryDetailActivity extends BaseActivity implements ImageCateItemeInterFace, alertListenerCallback, ITextColorChangeEvent, IFontChangeEvent, ITextBoldEvent,
        IItaliTextEvent, ColorPickerDialogListener, IColorChange, ColorPickerView.OnColorChangedListener,
        ITextSizeEvent, onFooterSelectListener, IBackendFrameSelect {
    Activity act;
    ViewPager viewPager;
    private boolean isLoading = false;
    ArrayList<ImageList> AddFavorite = new ArrayList<>();
    private ActivityViewGifDetailsBinding binding;
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
    //Version 3
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


    ArrayList<Bitmap> bitmaps;
    boolean canDownloadGIF = true;
   /* FFmpeg ffmpeg;
    public  void test(){
        ffmpeg = FFmpeg.getInstance(act);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
            // Handle if FFmpeg is not supported by device
        }
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
    //  test();
        //triggerUpgradePackage(); taflon tape
        //ffmpeg -i animated.gif -movflags faststart -pix_fmt yuv420p -vf "scale=trunc(iw/2)*2:trunc(ih/2)*2" video.mp4
        act.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_gif_details);
        preafManager = new PreafManager(this);
        binding.titleName.setSelected(true);
        gson = new Gson();
        selectedObject = gson.fromJson(getIntent().getStringExtra("selectedimage"), ImageList.class);
        getFrame();
        getBrandList();

//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, 10000);
//
//        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, 10000);

        Website = preafManager.getActiveBrand().getWebsite();
        imageList = gson.fromJson(getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
        binding.titleName.setText(imageList.getName());
        // getAllImages();
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
//                if (manuallyEnablePermission(2)) {
//
//                    if (!Utility.isUserPaid(preafManager.getActiveBrand())) {
//                        if (selectedObject.isImageFree()) {
//                            if (isUsingCustomFrame && selectedFooterModel != null && !selectedFooterModel.isFree()) {
//                                askForUpgradeToEnterpisePackage();
//                                return;
//                            }
//                            getImageDownloadRights("Share");
//                        } else {
//                            askForPayTheirPayment("You have selected premium design. To use this design please upgrade your package");
//                        }
//                    } else {
//                        getImageDownloadRights("Share");
//                    }
//
//
//                }

                saveGif();
            }
        });
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

            binding.logoEmptyState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectImageClick(view);
                }
            });
        }
        if (!getIntent().hasExtra("viewAll"))
            LoadDataToUI();

        new GifDataDownloader() {
            @Override
            protected void onPostExecute(final byte[] bytes) {
                binding.gifImageView.setBytes(bytes);
                binding.gifImageView.startAnimation();
                Log.e("TAG", "GIF width is " + binding.gifImageView.getGifWidth());
                Log.e("TAG", "GIF height is " + binding.gifImageView.getGifHeight());
            }
        }.execute("https://c.tenor.com/5VmvaWqS3sUAAAAC/happy-janmashtami.gif");

        //https://st1.latestly.com/wp-content/uploads/2021/08/Happy-Janmashtami-2021_5.gif
        //https://c.tenor.com/5VmvaWqS3sUAAAAC/happy-janmashtami.gif

        bitmaps = new ArrayList<>();

        //videoView
        /*Uri video = Uri.parse("https://www.shutterstock.com/video/clip-3869249-collection-nature-theme-short-clips");
        // Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");
        Uri video = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(binding.videoImageView);
        File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
        File file = null;
        file = new File(SDCardRoot, "sachin.mp4");
        Uri uri = Uri.fromFile(file);
        binding.videoImageView.setMediaController(mediaController);
        binding.videoImageView.setVideoURI(uri);
        binding.videoImageView.requestFocus();
        binding.videoImageView.start();*/

        //GifView


       binding.gifImageView.setOnFrameAvailable(new GifImageView.OnFrameAvailable() {
            @Override
            public Bitmap onFrameAvailable(Bitmap bitmap) {
                if (bitmaps.size() != binding.gifImageView.getFrameCount() && !bitmaps.contains(bitmap)) {
                    bitmaps.add(bitmap);
                } else {

                    if (canDownloadGIF) {
                        Log.e("canDownload", "canDownload");

                        canDownloadGIF = false;
                        Log.e("SizeFrame", String.valueOf(bitmaps.size()));
                    }

                }

                return bitmap;
            }
        });
    }


    private ArrayList<Bitmap> resultedGIFArray = new ArrayList<>();

    public byte[] generateGIF() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        encoder.setDelay(100);
        encoder.setRepeat(0);
        encoder.setQuality(100);
       // encoder.setSize(480,480);
        Log.e("size","height"+binding.gifImageView.getFramesDisplayDuration());

        for (Bitmap bitmap : bitmaps) {
            encoder.addFrame(manipulateGIF(bitmap, false));
          //  encoder.addFrame(bitmap);
        }
        encoder.finish();
        return bos.toByteArray();
    }

    public void saveGif() {

        try {
            Toast.makeText(act,"Image Download Start",Toast.LENGTH_LONG).show();
            File pictureFile = getOutputMediaFile();
            FileOutputStream outStream = new FileOutputStream(pictureFile);
            outStream.write(generateGIF());
            outStream.close();
            Log.e("GIF", "Saved"+pictureFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Drawable bitmapFrame;
    Bitmap merged;

    public Bitmap manipulateGIF(Bitmap gifFrameBitmap, boolean isFavourite) {
                if (isUsingCustomFrame) {
                    bitmapFrame = new BitmapDrawable(getResources(), getCustomFrameInBitmap(isFavourite));
                } else {
                    bitmapFrame = (BitmapDrawable) binding.backendFrame.getDrawable();
                }
                binding.recoImage.setImageBitmap(gifFrameBitmap);
                Drawable ImageDrawable = (BitmapDrawable) binding.recoImage.getDrawable();

                merged = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(merged);
                bitmapFrame.setBounds(0, 0, 450, 450);
                ImageDrawable.setBounds(0, 0, 450, 450);
                ImageDrawable.draw(canvas);
                bitmapFrame.draw(canvas);
                resultedGIFArray.add(merged);
        return merged;
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

        } else {
            //showTabIntro(binding.viewPager.getChildAt(0), "Category", "Choose your image as you want");
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
        CropImage.startPickImageActivity(this);
    }

    public void LoadDataToUI() {
        preafManager = new PreafManager(act);
        if (selectedObject != null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load("https://media.giphy.com/media/KwJFyQZZbPHzy/giphy.gif").into(binding.recoImage);
        } else {
            // binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }

        if (selectedFooterModel == null)
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
    private static final String root = Environment.getExternalStorageDirectory().toString();
    private static final String app_folder = root + "/GFG/";
    private Runnable r;
    int duration;
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
        if (requestCode == 10000){
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
               String filemanagerstring = selectedImageUri.getPath();
                Log.e("filemanagerstring",filemanagerstring);

                // MEDIA GALLERY
                String   selectedImagePath = getPath(selectedImageUri);
                if (selectedImagePath != null) {

                    Log.e("selectedImagePath",selectedImagePath);
                }
// now set the video uri in the VideoView
                binding.videoImageView.setVideoURI(selectedImageUri);

                // after successful retrieval of the video and properly
                // setting up the retried video uri in
                // VideoView, Start the VideoView to play that video
                binding.videoImageView.start();

                binding.videoImageView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // get the duration of the video
                        duration = mp.getDuration() / 1000;
                        Log.e("Video is loaded","yrdd");
                        Toast.makeText(act, "Now Start Converting", Toast.LENGTH_SHORT).show();
                       // choding(selectedImageUri);
                    }
                });



            }
        }
    }
   /* public void choding(Uri selectedImageUri){
        File video_file = null;
        try {
            video_file= FileUtilsDemo.getFileFromUri(this, selectedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String video_url = video_file.getAbsolutePath();
        // creating a new file in storage
        final String filePath;
        String filePrefix = "fastforward";
        String fileExtn = ".mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // With introduction of scoped storage in Android Q the primitive method gives error
            // So, it is recommended to use the below method to create a video file in storage.
            ContentValues valuesvideos = new ContentValues();
            valuesvideos.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "BRAND");
            valuesvideos.put(MediaStore.Video.Media.TITLE, filePrefix + System.currentTimeMillis());
            valuesvideos.put(MediaStore.Video.Media.DISPLAY_NAME, filePrefix + System.currentTimeMillis() + fileExtn);
            valuesvideos.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            valuesvideos.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            valuesvideos.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            Uri uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, valuesvideos);

            // get the path of the video file created in the storage.
            File file = null;
            try {
                file = FileUtilsDemo.getFileFromUri(this, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            filePath = file.getAbsolutePath();

        } else {
            // This else statement will work for devices with Android version lower than 10
            // Here, "app_folder" is the path to your app's root directory in device storage
            File dest = new File(new File(app_folder), filePrefix + fileExtn);
            int fileNo = 0;
            // check if the file name previously exist. Since we don't want
            // to overwrite the video files
            while (dest.exists()) {
                fileNo++;
                dest = new File(new File(app_folder), filePrefix + fileNo + fileExtn);
            }
            // Get the filePath once the file is successfully created.
            filePath = dest.getAbsolutePath();
        }
        String[] exe = new String[]{"-y -i " + video_url + " -filter_complex [0:v]trim=0:" + 0 + ",setpts=PTS-STARTPTS[v1];[0:v]trim=" + duration + ":" + duration  + ",setpts=0.5*(PTS-STARTPTS)[v2];[0:v]trim=" + (duration) + ",setpts=PTS-STARTPTS[v3];[0:a]atrim=0:" + (0) + ",asetpts=PTS-STARTPTS[a1];[0:a]atrim=" + (0) + ":" + (duration) + ",asetpts=PTS-STARTPTS,atempo=2[a2];[0:a]atrim=" + (duration) + ",asetpts=PTS-STARTPTS[a3];[v1][a1][v2][a2][v3][a3]concat=n=3:v=1:a=1 " + "-b:v 2097k -vcodec mpeg4 -crf 0 -preset superfast " + filePath};
        // the "exe" string contains the command to process video.The details of command are discussed later in this post.
        // "video_url" is the url of video which you want to edit. You can get this url from intent by selecting any video from gallery.
        execComd(exe);
    }*/
   /* private void execComd(String [] cmd){
        try {
            //ffmpeg -y -i input_file.mp4 -vcodec copy -an output_file.mp4
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.e("On start","ye");
                }

                @Override
                public void onProgress(String message) {
                    Log.e("On progress",message);

                }

                @Override
                public void onFailure(String message) {
                    Log.e("On failur",message);

                }

                @Override
                public void onSuccess(String message) {
                    Log.e("On success",message);
                }

                @Override
                public void onFinish() {
                    Log.e("On finish","yes");

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            e.printStackTrace();
        }
    }*/
    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
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

    //fire intent for share
    public void triggerShareIntent(File new_file, Bitmap merged) {
        //  Uri uri = Uri.parse();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(act, merged));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
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
                    if ((preafManager.getActiveBrand().getLogo().isEmpty() && selectedLogo != null) || preafManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
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


    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Christmas");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs())
                return null;
        }

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_MERRY_CHRISTMAS_" + Calendar.getInstance().getTimeInMillis() + ".gif");
        return mediaFile;
    }

    //save image with frame either custome or from backend
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

    private void addDynamicFooter(int layoutType, boolean isReload) {
        binding.elementFooter.removeAllViews();
        footerLayout = layoutType;
        if (layoutType == FooterModel.LAYOUT_FRAME_ONE) {
            oneBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_one, null, false);
            binding.elementFooter.addView(oneBinding.getRoot());
            FooterHelper.loadFrameFirstData(act, oneBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.main);
            mainLayout1 = (RelativeLayout) findViewById(R.id.addressLayoutElement2);
//            oneBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            oneBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            oneBinding.addressLayoutElement.setOnTouchListener(onTouchListenerrr());
        } else if (layoutType == FooterModel.LAYOUT_FRAME_TWO) {
            twoBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_two, null, false);
            binding.elementFooter.addView(twoBinding.getRoot());

            FooterHelper.loadFrameTwoData(act, twoBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.firstView);
            mainLayout1 = (RelativeLayout) findViewById(R.id.secondView);
//            twoBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            twoBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            twoBinding.locationLayout.setOnTouchListener(onTouchListenerrr());
//            twoBinding.websiteLayout.setOnTouchListener(onTouchListenerrr());


        } else if (layoutType == FooterModel.LAYOUT_FRAME_THREE) {
            threeBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_three, null, false);
            binding.elementFooter.addView(threeBinding.getRoot());

            FooterHelper.loadFrameThreeData(act, threeBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.section1);
            mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
//            threeBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            threeBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            threeBinding.loactionLayout.setOnTouchListener(onTouchListenerrr());
//            threeBinding.websiteEdtLayout.setOnTouchListener(onTouchListenerrr());
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FOUR) {
            fourBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_four, null, false);
            binding.elementFooter.addView(fourBinding.getRoot());

            FooterHelper.loadFrameFourData(act, fourBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.section1);
            //   mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
//            fourBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
//            fourBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
//            fourBinding.locationLayout.setOnTouchListener(onTouchListenerrr());
//            fourBinding.websiteLayout.setOnTouchListener(onTouchListenerrr());
        } else if (layoutType == FooterModel.LAYOUT_FRAME_FIVE) {
            fiveBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_five, null, false);
            binding.elementFooter.addView(fiveBinding.getRoot());

            FooterHelper.loadFrameFiveData(act, fiveBinding);

            mainLayout = (RelativeLayout) findViewById(R.id.main);
            //  mainLayout1 = (RelativeLayout) findViewById(R.id.element2);
            //   mainLayout1 = (RelativeLayout) findViewById(R.id.section2);
//            fiveBinding.element0.setOnTouchListener(onTouchListenerrr());
//            fiveBinding.elementMobile.setOnTouchListener(onTouchListenerrr());
//            fiveBinding.elementEmail.setOnTouchListener(onTouchListenerrr());
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SIX) {
            sixBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_six, null, false);
            binding.elementFooter.addView(sixBinding.getRoot());
            FooterHelper.loadFrameSixData(act, sixBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.containerElement);
//            sixBinding.socialFollow.setOnTouchListener(onTouchListenerrr());
//            sixBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
        } else if (layoutType == FooterModel.LAYOUT_FRAME_SEVEN) {
            sevenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_seven, null, false);
            binding.elementFooter.addView(sevenBinding.getRoot());
            FooterHelper.loadFrameSevenData(act, sevenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element0);
            // sevenBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
            mainLayout1 = (RelativeLayout) findViewById(R.id.socialFollow);
            // sevenBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
            //sevenBinding.socialLayout.setOnTouchListener(onTouchListenerrr());

        } else if (layoutType == FooterModel.LAYOUT_FRAME_EIGHT) {
            eightBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_eight, null, false);
            binding.elementFooter.addView(eightBinding.getRoot());
            FooterHelper.loadFrameEightData(act, eightBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.element1);
            //eightBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
            //eightBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
            mainLayout1 = (RelativeLayout) findViewById(R.id.element2);
            //eightBinding.addressLayoutElement.setOnTouchListener(onTouchListenerrr());
        } else if (layoutType == FooterModel.LAYOUT_FRAME_NINE) {
            nineBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_nine, null, false);
            binding.elementFooter.addView(nineBinding.getRoot());
            FooterHelper.loadFrameNineData(act, nineBinding);
            //   mainLayout = (RelativeLayout) findViewById(R.id.firstLayout);
//            nineBinding.gmailText.setOnTouchListener(onTouchListenerrr());
//            nineBinding.contactText.setOnTouchListener(onTouchListenerrr());
//            nineBinding.soialLayout.setOnTouchListener(onTouchListenerrr());

        } else if (layoutType == FooterModel.LAYOUT_FRAME_TEN) {
            tenBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.layout_for_load_ten, null, false);
            binding.elementFooter.addView(tenBinding.getRoot());
            FooterHelper.loadFrameTenData(act, tenBinding);
            mainLayout = (RelativeLayout) findViewById(R.id.addressLayout);
            mainLayout1 = (RelativeLayout) findViewById(R.id.layout);
            //  tenBinding.addressEdtLayout.setOnTouchListener(onTouchListenerrr());
            //tenBinding.gmailLayout.setOnTouchListener(onTouchListenerrr());
            //tenBinding.contactLayout.setOnTouchListener(onTouchListenerrr());
        }
    }

    @Override
    public void onColorSelected(int dialogId, int colorCode) {

    }

    public void loadSameColorToBackgroundAndTextAgain() {

        if (colorCodeForTextColor != 0) {
            if (footerLayout == 1) {
                FooterHelper.ChangeTextColorForFrameOne(act, oneBinding, colorCodeForTextColor);
            } else if (footerLayout == 2) {
                FooterHelper.ChangeTextColorForFrameTwo(act, twoBinding, colorCodeForTextColor);
            } else if (footerLayout == 3) {
                FooterHelper.ChangeTextColorForFrameThree(act, threeBinding, colorCodeForTextColor);
            } else if (footerLayout == 4) {
                FooterHelper.ChangeTextColorForFrameFour(act, fourBinding, colorCodeForTextColor);
            } else if (footerLayout == 5) {
                FooterHelper.ChangeTextColorForFrameFive(act, fiveBinding, colorCodeForTextColor);
            } else if (footerLayout == 6) {
                FooterHelper.ChangeTextColorForFrameSix(act, sixBinding, colorCodeForTextColor);
            } else if (footerLayout == 7) {
                FooterHelper.ChangeTextColorForFrameSeven(act, sevenBinding, colorCodeForTextColor);
            } else if (footerLayout == 8) {
                FooterHelper.ChangeTextColorForFrameEight(act, eightBinding, colorCodeForTextColor);
            } else if (footerLayout == 9) {
                FooterHelper.ChangeTextColorForFrameNine(act, nineBinding, colorCodeForTextColor);
            } else if (footerLayout == 10) {
                FooterHelper.ChangeTextColorForFrameTen(act, tenBinding, colorCodeForTextColor);
            }

        }


        if (footerLayout == 1) {
            FooterHelper.ChangeBackgroundColorForFrameOne(act, oneBinding, colorCodeForBackground);
        } else if (footerLayout == 2) {
            FooterHelper.ChangeBackgroundColorForFrameTwo(act, twoBinding, colorCodeForBackground);
        } else if (footerLayout == 3) {

        } else if (footerLayout == 4) {
            FooterHelper.ChangeBackgroundColorForFrameFour(act, fourBinding, colorCodeForBackground);
        } else if (footerLayout == 5) {
            FooterHelper.ChangeBackgroundColorForFrameFive(act, fiveBinding, colorCodeForBackground);
        } else if (footerLayout == 6) {
            FooterHelper.ChangeBackgroundColorForFrameSix(act, sixBinding, colorCodeForBackground);
        } else if (footerLayout == 7) {
            FooterHelper.ChangeBackgroundColorForFrameSeven(act, sevenBinding, colorCodeForBackground);
        } else if (footerLayout == 8) {
            FooterHelper.ChangeBackgroundColorForFrameEight(act, eightBinding, colorCodeForBackground);
        } else if (footerLayout == 9) {
            FooterHelper.ChangeBackgroundColorForFrameNine(act, nineBinding, colorCodeForBackground);
        } else if (footerLayout == 10) {
            FooterHelper.ChangeBackgroundColorForFrameTen(act, tenBinding, colorCodeForBackground);
        }

        changeBorderColorAsFrame();

        //bold
        if (footerLayout == 1) {
            FooterHelper.makeBoldForOne(oneBinding, isLoadBold);
        } else if (footerLayout == 2) {
            FooterHelper.makeItalicForTwo(twoBinding, isLoadBold);
        } else if (footerLayout == 3) {
            FooterHelper.makeItalicForThree(threeBinding, isLoadBold);
        } else if (footerLayout == 4) {
            FooterHelper.makeBoldForFour(fourBinding, isLoadBold);
        } else if (footerLayout == 5) {
            FooterHelper.makeBoldForFive(fiveBinding, isLoadBold);
        } else if (footerLayout == 6) {
            FooterHelper.makeBoldForSix(sixBinding, isLoadBold);
        } else if (footerLayout == 7) {
            FooterHelper.makeBoldForSeven(sevenBinding, isLoadBold);
        } else if (footerLayout == 8) {
            FooterHelper.makeBoldForEight(eightBinding, isLoadBold);
        } else if (footerLayout == 9) {
            FooterHelper.makeBoldForNine(nineBinding, isLoadBold);
        } else if (footerLayout == 10) {
            FooterHelper.makeBoldForTen(tenBinding, isLoadBold);
        }


        //italic
        if (footerLayout == 1) {
            FooterHelper.makeItalicForOne(oneBinding, isLoadItalic);
        } else if (footerLayout == 2) {
            FooterHelper.makeItalicForTwo(twoBinding, isLoadItalic);

        } else if (footerLayout == 3) {

            FooterHelper.makeItalicForThree(threeBinding, isLoadItalic);

        } else if (footerLayout == 4) {
            FooterHelper.makeItalicForFour(fourBinding, isLoadItalic);
        } else if (footerLayout == 5) {
            FooterHelper.makeItalicForFive(fiveBinding, isLoadItalic);
        } else if (footerLayout == 6) {
            FooterHelper.makeItalicForSix(sixBinding, isLoadItalic);
        } else if (footerLayout == 7) {

            FooterHelper.makeItalicForSeven(sevenBinding, isLoadItalic);

        } else if (footerLayout == 8) {
            FooterHelper.makeItalicForEight(eightBinding, isLoadItalic);
        } else if (footerLayout == 9) {
            FooterHelper.makeItalicForNine(nineBinding, isLoadItalic);
        } else if (footerLayout == 10) {
            FooterHelper.makeItalicForTen(tenBinding, isLoadItalic);

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
                //nineBinding.gmailText.setTypeface(custom_font);
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
            if (footerLayout == 1) {
                FooterHelper.ChangeBackgroundColorForFrameOne(act, oneBinding, colorCode);
            } else if (footerLayout == 2) {
                FooterHelper.ChangeBackgroundColorForFrameTwo(act, twoBinding, colorCode);
            } else if (footerLayout == 3) {

            } else if (footerLayout == 4) {
                FooterHelper.ChangeBackgroundColorForFrameFour(act, fourBinding, colorCode);
            } else if (footerLayout == 5) {
                FooterHelper.ChangeBackgroundColorForFrameFive(act, fiveBinding, colorCode);
            } else if (footerLayout == 6) {
                FooterHelper.ChangeBackgroundColorForFrameSix(act, sixBinding, colorCode);
            } else if (footerLayout == 7) {
                FooterHelper.ChangeBackgroundColorForFrameSeven(act, sevenBinding, colorCode);
            } else if (footerLayout == 8) {
                FooterHelper.ChangeBackgroundColorForFrameEight(act, eightBinding, colorCode);
            } else if (footerLayout == 9) {
                FooterHelper.ChangeBackgroundColorForFrameNine(act, nineBinding, colorCode);
            } else if (footerLayout == 10) {

                FooterHelper.ChangeBackgroundColorForFrameTen(act, tenBinding, colorCode);
            }
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
            //     nineBinding.gmailText.setTypeface(custom_font);
            nineBinding.contactText.setTypeface(custom_font);


        } else if (footerLayout == 10) {
            Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
            tenBinding.locationText.setTypeface(custom_font);
            tenBinding.gmailText.setTypeface(custom_font);
            tenBinding.contactText.setTypeface(custom_font);


        }
    }

    // for font size
    @Override
    public void onfontSize(int textsize) {

        previousFontSize = textsize;
        if (previousFontSize != -1) {
            if (footerLayout == 1) {
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
    }

    //for bold text
    @Override
    public void onBoldTextChange(boolean Bold) {
        isLoadBold = Bold;
        if (Bold) {
            if (footerLayout == 1) {
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
        } else {
            if (footerLayout == 1) {
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

    }

    //for italic
    @Override
    public void onItalicTextChange(boolean Italic) {
        isLoadItalic = Italic;
        if (Italic) {
            if (footerLayout == 1) {
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
        } else {
            if (footerLayout == 1) {
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
                                        //abc();
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
                                    // abc();
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
