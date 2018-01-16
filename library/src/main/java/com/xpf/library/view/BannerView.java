package com.xpf.library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.xpf.library.CBPageAdapter;
import com.xpf.library.LoopViewPager;
import com.xpf.library.ViewPagerScroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 页面翻转控件，极方便的广告栏
 * 支持无限循环，自动翻页，翻页特效
 *
 * @author Sai 支持自动翻页
 */
public class BannerView extends LinearLayout {

    private LoopViewPager viewPager;
    private ViewPagerScroller scroller;
    private long autoTurningTime;
    private boolean turning;
    private boolean canTurn = false;//是否可以切换

    private AdSwitchTask adSwitchTask;
    private boolean isSetpage = false;

    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoopViewPager getViewPager() {
        return viewPager;
    }

    private void init(Context context) {
        removeAllViews();
        viewPager = new LoopViewPager(context);
        viewPager.setBoundaryCaching(true);
        addView(viewPager);
        initViewPagerScroll();
        adSwitchTask = new AdSwitchTask(this);
    }


    private static class AdSwitchTask implements Runnable {

        private final WeakReference<BannerView> reference;

        AdSwitchTask(BannerView convenientBanner) {
            this.reference = new WeakReference<BannerView>(convenientBanner);
        }

        @Override
        public void run() {
            BannerView bannerView = reference.get();

            if (bannerView != null) {
                LoopViewPager viewPager = bannerView.viewPager;
                if (viewPager != null && bannerView.turning) {
                    final int page = viewPager.getCurrentItem() + 1;
                    viewPager.setCurrentItem(page, true);
                    bannerView.postDelayed(bannerView.adSwitchTask, bannerView.autoTurningTime);
                }
            }
        }
    }

    public <T> BannerView setPages(BannerHolder<T> holder, List<T> datas) {
        isSetpage = true;
        viewPager.clearOnPageChangeListeners();
        viewPager.setAdapter(new CBPageAdapter<T>(holder, datas));
        return this;
    }


    /***
     * 是否开启自动翻页
     *
     * @return
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * 开始自动翻页
     *
     * @param autoTurningTime 自动翻页时间
     */
    public void startTurning(long autoTurningTime) {
        //没有设置广告时，直接返回
        if (!isSetpage) return;
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        postDelayed(adSwitchTask, autoTurningTime);
    }

    /**
     * 停止自动翻页
     */
    public void stopTurning() {
        if (!isSetpage) return;
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    /**
     * 设置翻页动画效果
     */
    public BannerView setPageTransformer(PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }


    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(
                    viewPager.getContext());
            mScroller.set(viewPager, scroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn) startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (canTurn) stopTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取当前的页面index
     */
    public int getCurrentItem() {
        if (viewPager != null) {
            return viewPager.getCurrentItem();
        }
        return -1;
    }

    /**
     * 设置要显示的当前的页面
     */
    public BannerView setcurrentitem(int index) {
        if (viewPager != null) {
            viewPager.setCurrentItem(index);
        }
        return this;
    }


    /**
     * 设置翻页监听器
     */
    public BannerView addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        viewPager.addOnPageChangeListener(onPageChangeListener);
        return this;
    }


    /**
     * 设置ViewPager的滚动速度
     *
     * @param scrollDuration (单位:毫秒)
     */
    public BannerView setScrollDuration(int scrollDuration) {
        scroller.setScrollDuration(scrollDuration);
        return this;
    }

    public int getScrollDuration() {
        return scroller.getScrollDuration();
    }

    /**
     * 设置是否支持轮播<br>
     * 该方法要执行于{@linkplain BannerView#setPages(BannerHolder, List)}方法前,否则会报异常
     *
     * @param isBoundaryLooping 是否轮播
     * @since 1.1.0
     */
    public BannerView setBoundaryLooping(boolean isBoundaryLooping) {
        viewPager.setBoundaryLooping(isBoundaryLooping);
        return this;
    }

    /**
     * 设置过界滚动的模式
     *
     * @see ViewPager#setOverScrollMode(int)
     * @since 1.1.0
     */
    public BannerView setBannerOverScrollMode(int overScrollMode) {
        viewPager.setOverScrollMode(overScrollMode);
        return this;
    }


}
