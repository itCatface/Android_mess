package cc.catface.clibrary.util.view.viewpager.animation.define;

import android.view.View;

import cc.catface.clibrary.util.view.viewpager.animation.define.base.BaseTransformer;

public class StackTransformer extends BaseTransformer {
    @Override protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
    }
}