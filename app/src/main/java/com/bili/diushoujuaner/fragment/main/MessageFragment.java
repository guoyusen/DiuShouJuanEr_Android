package com.bili.diushoujuaner.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.MessageAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.model.response.MessageDto;
import com.bili.diushoujuaner.ui.CustomListViewRefresh;
import com.bili.diushoujuaner.ui.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/2.
 */
public class MessageFragment extends BaseFragment {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.customCircleRefreshLayout)
    CircleRefreshLayout customCircleRefreshLayout;

    private List<MessageDto> listMessage;
    private MessageAdapter messageAdapter;

    public static MessageFragment instantiation(int position) {
        MessageFragment fragment = new MessageFragment();
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
        customListViewRefresh.setCanLoadMore(false);
        for(int i=0;i<10;i++){
            listMessage.add(new MessageDto());
        }
        messageAdapter = new MessageAdapter(getContext(), listMessage);
        customListViewRefresh.setAdapter(messageAdapter);
    }

}
