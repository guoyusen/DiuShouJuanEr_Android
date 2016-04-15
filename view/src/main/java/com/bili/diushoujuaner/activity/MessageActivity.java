package com.bili.diushoujuaner.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.MessageAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.MessageActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.MessageActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IMessageView;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.CustomEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class MessageActivity extends BaseActivity<MessageActivityPresenter> implements IMessageView {

    public static final String TAG = "MessageActivity";
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.txtComment)
    CustomEditText txtComment;
    @Bind(R.id.layoutSend)
    RelativeLayout layoutSend;

    private MessageAdapter messageAdapter;
    private List<MessageVo> messageVoList;

    @Override
    public void beforeInitView() {
        basePresenter = new MessageActivityPresenterImpl(this, context);
        messageVoList = new ArrayList<>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_chatting);
    }

    @Override
    public void setViewStatus() {
        showPageHead("", null, null);

        messageAdapter = new MessageAdapter(context, messageVoList, getBindPresenter().getOwnerNo());
        listView.setAdapter(messageAdapter);
        getBindPresenter().getContactInfo();
        getBindPresenter().getMessageList();
    }

    @Override
    public void showContactInfo(FriendVo friendVo) {
        showPageHead(friendVo.getNickName(), null, null);
    }

    @Override
    public void showContactInfo(PartyVo partyVo) {
        showPageHead(partyVo.getDisplayName(), null, null);
    }

    @Override
    public void showMessageList(List<MessageVo> messageVoList) {
        messageAdapter.refresh(messageVoList);
    }

}
