package com.mcxtzhang.indexlib.IndexBar.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.helper.IIndexBarDataHelper;
import com.mcxtzhang.indexlib.IndexBar.helper.IndexBarDataHelperImpl;
import com.mcxtzhang.indexlib.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 介绍：索引右侧边栏
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/09/04.
 */

public class IndexBar extends View {

    //#在最后面（默认的数据源）
    public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    //索引数据源
    private List<String> mIndexDatas;


    //View的宽高
    private int mWidth, mHeight;
    //每个index区域的高度
    private int mGapHeight;

    private Paint mPaint;


    //以下是帮助类
    //汉语->拼音，拼音->tag
    private IIndexBarDataHelper mDataHelper;

    //以下边变量是外部set进来的
    private TextView mPressedShowTextView;//用于特写显示正在被触摸的index值
    private boolean mIsSourceDatasAlreadySorted;//源数据 已经有序？
    private List<? extends BaseIndexPinyinBean> mSourceDatas;//Adapter的数据源
    private LinearLayoutManager mLayoutManager;
    private int mHeaderViewCount = 0;


    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private int mPressedBackground;
    private boolean mNeedAdaptIndex;

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()); // 默认索引文字大小
        mPressedBackground = Color.GRAY;    // 按下时的背景色
        mNeedAdaptIndex = true;

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IndexBar, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.IndexBar_textSize)
                textSize = typedArray.getDimensionPixelSize(attr, textSize);
            else if (attr == R.styleable.IndexBar_pressedBackground)
                mPressedBackground = typedArray.getColor(attr, mPressedBackground);
            else if (attr == R.styleable.IndexBar_needAdaptIndex)
                mNeedAdaptIndex = typedArray.getBoolean(attr, mNeedAdaptIndex);

        }

        typedArray.recycle();

        /* 索引是否适应源数据 */
        if (mNeedAdaptIndex) mIndexDatas = new ArrayList<>();
        else mIndexDatas = Arrays.asList(INDEX_STRING);

        /* 初始化画笔 */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);

        /* 实现监听器 */
        setOnIndexPressedListener(new OnIndexPressedListener() {
            @Override public void onIndexPressed(int index, String text) {
                if (mPressedShowTextView != null) {
                    mPressedShowTextView.setAlpha(1.0f);
                    mPressedShowTextView.setVisibility(View.VISIBLE);
                    mPressedShowTextView.setText(text);
                }
                //滑动Rv
                if (mLayoutManager != null) {
                    int position = getPosByTag(text);
                    if (position != -1) {
                        mLayoutManager.scrollToPositionWithOffset(position, 0);
                    }
                }
            }

            @Override public void onMotionEventEnd() {
                //隐藏hintTextView
                if (mPressedShowTextView != null) {
//                    mPressedShowTextView.setVisibility(View.GONE);
                    ObjectAnimator.ofFloat(mPressedShowTextView, "alpha", 1.0f, 0.0f).start();
                }
            }
        });

        mDataHelper = new IndexBarDataHelperImpl();
    }


    /**
     * 根据传入的pos返回tag
     *
     * @param tag
     * @return
     */
    private int getPosByTag(String tag) {
        //add by zhangxutong 2016 09 08 :解决源数据为空 或者size为0的情况,
        if (null == mSourceDatas || mSourceDatas.isEmpty()) {
            return -1;
        }
        if (TextUtils.isEmpty(tag)) {
            return -1;
        }
        for (int i = 0; i < mSourceDatas.size(); i++) {
            if (tag.equals(mSourceDatas.get(i).getBaseIndexTag())) {
                return i + getHeaderViewCount();
            }
        }
        return -1;
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidth = 0, measureHeight = 0;

        @SuppressLint("DrawAllocation") Rect indexBounds = new Rect();  // 存放每个绘制的index的Rect区域
        String data;//每个要绘制的index内容
        for (int i = 0; i < mIndexDatas.size(); i++) {
            data = mIndexDatas.get(i);
            mPaint.getTextBounds(data, 0, data.length(), indexBounds);  // 测量计算文字所在矩形宽高
            measureWidth = Math.max(indexBounds.width(), measureWidth); // 最终宽度为最大宽度
            measureHeight = Math.max(indexBounds.height(), measureHeight);
        }
        measureHeight *= mIndexDatas.size();    // 最终高度为最大高度*size

        /* 最终测量宽高 */
        switch (wMode) {
            case MeasureSpec.EXACTLY:
                measureWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                measureWidth = Math.min(measureWidth, wSize);   // wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        switch (hMode) {
            case MeasureSpec.EXACTLY:
                measureHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(measureHeight, hSize); // wSize此时是父控件能给子View分配的最大空间
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }


    @Override protected void onDraw(Canvas canvas) {
        int t = getPaddingTop();//top的基准点(支持padding)
        String index;//每个要绘制的index内容
        for (int i = 0; i < mIndexDatas.size(); i++) {
            index = mIndexDatas.get(i);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();//获得画笔的FontMetrics，用来计算baseLine。因为drawText的y坐标，代表的是绘制的文字的baseLine的位置
            int baseline = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);//计算出在每格index区域，竖直居中的baseLine值
            canvas.drawText(index, mWidth / 2 - mPaint.measureText(index) / 2, t + mGapHeight * i + baseline, mPaint);//调用drawText，居中显示绘制index
        }
    }


    @SuppressLint("ClickableViewAccessibility") @Override public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(mPressedBackground);//手指按下时背景变色
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                /* 计算落点所在区域 */
                int pressI = (int) ((y - getPaddingTop()) / mGapHeight);
                /* 越界处理 */
                if (pressI < 0) pressI = 0;
                else if (pressI >= mIndexDatas.size()) pressI = mIndexDatas.size() - 1;

                /* 监听回调 */
                if (null != mOnIndexPressedListener && pressI > -1 && pressI < mIndexDatas.size())
                    mOnIndexPressedListener.onIndexPressed(pressI, mIndexDatas.get(pressI));

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                setBackgroundResource(android.R.color.transparent);

                /* 监听回调 */
                if (null != mOnIndexPressedListener) mOnIndexPressedListener.onMotionEventEnd();

                break;
        }

        return true;
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if (null == mIndexDatas || mIndexDatas.isEmpty()) return;

        computeGapHeight();
    }

    public int getHeaderViewCount() {
        return mHeaderViewCount;
    }

    /**
     * 设置Headerview的Count
     *
     * @param headerViewCount
     * @return
     */
    public IndexBar setHeaderViewCount(int headerViewCount) {
        mHeaderViewCount = headerViewCount;
        return this;
    }


    /********************************************* 触摸索引的监听器 *********************************************/
    public interface OnIndexPressedListener {
        void onIndexPressed(int index, String text);

        void onMotionEventEnd();
    }

    private OnIndexPressedListener mOnIndexPressedListener;

    public void setOnIndexPressedListener(OnIndexPressedListener mOnIndexPressedListener) {
        this.mOnIndexPressedListener = mOnIndexPressedListener;
    }

    /********************************************* 暴露的设置方法 *********************************************/
    public IndexBar setLayoutManager(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        return this;
    }

    public IndexBar setPressedShowTextView(TextView PressedShowTextView) {
        this.mPressedShowTextView = PressedShowTextView;
        return this;
    }

    public IndexBar setSourceData(List<? extends BaseIndexPinyinBean> sourceData) {
        this.mSourceDatas = sourceData;
        initSourceDatas();
        return this;
    }

    public IndexBar setIsSourceDatasAlreadySorted(boolean isSourceDatasAlreadySorted) {
        this.mIsSourceDatasAlreadySorted = isSourceDatasAlreadySorted;
        return this;
    }

    public IndexBar setDataHelper(IIndexBarDataHelper dataHelper) {
        mDataHelper = dataHelper;
        return this;
    }

    public IIndexBarDataHelper getDataHelper() {
        return mDataHelper;
    }

    /**
     * 初始化原始数据源 && 初始化索引数据源
     */
    private void initSourceDatas() {
        if (null == mSourceDatas || mSourceDatas.isEmpty()) return;

        if (!mIsSourceDatasAlreadySorted) mDataHelper.sortSourceDatas(mSourceDatas);
        else {
            mDataHelper.chinese2Pinyin(mSourceDatas);
            mDataHelper.pinyin2Tag(mSourceDatas);
        }

        if (mNeedAdaptIndex) {
            mDataHelper.getSortedIndexDatas(mSourceDatas, mIndexDatas);
            computeGapHeight();
        }
    }

    /**
     * 在数据源改变 & 控件size改变 --> 新的mGapHeight
     */
    private void computeGapHeight() {
        mGapHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mIndexDatas.size();
    }
}
