package com.bili.diushoujuaner.base;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bili.diushoujuaner.application.CustomApplication;

import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/2/28.
 */
public class BaseFragment extends Fragment {

    protected Context context;
    public CustomApplication customApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        customApplication = (CustomApplication) context.getApplicationContext();

        beforeInitView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        setViewStatus();
        return view;
    }

    public void beforeInitView(){
    }

    public int getLayout() {
        return 0;
    }

    public void setViewStatus() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
