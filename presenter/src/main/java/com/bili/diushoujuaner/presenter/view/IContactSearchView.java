package com.bili.diushoujuaner.presenter.view;

import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.entity.dto.ContactDto;

import java.util.List;

/**
 * Created by BiLi on 2016/4/23.
 */
public interface IContactSearchView extends IBaseView {

    void showSearchContactsResult(List<ContactDto> contactDtoList);

}
