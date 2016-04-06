package com.animenavigator.common;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.R;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;

import java.text.DecimalFormat;

import skyfish.CursorRecyclerViewAdapter;

/**
 * Created by a.g.seliverstov on 06.04.2016.
 */
public class AnimeItemAdapter extends CursorRecyclerViewAdapter<AnimeViewHolder> {
    private Context mContext;

    public AnimeItemAdapter(Context context, Cursor cursor){
        super(context, cursor);
        this.mContext = context;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnimeViewHolder(null);
    }

    @Override
    public void onBindViewHolder(final AnimeViewHolder holder, Cursor cursor) {
        final Anime anime = Anime.fromCursor(cursor);

        if (holder.mTitle!=null)
            holder.mTitle.setText((anime.title==null)?mContext.getString(R.string.unknown):anime.title);

        if (holder.mRating!=null)
            holder.mRating.setText(new DecimalFormat("#.#").format(anime.rating));

        if (holder.mTitles!=null ){
            Cursor titlesCursor  = mContext.getContentResolver().query(Contract.MangaTitleEntry.buildTitlesForManga((long) anime._id), null, null, null, null);
            if (titlesCursor!=null) {
                holder.mTitles.setText(mContext.getString(R.string.titles_tmp, Anime.titlesFromCursorAsString(titlesCursor)));
                titlesCursor.close();
            }
        }

        if (holder.mRelated!=null){
            String related = cursor.getString(cursor.getColumnIndex(Contract.RelatedEntry.NAME_COLUMN));
            if (related!=null && !"".equals(related)) {
                related = Character.toUpperCase(related.charAt(0)) + related.substring(1);
                holder.mRelated.setText(related);
            }
        }

        if (holder.mGenres!=null){
            Cursor genresCursor  = mContext.getContentResolver().query(Contract.GenreEntry.buildGenreForManga((long) anime._id), null, null, null, null);
            if (genresCursor!=null) {
                String s = Anime.genresFromCursorAsString(genresCursor);
                s = (s == null || "".equals(s)) ? mContext.getString(R.string.unknown) : s;
                holder.mGenres.setText(mContext.getString(R.string.genres_tmp, s));
                genresCursor.close();
            }
        }

        if (holder.mThemes!=null){
            Cursor themesCursor = mContext.getContentResolver().query(Contract.ThemeEntry.buildThemesForManga((long) anime._id), null, null, null, null);
            if (themesCursor!=null) {
                String s = Anime.themesFromCursorAsString(themesCursor);
                s = (s == null || "".equals(s)) ? mContext.getString(R.string.unknown) : s;
                holder.mThemes.setText(mContext.getString(R.string.themes_tmp, s));
                themesCursor.close();
            }
        }

        if (holder.mCreators!=null){
            Cursor creatorsCursor  = mContext.getContentResolver().query(Contract.PersonEntry.buildPersonsForManga((long) anime._id), null, null, null, null);
            if (creatorsCursor!=null) {
                String s = Anime.creatorsFromCursorAsString(creatorsCursor);
                s = (s == null || "".equals(s)) ? mContext.getString(R.string.unknown) : s;
                holder.mCreators.setText(s);
                creatorsCursor.close();
            }
        }

        if (holder.mPlot!=null)
            holder.mPlot.setText((anime.plot==null)?mContext.getString(R.string.unknown):anime.plot);

        if (holder.mPoster!=null && anime.posterUrl!=null)
            ImageLoader.loadImageToView(anime.posterUrl, mContext, holder.mPoster);

        if (holder.view!=null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof ItemSelectedCallback) {
                        ((ItemSelectedCallback) mContext).onItemSelected(anime._id);
                    }
                }
            });
        }
    }
}
