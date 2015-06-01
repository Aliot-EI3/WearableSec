package com.epam.wear.sec.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.epam.wear.sec.R;
import com.epam.wear.sec.entities.SimplePage;
import com.epam.wear.sec.entities.SimpleRow;

import java.util.ArrayList;


/**
 * Created by Vladimir_Kharchenko on 5/8/2015.
 */
public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context context;
    private ArrayList<SimpleRow> pages;

    public SampleGridPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initPages();
    }

    private void initPages() {
        pages = new ArrayList<SimpleRow>();

        SimpleRow row1 = new SimpleRow();
        row1.addPages(new SimplePage("Title1", "Text1", R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        row1.addPages(new SimplePage("Title2", "Text2", R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        SimpleRow row2 = new SimpleRow();
        row2.addPages(new SimplePage("Title3", "Text3", R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        SimpleRow row3 = new SimpleRow();
        row3.addPages(new SimplePage("Title4", "Text4", R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        SimpleRow row4 = new SimpleRow();
        row4.addPages(new SimplePage("Title5", "Text5", R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        row4.addPages(new SimplePage("Title6", "Text6", R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        pages.add(row1);
        pages.add(row2);
        pages.add(row3);
        pages.add(row4);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        SimplePage page = ((SimpleRow) pages.get(row)).getPages(col);
        CardFragment fragment = CardFragment.create(page.getmTitle(), page.getmText(), page.getmIconId());
        return fragment;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        SimplePage page = ((SimpleRow) pages.get(row)).getPages(col);
        return context.getResources().getDrawable(page.getmBackgroundId());
    }

    @Override
    public int getRowCount() {
        return pages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return pages.get(row).size();
    }
}