package com.bili.diushoujuaner.model.eventhelper;

import com.bili.diushoujuaner.utils.entity.dto.RecallDto;

/**
 * Created by BiLi on 2016/4/12.
 */
public class PublishRecallEvent {

    private RecallDto recallDto;

    public PublishRecallEvent(RecallDto recallDto) {
        this.recallDto = recallDto;
    }

    public RecallDto getRecallDto() {
        return recallDto;
    }

    public void setRecallDto(RecallDto recallDto) {
        this.recallDto = recallDto;
    }
}
