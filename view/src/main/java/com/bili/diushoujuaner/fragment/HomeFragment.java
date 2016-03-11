package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.RecallDetailActivity;
import com.bili.diushoujuaner.adapter.RecallAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.callback.IShowMainMenu;
import com.bili.diushoujuaner.utils.response.RecallVo;
import com.bili.diushoujuaner.presenter.presenter.HomeFragmentPresenter;
import com.bili.diushoujuaner.presenter.viewinterface.HomeFragmentView;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;
import com.bili.diushoujuaner.widget.waveswipe.WaveSwipeRefreshLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class HomeFragment extends BaseFragment implements WaveSwipeRefreshLayout.OnRefreshListener, HomeFragmentView, View.OnClickListener {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.waveSwipeRefreshLayout)
    WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;

    private List<RecallVo> recallVoList;
    private RecallAdapter recallAdapter;
    private IShowMainMenu showMainMenu;

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
        recallVoList = new ArrayList<>();
    }

    @Override
    public void setViewStatus() {
        showPageHead("首页", R.mipmap.icon_recall_add, null);
        ivNavHead.setOnClickListener(this);

        recallAdapter = new RecallAdapter(getContext(), recallVoList);
        customListViewRefresh.setCanLoadMore(true);
        customListViewRefresh.setAdapter(recallAdapter);
        customListViewRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), RecallDetailActivity.class));
            }
        });

        waveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        waveSwipeRefreshLayout.setWaveColor(Color.parseColor("#5C84DC"));
        waveSwipeRefreshLayout.setOnRefreshListener(this);

        basePresenter = new HomeFragmentPresenter(this, getActivity().getApplication().getApplicationContext());
        getPresenterByClass(HomeFragmentPresenter.class).loadRecallList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivNavHead:
                showMainMenu.showMainMenu();
                break;
        }
    }

    public void setShowMainMenu(IShowMainMenu showMainMenu) {
        this.showMainMenu = showMainMenu;
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

    @Override
    public void showRecallList(List<RecallVo> recallVoList) {
        recallAdapter.refresh(recallVoList);
    }

    @Override
    public void showMoreRecallList(List<RecallVo> recallVoList) {
        recallAdapter.add(recallVoList);
    }

    @Override
    public void showLoading(int loadingType) {

    }

    @Override
    public void hideLoading(int loadingType) {

    }

    @Override
    public void showWarning(String message) {

    }
}
