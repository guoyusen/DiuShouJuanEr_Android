package com.bili.diushoujuaner.utils;

/**
 * Created by BiLi on 2016/3/11.
 */
public interface ConstantUtil {

    // 返回结果的retCode
    String RETCODE_SUCCESS = "success";
    String RETCODE_FAIL = "fail";
    String RETCODE_ERROR = "error";
    // 跳转页面的类型
    int SHOW_TYPE_LOGIN = 0;
    int SHOW_TYPE_MAIN = 1;
    int SHOW_TYPE_CHATTING_SETTING = 2;
    int SHOW_TYPE_PARTY_DETAIL = 3;
    // 加载框类型
    int LOADING_NONE = -1;
    int LOADING_DEFAULT = 0; // 标题栏的加载框
    int LOADING_TOP = 1; // 顶部横向长条加载，重型加载
    int LOADING_CENTER = 2; // 屏幕中间加载，中型加载
    // 通用警告类型
    int ERROR_PARSE = -1;
    int WARNING_PARSE = 0;
    int WARNING_NET = 1;
    int WARNING_500 = 2;
    int WARNING_401 = 3;
    int WARNING_403 = 4;
    int WARNING_503 = 5;
    int WARNING_FILE = 6;
    // 数据库名称
    String DATABASE_NAME = "diushoujuaner_db";
    // 服务器ip信息
    String HOST_ADDRESS = "http://192.168.137.1:8080/diushoujuaner/";
    String SERVER_IP = "192.168.137.1";
    // 通讯录类型
    int CONTACT_PARTY = 1;
    int CONTACT_FRIEND = 2;
    // 童趣类型
    int RECALL_ALL = 1;
    int RECALL_USER = 2;
    // 下拉刷新类型  默认  主动
    int REFRESH_DEFAULT = 1;
    int REFRESH_INTENT = 2;
    // 本地信息加载提示
    String ACTION_LOAD_LOCAL_SUCCESS = "本地数据加载成功";
    String ACTION_LOAD_LOCAL_FAILURE = "本地数据加载失败";
    // 评论区域的点击类型
    int COMMENT_CLICK_LAYOUT_RESPON = 1;
    int COMMENT_CLICK_COMMENT_CONTENT = 2;
    int COMMENT_CLICK_SUB_RESPON = 3;
    // 删除评论的类型
    int DELETE_COMMENT = 1;
    int DELETE_RESPON = 2;
    // 文本编辑器的类型
    int EDIT_CONTENT_NONE = 0;
    int EDIT_CONTENT_AUTOGRAPH = 1;
    int EDIT_CONTENT_FEEDBACK = 2;
    int EDIT_CONTENT_MEMBER_NAME = 3;
    int EDIT_CONTENT_PARTY_NAME = 4;
    int EDIT_CONTENT_PARTY_INTRODUCE = 5;
    int EDIT_CONTENT_FRIEND_APPLY = 6;
    int EDIT_CONTENT_PARTY_APPLY = 7;
    int EDIT_CONTENT_FRIEND_REMARK = 8;
    // 文本编辑器对应文本的长度
    int EDIT_CONTENT_LENGTH_MEMBER_NAME = 50;
    int EDIT_CONTENT_LENGTH_AUTOGRAPH = 50;
    int EDIT_CONTENT_LENGTH_PARTY_NAME = 10;
    int EDIT_CONTENT_LENGTH_FEEDBACK = 200;
    int EDIT_CONTENT_LENGTH_PARTY_INTRODUCE = 100;
    int EDIT_CONTENT_LENGTH_FRIEND_APPLY = 50;
    int EDIT_CONTENT_LENGTH_PARTY_APPLY = 50;
    int EDIT_CONTENT_LENGTH_FRIEND_REMARK = 50;
    // 最近一条发布的缓存时间
    int ACACHE_TIME_RECENT_RECALL = 600000;
    // 缓存键
    String ACACHE_RECENT_RECALL_PREFIX = "A_RECENT_RECALL_";
    String ACACHE_RECALL_LIST = "A_RECALL_LIST";
    String ACACHE_LAST_TIME_CONTACT = "A_CONTACT_TIME";
    String ACACHE_USER_RECALL_PREFIX = "A_USER_RECALL_";

    // 区分是忘记密码还是注册
    int ACOUNT_UPDATE_REGIST = 1;
    int ACOUNT_UPDATE_RESET = 2;
    //传递给RecallAdapter的页面类型
    int RECALL_ADAPTER_HOME = 1;
    int RECALL_ADAPTER_SPACE = 2;
    // 传递给RecallAdapter的页面索引，仅表明home的就可以，因为SpaceActivity有静态变量公布
    int RECALL_GOOD_HOME_INDEX = 0;
    // 裁剪尺寸--头像
    int CORP_IMAGE_HEAD_EAGE = 280;
    // 裁剪尺寸--壁纸
    int CORP_IMAGE_WALLPAPER_EAGE = 320;
    // 图像输出尺寸
    int CORP_IMAGE_OUT_WIDTH = 800;
    int CORP_IMAGE_OUT_HEIGHT = 800;
    // 发布图片时，本地图片，resource
    int RECALL_ADD_PIC_PATH = 1;
    int RECALL_ADD_PIC_RES = 2;
    // 聊天的消息类型
    int CHAT_INIT = -1;//初始化，携带账号信息在服务端进行注册
    int CHAT_PING = 0;//心跳请求包
    int CHAT_PONG = 1;//心跳响应包
    int CHAT_FRI = 2;//好友消息
    int CHAT_TIME = 3;//同步时间
    int CHAT_CLOSE = 4;//关闭
    int CHAT_PAR = 5;//群消息
    int CHAT_GOOD = 6;//赞消息
    int CHAT_STATUS = 7;//消息更新状态
    int CHAT_PARTY_NAME = 8;//群广播-群名称
    int CHAT_PARTY_HEAD = 9;//群广播-头像
    int CHAT_PARTY_MEMBER_UPDATE = 10;//群成员增删
    int CHAT_PARTY_UNGROUP = 11;//群解散
    int CHAT_PARTY_INTRODUCE = 12;//修改群介绍
    int CHAT_PARTY_MEMBER_NAME = 13;//群成员修改自己的群名片
    int CHAT_FRIEND_APPLY = 14;//添加童友
    int CHAT_PARTY_APPLY = 15;//申请加群
    int CHAT_FRIEND_RECOMMEND = 16;//好友推荐
    int CHAT_FRIEND_DELETE = 17;//删除童友
    int CHAT_FRIEND_APPLY_AGREE = 18;//同意添加好友
    int CHAT_PARTY_APPLY_AGREE = 19;//同意加入群
    // 聊天消息中content的类型
    int CHAT_CONTENT_EMPTY = 0;
    int CHAT_CONTENT_TEXT = 1;
    int CHAT_CONTENT_IMG = 2;
    int CHAT_CONTENT_VOICE = 3;
    int CONTENT_PARTY_ADD = 4;
    // 如果20S内没有收到来自服务端的心跳请求，则触发离线
    int IDEL_TIMEOUT_FOR_INTERVAL = 20;
    // 5S连接超时
    int CONNECTTIMEOUT = 5000;
    // 收发器中的消息发送状态
    int MESSAGE_STATUS_SENDING = 0;
    int MESSAGE_STATUS_FAIL = 1;
    int MESSAGE_STATUS_SUCCESS = 2;
    // service和client通信过程中的消息类型，对应于CHAT_...
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
    int HANDLER_FRIEND_APPLY = 16;//添加童友
    int HANDLER_PARTY_APPLY = 17;//申请加群
    int HANDLER_FRIEND_APPLY_AGREE = 18;//添加好友成功，更新对应的联系人
    int HANDLER_PARTY_APPLY_AGREE = 19;//添加群成功，更新对应联系人
    int HANDLER_FRIEND_DELETE = 20;//删除童友
    // 群成员头像来源 服务端头像 本地加号
    int MEMBER_HEAD_SERVER = 1;
    int MEMBER_HEAD_LOCAL = 2;
    // 未读消息 类型
    int UNREAD_COUNT_MESSAGE = 1;//消息
    int UNREAD_COUNT_APPLY = 2;//申请
    // 添加好友类型
    int CONTACT_ADD_FRIEND = 1;//好友申请
    int CONTACT_ADD_PARTY = 2;//群申请
    int CONTACT_ADDED = 3;//已添加
    int CONTACT_MAY_KNOW = 4;//推荐
    // 获取单个用户的类型
    int CONTACT_INFO_ADD_BEFORE = 1;//添加好友之前
    int CONTACT_INFO_ADD_AFTER = 2;//添加好友之后
    // 删除联系人类型
    int DELETE_CONTACT_FRIEND = 1;
    int DELETE_CONTACT_PARTY = 2;
    // 群成员类型
    int MEMBER_OWNER = 1;//管理员
    int MEMBER_MEMBER = 2;//成员
}
