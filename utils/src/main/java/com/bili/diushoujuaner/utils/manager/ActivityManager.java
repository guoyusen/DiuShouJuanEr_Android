package com.bili.diushoujuaner.utils.manager;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by BiLi on 2016/2/27.
 */
public class ActivityManager extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();
    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public Activity getTopActivity(){
        if(activityList == null || activityList.isEmpty()){
            return null;
        }
        return activityList.get(activityList.size() - 1);
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }


    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void exit() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        int id = android.os.Process.myPid();
        if (id != 0) {
            android.os.Process.killProcess(id);
        }
    }

    public void finish() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public void finishBefore(){
        for(int i = 0, len = activityList.size(); i < len - 1; i++){
            if(!activityList.get(i).isFinishing()){
                activityList.get(i).finish();
            }
        }
    }

}
