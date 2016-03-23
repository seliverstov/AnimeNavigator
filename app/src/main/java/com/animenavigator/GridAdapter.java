package com.animenavigator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.animenavigator.model.Anime;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by a.g.seliverstov on 21.03.2016.
 */
public class GridAdapter extends RecyclerView.Adapter<AnimeViewHolder> {
    private List<Anime> mAnimeList;
    private Context mContext;

    public GridAdapter(List<Anime> animeList, Context context){
        this.mContext = context;
        this.mAnimeList = animeList;
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
    public void onBindViewHolder(AnimeViewHolder holder, int position) {
        final Anime anime = mAnimeList.get(position);
        holder.mTitle.setText(anime.title);
        holder.mRating.setText(anime.rating);
        Picasso.with(mContext).load(anime.posterUrl).into(holder.mPoster);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GridAdapter.this.mContext,DetailsActivity.class);
                intent.setData(Uri.parse("http://animenavigator.com/"+anime._id));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }

}
