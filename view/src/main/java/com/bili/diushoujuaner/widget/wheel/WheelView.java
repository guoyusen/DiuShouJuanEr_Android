package com.bili.diushoujuaner.widget.wheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.bili.diushoujuaner.R;

import java.util.LinkedList;
import java.util.List;


/**
 * WheelView 主对象
 */
public class WheelView extends View {
    /** 滚动花费时间 Scrolling duration */
    private static final int SCROLLING_DURATION = 400;

    /** 最小的滚动值, 每次最少滚动一个单位 */
    private static final int MIN_DELTA_FOR_SCROLLING = 1;

    /** 当前条目中的文字颜色 */
    private static final int VALUE_TEXT_COLOR = 0xBB040404;

    /** 非当前条目的文字颜色 */
    private static final int ITEMS_TEXT_COLOR = 0xFF000000;

    /** 顶部和底部的阴影颜色 */
    //private static final int[] SHADOWS_COLORS = new int[] { 0xFF5436EE, 0x0012CEAE, 0x0012CEAE };
    private static final int[] SHADOWS_COLORS = new int[] { 0xffffffff, 0x96ffffff, 0x00ffffff };

    /** 额外的条目高度 Additional items height (is added to standard text item height) */
    private static final int ADDITIONAL_ITEM_HEIGHT = 35;

    /** 字体大小 */
    private static final int TEXT_SIZE = 35;

    /** 顶部 和 底部 条目的隐藏大小, 
     * 如果是正数 会隐藏一部份, 
     * 0 顶部 和 底部的字正好紧贴 边缘, 
     * 负数时 顶部和底部 与 字有一定间距 */
    private static final int ITEM_OFFSET = -15;

    /** Additional width for items layout */
    private static final int ADDITIONAL_ITEMS_SPACE = 30;

    /** Label offset */
    private static final int LABEL_OFFSET = 8;

    /** Left and right padding value */
    private static final int PADDING = 10;

    /** 默认的可显示的条目数 */
    private static final int DEF_VISIBLE_ITEMS = 5;

    /** WheelView 适配器 */
    private WheelAdapter adapter = null;
    /** 当前显示的条目索引 */
    private int currentItem = 0;

    /** 条目宽度 */
    private int itemsWidth = 0;
    /** 标签宽度 */
    private int labelWidth = 0;

    /** 可见的条目数 */
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /** 条目高度 */
    private int itemHeight = 0;

    /** 绘制普通条目画笔 */
    private TextPaint itemsPaint;
    /** 绘制选中条目画笔 */
    private TextPaint valuePaint;

    /** 普通条目布局
     * StaticLayout 布局用于控制 TextView 组件, 一般情况下不会直接使用该组件, 
     * 除非你自定义一个组件 或者 想要直接调用  Canvas.drawText() 方法
     *  */
    private StaticLayout itemsLayout;
    private StaticLayout labelLayout;
    /** 选中条目布局 */
    private StaticLayout valueLayout;

    /** 标签 在选中条目的右边出现 */
    private String label;
    /** 选中条目的背景图片 */
    private Drawable centerDrawable;

    /** 顶部阴影图片 */
    private GradientDrawable topShadow;
    /** 底部阴影图片 */
    private GradientDrawable bottomShadow;

    /** 是否在滚动 */
    private boolean isScrollingPerformed;
    /** 滚动的位置 */
    private int scrollingOffset;

    /** 手势检测器 */
    private GestureDetector gestureDetector;
    /** 
     * Scroll 类封装了滚动动作. 
     * 开发者可以使用 Scroll 或者 Scroll 实现类 去收集产生一个滚动动画所需要的数据, 返回一个急冲滑动的手势.
     * 该对象可以追踪随着时间推移滚动的偏移量, 但是这些对象不会自动向 View 对象提供这些位置.
     * 如果想要使滚动动画看起来比较平滑, 开发者需要在适当的时机  获取 和 使用新的坐标; 
     *  */
    private Scroller scroller;
    /** 之前所在的 y 轴位置 */
    private int lastScrollY;

    /** 是否循环 */
    boolean isCyclic = false;

    /** 条目改变监听器集合  封装了条目改变方法, 当条目改变时回调 */
    private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
    /** 条目滚动监听器集合, 该监听器封装了 开始滚动方法, 结束滚动方法 */
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();

    /**
     * 构造方法
     */
    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    /**
     * 构造方法
     */
    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    /**
     * 构造方法
     */
    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    /**
     * 初始化数据
     * 
     * @param context
     *            上下文对象
     */
    private void initData(Context context) {
    	//创建一个手势处理
        gestureDetector = new GestureDetector(context, gestureListener);
        /*
         * 是否允许长按操作, 
         * 如果设置为 true 用户按下不松开, 会返回一个长按事件, 
         * 如果设置为 false, 按下不松开滑动的话 会收到滚动事件.
         */
        gestureDetector.setIsLongpressEnabled(false);
        
        //使用默认的 时间 和 插入器 创建一个滚动器
        scroller = new Scroller(context);
    }

    /**
     * 获取该 WheelView 的适配器
     * 
     * @return 
     * 		返回适配器
     */
    public WheelAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置适配器
     * 
     * @param adapter
     *            要设置的适配器
     */
    public void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        invalidateLayouts();
        invalidate();
    }

    /**
     * 设置 Scroll 的插入器
     * 
     * @param interpolator
     *            the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
    	//强制停止滚动
        scroller.forceFinished(true);
        //创建一个 Scroll 对象
        scroller = new Scroller(getContext(), interpolator);
    }

    /**
     * 获取课件条目数
     * 
     * @return the count of visible items
     */
    public int getVisibleItems() {
        return visibleItems;
    }

    /**
     * 设置可见条目数
     * 
     * @param count
     *            the new count
     */
    public void setVisibleItems(int count) {
        visibleItems = count;
        invalidate();
    }

    /**
     * 获取标签
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置标签
     * 
     * @param newLabel
     *            the label to set
     */
    public void setLabel(String newLabel) {
        if (label == null || !label.equals(newLabel)) {
            label = newLabel;
            labelLayout = null;
            invalidate();
        }
    }

    /**
     * 添加 WheelView 选择的元素改变监听器
     * 
     * @param listener
     *            the listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    /**
     * 移除 WheelView 元素改变监听器
     * 
     * @param listener
     *            the listener
     */
    public void removeChangingListener(OnWheelChangedListener listener) {
        changingListeners.remove(listener);
    }

    /**
     * 回调元素改变监听器集合的元素改变监听器元素的元素改变方法
     * 
     * @param oldValue
     *            旧的 WheelView选中的值
     * @param newValue
     *            新的 WheelView选中的值
     */
    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    /**
     * 添加 WheelView 滚动监听器
     * 
     * @param listener
     *            the listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.add(listener);
    }

    /**
     * 移除 WheelView 滚动监听器
     * 
     * @param listener
     *            the listener
     */
    public void removeScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.remove(listener);
    }

    /**
     * 通知监听器开始滚动
     */
    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
        	//回调开始滚动方法
            listener.onScrollingStarted(this);
        }
    }

    /**
     * 通知监听器结束滚动
     */
    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
        	//回调滚动结束方法
            listener.onScrollingFinished(this);
        }
    }

    /**
     * 获取当前选中元素的索引
     * 
     * @return 
     * 		当前元素索引
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * 设置当前元素的位置, 如果索引是错误的 不进行任何操作
     * -- 需要考虑该 WheelView 是否能循环
     * -- 根据是否需要滚动动画来确定是 ①滚动到目的位置 还是 ②晴空所有条目然后重绘
     * 
     * @param index
     *            要设置的元素索引值
     * @param animated
     *            动画标志位
     */
    public void setCurrentItem(int index, boolean animated) {
    	//如果没有适配器或者元素个数为0 直接返回
        if (adapter == null || adapter.getItemsCount() == 0) {
            return; // throw?
        }
        //目标索引小于 0 或者大于 元素索引最大值(个数 -1)
        if (index < 0 || index >= adapter.getItemsCount()) {
        	//入股WheelView 可循环, 修正索引值, 如果不可循环直接返回
            if (isCyclic) {
                while (index < 0) {
                    index += adapter.getItemsCount();
                }
                index %= adapter.getItemsCount();
            } else {
                return; // throw?
            }
        }
        
        //如果当前的索引不是传入的 索引
        if (index != currentItem) {
        	
        	/*
        	 * 如果需要动画, 就滚动到目标位置
        	 * 如果不需要动画, 重新设置布局
        	 */
            if (animated) {
            	/*
            	 * 开始滚动, 每个元素滚动间隔 400 ms, 滚动次数是 目标索引值 减去 当前索引值, 这是滚动的真实方法
            	 */
                scroll(index - currentItem, SCROLLING_DURATION);
            } else {
            	//所有布局设置为 null, 滚动位置设置为 0
                invalidateLayouts();

                int old = currentItem;
                currentItem = index;

                //便利回调元素改变监听器集合中的监听器元素中的元素改变方法
                notifyChangingListeners(old, currentItem);

                //重绘
                invalidate();
            }
        }
    }

    /**
     * 设置当前选中的条目, 没有动画, 当索引出错不做任何操作
     * 
     * @param index
     *            要设置的索引
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    /**
     * 获取 WheelView 是否可以循环
     * -- 如果可循环 : 第一个之前是最后一个, 最后一个之后是第一个;
     * -- 如果不可循环 : 到第一个就不能上翻, 最后一个不能下翻 
     * 
     * @return
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    /**
     * 设置 WheelView 循环标志
     * 
     * @param isCyclic
     *            the flag to set
     */
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;

        invalidate();
        invalidateLayouts();
    }

    /**
     * 使布局无效
     * 将 选中条目 和 普通条目设置为 null, 滚动位置设置为0
     */
    private void invalidateLayouts() {
        itemsLayout = null;
        valueLayout = null;
        scrollingOffset = 0;
    }

    /**
     * 初始化资源
     */
    private void initResourcesIfNecessary() {
    	/*
    	 * 设置绘制普通条目的画笔, 允许抗拒齿, 允许 fake-bold
    	 * 设置文字大小为 24
    	 */
        if (itemsPaint == null) {
            itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            itemsPaint.setTextSize(TEXT_SIZE);
        }

        /*
         * 设置绘制选中条目的画笔
         * 设置文字大小 24
         */
        if (valuePaint == null) {
            valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);
            valuePaint.setTextSize(40);
            valuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
        }

        //选中的条目背景
        if (centerDrawable == null) {
            centerDrawable = ContextCompat.getDrawable(getContext(),R.drawable.wheel_val);
        }

        //创建顶部阴影图片
        if (topShadow == null) {
        	/*
        	 * 构造方法中传入颜色渐变方向
        	 * 阴影颜色
        	 */
            topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }

        //创建底部阴影图片
        if (bottomShadow == null) {
            bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }

        setBackgroundResource(R.drawable.wheel_bg);

    }

    /**
     * 计算布局期望的高度
     * 
     * @param layout
     *      组件的布局的
     * @return 
     * 		布局需要的高度
     */
    private int getDesiredHeight(Layout layout) {
        if (layout == null) {
            return 0;
        }

        /*
         * 布局需要的高度是 条目个数 * 可见条目数 减去 顶部和底部隐藏的一部份 减去 额外的条目高度
         */
        int desired = getItemHeight() * visibleItems - ITEM_OFFSET * 2 - ADDITIONAL_ITEM_HEIGHT;

        // 将计算的布局高度 与 最小高度比较, 取最大值
        desired = Math.max(desired, getSuggestedMinimumHeight());

        return desired;
    }

    /**
     * 根据条目获取字符串
     * 
     * @param index
     *            条目索引
     * @return 
     * 		条目显示的字符串
     */
    private String getTextItem(int index) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return null;
        }
        //适配器显示的字符串个数
        int count = adapter.getItemsCount();
        
        //考虑 index 小于 0 的情况
        if ((index < 0 || index >= count) && !isCyclic) {
            return null;
        } else {
            while (index < 0) {
                index = count + index;
            }
        }

        //index 大于 0
        index %= count;
        return adapter.getItem(index);
    }

    /**
     * 根据当前值创建 字符串
     * 
     * @param useCurrentValue
     * 		是否在滚动
     * @return the text
     * 		生成的字符串
     */
    private String buildText(boolean useCurrentValue) {
    	//创建字符串容器
        StringBuilder itemsText = new StringBuilder();
        //计算出显示的条目相对位置, 例如显示 5个, 第 3 个是正中见选中的布局
        int addItems = visibleItems / 2 + 1;

        /*
         * 遍历显示的条目
         * 获取当前显示条目 上下 各 addItems 个文本, 将该文本添加到显示文本中去
         * 如果不是最后一个 都加上回车
         */
        for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
        	//如果在滚动
            if (useCurrentValue || i != currentItem) {
                String text = getTextItem(i);
                if (text != null) {
                    itemsText.append(text);
                }
            }
            if (i < currentItem + addItems) {
                itemsText.append("\n");
            }
        }

        return itemsText.toString();
    }

    /**
     * 返回 条目的字符串
     * 
     * @return 
     * 		条目最大宽度
     */
    private int getMaxTextLength() {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return 0;
        }

        String maxText = null;
        int addItems = visibleItems / 2;
        /*
         * 遍历当前显示的条目, 获取字符串长度最长的那个, 返回这个最长的字符串长度
         */
        for (int i = Math.max(currentItem - addItems, 0); i < Math.min(currentItem + visibleItems,
                adapter.getItemsCount()); i++) {
            String text = adapter.getItem(i);
            if (text != null && (maxText == null || maxText.length() < text.length())) {
                maxText = text;
            }
        }

        return maxText != null ? maxText.length() : 0;
    }

    /**
     * 获取每个条目的高度
     * 
     * @return 
     * 		条目的高度
     */
    private int getItemHeight() {
    	//如果条目高度不为 0, 直接返回
        if (itemHeight != 0) {
            return itemHeight;
        //如果条目的高度为 0, 并且普通条目布局不为null, 条目个数大于 2 
        } else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
        	/*
        	 * itemsLayout.getLineTop(2) : 获取顶部第二行上面的垂直(y轴)位置, 如果行数等于
        	 */
            itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
            return itemHeight;
        }

        //如果上面都不符合, 使用整体高度处以 显示条目数
        return getHeight() / visibleItems;
    }

    /**
     * 计算宽度并创建文字布局
     * 
     * @param widthSize
     *            输入的布局宽度
     * @param mode
     *            布局模式
     * @return 
     * 		计算的宽度
     */
    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();

        int width = widthSize;

        //获取最长的条目显示字符串字符个数
        int maxLength = getMaxTextLength();
        
        if (maxLength > 0) {
        	/*
        	 * 使用方法 FloatMath.ceil() 方法有以下警告
        	 * Use java.lang.Math#ceil instead of android.util.FloatMath#ceil() since it is faster as of API 8
        	 */
            //float textWidth = FloatMath.ceil(Layout.getDesiredWidth("0", itemsPaint));
        	//向上取整  计算一个字符串宽度
        	float textWidth = (float) Math.ceil(Layout.getDesiredWidth("正", itemsPaint));
        	
        	//获取字符串总的宽度
            itemsWidth = (int) (maxLength * textWidth);
        } else {
            itemsWidth = 0;
        }
        
        //总宽度加上一些间距
        itemsWidth += ADDITIONAL_ITEMS_SPACE; // make it some more

        //计算 label 的长度
        labelWidth = 0;
        if (label != null && label.length() > 0) {
        	labelWidth = (int) Math.ceil(Layout.getDesiredWidth(label, valuePaint));
            //labelWidth = (int) FloatMath.ceil(Layout.getDesiredWidth(label, valuePaint));
        }

        boolean recalculate = false;
        //精准模式
        if (mode == MeasureSpec.EXACTLY) {
        	//精准模式下, 宽度就是给定的宽度
            width = widthSize;
            recalculate = true;
        } else {
        	//未定义模式
            width = itemsWidth + labelWidth + 2 * PADDING;
            if (labelWidth > 0) {
                width += LABEL_OFFSET;
            }

            // 获取 ( 计算出来的宽度 与 最小宽度的 ) 最大值
            width = Math.max(width, getSuggestedMinimumWidth());

            //最大模式 如果 给定的宽度 小于 计算出来的宽度, 那么使用最小的宽度 ( 给定宽度 | 计算出来的宽度 )
            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
                recalculate = true;
            }
        }

        /*
         * 重新计算宽度 , 如果宽度是给定的宽度, 不是我们计算出来的宽度, 需要重新进行计算
         * 重新计算的宽度是用于
         * 
         * 计算 itemsWidth , 这个与返回的 宽度无关, 与创建布局有关
         */
        if (recalculate) {
            int pureWidth = width - LABEL_OFFSET - 2 * PADDING;
            if (pureWidth <= 0) {
                itemsWidth = labelWidth = 0;
            }
            if (labelWidth > 0) {
                double newWidthItems = (double) itemsWidth * pureWidth / (itemsWidth + labelWidth);
                itemsWidth = (int) newWidthItems;
                labelWidth = pureWidth - itemsWidth;
            } else {
                itemsWidth = pureWidth + LABEL_OFFSET; // no label
            }
        }

        if (itemsWidth > 0) {
        	//创建布局
            createLayouts(itemsWidth, labelWidth);
        }

        return width;
    }

    /**
     * 创建布局
     * 
     * @param widthItems
     *            布局条目宽度
     * @param widthLabel
     *            label 宽度
     */
    private void createLayouts(int widthItems, int widthLabel) {
    	/*
    	 * 创建普通条目布局
    	 * 如果 普通条目布局 为 null 或者 普通条目布局的宽度 大于 传入的宽度, 这时需要重新创建布局
    	 * 如果 普通条目布局存在, 并且其宽度小于传入的宽度, 此时需要将
    	 */
        if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
        	
        	/*
        	 * android.text.StaticLayout.StaticLayout(
        	 * CharSequence source, TextPaint paint, 
        	 * int width, Alignment align, 
        	 * float spacingmult, float spacingadd, boolean includepad)
        	 * 传入参数介绍 : 
        	 * CharSequence source : 需要分行显示的字符串
        	 * TextPaint paint : 绘制字符串的画笔
        	 * int width : 条目的宽度
        	 * Alignment align : Layout 的对齐方式, ALIGN_CENTER 居中对齐, ALIGN_NORMAL 左对齐, Alignment.ALIGN_OPPOSITE 右对齐
        	 * float spacingmult : 行间距, 1.5f 代表 1.5 倍字体高度
        	 * float spacingadd : 基础行距上增加多少 , 真实行间距 等于 spacingmult 和 spacingadd 的和
        	 * boolean includepad : 
        	 */
            itemsLayout = new StaticLayout(buildText(isScrollingPerformed), itemsPaint, widthItems,
                    widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER, 1,
                    ADDITIONAL_ITEM_HEIGHT, false);
        } else {
        	//调用 Layout 内置的方法 increaseWidthTo 将宽度提升到指定的宽度
            itemsLayout.increaseWidthTo(widthItems);
        }

        /*
         * 创建选中条目
         */
        if (!isScrollingPerformed && (valueLayout == null || valueLayout.getWidth() > widthItems)) {
            String text = getAdapter() != null ? getAdapter().getItem(currentItem) : null;
            valueLayout = new StaticLayout(text != null ? text : "", valuePaint, widthItems,
                    widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER, 1,
                    ADDITIONAL_ITEM_HEIGHT, false);
        } else if (isScrollingPerformed) {
            valueLayout = null;
        } else {
            valueLayout.increaseWidthTo(widthItems);
        }

        /*
         * 创建标签条目
         */
        if (widthLabel > 0) {
            if (labelLayout == null || labelLayout.getWidth() > widthLabel) {
                labelLayout = new StaticLayout(label, valuePaint, widthLabel, Layout.Alignment.ALIGN_NORMAL, 1,
                        ADDITIONAL_ITEM_HEIGHT, false);
            } else {
                labelLayout.increaseWidthTo(widthLabel);
            }
        }
    }

    /*
     * 测量组件大小
     * (non-Javadoc)
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	//获取宽度 和 高度的模式 和 大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //宽度就是 计算的布局的宽度
        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        /*
         * 精准模式
         * 		精准模式下 高度就是精确的高度
         */
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        
        //未定义模式 和 最大模式
        } else {
        	//未定义模式下 获取布局需要的高度
            height = getDesiredHeight(itemsLayout);

            //最大模式下 获取 布局高度 和 布局所需高度的最小值
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        //设置组件的宽和高
        setMeasuredDimension(width, height);
    }

    /*
     * 绘制组件
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //如果条目布局为 null, 就创建该布局
        if (itemsLayout == null) {
        	/*
        	 * 如果 条目宽度为0, 说明该宽度没有计算, 先计算, 计算完之后会创建布局
        	 * 如果 条目宽度 大于 0, 说明已经计算过宽度了, 直接创建布局
        	 */
            if (itemsWidth == 0) {
                calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
            } else {
            	//创建普通条目布局, 选中条目布局, 标签条目布局
                createLayouts(itemsWidth, labelWidth);
            }
        }

        //如果条目宽度大于0
        if (itemsWidth > 0) {
            canvas.save();
            // 使用平移方法忽略 填充的空间 和 顶部底部隐藏的一部份条目
            canvas.translate(PADDING, -ITEM_OFFSET);
            //绘制普通条目
            drawItems(canvas);
            //绘制选中条目
            drawValue(canvas);
            canvas.restore();
        }

        //在中心位置绘制
        drawCenterRect(canvas);
        //绘制阴影
        drawShadows(canvas);
    }

    /**
     * Draws shadows on top and bottom of control
     * 
     * @param canvas
     *            the canvas for drawing
     */
    private void drawShadows(Canvas canvas) {
        topShadow.setBounds(0, 0, getWidth(), getHeight() / visibleItems);
        topShadow.draw(canvas);

        bottomShadow.setBounds(0, getHeight() - getHeight() / visibleItems, getWidth(), getHeight());
        bottomShadow.draw(canvas);
    }

    /**
     * 绘制选中条目
     * 
     * @param canvas
     *            画布
     */
    private void drawValue(Canvas canvas) {
        valuePaint.setColor(Color.parseColor("#BB040404"));
        
        //将当前 View 状态属性值 转为整型集合, 赋值给 普通条目布局的绘制属性
        valuePaint.drawableState = getDrawableState();

        Rect bounds = new Rect();
        //获取选中条目布局的边界
        itemsLayout.getLineBounds(visibleItems / 2, bounds);

        // 绘制标签
        if (labelLayout != null) {
            canvas.save();
            canvas.translate(itemsLayout.getWidth() + LABEL_OFFSET, bounds.top);
            labelLayout.draw(canvas);
            canvas.restore();
        }

        // 绘制选中条目
        if (valueLayout != null) {
            canvas.save();
            canvas.translate(0, bounds.top + scrollingOffset);
            valueLayout.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 绘制普通条目
     * 
     * @param canvas
     *            画布
     */
    private void drawItems(Canvas canvas) {
        canvas.save();

        //获取 y 轴 定点高度
        int top = itemsLayout.getLineTop(1);
        canvas.translate(0, -top + scrollingOffset);

        //设置画笔颜色
        itemsPaint.setColor(Color.rgb(136, 136, 136));
        //将当前 View 状态属性值 转为整型集合, 赋值给 普通条目布局的绘制属性
        itemsPaint.drawableState = getDrawableState();
        //将布局绘制到画布上
        itemsLayout.draw(canvas);

        canvas.restore();
    }

    /**
     * 绘制当前选中条目的背景图片
     * 
     * @param canvas
     *            画布
     */
    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = getItemHeight() / 2;
        centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
        centerDrawable.draw(canvas);
    }

    /*
     * 继承自 View 的触摸事件, 当出现触摸事件的时候, 就会回调该方法
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	//获取适配器
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return true;
        }

        /*
         * gestureDetector.onTouchEvent(event) : 分析给定的动作, 如果可用, 调用 手势检测器的 onTouchEvent 方法
         * -- 参数解析 : ev , 触摸事件
         * -- 返回值 : 如果手势监听器成功执行了该方法, 返回true, 如果执行出现意外 返回 false;
         */
        if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }
        return true;
    }

    /**
     * 滚动 WheelView
     * 
     * @param delta
     *            滚动的值
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;
        
        //计算滚动的条目数, 使用滚动的值 处于 单个条目高度, 注意计算整数值
        int count = scrollingOffset / getItemHeight();
        /*
         * pos 是滚动后的目标元素索引
         * 计算当前位置, 当前条目数 减去 滚动的条目数
         * 注意 滚动条目数可正 可负
         */
        int pos = currentItem - count;
        //如果是可循环的, 并且条目数大于0
        if (isCyclic && adapter.getItemsCount() > 0) {
            //设置循环, 如果位置小于0, 那么该位置就显示最后一个元素
            while (pos < 0) {
                pos += adapter.getItemsCount();
            }
            //如果位置正无限大, 模条目数 取余
            pos %= adapter.getItemsCount();
            
        // (前提 : 不可循环  条目数大于0, 可循环 条目数小于0, 条目数小于0, 不可循环) , 如果滚动在执行
        } else if (isScrollingPerformed) {
            //位置一旦小于0, 计算的位置就赋值为 0, 条目滚动数为0
            if (pos < 0) {
                count = currentItem;
                pos = 0;
                
            //位置大于条目数的时候, 当前位置等于(条目数 - 1), 条目滚动数等于 当前位置 减去 (条目数 - 1)
            } else if (pos >= adapter.getItemsCount()) {
                count = currentItem - adapter.getItemsCount() + 1;
                pos = adapter.getItemsCount() - 1;
            }
        
        } else {
            // fix position
            pos = Math.max(pos, 0);
            pos = Math.min(pos, adapter.getItemsCount() - 1);
        }

        //滚动的高度
        int offset = scrollingOffset;
        
        /*
         * 如果当前位置不是滚动后的目标位置, 就将当前位置设置为目标位置
         * 否则就重绘组件
         */
        if (pos != currentItem) {
            setCurrentItem(pos, false);
        } else {
        	//重绘组件
            invalidate();
        }

        // 将滚动后剩余的小数部分保存
        scrollingOffset = offset - count * getItemHeight();
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }

    /**
     * 手势监听器
     */
    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
    	
    	//按下操作
        public boolean onDown(MotionEvent e) {
        	//如果滚动在执行
            if (isScrollingPerformed) {
            	//滚动强制停止, 按下的时候不能继续滚动
                scroller.forceFinished(true);
                //清理信息
                clearMessages();
                return true;
            }
            return false;
        }

        /*
         * 手势监听器监听到 滚动操作后回调
         * 
         * 参数解析 : 
         * MotionEvent e1 : 触发滚动时第一次按下的事件
         * MotionEvent e2 : 触发当前滚动的移动事件
         * float distanceX : 自从上一次调用 该方法 到这一次 x 轴滚动的距离, 
         * 				注意不是 e1 到 e2 的距离, e1 到 e2 的距离是从开始滚动到现在的滚动距离
         * float distanceY : 自从上一次回调该方法到这一次 y 轴滚动的距离
         * 
         * 返回值 : 如果事件成功触发, 执行完了方法中的操作, 返回true, 否则返回 false 
         * (non-Javadoc)
         * @see android.view.GestureDetector.SimpleOnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
         */
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        	//开始滚动, 并回调滚动监听器集合中监听器的 开始滚动方法
            startScrolling();
            doScroll((int) -distanceY);
            return true;
        }

        /*
         * 当一个急冲手势发生后 回调该方法, 会计算出该手势在 x 轴 y 轴的速率
         * 
         * 参数解析 : 
         * -- MotionEvent e1 : 急冲动作的第一次触摸事件;
         * -- MotionEvent e2 : 急冲动作的移动发生的时候的触摸事件;
         * -- float velocityX : x 轴的速率
         * -- float velocityY : y 轴的速率
         * 
         * 返回值 : 如果执行完毕返回 true, 否则返回false, 这个就是自己定义的
         * 
         * (non-Javadoc)
         * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
         */
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        	//计算上一次的 y 轴位置, 当前的条目高度 加上 剩余的 不够一行高度的那部分
            lastScrollY = currentItem * getItemHeight() + scrollingOffset;
            //如果可以循环最大值是无限大, 不能循环就是条目数的高度值
            int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount() * getItemHeight();
            int minY = isCyclic ? -maxY : 0;
            /*
             * Scroll 开始根据一个急冲手势滚动, 滚动的距离与初速度有关
             * 参数介绍 : 
             * -- int startX : 开始时的 X轴位置
             * -- int startY : 开始时的 y轴位置
             * -- int velocityX : 急冲手势的 x 轴的初速度, 单位 px/s
             * -- int velocityY : 急冲手势的 y 轴的初速度, 单位 px/s
             * -- int minX : x 轴滚动的最小值
             * -- int maxX : x 轴滚动的最大值
             * -- int minY : y 轴滚动的最小值
             * -- int maxY : y 轴滚动的最大值
             */
            scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY, maxY);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    // Handler 中的  Message 信息
    /** 滚动信息 */
    private final int MESSAGE_SCROLL = 0;
    /** 调整信息 */
    private final int MESSAGE_JUSTIFY = 1;

    /**
     * 清空之前的 Handler 队列, 发送下一个消息到 Handler 中
     * 
     * @param message
     *            要发送的消息
     */
    private void setNextMessage(int message) {
    	//清空 Handler 队列中的  what 消息
        clearMessages();
        //发送消息到 Handler 中
        animationHandler.sendEmptyMessage(message);
    }

    /**
     * 清空队列中的信息
     */
    private void clearMessages() {
    	//删除 Handler 执行队列中的滚动操作
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    /**
     * 动画控制器
     *  animation handler
     *  
     *  可能会造成内存泄露 : 添加注解 HandlerLeak
     *  Handler 类应该应该为static类型，否则有可能造成泄露。
     *  在程序消息队列中排队的消息保持了对目标Handler类的应用。
     *  如果Handler是个内部类，那 么它也会保持它所在的外部类的引用。
     *  为了避免泄露这个外部类，应该将Handler声明为static嵌套类，并且使用对外部类的弱应用。
     */
    @SuppressLint("HandlerLeak")
	private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
        	//回调该方法获取当前位置, 如果返回true, 说明动画还没有执行完毕
            scroller.computeScrollOffset();
            //获取当前 y 位置
            int currY = scroller.getCurrY();
            //获取已经滚动了的位置, 使用上一次位置 减去 当前位置
            int delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
            	//改变值不为 0 , 继续滚动
                doScroll(delta);
            }

            /*
             * 如果滚动到了指定的位置, 滚动还没有停止
             * 这时需要强制停止
             */
            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                currY = scroller.getFinalY();
                scroller.forceFinished(true);
            }
            
            /*
             * 如果滚动没有停止
             * 再向 Handler 发送一个停止
             */
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };

    /**
     * 调整 WheelView
     */
    private void justify() {
        if (adapter == null) {
            return;
        }
        //上一次的 y 轴的位置为 0
        lastScrollY = 0;
        int offset = scrollingOffset;
        int itemHeight = getItemHeight();
        /*
         * 当滚动补偿 大于 0, 说明还有没有滚动的部分,  needToIncrease 是 当前条目是否小于条目数
         * 如果 滚动补偿不大于 0,  needToIncrease 是当前条目是否大于 0
         */
        boolean needToIncrease = offset > 0 ? currentItem < adapter.getItemsCount() : currentItem > 0;
        if ((isCyclic || needToIncrease) && Math.abs((float) offset) > (float) itemHeight / 2) {
            if (offset < 0)
                offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
            else
                offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
        }
        if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
            scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
            setNextMessage(MESSAGE_JUSTIFY);
        } else {
            finishScrolling();
        }
    }

    /**
     * WheelView 开始滚动
     */
    private void startScrolling() {
    	//如果没有滚动, 将滚动状态 isScrollingPerformed 设为 true
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            //通知监听器开始滚动 回调所有的 滚动监听集合中 的 开始滚动方法
            notifyScrollingListenersAboutStart();
        }
    }

    /**
     * 结束滚动
     * 	设置滚动状态为 false, 回调滚动监听器的停止滚动方法
     */
    void finishScrolling() {
        if (isScrollingPerformed) {
            notifyScrollingListenersAboutEnd();
            isScrollingPerformed = false;
        }
        //设置布局无效
        invalidateLayouts();
        //重绘布局
        invalidate();
    }

    /**
     * 滚动 WheelView
     * 
     * @param itemsToScroll
     *            滚动的元素个数
     * @param time
     *            每次滚动的间隔
     */
    public void scroll(int itemsToScroll, int time) {
    	//如果有滚动强制停止
        scroller.forceFinished(true);

        lastScrollY = scrollingOffset;
        int offset = itemsToScroll * getItemHeight();

        /*
         * 给定 一个开始点, 滚动距离, 滚动间隔, 开始滚动
         * 
         * 参数解析 : 
         * 1. 开始的 x 轴位置
         * 2. 开始的 y 轴位置
         * 3. 要滚动 x 轴距离
         * 4. 要滚动 y 轴距离
         * 5. 滚动花费的时间
         */
        scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
        setNextMessage(MESSAGE_SCROLL);

        //设置开始滚动状态, 并回调滚动监听器方法
        startScrolling();
    }

}
