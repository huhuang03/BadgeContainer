package com.tonghu.badgecontainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by york on 5/16/16.
 */
public class BadgeContainer extends RelativeLayout {

    public static final int DIRECTION_INNER = 0;
    public static final int DIRECTION_OUTTER = 1;

    /**
     * 对于增长的部分, 是应该往外伸缩还是应该往里伸缩, 默认向里伸缩
     */
    @IntDef({DIRECTION_INNER, DIRECTION_OUTTER})
    public @interface Direction{}

    private @Direction int mDirection = DIRECTION_INNER;

private int mDefaultPaddingLeft = 4;    // dp
    private int mDefaultPaddingBottom = 5;  // dp

    private TextView mBadgeView;
    private int mPaddingLeft = 0;
    private int mPaddingBottom = 0;
    private int mTargetId = 0;
    private View mTargetView;

    private Drawable mDotBg;

    public BadgeContainer(Context context) {
        this(context, null);
    }

    public BadgeContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPaddingLeft = dip2Px(mDefaultPaddingLeft);
        mPaddingBottom = dip2Px(mDefaultPaddingBottom);

        int count = 0;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BadgeContainer, 0, 0);
        try {
            int paddingLeft = a.getDimensionPixelOffset(R.styleable.BadgeContainer_badgeContainer_padding_left, 0);
            int paddingBottom = a.getDimensionPixelOffset(R.styleable.BadgeContainer_badgeContainer_padding_bottom, 0);
            mTargetId  = a.getResourceId(R.styleable.BadgeContainer_badgeContainer_target_id, -1);

            int dic = a.getInt(R.styleable.BadgeContainer_badgeContainer_direction, DIRECTION_INNER);
            if (dic == DIRECTION_INNER) {
                mDirection = DIRECTION_INNER;
            } else {
                mDirection = DIRECTION_OUTTER;
            }

            count = a.getInt(R.styleable.BadgeContainer_badgeContainer_count, 0);
            if (paddingLeft > 0) {
                mPaddingLeft = paddingLeft;
            }
            if (paddingBottom > 0) {
                mPaddingBottom = paddingBottom;
            }
            Drawable tmpDotBg = a.getDrawable(R.styleable.BadgeContainer_badgeContainer_badge_bg);
            if (tmpDotBg != null) {
                mDotBg = tmpDotBg;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDotBg = getResources().getDrawable(R.drawable.bg_badge_container_dot, getContext().getTheme());
                } else {
                    mDotBg = getResources().getDrawable(R.drawable.bg_badge_container_dot);
                }
            }
        } finally {
            a.recycle();
        }

        addView(createBadgeView());

        setNum(count);
    }

    /**
     * set the target view which will show the badge on it
     * @param view the view to show badge
     */
    public void setTargetView(View view) {
        mTargetView = view;
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (mTargetId > 0 && mTargetView == null) {
            View targetViewRecursion = findTargetViewRecursion(child);
            if (targetViewRecursion != null) {
                mTargetView = targetViewRecursion;
            }
        }
    }

    private View findTargetViewRecursion(View view) {
        if (view.getId() == mTargetId) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = ((ViewGroup) view);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View targetViewRecursion = findTargetViewRecursion(viewGroup.getChildAt(i));
                if (targetViewRecursion != null) {
                    return targetViewRecursion;
                }
            }
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mBadgeView != null) {
            mBadgeView.bringToFront();
            if (mTargetView != null && mBadgeView.getVisibility() == View.VISIBLE) {
                int left = 0;
                int right = 0;
                int top = 0;
                int bottom = 0;
                if (mDirection == DIRECTION_INNER) {
                    left = mTargetView.getRight() - mBadgeView.getMeasuredWidth() + mPaddingLeft - mTargetView.getPaddingRight();
                    right = left + mBadgeView.getMeasuredWidth();
                    top = mTargetView.getTop() - mPaddingBottom;
                    bottom = top + mBadgeView.getMeasuredHeight();
                } else if (mDirection == DIRECTION_OUTTER) {
                    left = mTargetView.getRight() - mPaddingLeft - mTargetView.getPaddingRight();
                    right = left + mBadgeView.getMeasuredWidth();
                    bottom = mTargetView.getTop() + mPaddingBottom;
                    top = bottom - mBadgeView.getMeasuredHeight();
                }
                mBadgeView.layout(left, top, right, bottom);
                mBadgeView.invalidate();
            } else {
                mBadgeView.setVisibility(View.GONE);
            }
        }
    }

    private View createBadgeView() {
        int radius = dip2Px(8);
        int padding = dip2Px(4);

        mBadgeView = new TextView(getContext());
        setBackground(mBadgeView);
        mBadgeView.setTextColor(Color.WHITE);
        mBadgeView.setTextSize(10);
        mBadgeView.setGravity(Gravity.CENTER);
        mBadgeView.setPadding(padding, 0, padding, 0);
        return mBadgeView;
    }

    private void setBackground(View badgeView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            badgeView.setBackground(mDotBg);
        } else {
            badgeView.setBackgroundDrawable(mDotBg);
        }
    }

    /**
     * 设置显示的徽章数量
     * @param num less then 0 表示不显示
     */
    public void setNum(int num) {
        if (num <= 0) {
            mBadgeView.setVisibility(View.GONE);
        } else {
            mBadgeView.setVisibility(View.VISIBLE);
            if (num < 100) {
                mBadgeView.setText(num + "");
            } else {
                mBadgeView.setText("99+");
            }
        }
    }

    /**
     * 设置显示的徽章数量
     * @param num less then 0 表示不显示
     */
    public void setNum(String num) {
        int numInt = 0;
        if (!TextUtils.isEmpty(num)) {
            try {
                numInt = Integer.parseInt(num);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        setNum(numInt);
    }

    /*
     * converts dip to px
     */
    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
