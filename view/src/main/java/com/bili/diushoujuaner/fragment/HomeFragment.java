package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.ProgressActivity;
import com.bili.diushoujuaner.activity.RecallAddActivity;
import com.bili.diushoujuaner.activity.RecallDetailActivity;
import com.bili.diushoujuaner.adapter.RecallAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.model.apihelper.response.RecallDto;
import com.bili.diushoujuaner.presenter.event.PublishRecallEvent;
import com.bili.diushoujuaner.presenter.presenter.HomeFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.HomeFragmentPresenterImpl;
import com.bili.diushoujuaner.presenter.publisher.OnPublishListener;
import com.bili.diushoujuaner.presenter.publisher.RecallPublisher;
import com.bili.diushoujuaner.presenter.view.IHomeView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.presenter.event.GoodRecallEvent;
import com.bili.diushoujuaner.presenter.event.RefreshRecallEvent;
import com.bili.diushoujuaner.presenter.event.ShowHeadEvent;
import com.bili.diushoujuaner.presenter.event.ShowMainMenuEvent;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.bili.diushoujuaner.widget.badgeview.BGABadgeImageView;
import com.bili.diushoujuaner.widget.badgeview.BGABadgeRelativeLayout;
import com.bili.diushoujuaner.widget.waveswipe.WaveSwipeRefreshLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/2.
 */
public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements WaveSwipeRefreshLayout.OnRefreshListener, IHomeView, View.OnClickListener, CustomListViewRefresh.OnLoadMoreListener, OnPublishListener {

    @Bind(R.id.listviewRecall)
    CustomListViewRefresh listviewRecall;
    @Bind(R.id.waveSwipeRefreshLayout)
    WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.layoutTip)
    RelativeLayout layoutTip;
    @Bind(R.id.ivTip)
    ImageView ivTip;
    @Bind(R.id.btnRight)
    ImageButton btnRight;
    @Bind(R.id.ivUploading)
    ImageView ivUploading;
    @Bind(R.id.layoutProgress)
    BGABadgeRelativeLayout layoutProgress;

    private List<RecallDto> recallDtoList;
    private RecallAdapter recallAdapter;
    private Handler handler;
    private boolean goodStatus = false;
    private boolean isGoodStatusInited = false;
    private CustomRunnable customRunnable;
    private long goodRecallNo;
    private String headPicUrl;
    private AnimationDrawable uplaodingAni;


    class CustomRunnable implements Runnable {
        @Override
        public void run() {
            // 重置，允许再次进行点击，并发送请求
            isGoodStatusInited = false;
            getBindPresenter().executeGoodChange(goodStatus, goodRecallNo);
        }
    }

    public static HomeFragment instantiation(int position) {
        HomeFragment fragment = new HomeFragment();
        EventBus.getDefault().register(fragment);
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void beforeInitView() {
        recallDtoList = new ArrayList<>();
        handler = new Handler();
        customRunnable = new CustomRunnable();
        basePresenter = new HomeFragmentPresenterImpl(this, context);
    }

    @Override
    public void setViewStatus() {
        showPageHead("首页", R.mipmap.icon_editor, null);

        RecallPublisher.getInstance(context).register(this);
        uplaodingAni = (AnimationDrawable)ivUploading.getDrawable();

        waveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        waveSwipeRefreshLayout.setWaveColor(ContextCompat.getColor(context, R.color.COLOR_THEME_MAIN));
        waveSwipeRefreshLayout.setOnRefreshListener(this);
        ivNavHead.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        layoutProgress.setOnClickListener(this);

        ivTip.setImageDrawable(new TintedBitmapDrawable(getResources(), R.mipmap.icon_nodata, ContextCompat.getColor(context, R.color.COLOR_BFBFBF)));
        Common.displayDraweeView(headPicUrl, ivNavHead);

        recallAdapter = new RecallAdapter(getContext(), recallDtoList, Constant.RECALL_ADAPTER_HOME, Constant.RECALL_GOOD_HOME_INDEX);
        listviewRecall.setAdapter(recallAdapter);
        listviewRecall.setCanLoadMore(true);
        listviewRecall.setOnLoadMoreListener(this);
        listviewRecall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), RecallDetailActivity.class);
                intent.putExtra(RecallDetailActivity.TAG, recallAdapter.getItem(position).getRecallNo());
                startActivity(intent);
            }
        });

        getBindPresenter().showRecallFromCache();
        getBindPresenter().getRecallList(Constant.REFRESH_DEFAULT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNavHead:
                EventBus.getDefault().post(new ShowMainMenuEvent());
                break;
            case R.id.btnRight:
                startActivity(new Intent(getContext(), RecallAddActivity.class));
                break;
            case R.id.layoutProgress:
                startActivity(new Intent(context, ProgressActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onProgress(int position, float progress) {
        if(layoutProgress != null){
            layoutProgress.setVisibility(View.VISIBLE);
            layoutProgress.showTextBadge((position + 1) + "");
        }
    }

    @Override
    public void onFinishPublish() {
        if(layoutProgress != null){
            layoutProgress.hiddenBadge();
            layoutProgress.setVisibility(View.GONE);
        }
        uplaodingAni.stop();
    }

    @Override
    public void onStartPublish() {
        if(layoutProgress != null){
            layoutProgress.hiddenBadge();
            layoutProgress.setVisibility(View.VISIBLE);
        }
        if(!uplaodingAni.isRunning()){
            uplaodingAni.start();
        }
    }

    @Override
    public void onRefresh() {
        listviewRecall.setListViewStateRefresh();
        getBindPresenter().getRecallList(Constant.REFRESH_INTENT);
    }

    @Override
    public void onLoadMore() {
        getBindPresenter().getMoreRecallList();
    }

    @Override
    public void showRecallList(List<RecallDto> recallDtoList) {
        recallAdapter.refresh(recallDtoList);
        if (Common.isEmpty(recallDtoList)) {
            layoutTip.setVisibility(View.VISIBLE);
            listviewRecall.setVisibility(View.GONE);
            return;
        } else {
            layoutTip.setVisibility(View.GONE);
            listviewRecall.setVisibility(View.VISIBLE);
        }
        if (recallDtoList.size() < 20) {
            listviewRecall.setListViewStateComplete();
        } else {
            listviewRecall.setListViewStateFinished();
        }
    }

    @Override
    public void showMoreRecallList(List<RecallDto> recallDtoList) {
        recallAdapter.add(recallDtoList);
        if (Common.isEmpty(recallDtoList)) {
            listviewRecall.setListViewStateComplete();
            return;
        }
        if (recallDtoList.size() < 20) {
            listviewRecall.setListViewStateComplete();
        } else {
            listviewRecall.setListViewStateFinished();
        }
    }

    @Override
    public void setRefreshingEnd() {
        waveSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setLoadMoreEnd() {
        listviewRecall.setListViewStateFinished();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowHeadEvent(ShowHeadEvent showHeadEvent) {
        this.headPicUrl = showHeadEvent.getHeadPicUrl();
        if (ivNavHead != null) {
            Common.displayDraweeView(this.headPicUrl, ivNavHead);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoodRecallEvent(GoodRecallEvent goodRecallEvent) {
        if (!(goodRecallEvent.getIndex() == Constant.RECALL_GOOD_HOME_INDEX && goodRecallEvent.getType() == Constant.RECALL_ADAPTER_HOME)) {
            return;
        }
        if (!isGoodStatusInited) {
            isGoodStatusInited = true;
            goodStatus = getBindPresenter().getGoodStatusByRecallNo(recallAdapter.getItem(goodRecallEvent.getPosition()).getRecallNo());
            goodRecallNo = recallAdapter.getItem(goodRecallEvent.getPosition()).getRecallNo();
        }

        getBindPresenter().changeGoodStatusToLocal(goodRecallNo, goodRecallEvent.getPosition());
        recallAdapter.notifyDataSetChanged();
        handler.removeCallbacks(customRunnable);
        handler.postDelayed(customRunnable, 1500);
    }

    // 通讯录更新之后，需要重新刷新recall的右侧标志
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshRecallEvent(RefreshRecallEvent refreshRecallEvent) {
        if (recallAdapter != null) {
            recallAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPublishRecallEvent(PublishRecallEvent publishRecallEvent){
        if (recallAdapter != null) {
            recallAdapter.addFirst(publishRecallEvent.getRecallDto());
            recallAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        RecallPublisher.getInstance(context).unregister(this);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
