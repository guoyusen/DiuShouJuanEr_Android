package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
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
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;
import com.bili.diushoujuaner.utils.entity.dto.MemberDto;
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

    public static final String TAG_TYPE = "PartyDetailActivity_Type";
    public static final String TAG_DTO = "PartyDetailActivity_Dto";

    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_SEARCH = 2;
    @Bind(R.id.btnAddParty)
    Button btnAddParty;
    @Bind(R.id.layoutBottom)
    RelativeLayout layoutBottom;

    private TintedBitmapDrawable arrowRightDrawable;
    private List<MemberPicVo> memberPicVoList;
    private PartyDetailMemberAdapter partyDetailMemberAdapter;
    private ImagePicker imagePicker;

    private PartyVo partyVo;
    private int type;
    private ContactDto contactDto;
    private boolean isPartied;

    @Override
    public void initIntentParam(Intent intent) {
        type = intent.getIntExtra(TAG_TYPE, -1);
        if (type == TYPE_SEARCH) {
            contactDto = intent.getParcelableExtra(TAG_DTO);
        }
    }

    @Override
    public void beforeInitView() {
        memberPicVoList = new ArrayList<>();
        basePresenter = new PartyDetailActivityPresenterImpl(this, context);
        arrowRightDrawable = new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A));
        imagePicker = ImagePicker.getInstance();
        if (type == TYPE_CONTACT) {
            isPartied = true;
        } else {
            isPartied = getBindPresenter().isPartied(contactDto.getContNo());
        }
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
        layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
        partyDetailMemberAdapter = new PartyDetailMemberAdapter(context, memberPicVoList);
        gridViewMember.setAdapter(partyDetailMemberAdapter);
        if (type == TYPE_CONTACT || (type == TYPE_SEARCH && isPartied)) {
            showPageHead("", R.mipmap.icon_menu, null);
            ivFile.setImageDrawable(arrowRightDrawable);
            ivMemberCount.setImageDrawable(arrowRightDrawable);
            ivMemberName.setImageDrawable(arrowRightDrawable);
            ivRecord.setImageDrawable(arrowRightDrawable);
            ivPartyName.setImageDrawable(arrowRightDrawable);
            ivIntroduce.setImageDrawable(arrowRightDrawable);

            layoutMemberName.setVisibility(View.VISIBLE);
            layoutRecord.setVisibility(View.VISIBLE);

            layoutFile.setOnClickListener(this);
            layoutMemberName.setOnClickListener(this);
            btnRight.setOnClickListener(this);

            imagePicker.setImageLoader(new GlideImageLoader());
            imagePicker.setMultiMode(false);
            imagePicker.setStyle(CropImageView.Style.RECTANGLE);
            imagePicker.setFocusWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
            imagePicker.setFocusHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
            imagePicker.setOutPutX(ConstantUtil.CORP_IMAGE_OUT_WIDTH);
            imagePicker.setOutPutY(ConstantUtil.CORP_IMAGE_OUT_HEIGHT);

            if(type == TYPE_CONTACT){
                getBindPresenter().getContactInfo();
            }else{
                getBindPresenter().getContactInfo(contactDto.getContNo());
            }
        } else if (type == TYPE_SEARCH) {
            showPageHead("", null, null);
            layoutMemberName.setVisibility(View.GONE);
            layoutRecord.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.VISIBLE);
            btnAddParty.setOnClickListener(this);

            CommonUtil.displayDraweeView(contactDto.getPicPath(), ivWallPaper);
            txtNavTitle.setText(contactDto.getDisplayName());
            txtTime.setText("创建于" + TimeUtil.getYYMMDDFromTime(contactDto.getStartTime()));
            txtIntroduce.setText(StringUtil.isEmpty(contactDto.getInformation()) ? "暂无介绍" : contactDto.getInformation());
            showMemberList();
        }

        reboundScrollView.setChangeHeadStatusListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutFile:
                startActivity(new Intent(PartyDetailActivity.this, FileActivity.class));
                break;
            case R.id.layoutMemberName:
                startActivity(new Intent(PartyDetailActivity.this, ContentEditActivity.class)
                        .putExtra(ContentEditActivity.TAG_TYPE, ConstantUtil.EDIT_CONTENT_MEMBER_NAME)
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
                        .putExtra(ContentEditActivity.TAG_TYPE, ConstantUtil.EDIT_CONTENT_PARTY_NAME)
                        .putExtra(ContentEditActivity.TAG_CONTENT, txtPartyName.getText().toString()));
                break;
            case R.id.layoutIntroduce:
                startActivity(new Intent(PartyDetailActivity.this, ContentEditActivity.class)
                        .putExtra(ContentEditActivity.TAG_TYPE, ConstantUtil.EDIT_CONTENT_PARTY_INTRODUCE)
                        .putExtra(ContentEditActivity.TAG_CONTENT, txtIntroduce.getText().toString()));
                break;
            case R.id.btnAddParty:
                startActivity(new Intent(PartyDetailActivity.this, ContentEditActivity.class)
                        .putExtra(ContentEditActivity.TAG_TYPE, ConstantUtil.EDIT_CONTENT_PARTY_ADD)
                        .putExtra(ContentEditActivity.TAG_CONTENT, "")
                        .putExtra(ContentEditActivity.TAG_CONTNO, contactDto.getContNo()));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null && requestCode == 100) {
            ArrayList<ImageItemVo> images = data.getBundleExtra(ImagePicker.EXTRA_IMAGES_BUNDLE).getParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS);
            if (CommonUtil.isEmpty(images)) {
                return;
            }
            getBindPresenter().updatePartyHeadPic(this.partyVo.getPartyNo(), images.get(0).path);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePartyHeadEvent(UpdatePartyEvent updatePartyEvent) {
        if (updatePartyEvent.getPartyNo() == this.partyVo.getPartyNo()) {
            switch (updatePartyEvent.getType()) {
                case ConstantUtil.CHAT_PARTY_HEAD:
                    CommonUtil.displayDraweeView(updatePartyEvent.getContent(), ivWallPaper);
                    break;
                case ConstantUtil.CHAT_PARTY_NAME:
                    txtPartyName.setText(updatePartyEvent.getContent());
                    txtNavTitle.setText(updatePartyEvent.getContent());
                    break;
                case ConstantUtil.CHAT_PARTY_MEMBER_UPDATE:

                    break;
                case ConstantUtil.CHAT_PARTY_UNGROUP:

                    break;
                case ConstantUtil.CHAT_PARTY_MEMBER_NAME:
                    txtMemberName.setText(updatePartyEvent.getContent());
                    break;
                case ConstantUtil.CHAT_PARTY_INTRODUCE:
                    txtIntroduce.setText(StringUtil.isEmpty(updatePartyEvent.getContent()) ? "暂无介绍" : updatePartyEvent.getContent());
                    break;
            }
        }
    }

    @Override
    public void onHeadStatusChanged(int alpha) {
        if (alpha > 0) {
            layoutHead.setBackgroundColor(Color.parseColor(CommonUtil.getThemeAlphaColor(alpha)));
            setTintStatusColor(R.color.COLOR_THEME_MAIN);
            setTineStatusAlpha((float) alpha / 100);
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
        CommonUtil.displayDraweeView(partyVo.getPicPath(), ivWallPaper);
        txtNavTitle.setText(partyVo.getDisplayName());
        txtTime.setText("创建于" + TimeUtil.getYYMMDDFromTime(partyVo.getRegisterTime()));
        txtIntroduce.setText(StringUtil.isEmpty(partyVo.getInformation()) ? "暂无介绍" : partyVo.getInformation());
        if (partyVo.getOwnerNo() == getBindPresenter().getUserNo()) {
            layoutPartyName.setVisibility(View.VISIBLE);
            layoutPartyName.setOnClickListener(this);
            txtPartyName.setText(partyVo.getDisplayName());
            ivIntroduce.setVisibility(View.VISIBLE);
            layoutIntroduce.setOnClickListener(this);
        } else {
            layoutPartyName.setVisibility(View.GONE);
            ivIntroduce.setVisibility(View.GONE);
        }
        txtMemberName.setText(getBindPresenter().getMemberName());
        showMemberList();
    }

    private void showMemberList() {
        if (type == TYPE_CONTACT || (type == TYPE_SEARCH && isPartied)) {
            List<MemberVo> memberVoList;
            if(type == TYPE_CONTACT){
                memberVoList = getBindPresenter().getMemberVoList();
            }else{
                memberVoList = getBindPresenter().getMemberVoList(contactDto.getContNo());
            }
            if (CommonUtil.isEmpty(memberVoList)) {
                txtMemberCount.setText("0人");
                return;
            }
            txtMemberCount.setText(memberVoList.size() + "人");
            for (int i = 0, len = memberVoList.size(); i < len; i++) {
                MemberPicVo memberPicVo = new MemberPicVo();
                memberPicVo.setPath(memberVoList.get(i).getPicPath());
                memberPicVo.setType(ConstantUtil.MEMBER_HEAD_SERVER);
                memberPicVoList.add(memberPicVo);
                if (i >= 3) {
                    break;
                }
            }
            MemberPicVo memberPicVo = new MemberPicVo();
            memberPicVo.setResourceId(R.mipmap.icon_add_member);
            memberPicVo.setType(ConstantUtil.MEMBER_HEAD_LOCAL);
            memberPicVoList.add(memberPicVo);
        } else if (type == TYPE_SEARCH) {
            List<MemberDto> memberDtoList = contactDto.getMemberList();
            txtMemberCount.setText(memberDtoList.size() + "人");
            if (memberDtoList.isEmpty()) {
                return;
            }
            for (int i = 0, len = memberDtoList.size(); i < len; i++) {
                MemberPicVo memberPicVo = new MemberPicVo();
                memberPicVo.setPath(memberDtoList.get(i).getPicPath());
                memberPicVo.setType(ConstantUtil.MEMBER_HEAD_SERVER);
                memberPicVoList.add(memberPicVo);
                if (i >= 4) {
                    break;
                }
            }
        }
        gridViewMember.setVisibility(View.VISIBLE);
        partyDetailMemberAdapter.notifyDataSetChanged();
    }
}
