package com.bili.diushoujuaner.model.actionhelper;

import com.bili.diushoujuaner.model.actionhelper.respon.ActionResponse;
import com.bili.diushoujuaner.model.apihelper.request.PartyHeadUpdateReq;
import com.bili.diushoujuaner.model.apihelper.request.RecallSerialReq;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;

/**
 * Created by BiLi on 2016/4/11.
 */
public interface IFileAction {

    void uploadHeadPic(String path, ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener);

    void uploadWallpaper(String path, ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener);

    void uploadRecallPic(RecallSerialReq recallSerialReq, String path, ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener);

    void upoadPartyHeadPic(PartyHeadUpdateReq partyHeadUpdateReq, String path, ActionFileCallbackListener<ActionResponse<String>> actionFileCallbackListener);

}
