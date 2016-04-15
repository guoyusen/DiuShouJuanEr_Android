package com.bili.diushoujuaner.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.AcountVerifyFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.AcountVerifyFragmentPresenterImpl;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.model.eventhelper.NextPageEvent;
import com.bili.diushoujuaner.model.eventhelper.StartTimerEvent;
import com.bili.diushoujuaner.widget.CustomEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class AcountVerifyFragment extends BaseFragment<AcountVerifyFragmentPresenter> implements IBaseView {

    @Bind(R.id.edtVerify)
    CustomEditText edtVerify;
    @Bind(R.id.txtTimer)
    TextView txtTimer;
    @Bind(R.id.edtPsd)
    CustomEditText edtPsd;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;

    private String mobile;
    private int type, second = 60;
    private Handler handler;
    private CustomRunnable customRunnable;

    public void setType(int type) {
        this.type = type;
    }

    class CustomRunnable implements Runnable{
        @Override
        public void run() {
            updateTimer();
        }
    }

    class CustomTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if(edtPsd.getText().toString().length() >= 8 && edtVerify.getText().toString().trim().length() > 0){
                btnSubmit.setEnabled(true);
                btnSubmit.setBackground(ContextCompat.getDrawable(context, R.drawable.selector_btn_blue));
                btnSubmit.setTextColor(ContextCompat.getColor(context, R.color.COLOR_WHITE));
            }else{
                btnSubmit.setEnabled(false);
                btnSubmit.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_solid_gray));
                btnSubmit.setTextColor(ContextCompat.getColor(context, R.color.TC_ADADBB));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    public static AcountVerifyFragment instantiation(int type) {
        AcountVerifyFragment fragment = new AcountVerifyFragment();
        EventBus.getDefault().register(fragment);
        fragment.setType(type);
        Bundle args = new Bundle();
        args.putInt("position", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_verify;
    }

    @Override
    public void beforeInitView() {
        basePresenter = new AcountVerifyFragmentPresenterImpl(this, context);
        handler = new Handler();
        customRunnable = new CustomRunnable();
    }

    @Override
    public void setViewStatus() {
        switch (this.type){
            case Constant.ACOUNT_UPDATE_REGIST:
                btnSubmit.setText("注 册");
                break;
            case Constant.ACOUNT_UPDATE_RESET:
                btnSubmit.setText("重 置");
                break;
        }
        edtPsd.addTextChangedListener(new CustomTextWatcher());
        edtVerify.addTextChangedListener(new CustomTextWatcher());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.ACOUNT_UPDATE_REGIST:
                        getBindPresenter().getAcountRegister(mobile, edtPsd.getText().toString(), edtVerify.getText().toString().trim());
                        break;
                    case Constant.ACOUNT_UPDATE_RESET:
                        getBindPresenter().getAcountReset(mobile, edtPsd.getText().toString(), edtVerify.getText().toString().trim());
                        break;
                }
            }
        });
        txtTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(second <= 0){
                    getBindPresenter().getVerifyCode(mobile, type);
                }
            }
        });
    }

    private void updateTimer(){
        if(second == 0){
            txtTimer.setText("重新发送");
            txtTimer.setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
        }else{
            txtTimer.setText("重新发送("+ second +"S)");
            second--;
            txtTimer.setTextColor(ContextCompat.getColor(context, R.color.TC_ADADBB));
            handler.postDelayed(customRunnable,1000);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextPageEvent(NextPageEvent nextPageEvent) {
        // 0:显示填写手机号
        // 1:显示填写其他信息并执行注册或重置==当前
        switch (nextPageEvent.getPageIndex()) {
            case 1:
                mobile = nextPageEvent.getMobile();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartTimerEvent(StartTimerEvent startTimerEvent){
        second = 60;
        updateTimer();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        handler.removeCallbacks(customRunnable);
        super.onDestroyView();
    }
}
