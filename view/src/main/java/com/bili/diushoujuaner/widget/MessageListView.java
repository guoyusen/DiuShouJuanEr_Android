package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;


/**
 * ListView头部加载更多
 */
public class MessageListView extends ListView implements OnScrollListener {

	/** 加载中 */
	private final static int ENDINT_LOADING = 1;
	/** 加载数据完成 */
	private final static int ENDING_FINISHED = 2;
	/** 数据已经全部加载 */
	private final static int ENDING_COMPLETE = 3;
	/** 滑到顶部后自动加载 */
	private final static int ENDINT_AUTO_LOAD_DONE = 4;

	private int mEndState;

	private LayoutInflater mInflater;
	private View mStartRootView;
	private MaterialCircleView customRefreshCircle;

	private int mFirstItemIndex;
	private boolean mEnoughCount;// 足够数量充满屏幕？

	private OnLoadMoreListener mLoadMoreListener;

	/**
	 * 加载更多监听接口
	 */
	public interface OnLoadMoreListener {
		void onLoadMore();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			mLoadMoreListener = pLoadMoreListener;
			if (getHeaderViewsCount() == 0) {
				addHeadView();
			}
		}
	}

	public MessageListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public MessageListView(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public MessageListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}

	private void init(Context pContext) {
		if (pContext == null) {
			return;
		}
		setCacheColorHint(ContextCompat.getColor(pContext, R.color.TRANSPARENT));
		mInflater = LayoutInflater.from(pContext);
		addHeadView();
		setOnScrollListener(this);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setSelection(getCount() - 1);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/**
	 * 添加加载更多FootView
	 */
	private void addHeadView() {
		mStartRootView = mInflater.inflate(R.layout.footer_load_more,
				null);
		customRefreshCircle = (MaterialCircleView) mStartRootView.findViewById(R.id.customRefreshCircle);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)getResources().getDimension(R.dimen.x56),(int)getResources().getDimension(R.dimen.x56));
		customRefreshCircle.setLayoutParams(layoutParams);
		addHeaderView(mStartRootView);
		mEndState = ENDINT_AUTO_LOAD_DONE;
	}

	/**
	 * 为了判断滑动到ListView顶部
	 */
	@Override
	public void onScroll(AbsListView pView, int pFirstVisibleItem,
			int pVisibleItemCount, int pTotalItemCount) {
		mFirstItemIndex = getFirstVisiblePosition();
		if (pTotalItemCount > pVisibleItemCount) {
			mEnoughCount = true;
		} else {
			mEnoughCount = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView pView, int pScrollState) {
		if (mFirstItemIndex == 0 && pScrollState == SCROLL_STATE_IDLE) {
			// SCROLL_STATE_IDLE=0，滑动停止
			if (mEndState != ENDINT_LOADING && mEndState != ENDING_COMPLETE) {
				mEndState = ENDINT_LOADING;
				onLoadMore();
				changeEndViewByState();
			}
		}
	}

	public void setListViewStateFinished(){
		mEndState = ENDING_FINISHED;
		changeEndViewByState();
	}

	public void setListViewStateComplete(){
		mEndState = ENDING_COMPLETE;
		changeEndViewByState();
	}

	/**
	 * 改变加载更多状态
	 */
	private void changeEndViewByState() {
		switch (mEndState) {
			case ENDINT_LOADING:// 刷新中
				customRefreshCircle.setVisibility(View.VISIBLE);
				break;
			case ENDING_FINISHED:
				mStartRootView.setVisibility(View.GONE);
				break;
			case ENDING_COMPLETE:
				customRefreshCircle.setVisibility(View.GONE);
				break;
		}
	}

	/**
	 * 正在加载，显示加载旋转动画
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			customRefreshCircle.setVisibility(View.VISIBLE);
			mLoadMoreListener.onLoadMore();
		}
	}

}