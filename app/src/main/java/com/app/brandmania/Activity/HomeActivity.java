package com.app.brandmania.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Fragment.bottom.CustomFragment;
import com.app.brandmania.Fragment.bottom.DownloadsFragment;
import com.app.brandmania.Fragment.bottom.HomeFragment;
import com.app.brandmania.Fragment.bottom.ProfileFragment;
import com.app.brandmania.R;
import com.app.brandmania.Utils.CodeReUse;

import java.util.Timer;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ViewPager ViewPagerView;
    Timer timer;
    private Menu mMenuItem;
    PreafManager preafManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        preafManager=new PreafManager(this);



        loadFragment(new HomeFragment());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            setWhiteNavigationBar(this);
//        }
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
               // mMenuItem.getItem(0).setIcon(R.drawable.ic_homee);
             //   menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_home));


                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case R.id.navigation_custom:
                fragment = new CustomFragment();


                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;


            case R.id.navigation_download:
                fragment = new DownloadsFragment();

                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();

                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
        }

        return loadFragment(fragment);
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setWhiteNavigationBar( Activity act) {
        Window window = getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here
            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(act.getColor(R.color.Graycolor));
            // navigationBarDrawable.setTint(act.getColor(R.color.WhiteColor));
            Drawable[] layers = {dimDrawable, navigationBarDrawable};
            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);
            window.setBackgroundDrawable(windowBackground);
        }
    }
    public void onBackPressed() {
        CodeReUse.activityBackPress(this);
    }
    public void captureScreenShort()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
}