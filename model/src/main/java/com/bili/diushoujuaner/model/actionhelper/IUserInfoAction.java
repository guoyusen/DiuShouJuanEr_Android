package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.AcountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.utils.entity.po.User;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IUserInfoAction {

    User getUserFromLocal();

    void getUserInfo(ActionStringCallbackListener<ActionRespon<User>> actionStringCallbackListener);

    void getAutographModify(AutographModifyReq autographModifyReq, ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getUserInfoUpdate(UserInfoReq userInfoReq, ActionStringCallbackListener<ActionRespon<User>> actionStringCallbackListener);

    void clearUser();

    void getVerifyCode(VerifyReq verifyReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getAcountRegist(AcountUpdateReq acountUpdateReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getAcountReset(AcountUpdateReq acountUpdateReq, ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getLogout(final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);
}
