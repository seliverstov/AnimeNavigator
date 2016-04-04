package com.animenavigator.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.animenavigator.R;

/**
 * Created by alexander on 19.03.2016.
 */
public class PageFragment extends Fragment {
    private static final String TAB_POSITION = "TAB_POSITION";

    public static PageFragment newInstance(int tabPosition){
        PageFragment pageFragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        int tabPosition = args.getInt(TAB_POSITION);
        View view = inflater.inflate(R.layout.page_fragment, container, false);
        TextView page = (TextView)view.findViewById(R.id.page);
        page.setText("Page "+tabPosition);
        return view;
    }
}
