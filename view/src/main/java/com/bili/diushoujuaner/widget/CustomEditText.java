package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by BiLi on 2016/4/2.
 */
public class CustomEditText extends EditText {

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        this.setFilters(new InputFilter[]{new EmojiFilter()});
    }
}
