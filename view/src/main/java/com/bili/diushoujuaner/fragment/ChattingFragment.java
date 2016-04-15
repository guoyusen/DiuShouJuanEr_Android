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
import com.bili.diushoujuaner.model.eventhelper.ShowHeadEvent;
import com.bili.diushoujuaner.model.eventhelper.ShowMainMenuEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdatedContactEvent;
import com.bili.diushoujuaner.presenter.presenter.ChattingFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ChattingFragmentPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IChattingView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.vo.ChattingVo;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;
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
public class ChattingFragment extends BaseFragment<ChattingFragmentPresenter> implements IChattingView, WaveSwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
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
        if(chattingAdapter != null){
            return;
        }
        chattingAdapter = new ChattingAdapter(getContext(), chattingVoList);
        customListViewRefresh.setAdapter(chattingAdapter);
    }

    @Override
    public void updateAdapter() {
        if(chattingAdapter != null){
            //防止离线信息快速回调，而本地聊天列表却没有加载完成
            chattingAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setViewStatus() {
        showPageHead("消息", null, null);
        ivNavHead.setOnClickListener(this);

        customListViewRefresh.setCanLoadMore(false);
        customListViewRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBindPresenter().setCurrentChatting(chattingAdapter.getItem(position).getUserNo(), chattingAdapter.getItem(position).getMsgType());
                startActivity(new Intent(getContext(), MessageActivity.class));
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

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
