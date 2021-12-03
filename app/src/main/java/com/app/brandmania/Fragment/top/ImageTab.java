package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.brandmania.Adapter.GalleryFolderAdapter;
import com.app.brandmania.Adapter.ImageFromGalaryAddpater;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Interface.IImageFromGalary;
import com.app.brandmania.Model.ImageFromGalaryModel;
import com.app.brandmania.Model.ImagesFolder;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ImageTabBinding;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

public class ImageTab extends BaseFragment {
     private ImageTabBinding binding;
    ArrayList<ImageFromGalaryModel> spacecrafts;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.image_tab, container, false);
        binding.galleryView.setVisibility(View.GONE);
        binding.goBack.setVisibility(View.GONE);
        binding.folderView.setVisibility(View.VISIBLE);
        binding.folderView.setLayoutManager(new GridLayoutManager(act, 3));
        GalleryFolderAdapter galleryFolderAdapter = new GalleryFolderAdapter(act, SearchStorage());
        galleryFolderAdapter.setFolderSelect(new GalleryFolderAdapter.onFolderSelect() {
            @Override
            public void onFolderSelect(int position) {
                binding.galleryView.setLayoutManager(new GridLayoutManager(act, 4));
                ImageFromGalaryAddpater imageFromGalaryAddpater = new ImageFromGalaryAddpater(act, folders.get(position).getAllImagePaths());
                imageFromGalaryAddpater.setImageSelection(new ImageFromGalaryAddpater.onImageSelection() {
                    @Override
                    public void onImageSelection() {
                        binding.galleryView.setVisibility(View.GONE);
                        binding.folderView.setVisibility(View.VISIBLE);
                        binding.goBack.setVisibility(View.GONE);
                    }
                });
                binding.goBack.setVisibility(View.VISIBLE);
                binding.galleryView.setAdapter(imageFromGalaryAddpater);
                binding.galleryView.setVisibility(View.VISIBLE);
                binding.folderView.setVisibility(View.GONE);
            }
        });
        binding.folderView.setAdapter(galleryFolderAdapter);

        if (folders.get(0).getAllImagePaths()!=null && folders.get(0).getAllImagePaths().size()!=0){
            ImageFromGalaryModel galaryModel =new ImageFromGalaryModel();
            galaryModel.setName(folders.get(0).getAllImagePaths().get(0));
            File file = new File(folders.get(0).getAllImagePaths().get(0));
            galaryModel.setUri(Uri.fromFile(file));
            ((IImageFromGalary) getActivity()).onImageFromGalaryItemSelection(0, galaryModel);
        }
//        if (spacecrafts != null && spacecrafts.size() != 0) {
//            ((IImageFromGalary) getActivity()).onImageFromGalaryItemSelection(0, spacecrafts.get(0));
//        }
        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.galleryView.setVisibility(View.GONE);
                binding.folderView.setVisibility(View.VISIBLE);
                binding.goBack.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
    }


    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.GALLERY_ACTION) {
            if (getActivity() != null) {
                if (binding.folderView.getVisibility() != View.VISIBLE) {
                    binding.galleryView.setVisibility(View.GONE);
                    binding.folderView.setVisibility(View.VISIBLE);
                    binding.goBack.setVisibility(View.GONE);
                } else {
                    MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.GALLERY_CALLBACK);
                }

            }
        }
    }

    private Boolean boolean_folder = false;
    ArrayList<ImagesFolder> folders = new ArrayList<>();

    public ArrayList<ImagesFolder> SearchStorage() {
        folders.clear();
        folders = new ArrayList<>();
        spacecrafts = new ArrayList<>();
        int position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getActivity().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            for (int i = 0; i < folders.size(); i++) {
                if (folders.get(i).getAllFolderName() != null && cursor.getString(column_index_folder_name) != null) {
                    if (folders.get(i).getAllFolderName().equals(cursor.getString(column_index_folder_name))) {
                        boolean_folder = true;
                        position = i;
                        break;
                    } else {
                        boolean_folder = false;
                    }
                }
            }

            ArrayList<String> al_path = new ArrayList<>();
            if (boolean_folder) {
                al_path.addAll(folders.get(position).getAllImagePaths());
                al_path.add(absolutePathOfImage);
                folders.get(position).setAllImagePaths(al_path);
            } else {
                al_path.add(absolutePathOfImage);
                ImagesFolder obj_model = new ImagesFolder();
                obj_model.setAllFolderName(cursor.getString(column_index_folder_name));
                obj_model.setAllImagePaths(al_path);

                folders.add(obj_model);


            }


        }
        return folders;
    }
}
