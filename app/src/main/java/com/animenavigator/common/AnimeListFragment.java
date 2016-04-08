package com.animenavigator.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.animenavigator.db.Contract;

/**
 * Created by a.g.seliverstov on 07.04.2016.
 */
public abstract class AnimeListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    protected AnimeItemAdapter mAdapter;
    protected String mSelection = null;
    protected String[] mSelectionArgs = null;

    public static final String ARG_LOADER_ID = "ARG_LOADER_ID";
    public static final String ARG_SORT_ORDER = "ARG_SORT_ORDER";
    public static final String ARG_LOADER_URI = "ARG_LOADER_URI";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(getArguments().getInt(ARG_LOADER_ID), null, new CursorLoaderCallback(getActivity()));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Const.SP_ANIME_TYPE_KEY)){
            restartLoader();
        }
    }

    protected void refeshSelection(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sp.getString(Const.SP_ANIME_TYPE_KEY, null);

        if (Const.SP_MANGA_TYPE.equals(value)){
            mSelection = Contract.MangaEntry.TYPE_COLUMN+" = ?";
            mSelectionArgs = new String[]{Const.SP_MANGA_TYPE};
        }else if (Const.SP_ANIME_TYPE.equals(value)){
            mSelection = Contract.MangaEntry.TYPE_COLUMN+" != ?";
            mSelectionArgs = new String[]{Const.SP_MANGA_TYPE};
        }else{
            mSelection = null;
            mSelectionArgs = null;
        }
    }

    protected void restartLoader(){
        getLoaderManager().restartLoader(getArguments().getInt(ARG_LOADER_ID), null, new CursorLoaderCallback(getActivity()));
    }

    class CursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;

        public CursorLoaderCallback(Context context){
            mContext = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            refeshSelection(mContext);
            Uri uri = getArguments().getParcelable(ARG_LOADER_URI);
            return new CursorLoader(
                    mContext,
                    (uri==null)?Contract.MangaEntry.CONTENT_URI:uri,
                    null,
                    mSelection,
                    mSelectionArgs,
                    getArguments().getString(ARG_SORT_ORDER));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDetach() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onDetach();
    }
}

