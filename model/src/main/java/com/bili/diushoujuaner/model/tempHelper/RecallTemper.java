package com.bili.diushoujuaner.model.tempHelper;

import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.dto.ResponDto;
import com.bili.diushoujuaner.model.cachehelper.ACache;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.GsonUtil;
import com.bili.diushoujuaner.utils.entity.dto.CommentDto;
import com.bili.diushoujuaner.utils.entity.dto.GoodDto;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by BiLi on 2016/3/23.
 */
public class RecallTemper {

    private Hashtable<Long, RecallDto> recallDtoHashtable;
    private List<RecallDto> recallList;
    private static RecallTemper recallTemper;

    public RecallTemper(){
        recallDtoHashtable = new Hashtable<>();
        recallList = new ArrayList<>();
    }

    public static RecallTemper getInstance(){
        if(recallTemper == null){
            recallTemper = new RecallTemper();
        }
        return recallTemper;
    }

    public void addRecallDto(RecallDto recallDto){
        recallDtoHashtable.put(recallDto.getRecallNo(), recallDto);
    }

    public void addRecallDtoToHomeList(RecallDto recallDto){
        recallList.add(0,recallDto);
        saveRecallListToCache();
    }

    public void removeRecallDto(Long recallNo){
        recallDtoHashtable.remove(recallNo);
    }

    public RecallDto getRecallDto(Long recallNo){
        return recallDtoHashtable.get(recallNo);
    }

    public void addRecallDtoList(List<RecallDto> recallDtoList){
        // 默认进行存储，但在好友空间、个人空间的地方不进行存储
        addRecallDtoList(recallDtoList, true);
    }

    public void addRecallDtoList(List<RecallDto> recallDtoList, boolean isACache){
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
    public void addGood(GoodDto goodDto, Integer position){
        addGood(goodDto, position, true);
    }

    /**
     * 通过索引值来添加点赞 设置是否更新缓存
     */
    public void addGood(GoodDto goodDto, Integer position, boolean isACache){
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
    public void addGood(GoodDto goodDto, Long recallNo){
        addGood(goodDto, recallNo, true);
    }

    /**
     * 通过recallNo来添加点赞 设置是否更新缓存
     */
    public void addGood(GoodDto goodDto, Long recallNo, boolean isACache){
        recallDtoHashtable.get(recallNo).getGoodList().add(0, goodDto);
        if(isACache){
            saveRecallListToCache();
        }
    }

    /**
     * 通过索引值来删除点赞 默认更新缓存
     */
    public void removeGood(Integer position){
        removeGood(position, true);
    }

    /**
     * 通过索引值来删除点赞 设置是否更新缓存
     */
    public void removeGood(Integer position, boolean isACache){
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
    public void removeGood(Long recallNo){
        removeGood(recallNo, true);
    }

    /**
     * 通过recallNo来删除点赞 设置是否更新缓存
     */
    public void removeGood(Long recallNo, boolean isACache){
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

    public void saveRecallListToCache(){
        // 本地缓存只存储20条数据
        if(recallList.size() >= 20){
            ACache.getInstance().put(ConstantUtil.ACACHE_RECALL_LIST, GsonUtil.getInstance().toJson(recallList.subList(0,20)));
        }else{
            ACache.getInstance().put(ConstantUtil.ACACHE_RECALL_LIST, GsonUtil.getInstance().toJson(recallList.subList(0,recallList.size())));
        }
    }

    public void addCommentDtoByRecallNo(Long recallNo, CommentDto commentDto){
        addCommentDtoByRecallNo(recallNo, commentDto, true);
    }

    public void addCommentDtoByRecallNo(Long recallNo, CommentDto commentDto, boolean isACache){
        recallDtoHashtable.get(recallNo).getCommentList().add(commentDto);
        if(isACache){
            saveRecallListToCache();
        }
    }

    public void addResponDtoByRecallNo(Long recallNo, Long commentNo, ResponDto responDto){
        addResponDtoByRecallNo(recallNo, commentNo, responDto, true);
    }

    public void addResponDtoByRecallNo(Long recallNo, Long commentNo, ResponDto responDto, boolean isACache){
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

    public void removeCommentByRecalllNo(Long recallNo, Long commentNo){
        removeCommentByRecalllNo(recallNo, commentNo, true);
    }

    public void removeCommentByRecalllNo(Long recallNo, Long commentNo, boolean isACache){
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

    public void removeResponByRecallNo(Long recallNo, Long commentNo, Long responNo){
        removeResponByRecallNo(recallNo, commentNo, responNo, true);
    }

    public void removeResponByRecallNo(Long recallNo, Long commentNo, Long responNo, boolean isACache){
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

    public void clear(){
        recallList.clear();
        recallDtoHashtable.clear();
    }

}
