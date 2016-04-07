package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.ContentEditActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContentEditActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContentEditView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.widget.CustomEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ContentEditActivity extends BaseActivity<ContentEditActivityPresenter> implements IContentEditView, View.OnClickListener {

    @Bind(R.id.textLeftCount)
    TextView textLeftCount;
    @Bind(R.id.edtEditor)
    CustomEditText edtEditor;
    @Bind(R.id.btnRight)
    ImageButton btnRight;

    public static final String TAG = "ContentEditActivity";
    private int type;

    class CustomTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            switch (type) {
                case Constant.EDIT_CONTENT_AUTOGRAPH:
                    textLeftCount.setText((Constant.EDIT_CONTENT_LENGTH_AUTOGRAPH - s.toString().length()) + "");
                    break;
                case Constant.EDIT_CONTENT_FEEDBACK:
                    textLeftCount.setText((Constant.EDIT_CONTENT_LENGTH_FEEDBACK - s.toString().length()) + "");
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    @Override
    public void initIntentParam(Intent intent) {
        type = intent.getIntExtra(TAG, Constant.EDIT_CONTENT_NONE);
    }

    @Override
    public void beforeInitView() {
        basePresenter = new ContentEditActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_content_edit);
    }

    @Override
    public void setViewStatus() {
        btnRight.setOnClickListener(this);
        edtEditor.addTextChangedListener(new CustomTextWatcher());
        setContentType();
    }

    private void setContentType() {
        switch (type) {
            case Constant.EDIT_CONTENT_AUTOGRAPH:
                showPageHead("个性签名", R.mipmap.icon_finish, null);
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_AUTOGRAPH + "");
                edtEditor.setHint("输入新的签名吧...");
                edtEditor.setMaxLength(Constant.EDIT_CONTENT_LENGTH_AUTOGRAPH);
                getBindPresenter().getOldAutograph();
                break;
            case Constant.EDIT_CONTENT_FEEDBACK:
                showPageHead("意见反馈", R.mipmap.icon_finish, null);
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_FEEDBACK + "");
                edtEditor.setHint("尽情吐槽吧...");
                edtEditor.setMaxLength(Constant.EDIT_CONTENT_LENGTH_FEEDBACK);
                break;
            default:
                showPageHead(null, null, "");
                textLeftCount.setText("");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRight:
                executeClickForFinish();
                break;
        }
    }

    private void executeClickForFinish() {
        switch (type) {
            case Constant.EDIT_CONTENT_AUTOGRAPH:
                getBindPresenter().publishNewAutograph(edtEditor.getText().toString());
                break;
            case Constant.EDIT_CONTENT_FEEDBACK:
                getBindPresenter().publishNewFeedBack(edtEditor.getText().toString());
                break;
        }
    }

    @Override
    public void showHint(String content) {
        switch (type) {
            case Constant.EDIT_CONTENT_AUTOGRAPH:
                edtEditor.setHint(content);
                break;
        }
    }

    @Override
    public void finishView() {
        finish();
    }
}
