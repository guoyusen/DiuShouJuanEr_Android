package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.MessageActivity;
import com.bili.diushoujuaner.adapter.ChattingAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.callback.OnChatReadDismissListener;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.HeartBeatEvent;
import com.bili.diushoujuaner.model.eventhelper.LoginEvent;
import com.bili.diushoujuaner.model.eventhelper.LoginngEvent;
import com.bili.diushoujuaner.model.eventhelper.LogoutEvent;
import com.bili.diushoujuaner.model.eventhelper.NoticeAddMemberEvent;
import com.bili.diushoujuaner.model.eventhelper.ShowMainMenuEvent;
import com.bili.diushoujuaner.model.eventhelper.UnGroupPartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatePartyEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateRemarkEvent;
import com.bili.diushoujuaner.model.eventhelper.ContactUpdatedEvent;
import com.bili.diushoujuaner.model.messagehelper.LocalClient;
import com.bili.diushoujuaner.model.preferhelper.ConnectionPreference;
import com.bili.diushoujuaner.presenter.presenter.ChattingFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ChattingFragmentPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IChattingView;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.ChattingVo;
import com.bili.diushoujuaner.widget.scrollview.OnScrollRefreshListener;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenu;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenuCreator;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenuItem;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenuListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ChattingFragment extends BaseFragment<ChattingFragmentPresenter> implements IChattingView, View.OnClickListener, OnChatReadDismissListener, OnScrollRefreshListener {

    @Bind(R.id.listViewChatting)
    SwipeMenuListView listViewChatting;
    @Bind(R.id.btnMenu)
    ImageButton btnMenu;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;

    private List<ChattingVo> chattingVoList;
    private ChattingAdapter chattingAdapter;

    public static ChattingFragment instantiation(int position) {
        ChattingFragment fragment = new ChattingFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_chatting;
    }

    @Override
    public void beforeInitView() {
        chattingVoList = new ArrayList<>();
        basePresenter = new ChattingFragmentPresenterImpl(this, context);
    }

    @Override
    public void showChatting(List<ChattingVo> chattingVoList) {
        //每次启动app，该方法必须 保证回调，只在初始化的时候做一次，之后便是添加信息，即便是下拉刷新，也是增量更新
        if(chattingAdapter == null){
            return;
        }
        chattingAdapter.clear();
        chattingAdapter.refresh(chattingVoList);
    }

    private SwipeMenuCreator getSwipeMenuCreator(){
        SwipeMenuCreator creator = new SwipeMenuCreator(){
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                openItem.setBackground(R.color.COLOR_RED);
                openItem.setWidth((int)context.getResources().getDimension(R.dimen.x168));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };

        return creator;
    }

    @Override
    public void setViewStatus() {
        btnMenu.setOnClickListener(this);

        reboundScrollView.setScrollRefreshListener(this);
        chattingAdapter = new ChattingAdapter(getContext(), chattingVoList);
        chattingAdapter.setOnChatReadDismissListener(this);
        listViewChatting.setAdapter(chattingAdapter);
        listViewChatting.setMenuCreator(getSwipeMenuCreator());
        listViewChatting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chattingAdapter.getItem(position).setUnReadCount(0);
                chattingAdapter.notifyDataSetChanged();
                getBindPresenter().updateMessageRead(chattingAdapter.getItem(position).getUserNo(), chattingAdapter.getItem(position).getMsgType());
                getBindPresenter().setCurrentChatting(chattingAdapter.getItem(position).getUserNo(), chattingAdapter.getItem(position).getMsgType());
                startActivity(new Intent(getContext(), MessageActivity.class));
            }
        });
        listViewChatting.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                ChattingVo chattingVo = chattingAdapter.remove(position);
                getBindPresenter().deleteChattingVo(chattingVo.getUserNo(), chattingVo.getMsgType());
            }
        });
        if(getBindPresenter().isConnected()){
            showPageHead("消息-在线", null, null);
        }else{
            showPageHead("消息-离线", null, null);
        }

        getBindPresenter().getChattingList();
        getBindPresenter().getOffMessage();
    }

    @Override
    public void onScrollRefresh() {
        LocalClient.getInstance(context).sendMessageToService(ConstantUtil.HANDLER_RECONNECTION, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMenu:
                EventBus.getDefault().post(new ShowMainMenuEvent());
                break;
        }
    }

    @Override
    public void onChatReadDismiss(int position) {
        chattingAdapter.getItem(position).setUnReadCount(0);
        chattingAdapter.notifyDataSetChanged();
        getBindPresenter().updateMessageRead(chattingAdapter.getItem(position).getUserNo(), chattingAdapter.getItem(position).getMsgType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteContactEvent(DeleteContactEvent deleteContactEvent){
        getBindPresenter().getChattingVoListFromTemper();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContactUpdatedEvent(ContactUpdatedEvent contactUpdatedEvent) {
        if (chattingAdapter == null) {
            return;
        }
        chattingAdapter.notifyDataSetInvalidated();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRemarkEvent(UpdateRemarkEvent updateRemarkEvent){
        if (chattingAdapter == null) {
            return;
        }
        chattingAdapter.notifyDataSetInvalidated();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessMessageEvent(UpdateMessageEvent updateMessageEvent){
        getBindPresenter().getChattingVoListFromTemper();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnGroupPartyEvent(UnGroupPartyEvent unGroupPartyEvent){
        getBindPresenter().getChattingVoListFromTemper();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginEvent loginEvent){
        showPageHead("消息-在线", null, null);
        getBindPresenter().getOffMessage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeartBeatEvent(HeartBeatEvent heartBeatEvent){
        showPageHead("消息-在线", null, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent logoutEvent){
        showPageHead("消息-离线", null, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginngEvent(LoginngEvent loginngEvent){
        showPageHead("消息-连接中...", null, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatePartyEvent(UpdatePartyEvent updatePartyEvent){
        switch (updatePartyEvent.getType()){
            case ConstantUtil.CHAT_PARTY_HEAD:
                if (chattingAdapter != null) {
                    chattingAdapter.notifyDataSetInvalidated();
                }
                break;
            case ConstantUtil.CHAT_PARTY_NAME:
                if (chattingAdapter != null) {
                    chattingAdapter.notifyDataSetInvalidated();
                }
                break;
            case ConstantUtil.CHAT_PARTY_MEMBER_NAME:
                if (chattingAdapter != null) {
                    chattingAdapter.notifyDataSetInvalidated();
                }
                break;
            case ConstantUtil.CHAT_PARTY_MEMBER_EXIT:
                getBindPresenter().getChattingList();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoticeAddMemberEvent(NoticeAddMemberEvent noticeAddMemberEvent){
        getBindPresenter().getChattingVoListFromTemper();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
