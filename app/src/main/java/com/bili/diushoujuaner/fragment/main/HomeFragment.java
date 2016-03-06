package com.bili.diushoujuaner.fragment.main;

import android.os.Bundle;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.RecallAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.model.response.RecallVo;
import com.bili.diushoujuaner.ui.CustomListViewRefresh;
import com.bili.diushoujuaner.ui.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.customCircleRefreshLayout)
    CircleRefreshLayout customCircleRefreshLayout;

    private List<RecallVo> listRecall;
    private RecallAdapter recallAdapter;

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
        listRecall = new ArrayList<>();
    }

    @Override
    public void setViewStatus() {
        customListViewRefresh.setCanLoadMore(true);
        for(int i=0;i<10;i++){
            listRecall.add(new RecallVo());
        }
        recallAdapter = new RecallAdapter(getContext(), listRecall);
        customListViewRefresh.setAdapter(recallAdapter);
    }

}
