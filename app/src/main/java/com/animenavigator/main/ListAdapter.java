package com.animenavigator.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.common.AnimeViewHolder;
import com.animenavigator.common.ImageLoader;
import com.animenavigator.common.ItemSelectedCallback;
import com.animenavigator.R;
import com.animenavigator.model.Anime;

import java.util.List;

/**
 * Created by a.g.seliverstov on 22.03.2016.
 */
public class ListAdapter extends RecyclerView.Adapter<AnimeViewHolder>{
    private List<Anime> mAnimeList;
    private Context mContext;

    public ListAdapter(List<Anime> animeList, Context context){
        this.mContext = context;
        this.mAnimeList = animeList;
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        AnimeViewHolder viewHolder = new AnimeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AnimeViewHolder holder, int position) {
        final Anime anime = mAnimeList.get(position);

        holder.mTitle.setText(anime.title);

        holder.mRating.setText(anime.rating);

        //holder.mTitles.setText(mContext.getString(R.string.titles_tmp, Anime.printList(anime.alternativeTitles)));

        holder.mGenres.setText(Anime.printList(anime.genres));

        holder.mThemes.setText(Anime.printList(anime.themes));

        holder.mCreators.setText(mContext.getString(R.string.creators_tmp, Anime.printList(anime.creators)));

        holder.mPlot.setText(anime.plot);

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

    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }
}
