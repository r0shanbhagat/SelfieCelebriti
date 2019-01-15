package com.base.commonframework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.base.commonframework.R;
import com.base.commonframework.application.BaseApplication;
import com.base.commonframework.baselibrary.utility.HideUtility;
import com.base.commonframework.baselibrary.view.FragmentAnimation;
import com.base.commonframework.network.locator.IServiceLocator;
import com.base.commonframework.preference.PreferenceManager;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.girnarsoft.tracking.IAnalyticsManager;
import com.girnarsoft.tracking.event.EventInfo;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity {

    protected final static FragmentAnimation DEFAULT_FRAGMENT_ANIMATION = new FragmentAnimation(R.anim.anim_fragment_right_in,
            R.anim.anim_fragment_left_out,
            R.anim.anim_fragment_left_in,
            R.anim.anim_fragment_right_out);
    private static BaseActivity baseActivity;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected CompositeDisposable disposables = new CompositeDisposable();
    private ViewDataBinding viewDataBinding;

    public static BaseActivity getBaseActivity() {
        return BaseActivity.baseActivity;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAnalyticsManager().pushEvent(getScreenTrackEventInfo().build());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.backslide_right_in, R.anim.slide_left_in);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    private EventInfo.Builder getScreenTrackEventInfo() {
        return CommonFrameworkUtil.getEventTrackEventInfo()
                .withEventName(EventInfo.EventName.OPEN_SCREEN)
                .withPageType(CommonFrameworkUtil.getClassifiedScreenName(getScreenName()));
    }

    public EventInfo.Builder getEventTrackEventInfo() {
        return CommonFrameworkUtil.getEventTrackEventInfo()
                .withEventName(EventInfo.EventName.EVENT_TRACKING)
                .withPageType(CommonFrameworkUtil.getClassifiedScreenName(getScreenName()));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // http://blog.sqisland.com/2016/09/transactiontoolargeexception-crashes-nougat.html
        // https://www.devsbedevin.com/avoiding-transactiontoolargeexception-on-android-nougat-and-up/
        // SavedInstanceFragment.getInstance(getSupportFragmentManager()).pushData((Bundle) outState.clone());
        // outState.clear(); // We don't want a TransactionTooLargeException, so we handle things via the SavedInstanceFragment
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
        onPreSetContentView();
        viewDataBinding = DataBindingUtil.setContentView(this, getActivityLayout());
        HideUtility.init(this);
        onCreate(viewDataBinding);
        onActivityReady();
    }


    protected void onPreSetContentView() {
        //To minimize the overdraw of the screen
        getWindow().setBackgroundDrawable(null);
    }

    public void hideKeyboard() {
        HideUtility.hideSoftInput(this);
    }

    protected void onActivityReady() {
        // do nothing
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment baseFragment = CommonFrameworkUtil.getActiveFragment(getBaseActivity());
        if (null != baseFragment) {
            baseFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment baseFragment = CommonFrameworkUtil.getActiveFragment(getBaseActivity());
        if (null != baseFragment) {
            baseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isChangingConfigurations()) {
            attachBaseContext(getBaseContext());
        }
    }

    protected void showSnackBar(@NonNull String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }


    protected abstract int getActivityLayout();

    public abstract String getScreenName();

    // public abstract Toolbar getToolBar();

    //public abstract void setToolBarTitle(String title);

    //public abstract void setToolBarIcon(int drawable);

    protected abstract void onCreate(ViewDataBinding viewDataBinding);


    public void pushFragment(FragmentAnimation animation, Bundle bundle, Fragment destinationFragment, int containerId, String tag,
                             boolean addToBackstack) {
        if (destinationFragment == null) {
            throw new IllegalArgumentException("Destination Fragment is not defined.");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (animation != null) {
            ft.setCustomAnimations(animation.getEnter(), animation.getExit(), animation.getPopEnter(), animation.getPopExit());
        }
        if (bundle != null) {
            destinationFragment.setArguments(bundle);
        }

        if (destinationFragment.isAdded()) {
            ft.show(destinationFragment);
        } else {
            ft.replace(containerId, destinationFragment, tag);
        }
        if (addToBackstack) {
            ft.addToBackStack(tag);
        }
        if (!isFinishing()) {
            ft.commitAllowingStateLoss();
        }
    }

    public PreferenceManager getPreferenceManager() {
        return ((BaseApplication) getApplication()).getPreferenceManager();
    }

    public IAnalyticsManager getAnalyticsManager() {
        if (getApplication() instanceof BaseApplication) {
            return ((BaseApplication) getApplication()).getAnalyticsManager();
        }
        throw new UnsupportedOperationException("Your Application class must extends Abstract Application class.");
    }

    public IServiceLocator getLocator() {
        if (getApplication() instanceof BaseApplication) {
            return ((BaseApplication) getApplication()).getLocator();
        }
        throw new UnsupportedOperationException("Your Application class must extends Abstract Application class.");
    }
}
