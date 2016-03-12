package com.bili.diushoujuaner.model.databasehelper;

import android.content.Context;

import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.utils.Constant;

/**
 * Created by BiLi on 2016/3/12.
 */
public class DBManager {

    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private Context context;

    private static DBManager dbManager;

    private DBManager(Context context){
        this.context = context;
        this.daoMaster = getDaoMaster(context);
        this.daoSession = getDaoSession(context);
    }

    public static void initialize(Context context){
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

    public static DBManager instance(){
        if(dbManager == null){
            throw new NullPointerException("DBManager was not initialized!");
        }
        return dbManager;
    }

    public void saveUser(User user){
        daoSession.getUserDao().insertOrReplace(user);
    }

}
