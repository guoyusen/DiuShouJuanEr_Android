package com.bili.diushoujuaner.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.ui.TimeSinceTextView;
import com.nanotasks.BackgroundWork;
import com.nanotasks.Completion;
import com.nanotasks.Tasks;

import java.util.Date;
import java.util.List;

/**
 * Created by BiLi on 2016/2/27.
 */
public class BaseActivity extends AbstractBaseActivity {

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isCurrentRunningForeground = prefs.getBoolean("isCurrentRunningForeground", false);
        if (!isCurrentRunningForeground) {//TODO 验证手势密码
//            String passwordStr = XgGesturePreference.getInstance().getCurrLoginGesture();
//            if (!TextUtils.isEmpty(passwordStr)) {
//                Intent intent = new Intent(context,
//                        GestureReStartActivity.class);
//                startActivity(intent);
//            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Tasks.executeInBackground(this, new BackgroundWork<Boolean>() {
            @Override
            public Boolean doInBackground() throws Exception {

                boolean isCurrentRunningForeground = isRunningForeground();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isCurrentRunningForeground", isCurrentRunningForeground);
                editor.apply();

                return true;
            }
        }, new Completion<Boolean>() {
            @Override
            public void onSuccess(Context context, Boolean result) {
            }

            @Override
            public void onError(Context context, Exception e) {
            }
        });
    }

    public boolean isRunningForeground() {
        String packageName = getPackageName();
        String topActivityClassName = getTopActivityName(this);
        if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));
        //android.app.ActivityManager.getRunningTasks(int maxNum)
        //int maxNum--->The maximum number of entries to return in the list
        //即最多取得的运行中的任务信息(RunningTaskInfo)数量
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();

        }
        //按下Home键盘后 topActivityClassName=com.android.launcher2.Launcher
        return topActivityClassName;
    }

    @Override
    public void initIntentParam(Intent intent) {

    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void setViewStatus() {

    }

    @Override
    public void onPageResume() {

    }

    @Override
    public void onPagePause() {

    }

    @Override
    public void onPageDestroy() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void showPageHeadSpecial(String titleText, Integer iconId, Date rightText) {
        showPageHead(titleText, iconId, null);

        if(rightText == null){
            findViewById(R.id.textRight).setVisibility(View.INVISIBLE);
        }else{
            findViewById(R.id.textRight).setVisibility(View.VISIBLE);
            ((TimeSinceTextView)findViewById(R.id.textRight)).setDate(rightText);
        }
    }

    @Override
    public void showPageHead(String titleText, Integer iconId, String rightText) {
        if(titleText == null){
            findViewById(R.id.textNavTitle).setVisibility(View.INVISIBLE);
        }else{
            findViewById(R.id.textNavTitle).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.textNavTitle)).setText(titleText);
        }

        if(iconId == null){
            findViewById(R.id.btnRight).setVisibility(View.GONE);
        }else{
            findViewById(R.id.btnRight).setVisibility(View.VISIBLE);
            ((ImageButton)findViewById(R.id.btnRight)).setImageResource(iconId);
        }

        if(rightText == null){
            findViewById(R.id.textRight).setVisibility(View.INVISIBLE);
        }else{
            findViewById(R.id.textRight).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.textRight)).setText(rightText);
        }
    }
}
