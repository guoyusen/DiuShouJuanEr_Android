package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ImagePageAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.utils.entity.ImageItemVo;
import com.bili.diushoujuaner.widget.imagepicker.view.SuperCheckBox;
import com.bili.diushoujuaner.widget.imagepicker.view.ViewPagerFixed;

import java.util.ArrayList;

import butterknife.Bind;

public class ImagePreviewActivity extends BaseActivity implements ImagePicker.OnImageSelectedListener, View.OnClickListener {

    public static final String ISORIGIN = "isOrigin";
    @Bind(R.id.viewpager)
    ViewPagerFixed viewpager;
    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.cb_check)
    SuperCheckBox cbCheck;
    @Bind(R.id.bottom_bar)
    RelativeLayout bottomBar;
    @Bind(R.id.content)
    RelativeLayout content;
    @Bind(R.id.txtNavTitle)
    TextView txtNavTitle;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;

    private boolean isOrigin;    //是否选中原图
    private ImagePicker imagePicker;
    private ArrayList<ImageItemVo> mImageItemVos;      //跳转进ImagePreviewFragment的图片文件夹
    private int mCurrentPosition = 0;         //跳转进ImagePreviewFragment时的序号，第几个图片
    private ArrayList<ImageItemVo> selectedImages;   //所有已经选中的图片
    private ImagePageAdapter adapter;

    @Override
    public void initIntentParam(Intent intent) {
        isOrigin = getIntent().getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
        mCurrentPosition = getIntent().getIntExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        mImageItemVos =  getIntent().getBundleExtra(ImagePicker.EXTRA_IMAGES_BUNDLE).getParcelableArrayList(ImagePicker.EXTRA_IMAGE_ITEMS);
    }

    @Override
    public void beforeInitView() {
        imagePicker = ImagePicker.getInstance();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_image_preview);
    }

    @Override
    public void resetStatus() {
        RelativeLayout.LayoutParams lpHead = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);
    }

    @Override
    public void setViewStatus() {
        showPageHead("",null,"");
        setTintStatusColor(R.color.BG_CC22292C);
        imagePicker.addOnImageSelectedListener(this);
        selectedImages = imagePicker.getSelectedImages();

        txtRight.setOnClickListener(this);

        adapter = new ImagePageAdapter(this, mImageItemVos);
        adapter.setPhotoViewClickListener(new ImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(mCurrentPosition, false);

        onImageSelected(0, null, false);
        ImageItemVo item = mImageItemVos.get(mCurrentPosition);
        boolean isSelected = imagePicker.isSelect(item);

        txtNavTitle.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItemVos.size()));
        cbCheck.setChecked(isSelected);

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                ImageItemVo item = mImageItemVos.get(mCurrentPosition);
                boolean isSelected = imagePicker.isSelect(item);
                cbCheck.setChecked(isSelected);
                txtNavTitle.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItemVos.size()));
            }
        });
        //当点击当前选中按钮的时候，需要根据当前的选中状态添加和移除图片
        cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageItemVo imageItemVo = mImageItemVos.get(mCurrentPosition);
                int selectLimit = imagePicker.getSelectLimit();
                if (cbCheck.isChecked() && selectedImages.size() >= selectLimit) {
                    Toast.makeText(ImagePreviewActivity.this, ImagePreviewActivity.this.getString(R.string.select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                    cbCheck.setChecked(false);
                } else {
                    imagePicker.addSelectedImageItem(mCurrentPosition, imageItemVo, cbCheck.isChecked());
                }
            }
        });
    }

    /**
     * 图片添加成功后，修改当前图片的选中数量
     * 当调用 addSelectedImageItem 或 deleteSelectedImageItem 都会触发当前回调
     */
    @Override
    public void onImageSelected(int position, ImageItemVo item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            txtRight.setText(getString(R.string.select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
            txtRight.setEnabled(true);
        } else {
            txtRight.setText(getString(R.string.complete));
            txtRight.setEnabled(false);
        }

    }

    @Override
    public void back(View view) {
        Intent intent = new Intent();
        intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtRight:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                intent.putExtra(ImagePicker.EXTRA_IMAGES_BUNDLE, bundle);
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onPageDestroy() {
        super.onPageDestroy();
        imagePicker.removeOnImageSelectedListener(this);
    }

    /**
     * 单击时，隐藏头和尾
     */
    public void onImageSingleTap() {
        if (layoutHead.getVisibility() == View.VISIBLE) {
            layoutHead.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
            layoutHead.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
            setTintStatusColor(R.color.transparenta);
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
            if (Build.VERSION.SDK_INT >= 16)
                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            layoutHead.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            layoutHead.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            setTintStatusColor(R.color.BG_CC22292C);
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
            if (Build.VERSION.SDK_INT >= 16)
                content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

}
