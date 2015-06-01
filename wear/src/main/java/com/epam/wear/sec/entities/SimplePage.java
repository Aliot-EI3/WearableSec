package com.epam.wear.sec.entities;

/**
 * Created by Vladimir_Kharchenko on 5/8/2015.
 */
public class SimplePage {

    private String mTitle;
    private String mText;
    private int mIconId;
    private int mBackgroundId;

    public SimplePage(String title, String text, int iconId, int backgroundId) {
        this.mTitle = title;
        this.mText = text;
        this.mIconId = iconId;
        this.mBackgroundId = backgroundId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmIconId() {
        return mIconId;
    }

    public void setmIconId(int mIconId) {
        this.mIconId = mIconId;
    }

    public int getmBackgroundId() {
        return mBackgroundId;
    }

    public void setmBackgroundId(int mBackgroundId) {
        this.mBackgroundId = mBackgroundId;
    }
}
