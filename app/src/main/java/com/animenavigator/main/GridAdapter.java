package com.animenavigator.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.common.AnimeViewHolder;
import com.animenavigator.common.AnimeItemAdapter;
import com.animenavigator.R;


/**
 * Created by a.g.seliverstov on 21.03.2016.
 */
public class GridAdapter extends AnimeItemAdapter {
    private Context mContext;

    public GridAdapter(Context context, Cursor cursor){
        super(context, cursor);
        this.mContext = context;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.grid_item_layout, parent, false);
        return new AnimeViewHolder(view);
    }
}
