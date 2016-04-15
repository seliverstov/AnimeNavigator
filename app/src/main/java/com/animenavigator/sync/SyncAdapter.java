package com.animenavigator.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.animenavigator.R;
import com.animenavigator.common.Const;
import com.animenavigator.common.AppTracker;
import com.animenavigator.db.Contract;
import com.animenavigator.db.DbHelper;
import com.animenavigator.db.MangaDao;
import com.animenavigator.http.AnimeNewsNetworkClient;
import com.animenavigator.http.AnimeNewsNetworkClientImpl;
import com.animenavigator.main.MainActivity;
import com.animenavigator.xml.ANN;
import com.animenavigator.xml.Titles;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * Created by a.g.seliverstov on 11.04.2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int NOTIFICATION_ID = 1;

    private Context mContext;

    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Start sync");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        long lastSync = sp.getLong(Const.SP_LAST_SINC, 0);

        int titlesCount = 50;
        if (System.currentTimeMillis() - lastSync >= 30 * DAY_IN_MILLIS){
            titlesCount = 500;
        }else if (System.currentTimeMillis() - lastSync > 14 * DAY_IN_MILLIS){
            titlesCount = 200;
        }

        AnimeNewsNetworkClient client = new AnimeNewsNetworkClientImpl(getContext());
        String titlesXml = client.queryTitlesXML(0,titlesCount, null, null);
        if (titlesXml==null){
            Log.e(TAG,"Sync error: titles aml is null");
            return;
        }
        SQLiteOpenHelper dbHelper = new DbHelper(mContext);
        int maxAnnId = (int) DatabaseUtils.longForQuery(dbHelper.getReadableDatabase(),"SELECT MAX("+ Contract.MangaEntry._ID+") FROM "+ Contract.MangaEntry.TABLE_NAME,null);
        Log.i(TAG, "Set 'amxAnnId' to "+maxAnnId);
        MangaDao mangaDao = new MangaDao(dbHelper);
        Serializer serializer = new Persister();
        try {
            Titles titles = serializer.read(Titles.class, new ByteArrayInputStream(titlesXml.getBytes(Charset.forName("UTF-8"))));
            if (titles==null || titles.items==null){
                Log.i(TAG,"Sync complete: no new items was loaded");
                dbHelper.close();
                return;
            }
            Log.i(TAG, "Total titles count: "+titles.items.size());
            int n = 0;
            Collections.reverse(titles.items);
            for (Titles.Item t : titles.items) {
                if (Integer.valueOf(t.id) > maxAnnId && !"magazine".equalsIgnoreCase(t.type) && !"omnibus".equalsIgnoreCase(t.type)) {
                    try {
                        String annXml = client.queryDetailsXml(Integer.valueOf(t.id),null);
                        if (annXml!=null) {
                            ANN ann = serializer.read(ANN.class, new ByteArrayInputStream(annXml.getBytes(Charset.forName("UTF-8"))));
                            Log.i(TAG,ann.toString());
                            if (ann.anime != null) {
                                ann.anime.vintage = t.vintage;
                                mangaDao.create(ann.anime);
                            }
                            if (ann.manga != null) {
                                ann.manga.vintage = t.vintage;
                                mangaDao.create(ann.manga);
                            }
                        }
                        n++;
                    } catch (Exception e) {
                        Log.e(TAG,t.id + "," + t.type + " - error: "+e.getMessage(),e);
                        AppTracker.trackException(mContext, t.id + "," + t.type + " - error: " + e.getMessage());
                    }
                }
            }
            Log.i(TAG, "Sync complete: " + n + " new items was loaded");
            dbHelper.close();
            if (n>0) {
                mContentResolver.notifyChange(Contract.MangaEntry.CONTENT_URI, null);
                mContentResolver.notifyChange(Contract.GenreEntry.CONTENT_URI, null);
                mContentResolver.notifyChange(Contract.ThemeEntry.CONTENT_URI, null);
                displayNotification(n);
            }
            sp.edit().putLong(Const.SP_LAST_SINC,System.currentTimeMillis()).apply();
        } catch (Exception e) {
            Log.e(TAG,e.getMessage(),e);
            AppTracker.trackException(mContext, e.getMessage());
            dbHelper.close();
            return;
        }
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime){
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().syncPeriodic(syncInterval, flexTime).setSyncAdapter(account, authority).setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context){
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context){
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.account_type));
        if (accountManager.getPassword(newAccount)==null){
            if (!accountManager.addAccountExplicitly(newAccount, "", null)){
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context){
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, 0);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context){
        getSyncAccount(context);
    }

    private void displayNotification(int n) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (prefs.getBoolean(Const.SP_ENABLE_NOTIFICATIONS_KEY, true)){
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext())
                    .setColor(mContext.getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(mContext.getString(R.string.notification_title))
                    .setContentText(mContext.getString(R.string.notification_text_tmp, n))
                    .setAutoCancel(true);

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_START_TAB, Const.NEW_TAB);

            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
}
