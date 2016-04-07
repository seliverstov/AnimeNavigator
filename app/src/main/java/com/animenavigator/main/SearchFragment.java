package com.animenavigator.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.animenavigator.common.AnimeListFragment;
import com.animenavigator.common.DividerItemDecoration;
import com.animenavigator.R;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;

import static android.R.layout.simple_dropdown_item_1line;

/**
 * Created by a.g.seliverstov on 22.03.2016.
 */
public class SearchFragment extends AnimeListFragment {
    private AppCompatMultiAutoCompleteTextView mSearchView;

    public static AnimeListFragment newInstance(int loaderId){
        AnimeListFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(AnimeListFragment.ARG_LOADER_ID,loaderId);
        args.putString(AnimeListFragment.ARG_SORT_ORDER, Contract.MangaEntry.BAYESIAN_SCORE_COLUMN+" desc");
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SearchAdapter(getContext(), null);
        recyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        mSearchView = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.searchview);
        mSearchView.setTokenizer(new AppCompatMultiAutoCompleteTextView.CommaTokenizer());
        mSearchView.setThreshold(1);
        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.searchview && !hasFocus) {
                    InputMethodManager imm = (InputMethodManager) SearchFragment.this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getActivity(), simple_dropdown_item_1line);
        Cursor genresCursor  = getContext().getContentResolver().query(Contract.GenreEntry.CONTENT_URI, null, null, null, null);
        if (genresCursor!=null){
            for(String s:Anime.genresFromCursor(genresCursor)){
                searchAdapter.add(getContext().getString(R.string.genres_tmp,s));
            }
            genresCursor.close();
        }

        Cursor themesCursor  = getContext().getContentResolver().query(Contract.ThemeEntry.CONTENT_URI, null, null, null, null);
        if (themesCursor!=null){
            for(String s:Anime.themesFromCursor(themesCursor)){
                searchAdapter.add(getContext().getString(R.string.themes_tmp,s));
            }
            themesCursor.close();
        }

        mSearchView.setAdapter(searchAdapter);

        ImageButton btnClear = (ImageButton)view.findViewById(R.id.clear_btn);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setText("");
            }
        });

        ImageButton btnSearch = (ImageButton)view.findViewById(R.id.search_btn);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.clearFocus();
                Toast.makeText(SearchFragment.this.getContext(), mSearchView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }
}
