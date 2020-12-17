package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.brandmania.Adapter.ImageFromGalaryAddpater;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FrameTabBinding;
import com.app.brandmania.databinding.ImageTabBinding;

import java.io.File;
import java.util.ArrayList;

public class ImageTab extends Fragment {
    Activity act;
    private ImageTabBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater, R.layout.image_tab,container,false);
        binding.imageGalaryRecyclerVire.setLayoutManager(new GridLayoutManager(act,4));
        binding.imageGalaryRecyclerVire.setAdapter(new ImageFromGalaryAddpater(act,getData()));
        return binding.getRoot();
    }

        private ArrayList<ImageFromGalaryModel> getData()
        {
            ArrayList<ImageFromGalaryModel> spacecrafts=new ArrayList<>();
            //TARGET FOLDER
            File downloadsFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

            ImageFromGalaryModel s;

            if(downloadsFolder.exists())
            {
                //GET ALL FILES IN DOWNLOAD FOLDER
                File[] files=downloadsFolder.listFiles();

                //LOOP THRU THOSE FILES GETTING NAME AND URI
                for (int i=0;i<files.length;i++)
                {
                    File file=files[i];

                    s=new ImageFromGalaryModel();
                    s.setName(file.getName());
                    s.setUri(Uri.fromFile(file));

                    spacecrafts.add(s);
                }
            }


            return spacecrafts;
        }

}
