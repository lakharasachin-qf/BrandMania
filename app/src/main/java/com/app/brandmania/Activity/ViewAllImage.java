package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Connection.BaseActivity;
import com.bumptech.glide.Glide;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;
import static com.app.brandmania.Utils.Utility.dialog;

public class ViewAllImage extends BaseActivity implements ImageCateItemeInterFace,alertListenerCallback{
    Activity act;
    ViewPager viewPager;
    ArrayList<ImageList> AddFavorite=new ArrayList<>();
    private ActivityViewAllImageBinding binding;
    ArrayList<ImageList> menuModels = new ArrayList<>();
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    public static final int DOWLOAD = 1;
    public static final int ADDFAV = 3;
    private static final int REQUEST_CALL = 1;
    public static final int REMOVEFAV = 3;
    ArrayList<FrameItem> viewPagerItems = new ArrayList<>();
    PreafManager preafManager;
    Gson gson;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        captureScreenShort();

        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_all_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        preafManager = new PreafManager(this);
        binding.titleName.setSelected(true);
        gson = new Gson();
        selectedObject = gson.fromJson(getIntent().getStringExtra("selectedimage"), ImageList.class);
        Log.e("selectedObject",gson.toJson(selectedObject));
        getFrame();
        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorsecond,
                R.color.colorthird);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                startAnimation();

                getImageCtegory();
                // startAnimation();
                //getNotice(startDate, endDate);

            }
        });



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
                Log.e("FrameIdWithImage",selectedObject.getFrameId());
              preafManager.AddToMyFavorites(selectedObject);
              if (binding.fabroutIcon.getVisibility()==View.VISIBLE)
              {
                  binding.fabroutIcon.setVisibility(View.GONE);
                  binding.addfabroutIcon.setVisibility(View.VISIBLE);
              }
              Log.e("FAVVV",gson.toJson(preafManager.getSavedFavorites()));
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
                Log.e("FAVVV",gson.toJson(preafManager.getSavedFavorites()));
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
                               // startSave();
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
                dowloadAndShare(DOWLOAD);
            }
        });
    }
    public void LoadDataToUI(){
        preafManager=new PreafManager(act);
        if (selectedObject != null) {
            binding.simpleProgressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(selectedObject.getFrame()).into(binding.backgrounImageDuplicate);
            Glide.with(getApplicationContext())
                    .load(selectedObject.getFrame())
                    .into(binding.recoImage);

            AddFavorite= preafManager.getSavedFavorites();

            if (AddFavorite!=null) {
                for (int i = 0; i < AddFavorite.size(); i++) {
                    Log.e("Print-",AddFavorite.get(i).getFrameId()+"s");
//                    Log.e("Print--",selectedModelFromView.getFrameId()+"s");

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
        Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        Uri screenshotUri = Uri.parse(new_file.getPath());
        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Choose an app"));






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
        binding.swipeContainer.setRefreshing(true);
        Utility.Log("API : ", APIs.GET_IMAGEBUID_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGEBUID_CATEGORY + "/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
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
                        binding.swipeContainer.setRefreshing(false);
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
            showAlertDialogButtonClicked();
        }

        LoadDataToUI();

    }
    public void AlertBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your Frame Is Not Added ! Please Contact To Admin");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                onBackPressed();
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


                        frameViewPager();


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

    public void showAlertDialogButtonClicked()
    {

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.frame_alert_box, null);
        builder.setView(customLayout);
        ImageView call=customLayout.findViewById(R.id.call);
        ImageView whatsapp=customLayout.findViewById(R.id.whatsapp);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makePhoneCall();

            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whatsapp();
            }
        });
        // add a button
        builder
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {

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
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog.setCancelable(false);
        dialog.show();
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(Color.WHITE);
    }

    private void whatsapp()
    {
        try {
            String number ="8460638464";
            String BrandContact="\nRegistered Number: ";
            String text = "Hello *BrandMania* ,  \n" + "this is request to add  *Frame* For BrandName:"+ preafManager.getActiveBrand().getName() +BrandContact+preafManager.getMobileNumber();
            String toNumber ="91"+number;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void makePhoneCall() {
        String number ="8460638464";
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}