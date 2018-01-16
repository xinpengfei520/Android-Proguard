package com.xpf.library.view;

/**
 * Created by Sai on 15/12/14.
 *
 * @param <T> 任何你指定的对象
 */

import android.content.Context;
import android.view.View;

public interface BannerHolder<T> {
    View createView(Context context);

    /**
     * 更新ui
     * 由于viewpager会提前初始化布局,所以当前看到的ui不是该方法初始的ui
     *
     * @param context
     * @param nextPosition 下一个ui的位置
     * @param nextData     下一个ui的数据
     */
    void updateUI(Context context, int nextPosition, T nextData);

}