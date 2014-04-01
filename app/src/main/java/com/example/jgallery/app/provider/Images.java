package com.example.jgallery.app.provider;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Images {

    static File mRootPicDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
    static ArrayList<String> mPicDirPaths;

    private static void setDirectoryList(File file) {
        File files [] = file.listFiles();
        for (File f: files) {
            if (f.isDirectory()) {
                mPicDirPaths.add(f.toString());
                setDirectoryList(f);
            }
        }
    }

    public static ArrayList<String> getImagesFromDevice() {
        mPicDirPaths = new ArrayList<String>();
        mPicDirPaths.add(mRootPicDir.toString());
        setDirectoryList(mRootPicDir);

        ArrayList<String> imagePaths = new ArrayList<String>();
        for (String str: mPicDirPaths) {
            File dir = new File(str);
            File [] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].toString().toLowerCase().endsWith(".jpg") ||
                        files[i].toString().toLowerCase().endsWith(".png")) {
                    imagePaths.add(files[i].toString());
                }
            }
        }
        return imagePaths;
    }

    public static ArrayList<String> getImagesFromUrl() {
        ArrayList<String> imagePaths = new ArrayList<String>();
        imagePaths.add("https://lh6.googleusercontent.com/4gXhuW2H95K53n78egdvyqNRVVXTcaJ3_8OLPPdGhQ=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/QDGX8fwASMZeBqLjvJNs-4XTDRFtBMYsXtdtMTitaQ=w338-h211-p-no");
        imagePaths.add("https://lh6.googleusercontent.com/Iu5xUed_w7ZScwnE7TbLnJN2JxwGex6433qnZ7jzRQ=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/cuo8PWMGAXsRJv0lwxITgBnJuGHMKj2IgDQHCMb73Q=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/_FnON-Eq1QoKFr9yJwGHhCckjKuiCMBr5c1F8yIhPw=w338-h211-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/f3XzWulYVD1jBA4Jdc2ijUmS5gYkbF7UkYWgnQIuyg=w334-h208-p-no");
        imagePaths.add("https://lh5.googleusercontent.com/OGIF3udxdkfLViq5NtHHi_nh64Cf6AWpxGlws7LitnQ=w334-h208-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/AdsjDJixWEv9DB5fCngvIdIOadaMOIDQnIR6VZOV9w=w334-h208-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/zT1mYrIdKkZYSbGJ487VfLP1Vvby58ly_cGUnA9GPQ=w334-h208-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/0QJk24DV24VUgFh7VFqXpL7BfrEYtPftB3vJsnOjIQ=w334-h208-p-no");
        imagePaths.add("https://lh4.googleusercontent.com/g54LIav50BYBZEx3KAOf5Q1Ye-3rnoDpWQwq9crzbQ=w334-h208-p-no");
        imagePaths.add("https://lh3.googleusercontent.com/wHHUz57LQmfY_9OHx3ZrmlpkGEiljgzz6ndvfCcSmg=w334-h208-p-no");
        return imagePaths;
    }
}
