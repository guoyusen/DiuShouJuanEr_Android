package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.MessageAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.callback.OnReSendListener;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.NoticeAddMemberEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.presenter.presenter.MessageActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.MessageActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IMessageView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.MessageListView;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class MessageActivity extends BaseActivity<MessageActivityPresenter> implements IMessageView, View.OnClickListener, OnReSendListener, View.OnTouchListener, MessageListView.OnLoadMoreListener {

    public static final String TAG = "MessageActivity";
    @Bind(R.id.listView)
    MessageListView listView;
    @Bind(R.id.txtEditor)
    CustomEditText txtEditor;
    @Bind(R.id.layoutSend)
    RelativeLayout layoutSend;
    @Bind(R.id.btnRight)
    ImageButton btnRight;

    private MessageAdapter messageAdapter;
    private List<MessageVo> messageVoList;
    private HashMap<String, MessageVo> messageVoHashMap;

    private boolean isPartyChatting = false;
    private FriendVo friendVo;
    private PartyVo partyVo;

    class CommentTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 0) {
                layoutSend.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_send_ready_bg));
            } else {
                layoutSend.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_send_close_bg));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    @Override
    public void beforeInitView() {
        basePresenter = new MessageActivityPresenterImpl(this, context);
        messageVoList = new ArrayList<>();
        messageVoHashMap = new HashMap<>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    public void setViewStatus() {
        showPageHead("", null, null);

        layoutSend.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        txtEditor.addTextChangedListener(new CommentTextWatcher());
        messageAdapter = new MessageAdapter(context, messageVoList, getBindPresenter().getOwnerNo());
        messageAdapter.setReSendListener(this);
        listView.setAdapter(messageAdapter);
        listView.setOnTouchListener(this);
        listView.setOnLoadMoreListener(this);
        getBindPresenter().getContactInfo();
        getBindPresenter().getMessageList();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CommonUtil.hideSoftInputFromWindow(context, txtEditor);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutSend:
                if(!TextUtils.isEmpty(txtEditor.getText())){
                    getBindPresenter().saveMessageVo(txtEditor.getText().toString(), ConstantUtil.CHAT_CONTENT_TEXT);
                    txtEditor.setText(null);
                }
                break;
            case R.id.btnRight:
                getBindPresenter().getNextActivity();
                break;
        }
    }

    @Override
    public void showContactInfo(FriendVo friendVo) {
        if(friendVo == null){
            return;
        }
        showPageHead(friendVo.getDisplayName(), R.mipmap.icon_menu_user, null);
        this.friendVo = friendVo;
        this.isPartyChatting = false;
    }

    @Override
    public void showContactInfo(PartyVo partyVo) {
        showPageHead(partyVo.getDisplayName(), R.mipmap.icon_friend, null);
        this.partyVo = partyVo;
        this.isPartyChatting = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoticeAddMemberEvent(NoticeAddMemberEvent noticeAddMemberEvent){
        if(isPartyChatting && partyVo.getPartyNo() == noticeAddMemberEvent.getMessageVo().getToNo() && messageAdapter != null){
            messageAdapter.addLast(noticeAddMemberEvent.getMessageVo());
        }
    }

    @Override
    public void showNextActivity(int showType) {
        switch (showType){
            case ConstantUtil.SHOW_TYPE_CHATTING_SETTING:
                startActivity(new Intent(MessageActivity.this, ChattingSettingActivity.class));
                break;
            case ConstantUtil.SHOW_TYPE_PARTY_DETAIL:
                startActivity(new Intent(MessageActivity.this, PartyDetailActivity.class)
                .putExtra(PartyDetailActivity.TAG_TYPE, PartyDetailActivity.TYPE_CONTACT));
                break;
        }
    }

    @Override
    public void loadComplete() {
        listView.setListViewStateComplete();
    }

    @Override
    public void loadFinish() {
        listView.setListViewStateFinished();
    }

    @Override
    public void onLoadMore() {
        getBindPresenter().getMessageList();
    }

    @Override
    public void showMoreMessageList(List<MessageVo> messageVoList) {
        for (MessageVo messageVo : messageVoList) {
            if (messageVo.getStatus() == ConstantUtil.MESSAGE_STATUS_SENDING) {
                messageVoHashMap.put(messageVo.getSerialNo(), messageVo);
            }
        }
        messageAdapter.addFirst(messageVoList);
        listView.setSelection(messageVoList.size());
    }

    @Override
    public void showMessageList(List<MessageVo> messageVoList) {
        for (MessageVo messageVo : messageVoList) {
            if (messageVo.getStatus() == ConstantUtil.MESSAGE_STATUS_SENDING) {
                messageVoHashMap.put(messageVo.getSerialNo(), messageVo);
            }
        }
        messageAdapter.refresh(messageVoList);
        listView.setSelection(messageAdapter.getCount() - 1);
    }

    @Override
    public void onReSendMessageVo(final MessageVo messageVo) {
        DialogTool.createReSendDialog(context, new OnDialogPositiveClickListener() {
            @Override
            public void onPositiveClicked() {
                getBindPresenter().saveMessageVo(messageVo.getContent(), messageVo.getConType());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePartyEvent(UpdatePartyEvent updatePartyEvent){
        if(this.isPartyChatting && this.partyVo.getPartyNo() == updatePartyEvent.getPartyNo()){
            switch (updatePartyEvent.getType()){
                case ConstantUtil.CHAT_PARTY_NAME:
                    getBindPresenter().getContactInfo();
                    break;
                case ConstantUtil.CHAT_PARTY_MEMBER_NAME:
                    if (messageAdapter != null) {
                        messageAdapter.notifyDataSetInvalidated();
                    }
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteContactEvent(DeleteContactEvent deleteContactEvent){
        if(deleteContactEvent.getType() == ConstantUtil.DELETE_CONTACT_FRIEND
                && !this.isPartyChatting
                && deleteContactEvent.getContNo() == this.friendVo.getFriendNo()){
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateMessageEvent(UpdateMessageEvent updateMessageEvent) {
        if (!getBindPresenter().validateUpdateEvent(updateMessageEvent)) {
            //该更新事件不属于这个页面的
            return;
        }
        switch (updateMessageEvent.getType()) {
            case UpdateMessageEvent.MESSAGE_SEND:
                messageVoHashMap.put(updateMessageEvent.getMessageVo().getSerialNo(), updateMessageEvent.getMessageVo());
                messageAdapter.addLast(updateMessageEvent.getMessageVo());
                listView.setSelection(messageAdapter.getCount() - 1);

                break;
            case UpdateMessageEvent.MESSAGE_RECEIVE:
                messageAdapter.addLast(updateMessageEvent.getMessageVo());
                listView.setSelection(messageAdapter.getCount() - 1);
                break;
            case UpdateMessageEvent.MESSAGE_STATUS:
                messageVoHashMap.get(updateMessageEvent.getMessageVo().getSerialNo()).setStatus(updateMessageEvent.getMessageVo().getStatus());
                messageVoHashMap.remove(updateMessageEvent.getMessageVo().getSerialNo());
                messageAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onPageDestroy() {
        getBindPresenter().clearCurrentChat();
        super.onPageDestroy();
    }
}
