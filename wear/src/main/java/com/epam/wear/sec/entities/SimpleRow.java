package com.epam.wear.sec.entities;

import java.util.ArrayList;

/**
 * Created by Vladimir_Kharchenko on 5/8/2015.
 */
public class SimpleRow {

    private ArrayList<SimplePage> mPagesRow;

    public SimpleRow() {
        this.mPagesRow =  new ArrayList<SimplePage>();
    }

    public void addPages(SimplePage page) {
        mPagesRow.add(page);
    }

    public SimplePage getPages(int index) {
        return mPagesRow.get(index);
    }

    public int size(){
        return mPagesRow.size();
    }
}
