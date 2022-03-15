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
    private TTAdNative ttAdNative;
    private TTFullScreenVideoAd ttFullVideoAd;
    private TextView initResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initResult = findViewById(R.id.textView);
        initTTAdSdk();
    }

    /**
     * @Button PangleFullVideoButton
     * @param view
     */
    public void openFullScreenAd(View view) {
        loadAD("980051567");
        showAdIfAvailable();
    }

    /**
     * Pangle SDK build config
     * @return
     */
    private TTAdConfig buildConfig() {
        return new TTAdConfig.Builder()
                .appId("5001121")
                .supportMultiProcess(false)
                .debug(false)
                .coppa(0) //CoppaValue: 0 adult, 1 child
                .build();
    }

    /**
     * init Pangle SDK
     */
    public void initTTAdSdk() {
        TTAdSdk.init(getApplicationContext(), buildConfig(), new TTAdSdk.InitCallback() {
            @Override
            public void success() {
                //load pangle ads after this method is triggered.
                initResult.setText("SDK Init Success");
            }

            @Override
            public void fail(int code, String msg) {
                String message = "SDK Init Failed: Code: " + code + ", message: " + msg;
                initResult.setText(message);
            }
        });
    }

    /**
     * Load Pangle FullScreenAD
     * @param codeId
     */
    public void loadAD(String codeId) {
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
//                .setImageAcceptedSize(1080,1920)
                .build();
        ttAdNative = TTAdSdk.getAdManager().createAdNative(getApplicationContext());
        ttAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {

            @Override
            public void onError(int code, String message) {
                String errMessage = "Load Error Code: " + code + ", Message: " + message;
                Toast.makeText(MainActivity.this, errMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                Toast.makeText(MainActivity.this, "Full Screen Video Load", Toast.LENGTH_SHORT).show();
                ttFullVideoAd = ttFullScreenVideoAd;
                ttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

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

            }

            @Override
            public void onFullScreenVideoCached() {
                Toast.makeText(MainActivity.this, "on Full Screen Video Cached", Toast.LENGTH_SHORT).show();

            }

        });
    }

    /**
     * Show Pangle full screen video ad
     */
    public void showAdIfAvailable() {
        if (ttFullVideoAd != null) {
            //Call the showAppOpenAd method to render the ad and it needs to pass in activity.
            ttFullVideoAd.showFullScreenVideoAd(MainActivity.this);
        } else {
            Toast.makeText(this, "Load AD failed", Toast.LENGTH_SHORT).show();
        }
    }

}