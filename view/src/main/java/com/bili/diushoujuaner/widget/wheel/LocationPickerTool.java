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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BiLi on 2016/4/7.
 */
public class LocationPickerTool {

    private WheelView np1, np2, np3;
    private PopupWindow popLocationPickDialog;

    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONObject mJsonObj;
    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * 当前区的名称
     */
    private String mCurrentAreaName;

    private Context context;

    private boolean isUpdateCity = true;
    private boolean isUpdateArea = true;


    public LocationPickerTool(Context context) {
        this.context = context;
        initJsonData();
        initDatas();
    }

    private int getPIndexFromLocation(String currentLocation){
        String province = currentLocation.substring(0,currentLocation.indexOf("-"));
        for(int i = 0, len = mProvinceDatas.length;i < len; i++){
            if(mProvinceDatas[i].equals(province)){
                return i;
            }
        }
        return 0;
    }

    private int getCIndexFromLocation(String currentLocation){
        String city;
        if(currentLocation.indexOf("-") == currentLocation.lastIndexOf("-")){
            city = currentLocation.substring(currentLocation.indexOf("-") + 1);
        }else{
            city = currentLocation.substring(currentLocation.indexOf("-") + 1,currentLocation.lastIndexOf("-"));
        }
        for(int i = 0, len = np2.getAdapter().getItemsCount();i < len; i++){
            if(np2.getAdapter().getItem(i).equals(city)){
                return i;
            }
        }
        return 0;
    }

    private int getAIndexFromLocation(String currentLocation){
        String area;
        if(currentLocation.indexOf("-") == currentLocation.lastIndexOf("-")){
            return 0;
        }else{
            area = currentLocation.substring(currentLocation.lastIndexOf("-") + 1);
        }
        for(int i = 0, len = np3.getAdapter().getItemsCount();i < len; i++){
            if(np3.getAdapter().getItem(i).equals(area)){
                return i;
            }
        }
        return 0;
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @return
     */
    public void locationPicKDialog(Context context, final Activity activity, final String currentLocation, final OnLocationPickerChoseListener onLocationPickerChoseListener) {
        View dateTimeLayout;
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        dateTimeLayout = mLayoutInflater.inflate(
                R.layout.layout_popup_picker, null);
        popLocationPickDialog = new PopupWindow(dateTimeLayout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        np1 = (WheelView) dateTimeLayout.findViewById(R.id.np1);
        np2 = (WheelView) dateTimeLayout.findViewById(R.id.np2);
        np3 = (WheelView) dateTimeLayout.findViewById(R.id.np3);

        np1.setVisibleItems(5);
        np1.setCyclic(false);
        np1.setLabel(null);

        np2.setVisibleItems(5);
        np2.setCyclic(false);
        np2.setLabel(null);

        np3.setVisibleItems(5);
        np3.setCyclic(false);
        np3.setLabel(null);

        np1.setAdapter(new ArrayWheelAdapter<String>(mProvinceDatas));

        if(currentLocation.length() <= 0){
            np1.setCurrentItem(0);
            updateCities();
        } else {
            np1.setCurrentItem(getPIndexFromLocation(currentLocation));
            updateCities();
            np2.setCurrentItem(getCIndexFromLocation(currentLocation));
            updateAreas();
            np3.setCurrentItem(getAIndexFromLocation(currentLocation));
        }

        TextView txtFinish = (TextView) dateTimeLayout.findViewById(R.id.txtFinish);
        txtFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tmpP = np1.getAdapter().getItem(np1.getCurrentItem());
                String tmpC = np2.getAdapter().getItem(np2.getCurrentItem());
                String tmpD = np3.getAdapter().getItem(np3.getCurrentItem());
                onLocationPickerChoseListener.onLocationPickerChosened(tmpP.trim() + "-" + tmpC.trim() + (tmpD.length() <= 0 ? "" : ("-") + tmpD));
                dismiss();
            }
        });

        np2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue,
                                  int newValue) {
                updateAreas();
            }
        });

        np1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue,
                                  int newValue) {
                updateCities();
            }
        });
        np1.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                isUpdateCity = false;
                isUpdateArea = false;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                isUpdateCity = true;
                isUpdateArea = true;
                updateCities();
            }
        });
        np2.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                isUpdateArea = false;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                isUpdateArea = true;
                updateAreas();
            }
        });


        // 产生背景变暗效果
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        popLocationPickDialog.setTouchable(true);
        popLocationPickDialog.setFocusable(true);
        popLocationPickDialog.setBackgroundDrawable(new BitmapDrawable());

        popLocationPickDialog.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popLocationPickDialog.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popLocationPickDialog.setOutsideTouchable(true);
        popLocationPickDialog.showAtLocation(dateTimeLayout, Gravity.BOTTOM, 0, 0);
        popLocationPickDialog.update();
        popLocationPickDialog.setAnimationStyle(R.style.dialogWindowAnim);
        popLocationPickDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {

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
        if (popLocationPickDialog != null) {
            popLocationPickDialog.dismiss();
        }
    }

    private void updateAreas()
    {
        if(!isUpdateArea){
            return;
        }
        mCurrentCityName = mCitisDatasMap.get(np1.getAdapter().getItem(np1.getCurrentItem())) == null ? "" : mCitisDatasMap.get(np1.getAdapter().getItem(np1.getCurrentItem()))[np2.getCurrentItem()];
        String[] areas = mAreaDatasMap.get(mCurrentCityName);

        if (areas == null)
        {
            areas = new String[] { "" };
        }
        np3.setAdapter(new ArrayWheelAdapter<String>(areas));
        np3.setCurrentItem(0,true);
    }

    private void updateCities()
    {
        if(!isUpdateCity){
            return;
        }
        mCurrentProviceName = mProvinceDatas[np1.getCurrentItem()];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null)
        {
            cities = new String[] { "" };
        }
        np2.setAdapter(new ArrayWheelAdapter<String>(cities));
        np2.setCurrentItem(0,true);
        updateAreas();
    }


    private void initJsonData() {
        InputStream is = null;
        try {
            is  = this.context.getAssets().open("city.json");
            byte [] buffer = new byte[is.available()] ;
            is.read(buffer);
            String json = new String(buffer,"utf-8");
            mJsonObj = new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally{
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas()
    {
        try
        {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字

                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try
                {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1)
                {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++)
                {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try
                    {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e)
                    {
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++)
                    {
                        String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                        mAreasDatas[k] = area;
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }

                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

}
