package cc.catface.clibrary.util.view.recyclerview.drag;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


public class DragItemCallback extends ItemTouchHelper.Callback {

    /* 控制左右滑动 */
    private static final boolean FLAG_SWIPE = false;

    private DragItemAdapter mDragItemAdapter;

    public DragItemCallback(DragItemAdapter dragItemAdapter) {
        mDragItemAdapter = dragItemAdapter;
    }


    @Override public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = FLAG_SWIPE ? ItemTouchHelper.START | ItemTouchHelper.END : 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = FLAG_SWIPE ? ItemTouchHelper.START | ItemTouchHelper.END : 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }


    @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int startPosition = viewHolder.getAdapterPosition();
        int endPosition = target.getAdapterPosition();

        mDragItemAdapter.onMove(startPosition, endPosition);
        return true;
    }

    @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        System.out.println("catface_debug" + direction);
        boolean toLeft = direction == 16;
        int position = viewHolder.getAdapterPosition();

        mDragItemAdapter.onSwiped(position, toLeft);
    }

    @Override public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();  // 滑动时改变Item的透明度
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (background == null && bkcolor == -1) {
                Drawable drawable = viewHolder.itemView.getBackground();
                if (drawable == null) {
                    bkcolor = 0;
                } else {
                    background = drawable;
                }
            }
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(1.0f);
        if (background != null) viewHolder.itemView.setBackgroundDrawable(background);
        if (bkcolor != -1) viewHolder.itemView.setBackgroundColor(bkcolor);
        //viewHolder.itemView.setBackgroundColor(0);

        if (mDragItemAdapter != null) {
            mDragItemAdapter.onFinishDrag();
        }
    }

    private Drawable background = null;
    private int bkcolor = -1;



    /************************** 暴露[拖拽位置变化 & 拖拽完成 & 左划右划] *************************/
    public interface DragItemAdapter {
        void onMove(int fromPosition, int toPosition);

        void onFinishDrag();

        void onSwiped(int position, boolean toLeft);
    }
}
