package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.PartyAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.eventhelper.ContactUpdatedEvent;
import com.bili.diushoujuaner.model.eventhelper.StartChattingEvent;
import com.bili.diushoujuaner.presenter.presenter.PartyActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.PartyActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IPartyView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.PartyVo;
import com.bili.diushoujuaner.widget.BottomMoreListView;
import com.bili.diushoujuaner.widget.dialog.DialogTool;
import com.bili.diushoujuaner.widget.dialog.OnPartyClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @Bind(R.id.btnRight)
    ImageButton btnRight;

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
        showPageHead("群组", R.mipmap.icon_menu, null);

        partyAdapter = new PartyAdapter(this, partyVoList);
        bottomMoreListView.setAdapter(partyAdapter);
        bottomMoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBindPresenter().setCurrentChatting(partyAdapter.getItem(position).getPartyNo(), ConstantUtil.CHAT_PAR);
                EventBus.getDefault().post(new StartChattingEvent());
                startActivity(new Intent(PartyActivity.this, MessageActivity.class));
                finish();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTool.createPartyOperateDialog(context, new OnPartyClickListener() {
                    @Override
                    public void onSearchPartyClick() {
                        startActivity(new Intent(context, ContactSearchActivity.class));
                    }

                    @Override
                    public void onBuildPartyClick() {
                        startActivity(new Intent(context, BuildPartyActivity.class));
                    }
                });
            }
        });

        getBindPresenter().getPartyList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContactUpdatedEvent(ContactUpdatedEvent contactUpdatedEvent) {
        getBindPresenter().getPartyList();
    }

    @Override
    public void showPartyList(List<PartyVo> partyVoList) {
        partyAdapter.refresh(partyVoList);
        if(CommonUtil.isEmpty(partyVoList)){
            txtPartyCount.setText("暂无群组");
            return;
        } else{
            txtPartyCount.setText(partyVoList.size() + "个群组");
        }
    }
}
