package com.example.levine.gitmakerautcom.tools;

import android.util.Log;

/**
 * Created by Levine on 2018/3/17.
 */

public class L {
    private static final String TAG = "123456";

    public static void e(String s) {
        Log.e(TAG, "=====" + s + "=====");
    }

    public static void w(String s) {
        Log.w(TAG, "=====" + s + "=====");
    }

    public static void i(String s) {
        Log.i(TAG, "=====" + s + "=====");
    }
}
