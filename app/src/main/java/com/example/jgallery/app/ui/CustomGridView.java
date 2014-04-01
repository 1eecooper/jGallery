package com.example.jgallery.app.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomGridView extends GridView {

    private Context mContext;
    private int viewWidth = 0;
    private int viewHeight = 0;

    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        updateAdapter();
    }

    public int getViewHeight() {
        return viewHeight;
    }

    private void updateAdapter(){
        if (mContext instanceof MainActivity) {
            int imageHeight = 0;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageHeight = (viewHeight / 2);
            } else {
                imageHeight = (viewHeight / 5);
            }
            ((MainActivity) mContext).getAdapter().setImageHeight(imageHeight);
            ((MainActivity) mContext).getImageDecoder().setImageHeight(imageHeight);
        }
    }
}
