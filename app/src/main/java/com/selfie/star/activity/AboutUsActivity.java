package com.selfie.star.activity;

import android.view.MenuItem;

import com.base.commonframework.activity.BaseActivity;
import com.selfie.star.R;
import com.selfie.star.databinding.AboutUsActivityBinding;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;

public class AboutUsActivity extends BaseActivity {

    public static final String TAG = AboutUsActivity.class.getSimpleName();
    private AboutUsActivityBinding binding;

    @Override
    protected int getActivityLayout() {
        return R.layout.about_us_activity;
    }


    @Override
    protected void onActivityReady() {
        super.onActivityReady();
        Toolbar toolbarSearchActivity = findViewById(R.id.toolbar_actionbar);
        toolbarSearchActivity.setTitle("About Us");
        setSupportActionBar(toolbarSearchActivity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    @Override
    public String getScreenName() {
        return TAG;
    }
    private static final int glFilterId = 4;

    @Override
    protected void onCreate(ViewDataBinding viewDataBinding) {
        binding= (AboutUsActivityBinding) viewDataBinding;


    }
}
