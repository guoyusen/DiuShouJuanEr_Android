package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;

import com.bili.diushoujuaner.model.actionhelper.action.ContactAction;
import com.bili.diushoujuaner.model.actionhelper.action.FileAction;
import com.bili.diushoujuaner.model.actionhelper.action.GoodAction;
import com.bili.diushoujuaner.model.actionhelper.action.RecallAction;
import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.ContactInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallListReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallRemoveReq;
import com.bili.diushoujuaner.utils.entity.dto.GoodDto;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.SpaceActivityPresenter;
import com.bili.diushoujuaner.presenter.view.ISpaceView;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.vo.FriendVo;
import com.bili.diushoujuaner.model.eventhelper.UpdateWallPaperEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/4/9.
 */
public class SpaceActivityPresenterImpl extends BasePresenter<ISpaceView> implements SpaceActivityPresenter {

    private RecallListReq recallListReq = new RecallListReq();

    public SpaceActivityPresenterImpl(ISpaceView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public void updateWallpaper(String path) {
        showLoading(Constant.LOADING_TOP,"正在上传壁纸");
        FileAction.getInstance(context).uploadWallpaper(path, new ActionFileCallbackListener<ActionRespon<String>>() {
            @Override
            public void onSuccess(ActionRespon<String> result) {
                hideLoading(Constant.LOADING_TOP);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().updateWallPaper(result.getData());
                    }
                    EventBus.getDefault().post(new UpdateWallPaperEvent(result.getData()));
                }
            }

            @Override
            public void onFailure(int errorCode) {
                hideLoading(Constant.LOADING_TOP);
                showError(errorCode);
            }

            @Override
            public void onProgress(float progress) {

            }
        });
    }

    @Override
    public void getRecallRemove(final long recallNo, final int position) {
        showLoading(Constant.LOADING_CENTER, "正在删除...");
        RecallAction.getInstance(context).getRecallRemove(new RecallRemoveReq(recallNo), new ActionStringCallbackListener<ActionRespon<Long>>() {
            @Override
            public void onSuccess(ActionRespon<Long> result) {
                hideLoading(Constant.LOADING_CENTER);
                if(showMessage(result.getRetCode(), result.getMessage())){
                    getBindView().removeRecallByPosition(position);
                    RecallTemper.removeRecallDto(result.getData());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_CENTER);
            }
        });
    }

    @Override
    public void addGoodToLocal(RecallDto recallDto) {
        GoodDto goodDto = new GoodDto();
        goodDto.setUserNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        goodDto.setNickName(UserInfoAction.getInstance(context).getUserFromLocal().getNickName());

        if(recallDto != null){
            recallDto.getGoodList().add(0, goodDto);
        }
        RecallTemper.addGood(goodDto, recallDto.getRecallNo());
    }

    @Override
    public void removeGoodFromLocal(RecallDto recallDto) {
        if(recallDto != null){
            RecallTemper.removeGood(recallDto.getRecallNo());
            List<GoodDto> goodDtoList = recallDto.getGoodList();
            if(goodDtoList.isEmpty()){
                return;
            }
            for(GoodDto goodDto : goodDtoList){
                if(goodDto.getUserNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                    goodDtoList.remove(goodDto);
                    break;
                }
            }
        }
    }

    @Override
    public boolean executeGoodChange(boolean goodstatus, long recallNo) {
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

    @Override
    public void getGoodAdd(long recallNo){
        GoodAction.getInstance(context).getGoodAdd(recallNo, new ActionStringCallbackListener<ActionRespon<String>>() {
            @Override
            public void onSuccess(ActionRespon<String> result) {
                showMessage(result.getRetCode(), result.getMessage());
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void getGoodRemove(long recallNo){
        GoodAction.getInstance(context).getGoodRemove(recallNo, new ActionStringCallbackListener<ActionRespon<String>>() {
            @Override
            public void onSuccess(ActionRespon<String> result) {
                showMessage(result.getRetCode(), result.getMessage());
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public boolean getGoodStatusByRecallNo(long recallNo) {
        return GoodTemper.getGoodStatus(recallNo);
    }

    @Override
    public void setGoodStatusByRecallNo(long recallNo, boolean status) {
        GoodTemper.setGoodStatus(recallNo, status);
    }

    @Override
    public long getCustomSessionUserNo() {
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    @Override
    public void addRecallDtoToTemper(RecallDto recallDto) {
        RecallTemper.addRecallDto(recallDto);
    }

    @Override
    public void getContactInfo(final long userNo) {
        ContactAction.getInstance(context).getContactFromLocal(userNo, new ActionStringCallbackListener<ActionRespon<FriendVo>>() {
            @Override
            public void onSuccess(ActionRespon<FriendVo> result) {
                //数据不合法，不去做界面处理，走api
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    //TODO 更新获取联系人全量信息的时间间隔
                    if (result.getData() == null || Common.isEmpty(result.getData().getUpdateTime()) || Common.getHourDifferenceBetweenTime(result.getData().getUpdateTime(), Common.getCurrentTimeYYMMDD_HHMMSS()) > 1) {
                        //数据已经无效，则在不为空的情况下，先进行显示，在进行更新获取
                        if (result.getData() != null && isBindViewValid()) {
                            getBindView().showContactInfo(result.getData());
                        }
                        getFriendVoFromApi(userNo);
                    }else{
                        //数据仍在有效期内，直接显示，无需更新
                        if(isBindViewValid()){
                            getBindView().showContactInfo(result.getData());
                        }
                        hideLoading(Constant.LOADING_DEFAULT);
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
            }
        });
    }

    private void getFriendVoFromApi(long userNo){
        ContactInfoReq contactInfoReq = new ContactInfoReq();
        contactInfoReq.setUserNo(userNo);

        ContactAction.getInstance(context).getContactFromApi(contactInfoReq, new ActionStringCallbackListener<ActionRespon<FriendVo>>() {
            @Override
            public void onSuccess(ActionRespon<FriendVo> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    if(isBindViewValid()){
                        getBindView().showContactInfo(result.getData());
                    }
                }
                hideLoading(Constant.LOADING_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_DEFAULT);
            }
        });
    }

    @Override
    public void getMoreRecallList(long userNo) {
        RecallAction.getInstance(context).getRecallList(recallListReq, new ActionStringCallbackListener<ActionRespon<List<RecallDto>>>() {
            @Override
            public void onSuccess(ActionRespon<List<RecallDto>> result) {
                if (showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()) {
                    updateRequestParam(result.getData());
                    getBindView().showMoreRecallList(result.getData());
                    RecallTemper.addRecallDtoList(result.getData());
                }else{
                    if(isBindViewValid()){
                        getBindView().setLoadMoreEnd();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                if(isBindViewValid()){
                    getBindView().setLoadMoreEnd();
                }
            }
        });
    }

    @Override
    public void getRecallList(long userNo) {
        initRecallListReq(userNo);
        showLoading(Constant.LOADING_DEFAULT,"");

        RecallAction.getInstance(context).getRecallList(recallListReq, new ActionStringCallbackListener<ActionRespon<List<RecallDto>>>() {
            @Override
            public void onSuccess(ActionRespon<List<RecallDto>> result) {
                if (showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()) {
                    if(result.getData() != null){
                        getBindView().showRecallList(result.getData());
                    }
                    updateRequestParam(result.getData());
                }
                hideLoading(Constant.LOADING_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
                hideLoading(Constant.LOADING_DEFAULT);
            }
        });
    }

    @Override
    public void showRecallFromCache(long userNo) {
        RecallAction.getInstance(context).getUserRecallListFromACache(userNo, new ActionStringCallbackListener<ActionRespon<List<RecallDto>>>() {
            @Override
            public void onSuccess(ActionRespon<List<RecallDto>> result) {
                if (showMessage(result.getRetCode(), result.getMessage()) && isBindViewValid()) {
                    getBindView().showRecallList(result.getData());
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    private void updateRequestParam(List<RecallDto> recallDtoList){
        if(recallDtoList == null){
            return;
        }
        if(recallDtoList.size() >= recallListReq.getPageSize()){
            recallListReq.setPageIndex(recallListReq.getPageIndex() + 1);
            recallListReq.setLastRecall(recallDtoList.get(0).getRecallNo());
        }
    }

    private void initRecallListReq(long userNo){
        recallListReq.setType(Constant.RECALL_USER);
        recallListReq.setPageIndex(1);
        recallListReq.setPageSize(20);
        recallListReq.setUserNo(userNo);
        recallListReq.setLastRecall(-1);
    }
}
