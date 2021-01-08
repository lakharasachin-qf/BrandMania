package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.app.brandmania.Adapter.IImageFromGalary;
import com.app.brandmania.Adapter.ImageFromGalaryAddpater;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FrameTabBinding;
import com.app.brandmania.databinding.ImageTabBinding;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class ImageTab extends Fragment {
    Activity act;
    private ImageTabBinding binding;
    ArrayList<ImageFromGalaryModel> spacecrafts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater, R.layout.image_tab,container,false);
        binding.imageGalaryRecyclerVire.setLayoutManager(new GridLayoutManager(act,4));
        binding.imageGalaryRecyclerVire.setAdapter(new ImageFromGalaryAddpater(act,getData()));

        if (spacecrafts!=null && spacecrafts.size()!=0){
            ((IImageFromGalary) getActivity()).onImageFromGalaryItemSelection( 0, spacecrafts.get(0));
        }
        return binding.getRoot();
    }

    private ArrayList<ImageFromGalaryModel> getData()
    {
         spacecrafts= new ArrayList<>();

        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = act.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }

        Log.e("sachin-", new Gson().toJson(listOfAllImages));

        for (int i = 0; i < listOfAllImages.size(); i++) {
            File file = new File(listOfAllImages.get(i));
            ImageFromGalaryModel s = new ImageFromGalaryModel();
            s.setName(file.getName());
            s.setUri(Uri.fromFile(file));
            spacecrafts.add(s);
        }
        return spacecrafts;
    }

}
