package com.bili.diushoujuaner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.ContactDetailActivity;
import com.bili.diushoujuaner.activity.ContactSearchActivity;
import com.bili.diushoujuaner.activity.PartyActivity;
import com.bili.diushoujuaner.adapter.ContactAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.callback.IShowMainMenu;
import com.bili.diushoujuaner.model.entities.ContactDto;
import com.bili.diushoujuaner.widget.CustomListViewRefresh;
import com.bili.diushoujuaner.widget.ReboundScrollView;
import com.bili.diushoujuaner.widget.SideBarStar;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.sideBar)
    SideBarStar sideBar;
    @Bind(R.id.reboundScrollView)
    ReboundScrollView reboundScrollView;
    @Bind(R.id.layoutNewContact)
    LinearLayout layoutNewContact;
    @Bind(R.id.layoutParty)
    LinearLayout layoutParty;
    @Bind(R.id.textRight)
    TextView textRight;
    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;

    private ContactAdapter contactAdapter;
    private List<ContactDto> listContact;
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
        listContact = new ArrayList<>();
    }

    @Override
    public void setViewStatus() {

        showPageHead("联系人", null, "添加");
        ivNavHead.setOnClickListener(this);

        for (int i = 0; i < 10; i++) {
            listContact.add(new ContactDto());
        }
        contactAdapter = new ContactAdapter(getContext(), listContact);
        customListViewRefresh.setAdapter(contactAdapter);
        customListViewRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), ContactDetailActivity.class));
            }
        });

        layoutParty.setOnClickListener(this);
        textRight.setOnClickListener(this);
    }

    public void setShowMainMenu(IShowMainMenu showMainMenu) {
        this.showMainMenu = showMainMenu;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutParty:
                startActivity(new Intent(getContext(), PartyActivity.class));
                break;
            case R.id.textRight:
                startActivity(new Intent(getContext(), ContactSearchActivity.class));
                break;
            case R.id.ivNavHead:
                showMainMenu.showMainMenu();
                break;
        }
    }
}
