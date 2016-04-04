package com.bili.diushoujuaner.activity;

import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.presenter.UserActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.UserActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IUserView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.floatingactionbutton.FloatingActionButton;
import com.bili.diushoujuaner.widget.scrollview.ChangeHeadStatusListener;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class UserActivity extends BaseActivity<UserActivityPresenter> implements IUserView, ChangeHeadStatusListener {

    @Bind(R.id.ivUserName)
    ImageView ivUserName;
    @Bind(R.id.edtUserName)
    EditText edtUserName;
    @Bind(R.id.btnCamera)
    FloatingActionButton btnCamera;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;
    @Bind(R.id.ivEmail)
    ImageView ivEmail;
    @Bind(R.id.edtEmail)
    EditText edtEmail;
    @Bind(R.id.ivLocation)
    ImageView ivLocation;
    @Bind(R.id.ivHomeTown)
    ImageView ivHomeTown;
    @Bind(R.id.txtLocation)
    TextView txtLocation;
    @Bind(R.id.ivArrowRightLocation)
    ImageView ivArrowRightLocation;
    @Bind(R.id.txtHomeTown)
    TextView txtHomeTown;
    @Bind(R.id.ivArrowRightHome)
    ImageView ivArrowRightHome;
    @Bind(R.id.txtGender)
    TextView txtGender;
    @Bind(R.id.ivArrowDown)
    ImageView ivArrowDown;
    @Bind(R.id.layoutGender)
    LinearLayout layoutGender;
    @Bind(R.id.ivSmallName)
    ImageView ivSmallName;
    @Bind(R.id.txtSmallName)
    TextView txtSmallName;
    @Bind(R.id.ivArrowRightSmallName)
    ImageView ivArrowRightSmallName;
    @Bind(R.id.layoutSmallName)
    RelativeLayout layoutSmallName;
    @Bind(R.id.txtTimer)
    TextView txtTimer;
    @Bind(R.id.ivArrowRightTime)
    ImageView ivArrowRightTime;
    @Bind(R.id.layoutBirth)
    RelativeLayout layoutBirth;
    @Bind(R.id.ivTimer)
    ImageView ivTimer;
    @Bind(R.id.ivWallPaper)
    SimpleDraweeView ivWallPaper;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;

    private TintedBitmapDrawable arrowRightDrawable;

    @Override
    public void beforeInitView() {
        arrowRightDrawable = new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A));
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_user);
    }

    @Override
    public void resetStatus() {
        FrameLayout.LayoutParams lpHead = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);
        RelativeLayout.LayoutParams lpFocus = new RelativeLayout.LayoutParams((int)getResources().getDimension(R.dimen.x128), (int)getResources().getDimension(R.dimen.x128));
        lpFocus.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpFocus.setMargins(0, tintManager.getConfig().getStatusBarHeight() + (int)getResources().getDimension(R.dimen.x416), (int)getResources().getDimension(R.dimen.x24), 0);
        btnCamera.setLayoutParams(lpFocus);
        btnCamera.setPadding(0, 0, 0, 0);
    }

    @Override
    public void setViewStatus() {
        setTintStatusColor(R.color.TRANSPARENT_HALF);
        showPageHead(null, R.mipmap.icon_editor, null);
        layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));

        ivUserName.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_menu_user, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        ivEmail.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_menu_feedback, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        ivLocation.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_menu_location, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        ivHomeTown.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_menu_hometown, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        ivSmallName.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_menu_smallname, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        ivTimer.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_menu_time, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        ivArrowRightLocation.setImageDrawable(arrowRightDrawable);
        ivArrowRightHome.setImageDrawable(arrowRightDrawable);
        ivArrowRightTime.setImageDrawable(arrowRightDrawable);
        ivArrowRightSmallName.setImageDrawable(arrowRightDrawable);
        ivArrowDown.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_down, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));

        reboundScrollView.setChangeHeadStatusListener(this);

        basePresenter = new UserActivityPresenterImpl(this, context);
        getBindPresenter().getUserInfo();
    }

    @Override
    public void onHeadStatusChanged(boolean result) {
        if(result){
            layoutHead.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_THEME));
            setTintStatusColor(R.color.COLOR_THEME);
            showPageHead("个人资料", R.mipmap.icon_editor, null);
        }else{
            layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
            setTintStatusColor(R.color.TRANSPARENT_HALF);
            showPageHead(null, R.mipmap.icon_editor, null);
        }
    }

    @Override
    public void showUserInfo(User user) {
        if (user != null) {
            edtUserName.setText(user.getNickName());
            txtGender.setText(user.getGender() == 1 ? "男" : "女");
            txtTimer.setText(user.getBirthday());
            Common.displayDraweeView(user.getPicPath(),ivWallPaper);
        }
    }

}
