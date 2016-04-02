package com.bili.diushoujuaner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.adapter.viewholder.ViewHolder;
import com.bili.diushoujuaner.model.tempHelper.ContactTemper;
import com.bili.diushoujuaner.utils.response.ResponDto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/24.
 */
public class ResponAdapter extends CommonAdapter<ResponDto> {

    public ResponAdapter(Context context, List<ResponDto> list){
        super(context, list, R.layout.item_respon);
    }

    @Override
    public void convert(ViewHolder holder, ResponDto responDto, int position) throws Exception {
        if(responDto != null){
            ((TextView)holder.getView(R.id.txtRespon)).setText(getSpannableString(responDto));
            holder.getView(R.id.txtRespon).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.BG_ECECEC));
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_WHITE));
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.COLOR_WHITE));
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private SpannableString getSpannableString(ResponDto responDto){
        StringBuilder stringBuilder = new StringBuilder();
        String userFromName = ContactTemper.getFriendRemark(responDto.getFromNo());
        String userToName = ContactTemper.getFriendRemark(responDto.getToNo());

        stringBuilder.append(userFromName != null ? userFromName : responDto.getNickNameFrom());
        stringBuilder.append(" 回复 ");
        stringBuilder.append(userToName != null ? userToName : responDto.getNickNameTo());
        stringBuilder.append(":" + responDto.getContent());

        SpannableString responSpan = new SpannableString(stringBuilder.toString());
        int start = 0;
        int end = 0;
        end += userFromName != null ? userFromName.length() : responDto.getNickNameFrom().length();
        responSpan.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.TC_388ECD)),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end;
        end += " 回复 ".length();
        responSpan.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.TC_040404)),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end;
        end += userToName != null ? userToName.length() : responDto.getNickNameTo().length();
        responSpan.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.TC_388ECD)),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end;
        end += responDto.getContent().length() + 1;
        responSpan.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, R.color.TC_040404)),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return responSpan;
    }
}
