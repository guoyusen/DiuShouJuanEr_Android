package com.bili.diushoujuaner.base;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.application.CustomApplication;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.widget.CustomProgress;
import com.bili.diushoujuaner.widget.CustomToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/2/28.
 */
public class BaseFragment<T> extends Fragment {

    private TextView txtNavTitle;
    private TextView txtRight;
    private View defaultCircle;
    private ImageButton btnRight;

    protected Context context;
    protected BasePresenter basePresenter;
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
        initHeader(view);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            resetStatus();
        }
        try{
            EventBus.getDefault().register(this);
        }catch(EventBusException ebe){}
        setViewStatus();
        return view;
    }

    public void resetStatus(){}

    private void initHeader(View view){
        defaultCircle = view.findViewById(R.id.defaultCircle);
        txtNavTitle = (TextView)view.findViewById(R.id.txtNavTitle);
        txtRight = (TextView)view.findViewById(R.id.txtRight);
        btnRight = (ImageButton)view.findViewById(R.id.btnRight);
    }

    public void beforeInitView(){
    }

    public int getLayout() {
        return 0;
    }

    public void setViewStatus() {

    }

    public void showPageHead(String titleText, Integer iconId, String rightText) {
        if(defaultCircle == null || txtNavTitle == null || txtRight == null || btnRight == null){
            return;
        }
        if(titleText == null){
            txtNavTitle.setVisibility(View.INVISIBLE);
        }else{
            txtNavTitle.setVisibility(View.VISIBLE);
            txtNavTitle.setText(titleText);
        }

        if(iconId == null){
            btnRight.setVisibility(View.GONE);
        }else{
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setImageResource(iconId);
        }

        if(rightText == null){
            txtRight.setVisibility(View.INVISIBLE);
        }else{
            txtRight.setVisibility(View.VISIBLE);
            txtRight.setText(rightText);
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        if(basePresenter != null){
            basePresenter.detachView();
        }
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    protected T getBindPresenter(){
        return (T)basePresenter;
    }

    public void showLoading(int loadingType, String message) {
        switch (loadingType){
            case ConstantUtil.LOADING_CENTER:
                CustomProgress.getInstance(context).showCenter(message, true, null);
                break;
            case ConstantUtil.LOADING_TOP:
                CustomProgress.getInstance(context).showTop(message, true, null);
                break;
            case ConstantUtil.LOADING_DEFAULT:
                if(defaultCircle != null){
                    defaultCircle.setVisibility(View.VISIBLE);
                }
                if(txtNavTitle != null && TextUtils.isEmpty(txtNavTitle.getText())){
                    txtNavTitle.setVisibility(View.GONE);
                }
                break;
            default:break;
        }
    }

    public void hideLoading(int loadingType) {
        switch (loadingType){
            case ConstantUtil.LOADING_CENTER:
            case ConstantUtil.LOADING_TOP:
                CustomProgress.getInstance(context).dismiss();
                break;
            case ConstantUtil.LOADING_DEFAULT:
                if(defaultCircle != null){
                    defaultCircle.setVisibility(View.GONE);
                }
                break;
            default:break;
        }
    }

    public void showWarning(String message) {
        CustomToast.getInstance().showWarning(context, message);
    }

    public void showWarning(int warningType) {
        switch (warningType){
            case ConstantUtil.WARNING_401:
                showWarning(context.getString(R.string.warning_401));
                break;
            case ConstantUtil.WARNING_403:
                showWarning(context.getString(R.string.warning_403));
                break;
            case ConstantUtil.WARNING_503:
                showWarning(context.getString(R.string.warning_503));
                break;
            case ConstantUtil.WARNING_500:
                showWarning(context.getString(R.string.warning_500));
                break;
            case ConstantUtil.WARNING_NET:
                showWarning(context.getString(R.string.warning_net));
                break;
            case ConstantUtil.WARNING_PARSE:
                showWarning(context.getString(R.string.warning_parse));
                break;
            case ConstantUtil.WARNING_FILE:
                showWarning(context.getString(R.string.warning_file));
                break;
        }
    }
    public void finishView(){
    }

    public void exitActivity(){
    }
}
