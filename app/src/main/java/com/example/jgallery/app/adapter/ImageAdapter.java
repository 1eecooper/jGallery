package com.example.jgallery.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.jgallery.app.R;
import com.example.jgallery.app.provider.Images;
import com.example.jgallery.app.ui.MainActivity;
import com.example.jgallery.app.util.ImageDecoder;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ImageDecoder mImageDecoder;
    private ArrayList<String> mThumbFromPictureFolder;
    private GridView.LayoutParams mGridViewLayoutParams = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public ImageAdapter(Context c){
        mContext = c;
        if (mContext instanceof MainActivity) {
            mImageDecoder = ((MainActivity)mContext).getImageDecoder();
        }
        mThumbFromPictureFolder = Images.getImagesFromDevice();
    }

    @Override
    public int getCount() {
        return mThumbFromPictureFolder.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null || view.getHeight() != mGridViewLayoutParams.height) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(mGridViewLayoutParams);
        } else {
            imageView = (ImageView) view;
        }
        mImageDecoder.loadBitmap(mThumbFromPictureFolder.get(i), imageView);
        return imageView;
    }

    public void setImageHeight(int height) {
        mGridViewLayoutParams.height = height;
    }

    public void setImageWidth(int width) {
        mGridViewLayoutParams.width = width;
    }
}
