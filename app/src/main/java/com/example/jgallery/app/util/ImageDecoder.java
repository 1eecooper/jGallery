package com.example.jgallery.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDecoder extends ImageWorker {

    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private int mImageWidth = 0;
    private int mImageHeight = 0;

    public ImageDecoder(Context context) {
        super(context);
    }

    @Override
    protected Bitmap processBitmap(Object data) {
        if (data instanceof Integer) {
            return decodeBitmapFromResource(mResources, (Integer) data, mImageWidth, mImageHeight);
        } else if (data instanceof String && ((String)data).startsWith("/storage")) {
            return decodeBitmapFromFile((String)data, mImageWidth, mImageHeight);
        } else if (data instanceof String && ((String)data).startsWith("https://")) {
            return decodeBitmapFromUrl((String)data, mImageWidth, mImageHeight);
        }
        return mLoadingBitmap;
    }

    public void setImageWidth(int width) {
        mImageWidth = width;
    }

    public void setImageHeight(int height) {
        mImageHeight = height;
    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, ImageCache.getInstance());
        }
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFromFile(String fileName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, ImageCache.getInstance());
        }
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(fileName, options);
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        if (Utils.hasHoneycomb()) {
            addInBitmapOptions(options, ImageCache.getInstance());
        }
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static Bitmap decodeBitmapFromUrl(String urlPath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlPath);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.reset();
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            if (Utils.hasHoneycomb()) {
                addInBitmapOptions(options, ImageCache.getInstance());
            }
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeStream(in, null, options);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {}
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static void addInBitmapOptions(BitmapFactory.Options options, ImageCache cache) {
        options.inMutable = true;
        if (cache != null) {
            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);
            if (inBitmap != null) {
                options.inBitmap = inBitmap;
            }
        }
    }
}
