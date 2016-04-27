package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/26.
 */
public interface IMemberView extends IBaseView {

    void showMemberList(List<MemberVo> memberVoList);

}
