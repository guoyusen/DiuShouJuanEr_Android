package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ContactRecentGalleryAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.eventhelper.StartChattingEvent;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.presenter.presenter.ContactDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContactDetailActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContactDetailView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.dto.PictureDto;
import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.aligntextview.AlignTextView;
import com.bili.diushoujuaner.widget.floatingactionbutton.FloatingActionButton;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ContactDetailActivity extends BaseActivity<ContactDetailActivityPresenter> implements IContactDetailView, View.OnClickListener {

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

    private ContactRecentGalleryAdapter contactRecentGalleryAdapter;
    private List<PictureDto> pictureList;
    private FriendVo friendVo;
    private long userNo;
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
        FrameLayout.LayoutParams lpHead = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);

        RelativeLayout.LayoutParams lpFocus = new RelativeLayout.LayoutParams((int)getResources().getDimension(R.dimen.x128), (int)getResources().getDimension(R.dimen.x128));
        lpFocus.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpFocus.setMargins(0, tintManager.getConfig().getStatusBarHeight() + (int)getResources().getDimension(R.dimen.x416), (int)getResources().getDimension(R.dimen.x24), 0);
        btnFocus.setLayoutParams(lpFocus);
        btnFocus.setPadding(0, 0, 0, 0);
    }

    @Override
    public void setViewStatus() {
        showPageHead(null, R.mipmap.icon_menu, null);
        layoutHead.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_THEME_MAIN));

        layoutRecent.setOnClickListener(this);
        btnStartMsg.setOnClickListener(this);

        getBindPresenter().getFriendVo(userNo);
        getBindPresenter().getRecentRecall(userNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutRecent:
                startActivity(new Intent(ContactDetailActivity.this, SpaceActivity.class).putExtra(SpaceActivity.TAG,this.friendVo.getFriendNo()));
                break;
            case R.id.btnStartMsg:
                getBindPresenter().setCurrentChatting(this.friendVo.getFriendNo(), Constant.CHAT_FRI);
                EventBus.getDefault().post(new StartChattingEvent());
                startActivity(new Intent(ContactDetailActivity.this, MessageActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void showContactInfo(FriendVo friendVo) {
        this.friendVo = friendVo;
        if (friendVo != null) {
            layoutHead.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_black_down_bg));
            layoutDetail.setVisibility(View.VISIBLE);
            layoutTip.setVisibility(View.GONE);
            setTintStatusColor(R.color.TRANSPARENT_BLACK);

            Common.displayDraweeView(friendVo.getPicPath(), ivWallPaper);
            ivArrowRight.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_arrow_right, ContextCompat.getColor(context, R.color.COLOR_8A8A8A)));
            txtFriendName.setText(friendVo.getDisplayName());
            txtFriendAutograph.setText(friendVo.getAutograph());
        }else{
            layoutDetail.setVisibility(View.GONE);
            layoutTip.setVisibility(View.VISIBLE);
            ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));
        }
    }

    @Override
    public void showRecent(RecallDto recallDto) {
        layoutRecent.setVisibility(View.VISIBLE);
        if(recallDto != null){
            if(recallDto.getPictureList() != null && !recallDto.getPictureList().isEmpty()){
                customGridView.setVisibility(View.VISIBLE);
                txtRecent.setVisibility(View.GONE);
                if(recallDto.getPictureList().size() <= 3){
                    pictureList.addAll(recallDto.getPictureList());
                }else{
                    pictureList.addAll(recallDto.getPictureList().subList(0, 3));
                }
                contactRecentGalleryAdapter = new ContactRecentGalleryAdapter(this, pictureList);
                customGridView.setAdapter(contactRecentGalleryAdapter);
            }else{
                customGridView.setVisibility(View.GONE);
                txtRecent.setVisibility(View.VISIBLE);
                txtRecent.setText(recallDto.getContent());
                txtRecent.setTextColor(ContextCompat.getColor(context, R.color.TC_333333));
            }
        }else{
            txtRecent.setTextColor(ContextCompat.getColor(context, R.color.TC_8A8A8A));
            txtRecent.setVisibility(View.VISIBLE);
            txtRecent.setText("暂无发表");
        }
    }
}
