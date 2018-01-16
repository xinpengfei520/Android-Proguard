package com.xpf.library;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.xpf.library.view.BannerHolder;

import java.util.List;

public class CBPageAdapter<T> extends PagerAdapter {
    protected List<T> mDatas;
    protected BannerHolder<T> holder;


    public CBPageAdapter(BannerHolder<T> holder, List<T> datas) {
        this.holder = holder;
        this.mDatas = datas;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = holder.createView(container.getContext());
        if (mDatas != null && !mDatas.isEmpty()) {
            holder.updateUI(container.getContext(), position, mDatas.get(position));
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
