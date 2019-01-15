package com.base.commonframework.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.commonframework.activity.BaseActivity;
import com.base.commonframework.application.BaseApplication;
import com.base.commonframework.network.locator.IServiceLocator;
import com.base.commonframework.preference.PreferenceManager;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.girnarsoft.tracking.IAnalyticsManager;
import com.girnarsoft.tracking.event.EventInfo;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {


    private ViewDataBinding viewDataBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.bind(inflater.inflate(getFragmentLayout(), container, false));
        setHasOptionsMenu(true);
        createView(viewDataBinding);
        onFragmentReady();
        return viewDataBinding.getRoot();
    }

    /**
     * Gets fragment layout.
     *
     * @return the fragment layout
     */
    protected abstract int getFragmentLayout();

    protected void onFragmentReady() {
        // do nothing
    }


    /**
     * Create view.
     *
     * @param viewDataBinding the view
     */
    protected abstract void createView(ViewDataBinding viewDataBinding);

    public BaseActivity getBaseActivity() {
        return BaseActivity.getBaseActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            getAnalyticsManager().pushEvent(getScreenTrackEventInfo().build());
        }
    }

    public EventInfo.Builder getScreenTrackEventInfo() {
        return CommonFrameworkUtil.getEventTrackEventInfo()
                .withEventName(EventInfo.EventName.OPEN_SCREEN)
                .withPageType(CommonFrameworkUtil.getClassifiedScreenName(getFragmentScreenName()));
    }


    public EventInfo.Builder getEventTrackEventInfo() {
        return CommonFrameworkUtil.getEventTrackEventInfo()
                .withEventName(EventInfo.EventName.EVENT_TRACKING)
                .withPageType(CommonFrameworkUtil.getClassifiedScreenName(getFragmentScreenName()));
    }



    public PreferenceManager getPreferenceManager() {
        return ((BaseApplication) getActivity().getApplication()).getPreferenceManager();
    }

    public IServiceLocator getLocator() {
        return ((BaseActivity) getActivity()).getLocator();
    }


    public IAnalyticsManager getAnalyticsManager() {
        return ((BaseActivity) getActivity()).getAnalyticsManager();
    }


    //TODO Implement batter way restrict and remove old methodology
    public abstract String getFragmentScreenName();

}
