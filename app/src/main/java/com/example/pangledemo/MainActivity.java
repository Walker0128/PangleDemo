package com.example.pangledemo;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;

public class MainActivity extends AppCompatActivity {
    private TTAdNative mttAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTTAdSdk();

        mttAdNative = TTAdSdk.getAdManager().createAdNative(getApplicationContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("980051567")
                .build();
        TTAdNative.FullScreenVideoAdListener loadCallback = new TTAdNative.FullScreenVideoAdListener() {

            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                mttFullVideoAd = ttFullScreenVideoAd;

            }

            @Override
            public void onFullScreenVideoCached() {

            }

        };
        mttAdNative.loadFullScreenVideoAd(adSlot,loadCallback);
    }

    public void openFullScreenAd(View view) {
        showAdIfAvailable();
    }

    private TTAdConfig buildConfig() {
        return new TTAdConfig.Builder()
                .appId("5001121")
                .supportMultiProcess(false)
                .debug(true)
                .debugLog(1)
                .coppa(0) //CoppaValue: 0 adult, 1 child
                .build();
    }

    public void initTTAdSdk() {
        TTAdSdk.init(getApplicationContext(), buildConfig(), new TTAdSdk.InitCallback() {
            @Override
            public void success() {
                //load pangle ads after this method is triggered.
                TextView initResult = findViewById(R.id.textView);
                if (TTAdSdk.isInitSuccess()) {
                    String result = "SDK Init Success";
                    initResult.setText(result);
                } else {
                    String result = "SDK Init Failed";
                    initResult.setText(result);
                }

            }

            @Override
            public void fail(int code, String msg) {

            }
        });
    }


    public void showAdIfAvailable() {
        if (mttFullVideoAd != null) {
            mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                @Override
                public void onAdShow() {
                    Log.d(TAG, "Callback --> FullVideoAd show");
                }

                @Override
                public void onAdVideoBarClick() {
                    Log.d(TAG, "Callback --> FullVideoAd bar click");
                }

                @Override
                public void onAdClose() {
                    Log.d(TAG, "Callback --> FullVideoAd close");
                }

                @Override
                public void onVideoComplete() {
                    Log.d(TAG, "Callback --> FullVideoAd complete");
                }

                @Override
                public void onSkippedVideo() {
                    Log.d(TAG, "Callback --> FullVideoAd skipped");

                }

            });

            //Call the showAppOpenAd method to render the ad and it needs to pass in activity.
            mttFullVideoAd.showFullScreenVideoAd(MainActivity.this);
        }
    }

}