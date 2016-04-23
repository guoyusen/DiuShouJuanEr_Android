package com.bili.diushoujuaner.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
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
import com.bili.diushoujuaner.utils.entity.dto.OffMsgDto;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BiLi on 2016/3/10.
 */
public class Common {

    private static final String dd_album = Environment.getExternalStorageDirectory() + "/diudiu/album/";
    private static final String dd_voice = Environment.getExternalStorageDirectory() + "/diudiu/voice/";
    private static SimpleDateFormat sdf_YYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static SimpleDateFormat sdf_Full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
    private static SimpleDateFormat sdf_YYMMDD = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

    public static String getCurrentTimeYYMMDD_HHMMSS(){
        return sdf_YYMMDD_HHMMSS.format(new Date());
    }

    public static String getYYMMDDFromTime(String time){
        String result = "";
        try{
            result = sdf_YYMMDD.format(sdf_YYMMDD_HHMMSS.parse(time));
        }catch(ParseException pe){

        }
        return result;
    }

    public static String getCurrentTimeFull(){
        return sdf_Full.format(new Date());
    }

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

    public static int getHourDifferenceBetweenTime(String start){
        return getHourDifferenceBetweenTime(start, getCurrentTimeYYMMDD_HHMMSS());
    }

    public static int getHourDifferenceBetweenTime(String start, String end){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try{
            c1.setTime(sdf_YYMMDD_HHMMSS.parse(start));
            c2.setTime(sdf_YYMMDD_HHMMSS.parse(end));
            return (int)Math.abs(((c1.getTimeInMillis() - c2.getTimeInMillis())/3600000));
        }catch(ParseException pe){
            return 0;
        }
    }

    public static int getMinuteDifferenceBetweenTime(String start, String end){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try{
            c1.setTime(sdf_YYMMDD_HHMMSS.parse(start));
            c2.setTime(sdf_YYMMDD_HHMMSS.parse(end));
            return (int)Math.abs(((c1.getTimeInMillis() - c2.getTimeInMillis())/60000));
        }catch(ParseException pe){
            return 0;
        }
    }

    public static int getMilliDifferenceBetweenTime(String start){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try{
            c1.setTime(sdf_Full.parse(start));
            c2.setTime(sdf_Full.parse(getCurrentTimeFull()));
            return (int)Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis());
        }catch(ParseException pe){
            return 0;
        }
    }

    public static int getYearFromTime(String time){
        return Integer.valueOf(time.substring(0, 4));
    }

    public static int getMonthFromTime(String time){
        return Integer.valueOf(time.substring(5, 7));
    }

    public static int getDayFromTime(String time){
        return Integer.valueOf(time.substring(8, 10));
    }

    public static int getHourFromTime(String time){
        return Integer.valueOf(time.substring(13, 15));
    }

    public static int getMinuteFromTime(String time){
        return Integer.valueOf(time.substring(16, 18));
    }

    public static int getSecondFromTime(String time){
        return Integer.valueOf(time.substring(19));
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
        return object.toString() + "";
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
                    if(o != null){
                        stringBuilder.append(fields[i].getName() + "=" + o.toString() + "&");
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

    public static boolean isCorrectName(String str) {
        String regEx = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
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

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
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
        if (isEmpty(html)) {
            return html;
        } else {
            return html.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;","\"").replace("&nbsp;"," ");
        }
    }

    public static void displayDraweeView(String url, SimpleDraweeView draweeView) {
        if (Common.isEmpty(url) || url.length() <= 0) {
            return;
        }
        Uri uri = Uri.parse(Common.getCompleteUrl(url));
        draweeView.setImageURI(uri);
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

    public static String getSerialNo(){
        return UUID.randomUUID().toString();
    }

    public static MessageVo getMessageVoFromOffMsgDto(OffMsgDto offMsgDto){
        MessageVo messageVo = new MessageVo();
        messageVo.setContent(offMsgDto.getContent());
        messageVo.setTime(offMsgDto.getTime());
        messageVo.setConType(offMsgDto.getConType());
        messageVo.setMsgType(offMsgDto.getMsgType());
        messageVo.setStatus(Constant.MESSAGE_STATUS_SUCCESS);
        messageVo.setFromNo(offMsgDto.getFromNo());
        messageVo.setToNo(offMsgDto.getToNo());
        return messageVo;
    }

    public static MessageVo getMessageVoFromReceive(String jsonString){
        MessageDto messageDto = getMessageDtoFromJSONString(jsonString);
        MessageVo messageVo = null;
        if(messageDto != null){
            messageVo = new MessageVo();
            messageVo.setSerialNo(messageDto.getSerialNo());
            messageVo.setMsgType(messageDto.getMsgType());
            messageVo.setStatus(Constant.MESSAGE_STATUS_SUCCESS);
            messageVo.setToNo(messageDto.getReceiverNo());
            messageVo.setFromNo(messageDto.getSenderNo());
            messageVo.setTime(messageDto.getMsgTime());
            messageVo.setConType(messageDto.getConType());
            messageVo.setContent(messageDto.getMsgContent());
        }
        return messageVo;
    }

    /**
     * 发送成功之后，会回调messageSent方法，解析出对应的MessageVo，发送event来 通知更新状态
     * @return
     */
    public static MessageVo getMessageVoFromMessageDto(MessageDto messageDto){
        MessageVo messageVo = null;
        try{
            if(messageDto != null){
                messageVo = new MessageVo();
                messageVo.setSerialNo(messageDto.getSerialNo());
                messageVo.setMsgType(messageDto.getMsgType());
                messageVo.setStatus(Constant.MESSAGE_STATUS_SUCCESS);
                messageVo.setToNo(messageDto.getReceiverNo());
                messageVo.setFromNo(messageDto.getSenderNo());
                messageVo.setTime(messageDto.getMsgTime());
                messageVo.setConType(messageDto.getConType());
                messageVo.setContent(messageDto.getMsgContent());
            }
        }catch(Exception e){
            return null;
        }
        return messageVo;
    }

    /**
     * 将收到的数据转化成MessageDto
     * @param jsonString
     * @return
     */
    public static MessageDto getMessageDtoFromJSONString(String jsonString) {
        MessageDto messageDto;
        try{
            messageDto = GsonParser.getInstance().fromJson(jsonString, new TypeToken<MessageDto>(){}.getType());
        }catch(Exception e){
            return null;
        }
        return messageDto;
    }

    /**
     * 将要准备发送的messageVo转化成MessageDto
     */
    public static MessageDto getMessageDtoFromMessageVo(MessageVo messageVo){
        MessageDto messageDto = new MessageDto();
        messageDto.setSerialNo(messageVo.getSerialNo());
        messageDto.setConType(messageVo.getConType());
        messageDto.setMsgContent(messageVo.getContent());
        messageDto.setMsgType(messageVo.getMsgType());
        messageDto.setMsgTime(messageVo.getTime());
        messageDto.setReceiverNo(messageVo.getToNo());
        messageDto.setSenderNo(messageVo.getFromNo());

        return messageDto;
    }

    /**
     * 判断是否是心跳请求包
     * @param message
     * @return
     */
    public static boolean isMessageForHeartBeat(String message){
        MessageDto messageDto = getMessageDtoFromJSONString(message);
        return messageDto.getMsgType() == Constant.CHAT_PING;
    }

    /**
     * 得到空的消息包
     * @return
     */
    public static String getEmptyMessage(String serialNo, long senderNo, int chatType){
        MessageDto messageDto = new MessageDto();
        messageDto.setSerialNo(serialNo);
        messageDto.setMsgContent("");
        messageDto.setMsgTime("");
        messageDto.setMsgType(chatType);
        messageDto.setConType(Constant.CHAT_CONTENT_TEXT);
        messageDto.setReceiverNo(0);
        messageDto.setSenderNo(senderNo);

        return GsonParser.getInstance().toJson(messageDto);
    }

    public static MessageVo getEmptyMessageVo(long senderNo, int chatType){
        MessageVo messageVo = new MessageVo();
        messageVo.setSerialNo(getSerialNo());
        messageVo.setMsgType(chatType);
        messageVo.setFromNo(senderNo);
        messageVo.setStatus(Constant.MESSAGE_STATUS_SENDING);

        return messageVo;
    }

}
