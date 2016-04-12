package com.bili.diushoujuaner.activity;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.RecallAddPicAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.presenter.RecallAddActivityPresenter;
import com.bili.diushoujuaner.presenter.presenter.impl.RecallAddActivityPresenterImpl;
import com.bili.diushoujuaner.presenter.view.IRecallAddView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.RecallAddPicVo;
import com.bili.diushoujuaner.widget.CustomEditText;
import com.bili.diushoujuaner.widget.CustomGridView;
import com.bili.diushoujuaner.widget.imagepicker.ImagePicker;
import com.bili.diushoujuaner.utils.entity.ImageItemVo;
import com.bili.diushoujuaner.widget.imagepicker.loader.GlideImageLoader;
import com.bili.diushoujuaner.widget.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class RecallAddActivity extends BaseActivity<RecallAddActivityPresenter> implements IRecallAddView, View.OnClickListener {

    @Bind(R.id.txtRight)
    TextView txtRight;
    @Bind(R.id.gridview)
    CustomGridView gridview;
    @Bind(R.id.edtEditor)
    CustomEditText edtEditor;

    private List<RecallAddPicVo> picVoList;
    private RecallAddPicAdapter recallAddPicAdapter;
    private ArrayList<ImageItemVo> images;

    private ImagePicker imagePicker;

    @Override
    public void beforeInitView() {
        picVoList = new ArrayList<>();
        imagePicker = ImagePicker.getInstance();
        images = new ArrayList<>();
        basePresenter = new RecallAddActivityPresenterImpl(this, context);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_recall_add);
    }

    @Override
    public void setViewStatus() {
        showPageHead("写趣事儿", null, "发表");

        txtRight.setOnClickListener(this);

        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(true);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setFocusHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constant.CORP_IMAGE_HEAD_EAGE, getResources().getDisplayMetrics()));
        imagePicker.setOutPutX(Constant.CORP_IMAGE_OUT_WIDTH);
        imagePicker.setOutPutY(Constant.CORP_IMAGE_OUT_HEIGHT);

        RecallAddPicVo recallAddPicVo = new RecallAddPicVo();
        recallAddPicVo.setResourceId(R.mipmap.icon_recall_add);
        recallAddPicVo.setType(Constant.RECALL_ADD_PIC_RES);
        picVoList.add(recallAddPicVo);

        recallAddPicAdapter = new RecallAddPicAdapter(this, picVoList);
        gridview.setAdapter(recallAddPicAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(picVoList.get(position).getType() == Constant.RECALL_ADD_PIC_RES){
                    ImagePicker.getInstance().setmSelectedImages(images);
                    Intent intent = new Intent(context, ImageGridActivity.class).putExtra(ImageGridActivity.TAG,"选择图片");
                    startActivityForResult(intent, 100);
                }else if(picVoList.get(position).getType() == Constant.RECALL_ADD_PIC_PATH){
                    recallAddPicAdapter.remove(position);
                    images.remove(position);
                    //小于9张，且最后一张不是add，添加add图标
                    if(recallAddPicAdapter.getCount() < 9 && recallAddPicAdapter.getItem(recallAddPicAdapter.getCount() - 1).getType() != Constant.RECALL_ADD_PIC_RES){
                        RecallAddPicVo recallAddPicVo = new RecallAddPicVo();
                        recallAddPicVo.setType(Constant.RECALL_ADD_PIC_RES);
                        recallAddPicVo.setResourceId(R.mipmap.icon_recall_add);
                        picVoList.add(recallAddPicVo);
                        recallAddPicAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null && requestCode == 100) {
            images.clear();
            picVoList.clear();
            ArrayList<ImageItemVo> tmpList = data.getBundleExtra(ImagePicker.EXTRA_IMAGES_BUNDLE).getParcelableArrayList(ImagePicker.EXTRA_RESULT_ITEMS);
            images.addAll(tmpList);
            for(ImageItemVo item : images){
                RecallAddPicVo recallAddPicVo = new RecallAddPicVo();
                recallAddPicVo.setType(Constant.RECALL_ADD_PIC_PATH);
                recallAddPicVo.setPath(item.path);
                picVoList.add(recallAddPicVo);
            }
            if(picVoList.size() < 9){
                RecallAddPicVo recallAddPicVo = new RecallAddPicVo();
                recallAddPicVo.setType(Constant.RECALL_ADD_PIC_RES);
                recallAddPicVo.setResourceId(R.mipmap.icon_recall_add);
                picVoList.add(recallAddPicVo);
            }
            recallAddPicAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtRight:
                getBindPresenter().publishRecall(images,edtEditor.getText().toString());
                break;
        }
    }

    @Override
    public void finishView() {
        finish();
    }
}
