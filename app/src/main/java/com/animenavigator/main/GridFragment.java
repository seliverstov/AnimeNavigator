package com.animenavigator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animenavigator.R;
import com.animenavigator.common.AnimeListFragment;
import com.animenavigator.db.Contract;

/**
 * Created by a.g.seliverstov on 21.03.2016.
 */
public class GridFragment extends AnimeListFragment {

    public static AnimeListFragment newInstance(int loaderId){
        AnimeListFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putInt(AnimeListFragment.ARG_LOADER_ID, loaderId);
        args.putString(AnimeListFragment.ARG_SORT_ORDER, Contract.MangaEntry.BAYESIAN_SCORE_COLUMN + " desc");
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment,container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_view_column_count)));
        mAdapter = new GridAdapter(getContext(),null);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

}
