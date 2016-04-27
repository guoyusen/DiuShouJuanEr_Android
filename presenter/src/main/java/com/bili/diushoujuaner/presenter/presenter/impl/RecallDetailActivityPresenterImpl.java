package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.bili.diushoujuaner.model.actionhelper.action.CommentAction;
import com.bili.diushoujuaner.model.actionhelper.action.GoodAction;
import com.bili.diushoujuaner.model.actionhelper.action.ResponAction;
import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.CommentAddReq;
import com.bili.diushoujuaner.model.apihelper.request.CommentRemoveReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponAddReq;
import com.bili.diushoujuaner.model.apihelper.request.ResponRemoveReq;
import com.bili.diushoujuaner.utils.TimeUtil;
import com.bili.diushoujuaner.utils.entity.dto.PictureDto;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.RecallDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IRecallDetailView;
import com.bili.diushoujuaner.utils.entity.dto.CommentDto;
import com.bili.diushoujuaner.utils.entity.dto.GoodDto;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.utils.entity.dto.ResponDto;
import com.bili.diushoujuaner.utils.entity.vo.PictureVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/3/23.
 */
public class RecallDetailActivityPresenterImpl extends BasePresenter<IRecallDetailView> implements RecallDetailActivityPresenter {

    class Clickable extends ClickableSpan {
        private long userNo;

        public Clickable(long userNo) {
            this.userNo = userNo;
        }

        @Override
        public void onClick(View widget) {
            if(isBindViewValid()){
                getBindView().showGooderSpace(userNo);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    public RecallDetailActivityPresenterImpl(IRecallDetailView baseView, Context context) {
        super(baseView, context);
    }

    @Override
    public long getLoginedUserNo() {
        return CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    @Override
    public boolean getGoodStatus(long recallNo) {
        return GoodTemper.getInstance().getGoodStatus(recallNo);
    }

    @Override
    public List<GoodDto> changeGoodStatusToLocal(long recallNo) {
        if(GoodTemper.getInstance().getGoodStatus(recallNo)){
            GoodTemper.getInstance().setGoodStatus(recallNo, false);
            if(isBindViewValid()){
                getBindView().setGoodStatus(false);
            }
            removeGoodDtoFromTemper(recallNo);
        }else{
            GoodTemper.getInstance().setGoodStatus(recallNo, true);
            if(isBindViewValid()){
                getBindView().setGoodStatus(true);
            }
            addGoodDtoToTemper(recallNo);
        }
        return  getRecallDtoByRecallNo(recallNo).getGoodList();
    }

    @Override
    public void showRecallDetailByRecallNo(long recallNo) {
        if(isBindViewValid()){
            getBindView().showRecallDetail(getRecallDtoByRecallNo(recallNo));
        }
    }

    @Override
    public RecallDto getRecallDtoByRecallNo(long recallNo) {
        return RecallTemper.getInstance().getRecallDto(recallNo);
    }

    @Override
    public SpannableString getSpannableString(List<GoodDto> goodDtoList){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, len = goodDtoList.size(); i < len; i++) {
            String tmpNickName = ContactTemper.getInstance().getFriendRemark(goodDtoList.get(i).getUserNo());
            if (tmpNickName == null) {
                tmpNickName = goodDtoList.get(i).getNickName();
            }
            if (i < len - 1) {
                stringBuilder.append(tmpNickName + "、");
            } else if (i == len - 1) {
                stringBuilder.append(tmpNickName);
            }
        }
        SpannableString goodDetail = new SpannableString(stringBuilder.toString());
        int index = 0, tmpEnd = 0;
        for (int i = 0, len = goodDtoList.size(); i < len; i++) {
            String tmpNickName = ContactTemper.getInstance().getFriendRemark(goodDtoList.get(i).getUserNo());
            if (tmpNickName == null) {
                tmpNickName = goodDtoList.get(i).getNickName();
            }
            if (i < len - 1) {
                tmpEnd = index + tmpNickName.length() + 1;
            } else if (i == len - 1) {
                tmpEnd = index + tmpNickName.length();
            }
            goodDetail.setSpan(new Clickable(goodDtoList.get(i).getUserNo()), index, tmpEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = tmpEnd;
        }

        return goodDetail;
    }

    @Override
    public boolean executeGoodChange(boolean goodstatus, long recallNo){
        if(goodstatus == GoodTemper.getInstance().getGoodStatus(recallNo)){
            return goodstatus;
        }
        if(GoodTemper.getInstance().getGoodStatus(recallNo)){
            getGoodAdd(recallNo);
            return true;
        }else{
            getGoodRemove(recallNo);
            return false;
        }
    }

    @Override
    public void getGoodAdd(long recallNo){
        GoodAction.getInstance(context).getGoodAdd(recallNo, new ActionStringCallbackListener<ActionResponse<String>>() {
            @Override
            public void onSuccess(ActionResponse<String> result) {
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
        GoodAction.getInstance(context).getGoodRemove(recallNo, new ActionStringCallbackListener<ActionResponse<String>>() {
            @Override
            public void onSuccess(ActionResponse<String> result) {
                showMessage(result.getRetCode(), result.getMessage());
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void removeRecallDto(long recallNo){
        RecallTemper.getInstance().removeRecallDto(recallNo);
    }

    @Override
    public void getResponPublish(final long recallNo, long commentNo, long toNo, String content, String nickNameTo) {
        ResponDto responDto = addResponDtoToLocal(recallNo, commentNo, toNo, content, nickNameTo);

        final ResponAddReq responAddReq = new ResponAddReq();
        responAddReq.setToNo(responDto.getToNo());
        responAddReq.setTimeStamp(responDto.getTimeStamp());
        responAddReq.setCommentNo(responDto.getCommentNo());
        responAddReq.setContent(responDto.getContent());

        ResponAction.getInstance(context).getResponAdd(responAddReq, new ActionStringCallbackListener<ActionResponse<ResponDto>>() {
            @Override
            public void onSuccess(ActionResponse<ResponDto> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    RecallDto recallDto = RecallTemper.getInstance().getRecallDto(recallNo);
                    for (CommentDto commentDto : recallDto.getCommentList()) {
                        if (commentDto.getCommentNo() == result.getData().getCommentNo()) {
                            for (ResponDto responDto : commentDto.getResponList()) {
                                if (responDto.getTimeStamp() != null && responDto.getTimeStamp().equals(result.getData().getTimeStamp())) {
                                    responDto.setResponNo(result.getData().getResponNo());
                                    responDto.setAddTime(result.getData().getAddTime());
                                    responDto.setFromPicPath(result.getData().getFromPicPath());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });

    }

    @Override
    public void getCommentPublish(long recallNo, String content){
        CommentDto commentDto = addCommentDtoToLocal(recallNo, content);
        if(isBindViewValid()){
            getBindView().refreshComment();
        }

        CommentAddReq commentAddReq = new CommentAddReq();
        commentAddReq.setContent(commentDto.getContent());
        commentAddReq.setRecallNo(commentDto.getRecallNo());
        commentAddReq.setTimeStamp(commentDto.getTimeStamp());

        CommentAction.getInstance(context).getCommentAdd(commentAddReq, new ActionStringCallbackListener<ActionResponse<CommentDto>>() {
            @Override
            public void onSuccess(ActionResponse<CommentDto> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    RecallDto recallDto = RecallTemper.getInstance().getRecallDto(result.getData().getRecallNo());
                    for (CommentDto commentDto : recallDto.getCommentList()) {
                        if (commentDto.getTimeStamp() != null && commentDto.getTimeStamp().equals(result.getData().getTimeStamp())) {
                            commentDto.setAddTime(result.getData().getAddTime());
                            commentDto.setCommentNo(result.getData().getCommentNo());
                        }
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    private ResponDto addResponDtoToLocal(long recallNo, long commentNo, long toNo, String content, String nickNameTo){
        ResponDto responDto = new ResponDto();
        responDto.setCommentNo(commentNo);
        responDto.setAddTime(TimeUtil.getCurrentTimeYYMMDD_HHMMSS());
        responDto.setFromNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        responDto.setToNo(toNo);
        responDto.setContent(content);
        responDto.setNickNameTo(nickNameTo);
        responDto.setNickNameFrom(UserInfoAction.getInstance(context).getUserFromLocal().getNickName());
        responDto.setTimeStamp(System.currentTimeMillis() + "");

        RecallTemper.getInstance().addResponDtoByRecallNo(recallNo, commentNo, responDto);

        return responDto;
    }

    private CommentDto addCommentDtoToLocal(long recallNo, String content){
        CommentDto commentDto = new CommentDto();
        commentDto.setTimeStamp(System.currentTimeMillis() + "");
        commentDto.setContent(content);
        commentDto.setRecallNo(recallNo);
        commentDto.setNickName(UserInfoAction.getInstance(context).getUserFromLocal().getNickName());
        commentDto.setAddTime(TimeUtil.getCurrentTimeYYMMDD_HHMMSS());
        commentDto.setFromNo(UserInfoAction.getInstance(context).getUserFromLocal().getUserNo());
        commentDto.setFromPicPath(UserInfoAction.getInstance(context).getUserFromLocal().getPicPath());
        commentDto.setResponList(new ArrayList<ResponDto>());

        RecallTemper.getInstance().addCommentDtoByRecallNo(recallNo, commentDto);

        return commentDto;
    }

    @Override
    public List<PictureVo> changePictureDtoListToPictureVoList(List<PictureDto> pictureDtoList){
        List<PictureVo> pictureVoList = new ArrayList<>();
        for(PictureDto pictureDto : pictureDtoList){
            PictureVo pictureVo = new PictureVo();
            pictureVo.setPicPath(pictureDto.getPicPath());

            pictureVoList.add(pictureVo);
        }
        return pictureVoList;
    }

    @Override
    public void getCommentRemove(final long recallNo, long commentNo) {
        CommentRemoveReq commentRemoveReq = new CommentRemoveReq();
        commentRemoveReq.setCommentNo(commentNo);

        CommentAction.getInstance(context).getCommentRemove(commentRemoveReq, new ActionStringCallbackListener<ActionResponse<Long>>() {
            @Override
            public void onSuccess(ActionResponse<Long> result) {
                if(showMessage(result.getRetCode(), result.getMessage())){
                    RecallTemper.getInstance().removeCommentByRecalllNo(recallNo, result.getData());
                    if(isBindViewValid()){
                        getBindView().refreshComment();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    @Override
    public void getResponRemove(final long recallNo, final long commentNo, long responNo) {
        ResponRemoveReq responRemoveReq = new ResponRemoveReq();
        responRemoveReq.setResponNo(responNo);

        ResponAction.getInstance(context).getResponRemove(responRemoveReq, new ActionStringCallbackListener<ActionResponse<Long>>() {
            @Override
            public void onSuccess(ActionResponse<Long> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    RecallTemper.getInstance().removeResponByRecallNo(recallNo, commentNo, result.getData());
                    if(isBindViewValid()){
                        getBindView().refreshComment();
                    }
                }
            }

            @Override
            public void onFailure(int errorCode) {
                showError(errorCode);
            }
        });
    }

    private void addGoodDtoToTemper(long recallNo){
        GoodDto goodDto = new GoodDto();
        goodDto.setUserNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        goodDto.setNickName(DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo()).getNickName());
        RecallTemper.getInstance().addGood(goodDto, recallNo);
    }

    private void removeGoodDtoFromTemper(long recallNo){
        RecallTemper.getInstance().removeGood(recallNo);
    }
}
