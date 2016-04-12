package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.utils.entity.ImageItemVo;

import java.util.ArrayList;

/**
 * Created by BiLi on 2016/4/12.
 */
public interface RecallAddActivityPresenter {

    void publishRecall(ArrayList<ImageItemVo> imageItemVoList, String content);

}
