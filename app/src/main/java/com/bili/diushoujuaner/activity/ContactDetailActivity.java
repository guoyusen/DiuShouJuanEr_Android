package com.bili.diushoujuaner.activity;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ContactRecentGalleryAdapter;
import com.bili.diushoujuaner.adapter.RecallDetailGalleryAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.model.response.PictureDto;
import com.bili.diushoujuaner.ui.CustomGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/3/9.
 */
public class ContactDetailActivity extends BaseActivity{

    @Bind(R.id.customGridView)
    CustomGridView customGridView;

    private ContactRecentGalleryAdapter contactRecentGalleryAdapter;
    private List<PictureDto> pictureList;

    @Override
    public void tintStatusColor() {
        super.tintStatusColor();
        tintManager.setStatusBarTintResource(R.color.TRANSPARENT);
    }

    @Override
    public void beforeInitView() {
        pictureList = new ArrayList<>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_detail);
    }

    @Override
    public void setViewStatus() {
        showPageHead(null, null, "更多");

        for(int i=0; i<3;i++){
            pictureList.add(new PictureDto());
        }
        contactRecentGalleryAdapter = new ContactRecentGalleryAdapter(this, pictureList);
        customGridView.setAdapter(contactRecentGalleryAdapter);
    }
}
