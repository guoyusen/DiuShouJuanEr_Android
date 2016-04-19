package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.MessageActivity;
import com.bili.diushoujuaner.adapter.ChattingAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.callback.OnChatReadDismissListener;
import com.bili.diushoujuaner.model.eventhelper.ShowHeadEvent;
import com.bili.diushoujuaner.model.eventhelper.ShowMainMenuEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateMessageEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatedContactEvent;
import com.bili.diushoujuaner.presenter.presenter.ChattingFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ChattingFragmentPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IChattingView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.vo.ChattingVo;
import com.bili.diushoujuaner.widget.LoadMoreListView;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenu;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenuCreator;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenuItem;
import com.bili.diushoujuaner.widget.swipemenu.SwipeMenuListView;
import com.bili.diushoujuaner.widget.waveswipe.WaveSwipeRefreshLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ChattingFragment extends BaseFragment<ChattingFragmentPresenter> implements IChattingView, WaveSwipeRefreshLayout.OnRefreshListener, View.OnClickListener, OnChatReadDismissListener {

    @Bind(R.id.listViewChatting)
    SwipeMenuListView listViewChatting;
    @Bind(R.id.waveSwipeRefreshLayout)
    WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;

    private List<ChattingVo> chattingVoList;
    private ChattingAdapter chattingAdapter;
    private String headPicUrl;

    public static ChattingFragment instantiation(int position) {
        ChattingFragment fragment = new ChattingFragment();
        EventBus.getDefault().register(fragment);
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
        showPageHead("消息", null, null);
        ivNavHead.setOnClickListener(this);

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

        waveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        waveSwipeRefreshLayout.setWaveColor(ContextCompat.getColor(context, R.color.COLOR_THEME_MAIN));
        waveSwipeRefreshLayout.setOnRefreshListener(this);

        Common.displayDraweeView(headPicUrl, ivNavHead);

        getBindPresenter().getChattingList();
        getBindPresenter().getOffMessage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivNavHead:
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                waveSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowHeadEvent(ShowHeadEvent showHeadEvent) {
        this.headPicUrl = showHeadEvent.getHeadPicUrl();
        if(ivNavHead != null){
            Common.displayDraweeView(this.headPicUrl, ivNavHead);
        }
    }

    // 通讯录更新之后，需要重新刷新recall的右侧标志
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatedContactEvent(UpdatedContactEvent updatedContactEvent) {
        if (chattingAdapter != null) {
            chattingAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessMessageEvent(UpdateMessageEvent updateMessageEvent){
        getBindPresenter().getChattingVoListFromTemper();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
