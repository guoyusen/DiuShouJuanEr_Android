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
import com.bili.diushoujuaner.callback.IMainFragmentOperateListener;
import com.bili.diushoujuaner.callback.IMainOperateListener;
import com.bili.diushoujuaner.presenter.presenter.ContactFragmentPresenter;
import com.bili.diushoujuaner.presenter.view.IContactView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.widget.CustomListView;
import com.bili.diushoujuaner.widget.ReboundScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ContactFragment extends BaseFragment<ContactFragmentPresenter> implements View.OnClickListener, IContactView, IMainFragmentOperateListener {

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
    private IMainOperateListener iMainOperateListener;
    private String ivNavHeadUrl;

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
    }

    @Override
    public void setViewStatus() {
        showPageHead("联系人", null, "添加");
        ivNavHead.setOnClickListener(this);

        contactAdapter = new ContactAdapter(getContext(), friendVoList);
        customListView.setAdapter(contactAdapter);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ContactDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.INTENT_CONTACT_DETAIL,contactAdapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        layoutParty.setOnClickListener(this);
        txtRight.setOnClickListener(this);

        Common.displayDraweeView(ivNavHeadUrl, ivNavHead);

        basePresenter = new ContactFragmentPresenter(this, getContext());
        getRelativePresenter().getContactList();
    }

    public void setMainOperateListener(IMainOperateListener iMainOperateListener) {
        this.iMainOperateListener = iMainOperateListener;
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
                iMainOperateListener.showMainMenu();
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
    }

    @Override
    public void showHead(String url) {
        ivNavHeadUrl = url;
        if(ivNavHead != null){
            Common.displayDraweeView(ivNavHeadUrl, ivNavHead);
        }
    }
}
