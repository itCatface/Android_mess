package cc.catface.kotlin.view.viewpagerbanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */

public class ViewPagerBanner extends FrameLayout {

    private ViewPager mVp;

    @SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {

        }
    };



    public ViewPagerBanner(Context context) {
        this(context, null);
    }

    public ViewPagerBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context ctx) {
        mVp = new ViewPager(ctx);
    }


}
