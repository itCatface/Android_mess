package cc.catface.clibrary.util.view.viewpager.animation.define;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2017/12/2/002.
 */

public class A implements ViewPager.PageTransformer {
    @Override public void transformPage(View view, float position) {
        view.setPivotX(position < 0 ? 0 : view.getWidth());
        view.setScaleX(position < 0 ? 1f + position : 1f - position);


        System.out.println("catface_debug - position is； " + position);
    }
}
