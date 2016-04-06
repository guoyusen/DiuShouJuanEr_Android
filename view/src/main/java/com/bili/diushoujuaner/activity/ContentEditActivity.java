package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.ContentEditActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.ContentEditActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IContentEditView;
import com.bili.diushoujuaner.utils.Constant;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ContentEditActivity extends BaseActivity<ContentEditActivityPresenter> implements IContentEditView {

    @Bind(R.id.textLeftCount)
    TextView textLeftCount;
    @Bind(R.id.edtEditor)
    EditText edtEditor;

    public static final String TAG = "ContentEditActivity";
    private int type;

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
        setContentType();

    }

    private void setContentType(){
        switch (type){
            case Constant.EDIT_CONTENT_AUTOGRAPH:
                showPageHead("个性签名", null, "完成");
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_AUTOGRAPH + "");
                edtEditor.setHint("输入新的签名吧...");
                getBindPresenter().getOldAutograph();
                break;
            case Constant.EDIT_CONTENT_FEEDBACK:
                showPageHead("意见反馈", null, "完成");
                textLeftCount.setText(Constant.EDIT_CONTENT_LENGTH_FEEDBACK + "");
                edtEditor.setHint("尽情吐槽吧...");
                break;
            default:
                showPageHead(null, null, "");
                textLeftCount.setText("");
                break;
        }
    }

    @Override
    public void showHint(String content) {
        switch (type){
            case Constant.EDIT_CONTENT_AUTOGRAPH:
                edtEditor.setHint(content);
                break;
        }
    }
}
