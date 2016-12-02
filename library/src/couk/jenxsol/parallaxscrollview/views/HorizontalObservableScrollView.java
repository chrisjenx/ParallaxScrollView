package couk.jenxsol.parallaxscrollview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class HorizontalObservableScrollView extends HorizontalScrollView
{
    private ScrollCallbacks mCallbacks;

    public HorizontalObservableScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null)
        {
            mCallbacks.onScrollChanged(l, t, oldl, oldt);
        }
    }

    @Override
    public int computeHorizontalScrollRange()
    {
        return super.computeHorizontalScrollRange();
    }

    public void setCallbacks(ScrollCallbacks listener)
    {
        mCallbacks = listener;
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
    }
}
