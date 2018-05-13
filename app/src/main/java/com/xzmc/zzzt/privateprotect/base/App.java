package com.xzmc.zzzt.privateprotect.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chatuidemo.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.chatuidemo.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xzmc.zzzt.privateprotect.Utils.PhotoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zw on 17/5/7.
 */

public class App extends Application {
    public static App ctx;
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
    public static boolean debug = true;
    private List<Activity> list = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();
        ctx = this;
        //	JPushInterface.setLatestNotificationNumber(getApplicationContext(), 5);
        // 1、分享的初始化
        initImageLoader(ctx);
        openStrictMode();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase("com.xzmc.health")) {
            //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        } else {
            EMChat.getInstance().init(ctx);
            hxSDKHelper.onInit(ctx);
            EMChat.getInstance().setDebugMode(true);
        }
    }


    public static Activity getCurrentActivity() {
        return activityList.get(0);
    }



    /**
     * 用来记录foreground Activity
     */
    public static List<Activity> activityList = new ArrayList<Activity>();

    public static void pushActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(0, activity);
        }
        Log.i("WebService", activity.toString());
    }

    public static void popActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(emCallBack);
    }
    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "PP+/Cache");
        ImageLoaderConfiguration config = PhotoUtils.getImageLoaderConfig(
                context, cacheDir);
        ImageLoader.getInstance().init(config);
    }

    public void openStrictMode() {
        if (App.debug) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().build());
        }
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;


    }


    /**
     * 获取当前登陆用户名
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }
    public static App getInstance() {
        if (null == ctx) {
            ctx = new App();
        }
        return ctx;
    }
    /**
     * 获取内存中好友user list
     * @return
     */
    public Map<String, User> getContactList() {
        return hxSDKHelper.getContactList();
    }
    /**
     * 设置好友user list到内存中
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);
    }
    public void addActivity(Activity activity) {
        list.add(activity);
    }
}
