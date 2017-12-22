package cc.catface.kotlin.view.group;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.catface.kotlin.R;


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
public class WeatherTopView extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout rl_root;
    private TextView tv_left_one;
    private TextView tv_left_two;
    private TextView tv_title;
    private TextView tv_right_two;
    private TextView tv_right_one;


    public WeatherTopView(Context context) {
        this(context, null);
    }

    public WeatherTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.view_weather_top, null);
        rl_root = (RelativeLayout) view.findViewById(R.id.rl_root);
        tv_left_one = (TextView) view.findViewById(R.id.tv_left_one);
        tv_left_two = (TextView) view.findViewById(R.id.tv_left_two);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_right_two = (TextView) view.findViewById(R.id.tv_right_two);
        tv_right_one = (TextView) view.findViewById(R.id.tv_right_one);


        rl_root.setBackgroundColor(R.drawable.shape_bt_main_press);
        tv_title.setTextColor(Color.WHITE);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
        tv_left_one.setTypeface(font);
        tv_left_two.setTypeface(font);
        tv_right_two.setTypeface(font);
        tv_right_one.setTypeface(font);

        event();

        this.addView(view);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void event() {
        tv_title.setOnClickListener(this);
        tv_left_one.setOnClickListener(this);
        tv_left_two.setOnClickListener(this);
        tv_right_two.setOnClickListener(this);
        tv_right_one.setOnClickListener(this);
    }


    public void setLeftOneIcon(int iconId) {
        tv_left_one.setText(iconId);
    }

    public void setLeftTwoIcon(int iconId) {
        tv_left_two.setText(iconId);
    }

    public void setRightTwoIcon(int iconId) {
        tv_right_two.setText(iconId);
    }

    public void setRightOneIcon(int iconId) {
        tv_right_one.setText(iconId);
    }


    /**
     * Listener
     */
    public interface OnClickListener {
        void onClick(View view);
    }

    private OnClickListener mOnClickListener = null;

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override public void onClick(View view) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(view);
        }
    }
}
