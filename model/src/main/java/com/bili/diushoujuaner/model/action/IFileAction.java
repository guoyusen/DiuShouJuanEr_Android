package com.bili.diushoujuaner.model.action;

import com.bili.diushoujuaner.model.action.respon.ActionRespon;
import com.bili.diushoujuaner.model.callback.ActionFileCallbackListener;

/**
 * Created by BiLi on 2016/4/11.
 */
public interface IFileAction {

    void uploadHeadPic(String path, ActionFileCallbackListener<ActionRespon<String>> actionFileCallbackListener);

    void uploadWallpaper(String path, ActionFileCallbackListener<ActionRespon<String>> actionFileCallbackListener);
}
