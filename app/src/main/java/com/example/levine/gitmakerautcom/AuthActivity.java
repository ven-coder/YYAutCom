package com.example.levine.gitmakerautcom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.levine.gitmakerautcom.tools.L;
import com.example.levine.gitmakerautcom.tools.SP;
import com.example.levine.gitmakerautcom.tools.SystemUtil;
import com.example.levine.gitmakerautcom.tools.T;
import com.example.levine.gitmakerautcom.tools.TShow;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Levine on 2018/4/4.
 */

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String code = SP.getString(SP.CODE);
        if (!TextUtils.isEmpty(code)) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.view_auth);


        TextView tv_ok = findViewById(R.id.tv_ok);
        final EditText et_code = findViewById(R.id.et_code);


        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = et_code.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    TShow.showShort(getActivity(), "请输入授权码");
                    return;
                }

                auth(trim + "");
            }
        });

    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                if (MainActivity.activity != null) {
                    MainActivity.activity.finish();
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public AuthActivity getActivity() {
        return this;
    }

    public void auth(final String code) {
        final AlertDialog alertDialog = T.showDialog(getActivity(), "正在验证授权码，请稍后...", false)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OkGo.getInstance().cancelAll();
                        finish();
                    }
                }).create();
        alertDialog.show();
        OkGo.post("https://ra.jade-box.com/comments/code/bind")
                .params("code", code + "")
                .params("device", SystemUtil.getDeviceBrand() + "")
                .params("imei", getUniqueId(this).substring(0, 15))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        alertDialog.dismiss();
                        EntityAuth entityAuth;
                        try {
                            entityAuth = new Gson().fromJson(s, EntityAuth.class);
                        } catch (Exception e) {
                            onError(call, response, null);
                            return;
                        }
                        if (entityAuth.getCode() == 201) {
                            SP.put(SP.CODE, code + "");
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            TShow.showShort(getActivity(), "授权码验证成功");
                            finish();
                        } else if (entityAuth.getCode() == 403) {
                            TShow.showShort(getActivity(), "该授权码已过期");
                            initView();
                        } else {
                            TShow.showShort(getActivity(), "" + entityAuth.getMsg());
                            initView();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        initView();
                        alertDialog.dismiss();
                        TShow.showShort(getActivity(), "授权码验证失败，请检查网络或稍后重试");
                    }
                });
    }

    public void initView() {
        setContentView(R.layout.view_auth);

        TextView tv_ok = findViewById(R.id.tv_ok);
        final EditText et_code = findViewById(R.id.et_code);


        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = et_code.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    TShow.showShort(getActivity(), "请输入授权码");
                    return;
                }
                auth(trim + "");
            }
        });
    }

    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        L.i(id);
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }


    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }
}
