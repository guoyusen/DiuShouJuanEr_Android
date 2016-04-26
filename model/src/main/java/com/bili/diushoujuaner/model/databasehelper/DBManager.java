package com.bili.diushoujuaner.model.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ChattingTemper;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.EntityUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.comparator.ApplyComparator;
import com.bili.diushoujuaner.utils.entity.dto.UserDto;
import com.bili.diushoujuaner.utils.entity.po.Apply;
import com.bili.diushoujuaner.utils.entity.po.ApplyDao;
import com.bili.diushoujuaner.utils.entity.po.Chat;
import com.bili.diushoujuaner.utils.entity.po.ChatDao;
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
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;

import java.util.ArrayList;
import java.util.Collections;
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
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, ConstantUtil.DATABASE_NAME, null);
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
    public synchronized void saveUserList(List<User> userList){
        for(User user : userList){
            saveUser(user, true, false);
        }
    }

    /**
     * 保存群成员的信息，不需要更新时间
     * @param userList
     */
    public synchronized void saveUserListSelective(List<User> userList){
        for(User user : userList){
            saveUser(user, false, true);
        }
    }

    public synchronized void saveUser(User user, boolean isUpdateTime, boolean selective){
        if(isUpdateTime){
            user.setUpdateTime(TimeUtil.getCurrentTimeYYMMDD_HHMMSS());
        }
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(CommonUtil.getLongValue(user.getUserNo())))
                .build()
                .list();
        if (userList.isEmpty()) {
            daoSession.getUserDao().insertOrReplace(user);
        } else {
            for(User item : userList){
                //greendao通过行Id来进行更新，所以直接设置行id就可以全量跟新
                if(selective){
                    daoSession.getUserDao().insertOrReplace(EntityUtil.updateUserBySelective(item, user));
                }else{
                    user.setId(item.getId());
                    daoSession.getUserDao().insertOrReplace(user);
                }
            }
        }
    }

    public void saveUser(UserDto userDto) {
        User user = EntityUtil.getUserFromUserDto(userDto);
        saveUser(user, true, false);
    }

    public User getUser(long userNo){
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(userNo))
                .build()
                .list();
        return userList.isEmpty() ? null : userList.get(0);
    }

    public synchronized void updateAutograph(String autograph){
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

    public synchronized void updateHeadPic(String headPic){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PIC_PATH", headPic);
        daoSession.getDatabase().update("USER",contentValues,
                "USER_NO = ",
                new String[]{String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo())});
    }

    public synchronized void updateWallpaper(String wallpaper){
        ContentValues contentValues = new ContentValues();
        contentValues.put("WALL_PAPER", wallpaper);
        daoSession.getDatabase().update("USER",contentValues,
                "USER_NO = ?",
                new String[]{String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo())});
    }

    /**
     * 保存好友列表
     * @param friendList
     */
    public synchronized void saveFriendList(List<Friend> friendList){
        //不允许这么删除所有好友，好友信息只能在app初次开启之后获取一次，之后都是更新
//        daoSession.getDatabase().delete("Friend", "OWNER_NO = ? ", new String[]{CustomSessionPreference.getInstance().getCustomSession().getUserNo() + ""});
        for(Friend friend : friendList){
            saveFriend(friend);
        }
    }

    public synchronized boolean isFriended(long userNo){
        List<Friend> friendList = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.OwnerNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        FriendDao.Properties.FriendNo.eq(userNo))
                .build().list();
        return !friendList.isEmpty();
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
        ContactTemper.getInstance().clearFriend();
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
                ContactTemper.getInstance().addFriendVo(friendVo);
            } while (cursor.moveToNext());
        }

        return friendVoList;
    }

    public synchronized void deleteFriend(long friendNo){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("delete from FRIEND ");
        stringBuilder.append("where OWNER_NO = " + CustomSessionPreference.getInstance().getCustomSession().getUserNo() + " ");
        stringBuilder.append("and FRIEND_NO = " + friendNo);

        daoSession.getDatabase().delete("FRIEND","OWNER_NO = ? and FRIEND_NO = ?",
                new String[]{
                        String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        String.valueOf(friendNo)});
    }

    public synchronized void saveFriend(Friend friend){
        //好友列表如果没有，则增量更新，有则修改，不可以全部更新，因为friend表中包含有最近的聊天字段
        List<Friend> friendList = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.OwnerNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo())
                        , FriendDao.Properties.FriendNo.eq(CommonUtil.getLongValue(friend.getFriendNo())))
                .build()
                .list();
        if (friendList.isEmpty()) {
            daoSession.getFriendDao().insertOrReplace(friend);
        } else {
            for(Friend item : friendList){
                daoSession.getFriendDao().insertOrReplace(EntityUtil.updateFriendByNewer(item, friend));
            }
        }
    }

    /**
     * 保存群组列表
     * @param partyList
     */
    public synchronized void savePartyList(List<Party> partyList){
        for(Party party : partyList){
            saveParty(party);
        }
    }

    public synchronized void saveParty(Party party){
        List<Party> partyList = daoSession.getPartyDao().queryBuilder()
                .where(PartyDao.Properties.PartyNo.eq(CommonUtil.getLongValue(party.getPartyNo())))
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
                ContactTemper.getInstance().addPartyVo(partyVo);
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
                ContactTemper.getInstance().addMemberVo(memberVo);
            } while (cursor.moveToNext());
        }

        return memberVoList;
    }

    public synchronized void saveMemberList(List<Member> memberList){
        for(Member member : memberList){
            saveMember(member);
        }
    }

    public synchronized void saveMember(Member member){
        List<Member> memberList = daoSession.getMemberDao().queryBuilder()
                .where(MemberDao.Properties.PartyNo.eq(CommonUtil.getLongValue(member.getPartyNo()))
                        , MemberDao.Properties.UserNo.eq(CommonUtil.getLongValue(member.getUserNo())))
                .build()
                .list();
        if (memberList.isEmpty()) {
            member.setRecent(false);
            daoSession.getMemberDao().insertOrReplace(member);
        } else {
            for(Member item : memberList){
                daoSession.getMemberDao().insertOrReplace(EntityUtil.updateMemberByNewer(item, member));
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
            if(messageVo.getMsgType() == ConstantUtil.CHAT_FRI){
                friendNoList.remove(messageVo.getFromNo());
            }else if(messageVo.getMsgType() == ConstantUtil.CHAT_PAR){
                partyNoList.remove(messageVo.getToNo());
            }
        }
        if(!partyNoList.isEmpty()){
            messageVoList.addAll(getMessageVoListBySql(getTopOneRecentPartySql(partyNoList, currentUserNo), true));
        }
        if(!friendNoList.isEmpty()){
            messageVoList.addAll(getMessageVoListBySql(getTopOneRecentFriendSql(friendNoList, currentUserNo), true));
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
        StringBuilder partyNoRange = new StringBuilder();
        for(int i = 0, len = partyNoList.size(); i < len; i++){

            partyNoRange.append(partyNoList.get(i));
            if(i < len - 1){
                partyNoRange.append(",");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id,SERIAL_NO,FROM_NO,TO_NO,CONTENT,max(time)TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ ");
        stringBuilder.append("from chat ");
        stringBuilder.append("where TO_NO in (" + partyNoRange.toString() + ") ");
        stringBuilder.append("and OWNER_NO = " + currentUserNo + " ");
        stringBuilder.append("and MSG_TYPE = " +  ConstantUtil.CHAT_PAR + " ");
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
            friendNoRange.append(friendNoList.get(i));
            if(i < len - 1){
                friendNoRange.append(",");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id,SERIAL_NO,FROM_NO,TO_NO,CONTENT,max(TIME) TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ from ( ");
        stringBuilder.append("select _id,SERIAL_NO,TO_NO FROM_NO,FROM_NO TO_NO,CONTENT,max(TIME) TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ ");
        stringBuilder.append("from chat where MSG_TYPE = " + ConstantUtil.CHAT_FRI + " and FROM_NO in (" + friendNoRange.toString() + ") and TO_NO = " + currentUserNo + " group by FROM_NO ");
        stringBuilder.append("UNION ");
        stringBuilder.append("select _id,SERIAL_NO,FROM_NO,TO_NO,CONTENT,max(TIME) TIME,MSG_TYPE,CON_TYPE,STATUS,SHOW_TIME,READ ");
        stringBuilder.append("from chat where MSG_TYPE = " + ConstantUtil.CHAT_FRI + " and TO_NO in (" + friendNoRange.toString() + ") and FROM_NO = " + currentUserNo + " group by TO_NO )");
        stringBuilder.append("group by TO_NO order by 6 asc");
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
        stringBuilder.append("select _id,SERIAL_NO, FROM_NO, TO_NO, CONTENT, TIME, MSG_TYPE, CON_TYPE, STATUS, SHOW_TIME, READ ");
        stringBuilder.append("from CHAT ");
        stringBuilder.append("where OWNER_NO = " + currentUserNo + " ");
        stringBuilder.append("and MSG_TYPE = " + ConstantUtil.CHAT_PAR + " ");
        stringBuilder.append("and READ = 0 ");
        stringBuilder.append("UNION ");
        stringBuilder.append("select _id,SERIAL_NO, TO_NO FROM_NO, FROM_NO TO_NO, CONTENT, TIME, MSG_TYPE, CON_TYPE, STATUS, SHOW_TIME, READ ");
        stringBuilder.append("from CHAT ");
        stringBuilder.append("where OWNER_NO = " + currentUserNo + " ");
        stringBuilder.append("and MSG_TYPE = " + ConstantUtil.CHAT_FRI + " ");
        stringBuilder.append("and READ = 0 ");

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
                messageVo.setSerialNo(cursor.getString(cursor.getColumnIndex("SERIAL_NO")));
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
                    //从数据库加载数据，顺位加入，不需要设置是都显示时间
                    ChattingTemper.getInstance().addChattingVoFromLocal(messageVo);
                }
            } while (cursor.moveToNext());
        }
        return messageVoList;
    }

    /**
     * 保存信息
     * @param messageVo
     */
    public synchronized MessageVo saveMessage(MessageVo messageVo){
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
        chat.setSerialNo(messageVo.getSerialNo());

        messageVo.setId(daoSession.getChatDao().insertOrReplace(chat));

        return messageVo;
    }

    private String getMessageListSql(long lastId, int pageIndex, int pageSize){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select _id, SERIAL_NO, FROM_NO, TO_NO, CONTENT, TIME, MSG_TYPE, CON_TYPE, STATUS, SHOW_TIME, READ from (");
        stringBuilder.append("select _id, SERIAL_NO, FROM_NO, TO_NO, CONTENT, TIME, MSG_TYPE, CON_TYPE, STATUS, SHOW_TIME, READ ");
        stringBuilder.append("from CHAT ");
        stringBuilder.append("where OWNER_NO = " + CustomSessionPreference.getInstance().getCustomSession().getUserNo() + " ");
        stringBuilder.append("and MSG_TYPE = " + ChattingTemper.getInstance().getMsgType() + " ");
        stringBuilder.append("and (FROM_NO = " + ChattingTemper.getInstance().getToNo() + " or TO_NO = " +  ChattingTemper.getInstance().getToNo() + ") ");
        if(pageIndex == 1){
            stringBuilder.append("order by TIME desc ");
            stringBuilder.append("limit 0," + pageSize + " ");
        }else if(pageIndex > 1){
            stringBuilder.append("and _id < " + lastId + " ");
            stringBuilder.append("order by TIME desc ");
            stringBuilder.append("limit " + ((pageIndex - 1) * pageSize - 1) + "," + pageSize);
        }
        stringBuilder.append(") order by TIME asc");

        return stringBuilder.toString();
    }

    public List<MessageVo> getMessageList(long lastId, int pageIndex, int pageSize){
        if(!ChattingTemper.getInstance().isChatting()){
            return null;
        }
        List<MessageVo> messageVoList = new ArrayList<>();
        messageVoList.addAll(getMessageVoListBySql(getMessageListSql(lastId, pageIndex, pageSize), false));
        return messageVoList;
    }

    public synchronized void updateMessageStatus(MessageVo messageVo){
        List<Chat> chatList = daoSession.getChatDao().queryBuilder()
                .where(ChatDao.Properties.SerialNo.eq(messageVo.getSerialNo()))
                .build()
                .list();
        if (!chatList.isEmpty()) {
            for(Chat item : chatList){
                item.setStatus(messageVo.getStatus());
                daoSession.getChatDao().insertOrReplace(item);
            }
        }
    }

    public synchronized void updateMessageRead(long userNo, int msgType){
        ContentValues contentValues = new ContentValues();
        contentValues.put("READ",1);
        daoSession.getDatabase().update("CHAT",contentValues,
                "OWNER_NO = ? and (FROM_NO = ? or TO_NO = ?) and MSG_TYPE = ? and READ = 0",
                new String[]{String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                         String.valueOf(userNo),
                         String.valueOf(userNo),
                         String.valueOf(msgType)});
    }

    public synchronized void deleteRecent(long userNo, int msgType){
        if(msgType == ConstantUtil.CHAT_FRI){
            updateFriendRecent(userNo, false);
        }else if(msgType == ConstantUtil.CHAT_PAR){
            updateMemberRecent(userNo, false);
        }
    }

    public synchronized void updateFriendRecent(long friendNo, boolean recent){
        ContentValues contentValues = new ContentValues();
        contentValues.put("RECENT", recent ? 1 : 0);
        daoSession.getDatabase().update("FRIEND", contentValues,
                "OWNER_NO = ? and FRIEND_NO = ?",
                new String[]{
                        String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        String.valueOf(friendNo)
                });
    }

    public synchronized void updateFriendRemark(long friendNo, String remark){
        ContentValues contentValues = new ContentValues();
        contentValues.put("REMARK", remark);
        daoSession.getDatabase().update("FRIEND", contentValues,
                "OWNER_NO = ? and FRIEND_NO = ?",
                new String[]{
                        String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        String.valueOf(friendNo)
                });
    }

    public synchronized void updateMemberRecent(long partyNo, boolean recent){
        ContentValues contentValues = new ContentValues();
        contentValues.put("RECENT", recent ? 1 : 0);
        daoSession.getDatabase().update("MEMBER",contentValues,
                "PARTY_NO = ? and USER_NO = ?",
                new String[]{
                        String.valueOf(partyNo),
                        String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo())
                });
    }

    public void updateMemberName(long partyNo, long memberNo, String memberName){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MEMBER_NAME", memberName);
        daoSession.getDatabase().update("MEMBER",contentValues,
                "PARTY_NO = ? and USER_NO = ?",
                new String[]{String.valueOf(partyNo), String.valueOf(memberNo)});
    }

    public void updatePartyHeadPic(long partyNo, String picPath){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PIC_PATH", picPath);

        daoSession.getDatabase().update("PARTY",contentValues,
                "PARTY_NO = ?",
                new String[]{String.valueOf(partyNo)});
    }

    public void updatePartyName(long partyNo, String partyName){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PARTY_NAME", partyName);

        daoSession.getDatabase().update("PARTY",contentValues,
                "PARTY_NO = ?",
                new String[]{String.valueOf(partyNo)});
    }

    public void updatePartyIntroduce(long partyNo, String introduce){
        ContentValues contentValues = new ContentValues();
        contentValues.put("INFORMATION", introduce);

        daoSession.getDatabase().update("PARTY",contentValues,
                "PARTY_NO = ?",
                new String[]{String.valueOf(partyNo)});
    }

    public void saveApply(long fromNo, long toNo, String content, String time, int type){
        List<Apply> applyList = daoSession.getApplyDao().queryBuilder()
                .where(ApplyDao.Properties.FromNo.eq(fromNo),
                        ApplyDao.Properties.ToNo.eq(toNo),
                        ApplyDao.Properties.Accept.eq(0),
                        ApplyDao.Properties.Type.eq(type),
                        ApplyDao.Properties.OwnerNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo()))
                .build().list();
        if(!applyList.isEmpty()){
            return;
        }
        Apply apply = new Apply();
        apply.setOwnerNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        apply.setFromNo(fromNo);
        apply.setToNo(toNo);
        apply.setContent(content);
        apply.setTime(time);
        apply.setType(type);
        apply.setRead(false);
        apply.setAccept(false);

        daoSession.getApplyDao().insertOrReplace(apply);
    }

    public int getAddUnReadCount(){
        List<Apply> applyList = daoSession.getApplyDao().queryBuilder()
                .where(ApplyDao.Properties.OwnerNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        ApplyDao.Properties.Accept.eq(false),
                        ApplyDao.Properties.Read.eq(false))
                .build()
                .list();
        return applyList.size();
    }

    public void updateApplyRead(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("READ", 1);
        daoSession.getDatabase().update("APPLY",contentValues,
                "OWNER_NO = ?",
                new String[]{String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo())});
    }

    private String getApplyVoListSql(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select A.FROM_NO FROM_NO, A.TO_NO TO_NO, A.CONTENT CONTENT, A.TIME TIME, A.TYPE TYPE, A.READ READ, A.ACCEPT ACCEPT, U.NICK_NAME NICK_NAME, U.PIC_PATH PIC_PATH ");
        stringBuilder.append("from Apply A, User U ");
        stringBuilder.append("where A.OWNER_NO = " + CustomSessionPreference.getInstance().getCustomSession().getUserNo() + " ");
        stringBuilder.append("and A.FROM_NO = U.USER_NO ");
        return stringBuilder.toString();
    }

    public List<ApplyVo> getApplyVoList(){
        List<ApplyVo> applyVoList = new ArrayList<>();
        Cursor cursor = daoSession.getDatabase().rawQuery(getApplyVoListSql(), null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            do {
                ApplyVo applyVo = new ApplyVo();
                applyVo.setFromNo(cursor.getLong(cursor.getColumnIndex("FROM_NO")));
                applyVo.setToNo(cursor.getLong(cursor.getColumnIndex("TO_NO")));
                applyVo.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
                applyVo.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
                applyVo.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
                applyVo.setAccept(cursor.getInt(cursor.getColumnIndex("ACCEPT")) == 1);
                applyVo.setRead(cursor.getInt(cursor.getColumnIndex("READ")) == 1);
                applyVo.setUserName(cursor.getString(cursor.getColumnIndex("NICK_NAME")));
                applyVo.setPicPath(cursor.getString(cursor.getColumnIndex("PIC_PATH")));

                applyVoList.add(applyVo);
            } while (cursor.moveToNext());
        }
        Collections.sort(applyVoList, new ApplyComparator());

        return applyVoList;
    }

    public void updateApplyFriendAccept(long friendNo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACCEPT", 1);
        daoSession.getDatabase().update("APPLY",contentValues,
                "OWNER_NO = ? and FROM_NO = ? and TO_NO = ? and TYPE = ?",
                new String[]{
                        String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        String.valueOf(friendNo),
                        String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        String.valueOf(ConstantUtil.CHAT_FRIEND_APPLY)
                });
    }

    public void updateApplyPartyAccept(long partyNo, long memberNo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ACCEPT", 1);
        daoSession.getDatabase().update("APPLY",contentValues,
                "OWNER_NO = ? and FROM_NO = ? and TO_NO = ? and TYPE = ?",new String[]{
                        String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        String.valueOf(memberNo),
                        String.valueOf(partyNo),
                        String.valueOf(ConstantUtil.CHAT_PARTY_APPLY)
                });
    }

    public synchronized void deleteParty(long partyNo){
        daoSession.getDatabase().delete("MEMBER", "PARTY_NO = ?", new String[]{String.valueOf(partyNo)});
        daoSession.getDatabase().delete("PARTY", "PARTY_NO = ?", new String[]{String.valueOf(partyNo)});
        deleteChat(partyNo, CustomSessionPreference.getInstance().getCustomSession().getUserNo());
    }

    public synchronized void deleteMember(long partyNo, long memberNo){
        daoSession.getDatabase().delete("MEMBER", "PARTY_NO = ? and USER_NO = ?", new String[]{String.valueOf(partyNo),  String.valueOf(memberNo)});
    }

    public synchronized void deleteChat(long partyNo, long memberNo){
        daoSession.getDatabase().delete("CHAT", "OWNER_NO = ? and FROM_NO = ? and TO_NO = ?",
                new String[]{String.valueOf(CustomSessionPreference.getInstance().getCustomSession().getUserNo()),
                        String.valueOf(memberNo),
                        String.valueOf(partyNo)});
    }

}
