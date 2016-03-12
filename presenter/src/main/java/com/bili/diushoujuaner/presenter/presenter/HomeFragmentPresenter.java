package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.response.RecallVo;
import com.bili.diushoujuaner.model.action.RecallAction;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.viewinterface.HomeFragmentView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public class HomeFragmentPresenter extends BasePresenter {

    public HomeFragmentPresenter(IBaseView baseView, Context context) {
        super(baseView, context);
    }

    public void loadRecallList(){
        getViewByClass(HomeFragmentView.class).showLoading(Constant.LOADING_TOP);
        RecallAction.getInstance().getRecallList(new ActionCallbackListener<List<RecallVo>>() {
            @Override
            public void onSuccess(List<RecallVo> recallVoList) {
                getViewByClass(HomeFragmentView.class).showRecallList(recallVoList);
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });

    }

    public void loadMoreRecallList(){
        getViewByClass(HomeFragmentView.class).showLoading(Constant.LOADING_NONE);
    }

}
