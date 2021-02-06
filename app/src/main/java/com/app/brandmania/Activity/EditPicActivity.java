package com.app.brandmania.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

public class EditPicActivity extends AppCompatActivity implements ItemeInterFace {
    public static final int VIEW_RECOMDATION = 0;

    Activity act;
    int windowwidth;
    int windowheight;
    ImageView imageView;
    private ViewGroup.LayoutParams layoutParams;
    private ActivityEditPic2Binding binding;
    Timer timer;
    private ViewGroup mainLayout;
    ArrayList<MultiListItem> menuModels = new ArrayList<>();
    private MultiListItem listModel;
    Gson gson;
    private int showingView = -1;
    private static final int REQUEST_IMAGE = 101;
    private String filename;
    private int xDelta, yDelta;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;
        binding= DataBindingUtil.setContentView(act, R.layout.activity_edit_pic2);
        imageView = new ImageView(EditPicActivity.this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        gson = new Gson();
        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();
        Recommendation();
        if (getIntent().hasExtra("flag")) {
            int flag = getIntent().getIntExtra("flag", -1);
            if (flag == VIEW_RECOMDATION) {
                showingView = VIEW_RECOMDATION;
            }
        }
        if (showingView == VIEW_RECOMDATION)
            viewRecomdation();

    }

    private void viewRecomdation() {
        binding.recoText.setVisibility(View.VISIBLE);
        binding.recoText.setText(getString(R.string.recommendation_text));
        binding.recommendation.setVisibility(View.VISIBLE);
        mainLayout = (RelativeLayout) findViewById(R.id.main);

        binding.recoframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto , 1);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });

        listModel = gson.fromJson(getIntent().getStringExtra("detailsObj"), MultiListItem.class);
        Log.e("-- ", getIntent().getStringExtra("detailsObj"));
        Recommendation();
    }
    public void Recommendation() {

        ArrayList<MultiListItem> menuModels = new ArrayList<>();
        MultiListItem model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.recfirs);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.firstn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.secondn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.thirdn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.fourn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.five);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.sixn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.
                firstn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.secondn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.thirdn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.fourn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.recofifth);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.five);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.sixn);
        menuModels.add(model);

        model = new MultiListItem();
        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLIMAGE);
        model.setImage(R.drawable.recfirs);
        menuModels.add(model);
        MenuAddaptor menuAddaptor = new MenuAddaptor(menuModels, act);
        binding.viewRecoRecyclerrrr.setLayoutManager(new GridLayoutManager(this,3));
        binding.viewRecoRecyclerrrr.setHasFixedSize(true);
        binding.viewRecoRecyclerrrr.setAdapter(menuAddaptor);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE: {
                    if (data != null && data.getData() != null) {
                        startCrop(data.getData());
                    }
                    break;
                }
                case UCrop.REQUEST_CROP: {
                    createImageView();
                    break;
                }
            }
        }
    }
    boolean isAdded=false;
    private void startCrop(Uri data) {
        String name = getFileName(data);
        File file = new File(getCacheDir(), name);
        filename = name;
        UCrop uCrop = UCrop.of(data, Uri.fromFile(file));
        uCrop.start(this);
    }
    private void createImageView() {
        File file = new File(getCacheDir(), filename);
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
     //   ImageView imageView = new ImageView(EditPicActivity.this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(250, 250);
        params.leftMargin = 0;
        params.topMargin = 0;
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bmp);
        imageView.setOnTouchListener(touchListener);
        if (!isAdded) {
            binding.recoImageee.addView(imageView);
            file.delete();
            isAdded=true;
            binding.recoframe.setVisibility(View.GONE);
        }
    }
    @Override public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();

                    xDelta = x - lParams.leftMargin;
                    yDelta = y - lParams.topMargin;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                  //  Toast.makeText(getApplicationContext(), "Объект перемещён", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (x - xDelta + view.getWidth() <= binding.recoImageee.getWidth()
                            && y - yDelta + view.getHeight() <= binding.recoImageee.getHeight()
                            && x - xDelta >= 0
                            && y - yDelta >= 0) {
                        FrameLayout.LayoutParams layoutParams =
                                (FrameLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                    }
                    break;
                }
            }
            binding.recoImageee.invalidate();
            return true;
        }
    };
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    @Override public void onItemSelection(int position, MultiListItem listModel) {
         binding.recoImageee.setBackgroundResource(listModel.getImage());
    }



}