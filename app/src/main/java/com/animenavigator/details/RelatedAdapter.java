package com.animenavigator.details;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.R;
import com.animenavigator.common.AnimeViewHolder;
import com.animenavigator.db.Contract;
import com.animenavigator.main.SearchAdapter;

/**
 * Created by a.g.seliverstov on 05.04.2016.
 */
public class RelatedAdapter extends SearchAdapter {
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

    @Override
    public void onBindViewHolder(final AnimeViewHolder holder, Cursor cursor) {
        String related = cursor.getString(cursor.getColumnIndex(Contract.RelatedEntry.NAME_COLUMN));

        if (holder.mRelated!=null && related!=null && !"".equals(related)){
            related = Character.toUpperCase(related.charAt(0)) + related.substring(1);
            holder.mRelated.setText(related);
        }

        super.onBindViewHolder(holder, cursor);
    }
}
