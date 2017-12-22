package cc.catface.clibrary.util.view.recyclerview.swipe;

import android.support.v7.widget.RecyclerView;

/**
 * Created by AItsuki on 2017/7/11.
 * SwipeItemListener
 */
public interface SwipeItemListener {
    void onItemClick(RecyclerView.ViewHolder holder, int position, String content);

    void onLeftMenuClick(int position);

    void onRightMenuClick(int position);
}
