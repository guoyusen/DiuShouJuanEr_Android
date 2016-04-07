package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by BiLi on 2016/4/2.
 */
public class CustomEditText extends EditText {

    private EmojiFilter emojiFilter;

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
        emojiFilter = new EmojiFilter();
        this.setFilters(new InputFilter[]{emojiFilter});
    }

    public void setMaxLength(int maxLength) {
        this.setFilters(new InputFilter[]{emojiFilter, new InputFilter.LengthFilter(maxLength)});
    }
}
