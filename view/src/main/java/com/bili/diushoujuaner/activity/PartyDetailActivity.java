package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.PartyDetailMemberAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.presenter.presenter.PartyDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.PartyDetailActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IPartyDetailView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.ImageItemVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberPicVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnPartyDetailClickListener;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.widget.imagepicker.loader.GlideImageLoader;
import com.bili.diushoujuaner.widget.imagepicker.view.CropImageView;
import com.bili.diushoujuaner.widget.scrollview.OnChangeHeadStatusListener;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class PartyDetailActivity extends BaseActivity<PartyDetailActivityPresenter> implements IPartyDetailView, OnChangeHeadStatusListener, View.OnClickListener {


    @Bind(R.id.ivWallPaper)
    SimpleDraweeView ivWallPaper;
    @Bind(R.id.txtTime)
    TextView txtTime;
    @Bind(R.id.txtMemberName)
    TextView txtMemberName;
    @Bind(R.id.ivMemberName)
    ImageView ivMemberName;
    @Bind(R.id.layoutMemberName)
    RelativeLayout layoutMemberName;
    @Bind(R.id.txtMemberCount)
    TextView txtMemberCount;
    @Bind(R.id.ivMemberCount)
    ImageView ivMemberCount;
    @Bind(R.id.gridViewMember)
    CustomGridView gridViewMember;
    @Bind(R.id.layoutMember)
    RelativeLayout layoutMember;
    @Bind(R.id.ivRecord)
    ImageView ivRecord;
    @Bind(R.id.layoutRecord)
    RelativeLayout layoutRecord;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;
    @Bind(R.id.ivFile)
    ImageView ivFile;
    @Bind(R.id.layoutFile)
    RelativeLayout layoutFile;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;
    @Bind(R.id.btnRight)
    ImageButton btnRight;
    @Bind(R.id.txtNavTitle)
    TextView txtNavTitle;
    @Bind(R.id.txtIntroduce)
    TextView txtIntroduce;
    @Bind(R.id.txtPartyName)
    TextView txtPartyName;
    @Bind(R.id.ivPartyName)
    ImageView ivPartyName;
    @Bind(R.id.layoutPartyName)
    RelativeLayout layoutPartyName;
    @Bind(R.id.ivIntroduce)
    ImageView ivIntroduce;
    @Bind(R.id.layoutIntroduce)
    RelativeLayout layoutIntroduce;

    private TintedBitmapDrawable arrowRightDrawable;
    private List<MemberPicVo> memberPicVoList;
    private PartyDetailMemberAdapter partyDetailMemberAdapter;
    private ImagePicker imagePicker;

    private PartyVo partyVo;

    @Override
    public void beforeInitView() {
        basePresenter = new PartyDetailActivityPresenterImpl(this, context);
        memberPicVoList = new ArrayList<>();
        arrowRightDrawable = new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A));

        imagePicker = ImagePicker.getInstance();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_party_detail);
    }

    @Override
    public void resetStatus() {
        FrameLayout.LayoutParams lpHead = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);
    }

    @Override
    public void setViewStatus() {
        setTintStatusColor(R.color.TRANSPARENT_BLACK);
        showPageHead("", R.mipmap.icon_menu, null);
        layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
        ivFile.setImageDrawable(arrowRightDrawable);
        ivMemberCount.setImageDrawable(arrowRightDrawable);
        ivMemberName.setImageDrawable(arrowRightDrawable);
        ivRecord.setImageDrawable(arrowRightDrawable);
        ivPartyName.setImageDrawable(arrowRightDrawable);
        ivIntroduce.setImageDrawable(arrowRightDrawable);

        reboundScrollView.setChangeHeadStatusListener(this);

        layoutFile.setOnClickListener(this);
        layoutMemberName.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        partyDetailMemberAdapter = new PartyDetailMemberAdapter(context, memberPicVoList);
        gridViewMember.setAdapter(partyDetailMemberAdapter);

        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setFocusHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setOutPutX(Constant.CORP_IMAGE_OUT_WIDTH);
        imagePicker.setOutPutY(Constant.CORP_IMAGE_OUT_HEIGHT);

        getBindPresenter().getContactInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutFile:
                startActivity(new Intent(PartyDetailActivity.this, FileActivity.class));
                break;
            case R.id.layoutMemberName:
                startActivity(new Intent(PartyDetailActivity.this, ContentEditActivity.class)
                        .putExtra(ContentEditActivity.TAG_TYPE, Constant.EDIT_CONTENT_MEMBER_NAME)
                        .putExtra(ContentEditActivity.TAG_CONTENT, txtMemberName.getText().toString()));
                break;
            case R.id.btnRight:
                DialogTool.createPartyDetailDialog(context, this.partyVo.getOwnerNo() == getBindPresenter().getUserNo(), new OnPartyDetailClickListener() {
                    @Override
                    public void onPartyExit() {

                    }

                    @Override
                    public void onUpdateHead() {
                        Intent intent = new Intent(PartyDetailActivity.this, ImageGridActivity.class).putExtra(ImageGridActivity.TAG, "更换群头像");
                        startActivityForResult(intent, 100);
                    }
                });
                break;
            case R.id.layoutPartyName:
                startActivity(new Intent(PartyDetailActivity.this, ContentEditActivity.class)
                        .putExtra(ContentEditActivity.TAG_TYPE, Constant.EDIT_CONTENT_PARTY_NAME)
                        .putExtra(ContentEditActivity.TAG_CONTENT, txtPartyName.getText().toString()));
                break;
            case R.id.layoutIntroduce:
                startActivity(new Intent(PartyDetailActivity.this, ContentEditActivity.class)
                        .putExtra(ContentEditActivity.TAG_TYPE, Constant.EDIT_CONTENT_PARTY_INTRODUCE)
                        .putExtra(ContentEditActivity.TAG_CONTENT, txtIntroduce.getText().toString()));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null && requestCode == 100) {
            ArrayList<ImageItemVo> images = data.getBundleExtra(ImagePicker.EXTRA_IMAGES_BUNDLE).getParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS);
            if (Common.isEmpty(images)) {
                return;
            }
            getBindPresenter().updatePartyHeadPic(this.partyVo.getPartyNo(), images.get(0).path);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePartyHeadEvent(UpdatePartyEvent updatePartyEvent){
        if(updatePartyEvent.getPartyNo() == this.partyVo.getPartyNo()){
            switch (updatePartyEvent.getType()){
                case Constant.CHAT_PARTY_HEAD:
                    Common.displayDraweeView(updatePartyEvent.getContent(), ivWallPaper);
                    break;
                case Constant.CHAT_PARTY_NAME:
                    txtPartyName.setText(updatePartyEvent.getContent());
                    txtNavTitle.setText(updatePartyEvent.getContent());
                    break;
                case Constant.CHAT_PARTY_MEMBER_UPDATE:

                    break;
                case Constant.CHAT_PARTY_UNGROUP:

                    break;
                case Constant.CHAT_PARTY_MEMBER_NAME:
                    txtMemberName.setText(updatePartyEvent.getContent());
                    break;
                case Constant.CHAT_PARTY_INTRODUCE:
                    txtIntroduce.setText(Common.isEmpty(updatePartyEvent.getContent()) ? "暂无介绍" : updatePartyEvent.getContent());
                    break;
            }
        }
    }

    @Override
    public void onHeadStatusChanged(int alpha) {
        if (alpha > 0) {
            layoutHead.setBackgroundColor(Color.parseColor(Common.getThemeAlphaColor(alpha)));
            setTintStatusColor(R.color.COLOR_THEME_MAIN);
            setTineStatusAlpha((float)alpha / 100);
        } else {
            layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
            setTintStatusColor(R.color.TRANSPARENT_BLACK);
            setTineStatusAlpha(1.0f);
        }
    }

    @Override
    public void showContactInfo(PartyVo partyVo) {
        if (partyVo == null) {
            return;
        }
        this.partyVo = partyVo;
        Common.displayDraweeView(partyVo.getPicPath(), ivWallPaper);
        txtNavTitle.setText(partyVo.getDisplayName());
        txtTime.setText("创建于" + Common.getYYMMDDFromTime(partyVo.getRegisterTime()));
        txtIntroduce.setText(Common.isEmpty(partyVo.getInformation()) ? "暂无介绍" : partyVo.getInformation());
        if(partyVo.getOwnerNo() == getBindPresenter().getUserNo()){
            layoutPartyName.setVisibility(View.VISIBLE);
            layoutPartyName.setOnClickListener(this);
            txtPartyName.setText(partyVo.getDisplayName());
            ivIntroduce.setVisibility(View.VISIBLE);
            layoutIntroduce.setOnClickListener(this);
        }else{
            layoutPartyName.setVisibility(View.GONE);
            ivIntroduce.setVisibility(View.GONE);
        }
        txtMemberName.setText(getBindPresenter().getMemberName());
        showMemberList();
    }

    private void showMemberList(){
        List<MemberVo> memberVoList = getBindPresenter().getMemberVoList();
        txtMemberCount.setText(memberVoList.size() + "人");

        if (memberVoList.isEmpty()) {
            return;
        }
        gridViewMember.setVisibility(View.VISIBLE);
        if (memberVoList.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                MemberPicVo memberPicVo = new MemberPicVo();
                memberPicVo.setPath(memberVoList.get(i).getPicPath());
                memberPicVo.setType(Constant.MEMBER_HEAD_SERVER);

                memberPicVoList.add(memberPicVo);
            }
            MemberPicVo memberPicVo = new MemberPicVo();
            memberPicVo.setResourceId(R.mipmap.icon_add_member);
            memberPicVo.setType(Constant.MEMBER_HEAD_LOCAL);
            memberPicVoList.add(memberPicVo);
        } else {
            for (MemberVo memberVo : memberVoList) {
                MemberPicVo memberPicVo = new MemberPicVo();
                memberPicVo.setPath(memberVo.getPicPath());
                memberPicVo.setType(Constant.MEMBER_HEAD_SERVER);

                memberPicVoList.add(memberPicVo);
            }
            MemberPicVo memberPicVo = new MemberPicVo();
            memberPicVo.setResourceId(R.mipmap.icon_add_member);
            memberPicVo.setType(Constant.MEMBER_HEAD_LOCAL);
            memberPicVoList.add(memberPicVo);
        }
        partyDetailMemberAdapter.notifyDataSetChanged();
    }

}
