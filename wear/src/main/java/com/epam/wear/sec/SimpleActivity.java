package com.epam.wear.sec;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Vladimir_Kharchenko on 5/29/2015.
 */
public class SimpleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
    }
}
