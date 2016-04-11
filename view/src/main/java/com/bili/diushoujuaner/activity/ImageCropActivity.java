package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.widget.imagepicker.bean.ImageItem;
import com.bili.diushoujuaner.widget.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;

public class ImageCropActivity extends BaseActivity implements View.OnClickListener, CropImageView.OnBitmapSaveCompleteListener {

    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.cv_crop_image)
    CropImageView cvCropImage;
    @Bind(R.id.layoutHead)
    RelativeLayout layoutHead;

    private Bitmap mBitmap;
    private boolean mIsSaveRectangle;
    private int mOutputX;
    private int mOutputY;
    private ArrayList<ImageItem> mImageItems;
    private ImagePicker imagePicker;

    @Override
    public void beforeInitView() {
        imagePicker = ImagePicker.getInstance();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_image_crop);
    }

    @Override
    public void resetStatus() {
        LinearLayout.LayoutParams lpHead = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y100));
        lpHead.setMargins(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
        layoutHead.setLayoutParams(lpHead);
    }

    @Override
    public void setViewStatus() {
        showPageHead("裁剪尺寸", null, getString(R.string.complete));
        setTintStatusColor(R.color.BG_CC22292C);
        txtRight.setOnClickListener(this);
        cvCropImage.setOnBitmapSaveCompleteListener(this);

        //获取需要的参数
        mOutputX = imagePicker.getOutPutX();
        mOutputY = imagePicker.getOutPutY();
        mIsSaveRectangle = imagePicker.isSaveRectangle();
        mImageItems = imagePicker.getSelectedImages();
        String imagePath = mImageItems.get(0).path;

        cvCropImage.setFocusStyle(imagePicker.getStyle());
        cvCropImage.setFocusWidth(imagePicker.getFocusWidth());
        cvCropImage.setFocusHeight(imagePicker.getFocusHeight());

        //缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, options);
        cvCropImage.setImageBitmap(mBitmap);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtRight:
                cvCropImage.saveBitmapToFile(imagePicker.getCropCacheFolder(this), mOutputX, mOutputY, mIsSaveRectangle);
                break;
        }
    }

    @Override
    public void back(View view) {
        setResult(RESULT_CANCELED);
        super.back(view);
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        //裁剪后替换掉返回数据的内容，但是不要改变全局中的选中数据
        mImageItems.remove(0);
        ImageItem imageItem = new ImageItem();
        imageItem.path = file.getAbsolutePath();
        mImageItems.add(imageItem);

        Intent intent = new Intent();
        intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, mImageItems);
        setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
        finish();
    }

    @Override
    public void onBitmapSaveError(File file) {

    }

    @Override
    public void onPageDestroy() {
        super.onPageDestroy();
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
