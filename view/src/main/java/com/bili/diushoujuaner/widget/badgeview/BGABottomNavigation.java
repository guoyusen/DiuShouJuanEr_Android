package com.bili.diushoujuaner.widget.badgeview;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bili.diushoujuaner.R;

import java.util.ArrayList;

/**
 * AHBottomNavigationLayout
 * Material Design guidelines : https://www.google.com/design/spec/components/bottom-navigation.html
 */
public class BGABottomNavigation extends FrameLayout {

	// Static
	private static String TAG = "AHBottomNavigation";

	// Listener
	private AHBottomNavigationListener listener;

	// Variables
	private Context context;
	private ArrayList<BGABottomNavigationItem> items = new ArrayList<>();
	private ArrayList<BGABottomNavigationItemView> views = new ArrayList<>();
	private View backgroundColorView;
	private boolean colored = false;

	private int defaultBackgroundColor = Color.WHITE;
	private int accentColor = Color.WHITE;
	private int inactiveColor = Color.WHITE;

	private int currentItem = 0;
	private int currentColor = 0;
	private float selectedItemWidth, notSelectedItemWidth;
	private IViewInitFinishListener iViewInitFinishListener;

	public interface IViewInitFinishListener{
		void initFinished();
	}

	public void setiViewInitFinishListener(IViewInitFinishListener iViewInitFinishListener) {
		this.iViewInitFinishListener = iViewInitFinishListener;
	}

	/**
	 * Constructor
	 * @param context
	 */
	public BGABottomNavigation(Context context) {
		super(context);
		this.context = context;
		initColors();
	}

	public BGABottomNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initColors();
	}

	public BGABottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		initColors();
	}

	public BGABottomNavigationItemView getNavViewByPosition(int position){
		if(position >= 0 && position < views.size()){
			return views.get(position);
		}
		return null;
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initViews();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		createItems();
	}

	public BGABottomNavigationItemView getBGAButtonItemView(int position){
		if(position >=0 && position < views.size()){
			return views.get(position);
		}
		return views.get(0);
	}

	/**
	 * Init the default colors
	 */
	private void initColors() {
		accentColor = ContextCompat.getColor(context, R.color.colorAccent);
		inactiveColor = ContextCompat.getColor(context, R.color.colorInactive);
	}

	/**
	 * Init
	 */
	private void initViews() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setElevation(context.getResources().getDimension(R.dimen.bottom_navigation_elevation));
			setClipToPadding(false);
		}

		ViewGroup.LayoutParams params = getLayoutParams();
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = (int) context.getResources().getDimension(R.dimen.bottom_navigation_height);
		setLayoutParams(params);

	}

	/**
	 * Create the items in the bottom navigation
	 */
	private void createItems() {

		currentItem = 0;
		removeAllViews();
		views.clear();

		backgroundColorView = new View(context);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colored) {
			LayoutParams backgroundLayoutParams = new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			addView(backgroundColorView, backgroundLayoutParams);
		}

		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER);

		int layoutHeight = (int) context.getResources().getDimension(R.dimen.bottom_navigation_height);
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight);
		addView(linearLayout, layoutParams);

		createClassicItems(linearLayout);
	}

	/**
	 * Create classic items (only 3 items in the bottom navigation)
	 *
	 * @param linearLayout The layout where the items are added
	 */
	private void createClassicItems(LinearLayout linearLayout) {

		float height = context.getResources().getDimension(R.dimen.bottom_navigation_height);
		float minWidth = context.getResources().getDimension(R.dimen.bottom_navigation_min_width);
		float maxWidth = context.getResources().getDimension(R.dimen.bottom_navigation_max_width);

		int layoutWidth = getWidth();
		if (layoutWidth == 0 || items.size() == 0) {
			return;
		}

		float itemWidth = layoutWidth / items.size();
		if (itemWidth < minWidth) {
			itemWidth = minWidth;
		} else if (itemWidth > maxWidth) {
			itemWidth = maxWidth;
		}

		for (int i = 0; i < items.size(); i++) {

			final int itemIndex = i;
			BGABottomNavigationItem item = items.get(itemIndex);

			BGABottomNavigationItemView view = (BGABottomNavigationItemView)LayoutInflater.from(context).inflate(R.layout.bottom_navigation_item, null);
			ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
			TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
			icon.setImageResource(item.getResource());
			title.setText(item.getTitle());

			if (i == currentItem) {
				int activePaddingTop = (int) context.getResources()
						.getDimension(R.dimen.bottom_navigation_padding_top_active);
				view.setPadding(view.getPaddingLeft(), activePaddingTop, view.getPaddingRight(),
						view.getPaddingBottom());
			}

			if (colored) {
				if (i == currentItem) {
					setBackgroundColor(item.getColor());
					currentColor = item.getColor();
				}

				icon.setImageDrawable(BGABottomNavigatonHelper.getTintDrawable(context, items.get(i).getResource(),
						currentItem == i ? ContextCompat.getColor(context, R.color.colorActiveSmall) :
								ContextCompat.getColor(context, R.color.colorInactiveSmall)));
				title.setTextColor(currentItem == i ?
						ContextCompat.getColor(context, R.color.colorActiveSmall) :
						ContextCompat.getColor(context, R.color.colorInactiveSmall));

			} else {
				setBackgroundColor(defaultBackgroundColor);
				icon.setImageDrawable(BGABottomNavigatonHelper.getTintDrawable(context, items.get(i).getResource(),
						currentItem == i ? accentColor : inactiveColor));
				title.setTextColor(currentItem == i ? accentColor : inactiveColor);
			}

			title.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentItem == i ?
					context.getResources().getDimension(R.dimen.bottom_navigation_text_size_active) :
					context.getResources().getDimension(R.dimen.bottom_navigation_text_size_inactive));

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateItems(itemIndex);
				}
			});

			LayoutParams params = new LayoutParams((int) itemWidth, (int) height);
			linearLayout.addView(view, params);
			views.add(view);
		}
		if(iViewInitFinishListener != null){
			iViewInitFinishListener.initFinished();
		}
	}

	/**
	 * Update Items UI
	 */
	private void updateItems(final int itemIndex) {

		if (currentItem == itemIndex) {
			return;
		}

		int activePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_active);
		int inactivePaddingTop = (int) context.getResources().getDimension(R.dimen.bottom_navigation_padding_top_inactive);
		float activeSize = context.getResources().getDimension(R.dimen.bottom_navigation_text_size_active);
		float inactiveSize = context.getResources().getDimension(R.dimen.bottom_navigation_text_size_inactive);
		int itemActiveColor = colored ? ContextCompat.getColor(context, R.color.colorActiveSmall) :
				accentColor;
		int itemInactiveColor = colored ? ContextCompat.getColor(context, R.color.colorInactiveSmall) :
				inactiveColor;

		for (int i = 0; i < views.size(); i++) {

			if (i == itemIndex) {

				final View container = views.get(itemIndex).findViewById(R.id.bottom_navigation_container);
				final TextView title = (TextView) views.get(itemIndex).findViewById(R.id.bottom_navigation_item_title);
				final ImageView icon = (ImageView) views.get(itemIndex).findViewById(R.id.bottom_navigation_item_icon);

				BGABottomNavigatonHelper.updateTopPadding(container, inactivePaddingTop, activePaddingTop);
				BGABottomNavigatonHelper.updateTextColor(title, itemInactiveColor, itemActiveColor);
				BGABottomNavigatonHelper.updateTextSize(title, inactiveSize, activeSize);
				BGABottomNavigatonHelper.updateDrawableColor(context, items.get(itemIndex).getResource(), icon,
						itemInactiveColor, itemActiveColor);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colored) {
					backgroundColorView.setBackgroundColor(items.get(itemIndex).getColor());
					int finalRadius = Math.max(getWidth(), getHeight());

					int cx = (int) views.get(itemIndex).getX() + views.get(itemIndex).getWidth() / 2;
					int cy = views.get(itemIndex).getHeight() / 2;
					Animator anim = ViewAnimationUtils.createCircularReveal(backgroundColorView, cx, cy, 0, finalRadius);
					anim.addListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animation) {
						}

						@Override
						public void onAnimationEnd(Animator animation) {
							setBackgroundColor(items.get(itemIndex).getColor());
						}

						@Override
						public void onAnimationCancel(Animator animation) {
						}

						@Override
						public void onAnimationRepeat(Animator animation) {
						}
					});
					anim.start();
				} else if (colored) {
					BGABottomNavigatonHelper.updateViewBackgroundColor(this, currentColor,
							items.get(itemIndex).getColor());
				} else {
					setBackgroundColor(defaultBackgroundColor);
				}

			} else if (i == currentItem) {

				final View container = views.get(currentItem).findViewById(R.id.bottom_navigation_container);
				final TextView title = (TextView) views.get(currentItem).findViewById(R.id.bottom_navigation_item_title);
				final ImageView icon = (ImageView) views.get(currentItem).findViewById(R.id.bottom_navigation_item_icon);

				BGABottomNavigatonHelper.updateTopPadding(container, activePaddingTop, inactivePaddingTop);
				BGABottomNavigatonHelper.updateTextColor(title, itemActiveColor, itemInactiveColor);
				BGABottomNavigatonHelper.updateTextSize(title, activeSize, inactiveSize);
				BGABottomNavigatonHelper.updateDrawableColor(context, items.get(currentItem).getResource(), icon,
						itemActiveColor, itemInactiveColor);
			}
		}

		currentItem = itemIndex;
		currentColor = items.get(currentItem).getColor();

		if (listener != null) {
			listener.onTabSelected(itemIndex);
		}
	}

	/**
	 * Add an item
	 */
	public void addItem(BGABottomNavigationItem item) {
		items.add(item);
		createItems();
	}

	/**
	 * Add all items
	 */
	public void addItems(ArrayList<BGABottomNavigationItem> items) {
		this.items.addAll(items);
		createItems();
	}

	/**
	 * Remove an item at the given index
	 */
	public void removeItemAtIndex(int index) {
		if (index < items.size()) {
			this.items.remove(index);
			createItems();
		}
	}

	/**
	 * Remove all items
	 */
	public void removeAllItems() {
		this.items.clear();
		createItems();
	}

	/**
	 * Return if the Bottom Navigation is colored
	 */
	public boolean isColored() {
		return colored;
	}

	/**
	 * Set if the Bottom Navigation is colored
	 */
	public void setColored(boolean colored) {
		this.colored = colored;
		createItems();
	}


	/**
	 * Set the GraphView listener
	 */
	public void setAHBottomNavigationListener(AHBottomNavigationListener listener) {
		this.listener = listener;
	}

	/**
	 * Remove the GraphView listener
	 */
	public void removeAHBottomNavigationListener() {
		this.listener = null;
	}

	/**
	 * Return the bottom navigation background color
	 *
	 * @return The bottom navigation background color
	 */
	public int getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	/**
	 * Set the bottom navigation background color
	 *
	 * @param defaultBackgroundColor The bottom navigation background color
	 */
	public void setDefaultBackgroundColor(int defaultBackgroundColor) {
		this.defaultBackgroundColor = defaultBackgroundColor;
		createItems();
	}

	/**
	 * Get the accent color (used when the view contains 3 items)
	 *
	 * @return The default accent color
	 */
	public int getAccentColor() {
		return accentColor;
	}

	/**
	 * Set the accent color (used when the view contains 3 items)
	 *
	 * @param accentColor The new accent color
	 */
	public void setAccentColor(int accentColor) {
		this.accentColor = accentColor;
		createItems();
	}

	/**
	 * Get the inactive color (used when the view contains 3 items)
	 *
	 * @return The inactive color
	 */
	public int getInactiveColor() {
		return inactiveColor;
	}

	/**
	 * Set the inactive color (used when the view contains 3 items)
	 *
	 * @param inactiveColor The inactive color
	 */
	public void setInactiveColor(int inactiveColor) {
		this.inactiveColor = inactiveColor;
		createItems();
	}

	/**
	 * Get the current item
	 *
	 * @return The current item position
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Set the current item
	 *
	 * @param position The new position
	 */
	public void setCurrentItem(int position) {
		if (position >= items.size()) {
			Log.w(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
			return;
		}
		updateItems(position);
	}

	////////////////
	// INTERFACES //
	////////////////

	/**
	 * Interface for Bottom Navigation
	 */
	public interface AHBottomNavigationListener {
		void onTabSelected(int position);
	}

}