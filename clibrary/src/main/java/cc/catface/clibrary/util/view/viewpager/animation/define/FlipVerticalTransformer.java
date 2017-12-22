package cc.catface.clibrary.util.view.viewpager.animation.define;

import android.view.View;

import cc.catface.clibrary.util.view.viewpager.animation.define.base.BaseTransformer;

public class FlipVerticalTransformer extends BaseTransformer {
    @Override protected void onTransform(View view, float position) {
        final float rotation = -180f * position;
        view.setAlpha(rotation > 90f || rotation < -90f ? 0f : 1f);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationX(rotation);
    }
}