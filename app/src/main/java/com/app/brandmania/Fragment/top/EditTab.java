package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.brandmania.Adapter.FilterViewAdapter;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Connection.FilterList;
import com.app.brandmania.Connection.ThumbnailsManager;
import com.app.brandmania.Interface.IImageBritnessEvent;
import com.app.brandmania.Interface.IrotateEvent;
import com.app.brandmania.Interface.ThumbnailCallback;
import com.app.brandmania.Model.ThumbnailItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.EditTabBinding;

import java.util.List;

public class EditTab extends Fragment {
    private Activity activity;
    private EditTabBinding binding;

    private boolean mIsFilterVisible;
    PreafManager preafManager;
    private ConstraintLayout mRootView;
    static {
        System.loadLibrary("NativeImageProcessor");
    }
    private ConstraintSet mConstraintSet = new ConstraintSet();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity  = getActivity();

        preafManager=new PreafManager(activity);
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_tab, container, false);
        initHorizontalList();
        binding.rotateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IrotateEvent) activity).onRotateImage(90);
            }
        });


        binding.seekBar.setProgress(125);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

               // im_brightness.setColorFilter(setBrightness(progress));
                ((IImageBritnessEvent) activity).onimageBritness(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return binding.getRoot();
    }

//    private void initUIWidgets() {
//        thumbListView = (RecyclerView) findViewById(R.id.thumbnails);
//        placeHolderImageView = (ImageView) findViewById(R.id.place_holder_imageview);
//        placeHolderImageView.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.photo), 640, 640, false));
//        initHorizontalList();
//    }
    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        binding.filterRecycler.setLayoutManager(layoutManager);
        binding.filterRecycler.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = getActivity();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.filter_logo), 640, 640, false);
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();
                ThumbnailItem t7 = new ThumbnailItem();

                t1.image = thumbImage;
                t2.image = thumbImage;
                t3.image = thumbImage;
                t4.image = thumbImage;
                t5.image = thumbImage;
                t6.image = thumbImage;
                t7.image = thumbImage;
                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(t1); // Original Image

                t2.filter = FilterList.getStarLitFilter();
                ThumbnailsManager.addThumb(t2);

                t3.filter = FilterList.getBlueMessFilter();
                ThumbnailsManager.addThumb(t3);

                t4.filter = FilterList.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(t4);

                t5.filter = FilterList.getLimeStutterFilter();
                ThumbnailsManager.addThumb(t5);

                t6.filter = FilterList.getNightWhisperFilter();
                ThumbnailsManager.addThumb(t6);

//                t7.filter = FilterList.getBlueMessFilterrrr();
//                ThumbnailsManager.addThumb(t7);

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                FilterViewAdapter adapter = new FilterViewAdapter(thumbs, (ThumbnailCallback) getActivity());
                binding.filterRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }






    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >=    100)
        {
            int value = (int) (progress-100) * 255 / 100;

            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);

        }
        else
        {
            int value = (int) (100-progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);


        }
    }

}
