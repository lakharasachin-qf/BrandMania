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
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
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
import com.app.brandmania.Adapter.IImageFromGalary;
import com.app.brandmania.Adapter.ItemeInterFace;
import com.app.brandmania.Adapter.MenuAddaptor;
import com.app.brandmania.Adapter.MultiListItem;

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

public class CustomViewAllActivit extends AppCompatActivity  implements FrameInterFace, IImageFromGalary, View.OnTouchListener {
    public static final int VIEW_RECOMDATION = 0;
    Activity act;
    private ActivityCustomViewAllBinding binding;
    Gson gson;
    int windowwidth;
    int windowheight;
    private ViewGroup mainLayout;
    ArrayList<MultiListItem> menuModels = new ArrayList<>();
    private MultiListItem listModel;
    Drawable yourDrawable;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    MotionEvent onClickTimeHelper;
    boolean isFirstTouchOnImage=false;
    private int showingView = -1;
    private int xDelta, yDelta;






    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_custom_view_all);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        gson = new Gson();

        binding.backImage.setOnTouchListener((View.OnTouchListener) act);

        if (getIntent().hasExtra("flag")) {
            int flag = getIntent().getIntExtra("flag", -1);
            if (flag == VIEW_RECOMDATION) {
                showingView = VIEW_RECOMDATION;
            }
        }
        bottomFramgment();
        binding.backImage.setTag("0");


    }
    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        //  Utility.Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
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

                        Toast.makeText(act, "Transperent", Toast.LENGTH_SHORT).show();
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
    @Override public void onImageFromGalaryItemSelection(int position, ImageFromGalaryModel listModel) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(listModel.getUri());
            yourDrawable = Drawable.createFromStream(inputStream, listModel.getUri().toString() );
            binding.backImage.setImageDrawable(yourDrawable);
        } catch (FileNotFoundException e) {

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

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
}




