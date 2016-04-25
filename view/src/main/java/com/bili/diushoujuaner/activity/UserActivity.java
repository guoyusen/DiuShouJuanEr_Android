package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.fragment.PictureFragment;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.po.User;
import com.bili.diushoujuaner.presenter.presenter.UserActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.UserActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IUserView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.entity.vo.PictureVo;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnGenderChoseListener;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;
import com.bili.diushoujuaner.widget.floatingactionbutton.FloatingActionButton;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.utils.entity.vo.ImageItemVo;
import com.bili.diushoujuaner.widget.imagepicker.loader.GlideImageLoader;
import com.bili.diushoujuaner.widget.imagepicker.view.CropImageView;
import com.bili.diushoujuaner.widget.scrollview.OnChangeHeadStatusListener;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.bili.diushoujuaner.widget.wheel.DatePickerTool;
import com.bili.diushoujuaner.widget.wheel.LocationPickerTool;
import com.bili.diushoujuaner.widget.wheel.OnDatePickerChoseListener;
import com.bili.diushoujuaner.widget.wheel.OnLocationPickerChoseListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class UserActivity extends BaseFragmentActivity<UserActivityPresenter> implements IUserView, OnChangeHeadStatusListener, View.OnClickListener, OnDatePickerChoseListener, OnLocationPickerChoseListener, OnGenderChoseListener {

    @Bind(R.id.ivUserName)
    ImageView ivUserName;
    @Bind(R.id.edtUserName)
    CustomEditText edtUserName;
    @Bind(R.id.btnCamera)
    FloatingActionButton btnCamera;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;
    @Bind(R.id.ivEmail)
    ImageView ivEmail;
    @Bind(R.id.edtEmail)
    CustomEditText edtEmail;
    @Bind(R.id.edtSmallName)
    CustomEditText edtSmallName;
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
    @Bind(R.id.layoutUserName)
    RelativeLayout layoutUserName;
    @Bind(R.id.layoutEmail)
    RelativeLayout layoutEmail;
    @Bind(R.id.layoutSmallName)
    RelativeLayout layoutSmallName;
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

    private ImagePicker imagePicker;

    private User currentUser;

    class CustomTouchLisener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isEditable = true;
            return false;
        }
    }

    @Override
    public void beforeInitView() {
        arrowRightDrawable = new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A));
        basePresenter = new UserActivityPresenterImpl(this, context);
        datePickerTool = new DatePickerTool();
        locationPickerTool = new LocationPickerTool(context);

        imagePicker = ImagePicker.getInstance();
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
        RelativeLayout.LayoutParams lpCamera = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x128), (int) getResources().getDimension(R.dimen.x128));
        lpCamera.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpCamera.setMargins(0, tintManager.getConfig().getStatusBarHeight() + (int) getResources().getDimension(R.dimen.x416), (int) getResources().getDimension(R.dimen.x24), 0);
        btnCamera.setLayoutParams(lpCamera);
        btnCamera.setPadding(0, 0, 0, 0);
    }

    @Override
    public void setViewStatus() {
        setTintStatusColor(R.color.TRANSPARENT_BLACK);
        showPageHead(null, R.mipmap.icon_finish, null);
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

        edtUserName.setMaxLength(10);
        edtEmail.setMaxLength(40);
        edtSmallName.setMaxLength(50);

        layoutBirth.setOnClickListener(this);
        layoutHomeTown.setOnClickListener(this);
        layoutLocation.setOnClickListener(this);
        layoutGender.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        ivWallPaper.setOnClickListener(this);

        edtUserName.setOnTouchListener(new CustomTouchLisener());
        edtEmail.setOnTouchListener(new CustomTouchLisener());
        edtSmallName.setOnTouchListener(new CustomTouchLisener());

        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setFocusHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setOutPutX(ConstantUtil.CORP_IMAGE_OUT_WIDTH);
        imagePicker.setOutPutY(ConstantUtil.CORP_IMAGE_OUT_HEIGHT);

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
                DialogTool.createChoseGenderDialog(context,this);
                break;
            case R.id.btnRight:
                if(isEditable){
                    if(edtUserName.getText().toString().length() <= 0){
                        showWarning("用户昵称不能为空");
                        return;
                    }
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
            case R.id.btnCamera:
                Intent intent = new Intent(this, ImageGridActivity.class).putExtra(ImageGridActivity.TAG,"更换头像");
                startActivityForResult(intent, 100);
                break;
            case R.id.ivWallPaper:
                if(currentUser != null){
                    ArrayList<PictureVo> pictureVoList = new ArrayList<>();
                    PictureVo pictureVo = new PictureVo();
                    pictureVo.setPicPath(currentUser.getPicPath());
                    pictureVoList.add(pictureVo);
                    PictureFragment.showPictureDetail(getSupportFragmentManager(),pictureVoList,0, false);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null && requestCode == 100) {
            ArrayList<ImageItemVo> images = data.getBundleExtra(ImagePicker.EXTRA_IMAGES_BUNDLE).getParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS);
            if(CommonUtil.isEmpty(images)){
                return;
            }
            getBindPresenter().updateHeadPic(images.get(0).path);
        }
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
        isEditable = true;
    }

    @Override
    public void onDatePickerChosened(String str) {
        txtBirth.setText(str);
        isEditable = true;
    }

    @Override
    public void onHeadStatusChanged(int alpha) {
        if (alpha > 0) {
            layoutHead.setBackgroundColor(Color.parseColor(CommonUtil.getThemeAlphaColor(alpha)));
            setTintStatusColor(R.color.COLOR_THEME_MAIN);
            setTineStatusAlpha((float)alpha / 100);
        } else {
            layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
            setTintStatusColor(R.color.TRANSPARENT_BLACK);
            setTineStatusAlpha(1.0f);
        }
    }

    @Override
    public void onDialogChose(String str) {
        txtGender.setText(str);
        isEditable = true;
    }

    @Override
    public void showUserInfo(User user) {
        if (user != null) {
            currentUser = user;
            edtUserName.setText(user.getNickName());
            txtGender.setText(user.getGender() == 1 ? "男" : "女");
            txtBirth.setText(user.getBirthday());
            txtLocation.setText(user.getLocation());
            txtHomeTown.setText(user.getHomeTown());
            edtSmallName.setText(user.getSmallNick());
            edtEmail.setText(user.getEmail());
            CommonUtil.displayDraweeView(user.getPicPath(), ivWallPaper);
        }
    }

    @Override
    public void updateHeadPic(String headPath) {
        CommonUtil.displayDraweeView(headPath, ivWallPaper);
    }

    @Override
    public void back(View view) {
        if(isEditable){
            DialogTool.createDropInfoDialog(context, new OnDialogPositiveClickListener() {
                @Override
                public void onPositiveClicked() {
                    finish();
                }
            });
        }else{
            super.back(view);
        }
    }

    @Override
    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_BACK:
                if(isEditable){
                    DialogTool.createDropInfoDialog(context, new OnDialogPositiveClickListener() {
                        @Override
                        public void onPositiveClicked() {
                            finish();
                        }
                    });
                }else{
                    finish();
                }
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(isEditable){
            DialogTool.createDropInfoDialog(context, new OnDialogPositiveClickListener() {
                @Override
                public void onPositiveClicked() {
                    finish();
                }
            });
        }else{
            finish();
        }
    }
}
