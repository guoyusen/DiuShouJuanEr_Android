package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.SpaceActivity;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.actionhelper.action.UserInfoAction;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.model.eventhelper.RemoveRecallEvent;
import com.bili.diushoujuaner.model.eventhelper.GoodRecallEvent;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.model.tempHelper.GoodTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.utils.entity.dto.GoodDto;
import com.bili.diushoujuaner.utils.entity.dto.RecallDto;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by BiLi on 2016/3/5.
 */
public class RecallAdapter extends CommonAdapter<RecallDto> {

    private Drawable thumbUpDrawable, thumbDownDrawable, commentDrawable, frienddrawable, deleteDrawable;
    private int type, index;
    private long currentUserNo;

    public RecallAdapter(Context context, List<RecallDto> list, int type, int index){
        super(context, list, R.layout.item_main_recall);
        this.type = type;
        this.index = index;
        this.currentUserNo = CustomSessionPreference.getInstance().getCustomSession().getUserNo();
    }

    @Override
    public void convert(final ViewHolder holder, final RecallDto recallDto, final int position) throws Exception {
        if(recallDto != null){
            //头像展示和头像点击事件
            if(recallDto.getUserNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                Common.displayDraweeView(UserInfoAction.getInstance(context).getUserFromLocal().getPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));
            }else{
                Common.displayDraweeView(recallDto.getUserPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));
            }
            holder.getView(R.id.ivItemHead).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpaceActivity.showSpaceActivity(context,(IBaseView)context,recallDto.getUserNo());
                }
            });
            //显示展示的主图片
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
            //设置昵称
            String userName = ContactTemper.getFriendRemark(recallDto.getUserNo());
            if(userName != null){
                ((TextView) holder.getView(R.id.txtItemUserName)).setText(userName);
            }else{
                ((TextView) holder.getView(R.id.txtItemUserName)).setText(recallDto.getUserName());
            }
            //根据Adapter的类型设置右侧的图标
            if(type == Constant.RECALL_ADAPTER_SPACE){
                // 空间类型，根据是否是用户自己，来设置是否显示删除按钮，并设置删除点击事件
                holder.getView(R.id.ivItemFriend).setVisibility(View.GONE);
                if(recallDto.getUserNo() == this.currentUserNo){
                    holder.getView(R.id.layoutDelete).setVisibility(View.VISIBLE);
                    if(deleteDrawable == null){
                        deleteDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_delete,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
                    }
                    ((ImageView)holder.getView(R.id.ivItemDelete)).setImageDrawable(deleteDrawable);
                    holder.getView(R.id.ivItemDelete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new RemoveRecallEvent(index, position, recallDto.getRecallNo()));
                        }
                    });
                }else{
                    holder.getView(R.id.layoutDelete).setVisibility(View.GONE);
                }
            }else if(type == Constant.RECALL_ADAPTER_HOME){
                // 首页类型，根据该该用户是否是好友类型，来设置时候显示好友标签
                holder.getView(R.id.layoutDelete).setVisibility(View.GONE);
                if(userName != null){
                    if(frienddrawable == null){
                        frienddrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_friend,ContextCompat.getColor(context, R.color.COLOR_BFBFBF));
                    }
                    holder.getView(R.id.ivItemFriend).setVisibility(View.VISIBLE);
                    ((ImageView)holder.getView(R.id.ivItemFriend)).setImageDrawable(frienddrawable);
                }else{
                    holder.getView(R.id.ivItemFriend).setVisibility(View.GONE);
                }
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
                    EventBus.getDefault().post(new GoodRecallEvent(index, position, type));
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

    private void  setGoodStatus(ViewHolder holder, boolean goodStatus){
        if(goodStatus){
            if(thumbUpDrawable == null){
                thumbUpDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_good,ContextCompat.getColor(context, R.color.COLOR_THEME_SUB));
            }
            ((ImageView)holder.getView(R.id.ivItemGood)).setImageDrawable(thumbUpDrawable);
            ((TextView)holder.getView(R.id.txtGoodCount)).setTextColor(ContextCompat.getColor(context, R.color.TC_388ECD));
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
