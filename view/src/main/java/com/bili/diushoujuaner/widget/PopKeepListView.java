package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by BiLi on 2016/3/24.
 */
public class PopKeepListView extends ListView {

    public PopKeepListView(Context context) {
        super(context);
    }

    public PopKeepListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopKeepListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setSelection(getCount() - 1);
    }
}
