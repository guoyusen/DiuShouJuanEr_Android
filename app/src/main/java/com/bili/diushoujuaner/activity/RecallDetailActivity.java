package com.bili.diushoujuaner.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.RecallDetailGalleryAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.response.Picture;
import com.bili.diushoujuaner.ui.CustomGridView;
import com.bili.diushoujuaner.ui.aligntextview.CBAlignTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BiLi on 2016/3/8.
 */
public class RecallDetailActivity extends BaseActivity {

    @Bind(R.id.ivNavHead)
    SimpleDraweeView ivNavHead;
    @Bind(R.id.textAuthor)
    TextView textAuthor;
    @Bind(R.id.textRight)
    TextView textRight;
    @Bind(R.id.textRecallDetail)
    CBAlignTextView textRecallDetail;
    @Bind(R.id.textComment)
    EditText textComment;
    @Bind(R.id.customGridView)
    CustomGridView customGridView;

    private RecallDetailGalleryAdapter recallDetailGalleryAdapter;
    private List<Picture> pictureList;

    @Override
    public void initView() {
        setContentView(R.layout.activity_recall_detail);
    }

    @Override
    public void beforeInitView() {
        pictureList = new ArrayList<>();
    }

    @Override
    public void setViewStatus() {
        showPageHeadSpecial(null, null, new Date());
        textRecallDetail.setText(getString(R.string.tmpString));

        for(int i=0; i<9;i++){
            pictureList.add(new Picture());
        }
        recallDetailGalleryAdapter = new RecallDetailGalleryAdapter(this, pictureList);
        customGridView.setAdapter(recallDetailGalleryAdapter);
    }

}
