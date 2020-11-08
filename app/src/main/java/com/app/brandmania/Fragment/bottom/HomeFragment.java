package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.app.brandmania.Adapter.DasboardAddaptor;
import com.app.brandmania.Adapter.ImageCateItemeInterFace;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Connection.ItemMultipleSelectionInterface;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.Adapter.ViewPagerAdapter;
import com.app.brandmania.Model.ViewPagerItem;
import com.app.brandmania.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class HomeFragment extends Fragment  implements ItemMultipleSelectionInterface , ImageCateItemeInterFace {
    public static int BUSINESS_TYPE = 1;
    private String BusinessTitle;
    ArrayList<BrandListItem> BusinessTypeList = new ArrayList<>();
    ArrayList<DashBoardItem> menuModels = new ArrayList<>();
    BrandListItem brandListItem;

    private DasboardAddaptor dasboardAddaptor;
    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.8f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.2f;
    private static final int ALPHA_ANIMATIONS_DURATION = 100;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private RelativeLayout mTitleContainer;
    Activity act;
    PreafManager preafManager;
    private String deviceToken = "";
    private FragmentHomeBinding binding;
    Timer timer;
    private HomeFragment homeFragment;
    private SelectBrandListBottomFragment bottomSheetFragment;
    private AppUpdateManager appUpdateManager;
    private Task<AppUpdateInfo> appUpdateInfoTask;
    public String getDeviceToken(Activity act) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(act, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        deviceToken = instanceIdResult.getToken();
                        UpdateToken();
                    }
                });
        return deviceToken;
    }

    private void checkForUpdates() {
        appUpdateManager = AppUpdateManagerFactory.create(act);
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    startAppUpdates(appUpdateInfo);
                }
            }
        });
    }
    private void startAppUpdates(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    IMMEDIATE,
                    act,
                    Constant.APP_UPDATES);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (appUpdateManager != null) {
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(
                            new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
                                @Override
                                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                                    if (appUpdateInfo.updateAvailability()
                                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                        // If an in-app update is already running, resume the update.
                                        try {
                                            appUpdateManager.startUpdateFlowForResult(
                                                    appUpdateInfo,
                                                    IMMEDIATE,
                                                    act,
                                                    Constant.APP_UPDATES);
                                        } catch (IntentSender.SendIntentException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        homeFragment=this;
          preafManager=new PreafManager(act);
        if (preafManager.getAddBrandList()!=null && preafManager.getAddBrandList().size()!=0 &&preafManager.getActiveBrand()==null){
            preafManager.setActiveBrand(preafManager.getAddBrandList().get(0));
            preafManager=new PreafManager(act);
        }
        Gson gson=new Gson();
//        Log.e("Frames",gson.toJson(preafManager.getActiveBrand().getFrame()));
//        Toast.makeText(act,preafManager.getActiveBrand().getId(),Toast.LENGTH_SHORT).show();
        checkForUpdates();
        binding.businessName.setText(preafManager.getActiveBrand().getName());
        mTitleContainer =act.findViewById(R.id.main_linearlayout_title);
        getDeviceToken(act);
        AddUserActivity();
        binding.businessNameDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentList(BUSINESS_TYPE,BusinessTitle);
            }
        });
        startAnimation();
        getImageCtegory();
        getBanner();

        return binding.getRoot();


    }
    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.rocommRecycler.setVisibility(View.GONE);

    }
    //Show Fragment For BrandList...........
    public void showFragmentList(int callingFlag, String title) {
        bottomSheetFragment = new SelectBrandListBottomFragment();
        bottomSheetFragment.setHomeFragment(homeFragment);
        bottomSheetFragment.setListData(callingFlag,title);
        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }
    //SetAddeptor.....................
    public void setAdapter() {
        DasboardAddaptor dasboardAddaptor = new DasboardAddaptor(menuModels,act);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.rocommRecycler.setHasFixedSize(true);
        binding.rocommRecycler.setLayoutManager(mLayoutManager);
        binding.rocommRecycler.setAdapter(dasboardAddaptor);

    }
    //GetBanner........................
    private void getBanner() {

        Utility.Log("API : ", APIs.GET_BANNER);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_BANNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_BANNER : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    viewPagerItems = ResponseHandler.HandleGetBanneList(jsonObject);
                    if (viewPagerItems != null && viewPagerItems.size() != 0) {
                        final ViewPagerAdapter viewPagerAddeptor=new ViewPagerAdapter(viewPagerItems,act);
                        binding.ViewPagerView.setAdapter(viewPagerAddeptor);
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() { binding.ViewPagerView.post(new Runnable(){
                                @Override
                                public void run() {
                                    binding.ViewPagerView.setCurrentItem((binding.ViewPagerView.getCurrentItem()+1)%viewPagerAddeptor.getCount());
                                }
                            }); }
                        };
                        timer = new Timer();
                        timer.schedule(timerTask, 3000, 3000);

                    } else {
                        Log.e("Condidtion", "Else");

                    }



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
                params.put("Authorization", "Bearer"+preafManager.getUserToken());
                Log.e("Token", params.toString());
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

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    //GetImageCategory..................
    private void getImageCtegory() {
        Utility.Log("API : ", APIs.GET_IMAGE_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGE_CATEGORY+"/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_IMAGE_CATEGORY : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    menuModels = ResponseHandler.HandleGetImageCategory(jsonObject);
                    if (menuModels != null && menuModels.size() != 0) {
                        setAdapter();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.rocommRecycler.setVisibility(View.VISIBLE);
                    }
                    else {
                        Log.e("Condidtion", "Else");


                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        binding.rocommRecycler.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);

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
                params.put("Authorization", "Bearer"+preafManager.getUserToken());
                Log.e("Token", params.toString());
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
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    //Update Token......................
    private void UpdateToken() {
        Utility.Log("Verify-Responce-Api", APIs.UPDATE_TOKEN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.UPDATE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("UPDATE_TOKENnn", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "application/json");
                params.put("Authorization","Bearer"+preafManager.getUserToken());
                return params;

            }


            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("firebase_token", deviceToken);
                Utility.Log("Verify-Param", hashMap.toString());
                return hashMap;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    private void AddUserActivity() {
        Utility.Log("Verify-Responce-Api", APIs.ADD_BRAND);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.ADD_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("ADD_BRAND", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "application/json");
                params.put("Authorization","Bearer"+preafManager.getUserToken());
                return params;

            }


            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                Utility.Log("Verify-Param", hashMap.toString());
                return hashMap;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    //Back Event.........................
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
    @Override public void onItemSMultipleelection(int calledFlag, int position, BrandListItem listModel) {
        if (bottomSheetFragment != null && bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }

            binding.businessName.setText(listModel.getName());
            brandListItem=listModel;
            preafManager.setActiveBrand(listModel);
            Gson gson=new Gson();
            Log.e("Second",gson.toJson(preafManager.getActiveBrand()));

    }
    @Override public void ImageCateonItemSelection(int position, ImageList listModel) {

    }
}
