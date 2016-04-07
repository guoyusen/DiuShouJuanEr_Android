package com.bili.diushoujuaner.widget.wheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.utils.Common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mafei on 15/10/8.
 */
public class DatePickerTool {

    private Calendar c = Calendar.getInstance();
    private WheelView np1, np2, np3;
    private List lyear = new ArrayList();
    private String year[] = new String[]{};
    private String month[] = new String[]{};
    private String strDay30[] = new String[]{};
    private String strDay31[] = new String[]{};
    private String strDay28[] = new String[]{};
    private String strDay29[] = new String[]{};
    private PopupWindow popDateTimePickDialog;
    private int startYear, endYear;

    public DatePickerTool() {

        startYear = Integer.valueOf(Common.getYearFromTime(Common.getCurrentTimeYYMMDD_HHMMSS())) - 100;
        endYear = startYear + 200;
        for (int i = startYear; i <= endYear; i++) {
            lyear.add(String.valueOf(i));
        }
        year = (String[]) lyear.toArray(new String[0]);

        month = new String[]{"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12"};
        strDay31 = new String[]{"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
                "28", "29", "30", "31"};
        strDay30 = new String[]{"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
                "28", "29", "30"};
        strDay28 = new String[]{"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
                "28"};
        strDay29 = new String[]{"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
                "28", "29"};
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @return
     */
    public void dateTimePickDialog(Context context, final Activity activity, final String currentTime, final OnDatePickerChoseListener onDatePickerChoseListener) {
        View dateTimeLayout;
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        dateTimeLayout = mLayoutInflater.inflate(
                R.layout.layout_popup_picker, null);
        popDateTimePickDialog = new PopupWindow(dateTimeLayout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popDateTimePickDialog.setAnimationStyle(R.style.dialogWindowAnim);
        np1 = (WheelView) dateTimeLayout.findViewById(R.id.np1);
        np2 = (WheelView) dateTimeLayout.findViewById(R.id.np2);
        np3 = (WheelView) dateTimeLayout.findViewById(R.id.np3);

        np1.setVisibleItems(5);
        np1.setCyclic(false);
        np1.setLabel("年");
        np1.setAdapter(new ArrayWheelAdapter<String>(year));

        np2.setVisibleItems(5);
        np2.setCyclic(false);
        np2.setLabel("月");
        np2.setAdapter(new ArrayWheelAdapter<String>(month));

        np3.setVisibleItems(5);
        np3.setCyclic(false);
        np3.setLabel("日");
        np3.setAdapter(new ArrayWheelAdapter<String>(strDay31));

        int tmpYear, tmpMonth, tmpDay;
        if(currentTime.length() <= 0){
            tmpYear = c.get(Calendar.YEAR);
            tmpMonth = c.get(Calendar.MONTH);
            tmpDay = c.get(Calendar.DAY_OF_MONTH);
        }else{
            tmpYear = Common.getYearFromTime(currentTime);
            tmpMonth = Common.getMonthFromTime(currentTime) - 1;
            tmpDay = Common.getDayFromTime(currentTime) - 1;
            if(tmpYear < startYear){
                tmpYear = startYear;
                tmpMonth = 1;
                tmpDay = 1;
            }else if(tmpYear > endYear){
                tmpYear = endYear;
                tmpMonth = 1;
                tmpDay = 1;
            }
        }


        np1.setCurrentItem(tmpYear - startYear);
        np2.setCurrentItem(tmpMonth);
        np3.setCurrentItem(tmpDay);

        TextView txtFinish = (TextView) dateTimeLayout.findViewById(R.id.txtFinish);
        txtFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int iyear = np1.getCurrentItem();
                int imonth = np2.getCurrentItem();
                int iday = np3.getCurrentItem();
                String vYear = year[iyear];
                String vMonth = month[imonth];
                String vDay = strDay31[iday];
                onDatePickerChoseListener.onDatePickerChosened(vYear.trim() + "-" + vMonth.trim() + "-"+ vDay.trim());
                dismiss();
            }
        });

        np2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue,
                                  int newValue) {
                int iyear = np1.getCurrentItem();
                int imonth = np2.getCurrentItem();
                String vMonth = month[imonth];
                String vYear = year[iyear];
                if (vMonth.equalsIgnoreCase("01")
                        || vMonth.equalsIgnoreCase("03")
                        || vMonth.equalsIgnoreCase("05")
                        || vMonth.equalsIgnoreCase("07")
                        || vMonth.equalsIgnoreCase("08")
                        || vMonth.equalsIgnoreCase("10")
                        || vMonth.equalsIgnoreCase("12")) {
                    np3.setAdapter(new ArrayWheelAdapter<String>(strDay31));
                    if (np3.getCurrentItem() == 29) {
                        np3.setCurrentItem(30);
                    }
                } else if (vMonth.equalsIgnoreCase("02")) {
                    if (Integer.valueOf(vYear) % 4 == 0
                            && Integer.valueOf(vYear) % 100 != 0
                            || Integer.valueOf(vYear) % 400 == 0) {
                        np3.setAdapter(new ArrayWheelAdapter<String>(strDay29));
                        if (np3.getCurrentItem() == 30) {
                            np3.setCurrentItem(28);
                        }
                    } else {
                        np3.setAdapter(new ArrayWheelAdapter<String>(strDay28));
                        if (np3.getCurrentItem() == 30) {
                            np3.setCurrentItem(27);
                        }
                    }

                } else {
                    np3.setAdapter(new ArrayWheelAdapter<String>(strDay30));
                    // np3.setCurrentItem(1);
                    if (np3.getCurrentItem() == 30) {
                        np3.setCurrentItem(29);
                    }
                }
            }
        });

        np1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue,
                                  int newValue) {
                int iyear = np1.getCurrentItem();
                int imonth = np2.getCurrentItem();
                String vMonth = month[imonth];
                String vYear = year[iyear];
                // 设置右侧的 WheelView 的适配器
                if (vMonth.equalsIgnoreCase("01")
                        || vMonth.equalsIgnoreCase("03")
                        || vMonth.equalsIgnoreCase("05")
                        || vMonth.equalsIgnoreCase("07")
                        || vMonth.equalsIgnoreCase("08")
                        || vMonth.equalsIgnoreCase("10")
                        || vMonth.equalsIgnoreCase("12")) {
                    np3.setAdapter(new ArrayWheelAdapter<String>(strDay31));
                } else if (vMonth.equalsIgnoreCase("02")) {
                    if (Integer.valueOf(vYear) % 4 == 0
                            && Integer.valueOf(vYear) % 100 != 0
                            || Integer.valueOf(vYear) % 400 == 0) {
                        np3.setAdapter(new ArrayWheelAdapter<String>(strDay29));
                    } else {
                        np3.setAdapter(new ArrayWheelAdapter<String>(strDay28));
                    }

                } else {
                    np3.setAdapter(new ArrayWheelAdapter<String>(strDay30));
                }
            }
        });

        np3.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue,
                                  int newValue) {
                int iyear = np1.getCurrentItem();
                int imonth = np2.getCurrentItem();
                String vMonth = month[imonth];
                String vYear = year[iyear];
                // 设置右侧的 WheelView 的适配器
                if (vMonth.equalsIgnoreCase("01")
                        || vMonth.equalsIgnoreCase("03")
                        || vMonth.equalsIgnoreCase("05")
                        || vMonth.equalsIgnoreCase("07")
                        || vMonth.equalsIgnoreCase("08")
                        || vMonth.equalsIgnoreCase("10")
                        || vMonth.equalsIgnoreCase("12")) {
                    np3.setAdapter(new ArrayWheelAdapter<String>(strDay31));
                } else if (vMonth.equalsIgnoreCase("02")) {
                    if (Integer.valueOf(vYear) % 4 == 0
                            && Integer.valueOf(vYear) % 100 != 0
                            || Integer.valueOf(vYear) % 400 == 0) {
                        np3.setAdapter(new ArrayWheelAdapter<String>(strDay29));
                    } else {
                        np3.setAdapter(new ArrayWheelAdapter<String>(strDay28));
                    }

                } else {
                    np3.setAdapter(new ArrayWheelAdapter<String>(strDay30));
                }
            }
        });

        np1.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int iyear = np1.getCurrentItem();
                int imonth = np2.getCurrentItem();
                int iday = np3.getCurrentItem();
            }
        });
        np2.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int iyear = np1.getCurrentItem();
                int imonth = np2.getCurrentItem();
                int iday = np3.getCurrentItem();
            }
        });
        np3.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int iyear = np1.getCurrentItem();
                int imonth = np2.getCurrentItem();
                int iday = np3.getCurrentItem();
            }
        });

        // 产生背景变暗效果
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        popDateTimePickDialog.setTouchable(true);
        popDateTimePickDialog.setFocusable(true);
        popDateTimePickDialog.setBackgroundDrawable(new BitmapDrawable());

        popDateTimePickDialog.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popDateTimePickDialog.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popDateTimePickDialog.setOutsideTouchable(true);
        popDateTimePickDialog.showAtLocation(dateTimeLayout, Gravity.BOTTOM, 0, 0);
        popDateTimePickDialog.update();
        popDateTimePickDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(lp);
            }
        });
    }

    public void dismiss() {
        if (popDateTimePickDialog != null) {
            popDateTimePickDialog.dismiss();
        }
    }


    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

}
