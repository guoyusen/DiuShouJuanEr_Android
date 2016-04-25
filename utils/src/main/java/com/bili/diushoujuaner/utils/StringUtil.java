/**
 * 
 */
package com.bili.diushoujuaner.utils;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的帮助类
 *
 */
public class StringUtil {

	/**
	 * 判断字符串是否为手机号码</br>
	 * 只能判断是否为大陆的手机号码
	 * @param str
	 * @return
	 */
	public static boolean checkMobile(String str) {
		Pattern p = Pattern.compile("1[34578][0-9]{9}");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	/**
	 * 验证email的合法性
	 * 
	 * @param emailStr
	 * @return
	 */
	public static boolean checkEmail(String emailStr) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(emailStr.trim());
		boolean isMatched = matcher.matches();
		if (isMatched) {
			return true;
		} else {
			return false;
		}
	}


    public static String getCompleteUrl(String url){
        return ConstantUtil.HOST_ADDRESS + url;
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

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String getSerialNo(){
        return UUID.randomUUID().toString();
    }
}
