package com.bili.diushoujuaner.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.databasehelper.dao.User;
import com.bili.diushoujuaner.presenter.presenter.UserActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.UserActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IUserView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnDialogChoseListener;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;
import com.bili.diushoujuaner.widget.floatingactionbutton.FloatingActionButton;
import com.bili.diushoujuaner.widget.scrollview.ChangeHeadStatusListener;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.bili.diushoujuaner.widget.wheel.DatePickerTool;
import com.bili.diushoujuaner.widget.wheel.LocationPickerTool;
import com.bili.diushoujuaner.widget.wheel.OnDatePickerChoseListener;
import com.bili.diushoujuaner.widget.wheel.OnLocationPickerChoseListener;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class UserActivity extends BaseActivity<UserActivityPresenter> implements IUserView, ChangeHeadStatusListener, View.OnClickListener, OnDatePickerChoseListener, OnLocationPickerChoseListener, OnDialogChoseListener {

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
    @Bind(R.id.edtSmallName)
    EditText edtSmallName;
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
    @Bind(R.id.txtBirth)
    TextView txtBirth;
    @Bind(R.id.ivArrowRightTime)
    ImageView ivArrowRightTime;
    @Bind(R.id.layoutBirth)
    RelativeLayout layoutBirth;
    @Bind(R.id.ivBirth)
    ImageView ivBirth;
    @Bind(R.id.ivWallPaper)
    SimpleDraweeView ivWallPaper;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;
    @Bind(R.id.layoutLocation)
    RelativeLayout layoutLocation;
    @Bind(R.id.layoutHomeTown)
    RelativeLayout layoutHomeTown;
    @Bind(R.id.btnRight)
    ImageButton btnRight;

    private TintedBitmapDrawable arrowRightDrawable;
    private DatePickerTool datePickerTool;
    private LocationPickerTool locationPickerTool;
    private int typeAddress = 0;
    private boolean isEditable = false;

    private final static int ADDRESS_NONE = -1;
    private final static int ADDRESS_LOCATION = 1;
    private final static int ADDRESS_HOMETOWN = 2;

    @Override
    public void beforeInitView() {
        arrowRightDrawable = new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A));
        basePresenter = new UserActivityPresenterImpl(this, context);
        datePickerTool = new DatePickerTool();
        locationPickerTool = new LocationPickerTool(context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_user);
    }

    @Override
    public void resetStatus() {
        FrameLayout.LayoutParams lpHead = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);
        RelativeLayout.LayoutParams lpFocus = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x128), (int) getResources().getDimension(R.dimen.x128));
        lpFocus.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpFocus.setMargins(0, tintManager.getConfig().getStatusBarHeight() + (int) getResources().getDimension(R.dimen.x416), (int) getResources().getDimension(R.dimen.x24), 0);
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
        ivBirth.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_menu_time, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        ivArrowRightLocation.setImageDrawable(arrowRightDrawable);
        ivArrowRightHome.setImageDrawable(arrowRightDrawable);
        ivArrowRightTime.setImageDrawable(arrowRightDrawable);
        ivArrowDown.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_down, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));

        reboundScrollView.setChangeHeadStatusListener(this);

        layoutBirth.setOnClickListener(this);
        layoutHomeTown.setOnClickListener(this);
        layoutLocation.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        layoutGender.setOnClickListener(this);

        getBindPresenter().getUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutBirth:
                datePickerTool.dateTimePickDialog(context, this, txtBirth.getText().toString(), this);
                break;
            case R.id.layoutHomeTown:
                typeAddress = ADDRESS_HOMETOWN;
                locationPickerTool.locationPicKDialog(context, this, txtHomeTown.getText().toString(), this);
                break;
            case R.id.layoutLocation:
                typeAddress = ADDRESS_LOCATION;
                locationPickerTool.locationPicKDialog(context, this, txtLocation.getText().toString(), this);
                break;
            case R.id.layoutGender:
                DialogTool.createGenderDialog(context,this);
                break;
            case R.id.btnRight:
                if(isEditable){
                    UserInfoReq userInfoReq = new UserInfoReq();
                    userInfoReq.setBirthday(txtBirth.getText().toString());
                    userInfoReq.setEmail(edtEmail.getText().toString());
                    userInfoReq.setGender((short) (txtGender.getText().toString().contains("男") ? 1 : 0));
                    userInfoReq.setHomeTown(txtHomeTown.getText().toString());
                    userInfoReq.setLocation(txtLocation.getText().toString());
                    userInfoReq.setSmallNick(edtSmallName.getText().toString());
                    userInfoReq.setNickName(edtUserName.getText().toString());
                    getBindPresenter().updateUserInfo(userInfoReq);
                }
                break;
        }
        isEditable = true;
        showPageHead(null, R.mipmap.icon_finish, null);
        edtUserName.setEnabled(true);
        edtUserName.setSelection(edtUserName.getText().toString().trim().length());
        edtEmail.setEnabled(true);
        edtSmallName.setEnabled(true);
    }

    @Override
    public void onLocationPickerChosened(String str) {
        switch (typeAddress){
            case ADDRESS_HOMETOWN:
                txtHomeTown.setText(str);
                break;
            case ADDRESS_LOCATION:
                txtLocation.setText(str);
                break;
        }
        typeAddress = ADDRESS_NONE;
    }

    @Override
    public void onDatePickerChosened(String str) {
        txtBirth.setText(str);
    }

    @Override
    public void onHeadStatusChanged(boolean result) {
        if (result) {
            layoutHead.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_THEME));
            setTintStatusColor(R.color.COLOR_THEME);
            if(isEditable){
                showPageHead("个人资料", R.mipmap.icon_finish, null);
            }else{
                showPageHead("个人资料", R.mipmap.icon_editor, null);
            }
        } else {
            layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
            setTintStatusColor(R.color.TRANSPARENT_HALF);
            showPageHead(null, R.mipmap.icon_editor, null);
            if(isEditable){
                showPageHead(null, R.mipmap.icon_finish, null);
            }else{
                showPageHead(null, R.mipmap.icon_editor, null);
            }
        }
    }

    @Override
    public void onDialogChose(String str) {
        txtGender.setText(str);
    }

    @Override
    public void showUserInfo(User user) {
        if (user != null) {
            edtUserName.setText(user.getNickName());
            txtGender.setText(user.getGender() == 1 ? "男" : "女");
            txtBirth.setText(user.getBirthday());
            txtLocation.setText(user.getLocation());
            txtHomeTown.setText(user.getHomeTown());
            edtSmallName.setText(user.getSmallNick());
            edtEmail.setText(user.getEmail());
            Common.displayDraweeView(user.getPicPath(), ivWallPaper);
        }
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void back(View view) {
        if(isEditable){
            DialogTool.createUserInfoExitDialog(context, new OnDialogPositiveClickListener() {
                @Override
                public void onPositiveClicked() {
                    finish();
                }
            });
        }else{
            super.back(view);
        }
    }
}
