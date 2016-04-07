package com.animenavigator.details;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.animenavigator.R;
import com.animenavigator.common.DividerItemDecoration;
import com.animenavigator.db.Contract;

/**
 * Created by a.g.seliverstov on 05.04.2016.
 */
public class RelatedFragment  extends Fragment{
    public static final String MANGA_URI_KEY = "MANGA_URI_KEY";
    private int RELATED_CURSOR_LOADER_ID = 6;
    private RelatedAdapter mAdapter;
    private TextView mEmptyView;

    public static RelatedFragment newInstance(Uri uri){
        RelatedFragment fragment = new RelatedFragment();
        Bundle args = new Bundle();
        args.putParcelable(MANGA_URI_KEY, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.related_fragment,container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RelatedAdapter(getContext(), null);
        recyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        mEmptyView = (TextView)view.findViewById(R.id.no_related);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(RELATED_CURSOR_LOADER_ID, null, new CursorLoaderCallback(getActivity()));
    }

    class CursorLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;

        public CursorLoaderCallback(Context context){
            mContext = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri uri = getArguments().getParcelable(MANGA_URI_KEY);
            if (uri!=null) {
                return new CursorLoader(
                        mContext,
                        Contract.MangaEntry.buildRelatedForManga(ContentUris.parseId(uri)),
                        null,
                        null,
                        null,
                        Contract.RelatedEntry.NAME_COLUMN);
            }else{
                return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
            if (mEmptyView!=null){
                mEmptyView.setVisibility((data==null || data.getCount()==0)?View.VISIBLE:View.GONE);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    }
}
