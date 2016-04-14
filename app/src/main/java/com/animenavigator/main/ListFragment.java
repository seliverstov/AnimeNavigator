package com.animenavigator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.animenavigator.R;
import com.animenavigator.common.AnimeListFragment;
import com.animenavigator.db.Contract;

/**
 * Created by a.g.seliverstov on 22.03.2016.
 */
public class ListFragment extends AnimeListFragment {

    public static AnimeListFragment newInstance(int loaderId){
        AnimeListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(AnimeListFragment.ARG_LOADER_ID,loaderId);
        args.putString(AnimeListFragment.ARG_SORT_ORDER, Contract.MangaEntry._ID+" desc");
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment,container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ListAdapter(getContext(), null);
        recyclerView.setAdapter(mAdapter);
        mEmptyView = (TextView)view.findViewById(R.id.empty_list);
        return view;
    }

}
