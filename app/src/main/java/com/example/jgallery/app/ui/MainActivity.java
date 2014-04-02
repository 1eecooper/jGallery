package com.example.jgallery.app.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.jgallery.app.R;
import com.example.jgallery.app.adapter.ImageAdapter;
import com.example.jgallery.app.util.ImageCache;
import com.example.jgallery.app.util.ImageDecoder;

public class MainActivity extends Activity {

    private ImageAdapter mAdapter;
    private ImageDecoder mImageDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        CustomGridView gridView = (CustomGridView)findViewById(R.id.gridview);
        ImageCache.getInstance().init(this);
        mImageDecoder = new ImageDecoder(this);
        mImageDecoder.setLoadingImage(R.drawable.def);
        mAdapter = new ImageAdapter(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int imageWidth;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.setNumColumns(5);
            imageWidth = metrics.widthPixels / 5;
        } else {
            gridView.setNumColumns(2);
            imageWidth = metrics.widthPixels / 2;
        }
        mAdapter.setImageWidth(imageWidth);
        mImageDecoder.setImageWidth(imageWidth);
        gridView.setAdapter(mAdapter);
    }

    public ImageAdapter getAdapter(){
        return mAdapter;
    }
    public ImageDecoder getImageDecoder() {
        return mImageDecoder;
    }
}
