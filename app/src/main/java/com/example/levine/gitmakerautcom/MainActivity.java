package com.example.levine.gitmakerautcom;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.levine.gitmakerautcom.tools.AppUtils;
import com.example.levine.gitmakerautcom.tools.DateUtil;
import com.example.levine.gitmakerautcom.tools.KeyBoardUtils;
import com.example.levine.gitmakerautcom.tools.L;
import com.example.levine.gitmakerautcom.tools.PoPu;
import com.example.levine.gitmakerautcom.tools.SP;
import com.example.levine.gitmakerautcom.tools.SystemUtil;
import com.example.levine.gitmakerautcom.tools.T;
import com.example.levine.gitmakerautcom.tools.TShow;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        TextView tv_open_douyin = findViewById(R.id.tv_open_douyin);
        tv_open_douyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AppUtils.isInstallAppForPackageName(getActivity(), "com.duowan.mobile")) {
                    T.showDialog(getActivity(), "请先安装YY", false)
                            .setNegativeButton("确定", null).show();
                } else {
                    T.showDialog(getActivity(), "确定执行？", false)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startAPP("com.duowan.mobile");
                                }
                            }).show();
                }
            }
        });
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceIsRunning()) {
                    TShow.showShort(getActivity(), "服务已开启");
                } else {
                    startAccessibilityService();
                }
            }
        });

    }


    /**
     * 启动一个app
     */
    public void startAPP(String appPackageName) {
        try {
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        } catch (Exception e) {
            TShow.showShort(getActivity(), "抖音APP打开失败：" + e.getMessage());
        }
    }

    @Override
    public void finish() {
        super.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }


    public MainActivity getActivity() {
        return this;
    }

    /**
     * 判断自己的应用的AccessibilityService是否在运行
     *
     * @return
     */
    private boolean serviceIsRunning() {
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            if (info.service.getClassName().equals(getPackageName() + ".MyService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 前往设置界面开启服务
     */
    private void startAccessibilityService() {
        new AlertDialog.Builder(this)
                .setTitle("开启辅助功能")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("使用此项功能需要您开启辅助功能")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐式调用系统设置界面
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
