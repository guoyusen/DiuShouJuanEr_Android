package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.MemberAddAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.callback.OnMemberChoseListener;
import com.bili.diushoujuaner.model.eventhelper.ContactUpdatedEvent;
import com.bili.diushoujuaner.model.eventhelper.UnGroupPartyEvent;
import com.bili.diushoujuaner.presenter.presenter.MemberAddActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.MemberAddActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IMemberAddView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/4/3.
 */
public class MemberAddActivity extends BaseActivity<MemberAddActivityPresenter> implements IMemberAddView, OnMemberChoseListener {

    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.listView)
    CustomListView listView;
    @Bind(R.id.ivTip)
    ImageView ivTip;
    @Bind(R.id.layoutTip)
    RelativeLayout layoutTip;

    public static final String TAG = "MemberAddActivity";

    private List<MemberVo> memberVoList;
    private List<FriendVo> friendVoList;
    private List<FriendVo> adapterFriendVoList;
    private long partyNo;
    private boolean isFriendListLoad = false;
    private boolean isMemberListLoad = false;
    private MemberAddAdapter memberAddAdapter;
    private Object lock;

    @Override
    public void initIntentParam(Intent intent) {
        partyNo = intent.getLongExtra(TAG, -1);
    }

    @Override
    public void beforeInitView() {
        basePresenter = new MemberAddActivityPresenterImpl(this, context);
        memberVoList = new ArrayList<>();
        friendVoList = new ArrayList<>();
        adapterFriendVoList = new ArrayList<>();
        lock = new Object();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_member_add);
    }

    @Override
    public void setViewStatus() {
        showPageHead("添加成员", null, "提交");
        memberAddAdapter = new MemberAddAdapter(context, adapterFriendVoList);
        memberAddAdapter.setMemberChoseListener(this);
        listView.setAdapter(memberAddAdapter);

        txtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBindPresenter().getMembersAddToParty(memberVoList.size() > 0 ? memberVoList.get(0).getPartyNo() : -1, memberAddAdapter.getChoseList());
            }
        });

        ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));
        getBindPresenter().getMemberVoList(partyNo);
        getBindPresenter().getContactList();
    }

    @Override
    public void onMemberChose(int count) {
        if(count <= 0){
            txtRight.setText("提交");
        }else {
            txtRight.setText("提交(" + count + ")");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContactUpdatedEvent(ContactUpdatedEvent contactUpdatedEvent){
        getBindPresenter().getMemberVoList(partyNo);
    }

    private void showResult(){
        if(!CommonUtil.isEmpty(friendVoList)){
            for(MemberVo memberVo : memberVoList){
                for(FriendVo friendVo : friendVoList){
                    if(memberVo.getUserNo() == friendVo.getFriendNo()){
                        friendVoList.remove(friendVo);
                        break;
                    }
                }
            }
        }
        if(CommonUtil.isEmpty(friendVoList)){
            layoutTip.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            layoutTip.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            memberAddAdapter.refresh(friendVoList);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnGroupPartyEvent(UnGroupPartyEvent unGroupPartyEvent){
        if(partyNo == unGroupPartyEvent.getPartyNo()){
            finishView();
        }
    }

    @Override
    public void showMemberVoList(List<MemberVo> memberVoList) {
        synchronized (lock){
            isMemberListLoad = true;
            this.memberVoList.clear();
            this.memberVoList.addAll(memberVoList);
            if(isFriendListLoad){
                showResult();
            }
        }
    }

    @Override
    public void showContactList(List<FriendVo> friendVoList) {
        synchronized (lock){
            isFriendListLoad = true;
            this.friendVoList.clear();
            this.friendVoList.addAll(friendVoList);
            if(isMemberListLoad){
                showResult();
            }
        }
    }
}
