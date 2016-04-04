package com.animenavigator.details;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animenavigator.common.ImageLoader;
import com.animenavigator.R;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;

/**
 * Created by a.g.seliverstov on 29.03.2016.
 */
public class DetailsFragment extends Fragment {
    public static final String MANGA_URI_KEY = "MANGA_URI_KEY";
    private int MANGA_LOADER_ID = 40;
    private int GENRES_LOADER_ID = 41;
    private int THEMES_LOADER_ID = 42;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args==null){
            return inflater.inflate(R.layout.no_details,container,false);
        }

        mView = inflater.inflate(R.layout.details_fragment, container, false);

        if (getActivity() instanceof DetailsActivity){
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            Toolbar toolbar = (Toolbar) mView.findViewById(R.id.details_toolbar);
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MANGA_LOADER_ID, null, new MangaLoaderCallback(getActivity()));
        getLoaderManager().initLoader(GENRES_LOADER_ID, null, new GenresLoaderCallback(getActivity()));
        getLoaderManager().initLoader(THEMES_LOADER_ID, null, new ThemesLoaderCallback(getActivity()));
    }

    class MangaLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;

        public MangaLoaderCallback(Context context){
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

                TextView title = (TextView) mView.findViewById(R.id.title);
                TextView rating = (TextView) mView.findViewById(R.id.rating);

                if (title != null)
                    title.setText(anime.title);
                if (rating != null)
                    rating.setText(new DecimalFormat("#.#").format(anime.rating));


                final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mView.findViewById(R.id.collapsing_toolbar);

                if (collapsingToolbarLayout != null) {
                    collapsingToolbarLayout.setTitle(anime.title);
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                }

                final LinearLayout header = (LinearLayout) mView.findViewById(R.id.header);

                DetailsPagerAdapter adapter = new DetailsPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());

                ViewPager viewPager = (ViewPager) mView.findViewById(R.id.details_viewpager);
                if (viewPager != null) {
                    viewPager.setAdapter(adapter);
                }

                final TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.details_tablayout);
                if (tabLayout != null)
                    tabLayout.setupWithViewPager(viewPager);

                final ImageView poster = (ImageView) mView.findViewById(R.id.poster);

                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (poster != null)
                                    poster.setImageBitmap(bitmap);
                                Palette.Swatch swatch = palette.getVibrantSwatch();
                                if (swatch == null) {
                                    for (Palette.Swatch s : palette.getSwatches()) {
                                        if (s != null) {
                                            swatch = s;
                                            break;
                                        }
                                    }
                                }
                                if (swatch != null) {
                                    if (header != null) {
                                        header.setBackgroundColor(swatch.getRgb());
                                    }
                                    if (tabLayout != null)
                                        tabLayout.setBackgroundColor(swatch.getRgb());
                                }
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                ImageLoader.loadImageToView(anime.posterUrl, getActivity(), target);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    class GenresLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;

        public GenresLoaderCallback(Context context){
            mContext = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri uri = getArguments().getParcelable(MANGA_URI_KEY);
            if (uri!=null) {
                return new CursorLoader(
                        mContext,
                        Contract.GenreEntry.buildGenreForManga(ContentUris.parseId(uri)),
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
            TextView textView = (TextView) mView.findViewById(R.id.genres);
            String text = "";
            for(String s:Anime.genresFromCursor(data)){
                text+=s+", ";
            }
            text = text.endsWith(", ")?text.substring(0, text.length()-2):text;
            textView.setText(mContext.getString(R.string.genres_tmp, text));
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    class ThemesLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;

        public ThemesLoaderCallback(Context context){
            mContext = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri uri = getArguments().getParcelable(MANGA_URI_KEY);
            if (uri!=null) {
                return new CursorLoader(
                        mContext,
                        Contract.ThemeEntry.buildThemesForManga(ContentUris.parseId(uri)),
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
            TextView textView = (TextView) mView.findViewById(R.id.themes);
            String text = "";
            for(String s:Anime.themesFromCursor(data)){
                text+=s+", ";
            }
            text = text.endsWith(", ")?text.substring(0, text.length()-2):text;
            textView.setText(mContext.getString(R.string.themes_tmp, text));
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
