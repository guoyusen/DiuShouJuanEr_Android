package com.bili.diushoujuaner.model.databasehelper;

import android.content.Context;
import android.database.Cursor;

import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.utils.entity.po.Chat;
import com.bili.diushoujuaner.utils.entity.po.DaoMaster;
import com.bili.diushoujuaner.utils.entity.po.DaoSession;
import com.bili.diushoujuaner.utils.entity.po.Friend;
import com.bili.diushoujuaner.utils.entity.po.FriendDao;
import com.bili.diushoujuaner.utils.entity.po.Member;
import com.bili.diushoujuaner.utils.entity.po.MemberDao;
import com.bili.diushoujuaner.utils.entity.po.Party;
import com.bili.diushoujuaner.utils.entity.po.PartyDao;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.utils.entity.po.UserDao;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.utils.entity.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/3/12.
 */
public class DBManager {

    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private Context context;

    private static DBManager dbManager;

    private DBManager(Context context) {
        this.context = context;
        this.daoMaster = getDaoMaster(context);
        this.daoSession = getDaoSession(context);
    }

    public static void initialize(Context context) {
        dbManager = new DBManager(context);
    }

    private DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, Constant.DATABASE_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            throw new NullPointerException("DBManager was not initialized!");
        }
        return dbManager;
    }

    /**
     * 保存用户列表
     * @param userList
     */
    public void saveUserList(List<User> userList){
        for(User user : userList){
            saveUser(user, true, false);
        }
    }

    /**
     * 保存群成员的信息，不需要更新时间
     * @param userList
     */
    public void saveUserListSelective(List<User> userList){
        for(User user : userList){
            saveUser(user, false, true);
        }
    }

    public void saveUser(User user, boolean isUpdateTime, boolean selective){
        if(isUpdateTime){
            user.setUpdateTime(Common.getCurrentTimeYYMMDD_HHMMSS());
        }
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(Common.getLongValue(user.getUserNo())))
                .build()
                .list();
        if (userList.isEmpty()) {
            daoSession.getUserDao().insertOrReplace(user);
        } else {
            for(User item : userList){
                //greendao通过行Id来进行更新，所以直接设置行id就可以全量跟新
                if(selective){
                    daoSession.getUserDao().insertOrReplace(DataTypeUtil.updateUserBySelective(item, user));
                }else{
                    user.setId(item.getId());
                    daoSession.getUserDao().insertOrReplace(user);
                }
            }
        }
    }

    public void saveUser(UserDto userDto) {
        User user = DataTypeUtil.changeUserDtoToUser(userDto);
        saveUser(user, true, false);
    }

    public User getUser(long userNo){
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(userNo))
                .build()
                .list();
        return userList.isEmpty() ? null : userList.get(0);
    }

    public void updateAutograph(String autograph){
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo()))
                .build()
                .list();
        if(!userList.isEmpty()){
            User user = userList.get(0);
            user.setAutograph(autograph);
            daoSession.getUserDao().insertOrReplace(user);
        }
    }

    public void updateHeadPic(String headPic){
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo()))
                .build()
                .list();
        if(!userList.isEmpty()){
            User user = userList.get(0);
            user.setPicPath(headPic);
            daoSession.getUserDao().insertOrReplace(user);
        }
    }

    public void updateWallpaper(String wallpaper){
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo()))
                .build()
                .list();
        if(!userList.isEmpty()){
            User user = userList.get(0);
            user.setWallPaper(wallpaper);
            daoSession.getUserDao().insertOrReplace(user);
        }
    }

    /**
     * 保存好友列表
     * @param friendList
     */
    public void saveFriendList(List<Friend> friendList){
        //不允许这么删除所有好友，好友信息只能在app初次开启之后获取一次，之后都是更新
//        daoSession.getDatabase().delete("Friend", "OWNER_NO = ? ", new String[]{CustomSessionPreference.getInstance().getCustomSession().getUserNo() + ""});
        for(Friend friend : friendList){
            saveFriend(friend);
        }
    }

    public FriendVo getFriendVo(long userNo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select user_No, nick_Name, mobile, autograph, gender, birthday,home_Town, location, pic_Path, small_Nick, regist_Time, update_Time, wall_Paper ");
        stringBuilder.append("from User ");
        stringBuilder.append("where user_No = " + userNo);

        List<FriendVo> friendVoList = new ArrayList<>();
        FriendVo friendVo;
        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            do {
                friendVo = new FriendVo();
                friendVo.setFriendNo(cursor.getLong(cursor.getColumnIndex("USER_NO")));
                friendVo.setNickName(cursor.getString(cursor.getColumnIndex("NICK_NAME")));
                friendVo.setMobile(cursor.getString(cursor.getColumnIndex("MOBILE")));
                friendVo.setAutograph(cursor.getString(cursor.getColumnIndex("AUTOGRAPH")));
                friendVo.setGender(cursor.getInt(cursor.getColumnIndex("GENDER")));
                friendVo.setBirthday(cursor.getString(cursor.getColumnIndex("BIRTHDAY")));
                friendVo.setHomeTown(cursor.getString(cursor.getColumnIndex("HOME_TOWN")));
                friendVo.setLocation(cursor.getString(cursor.getColumnIndex("LOCATION")));
                friendVo.setPicPath(cursor.getString(cursor.getColumnIndex("PIC_PATH")));
                friendVo.setSmallNick(cursor.getString(cursor.getColumnIndex("SMALL_NICK")));
                friendVo.setRegistTime(cursor.getString(cursor.getColumnIndex("REGIST_TIME")));
                friendVo.setUpdateTime(cursor.getString(cursor.getColumnIndex("UPDATE_TIME")));
                friendVo.setWallPaper(cursor.getString(cursor.getColumnIndex("WALL_PAPER")));

                friendVoList.add(friendVo);
            } while (cursor.moveToNext());
        }
        return friendVoList.isEmpty() ? null : friendVoList.get(0);
    }

    public List<FriendVo> getFriendVoList(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select user_No, nick_Name, remark, mobile, autograph, gender, birthday,home_Town, location, pic_Path, small_Nick, regist_Time, update_Time, wall_Paper ");
        stringBuilder.append("from User, Friend ");
        stringBuilder.append("where owner_No = " + CustomSessionPreference.getInstance().getCustomSession().getUserNo() + " ");
        stringBuilder.append("and user_No = friend_No");

        List<FriendVo> friendVoList = new ArrayList<>();
        FriendVo friendVo;
        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();
        ContactTemper.clearFriend();
        if(cursor.getCount() > 0) {
            do {
                friendVo = new FriendVo();
                friendVo.setFriendNo(cursor.getLong(cursor.getColumnIndex("USER_NO")));
                friendVo.setNickName(cursor.getString(cursor.getColumnIndex("NICK_NAME")));
                friendVo.setDisplayName(cursor.getString(cursor.getColumnIndex("REMARK")));
                friendVo.setMobile(cursor.getString(cursor.getColumnIndex("MOBILE")));
                friendVo.setAutograph(cursor.getString(cursor.getColumnIndex("AUTOGRAPH")));
                friendVo.setGender(cursor.getInt(cursor.getColumnIndex("GENDER")));
                friendVo.setBirthday(cursor.getString(cursor.getColumnIndex("BIRTHDAY")));
                friendVo.setHomeTown(cursor.getString(cursor.getColumnIndex("HOME_TOWN")));
                friendVo.setLocation(cursor.getString(cursor.getColumnIndex("LOCATION")));
                friendVo.setPicPath(cursor.getString(cursor.getColumnIndex("PIC_PATH")));
                friendVo.setSmallNick(cursor.getString(cursor.getColumnIndex("SMALL_NICK")));
                friendVo.setRegistTime(cursor.getString(cursor.getColumnIndex("REGIST_TIME")));
                friendVo.setUpdateTime(cursor.getString(cursor.getColumnIndex("UPDATE_TIME")));
                friendVo.setWallPaper(cursor.getString(cursor.getColumnIndex("WALL_PAPER")));

                friendVoList.add(friendVo);
                ContactTemper.addFriendVo(friendVo);
            } while (cursor.moveToNext());
        }

        return friendVoList;
    }

    public void saveFriend(Friend friend){
        //好友列表如果没有，则增量更新，有则修改，不可以全部更新，因为friend表中包含有最近的聊天字段
        List<Friend> friendList = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.OwnerNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo())
                        , FriendDao.Properties.FriendNo.eq(Common.getLongValue(friend.getFriendNo())))
                .build()
                .list();
        if (friendList.isEmpty()) {
            friend.setRecent(false);
            daoSession.getFriendDao().insertOrReplace(friend);
        } else {
            for(Friend item : friendList){
                daoSession.getFriendDao().insertOrReplace(DataTypeUtil.updateFriendByNewer(item, friend));
            }
        }
    }

    /**
     * 保存群组列表
     * @param partyList
     */
    public void savePartyList(List<Party> partyList){
        for(Party party : partyList){
            saveParty(party);
        }
    }

    public void saveParty(Party party){
        List<Party> partyList = daoSession.getPartyDao().queryBuilder()
                .where(PartyDao.Properties.PartyNo.eq(Common.getLongValue(party.getPartyNo())))
                .build()
                .list();
        if (partyList.isEmpty()) {
            daoSession.getPartyDao().insertOrReplace(party);
        } else {
            for(Party item : partyList){
                party.setId(item.getId());
                daoSession.getPartyDao().insertOrReplace(party);
            }
        }
    }

    public List<PartyVo> getPartyVoList(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select Party.PARTY_NO, PARTY_NAME, OWNER_NO, INFORMATION, REGISTER_TIME, PIC_PATH ");
        stringBuilder.append("from Party,Member ");
        stringBuilder.append("where Party.PARTY_NO = Member.PARTY_NO ");
        stringBuilder.append("and Member.USER_NO = " + CustomSessionPreference.getInstance().getCustomSession().getUserNo());

        List<PartyVo> partyVoList = new ArrayList<>();
        PartyVo partyVo;
        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            do {
                partyVo = new PartyVo();
                partyVo.setPartyNo(cursor.getLong(cursor.getColumnIndex("PARTY_NO")));
                partyVo.setDisplayName(cursor.getString(cursor.getColumnIndex("PARTY_NAME")));
                partyVo.setOwnerNo(cursor.getLong(cursor.getColumnIndex("OWNER_NO")));
                partyVo.setInformation(cursor.getString(cursor.getColumnIndex("INFORMATION")));
                partyVo.setRegisterTime(cursor.getString(cursor.getColumnIndex("REGISTER_TIME")));
                partyVo.setPicPath(cursor.getString(cursor.getColumnIndex("PIC_PATH")));

                partyVoList.add(partyVo);
                ContactTemper.addPartyVo(partyVo);
                //将该群的成员信息加入ContactTemper
                getMemberVoList(partyVo.getPartyNo());
            } while (cursor.moveToNext());
        }

        return partyVoList;
    }

    public List<MemberVo> getMemberVoList(long partyNo){
        List<MemberVo> memberVoList = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select PARTY_NO, MEMBER.USER_NO, MEMBER_NAME, ADD_TIME, TYPE, PIC_PATH ");
        stringBuilder.append("from USER, MEMBER ");
        stringBuilder.append("where USER.USER_NO = MEMBER.USER_NO");

        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            do {
                MemberVo memberVo = new MemberVo();
                memberVo.setPartyNo(cursor.getLong(cursor.getColumnIndex("PARTY_NO")));
                memberVo.setUserNo(cursor.getLong(cursor.getColumnIndex("USER_NO")));
                memberVo.setMemberName(cursor.getString(cursor.getColumnIndex("MEMBER_NAME")));
                memberVo.setAddTime(cursor.getString(cursor.getColumnIndex("ADD_TIME")));
                memberVo.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
                memberVo.setPicPath(cursor.getString(cursor.getColumnIndex("PIC_PATH")));

                memberVoList.add(memberVo);
                ContactTemper.addMemberVo(memberVo);
            } while (cursor.moveToNext());
        }

        return memberVoList;
    }

    public void saveMemberList(List<Member> memberList){
        for(Member member : memberList){
            saveMember(member);
        }
    }

    public void saveMember(Member member){
        List<Member> memberList = daoSession.getMemberDao().queryBuilder()
                .where(MemberDao.Properties.PartyNo.eq(Common.getLongValue(member.getPartyNo()))
                        , MemberDao.Properties.UserNo.eq(Common.getLongValue(member.getUserNo())))
                .build()
                .list();
        if (memberList.isEmpty()) {
            member.setRecent(false);
            daoSession.getMemberDao().insertOrReplace(member);
        } else {
            for(Member item : memberList){
                daoSession.getMemberDao().insertOrReplace(DataTypeUtil.updateMemberByNewer(item, member));
            }
        }
    }

    /**
     * 获取最近的消息列表
     * @return
     */
    public List<MessageVo> getRecentMessage(){
        List<MessageVo> messageVoList = new ArrayList<>();
        long currentUserNo =  CustomSessionPreference.getInstance().getCustomSession().getUserNo();
        messageVoList.addAll(getMessageVoListBySql(getUnReadMessageSql(currentUserNo), true));

        List<Long> friendNoList = getRecentFriendNo(currentUserNo);
        List<Long> partyNoList = getRecentPartyNo(currentUserNo);
        for(MessageVo messageVo : messageVoList){
            if(messageVo.getMsgType() == Constant.CHAT_FRI){
                friendNoList.remove(messageVo.getFromNo());
            }else if(messageVo.getMsgType() == Constant.CHAT_PAR){
                partyNoList.remove(messageVo.getToNo());
            }
        }
        if(!friendNoList.isEmpty()){
            messageVoList.addAll(getMessageVoListBySql(getTopOneRecentFriendSql(friendNoList, currentUserNo), true));
        }
        if(!partyNoList.isEmpty()){
            messageVoList.addAll(getMessageVoListBySql(getTopOneRecentPartySql(partyNoList, currentUserNo), true));
        }
        return messageVoList;
    }

    /**
     * 通过最近群账号获取最近的消息列表
     * @param partyNoList
     * @param currentUserNo
     * @return
     */
    private String getTopOneRecentPartySql(List<Long> partyNoList, long currentUserNo){
        StringBuilder friendNoRange = new StringBuilder();
        for(int i = 0, len = partyNoList.size(); i < len; i++){
            friendNoRange.append(partyNoList.get(0));
            if(i <= len - 1){
                friendNoRange.append(",");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id,FROM_NO,TO_NO,CONTENT,max(time)TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ ");
        stringBuilder.append("from chat ");
        stringBuilder.append("where TO_NO in (" + friendNoRange.toString() + ") ");
        stringBuilder.append("and OWNER_NO = " + currentUserNo + " ");
        stringBuilder.append("and MSG_TYPE = " +  Constant.CHAT_PAR);
        stringBuilder.append("group by TO_NO");

        return stringBuilder.toString();
    }

    /**
     * 通过好友账号获得这批好友的最近一条消息，这里不管发送和接收的账号位置；
     * 附：这条该死的SQL语句花了本宝宝进四个小时。。。SQL语句不过关啊
     * @param friendNoList
     * @param currentUserNo
     * @return
     */
    private String getTopOneRecentFriendSql(List<Long> friendNoList, long currentUserNo){
        StringBuilder friendNoRange = new StringBuilder();
        for(int i = 0, len = friendNoList.size(); i < len; i++){
            friendNoRange.append(friendNoList.get(0));
            if(i <= len - 1){
                friendNoRange.append(",");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id,FROM_NO,TO_NO,CONTENT,max(time)TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ from");
        stringBuilder.append("(select _id,TO_NO FROM_NO,FROM_NO TO_NO,CONTENT,max(time) TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ ");
        stringBuilder.append("from chat where MSG_TYPE = " + Constant.CHAT_FRI + " and FROM_NO in (" + friendNoRange.toString() + ") and TO_NO = " + currentUserNo + " group by FROM_NO ");
        stringBuilder.append("UNION");
        stringBuilder.append("select _id,FROM_NO,TO_NO,CONTENT,max(time)TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ ");
        stringBuilder.append("from chat where MSG_TYPE = " + Constant.CHAT_FRI + " TO_NO in (" + friendNoRange.toString() + ") and FROM_NO = " + currentUserNo + " group by TO_NO)");
        stringBuilder.append("group by TO_NO");

        return stringBuilder.toString();
    }

    /**
     * 获取最近的群账号列表
     * @param currentUserNo
     * @return
     */
    private List<Long> getRecentPartyNo(long currentUserNo){
        List<Long> listNo = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select PARTY_NO from MEMBER ");
        stringBuilder.append("where USER_NO = " + currentUserNo + " ");
        stringBuilder.append("and RECENT = 1");

        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            do {
                listNo.add(cursor.getLong(cursor.getColumnIndex("PARTY_NO")));
            } while (cursor.moveToNext());
        }

        return listNo;
    }

    /**
     * 获取最近的好友账号列表
     * @param currentUserNo
     * @return
     */
    private List<Long> getRecentFriendNo(long currentUserNo){
        List<Long> listNo = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select FRIEND_NO from FRIEND ");
        stringBuilder.append("where OWNER_NO = " + currentUserNo + " ");
        stringBuilder.append("and RECENT = 1");

        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            do {
                listNo.add(cursor.getLong(cursor.getColumnIndex("FRIEND_NO")));
            } while (cursor.moveToNext());
        }

        return listNo;
    }

    /**
     * 获取未读消息的sql语句
     * @param currentUserNo  当前用户的账号
     * @return
     */
    private String getUnReadMessageSql(long currentUserNo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id, FROM_NO, TO_NO, CONTENT, TIME, MSG_TYPE, CON_TYPE, STATUS, SHOW_TIME, READ ");
        stringBuilder.append("from CHAT ");
        stringBuilder.append("where OWNER_NO = " + currentUserNo + " ");
        stringBuilder.append("and MSG_TYPE in (" + Constant.CHAT_FRI + ","+ Constant.CHAT_PAR + ") ");
        stringBuilder.append("and READ = 0");

        return stringBuilder.toString();
    }

    /**
     * @param sql {_id, FROM_NO, TO_NO, CONTENT, TIME, MSG_TYPE, CON_TYPE, STATUS, SHOW_TIME, READ}
     * @param addToChatting 是否存入ChattingTemper
     * @return List<MessageVo>
     */
    private List<MessageVo> getMessageVoListBySql(String sql, boolean addToChatting){
        List<MessageVo> messageVoList = new ArrayList<>();
        Cursor cursor = daoSession.getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            do {
                MessageVo messageVo = new MessageVo();
                messageVo.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                messageVo.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
                messageVo.setTimeShow(cursor.getInt(cursor.getColumnIndex("SHOW_TIME")) == 1);
                messageVo.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
                messageVo.setMsgType(cursor.getInt(cursor.getColumnIndex("MSG_TYPE")));
                messageVo.setConType(cursor.getInt(cursor.getColumnIndex("CON_TYPE")));
                messageVo.setFromNo(cursor.getLong(cursor.getColumnIndex("FROM_NO")));
                messageVo.setToNo(cursor.getLong(cursor.getColumnIndex("TO_NO")));
                messageVo.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
                messageVo.setRead(cursor.getInt(cursor.getColumnIndex("READ")) == 1);

                messageVoList.add(messageVo);
                if(addToChatting){
                    //在首次加载消息列表时，要加入ChattingTemper
                    ChattingTemper.addChattingVo(messageVo);
                }
            } while (cursor.moveToNext());
        }
        return messageVoList;
    }

    /**
     * 保存信息
     * @param messageVo
     */
    public long saveMessage(MessageVo messageVo){
        Chat chat = new Chat();
        chat.setContent(messageVo.getContent());
        chat.setConType(messageVo.getConType());
        chat.setMsgType(messageVo.getMsgType());
        chat.setFromNo(messageVo.getFromNo());
        chat.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        chat.setRead(messageVo.isRead());
        chat.setShowTime(messageVo.isTimeShow());
        chat.setStatus(messageVo.getStatus());
        chat.setToNo(messageVo.getToNo());
        chat.setTime(messageVo.getTime());

        return daoSession.getChatDao().insertOrReplace(chat);
    }

    private String getMessageListSql(long lastId, int pageIndex, int pageSize){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id, FROM_NO, TO_NO, CONTENT, TIME, MSG_TYPE, CON_TYPE, STATUS, SHOW_TIME, READ ");
        stringBuilder.append("from CHAT ");
        stringBuilder.append("where OWNER_NO = " + CustomSessionPreference.getInstance().getCustomSession().getUserNo() + " ");
        stringBuilder.append("and MSG_TYPE = " + ChattingTemper.getMsgType() + " ");
        stringBuilder.append("and (FROM_NO = " + ChattingTemper.getUserNo() + " or TO_NO = " +  ChattingTemper.getUserNo() + ") ");
        if(pageIndex == 1){
            stringBuilder.append("order by TIME asc ");
            stringBuilder.append("limit 0," + pageSize + " ");
        }else if(pageIndex > 1){
            stringBuilder.append("and _id < " + lastId + " ");
            stringBuilder.append("order by TIME asc ");
            stringBuilder.append("limit " + ((pageIndex - 1) * pageSize - 1) + "," + pageSize);
        }

        return stringBuilder.toString();
    }

    public List<MessageVo> getMessageList(long lastId, int pageIndex, int pageSize){
        if(!ChattingTemper.isChatting()){
            return null;
        }
        List<MessageVo> messageVoList = new ArrayList<>();
        messageVoList.addAll(getMessageVoListBySql(getMessageListSql(lastId, pageIndex, pageSize), false));
        return messageVoList;
    }

}
