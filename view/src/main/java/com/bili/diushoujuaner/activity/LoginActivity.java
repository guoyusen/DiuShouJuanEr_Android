package com.bili.diushoujuaner.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.LoginActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.LoginActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.ILoginView;
import com.bili.diushoujuaner.utils.manager.ActivityManager;

import butterknife.Bind;

public class LoginActivity extends BaseActivity<LoginActivityPresenter> implements ILoginView, View.OnClickListener {

    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.txtForgetPsd)
    TextView txtForgetPsd;
    @Bind(R.id.edtMobile)
    EditText edtMobile;
    @Bind(R.id.edtPassword)
    EditText edtPassword;

    @Override
    public void beforeInitView() {
        basePresenter = new LoginActivityPresenterImpl(this, getApplicationContext());
        ActivityManager.getInstance().finishBefore();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void setViewStatus() {
        setTintStatusColor(R.color.TRANSPARENT);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                getBindPresenter().getUserLogin(edtMobile.getText().toString().trim(), edtPassword.getText().toString().trim());
                break;
        }
    }

}
