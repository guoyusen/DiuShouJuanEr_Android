package com.bili.diushoujuaner.view.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bili.diushoujuaner.view.application.CustomApplication;

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
        getAndSetViews();
        return view;
    }

    public void beforeInitView(){
        //TODO 初始化Store
    }

    public int getLayout() {
        return 0;
    }

    public void getAndSetViews() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
