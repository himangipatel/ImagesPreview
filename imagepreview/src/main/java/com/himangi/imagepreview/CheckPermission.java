package com.himangi.imagepreview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

/**
 * Created by Himangi on 20/4/18
 */
public class CheckPermission {

    public static final int PERMISSION_STORAGE = 100;

    public static boolean hasPermission(String permission, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestPerm(String[] permissions, int requestCode, Activity activity) {
        activity.requestPermissions(permissions, requestCode);
    }
}
