package com.bili.diushoujuaner.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bili.diushoujuaner.utils.entity.SortVo;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BiLi on 2016/3/10.
 */
public class Common {

    private static SimpleDateFormat sdf_YYMMDD_HHMMSS = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static int getYearDifferenceBetweenTime(String start, String end){
        return Math.abs(getYearFromTime(start) - getYearFromTime(end));
    }

    private static int getDayDifferenceBetweenTime(String start, String end){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(getYearFromTime(start), getMonthFromTime(start), getDayFromTime(start));
        c2.set(getYearFromTime(end), getMonthFromTime(end), getDayFromTime(end));

        return (int)Math.abs(((c1.getTimeInMillis() - c2.getTimeInMillis())/24/3600000));
    }

    private static int getYearFromTime(String time){
        return Integer.valueOf(time.substring(0, 4));
    }

    private static int getMonthFromTime(String time){
        return Integer.valueOf(time.substring(5, 7));
    }

    private static int getDayFromTime(String time){
        return Integer.valueOf(time.substring(8, 10));
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * @param time
     */
    public static String getFormatTime(String time){
        int dayDifference, yearDifference;
        StringBuilder result = new StringBuilder();

        dayDifference = getDayDifferenceBetweenTime(time, sdf_YYMMDD_HHMMSS.format(new Date()));
        yearDifference = getYearDifferenceBetweenTime(time, sdf_YYMMDD_HHMMSS.format(new Date()));

        if(dayDifference == 0){
            result.append("今天" + getTimeHHMMFromTime(time));
        }else if(dayDifference == 1){
            result.append("昨天" + getTimeHHMMFromTime(time));
        }else if(dayDifference == 2){
            result.append("前天" + getTimeHHMMFromTime(time));
        }else if(dayDifference >= 3 && dayDifference <= 60){
            result.append(getTimeMMDD_HHMMFromTime(time));
        }else if(yearDifference > 0){
            result.append(getTimeYYMMDDFromTime(time));
        }else{
            result.append(getTimeMMDD_HHMMFromTime(time));
        }

        return result.toString();
    }

    public static String getTimeHHMMFromTime(String time){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time.substring(11, 16));

        return stringBuilder.toString();
    }

    public static String getTimeYYMMDDFromTime(String time){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time.substring(0,10));

        return stringBuilder.toString();
    }

    public static String getTimeMMDD_HHMMFromTime(String time){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time.substring(5,16));

        return stringBuilder.toString();
    }

    public static String changeObjToString(Object object){
        return object + "";
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static boolean isApkDebugable(Context context){
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static long getLongValue(Long value){
        if(value == null){
            return 0;
        }
        return value.longValue();
    }
    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str.replaceAll(" ", ""));
        b = m.matches();
        return b;
    }

    public static String getCompleteUrl(String url){
        return Constant.HOST_ADDRESS + url;
    }

    public static String getCompleteUrl(String prefixUrl, Object obj){
        StringBuilder stringBuilder = new StringBuilder();
        if(obj == null){
            return "";
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            stringBuilder.append(prefixUrl);
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    stringBuilder.append(fields[i].getName() + "=" + o.toString() + "&");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> ConvertObjToMap(Object obj) {
        Map<String, String> reMap = new HashMap<>();
        if (obj == null)
            return null;
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    reMap.put(fields[i].getName(), o.toString());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    public static boolean isCorrectName(String str) {
        String regEx = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 编辑框获得焦点时去掉提示文字
     */
    public static View.OnFocusChangeListener onFocusAutoClearHintListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText textView = (EditText) v;
            String hint;
            if (hasFocus) {
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }
        }
    };

    public static boolean isWiFiActive(Context context) {
        WifiManager wm = null;
        try {
            wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (wm == null || wm.isWifiEnabled() == false) return false;

        return true;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            String pkName = context.getPackageName();
            versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 获取应用版本信息
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 获取手机的设备号（imei）
     *
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager mphonemanger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "";
        if (mphonemanger != null) {
            imei = mphonemanger.getDeviceId();
        }
        return imei;
    }

    /**
     * 检测网络状态
     *
     * @param context
     * @return
     */
    public static boolean checkNetworkStatus(Context context) {
        boolean resp = false;
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connMgr.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            resp = true;
        }
        return resp;
    }

    /**
     * 关闭虚拟键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInputFromWindow(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * html的转义字符转换成正常的字符串
     *
     * @param html
     * @return
     */
    public static String htmlEscapeCharsToString(String html) {
        if (isEmpty(html)) {
            return html;
        } else {
            return html.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;","\"");
        }
    }

}
