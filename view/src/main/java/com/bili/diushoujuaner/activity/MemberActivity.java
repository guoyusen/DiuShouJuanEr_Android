package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.MemberAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.callback.OnDeleteMemberListener;
import com.bili.diushoujuaner.model.eventhelper.UnGroupPartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.presenter.presenter.MemberActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.MemberActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IMemberView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.comparator.MemberVoComparator;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/4/3.
 */
public class MemberActivity extends BaseActivity<MemberActivityPresenter> implements IMemberView , View.OnClickListener, OnDeleteMemberListener {

    @Bind(R.id.listViewMember)
    CustomListView listViewMember;
    @Bind(R.id.txtMemberCount)
    TextView txtMemberCount;
    @Bind(R.id.txtRight)
    TextView txtRight;

    public static final String TAG_VO = "MemberActivity_PartyVo";
    public static final String TAG_TYPE = "MemberActivity_Type";
    public static final String TAG_MEMBERLIST = "MemberActivity_MemberList";

    public static final int TYPE_SERVER = 1;
    public static final int TYPE_LOCAL = 2;

    private PartyVo partyVo;
    private List<MemberVo> memberVoList;
    private int type;

    private MemberAdapter memberAdapter;

    @Override
    public void initIntentParam(Intent intent) {
        type = intent.getIntExtra(TAG_TYPE, -1);
        partyVo = intent.getParcelableExtra(TAG_VO);
        if (type == TYPE_SERVER) {
            memberVoList = intent.getParcelableArrayListExtra(TAG_MEMBERLIST);
            if (memberVoList.size() == 1) {
                memberVoList.get(0).setSortLetter("★");
            } else {
                Collections.sort(memberVoList, new MemberVoComparator());
            }
        }
    }

    @Override
    public void beforeInitView() {
        basePresenter = new MemberActivityPresenterImpl(this, context);
        if (type == TYPE_LOCAL) {
            memberVoList = new ArrayList<>();
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_member);
    }

    @Override
    public void setViewStatus() {
        if (partyVo.getOwnerNo() == basePresenter.getCurrentUserNo()) {
            showPageHead("群成员", null, "编辑");
        } else {
            showPageHead("群成员", null, null);
        }
        txtRight.setOnClickListener(this);
        memberAdapter = new MemberAdapter(context, memberVoList);
        memberAdapter.setDeleteMemberListener(this);
        listViewMember.setAdapter(memberAdapter);
        listViewMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ContactDetailActivity.class);
                intent.putExtra(ContactDetailActivity.TAG, memberAdapter.getItem(position).getUserNo());
                startActivity(intent);
            }
        });

        if (type == TYPE_LOCAL) {
            getBindPresenter().getMemberList(partyVo.getPartyNo());
        } else {
            txtMemberCount.setText(memberVoList.size() + "个成员");
        }
    }

    @Override
    public void onDeleteMember(final long memberNo) {
        DialogTool.createDeleteContactDialog(context, "确定踢出该成员？", new OnDialogPositiveClickListener() {
            @Override
            public void onPositiveClicked() {
                getBindPresenter().getMemberForceExit(partyVo.getPartyNo(), memberNo);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(memberAdapter.isEditable()){
            txtRight.setText("编辑");
            memberAdapter.setEditable(false);
        }else{
            txtRight.setText("完成");
            memberAdapter.setEditable(true);
        }
        memberAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void showMemberList(List<MemberVo> memberVoList) {
        memberAdapter.refresh(memberVoList);
        txtMemberCount.setText(memberVoList.size() + "个成员");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePartyEvent(UpdatePartyEvent updatePartyEvent) {
        if (updatePartyEvent.getPartyNo() == this.partyVo.getPartyNo()) {
            switch (updatePartyEvent.getType()) {
                case ConstantUtil.CHAT_PARTY_MEMBER_EXIT:
                    if(updatePartyEvent.getMemberNo() == basePresenter.getCurrentUserNo()){
                        finish();
                    }else{
                        getBindPresenter().getMemberList(partyVo.getPartyNo());
                    }
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnGroupPartyEvent(UnGroupPartyEvent unGroupPartyEvent){
        if(partyVo.getPartyNo() == unGroupPartyEvent.getPartyNo()){
            finishView();
        }
    }

}
