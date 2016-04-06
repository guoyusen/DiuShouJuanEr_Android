package com.bili.diushoujuaner.model.databasehelper;

import android.content.Context;
import android.database.Cursor;

import com.bili.diushoujuaner.model.databasehelper.dao.DaoMaster;
import com.bili.diushoujuaner.model.databasehelper.dao.DaoSession;
import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.model.databasehelper.dao.FriendDao;
import com.bili.diushoujuaner.model.databasehelper.dao.Member;
import com.bili.diushoujuaner.model.databasehelper.dao.MemberDao;
import com.bili.diushoujuaner.model.databasehelper.dao.Party;
import com.bili.diushoujuaner.model.databasehelper.dao.PartyDao;
import com.bili.diushoujuaner.model.databasehelper.dao.UpdateMark;
import com.bili.diushoujuaner.model.databasehelper.dao.UpdateMarkDao;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.databasehelper.dao.UserDao;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.entity.PartyVo;
import com.bili.diushoujuaner.model.apihelper.response.UserRes;

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
            saveUser(user);
        }
    }

    public void saveUser(User user){
        user.setUpdateTime(Common.getCurrentTimeYYMMDD_HHMMSS());
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(Common.getLongValue(user.getUserNo())))
                .build()
                .list();
        if (userList.isEmpty()) {
            daoSession.getUserDao().insertOrReplace(user);
        } else {
            for(User item : userList){
                daoSession.getUserDao().insertOrReplace(DataTypeUtil.updateUserByUser(item,user));
            }
        }
    }

    public void saveUser(UserRes userRes) {
        User user = DataTypeUtil.changeUserResToUser(userRes);
        saveUser(user);
    }

    public User getUser(long userNo){
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(userNo))
                .build()
                .list();
        return userList.isEmpty() ? null : userList.get(0);
    }

    /**
     * 保存好友列表
     * @param friendList
     */
    public void saveFriendList(List<Friend> friendList){
        daoSession.getDatabase().delete("Friend", "OWNER_NO = ? ", new String[]{CustomSessionPreference.getInstance().getCustomSession().getUserNo() + ""});
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
        List<Friend> friendList = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.OwnerNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo())
                        , FriendDao.Properties.FriendNo.eq(Common.getLongValue(friend.getFriendNo())))
                .build()
                .list();
        if (friendList.isEmpty()) {
            daoSession.getFriendDao().insertOrReplace(friend);
        } else {
            for(Friend item : friendList){
                daoSession.getFriendDao().insertOrReplace(DataTypeUtil.updateFriendByFriend(item, friend));
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
                daoSession.getPartyDao().insertOrReplace(DataTypeUtil.updatePartyByParty(item, party));
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
            } while (cursor.moveToNext());
        }

        return partyVoList;
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
            daoSession.getMemberDao().insertOrReplace(member);
        } else {
            for(Member item : memberList){
                daoSession.getMemberDao().insertOrReplace(DataTypeUtil.updateMemberByMember(item, member));
            }
        }
    }

    public String getUpdateTimeContact(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select update_Time ");
        stringBuilder.append("from Update_Mark ");
        stringBuilder.append("where TABLE_NO = " + Constant.DB_UPDATE_TABLE_CONTACT);

        List<String> list = new ArrayList<>();
        Cursor cursor = daoSession.getDatabase().rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("update_Time")));
            } while (cursor.moveToNext());
        }
        return list.isEmpty() ? null : list.get(0);
    }

    public void updateTimeContactToNow(){
        UpdateMark updateMark = new UpdateMark();
        updateMark.setTableNo(Constant.DB_UPDATE_TABLE_CONTACT);
        updateMark.setUpdateTime(Common.getCurrentTimeYYMMDD_HHMMSS());
        List<UpdateMark> updateMarkList = daoSession.getUpdateMarkDao().queryBuilder()
                .where(UpdateMarkDao.Properties.TableNo.eq(Constant.DB_UPDATE_TABLE_CONTACT))
                .build()
                .list();
        if (updateMarkList.isEmpty()) {
            daoSession.getUpdateMarkDao().insertOrReplace(updateMark);
        } else {
            for(UpdateMark item : updateMarkList){
                item.setUpdateTime(updateMark.getUpdateTime());
                daoSession.getUpdateMarkDao().insertOrReplace(item);
            }
        }
    }

}
