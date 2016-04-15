package com.animenavigator.common;

import android.content.Context;
import android.util.Log;

import com.animenavigator.Application;
import com.animenavigator.R;
import com.animenavigator.details.DetailsActivity;
import com.animenavigator.main.MainActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by a.g.seliverstov on 15.04.2016.
 */
public class ScreenTracker {
    public static final String TAG = ScreenTracker.class.getSimpleName();

    public static void trackMainScreen(Context context, int position){
        if (context.getApplicationContext() instanceof Application) {
            String screen = MainActivity.class.getSimpleName()+".";
            switch (position) {
                case Const.TOP_RATED_TAB:
                    screen += context.getString(R.string.top_rated_tab_screen_name);
                    break;
                case Const.SEARCH_TAB:
                    screen += context.getString(R.string.search_tab_screen_name);
                    break;
                case Const.NEW_TAB:
                    screen += context.getString(R.string.new_tab_screen_name);
                    break;
            }
            Tracker tracker = ((Application) context.getApplicationContext()).getDefaultTracker();
            tracker.setScreenName(screen);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
            Log.e(TAG, "Track "+screen);
        }else{
            Log.e(TAG,"Application has wrong class");
        }
    }
    public static void trackDetailsScreen(Context context, int position){
        if (context.getApplicationContext() instanceof Application) {
            String screen = DetailsActivity.class.getSimpleName()+".";
            switch (position) {
                case Const.SUMMARY_TAB:
                    screen = context.getString(R.string.summary_tab_screen_name);
                    break;
                case Const.RELATED_TAB:
                    screen = context.getString(R.string.related_tab_screen_name);
                    break;
                case Const.EXTRA_TAB:
                    screen = context.getString(R.string.extra_tab_screen_name);
                    break;
            }
            Tracker tracker = ((Application) context.getApplicationContext()).getDefaultTracker();
            tracker.setScreenName(screen);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
            Log.e(TAG, "Track "+screen);
        }else{
            Log.e(TAG,"Application has wrong class");
        }
    }
}
