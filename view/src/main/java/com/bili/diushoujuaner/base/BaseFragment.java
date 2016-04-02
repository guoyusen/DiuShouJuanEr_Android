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
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.widget.CustomProgress;
import com.bili.diushoujuaner.widget.CustomToast;
import com.bili.diushoujuaner.widget.MaterialCircleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/2/28.
 */
public class BaseFragment<T> extends Fragment {

    @Bind(R.id.txtNavTitle)
    TextView txtNavTitle;
    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.btnRight)
    ImageButton btnRight;

    protected Context context;
    public CustomApplication customApplication;
    protected BasePresenter basePresenter;
    private View defaultCircle;

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
        initCircleLoder(view);
        setViewStatus();
        return view;
    }

    private void initCircleLoder(View view){
        defaultCircle = view.findViewById(R.id.defaultCircle);
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
        super.onDestroyView();
        ButterKnife.unbind(this);
        if(basePresenter != null){
            basePresenter.detachView();
        }
    }

    protected T getBindPresenter(){
        return (T)basePresenter;
    }

    public void showLoading(int loadingType, String message) {
        switch (loadingType){
            case Constant.LOADING_CENTER:
                CustomProgress.getInstance(context).showCenter(message, true, null);
                break;
            case Constant.LOADING_TOP:
                CustomProgress.getInstance(context).showTop(message, true, null);
                break;
            case Constant.LOADING_DEFAULT:
                if(defaultCircle != null){
                    defaultCircle.setVisibility(View.VISIBLE);
                }
                break;
            default:break;
        }
    }

    public void hideLoading(int loadingType) {
        switch (loadingType){
            case Constant.LOADING_CENTER:
            case Constant.LOADING_TOP:
                CustomProgress.getInstance(context).dismiss();
                break;
            case Constant.LOADING_DEFAULT:
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
            case Constant.WARNING_401:
                showWarning(context.getString(R.string.warning_401));
                break;
            case Constant.WARNING_403:
                showWarning(context.getString(R.string.warning_403));
                break;
            case Constant.WARNING_503:
                showWarning(context.getString(R.string.warning_503));
                break;
            case Constant.WARNING_500:
                showWarning(context.getString(R.string.warning_500));
                break;
            case Constant.WARNING_NET:
                showWarning(context.getString(R.string.warning_net));
                break;
            case Constant.WARNING_PARSE:
                showWarning(context.getString(R.string.warning_parse));
                break;
        }
    }
}
