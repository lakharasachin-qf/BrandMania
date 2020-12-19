package com.app.brandmania.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.PreafManager;

import com.google.gson.Gson;

import java.util.HashMap;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        View view = provideFragmentView(inflater, parent, savedInstanseState);
        return view;
    }

    public abstract View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Override
    public void update(Observable observable, Object data) {

    }


}
