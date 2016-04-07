package com.bili.diushoujuaner.widget.wheel;

/**
 * WheelView 滚动监听器
 */
public interface OnWheelScrollListener {
    /**
     * 在 WheelView 滚动开始的时候回调该接口
     * 
     * @param wheel
     *            开始滚动的 WheelView 对象
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * 在 WheelView 滚动结束的时候回调该接口
     * 
     * @param wheel
     *            结束滚动的 WheelView 对象
     */
    void onScrollingFinished(WheelView wheel);
}
