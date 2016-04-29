package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.ContactAddActivity;
import com.bili.diushoujuaner.activity.ContactDetailActivity;
import com.bili.diushoujuaner.activity.ContactSearchActivity;
import com.bili.diushoujuaner.activity.PartyActivity;
import com.bili.diushoujuaner.adapter.ContactAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.model.eventhelper.RequestContactEvent;
import com.bili.diushoujuaner.model.eventhelper.DeleteContactEvent;
import com.bili.diushoujuaner.model.eventhelper.ShowMainMenuEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateContactEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateReadCountEvent;
import com.bili.diushoujuaner.model.eventhelper.UpdateRemarkEvent;
import com.bili.diushoujuaner.presenter.presenter.ContactFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContactFragmentPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContactView;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.badgeview.BGABadgeTextView;
import com.bili.diushoujuaner.widget.scrollview.ReboundScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @Bind(R.id.btnMenu)
    ImageButton btnMenu;
    @Bind(R.id.txtPartyCount)
    TextView txtPartyCount;
    @Bind(R.id.txtAddContact)
    BGABadgeTextView txtAddContact;

    private ContactAdapter contactAdapter;
    private List<FriendVo> friendVoList;

    public static ContactFragment instantiation(int position) {
        ContactFragment fragment = new ContactFragment();
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
        basePresenter = new ContactFragmentPresenterImpl(this, context);
    }

    @Override
    public void setViewStatus() {
        showPageHead("联系人", null, "添加");

        layoutParty.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        txtRight.setOnClickListener(this);
        layoutNewContact.setOnClickListener(this);

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

        getBindPresenter().getContactList();
        getBindPresenter().getAddUnReadCount();
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
            case R.id.btnMenu:
                EventBus.getDefault().post(new ShowMainMenuEvent());
                break;
            case R.id.layoutNewContact:
                getBindPresenter().updateApplyRead();
                startActivity(new Intent(getContext(), ContactAddActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateReadCountEvent(UpdateReadCountEvent updateReadCountEvent){
        switch (updateReadCountEvent.getType()){
            case ConstantUtil.UNREAD_COUNT_APPLY:
                if(updateReadCountEvent.getCount() <= 0){
                    txtAddContact.hiddenBadge();
                }else {
                    txtAddContact.showTextBadge(updateReadCountEvent.getCount() + "");
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteContactEvent(DeleteContactEvent deleteContactEvent){
        //删除联系人
        getBindPresenter().getContactList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateContactEvent(UpdateContactEvent updateContactEvent){
        //群，联系人更改，需要重新获取
        getBindPresenter().getContactList();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRequestContactEvent(RequestContactEvent requestContactEvent){
        // 有添加请求
        getBindPresenter().getAddUnReadCount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRemarkEvent(UpdateRemarkEvent updateRemarkEvent){
        //更新备注名
        getBindPresenter().getContactList();
    }

    @Override
    public void showContactList(List<FriendVo> friendVoList) {
        contactAdapter.refresh(friendVoList);
        if(CommonUtil.isEmpty(friendVoList)){
            txtPartyCount.setText("暂无联系人");
            return;
        } else{
            txtPartyCount.setText(friendVoList.size() + "个联系人");
        }
    }

}
