package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.PartyAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.eventhelper.StartChattingEvent;
import com.bili.diushoujuaner.presenter.presenter.PartyActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.PartyActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IPartyView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.BottomMoreListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class PartyActivity extends BaseActivity<PartyActivityPresenter> implements IPartyView {


    @Bind(R.id.customListViewRefresh)
    BottomMoreListView bottomMoreListView;
    @Bind(R.id.txtPartyCount)
    TextView txtPartyCount;

    private List<PartyVo> partyVoList;
    private PartyAdapter partyAdapter;

    @Override
    public void beforeInitView() {
        partyVoList = new ArrayList<>();
        basePresenter = new PartyActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_party);
    }

    @Override
    public void setViewStatus() {
        showPageHead("群组",null,null);

        partyAdapter = new PartyAdapter(this, partyVoList);
        bottomMoreListView.setAdapter(partyAdapter);
        bottomMoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBindPresenter().setCurrentChatting(partyAdapter.getItem(position).getPartyNo(), Constant.CHAT_PAR);
                EventBus.getDefault().post(new StartChattingEvent());
                startActivity(new Intent(PartyActivity.this, MessageActivity.class));
                finish();
            }
        });

        getBindPresenter().getPartyList();
    }

    @Override
    public void showPartyList(List<PartyVo> partyVoList) {
        partyAdapter.refresh(partyVoList);
        if(Common.isEmpty(partyVoList)){
            txtPartyCount.setText("暂无群组");
            return;
        } else{
            txtPartyCount.setText(partyVoList.size() + "个群组");
        }
    }
}
