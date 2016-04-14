package com.animenavigator.common;

import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.TextView;

import com.animenavigator.R;
import com.animenavigator.db.Contract;

/**
 * Created by a.g.seliverstov on 07.04.2016.
 */
public abstract class AnimeListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    protected AnimeItemAdapter mAdapter;
    protected String mSelection = null;
    protected String[] mSelectionArgs = null;
    protected ProgressDialog progressDialog;
    protected TextView mEmptyView;

    public static final String ARG_LOADER_ID = "ARG_LOADER_ID";
    public static final String ARG_SORT_ORDER = "ARG_SORT_ORDER";
    public static final String ARG_LOADER_URI = "ARG_LOADER_URI";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getActivity().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Loader loader = getLoaderManager().getLoader(getArguments().getInt(ARG_LOADER_ID));
        if (loader!=null && loader.isReset()){
            getLoaderManager().restartLoader(getArguments().getInt(ARG_LOADER_ID), null, new CursorLoaderCallback(getActivity()));
        }else {
            getLoaderManager().initLoader(getArguments().getInt(ARG_LOADER_ID), null, new CursorLoaderCallback(getActivity()));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Const.SP_ANIME_TYPE_KEY) || key.equals(Const.SP_FAVORITE_KEY) || (key.equals(Const.SP_FAVORITE_LIST_KEY) && sharedPreferences.getBoolean(Const.SP_FAVORITE_KEY, false))){
            restartLoader();
        }
    }

    protected void refeshSelection(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        if (sp.getBoolean(Const.SP_FAVORITE_KEY, false)){
            String[] args = sp.getString(Const.SP_FAVORITE_LIST_KEY,"-1").split(",");
            String list = "";
            for(String s: args){
                list +="?,";
            }
            if (list.length()>0) list = list.substring(0,list.length()-1);
            mSelection = Contract.MangaEntry._ID+" IN ("+list+")";
            mSelectionArgs = args;
        }else{
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
    }

    protected void restartLoader(){
        progressDialog.show();
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
            Uri uri = (getArguments()!=null)?(Uri)getArguments().getParcelable(ARG_LOADER_URI):null;
            uri = (uri==null)?Contract.MangaEntry.CONTENT_URI:uri;
            String sortOrder = (getArguments()!=null)?getArguments().getString(ARG_SORT_ORDER):null;
            return new CursorLoader(
                    mContext,
                    uri,
                    null,
                    mSelection,
                    mSelectionArgs,
                    sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
            if (progressDialog!=null && progressDialog.isShowing()) progressDialog.hide();
            if (mEmptyView!=null) mEmptyView.setVisibility((data==null || data.getCount()==0)? View.VISIBLE:View.GONE);

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

