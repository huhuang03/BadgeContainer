package com.tonghu.example.badgecontainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 徽章containerView
 * 使用方法:
 * <BadgeContainer>
 *      <The View need badge at top|right/>
 * </BadgeContainer>
 *  only can have a child
 *  layout must be warp_content
 *
 *  custom attribute:
 *      1. badgeContainer_adjust_x: 是否应该以targetView来调整水平位置
 *      2. badgeContainer_padding_left: x偏移
 *      3. badgeContainer_padding_bottom: y偏移
 *
 *
 * Created by tonghu on 5/16/16.
 */
public class BadgeContainer extends RelativeLayout {
    private static final int DEFAULT_BADGE_COLOR = Color.parseColor("#CC00ff00");

    private TextView mBadgeView;
    private int badgeColor;
    private int mPaddingLeft = 0;
    private int mPaddingBottom = 0;
    private int mTargetId = 0;
    private View mTargetView;

    public BadgeContainer(Context context) {
        this(context, null);
    }

    public BadgeContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.badgeColor = DEFAULT_BADGE_COLOR;
        mPaddingLeft = dip2Px(7);
        mPaddingBottom = dip2Px(6);

        setBackgroundColor(Color.parseColor("#eeeeeeee"));

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BadgeContainer, 0, 0);
        try {
            int paddingLeft = a.getDimensionPixelOffset(R.styleable.BadgeContainer_badgeContainer_padding_left, 0);
            int paddingBottom = a.getDimensionPixelOffset(R.styleable.BadgeContainer_badgeContainer_padding_bottom, 0);
            mTargetId  = a.getResourceId(R.styleable.BadgeContainer_badgeContainer_target_id, -1);
            Log.i("tonghu", "BadgeContainer BadgeContainer, mTargetId: " + mTargetId);
            if (paddingLeft > 0) {
                mPaddingLeft = paddingLeft;
            }
            if (paddingBottom > 0) {
                mPaddingBottom = paddingBottom;
            }
        } finally {
            a.recycle();
        }

        Log.i("tonghu", "BadgeContainer BadgeContainer: " + getChildCount());
        addView(createBadgeView());
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (mTargetId > 0) {
            if (child.getId() == mTargetId) {
                mTargetView = child;
            }
        }
    }

    public void setTargetView(View view) {
        mTargetView = view;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mBadgeView.bringToFront();
        Log.i("tonghu", "BadgeContainer onLayout, mTargetView: " + mTargetView);
        if (mTargetView != null) {
//            int left = mTargetView.getRight() - mBadgeView.getMeasuredWidth();
//            int right = left + mBadgeView.getMeasuredWidth();
//            int top = mTargetView.getTop()
            int left = mTargetView.getRight() - mBadgeView.getMeasuredWidth() + mPaddingLeft / 2;
            int right = left + mBadgeView.getMeasuredWidth();
            int top = mTargetView.getTop() - mPaddingBottom / 2;
            int bottom = top + mBadgeView.getMeasuredHeight();
            mBadgeView.layout(left, top, right, bottom);
        } else {
            mBadgeView.setVisibility(View.GONE);
        }
    }

    protected void onLayout1(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
        Log.i("tonghu", "BadgeContainer onLayout, mTargetView: " + mTargetView);
        if (mTargetView != null) {
//            int left = mTargetView.getRight() - mBadgeView.getMeasuredWidth();
//            int right = left + mBadgeView.getMeasuredWidth();
//            int top = mTargetView.getTop()
            mBadgeView.layout(mTargetView.getRight(), mBadgeView.getTop(), mTargetView.getRight() + mBadgeView.getMeasuredWidth(), mBadgeView.getBottom());
        } else {
            mBadgeView.setVisibility(View.GONE);
        }
    }

    private View createBadgeView() {
        mBadgeView = new TextView(getContext());
        mBadgeView.setBackgroundDrawable(getDefaultBackground());
        mBadgeView.setPadding(dip2Px(5), dip2Px(1), dip2Px(5), dip2Px(1));
        return mBadgeView;
    }

    /**
     * 设置显示的徽章数量
     * @param num <=0 表示不显示
     */
    public void setNum(int num) {
        if (num <= 0) {
            mBadgeView.setVisibility(View.GONE);
        } else {
            mBadgeView.setVisibility(View.VISIBLE);
            mBadgeView.setText(num + "");
        }
    }

    /**
     * 设置显示的徽章数量
     * @param num <=0 表示不显示
     */
    public void setNum(String num) {
        int numInt = 0;
        try {
            numInt = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        setNum(numInt);
    }

    private ShapeDrawable getDefaultBackground() {
        int r = 8;
        float[] outerR = new float[]{(float)r, (float)r, (float)r, (float)r, (float)r, (float)r, (float)r, (float)r};
        RoundRectShape rr = new RoundRectShape(outerR, (RectF)null, (float[])null);
        ShapeDrawable drawable = new ShapeDrawable(rr);
        drawable.getPaint().setColor(this.badgeColor);
        return drawable;
    }

    /*
     * converts dip to px
     */
    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
