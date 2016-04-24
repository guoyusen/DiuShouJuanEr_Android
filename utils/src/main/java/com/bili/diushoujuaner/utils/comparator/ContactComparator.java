package com.bili.diushoujuaner.utils.comparator;

import java.util.Comparator;

import com.bili.diushoujuaner.utils.entity.vo.SortVo;

public class ContactComparator implements Comparator<SortVo> {

    @Override
    public int compare(SortVo arg0, SortVo arg1) {
        char capitalA = PinyinUtil.getHeadCapitalByChar(arg0.getDisplayName().charAt(0));
        char capitalB = PinyinUtil.getHeadCapitalByChar(arg1.getDisplayName().charAt(0));
        arg0.setSortLetter((capitalA >= 'A' && capitalA <= 'Z') ? (capitalA + "") : "#");
        arg1.setSortLetter((capitalB >= 'A' && capitalB <= 'Z') ? (capitalB + "") : "#");

        if((capitalA >= 'A' && capitalA <= 'Z') && (capitalB >= 'A' || capitalB <= 'Z')){
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
