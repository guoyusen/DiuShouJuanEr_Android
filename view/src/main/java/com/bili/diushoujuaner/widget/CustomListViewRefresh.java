package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bili.diushoujuaner.R;


/**
 * ListView加载更多
 */
public class CustomListViewRefresh extends ListView implements OnScrollListener {

	/** 刷新中 */
	private final static int ENDINT_REFRESH = 0;
	/** 加载中 */
	private final static int ENDINT_LOADING = 1;
	/** 加载数据完成 */
	private final static int ENDING_FINISHED = 2;
	/** 数据已经全部加载 */
	private final static int ENDING_COMPLETE = 3;
	/** 自动完成刷新 */
	private final static int ENDINT_AUTO_LOAD_DONE = 4;
	/**
	 * 0:完成/等待刷新 ;
	 * <p>
	 * 1:加载中
	 */
	private int mEndState;

	/** 可以加载更多？ */
	private boolean mCanLoadMore = true;
	/** 下拉刷新后是否显示第一条Item */
	private boolean mIsMoveToFirstItemAfterRefresh = true;

	private LayoutInflater mInflater;
	private View mEndRootView;
	private MaterialCircleView customRefreshCircle;
	public TextView mEndLoadTipsTextView;

	private int mLastItemIndex;
	private int mCount;
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
			mCanLoadMore = true;
			if (getFooterViewsCount() == 0) {
				addFootView();
			}
		}
	}

	public CustomListViewRefresh(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public CustomListViewRefresh(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public CustomListViewRefresh(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}

	private void init(Context pContext) {
		if (pContext == null) {
			return;
		}

		setCacheColorHint(ContextCompat.getColor(pContext, R.color.TRANSPARENT));
		mInflater = LayoutInflater.from(pContext);
		setOnScrollListener(this);
	}

	public boolean isCanLoadMore() {
		return mCanLoadMore;
	}

	public void setCanLoadMore(boolean pCanLoadMore) {
		mCanLoadMore = pCanLoadMore;
		if (mCanLoadMore && getFooterViewsCount() == 0) {
			addFootView();
		}
	}

	public boolean isMoveToFirstItemAfterRefresh() {
		return mIsMoveToFirstItemAfterRefresh;
	}

	public void setMoveToFirstItemAfterRefresh(boolean pIsMoveToFirstItemAfterRefresh) {
		mIsMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
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
	private void addFootView() {
		mEndRootView = mInflater.inflate(R.layout.footer_load_more,
				null);
		mEndRootView.setVisibility(View.GONE);
		customRefreshCircle = (MaterialCircleView) mEndRootView.findViewById(R.id.customRefreshCircle);
		mEndLoadTipsTextView = (TextView) mEndRootView.findViewById(R.id.textLoadMore);

		addFooterView(mEndRootView);
		mEndState = ENDINT_AUTO_LOAD_DONE;
	}

	/**
	 * 为了判断滑动到ListView底部没
	 */
	@Override
	public void onScroll(AbsListView pView, int pFirstVisibleItem,
			int pVisibleItemCount, int pTotalItemCount) {
		mLastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
		mCount = pTotalItemCount - 2;
		if (pTotalItemCount > pVisibleItemCount) {
			mEnoughCount = true;
		} else {
			mEnoughCount = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView pView, int pScrollState) {
		if (mCanLoadMore) {
			if (mLastItemIndex == mCount && pScrollState == SCROLL_STATE_IDLE) {
				// SCROLL_STATE_IDLE=0，滑动停止
				if (mEndState != ENDINT_LOADING && mEndState != ENDINT_REFRESH && mEndState != ENDING_COMPLETE) {
					mEndState = ENDINT_LOADING;
					onLoadMore();
					changeEndViewByState();
				}
			}
		} else if (mEndRootView != null && mEndRootView.getVisibility() == VISIBLE) {
			// 突然关闭加载更多功能之后，我们要移除FootView。
			mEndRootView.setVisibility(View.GONE);
			this.removeFooterView(mEndRootView);
		}
	}

	public void setListViewStateRefresh(){
		mEndState = ENDINT_REFRESH;
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
		if (mCanLoadMore) {
			// 允许加载更多
			switch (mEndState) {
			case ENDINT_LOADING:// 刷新中
				mEndLoadTipsTextView.setVisibility(View.INVISIBLE);
				customRefreshCircle.setVisibility(View.VISIBLE);
				mEndRootView.setVisibility(View.VISIBLE);
				break;
			case ENDING_FINISHED:
				if(mIsMoveToFirstItemAfterRefresh){
					setSelection(0);
				}
				mEndRootView.setVisibility(View.GONE);
				break;
			case ENDING_COMPLETE:
				mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				customRefreshCircle.setVisibility(View.GONE);
				mEndRootView.setVisibility(View.VISIBLE);
				break;
			default:
				if (mEnoughCount) {
					mEndRootView.setVisibility(View.VISIBLE);
				} else {
					mEndRootView.setVisibility(View.GONE);
				}
				break;
			}
		}
	}

	/**
	 * 正在加载，显示加载旋转动画
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			mEndLoadTipsTextView.setVisibility(View.INVISIBLE);
			customRefreshCircle.setVisibility(View.VISIBLE);
			mEndRootView.setVisibility(View.VISIBLE);
			mLoadMoreListener.onLoadMore();
		}
	}

}