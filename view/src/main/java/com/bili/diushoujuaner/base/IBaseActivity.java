package com.bili.diushoujuaner.base;

import android.content.Intent;

/**
 * Created by BiLi on 2016/3/31.
 */
public interface IBaseActivity {

    /**
     * 初始化activity传递的参数
     */
    void initIntentParam(Intent intent);

    /**
     * 初始化页面之前的操作
     */
    void beforeInitView();

    /**
     * 定义页面控件
     */
    void initView();

    /**
     * 设置页面控件事件和状态
     */
    void setViewStatus();


    /**
     * == onResume()
     */
    void onPageResume();

    /**
     * == onPause()
     */
    void onPagePause();

    /**
     * == onDestroy()
     */
    void onPageDestroy();

    /**
     * == 显示加载框
     */
    void showLoading(int loadingType, String message);

    /**
     * == 隐藏加载框
     */
    void hideLoading(int loadingType);

    /**
     * == 显示警告信息
     */
    void showWarning(String message);

    /**
     * == 显示警告信息
     */
    void showWarning(int warningType);

    /**
     * == 显示标题
     */
    void showPageHead(String titleText, Integer iconId, String rightText);

    /**
     * == 在Api19之上，需要重新设置一些控件的位置
     */
    void resetStatus();

    /**
     * == 初始化header中的控件
     */
    void initHeader();

    /**
     * 退出到登录界面
     */
    void exitActivity();

    /**
     * 关闭当前页面
     */
    void finishView();

}
