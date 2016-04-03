package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.ContactDetailActivity;
import com.bili.diushoujuaner.activity.ContactSearchActivity;
import com.bili.diushoujuaner.activity.PartyActivity;
import com.bili.diushoujuaner.adapter.ContactAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.event.RefreshRecallEvent;
import com.bili.diushoujuaner.event.ShowHeadEvent;
import com.bili.diushoujuaner.event.ShowMainMenuEvent;
import com.bili.diushoujuaner.presenter.presenter.ContactFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContactFragmentPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContactView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.ReboundScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ContactFragment extends BaseFragment<ContactFragmentPresenter> implements View.OnClickListener, IContactView {

    @Bind(R.id.customListView)
    CustomListView customListView;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;
    @Bind(R.id.layoutNewContact)
    RelativeLayout layoutNewContact;
    @Bind(R.id.layoutParty)
    RelativeLayout layoutParty;
    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.txtPartyCount)
    TextView txtPartyCount;

    private ContactAdapter contactAdapter;
    private List<FriendVo> friendVoList;
    private String headPicUrl;

    public static ContactFragment instantiation(int position) {
        ContactFragment fragment = new ContactFragment();
        EventBus.getDefault().register(fragment);
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_contact;
    }

    @Override
    public void beforeInitView() {
        friendVoList = new ArrayList<>();
    }

    @Override
    public void setViewStatus() {
        showPageHead("联系人", null, "添加");

        layoutParty.setOnClickListener(this);
        ivNavHead.setOnClickListener(this);
        txtRight.setOnClickListener(this);

        Common.displayDraweeView(headPicUrl, ivNavHead);

        contactAdapter = new ContactAdapter(getContext(), friendVoList);
        customListView.setAdapter(contactAdapter);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ContactDetailActivity.class);
                intent.putExtra(ContactDetailActivity.TAG, contactAdapter.getItem(position).getFriendNo());
                startActivity(intent);
            }
        });

        basePresenter = new ContactFragmentPresenterImpl(this, getContext());
        getBindPresenter().getContactList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutParty:
                startActivity(new Intent(getContext(), PartyActivity.class));
                break;
            case R.id.txtRight:
                startActivity(new Intent(getContext(), ContactSearchActivity.class));
                break;
            case R.id.ivNavHead:
                EventBus.getDefault().post(new ShowMainMenuEvent());
                break;
        }
    }

    @Override
    public void showContactList(List<FriendVo> friendVoList) {
        contactAdapter.refresh(friendVoList);
        if(friendVoList.size() <= 0){
            txtPartyCount.setText("暂无联系人");
        } else{
            txtPartyCount.setText(friendVoList.size() + "个联系人");
        }
        EventBus.getDefault().post(new RefreshRecallEvent());
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
