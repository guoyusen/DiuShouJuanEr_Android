package com.bili.diushoujuaner.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.LoginActivityPresenter;
import com.bili.diushoujuaner.presenter.viewinterface.LoginActivityView;
import com.bili.diushoujuaner.widget.CustomProgress;
import com.bili.diushoujuaner.widget.CustomToast;

import butterknife.Bind;

public class LoginActivity extends BaseActivity implements LoginActivityView, View.OnClickListener {

    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.txtForgetPsd)
    TextView txtForgetPsd;
    @Bind(R.id.edtMobile)
    EditText edtMobile;
    @Bind(R.id.edtPassword)
    EditText edtPassword;

    @Override
    public void tintStatusColor() {
        super.tintStatusColor();
        tintManager.setStatusBarTintResource(R.color.TRANSPARENT);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void setViewStatus() {
        showPageHead(null, null, "新用户");

        btnLogin.setOnClickListener(this);
        basePresenter = new LoginActivityPresenter(this, getApplicationContext());
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showWarning(String message) {
        super.showWarning(message);
    }

    @Override
    public void showLoading(int loadingType) {
        super.showLoading(loadingType);
    }

    @Override
    public void hideLoading(int loadingType) {
        super.hideLoading(loadingType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                getPresenterByClass(LoginActivityPresenter.class).getUserLogin(edtMobile.getText().toString().trim(), edtPassword.getText().toString().trim());
                break;
        }
    }

}
