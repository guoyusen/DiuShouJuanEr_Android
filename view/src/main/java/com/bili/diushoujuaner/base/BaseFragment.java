package com.bili.diushoujuaner.base;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.application.CustomApplication;
import com.bili.diushoujuaner.presenter.base.BasePresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/2/28.
 */
public class BaseFragment extends Fragment {

    @Bind(R.id.textNavTitle)
    TextView textNavTitle;
    @Bind(R.id.textRight)
    TextView textRight;
    @Bind(R.id.btnRight)
    ImageButton btnRight;

    protected Context context;
    public CustomApplication customApplication;
    protected BasePresenter basePresenter;

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

    public void showPageHead(String titleText, Integer iconId, String rightText) {
        if(titleText == null){
            textNavTitle.setVisibility(View.INVISIBLE);
        }else{
            textNavTitle.setVisibility(View.VISIBLE);
            textNavTitle.setText(titleText);
        }

        if(iconId == null){
            btnRight.setVisibility(View.GONE);
        }else{
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setImageResource(iconId);
        }

        if(rightText == null){
            textRight.setVisibility(View.INVISIBLE);
        }else{
            textRight.setVisibility(View.VISIBLE);
            textRight.setText(rightText);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        basePresenter.detachView();
    }

    protected <T> T getPresenterByClass(Class<T> t){
        return (T)basePresenter;
    }
}
