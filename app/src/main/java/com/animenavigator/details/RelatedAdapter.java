package com.animenavigator.details;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.R;
import com.animenavigator.common.AnimeViewHolder;
import com.animenavigator.common.AnimeItemAdapter;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;

import java.text.DecimalFormat;

/**
 * Created by a.g.seliverstov on 05.04.2016.
 */
public class RelatedAdapter extends AnimeItemAdapter {
    private Context mContext;

    public RelatedAdapter(Context context, Cursor cursor){
        super(context, cursor);
        this.mContext = context;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.related_item_layout, parent, false);
        return new AnimeViewHolder(view);
    }

}
