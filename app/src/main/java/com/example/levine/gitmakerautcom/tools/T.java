package com.example.levine.gitmakerautcom.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.levine.gitmakerautcom.R;

/**
 * Toast统一管理类
 */
public class T {

    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * popuwindow
     */
    public static PoPu showPopu(View showView, View asView, int width, int height, int gravity) {
        PoPu poPu = new PoPu(showView, width, height);
        poPu.setFocusable(true);
        poPu.setAnimationStyle(R.style.pw_details_fuwu);
        poPu.setBackgroundDrawable(new ColorDrawable());
        poPu.setDarkStyle(-1);//动画
        poPu.setDarkColor(Color.parseColor("#a0000000"));//颜色
        poPu.resetDarkPosition();
        poPu.showAtLocation(asView, gravity, 0, 0);
        return poPu;
    }

    public static PoPu showPopu(View showView, View asView, int gravity) {
        return showPopu(showView, asView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, gravity);
    }

    /**
     * 提示
     */
    public static AlertDialog.Builder showDialog(Context context, String messege, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messege);
        builder.setCancelable(cancelable);
        return builder;
    }

    /**
     * 带图标提示
     */
    public static AlertDialog.Builder showDialog(Context context, String message, int icon, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setIcon(icon);
        builder.setCancelable(cancelable);
        return builder;
    }


    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }


}