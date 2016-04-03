package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.utils.Constant;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ContentEditActivity extends BaseActivity {

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
    public void initView() {
        setContentView(R.layout.activity_content_edit);
    }

    @Override
    public void setViewStatus() {
        switch (type){
            case Constant.EDIT_CONTENT_AUTOGRAPH:
                showPageHead("个性签名", null, "完成");
                textLeftCount.setText("50");
                edtEditor.setHint("输入新的签名吧...");
                break;
            case Constant.EDIT_CONTENT_FEEDBACK:
                showPageHead("意见反馈", null, "完成");
                textLeftCount.setText("200");
                edtEditor.setHint("尽情吐槽吧...");
                break;
            default:
                showPageHead("", null, "");
                textLeftCount.setText("");
                break;
        }
    }
}
