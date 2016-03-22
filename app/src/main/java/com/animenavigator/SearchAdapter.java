package com.animenavigator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.animenavigator.model.Anime;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by a.g.seliverstov on 22.03.2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Anime> mAnimeList;
    private Context mContext;

    public SearchAdapter(List<Anime> animeList, Context context){
        this.mContext = context;
        this.mAnimeList = animeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Anime anime = mAnimeList.get(position);

        holder.mTitle.setText(anime.title);

        holder.mRating.setText(anime.rating);

        holder.mTitles.setText(mContext.getString(R.string.alternative_titles, Anime.printList(anime.alternativeTitles)));

        holder.mGenres.setText(mContext.getString(R.string.genres, Anime.printList(anime.genres)));

        holder.mThemes.setText(mContext.getString(R.string.themes, Anime.printList(anime.themes)));

        holder.mCreators.setText(mContext.getString(R.string.creators, Anime.printList(anime.creators)));

        Picasso.with(mContext).load(anime.posterUrl).into(holder.mPoster);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, anime._id+": "+anime.title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView mTitle;
        public ImageView mPoster;
        public TextView mRating;
        public TextView mTitles;
        public TextView mGenres;
        public TextView mThemes;
        public TextView mCreators;

        public ViewHolder(View itemView) {
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
}
