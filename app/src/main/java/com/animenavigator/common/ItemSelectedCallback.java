package com.animenavigator.common;

import android.view.View;

/**
 * Created by a.g.seliverstov on 29.03.2016.
 */
public interface ItemSelectedCallback {
    void onItemSelected(int id);
    void onItemSelected(int id, View view);
}
