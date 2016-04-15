package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.RecallAddActivityPresenter;
import com.bili.diushoujuaner.presenter.publisher.RecallPublisher;
import com.bili.diushoujuaner.presenter.view.IRecallAddView;
import com.bili.diushoujuaner.utils.entity.vo.ImageItemVo;

import java.util.ArrayList;

/**
 * Created by BiLi on 2016/4/12.
 */
public class RecallAddActivityPresenterImpl extends BasePresenter<IRecallAddView> implements RecallAddActivityPresenter {

    public RecallAddActivityPresenterImpl(IRecallAddView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void publishRecall(ArrayList<ImageItemVo> imageItemVoList, String content) {
        getBindView().finishView();
        RecallPublisher.getInstance(context).publishRecall(imageItemVoList, content);
    }
}
