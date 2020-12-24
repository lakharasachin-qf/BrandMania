package com.app.brandmania.Activity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app.brandmania.Adapter.FooterAdapter;
import com.app.brandmania.Adapter.FooterModel;
import com.app.brandmania.Adapter.ViewAllTopTabAdapter;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Interface.IItaliTextEvent;
import com.app.brandmania.Interface.ITextBoldEvent;
import com.app.brandmania.Interface.ITextColorChangeEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.IUnderLineTextEvent;
import com.app.brandmania.Interface.onFooterSelectListener;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.Utils.IFontChangeEvent;
import com.app.brandmania.databinding.DialogUpgradeLayoutBinding;
import com.app.brandmania.databinding.ItemFooterOneBinding;
import com.app.brandmania.databinding.LayoutForLoadFiveBinding;
import com.app.brandmania.databinding.LayoutForLoadFourBinding;
import com.app.brandmania.databinding.LayoutForLoadOneBinding;
import com.app.brandmania.databinding.LayoutForLoadSixBinding;
import com.app.brandmania.databinding.LayoutForLoadThreeBinding;
import com.app.brandmania.databinding.LayoutForLoadTwoBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.app.brandmania.Adapter.ImageCateItemeInterFace;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Adapter.ViewPagerAdapterFrame;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityViewAllImageBinding;
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
import java.net.HttpURLConnection;
import java.net.URL;
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
        ITextSizeEvent,View.OnTouchListener, onFooterSelectListener {
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
    private int _xDelta;
    private int _yDelta;
    private ProgressDialog simpleWaitDialog;
    private DashBoardItem imageList;
    private ImageList selectedObject;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Drawable drawable;
    FrameItem selectedModelFromView;
    AlertDialog.Builder alertDialogBuilder;
    File new_file;
    private Uri mCropImageUri;
    ImageView imageView;
    TextView selectedForEdit;
    View selectedForBackgroundChange;
    int editorFragment;


    int FramePrimaryOrSecondary=0;
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
                dowloadAndShare(ADDFAV);
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

                removeFavourit(REMOVEFAV);
            }
        });
        binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder = new AlertDialog.Builder(act);
                alertDialogBuilder.setTitle("Save image");
                alertDialogBuilder.setMessage("You sure to save your image?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                requestAgain();
                                Log.e("CSelectedImg",gson.toJson(selectedModelFromView.getFrame1()));
                                new DownloadImageTask(selectedModelFromView.getFrame1()).execute(selectedModelFromView.getFrame1());
                                dowloadAndShare(DOWLOAD);
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
        });
        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAgain();
                Log.e("CSelectedImg",gson.toJson(selectedModelFromView.getFrame1()));
                new ShareImageTask(selectedModelFromView.getFrame1()).execute(selectedModelFromView.getFrame1());
                //checkPermisionForDontAskAgain();
                dowloadAndShare(DOWLOAD);
            }
        });
        binding.logoEmptyState.setVisibility(View.VISIBLE);
        fetchAutomaticCustomeFrame();


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
            
          /*  binding.logoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectImageClick(view);
                }
            });*/
        }


         RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.logoEmptyState.setLayoutParams(mRparams);
        binding.logoEmptyState.setOnTouchListener((View.OnTouchListener) act);
//        mRlayout.addView( binding.logoCard);
    }

    public void checkPermisionForDontAskAgain(){
        AlertDialog.Builder alertDialog;
        AlertDialog dialog = null;
        if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CodeReUse.ASK_PERMISSSION);

        } else {
            alertDialog=new AlertDialog.Builder(act)
                    .setMessage("Click ok to allow permission from settings")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            isPermissionGranted(false);
                        }
                    });
            dialog=alertDialog.create();
            dialog.show();
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    CodeReUse.ASK_PERMISSSION);

        } else {
        if (dialog!=null && !dialog.isShowing()) {
            alertDialog = new AlertDialog.Builder(act)
                    .setMessage("You have denied permission")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            isPermissionGranted(false);
                        }
                    });

            dialog = alertDialog.create();
            dialog.show();
        }
        }

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
    public void bottomFramgment(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Category")));

        if (!is_frame.equalsIgnoreCase("1")) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Background")));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Text")));

                IntroCounter=0;
                preafManager.setFrameIntro(false);
                startIntroForFrameOnly(binding.logoEmptyState, "Logo", "you can upload logo here");
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Footer")));

        }else{
            if (preafManager.getViewAllActivityIntro()) {
                startIntro(binding.downloadIcon,"Download","Download Image From here");
                preafManager.setViewAllActivityIntro(false);
            }
        }


        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewAllTopTabAdapter adapter = new ViewAllTopTabAdapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
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
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.backgrounImageDuplicate);
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.recoImage);

            AddFavorite= preafManager.getSavedFavorites();

            if (AddFavorite!=null) {
                for (int i = 0; i < AddFavorite.size(); i++) {
                    Log.e("Fav--",new Gson().toJson(AddFavorite.get(i)));
                    Log.e("Print--",new Gson().toJson(selectedModelFromView));

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

                Log.e("Print-",AddFavorite.get(i).getFrameId()+"s");
                Log.e("Print--",selectedModelFromView.getFrameId()+"s");

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
    public void startShare(File new_file) {

        Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(new_file.getPath()),null,null));
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));

    }
    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.viewRecoRecycler.setVisibility(View.GONE);

    }
    Bitmap drawableFromUrl(String url)  {

        HttpURLConnection connection = null;
        InputStream input=null;
        try {
            connection = (HttpURLConnection)new URL(url) .openConnection();
            connection.setRequestProperty("User-agent","Mozilla/4.0");
            connection.connect();
            input = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(input);
    }
    BitmapDrawable FrameDrawbable;
    public void startSave() {
        //Bitmap FrameDrawbable = drawableFromUrl(selectedModelFromView.getFrame1());
        Drawable d = FrameDrawbable;
        Drawable ImageDrawable = (BitmapDrawable) binding.backgrounImageDuplicate.getDrawable();
        Bitmap merged = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(merged);
        d.setBounds(0, 0, 1000, 1000);
        ImageDrawable.setBounds(0, 0, 1000, 1000);
        ImageDrawable.draw(canvas);
        d.draw(canvas);
        binding.allSetImage.setImageBitmap(merged);

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
            Bitmap bitmap = merged;//viewToBitmap(binding.allSetImage,binding.allSetImage.getWidth(),binding.recoImage.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(act, "Your image is downloaded", Toast.LENGTH_SHORT).show();
            // startShare(new_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshgallery(new_file);
    }
    public void startsShare() {
        //Bitmap FrameDrawbable = drawableFromUrl(selectedModelFromView.getFrame1());
        Drawable d = FrameDrawbable;
        Drawable ImageDrawable = (BitmapDrawable) binding.backgrounImageDuplicate.getDrawable();
        Bitmap merged = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(merged);
        d.setBounds(0, 0, 1000, 1000);
        ImageDrawable.setBounds(0, 0, 1000, 1000);
        ImageDrawable.draw(canvas);
        d.draw(canvas);
        binding.allSetImage.setImageBitmap(merged);

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
            Bitmap bitmap = merged;//viewToBitmap(binding.allSetImage,binding.allSetImage.getWidth(),binding.recoImage.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
            startShare(new_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshgallery(new_file);
    }
    //For RefresGalary........................
    public void refreshgallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    //For CreatFileeDisc For Download Image.........................
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Image Demo");
    }
    //For SetAddaptor.................................
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
        }
        else
        {
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }


    }
    //For GetImageCategory..............................
    private void getImageCtegory() {
      //  binding.swipeContainer.setRefreshing(true);
        Utility.Log("API : ", APIs.GET_IMAGEBUID_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGEBUID_CATEGORY + "/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   binding.swipeContainer.setRefreshing(false);
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
                      //  binding.swipeContainer.setRefreshing(false);
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
    //For Download,Share and Faviorit Api...............
    private void dowloadAndShare(final int download) {

        Utility.Log("API : ", APIs.DOWNLOAD_SHARE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.DOWNLOAD_SHARE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("DOWNLOAD_SHARE : ", response);
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
                    params.put("frame_id", selectedModelFromView.getFrameId());

                } else {
                    params.put("brand_id",  preafManager.getActiveBrand().getId());
                    params.put("image_id", selectedObject.getImageid());


                }
                params.put("type", String.valueOf(download));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    //For ImageSelectInterface.......................
    @Override public void ImageCateonItemSelection(int position, ImageList listModel) {

        if (selectedObject!=null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            selectedObject = listModel;
            LoadDataToUI();
        }
        else
        {
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
        }
    }
    // For Frmae Loade ..................................
    public void frameViewPager() {
        viewPager = (ViewPager) findViewById(R.id.recoframe);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        viewPagerItems =brandListItems;// preafManager.getActiveBrand().getFrame();
        Log.e("Frames",gson.toJson(preafManager.getActiveBrand().getFrame()));
        if (viewPagerItems!=null && viewPagerItems.size()!=0) {
            Log.e("BrandListSize", String.valueOf(viewPagerItems.size()) + "kkl");
            Gson gson = new Gson();
            Log.e("Viewwwjhfjkdhsjk", gson.toJson(viewPagerItems));
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
                    //drawable = drawableFromUrl(selectedModelFromView.getFrame1());
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
        else{
            // showAlertDialogButtonClicked();
        }

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
                        getBitmapFromView();

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
                      //  binding.customFrameRelative.setVisibility(View.GONE);
                        // Toast.makeText(act,brandListItems.size()+"",Toast.LENGTH_LONG).show();
                        frameViewPager();
                        is_payment_pending= datajsonobjecttt.getString("is_payment_pending");
                        packagee=datajsonobjecttt.getString("package");
                        if (packagee.equals("")) {
                            binding.shareIcon.setOnClickListener(new View.OnClickListener() {
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
                            });
                            binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPackage(act,ResponseHandler.getString(datajsonobjecttt,"package_message"));

                                }
                            });


                        }
                        else if (is_payment_pending.equals("1"))
                        {

                            binding.shareIcon.setOnClickListener(new View.OnClickListener() {
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
                            });
                            binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utility.showAlertForPayment(act,ResponseHandler.getString(datajsonobjecttt,"payment_message"));
                                }
                            });
                        }
                        else
                        {
                            // Toast.makeText(act,"TTTTTTTT",Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {
                        LoadDataToUI();
                        //fetchAutomaticCustomeFrame();
                   //     binding.customFrameRelative.setVisibility(View.VISIBLE);
                        binding.recoframe.setVisibility(View.GONE);
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
                        binding.downloadIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertBoxForSaveFrame();
                            }
                        });
                    }

                    bottomFramgment();

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
                params.put("brand_id",preafManager.getActiveBrand().getId());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    @Override public void onBackPressed() {CodeReUse.activityBackPress(act); }







    @Override
    public void onDialogDismissed(int dialogId) {

    }



    private class DownloadImageTask extends AsyncTask<String, Void, BitmapDrawable> {
        String url;
        public DownloadImageTask(String url) {
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
            startSave();


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
    }
    private void removeFavourit(final int removeFav) {

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
    public void captureScreenShort() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
    public void showAlertDialogButtonClicked() {

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.frame_alert_box, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(
                    DialogInterface dialog, int which)
            {
                dialog.dismiss();
                onBackPressed();

            }
        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorNavText);
        dialog.setCancelable(false);
        dialog.show();
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.WHITE);
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
    private void getBitmapFromView() {

      /*  Bitmap newFinal;
        Bitmap returnedBitmap = Bitmap.createBitmap(binding.customFrameRelative.getWidth(), binding.customFrameRelative.getHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);

        Drawable bgDrawable =binding.customFrameRelative.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        }
        Log.e("logggg","lkhjkgbjhmn");
        binding.customFrameRelative.draw(canvas);

        binding.FrameImageDuplicate.setVisibility(View.VISIBLE);
        binding.FrameImageDuplicate.setImageBitmap(returnedBitmap);
        BitmapDrawable drawable = (BitmapDrawable) binding.FrameImageDuplicate.getDrawable();
        newFinal = drawable.getBitmap();

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

        addCustomFrame(newFinal);*/

    }
    private void addCustomFrame(Bitmap img) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        Log.e("API", APIs.ADD_CUSTOMFRAME);
        Log.e("API", preafManager.getUserToken());
        File img1File = null;
        if (img != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.png", img);
        }

        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.ADD_CUSTOMFRAME)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer" + preafManager.getUserToken())
                .addMultipartParameter("brand_id", preafManager.getActiveBrand().getId())
                .setTag("Add CustomeFrame")
                .setPriority(Priority.HIGH);

        if (img1File != null) {
            request.addMultipartFile("frame", img1File);
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
                        Utility.dismissProgress();
                        finish();
                        startActivity(getIntent());
                        Utility.Log("Verify-Response", response);
                        binding.FrameImageDuplicate.setVisibility(View.GONE);
                        ArrayList<BrandListItem> brandListItems = new ArrayList<>();
                        try {

                            if (response.getBoolean("status")) {
                                JSONObject jsonArray = response.getJSONObject("data");


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading = false;
                        Utility.dismissProgress();

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
    private void fetchAutomaticCustomeFrame() {


        if (preafManager.getActiveBrand().getAddress()!=null)
        {
           // binding.customAddressEdit1.setText(preafManager.getActiveBrand().getEmail());
        }


        if (preafManager.getActiveBrand().getPhonenumber()!=null)
        {

          //  binding.customeContactEdit1.setText(preafManager.getActiveBrand().getPhonenumber());
        }


    }
    private void getBrandById(BrandListItem model) {

        Utility.Log("API : ", APIs.GET_BRAND_BY_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND_BY_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_BRAND_BY_ID : ", response);
                ArrayList<BrandListItem> brandListItems=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    multiListItems = ResponseHandler.HandleGetBrandById(jsonObject);

                    SliderItem sliderItem=new SliderItem();
                    sliderItem.setPriceForPay(multiListItems.get(0).getRate());
                    sliderItem.setPackageTitle(multiListItems.get(0).getPackagename());
                    sliderItem.setPackageid(multiListItems.get(0).getPackage_id());
                    sliderItem.setTemplateTitle(multiListItems.get(0).getNo_of_frame());
                    sliderItem.setImageTitle(multiListItems.get(0).getNo_of_total_image());
                    sliderItem.setBrandId(multiListItems.get(0).getId());

                    Gson gson=new Gson();
                    Log.e("DATA",gson.toJson(sliderItem));



                    Intent i = new Intent(act, RazorPayActivity.class);

                    i.putExtra("detailsObj",gson.toJson(sliderItem));

                    startActivity(i);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   binding.swipeContainer.setRefreshing(false);
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

                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id",model.getId());
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
    @Override public void onBoldTextChange(boolean Bold) {
        if (Bold) {
            //  Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            if (selectedForEdit!=null) {
                selectedForEdit.setTypeface(selectedForEdit.getTypeface(), Typeface.BOLD);
            }

        }else {
            //Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
            if (selectedForEdit!=null) {
                selectedForEdit.setTypeface(null, Typeface.NORMAL);
            }
        }

    }
    @Override public void onItalicTextChange(boolean Italic) {
        if (Italic) {
            //   Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            if (selectedForEdit!=null) {
                selectedForEdit.setTypeface(selectedForEdit.getTypeface(), Typeface.ITALIC);
            }
        }else {
            if (selectedForEdit!=null) {

             //   Toast.makeText(act, "false", Toast.LENGTH_SHORT).show();
                selectedForEdit.setTypeface(null, Typeface.NORMAL);
            }
        }
    }
    @Override public void onfontSize(int textsize) {
        if (selectedForEdit!=null) {
            selectedForEdit.setTextSize(textsize);
        }


    }

    @Override public void onUnderLineItalic(boolean Left) {
        if (Left) {
            // Toast.makeText(act,"true",Toast.LENGTH_SHORT).show();
            //  binding.customAddressEdit.setPaintFlags( binding.customAddressEdit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            if (selectedForEdit!=null) {
                selectedForEdit.setPaintFlags(selectedForEdit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }
        }
        else
        {
            // Toast.makeText(act,"false",Toast.LENGTH_SHORT).show();
            //  .setTypeface(null, Typeface.NORMAL);
            if (selectedForEdit!=null) {

                selectedForEdit.setPaintFlags(0);
            }
        }
    }

    @Override public void onFontChangeListenert(String Font) {
        Typeface custom_font = Typeface.createFromAsset(act.getAssets(), Font);
        if (selectedForEdit!=null) {
            selectedForEdit.setTypeface(custom_font);
        }
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
    public boolean onTouch(View view, MotionEvent event) {


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
            return false;

    }


    //version 3 ======================================
    @Override
    public void onFooterSelectEvent(int layoutType) {
        addDynamicFooter(layoutType);
    }
    boolean isFirstFooterSelect=true;
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




     /*   LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footerView = null;*/

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



       /* RelativeLayout.LayoutParams rLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);*/

    }

    @Override public void onColorItemChange(int colorcode) {

        if (editorFragment==2) {
            if (footerLayout==1){
                ChangeTextColorForFrameOne(colorcode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

            }
        }
        if (editorFragment==1){

            if (footerLayout==1){
                ChangeBackgroundColorForFrameOne(colorcode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

            }
        }

    }

    @Override
    public void onColorChanged(int colorCode) {
        if (editorFragment==2) {
            if (footerLayout==1){
                ChangeTextColorForFrameOne(colorCode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

            }
        }
        if (editorFragment==1){

            if (footerLayout==1){
                ChangeBackgroundColorForFrameOne(colorCode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

            }
        }



    }

    @Override
    public void onColorSelected(int dialogId, int colorCode) {
        if (editorFragment==2) {
            if (footerLayout==1){
                ChangeTextColorForFrameOne(colorCode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

            }
        }
        if (editorFragment==1){

            if (footerLayout==1){
                ChangeBackgroundColorForFrameOne(colorCode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

            }
        }


    }


    @Override
    public void onChooseColor(int colorCode) {
        if (editorFragment==2) {
            if (footerLayout==1){
                ChangeTextColorForFrameOne(colorCode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

            }
        }
        if (editorFragment==1){

            if (footerLayout==1){
                ChangeBackgroundColorForFrameOne(colorCode);
            }else if (footerLayout==2){

            }else if (footerLayout==3){

            }else if (footerLayout==4){

            }else if (footerLayout==5){

            }else if (footerLayout==6){

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




}
