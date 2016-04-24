package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/24.
 */
public interface IContactAddView extends IBaseView {

    void showApplyVoList(List<ApplyVo> applyVoList);

}
