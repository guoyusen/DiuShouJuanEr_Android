package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.activity.SpaceActivity;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.action.impl.UserInfoAction;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.presenter.base.IBaseView;
import com.bili.diushoujuaner.utils.event.ResponEvent;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.Common;
import com.bili.diushoujuaner.model.apihelper.response.CommentDto;
import com.bili.diushoujuaner.utils.Constant;
import com.bili.diushoujuaner.widget.TintedBitmapDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by BiLi on 2016/3/23.
 */
public class CommentAdapter extends CommonAdapter<CommentDto> {

    private Hashtable<Long, ResponAdapter> responAdapterHashtable = new Hashtable<>();
    private Drawable commentFocusDrawable;

    public CommentAdapter(Context context, List<CommentDto> list){
        super(context, list, R.layout.item_comment);
    }

    @Override
    public void convert(ViewHolder holder, final CommentDto commentDto, int position) throws Exception {
        if(commentDto != null){
            if(commentFocusDrawable == null){
                commentFocusDrawable = new TintedBitmapDrawable(context.getResources(),R.mipmap.icon_comment,ContextCompat.getColor(context, R.color.COLOR_THEME_SUB));
            }
            ((ImageView)holder.getView(R.id.ivComment)).setImageDrawable(commentFocusDrawable);

            if(commentDto.getFromNo() == CustomSessionPreference.getInstance().getCustomSession().getUserNo()){
                Common.displayDraweeView(UserInfoAction.getInstance(context).getUserFromLocal().getPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));
            }else{
                Common.displayDraweeView(commentDto.getFromPicPath(), (SimpleDraweeView) holder.getView(R.id.ivItemHead));
            }
            holder.getView(R.id.ivItemHead).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpaceActivity.showSpaceActivity(context,(IBaseView)context,commentDto.getFromNo());
                }
            });

            String userName = ContactTemper.getFriendRemark(commentDto.getFromNo());
            if(userName != null){
                ((TextView) holder.getView(R.id.itemCommentUserName)).setText(userName);
            }else{
                ((TextView) holder.getView(R.id.itemCommentUserName)).setText(commentDto.getNickName());
            }
            holder.getView(R.id.itemCommentUserName).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpaceActivity.showSpaceActivity(context,(IBaseView)context,commentDto.getFromNo());
                }
            });

            ((TextView) holder.getView(R.id.itemCommentTime)).setText(Common.getFormatTime(commentDto.getAddTime()));
            ((TextView) holder.getView(R.id.txtCommentContent)).setText(commentDto.getContent());
            holder.getView(R.id.txtCommentContent).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.BG_ECECEC));
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_WHITE));
                            EventBus.getDefault().post(ResponEvent.getResponEvent(commentDto.getCommentNo(), null, commentDto.getFromNo(), Constant.COMMENT_CLICK_COMMENT_CONTENT, commentDto.getNickName()));
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_WHITE));
                            break;
                    }
                    return true;
                }
            });
            holder.getView(R.id.layoutRespon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(ResponEvent.getResponEvent(commentDto.getCommentNo(), null, commentDto.getFromNo(), Constant.COMMENT_CLICK_LAYOUT_RESPON, commentDto.getNickName()));
                }
            });
            if(responAdapterHashtable.get(commentDto.getCommentNo()) == null){
                ResponAdapter responAdapter = new ResponAdapter(context, commentDto.getResponList());
                responAdapterHashtable.put(commentDto.getCommentNo(), responAdapter);
            }
            ((ListView)holder.getView(R.id.listViewRespon)).setAdapter(responAdapterHashtable.get(commentDto.getCommentNo()));
            responAdapterHashtable.get(commentDto.getCommentNo()).notifyDataSetChanged();
        }
    }


}
