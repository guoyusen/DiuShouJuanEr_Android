package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.event.RecallGoodEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.response.GoodDto;
import com.bili.diushoujuaner.utils.response.RecallDto;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/3/5.
 */
public class RecallAdapter extends CommonAdapter<RecallDto> {

    private Drawable thumbUpDrawable, thumbDownDrawable, commentDrawable, frienddrawable;

    public RecallAdapter(Context context, List<RecallDto> list){
        super(context, list, R.layout.item_main_recall);
    }

    @Override
    public void convert(final ViewHolder holder, RecallDto recallDto, final int position) throws Exception {
        if(recallDto != null){
            Common.displayDraweeView(recallDto.getUserPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));
            if(recallDto.getPictureList().size() > 0){
                holder.getView(R.id.layoutItemPic).setVisibility(View.VISIBLE);
                Common.displayDraweeView(recallDto.getPictureList().get(0).getPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemPic));
                if(recallDto.getPictureList().size() > 1){
                    holder.getView(R.id.txtItemPicCount).setVisibility(View.VISIBLE);
                    ((TextView)holder.getView(R.id.txtItemPicCount)).setText(recallDto.getPictureList().size() + "");
                }else{
                    holder.getView(R.id.txtItemPicCount).setVisibility(View.GONE);
                }
            }else{
                holder.getView(R.id.layoutItemPic).setVisibility(View.GONE);
            }
            //设置好友标志和昵称
            String userName = ContactTemper.getFriendRemark(recallDto.getUserNo());
            if(userName != null){
                ((TextView) holder.getView(R.id.txtItemUserName)).setText(userName);
                if(frienddrawable == null){
                    frienddrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_friend,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
                }
                ((ImageView)holder.getView(R.id.ivItemFriend)).setVisibility(View.VISIBLE);
                ((ImageView)holder.getView(R.id.ivItemFriend)).setImageDrawable(frienddrawable);
            }else{
                ((TextView) holder.getView(R.id.txtItemUserName)).setText(recallDto.getUserName());
                holder.getView(R.id.ivItemFriend).setVisibility(View.GONE);
            }
            // 设置评论图标
            if(commentDrawable == null){
                commentDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_comment,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
            }
            ((ImageView)holder.getView(R.id.ivItemComment)).setImageDrawable(commentDrawable);

            ((TextView)holder.getView(R.id.tetItemPublishTime)).setText(Common.getFormatTime(recallDto.getPublishTime()));
            ((TextView)holder.getView(R.id.tetItemContent)).setText(recallDto.getContent());
            ((TextView)holder.getView(R.id.txtCommentCount)).setText(recallDto.getCommentList().size() + "");
            ((TextView)holder.getView(R.id.txtGoodCount)).setText(recallDto.getGoodList().size() + "");

            getGoodStatus(holder, recallDto);

            holder.getView(R.id.layoutItemGood).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new RecallGoodEvent(position));
                }
            });
        }
    }

    private void getGoodStatus(ViewHolder holder, RecallDto recallDto){
        if(GoodTemper.isExists(recallDto.getRecallNo())){
            if(GoodTemper.getGoodStatus(recallDto.getRecallNo())){
                setGoodStatus(holder, true);
            }else{
                setGoodStatus(holder, false);
            }
        }else{
            if(getGoodStatusFromRecallDto(recallDto)){
                GoodTemper.setGoodStatus(recallDto.getRecallNo(), true);
                setGoodStatus(holder, true);
            }else{
                GoodTemper.setGoodStatus(recallDto.getRecallNo(), false);
                setGoodStatus(holder, false);
            }
        }
    }

    private void setGoodStatus(ViewHolder holder, boolean goodStatus){
        if(goodStatus){
            if(thumbUpDrawable == null){
                thumbUpDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_good,ContextCompat.getColor(context, R.color.COLOR_THEME));
            }
            ((ImageView)holder.getView(R.id.ivItemGood)).setImageDrawable(thumbUpDrawable);
            ((TextView)holder.getView(R.id.txtGoodCount)).setTextColor(ContextCompat.getColor(context, R.color.TC_12B7F5));
        }else{
            if(thumbDownDrawable == null){
                thumbDownDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_good,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
            }
            ((ImageView)holder.getView(R.id.ivItemGood)).setImageDrawable(thumbDownDrawable);
            ((TextView)holder.getView(R.id.txtGoodCount)).setTextColor(ContextCompat.getColor(context, R.color.TC_BFBFBF));
        }
    }

    private boolean getGoodStatusFromRecallDto(RecallDto recallDto){
        boolean goodStatus = false;
        for(GoodDto goodDto : recallDto.getGoodList()){
            if(goodDto.getUserNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                goodStatus = true;
                break;
            }
        }
        return goodStatus;
    }
}
