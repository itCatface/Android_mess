package cc.catface.clibrary.util.view.simple;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * 控制滑动速度的LinearLayoutManager
 */
public class LinearLayoutMangerScrollSpeed extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 0.03f;
    private float MILLISECONDS_PER_INCH_SLOW = 0.3f;
    private Context contxt;

    public LinearLayoutMangerScrollSpeed(Context context) {
        super(context);
        this.contxt = context;
    }


    @Override public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override public PointF computeScrollVectorForPosition(int targetPosition) {
                return LinearLayoutMangerScrollSpeed.this.computeScrollVectorForPosition(targetPosition);
            }

            //This returns the milliseconds it takes to
            //scroll one pixel.
            @Override protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                System.out.println("catface_debug" + MILLISECONDS_PER_INCH);
                return MILLISECONDS_PER_INCH / displayMetrics.density;
                //返回滑动一个pixel需要多少毫秒
            }
        };


        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }


    public void setScrollSpeed(Float ratio) {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        //0.3f是自己估摸的一个值，可以根据不同需求自己修改
//        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * ratio;
        MILLISECONDS_PER_INCH = ratio;
    }
}