package com.girnarsoft.tracking;

import android.content.Context;
import android.os.Bundle;

import com.girnarsoft.tracking.config.TrackerConfiguration;
import com.girnarsoft.tracking.event.EventInfo;
import com.girnarsoft.tracking.exception.TrackerConfigurationException;
import com.girnarsoft.tracking.exception.TrackerException;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;


public class AnalyticsManager implements IAnalyticsManager {
    private Context context;
    private FirebaseAnalytics firebaseAnalytics;

    public AnalyticsManager(Context context) {
        this.context = context;
    }

    @Override
    public void configure(TrackerConfiguration configuration) throws TrackerConfigurationException {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        firebaseAnalytics.setSessionTimeoutDuration(500000);

    }


    @Override
    public void pushEvent(EventInfo eventInfo) throws TrackerException {
        Map<String, String> eventMap = eventInfo.getEventInfo();
        Bundle analyticBundle = new Bundle();
        for (Map.Entry<String, String> entryMap : eventMap.entrySet()) {
            String mapKey = entryMap.getKey(); //Getting Key
            String mapValue = entryMap.getValue(); //Getting Value
            analyticBundle.putString(mapKey, mapValue); //For Firebase
        }

        if (firebaseAnalytics != null) {
            firebaseAnalytics.logEvent(eventInfo.getEventName(), analyticBundle);
        }


    }


    private Bundle toBundle(Map<String, String> eventInfo) {
        Bundle bundle = new Bundle();
        for (String key : eventInfo.keySet()) {
            bundle.putString(key, eventInfo.get(key));
        }
        return bundle;
    }


}
