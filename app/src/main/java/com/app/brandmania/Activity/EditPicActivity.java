package com.app.brandmania.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.app.brandmania.Adapter.ItemeInterFace;
import com.app.brandmania.Adapter.MenuAddaptor;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityEditPic2Binding;

import com.app.brandmania.databinding.FragmentCustomBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

public class EditPicActivity extends AppCompatActivity implements ItemeInterFace {
    public static final int VIEW_RECOMDATION = 0;

    Activity act;
    int windowwidth;
    int windowheight;

    private ViewGroup.LayoutParams layoutParams;
    private ActivityEditPic2Binding binding;
    Timer timer;
    private int xDelta;
    private int yDelta;
    private ViewGroup mainLayout;
    ArrayList<MultiListItem> menuModels = new ArrayList<>();
    private MultiListItem listModel;
    Gson gson;
    private int showingView = -1;
    private static final int REQUEST_IMAGE = 101;
    private String filename;
    private Uri mCropImageUri;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    MotionEvent onClickTimeHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;
        binding= DataBindingUtil.setContentView(act, R.layout.activity_edit_pic2);
        //imageView = new ImageView(EditPicActivity.this);
        mainLayout = (RelativeLayout) findViewById(R.id.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        gson = new Gson();
        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


























        Recommendation();
//        binding.recoImageee.setImageDrawable(ContextCompat.getDrawable(act,R.drawable.firstframe));
//        binding.recoImageee.setDrawingCacheEnabled(true);
        binding.recoframe.setTag("0");




        binding.recoImageee.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                int color = bmp.getPixel((int) event.getX(), (int) event.getY());
                onClickTimeHelper=event;
                if (color == Color.TRANSPARENT) {
                    binding.recoframe.setVisibility(View.VISIBLE);


                    if (binding.recoframe.getTag().toString().equals("1"))
                    {
                        binding.recoframe.setOnTouchListener(onTouchListener());
                        binding.recoImageee.setEnabled(false);
                    }else{
                        onSelectImageClick();
                    }
                    //Toast.makeText(act, "Yes TransPerent", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    //code to execute
                    return true;
                }
            }
        });


        if (getIntent().hasExtra("flag")) {
            int flag = getIntent().getIntExtra("flag", -1);
            if (flag == VIEW_RECOMDATION) {
                showingView = VIEW_RECOMDATION;
            }
        }
       // if (showingView == VIEW_RECOMDATION);


    }
    public void Recommendation() {

        ArrayList<MultiListItem> menuModels = new ArrayList<>();
        MultiListItem model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.firstframe);
        menuModels.add(model);


        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.secondframe);
        menuModels.add(model);


        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.thirdframe);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.fourthframe);
        menuModels.add(model);


        MenuAddaptor menuAddaptor = new MenuAddaptor(menuModels, act);
        binding.viewRecoRecyclerrrr.setLayoutManager(new GridLayoutManager(this,3));
        binding.viewRecoRecyclerrrr.setHasFixedSize(true);
        binding.viewRecoRecyclerrrr.setAdapter(menuAddaptor);
    }
    @Override public void onItemSelection(int position, MultiListItem listModel) {
         //binding.recoImageee.setBackgroundResource(listModel.getImage());
       binding.recoImageee.setImageDrawable(ContextCompat.getDrawable(act,listModel.getImage()));
        binding.recoImageee.setDrawingCacheEnabled(true);


    }
    private View.OnTouchListener onTouchListener() {
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

                mainLayout.invalidate();
                return true;
            }
        };
    }
    public void onSelectImageClick() {
        CropImage.startPickImageActivity(this);

    }
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

                ((ImageView) findViewById(R.id.recoframe)).setImageURI(result.getUri());

                binding.recoframe.setTag("1");
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
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            binding.recoframe.setScaleX(mScaleFactor);
            binding.recoframe.setScaleY(mScaleFactor);
            return true;
        }
    }

}