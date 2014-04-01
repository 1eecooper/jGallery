package com.example.jgallery.app.util;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;

    private ImageCache() {
        init();
    }

    private void init() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public static ImageCache getInstance(){
        return ImageCacheHolder.INSTANCE;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static class ImageCacheHolder {
        public static final ImageCache INSTANCE = new ImageCache();
    }
}
