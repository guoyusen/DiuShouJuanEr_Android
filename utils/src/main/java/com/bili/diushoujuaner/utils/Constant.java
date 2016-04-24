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
    int SHOW_TYPE_CHATTING_SETTING = 2;
    int SHOW_TYPE_PARTY_DETAIL = 3;

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
    int WARNING_FILE = 6;

    String DATABASE_NAME = "diushoujuaner_db";

    String HOST_ADDRESS = "http://192.168.137.1:8080/diushoujuaner/";
    String SERVER_IP = "192.168.137.1";

    // 通讯录类型
    int CONTACT_PARTY = 1;
    int CONTACT_FRIEND = 2;

    // 童趣类型
    int RECALL_ALL = 1;
    int RECALL_USER = 2;

    int REFRESH_DEFAULT = 1;
    int REFRESH_INTENT = 2;

    String ACTION_LOAD_LOCAL_SUCCESS = "本地数据加载成功";
    String ACTION_LOAD_LOCAL_FAILURE = "本地数据加载失败";

    int COMMENT_CLICK_LAYOUT_RESPON = 1;
    int COMMENT_CLICK_COMMENT_CONTENT = 2;
    int COMMENT_CLICK_SUB_RESPON = 3;

    int DELETE_COMMENT = 1;
    int DELETE_RESPON = 2;

    int EDIT_CONTENT_NONE = 0;
    int EDIT_CONTENT_AUTOGRAPH = 1;
    int EDIT_CONTENT_FEEDBACK = 2;
    int EDIT_CONTENT_MEMBER_NAME = 3;
    int EDIT_CONTENT_PARTY_NAME = 4;
    int EDIT_CONTENT_PARTY_INTRODUCE = 5;
    int EDIT_CONTENT_FRIEND_ADD = 6;
    int EDIT_CONTENT_PARTY_ADD = 7;

    int EDIT_CONTENT_LENGTH_MEMBER_NAME = 50;
    int EDIT_CONTENT_LENGTH_AUTOGRAPH = 50;
    int EDIT_CONTENT_LENGTH_PARTY_NAME = 50;
    int EDIT_CONTENT_LENGTH_FEEDBACK = 200;
    int EDIT_CONTENT_LENGTH_PARTY_INTRODUCE = 100;
    int EDIT_CONTENT_LENGTH_FRIEND_ADD = 50;
    int EDIT_CONTENT_LENGTH_PARTY_ADD = 50;


    int ACACHE_TIME_RECENT_RECALL = 600000;
    String ACACHE_RECENT_RECALL_PREFIX = "A_RECENT_RECALL_";
    String ACACHE_RECALL_LIST = "A_RECALL_LIST";
    String ACACHE_LAST_TIME_CONTACT = "A_CONTACT_TIME";
    String ACACHE_USER_RECALL_PREFIX = "A_USER_RECALL_";

    //区分是忘记密码还是注册
    int ACOUNT_UPDATE_REGIST = 1;
    int ACOUNT_UPDATE_RESET = 2;
    //传递给RecallAdapter的页面类型
    int RECALL_ADAPTER_HOME = 1;
    int RECALL_ADAPTER_SPACE = 2;
    //传递给RecallAdapter的页面索引，仅表明home的就可以，因为SpaceActivity有静态变量公布
    int RECALL_GOOD_HOME_INDEX = 0;

    int CORP_IMAGE_HEAD_EAGE = 280;
    int CORP_IMAGE_WALLPAPER_EAGE = 320;

    int CORP_IMAGE_OUT_WIDTH = 800;
    int CORP_IMAGE_OUT_HEIGHT = 800;

    int RECALL_ADD_PIC_PATH = 1;
    int RECALL_ADD_PIC_RES = 2;

    // 聊天的消息类型
    int CHAT_INIT = -1;
    int CHAT_PING = 0;
    int CHAT_PONG = 1;
    int CHAT_FRI = 2;
    int CHAT_TIME = 3;
    int CHAT_CLOSE = 4;
    int CHAT_PAR = 5;
    int CHAT_GOOD = 6;
    int CHAT_STATUS = 7;
    int CHAT_PARTY_NAME = 8;//群广播-群名称
    int CHAT_PARTY_HEAD = 9;//群广播-头像
    int CHAT_PARTY_MEMBER_UPDATE = 10;//群成员增删
    int CHAT_PARTY_UNGROUP = 11;//群解散
    int CHAT_PARTY_INTRODUCE = 12;//修改群介绍
    int CHAT_PARTY_MEMBER_NAME = 13;//群成员修改自己的群名片
    int CHAT_FRIEND_ADD = 14;//添加童友
    int CHAT_PARTY_ADD = 15;//申请加群
    int CHAT_FRIEND_RECOMMEND = 16;//好友推荐
    // 聊天消息中content的类型
    int CHAT_CONTENT_EMPTY = 0;
    int CHAT_CONTENT_TEXT = 1;
    int CHAT_CONTENT_IMG = 2;
    int CHAT_CONTENT_VOICE = 3;
    int CHAT_CONTENT_FRIEND_AGREE = 4;

    int IDEL_TIMEOUT_FOR_INTERVAL = 20;//如果20S内没有收到来自服务端的心跳请求，则触发离线
    int CONNECTTIMEOUT = 5000;//5S连接超时

    int MESSAGE_STATUS_SENDING = 0;
    int MESSAGE_STATUS_FAIL = 1;
    int MESSAGE_STATUS_SUCCESS = 2;

    //service和client通信过程中的消息类型，对应于CHAT_...
    int HANDLER_INIT = 1;
    int HANDLER_CHAT = 2;
    int HANDLER_TIME = 3;
    int HANDLER_CLOSE = 4;
    int HANDLER_GOOD = 5;
    int HANDLER_STATUS = 6;
    int HANDLER_LOGIN = 7;
    int HANDLER_RELOGIN = 8;
    int HANDLER_LOGINING = 9;
    int HANDLER_LOGOUT = 10;
    int HANDLER_PARTY_NAME = 11;//群广播-群名称
    int HANDLER_PARTY_HEAD = 12;//群广播-头像
    int HANDLER_PARTY_MEMBER_UPDATE = 13;//群成员增删
    int HANDLER_PARTY_UNGROUP = 14;//群解散
    int HANDLER_PARTY_MEMBER_NAME = 15;//修改群名片
    int HANDLER_FRIEND_ADD = 16;//添加童友
    int HANDLER_PARTY_ADD = 17;//申请加群
    int HANDLER_CONTACT_UPDATE = 18;//更新联系人

    int MEMBER_HEAD_SERVER = 1;
    int MEMBER_HEAD_LOCAL = 2;

    int UNREAD_COUNT_MESSAGE = 1;
    int UNREAD_COUNT_APPLY = 2;

    int CONTACT_ADD_FRIEND = 1;
    int CONTACT_ADD_PARTY = 2;
    int CONTACT_ADDED = 3;
    int CONTACT_MAY_KNOW = 4;

}
