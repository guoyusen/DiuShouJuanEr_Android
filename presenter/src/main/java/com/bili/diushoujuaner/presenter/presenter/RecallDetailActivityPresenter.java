package com.bili.diushoujuaner.presenter.presenter;

import android.text.SpannableString;

import com.bili.diushoujuaner.utils.entity.dto.GoodDto;
import com.bili.diushoujuaner.utils.entity.dto.PictureDto;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.utils.entity.vo.PictureVo;

import java.util.List;

/**
 * Created by BiLi on 2016/4/2.
 */
public interface RecallDetailActivityPresenter {

    /**
     * 获取赞列表
     * @param goodDtoList
     * @return
     */
    SpannableString getSpannableString(List<GoodDto> goodDtoList);

    boolean executeGoodChange(boolean goodstatus, long recallNo);

    void getGoodAdd(long recallNo);

    void getGoodRemove(long recallNo);

    void removeRecallDto(long recallNo);

    void getCommentPublish(long recallNo, String content);

    void getResponPublish(long recallNo, long commentNo, long toNo, String content, String nickNameTo);

    RecallDto getRecallDtoByRecallNo(long recallNo);

    void showRecallDetailByRecallNo(long recallNo);

    List<GoodDto> changeGoodStatusToLocal(long recallNo);

    boolean getGoodStatus(long recallNo);

    List<PictureVo> changePictureDtoListToPictureVoList(List<PictureDto> pictureDtoList);

    long getLoginedUserNo();

    void getCommentRemove(long recallNo, long commentNo);

    void getResponRemove(long recallNo, long commentNo, long responNo);

}
