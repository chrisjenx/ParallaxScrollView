package com.jenxsol.parallaxscrollviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import couk.jenxsol.parallaxscrollview.views.ParallaxScrollView;
import couk.jenxsol.parallaxscrollviewdemo.R;

public class DemoActivity extends Activity implements OnClickListener
{

    private ParallaxScrollView mScrollView;
    private TextView mFactorText;
    private Button mMinus;
    private Button mPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mScrollView = (ParallaxScrollView) findViewById(R.id.scroll_view);
        mFactorText = (TextView) findViewById(R.id.factor_text);
        mMinus = (Button) findViewById(R.id.minus);
        mMinus.setOnClickListener(this);
        mPlus = (Button) findViewById(R.id.plus);
        mPlus.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        float offset = mScrollView.getParallaxOffset();
        switch (v.getId())
        {
            case R.id.minus:
                offset = mScrollView.getParallaxOffset();
                offset = offset - 0.05f;
                mScrollView.setParallaxOffset(offset);
                offset = mScrollView.getParallaxOffset();
                mFactorText.setText(Float.toString(offset));
                break;

            case R.id.plus:
                offset = mScrollView.getParallaxOffset();
                offset = offset + 0.05f;
                mScrollView.setParallaxOffset(offset);
                offset = mScrollView.getParallaxOffset();
                mFactorText.setText(Float.toString(offset));
                break;

            default:
                break;
        }
        if (offset * 100 <= 10)
        {
            mMinus.setEnabled(false);
            mPlus.setEnabled(true);
        }
        else
        {
            mMinus.setEnabled(true);
            mPlus.setEnabled(true);
        }
    }
}
