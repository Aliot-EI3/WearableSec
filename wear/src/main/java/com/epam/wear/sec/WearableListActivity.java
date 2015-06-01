package com.epam.wear.sec;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import com.epam.wear.sec.adapter.WearableListAdapter;

import static android.support.wearable.view.WearableListView.*;

/**
 * Created by Vladimir_Kharchenko on 5/29/2015.
 */
public class WearableListActivity extends Activity implements ClickListener {

        // Sample dataset for the list
        String[] elements = { "Blah-blah.. 1", "Blah-blah.. 2","Blah-blah.. 3", "Blah-blah.. 4", "Blah-blah.. 5", "Blah-blah..6" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_list);

        // Get the list component from the layout of the activity
        WearableListView listView =
                (WearableListView) findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        listView.setAdapter(new WearableListAdapter(this, elements));

        // Set a click listener
        listView.setClickListener(this);
    }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
        // use this data to complete some action ...
    }

    @Override
    public void onTopEmptyRegionClick() {
    }
}
