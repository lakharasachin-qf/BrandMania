package com.make.mybrand.Model;

import java.util.ArrayList;

public class ImagesFolder {
    public String AllFolderName;
    public ArrayList<String> AllImagePaths;

    public String getAllFolderName() {
        return AllFolderName;
    }

    public ImagesFolder setAllFolderName(String allFolderName) {
        AllFolderName = allFolderName;
        return this;
    }

    public ArrayList<String> getAllImagePaths() {
        return AllImagePaths;
    }

    public ImagesFolder setAllImagePaths(ArrayList<String> allImagePaths) {
        AllImagePaths = allImagePaths;
        return this;
    }
}
