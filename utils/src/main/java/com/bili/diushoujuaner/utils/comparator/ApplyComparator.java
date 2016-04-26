package com.bili.diushoujuaner.utils.comparator;

import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.ApplyVo;

import java.util.Comparator;

/**
 * Created by BiLi on 2016/4/24.
 */
public class ApplyComparator implements Comparator<ApplyVo> {

    @Override
    public int compare(ApplyVo arg0, ApplyVo arg1) {
        // 1 -> 2,3,4  r -1
        // 2 -> 1  r 1
        // 2 -> 3,4  r -1
        // 3 -> 1,2  r 1
        // 3 -> 4  r -1
        // 4 -> 1,2,3  r 1
        if(((!arg0.getAccept() && arg0.getType() == ConstantUtil.CHAT_FRIEND_APPLY) && (!arg1.getAccept() && arg1.getType() == ConstantUtil.CHAT_PARTY_APPLY))
                || ((!arg0.getAccept() && arg0.getType() == ConstantUtil.CHAT_FRIEND_APPLY) && arg1.getAccept())
                || ((!arg0.getAccept() && arg0.getType() == ConstantUtil.CHAT_FRIEND_APPLY) && (arg1.getType() == ConstantUtil.CHAT_FRIEND_RECOMMEND))){
            return -1;
        }else if((!arg0.getAccept() && arg0.getType() == ConstantUtil.CHAT_PARTY_APPLY) && (!arg1.getAccept() && arg1.getType() == ConstantUtil.CHAT_FRIEND_APPLY)){
            return 1;
        }else if(((!arg0.getAccept() && arg0.getType() == ConstantUtil.CHAT_PARTY_APPLY) && arg1.getAccept())
                || ((!arg0.getAccept() && arg0.getType() == ConstantUtil.CHAT_PARTY_APPLY) && (arg1.getType() == ConstantUtil.CHAT_FRIEND_RECOMMEND))){
            return -1;
        }else if((arg0.getAccept() && (!arg1.getAccept() && arg1.getType() == ConstantUtil.CHAT_FRIEND_APPLY))
                || (arg0.getAccept() && (!arg1.getAccept() && arg1.getType() == ConstantUtil.CHAT_PARTY_APPLY))){
            return 1;
        }else if(arg0.getAccept() && arg1.getType() == ConstantUtil.CHAT_FRIEND_RECOMMEND){
            return -1;
        }else if((arg0.getType() == ConstantUtil.CHAT_FRIEND_RECOMMEND && (!arg1.getAccept() && arg1.getType() == ConstantUtil.CHAT_FRIEND_APPLY))
                || (arg0.getType() == ConstantUtil.CHAT_FRIEND_RECOMMEND && (!arg1.getAccept() && arg1.getType() == ConstantUtil.CHAT_PARTY_APPLY))
                || (arg0.getType() == ConstantUtil.CHAT_FRIEND_RECOMMEND && arg1.getAccept())){
            return 1;
        }else if(arg0.getAccept() && arg1.getAccept()){
            // 对于已添加的情况，需要分好友和群
            if(arg0.getType() == ConstantUtil.CHAT_PARTY_APPLY && arg1.getType() == ConstantUtil.CHAT_FRIEND_APPLY){
                return -1;
            }else if(arg0.getType() == ConstantUtil.CHAT_FRIEND_APPLY && arg1.getType() == ConstantUtil.CHAT_PARTY_APPLY){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }
}
