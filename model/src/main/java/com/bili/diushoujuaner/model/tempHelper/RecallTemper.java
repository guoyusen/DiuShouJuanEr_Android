package com.bili.diushoujuaner.model.tempHelper;

import com.bili.diushoujuaner.model.apihelper.response.ResponDto;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.utils.GsonParser;
import com.bili.diushoujuaner.model.apihelper.response.CommentDto;
import com.bili.diushoujuaner.model.apihelper.response.GoodDto;
import com.bili.diushoujuaner.model.apihelper.response.RecallDto;

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

    public static void removeRecallDto(Long recallNo){
        recallDtoHashtable.remove(recallNo);
    }

    public static RecallDto getRecallDto(Long recallNo){
        return recallDtoHashtable.get(recallNo);
    }

    public static void addRecallDtoList(List<RecallDto> recallDtoList){
        // 默认进行存储，但在好友空间、个人空间的地方不进行存储
        addRecallDtoList(recallDtoList, true);
    }

    public static void addRecallDtoList(List<RecallDto> recallDtoList, boolean isACache){
        if(recallDtoList == null){
            return;
        }
        recallList.addAll(recallDtoList);
        for(RecallDto recallDto : recallDtoList){
            recallDtoHashtable.put(recallDto.getRecallNo(), recallDto);
        }
        if(isACache){
            saveRecallListToCache();
        }
    }

    /**
     * 通过索引值来添加点赞 默认更新缓存
     */
    public static void addGood(GoodDto goodDto, Integer position){
        addGood(goodDto, position, true);
    }

    /**
     * 通过索引值来添加点赞 设置是否更新缓存
     */
    public static void addGood(GoodDto goodDto, Integer position, boolean isACache){
        RecallDto recallDto = recallList.get(position);
        if(recallDto != null){
            recallDto.getGoodList().add(0, goodDto);
        }
        if(isACache){
            saveRecallListToCache();
        }
    }

    /**
     * 通过recallNo来添加点赞 默认更新缓存
     */
    public static void addGood(GoodDto goodDto, Long recallNo){
        addGood(goodDto, recallNo, true);
    }

    /**
     * 通过recallNo来添加点赞 设置是否更新缓存
     */
    public static void addGood(GoodDto goodDto, Long recallNo, boolean isACache){
        recallDtoHashtable.get(recallNo).getGoodList().add(0, goodDto);
        if(isACache){
            saveRecallListToCache();
        }
    }

    /**
     * 通过索引值来删除点赞 默认更新缓存
     */
    public static void removeGood(Integer position){
        removeGood(position, true);
    }

    /**
     * 通过索引值来删除点赞 设置是否更新缓存
     */
    public static void removeGood(Integer position, boolean isACache){
        RecallDto recallDto = recallList.get(position);
        if(recallDto != null){
            List<GoodDto> goodDtoList = recallDto.getGoodList();
            if(goodDtoList.isEmpty()){
                return;
            }
            for(GoodDto goodDto : goodDtoList){
                if(goodDto.getUserNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                    goodDtoList.remove(goodDto);
                    break;
                }
            }
            if(isACache){
                saveRecallListToCache();
            }
        }
    }

    /**
     * 通过recallNo来删除点赞 默认更新缓存
     */
    public static void removeGood(Long recallNo){
        removeGood(recallNo, true);
    }

    /**
     * 通过recallNo来删除点赞 设置是否更新缓存
     */
    public static void removeGood(Long recallNo, boolean isACache){
        RecallDto recallDto = recallDtoHashtable.get(recallNo);
        if(recallDto != null){
            List<GoodDto> goodDtoList = recallDto.getGoodList();
            if(goodDtoList.isEmpty()){
                return;
            }
            for(GoodDto goodDto : goodDtoList){
                if(goodDto.getUserNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                    goodDtoList.remove(goodDto);
                    break;
                }
            }
            if(isACache){
                saveRecallListToCache();
            }
        }
    }

    public static void saveRecallListToCache(){
        // 本地缓存只存储20条数据
        if(recallList.size() >= 20){
            ACache.getInstance().put(Constant.ACACHE_RECALL_LIST, GsonParser.getInstance().toJson(recallList.subList(0,20)));
        }else{
            ACache.getInstance().put(Constant.ACACHE_RECALL_LIST, GsonParser.getInstance().toJson(recallList.subList(0,recallList.size())));
        }
    }

    public static void addCommentDtoByRecallNo(Long recallNo, CommentDto commentDto){
        addCommentDtoByRecallNo(recallNo, commentDto, true);
    }

    public static void addCommentDtoByRecallNo(Long recallNo, CommentDto commentDto, boolean isACache){
        recallDtoHashtable.get(recallNo).getCommentList().add(commentDto);
        if(isACache){
            saveRecallListToCache();
        }
    }

    public static void addResponDtoByRecallNo(Long recallNo, Long commentNo, ResponDto responDto){
        addResponDtoByRecallNo(recallNo, commentNo, responDto, true);
    }

    public static void addResponDtoByRecallNo(Long recallNo, Long commentNo, ResponDto responDto, boolean isACache){
        for(CommentDto commentDto : recallDtoHashtable.get(recallNo).getCommentList()){
            if(commentDto.getCommentNo() == commentNo){
                commentDto.getResponList().add(responDto);
                break;
            }
        }
        if(isACache){
            saveRecallListToCache();
        }
    }

    public static void removeCommentByRecalllNo(Long recallNo, Long commentNo){
        removeCommentByRecalllNo(recallNo, commentNo, true);
    }

    public static void removeCommentByRecalllNo(Long recallNo, Long commentNo, boolean isACache){
        for(CommentDto commentDto : recallDtoHashtable.get(recallNo).getCommentList()){
            if(commentDto.getCommentNo() == commentNo){
                recallDtoHashtable.get(recallNo).getCommentList().remove(commentDto);
                break;
            }
        }
        if(isACache){
            saveRecallListToCache();
        }
    }

    public static void removeResponByRecallNo(Long recallNo, Long commentNo, Long responNo){
        removeResponByRecallNo(recallNo, commentNo, responNo, true);
    }

    public static void removeResponByRecallNo(Long recallNo, Long commentNo, Long responNo, boolean isACache){
        for(CommentDto commentDto : recallDtoHashtable.get(recallNo).getCommentList()){
            if(commentDto.getCommentNo() == commentNo){
                for(ResponDto responDto : commentDto.getResponList()){
                    if(responDto.getResponNo() == responNo){
                        commentDto.getResponList().remove(responDto);
                        break;
                    }
                }
                break;
            }
        }
        if(isACache){
            saveRecallListToCache();
        }
    }

    public static void clear(){
        recallList.clear();
        recallDtoHashtable.clear();
    }

}
