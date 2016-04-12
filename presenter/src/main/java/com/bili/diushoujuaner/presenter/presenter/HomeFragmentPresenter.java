package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.model.apihelper.response.RecallDto;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface HomeFragmentPresenter {

    void getGoodAdd(long recallNo);

    void getGoodRemove(long recallNo);

    void getRecallList(final int refreshType);

    void showRecallFromCache();

    void getMoreRecallList();

    boolean executeGoodChange(boolean goodstatus, long recallNo);

    boolean getGoodStatusByRecallNo(long recallNo);

    void changeGoodStatusToLocal(long recallNo, int position);

}
