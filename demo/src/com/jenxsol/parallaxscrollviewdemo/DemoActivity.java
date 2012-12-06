package com.jenxsol.parallaxscrollviewdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import couk.jenxsol.parallaxscrollviewdemo.R;

public class DemoActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_demo, menu);
        return true;
    }

}
