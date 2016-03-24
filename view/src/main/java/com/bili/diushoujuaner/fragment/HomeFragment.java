package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.RecallDetailActivity;
import com.bili.diushoujuaner.adapter.RecallAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.callback.IMainFragmentOperateListener;
import com.bili.diushoujuaner.callback.IMainOperateListener;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.Imageloader;
import com.bili.diushoujuaner.utils.ListViewHighter;
import com.bili.diushoujuaner.utils.response.RecallDto;
import com.bili.diushoujuaner.presenter.presenter.HomeFragmentPresenter;
import com.bili.diushoujuaner.presenter.view.IHomeView;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;
import com.bili.diushoujuaner.widget.waveswipe.WaveSwipeRefreshLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements WaveSwipeRefreshLayout.OnRefreshListener, IHomeView, View.OnClickListener, IMainFragmentOperateListener,CustomListViewRefresh.OnLoadMoreListener {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.waveSwipeRefreshLayout)
    WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.layoutTip)
    RelativeLayout layoutTip;

    private List<RecallDto> recallDtoList;
    private RecallAdapter recallAdapter;
    private IMainOperateListener iMainOperateListener;
    private String ivNavHeadUrl;

    public static HomeFragment instantiation(int position){
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public void setViewStatus() {
        showPageHead("首页", R.mipmap.icon_recall_add, null);
        ivNavHead.setOnClickListener(this);

        recallAdapter = new RecallAdapter(getContext(), recallDtoList);
        customListViewRefresh.setCanLoadMore(true);
        customListViewRefresh.setOnLoadMoreListener(this);
        customListViewRefresh.setAdapter(recallAdapter);
        customListViewRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), RecallDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.INTENT_RECALL_DETAIL, recallAdapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        waveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        waveSwipeRefreshLayout.setWaveColor(Color.parseColor("#5C84DC"));
        waveSwipeRefreshLayout.setOnRefreshListener(this);

        Imageloader.getInstance().displayDraweeView(ivNavHeadUrl, ivNavHead);

        basePresenter = new HomeFragmentPresenter(this, getContext());
        getRelativePresenter().showRecallFromCache();
        getRelativePresenter().getRecallList(Constant.REFRESH_DEFAULT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivNavHead:
                iMainOperateListener.showMainMenu();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recallAdapter.notifyDataSetChanged();
    }

    public void setMainOperateListener(IMainOperateListener iMainOperateListener) {
        this.iMainOperateListener = iMainOperateListener;
    }

    @Override
    public void onRefresh() {
        customListViewRefresh.setListViewStateRefresh();
        getRelativePresenter().getRecallList(Constant.REFRESH_INTENT);
    }

    @Override
    public void onLoadMore() {
        getRelativePresenter().getMoreRecallList();
    }

    @Override
    public void showRecallList(List<RecallDto> recallDtoList) {
        if(recallDtoList.size() <= 0){
            layoutTip.setVisibility(View.VISIBLE);
            customListViewRefresh.setVisibility(View.GONE);
        }else{
            layoutTip.setVisibility(View.GONE);
            customListViewRefresh.setVisibility(View.VISIBLE);
        }
        if(recallDtoList.size() < 20){
            customListViewRefresh.setListViewStateComplete();
        }else{
            customListViewRefresh.setListViewStateFinished();
        }
        recallAdapter.refresh(recallDtoList);
        ListViewHighter.setListViewHeightBasedOnChildren(customListViewRefresh);
    }

    @Override
    public void showMoreRecallList(List<RecallDto> recallDtoList) {
        if(recallDtoList.size() < 20){
            customListViewRefresh.setListViewStateComplete();
        }else{
            customListViewRefresh.setListViewStateFinished();
        }
        recallAdapter.add(recallDtoList);
        ListViewHighter.setListViewHeightBasedOnChildren(customListViewRefresh);
    }

    @Override
    public void setRefreshingEnd() {
        waveSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setLoadMoreEnd() {
        customListViewRefresh.setListViewStateFinished();
    }

    @Override
    public void showHead(String url) {
        ivNavHeadUrl = url;
        if(ivNavHead != null){
            Imageloader.getInstance().displayDraweeView(ivNavHeadUrl, ivNavHead);
        }
    }
}
