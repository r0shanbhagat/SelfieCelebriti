package com.girnarsoft.tracking;

import com.girnarsoft.tracking.config.TrackerConfiguration;
import com.girnarsoft.tracking.event.EventInfo;
import com.girnarsoft.tracking.exception.TrackerConfigurationException;
import com.girnarsoft.tracking.exception.TrackerException;

public interface IAnalyticsManager {
    void configure(TrackerConfiguration configuration) throws TrackerConfigurationException;

    void pushEvent(EventInfo eventInfo) throws TrackerException;
}
