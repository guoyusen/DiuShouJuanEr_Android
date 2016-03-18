package com.bili.diushoujuaner.widget.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * BGABottomNavigationItem
 * The item is display in the AHBottomNavigation layout
 */
public class BGABottomNavigationItemView extends FrameLayout implements BGABadgeable{

	private BGABadgeViewHelper mBadgeViewHeler;

	public BGABottomNavigationItemView(Context context) {
		this(context, null);
	}

	public BGABottomNavigationItemView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.radioButtonStyle);
	}

	public BGABottomNavigationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mBadgeViewHeler = new BGABadgeViewHelper(this, context, attrs, BGABadgeViewHelper.BadgeGravity.RightTop);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mBadgeViewHeler.onTouchEvent(event);
	}

	@Override
	public boolean callSuperOnTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mBadgeViewHeler.drawBadge(canvas);
	}

	@Override
	public void showCirclePointBadge() {
		mBadgeViewHeler.showCirclePointBadge();
	}

	@Override
	public void showTextBadge(String badgeText) {
		mBadgeViewHeler.showTextBadge(badgeText);
	}

	@Override
	public void hiddenBadge() {
		mBadgeViewHeler.hiddenBadge();
	}

	@Override
	public void showDrawableBadge(Bitmap bitmap) {
		mBadgeViewHeler.showDrawable(bitmap);
	}

	@Override
	public void setDragDismissDelegage(BGADragDismissDelegate delegate) {
		mBadgeViewHeler.setDragDismissDelegage(delegate);
	}

}
