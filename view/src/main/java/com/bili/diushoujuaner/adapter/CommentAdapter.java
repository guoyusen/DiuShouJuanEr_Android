package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.Imageloader;
import com.bili.diushoujuaner.utils.response.CommentDto;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by BiLi on 2016/3/23.
 */
public class CommentAdapter extends CommonAdapter<CommentDto> {

    private Hashtable<Long, ResponAdapter> responAdapterHashtable = new Hashtable<>();

    public CommentAdapter(Context context, List<CommentDto> list){
        super(context, list, R.layout.item_comment);
    }

    @Override
    public void convert(ViewHolder holder, CommentDto commentDto, int position) throws Exception {
        if(commentDto != null){
            Imageloader.getInstance().displayDraweeView(commentDto.getFromPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));

            String userName = ContactTemper.getFriendRemark(commentDto.getFromNo());
            if(userName != null){
                ((TextView) holder.getView(R.id.itemCommentUserName)).setText(userName);
            }else{
                ((TextView) holder.getView(R.id.itemCommentUserName)).setText(commentDto.getNickName());
            }
            ((TextView) holder.getView(R.id.itemCommentTime)).setText(Common.getFormatTime(commentDto.getAddTime()));
            ((TextView) holder.getView(R.id.txtCommentContent)).setText(commentDto.getContent());
            if(responAdapterHashtable.get(commentDto.getCommentNo()) == null){
                ResponAdapter responAdapter = new ResponAdapter(context, commentDto.getResponList());
                responAdapterHashtable.put(commentDto.getCommentNo(), responAdapter);
            }
            ((ListView)holder.getView(R.id.listViewRespon)).setAdapter(responAdapterHashtable.get(commentDto.getCommentNo()));
            responAdapterHashtable.get(commentDto.getCommentNo()).notifyDataSetChanged();
        }
    }
}
