package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.presenter.presenter.BuildPartyActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.BuildPartyActivityPresenterImpl;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.ImageItemVo;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.widget.imagepicker.loader.GlideImageLoader;
import com.bili.diushoujuaner.widget.imagepicker.view.CropImageView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class BuildPartyActivity extends BaseActivity<BuildPartyActivityPresenter> implements IBaseView, View.OnClickListener {

    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.ivWallPaper)
    SimpleDraweeView ivWallPaper;
    @Bind(R.id.layoutPartyHead)
    RelativeLayout layoutPartyHead;
    @Bind(R.id.edtEditor)
    CustomEditText edtEditor;
    @Bind(R.id.txtCount)
    TextView txtCount;

    private ImagePicker imagePicker;
    private ImageItemVo imageItemVo;

    class CustomTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            txtCount.setText((s.toString().length()) + "/10");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    @Override
    public void beforeInitView() {
        imagePicker = ImagePicker.getInstance();
        basePresenter = new BuildPartyActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_build_party);
    }

    @Override
    public void setViewStatus() {
        showPageHead("创建群", null, "提交");

        edtEditor.setMaxLength(10);
        edtEditor.addTextChangedListener(new CustomTextWatcher());

        layoutPartyHead.setOnClickListener(this);
        txtRight.setOnClickListener(this);

        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setFocusHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ConstantUtil.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setOutPutX(ConstantUtil.CORP_IMAGE_OUT_WIDTH);
        imagePicker.setOutPutY(ConstantUtil.CORP_IMAGE_OUT_HEIGHT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layoutPartyHead:
                Intent intent = new Intent(this, ImageGridActivity.class).putExtra(ImageGridActivity.TAG,"更换头像");
                startActivityForResult(intent, 100);
                break;
            case R.id.txtRight:
                getBindPresenter().getPartyAdd(edtEditor.getText().toString().trim(), imageItemVo.path);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null && requestCode == 100) {
            ArrayList<ImageItemVo> images = data.getBundleExtra(ImagePicker.EXTRA_IMAGES_BUNDLE).getParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS);
            if(CommonUtil.isEmpty(images)){
                return;
            }
            imageItemVo = images.get(0);
            CommonUtil.displayDraweeViewFromLocal(images.get(0).path, ivWallPaper);
        }
    }
}
