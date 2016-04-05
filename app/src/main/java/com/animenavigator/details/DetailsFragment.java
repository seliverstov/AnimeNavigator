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
    private int MANGA_CURSOR_LOADER_ID = 4;
    private View mView;
    private Uri mMangaUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args==null || args.getParcelable(MANGA_URI_KEY)==null){

            return inflater.inflate(R.layout.no_details,container,false);
        }
        mMangaUri = getArguments().getParcelable(MANGA_URI_KEY);
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
        getLoaderManager().initLoader(MANGA_CURSOR_LOADER_ID, null, new CursorLoaderCallback(getActivity()));
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

                final TextView title = (TextView) mView.findViewById(R.id.title);
                final TextView rating = (TextView) mView.findViewById(R.id.rating);
                final TextView type = (TextView) mView.findViewById(R.id.type);
                final TextView vintage = (TextView) mView.findViewById(R.id.vintage);

                if (title != null)
                    title.setText(anime.title);
                if (rating != null)
                    rating.setText(getContext().getString(R.string.rating_tmp, new DecimalFormat("#.#").format(anime.rating)));
                if (type != null) {
                    type.setText(Character.toUpperCase(anime.type.charAt(0)) + anime.type.substring(1));
                }

                /*final TextView genres = (TextView)mView.findViewById(R.id.genres);
                if (genres!=null) {
                    Cursor genresCursor = mContext.getContentResolver().query(Contract.GenreEntry.buildGenreForManga((long) anime._id), null, null, null, null);
                    if (genresCursor != null) {
                        genres.setText(mContext.getString(R.string.genres_tmp, Anime.genresFromCursorAsString(genresCursor)));
                        genresCursor.close();
                    }
                }

                final TextView themes = (TextView)mView.findViewById(R.id.themes);
                if (themes!=null) {
                    Cursor themesCursor = mContext.getContentResolver().query(Contract.ThemeEntry.buildThemesForManga((long) anime._id), null, null, null, null);
                    if (themesCursor != null) {
                        themes.setText(mContext.getString(R.string.themes_tmp, Anime.themesFromCursorAsString(themesCursor)));
                        themesCursor.close();
                    }
                }*/
                final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mView.findViewById(R.id.collapsing_toolbar);

                if (collapsingToolbarLayout != null) {
                    collapsingToolbarLayout.setTitle(anime.title);
                    collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                }

                final LinearLayout header = (LinearLayout) mView.findViewById(R.id.header);


                Uri uri = getArguments().getParcelable(MANGA_URI_KEY);
                DetailsPagerAdapter adapter = new DetailsPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), uri);

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
                                    if (title!=null){
                                        title.setTextColor(swatch.getBodyTextColor());
                                    }
                                    if (rating!=null){
                                        rating.setTextColor(swatch.getTitleTextColor());
                                    }
                                    if (type!=null){
                                        type.setTextColor(swatch.getTitleTextColor());
                                    }
                                    if (vintage!=null){
                                        vintage.setTextColor(swatch.getTitleTextColor());
                                    }
                                    /*if (genres!=null){
                                        genres.setTextColor(swatch.getTitleTextColor());
                                    }
                                    if (themes!=null){
                                        themes.setTextColor(swatch.getTitleTextColor());
                                    }*/
                                    if (tabLayout != null) {
                                        tabLayout.setBackgroundColor(swatch.getRgb());
                                        tabLayout.setTabTextColors(swatch.getTitleTextColor(),swatch.getBodyTextColor());
                                        tabLayout.setSelectedTabIndicatorColor(swatch.getBodyTextColor());
                                    }
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
}
