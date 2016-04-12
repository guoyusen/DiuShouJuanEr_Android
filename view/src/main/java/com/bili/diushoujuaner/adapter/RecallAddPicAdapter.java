package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.RecallAddPicVo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.FileInputStream;
import java.util.List;

/**
 * Created by BiLi on 2016/3/25.
 */
public class RecallAddPicAdapter extends CommonAdapter<RecallAddPicVo> {

    public RecallAddPicAdapter(Context context, List<RecallAddPicVo> pictureVoList){
        super(context, pictureVoList, R.layout.item_recall_add_pic);
    }

    @Override
    public void convert(ViewHolder holder, RecallAddPicVo pictureVo, int position) throws Exception {
        if(pictureVo != null){
            if(pictureVo.getType() == Constant.RECALL_ADD_PIC_PATH){
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + pictureVo.getPath()))
                        .setResizeOptions(new ResizeOptions(200, 200))
                        .build();
                PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
                controller.setOldController(((SimpleDraweeView)holder.getView(R.id.itemImg)).getController());
                controller.setImageRequest(request);
                controller.build();
                ((SimpleDraweeView)holder.getView(R.id.itemImg)).setController(controller.build());
            }else if(pictureVo.getType() == Constant.RECALL_ADD_PIC_RES){
                ((SimpleDraweeView)holder.getView(R.id.itemImg)).setImageURI(Uri.parse("res://com.bili.diushoujuaner/" + pictureVo.getResourceId()));
            }
        }
    }
}
