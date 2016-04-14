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
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.animenavigator.R;
import com.animenavigator.common.Const;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;


/**
 * Created by a.g.seliverstov on 06.04.2016.
 */
public class ExtraFragment extends Fragment {
    public static final String MANGA_URI_KEY = "MANGA_URI_KEY";
    private int MANGA_CURSOR_LOADER_ID = 7;
    private View mView;

    public static ExtraFragment newInstance(Uri uri){
        ExtraFragment fragment = new ExtraFragment();
        Bundle args = new Bundle();
        args.putParcelable(MANGA_URI_KEY, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.extra_fragment,container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Loader loader = getLoaderManager().getLoader(Const.EXTRA_CURSOR_LOADER_ID);
        if (loader!=null && loader.isReset()){
            getLoaderManager().restartLoader(Const.EXTRA_CURSOR_LOADER_ID, null, new CursorLoaderCallback(getActivity()));
        }else {
            getLoaderManager().initLoader(Const.EXTRA_CURSOR_LOADER_ID, null, new CursorLoaderCallback(getActivity()));
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
                final TextView reviews = (TextView) mView.findViewById(R.id.reviews);
                if (reviews != null) {
                    Cursor reviewsCursor = mContext.getContentResolver().query(Contract.MangaReviewEntry.buildReviewsForManga((long) anime._id), null, null, null, null);
                    if (reviewsCursor != null && reviewsCursor.getCount()>0) {
                        reviews.setClickable(true);
                        reviews.setMovementMethod(LinkMovementMethod.getInstance());
                        reviews.setText(Html.fromHtml(Anime.reviewsFromCursorAsHtml(reviewsCursor)));
                        reviewsCursor.close();
                    }else{
                        reviews.setText(mContext.getString(R.string.no_reviews));
                    }
                }

                final TextView links = (TextView) mView.findViewById(R.id.links);
                if (reviews != null) {
                    Cursor linksCursor = mContext.getContentResolver().query(Contract.MangaLinkEntry.buildLinksForManga((long) anime._id), null, null, null, null);
                    String sourceLink = "<a href='"+mContext.getString(R.string.ANN_URL,anime._id)+"'>"+mContext.getString(R.string.ANN)+"</a><br/>";
                    links.setClickable(true);
                    links.setMovementMethod(LinkMovementMethod.getInstance());
                    if (linksCursor != null && linksCursor.getCount()>0) {
                        links.setText(Html.fromHtml(sourceLink+"<br/>"+Anime.linksFromCursorAsHtml(linksCursor)));
                        linksCursor.close();
                    }else{
                        links.setText(Html.fromHtml(sourceLink));
                    }
                }

                final TextView episodes = (TextView) mView.findViewById(R.id.episodes);
                final TextView episodes_header = (TextView) mView.findViewById(R.id.episodes_header);
                if (reviews != null) {
                    Cursor episodesCursor = mContext.getContentResolver().query(Contract.MangaEpisodeEntry.buildEpisodesForManga((long) anime._id), null, null, null, null);
                    if (episodesCursor != null && episodesCursor.getCount()>0) {
                        if (episodes_header!=null) episodes_header.setVisibility(View.VISIBLE);
                        episodes.setVisibility(View.VISIBLE);
                        episodes.setClickable(true);
                        episodes.setMovementMethod(LinkMovementMethod.getInstance());
                        episodes.setText(Html.fromHtml(Anime.episodesFromCursorAsHtml(episodesCursor)));
                        episodesCursor.close();
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
