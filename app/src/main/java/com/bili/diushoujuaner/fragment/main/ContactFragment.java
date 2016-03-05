package com.bili.diushoujuaner.fragment.main;

import android.os.Bundle;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragment;

/**
 * Created by BiLi on 2016/3/2.
 */
public class ContactFragment extends BaseFragment{

    public static ContactFragment instantiation(int position){
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

    }

    @Override
    public void setViewStatus() {

    }
}
