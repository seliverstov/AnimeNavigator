package com.animenavigator.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.animenavigator.common.Const;
import com.animenavigator.common.DividerItemDecoration;
import com.animenavigator.R;
import com.animenavigator.db.Contract;
import com.animenavigator.model.Anime;

import java.util.ArrayList;
import java.util.List;

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

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSearchView.setText(sp.getString(Const.SP_SEARCH_KEY,null));

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
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sp.edit().putString(Const.SP_SEARCH_KEY,null).apply();
                mSearchView.setText(null);
            }
        });

        ImageButton btnSearch = (ImageButton)view.findViewById(R.id.search_btn);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sp.edit().putString(Const.SP_SEARCH_KEY,String.valueOf(mSearchView.getText())).apply();
                mSearchView.clearFocus();
                Toast.makeText(SearchFragment.this.getContext(), mSearchView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Const.SP_SEARCH_KEY)){
            restartLoader();
        }else {
            super.onSharedPreferenceChanged(sharedPreferences, key);
        }
    }

    @Override
    protected void refeshSelection(Context context) {
        super.refeshSelection(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sp.getString(Const.SP_SEARCH_KEY, null);
        if (value!=null && !"".equals(value.trim())){
            String[] parts = value.split(",");
            List<String> genres = new ArrayList<>();
            List<String> themes = new ArrayList<>();
            List<String> other = new ArrayList<>();
            String genrePrefix = getActivity().getString(R.string.genre_search_prefix);
            String themePrefix = getActivity().getString(R.string.theme_search_prefix);
            for(String s:parts){
                if (s.startsWith(genrePrefix)) genres.add(s.replace(genrePrefix,"").trim());
                else if (s.startsWith(themePrefix)) themes.add(s.replace(themePrefix,"").trim());
                else other.add(s);
            }
            String otherSelection = "";
            List<String> otherSelectionArgs = new ArrayList<>();
            for(String s: other){
                otherSelection += ((otherSelection.equals(""))?"":" AND ") + Contract.MangaEntry.NAME_COLUMN + " LIKE ?";
                otherSelectionArgs.add("%"+s+"%");
            }
            if (otherSelectionArgs.size()>0) {
                String newSelection = (mSelection == null ) ? otherSelection : mSelection + " AND " + otherSelection;

                int shift = (mSelectionArgs == null) ? 0 : mSelectionArgs.length;
                int n = otherSelectionArgs.size() + shift;
                String[] newSelectionArgs = new String[n];
                for (int i = 0; i < n; i++) {
                    if (i<shift)
                        newSelectionArgs[i] = mSelectionArgs[i];
                    else
                        newSelectionArgs[i] = otherSelectionArgs.get(i-shift);
                }

                mSelection =  newSelection;
                mSelectionArgs = newSelectionArgs;
            }
        }
    }

}
