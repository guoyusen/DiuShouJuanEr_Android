package com.bili.diushoujuaner.activity;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.MessageAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.callback.OnReSendListener;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.presenter.presenter.MessageActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.MessageActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IMessageView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.utils.entity.vo.MessageVo;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.PopKeepListView;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnDialogPositiveClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class MessageActivity extends BaseActivity<MessageActivityPresenter> implements IMessageView, View.OnClickListener, OnReSendListener {

    public static final String TAG = "MessageActivity";
    @Bind(R.id.listView)
    PopKeepListView listView;
    @Bind(R.id.txtEditor)
    CustomEditText txtEditor;
    @Bind(R.id.layoutSend)
    RelativeLayout layoutSend;

    private MessageAdapter messageAdapter;
    private List<MessageVo> messageVoList;
    private HashMap<String, MessageVo> messageVoHashMap;

    class CommentTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().trim().length() > 0){
                layoutSend.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_send_ready_bg));
            }else{
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
        EventBus.getDefault().register(this);
        showPageHead("", null, null);

        layoutSend.setOnClickListener(this);

        txtEditor.addTextChangedListener(new CommentTextWatcher());
        messageAdapter = new MessageAdapter(context, messageVoList, getBindPresenter().getOwnerNo());
        messageAdapter.setReSendListener(this);
        listView.setAdapter(messageAdapter);
        getBindPresenter().getContactInfo();
        getBindPresenter().getMessageList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutSend:
                getBindPresenter().saveMessageVo(txtEditor.getText().toString(), Constant.CHAT_CONTENT_TEXT);
                txtEditor.setText(null);
                break;
        }
    }

    @Override
    public void showContactInfo(FriendVo friendVo) {
        showPageHead(friendVo.getNickName(), R.mipmap.icon_menu_user, null);
    }

    @Override
    public void showContactInfo(PartyVo partyVo) {
        showPageHead(partyVo.getDisplayName(), R.mipmap.icon_friend, null);
    }

    @Override
    public void showMessageList(List<MessageVo> messageVoList) {
        for(MessageVo messageVo : messageVoList){
            if(messageVo.getStatus() == Constant.MESSAGE_STATUS_SENDING){
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
    public void onUpdateMessageEvent(UpdateMessageEvent updateMessageEvent){
        if(!getBindPresenter().validateUpdateEvent(updateMessageEvent)){
            //该更新事件不属于这个页面的
            return;
        }
        switch (updateMessageEvent.getType()){
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
        EventBus.getDefault().unregister(this);
        super.onPageDestroy();
    }
}
