package com.animenavigator.details;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.animenavigator.R;
import com.animenavigator.common.Const;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;


/**
 * Created by a.g.seliverstov on 05.04.2016.
 */
public class SummaryFragment extends Fragment{
    public static final String MANGA_URI_KEY = "MANGA_URI_KEY";
    private int MANGA_CURSOR_LOADER_ID = 5;
    private View mView;

    public static SummaryFragment newInstance(Uri uri){
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable(MANGA_URI_KEY, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.summary_fragment,container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Loader loader = getLoaderManager().getLoader(Const.SUMMARY_CURSOR_LOADER_ID);
        if (loader!=null && loader.isReset()){
            getLoaderManager().restartLoader(Const.SUMMARY_CURSOR_LOADER_ID, null, new CursorLoaderCallback(getActivity()));
        }else {
            getLoaderManager().initLoader(Const.SUMMARY_CURSOR_LOADER_ID, null, new CursorLoaderCallback(getActivity()));
        }
    }

    class CursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;

        public CursorLoaderCallback(Context context){
            mContext = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri uri = getArguments().getParcelable(MANGA_URI_KEY);
            if (uri!=null) {
                return new CursorLoader(
                        mContext,
                        uri,
                        null,
                        null,
                        null,
                        null);
            }else{
                return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data.moveToFirst()) {
                final Anime anime = Anime.fromCursor(data);



                final TextView plot = (TextView) mView.findViewById(R.id.plot);


                if (plot != null)
                    plot.setText((anime.plot==null)?mContext.getString(R.string.unknown):anime.plot);

                final TextView titles = (TextView) mView.findViewById(R.id.titles);
                if (titles != null) {
                    Cursor titlesCursor = mContext.getContentResolver().query(Contract.MangaTitleEntry.buildTitlesForManga((long) anime._id), null, null, null, Contract.MangaTitleEntry.NAME_COLUMN);
                    if (titlesCursor != null) {
                        titles.setText(Anime.titlesFromCursorAsString(titlesCursor));
                        titlesCursor.close();
                    }
                }

                final TextView genres = (TextView) mView.findViewById(R.id.genres);
                if (genres != null) {
                    Cursor genresCursor = mContext.getContentResolver().query(Contract.GenreEntry.buildGenreForManga((long) anime._id), null, null, null, null);
                    if (genresCursor != null) {
                        String s = Anime.genresFromCursorAsString(genresCursor);
                        s = (s==null || "".equals(s))? mContext.getString(R.string.unknown) : s;
                        genres.setText(s);
                        genresCursor.close();
                    }
                }

                final TextView themes = (TextView) mView.findViewById(R.id.themes);
                if (themes != null) {
                    Cursor themesCursor = mContext.getContentResolver().query(Contract.ThemeEntry.buildThemesForManga((long) anime._id), null, null, null, null);
                    if (themesCursor != null) {
                        String s = Anime.themesFromCursorAsString(themesCursor);
                        s = (s==null || "".equals(s))? mContext.getString(R.string.unknown) : s;
                        themes.setText(s);
                        themesCursor.close();
                    }
                }
                final TextView creators = (TextView) mView.findViewById(R.id.creators);
                if (creators != null) {
                    Cursor creatorsCursor = mContext.getContentResolver().query(Contract.PersonEntry.buildPersonsAndTasksForManga((long) anime._id), null, null, null, Contract.TaskEntry.NAME_COLUMN);
                    if (creatorsCursor != null) {
                        String s = Anime.creatorsAndTasksFromCursorAsHtml(creatorsCursor);
                        s = (s==null || "".equals(s))? mContext.getString(R.string.unknown) : s;
                        creators.setText(Html.fromHtml(s));
                        creatorsCursor.close();
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
