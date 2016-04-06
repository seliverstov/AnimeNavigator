package com.animenavigator.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.common.AnimeViewHolder;
import com.animenavigator.common.AnimeItemAdapter;
import com.animenavigator.R;
import com.animenavigator.model.Anime;

import java.text.DecimalFormat;

/**
 * Created by a.g.seliverstov on 22.03.2016.
 */
public class ListAdapter extends AnimeItemAdapter {
    private Context mContext;

    public ListAdapter(Context context, Cursor cursor){
        super(context, cursor);
        this.mContext = context;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder holder, Cursor cursor) {
        super.onBindViewHolder(holder, cursor);
        final Anime anime = Anime.fromCursor(cursor);
        if (holder.mRating!=null)
            holder.mRating.setText(mContext.getString(R.string.rating_tmp, new DecimalFormat("#.#").format(anime.rating)));

    }
}
