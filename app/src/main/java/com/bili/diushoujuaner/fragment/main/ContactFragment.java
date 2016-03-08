package com.bili.diushoujuaner.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ContactAdapter;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.model.response.ContactDto;
import com.bili.diushoujuaner.ui.CustomListViewRefresh;
import com.bili.diushoujuaner.ui.SideBarStar;
import com.bili.diushoujuaner.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ContactFragment extends BaseFragment {

    @Bind(R.id.customListViewRefresh)
    CustomListViewRefresh customListViewRefresh;
    @Bind(R.id.sideBar)
    SideBarStar sideBar;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private ContactAdapter contactAdapter;
    private List<ContactDto> listContact;

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
        customListViewRefresh.setCanLoadMore(false);
        for (int i = 0; i < 10; i++) {
            listContact.add(new ContactDto());
        }
        contactAdapter = new ContactAdapter(getContext(), listContact);
        customListViewRefresh.setAdapter(contactAdapter);
//        ViewUtil.getListViewHeightBasedOnChildren(customListViewRefresh);
    }

}
