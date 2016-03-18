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
import com.bili.diushoujuaner.callback.IShowMainMenu;
import com.bili.diushoujuaner.presenter.presenter.ContactFragmentPresenter;
import com.bili.diushoujuaner.presenter.viewinterface.ContactFragmentView;
import com.bili.diushoujuaner.utils.entity.FriendVo;
import com.bili.diushoujuaner.utils.response.ContactDto;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;
import com.bili.diushoujuaner.widget.ReboundScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener, ContactFragmentView {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
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

    private ContactAdapter contactAdapter;
    private List<FriendVo> friendVoList;
    private IShowMainMenu showMainMenu;

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
        customListViewRefresh.setAdapter(contactAdapter);
        customListViewRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), ContactDetailActivity.class));
            }
        });

        layoutParty.setOnClickListener(this);
        txtRight.setOnClickListener(this);

        basePresenter = new ContactFragmentPresenter(this, getContext());
        getPresenterByClass(ContactFragmentPresenter.class).getContacts();
    }

    public void setShowMainMenu(IShowMainMenu showMainMenu) {
        this.showMainMenu = showMainMenu;
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
                showMainMenu.showMainMenu();
                break;
        }
    }

    @Override
    public void showContactList(List<FriendVo> friendVoList) {
        contactAdapter.refresh(friendVoList);
    }

    @Override
    public void showLoading(int loadingType) {
        super.showLoading(loadingType);
    }

    @Override
    public void hideLoading(int loadingType) {
        super.hideLoading(loadingType);
    }

    @Override
    public void showWarning(String message) {
        super.showWarning(message);
    }

}
