package com.bili.diushoujuaner.presenter.presenter;

import android.content.Context;

import com.bili.diushoujuaner.model.action.ContactAction;
import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionCallbackListener;
import com.bili.diushoujuaner.presenter.base.BasePresenter;
import com.bili.diushoujuaner.presenter.view.IPartyView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.entity.PartyVo;
import java.util.List;

/**
 * Created by BiLi on 2016/3/19.
 */
public class PartyActivityPresenter extends BasePresenter<IPartyView> {

    public PartyActivityPresenter(IPartyView baseView, Context context) {
        super(baseView, context);
    }

    public void getPartyList(){
        showLoading(Constant.LOADING_DEFAULT, "");
        ContactAction.getInstance().getPartyVoList(new ActionCallbackListener<ActionRespon<List<PartyVo>>>() {
            @Override
            public void onSuccess(ActionRespon<List<PartyVo>> result) {
                if (showMessage(result.getRetCode(), result.getMessage())) {
                    getRelativeView().showPartyList(result.getData());
                }
                hideLoading(Constant.LOADING_DEFAULT);
            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }
}
