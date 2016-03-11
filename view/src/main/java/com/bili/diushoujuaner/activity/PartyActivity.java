package com.bili.diushoujuaner.activity;

import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.PartyAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.utils.response.PartyDto;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class PartyActivity extends BaseActivity {


    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.textPartyCount)
    TextView textPartyCount;

    private List<PartyDto> partyDtoList;
    private PartyAdapter partyAdapter;

    @Override
    public void beforeInitView() {
        partyDtoList = new ArrayList<>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_party);
    }

    @Override
    public void setViewStatus() {
        showPageHead("群组",null,null);
        for(int i=0;i<9;i++){
            partyDtoList.add(new PartyDto());
        }
        partyAdapter = new PartyAdapter(this, partyDtoList);
        customListViewRefresh.setAdapter(partyAdapter);
    }

}
