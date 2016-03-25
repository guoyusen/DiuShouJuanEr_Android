package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.action.GoodAction;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.request.RecallListReq;
import com.bili.diushoujuaner.utils.response.GoodDto;
import com.bili.diushoujuaner.utils.response.RecallDto;
import com.bili.diushoujuaner.model.action.RecallAction;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.view.IHomeView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/3/10.
 */
public class HomeFragmentPresenter extends BasePresenter<IHomeView> {

    private RecallListReq recallListReq = new RecallListReq();

    public HomeFragmentPresenter(IHomeView baseView, Context context) {
        super(baseView, context);
    }

    public boolean executeGoodChange(boolean goodstatus, long recallNo){
        if(goodstatus == GoodTemper.getGoodStatus(recallNo)){
            return goodstatus;
        }
        if(GoodTemper.getGoodStatus(recallNo)){
            getGoodAdd(recallNo);
            return true;
        }else{
            getGoodRemove(recallNo);
            return false;
        }
    }

    public void getGoodAdd(long recallNo){
        GoodAction.getInstance().getGoodAdd(recallNo, new ActionCallbackListener<ApiRespon<String>>() {
            @Override
            public void onSuccess(ApiRespon<String> result) {
                showMessage(result.getRetCode(), result.getMessage());
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    public void getGoodRemove(long recallNo){
        GoodAction.getInstance().getGoodRemove(recallNo, new ActionCallbackListener<ApiRespon<String>>() {
            @Override
            public void onSuccess(ApiRespon<String> result) {
                showMessage(result.getRetCode(), result.getMessage());
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    public void addGoodDtoToTemper(long recallNo){
        GoodDto goodDto = new GoodDto();
        goodDto.setUserNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        goodDto.setNickName(DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo()).getRealName());
        RecallTemper.addGood(recallNo, goodDto);
    }

    public void removeGoodDtoFromTemper(long recallNo){
        RecallTemper.removeGood(recallNo);
    }

    public void getRecallList(final int refreshType){
        showLoadingByRefreshType(refreshType);
        initRecallListReq();

        RecallAction.getInstance().getRecallList(recallListReq, new ActionCallbackListener<ApiRespon<List<RecallDto>>>() {
            @Override
            public void onSuccess(ApiRespon<List<RecallDto>> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    updateRequestParam(result.getData());
                    getRelativeView().showRecallList(result.getData());

                    RecallTemper.clear();
                    GoodTemper.clear();
                    RecallTemper.addRecallDtoList(result.getData());
                }
                hideLoading();
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading();
            }
        });

    }

    private void showLoadingByRefreshType(int refreshType){
        if(refreshType == Constant.REFRESH_DEFAULT){
            showLoading(Constant.LOADING_DEFAULT, "");
        }
    }

    private void hideLoading(){
        hideLoading(Constant.LOADING_DEFAULT);
        getRelativeView().setRefreshingEnd();
    }

    public void showRecallFromCache(){
        List<RecallDto> recallDtoList = GsonParser.getInstance().fromJson(ACache.getInstance().getAsString(Constant.ACACHE_RECALL_LIST), new TypeToken<List<RecallDto>>() {
        }.getType());

        RecallTemper.clear();
        RecallTemper.addRecallDtoList(recallDtoList);

        getRelativeView().showRecallList(recallDtoList);
    }

    public void getMoreRecallList(){
        if(Common.checkNetworkStatus(context)) {
            RecallAction.getInstance().getRecallList(recallListReq, new ActionCallbackListener<ApiRespon<List<RecallDto>>>() {
                @Override
                public void onSuccess(ApiRespon<List<RecallDto>> result) {
                    if (showMessage(result.getRetCode(), result.getMessage())) {
                        updateRequestParam(result.getData());
                        getRelativeView().showMoreRecallList(result.getData());

                        RecallTemper.addRecallDtoList(result.getData());
                    }
                    hideLoading(Constant.LOADING_DEFAULT);
                }

                @Override
                public void onFailure(int errorCode) {
                    showError(errorCode);
                    hideLoading(Constant.LOADING_DEFAULT);
                }
            });
        }else{
            getRelativeView().showWarning(Constant.WARNING_NET);
            getRelativeView().setLoadMoreEnd();
        }
    }

    private void updateRequestParam(List<RecallDto> recallDtoList){
        if(recallDtoList.size() > 0){
            recallListReq.setPageIndex(recallListReq.getPageIndex() + 1);
            recallListReq.setLastRecall(recallDtoList.get(0).getRecallNo());
        }
    }

    private void initRecallListReq(){
        recallListReq.setType(Constant.RECALL_ALL);
        recallListReq.setPageIndex(1);
        recallListReq.setPageSize(20);
        recallListReq.setUserNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        recallListReq.setLastRecall(-1);
    }

}
