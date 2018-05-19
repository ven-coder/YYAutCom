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

    private TextView tv;
    private RecyclerView recyclerview;
    private MyAdapter adapter;
    private View viewInput;
    public static List<String> comments = new ArrayList<>();
    private EditText et_frequency;
    public static int frequency = 30;//步骤执行频率
    public static Activity activity;
    private TextView tv_code_valid;
    public static CheckBox cb_time;
    public static CheckBox cb_time_end;

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
        tv = findViewById(R.id.tv);
        final FrameLayout fl_rv = findViewById(R.id.fl_rv);
        cb_time = findViewById(R.id.cb_time);
        cb_time_end = findViewById(R.id.cb_time_end);

        cb_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fl_rv.setVisibility(View.GONE);
                    cb_time_end.setVisibility(View.GONE);
                } else {
                    fl_rv.setVisibility(View.VISIBLE);
                    cb_time_end.setVisibility(View.VISIBLE);
                }
            }
        });
        TextView tv_open_douyin = findViewById(R.id.tv_open_douyin);
        TextView close = findViewById(R.id.close);
        TextView tv_add = findViewById(R.id.tv_add);
        et_frequency = findViewById(R.id.et_frequency);
        tv_code_valid = findViewById(R.id.tv_code_valid);
        et_frequency.setText("30");
        recyclerview = findViewById(R.id.recyclerview);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addData(new BaseEntity(BaseEntity.ITEMTYPE1, ""));
                recyclerview.smoothScrollToPosition(adapter.getData().size() - 1);
            }
        });
        tv_open_douyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!gatherComment(true)) {
                    return;
                }
                String trim = et_frequency.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    frequency = Integer.parseInt(trim);
                } else {
                    TShow.showShort(getActivity(), "请设置有效的执行频率");
                    return;
                }


                if (!AppUtils.isInstallAppForPackageName(getActivity(), "com.duowan.mobile")) {
                    T.showDialog(getActivity(), "请先安装快手APP", false)
                            .setNegativeButton("确定", null).show();
                } else {
                    T.showDialog(getActivity(), "确定执行？", false)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MyService.isStart = true;
                                    SP.put(SP.FREQUENCY, frequency + "");
                                    startAPP("com.duowan.mobile");
                                }
                            }).show();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TShow.showShort(getActivity(), "已停止监测服务");
                MyService.isLaunch = false;
                MyService.isStopHandle = true;
            }
        });
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyService.isLaunch = true;
                MyService.isStart = true;
                MyService.clickCount = 0;
                if (serviceIsRunning()) {
                    MyService.isStart = true;
                    TShow.showShort(getActivity(), "服务已开启");
                } else {
                    startAccessibilityService();
                }
            }
        });


        List<BaseEntity> baseEntities = new ArrayList<>();
        String s = readerRecord();
        if (!TextUtils.isEmpty(s)) {
            Record record = new Gson().fromJson(s, Record.class);
            for (String string : record.getRecord()) {
                baseEntities.add(new BaseEntity(BaseEntity.ITEMTYPE1, string + ""));
            }
        } else {
            baseEntities.add(new BaseEntity(BaseEntity.ITEMTYPE1, ""));
        }

        adapter = new MyAdapter(baseEntities);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerview.setAdapter(adapter);

//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion < Build.VERSION_CODES.N) {
//            T.showDialog(getActivity(), "抱歉，由于你的系统版本低于7.0，所以无法使用该APP", false)
//                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
//                        }
//                    }).show();
//        }
        TextView tv_out = findViewById(R.id.tv_out);
        tv_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T.showDialog(getActivity(), "确定退出授权？\n提示：退出授权将重置所有内容", false)
                        .setPositiveButton("取消", null)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveSearchRecord("");
                                SP.clearAll(getActivity());
                                startActivity(new Intent(getActivity(), AuthActivity.class));
                            }
                        }).show();
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

    public void auth(final String code) {
        OkGo.post("https://ra.jade-box.com/comments/code/bind")
                .params("code", code + "")
                .params("device", SystemUtil.getDeviceBrand() + "")
                .params("imei", getUniqueId(this).substring(0, 15))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        EntityAuth entityAuth;
                        try {
                            entityAuth = new Gson().fromJson(s, EntityAuth.class);
                        } catch (Exception e) {
                            onError(call, response, null);
                            return;
                        }
                        if (entityAuth.getCode() == 201) {
                            tv_code_valid.setText("授权码有效期至：" + DateUtil.stampToDate(entityAuth.getData().getExpire_time() + ""));
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Message message = new Message();
                                    message.what = 1;
                                    hand.handleMessage(message);
                                }
                            }, 10000);
                        } else if (entityAuth.getCode() == 403) {
                            MyService.isLaunch = false;
                            MyService.isStopHandle = true;
                            T.showDialog(getActivity(), "授权码已过期，请重新获取授权码", false)
                                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    }).show();

                        } else {
                            MyService.isLaunch = false;
                            MyService.isStopHandle = true;
                            T.showDialog(getActivity(), "授权码验证失败：" + entityAuth.getMsg(), false)
                                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        MyService.isLaunch = false;
                        MyService.isStopHandle = true;
                        T.showDialog(getActivity(), "授权码验证失败，请检查网络或稍后重试", false)
                                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }).show();
                    }
                });
    }

    private Hand hand = new Hand();

    public class Hand extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String code = SP.getString(SP.CODE);
                    auth(code);
                    break;
            }
        }
    }

    private class Record {

        private List<String> record;

        public List<String> getRecord() {
            return record;
        }

        public void setRecord(List<String> record) {
            this.record = record;
        }
    }

    private final String FILE_NAME = "record";//搜索记录文件名

    public String recordToJson() {
        gatherComment(false);
        String s = new Gson().toJson(comments);
        L.e("{\"record\":" + s + "}");
        return "{\"record\":" + s + "}";
    }

    /**
     * 保存搜索记录
     */
    public boolean saveSearchRecord(String json) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            FileOutputStream outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(json);
            outputStreamWriter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 读取本地保存的记录
     */
    public String readerRecord() {
        BufferedReader bufferedReader = null;
        try {
            FileInputStream fileInputStream = openFileInput(FILE_NAME);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 遍历评论
     */
    public boolean gatherComment(boolean isExamine) {
        comments.clear();
        for (BaseEntity s : adapter.getData()) {
            if (!TextUtils.isEmpty(s.getData() + "")) {
                comments.add(s.getData() + "");
            }
        }
        if (comments.size() == 0 && isExamine) {
            TShow.showShort(getActivity(), "评论不能为空");
            return false;
        }
        return true;
    }

    public void showInputView(final int position) {
        viewInput = getLayoutInflater().inflate(R.layout.view_input, null);
        LinearLayout ll_text = viewInput.findViewById(R.id.ll_text);
        FrameLayout fl_parent = viewInput.findViewById(R.id.fl_parent);
        final EditText et_text = viewInput.findViewById(R.id.et_text);
        TextView tv_ok = viewInput.findViewById(R.id.tv_ok);
        TextView tv_cancel = viewInput.findViewById(R.id.tv_cancel);

        et_text.setText(adapter.getData().get(position).getData() + "");
        et_text.setSelection(et_text.getText().toString().length());
        final PoPu poPu = T.showPopu(viewInput,
                getWindow().getDecorView(),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.BOTTOM);
        KeyBoardUtils.openKeybord(et_text, getActivity());

        fl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtils.closeKeybord(et_text, getActivity());
                poPu.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getData().get(position).setData(et_text.getText().toString().trim() + "");
                adapter.notifyItemChanged(position);

                KeyBoardUtils.closeKeybord(et_text, getActivity());
                poPu.dismiss();

                saveSearchRecord(recordToJson());
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtils.closeKeybord(et_text, getActivity());
                poPu.dismiss();
            }
        });
    }

    private class MyAdapter extends BaseMultiItemQuickAdapter<BaseEntity, BaseViewHolder> {

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        public MyAdapter(List<BaseEntity> data) {
            super(data);
            addItemType(BaseEntity.ITEMTYPE1, R.layout.item);
        }

        @Override
        protected void convert(final BaseViewHolder helper, BaseEntity item) {
            switch (item.getItemType()) {
                case BaseEntity.ITEMTYPE1:
                    helper.setText(R.id.tv_sum, "" + (helper.getAdapterPosition() + 1) + ".");
                    TextView tv_text = helper.getView(R.id.tv_text);
                    TextView tv_edit = helper.getView(R.id.tv_edit);
                    TextView tv_delete = helper.getView(R.id.tv_delete);
                    tv_text.setText(item.getData() + "");
                    tv_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showInputView(helper.getAdapterPosition());
                        }
                    });
                    tv_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showInputView(helper.getAdapterPosition());
                        }
                    });
                    tv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (adapter.getData().size() == 1) {
                                TShow.showShort(getActivity(), "至少需要有一个评论");
                                return;
                            }
                            adapter.remove(helper.getAdapterPosition());
                            saveSearchRecord(recordToJson());
                        }
                    });
                    break;
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        gatherComment(false);
        String frequency = SP.getString(SP.FREQUENCY);
        if (!TextUtils.isEmpty(frequency)) {
            MainActivity.frequency = Integer.parseInt(frequency);
        } else {
            MainActivity.frequency = 30;
        }
        et_frequency.setText(MainActivity.frequency + "");
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
