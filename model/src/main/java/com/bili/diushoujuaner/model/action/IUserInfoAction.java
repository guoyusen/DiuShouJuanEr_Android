package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.apihelper.request.AcountUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.AutographModifyReq;
import com.bili.diushoujuaner.model.apihelper.request.UserInfoReq;
import com.bili.diushoujuaner.model.apihelper.request.VerifyReq;
import com.bili.diushoujuaner.model.callback.ActionStringCallbackListener;
import com.bili.diushoujuaner.model.databasehelper.dao.User;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface IUserInfoAction {

    User getUserFromLocal();

    void getUserInfo(final ActionStringCallbackListener<ActionRespon<User>> actionStringCallbackListener);

    void getAutographModify(AutographModifyReq autographModifyReq, final ActionStringCallbackListener<ActionRespon<String>> actionStringCallbackListener);

    void getUserInfoUpdate(UserInfoReq userInfoReq, final ActionStringCallbackListener<ActionRespon<User>> actionStringCallbackListener);

    void clearUser();

    void getVerifyCode(VerifyReq verifyReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getAcountRegist(AcountUpdateReq acountUpdateReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getAcountReset(AcountUpdateReq acountUpdateReq, final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);

    void getLogout(final ActionStringCallbackListener<ActionRespon<Void>> actionStringCallbackListener);
}
