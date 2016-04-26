package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ContactRecentGalleryAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.eventhelper.StartChattingEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateRemarkEvent;
import com.bili.diushoujuaner.presenter.presenter.ContactDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContactDetailActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContactDetailView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.utils.entity.dto.PictureDto;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.aligntextview.AlignTextView;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;
import com.bili.diushoujuaner.widget.dialog.OnFriendDetailClickListener;
import com.bili.diushoujuaner.widget.floatingactionbutton.FloatingActionButton;
import com.bili.diushoujuaner.widget.scrollview.OnChangeHeadStatusListener;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ContactDetailActivity extends BaseActivity<ContactDetailActivityPresenter> implements IContactDetailView, View.OnClickListener, OnChangeHeadStatusListener {

    @Bind(R.id.customGridView)
    CustomGridView customGridView;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;
    @Bind(R.id.txtFriendName)
    TextView txtFriendName;
    @Bind(R.id.txtRecent)
    TextView txtRecent;
    @Bind(R.id.txtFriendAutograph)
    AlignTextView txtFriendAutograph;
    @Bind(R.id.btnStartMsg)
    Button btnStartMsg;
    @Bind(R.id.ivArrowRight)
    ImageView ivArrowRight;
    @Bind(R.id.layoutDetail)
    RelativeLayout layoutDetail;
    @Bind(R.id.layoutRecent)
    RelativeLayout layoutRecent;
    @Bind(R.id.layoutTip)
    RelativeLayout layoutTip;
    @Bind(R.id.ivTip)
    ImageView ivTip;
    @Bind(R.id.btnFocus)
    FloatingActionButton btnFocus;
    @Bind(R.id.ivWallPaper)
    SimpleDraweeView ivWallPaper;
    @Bind(R.id.layoutBottom)
    RelativeLayout layoutBottom;
    @Bind(R.id.btnRight)
    ImageButton btnRight;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;
    @Bind(R.id.txtGender)
    TextView txtGender;
    @Bind(R.id.txtBirth)
    TextView txtBirth;
    @Bind(R.id.txtLocat)
    TextView txtLocat;
    @Bind(R.id.txtHomeTown)
    TextView txtHomeTown;

    private ContactRecentGalleryAdapter contactRecentGalleryAdapter;
    private List<PictureDto> pictureList;
    private FriendVo friendVo;
    private long userNo;
    private boolean isFriended, isOwner;
    public static final String TAG = "ContactDetailActivity";

    @Override
    public void initIntentParam(Intent intent) {
        userNo = intent.getLongExtra(TAG, -1);
    }

    @Override
    public void beforeInitView() {
        pictureList = new ArrayList<>();
        basePresenter = new ContactDetailActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_detail);
    }

    @Override
    public void resetStatus() {
        FrameLayout.LayoutParams lpHead = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);

        RelativeLayout.LayoutParams lpFocus = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.x128), (int) getResources().getDimension(R.dimen.x128));
        lpFocus.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpFocus.setMargins(0, tintManager.getConfig().getStatusBarHeight() + (int) getResources().getDimension(R.dimen.x416), (int) getResources().getDimension(R.dimen.x24), 0);
        btnFocus.setLayoutParams(lpFocus);
        btnFocus.setPadding(0, 0, 0, 0);
    }

    @Override
    public void setViewStatus() {
        layoutHead.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_THEME_MAIN));
        layoutRecent.setOnClickListener(this);
        btnStartMsg.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        reboundScrollView.setChangeHeadStatusListener(this);
        customGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ContactDetailActivity.this, SpaceActivity.class).putExtra(SpaceActivity.TAG, userNo));
            }
        });
        ivArrowRight.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
        isFriended = getBindPresenter().isFriend(userNo);
        isOwner = getBindPresenter().isOwner(userNo);
        if (isFriended) {
            showPageHead(null, R.mipmap.icon_menu, null);
            btnStartMsg.setText("发消息");
        } else if (isOwner) {
            showPageHead(null, null, null);
            btnFocus.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.GONE);
        } else {
            showPageHead(null, null, null);
            btnStartMsg.setText("加好友");
        }
        getBindPresenter().getFriendVo(userNo);
        getBindPresenter().getRecentRecall(userNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutRecent:
                startActivity(new Intent(ContactDetailActivity.this, SpaceActivity.class).putExtra(SpaceActivity.TAG, userNo));
                break;
            case R.id.btnStartMsg:
                if (isFriended) {
                    getBindPresenter().setCurrentChatting(this.friendVo.getFriendNo(), ConstantUtil.CHAT_FRI);
                    EventBus.getDefault().post(new StartChattingEvent());
                    startActivity(new Intent(ContactDetailActivity.this, MessageActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(ContactDetailActivity.this, ContentEditActivity.class)
                            .putExtra(ContentEditActivity.TAG_TYPE, ConstantUtil.EDIT_CONTENT_FRIEND_APPLY)
                            .putExtra(ContentEditActivity.TAG_CONTENT, "")
                            .putExtra(ContentEditActivity.TAG_CONTNO, userNo));
                }
                break;
            case R.id.btnRight:
                DialogTool.createFriendDetailDialog(context, new OnFriendDetailClickListener() {
                    @Override
                    public void onModifyRemarkClick() {
                        startActivity(new Intent(ContactDetailActivity.this, ContentEditActivity.class)
                                .putExtra(ContentEditActivity.TAG_TYPE, ConstantUtil.EDIT_CONTENT_FRIEND_REMARK)
                                .putExtra(ContentEditActivity.TAG_CONTENT, friendVo.getDisplayName())
                                .putExtra(ContentEditActivity.TAG_CONTNO, userNo));
                    }

                    @Override
                    public void onDeleteFriendClick() {
                        showDeleteWarning();
                    }
                });
                break;
        }
    }

    private void showDeleteWarning(){
        DialogTool.createDeleteContactDialog(context,"确定解除童友关系？", new OnDialogPositiveClickListener() {
            @Override
            public void onPositiveClicked() {
                getBindPresenter().getFriendDelete(userNo);
            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRemarkEvent(UpdateRemarkEvent updateRemarkEvent) {
        if (this.friendVo.getFriendNo() != updateRemarkEvent.getFriendNo()) {
            return;
        }
        friendVo.setDisplayName(updateRemarkEvent.getRemark());
        showPageHead(friendVo.getDisplayName(), R.mipmap.icon_menu, null);
    }

    @Override
    public void showContactInfo(FriendVo friendVo) {
        this.friendVo = friendVo;
        if (friendVo != null) {
            if (isFriended) {
                showPageHead(friendVo.getDisplayName(), R.mipmap.icon_menu, null);
                btnStartMsg.setText("发消息");
            } else if (isOwner) {
                showPageHead("", null, null);
                layoutBottom.setVisibility(View.GONE);
            } else {
                showPageHead("", null, null);
                btnStartMsg.setText("加好友");
            }

            layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
            layoutDetail.setVisibility(View.VISIBLE);
            layoutTip.setVisibility(View.GONE);
            setTintStatusColor(R.color.TRANSPARENT_BLACK);

            CommonUtil.displayDraweeView(friendVo.getPicPath(), ivWallPaper);
            txtFriendName.setText(friendVo.getNickName());
            txtFriendAutograph.setText(friendVo.getAutograph());

            txtGender.setText(friendVo.getGender() == 1 ? "男" : "女");
            txtBirth.setText(StringUtil.isEmpty(friendVo.getBirthday()) ? "未填" : friendVo.getBirthday());
            txtLocat.setText(StringUtil.isEmpty(friendVo.getLocation()) ? "未填" : friendVo.getLocation());
            txtHomeTown.setText(StringUtil.isEmpty(friendVo.getHomeTown()) ? "未填" : friendVo.getHomeTown());
        } else {
            layoutDetail.setVisibility(View.GONE);
            layoutTip.setVisibility(View.VISIBLE);
            ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));
        }
    }

    @Override
    public void showRecent(RecallDto recallDto) {
        layoutRecent.setVisibility(View.VISIBLE);
        if (recallDto != null) {
            if (recallDto.getPictureList() != null && !recallDto.getPictureList().isEmpty()) {
                customGridView.setVisibility(View.VISIBLE);
                txtRecent.setVisibility(View.GONE);
                if (recallDto.getPictureList().size() <= 3) {
                    pictureList.addAll(recallDto.getPictureList());
                } else {
                    pictureList.addAll(recallDto.getPictureList().subList(0, 3));
                }
                contactRecentGalleryAdapter = new ContactRecentGalleryAdapter(this, pictureList);
                customGridView.setAdapter(contactRecentGalleryAdapter);
            } else {
                customGridView.setVisibility(View.GONE);
                txtRecent.setVisibility(View.VISIBLE);
                txtRecent.setText(recallDto.getContent());
                txtRecent.setTextColor(ContextCompat.getColor(context, R.color.TC_333333));
            }
        } else {
            txtRecent.setTextColor(ContextCompat.getColor(context, R.color.TC_8A8A8A));
            txtRecent.setVisibility(View.VISIBLE);
            txtRecent.setText("暂无发表");
        }
    }

}
