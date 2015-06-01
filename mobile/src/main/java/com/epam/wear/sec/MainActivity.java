package com.epam.wear.sec;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.epam.wear.sec.utils.Action;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.util.Set;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, CapabilityApi.CapabilityListener {

    private GoogleApiClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show_simple).setOnClickListener(this);
        findViewById(R.id.btn_show_box).setOnClickListener(this);
        findViewById(R.id.btn_show_stub).setOnClickListener(this);
        findViewById(R.id.btn_show_list).setOnClickListener(this);
        findViewById(R.id.btn_show_grid).setOnClickListener(this);
        findViewById(R.id.btn_send_message).setOnClickListener(this);
        findViewById(R.id.btn_send_asset).setOnClickListener(this);
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();
    }

// Send a data object when the data layer connection is successful.

    @Override
    public void onConnected(Bundle connectionHint) {
        setupWearNode();

    }

    // Disconnect from the data layer when the Activity stops
    @Override
    protected void onStop() {
        if (null != googleClient && googleClient.isConnected()) {
            googleClient.disconnect();
        }
        super.onStop();
    }

    // Placeholders for required connection callbacks
    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    @Override
    public void onClick(View v) {
        Action action = null;
        switch (v.getId()) {
            case R.id.btn_show_simple:
                action = Action.SHOW_SIMPLE;
                break;
            case R.id.btn_show_box:
                action = Action.SHOW_BOX;
                break;
            case R.id.btn_show_stub:
                action = Action.SHOW_STUB;
                break;
            case R.id.btn_show_list:
                action = Action.SHOW_LIST;
                break;
            case R.id.btn_show_grid:
                action = Action.SHOW_GRID;
                break;
            case R.id.btn_send_message:
                sendMessage("Message!");
                break;
            case R.id.btn_send_asset:
                sendAsset();
                break;
            default:
                return;
        }
        if (action != null) {
            sendDataMapItem(action);
        }
    }

    private void sendDataMapItem(Action action) {
        String WEARABLE_DATA_PATH = "/wearable_data";

        DataMap dataMap = new DataMap();
        dataMap.putString("action", action.toString());

        //Requires a new thread to avoid blocking the UI
        new SendToDataLayerThread(WEARABLE_DATA_PATH, dataMap).start();

    }


    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }

        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient).await();
            for (Node node : nodes.getNodes()) {

                // Construct a DataRequest and send over the data layer
                PutDataMapRequest putDMR = PutDataMapRequest.create(path);
                putDMR.getDataMap().putAll(dataMap);
                PutDataRequest request = putDMR.asPutDataRequest();
                DataApi.DataItemResult result = Wearable.DataApi.putDataItem(googleClient, request).await();
                if (result.getStatus().isSuccess()) {
                    Log.v("myTag", "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.v("myTag", "ERROR: failed to send DataMap");
                }
            }
        }
    }


    private static final String
            WEAR_CAPABILITY = "wear_capability";

    private static final String TAG = "DelayedStub";

    /* the preferred note that can handle the stub capability */
    private Node wearNode;

    private void setupWearNode() {
        Wearable.CapabilityApi.addCapabilityListener(
                googleClient, this, WEAR_CAPABILITY);

        Wearable.CapabilityApi.getCapability(
                googleClient, WEAR_CAPABILITY,
                CapabilityApi.FILTER_REACHABLE).setResultCallback(
                new ResultCallback<CapabilityApi.GetCapabilityResult>() {
                    @Override
                    public void onResult(CapabilityApi.GetCapabilityResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e(TAG, "setupConfirmationHandlerNode() Failed to get capabilities, "
                                    + "status: " + result.getStatus().getStatusMessage());
                            return;
                        }
                        updateWearCapability(result.getCapability());
                    }
                });
    }

    private void updateWearCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();
        if (connectedNodes.isEmpty()) {
            wearNode = null;
        } else {
            wearNode = pickBestNode(connectedNodes);
        }
    }

    /**
     * We pick a node that is capabale of handling the stub. If there is more than one,
     * then we would prefer the one that is directly connected to this device. In general,
     * depending on the situation and requirements, the "best" node might be picked based on other
     * criteria.
     */
    private Node pickBestNode(Set<Node> nodes) {
        Node best = null;
        if (nodes != null) {
            for (Node node : nodes) {
                if (node.isNearby()) {
                    return node;
                }
                best = node;
            }
        }
        return best;
    }

    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        updateWearCapability(capabilityInfo);
    }

    public static final String WEAR_CAPABILITY_MESSAGE_PATH = "/wear_capability";

    private void sendMessage(String message) {
        if (wearNode != null) {
            Wearable.MessageApi.sendMessage(googleClient, wearNode.getId(),
                    WEAR_CAPABILITY_MESSAGE_PATH, message.getBytes())
                    .setResultCallback(
                            new ResultCallback() {
                                @Override
                                public void onResult(Result result) {
                                    if (!result.getStatus().isSuccess()) {
                                        // Failed to send message
                                    }
                                }
                            });
        }
    }

    private void sendAsset() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.i_am);
        Asset asset = createAssetFromBitmap(bitmap);
        PutDataRequest request = PutDataRequest.create("/image");
        request.putAsset("profileImage", asset);
        Wearable.DataApi.putDataItem(googleClient, request).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(Result result) {
                        if (!result.getStatus().isSuccess()) {
                            // Failed to send message
                            Log.e("ERROR_RES", "ffsfs");
                        }else {
                            Log.e("RES", "ffsfs");
                        }
                    }
                });
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }
}