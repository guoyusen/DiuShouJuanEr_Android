package com.bili.diushoujuaner.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseFragment;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.AccountMobileFragmentPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.AccountMobileFragmentPresenterImpl;
import com.bili.diushoujuaner.utils.StringUtil;
import com.bili.diushoujuaner.widget.CustomEditText;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/2.
 */
public class AccountMobileFragment extends BaseFragment<AccountMobileFragmentPresenter> implements IBaseView {

    @Bind(R.id.edtMobile)
    CustomEditText edtMobile;
    @Bind(R.id.btnNext)
    Button btnNext;

    private int type;
    public void setType(int type) {
        this.type = type;
    }

    class CustomTextWatcher implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {
            if(StringUtil.checkMobile(s.toString().trim())){
                btnNext.setEnabled(true);
                btnNext.setBackground(ContextCompat.getDrawable(context, R.drawable.selector_btn_blue));
                btnNext.setTextColor(ContextCompat.getColor(context, R.color.COLOR_WHITE));
            }else{
                btnNext.setEnabled(false);
                btnNext.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_solid_gray));
                btnNext.setTextColor(ContextCompat.getColor(context, R.color.TC_ADADBB));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    public static AccountMobileFragment instantiation(int type) {
        AccountMobileFragment fragment = new AccountMobileFragment();
        fragment.setType(type);
        Bundle args = new Bundle();
        args.putInt("position", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_mobile;
    }

    @Override
    public void beforeInitView() {
        basePresenter = new AccountMobileFragmentPresenterImpl(this,context);
    }

    @Override
    public void setViewStatus() {
        edtMobile.addTextChangedListener(new CustomTextWatcher());
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBindPresenter().getVerifyCode(edtMobile.getText().toString(), type);
            }
        });
    }

}
