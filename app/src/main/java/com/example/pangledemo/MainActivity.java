package com.example.pangledemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;

public class MainActivity extends AppCompatActivity {
    private TTAdNative mttAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;
    private TextView initResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initResult = findViewById(R.id.textView);
        initTTAdSdk();
        loadAD("980051567");
    }

    public void openFullScreenAd(View view) {
        showAdIfAvailable();
    }

    private TTAdConfig buildConfig() {
        return new TTAdConfig.Builder()
                .appId("5001121")
                .supportMultiProcess(false)
                .debug(false)
                .coppa(0) //CoppaValue: 0 adult, 1 child
                .build();
    }

    public void initTTAdSdk() {
        TTAdSdk.init(getApplicationContext(), buildConfig(), new TTAdSdk.InitCallback() {
            @Override
            public void success() {
                //load pangle ads after this method is triggered.
                initResult.setText("SDK Init Success");
            }

            @Override
            public void fail(int code, String msg) {
                initResult.setText("SDK Init Failed");
            }
        });
    }

    public void loadAD(String codeId) {
        mttAdNative = TTAdSdk.getAdManager().createAdNative(getApplicationContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setImageAcceptedSize(1080,1920)
                .build();
        TTAdNative.FullScreenVideoAdListener loadCallback = new TTAdNative.FullScreenVideoAdListener() {

            @Override
            public void onError(int code, String message) {
                Toast.makeText(MainActivity.this, "Load Ad Error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                mttFullVideoAd = ttFullScreenVideoAd;
                Toast.makeText(MainActivity.this, "Full Screen Video Load", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFullScreenVideoCached() {
                Toast.makeText(MainActivity.this, "on Full Screen Video Cached", Toast.LENGTH_SHORT).show();

            }

        };
        mttAdNative.loadFullScreenVideoAd(adSlot, loadCallback);
    }

    public void showAdIfAvailable() {
        if (mttFullVideoAd != null) {
            mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                @Override
                public void onAdShow() {
                }

                @Override
                public void onAdVideoBarClick() {
                }

                @Override
                public void onAdClose() {
                }

                @Override
                public void onVideoComplete() {
                }

                @Override
                public void onSkippedVideo() {
                }

            });

            //Call the showAppOpenAd method to render the ad and it needs to pass in activity.
            mttFullVideoAd.showFullScreenVideoAd(MainActivity.this);
        } else {
            Toast.makeText(this, "Full Video need load", Toast.LENGTH_SHORT).show();
        }
    }

}