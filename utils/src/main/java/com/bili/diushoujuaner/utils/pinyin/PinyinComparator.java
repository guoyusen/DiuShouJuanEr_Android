package com.bili.diushoujuaner.utils.pinyin;

import java.util.Comparator;

import com.bili.diushoujuaner.utils.entity.FriendVo;

public class PinyinComparator implements Comparator<FriendVo> {

	@Override
	public int compare(FriendVo arg0, FriendVo arg1) {
			char capitalA = PinyinUtil.getHeadCapitalByChar(arg0.getRemark().charAt(0));
			char capitalB = PinyinUtil.getHeadCapitalByChar(arg1.getRemark().charAt(0));
			arg0.setSortLetter(capitalA+"");
			arg1.setSortLetter(capitalB+"");
			if((capitalA < 'A' || capitalA > 'Z') && (capitalB < 'A' || capitalB > 'Z')){
				return 0;
			}
			else if((capitalA < 'A' || capitalA > 'Z') && (capitalB >= 'A' && capitalB <= 'Z')){
				return 1;
			}
			else if((capitalA >= 'A' && capitalA <= 'Z') && (capitalB < 'A' || capitalB > 'Z')){
				return -1;
			}
			else if(capitalA >= capitalB){
				return 1;
			}
			else{
				return -1;
			}
	}

}
