package cc.catface.clibrary.util.view.viewpager.animation.define;

import android.view.View;

import cc.catface.clibrary.util.view.viewpager.animation.define.base.BaseTransformer;

public class AccordionTransformer extends BaseTransformer {
    @Override protected void onTransform(View view, float position) {
//        view.setPivotX(position < 0 ? 0 : view.getWidth());
//        view.setScaleX(position < 0 ? 1f + position : 1f - position);

        int pageWidth = view.getWidth();

        if (position < -1) {
            view.setAlpha(0);

        } else if (position <= 0) {
            view.setPivotX(0);
            view.setScaleX(1f + position);

        } else if (position <= 1) {
            view.setPivotX(pageWidth);
            view.setScaleX(1f - position);

        } else {
            view.setAlpha(0);
        }
    }
}