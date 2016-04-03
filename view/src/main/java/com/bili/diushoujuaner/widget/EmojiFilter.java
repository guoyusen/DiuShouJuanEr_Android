package com.bili.diushoujuaner.widget;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by BiLi on 2016/4/2.
 */
public class EmojiFilter implements InputFilter {

    private static Set<String> filterSet = null;

    private static void addUnicodeRangeToSet(Set<String> set, int start, int end) {
        if (set == null) {
            return;
        }
        if (start > end) {
            return;
        }

        for (int i = start; i <= end; i++) {
            filterSet.add(new String(new int[] {
                    i
            }, 0, 1));
        }
    }

    static {
        filterSet = new HashSet<>();

        // 1F601 - 1F64F
        addUnicodeRangeToSet(filterSet, 0x1F601, 0X1F64F);

        // 1F680 - 1F6C0
        addUnicodeRangeToSet(filterSet, 0X1F680, 0X1F6C0);

        // 1F600 - 1F636
        addUnicodeRangeToSet(filterSet, 0X1F600, 0X1F636);

        // 1F681 - 1F6C5
        addUnicodeRangeToSet(filterSet, 0X1F681, 0X1F6C5);

        // 1F30D - 1F567
        addUnicodeRangeToSet(filterSet, 0X1F30D, 0X1F567);

    }

    public EmojiFilter() {
        super();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        if (filterSet.contains(source.toString())) {
            return "";
        }
        return source;
    }

}
