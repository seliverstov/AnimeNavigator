package com.animenavigator.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by a.g.seliverstov on 04.04.2016.
 */
public class Contract {
    public static final String CONTENT_AUTHORITY = "com.animenavigator.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MANGA = "manga";

    public static final class MangaEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MANGA).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MANGA;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MANGA;

        public static final String TABLE_NAME = "manga";

        public static final String TYPE_COLUMN = "type";
        public static final String NAME_COLUMN = "name";
        public static final String PLOT_COLUMN = "plot";
        public static final String VOTES_COLUMN = "votes";
        public static final String WEIGHTED_SCORE_COLUMN = "weighted_score";
        public static final String BAYESIAN_SCORE_COLUMN = "bayesian_score";
        public static final String PAGES_COLUMN = "pages";
        public static final String EPISODES_COLUMN = "episodes";
        public static final String OBJECTIONABLE_CONTENT_COLUMN = "objectionable_content";
        public static final String PICTURE_COLUMN = "picture";
        public static final String COPYRIGHT_COLUMN = "copyright";
    }

}
