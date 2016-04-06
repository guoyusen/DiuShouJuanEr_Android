package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.ChattingActivity;
import com.bili.diushoujuaner.adapter.MessageAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.utils.event.ShowHeadEvent;
import com.bili.diushoujuaner.utils.event.ShowMainMenuEvent;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.model.apihelper.response.MessageDto;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;
import com.bili.diushoujuaner.widget.waveswipe.WaveSwipeRefreshLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class MessageFragment extends BaseFragment implements WaveSwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.waveSwipeRefreshLayout)
    WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;

    private List<MessageDto> listMessage;
    private MessageAdapter messageAdapter;
    private String headPicUrl;

    public static MessageFragment instantiation(int position) {
        MessageFragment fragment = new MessageFragment();
        EventBus.getDefault().register(fragment);
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void beforeInitView() {
        listMessage = new ArrayList<>();
    }

    @Override
    public void setViewStatus() {
        showPageHead("消息", null, null);
        ivNavHead.setOnClickListener(this);

        for(int i=0;i<10;i++){
            listMessage.add(new MessageDto());
        }
        messageAdapter = new MessageAdapter(getContext(), listMessage);
        customListViewRefresh.setCanLoadMore(false);
        customListViewRefresh.setAdapter(messageAdapter);
        customListViewRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), ChattingActivity.class));
            }
        });

        waveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        waveSwipeRefreshLayout.setWaveColor(ContextCompat.getColor(context, R.color.COLOR_THEME));
        waveSwipeRefreshLayout.setOnRefreshListener(this);

        Common.displayDraweeView(headPicUrl, ivNavHead);
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

    @Subscribe
    public void showHead(ShowHeadEvent showHeadEvent) {
        this.headPicUrl = showHeadEvent.getHeadPicUrl();
        if(ivNavHead != null){
            Common.displayDraweeView(this.headPicUrl, ivNavHead);
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
