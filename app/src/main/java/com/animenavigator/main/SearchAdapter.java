package com.animenavigator.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.common.AnimeViewHolder;
import com.animenavigator.common.ImageLoader;
import com.animenavigator.common.ItemSelectedCallback;
import com.animenavigator.R;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;

import java.text.DecimalFormat;

import skyfish.CursorRecyclerViewAdapter;

/**
 * Created by a.g.seliverstov on 22.03.2016.
 */
public class SearchAdapter extends CursorRecyclerViewAdapter<AnimeViewHolder> {
    private Context mContext;

    public SearchAdapter(Context context, Cursor cursor){
        super(context, cursor);
        this.mContext = context;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_item_layout, parent, false);
        AnimeViewHolder viewHolder = new AnimeViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final AnimeViewHolder holder, Cursor cursor) {
        final Anime anime = Anime.fromCursor(cursor);

        holder.mTitle.setText(anime.title);

        holder.mRating.setText(new DecimalFormat("#.#").format(anime.rating));

        holder.mTitles.setText(mContext.getString(R.string.titles_tmp, Anime.printList(anime.alternativeTitles)));

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

        holder.mCreators.setText(mContext.getString(R.string.creators_tmp, Anime.printList(anime.creators)));

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
