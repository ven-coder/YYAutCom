package com.example.levine.gitmakerautcom;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.levine.gitmakerautcom.tools.L;
import com.example.levine.gitmakerautcom.tools.ScreenUtils;
import com.example.levine.gitmakerautcom.tools.TShow;

import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 2016/11/17.
 */
public class MyService extends AccessibilityService {

    public static boolean isStart = true;

    /**
     * 当启动服务的时候就会被调用
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        L.w("服务启动了");
        isLaunch = true;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    /**
     * 页面变化回调事件
     *
     * @param event event.getEventType() 当前事件的类型;
     * 类型：
     * #TYPES_ALL_MASK：所有类型
     * #TYPE_VIEW_CLICKED ：单击
     * #TYPE_VIEW_LONG_CLICKED ：长按
     * #TYPE_VIEW_SELECTED ：选中
     * #TYPE_VIEW_FOCUSED ：获取焦点
     * #TYPE_VIEW_TEXT_CHANGED ：文字改变
     * #TYPE_WINDOW_STATE_CHANGED ：窗口状态改变
     * #TYPE_NOTIFICATION_STATE_CHANGED ：通知状态改变
     * #TYPE_VIEW_HOVER_ENTER
     * #TYPE_VIEW_HOVER_EXIT
     * #TYPE_TOUCH_EXPLORATION_GESTURE_START
     * #TYPE_TOUCH_EXPLORATION_GESTURE_END
     * #TYPE_WINDOW_CONTENT_CHANGED
     * #TYPE_VIEW_SCROLLED
     * #TYPE_VIEW_TEXT_SELECTION_CHANGED
     * #TYPE_ANNOUNCEMENT
     * #TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY
     * #TYPE_GESTURE_DETECTION_START
     * #TYPE_GESTURE_DETECTION_END
     * #TYPE_TOUCH_INTERACTION_START
     * #TYPE_TOUCH_INTERACTION_END
     * #TYPE_WINDOWS_CHANGED
     * event.getClassName() 当前类的名称;
     * event.getSource() 当前页面中的节点信息；
     * event.getPackageName() 事件源所在的包名
     */
    boolean isOpenComment = false;
    boolean isInput = false;
    public static boolean isLaunch = false;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    //所控制的app页面变化所执行的方法
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 事件页面节点信息不为空
        AccessibilityNodeInfo source = event.getSource();
        int eventType = event.getEventType();
        String eventText = "";

        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "TYPE_VIEW_FOCUSED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventText = "TYPE_VIEW_SELECTED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventText = "TYPE_VIEW_TEXT_CHANGED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventText = "TYPE_ANNOUNCEMENT";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventText = "TYPE_VIEW_HOVER_ENTER";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventText = "TYPE_VIEW_HOVER_EXIT";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventText = "TYPE_VIEW_SCROLLED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventText = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                L.i(eventText);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventText = "TYPE_WINDOW_CONTENT_CHANGED";
                L.i(eventText);
                break;
        }
        if (!isLaunch) {
            stopSelf(0);
        }
        if (source != null) {
            AccessibilityNodeInfo open = findTextView(getRootInActiveWindow(), "开播", 0);
            if (open != null && isStart == true) {
                isStart = false;
                TShow.showShort("点击个人中心");
                isStopHandle = false;
                next(1, 2000);
            }
        }
    }

    public class Hand extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:
                    AccessibilityNodeInfo a_1_1 = findTextView(getRootInActiveWindow(), "个人中心", 0);
                    if (a_1_1 != null) {
                        a_1_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(2, 2000);
                    } else {
                        TShow.showShort("个人中心点击失败");
                    }
                    break;
                case 2:
                    AccessibilityNodeInfo a_2_1 =
                            findIdView(getRootInActiveWindow(),
                                    "com.yy.mobile.plugin.main:id/rl_user_info_no_login", 0);
                    if (a_2_1 != null) {
                        a_2_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(3, 2000);
                    } else {
                        TShow.showShort("登陆点击失败");
                        next(7);
                    }
                    break;
                case 3:
                    //检查是否存在账号
                    AccessibilityNodeInfo a_3_2 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/show_list", 0);
                    if (a_3_2 != null) {
                        //存在账号
                        next(5, 2000);
                    } else {
                        //不存在账号直接点击输入框
                        AccessibilityNodeInfo a_3_1 = findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/EdtAccount");
                        if (a_3_1 != null) {
                            a_3_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            next(44, 2000);
                        } else {
                            TShow.showShort("输入框点击失败");
                            L.e("输入框点击失败");
                        }
                    }
                    break;
                case 4:
                    AccessibilityNodeInfo a_4_1 =
                            findIdView(getRootInActiveWindow(),
                                    "com.yy.mobile.plugin.main:id/EdtAccount", 0);
                    AccessibilityNodeInfo a_4_2 = findTextView(getRootInActiveWindow(), "YY帐号/手机号", 0);
                    if (a_4_2 != null) {
                        //直接输入账号
                        paste(a_4_2, "13148934781");
                    } else {
                        //需要删除账号
                        TShow.showShort("需要删除账号");
                    }
                    break;
                case 5:
                    //切换账号
                    AccessibilityNodeInfo a_5_1 =
                            findIdView(getRootInActiveWindow(),
                                    "com.yy.mobile.plugin.main:id/EdtAccount", 0);
                    if (a_5_1 != null) {
                        a_5_1.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                        next(6, 2000);
                    } else {
                        TShow.showShort("获取焦点失败");
                    }
                    break;
                case 6:
                    AccessibilityNodeInfo a_6_1 =
                            findIdView(getRootInActiveWindow(),
                                    "com.yy.mobile.plugin.main:id/EdtAccount", 0);
                    if (a_6_1 != null) {
                        a_6_1.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                        next(44, 2000);
                    } else {
                        TShow.showShort("长按失败");
                    }
                    break;
                case 7:
                    //检查是否已经登陆
                    AccessibilityNodeInfo a_7_1 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/tv_yy_number", 0);
                    if (a_7_1 != null) {
                        next(8, 2000);
                    } else {
                        L.e("未登录");
                    }
                    break;
                case 8:
                    //点击设置，准备退出登陆
                    AccessibilityNodeInfo a_8_1 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/iv_setting", 0);
                    if (a_8_1 != null) {
                        a_8_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(9, 2000);
                    } else {
                        L.e("点击设置失败");
                    }
                    break;
                case 9:
                    //点击退出
                    AccessibilityNodeInfo a_9_1 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/tv_logout", 0);
                    if (a_9_1 != null) {
                        a_9_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(10, 2000);
                    } else {
                        L.e("点击退出失败");
                    }
                    break;
                case 10:
                    //点击确定退出
                    AccessibilityNodeInfo a_10_1 =
                            findIdView(getRootInActiveWindow(), "com.duowan.mobile:id/cq", 0);
                    if (a_10_1 != null) {
                        a_10_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(2, 2000);
                    } else {
                        L.e("点击确定退出失败");
                    }
                    break;
                //===========================================================================//
                case 44:
                    //输入账号
                    AccessibilityNodeInfo a_44_1 = findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/EdtAccount");
                    if (a_44_1 != null) {
                        paste(a_44_1, "1314781");
                        next(55, 2000);
                    } else {
                        TShow.showShort("账号输入失败");
                    }
                    break;
                case 55:
                    //点击密码
                    AccessibilityNodeInfo a_55_1 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/EdtPassword");
                    if (a_55_1 != null) {
                        a_55_1.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
//                        a_55_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(66, 2000);
                    } else {
                        TShow.showShort("密码点击失败");
                    }
                    break;
                case 66:
                    //输入密码
                    AccessibilityNodeInfo a_66_1 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/EdtPassword");
                    if (a_66_1 != null) {
                        paste(a_66_1, "li109198");
                        next(77, 2000);
                    } else {
                        TShow.showShort("密码输入失败");
                    }
                    break;
                case 77:
                    //点击登陆
                    AccessibilityNodeInfo a_77_1 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/btn_login");
                    if (a_77_1 != null) {
                        a_77_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(88, 5000);
                    } else {
                        TShow.showShort("登陆点击失败");
                    }
                    break;
                case 88:
                    //检查是否登陆成功
                    AccessibilityNodeInfo a_88_1 = findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/tv_yy_number");
                    if (a_88_1 != null) {
                        //登陆成功
                        next(99, 2000);
                    } else {
                        TShow.showShort("登陆失败");
                    }
                    break;
                case 99:
                    //点击直播
                    AccessibilityNodeInfo a_99_1 = findTextView(getRootInActiveWindow(), "直播");
                    if (a_99_1 != null) {
                        a_99_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(100, 2000);
                    } else {
                        TShow.showShort("直播点击失败");
                    }
                    break;
                case 100:
                    //点击搜索
                    AccessibilityNodeInfo a_100_1 = findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/search_input");
                    if (a_100_1 != null) {
                        a_100_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        next(101);
                    } else {
                        TShow.showShort("搜索点击失败");
                    }
                    break;
                case 101:
                    //输入房间号
                    AccessibilityNodeInfo a_101_1 =
                            findIdView(getRootInActiveWindow(), "com.yy.mobile.plugin.main:id/search_input");
                    if (a_101_1 != null) {
                        paste(a_101_1, "991");
                        next(102);
                    } else {
                        TShow.showShort("房间号输入失败");
                        L.e("房间号输入失败");
                    }
                    break;
                case 102:
                    //点击进入房间
                    AccessibilityNodeInfo a_102_1 =
                            findIdView(getRootInActiveWindow(),
                                    "com.yy.mobile.plugin.main:id/ll_item");
                    if (a_102_1 != null) {
                        L.e("点击进入房间");
                        a_102_1.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    } else {
                        TShow.showShort("进入房间失败");
                        L.e("进入房间失败");
                    }
                    break;


            }
        }
    }


    private Hand hand = new Hand();
    private int commentCount = 0;//当前评论内容的位置
    public static boolean isStopHandle = false;

    public boolean examineStopHandle() {
        if (isStopHandle) {
            TShow.showShort(getApplication(), "已停止自动评论");
        }
        return isStopHandle;
    }

    private void next(final int i, long time) {
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (examineStopHandle()) {
                    return;
                }
                Message message = new Message();
                message.what = i;
                hand.sendMessage(message);
            }
        }, time);

    }

    private void next(final int i) {
        next(i, 2000);
    }

    /**
     * 滑动
     * 滑动比例 0~20
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void slideVertical(final int next, final long nextTime, int startSlideRatio, int stopSlideRatio) {
        int screenHeight = ScreenUtils.getScreenHeight(getApplicationContext());
        int screenWidth = ScreenUtils.getScreenWidth(getApplicationContext());
        L.e("屏幕：" + (screenHeight - (screenHeight / 10)) + "/" +
                (screenHeight - (screenHeight - (screenHeight / 10))) + "/" + screenWidth / 2);

        Path path = new Path();
        int start = (screenHeight / 20) * startSlideRatio;
        int stop = (screenHeight / 20) * stopSlideRatio;
        path.moveTo(screenWidth / 2, start);//如果只是设置moveTo就是点击
        path.lineTo(screenWidth / 2, stop);//如果设置这句就是滑动
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.
                        StrokeDescription(path,
                        200,
                        200))
                .build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                L.w("滑动结束" + gestureDescription.getStrokeCount());
                next(next, nextTime);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                L.w("滑动取消");
            }
        }, null);
    }

    /**
     * 点击屏幕
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clickScreen(final int next, final long nextTime, int xRatio, int yRatio) {
        int screenHeight = ScreenUtils.getScreenHeight(getApplicationContext());
        int screenWidth = ScreenUtils.getScreenWidth(getApplicationContext());
        int y = (screenHeight / 20) * yRatio;
        int x = (screenWidth / 20) * xRatio;
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.
                        StrokeDescription(path,
                        100,
                        50))
                .build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                L.w("点击结束" + gestureDescription.getStrokeCount());
                next(next, nextTime);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                L.w("点击取消");
            }
        }, null);
    }

    /**
     * 通过id找控件
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private AccessibilityNodeInfo findIdView(AccessibilityNodeInfo a, String id, int i) {
        if (a != null) {
            List<AccessibilityNodeInfo> as = a.findAccessibilityNodeInfosByViewId(id);
            if (!as.isEmpty()) {
                L.e("findIdView:" + as.size() + "===" + id);
                return as.get(i);
            } else {
                L.e("as:null");
            }
        } else {
            L.e("rootview:null");
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private AccessibilityNodeInfo findIdView(AccessibilityNodeInfo a, String id) {
        if (a != null) {
            List<AccessibilityNodeInfo> as = a.findAccessibilityNodeInfosByViewId(id);
            if (!as.isEmpty()) {
                return as.get(0);
            }
        }
        return null;
    }

    /**
     * 通过文字找控件
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private AccessibilityNodeInfo findTextView(AccessibilityNodeInfo a, String text, int i) {
        if (a != null) {
            List<AccessibilityNodeInfo> as = a.findAccessibilityNodeInfosByText(text);
            if (!as.isEmpty()) {
                return as.get(i);
            }
        }
        return null;
    }

    private AccessibilityNodeInfo findTextView(AccessibilityNodeInfo a, String text) {
        if (a != null) {
            List<AccessibilityNodeInfo> as = a.findAccessibilityNodeInfosByText(text);
            if (!as.isEmpty()) {
                return as.get(0);
            }
        }
        return null;
    }

    /**
     * 输入内容
     */
    public void paste(AccessibilityNodeInfo a, String text) {
        ClipData clip = ClipData.newPlainText(System.currentTimeMillis() + "", text);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clip);
        ClipData abc = clipboardManager.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        L.e("黏贴：" + item.getText().toString());
        a.performAction(AccessibilityNodeInfo.ACTION_PASTE);
    }

    /**
     * 判断当前界面是否是监测的app
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean isThis() {
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            return rootInActiveWindow.getPackageName().equals("com.smile.gifmaker") ? true : false;
        }
        return true;
    }




    /**
     * 中断AccessibilityService的反馈时调用
     */
    @Override
    public void onInterrupt() {
        L.w("服务中断了");
    }
}
