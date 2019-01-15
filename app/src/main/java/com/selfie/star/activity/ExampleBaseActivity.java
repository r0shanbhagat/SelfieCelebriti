package com.selfie.star.activity;


import android.view.Menu;
import android.view.MenuItem;

import com.base.commonframework.activity.BaseActivity;
import com.selfie.star.R;

import androidx.databinding.ViewDataBinding;

public class ExampleBaseActivity extends BaseActivity {

    @Override
    protected int getActivityLayout() {
        return R.layout.test;
    }

    @Override
    public String getScreenName() {
        return "Test screen";
    }

    @Override
    protected void onCreate(ViewDataBinding viewDataBinding) {
        /*Toolbar toolbarSearchActivity = findViewById(R.id.toolbar_actionbar);
        toolbarSearchActivity.setTitle("Test Title");
        setSupportActionBar(toolbarSearchActivity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.backslide_right_out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.home_menu, menu);
        menu.findItem(R.id.notification).setVisible(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ToastUtility.showToastMessage(getAbstractActivity(), "Notification Clicked");
                return true;
            }
        });
        menu.findItem(R.id.search).setVisible(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Navigator.launchActivity(getAbstractActivity(), getIntentHelper().newGoogleSearchIntent(getAbstractActivity()));
                overridePendingTransition(0, 0);
                return true;
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

/*
      <include
            android:id="@+id/includeButton"
            layout="@layout/widget_footer_button" />

        Button searchButton = fragmentSearchByPriceBinding.rlSearchButton.buttonFooter;
        searchButton.setText(getResources().getString(R.string.search));
        searchButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            search();
        }
    });
*/
}
