package couk.jenxsol.parallaxscrollview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import couk.jenxsol.parallaxscrollview.R;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
public class HorizontalParallaxScrollView extends ViewGroup
{
    private static final int DEFAULT_CHILD_GRAVITY = Gravity.CENTER_HORIZONTAL;

    private static final String TAG = "ParallaxScrollView";

    private static float PARALLAX_OFFSET_DEFAULT = 0.2f;
    
    /**
     * By how much should the background move to the foreground
     */
    private float mParallaxOffset = PARALLAX_OFFSET_DEFAULT;

    private View mBackground;
    private HorizontalObservableScrollView mScrollView;
    private final ScrollCallbacks mScrollCallbacks = new ScrollCallbacks()
    {
        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt)
        {
            requestLayout();
        }
    };

    // Layout stuff

    private int mBackgroundRight;
    private int mBackgroundBottom;
    /**
     * Height of the Foreground ScrollView Content
     */
    private int mScrollContentWidth = 0;
    /**
     * Height of the ScrollView, should be the same as this view
     */
    private int mScrollViewWidth = 0;
    /**
     * The multipler by how much to move the background to the foreground
     */
    private float mScrollDiff = 0f;

    public HorizontalParallaxScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ParallaxScrollView, 0, 0);

        try
        {
            setParallaxOffset(a.getFloat(R.styleable.ParallaxScrollView_parallexOffset,
                    PARALLAX_OFFSET_DEFAULT));
        }
        catch (Exception e)
        {
        }
        finally
        {
            a.recycle();
        }
    }

    public HorizontalParallaxScrollView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public HorizontalParallaxScrollView(Context context)
    {
        this(context, null);
    }

    @Override
    public void addView(View child)
    {
        if (getChildCount() > 1)
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");

        super.addView(child);
    }

    @Override
    public void addView(View child, int index)
    {
        if (getChildCount() > 1)
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, LayoutParams params)
    {
        if (getChildCount() > 1)
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, int width, int height)
    {
        if (getChildCount() > 1)
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, LayoutParams params)
    {
        if (getChildCount() > 1)
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");
        super.addView(child, params);
    }

    /**
     * Set the offset
     *
     * @param offset
     *            a number greater than 0.0
     */
    public void setParallaxOffset(float offset)
    {
        // Make sure we only get to .05 of a floating number
        offset = (float) Math.rint(offset * 100) / 100;
        if (offset > 0.0)
            mParallaxOffset = offset;
        else
            mParallaxOffset = PARALLAX_OFFSET_DEFAULT;

        requestLayout();
    }

    /**
     * Get the current offset
     *
     * @return
     */
    public float getParallaxOffset()
    {
        return mParallaxOffset;
    }

    /**
     * Sort the views out after they have been inflated from a layout
     */
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        if (getChildCount() > 2)
        {
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");
        }
        organiseViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mScrollView != null)
        {
            measureChild(mScrollView,
                    MeasureSpec.makeMeasureSpec(
                            MeasureSpec.getSize(widthMeasureSpec),
                            MeasureSpec.AT_MOST
                    ),
                    MeasureSpec.makeMeasureSpec(
                            MeasureSpec.getSize(heightMeasureSpec),
                            MeasureSpec.AT_MOST
                    )
            );

            mScrollContentWidth = mScrollView.getChildAt(0).getMeasuredWidth();
            mScrollViewWidth = mScrollView.getMeasuredWidth();

        }
        if (mBackground != null)
        {
            int minWidth = 0;
            minWidth = (int) (mScrollViewWidth + mParallaxOffset
                    * (mScrollContentWidth - mScrollViewWidth));
            minWidth = Math.max(minWidth, MeasureSpec.getSize(widthMeasureSpec));

            measureChild(mBackground,
                    MeasureSpec.makeMeasureSpec(
                            minWidth,
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(
                            MeasureSpec.getSize(heightMeasureSpec),
                            MeasureSpec.EXACTLY));

            mBackgroundRight = getLeft() + mBackground.getMeasuredWidth();
            mBackgroundBottom = getTop() + mBackground.getMeasuredHeight();

            mScrollDiff = (float) (mBackground.getMeasuredWidth() - mScrollViewWidth)
                    / (float) (mScrollContentWidth - mScrollViewWidth);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        if (mScrollView != null && mScrollView.getVisibility() != GONE)
        {
            final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mScrollView
                    .getLayoutParams();

            final int width = mScrollView.getMeasuredWidth();
            final int height = mScrollView.getMeasuredHeight();

            int childLeft;
            int childTop;

            int gravity = lp.gravity;
            if (gravity == -1)
            {
                gravity = DEFAULT_CHILD_GRAVITY;
            }

            final int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

            switch (horizontalGravity)
            {
                case Gravity.LEFT:
                    childLeft = parentLeft + lp.leftMargin;
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = parentLeft + (parentRight - parentLeft - width) / 2 + lp.leftMargin
                            - lp.rightMargin;
                    break;
                case Gravity.RIGHT:
                    childLeft = parentRight - width - lp.rightMargin;
                    break;
                default:
                    childLeft = parentLeft + lp.leftMargin;
            }

            switch (verticalGravity)
            {
                case Gravity.TOP:
                    childTop = parentTop + lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    childTop = parentTop + (parentBottom - parentTop - height) / 2 + lp.topMargin
                            - lp.bottomMargin;
                    break;
                case Gravity.BOTTOM:
                    childTop = parentBottom - height - lp.bottomMargin;
                    break;
                default:
                    childTop = parentTop + lp.topMargin;
            }

            mScrollView.layout(childLeft, childTop, childLeft + width, childTop + height);

        }

        if (mBackground != null)
        {
            final int scrollXCenterOffset = -mScrollView.getScrollX();
            final int offset = (int) (scrollXCenterOffset * mScrollDiff);
            mBackground.layout(offset, getTop(), offset + mBackgroundRight, mBackgroundBottom);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p)
    {
        return new FrameLayout.LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                Gravity.CENTER_HORIZONTAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(LayoutParams p)
    {
        return p instanceof FrameLayout.LayoutParams;
    }

    /**
     * Take the direct children and sort them
     */
    private void organiseViews()
    {
        if (getChildCount() <= 0) return;

        if (getChildCount() == 1)
        {
            // Get the only child
            final View foreground = getChildAt(0);
            organiseBackgroundView(null);
            organiseForegroundView(foreground);
        }
        else if (getChildCount() == 2)
        {
            final View background = getChildAt(0);
            final View foreground = getChildAt(1);

            organiseBackgroundView(background);
            organiseForegroundView(foreground);
        }
        else
        {
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");
        }
    }

    private void organiseBackgroundView(final View background)
    {
        mBackground = background;
    }

    private void organiseForegroundView(final View foreground)
    {
        final int insertPos = getChildCount() - 1;

        // See if its a observable scroll view?
        if (foreground instanceof HorizontalObservableScrollView)
        {
            // Attach the callback to it.
            mScrollView = (HorizontalObservableScrollView) foreground;
        }
        else if (foreground instanceof ViewGroup && !(foreground instanceof ScrollView))
        {
            // See if it is a view group but not a scroll view and wrap it
            // with an observable ScrollView
            mScrollView = new HorizontalObservableScrollView(getContext(), null);
            removeView(foreground);
            mScrollView.addView(foreground);
            addView(mScrollView, insertPos);
        }
        else if (foreground instanceof ScrollView)
        {
            final View child;
            if (((ScrollView) foreground).getChildCount() > 0)
                child = ((ScrollView) foreground).getChildAt(0);
            else
                child = null;

            mScrollView = new HorizontalObservableScrollView(getContext(), null);
            removeView(foreground);
            if (child != null) mScrollView.addView(child);
            addView(mScrollView, insertPos);
        }
        else if (foreground instanceof View)
        {
            mScrollView = new HorizontalObservableScrollView(getContext(), null);
            removeView(foreground);
            mScrollView.addView(foreground);
            addView(mScrollView, insertPos);

        }
        if (mScrollView != null)
        {
            mScrollView.setLayoutParams(foreground.getLayoutParams());
            mScrollView.setCallbacks(mScrollCallbacks);
            mScrollView.setFillViewport(true);
        }
    }

}