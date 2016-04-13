package com.animenavigator.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.animenavigator.R;
import com.animenavigator.common.Const;
import com.animenavigator.details.DetailsActivity;
import com.animenavigator.main.MainActivity;

/**
 * Created by a.g.seliverstov on 13.04.2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FavoriteWidgetProvide extends AppWidgetProvider {
    private static final String TAG = FavoriteWidgetProvide.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(), launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                views.setRemoteAdapter(R.id.widget_list, new Intent(context, FavoriteWidgetService.class));
            } else {
                views.setRemoteAdapter(0, R.id.widget_list, new Intent(context, FavoriteWidgetService.class));
            }

            Intent clickIntentTemplate = new Intent(context, DetailsActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
            views.setEmptyView(R.id.widget_list, R.id.widget_empty);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "onReceive " + intent.getAction());
        if (Const.ACTION_FAVORITE_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, FavoriteWidgetProvide.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }

    }
}
