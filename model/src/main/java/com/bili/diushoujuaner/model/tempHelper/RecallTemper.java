package com.bili.diushoujuaner.model.tempHelper;

import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.utils.response.GoodDto;
import com.bili.diushoujuaner.utils.response.RecallDto;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by BiLi on 2016/3/23.
 */
public class RecallTemper {

    private static Hashtable<Long, RecallDto> recallDtoHashtable = new Hashtable<>();
    private static List<RecallDto> recallList = new ArrayList<>();

    public static void addRecallDto(RecallDto recallDto){
        recallDtoHashtable.put(recallDto.getRecallNo(), recallDto);
    }

    public static RecallDto getRecallDto(Long recallNo){
        return recallDtoHashtable.get(recallNo);
    }

    public static void addRecallDtoList(List<RecallDto> recallDtoList){
        recallList.addAll(recallDtoList);
        saveRecallListToCache();
        for (RecallDto recallDto : recallDtoList){
            addRecallDto(recallDto);
        }
    }

    public static void addGood(Long recallNo, GoodDto goodDto){
        recallDtoHashtable.get(recallNo).getGoodList().add(0,goodDto);
        saveRecallListToCache();
    }

    public static void removeGood(Long recallNo){
        List<GoodDto> goodDtoList = recallDtoHashtable.get(recallNo).getGoodList();
        if(goodDtoList.size() <= 0){
            return;
        }
        for(GoodDto goodDto : goodDtoList){
            if(goodDto.getUserNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                goodDtoList.remove(goodDto);
                break;
            }
        }
        saveRecallListToCache();
    }

    private static void saveRecallListToCache(){
        ACache.getInstance().put(Constant.ACACHE_RECALL_LIST, GsonParser.getInstance().toJson(recallList));
    }

    public static void clear(){
        recallList.clear();
        recallDtoHashtable.clear();
    }

}
