package com.bili.diushoujuaner.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bili.diushoujuaner.utils.entity.dto.MessageDto;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BiLi on 2016/3/10.
 */
public class CommonUtil {

    private static final String dd_album = Environment.getExternalStorageDirectory() + "/diudiu/album/";
    private static final String dd_voice = Environment.getExternalStorageDirectory() + "/diudiu/voice/";

    private CommonUtil() {}

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
                    if(o != null){
                        reMap.put(fields[i].getName(), o.toString());
                    }
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
     * @param context
     * @param view
     */
    public static void hideSoftInputFromWindow(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * 显示虚拟键盘
     * @param context
     * @param view
     */
    public static void showSoftInputFromWindow(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    public static <T> boolean isEmpty(List<T> list){
        if(list == null || list.isEmpty()){
            return true;
        }
        return false;
    }
    /**
     * html的转义字符转换成正常的字符串
     *
     * @param html
     * @return
     */
    public static String htmlEscapeCharsToString(String html) {
        if (StringUtil.isEmpty(html)) {
            return html;
        } else {
            return html.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;","\"").replace("&nbsp;"," ");
        }
    }

    public static void displayDraweeView(String url, SimpleDraweeView draweeView) {
        if (StringUtil.isEmpty(url) || url.length() <= 0) {
            return;
        }
        Uri uri = Uri.parse(StringUtil.getCompleteUrl(url));
        draweeView.setImageURI(uri);
    }

    public static void displayDraweeViewFromLocal(String url, SimpleDraweeView draweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + url))
                .setResizeOptions(new ResizeOptions(200, 200))
                .build();
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setOldController(draweeView.getController());
        controller.setImageRequest(request);
        controller.build();
        draweeView.setController(controller.build());
    }

    public static String getThemeAlphaColor(int alpha){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(String.format("%02x",alpha * 255 / 100));
        stringBuilder.append("12B7F5");

        return stringBuilder.toString();
    }

    /**
     * 得到文件名
     */
    public static String getSuffixFromFileName(String fileName){
        if(fileName.lastIndexOf('/') < 0){
            return "";
        }else{
            return fileName.substring(fileName.lastIndexOf('/') + 1);
        }
    }

    public static void makeRootDirectory() {
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + "");
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public static boolean savePictureFromFresco(Context context, String url){
        File dirFile = new File(dd_album);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File f = new File(dirFile, fileName);
        try{
            int byteread = 0;
            InputStream inStream = new FileInputStream(getCachedImageOnDisk(Uri.parse(url)).getPath()); //读入原文件
            FileOutputStream fs = new FileOutputStream(f);
            byte[] buffer = new byte[1024];
            while ( (byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ dd_album)));
        return true;
    }

    public static File getCachedImageOnDisk(Uri loadUri) {
        File localFile = null;
        if (loadUri != null) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    /**
     * 判断Fresco是否把图片 已经下载到本地
     * @param loadUri
     * @return
     */
    public static boolean isImageDownloaded(Uri loadUri) {
        if (loadUri == null) {
            return false;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
        return ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey);
    }

    /**
     * 判断是否是心跳请求包
     * @param message
     * @return
     */
    public static boolean isMessageForHeartBeat(String message){
        MessageDto messageDto = EntityUtil.getMessageDtoFromJSONString(message);
        return messageDto.getMsgType() == ConstantUtil.CHAT_PING;
    }

}
