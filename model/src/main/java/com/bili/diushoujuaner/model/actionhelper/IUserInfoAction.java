package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.AccountUpdateReq;
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

    void getUserInfo(ActionStringCallbackListener<ActionResponse<User>> actionStringCallbackListener);

    void getAutographModify(AutographModifyReq autographModifyReq, ActionStringCallbackListener<ActionResponse<String>> actionStringCallbackListener);

    void getUserInfoUpdate(UserInfoReq userInfoReq, ActionStringCallbackListener<ActionResponse<User>> actionStringCallbackListener);

    void clearUser();

    void getVerifyCode(VerifyReq verifyReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getAcountRegist(AccountUpdateReq accountUpdateReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getAcountReset(AccountUpdateReq accountUpdateReq, ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);

    void getLogout(final ActionStringCallbackListener<ActionResponse<Void>> actionStringCallbackListener);
}
