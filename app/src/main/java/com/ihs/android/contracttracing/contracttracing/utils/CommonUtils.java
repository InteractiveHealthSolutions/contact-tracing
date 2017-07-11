package com.ihs.android.contracttracing.contracttracing.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by MaazAhmad on 6/20/2017.
 */

public class CommonUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
