package com.bili.diushoujuaner.activity;

import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.ProgresAdapter;
import com.bili.diushoujuaner.base.BaseActivity;
import com.bili.diushoujuaner.presenter.publisher.OnPublishListener;
import com.bili.diushoujuaner.presenter.publisher.RecallPublisher;
import com.bili.diushoujuaner.utils.entity.vo.ImageItemVo;
import com.bili.diushoujuaner.utils.entity.vo.ProgressVo;
import com.bili.diushoujuaner.widget.CustomGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by BiLi on 2016/4/3.
 */
public class ProgressActivity extends BaseActivity implements OnPublishListener {

    @Bind(R.id.listViewProgress)
    CustomGridView customGridView;
    @Bind(R.id.txtRight)
    TextView txtRight;

    private List<ProgressVo> progressVoList;
    private ProgresAdapter progresAdapter;

    @Override
    public void beforeInitView() {
        progressVoList = new ArrayList<>();
        ArrayList<ImageItemVo> imageItemVos = RecallPublisher.getInstance(context).getImageItemVoList();
        for (int i = 0, len = imageItemVos.size(); i < len; i++) {
            ProgressVo progressVo = new ProgressVo();
            progressVo.setPath(imageItemVos.get(i).path);
            if (i < RecallPublisher.getInstance(context).getCurrentPosition()) {
                progressVo.setProgress(1.0f);
            } else {
                progressVo.setProgress(0.0f);
            }
            progressVoList.add(progressVo);
        }

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_progress);
    }

    @Override
    public void setViewStatus() {
        if(RecallPublisher.getInstance(context).isErrorOccured()){
            showPageHead("上传进度", null, "重新发布");
        }else{
            showPageHead("上传进度", null, null);
        }

        progresAdapter = new ProgresAdapter(context, progressVoList);
        customGridView.setAdapter(progresAdapter);

        txtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPageHead("上传进度", null, null);
                RecallPublisher.getInstance(context).republishRecall();
            }
        });

        RecallPublisher.getInstance(context).register(this);
    }

    @Override
    public void onError() {
        showWarning("趣事发表失败");
        showPageHead("上传进度", null, "重新发布");
    }

    @Override
    public void onProgress(int position, float progress) {
        progressVoList.get(position).setProgress(progress);
        progresAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishPublish() {
        finish();
    }

    @Override
    public void onStartPublish() {

    }

    @Override
    public void onPageDestroy() {
        RecallPublisher.getInstance(context).unregister(this);
        super.onPageDestroy();
    }
}
