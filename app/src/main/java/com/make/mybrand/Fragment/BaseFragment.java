package com.make.mybrand.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.make.mybrand.Common.MakeMyBrandApp;
import com.make.mybrand.Common.PreafManager;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public abstract class BaseFragment extends Fragment implements Observer {

    public MakeMyBrandApp myBrandApp;
    public PreafManager prefManager;
    public Activity act;
    public Gson gson;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        gson = new Gson();

        myBrandApp = (MakeMyBrandApp) act.getApplication();
        myBrandApp.getObserver().addObserver(this);
        prefManager = new PreafManager(act);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        return provideFragmentView(inflater, parent, savedInstanseState);
    }

    public abstract View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Override
    public void update(Observable observable, Object data) {

    }

    protected Map<String, String> getHeader(int flag) {
        return  MakeMyBrandApp.getInstance().getHeader(flag);

//        Map<String, String> headers = new HashMap<>();
//        if (flag == CodeReUse.GET_JSON_HEADER) {
//            headers.put("Accept", "application/json");
//            headers.put("Content-Type", "application/json");
//        } else {
//            headers.put("Accept", "application/x-www-form-urlencoded");
//            headers.put("Content-Type", "application/x-www-form-urlencoded");
//        }
//
//        if (prefManager.getUserToken() != null) {
//            //headers.put("X-Authorization", "Bearer " + prefManager.getUserToken());
//            headers.put("Authorization", "Bearer " + prefManager.getUserToken());
//        }
//        return headers;
    }


}
