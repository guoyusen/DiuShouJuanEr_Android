package com.bili.diushoujuaner.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BiLi on 2016/4/25.
 */
public class TimeUtil {
    private static SimpleDateFormat sdf_YYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static SimpleDateFormat sdf_Full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA);
    private static SimpleDateFormat sdf_YYMMDD = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

    private TimeUtil(){}

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
            return (int)Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis()) / 3600000;
        }catch(ParseException pe){
            pe.printStackTrace();
            return 0;
        }
    }

    public static int getMinuteDifferenceBetweenTime(String start, String end){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try{
            c1.setTime(sdf_YYMMDD_HHMMSS.parse(start));
            c2.setTime(sdf_YYMMDD_HHMMSS.parse(end));

            return (int)Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis()) / 60000;
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
}
