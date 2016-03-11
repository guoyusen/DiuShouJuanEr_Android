package com.bili.diushoujuaner.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BiLi on 2016/3/10.
 */
public class Common {

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

}
