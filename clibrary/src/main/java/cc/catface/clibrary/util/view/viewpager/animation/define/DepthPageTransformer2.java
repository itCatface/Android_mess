package cc.catface.clibrary.util.view.viewpager.animation.define;

import android.view.View;

import cc.catface.clibrary.util.view.viewpager.animation.define.base.BaseTransformer;

public class DepthPageTransformer2 extends BaseTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override protected void onTransform(View view, float position) {
        if (position <= 0f) {
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        } else if (position <= 1f) {
            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setAlpha(1 - position);
            view.setPivotY(0.5f * view.getHeight());
            view.setTranslationX(view.getWidth() * -position);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }

    @Override protected boolean isPagingEnabled() {
        return true;
    }
}