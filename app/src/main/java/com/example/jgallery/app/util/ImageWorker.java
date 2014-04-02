package com.example.jgallery.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.Objects;

public abstract class ImageWorker {

    protected Resources mResources;
    protected Bitmap mLoadingBitmap;
    private ImageCache mImageCache;

    public ImageWorker(Context context) {
        mResources = context.getResources();
        mImageCache = ImageCache.getInstance();
    }

    protected abstract Bitmap processBitmap(Object data);

    public void loadBitmap (Object data, ImageView imageView) {
        final String imageKey = String.valueOf(data);
        final Bitmap bitmap = mImageCache.getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            if (cancelPotentialWork(data, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, mLoadingBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(data);
            }
        }
    }

    public static boolean cancelPotentialWork(Object data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.data;
            if (bitmapData == null || !bitmapData.equals(data)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public void setLoadingImage(int resId) {
        mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
    }

    class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private Object data = null;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            data = params[0];
            String dataString = String.valueOf(data);
            Bitmap bitmap = null;
            if (mImageCache != null) {
                bitmap = mImageCache.getBitmapFromDiskCache(dataString);
            }
            if (bitmap == null) {
                bitmap = processBitmap(data);
                mImageCache.addBitmapToMemoryCache(dataString, bitmap);
                mImageCache.addBitmapToDiskCache(dataString, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            final ImageView imageView = getAttachedImageView();
            if (imageView != null && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
            }
        }

        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask) {
                return imageView;
            }
            return null;
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }
}
