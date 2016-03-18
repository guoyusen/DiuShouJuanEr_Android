package com.bili.diushoujuaner.widget.badgeview;

import android.graphics.Color;

/**
 * BGABottomNavigationItem
 * The item is display in the AHBottomNavigation layout
 */
public class BGABottomNavigationItem {

	private String title = "";
	private int color = Color.GRAY;
	private int resource = 0;

	/**
	 * Constructor
	 */
	public BGABottomNavigationItem() {
	}

	/**
	 * Constructor
	 *
	 * @param title    Title
	 * @param resource Drawable resource
	 */
	public BGABottomNavigationItem(String title, int resource) {
		this.title = title;
		this.resource = resource;
	}

	/**
	 * @param title    Title
	 * @param resource Drawable resource
	 * @param color    Background color
	 */
	public BGABottomNavigationItem(String title, int resource, int color) {
		this.title = title;
		this.resource = resource;
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}
}
