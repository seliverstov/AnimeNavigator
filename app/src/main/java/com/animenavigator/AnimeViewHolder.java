package com.animenavigator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by a.g.seliverstov on 22.03.2016.
 */
public class AnimeViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView mTitle;
    public ImageView mPoster;
    public TextView mRating;
    public TextView mTitles;
    public TextView mGenres;
    public TextView mThemes;
    public TextView mCreators;

    public AnimeViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        mTitle = (TextView)itemView.findViewById(R.id.item_title);
        mPoster = (ImageView)itemView.findViewById(R.id.item_poster);
        mRating = (TextView)itemView.findViewById(R.id.item_rating);
        mTitles = (TextView)itemView.findViewById(R.id.item_titles);
        mGenres = (TextView)itemView.findViewById(R.id.item_genres);
        mThemes = (TextView)itemView.findViewById(R.id.item_themes);
        mCreators = (TextView)itemView.findViewById(R.id.item_creators);
    }
}
