package com.epam.wear.sec;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.epam.wear.sec.adapter.SampleGridPagerAdapter;


/**
 * Created by Vladimir_Kharchenko on 5/8/2015.jgkuhkhkhkhkhk
 */
public class GridActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        final GridViewPager mGridPager = (GridViewPager) findViewById(R.id.pager);
        mGridPager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));
    }
}