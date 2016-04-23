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
import com.bili.diushoujuaner.utils.Common;
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

    public static final String TAG_TYPE = "ContentEditActivity_Type";
    public static final String TAG_CONTENT = "ContentEditActivity_Content";
    private int type;
    private String content;

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
                case Constant.EDIT_CONTENT_MEMBER_NAME:
                    textLeftCount.setText((Constant.EDIT_CONTENT_LENGTH_MEMBER_NAME - s.toString().length()) + "");
                    break;
                case Constant.EDIT_CONTENT_PARTY_NAME:
                    textLeftCount.setText((Constant.EDIT_CONTENT_LENGTH_PARTY_NAME - s.toString().length()) + "");
                    break;
                case Constant.EDIT_CONTENT_PARTY_INTRODUCE:
                    textLeftCount.setText((Constant.EDIT_CONTENT_LENGTH_PARTY_INTRODUCE - s.toString().length()) + "");
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
        type = intent.getIntExtra(TAG_TYPE, Constant.EDIT_CONTENT_NONE);
        content = intent.getStringExtra(TAG_CONTENT);
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
                break;
            case Constant.EDIT_CONTENT_FEEDBACK:
                showPageHead("意见反馈", R.mipmap.icon_finish, null);
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_FEEDBACK + "");
                edtEditor.setHint("尽情吐槽吧...");
                edtEditor.setMaxLength(Constant.EDIT_CONTENT_LENGTH_FEEDBACK);
                break;
            case Constant.EDIT_CONTENT_MEMBER_NAME:
                showPageHead("群名片", R.mipmap.icon_finish, null);
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_MEMBER_NAME+ "");
                edtEditor.setHint("更改您的群名片吧...");
                edtEditor.setMaxLength(Constant.EDIT_CONTENT_LENGTH_MEMBER_NAME);
                break;
            case Constant.EDIT_CONTENT_PARTY_NAME:
                showPageHead("群名称", R.mipmap.icon_finish, null);
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_PARTY_NAME + "");
                edtEditor.setHint("更改您的群名称...");
                edtEditor.setMaxLength(Constant.EDIT_CONTENT_LENGTH_PARTY_NAME);
                break;
            case Constant.EDIT_CONTENT_PARTY_INTRODUCE:
                showPageHead("群介绍", R.mipmap.icon_finish, null);
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_PARTY_INTRODUCE + "");
                edtEditor.setHint("更改您的群介绍...");
                edtEditor.setMaxLength(Constant.EDIT_CONTENT_LENGTH_PARTY_INTRODUCE);
                break;
        }
        edtEditor.setText(content);
        edtEditor.setSelection(edtEditor.getText().length());
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
            case Constant.EDIT_CONTENT_MEMBER_NAME:
                getBindPresenter().publishNewMemberName(edtEditor.getText().toString());
                break;
            case Constant.EDIT_CONTENT_PARTY_NAME:
                getBindPresenter().publishNewPartyName(edtEditor.getText().toString());
                break;
            case Constant.EDIT_CONTENT_PARTY_INTRODUCE:
                getBindPresenter().publishNewPartyIntroduce(edtEditor.getText().toString());
                break;
        }
        Common.hideSoftInputFromWindow(context, edtEditor);
    }

    @Override
    public void finishView() {
        finish();
    }
}
