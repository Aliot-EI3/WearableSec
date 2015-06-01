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
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                imageView = (ImageView) findViewById(R.id.image_asset);
                textView = (TextView) stub.findViewById(R.id.text);
                Intent intent = getIntent();
                processBitmapFromIntent(intent);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processBitmapFromIntent(intent);
    }

    private void processBitmapFromIntent(Intent intent) {
        if (intent.hasExtra(BITMAP_EXTRA)){
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra(BITMAP_EXTRA);
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }
}
