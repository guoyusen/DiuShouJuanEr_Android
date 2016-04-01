package com.bili.diushoujuaner.utils;

/**
 * Created by BiLi on 2016/3/11.
 */
public interface Constant {

    // 返回结果的retCode
    String RETCODE_SUCCESS = "success";
    String RETCODE_FAIL = "fail";
    String RETCODE_ERROR = "error";

    int SHOW_TYPE_LOGIN = 0;
    int SHOW_TYPE_MAIN = 1;

    int ERROR_PARSE = -1;

    // 加载框类型
    int LOADING_NONE = -1;
    int LOADING_DEFAULT = 0; // 标题栏的加载框
    int LOADING_TOP = 1; // 顶部横向长条加载，重型加载
    int LOADING_CENTER = 2; // 屏幕中间加载，中型加载

    // 通用警告类型
    int WARNING_PARSE = 0;
    int WARNING_NET = 1;
    int WARNING_500 = 2;
    int WARNING_401 = 3;
    int WARNING_403 = 4;
    int WARNING_503 = 5;

    String DATABASE_NAME = "diushoujuaner_db";

    String HOST_ADDRESS = "http://192.168.137.1:8080/diushoujuaner/";

    // 通讯录类型
    int CONTACT_PARTY = 1;
    int CONTACT_FRIEND = 2;

    // 童趣类型
    int RECALL_ALL = 1;
    int RECALL_USER = 2;

    // Acache缓存键值
    String ACACHE_RECALL_LIST = "A_RECALL_LIST";

    int REFRESH_DEFAULT = 1;
    int REFRESH_INTENT = 2;

    String ACTION_LOAD_LOCAL_SUCCESS = "本地数据加载成功";
    String ACTION_LOAD_LOCAL_FAILURE = "本地数据加载失败";

}
