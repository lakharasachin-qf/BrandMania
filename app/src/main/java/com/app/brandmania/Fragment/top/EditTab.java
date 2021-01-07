package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.SeekBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.brandmania.Adapter.FilterListener;
import com.app.brandmania.Adapter.FilterViewAdapter;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Interface.IImageBritnessEvent;
import com.app.brandmania.Interface.ITextSizeEvent;
import com.app.brandmania.Interface.IrotateEvent;
import com.app.brandmania.R;
import com.app.brandmania.databinding.EditTabBinding;
import com.app.brandmania.databinding.FooterTabBinding;

public class EditTab extends Fragment {
    Activity act;
    private EditTabBinding binding;
    private FilterViewAdapter mFilterViewAdapter;
    private boolean mIsFilterVisible;
    PreafManager preafManager;
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        mFilterViewAdapter = new FilterViewAdapter((FilterListener) act);
        preafManager=new PreafManager(act);
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_tab, container, false);
        LinearLayoutManager llmFilters = new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false);
        binding.filterRecycler.setLayoutManager(llmFilters);
        binding.rotateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IrotateEvent) act).onRotateImage(90);
            }
        });
        binding.filterRecycler.setAdapter(mFilterViewAdapter);

        binding.seekBar.setProgress(125);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

               // im_brightness.setColorFilter(setBrightness(progress));
                ((IImageBritnessEvent) act).onimageBritness(progress);

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

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(binding.filterRecycler.getId(), ConstraintSet.START);
            mConstraintSet.connect(binding.filterRecycler.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(binding.filterRecycler.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(binding.filterRecycler.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(binding.filterRecycler.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
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
