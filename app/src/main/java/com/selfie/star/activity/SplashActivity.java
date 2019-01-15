package com.selfie.star.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.selfie.star.R;
import com.selfie.star.navigation.IntentHelper;

public class SplashActivity extends Activity {

    public static final String TAG = SplashActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            startActivity(IntentHelper.getInstance().newMainIntent(this,TAG));
            finish();
        }, SPLASH_TIME_OUT);
    }

}