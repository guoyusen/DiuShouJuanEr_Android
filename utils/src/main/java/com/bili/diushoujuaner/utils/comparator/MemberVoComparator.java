package com.bili.diushoujuaner.utils.comparator;

import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.PinyinUtil;
import com.bili.diushoujuaner.utils.entity.vo.MemberVo;
import com.bili.diushoujuaner.utils.entity.vo.SortVo;

import java.util.Comparator;

public class MemberVoComparator implements Comparator<MemberVo> {

    @Override
    public int compare(MemberVo arg0, MemberVo arg1) {
        char capitalA = PinyinUtil.getHeadCapitalByChar(arg0.getMemberName().charAt(0));
        char capitalB = PinyinUtil.getHeadCapitalByChar(arg1.getMemberName().charAt(0));
        arg0.setSortLetter((capitalA >= 'A' && capitalA <= 'Z') ? (capitalA + "") : "#");
        arg1.setSortLetter((capitalB >= 'A' && capitalB <= 'Z') ? (capitalB + "") : "#");

        if(arg0.getType() == ConstantUtil.CONTACT_PARTY && arg1.getType() == ConstantUtil.CONTACT_FRIEND){
            arg0.setSortLetter("★");
            return -1;
        }else if(arg0.getType() == ConstantUtil.CONTACT_FRIEND && arg1.getType() == ConstantUtil.CONTACT_PARTY){
            arg1.setSortLetter("★");
            return 1;
        }else if((capitalA >= 'A' && capitalA <= 'Z') && (capitalB >= 'A' || capitalB <= 'Z')){
            if(capitalA > capitalB){
                return 1;
            }else if(capitalA == capitalB){
                return 0;
            }else {
                return -1;
            }
        }else if((capitalA < 'A' || capitalA > 'Z') && (capitalB >= 'A' || capitalB <= 'Z')){
            return 1;
        }else if((capitalA >= 'A' || capitalA <= 'Z') && (capitalB < 'A' || capitalB > 'Z')) {
            return -1;
        }else {
            return 0;
        }

    }

}
