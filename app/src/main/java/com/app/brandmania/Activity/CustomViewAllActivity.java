package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.brandmania.Adapter.EditPicAddapter;
import com.app.brandmania.Adapter.FrameInterFace;
import com.app.brandmania.Adapter.ItemeInterFace;
import com.app.brandmania.Adapter.MenuAddaptor;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.Interface.IImageFromGalary;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.R;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityCustomViewAllBinding;
import com.app.brandmania.databinding.ActivityEditPic2Binding;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tooltip.Tooltip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;

import static com.app.brandmania.Activity.EditPicActivity.VIEW_RECOMDATION;

public class CustomViewAllActivity extends AppCompatActivity  implements FrameInterFace, IImageFromGalary {
    public static final int VIEW_RECOMDATION = 0;
    Activity act;
    private ActivityCustomViewAllBinding binding;
    Gson gson;
    private ViewGroup mainLayout;
    ArrayList<MultiListItem> menuModels = new ArrayList<>();
    private MultiListItem listModel;
    Drawable yourDrawable;
    MotionEvent onClickTimeHelper;
    boolean isFirstTouchOnImage=false;
    private int showingView = -1;
    private int xDelta, yDelta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_custom_view_all);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        gson = new Gson();
        mainLayout = (RelativeLayout) findViewById(R.id.CustomImageMain);
        binding.backImage.setOnTouchListener(touchListener);
        //act.addView(imageView);
       // file.delete();

        if (getIntent().hasExtra("flag")) {
            int flag = getIntent().getIntExtra("flag", -1);
            if (flag == VIEW_RECOMDATION) {
                showingView = VIEW_RECOMDATION;
            }
        }

       // binding.CustomImageMain.addView(binding.backImage);





















        bottomFramgment();

        binding.backImage.setTag("0");







    }
    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Utility.Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    public void bottomFramgment() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Frame")));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(convertFirstUpper("Image")));
        binding.tabLayout.setTabTextColors(Color.parseColor("#727272"),Color.parseColor("#ad2753"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final EditPicAddapter adapter = new EditPicAddapter(act, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()

        {
            @Override
            public void onTabSelected (TabLayout.Tab tab){
                binding.viewPager.setCurrentItem(tab.getPosition());
//                editorFragment=tab.getPosition();
//                handler(editorFragment);
            }

            @Override
            public void onTabUnselected (TabLayout.Tab tab){
            }

            @Override
            public void onTabReselected (TabLayout.Tab tab){
            }
        });


    }
    @Override public void onFrameItemSelection(int position, MultiListItem listModel) {
        binding.frameImage.setImageDrawable(ContextCompat.getDrawable(act,listModel.getImage()));
        binding.backImage.setOnTouchListener(touchListener);
        binding.frameImage.setDrawingCacheEnabled(true);

        binding.frameImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                int color = bmp.getPixel((int) event.getX(), (int) event.getY());
                onClickTimeHelper=event;
                if (color == Color.TRANSPARENT) {
                    isFirstTouchOnImage=true;
                    binding.backImage.setOnTouchListener(touchListener);
                    return false;
                }
                else {

                    return true;
                }
            }
        });
    }
    @Override public void onImageFromGalaryItemSelection(int position, ImageFromGalaryModel listModel) {

        try {

            InputStream inputStream = getContentResolver().openInputStream(listModel.getUri());
            yourDrawable = Drawable.createFromStream(inputStream, listModel.getUri().toString() );

            binding.backImage.setBackground(yourDrawable);
            binding.backImage.setOnTouchListener(touchListener);





        } catch (FileNotFoundException e) {

        }
    }





    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent event) {
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
                    Toast.makeText(act,"I'm here!", Toast.LENGTH_SHORT).show();
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

            //mainLayout.invalidate();
            return true;
        }
    };




}