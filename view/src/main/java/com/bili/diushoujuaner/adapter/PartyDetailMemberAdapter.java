package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.net.Uri;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.utils.CommonUtil;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.entity.vo.MemberPicVo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by BiLi on 2016/4/22.
 */
public class PartyDetailMemberAdapter extends CommonAdapter<MemberPicVo> {

    public PartyDetailMemberAdapter(Context context, List<MemberPicVo> list) {
        super(context, list, R.layout.item_party_detail_member);
    }

    @Override
    public void convert(ViewHolder holder, MemberPicVo memberPicVo, int position) throws Exception {
        if(memberPicVo != null){
            if(memberPicVo.getType() == ConstantUtil.MEMBER_HEAD_SERVER){
                CommonUtil.displayDraweeView(memberPicVo.getPath(), (SimpleDraweeView)holder.getView(R.id.ivMember));
            }else if(memberPicVo.getType() == ConstantUtil.MEMBER_HEAD_LOCAL ){
                ((SimpleDraweeView)holder.getView(R.id.ivMember)).setImageURI(Uri.parse("res://com.bili.diushoujuaner/" + memberPicVo.getResourceId()));
            }
        }
    }
}
