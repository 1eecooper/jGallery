package com.example.jgallery.app.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jgallery.app.R;
import com.example.jgallery.app.adapter.ImageAdapter;
import com.example.jgallery.app.util.ImageCache;
import com.example.jgallery.app.util.ImageDecoder;

public class MainActivity extends Activity {

    private ImageAdapter mAdapter;
    private ImageDecoder mImageDecoder;
    private ImageCache mImageCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        CustomGridView gridView = (CustomGridView)findViewById(R.id.gridview);
        mImageCache = ImageCache.getInstance();
        mImageCache.setContext(this);
        mImageCache.initMemoryCache();
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
        mImageDecoder.initDiskCache();
        gridView.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageDecoder.flushCache();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ImageCache.getDiskCacheDir(this, ImageCache.DISK_CACHE_SUBDIR).exists()) {
            mImageDecoder.initDiskCache();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageDecoder.closeCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageDecoder.clearCache();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public ImageAdapter getAdapter(){
        return mAdapter;
    }
    public ImageDecoder getImageDecoder() {
        return mImageDecoder;
    }
}
