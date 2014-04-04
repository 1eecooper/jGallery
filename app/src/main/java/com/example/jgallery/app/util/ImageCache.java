package com.example.jgallery.app.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageCache {

    private Context mContext;

    private static final int DISK_CACHE_INDEX = 0;
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;

    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    public static final String DISK_CACHE_SUBDIR = "thumbnails";

    private ImageCache() {
    }

    public void initMemoryCache() {
        RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(((Activity) mContext).getFragmentManager());
        mMemoryCache = retainFragment.mRetainedCache;
        if (mMemoryCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };
            retainFragment.mRetainedCache = mMemoryCache;
        }
    }

    public void initDiskCache() {
        File cacheDir = getDiskCacheDir(mContext, DISK_CACHE_SUBDIR);
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null && !mDiskCache.isClosed()) {
                mDiskCache = null;
            }
            try {
                mDiskCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }

    public static ImageCache getInstance(){
        return ImageCacheHolder.INSTANCE;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public void addBitmapToDiskCache(String data, Bitmap bitmap) {
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                final String key = hashKeyForDisk(data);
                OutputStream out = null;
                try {
                    DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                    if (snapshot == null) {
                        final DiskLruCache.Editor editor = mDiskCache.edit(key);
                        if (editor != null) {
                            out = editor.newOutputStream(DISK_CACHE_INDEX);
                            bitmap.compress(
                                    DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, out);
                            editor.commit();
                            out.close();
                        }
                    } else {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {}
                }
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        Log.v(Utils.TAG,"_____getBitmapFromMemCache");
        return mMemoryCache.get(key);
    }

    public Bitmap getBitmapFromDiskCache(String data) {
        final String key = hashKeyForDisk(data);
        Bitmap bitmap = null;

        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (mDiskCache != null) {
                InputStream inputStream = null;
                try {
                    final DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                    if (snapshot != null) {
                        inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
                        if (inputStream != null) {
                            FileDescriptor fd = ((FileInputStream) inputStream).getFD();
                            Log.v(Utils.TAG,"_____getBitmapFromDiskCache");
                            bitmap = ImageDecoder.decodeSampledBitmapFromDescriptor(fd, Integer.MAX_VALUE, Integer.MAX_VALUE);
                        }
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {}
                }
            }
            return bitmap;
        }
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalCacheDir();
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }


    public static class ImageCacheHolder {
        public static final ImageCache INSTANCE = new ImageCache();
    }

    class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                try {
                    mDiskCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mDiskCacheStarting = false;
                mDiskCacheLock.notifyAll();
            }
            return null;
        }
    }

    static class RetainFragment extends Fragment {
        private static final String TAG = "RetainFragment";
        public LruCache<String, Bitmap> mRetainedCache;

        public RetainFragment() {}

        public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
            RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
            if (fragment == null) {
                fragment = new RetainFragment();
                fm.beginTransaction().add(fragment, TAG).commit();
            }
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }
    }

    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;
            if (mDiskCache != null && !mDiskCache.isClosed()) {
                try {
                    mDiskCache.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mDiskCache = null;
                initDiskCache();
            }
        }
    }

    public void flush() {
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                try {
                    mDiskCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        synchronized (mDiskCacheLock) {
            if (mDiskCache != null) {
                try {
                    if (!mDiskCache.isClosed()) {
                        mDiskCache.close();
                        mDiskCache = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setContext(Context c) {
        mContext = c;
    }
}
