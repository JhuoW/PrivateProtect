package com.xzmc.zzzt.privateprotect.Utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zw on 17/5/8.
 */

public class Utils {

    public static int getColor(int resId) {
        return App.ctx.getResources().getColor(resId);
    }
    public static String transObject2Json(Object newlist, String key) {
        StringBuffer jsonstr = new StringBuffer();
        String str = com.alibaba.fastjson.JSON.toJSONString(newlist);
        jsonstr.append("{\"" + key + "\":");
        jsonstr.append(str);
        jsonstr.append("}");
        String result=jsonstr.toString();
        return result;
    }
    /**
     * get user id
     * @return
     */
    public static String getID() {
        String id = new PreferenceMap(App.ctx).getUser().getID();
        return id;
    }
    /**
     * get user id
     * @return
     */
    public static User getUser() {
        User user = new PreferenceMap(App.ctx).getUser();
        return user;
    }

    /**
     * get the app version
     *
     * @return
     */
    public static String getVersion() {
        PackageManager manager;
        PackageInfo info = null;
        manager = App.ctx.getPackageManager();
        try {
            info = manager.getPackageInfo(App.ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static List<Camera.Size> getResolutionList(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        return previewSizes;
    }

    public static class ResolutionComparator implements Comparator<Camera.Size> {


        @Override
        public int compare(Camera.Size o1, Camera.Size o2) {
            if (o1.height != o2.height)
                return o1.height - o2.height;
            else
                return o1.width - o2.width;
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static void setEditTextLastPosition(EditText edittext) {
        String text = edittext.getEditableText().toString();
        edittext.setSelection(text.length());
    }

    public static void goActivity(Context cxt, Class<?> clz) {
        Intent intent = new Intent(cxt, clz);
        cxt.startActivity(intent);
        ((Activity) cxt).overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    public static void goActivityAndFinish(Activity cxt, Class<?> clz) {
        Intent intent = new Intent(cxt, clz);
        cxt.startActivity(intent);
        ((Activity) cxt).overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        cxt.finish();
    }

    public static void fixAsyncTaskBug() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
        }.execute();
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx
                .getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    /**
     * 隐藏软键盘显示
     *
     * @param activity
     */
    public static void hideSoftInputView(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager manager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 过滤异常信息
     *
     * @param e
     * @return
     */
    public static boolean filterException(Exception e) {
        if (e != null) {
            Utils.toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    public static boolean isListNotEmpty(Collection<?> collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }




    public static String uuid() {
        // return UUID.randomUUID().toString().substring(0, 24);
        return myUUID();
    }

    public static void appendChar(StringBuilder sb, int start, int end) {
        int i;
        for (i = start; i < end; i++) {
            sb.append((char) i);
        }
    }

    public static String myUUID() {
        StringBuilder sb = new StringBuilder();
        int start = 48, end = 58;
        appendChar(sb, start, end);
        appendChar(sb, 65, 90);
        appendChar(sb, 97, 123);
        String charSet = sb.toString();
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            int len = charSet.length();
            int pos = new Random().nextInt(len);
            sb1.append(charSet.charAt(pos));
        }
        return sb1.toString();
    }



    public static void inputToOutput(FileOutputStream outputStream,
                                     InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }

    public static void toast(String s) {
        toast(App.ctx, s);
    }

    public static void toast(int id) {
        toast(App.ctx, id);
    }

    public static void toast(Context cxt, int id) {
        Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static String md5(String string) {
        byte[] hash = null;
        try {
            hash = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh,UTF-8 should be supported?", e);
        }
        return computeMD5(hash);
    }

    public static String computeMD5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input, 0, input.length);
            byte[] md5bytes = md.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < md5bytes.length; i++) {
                String hex = Integer.toHexString(0xff & md5bytes[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 显示圆形转动进度框
     *
     * @param activity
     * @return
     */
    public static ProgressDialog showSpinnerDialog(Activity activity,
                                                   String content) {
        // activity = modifyDialogContext(activity);
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setMessage(content);
        if (activity.isFinishing() == false) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 判断手机号码是否合理
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    /**
     * 像素密度
     *
     * @param ctx
     * @return
     */
    public static final float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = getDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getDetailTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date).substring(10).trim();
        if (time.charAt(0) == '0') {
            return time.substring(1);
        }
        return time;
    }

    public static String getTime(long timestamp, boolean isNormal) {
        long currentTime = System.currentTimeMillis();
        // currentTime=1410252272171
        // --timestamp=1408599253
        if (!isNormal) {
            timestamp *= 1000;
        }
        // currentTime=1410253194419
        // --timestamp=1409238751
        if (timestamp < currentTime - 24 * 60 * 60 * 1000 * 3) {
            // 三天前的，显示几月几号
            String result = new SimpleDateFormat("MM-dd").format(new Date(
                    timestamp));
            return result;
        } else if (timestamp < currentTime - 24 * 60 * 60 * 1000) {
            // 一天前，三天内的，显示几天
            return (currentTime - timestamp) / (24 * 60 * 60 * 1000) + "天前";
        } else {
            // 一天内的
            if (timestamp < currentTime - 60 * 60 * 1000) {
                // 一天内，一小时前，显示几小时前
                return (currentTime - timestamp) / (60 * 60 * 1000) + "小时前";
            } else {
                // 一小时内
                if (timestamp < currentTime - 30 * 60 * 1000) {
                    return "半小时前";
                } else {
                    long min = (currentTime - timestamp) / (60 * 1000);
                    if (min < 5) {
                        return "刚刚";
                    } else
                        return min + "分钟前";

                }
            }
        }
    }

    /**
     * 将字符串转为时间戳
     *
     * @param user_time
     * @return
     */
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {

            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 获取网络状态
     *
     * NetworkInfo info = connectMgr.getActiveNetworkInfo();
     *
     * 二、判断是否是手机网络 info !=null && info.getType() ==
     * ConnectivityManager.TYPE_MOBILE
     *
     * 手机网络进行详细区分：
     *
     * info.getSubtype() 这里使用 getSubtype()，不是
     * getType()；getType()返回的是0，或者1，是区分是手机网络还是wifi。
     *
     * info.getSubtype()取值列表如下：
     *
     * NETWORK_TYPE_CDMA 网络类型为CDMA NETWORK_TYPE_EDGE 网络类型为EDGE
     * NETWORK_TYPE_EVDO_0 网络类型为EVDO0 NETWORK_TYPE_EVDO_A 网络类型为EVDOA
     * NETWORK_TYPE_GPRS 网络类型为GPRS NETWORK_TYPE_HSDPA 网络类型为HSDPA
     * NETWORK_TYPE_HSPA 网络类型为HSPA NETWORK_TYPE_HSUPA 网络类型为HSUPA
     * NETWORK_TYPE_UMTS 网络类型为UMTS
     *
     * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     *
     * @return
     */
    public String getNetType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // wifi
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState();
        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (wifi == NetworkInfo.State.CONNECTED) {
            return "wifi";
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
        }

        // mobile 3G Data Network
        NetworkInfo.State mobile = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState();
        if (mobile == NetworkInfo.State.CONNECTED) {
            return "3G";
        }
        return "unknow";
    }

    /** 没有网络 */
    public static final int NETWORKTYPE_INVALID = 0;
    /** wap网络 */
    public static final int NETWORKTYPE_WAP = 1;
    /** 2G网络 */
    public static final int NETWORKTYPE_2G = 2;
    /** 3G和3G以上网络，或统称为快速网络 */
    public static final int NETWORKTYPE_3G = 3;
    /** wifi网络 */
    public static final int NETWORKTYPE_WIFI = 4;

    /**
     *      * 获取网络状态，wifi,wap,2g,3g.  
     *
     * @param context
     *            上下文      
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}
     *         ,          {@link #NETWORKTYPE_INVALID}, {@link #NETWORKTYPE_WAP}
     *         *
     *         <p>
     *         {@link #NETWORKTYPE_WIFI}      
     */
    public static int getNetWorkType(Context context) {
        int mNetWorkType = NETWORKTYPE_INVALID;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
                        : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

    public static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            // case TelephonyManager.NETWORK_TYPE_EHRPD:
            // return true; // ~ 1-2 Mbps
            // case TelephonyManager.NETWORK_TYPE_EVDO_B:
            // return true; // ~ 5 Mbps
            // case TelephonyManager.NETWORK_TYPE_HSPAP:
            // return true; // ~ 10-20 Mbps
            // case TelephonyManager.NETWORK_TYPE_IDEN:
            // return false; // ~25 kbps
            // case TelephonyManager.NETWORK_TYPE_LTE:
            // return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;

        }
        return false;
    }

    /**
     * 将时间戳转为字符串
     *
     * @param cc_time
     * @return
     */
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;

    }

    public static String getTextFromInputStream(InputStream is)
            throws IOException {
        // 将输入流对象包装成输入流读对象，（输入流/指定编码）
        // 当文本文件已是UTF-8无ROM编码模式时，若使用GBK编码，则会出现乱码，而当文本为ANSI编码模式时，使用GBK编码可实现乱码转正常
        InputStreamReader isr = new InputStreamReader(is, "gbk");
        StringBuffer sb = new StringBuffer();
        int count = -1;
        // @return the character read or -1 if the end of the reader has been
        // reached.
        // 按单个字符地读
        while ((count = isr.read()) != -1) {
            sb.append((char) count);
        }
        isr.close();
        return sb.toString();
    }


}
