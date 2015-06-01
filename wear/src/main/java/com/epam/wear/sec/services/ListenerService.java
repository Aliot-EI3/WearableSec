package com.epam.wear.sec.services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.epam.wear.sec.BoxInsetActivity;
import com.epam.wear.sec.GridActivity;
import com.epam.wear.sec.SimpleActivity;
import com.epam.wear.sec.WatchViewStubActivity;
import com.epam.wear.sec.WearableListActivity;
import com.epam.wear.sec.utils.Action;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vladimir Kharchenko on 01.06.2015.
 */
public class ListenerService extends WearableListenerService {

    private static final String WEARABLE_DATA_PATH = "/wearable_data";

    private static final String ASSERT_DATA_PATH = "/image";

    public static final String WEAR_CAPABILITY_MESSAGE_PATH = "/wear_capability";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_DATA_PATH)) {
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    String actionValue = dataMap.getString("action");
                    Action action = Action.valueOf(actionValue);
                    showActivity(action);
                    Log.v("myTag", "DataMap received on watch: " + dataMap);
                } else if (path.equals(ASSERT_DATA_PATH)){
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    Asset profileAsset = dataMapItem.getDataMap().getAsset("profileImage");
                    Bitmap bitmap = loadBitmapFromAsset(profileAsset);
                    bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.3), (int)(bitmap.getHeight()*0.3), true);
                    Intent intent = new Intent(this, WatchViewStubActivity.class);
                    intent.putExtra(WatchViewStubActivity.BITMAP_EXTRA, bitmap);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }
    }

    private void showActivity(Action action){
        Intent intent = null;
        switch (action){
            case SHOW_BOX:
                intent = new Intent(this, BoxInsetActivity.class);
                break;
            case SHOW_SIMPLE:
                intent = new Intent(this, SimpleActivity.class);
                break;
            case SHOW_GRID:
                intent = new Intent(this, GridActivity.class);
                break;
            case SHOW_LIST:
                intent = new Intent(this, WearableListActivity.class);
                break;
            case SHOW_STUB:
                intent = new Intent(this, WatchViewStubActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase( WEAR_CAPABILITY_MESSAGE_PATH ) ) {

            Toast.makeText(this, new String(messageEvent.getData()), Toast.LENGTH_SHORT).show();
        } else {
            super.onMessageReceived( messageEvent );
        }
    }

    public Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.e("myTag", "Failed to connect to GoogleApiClient.");
            return null;
        }

        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                googleApiClient, asset).await().getInputStream();
        googleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w("myTag", "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }
}
