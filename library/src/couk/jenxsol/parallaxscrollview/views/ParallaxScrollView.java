package couk.jenxsol.parallaxscrollview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import couk.jenxsol.parallaxscrollview.views.ObservableScrollView.ScrollCallbacks;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
public class ParallaxScrollView extends ViewGroup
{
    private static final String TAG = "ParallaxScrollView";

    private static float PARALLAX_OFFSET_DEFAULT = 0.2f;

    /**
     * By how much should the background move to the foreground
     */
    private float mParallaxOffset = PARALLAX_OFFSET_DEFAULT;

    private View mBackground;
    private final Rect mBackgroundRect = new Rect();
    private ObservableScrollView mScrollView;
    private final ScrollCallbacks mScrollCallbacks = new ScrollCallbacks()
    {
        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt)
        {

        }
    };

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ParallaxScrollView(Context context)
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
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params)
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
    public void addView(View child, android.view.ViewGroup.LayoutParams params)
    {
        if (getChildCount() > 1)
            throw new IllegalStateException("ParallaxScrollView can host only two direct children");
        super.addView(child, params);
    }

    public void setParallaxOffset(float offset)
    {
        if (offset >= 0 && offset <= 1)
            mParallaxOffset = offset;
        else
            mParallaxOffset = PARALLAX_OFFSET_DEFAULT;
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

        if (mBackground != null)
        {
            measureChild(mBackground, MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),
                            MeasureSpec.UNSPECIFIED));
            mBackgroundCentreOffset = -(mBackground.getMeasuredHeight() / 2)
                    + (getMeasuredHeight() / 2);
            mBackgroundRight = getLeft() + mBackground.getMeasuredWidth();
            mBackgroundBottom = getTop() + mBackground.getMeasuredHeight();
        }
        if (mScrollView != null)
        {
            measureChild(mScrollView, MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),
                            MeasureSpec.AT_MOST));
            mScrollContentHeight = mScrollView.getChildAt(0).getMeasuredHeight();
            mScrollViewHeight = mScrollView.getMeasuredHeight();

        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if (mBackground != null)
        {
            final int offset = (int) (mBackgroundCentreOffset + (mParallaxOffset * mScrollView
                    .getScrollY()));
            mBackground.layout(0, offset, mBackgroundRight, offset + mBackgroundBottom);
        }
        if (mScrollView != null)
        {
            mScrollView.layout(getLeft(), getTop(), getLeft() + mScrollView.getMeasuredWidth(),
                    getTop() + mScrollView.getMeasuredHeight());

        }
    }

    private int mBackgroundRight;
    private int mBackgroundBottom;
    private int mBackgroundCentreOffset = 0;
    private int mScrollContentHeight = 0;
    private int mScrollViewHeight = 0;

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        if (mBackground != null)
        {
            final int scrollYCenterOffset = (mScrollContentHeight / 2)
                    - (mScrollView.getScrollY() + mScrollViewHeight / 2);
            final int offset = (int) (mBackgroundCentreOffset + (mParallaxOffset * scrollYCenterOffset));
            // Log.d(TAG, "Layout Scroll Y: " + scrollYCenterOffset +
            // " Background Offset:" + offset);
            mBackground.layout(getLeft(), offset, mBackgroundRight, offset + mBackgroundBottom);
        }
        super.dispatchDraw(canvas);
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
            final View forground = getChildAt(0);
            organiseBackgroundView(null);
            organiseForgroundView(forground);
        }
        else if (getChildCount() == 2)
        {
            final View background = getChildAt(0);
            final View foreground = getChildAt(1);

            organiseBackgroundView(background);
            organiseForgroundView(foreground);
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

    private void organiseForgroundView(final View forground)
    {
        final int insertPos = getChildCount() - 1;

        // See if its a observable scroll view?
        if (forground instanceof ObservableScrollView)
        {
            // Attach the callback to it.
            mScrollView = (ObservableScrollView) forground;
            mScrollView.setCallbacks(mScrollCallbacks);
        }
        else if (forground instanceof ViewGroup && !(forground instanceof ScrollView))
        {
            // See if it is a view group but not a scroll view and wrap it
            // with an observable ScrollView
            mScrollView = new ObservableScrollView(getContext(), null);
            removeView(forground);
            mScrollView.addView(forground);
            addView(mScrollView, insertPos);
            mScrollView.setCallbacks(mScrollCallbacks);
        }
        else if (forground instanceof ScrollView)
        {
            final View child;
            if (((ScrollView) forground).getChildCount() > 0)
                child = ((ScrollView) forground).getChildAt(0);
            else
                child = null;

            mScrollView = new ObservableScrollView(getContext(), null);
            removeView(forground);
            if (child != null) mScrollView.addView(child);
            addView(mScrollView, insertPos);
            mScrollView.setCallbacks(mScrollCallbacks);
        }
        else if (forground instanceof View)
        {
            mScrollView = new ObservableScrollView(getContext(), null);
            removeView(forground);
            mScrollView.addView(forground);
            addView(mScrollView, insertPos);
            mScrollView.setCallbacks(mScrollCallbacks);
        }

    }
}