package com.himangi.imagepreview;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Himangi on 20/4/18
 */
public class Util {

    public static void saveImageToGallery(Context context, Bitmap bitmap) {
        File photo =
                new File(getAppFolder(context),
                        SystemClock.currentThreadTimeMillis() + ".jpg");
        try {

            FileOutputStream fos = new FileOutputStream(photo.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            if (photo.exists()) {
                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.DATA, photo.getAbsolutePath());

                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("Picture", "Exception in photoCallback", e);
        }
    }

    public static Uri shareImage(Context context, Bitmap bitmap) {
        File photo =
                new File(getAppFolder(context),
                        SystemClock.currentThreadTimeMillis() + ".jpg");
        try {

            FileOutputStream fos = new FileOutputStream(photo.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            if (photo.exists()) {
                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.DATA, photo.getAbsolutePath());

                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
        } catch (Exception e) {
            Log.e("Picture", "Exception in photoCallback", e);
        }
        return null;
    }

    private static String getAppName(Context context) {
        final PackageManager pm = context.getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    private static File getWorkingDirectory() {
        File directory =
                new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID);
        if (!directory.exists()) {
            directory.mkdir();
        }
        return directory;
    }

    public static File getAppFolder(Context context) {
        File photoDirectory = new File(getWorkingDirectory().getAbsolutePath(),
                getAppName(context.getApplicationContext()));
        if (!photoDirectory.exists()) {
            photoDirectory.mkdir();
        }
        return photoDirectory;

    }

}

