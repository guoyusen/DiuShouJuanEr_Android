package com.bili.diushoujuaner.presenter.presenter.impl;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.bili.diushoujuaner.model.action.impl.GoodAction;
import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.presenter.RecallDetailActivityPresenter;
import com.bili.diushoujuaner.presenter.view.IRecallDetailView;
import com.bili.diushoujuaner.utils.response.CommentDto;
import com.bili.diushoujuaner.utils.response.GoodDto;
import com.bili.diushoujuaner.utils.response.RecallDto;

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
            getBindView().showGooderDetail(userNo);
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
    public boolean getGoodStatus(long recallNo) {
        return GoodTemper.getGoodStatus(recallNo);
    }

    @Override
    public List<GoodDto> changeGoodStatusToLocal(long recallNo) {
        if(GoodTemper.getGoodStatus(recallNo)){
            GoodTemper.setGoodStatus(recallNo, false);
            getBindView().setGoodStatus(false);
            removeGoodDtoFromTemper(recallNo);
        }else{
            GoodTemper.setGoodStatus(recallNo, true);
            getBindView().setGoodStatus(true);
            addGoodDtoToTemper(recallNo);
        }
        return  getRecallDtoByRecallNo(recallNo).getGoodList();
    }

    @Override
    public void showRecallDetailByRecallNo(long recallNo) {
        getBindView().showRecallDetail(getRecallDtoByRecallNo(recallNo));
    }

    @Override
    public RecallDto getRecallDtoByRecallNo(long recallNo) {
        return RecallTemper.getRecallDto(recallNo);
    }

    @Override
    public SpannableString getSpannableString(List<GoodDto> goodDtoList){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, len = goodDtoList.size(); i < len; i++) {
            String tmpNickName = ContactTemper.getFriendRemark(goodDtoList.get(i).getUserNo());
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
            String tmpNickName = ContactTemper.getFriendRemark(goodDtoList.get(i).getUserNo());
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

    @Override
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

    @Override
    public void removeRecallDto(long recallNo){
        RecallTemper.removeRecallDto(recallNo);
    }

    @Override
    public void getCommentPublish(long recallNo, String content){
        CommentDto commentDto = new CommentDto();
        commentDto.setTimeStamp(System.currentTimeMillis() + "");
        commentDto.setContent(content);
        commentDto.setNickName(UserInfoAction.getInstance().getUserFromLocal().getNickName());

    }

    private void addGoodDtoToTemper(long recallNo){
        GoodDto goodDto = new GoodDto();
        goodDto.setUserNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        goodDto.setNickName(DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo()).getRealName());
        RecallTemper.addGood(goodDto, recallNo);
    }

    private void removeGoodDtoFromTemper(long recallNo){
        RecallTemper.removeGood(recallNo);
    }
}
