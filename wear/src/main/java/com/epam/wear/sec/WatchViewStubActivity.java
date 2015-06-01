package com.epam.wear.sec;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WatchViewStubActivity extends Activity {

    private TextView textView;

    private ImageView imageView;

    public static final String BITMAP_EXTRA = "bitmap_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_view_stub);
        imageView = (ImageView) findViewById(R.id.image_asset);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                textView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(BITMAP_EXTRA)){
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra(BITMAP_EXTRA);
            imageView.setImageBitmap(bitmap);
            textView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
