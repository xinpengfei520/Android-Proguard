package com.xpf.library;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 修改viewpager切换的时间
 */
public class ViewPagerScroller extends Scroller {
	private int mScrollDuration = 1500;// 滑动速度,值越大滑动越慢，滑动太快会使3d效果不明显
	private boolean zero;

	public ViewPagerScroller(Context context) {
		super(context);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}


	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
	}

	public int getScrollDuration() {
		return mScrollDuration;
	}

	public void setScrollDuration(int scrollDuration) {
		this.mScrollDuration = scrollDuration;
	}

	public boolean isZero() {
		return zero;
	}

	public void setZero(boolean zero) {
		this.zero = zero;
	}
}