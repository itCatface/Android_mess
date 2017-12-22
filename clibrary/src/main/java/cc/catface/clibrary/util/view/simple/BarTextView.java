package cc.catface.clibrary.util.view.simple;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by wyh
 */

public class BarTextView extends AppCompatTextView {
    public BarTextView(Context context) {
        this(context, null);
    }

    public BarTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 强制让TextView具有焦点
    @Override public boolean isFocused() {
        return true;
    }
}
