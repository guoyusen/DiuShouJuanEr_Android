package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ImageFolderAdapter;
import com.bili.diushoujuaner.adapter.ImageGridAdapter;
import com.bili.diushoujuaner.base.BaseFragmentActivity;
import com.bili.diushoujuaner.widget.imagepicker.ImageDataSource;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.widget.imagepicker.Utils;
import com.bili.diushoujuaner.widget.imagepicker.bean.ImageFolder;
import com.bili.diushoujuaner.utils.entity.ImageItemVo;

import java.util.List;

import butterknife.Bind;

public class ImageGridActivity extends BaseFragmentActivity implements ImageDataSource.OnImagesLoadedListener, ImageGridAdapter.OnImageItemClickListener, ImagePicker.OnImageSelectedListener, View.OnClickListener {

    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.btn_dir)
    Button btnDir;
    @Bind(R.id.btn_preview)
    Button btnPreview;
    @Bind(R.id.footer_bar)
    RelativeLayout footerBar;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;

    public static final String TAG = "ImageGridActivity";

    private ImagePicker imagePicker;
    private boolean isOrigin = false;  //是否选中原图
    private int screenWidth;     //屏幕的宽
    private int screenHeight;    //屏幕的高
    private ImageFolderAdapter mImageFolderAdapter;    //图片文件夹的适配器
    private ListPopupWindow mFolderPopupWindow;  //ImageSet的PopupWindow
    private List<ImageFolder> mImageFolders;   //所有的图片文件夹
    private ImageGridAdapter mImageGridAdapter;  //图片九宫格展示的适配器
    private String title;

    @Override
    public void initIntentParam(Intent intent) {
        title = intent.getStringExtra(TAG);
    }

    @Override
    public void beforeInitView() {
        imagePicker = ImagePicker.getInstance();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_image_grid);
    }

    @Override
    public void resetStatus() {
        LinearLayout.LayoutParams lpHead = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);
    }

    @Override
    public void setViewStatus() {
        showPageHead(title,null,"完成");
        setTintStatusColor(R.color.BG_CC22292C);
        imagePicker.addOnImageSelectedListener(this);
        DisplayMetrics dm = Utils.getScreenPix(this);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        if (imagePicker.isMultiMode()) {
            btnPreview.setVisibility(View.VISIBLE);
            txtRight.setVisibility(View.VISIBLE);
        } else {
            btnPreview.setVisibility(View.GONE);
            txtRight.setVisibility(View.GONE);
        }

        btnDir.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
        txtRight.setOnClickListener(this);

        mImageGridAdapter = new ImageGridAdapter(this, imagePicker.getSelectedImages());
        mImageFolderAdapter = new ImageFolderAdapter(this, null);

        onImageSelected(0, null, false);

        new ImageDataSource(this, null, this);
    }

    @Override
    public void onPageDestroy() {
        super.onPageDestroy();
        imagePicker.removeOnImageSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtRight:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                intent.putExtra(ImagePicker.EXTRA_IMAGES_BUNDLE,bundle);
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
                finish();
                break;
            case R.id.btn_dir:
                //点击文件夹按钮
                if (mFolderPopupWindow == null) createPopupFolderList(screenWidth, screenHeight);
                backgroundAlpha(0.3f);   //改变View的背景透明度
                mImageFolderAdapter.refreshData(mImageFolders);  //刷新数据
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                    int index = mImageFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
                break;
            case R.id.btn_preview:
                intent = new Intent(ImageGridActivity.this, ImagePreviewActivity.class);
                intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
                bundle = new Bundle();
                bundle.putParcelableArrayList(ImagePicker.EXTRA_IMAGE_ITEMS,imagePicker.getSelectedImages());
                intent.putExtra(ImagePicker.EXTRA_IMAGES_BUNDLE, bundle);
                intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
                startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
                break;
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mImageFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);  //如果不设置，就是 AnchorView 的宽度
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(footerBar);  //ListPopupWindow总会相对于这个View
        mFolderPopupWindow.setModal(true);  //是否为模态，影响返回键的处理
        mFolderPopupWindow.setAnimationStyle(R.style.popupwindow_anim_style);
        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mImageFolderAdapter.setSelectIndex(position);
                imagePicker.setCurrentImageFolderPosition(position);
                mFolderPopupWindow.dismiss();
                ImageFolder imageFolder = (ImageFolder) adapterView.getAdapter().getItem(position);
                if (null != imageFolder) {
                    mImageGridAdapter.refreshData(imageFolder.images);
                    btnDir.setText(imageFolder.name);
                }
                gridview.smoothScrollToPosition(0);//滑动到顶部
            }
        });
    }

    /**
     * 设置屏幕透明度  0.0透明  1.0不透明
     */
    public void backgroundAlpha(float alpha) {
        gridview.setAlpha(alpha);
        layoutHead.setAlpha(alpha);
        footerBar.setAlpha(1.0f);
    }

    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
        imagePicker.setImageFolders(imageFolders);
        mImageGridAdapter.refreshData(imageFolders.get(0).images);
        mImageGridAdapter.setOnImageItemClickListener(this);
        gridview.setAdapter(mImageGridAdapter);
        mImageFolderAdapter.refreshData(imageFolders);
    }

    @Override
    public void onImageItemClick(View view, ImageItemVo imageItemVo, int position) {
        //根据是否有相机按钮确定位置
        position = imagePicker.isShowCamera() ? position - 1 : position;
        if (imagePicker.isMultiMode()) {
            Intent intent = new Intent(ImageGridActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ImagePicker.EXTRA_IMAGE_ITEMS,imagePicker.getCurrentImageFolderItems());
            intent.putExtra(ImagePicker.EXTRA_IMAGES_BUNDLE, bundle);
            intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面
        } else {
            imagePicker.clearSelectedImages();
            imagePicker.addSelectedImageItem(position, imagePicker.getCurrentImageFolderItems().get(position), true);
            if (imagePicker.isCrop()) {
                Intent intent = new Intent(ImageGridActivity.this, ImageCropActivity.class);
                startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
            } else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                intent.putExtra(ImagePicker.EXTRA_IMAGES_BUNDLE, bundle);
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                finish();
            }
        }
    }

    @Override
    public void onImageSelected(int position, ImageItemVo item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            txtRight.setText(getString(R.string.select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
            txtRight.setEnabled(true);
            btnPreview.setEnabled(true);
        } else {
            txtRight.setText(getString(R.string.complete));
            txtRight.setEnabled(false);
            btnPreview.setEnabled(false);
        }
        btnPreview.setText(getResources().getString(R.string.preview_count, imagePicker.getSelectImageCount()));
        mImageGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == ImagePicker.RESULT_CODE_BACK) {
                isOrigin = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
            } else {
                //说明是从裁剪页面过来的数据，直接返回就可以
                setResult(ImagePicker.RESULT_CODE_ITEMS, data);
                finish();
            }
        } else {
            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
                ImageItemVo imageItemVo = new ImageItemVo();
                imageItemVo.path = imagePicker.getTakeImageFile().getAbsolutePath();
                imagePicker.clearSelectedImages();
                imagePicker.addSelectedImageItem(0, imageItemVo, true);
                if (imagePicker.isCrop()) {
                    Intent intent = new Intent(ImageGridActivity.this, ImageCropActivity.class);
                    startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                    intent.putExtra(ImagePicker.EXTRA_IMAGES_BUNDLE, bundle);
                    setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                    finish();
                }
            }
        }
    }
}