package com.bili.diushoujuaner.model.databasehelper;

import android.content.Context;

import com.bili.diushoujuaner.model.databasehelper.dao.DaoMaster;
import com.bili.diushoujuaner.model.databasehelper.dao.DaoSession;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.model.databasehelper.dao.UserDao;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.response.UserRes;

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


    public void saveUser(UserRes userRes) {
        User user = DataTypeUtil.changeUserResToUser(userRes);
        List<User> userList = daoSession.getUserDao().queryBuilder()
                .where(UserDao.Properties.UserNo.eq(user.getUserNo()))
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

}
