package com.bili.diushoujuaner.presenter.presenter;

import com.bili.diushoujuaner.utils.entity.dto.RecallDto;

/**
 * Created by BiLi on 2016/4/9.
 */
public interface SpaceActivityPresenter {

    void getContactInfo(long userNo);

    void getRecallList(long userNo);

    void showRecallFromCache(long userNo);

    void getMoreRecallList(long userNo);

    void addRecallDtoToTemper(RecallDto recallDto);

    long getCustomSessionUserNo();

    boolean executeGoodChange(boolean goodstatus, long recallNo);

    boolean getGoodStatusByRecallNo(long recallNo);

    boolean isFriend(long userNo);

    void setGoodStatusByRecallNo(long recallNo, boolean status);

    void removeGoodFromLocal(RecallDto recallDto);

    void addGoodToLocal(RecallDto recallDto);

    void getGoodAdd(long recallNo);

    void getGoodRemove(long recallNo);

    void getRecallRemove(long recallNo, int position);

    void updateWallpaper(String path);

}
