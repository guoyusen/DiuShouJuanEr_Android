package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.bili.diushoujuaner.model.action.GoodAction;
import com.bili.diushoujuaner.model.apihelper.ApiRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.DBManager;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.model.tempHelper.RecallTemper;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.view.IRecallDetailView;
import com.bili.diushoujuaner.utils.response.GoodDto;
import com.bili.diushoujuaner.utils.response.RecallDto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/23.
 */
public class RecallDetailActivityPresenter extends BasePresenter<IRecallDetailView> {

    public RecallDetailActivityPresenter(IRecallDetailView baseView, Context context) {
        super(baseView, context);
    }

    /**
     * 获取赞列表
     * @param goodDtoList
     * @return
     */
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

    class Clickable extends ClickableSpan {
        private long userNo;

        public Clickable(long userNo) {
            this.userNo = userNo;
        }

        @Override
        public void onClick(View widget) {
            getRelativeView().showGooderDetail(userNo);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
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

    public void addGoodDtoToTemper(long recallNo){
        GoodDto goodDto = new GoodDto();
        goodDto.setUserNo(CustomSessionPreference.getInstance().getCustomSession().getUserNo());
        goodDto.setNickName(DBManager.getInstance().getUser(CustomSessionPreference.getInstance().getCustomSession().getUserNo()).getRealName());
        RecallTemper.addGood(goodDto, recallNo);
    }

    public void removeGoodDtoFromTemper(long recallNo){
        RecallTemper.removeGood(recallNo);
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
}
