package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.model.RecallModel;
import com.bili.diushoujuaner.model.callback.IDataCallback;
import com.bili.diushoujuaner.model.entities.RecallVo;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.viewinterface.HomeFragmentView;

import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public class HomeFragmentPresenter extends BasePresenter {

    public HomeFragmentPresenter(HomeFragmentView homeFragmentView){
        attachView(homeFragmentView);
    }

    public void loadRecallList(){
        getViewByClass(HomeFragmentView.class).showLoading();
        RecallModel.getInstance().getRecallList(new IDataCallback<List<RecallVo>>() {
            @Override
            public void onSuccess(List<RecallVo> recallVoList) {
                getViewByClass(HomeFragmentView.class).showRecallList(recallVoList);
            }

            @Override
            public void onFail() {

            }
        });

    }

    public void loadMoreRecallList(){
        getViewByClass(HomeFragmentView.class).showLoading();
    }

}
