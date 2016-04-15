package com.animenavigator;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by a.g.seliverstov on 14.04.2016.
 */
public class Application extends android.app.Application {
    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker.enableExceptionReporting(true);
        }
        return mTracker;
    }
}
