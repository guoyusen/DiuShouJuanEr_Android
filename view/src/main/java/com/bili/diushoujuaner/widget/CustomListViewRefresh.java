package com.bili.diushoujuaner.widget;

import android.content.Context;
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

	/** 加载中 */
	private final static int ENDINT_LOADING = 1;
	/** 手动完成刷新 */
	private final static int ENDINT_MANUAL_LOAD_DONE = 2;
	/** 自动完成刷新 */
	private final static int ENDINT_AUTO_LOAD_DONE = 3;
	/**
	 * 0:完成/等待刷新 ;
	 * <p>
	 * 1:加载中
	 */
	private int mEndState;

	/** 可以加载更多？ */
	private boolean mCanLoadMore = false;
	/** 可以自动加载更多吗？（注意，先判断是否有加载更多，如果没有，这个flag也没有意义） */
	private boolean mIsAutoLoadMore = true;
	/** 下拉刷新后是否显示第一条Item */
	private boolean mIsMoveToFirstItemAfterRefresh = false;

	private LayoutInflater mInflater;
	private View mEndRootView;
	private MaterialCircleView customRefreshCircle;
	public TextView mEndLoadTipsTextView;

	private int mLastItemIndex;
	private int mCount;
	private boolean mEnoughCount;// 足够数量充满屏幕？

	private OnLoadMoreListener mLoadMoreListener;

	public CustomListViewRefresh(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public CustomListViewRefresh(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public CustomListViewRefresh(Context pContext, AttributeSet pAttrs,
								 int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}

	/**
	 * 初始化操作
	 */
	private void init(Context pContext) {
		if (pContext == null) {
			return;
		}

		setCacheColorHint(pContext.getResources().getColor(R.color.TRANSPARENT));
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

	public boolean isAutoLoadMore() {
		return mIsAutoLoadMore;
	}

	public void setAutoLoadMore(boolean pIsAutoLoadMore) {
		mIsAutoLoadMore = pIsAutoLoadMore;
	}

	public boolean isMoveToFirstItemAfterRefresh() {
		return mIsMoveToFirstItemAfterRefresh;
	}

	public void setMoveToFirstItemAfterRefresh(
			boolean pIsMoveToFirstItemAfterRefresh) {
		mIsMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
	}


	/**
	 * 添加加载更多FootView
	 */
	private void addFootView() {
		mEndRootView = mInflater.inflate(R.layout.footer_load_more,
				null);
		mEndRootView.setVisibility(View.VISIBLE);
		customRefreshCircle = (MaterialCircleView) mEndRootView.findViewById(R.id.customRefreshCircle);
		mEndLoadTipsTextView = (TextView) mEndRootView
				.findViewById(R.id.textLoadMore);
		mEndRootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCanLoadMore && mEndState != ENDINT_LOADING) {
					// 当不能下拉刷新时，FootView不正在加载时，才可以点击加载更多。
					mEndState = ENDINT_LOADING;
					onLoadMore();
				}
			}
		});

		addFooterView(mEndRootView);

		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		} else {
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
	}

	/**
	 * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
	 */
	private void measureView(View pChild) {
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
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
			// 存在加载更多功能
			if (mLastItemIndex == mCount && pScrollState == SCROLL_STATE_IDLE) {
				// SCROLL_STATE_IDLE=0，滑动停止
				if (mEndState != ENDINT_LOADING) {
					if (mIsAutoLoadMore) {
							// 没有下拉刷新，我们直接进行加载更多。
							// FootView显示 : 更 多 ---> 加载中...
							mEndState = ENDINT_LOADING;
							onLoadMore();
							changeEndViewByState();
					} else {
						// 不是自动加载更多，我们让FootView显示 “点击加载”
						// FootView显示 : 点击加载 ---> 加载中...
						mEndState = ENDINT_MANUAL_LOAD_DONE;
						changeEndViewByState();
					}
				}
			}
		} else if (mEndRootView != null
				&& mEndRootView.getVisibility() == VISIBLE) {
			// 突然关闭加载更多功能之后，我们要移除FootView。
			System.out.println("this.removeFooterView(endRootView);...");
			mEndRootView.setVisibility(View.GONE);
			this.removeFooterView(mEndRootView);
		}
	}

	/**
	 * 改变加载更多状态
	 */
	private void changeEndViewByState() {
		if (mCanLoadMore) {
			// 允许加载更多
			switch (mEndState) {
			case 0:// 刷新中

				// 加载中...
				if (mEndLoadTipsTextView.getText().equals(
						R.string.p2refresh_doing_end)) {
					break;
				}
				mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				customRefreshCircle.setVisibility(View.GONE);
				break;
			case ENDINT_LOADING:// 刷新中

				// 加载中...
				if (mEndLoadTipsTextView.getText().equals(
						R.string.p2refresh_doing_end_refresh)) {
					break;
				}
				mEndLoadTipsTextView
						.setText(R.string.p2refresh_doing_end_refresh);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				customRefreshCircle.setVisibility(View.VISIBLE);
				break;
			case ENDINT_MANUAL_LOAD_DONE:// 手动刷新完成

				// 点击加载
				mEndLoadTipsTextView
						.setText(R.string.p2refresh_end_click_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				customRefreshCircle.setVisibility(View.GONE);

				mEndRootView.setVisibility(View.VISIBLE);
				break;
			case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成

				// 更 多
				mEndLoadTipsTextView.setText(R.string.p2refresh_end_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				customRefreshCircle.setVisibility(View.GONE);

				mEndRootView.setVisibility(View.VISIBLE);
				break;
			default:
				// 原来的代码是为了： 当所有item的高度小于ListView本身的高度时，
				// 要隐藏掉FootView，

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
	 * 下拉刷新监听接口
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * 加载更多监听接口
	 */
	public interface OnLoadMoreListener {
		void onLoadMore();
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			mLoadMoreListener = pLoadMoreListener;
			mCanLoadMore = true;
			if (mCanLoadMore && getFooterViewsCount() == 0) {
				addFootView();
			}
		}
	}

	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			// 加载中...
			mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
			mEndLoadTipsTextView.setVisibility(View.VISIBLE);
			customRefreshCircle.setVisibility(View.VISIBLE);

			mLoadMoreListener.onLoadMore();
		}
	}
	/**
	 * 加载更多完成
	 */
	public void onLoadMoreComplete() {
		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		} else {
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
		changeEndViewByState();
	}

	public void onLoadComplete() {

		mEndState = 0;

		changeEndViewByState();
	}

}