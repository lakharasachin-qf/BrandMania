package com.make.mybrand.Fragment.top;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Adapter.DownloadFavoriteAdapter;
import com.make.mybrand.BuildConfig;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Fragment.BaseFragment;
import com.make.mybrand.Model.DownloadFavoriteItemList;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DialogUpgradeDownloadLimitExpireBinding;
import com.make.mybrand.databinding.DownloadlisTabBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DownloadListTab extends BaseFragment {
    private DownloadlisTabBinding binding;
    ArrayList<DownloadFavoriteItemList> menuModels = new ArrayList<>();

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.downloadlis_tab, parent, false);
        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorsecond,
                R.color.colorthird);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startAnimation();
                getDownloadListItem();
            }
        });
        getDownloadListItem();
        return binding.getRoot();
    }

    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.DownloadRecycler.setVisibility(View.GONE);

    }

    public boolean manuallyEnablePermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(act)
                        .setMessage("Allow BrandMania to access photos, files to download and share images ")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
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


    }


    DownloadFavoriteItemList downloadingOject;

    public void setAdapter() {
        DownloadFavoriteAdapter menuAddaptor = new DownloadFavoriteAdapter(menuModels, act);
        DownloadFavoriteAdapter.onShareImageClick onShareImageClick = new DownloadFavoriteAdapter.onShareImageClick() {
            @Override
            public void onShareClick(DownloadFavoriteItemList favoriteItemList, int position) {
                //requestAgain();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    downloadingOject = favoriteItemList;
                    if (!downloadingOject.isCustom())
                        new DownloadImageTaskFrame(downloadingOject.getFrame()).execute(downloadingOject.getFrame());
                    else
                        new DownloadImageTaskImage(downloadingOject.getImage()).execute(downloadingOject.getImage());
                } else {
                    if (manuallyEnablePermission()) {
                        downloadingOject = favoriteItemList;
                        if (!downloadingOject.isCustom())
                            new DownloadImageTaskFrame(downloadingOject.getFrame()).execute(downloadingOject.getFrame());
                        else
                            new DownloadImageTaskImage(downloadingOject.getImage()).execute(downloadingOject.getImage());

                    }
                }
            }
        };

        DownloadFavoriteAdapter.onShareVideoClick onShareVideoClick = new DownloadFavoriteAdapter.onShareVideoClick() {
            @Override
            public void onShareClick(DownloadFavoriteItemList favoriteItemList, int position) {
                //requestAgain();
                downloadingOject = favoriteItemList;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    new AsyncTaskRunner().execute();
                } else {
                    new AsyncTaskRunner().execute();
                }
            }
        };
        menuAddaptor.setOnShareImageClick(onShareImageClick);
        menuAddaptor.setOnShareVideoClick(onShareVideoClick);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.DownloadRecycler.setLayoutManager(mLayoutManager);
        binding.DownloadRecycler.setHasFixedSize(true);
        binding.DownloadRecycler.setAdapter(menuAddaptor);
    }

    ProgressDialog progressDialog;

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping...");
            saveVideoInCatch();
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {

            return;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(act);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    DownloadManager manager;
    boolean isVideoIsDownloading = false;
    String framePath = "";
    String finalVideoPath = "";
    long downloadID;
    String videoUrl;

    public void saveVideoInCatch() {
        if (!isVideoIsDownloading) {
            String url = String.valueOf(downloadingOject.getImage());
            DownloadManager.Request request;
            request = new DownloadManager.Request(Uri.parse(url));
            videoUrl = URLUtil.guessFileName(url, null, null).replace("0", System.currentTimeMillis() + "");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setDescription("Downloading Please wait...");
            request.setTitle(videoUrl);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, videoUrl);
            request.allowScanningByMediaScanner();
            manager = (DownloadManager) act.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID = manager.enqueue(request);
            act.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            isVideoIsDownloading = true;
        }
    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                //Log.e("Download-Process", "Done");
                File outputFrameFile = new File(act.getCacheDir() + File.separator);
                String vdPaths = outputFrameFile.getAbsolutePath();

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + File.separator);
                if (!file.exists()) {
                    file.mkdir();
                }

                String path = file.getAbsolutePath();
                file = new File(path + "/" + videoUrl);
                String vdPath = file.getAbsolutePath();
                file = new File(vdPaths + "/" + videoUrl);
                if (!vdPath.isEmpty()) {
                    finalVideoPath = vdPath;
                }
                progressDialog.dismiss();
                shareVideo(finalVideoPath);
                isVideoIsDownloading = false;
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTaskFrame extends AsyncTask<String, Void, BitmapDrawable> {
        String url;

        public DownloadImageTaskFrame(String url) {
            this.url = url;
        }

        protected BitmapDrawable doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new BitmapDrawable(getResources(), mIcon11);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showLoadingTran(act);
        }

        protected void onPostExecute(BitmapDrawable result) {
            //bmImage.setImageBitmap(result);
            FrameDrawbable = result;
            Utility.dismissLoadingTran();
            new DownloadImageTaskImage(downloadingOject.getImage()).execute(downloadingOject.getImage());

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTaskImage extends AsyncTask<String, Void, BitmapDrawable> {
        String url;

        public DownloadImageTaskImage(String url) {
            this.url = url;
        }

        protected BitmapDrawable doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new BitmapDrawable(getResources(), mIcon11);
        }

        protected void onPostExecute(BitmapDrawable result) {
            backgroundImageDrable = result;
            startsShare();
            Utility.dismissLoadingTran();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showLoadingTran(act);
        }
    }

    //For CreatFileeDisc For Download Image.........................
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "BrandMania");
    }

    File new_file;
    BitmapDrawable FrameDrawbable;
    BitmapDrawable backgroundImageDrable;

    //fire intent for share


    public void triggerShareIntent(File new_file, Bitmap merged) {
        //  Uri uri = Uri.parse();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(act, merged));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void shareVideo(String finalVideoPath) {
        File videoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), finalVideoPath.substring(finalVideoPath.lastIndexOf('/') + 1));
        Uri videoUri = FileProvider.getUriForFile(act, BuildConfig.APPLICATION_ID + ".provider", videoFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/mp4");
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Video"));
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public void startsShare() {
        Drawable d = FrameDrawbable;
        Drawable ImageDrawable = backgroundImageDrable;

        if (downloadingOject.isCustom()) {
            d = backgroundImageDrable;
        }

        Bitmap merged = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(merged);
        d.setBounds(0, 0, 1000, 1000);
        ImageDrawable.setBounds(0, 0, 1000, 1000);
        ImageDrawable.draw(canvas);
        d.draw(canvas);

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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        triggerShareIntent(new_file, merged);
    }

    private void getDownloadListItem() {

        Utility.Log("API : ", APIs.GET_DOWNLOADLIST_ITEM);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_DOWNLOADLIST_ITEM, new Response.Listener<String>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(String response) {
                Utility.Log("download", response);
                binding.swipeContainer.setRefreshing(false);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    menuModels = ResponseHandler.HandleGetIDownloadFavoritList(jsonObject);

                    if (menuModels != null && menuModels.size() != 0) {
                        setAdapter();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.DownloadRecycler.setVisibility(View.VISIBLE);
                        binding.emptyStateLayout.setVisibility(View.GONE);
                    }
                    if (menuModels == null || menuModels.size() == 0) {
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.DownloadRecycler.setVisibility(View.GONE);
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

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", prefManager.getActiveBrand().getId());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }


}
