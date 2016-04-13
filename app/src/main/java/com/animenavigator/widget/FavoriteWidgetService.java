package com.animenavigator.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.animenavigator.R;
import com.animenavigator.common.ImageLoader;
import com.animenavigator.db.Contract;

import java.text.DecimalFormat;

/**
 * Created by a.g.seliverstov on 13.04.2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FavoriteWidgetService extends RemoteViewsService {
    private static final String TAG = FavoriteWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteRemoteViewsFactory();
    }

    class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private Cursor cursor;

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (cursor != null) cursor.close();
            final long identityToken = Binder.clearCallingIdentity();
            cursor = getContentResolver().query(
                    Contract.MangaEntry.buildFavorite(),
                    null,
                    null,
                    null,
                    null);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (cursor!=null) cursor.close();
            cursor=null;
        }

        @Override
        public int getCount() {
            return (cursor==null)?0:cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final Context context = FavoriteWidgetService.this;
            if (position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)) return null;

            final RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item);

            views.setTextViewText(R.id.item_title, cursor.getString(cursor.getColumnIndex(Contract.MangaEntry.NAME_COLUMN)));
            views.setTextViewText(R.id.item_type, cursor.getString(cursor.getColumnIndex(Contract.MangaEntry.TYPE_COLUMN)));
            views.setTextViewText(R.id.item_rating, context.getString(R.string.rating_tmp, new DecimalFormat("#.#").format(cursor.getFloat(cursor.getColumnIndex(Contract.MangaEntry.BAYESIAN_SCORE_COLUMN)))));

            Bitmap bitmap = ImageLoader.getBitmap(cursor.getString(cursor.getColumnIndex(Contract.MangaEntry.PICTURE_COLUMN)), context);
            views.setImageViewBitmap(R.id.item_poster, bitmap);

            Intent intent = new Intent();
            intent.setData(Contract.MangaEntry.buildMangaWithId(cursor.getLong(cursor.getColumnIndex(Contract.MangaEntry._ID))));
            views.setOnClickFillInIntent(R.id.widget_list_item,intent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.widget_list_item);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if (cursor.moveToPosition(position))
                return cursor.getLong(cursor.getColumnIndex(Contract.MangaEntry._ID));
            else return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
