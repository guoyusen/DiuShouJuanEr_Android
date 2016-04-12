package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.entity.ProgressVo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;
import java.util.Locale;

/**
 * Created by BiLi on 2016/3/25.
 */
public class ProgresAdapter extends CommonAdapter<ProgressVo> {

    public ProgresAdapter(Context context, List<ProgressVo> progressVoList){
        super(context, progressVoList, R.layout.item_upload_progress);
    }

    @Override
    public void convert(ViewHolder holder, ProgressVo progressVo, int position) throws Exception {
        if(progressVo != null){
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + progressVo.getPath()))
                    .setResizeOptions(new ResizeOptions(200, 200))
                    .build();
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setOldController(((SimpleDraweeView)holder.getView(R.id.itemImg)).getController());
            controller.setImageRequest(request);
            controller.build();
            ((SimpleDraweeView)holder.getView(R.id.itemImg)).setController(controller.build());

            if(progressVo.getProgress() >= 1.0){
                ((TextView)holder.getView(R.id.txtProgress)).setText("完成");
            }else if(progressVo.getProgress() <= 0){
                ((TextView)holder.getView(R.id.txtProgress)).setText("等待");
            }else{
                ((TextView)holder.getView(R.id.txtProgress)).setText(String.format(Locale.CHINA,"%d%%",(int)(progressVo.getProgress() * 100)));
            }
        }
    }

}
