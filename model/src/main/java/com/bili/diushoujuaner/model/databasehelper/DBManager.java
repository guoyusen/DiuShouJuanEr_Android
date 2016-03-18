package com.bili.diushoujuaner.model.databasehelper;

import android.content.Context;
import android.database.Cursor;

import com.bili.diushoujuaner.model.databasehelper.dao.DaoMaster;
import com.bili.diushoujuaner.model.databasehelper.dao.DaoSession;
import com.bili.diushoujuaner.model.databasehelper.dao.Friend;
import com.bili.diushoujuaner.model.databasehelper.dao.FriendDao;
import com.bili.diushoujuaner.model.databasehelper.dao.Party;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.databasehelper.dao.UserDao;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.response.UserRes;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.CursorQuery;
import de.greenrobot.dao.query.QueryBuilder;

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

    public void saveUserList(List<User> userList){
        for(User user : userList){
            saveUser(user);
        }
    }

    public void saveUser(User user){
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(Common.getLongValue(user.getUserNo())))
                .build()
                .list();
        if (userList.size() <= 0) {
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
        if(userList.size() > 0){
            return userList.get(0);
        }
        return new User();
    }

    public void saveFriendList(List<Friend> friendList){
        for(Friend friend : friendList){
            saveFriend(friend);
        }
    }

    public List<FriendVo> getFriendVo(){
        String sql = "select user_No, nick_Name, remark, mobile, autograph, gender, birthday,home_Town, location, pic_Path, small_Nick, regist_Time, update_Time from User, Friend where owner_No = " +
                + CustomSessionPreference.getInstance().getCustomSession().getUserNo() +
                " and user_No = friend_No";
        List<FriendVo> friendVoList = new ArrayList<>();
        FriendVo friendVo;
        Cursor cursor = daoSession.getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        do{
            friendVo = new FriendVo();
            friendVo.setFriendNo(cursor.getLong(cursor.getColumnIndex("USER_NO")));
            friendVo.setNickName(cursor.getString(cursor.getColumnIndex("NICK_NAME")));
            friendVo.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
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

            friendVoList.add(friendVo);
        }while(cursor.moveToNext());

        return friendVoList;
    }

    public void saveFriend(Friend friend){
        List<Friend> friendList = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.OwnerNo.eq(CustomSessionPreference.getInstance().getCustomSession().getUserNo())
                        , FriendDao.Properties.FriendNo.eq(Common.getLongValue(friend.getFriendNo())))
                .build()
                .list();
        if (friendList.size() <= 0) {
            daoSession.getFriendDao().insertOrReplace(friend);
        } else {
            for(Friend item : friendList){
                daoSession.getFriendDao().insertOrReplace(DataTypeUtil.updateFriendByFriend(item, friend));
            }
        }
    }

    public void savePartyList(List<Party> partyList){
        for(Party party : partyList){
            saveParty(party);
        }
    }

    public void saveParty(Party party){

    }

}
