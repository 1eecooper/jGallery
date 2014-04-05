package com.example.jgallery.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public abstract class ImageWorker {

    private static final int MESSAGE_CLEAR = 0;
    private static final int MESSAGE_FLUSH = 1;
    private static final int MESSAGE_CLOSE = 2;
    private static final int MESSAGE_INIT_DISK_CACHE = 4;

    protected Resources mResources;
    protected Bitmap mLoadingBitmap;

    public ImageWorker(Context context) {
        mResources = context.getResources();
    }

    protected abstract Bitmap processBitmap(Object data);

    public void loadBitmap (Object data, ImageView imageView) {
        final String imageKey = String.valueOf(data);
        final Bitmap bitmap = ImageCache.getInstance().getBitmapFromMemCache(imageKey);
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
            if (ImageCache.getInstance() != null) {
                bitmap = ImageCache.getInstance().getBitmapFromDiskCache(dataString);
            }
            if (bitmap == null) {
                Log.v(Utils.TAG,"_____ProcessBitmap");
                bitmap = processBitmap(data);
                ImageCache.getInstance().addBitmapToMemoryCache(dataString, bitmap);
                ImageCache.getInstance().addBitmapToDiskCache(dataString, bitmap);
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

    protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            switch ((Integer)params[0]) {
                case MESSAGE_CLEAR:
                    clearCacheInternal();
                    break;
                case MESSAGE_FLUSH:
                    flushCacheInternal();
                    break;
                case MESSAGE_CLOSE:
                    closeCacheInternal();
                    break;
                case MESSAGE_INIT_DISK_CACHE:
                    initDiskCacheInternal();
                    break;
            }
            return null;
        }
    }

    protected void clearCacheInternal() {
        if (ImageCache.getInstance() != null) {
            ImageCache.getInstance().clearCache();
        }
    }

    protected void flushCacheInternal() {
        if (ImageCache.getInstance() != null) {
            ImageCache.getInstance().flush();
        }
    }

    protected void closeCacheInternal() {
        if (ImageCache.getInstance() != null) {
            ImageCache.getInstance().close();
        }
    }

    protected void initDiskCacheInternal() {
        if (ImageCache.getInstance() != null) {
            ImageCache.getInstance().initDiskCache();
        }
    }

    public void clearCache() {
        new CacheAsyncTask().execute(MESSAGE_CLEAR);
    }

    public void flushCache() {
        new CacheAsyncTask().execute(MESSAGE_FLUSH);
    }

    public void closeCache() {
        new CacheAsyncTask().execute(MESSAGE_CLOSE);
    }

    public void initDiskCache() {
        new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
    }
}
