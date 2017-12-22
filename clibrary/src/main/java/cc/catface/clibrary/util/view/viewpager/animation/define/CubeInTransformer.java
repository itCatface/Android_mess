package cc.catface.clibrary.util.view.viewpager.animation.define;

import android.view.View;

import cc.catface.clibrary.util.view.viewpager.animation.define.base.BaseTransformer;

public class CubeInTransformer extends BaseTransformer {
    @Override protected void onTransform(View view, float position) {
        // Rotate the fragment on the left or right edge
        view.setPivotX(position > 0 ? 0 : view.getWidth());
        view.setPivotY(0);
        view.setRotationY(-90f * position);
    }

    @Override public boolean isPagingEnabled() {
        return true;
    }
}