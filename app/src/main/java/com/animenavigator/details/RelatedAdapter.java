package com.animenavigator.details;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.R;
import com.animenavigator.common.AnimeViewHolder;
import com.animenavigator.common.ImageLoader;
import com.animenavigator.common.ItemSelectedCallback;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;

import java.text.DecimalFormat;

import skyfish.CursorRecyclerViewAdapter;

/**
 * Created by a.g.seliverstov on 05.04.2016.
 */
public class RelatedAdapter extends CursorRecyclerViewAdapter<AnimeViewHolder> {
    private Context mContext;

    public RelatedAdapter(Context context, Cursor cursor){
        super(context, cursor);
        this.mContext = context;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.related_item_layout, parent, false);
        AnimeViewHolder viewHolder = new AnimeViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final AnimeViewHolder holder, Cursor cursor) {
        final Anime anime = Anime.fromCursor(cursor);

        String related = cursor.getString(cursor.getColumnIndex(Contract.RelatedEntry.NAME_COLUMN));

        if (related!=null && !"".equals(related)){
            related = Character.toUpperCase(related.charAt(0)) + related.substring(1);
            holder.mRelated.setText(related);
        }

        holder.mTitle.setText(anime.title);

        holder.mRating.setText(new DecimalFormat("#.#").format(anime.rating));

        Cursor titlesCursor  = mContext.getContentResolver().query(Contract.MangaTitleEntry.buildTitlesForManga((long) anime._id), null, null, null, null);
        if (titlesCursor!=null){
            holder.mTitles.setText(mContext.getString(R.string.titles_tmp,Anime.titlesFromCursorAsString(titlesCursor)));
            titlesCursor.close();
        }

        Cursor genresCursor  = mContext.getContentResolver().query(Contract.GenreEntry.buildGenreForManga((long) anime._id), null, null, null, null);
        if (genresCursor!=null){
            holder.mGenres.setText(mContext.getString(R.string.genres_tmp,Anime.genresFromCursorAsString(genresCursor)));
            genresCursor.close();
        }

        Cursor themesCursor  = mContext.getContentResolver().query(Contract.ThemeEntry.buildThemesForManga((long) anime._id), null, null, null, null);
        if (themesCursor!=null){
            holder.mThemes.setText(mContext.getString(R.string.themes_tmp, Anime.themesFromCursorAsString(themesCursor)));
            themesCursor.close();
        }

        Cursor creatorsCursor  = mContext.getContentResolver().query(Contract.PersonEntry.buildPersonsForManga((long) anime._id), null, null, null, null);
        if (creatorsCursor!=null){
            holder.mCreators.setText(mContext.getString(R.string.creators_tmp,Anime.creatorsFromCursorAsString(creatorsCursor)));
            creatorsCursor.close();
        }

        ImageLoader.loadImageToView(anime.posterUrl, mContext, holder.mPoster);

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
