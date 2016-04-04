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
import com.animenavigator.model.Anime;

import java.text.DecimalFormat;

import skyfish.CursorRecyclerViewAdapter;


/**
 * Created by a.g.seliverstov on 21.03.2016.
 */
public class GridAdapter extends CursorRecyclerViewAdapter<AnimeViewHolder> {
    private Context mContext;

    public GridAdapter(Context context, Cursor cursor){
        super(context, cursor);
        this.mContext = context;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grid_item_layout, parent, false);
        AnimeViewHolder viewHolder = new AnimeViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final AnimeViewHolder holder, Cursor cursor) {
        final Anime anime = Anime.fromCursor(cursor);
        holder.mTitle.setText(anime.title);
        holder.mRating.setText(new DecimalFormat("#.#").format(anime.rating));

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
